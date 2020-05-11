package com.kronos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.kronos.App;
import com.kronos.api.LapRace;
import com.kronos.api.MainCar;
import com.kronos.api.Observer;
import com.kronos.api.TimeRace;
import com.kronos.global.animation.PulseTransition;
import com.kronos.global.enums.RaceState;
import com.kronos.global.util.Alerts;
import com.kronos.model.*;
import com.kronos.module.main.Main;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RaceResumeController implements Initializable, Observer {

    @FXML
    public Label i18n_raceinfo;
    @FXML
    public Label i18n_departureHour;
    @FXML
    public Label i18n_actualHour;
    @FXML
    public Label i18n_elapsedTime;
    @FXML
    public Label i18n_remainingTime;
    @FXML
    public Label i18n_elapsedLaps;
    @FXML
    public Label i18n_remainingLaps;
    @FXML
    public Label i18n_panel;
    @FXML
    public Label i18n_details;
    @FXML
    public Tab i18n_currentPilot;
    @FXML
    public Label i18n_currentPilotLastName;
    @FXML
    public Label i18n_currentPilotFirstname;
    @FXML
    public Label i18n_currentPilotDateOfBirth;
    @FXML
    public Tab i18n_currentCar;
    @FXML
    public Label i18n_currentCarBrand;
    @FXML
    public Label i18n_currentCarModel;
    @FXML
    public Label i18n_currentCarTeam;
    @FXML
    public Label i18n_averageLapTime;
    @FXML
    public Label i18n_timerOpponentCar;
    @FXML
    public Label i18n_type;
    @FXML
    public Label i18n_car;
    @FXML
    public Label i18n_comment;
    @FXML
    public Label i18n_raceRanking;
    @FXML
    public Label i18n_previous;
    @FXML
    public Label i18n_inProgress;


    private static ArrayList<Double> listOfMeanTime = new ArrayList<>();
    private static Double meantime = 0.00;
    PulseTransition pulseTransition;
    MainCarModel mycar;
    boolean isStartRivalTimer = true;
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
    public Label currentHour;
    @FXML
    private Label spentTime;
    @FXML
    private Label remainingTime;
    @FXML
    public JFXButton startRace;
    @FXML
    public JFXButton pauseRace;
    @FXML
    public JFXButton stopRace;
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
    private Label carNumber;
    @FXML
    private Label remainingLapsBeforeStop;
    @FXML
    private Label state;
    @FXML
    private Label time;
    @FXML
    private TableView<TopModel> table_info;
    @FXML
    public TableColumn<TopModel, Double> col_delete;
    @FXML
    public TableColumn<TopModel, Integer> colCarNumber;
    @FXML
    public TableColumn<TopModel, String> col_typetop;
    @FXML
    public TableColumn<TopModel, String> col_comment;
    @FXML
    public TableColumn<TopModel, String> col_time;
    @FXML
    public TableColumn<TopModel, String> col_racetime;
    @FXML
    public TableColumn<TopModel, String> col_laptime;
    @FXML
    public TableColumn<TopModel, Integer> colLapNumber;
    @FXML
    public JFXToggleButton toogleedit;
    @FXML
    private JFXTextField topComment;
    @FXML
    private ComboBox<String> car;
    @FXML
    private ComboBox<String> topType;
    @FXML
    private ListView<Integer> listNowRank = new ListView<>();
    @FXML
    private ListView<Integer> listPastRank = new ListView<>();
    private LapRaceModel lapRaceModel;
    private RaceModel raceModel;
    private CarController carController = new CarController();
    private ArrayList<TopModel> topModels = new ArrayList<>();
    private ArrayList<CarModel> carModels = new ArrayList<>();
    private Integer[] relayCycles = new Integer[3];
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
    private HashMap<Integer, ArrayList<TopModel>> topsMaps = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> rivalCarTopsTimes = new HashMap<>();
    private HashMap<Integer, ArrayList<Double>> rivalCarListOfMeantime = new HashMap<>();
    private boolean isExtancier = true, breakThread = true;
    private Thread thread, threadChrono, threadChronoRivalCar;
    private int munites = 0, secondes = 0, millisecondes = 0,hours=0, decimalpartTosecond = 0, intergerpart = 0, numberOfLapsDone = 0, remainingLaps;
    private int rivalTimerMunites = 0, rivalTimerHours = 0, rivalTimerSecondes = 0, rivalTimerMillisecondes = 0, numberMainCar = 0;
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

    public static RaceResumeController ctrl;

    public RaceResumeController() {
    }

    /**
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ctrl=this;
        App.getDataManager().attach(this);
        col_racetime.setVisible(false);
        colLapNumber.setVisible(false);
        topType.setDisable(true);
        localSpentTime = LocalTime.parse("00:00:00");
        time2 = LocalTime.parse("00:00");
        col_delete.setVisible(false);
        for (CarModel carModel : getFollowedCars()) {
            if (carModel instanceof MainCar) {
                carNumber.setText(Integer.toString(carModel.getNumber()));
            }
        }
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
                computeNumberOfStops();
                listOfMeanTime.add((double)raceModel.getDefaultMeanLapTime());
            } else {
                startRace.setText("Restart");
                localRemainningTime = LocalTime.parse(raceModel.getTimeLapsRemaining());
                localSpentTime = LocalTime.parse(raceModel.getTimeLapsSpent());
                spentTime.setText(localSpentTime.format(dtf));
                remainingTime.setText(localRemainningTime.format(dtf));
                setRaceInformations(RaceState.BREAK);
                computeNumberOfStops();

            }
            spentTimeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> incrementTime()));
            spentTimeline.setCycleCount(Animation.INDEFINITE);
            remainingTimeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> decrementTime()));
            remainingTimeline.setCycleCount(Animation.INDEFINITE);
        } else {
            colLapNumber.setVisible(true);
            i18n_elapsedTime.setVisible(false);
            i18n_remainingTime.setVisible(false);
            i18n_elapsedLaps.setVisible(true);
            i18n_remainingLaps.setVisible(true);
            System.out.println("test race " + raceModel);

            if (raceModel.getRaceState().equals(RaceState.CREATION)) {
                remainingLaps = ((LapRaceModel) raceModel).getNumberOfLaps();
                numberOfLapsDone = Integer.parseInt(raceModel.getTimeLapsSpent());
                raceModel.setTimeLapsRemaining(String.valueOf(remainingLaps));
                raceModel.setTimeLapsSpent(String.valueOf(numberOfLapsDone));
                computeNumberOfStops();
                listOfMeanTime.add((double)raceModel.getDefaultMeanLapTime());



            } else {
                startRace.setText("Restart");
                remainingLaps = Integer.parseInt(raceModel.getTimeLapsRemaining());
                numberOfLapsDone = Integer.parseInt(raceModel.getTimeLapsSpent());
                computeNumberOfStops();
                setRaceInformations(RaceState.BREAK);

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
            ArrayList<TopModel> mytopmodelList = new ArrayList<>();
            topsMaps.put(carmodel.getNumber(), mytopmodelList);
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
                        if (topModel.getCarNumber() == carmodel.getNumber()) {
                            topsMaps.get(carmodel.getNumber()).add(topModel);
                            carmodel.getTopList().add(topModel);
                        }
                        if (topModel.getCarNumber() == carmodel.getNumber() && (topModel.getTopType().equals("R") || topModel.getTopType().equals("O"))) {
                            rivalCarListOfMeantime.get(carmodel.getNumber()).add(lapTimeForMeanTime);

                        }
                    }
                }
            } else {
                numberMainCar = carmodel.getNumber();
                if (!topModelList.isEmpty()) {
                    for (TopModel topModel : topModelList) {
                        loadData(topModel);
                        topModels.add(topModel);
                        int muniteteToLoad = Integer.parseInt(topModel.getLapTime().substring(0, 2));
                        int secondeToLoad = Integer.parseInt(topModel.getLapTime().substring(3, 5));
                        int milliToLoad = Integer.parseInt(topModel.getLapTime().substring(6, 8));
                        lapTimeForMeanTime = (muniteteToLoad + (secondeToLoad / 60.0) + (milliToLoad / 100) / 60.0);
                        if (topModel.getCarNumber() == carmodel.getNumber()) {
                            topsMaps.get(carmodel.getNumber()).add(topModel);
                            carmodel.getTopList().add(topModel);
                        }
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


        Scene scene = App.getDecorator().getScene();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                System.out.println(" verif bouton top ");
                KeyCode keyCode = event.getCode();

                File file = new File("top.properties");
                Properties properties = new Properties();
                try {
                    if (!file.exists()) {

                        //file.createNewFile();
                    } else {

                        FileInputStream fileInputStream = new FileInputStream(file);
                        properties.load(fileInputStream);
                        if (keyCode.toString().equals(properties.getProperty("key"))) {
                            handleNewTop();
                            displayNewRank();
                            System.out.println("vous avez fait un top ! ");

                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        });


    }

    private void computeNumberOfStops() {
        relayCycles[0] = raceModel.getRelayInterval();
        try {
            if (raceModel instanceof LapRace) {
                int euclidResult = remainingLaps / raceModel.getRelayInterval();
                int remaining = remainingLaps % raceModel.getRelayInterval();
                relayCycles[0] = raceModel.getRelayInterval();
                relayCycles[1] = euclidResult;
                relayCycles[2] = remaining;

            } else {
                relayCycles[0] = raceModel.getRelayInterval();
                relayCycles[1] = -1;
                relayCycles[2] = 0;
            }
            updatePannel();
        }catch (Exception logArithm){
            logArithm.printStackTrace();
        }

    }

    private void decrementPanel(String lapTime) {
        System.out.println("update");
        int carNumber = Integer.parseInt(car.getSelectionModel().getSelectedItem());
        CarModel carModel = carController.findCar(carModels, carNumber);
        if (carModel instanceof MainCarModel) {
            time.setText(lapTime);
            if (relayCycles[0] > 1 && relayCycles[1] != 0) {
                relayCycles[0]--;
            } else if (relayCycles[0] == 1 && relayCycles[1] != 0) {
                relayCycles[0] = raceModel.getRelayInterval();
                relayCycles[1]--;
            } else if (relayCycles[0] == 1) {
                relayCycles[0] = raceModel.getRelayInterval();
            } else {
                relayCycles[2]--;
            }
            System.out.println("cycle" + relayCycles[1]);
            updatePannel();
        }
    }

    private void incrementPanel(String lapTime) {
        System.out.println("TIME RACE SUPPRIME 2");
        time.setText(lapTime);
        if (raceModel instanceof LapRace) {
            if (relayCycles[1] != -1 && remainingLaps - 1 < ((LapRace) raceModel).getNumberOfLaps() % raceModel.getRelayInterval()) {
                relayCycles[2]++;
            } else {
                if (relayCycles[1] == 0) {
                    relayCycles[1]++;
                }
                if (relayCycles[0] == raceModel.getRelayInterval()) {
                    relayCycles[0] = 1;
                    if (relayCycles[1] != -1) {
                        relayCycles[1]++;
                    }
                } else {
                    relayCycles[0]++;
                }
            }
        } else {
            if (relayCycles[0] == raceModel.getRelayInterval()) {
                relayCycles[0] = 1;
            } else {
                relayCycles[0]++;
            }
        }
        updatePannel();
    }

    private void updatePannel() {

        if (relayCycles[0] > 1 && relayCycles[1] != 0) {
            remainingLapsBeforeStop.setText(Integer.toString(relayCycles[0]));
            state.setText("-");
        } else if (relayCycles[1] == 0 && relayCycles[2] > 1) {
            remainingLapsBeforeStop.setText(Integer.toString(relayCycles[2]));
            state.setText("-");
        } else {
            if (relayCycles[0] == 1 || relayCycles[2] == 1) {
                if (relayCycles[1] == 0) {
                    remainingLapsBeforeStop.setText("-");
                    state.setText("LAST");
                } else {
                    remainingLapsBeforeStop.setText("-");
                    state.setText("IN");
                }
            } else {
                remainingLapsBeforeStop.setText("-");
                state.setText("END");
            }
        }
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
                    handleTopTimeRace(topModelFirstTop, carModel.getNumber());
                    loadData(topModelFirstTop);
                    carModel.getTopList().add(topModelFirstTop);
                    raceModel.getTopsMap().get(carModel.getNumber()).add(topModelFirstTop);
                    saveTopModel(topModelFirstTop);

                } else {
                    TopModel topModelFirstTop = new TopModel(carModel.getNumber(), dateTimeFirstTop, "O", 0, localTimeFirstTop.format(dtf), "Fist Top");
                    handleTopLapRace(topModelFirstTop, carModel.getNumber());
                    loadData(topModelFirstTop);
                    carModel.getTopList().add(topModelFirstTop);
                    raceModel.getTopsMap().get(carModel.getNumber()).add(topModelFirstTop);
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
        System.out.println(type);
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
            carNumber = numberMainCar;
            carModel = carController.findCar(carModels, carNumber);
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
                handleTopTimeRace(topModel, carNumber);
                decrementPanel(lapTime);
                numberOfLapsDone++;
            } else {
                if (firstTop) {
                    lap = numberOfLapsDone;
                    firstTop = false;
                    decrementPanel(lapTime);
                } else {
                    if (b) {
                        remainingLaps--;
                        numberOfLapsDone++;
                        lap = numberOfLapsDone;
                        decrementPanel(lapTime);
                    } else {
                        lap = numberOfLapsDone;
                    }
                    if (!(carModel instanceof MainCarModel)) {
                        //  System.out.println("cherche la taille du topsmap");
                        int sizeOfArray = carModel.getTopList().size();
                        sizeOfArray = sizeOfArray - 1;
                        TopModel topModel1 = carModel.getTopList().get(sizeOfArray);
                        if (type.equals("R") || type.equals("O")) {
                            lap = topModel1.getLap() + 1;
                        } else {
                            lap = topModel1.getLap();
                        }
                    }
                }
                topModel = new TopModel(carNumber, dateTime, type, lap, lapTime, comment);
                handleTopLapRace(topModel, carNumber);
            }
        } else {
            //Case where top does not respect logical top type order
            if (raceModel instanceof TimeRaceModel) {
                raceTime = localSpentTime.format(dtf);
                if (findPreviousTop(carNumber).getTopType().equals("I") && (type.equals("I") || type.equals("R"))) {
                    topModel = new TopModel(carNumber, dateTime, "O", raceTime, lapTime, comment + "-Top O système");
                    decrementPanel(lapTime);
                    numberOfLapsDone++;
                } else if (!findPreviousTop(carNumber).getTopType().equals("I") && type.equals("O")) {
                    topModel = new TopModel(carNumber, dateTime, "R", raceTime, lapTime, comment + "-Top R système");
                    decrementPanel(lapTime);
                    numberOfLapsDone++;
                }
                handleTopTimeRace(topModel, carNumber);
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
                    if (!(carModel instanceof MainCarModel)) {
                        int sizeOfArray = carModel.getTopList().size();
                        sizeOfArray = sizeOfArray - 1;
                        TopModel topModel1 = carModel.getTopList().get(sizeOfArray);
                        if (type.equals("R") || type.equals("O")) {
                            lap = topModel1.getLap() + 1;
                        } else {
                            lap = topModel1.getLap();
                        }
                    }
                }

                if (findPreviousTop(carNumber).getTopType().equals("I") && (type.equals("I") || type.equals("R"))) {
                    topModel = new TopModel(carNumber, dateTime, "O", lap, lapTime, comment + "-Top O système");
                    decrementPanel(lapTime);
                } else if (!findPreviousTop(carNumber).getTopType().equals("I") && type.equals("O")) {
                    topModel = new TopModel(carNumber, dateTime, "R", lap, lapTime, comment + "-Top R système");
                    decrementPanel(lapTime);
                }
                handleTopLapRace(topModel, carNumber);
            }
        }
        carModel.getTopList().add(topModel);
        loadData(topModel);
        //Save Top list of Object to persist
        raceModel.getTopsMap().get(carNumber).add(topModel);
        saveTopModel(topModel);


        //CHECK THE END OF THE RACE

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
        topComment.clear();

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

    private void handleTopTimeRace(TopModel topModel, int carNumber) {
        topModels.add(topModel);
        if (raceModel.getTopsMap().containsKey(carNumber)) {
            raceModel.getTopsMap().get(carNumber).add(topModel);
        } else {
            raceModel.getTopsMap().put(carNumber, new ArrayList<>());
            raceModel.getTopsMap().get(carNumber).add(topModel);
        }
    }

    private void handleTopLapRace(TopModel topModel, int carNumber) {
        topModels.add(topModel);
        if (raceModel.getTopsMap().containsKey(carNumber)) {
            raceModel.getTopsMap().get(carNumber).add(topModel);
        } else {
            raceModel.getTopsMap().put(carNumber, new ArrayList<>());
            raceModel.getTopsMap().get(carNumber).add(topModel);
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

        /*col_racetime.setCellFactory(TextFieldTableCell.forTableColumn());
        col_racetime.setOnEditCommit(this::editRaceTime);*/

        /*colLapNumber.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colLapNumber.setOnEditCommit(this::editLap);*/

        col_comment.setCellFactory(TextFieldTableCell.forTableColumn());
        col_comment.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setComment(e.getNewValue());
        });

    }

    private void editLap(TableColumn.CellEditEvent<TopModel, Integer> event) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        int row = event.getTableView().getSelectionModel().selectedIndexProperty().get();
        int carNumber = event.getTableView().getItems().get(row).getCarNumber();
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        Integer oldLap = event.getOldValue();
        Integer newLap = event.getNewValue();
        long topId = event.getTableView().getItems().get(row).getId();
        int index = findTopIndexWithId(carNumber, topId);
        TopModel top = topModels.get(index);
        top.setLap(newLap);
        int newPos = findTopNewPositionOnLapChange(topModels, index, oldLap, newLap);
        if (newPos < topModels.size() - 1) {
            topModels.remove(index);
            topModels.add(newPos, top);
            top = topModels.get(newPos);
            if (newPos == 0) {
                top.setTime(df.format(raceModel.getStartingTime()));
            } else {
                top.setTime(topModels.get(newPos + 1).getTime());
            }
            updateTopLogic(carNumber, newPos, false);
            recalculateLapTime(topModels);
        } else {
            topModels.add(top);
            if (newPos == 0) {
                top.setTime(df.format(raceModel.getStartingTime()));
            } else {
                top.setTime(df.format(System.currentTimeMillis()));
            }
            updateTopLogic(carNumber, newPos, false);
            recalculateLapTime(topModels);
        }
        colLapNumber.setSortType(TableColumn.SortType.DESCENDING);
        table_info.getSortOrder().remove(col_time);
        table_info.getSortOrder().add(colLapNumber);
        table_info.sort();
        table_info.refresh();
        table_info.getSortOrder().remove(colLapNumber);
        table_info.getSortOrder().add(col_time);
    }

    private void editRaceTime(TableColumn.CellEditEvent<TopModel, String> event) {
        int row = event.getTableView().getSelectionModel().selectedIndexProperty().get();
        int carNumber = event.getTableView().getItems().get(row).getCarNumber();
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        long topId = event.getTableView().getItems().get(row).getId();
        int index = findTopIndexWithId(carNumber, topId);
        TopModel top = topModels.get(index);
        String oldRaceTime = event.getOldValue();
        String newRaceTime = event.getNewValue();
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");
        int newPos = 0;
        top.setRaceTime(newRaceTime);
        try {
            Date oldTopTimeMillis = df1.parse(top.getTime());
            Date oldRaceTimeMillis = df2.parse(oldRaceTime);
            Date newRaceTimeMillis = df2.parse(newRaceTime);
            long newTopTimeMillis = oldTopTimeMillis.getTime() + (newRaceTimeMillis.getTime() - oldRaceTimeMillis.getTime());
            String newTopTime = df1.format(newTopTimeMillis);
            String oldTopTime = top.getTime();
            if (newTopTimeMillis < raceModel.getStartingTime().getTime()) {
                top.setRaceTime("00:00:00");
                top.setTime(df1.format(raceModel.getStartingTime()));
                newPos = findTopNewPositionOnRaceTimeChange(topModels, index, oldRaceTime, "00:00:00");
            } else if (newTopTimeMillis > System.currentTimeMillis()) {
                long currentRaceTime = System.currentTimeMillis() - raceModel.getStartingTime().getTime();
                top.setRaceTime(df2.format(currentRaceTime));
                top.setTime(df1.format(System.currentTimeMillis()));
                newPos = findTopNewPositionOnRaceTimeChange(topModels, index, oldRaceTime, df2.format(currentRaceTime));
            } else {
                top.setRaceTime(newRaceTime);
                top.setTime(newTopTime);
                newPos = findTopNewPositionOnRaceTimeChange(topModels, index, oldRaceTime, newRaceTime);

            }
            if (newPos < topModels.size()) {
                topModels.remove(index);
                topModels.add(newPos, top);
                updateTopLogic(carNumber, newPos, false);

            } else {
                topModels.remove(index);
                topModels.add(top);
                updateTopLogic(carNumber, newPos, false);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        col_racetime.setSortType(TableColumn.SortType.DESCENDING);
        table_info.getSortOrder().remove(col_time);
        table_info.getSortOrder().add(col_racetime);
        table_info.sort();
        table_info.refresh();
        table_info.getSortOrder().remove(col_racetime);
        table_info.getSortOrder().add(col_time);
        recalculateTopTime(topModels, newPos, oldRaceTime, newRaceTime, true);
        recalculateLapTime(topModels);
        table_info.refresh();
    }

    private void editLapTime(TableColumn.CellEditEvent<TopModel, String> event) {
        try {
            int row = event.getTableView().getSelectionModel().selectedIndexProperty().get();
            int carNumber = event.getTableView().getItems().get(row).getCarNumber();
            ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
            long topId = event.getTableView().getItems().get(row).getId();
            int index = findTopIndexWithId(carNumber, topId);
            TopModel top = topModels.get(index);
            String oldLapTime = event.getOldValue();
            String newLapTime = event.getNewValue();
            String oldTopTime = top.getTime();
            SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            SimpleDateFormat df2 = new SimpleDateFormat("mm:ss:SS");
            if (index > 0) {
                int i = index;
                while (i < topModels.size() && i != 0) {
                    recalculateTopTime(topModels, i, oldLapTime, newLapTime, false);
                    if (raceModel instanceof TimeRaceModel) {
                        recalculateRaceTime(topModels, i);
                    }
                    i++;
                }
                top = topModels.get(index);
                Calendar previousTopTimeCalendar = Calendar.getInstance();
                TopModel previousTop = topModels.get(index - 1);
                Calendar newLapTimeCalendar = Calendar.getInstance();
                previousTopTimeCalendar.setTime(df1.parse(previousTop.getTime()));
                newLapTimeCalendar.setTime(df2.parse(newLapTime));
                previousTopTimeCalendar.add(Calendar.HOUR_OF_DAY, newLapTimeCalendar.get(Calendar.HOUR_OF_DAY));
                previousTopTimeCalendar.add(Calendar.MINUTE, newLapTimeCalendar.get(Calendar.MINUTE));
                previousTopTimeCalendar.add(Calendar.SECOND, newLapTimeCalendar.get(Calendar.SECOND));
                previousTopTimeCalendar.add(Calendar.MILLISECOND, newLapTimeCalendar.get(Calendar.MILLISECOND));

                if (previousTopTimeCalendar.getTime().getTime() > System.currentTimeMillis()) {
                    recalculateLapTime(topModels);
                } else {
                    top.setLapTime(newLapTime);
                }
                table_info.refresh();
            } else {
                table_info.refresh();
                Alerts.error("ERREUR", "Top initial: temps au tour non modifiable");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            table_info.refresh();
            Alerts.error("ERREUR", "Format de temps à respecter: mm:ss:mm");
        }
    }

    private void recalculateTopTime(ArrayList<TopModel> topModels, int index, String oldTime, String newTime, boolean useRaceTime) {
        SimpleDateFormat df1 = null;
        if (useRaceTime) {
            df1 = new SimpleDateFormat("HH:mm:ss");
        } else {
            df1 = new SimpleDateFormat("mm:ss:SS");
        }
        try {
            Calendar baseCalendar = Calendar.getInstance();
            Calendar newTimeCalendar = Calendar.getInstance();
            Calendar oldTimeCalendar = Calendar.getInstance();
            Date currentTime = df1.parse(newTime);
            Date formerTime = df1.parse(oldTime);
            SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date topTime = df2.parse(topModels.get(index).getTime());
            long newTopTimeInMillis = 0;
            if (useRaceTime) {
                baseCalendar.setTime(raceModel.getStartingTime());
                newTimeCalendar.setTime(currentTime);
                baseCalendar.add(Calendar.HOUR_OF_DAY, newTimeCalendar.get(Calendar.HOUR_OF_DAY));
                baseCalendar.add(Calendar.MINUTE, newTimeCalendar.get(Calendar.MINUTE));
                baseCalendar.add(Calendar.SECOND, newTimeCalendar.get(Calendar.SECOND));
            } else {
                baseCalendar.setTime(topTime);
                newTimeCalendar.setTime(currentTime);
                oldTimeCalendar.setTime(formerTime);
                if (newTimeCalendar.getTime().after(oldTimeCalendar.getTime())) {
                    newTimeCalendar.add(Calendar.MINUTE, -oldTimeCalendar.get(Calendar.MINUTE));
                    newTimeCalendar.add(Calendar.SECOND, -oldTimeCalendar.get(Calendar.SECOND));
                    newTimeCalendar.add(Calendar.MILLISECOND, -oldTimeCalendar.get(Calendar.MILLISECOND));
                    baseCalendar.add(Calendar.MINUTE, newTimeCalendar.get(Calendar.MINUTE));
                    baseCalendar.add(Calendar.SECOND, newTimeCalendar.get(Calendar.SECOND));
                    baseCalendar.add(Calendar.MILLISECOND, newTimeCalendar.get(Calendar.MILLISECOND));
                } else if (newTimeCalendar.getTime().before(oldTimeCalendar.getTime())) {
                    oldTimeCalendar.add(Calendar.MINUTE, -newTimeCalendar.get(Calendar.MINUTE));
                    oldTimeCalendar.add(Calendar.SECOND, -newTimeCalendar.get(Calendar.SECOND));
                    oldTimeCalendar.add(Calendar.MILLISECOND, -newTimeCalendar.get(Calendar.MILLISECOND));
                    baseCalendar.add(Calendar.MINUTE, -oldTimeCalendar.get(Calendar.MINUTE));
                    baseCalendar.add(Calendar.SECOND, -oldTimeCalendar.get(Calendar.SECOND));
                    baseCalendar.add(Calendar.MILLISECOND, -oldTimeCalendar.get(Calendar.MILLISECOND));
                }
            }
            if (baseCalendar.getTime().getTime() > System.currentTimeMillis()) {
                topModels.get(index).setTime(df2.format(System.currentTimeMillis()));
            } else {
                topModels.get(index).setTime(df2.format(baseCalendar.getTime()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void recalculateRaceTime(ArrayList<TopModel> topModels, int index) {
        String raceTime = "";
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date currentTopTime = df1.parse(topModels.get(index).getTime());
            Date raceStartTime = raceModel.getStartingTime();
            long newRaceTimeInMillis = currentTopTime.getTime() - raceStartTime.getTime();
            String sec = Integer.toString((int) (newRaceTimeInMillis / 1000) % 60);
            String min = Integer.toString((int) ((newRaceTimeInMillis / (1000 * 60)) % 60));
            String hr = Integer.toString((int) ((newRaceTimeInMillis / (1000 * 60 * 60)) % 24));
            if (Integer.parseInt(sec) < 10) {
                sec = "0" + sec;
            }
            if (Integer.parseInt(min) < 10) {
                min = "0" + min;
            }
            if (Integer.parseInt(hr) < 10) {
                hr = "0" + hr;
            }
            raceTime = hr + ":" + min + ":" + sec;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        topModels.get(index).setRaceTime(raceTime);
    }

    private void recalculateLapTime(ArrayList<TopModel> topModels) {
        String newLapTime = "00:00:00";
        int index = 0;
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        while (index < topModels.size()) {
            try {
                Date previousTopTime;
                Date currentTopTime = df1.parse(topModels.get(index).getTime());
                if (index > 0) {
                    previousTopTime = df1.parse(topModels.get(index - 1).getTime());
                    long newLapTimeMillis = currentTopTime.getTime() - previousTopTime.getTime();

                    String ms = Integer.toString((int) newLapTimeMillis % 1000);
                    String sec = Integer.toString((int) (newLapTimeMillis / 1000) % 60);
                    String min = Integer.toString((int) ((newLapTimeMillis / (1000 * 60)) % 60));
                    if (Integer.parseInt(sec) < 10) {
                        sec = "0" + sec;
                    }
                    if (Integer.parseInt(min) < 10) {
                        min = "0" + min;
                    }
                    if (Integer.parseInt(ms) < 10) {
                        ms = "0" + ms;
                    }
                    newLapTime = min + ":" + sec + ":" + ms;
                    topModels.get(index).setLapTime(newLapTime);

                } else if (!topModels.get(index).getLapTime().equals("00:00:00")) {
                    topModels.get(index).setLapTime("00:00:00");
                    topModels.get(index).setComment("-First Top O time is always 00:00:00");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            index++;
        }
    }

    private void recalculateLaps(ArrayList<TopModel> topModels, int carNumber) {
        int i = 0;
        int numTour = 0;
        int oldLap = topModels.get(topModels.size() - 1).getLap();
        if (raceModel instanceof LapRace) {
            while (i < topModels.size()) {
                if (i > 0 && topModels.get(i).getTopType().equals("I")) {
                    topModels.get(i).setLap(numTour);
                } else if (i > 0) {
                    numTour++;
                    topModels.get(i).setLap(numTour);
                }
                i++;
            }
            if (carNumber == numberMainCar) {
                int newLap = topModels.get(topModels.size() - 1).getLap();
                numberOfLapsDone = topModels.get(topModels.size() - 1).getLap();
                spentTime.setText(Integer.toString(numberOfLapsDone));
                remainingLaps = ((LapRace) raceModel).getNumberOfLaps() - numberOfLapsDone;
                remainingTime.setText(Integer.toString(remainingLaps));
                if (oldLap < newLap) {
                    decrementPanel(topModels.get(topModels.size() - 1).getLapTime());
                } else {
                    incrementPanel(topModels.get(topModels.size() - 1).getLapTime());
                }
            }
        }

    }

    /**
     * @param e
     */
    private void editTopType(TableColumn.CellEditEvent<TopModel, String> e) {
        int row = e.getTableView().getSelectionModel().selectedIndexProperty().get();
        int carNumber = e.getTableView().getItems().get(row).getCarNumber();
        long topId = e.getTableView().getItems().get(row).getId();
        String oldTopType = e.getOldValue();
        String newTopType = e.getNewValue();
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        if (!oldTopType.equals(newTopType)) {
            if ((newTopType.equals("I") || newTopType.equals("O") || newTopType.equals("R"))) {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setTopType(newTopType);
                if (!checkTopLogicOnEdit(carNumber, topId)) {
                    updateTopLogic(carNumber, findTopIndexWithId(carNumber, topId), false);
                }
                if (raceModel instanceof LapRaceModel) {
                    recalculateLaps(topModels, carNumber);
                } else {
                    if (carNumber == numberMainCar) {
                        if (row != table_info.getItems().size() - 1 && !newTopType.equals("R")) {
                            numberOfLapsDone++;
                            incrementPanel(topModels.get(topModels.size() - 1).getLapTime());
                        } else if (row != table_info.getItems().size() - 1) {
                            numberOfLapsDone--;
                            decrementPanel(topModels.get(topModels.size() - 1).getLapTime());

                        }
                    }
                }
                table_info.refresh();
            } else {
                table_info.refresh();
                Alerts.error("ERREUR", "Type de top invalide");
            }
        }
    }

    private void editCarNumber(TableColumn.CellEditEvent<TopModel, Integer> event) {
        try {
            int row = event.getTableView().getSelectionModel().selectedIndexProperty().get();
            int oldCarNumber = event.getOldValue();
            int newCarNumber = event.getNewValue();
            long topId = event.getTableView().getItems().get(row).getId();
            int index = findTopIndexWithId(oldCarNumber, topId);
            boolean carExists = carExists(newCarNumber);
            ArrayList<TopModel> topModels = raceModel.getTopsMap().get(newCarNumber);
            if (index > 0) {
                if (carExists) {
                    TopModel top = findTop(oldCarNumber, topId);
                    removeTop(oldCarNumber, topId);
                    top.setCarNumber(newCarNumber);
                    int newPos = findTopNewPositionOnCarNumberChange(topModels, top.getTime());
                    if (newPos < topModels.size()) {
                        topModels.add(newPos, top);
                    } else {
                        topModels.add(top);
                    }
                    updateTopLogic(newCarNumber, newPos, false);
                    table_info.refresh();
                } else {
                    table_info.refresh();
                    Alerts.error("ERREUR", "Cette voiture n'existe pas");
                }
            } else {
                table_info.refresh();
                Alerts.error("ERREUR", "Top initial : modification numéro voiture impossible");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            table_info.refresh();
            Alerts.error("ERREUR", "Seuls les nombres entiers sont autorisés");
        }

    }

    /**
     * @param event
     */
    private void editTopTime(TableColumn.CellEditEvent<TopModel, String> event) {
        try {
            int row = event.getTableView().getSelectionModel().selectedIndexProperty().get();
            int carNumber = event.getTableView().getItems().get(row).getCarNumber();
            ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
            long topId = event.getTableView().getItems().get(row).getId();
            int index = findTopIndexWithId(carNumber, topId);
            String oldTopTime = event.getOldValue();
            String newTopTime = event.getNewValue();
            TopModel top = topModels.get(index);
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            if (index > 0) {
                long newTopTimeMillis = df.parse(newTopTime).getTime();
                long raceStartTimeMillis = raceModel.getStartingTime().getTime();
                if (newTopTimeMillis < raceStartTimeMillis) {
                    top.setTime(df.format(raceStartTimeMillis));
                    top.setComment("-Heure du top doit être supérieure à heure de départ");
                } else if (newTopTimeMillis > System.currentTimeMillis()) {
                    top.setComment("-Heure du top doit être inférieure à heure actuelle");
                } else {
                    top.setTime(newTopTime);
                }
                int newPos = findTopNewPositionOnTopTimeChange(topModels, index, oldTopTime, newTopTime);
                if (newPos < topModels.size()) {
                    topModels.remove(index);
                    topModels.add(newPos, top);
                    updateTopLogic(carNumber, newPos, false);

                } else {
                    topModels.remove(index);
                    topModels.add(top);
                    updateTopLogic(carNumber, newPos, false);
                }

                top = topModels.get(newPos);
                recalculateLapTime(topModels);
                if (raceModel instanceof LapRaceModel) {
                    recalculateLaps(topModels, carNumber);
                } else {
                    recalculateRaceTime(topModels, newPos);
                }
                table_info.refresh();
                table_info.getSortOrder().add(col_time);
                table_info.sort();
                table_info.getSortOrder().remove(col_time);
            } else {
                table_info.refresh();
                Alerts.error("ERROR", "Top initial: modification du temps du top impossible");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            table_info.refresh();
            Alerts.error("ERREUR", "Format de date à respecter: jj-mm-aaaa hh:mm:ss");
        }

    }

    private void updateTopLogic(int carNumber, int origin, boolean onRemove) {
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        int index = origin;
        if (origin > 1) {
            while (index > 1) {
                TopModel currentTop = topModels.get(index);
                TopModel previousTop = topModels.get(index - 1);
                if (currentTop.getTopType().equals("I") && (!previousTop.getTopType().equals("O") && !previousTop.getTopType().equals("R"))) {
                    previousTop.setTopType("R");
                    previousTop.setComment("-Top R système-" + previousTop.getComment());
                } else if (currentTop.getTopType().equals("O") && !previousTop.getTopType().equals("I")) {
                    previousTop.setTopType("I");
                    previousTop.setComment("-Top I système-" + previousTop.getComment());
                    if (raceModel instanceof LapRace) {
                        previousTop.setLap(currentTop.getLap() - 1);
                    }
                } else if (currentTop.getTopType().equals("R") && (!previousTop.getTopType().equals("O") && !previousTop.getTopType().equals("R"))) {
                    previousTop.setTopType("R");
                    previousTop.setComment("-Top R système-" + previousTop.getComment());
                }
                index--;
            }
        } else {
            if (origin == 0) {
                topModels.get(0).setTopType("O");
                if (!onRemove) {
                    topModels.get(0).setComment("-Top O système" + topModels.get(0).getComment());
                }
            } else {
                if (!topModels.get(1).getTopType().equals("I") || !topModels.get(1).getTopType().equals("R")) {
                    topModels.get(1).setTopType("R");
                    topModels.get(1).setComment("-Top R système" + topModels.get(1).getComment());
                }
            }
        }
        index = origin + 1;
        if (origin < topModels.size() - 1) {
            while (index < topModels.size()) {
                TopModel currentTop = topModels.get(index);
                TopModel previousTop = topModels.get(index - 1);
                if (currentTop.getTopType().equals("I") && (!previousTop.getTopType().equals("O") && !previousTop.getTopType().equals("R"))) {
                    currentTop.setTopType("O");
                    currentTop.setComment("-Top O système-" + currentTop.getComment());
                } else if (currentTop.getTopType().equals("O") && !previousTop.getTopType().equals("I")) {
                    currentTop.setTopType("R");
                    currentTop.setComment("-Top R système-" + currentTop.getComment());
                } else if (currentTop.getTopType().equals("R") && (!previousTop.getTopType().equals("O") && !previousTop.getTopType().equals("R"))) {
                    currentTop.setTopType("O");
                    currentTop.setComment("-Top O système-" + currentTop.getComment());
                }
                index++;
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
            if (respectsLogic && index < tops.size() - 1) {
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
        if (index != 0) {
            TopModel currentTop = topModels.get(index);
            CarModel carModel = carController.findCar(carModels, carNumber);
            carModel.getTopList().remove(currentTop);
            topModels.remove(currentTop);
            updateTopLogic(carNumber, 0, true);
            recalculateLapTime(topModels);
            if (raceModel instanceof LapRace) {
                recalculateLaps(topModels, carNumber);
            } else {
                if (carModel instanceof MainCarModel) {
                    if (!currentTop.getTopType().equals("I")) {
                        numberOfLapsDone--;
                        incrementPanel(topModels.get(topModels.size() - 1).getLapTime());
                    }
                }
            }
            table_info.refresh();
            System.out.println("Le top " + topId + "  a été supprimé");
        } else {
            table_info.refresh();
            Alerts.error("ERREUR", "Impossible de supprimer le top initial");
        }
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
     * @param topModels
     * @param index
     * @param oldTime
     * @param newTime
     * @return
     */
    private int findTopNewPositionOnTopTimeChange(ArrayList<TopModel> topModels, int index, String oldTime, String newTime) {
        int i = index;
        boolean found = false;
        if (LocalDateTime.parse(oldTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isBefore(LocalDateTime.parse(newTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
            TopModel newTop = topModels.get(index);
            i++;
            while (i < topModels.size() && !found) {
                if (LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isBefore(LocalDateTime.parse(newTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))) || LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isEqual(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
                    found = true;
                } else {
                    i++;
                }
            }
            i--;
        } else if (LocalDateTime.parse(oldTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isAfter(LocalDateTime.parse(newTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
            TopModel newTop = topModels.get(index);
            i--;
            while (i >= 0 && !found) {
                if ((LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isAfter(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) || LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isEqual(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
                    found = true;
                } else {
                    i--;
                }
            }
            i++;
        }
        return i;
    }

    /**
     * @param topModels
     * @param index
     * @param oldTime
     * @param newTime
     * @return
     */
    private int findTopNewPositionOnRaceTimeChange(ArrayList<TopModel> topModels, int index, String oldTime, String newTime) {
        int i = index;
        boolean found = false;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        try {
            if (df.parse(oldTime).before(df.parse(newTime))) {
                TopModel newTop = topModels.get(index);
                i++;
                while (i < topModels.size() && !found) {
                    if (df.parse(newTop.getRaceTime()).before(df.parse(topModels.get(i).getRaceTime())) || df.parse(newTop.getRaceTime()).equals(df.parse(topModels.get(i).getRaceTime()))) {
                        found = true;
                    } else {
                        i++;
                    }
                }
                i--;
            } else if (df.parse(oldTime).after(df.parse(newTime))) {
                TopModel newTop = topModels.get(index);
                i--;
                while (i >= 0 && !found) {
                    if (df.parse(newTop.getRaceTime()).after(df.parse(topModels.get(i).getRaceTime())) || df.parse(newTop.getRaceTime()).equals(df.parse(topModels.get(i).getRaceTime()))) {
                        found = true;
                    } else {
                        i--;
                    }
                }
                i++;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * @param topModels
     * @param index
     * @param oldLap
     * @param newLap
     * @return
     */
    private int findTopNewPositionOnLapChange(ArrayList<TopModel> topModels, int index, int oldLap, int newLap) {
        int i = index;
        boolean found = false;
        if (newLap == 0) {
            i = 1;
        } else {
            if (oldLap < newLap) {
                i++;
                while (i < topModels.size() && !found) {
                    if (newLap < topModels.get(i).getLap() || newLap == topModels.get(i).getLap()) {
                        found = true;
                    } else {
                        i++;
                    }
                }
                i--;
            } else if (oldLap > newLap) {
                i--;
                while (i >= 0 && !found) {
                    if (newLap > topModels.get(i).getLap()) {
                        found = true;
                    } else {
                        i--;
                    }
                }
                i++;
            }
        }
        return i;
    }

    private int findTopNewPositionOnCarNumberChange(ArrayList<TopModel> topModels, String topTime) {
        boolean found = false;
        int i = 0;
        while (i < topModels.size() && !found) {
            if (LocalDateTime.parse(topTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isBefore(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))) || LocalDateTime.parse(topTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isEqual(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
                found = true;
            } else {
                i++;
            }
        }
        return i;
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
        if (!raceModel.getRaceState().equals(RaceState.DONE)) {
            istartRace = true;
            startRace.setDisable(true);
            if (raceModel instanceof TimeRace) {
                if (!localRemainningTime.equals(LocalTime.parse("00:00:00"))) {
                    departureTime = LocalTime.now();
                    LocalDateTime localDateTime = LocalDateTime.now();
                    Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    raceModel.setStartingTime(date);
                    spentTimeline.play();
                    remainingTimeline.play();
                    departureHour.setText(currentTime.format(dtf));
                }
            } else {
                departureTime = LocalTime.now();
                LocalDateTime localDateTime = LocalDateTime.now();
                Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                raceModel.setStartingTime(date);
                departureHour.setText(departureTime.format(dtf));
                startRace.setDisable(true);
            }
            if (raceModel.getRaceState().equals(RaceState.BREAK)) {
                handleNewTop();
                if (getFollowedCars().size() >= 2)
                    startTimerForRivalCar();
                setRaceInformations(RaceState.IN_PROGRESS);
            } else {
                handleNewTop();
                if (getFollowedCars().size() >= 2) {
                    handleNewTopForRivalCar();
                    startTimerForRivalCar();
                }
                setRaceInformations(RaceState.IN_PROGRESS);
            }
            topType.setDisable(false);
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
                remainingTime.setText(String.valueOf(remainingLaps));
                spentTime.setText(String.valueOf(numberOfLapsDone));
                setRaceInformations(RaceState.DONE);
                endAllThread();
                Alerts.info("Information", "la course est terminée");


            } else {

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
        if (mycar != null) {
            lastNamePilotMainCar.setText(mycar.getPilotModel().getLastName());
            firstNamePilotMainCar.setText(mycar.getPilotModel().getFirstName());
            if (!(mycar.getPilotModel().getDateOfBirth() == null)) {
                dateOfBirthPilot.setText(new SimpleDateFormat("dd-MM-yyyy").format(mycar.getPilotModel().getDateOfBirth()));
            }
            mainCarBrand.setText(mycar.getBrand());
            mainCarModel.setText(mycar.getModel());
            mainCarTeam.setText(mycar.getTeam());
        }

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
        hours=0;
        chronoTime = LocalTime.of(0, munites, secondes, millisecondes);
        chronoTopTime.setText(chronoTime.format(dtf2));
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
                    if(munites==60){
                        hours++;
                        munites=0;
                    }

                    Platform.runLater(() -> {
                        chronoTime = LocalTime.of(hours, munites, secondes, millisecondes);
                        chronoTopTime.setText(chronoTime.format(dtf2));
                        labelMeanTime.setText(timebar.format(dtf));
                        if(hours>0)
                            chronoTopTime.setText(chronoTime.format(dtf1));
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

    @Override
    public void update() {

    }
}
