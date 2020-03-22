package com.kronos.controller;

import com.jfoenix.controls.JFXToggleButton;
import com.kronos.App;
import com.kronos.api.Observer;
import com.jfoenix.controls.JFXButton;

import com.kronos.global.animation.PulseTransition;
import com.kronos.model.*;
import javafx.collections.FXCollections;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Date;
import java.util.List;

public class RaceResumeController implements Initializable, Observer {

    private RaceModel raceModel;
    private CarController carController = new CarController();
    private ArrayList<TopModel> topModels = new ArrayList<>();
    private ArrayList<CarModel> carModels = new ArrayList<>();

    @FXML
    private Label chronoMeanTime;
    @FXML
    private ProgressBar meanTimeBar;
    @FXML
    private Button TopBtn;
    @FXML
    private Label departureHour;

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

    Timeline spentTimeline;
    Timeline remainingTimeline;
    LocalTime time = LocalTime.parse("00:00:00");  // spend time
    LocalTime localRemainningTime = LocalTime.parse("00:00:05");// temps restant
    LocalTime time2 = LocalTime.parse("00:00");//initialisation l'heure de depart
    LocalTime time3 = LocalTime.parse("00:00:05");
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm:ss:nnn");
    LocalTime currentTime; // temps actuelle
    LocalTime departureTime;
    Timeline meanTimeLine;


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
    private TableColumn<TopModel, Integer> colCarNumber;
    @FXML
    private TableColumn<TopModel, String> col_typetop;
    @FXML
    private TableColumn<TopModel, String> col_comment;
    @FXML
    private TableColumn<TopModel, Date> col_time;
    @FXML
    private TableColumn<TopModel, Double> col_racetime;
    @FXML
    private TableColumn<TopModel, Double> col_laptime;
    @FXML
    private JFXToggleButton toogleedit;


    private static ArrayList<Double> listOfMeanTime = new ArrayList<>();
    private static double meantime = 0.00;
    private static boolean testReference = true;
    PulseTransition pulseTransition;
    MainCarModel mycar;

    @FXML
    private ComboBox<String> car;
    @FXML
    private ComboBox<String> topType;

    public RaceResumeController() {
    }

    /**
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        App.getDataManager().attach(this);
        if (!getRace().isEmpty()) {
            raceModel = getRace().get(0);
        }
        topType.setItems(FXCollections.observableArrayList("I", "O", "R"));
        topType.setValue("O");
        carModels.addAll(getFollowedCars());
        car.setItems(FXCollections.observableArrayList(getFollowedCarsNumbers(getFollowedCars())));
        car.getSelectionModel().selectFirst();

        initTable();
        //loadData();
        maincarinformation();

        time = LocalTime.parse("00:00:00");
        time2 = LocalTime.parse("00:00");
        chronoMeanTime.setText(time3.format(dtf2));
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


        meanTimeLine = new Timeline(new KeyFrame(Duration.millis(1000), e -> decrementTime()));
        meanTimeLine.setCycleCount(Animation.INDEFINITE);


    }


    /**
     * @param event
     */
    @FXML
    public void handleTopButtonClick(ActionEvent event) {
        handleNewTop();
    }

