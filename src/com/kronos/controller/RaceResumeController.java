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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RaceResumeController implements Initializable, Observer {


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


    private static ArrayList<Double> listOfMeanTime = new ArrayList<>();
    private static Double meantime = 0.00;
    PulseTransition pulseTransition;
    MainCarModel mycar;
    private LapRaceModel lapRaceModel;
    private RaceModel raceModel;
    private CarController carController = new CarController();
    private ArrayList<TopModel> topModels = new ArrayList<>();
    private ArrayList<CarModel> carModels = new ArrayList<>();
    private boolean isExtancier = true, breakThread = true;
    private Thread thread, threadChrono;
    private int munites = 0, secondes = 0, millisecondes = 0, decimalpartTosecond = 0, intergerpart = 0, numberOfLapsDone = 0, remainingLaps;
    private boolean isStartTimer, isSetTimerBar = true, istartRace = false, timerIsInitialize = true, firstTop = true;
    private double decimalpart = 0.0, lapTimeForMeanTime = 0.0;
    private Timeline spentTimeline;
    private Timeline remainingTimeline;
    private LocalTime localRemainningTime = LocalTime.parse("00:00:00");
    private LocalTime localSpentTime = LocalTime.parse("00:00:00");
    private LocalTime time2 = LocalTime.parse("00:00");
    private LocalTime timebar = LocalTime.parse("00:00:00");
    private LocalTime chronoTime = LocalTime.parse("00:00:00");
    private LocalTime timeTocompare = LocalTime.parse("00:00:00");
    private LocalTime currentTime;
    private LocalTime departureTime;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    private DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("mm:ss:nn");


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
        topType.setDisable(true);
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
        List<TopModel> topModelList = (List<TopModel>) (List<?>) App.getDataManager().getModels(TopModel.class);
        if (!topModelList.isEmpty()) {
            for (TopModel topModel : topModelList) {
                loadData(topModel);
                int muniteteToLoad = Integer.parseInt(topModel.getLapTime().substring(0, 2));
                int secondeToLoad = Integer.parseInt(topModel.getLapTime().substring(3, 5));
                int milliToLoad = Integer.parseInt(topModel.getLapTime().substring(6, 8));
                lapTimeForMeanTime = (muniteteToLoad + (secondeToLoad / 60.0) + (milliToLoad / 100) / 60.0);
                listOfMeanTime.add(lapTimeForMeanTime);
            }
        }
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
        } else {
            Alerts.info("INFORMATION", "veuillez demarrer/continuer la course ");
        }
    }



    /**
     *
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
            startTimerBar();
        } else {

            lapTime = chronoTime.format(dtf2);
            lapTimeForMeanTime = (munites + (secondes / 60.0) + (millisecondes / 100) / 60.0);
            listOfMeanTime.add(lapTimeForMeanTime);
            resetTimerBar();
        }

        decimalpart = getMeanTime(listOfMeanTime);
        intergerpart = (int) getMeanTime(listOfMeanTime);
        decimalpart = decimalpart - intergerpart;
        decimalpartTosecond = (int) (decimalpart * 60);
        timebar = LocalTime.of(0, intergerpart, decimalpartTosecond);
        labelMeanTime.setText(timebar.format(dtf));

        if (findPreviousTop(carNumber) == null || checkTopLogic(type, findPreviousTop(carNumber).getTopType())) {
            //Case where top respects logical top type order
            if (raceModel instanceof TimeRaceModel) {
                raceTime = localSpentTime.format(dtf);
                topModel = new TopModel(carNumber, dateTime, type, raceTime, lapTime, comment);
                handleTopTimeRace(topModel, carModel, carNumber);
            } else {
                lap = numberOfLapsDone + 1;
                topModel = new TopModel(carNumber, dateTime, type, lap, lapTime, comment);
                handleTopLapRace(topModel, carModel, carNumber);
            }
            loadData(topModel);
            carModel.getTopList().add(topModel);
            if ((carModel instanceof MainCarModel) && (topModel.getTopType().equals("R"))) {
                checkEndOfRace();
                handleMeanTimeBar(1);
            } else {

                handleMeanTimeBar(1);
            }

        } else {
            //Case where top does not respect logical top type order
            if (raceModel instanceof TimeRaceModel) {
                raceTime = localSpentTime.format(dtf);
                if (type.equals("I")) {
                    topModel = new TopModel(carNumber, dateTime, "O", raceTime, lapTime, comment + "-Top O système");
                } else if (type.equals("R") || type.equals("O")) {
                    topModel = new TopModel(carNumber, dateTime, "R", raceTime, lapTime, comment + "-Top R système");
                }
                handleTopTimeRace(topModel, carModel, carNumber);
            } else {
                lap = numberOfLapsDone + 1;
                if (type == "I") {
                    topModel = new TopModel(carNumber, dateTime, "O", lap, lapTime, comment + "-Top O système");
                } else if (type == "R" || type == "O") {
                    topModel = new TopModel(carNumber, dateTime, "R", lap, lapTime, comment + "-Top R système");
                }
                handleTopLapRace(topModel, carModel, carNumber);
            }
            loadData(topModel);
            System.out.println(topModel.getLap());
            carModel.getTopList().add(topModel);
            if ((carModel instanceof MainCarModel) && (topModel.getTopType().equals("R"))) {
                checkEndOfRace();
                handleMeanTimeBar(1);
            } else {
                handleMeanTimeBar(1);
            }
        }
        topComment.clear();


         //*  Save Top list of Object to persist  *//
        App.getDataManager().persist(topModel);
        App.getDataManager().saveFile();
