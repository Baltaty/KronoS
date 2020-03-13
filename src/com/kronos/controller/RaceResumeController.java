package com.kronos.controller;

import com.jfoenix.controls.JFXToggleButton;
import com.kronos.App;
import com.kronos.api.LapRace;
import com.kronos.api.Observer;
import com.kronos.api.Race;
import com.kronos.api.TimeRace;
import com.kronos.global.animation.PulseTransition;
import com.kronos.model.*;
import com.kronos.module.main.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class RaceResumeController implements Initializable, Observer {

    private RaceModel raceModel;
    private CarController carController = new CarController();
    private ArrayList<TopModel> topModels = new ArrayList<>();
    private ArrayList<CarModel> carModels = new ArrayList<>();

    @FXML
    ProgressBar meanTimeBar;

    @FXML
    Button TopBtn;
    @FXML
    Label lastNamePilotMainCar;
    @FXML
    Label firstNamePilotMainCar;
    @FXML
    Label dateOfBirthPilot;
    @FXML
    Label mainCarBrand;
    @FXML
    Label mainCarModel;
    @FXML
    Label mainCarTeam;


    @FXML
    private TableView<String> table_info;

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

    public static ObservableList<String> data_table;

    private static ArrayList<Double> listOfMeanTime = new ArrayList<>();
    private static Double meantime = 0.00;
    PulseTransition pulseTransition;
    MainCarModel mycar;

    @FXML
    private ComboBox<String> car;
    @FXML
    private ComboBox<String> topType;

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
        carModels.addAll(getFollowedCars());
        car.setItems(FXCollections.observableArrayList(getFollowedCarsNumbers(getFollowedCars())));

        initTable();
        //loadData();
    }

    /**
     * @param event
     */
    @FXML
    public void handleTopButtonClick(ActionEvent event) {
       // handleNewTop();
        handleMeanTimeBar();
    }

    /**
     *
     */
    private void handleNewTop() {
        String type = topType.getSelectionModel().getSelectedItem();
        int carNumber = Integer.parseInt(car.getSelectionModel().getSelectedItem());
        Date topTime = null;
        Double raceTime = null;
        double lapTime = 0.0;
        int lap = 0;
        String comment = "";
        CarModel carModel = carController.findCar(carModels, carNumber);
        if (findPreviousTop(carNumber) == null || checkTopLogic(type, findPreviousTop(carNumber).getTopType())) {
            TopModel topModel = null;
            if (raceModel instanceof TimeRaceModel) {
                topModel = new TopModel(new Date(), type, raceTime, lapTime, comment);
                topModels.add(topModel);
                if (carModel.getTimeRace().getTopsMap().containsKey(carNumber)) {
                    carModel.getTimeRace().getTopsMap().get(carNumber).add(topModel);
                } else {
                    carModel.getTimeRace().getTopsMap().put(carNumber, new ArrayList<>());
                }
            } else {
                topModel = new TopModel(new Date(), type, lap, lapTime, comment);
                topModels.add(topModel);
                loadData(topModel);
                if (carModel.getLapRace().getTopsMap().containsKey(carNumber)) {
                    carModel.getLapRace().getTopsMap().get(carNumber).add(topModel);
                } else {
                    carModel.getLapRace().getTopsMap().put(carNumber, new ArrayList<>());
                }
            }
            carController.findCar(carModels, carNumber).getTopList().add(topModel);
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
        if (previousTopType.equals("RACE") && (lastTopType.equals("RACE") || lastTopType.equals("IN"))) {
            respectsLogic = true;
        } else if (previousTopType.equals("IN") && lastTopType.equals("OUT")) {
            respectsLogic = true;
        } else if (previousTopType.equals("OUT") && (lastTopType.equals("RACE") || lastTopType.equals("IN"))) {
            respectsLogic = true;
        }

        return respectsLogic;
    }

    /**
     *
     */
    private void handleMeanTimeBar() {

        maincarinformation();
        pulseTransition = new PulseTransition(meanTimeBar);
        listOfMeanTime.add(0.2);
        meantime = getMeanTime(listOfMeanTime);
        int timeToUpload = (int) (meantime * 60);
        stopanimation();

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

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
            data_table = FXCollections.observableArrayList("date", topModel.getTopType(), null, null, null);
        } else {
            data_table = FXCollections.observableArrayList("date", topModel.getTopType(), null, null, null);
        }
        /*data_table = FXCollections.observableArrayList(
                new TopModel(new Date(),"R",15,4, "Hac ex causa conlaticia stipe "),
                new TopModel(new Date(),"R",15,4, "Hac ex causa conlaticia stipe "),
                new TopModel(new Date(),"R",15,4, "Hac ex causa conlaticia stipe "),
                new TopModel(new Date(),"R",15,4, "Hac ex causa conlaticia stipe "),
                new TopModel(new Date(),"R",15,4, "Hac ex causa conlaticia stipe "),
                new TopModel(new Date(),"R",15,4, "Hac ex causa conlaticia stipe "));*/

        table_info.setItems(data_table);
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
        if (mylistoftime.size() == 1) {
            meantimeaux = mylistoftime.get(0);
        } else if (mylistoftime.size() == 2) {
            meantimeaux = ((mylistoftime.get(0) + mylistoftime.get(1)) / 2);
        } else {
            int start = mylistoftime.size() - 2;
            meantimeaux = ((mylistoftime.get(start - 1) + mylistoftime.get(start) + mylistoftime.get(start + 1)) / 3);


        }

        return meantimeaux;
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
        System.out.println("======= RACESUME =========");
        System.out.println(mycar.getTeam());
        System.out.println(mycar.getPilotModel().getFirstName());
        System.out.println("======= RACESUME =========");

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
        return topModel;
    }

    /**
     *
     */
    @Override
    public void update() {

    }


}
