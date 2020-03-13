package com.kronos.controller;

import com.kronos.App;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import com.kronos.App;
import com.kronos.api.Observer;
import com.kronos.global.animation.PulseTransition;
import com.kronos.model.GenericParser;
import com.kronos.model.MainCarModel;
import com.kronos.module.main.Main;
import com.kronos.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.util.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
public class RaceResumeController implements Initializable, Observer {

    private RaceModel raceModel;
    private CarController carController = new CarController();

    @FXML
    ProgressBar meanTimeBar;

    @FXML
    Button TopBtn;
    @FXML
    Label lastNamePilotMainCar, firstNamePilotMainCar, dateOfBirthPilot, maincarBrand, mainCarModel, mainCarTeam;

    @FXML
    private TableView<TopModel> table_info;

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

    public static ObservableList<TopModel> data_table;

    private static ArrayList<Double> listOfMeanTime = new ArrayList<>();
    private static Double meantime = 0.00;
    PulseTransition pulseTransition;

    @FXML
    private ComboBox<String> car;
    @FXML
    private ComboBox<String> topType;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        App.getDataManager().attach(this);
        topType.setItems(FXCollections.observableArrayList("I", "O", "R"));
        System.out.println("coucou");
        System.out.println(getFollowedCarsNumbers(getFollowedCars()).size());
        car.setItems(FXCollections.observableArrayList(getFollowedCarsNumbers(getFollowedCars())));

        initTable();
        loadData();
    }

    /**
     *
     * @param event
     */
    @FXML
    public void handleTopButtonClick(ActionEvent event) {
        handleNewTop();
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
        Double lapTime = null;
        Integer lap = null;
        String comment = "";
        CarModel carModel = carController.findCar(carNumber);
        if (carController.checkTopLogic(type, carModel.getTopList().get(carModel.getTopList().size() - 1).getTopType())) {
            TopModel topModel = null;
            if(raceModel instanceof TimeRaceModel) {
                topModel = new TopModel(topTime, type, raceTime , lapTime, comment);
                System.out.println("top time");
            }
            else {
                topModel = new TopModel(topTime, type, lap, lapTime, comment);
                System.out.println("top lap");
            }
            carController.findCar(carNumber).getTopList().add(topModel);
        }
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
    private void loadData() {
        table_info.getItems().clear();
        data_table = FXCollections.observableArrayList(
                new TopModel(new Date(),"R",15,4, "Hac ex causa conlaticia stipe "),
                new TopModel(new Date(),"R",15,4, "Hac ex causa conlaticia stipe "),
                new TopModel(new Date(),"R",15,4, "Hac ex causa conlaticia stipe "),
                new TopModel(new Date(),"R",15,4, "Hac ex causa conlaticia stipe "),
                new TopModel(new Date(),"R",15,4, "Hac ex causa conlaticia stipe "),
                new TopModel(new Date(),"R",15,4, "Hac ex causa conlaticia stipe "));

        table_info.setItems(data_table);

    }

    /**
     *
     */
    /**
     * Pulse animation
     * stop animation on the progress Bar
     */

    public void stopanimation() {

        pulseTransition.stop();
        System.out.println("bouton pressed");

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

        List<GenericParser> maincarinformation = App.getDataManager().getModels(MainCarModel.class);
        MainCarModel maincar = (MainCarModel) maincarinformation.get(0).getObjectToGenerify();
        lastNamePilotMainCar.setText(maincar.getPilotModel().getLastName());
        firstNamePilotMainCar.setText(maincar.getPilotModel().getFirstName());
        dateOfBirthPilot.setText(new SimpleDateFormat("dd-MM-yyyy").format(maincar.getPilotModel().getDateOfBirth()));
        maincarBrand.setText(maincar.getBrand());
        mainCarModel.setText(maincar.getModel());
        mainCarTeam.setText(maincar.getTeam());


    }

    /**
     *
     * @return
     */
    public ArrayList<CarModel> getFollowedCars() {
        ArrayList<CarModel> followedCars = new ArrayList<>();
        List<GenericParser> genericModels = App.getDataManager().getModels(CarModel.class);
        for(GenericParser genericModel : genericModels) {
            followedCars.add((CarModel) genericModel.getObjectToGenerify());
        }
        return followedCars;
    }

    /**
     *
     * @param followedCars
     * @return
     */
    public ArrayList<String> getFollowedCarsNumbers(ArrayList<CarModel> followedCars) {
        ArrayList<String> followedCarsNumbers = new ArrayList<>();
        for(CarModel followedCar : followedCars) {
            followedCarsNumbers.add(Integer.toString(followedCar.getNumber()));
        }
        return followedCarsNumbers;
    }

    /**
     *
     */
    @Override
    public void update() {

    }


}