//        System.out.println("==== top =====");
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
        colLapNumber.setOnEditCommit(this::editLap);

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
        if(newPos < topModels.size() - 1) {
            System.out.println("size"+topModels.size());
            topModels.remove(index);
            topModels.add(newPos, top);
            top = topModels.get(newPos);
            if(newPos == 0) {
                top.setTime(df.format(raceModel.getStartingTime()));
            }
            else {
                top.setTime(topModels.get(newPos+1).getTime());
            }
            System.out.println("pos"+newPos);
            updateTopLogic(carNumber, newPos);
            recalculateLapTime(topModels);
        }
        else {
            topModels.add(top);
            if(newPos == 0) {
                top.setTime(df.format(raceModel.getStartingTime()));
            }
            else {
                top.setTime(df.format(System.currentTimeMillis()));
            }
            updateTopLogic(carNumber, newPos);
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

    private void editLapTime(TableColumn.CellEditEvent<TopModel, String> event) {
        int row = event.getTableView().getSelectionModel().selectedIndexProperty().get();
        int carNumber = event.getTableView().getItems().get(row).getCarNumber();
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        long topId = event.getTableView().getItems().get(row).getId();
        int index = findTopIndexWithId(carNumber, topId);
        TopModel top = topModels.get(index);
        String oldLapTime = event.getOldValue();
        String newLapTime = event.getNewValue();
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat df2 = new SimpleDateFormat("mm:ss:SS");
        try {
            int i = index;
            while(i < topModels.size() && i != 0) {
                top = topModels.get(i);
                recalculateTopTime(topModels, i, oldLapTime, newLapTime);
                Date topTime = df1.parse(top.getTime());
                Date previousTopTime = df1.parse(topModels.get(i - 1).getTime());
                Date lapTime = df2.parse(top.getLapTime());
                System.out.println(previousTopTime.getTime());
                System.out.println(top.getTime());
                if(topTime.getTime() > System.currentTimeMillis()) {
                    System.out.println("here");
                    top.setTime(df1.format(System.currentTimeMillis()));
                    topTime = df1.parse(top.getTime());
                    top.setLapTime(df2.format(topTime.getTime() - previousTopTime.getTime()));
                }
                else {
                    top.setLapTime(newLapTime);
                }
                i++;
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        table_info.refresh();
    }

    private void recalculateTopTime(ArrayList<TopModel> topModels, int index, String oldLapTime, String newLapTime) {
        SimpleDateFormat df1 = new SimpleDateFormat("mm:ss:SS");
        try {
            Date currentLapTime = df1.parse(newLapTime);
            Date formerLapTime = df1.parse(oldLapTime);
            SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date topTime = df2.parse(topModels.get(index).getTime());
            long newTopTimeInMillis = topTime.getTime() + (currentLapTime.getTime() - formerLapTime.getTime());
            topModels.get(index).setTime(df2.format(newTopTimeInMillis));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void recalculateRaceTime(ArrayList<TopModel> topModels, int index) {
        String raceTime = "";
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        //df1.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date currentTopTime = df1.parse(topModels.get(index).getTime());
            Date raceStartTime = raceModel.getStartingTime();
            System.out.println(df1.format(raceStartTime));
            long newRaceTimeInMillis = currentTopTime.getTime() - raceStartTime.getTime();
            System.out.println(newRaceTimeInMillis);
            String sec  = Integer.toString((int)(newRaceTimeInMillis/ 1000) % 60) ;
            String min  = Integer.toString((int)((newRaceTimeInMillis/ (1000*60)) % 60));
            String hr   = Integer.toString((int)((newRaceTimeInMillis/ (1000*60*60)) % 24));
            if(Integer.parseInt(sec) < 10) {
                sec = "0"+sec;
            }
            if(Integer.parseInt(min) < 10) {
                min = "0"+min;
            }
            if(Integer.parseInt(hr) < 10) {
                hr = "0"+hr;
            }
            raceTime = hr+":"+min+":"+sec;
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        topModels.get(index).setRaceTime(raceTime);
    }

    private void recalculateLapTime(ArrayList<TopModel> topModels) {
        String newLapTime = "00:00:00";
        int index = 0;
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        while(index < topModels.size()) {
            try {
                Date previousTopTime;
                Date currentTopTime = df1.parse(topModels.get(index).getTime());
                if(index > 0) {
                    previousTopTime = df1.parse(topModels.get(index - 1).getTime());
                    long newLapTimeMillis = currentTopTime.getTime() - previousTopTime.getTime();

                    String ms = Integer.toString((int) newLapTimeMillis % 1000);
                    String sec  = Integer.toString((int)(newLapTimeMillis/ 1000) % 60) ;
                    String min  = Integer.toString((int)((newLapTimeMillis/ (1000*60)) % 60));
                    if(Integer.parseInt(sec) < 10) {
                        sec = "0"+sec;
                    }
                    if(Integer.parseInt(min) < 10) {
                        min = "0"+min;
                    }
                    if(Integer.parseInt(ms) < 10) {
                        ms = "0"+ms;
                    }
                    newLapTime = min+":"+sec+":"+ms;
                    topModels.get(index).setLapTime(newLapTime);

                }
                else if(!topModels.get(index).getLapTime().equals("00:00:00")) {
                    topModels.get(index).setLapTime("00:00:00");
                    topModels.get(index).setComment("-First Top O time is always 00:00:00");
                }

            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            index++;
        }
    }

    private void recalculateLaps(ArrayList<TopModel> topModels) {
        int i = 0;
        while(i < topModels.size()) {
            topModels.get(i).setLap(i);
            i++;
            if(i < topModels.size() && topModels.get(i).getTopType().equals("I")) {
                System.out.println("ici");
                System.out.println(i - 1);
                topModels.get(i - 1).setLap(i - 1);
            }
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
                updateTopLogic(carNumber, findTopIndexWithId(carNumber, lastTopId));
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
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(newCarNumber);;
        if (carExists) {
            TopModel top = findTop(oldCarNumber, topId);
            removeTop(oldCarNumber, topId);
            top.setCarNumber(newCarNumber);
            int newPos = findTopNewPositionOnCarNumberChange(topModels, top.getTime());
            if(newPos < topModels.size()) {
                System.out.println("Indice1:"+newPos);
                topModels.add(newPos, top);
            }
            else {
                System.out.println("Indice2:"+newPos);
                topModels.add(top);
            }
            updateTopLogicForOtherFields(newCarNumber, newPos);
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
        System.out.println("index" + index);
        String oldTopTime = event.getOldValue();
        String newTopTime = event.getNewValue();
        TopModel top = topModels.get(index);
        String lapTime = top.getLapTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        //df.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long newTopTimeMillis = df.parse(newTopTime).getTime();
            long raceStartTimeMillis = raceModel.getStartingTime().getTime();
            if(newTopTimeMillis < raceStartTimeMillis) {
                top.setTime(df.format(raceStartTimeMillis));
                top.setComment("-Heure du top doit être supérieure à heure de départ");
            }
            else if(newTopTimeMillis > System.currentTimeMillis()) {
                top.setComment("-Heure du top doit être inférieure à heure actuelle");
            }
            else {
                top.setTime(newTopTime);
            }

        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        int newPos = findTopNewPositionOnTimeChange(topModels, index, oldTopTime, newTopTime);
        if(newPos < topModels.size()) {
            topModels.remove(index);
            topModels.add(newPos, top);
            updateTopLogic(carNumber, newPos);

        }
        else {
            topModels.remove(index);
            topModels.add(top);
            updateTopLogic(carNumber, newPos);
        }

        top = topModels.get(newPos);
        recalculateLapTime(topModels);
        if(raceModel instanceof LapRaceModel) {
            recalculateLaps(topModels);
        }
        else {
            System.out.println("newpos:"+newPos);
            recalculateRaceTime(topModels, newPos);
        }
        table_info.refresh();
        table_info.sort();

    }

    private void updateTopLogic(int carNumber, int origin) {
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        int index = origin;
        if(origin > 1) {
            while (index > 1) {
                TopModel currentTop = topModels.get(index);
                TopModel previousTop = topModels.get(index - 1);
                if(currentTop.getTopType().equals("I") && (!previousTop.getTopType().equals("O") && !previousTop.getTopType().equals("R"))) {
                    previousTop.setTopType("R");
                    previousTop.setComment("-Top R système-" + previousTop.getComment());
                }
                else if(currentTop.getTopType().equals("O") && !previousTop.getTopType().equals("I")) {
                    previousTop.setTopType("I");
                    previousTop.setComment("-Top I système-" + previousTop.getComment());
                }
                else if(currentTop.getTopType().equals("R") && (!previousTop.getTopType().equals("O") && !previousTop.getTopType().equals("R"))) {
                    previousTop.setTopType("R");
                    previousTop.setComment("-Top R système-" + previousTop.getComment());
                }
                index--;
            }
        }
        else {
            if(origin == 0) {
                topModels.get(0).setTopType("O");
                topModels.get(0).setComment("-Top O système" + topModels.get(0).getComment());
            }
            else {
                if(!topModels.get(1).getTopType().equals("I") || !topModels.get(1).getTopType().equals("R")) {
                    topModels.get(1).setTopType("R");
                    topModels.get(1).setComment("-Top R système" + topModels.get(1).getComment());
                }
            }
        }
        index = origin + 1;
        if(origin < topModels.size() - 1) {
            while (index < topModels.size()) {
                TopModel currentTop = topModels.get(index);
                TopModel previousTop = topModels.get(index - 1);
                if(currentTop.getTopType().equals("I") && (!previousTop.getTopType().equals("O") && !previousTop.getTopType().equals("R"))) {
                    currentTop.setTopType("O");
                    currentTop.setComment("-Top O système-" + currentTop.getComment());
                }
                else if(currentTop.getTopType().equals("O") && !previousTop.getTopType().equals("I")) {
                    currentTop.setTopType("R");
                    currentTop.setComment("-Top R système-" + currentTop.getComment());
                }
                else if(currentTop.getTopType().equals("R") && (!previousTop.getTopType().equals("O") && !previousTop.getTopType().equals("R"))) {
                    currentTop.setTopType("O");
                    currentTop.setComment("-Top O système-" + currentTop.getComment());
                }
                index++;
            }
        }


        /*if (index <= origin && index > 0) {
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
        if(index == 0 && !currentTop.getTopType().equals("O")) {
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
        }*/
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
     * @param topModels
     * @param index
     * @param oldTime
     * @param newTime
     * @return
     */
    private int findTopNewPositionOnTimeChange(ArrayList<TopModel> topModels, int index, String oldTime, String newTime) {
        int i = index;
        boolean found = false;
        if(LocalDateTime.parse(oldTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isBefore(LocalDateTime.parse(newTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
            TopModel newTop = topModels.get(index);
            i++;
            System.out.println("newtop:"+newTop.getTime());
            System.out.println("oldtop:"+topModels.get(index).getTime());
            while(i < topModels.size() && !found) {
                if(LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isBefore(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))) || LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isEqual(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
                    found = true;
                }
                else {
                    i++;
                }
            }
            i--;
        }
        else if(LocalDateTime.parse(oldTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isAfter(LocalDateTime.parse(newTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
            TopModel newTop = topModels.get(index);
            i--;
            while(i >= 0 && !found) {
                if((LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isAfter(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) || LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isEqual(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
                    found = true;
                }
                else {
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
     * @param oldLap
     * @param newLap
     * @return
     */
    private int findTopNewPositionOnLapChange(ArrayList<TopModel> topModels, int index, int oldLap, int newLap) {
        int i = index;
        boolean found = false;
        if(oldLap < newLap) {
            i++;
            while(i < topModels.size() && !found) {
                if(newLap < topModels.get(i).getLap() || newLap == topModels.get(i).getLap()) {
                    found = true;
                }
                else {
                    i++;
                }
            }
            i--;
        }
        else if(oldLap > newLap) {
            i--;
            while(i >= 0 && !found) {
                if(newLap > topModels.get(i).getLap() || newLap == topModels.get(i).getLap()) {
                    found = true;
                }
                else {
                    i--;
                }
            }
            i++;
        }
        return i;
    }


    private int findTopNewPositionOnCarNumberChange(ArrayList<TopModel> topModels, String topTime) {
        boolean found = false;
        int i  = 0;
        while(i < topModels.size() && !found) {
            if(LocalDateTime.parse(topTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isBefore(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))) || LocalDateTime.parse(topTime, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isEqual(LocalDateTime.parse(topModels.get(i).getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
                found = true;
            }
            else {
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

    private void incrementTime() {
        localSpentTime = localSpentTime.plusSeconds(1);
        spentTime.setText(localSpentTime.format(dtf));
    }

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

    private void getCurrentTime() {
        currentTime = LocalTime.now();
        currentHour.setText(currentTime.format(dtf));
    }

    @FXML
    private void startTimer(ActionEvent event) {
        if (raceModel.getRaceState() != RaceState.DONE) {
            istartRace = true;
            setRaceInformations(RaceState.IN_PROGRESS);
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
                System.out.println(date);
                departureHour.setText(departureTime.format(dtf));
                startRace.setDisable(true);
            }
            handleNewTop();
            topType.setDisable(false);
        } else {
            Alerts.info("INFORMATION", "Cette course est terminée");
        }
    }

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
     * setting of the informa
     * tion when it's the end of the Race
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
                remainingLaps--;
                numberOfLapsDone++;
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
        if(mycar !=null) {
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
                        millisecondes = 0; }
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
     * end the different timers
     */

    public void endAllThread() {

        threadChrono.stop();
        thread.stop();
    }

    /**
     * pause all the different stopwatches
     */

    public void breakTimer() throws InterruptedException {
        if (breakThread) {
           thread.interrupt();
           thread.join();
            isStartTimer = false;
            firstTop=false;
            breakThread = false;
        }

    }

    /**
     * restarting the various timers after pausing it .
     */
    public void wakeUpThread() {

        isStartTimer = true;
        firstTop=true;
        startTimerBar();
        breakThread = true;
        chronoTime = LocalTime.of(0, munites, secondes, millisecondes);
        chronoTopTime.setText(chronoTime.format(dtf2));
        if (!(tmierSign.isVisible())) {
            int pastTime = (munites * 60) + secondes;
            handleMeanTimeBar(pastTime);
        }

    }

    /**
     *
     */
    @Override
    public void update() {

    }


}
