package com.kronos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.kronos.App;
import com.kronos.api.Observer;
import com.kronos.api.TimeRace;
import com.kronos.global.animation.PulseTransition;
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
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class RaceResumeController implements Initializable, Observer {

    private static ArrayList<Double> listOfMeanTime = new ArrayList<>();
    private static Double meantime = 0.00;
    @FXML
    private ProgressBar meanTimeBar;
    @FXML
    private Button TopBtn;
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
    private Label labelnano;
    @FXML
    private Label labelsecondes;
    @FXML
    private Label chronoTopTime;
    @FXML
    private Label labelMeanTime;
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
    private TableView<TopModel> table_info;
    @FXML
    private TableColumn<TopModel, Integer> colCarNumber;
    @FXML
    private TableColumn<TopModel, String> col_typetop;
    @FXML
    private TableColumn<TopModel, String> col_comment;
    @FXML
    private TableColumn<TopModel, String> col_time;
    @FXML
    private TableColumn<TopModel, Double> col_racetime;
    @FXML
    private TableColumn<TopModel, Double> col_laptime;
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
    private PulseTransition pulseTransition;
    private MainCarModel mycar;
    private RaceModel raceModel;
    private CarController carController = new CarController();
    private ArrayList<TopModel> topModels = new ArrayList<>();
    private ArrayList<CarModel> carModels = new ArrayList<>();
    private boolean isExtancier = true;
    private Thread thread;
    private Thread threadChrono;
    private Timeline spentTimeline;
    private Timeline remainingTimeline;
    private LocalTime time = LocalTime.parse("00:00:00");
    private LocalTime localRemainningTime = LocalTime.parse("00:00:05");
    private LocalTime time2 = LocalTime.parse("00:00");
    private LocalTime timebar = LocalTime.parse("00:00:00");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    private LocalTime currentTime;
    private LocalTime departureTime;
    private DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("mm:ss:nn");
    private LocalTime chronoTime = LocalTime.parse("00:00:00");
    private LocalTime timeTocompare = LocalTime.parse("00:00:00");
    private int munites = 0, secondes = 0, millisecondes = 0, decimalpartTosecond = 0, intergerpart = 0;
    private boolean isStartTimer, isSetTimerBar, istartRace = false, timerIsInitialize = true, firstTop = true;
    private double decimalpart = 0.0, lapTimeForMeanTime = 0.0;


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
        if (!getRace().isEmpty()) {
            raceModel = getRace().get(0);
        }
        if (raceModel instanceof TimeRace) {
            col_racetime.setVisible(true);
        } else {
            colLapNumber.setVisible(true);
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

        time = LocalTime.parse("00:00:00");
        time2 = LocalTime.parse("00:00");

        departureHour.setText(time2.format(dtf));
        spentTime.setText(time.format(dtf));
        remainingTime.setText(localRemainningTime.format(dtf));

        spentTimeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> incrementTime()));
        spentTimeline.setCycleCount(Animation.INDEFINITE);

        Timeline clock = new Timeline(new KeyFrame(Duration.millis(1000), e -> getCurrentTime()));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        remainingTimeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> decrementTime()));
        remainingTimeline.setCycleCount(Animation.INDEFINITE);

    }

    /**
     * @param event
     */
    @FXML
    public void handleTopButtonClick(ActionEvent event) {
        if (istartRace) {
            handleNewTop();
        } else {
            Alerts.info("INFORMATION", "veuillez demarrer la course ");
        }
    }

    /**
     *
     */
    private void handleNewTop() {


        String type = topType.getSelectionModel().getSelectedItem();
        int carNumber = Integer.parseInt(car.getSelectionModel().getSelectedItem());
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        double raceTime = 0.0;
        String lapTime = "";
        int lap = 0;
        String comment = topComment.getText();
        CarModel carModel = carController.findCar(carModels, carNumber);
        TopModel topModel = null;
        if (timerIsInitialize) {
            timerIsInitialize = false;
        } else {
            stopTimerBar();
            lapTime = chronoTime.format(dtf2);
            lapTimeForMeanTime = (munites + (secondes / 60.0) + (millisecondes / 100) / 60.0);
            listOfMeanTime.add(lapTimeForMeanTime);
            resetTimerBar();
        }
        if (listOfMeanTime.size() == 0) {
            long munite = departureTime.until(currentTime, ChronoUnit.MINUTES);
            long seconde = departureTime.until(currentTime, ChronoUnit.SECONDS);
            lapTime = LocalTime.of(0, (int) munite, (int) (seconde - munite * 60), 0).format(dtf2);
            listOfMeanTime.add(getMeanTime(listOfMeanTime));
        } else if (listOfMeanTime.size() > 1) {
            if (firstTop) {
                firstTop = false;
                long munite = departureTime.until(currentTime, ChronoUnit.MINUTES);
                long seconde = departureTime.until(currentTime, ChronoUnit.SECONDS);
                lapTime = LocalTime.of(0, (int) munite, (int) (seconde - munite * 60), 0).format(dtf2);
                listOfMeanTime.add(munite + ((seconde - munite * 60) / 60.0));
            } else {
                thread.stop();
            }
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
                topModel = new TopModel(carNumber, dateTime, type, raceTime, lapTime, comment);
                handleTopTimeRace(topModel, carModel, carNumber);
            } else {
                topModel = new TopModel(carNumber, dateTime, type, lap, lapTime, comment);
                handleTopLapRace(topModel, carModel, carNumber);
            }
            loadData(topModel);
            carModel.getTopList().add(topModel);
            handleMeanTimeBar();
        } else {
            //Case where top does not respect logical top type order
            if (raceModel instanceof TimeRaceModel) {
                if (type == "I") {
                    topModel = new TopModel(carNumber, dateTime, "O", raceTime, lapTime, comment + "-Top O système");
                } else if (type == "R" || type == "O") {
                    topModel = new TopModel(carNumber, dateTime, "R", raceTime, lapTime, comment + "-Top R système");
                }
                handleTopTimeRace(topModel, carModel, carNumber);
            } else {
                if (type == "I") {
                    topModel = new TopModel(carNumber, dateTime, "O", lap, lapTime, comment + "-Top O système");
                } else if (type == "R" || type == "O") {
                    topModel = new TopModel(carNumber, dateTime, "R", lap, lapTime, comment + "-Top R système");
                }
                handleTopLapRace(topModel, carModel, carNumber);
            }
            loadData(topModel);
            carModel.getTopList().add(topModel);
            handleMeanTimeBar();
        }
        topComment.clear();

        /*  Save Top list of Object to persist  */
        App.getDataManager().persist(topModel);
        App.getDataManager().saveFile();
