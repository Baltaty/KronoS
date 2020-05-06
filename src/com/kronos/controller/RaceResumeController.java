package com.kronos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.kronos.App;
import com.kronos.api.Observer;
import com.kronos.api.TimeRace;
import com.kronos.global.animation.PulseTransition;
import com.kronos.global.enums.RaceState;
import com.kronos.global.util.Alerts;
import com.kronos.model.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RaceResumeController implements Initializable, Observer {


    @FXML
    private Label chronoRivalCar;
    @FXML
    private Label chronoTopTime;
    @FXML
    private Label labelMeanTime;
    @FXML
    private Label tempsTourEcoulé;
    @FXML
    private Label tempsTourRestant;
    @FXML
    private ProgressBar meanTimeBar;
    @FXML
    private Button TopBtn;
    @FXML
    private Label departureHour;
    @FXML
    private Label tmierSign;
    @FXML
    private Label currentHour;

    @FXML
    private Label spentTime;

    @FXML
    private Label remainingTime;
    @FXML
    private JFXButton startRace;

    @FXML
    private JFXButton pauseRace;

    @FXML
    private JFXButton stopRace;
    @FXML
    private Label lastNamePilotMainCar;
    @FXML
    private Label firstNamePilotMainCar;
    @FXML
    private Label dateOfBirthPilot;
    @FXML
    private Label mainCarBrand;
    @FXML
    private Label mainCarModel;
    @FXML
    private Label mainCarTeam;

    @FXML
    private TableView<TopModel> table_info;
    @FXML
    private TableColumn<TopModel, Double> col_delete;
    @FXML
    private TableColumn<TopModel, Integer> colCarNumber;

    @FXML
    private TableColumn<TopModel, String> col_typetop;

    @FXML
    private TableColumn<TopModel, String> col_comment;

    @FXML
    private TableColumn<TopModel, String> col_time;

    @FXML
    private TableColumn<TopModel, String> col_racetime;

    @FXML
    private TableColumn<TopModel, String> col_laptime;

    @FXML
    private TableColumn<TopModel, Integer> colLapNumber;

    @FXML
    private JFXToggleButton toogleedit;

    @FXML
    private JFXTextField topComment;
    @FXML
    private ComboBox<String> car;
    @FXML
    private ComboBox<String> topType;
    @FXML
    private ListView<Integer> listNowRank=new ListView<>();
    @FXML
    private ListView<Integer> listPastRank= new ListView<>();


    private static ArrayList<Double> listOfMeanTime = new ArrayList<>();
    private static Double meantime = 0.00;
    PulseTransition pulseTransition;
    MainCarModel mycar;
    private LapRaceModel lapRaceModel;
    private RaceModel raceModel;
    private CarController carController = new CarController();
    private ArrayList<TopModel> topModels = new ArrayList<>();
    private ArrayList<CarModel> carModels = new ArrayList<>();
    private HashMap<Integer,ArrayList<TopModel>> topsMaps=new HashMap<>();
    private HashMap<Integer, ArrayList<String>> rivalCarTopsTimes = new HashMap<>();
    private HashMap<Integer, ArrayList<Double>> rivalCarListOfMeantime = new HashMap<>();
    private boolean isExtancier = true, breakThread = true;
    private Thread thread, threadChrono, threadChronoRivalCar;
    private int munites = 0, secondes = 0, millisecondes = 0, decimalpartTosecond = 0, intergerpart = 0, numberOfLapsDone = 0, remainingLaps;
    private int rivalTimerMunites = 0, rivalTimerHours = 0, rivalTimerSecondes = 0, rivalTimerMillisecondes = 0;
    boolean isStartRivalTimer = true;
    private boolean isStartTimer, isSetTimerBar = true, istartRace = false, timerIsInitialize = true, firstTop = true, isInitialize = true;
    private double decimalpart = 0.0, lapTimeForMeanTime = 0.0, rivalCarLapTimeForMeanTime = 0.0;
    private Timeline spentTimeline;
    private Timeline remainingTimeline;
    private LocalTime localRemainningTime = LocalTime.parse("00:00:00");
    private LocalTime localSpentTime = LocalTime.parse("00:00:00");
    private LocalTime time2 = LocalTime.parse("00:00");
    private LocalTime timebar = LocalTime.parse("00:00:00");
    private LocalTime chronoTime = LocalTime.parse("00:00:00");
    private LocalTime chronoTimeRival = LocalTime.parse("00:00:00");
    private LocalTime timeTocompare = LocalTime.parse("00:00:00");
    private LocalTime oldRivalCarTime = LocalTime.parse("00:00:00");
    private LocalTime newRivalCarTime = LocalTime.parse("00:00:00");
    private LocalTime currentTime;
    private LocalTime departureTime;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    private DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("mm:ss:nn");
    private DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("HH:mm:ss:nn");


    public RaceResumeController() {
    }

    /**
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.getDataManager().attach(this);
        col_racetime.setVisible(false);
        colLapNumber.setVisible(false);
        localSpentTime = LocalTime.parse("00:00:00");
        time2 = LocalTime.parse("00:00");
        col_delete.setVisible(false);
        if (!getRace().isEmpty()) {
            raceModel = getRace().get(0);
        }


        if (raceModel instanceof TimeRace) {
            col_racetime.setVisible(true);
            if (raceModel.getRaceState().equals(RaceState.CREATION)) {
                long duration = ((TimeRace) raceModel).getDuration();
                int heureDuration = (int) (duration / 60);
                localRemainningTime = LocalTime.of(heureDuration, (int) (duration - (60 * heureDuration)), 0);
                spentTime.setText(localSpentTime.format(dtf));
                remainingTime.setText(localRemainningTime.format(dtf));
            } else {
                startRace.setText("Restart");
                localRemainningTime = LocalTime.parse(raceModel.getTimeLapsRemaining());
                localSpentTime = LocalTime.parse(raceModel.getTimeLapsSpent());
                spentTime.setText(localSpentTime.format(dtf));
                remainingTime.setText(localRemainningTime.format(dtf));

            }

            spentTimeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> incrementTime()));
            spentTimeline.setCycleCount(Animation.INDEFINITE);
            remainingTimeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> decrementTime()));
            remainingTimeline.setCycleCount(Animation.INDEFINITE);
        } else {
            colLapNumber.setVisible(true);
            tempsTourRestant.setText("Tours Restants");
            tempsTourEcoulé.setText("Tour Ecoulés");


            if (raceModel.getRaceState().equals(RaceState.CREATION)) {
                remainingLaps = ((LapRaceModel) raceModel).getNumberOfLaps();
                numberOfLapsDone = Integer.parseInt(raceModel.getTimeLapsSpent());
                raceModel.setTimeLapsRemaining(String.valueOf(remainingLaps));
                raceModel.setTimeLapsSpent(String.valueOf(numberOfLapsDone));

            } else {
                startRace.setText("Restart");
                remainingLaps = Integer.parseInt(raceModel.getTimeLapsRemaining());
                numberOfLapsDone = Integer.parseInt(raceModel.getTimeLapsSpent());
            }
            remainingTime.setText(String.valueOf(remainingLaps));
            spentTime.setText(String.valueOf(numberOfLapsDone));
        }
        topType.setItems(FXCollections.observableArrayList("I", "O", "R"));
        topType.setValue("O");
        carModels.addAll(getFollowedCars());
        car.setItems(FXCollections.observableArrayList(getFollowedCarsNumbers(getFollowedCars())));
        car.getSelectionModel().selectFirst();
        initTable();


        /* Refresh table after import */
        // initialised table of rival Car
        List<TopModel> topModelList = (List<TopModel>) (List<?>) App.getDataManager().getModels(TopModel.class);
        ArrayList<CarModel> followedCar = getFollowedCars();
        for (CarModel carmodel : followedCar) {
            ArrayList<TopModel> mytopmodelList=new ArrayList<>();
            topsMaps.put(carmodel.getNumber(),mytopmodelList);
            if (!(carmodel instanceof MainCarModel)) {
                ArrayList<String> mytable = new ArrayList<>();
                ArrayList<Double> mytableOflaptime = new ArrayList<>();
                rivalCarTopsTimes.put(carmodel.getNumber(), mytable);
                rivalCarListOfMeantime.put(carmodel.getNumber(), mytableOflaptime);
                rivalCarTopsTimes.get(carmodel.getNumber()).add(newRivalCarTime.format(dtf1));
                rivalCarListOfMeantime.get(carmodel.getNumber()).add(0.0);

                if (!topModelList.isEmpty()) {
                    for (TopModel topModel : topModelList) {
                        int muniteteToLoad = Integer.parseInt(topModel.getLapTime().substring(0, 2));
                        int secondeToLoad = Integer.parseInt(topModel.getLapTime().substring(3, 5));
                        int milliToLoad = Integer.parseInt(topModel.getLapTime().substring(6, 8));
                        lapTimeForMeanTime = (muniteteToLoad + (secondeToLoad / 60.0) + (milliToLoad / 100) / 60.0);
                         if(topModel.getCarNumber()==carmodel.getNumber())
                             topsMaps.get(carmodel.getNumber()).add(topModel);
                         if (topModel.getCarNumber() == carmodel.getNumber() && (topModel.getTopType().equals("R") || topModel.getTopType().equals("O"))) {
                            rivalCarListOfMeantime.get(carmodel.getNumber()).add(lapTimeForMeanTime);

                        }
                    }
                }
            } else {
                if (!topModelList.isEmpty()) {
                    for (TopModel topModel : topModelList) {
                        topsMaps.get(carmodel.getNumber()).add(topModel);
                        loadData(topModel);
                        int muniteteToLoad = Integer.parseInt(topModel.getLapTime().substring(0, 2));
                        int secondeToLoad = Integer.parseInt(topModel.getLapTime().substring(3, 5));
                        int milliToLoad = Integer.parseInt(topModel.getLapTime().substring(6, 8));
                        lapTimeForMeanTime = (muniteteToLoad + (secondeToLoad / 60.0) + (milliToLoad / 100) / 60.0);
                        if(topModel.getCarNumber()==carmodel.getNumber())
                            topsMaps.get(carmodel.getNumber()).add(topModel);
                        if ((carmodel.getNumber() == topModel.getCarNumber()) && (topModel.getTopType().equals("R") || topModel.getTopType().equals("O"))) {
                            listOfMeanTime.add(lapTimeForMeanTime);
                        }
                    }
                }
            }


        }

        raceModel.setTopsMap(topsMaps);
        maincarinformation();
        departureHour.setText(time2.format(dtf));
        Timeline clock = new Timeline(new KeyFrame(Duration.millis(1000), e -> getCurrentTime()));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();


    }

    /**
     * @param event
     */
    @FXML
    public void handleTopButtonClick(ActionEvent event) {
        if (startRace.isDisable() && raceModel.getRaceState().equals(RaceState.IN_PROGRESS)) {
            handleNewTop();
            displayNewRank();
        } else {
            Alerts.info("INFORMATION", "veuillez demarrer/continuer la course ");
        }
    }

    /**
     * first top for competing cars
     * the top is type out  (O)
     */

    private void handleNewTopForRivalCar() {
        LocalTime localTimeFirstTop = LocalTime.parse("00:00:00");
        String dateTimeFirstTop = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        ArrayList<CarModel> followedCars = getFollowedCars();
        for (CarModel carModel : followedCars) {
            if (!(carModel instanceof MainCarModel)) {
                if (raceModel instanceof TimeRace) {
                    TopModel topModelFirstTop = new TopModel(carModel.getNumber(), dateTimeFirstTop, "O", localSpentTime.format(dtf), localTimeFirstTop.format(dtf), "Fist Top");
                    handleTopTimeRace(topModelFirstTop, carModel, carModel.getNumber());
                    loadData(topModelFirstTop);
                    carModel.getTopList().add(topModelFirstTop);
                    saveTopModel(topModelFirstTop);

                } else {
                    TopModel topModelFirstTop = new TopModel(carModel.getNumber(), dateTimeFirstTop, "O", numberOfLapsDone, localTimeFirstTop.format(dtf), "Fist Top");
                    handleTopLapRace(topModelFirstTop, carModel, carModel.getNumber());
                    loadData(topModelFirstTop);
                    carModel.getTopList().add(topModelFirstTop);
                    saveTopModel(topModelFirstTop);

                }
            }
        }


    }

    /**
     * handle New Top for the car which has been choose
     */
    private void handleNewTop() {

        String type = topType.getSelectionModel().getSelectedItem();
        int carNumber = Integer.parseInt(car.getSelectionModel().getSelectedItem());
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        String raceTime = "";
        String lapTime = "";
        int lap = 0;
        TopModel topModel = null;
        String comment = topComment.getText();
        CarModel carModel = carController.findCar(carModels, carNumber);
        if (timerIsInitialize) {
            timerIsInitialize = false;
            lapTime = LocalTime.of(0, 0, 0, 0).format(dtf2);
            listOfMeanTime.add(getMeanTime(listOfMeanTime));
            setMeanTimeBar();
            startTimerBar();
        } else {
            if (carModel instanceof MainCarModel) {
                lapTime = chronoTime.format(dtf2);
                lapTimeForMeanTime = (munites + (secondes / 60.0) + (millisecondes / 100) / 60.0);
                resetTimerBar();
                setMeanTimeBar();

            } else {
                ArrayList<String> timeOfRivalCarTops = rivalCarTopsTimes.get(carModel.getNumber());
                int listLength = (timeOfRivalCarTops.size() - 1);
                String timeOfLastTop = timeOfRivalCarTops.get(listLength);
                int hh = Integer.parseInt(timeOfLastTop.substring(0, 2));
                int mm = Integer.parseInt(timeOfLastTop.substring(3, 5));
                int ss = Integer.parseInt(timeOfLastTop.substring(6, 8));
                int nn = Integer.parseInt(timeOfLastTop.substring(9, 11));
                newRivalCarTime = LocalTime.of(rivalTimerHours, rivalTimerMunites, rivalTimerSecondes, rivalTimerMillisecondes);
                oldRivalCarTime = LocalTime.of(hh, mm, ss, nn);
                java.time.Duration duration = java.time.Duration.between(newRivalCarTime, oldRivalCarTime).abs();
                hh = (int) (duration.getSeconds() / 3600);
                mm = (int) (duration.getSeconds() / 60) - (hh * 60);
                ss = (int) (duration.getSeconds() - ((hh * 3600) + (mm * 60)));
                nn = Math.abs(nn - rivalTimerMillisecondes);
                lapTime = LocalTime.of(hh, mm, ss, nn).format(dtf2);
                rivalCarTopsTimes.get(carModel.getNumber()).add(newRivalCarTime.format(dtf1));
                rivalCarLapTimeForMeanTime = (mm + (ss / 60.0) + (nn / 100) / 60.0);
            }
        }

        boolean b = (carModel instanceof MainCarModel) && (type.equals("R") || type.equals("O"));
        if (findPreviousTop(carNumber) == null || checkTopLogic(type, findPreviousTop(carNumber).getTopType())) {
            //Case where top respects logical top type order
            if (raceModel instanceof TimeRaceModel) {
                raceTime = localSpentTime.format(dtf);
                topModel = new TopModel(carNumber, dateTime, type, raceTime, lapTime, comment);
                handleTopTimeRace(topModel, carModel, carNumber);
            } else {
                lap = numberOfLapsDone;
                if (firstTop) {
                    lap = numberOfLapsDone;
                    firstTop = false;
                } else {
                    if (b) {
                        remainingLaps--;
                        numberOfLapsDone++;
                        lap = numberOfLapsDone;
                    } else {
                        lap = numberOfLapsDone;
                    }
                }


                topModel = new TopModel(carNumber, dateTime, type, lap, lapTime, comment);
                handleTopLapRace(topModel, carModel, carNumber);
            }
            loadData(topModel);
            carModel.getTopList().add(topModel);
            if ((carModel instanceof MainCarModel)) {
                if ((topModel.getTopType().equals("R") || topModel.getTopType().equals("O"))) {
                    checkEndOfRace();
                    handleMeanTimeBar(1);
                    listOfMeanTime.add(lapTimeForMeanTime);
                } else {
                    checkEndOfRace();
                    handleMeanTimeBar(1);
                }
            } else {
                if ((topModel.getTopType().equals("R") || topModel.getTopType().equals("O"))) {
                    checkEndOfRace();
                    rivalCarListOfMeantime.get(carModel.getNumber()).add(rivalCarLapTimeForMeanTime);
                }
            }

        } else {
            //Case where top does not respect logical top type order
            if (raceModel instanceof TimeRaceModel) {
                raceTime = localSpentTime.format(dtf);
                if (type == "I") {
                    topModel = new TopModel(carNumber, dateTime, "O", raceTime, lapTime, comment + "-Top O système");
                } else if (type == "R" || type == "O") {
                    topModel = new TopModel(carNumber, dateTime, "R", raceTime, lapTime, comment + "-Top R système");
                }
                handleTopTimeRace(topModel, carModel, carNumber);
            } else {
                lap = numberOfLapsDone;
                if (firstTop) {
                    lap = numberOfLapsDone;
                    firstTop = false;
                } else {
                    if (b) {
                        System.out.println("test je suis dans le +1 pas de respect");
                        remainingLaps--;
                        numberOfLapsDone++;
                        lap = numberOfLapsDone;
                    } else {
                        lap = numberOfLapsDone;
                    }
                }

                if (type == "I") {
                    topModel = new TopModel(carNumber, dateTime, "O", lap, lapTime, comment + "-Top O système");
                } else if (type == "R" || type == "O") {
                    topModel = new TopModel(carNumber, dateTime, "R", lap, lapTime, comment + "-Top R système");
                }
                handleTopLapRace(topModel, carModel, carNumber);
            }
            loadData(topModel);
            carModel.getTopList().add(topModel);
            if ((carModel instanceof MainCarModel)) {
                if ((topModel.getTopType().equals("R") || topModel.getTopType().equals("O"))) {
                    checkEndOfRace();
                    handleMeanTimeBar(1);
                    listOfMeanTime.add(lapTimeForMeanTime);
                } else {
                    checkEndOfRace();
                    handleMeanTimeBar(1);
                }
            } else {
                if ((topModel.getTopType().equals("R") || topModel.getTopType().equals("O"))) {

                    rivalCarListOfMeantime.get(carModel.getNumber()).add(rivalCarLapTimeForMeanTime);
                }
            }
        }
        topComment.clear();
        //Save Top list of Object to persist
        saveTopModel(topModel);
        //System.out.println("==== top =====");

    }

    /**
     * persist and save the new top to the file
     *
     * @param topModel
     */

    public void saveTopModel(TopModel topModel) {

        App.getDataManager().persist(topModel);
        App.getDataManager().saveFile();
    }

    /**
     * set mean time bar
     * if it's the top is for the Main Car
     */
    public void setMeanTimeBar() {

        decimalpart = getMeanTime(listOfMeanTime);
        intergerpart = (int) getMeanTime(listOfMeanTime);
        decimalpart = decimalpart - intergerpart;
        decimalpartTosecond = (int) (decimalpart * 60);
        timebar = LocalTime.of(0, intergerpart, decimalpartTosecond);
        labelMeanTime.setText(timebar.format(dtf));
    }

    private void handleTopTimeRace(TopModel topModel, CarModel carModel, int carNumber) {
        topModels.add(topModel);
        if (carModel.getTimeRace().getTopsMap().containsKey(carNumber)) {
            carModel.getTimeRace().getTopsMap().get(carNumber).add(topModel);
        } else {
            carModel.getTimeRace().getTopsMap().put(carNumber, new ArrayList<>());
            carModel.getTimeRace().getTopsMap().get(carNumber).add(topModel);
        }
    }

    private void handleTopLapRace(TopModel topModel, CarModel carModel, int carNumber) {
        topModels.add(topModel);
        if (carModel.getLapRace().getTopsMap().containsKey(carNumber)) {
            carModel.getLapRace().getTopsMap().get(carNumber).add(topModel);
            System.out.println("ici");
        } else {
            carModel.getLapRace().getTopsMap().put(carNumber, new ArrayList<>());
            carModel.getLapRace().getTopsMap().get(carNumber).add(topModel);
            System.out.println("là");
        }
    }

    /**
     * Checks if {@link com.kronos.model.TopModel top} for this car respects the job logic.
     * A "RACE" top should only happen after a "RACE" or a "OUT" top.
     * An "IN" top should only happen after a "RACE" or an "OUT" top.
     * An "OUT" top should only happen after an "IN" top.
     *
     * @param lastTopType     the last top type entered by the board man
     * @param previousTopType the previous top entered by the board man
     * @return true if the job logic is respected, false otherwise
     */
    public boolean checkTopLogic(String lastTopType, String previousTopType) {
        boolean respectsLogic = false;
        if (previousTopType.equals("R") && (lastTopType.equals("R") || lastTopType.equals("I"))) {
            respectsLogic = true;
        } else if (previousTopType.equals("I") && lastTopType.equals("O")) {
            respectsLogic = true;
        } else if (previousTopType.equals("O") && (lastTopType.equals("R") || lastTopType.equals("I"))) {
            respectsLogic = true;
        } else if (lastTopType == null) {
            respectsLogic = true;
        }
        return respectsLogic;
    }

    /**
     * load the progress bar according to the average time
     *
     * @param firstTime the first value for loading
     */
    private void handleMeanTimeBar(int firstTime) {

        if (isExtancier)
            pulseTransition = new PulseTransition(meanTimeBar);
        meantime = getMeanTime(listOfMeanTime);
        double timeToUpload = meantime * 60;
        stopanimation();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                isExtancier = false;
                meanTimeBar.setStyle("-fx-accent: blue;");
                updateProgress(firstTime, timeToUpload);
                for (int i = firstTime; i < timeToUpload; i++) {
                    updateProgress(i + 1, timeToUpload);
                    Thread.sleep(1000);
                    if (timeToUpload - (i + 30) < 1) {
                        meanTimeBar.setStyle("-fx-accent: red;");
                    }

                }
                pulseTransition.setCycleCount(PulseTransition.INDEFINITE);
                pulseTransition.play();
                return null;
            }
        };

        meanTimeBar.progressProperty().unbind();
        meanTimeBar.progressProperty().bind(task.progressProperty());
        thread = new Thread(task);
        thread.start();

    }


    /**
     *
     */
    @FXML
    public void editable() {
        if (toogleedit.isSelected()) {
            table_info.setEditable(true);
            col_delete.setVisible(true);
        } else {
            table_info.setEditable(false);
            col_delete.setVisible(false);
        }
    }

    /**
     *
     */
    private void initTable() {
        initCols();
    }

    /**
     *
     */
    private void initCols() {
        colCarNumber.setCellValueFactory(new PropertyValueFactory<>("carNumber"));
        col_typetop.setCellValueFactory(new PropertyValueFactory<>("topType"));
        col_racetime.setCellValueFactory(new PropertyValueFactory<>("raceTime"));
        col_laptime.setCellValueFactory(new PropertyValueFactory<>("lapTime"));
        colLapNumber.setCellValueFactory(new PropertyValueFactory<>("lap"));
        col_time.setCellValueFactory(new PropertyValueFactory<>("time"));
        col_comment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        col_time.setSortType(TableColumn.SortType.DESCENDING);
        table_info.getSortOrder().add(col_time);
        col_delete.setCellFactory(cellFactory);

        editableCols();
    }

    /**
     *
     */
    private void editableCols() {

        colCarNumber.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colCarNumber.setOnEditCommit(this::editCarNumber);

        col_typetop.setCellFactory(TextFieldTableCell.forTableColumn());
        col_typetop.setOnEditCommit(this::editTopType);

        col_time.setCellFactory(TextFieldTableCell.forTableColumn());
        col_time.setOnEditCommit(this::editTopTime);

        col_laptime.setCellFactory(TextFieldTableCell.forTableColumn());
        col_laptime.setOnEditCommit(this::editLapTime);

        col_racetime.setCellFactory(TextFieldTableCell.forTableColumn());
        col_racetime.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setRaceTime(e.getNewValue());
        });

        colLapNumber.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colLapNumber.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setLap(e.getNewValue());
        });

        col_comment.setCellFactory(TextFieldTableCell.forTableColumn());
        col_comment.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setComment(e.getNewValue());
        });

    }

    private void editLapTime(TableColumn.CellEditEvent<TopModel, String> e) {
        int row = e.getTableView().getSelectionModel().selectedIndexProperty().get();
        int carNumber = e.getTableView().getItems().get(row).getCarNumber();
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        long topId = e.getTableView().getItems().get(row).getId();
        int index = findTopIndexWithId(carNumber, topId);
        String topTime = e.getTableView().getItems().get(row).getTime();
        String oldLapTime = e.getOldValue();
        String newLapTime = e.getNewValue();
        TopModel top = topModels.get(index);
        top.setLapTime(newLapTime);
        top.setTime(recalculateTopTime(topModels, index, oldLapTime, newLapTime));
        //top.setTime(recalculateTime(topTime, oldLapTime, newLapTime, "dd-MM-yyyy HH:mm:ss", "mm:ss:SS"));
        int newPos = findTopNewPosition(carNumber, top);
        if (newPos != index) {
            topModels.remove(index);
            topModels.add(newPos, top);
            updateTopLogic(carNumber, newPos, newPos);
            System.out.println("nouv :" + newPos);
            System.out.println(topModels.get(newPos).getTopType());
            System.out.println(topModels.get(newPos).getLapTime());
        }
        table_info.refresh();
        table_info.sort();
    }

    private String recalculateTime(String timeToUpdate, String oldTime, String newTime, String pattern1, String pattern2) {
        String ret = timeToUpdate;
        if (!timeToUpdate.isEmpty()) {
            try {
                SimpleDateFormat dtfTtu = new SimpleDateFormat(pattern1);
                dtfTtu.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date ttu = dtfTtu.parse(timeToUpdate);
                long ttuMillis = ttu.getTime();
                SimpleDateFormat dtfOldTime = new SimpleDateFormat(pattern2);
                dtfOldTime.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date oldT = dtfOldTime.parse(oldTime);
                SimpleDateFormat dtfNewTime = new SimpleDateFormat(pattern2);
                dtfNewTime.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date newT = dtfNewTime.parse(newTime);
                long diffInMillies = newT.getTime() - oldT.getTime();
                System.out.println("diffMillies :" + diffInMillies);
                if (Math.abs(diffInMillies) <= ttuMillis) {
                    long newTMillis = ttuMillis + diffInMillies;
                    DateFormat simple = new SimpleDateFormat(pattern1);
                    simple.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date result = new Date(newTMillis);
                    ret = simple.format(result);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    private String recalculateTopTime(ArrayList<TopModel> topModels, int index, String oldLapTime, String newLapTime) {
        String ret = "";
        SimpleDateFormat df1 = new SimpleDateFormat("mm:ss:SS");
        df1.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date currentLapTime = df1.parse(newLapTime);
            Date formerLapTime = df1.parse(oldLapTime);
            SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            df2.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date topTime = df2.parse(topModels.get(index).getTime());
            long newTopTimeInMillis = topTime.getTime() + (currentLapTime.getTime() - formerLapTime.getTime());
            ret = df2.format(newTopTimeInMillis);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private String recalculateRaceTime(ArrayList<TopModel> topModels, int index) {
        String ret = "";
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        df1.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date currentTopTime = df1.parse(topModels.get(index).getTime());
            Date raceStartTime = raceModel.getStartingTime();
            System.out.println(df1.format(raceStartTime));
            long newRaceTimeInMillis = currentTopTime.getTime() - raceStartTime.getTime();
            SimpleDateFormat df2 = new SimpleDateFormat("mm:ss:SS");
            df2.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date newRaceTime = new Date(newRaceTimeInMillis);
            ret = df2.format(newRaceTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private void recalculateLapTime(ArrayList<TopModel> topModels) {
        String ret = "";
        int index = 0;
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        df1.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat df2 = new SimpleDateFormat("mm:ss:SS");
        df2.setTimeZone(TimeZone.getTimeZone("GMT"));
        while (index < topModels.size()) {
            try {
                Date previousTopTime;
                Date currentTopTime = df1.parse(topModels.get(index).getTime());
                if (index > 0) {
                    previousTopTime = df1.parse(topModels.get(index - 1).getTime());
                    long newLapTimeMillis = currentTopTime.getTime() - previousTopTime.getTime();
                    Date newLapTime = new Date(newLapTimeMillis);
                    topModels.get(index).setLapTime(df2.format(newLapTime));
                } else {
                    topModels.get(index).setLapTime("00:00:00");
                    topModels.get(index).setComment("-First Top O time is always 00:00:00");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            index++;
        }
        System.out.println(ret);
    }

    private void recalculateLaps(ArrayList<TopModel> topModels, int index) {
        int i = index;
        System.out.println("index:" + index);
        if (index < topModels.size() - 1 && topModels.size() > 1) {
            topModels.get(index).setLap(topModels.get(index + 1).getLap());
            while (i < topModels.size() - 1 && topModels.get(i).getLap() == topModels.get(i + 1).getLap()) {
                topModels.get(i + 1).setLap(topModels.get(i + 1).getLap() + 1);
                i++;
            }
        } else if (index == topModels.size() - 1 && topModels.size() > 1) {
            topModels.get(index).setLap(topModels.get(index - 1).getLap() + 1);
        }
    }

    /**
     * @param e
     */
    private void editTopType(TableColumn.CellEditEvent<TopModel, String> e) {
        int row = e.getTableView().getSelectionModel().selectedIndexProperty().get();
        int carNumber = e.getTableView().getItems().get(row).getCarNumber();
        long lastTopId = e.getTableView().getItems().get(row).getId();
        String oldTopType = e.getOldValue();
        String newTopType = e.getNewValue();
        if ((newTopType.equals("I") || newTopType.equals("O") || newTopType.equals("R"))) {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setTopType(newTopType);
            if (!checkTopLogicOnEdit(carNumber, lastTopId)) {
                updateTopLogic(carNumber, findTopIndexWithId(carNumber, lastTopId), findTopIndexWithId(carNumber, lastTopId));
            }
            table_info.refresh();
        } else {
            table_info.refresh();
            Alerts.error("ERREUR", "Type de top invalide");
        }
    }

    private void editCarNumber(TableColumn.CellEditEvent<TopModel, Integer> e) {
        int row = e.getTableView().getSelectionModel().selectedIndexProperty().get();
        int oldCarNumber = e.getOldValue();
        int newCarNumber = e.getNewValue();
        long topId = e.getTableView().getItems().get(row).getId();
        boolean carExists = carExists(newCarNumber);
        if (carExists) {
            TopModel top = findTop(oldCarNumber, topId);
            int index = findTopNewPosition(newCarNumber, top);
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setCarNumber(newCarNumber);
            raceModel.getTopsMap().get(newCarNumber).add(index, top);
            updateTopLogicForOtherFields(newCarNumber, index);
            removeTop(oldCarNumber, topId);
            table_info.refresh();
        } else {
            Alerts.error("ERREUR", "Cette voiture n'existe pas");
        }
    }

    /**
     * @param event
     */
    private void editTopTime(TableColumn.CellEditEvent<TopModel, String> event) {
        int row = event.getTableView().getSelectionModel().selectedIndexProperty().get();
        int carNumber = event.getTableView().getItems().get(row).getCarNumber();
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        long topId = event.getTableView().getItems().get(row).getId();
        int index = findTopIndexWithId(carNumber, topId);
        String oldTopTime = event.getOldValue();
        String newTopTime = event.getNewValue();
        TopModel top = topModels.get(index);
        String lapTime = top.getLapTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long newTopTimeMillis = df.parse(newTopTime).getTime();
            long raceStartTimeMillis = raceModel.getStartingTime().getTime();
            System.out.println(df.format(raceStartTimeMillis));
            if (newTopTimeMillis < raceStartTimeMillis) {
                top.setTime(df.format(raceStartTimeMillis));
                top.setComment("-Top time cannot be inferior to starting time");
            } else {
                top.setTime(newTopTime);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        int newPos = findTopNewPosition(carNumber, top);
        if (newPos != index) {
            topModels.remove(index);
            if (newPos < topModels.size()) {
                topModels.add(newPos, top);
            } else {
                topModels.add(top);
            }
            updateTopLogic(carNumber, newPos, newPos);
        }

        System.out.println(topModels.size());
        top = topModels.get(index);
        recalculateLapTime(topModels);
        if (raceModel instanceof LapRaceModel) {
            recalculateLaps(topModels, newPos);
        } else {
            top.setRaceTime(recalculateRaceTime(topModels, newPos));
        }
        table_info.refresh();
        table_info.sort();

    }

    private void updateTopLogic(int carNumber, int origin, int index) {
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        TopModel currentTop = topModels.get(index);
        if (index <= origin && index > 0) {
            TopModel previousTop = topModels.get(index - 1);
            if (checkTopLogic(currentTop.getTopType(), previousTop.getTopType())) {
                updateTopLogic(carNumber, origin, index - 1);
            } else {
                if (currentTop.getTopType().equals("R") || currentTop.getTopType().equals("I")) {
                    if (index != 1) {
                        previousTop.setTopType("R");
                        previousTop.setComment("-Top R système-" + previousTop.getComment());
                    }
                } else {
                    if (index == 1) {
                        currentTop.setTopType("R");
                        currentTop.setComment("-Top R système-" + currentTop.getComment());
                    } else {
                        previousTop.setTopType("I");
                        previousTop.setComment("-Top I système-" + previousTop.getComment());
                    }

                }
                updateTopLogic(carNumber, origin, index - 1);
            }
        }
        if (index == 0 && !currentTop.getTopType().equals("O")) {
            currentTop.setTopType("O");
            currentTop.setComment("-Top O système-" + currentTop.getComment());
        }
        if (index >= origin && index < topModels.size() - 1) {
            TopModel nextTop = topModels.get(index + 1);
            if (checkTopLogic(nextTop.getTopType(), currentTop.getTopType())) {
                updateTopLogic(carNumber, origin, index + 1);
            } else {
                if (currentTop.getTopType().equals("R") || currentTop.getTopType().equals("O")) {
                    nextTop.setTopType("R");
                    nextTop.setComment("-Top R système-" + nextTop.getComment());
                } else {
                    nextTop.setTopType("O");
                    nextTop.setComment("-Top O système-" + nextTop.getComment());
                }
                updateTopLogic(carNumber, origin, index + 1);
            }
        }
    }

    /**
     * @param carNumber
     * @param index
     */
    private void updateTopLogicForOtherFields(int carNumber, int index) {
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        TopModel currentTop = topModels.get(index);
        if (index > 0 && index < topModels.size() - 1) {
            TopModel previousTop = topModels.get(index - 1);
            if (previousTop.getTopType().equals("I")) {
                previousTop.setTopType("R");
                previousTop.setComment("-Top R système-" + previousTop.getComment());
                if (!currentTop.getTopType().equals("I")) {
                    currentTop.setTopType("I");
                    currentTop.setComment("-Top I système-" + currentTop.getComment());
                }
            }
        } else if (index == 0) {
            if (topModels.size() > 1) {
                TopModel nextTop = topModels.get(index + 1);
                nextTop.setTopType("R");
                nextTop.setComment("-Top R système-" + nextTop.getComment());
            }
            if (!currentTop.getTopType().equals("O")) {
                currentTop.setTopType("O");
                currentTop.setComment("-Top O système-" + currentTop.getComment());
            }
        } else if (index == topModels.size() - 1) {
            TopModel previousTop = topModels.get(index - 1);
            if (previousTop.getTopType().equals("I")) {
                if (!currentTop.getTopType().equals("O")) {
                    currentTop.setTopType("O");
                    currentTop.setComment("-Top O système-" + currentTop.getComment());
                }
            }
        }
    }

    /**
     * @param carNumber
     * @param lastTopId
     * @return
     */
    private boolean checkTopLogicOnEdit(int carNumber, long lastTopId) {
        boolean found = false;
        boolean respectsLogic = false;
        int index = findTopIndexWithId(carNumber, lastTopId);
        ArrayList<TopModel> tops = raceModel.getTopsMap().get(carNumber);
        if (index > 0) {
            respectsLogic = checkTopLogic(tops.get(index).getTopType(), tops.get(index - 1).getTopType());
            if (respectsLogic && index < tops.size()) {
                respectsLogic = checkTopLogic(tops.get(index + 1).getTopType(), tops.get(index).getTopType());
            }
        }
        return respectsLogic;
    }

    /**
     * @param oldCarNumber
     * @param topId
     * @return
     */
    private TopModel findTop(int oldCarNumber, long topId) {
        boolean found = false;
        TopModel topModel = null;
        ArrayList<TopModel> oldCarTops = raceModel.getTopsMap().get(oldCarNumber);
        Iterator<TopModel> it = oldCarTops.iterator();
        while (it.hasNext() && !found) {
            TopModel top = it.next();
            if (top.getId() == topId) {
                found = true;
                topModel = top;
            }
        }
        return topModel;
    }

    /**
     * @param carNumber
     * @param topId
     */
    private void removeTop(int carNumber, long topId) {
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        int index = findTopIndexWithId(carNumber, topId);
        TopModel currentTop = topModels.get(index);
        updateTopLogicOnRemove(topModels, index);
        topModels.remove(currentTop);
        System.out.println("Le top " + topId + "  a été supprimé");
    }

    /**
     * @param topModels
     * @param index
     */
    private void updateTopLogicOnRemove(ArrayList<TopModel> topModels, int index) {

        if (index > 0 && index < topModels.size() - 1) {
            TopModel previousTop = topModels.get(index - 1);
            TopModel nextTop = topModels.get(index + 1);
            if (previousTop.getTopType().equals("R") && nextTop.getTopType().equals("I")) {
                previousTop.setTopType("O");
                previousTop.setComment("-Top O système-" + previousTop.getComment());
            } else if (previousTop.getTopType().equals("I") && nextTop.getTopType().equals("R")) {
                previousTop.setTopType("R");
                previousTop.setComment("-Top R système-" + previousTop.getComment());
            } else if (previousTop.getTopType().equals("I") && nextTop.getTopType().equals("I")) {
                nextTop.setTopType("O");
                nextTop.setComment("-Top O système-" + nextTop.getComment());
            } else if (previousTop.getTopType().equals("O") && nextTop.getTopType().equals("O")) {
                nextTop.setTopType("R");
                nextTop.setComment("-Top R système-" + nextTop.getComment());
            }
        } else if (index == 0) {
            if (topModels.size() > 1) {
                TopModel nextTop = topModels.get(index + 1);
                nextTop.setTopType("O");
                nextTop.setComment("-Top O système-" + nextTop.getComment());
                if (topModels.size() > 2) {
                    TopModel topAfterNext = topModels.get(index + 2);
                    if (topAfterNext.getTopType().equals("O")) {
                        topAfterNext.setTopType("R");
                        topAfterNext.setComment("-Top R système-" + topAfterNext.getComment());
                    }
                }
            }
        }
    }


    /**
     * @param carNumber
     * @param topId
     * @return
     */
    private int findTopIndexWithId(int carNumber, long topId) {
        boolean found = false;
        int index = 0;
        ArrayList<TopModel> newCarTops = raceModel.getTopsMap().get(carNumber);
        Iterator<TopModel> it = newCarTops.iterator();
        while (it.hasNext() && !found) {
            TopModel top = it.next();
            if (top.getId() == topId) {
                found = true;
            } else {
                index++;
            }
        }
        return index;
    }

    /**
     * @param carNumber
     * @param newTop
     * @return
     */
    private int findTopNewPosition(int carNumber, TopModel newTop) {

        boolean found = false;
        int index = 0;
        ArrayList<TopModel> newCarTops = raceModel.getTopsMap().get(carNumber);
        Iterator<TopModel> it = newCarTops.iterator();
        while (it.hasNext() && !found) {
            TopModel top = it.next();
            if (LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isBefore(LocalDateTime.parse(top.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
                found = true;
            } else if ((LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isEqual(LocalDateTime.parse(top.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))))) {
                index = findTopIndexWithId(carNumber, newTop.getId());
            } else {
                index++;
            }
        }
        return index;
    }

    private boolean carExists(int carNumber) {
        boolean exists = false;
        Set<Integer> keys = raceModel.getTopsMap().keySet();
        if (keys.contains(carNumber)) {
            exists = true;
        }
        return exists;
    }

    /**
     *
     */
    private void loadData(TopModel topModel) {
        table_info.getItems().add(0, topModel);
    }

    /**
     *
     */
    Callback<TableColumn<TopModel, Double>, TableCell<TopModel, Double>> cellFactory = new Callback<TableColumn<TopModel, Double>, TableCell<TopModel, Double>>() {
        @Override
        public TableCell<TopModel, Double> call(final TableColumn<TopModel, Double> param) {
            final TableCell<TopModel, Double> cell = new TableCell<TopModel, Double>() {

                private final Button btn = new Button("Delete");
                private final Button btn1 = new Button("Edit");

                {
                    btn.setOnAction((ActionEvent event) -> {
                        TopModel top = getTableView().getItems().get(getIndex());
                        System.out.println("J'ai appuyé sur le bouton dont l'ID est:  " + top.getId() +
                                "  et le numVoiture est:  " + top.getCarNumber());
                        removeTop(top.getCarNumber(), top.getId());
                        table_info.getItems().remove(top);
                        table_info.refresh();
                    });
                }

                @Override
                public void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
            return cell;
        }
    };


    /**
     * Pulse animation
     * stop animation on the progress Bar
     */

    public void stopanimation() {
        pulseTransition.stop();
    }

    /**
     * Get's the mean Time to upload the progess Bar
     *
     * @param mylistoftime
     * @return meanTime  of laps
     */

    public double getMeanTime(ArrayList<Double> mylistoftime) {
        double meantimeaux = 0.00;
        if (mylistoftime.size() == 0) {
            LocalTime tempsmoyen1 = LocalTime.now();
            double mytime = (tempsmoyen1.getHour() * 60) + tempsmoyen1.getMinute() + (tempsmoyen1.getSecond() / 60.0);
            double departtime = (departureTime.getHour() * 60) + departureTime.getMinute() + (departureTime.getSecond() / 60.0);
            meantimeaux = mytime - departtime;
        } else if (mylistoftime.size() == 1) {
            meantimeaux = mylistoftime.get(0);
        } else if (mylistoftime.size() == 2) {
            meantimeaux = ((mylistoftime.get(0) + mylistoftime.get(1)) / 2.0);
        } else {
            int start = mylistoftime.size() - 2;
            meantimeaux = ((mylistoftime.get(start - 1) + mylistoftime.get(start) + mylistoftime.get(start + 1)) / 3.0);


        }

        return meantimeaux;
    }

    /**
     * have the average time of the competing cars to make the rankings
     *
     * @return HashMap<Long, Double>
     */
    public HashMap<Integer, Double> getMeanTimeForFollowedCars() {

        HashMap<Integer, Double> follewedCarsMeantime = new HashMap<>();
        Double meanTime = 0.0;
        for (Map.Entry listOfRivalMeantime : rivalCarListOfMeantime.entrySet()) {
            ArrayList<Double> listMeantimeRicval = (ArrayList<Double>) listOfRivalMeantime.getValue();

            if (listMeantimeRicval.size() == 1) {
                meanTime = listMeantimeRicval.get(0);
            } else if (listMeantimeRicval.size() == 2) {
                meanTime = ((listMeantimeRicval.get(0) + listMeantimeRicval.get(1)) / 2.0);
            } else {
                int lengthOf = listMeantimeRicval.size();
                meanTime = ((listMeantimeRicval.get(lengthOf - 1) + listMeantimeRicval.get(lengthOf - 2) + listMeantimeRicval.get(lengthOf - 3)) / 3.0);
            }
            System.out.println("le temps moyen est pour la voiture est %s  " + meanTime + " et son numero " + listOfRivalMeantime.getKey());
            follewedCarsMeantime.put((Integer) listOfRivalMeantime.getKey(), meanTime);
        }
        return follewedCarsMeantime;
    }

    /**
     * classification of cars by average lap time
     */

    public void displayNewRank() {
        List<MainCarModel> maincar = (List<MainCarModel>) (List<?>) App.getDataManager().getModels(MainCarModel.class);
        listPastRank.getItems().clear();
        listPastRank.getItems().addAll(listNowRank.getItems());
        listNowRank.getItems().clear();

        HashMap<Integer, Double> rivalCarsMeanTime = getMeanTimeForFollowedCars();
        Double mainCarMeanTime = getMeanTime(listOfMeanTime);
        rivalCarsMeanTime.put(maincar.get(0).getNumber(), mainCarMeanTime);
        List<Map.Entry<Integer, Double>> list =
                new LinkedList<>(rivalCarsMeanTime.entrySet());
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));

        HashMap<Integer, Double> map_apres = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> entry : list) {
            map_apres.put(entry.getKey(), entry.getValue());
            listNowRank.getItems().add(entry.getKey());
        }


    }

    /**
     * increment the clock  of the spent time when it's a time race
     */

    private void incrementTime() {
        localSpentTime = localSpentTime.plusSeconds(1);
        spentTime.setText(localSpentTime.format(dtf));
    }

    /**
     * decrement the remaining clock when it's a time race
     */

    private void decrementTime() {
        localRemainningTime = localRemainningTime.minusSeconds(1);
        remainingTime.setText(localRemainningTime.format(dtf));
        if (localRemainningTime.equals(LocalTime.parse("00:00:00"))) {
            localRemainningTime = LocalTime.parse("00:00:00");
            remainingTime.setText(localRemainningTime.format(dtf));
            setRaceInformations(RaceState.DONE);
            if (raceModel instanceof TimeRace) {
                endOfTimeRace();
            }

        }

    }

    /**
     * getting  of current time
     */
    private void getCurrentTime() {
        currentTime = LocalTime.now();
        currentHour.setText(currentTime.format(dtf));
    }

    /**
     * starting of the race timer  when the user click on the button start
     *
     * @param event
     */
    @FXML
    private void startTimer(ActionEvent event) {
        if (raceModel.getRaceState() != RaceState.DONE) {
            istartRace = true;
            startRace.setDisable(true);
            if (raceModel instanceof TimeRace) {
                if (!localRemainningTime.equals(LocalTime.parse("00:00:00"))) {
                    departureTime = LocalTime.now();
                    spentTimeline.play();
                    remainingTimeline.play();
                    departureHour.setText(currentTime.format(dtf));
                }
            } else {
                departureTime = LocalTime.now();
                departureHour.setText(departureTime.format(dtf));
                startRace.setDisable(true);
            }
            if(raceModel.getRaceState().equals(RaceState.BREAK)){
                handleNewTop();
                startTimerForRivalCar();
                setRaceInformations(RaceState.IN_PROGRESS);
            }else {
                handleNewTop();
                handleNewTopForRivalCar();
                startTimerForRivalCar();
                setRaceInformations(RaceState.IN_PROGRESS);
            }




        } else {
            Alerts.info("INFORMATION", "Cette course est terminée");
        }
    }

    /**
     * make the break on the race
     *
     * @param event
     * @throws InterruptedException
     */
    @FXML
    private void pauseTimer(ActionEvent event) throws InterruptedException {
        if (istartRace) {
            if (raceModel instanceof TimeRace) {
                if (spentTimeline.getStatus().equals(Animation.Status.PAUSED)) {
                    spentTimeline.play();
                    remainingTimeline.play();
                    pauseRace.setText("Pause");
                    wakeUpThread();
                    setRaceInformations(RaceState.IN_PROGRESS);


                } else if (spentTimeline.getStatus().equals(Animation.Status.RUNNING)) {
                    spentTimeline.pause();
                    remainingTimeline.pause();
                    pauseRace.setText("Continue");
                    breakTimer();
                    setRaceInformations(RaceState.BREAK);

                }
            } else {
                if (raceModel.getRaceState().equals(RaceState.IN_PROGRESS)) {
                    pauseRace.setText("Continue");
                    breakTimer();
                    setRaceInformations(RaceState.BREAK);


                } else if (raceModel.getRaceState().equals(RaceState.BREAK)) {
                    pauseRace.setText("Pause");
                    wakeUpThread();
                    setRaceInformations(RaceState.IN_PROGRESS);


                }
            }
        } else {
            Alerts.info("INFORMATION", "aucune course n'a été demarrer");
        }
    }

    /**
     * ending of the race
     *
     * @param event
     */
    @FXML
    private void endTimer(ActionEvent event) {
        Alerts.warning("Avertissement", "vouliez vous mettre fin a cette course");
        if (startRace.isDisable()) {
            setRaceInformations(RaceState.DONE);
            if (raceModel instanceof TimeRace) {
                endOfTimeRace();
            } else {
                stopRace.setDisable(true);
                pauseRace.setDisable(true);
                startRace.setDisable(true);
                spentTime.setText(String.valueOf(numberOfLapsDone));
                remainingTime.setText(String.valueOf(remainingLaps));
                endAllThread();
            }

        } else {
            Alerts.info("INFORMATION", "aucune course n'a été demarrer");
        }
    }

    /**
     * setting of the information when it's the end of the Race
     */

    public void endOfTimeRace() {
        spentTimeline.stop();
        remainingTimeline.stop();
        startRace.setDisable(true);
        pauseRace.setDisable(true);
        stopRace.setDisable(true);
        spentTime.setText(localSpentTime.format(dtf));
        remainingTime.setText(localSpentTime.format(dtf));
        endAllThread();


    }

    /**
     * setting of information about the race
     *
     * @param raceState
     */

    public void setRaceInformations(RaceState raceState) {
        raceModel.setRaceState(raceState);
        raceModel.setTimeLapsRemaining(remainingTime.getText());
        raceModel.setTimeLapsSpent(spentTime.getText());
        App.getDataManager().persist(raceModel);
        App.getDataManager().saveFile();

    }

    /**
     * check if we should end the race
     * setting of the informations about the race
     */
    public void checkEndOfRace() {

        if (raceModel instanceof TimeRace) {
            if (localRemainningTime.equals(LocalTime.parse("00:00:00"))) {
                setRaceInformations(RaceState.DONE);
                endOfTimeRace();

            }
            setRaceInformations(RaceState.IN_PROGRESS);
        } else {

            if (remainingLaps == 0) {
                Alerts.info("Information", "la course est terminée");
                setRaceInformations(RaceState.DONE);
                endAllThread();

            } else {
                /*remainingLaps--;
                numberOfLapsDone++;*/
                remainingTime.setText(String.valueOf(remainingLaps));
                spentTime.setText(String.valueOf(numberOfLapsDone));
                setRaceInformations(RaceState.IN_PROGRESS);
            }
        }
    }

    /**
     * Display main car information and the currently pilot
     * pilot information
     * first name
     * last name
     * birthday
     * <p>
     * car information
     * car model
     * car Brand
     * car Team
     */

    public void maincarinformation() {


        List<MainCarModel> maincarinformation = (List<MainCarModel>) (List<?>) App.getDataManager().getModels(MainCarModel.class);
        for (MainCarModel model : maincarinformation) {
            mycar = model;
        }

        lastNamePilotMainCar.setText(mycar.getPilotModel().getLastName());
        firstNamePilotMainCar.setText(mycar.getPilotModel().getFirstName());
        if (!(mycar.getPilotModel().getDateOfBirth() == null)) {
            dateOfBirthPilot.setText(new SimpleDateFormat("dd-MM-yyyy").format(mycar.getPilotModel().getDateOfBirth()));
        }
        mainCarBrand.setText(mycar.getBrand());
        mainCarModel.setText(mycar.getModel());
        mainCarTeam.setText(mycar.getTeam());

    }

    /**
     * @return
     */
    public ArrayList<CarModel> getFollowedCars() {
        ArrayList<CarModel> followedCars = new ArrayList<>();
        List<CarModel> carModels = (List<CarModel>) (List<?>) App.getDataManager().getModels(CarModel.class);
        for (CarModel carModel : carModels) {
            followedCars.add(carModel);
        }
        return followedCars;
    }

    public ArrayList<RaceModel> getRace() {
        ArrayList<RaceModel> raceModels = new ArrayList<>();
        List<TimeRaceModel> timeRaceModels = (List<TimeRaceModel>) (List<?>) App.getDataManager().getModels(TimeRaceModel.class);
        List<LapRaceModel> lapRaceModels = (List<LapRaceModel>) (List<?>) App.getDataManager().getModels(LapRaceModel.class);
        if (!timeRaceModels.isEmpty()) {
            for (TimeRaceModel model : timeRaceModels) {
                raceModels.add(model);
            }
        } else {
            for (LapRaceModel model : lapRaceModels) {
                raceModels.add(model);
            }
        }
        return raceModels;
    }

    /**
     * @param followedCars
     * @return
     */
    public ArrayList<String> getFollowedCarsNumbers(ArrayList<CarModel> followedCars) {
        ArrayList<String> followedCarsNumbers = new ArrayList<>();
        for (CarModel followedCar : followedCars) {
            followedCarsNumbers.add(Integer.toString(followedCar.getNumber()));
        }
        return followedCarsNumbers;
    }

    /**
     * @param carNumber
     * @return
     */
    public TopModel findPreviousTop(int carNumber) {
        TopModel topModel = null;
        if (raceModel.getTopsMap() != null && raceModel.getTopsMap().containsKey(carNumber)) {
            ArrayList<TopModel> topModels;
            topModels = raceModel.getTopsMap().get(carNumber);
            if (!topModels.isEmpty()) {
                topModel = topModels.get(topModels.size() - 1);
            }
        }
        return topModel;
    }

    /**
     * stop the timer to get the time of the current top of the main car
     */
    public void stopTimerBar() {
        isStartTimer = false;
        isSetTimerBar = true;
        threadChrono.stop();

    }

    /**
     * reset  the timer for the next top of the main car
     */
    public void resetTimerBar() {
        millisecondes = 0;
        munites = 0;
        secondes = 0;
        chronoTime = LocalTime.of(0, munites, secondes, millisecondes);
        chronoTopTime.setText(chronoTime.format(dtf2));
        //isStartTimer = false;
        isSetTimerBar = true;
        tmierSign.setVisible(false);


    }

    /**
     * started a Timer of the next top for the main car
     * format of the Time is MM :ss:nn
     */
    public void startTimerBar() {
        isStartTimer = true;


        threadChrono = new Thread(() -> {
            while (isStartTimer) {

                try {
                    Thread.sleep(10);
                    millisecondes++;
                    if (millisecondes == 95) {
                        secondes++;

                        if (!(timebar.equals(timeTocompare)) && isSetTimerBar) {
                            timebar = timebar.minusSeconds(1);
                        } else {
                            isSetTimerBar = false;
                            timebar = timebar.plusSeconds(1);
                        }
                        millisecondes = 0;
                    }
                    if (secondes == 60) {
                        munites++;
                        secondes = 0;
                    }

                    Platform.runLater(() -> {
                        chronoTime = LocalTime.of(0, munites, secondes, millisecondes);
                        chronoTopTime.setText(chronoTime.format(dtf2));
                        labelMeanTime.setText(timebar.format(dtf));
                        if (!isSetTimerBar) {
                            tmierSign.setVisible(true);
                        }
                    });


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        });
        threadChrono.start();
    }

    /**
     * Timer for the rivals cars
     * format of the Time is  HH:MM:ss:nn
     * this timer will be never stopped until the end of race or until the user will closed the
     * software
     */

    public void startTimerForRivalCar() {


        threadChronoRivalCar = new Thread(() -> {
            while (isStartRivalTimer) {

                try {
                    Thread.sleep(10);
                    rivalTimerMillisecondes++;
                    if (rivalTimerMillisecondes == 95) {
                        rivalTimerSecondes++;
                        rivalTimerMillisecondes = 0;
                    }
                    if (rivalTimerSecondes == 60) {
                        rivalTimerMunites++;
                        rivalTimerSecondes = 0;
                    }

                    if (rivalTimerMunites == 60) {
                        rivalTimerHours++;
                        rivalTimerMunites = 0;
                    }
                    Platform.runLater(() -> {
                        chronoTimeRival = LocalTime.of(rivalTimerHours, rivalTimerMunites, rivalTimerSecondes, rivalTimerMillisecondes);
                        chronoRivalCar.setText(chronoTimeRival.format(dtf1));
                    });


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        });
        threadChronoRivalCar.start();


    }

    /**
     * end the different timers
     */

    public void endAllThread() {

        threadChrono.stop();
        thread.stop();
        threadChronoRivalCar.stop();
    }

    /**
     * pause all the different stopwatches
     */

    public void breakTimer() throws InterruptedException {
        if (breakThread) {

            isStartTimer = false;
            isStartRivalTimer = false;
            firstTop = false;
            breakThread = false;
            thread.interrupt();
            thread.join();
        }

    }

    /**
     * restarting the various timers after pausing it .
     */
    public void wakeUpThread() {

        isStartTimer = true;
        isStartRivalTimer = true;
        firstTop = true;
        breakThread = true;
        chronoTime = LocalTime.of(0, munites, secondes, millisecondes);
        chronoTopTime.setText(chronoTime.format(dtf2));
        if (!(tmierSign.isVisible())) {
            int pastTime = (munites * 60) + secondes;
            handleMeanTimeBar(pastTime);
        }
        chronoTimeRival = LocalTime.of(rivalTimerHours, rivalTimerMunites, rivalTimerSecondes, rivalTimerMillisecondes);
        chronoRivalCar.setText(chronoTimeRival.format(dtf1));
        startTimerBar();
        startTimerForRivalCar();

    }

    /**
     *
     */
    @Override
    public void update() {

    }


}