    /**
     *
     */
    private void handleNewTop() {
        String type = topType.getSelectionModel().getSelectedItem();
        int carNumber = Integer.parseInt(car.getSelectionModel().getSelectedItem());
        Date topTime = null;
        Double raceTime = null;
        double lapTime = 0.00;
        double temps = (time).getHour();
        if (listOfMeanTime.size() == 0) {
            listOfMeanTime.add(getMeanTime(listOfMeanTime));
            lapTime = getMeanTime(listOfMeanTime);
            //faire son propre chrono pour le temps moyenn afin d'avoir le temps du top

        } else {
            LocalTime currentLocalTime = LocalTime.now();
            lapTime = (currentLocalTime.getHour());
            listOfMeanTime.add(lapTime);
        }
        getMeanTime(listOfMeanTime);

        //double lapTime = getMeanTime(listOfMeanTime);
        int lap = 0;
        String comment = "";
        CarModel carModel = carController.findCar(carModels, carNumber);
        TopModel topModel = null;
        if (findPreviousTop(carNumber) == null || checkTopLogic(type, findPreviousTop(carNumber).getTopType())) {
            //Case where top respects logical top type order
            if (raceModel instanceof TimeRaceModel) {
                topModel = new TopModel(carNumber, new Date(), type, raceTime, lapTime, comment);
                handleTopTimeRace(topModel, carModel, carNumber);
            } else {
                topModel = new TopModel(carNumber, new Date(), type, lap, lapTime, comment);
                handleTopLapRace(topModel, carModel, carNumber);
            }
            loadData(topModel);
            carModel.getTopList().add(topModel);
            handleMeanTimeBar();
        } else {
            //Case where top does not respect logical top type order
            if (raceModel instanceof TimeRaceModel) {
                if (type == "I") {
                    topModel = new TopModel(carNumber, new Date(), "O", raceTime, lapTime, "Top OUT système");
                } else if (type == "R" || type == "O") {
                    topModel = new TopModel(carNumber, new Date(), "R", raceTime, lapTime, "Top R système");
                }
                handleTopTimeRace(topModel, carModel, carNumber);
            } else {
                if (type == "I") {
                    topModel = new TopModel(carNumber, new Date(), "O", lap, lapTime, "Top OUT système");
                } else if (type == "R" || type == "O") {
                    topModel = new TopModel(carNumber, new Date(), "R", lap, lapTime, "Top RACE système");
                }
                handleTopLapRace(topModel, carModel, carNumber);
            }
            loadData(topModel);
            carModel.getTopList().add(topModel);
            handleMeanTimeBar();
        }

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

        if (testReference)
            pulseTransition = new PulseTransition(meanTimeBar);
        pulseTransition.stop();
        meantime = getMeanTime(listOfMeanTime);
        int timeToUpload = (int) meantime;


        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                testReference = false;
                meanTimeBar.setStyle("-fx-accent: blue;");
                updateProgress(1, timeToUpload);
                for (int i = 0; i < timeToUpload; i++) {
                    updateProgress(i + 1, timeToUpload);
                    Thread.sleep(1000);
                }

                meanTimeBar.setStyle("-fx-accent: red;");
                pulseTransition.setCycleCount(PulseTransition.INDEFINITE);
                pulseTransition.play();
                return null;
            }
        };

        meanTimeBar.progressProperty().unbind();
        meanTimeBar.progressProperty().bind(task.progressProperty());

        Thread thread = new Thread(task);
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

        col_typetop.setCellValueFactory(new PropertyValueFactory<>("topType"));
        col_laptime.setCellValueFactory(new PropertyValueFactory<>("lapTime"));
        col_racetime.setCellValueFactory(new PropertyValueFactory<>("raceTime"));
        col_time.setCellValueFactory(new PropertyValueFactory<>("time"));
        col_comment.setCellValueFactory(new PropertyValueFactory<>("comment"));

        editableCols();
    }

    /**
     *
     */
    private void editableCols() {

        colCarNumber.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colCarNumber.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setCarNumber(e.getNewValue());
        });

        col_typetop.setCellFactory(TextFieldTableCell.forTableColumn());
        col_typetop.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setTopType(e.getNewValue());
        });

        col_racetime.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        col_racetime.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setRaceTime(e.getNewValue());
        });

        col_time.setCellFactory(TextFieldTableCell.forTableColumn(new DateStringConverter()));
        col_time.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setTime(e.getNewValue());
        });

        col_comment.setCellFactory(TextFieldTableCell.forTableColumn());
        col_comment.setOnEditCommit(e -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setComment(e.getNewValue());
        });

    }

    /**
     *
     */
    private void loadData(TopModel topModel) {
        if (raceModel instanceof TimeRaceModel) {
            table_info.getItems().add(0, topModel);
        } else {
            table_info.getItems().add(0, topModel);
        }

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
            spentTimeline.play();
            remainingTimeline.play();
            departureTime = LocalTime.now();
            departureHour.setText(currentTime.format(dtf));
            startRace.setDisable(true);
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

    @FXML
    private void startMeanTimer(ActionEvent event) {


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
//        List<GenericParser> genericParsers = App.getDataManager().getModels(CarModel.class);
        for (CarModel carModel : carModels) {
//            followedCars.add((CarModel) genericParser.getObjectToGenerify());
            followedCars.add(carModel);
        }
        return followedCars;
    }

    public ArrayList<RaceModel> getRace() {
        ArrayList<RaceModel> raceModels = new ArrayList<>();
        List<TimeRaceModel> timeRaceModels = (List<TimeRaceModel>) (List<?>) App.getDataManager().getModels(TimeRaceModel.class);
        List<LapRaceModel> lapRaceModels = (List<LapRaceModel>) (List<?>) App.getDataManager().getModels(LapRaceModel.class);

//        List<GenericParser> timeRaceGenericParsers = App.getDataManager().getModels(TimeRaceModel.class);
//        List<GenericParser> lapRaceGenericParsers = App.getDataManager().getModels(LapRaceModel.class);
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
        System.out.println(topModel);
        return topModel;
    }

    /**
     *
     */
    @Override
    public void update() {

    }


}