//        System.out.println("==== top =====");
        /*  */
        startTimerBar();

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
        } else {
            carModel.getLapRace().getTopsMap().put(carNumber, new ArrayList<>());
            carModel.getLapRace().getTopsMap().get(carNumber).add(topModel);
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
     *
     */
    private void handleMeanTimeBar() {

        // maincarinformation();
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
                updateProgress(1, timeToUpload);
                for (double i = 0; i < timeToUpload; i++) {
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
        thread.setDaemon(true);
        thread.start();
    }

    /**
     *
     */
    @FXML
    public void editable() {
        if (toogleedit.isSelected()) {
            table_info.setEditable(true);
        } else {
            table_info.setEditable(false);
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
        col_laptime.setCellValueFactory(new PropertyValueFactory<>("lapTime"));
        col_racetime.setCellValueFactory(new PropertyValueFactory<>("raceTime"));
        colLapNumber.setCellValueFactory(new PropertyValueFactory<>("lapNumber"));
        col_time.setCellValueFactory(new PropertyValueFactory<>("time"));
        col_comment.setCellValueFactory(new PropertyValueFactory<>("comment"));

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

        col_racetime.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
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
                System.out.println("check");
                updateTopLogicForTopType(carNumber, findTopIndexWithId(carNumber, lastTopId), findTopIndexWithId(carNumber, lastTopId));
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
     *
     * @param e
     */
    private void editTopTime(TableColumn.CellEditEvent<TopModel, String> e) {
        int row = e.getTableView().getSelectionModel().selectedIndexProperty().get();
        int carNumber = e.getTableView().getItems().get(row).getCarNumber();
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        long topId = e.getTableView().getItems().get(row).getId();
        int index = findTopIndexWithId(carNumber, topId);
        String oldTopTime = e.getOldValue();
        String newTopTime = e.getNewValue();
        TopModel top = topModels.get(index);
        int newPos = findTopNewPosition(carNumber, top);
        if(newPos != index) {
            System.out.println(index);
            removeTop(carNumber, index);
            topModels.add(newPos, top);
            updateTopLogicForOtherFields(carNumber, newPos);
        }

    }

    private void updateTopLogicForTopType(int carNumber, int origin, int index) {
        ArrayList<TopModel> topModels = raceModel.getTopsMap().get(carNumber);
        TopModel currentTop = topModels.get(index);
        if (index <= origin && index > 0) {
            TopModel previousTop = topModels.get(index - 1);
            if (checkTopLogic(currentTop.getTopType(), previousTop.getTopType())) {
                updateTopLogicForTopType(carNumber, origin, index - 1);
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
                updateTopLogicForTopType(carNumber, origin, index - 1);
            }
        }
        if (index >= origin && index < topModels.size() - 1) {
            TopModel nextTop = topModels.get(index + 1);
            if (checkTopLogic(nextTop.getTopType(), currentTop.getTopType())) {
                updateTopLogicForTopType(carNumber, origin, index + 1);
            } else {
                if (currentTop.getTopType().equals("R") || currentTop.getTopType().equals("O")) {
                    nextTop.setTopType("R");
                    nextTop.setComment("-Top R système-" + nextTop.getComment());
                } else {
                    nextTop.setTopType("O");
                    nextTop.setComment("-Top O système-" + nextTop.getComment());
                }
                updateTopLogicForTopType(carNumber, origin, index + 1);
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
            System.out.println(newTop.getTime());
            if (LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isBefore(LocalDateTime.parse(top.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))) || LocalDateTime.parse(newTop.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")).isEqual(LocalDateTime.parse(top.getTime(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))) {
                found = true;
            } else {
                index++;
            }
        }
        return index;
    }

    private boolean carExists(int carNumber) {
        boolean exists = false;
        Set<Integer> keys = raceModel.getTopsMap().keySet();
        System.out.println(keys);
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

    private void incrementTime() {
        time = time.plusSeconds(1);
        spentTime.setText(time.format(dtf));
    }

    private void decrementTime() {
        localRemainningTime = localRemainningTime.minusSeconds(1);
        if (localRemainningTime.equals(LocalTime.parse("00:00:00"))) {
            stopRace.setDisable(true);
            startRace.setDisable(false);
            remainingTimeline.stop();
            spentTimeline.stop();
        }
        remainingTime.setText(localRemainningTime.format(dtf));
    }

    private void getCurrentTime() {
        currentTime = LocalTime.now();
        currentHour.setText(currentTime.format(dtf));
    }

    @FXML
    private void startTimer(ActionEvent event) {
        if (!localRemainningTime.equals(LocalTime.parse("00:00:00"))) {
            departureTime = LocalTime.now();
            spentTimeline.play();
            remainingTimeline.play();
            departureHour.setText(currentTime.format(dtf));
            startRace.setDisable(true);
            istartRace = true;
        }
    }

    @FXML
    private void pauseTimer(ActionEvent event) {
        if (spentTimeline.getStatus().equals(Animation.Status.PAUSED)) {
            spentTimeline.play();
            pauseRace.setText("Pause");
        } else if (spentTimeline.getStatus().equals(Animation.Status.RUNNING)) {
            spentTimeline.pause();
            pauseRace.setText("Continue");
        }
    }

    @FXML
    private void endTimer(ActionEvent event) {
        if (startRace.isDisable()) {
            spentTimeline.stop();
            remainingTimeline.stop();
            startRace.setDisable(false);
            time = LocalTime.parse("00:00:00");
            spentTime.setText(time.format(dtf));
            remainingTime.setText(time.format(dtf));

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
        isStartTimer = false;
        isSetTimerBar = true;
        tmierSign.setVisible(false);


    }

    /**
     * started a Timer of the next top for the main car
     * format of the Time is MM :ss:nn
     */
    public void startTimerBar() {
        isStartTimer = true;
        isSetTimerBar = true;

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
     *
     */
    @Override
    public void update() {

    }


}
