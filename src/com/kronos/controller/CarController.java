package com.kronos.controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.kronos.api.TimeRace;
import com.kronos.api.Top;
import com.kronos.global.util.Mask;
import com.kronos.model.CarModel;
import com.kronos.model.RaceModel;
import com.kronos.model.TimeRaceModel;
import com.kronos.model.TopModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author TeamKronoS
 * @version 1.0
 * Controller of the {@link com.kronos.model.CarModel car} objects.
 */
public class CarController implements Initializable {

    private RaceModel raceModel;
    private ArrayList<CarModel> carModels = new ArrayList<>();
    private ArrayList<String> carNumbers = new ArrayList<>();
    private ObservableList<String> carModelsObs = FXCollections.observableArrayList(carNumbers);

    @FXML
    private JFXButton TopBtn;
    @FXML
    private ComboBox<String> car;
    @FXML
    private ComboBox<String> topType;

    public CarController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        topType.setItems(FXCollections.observableArrayList("I", "O", "R"));
        car.setItems(FXCollections.observableArrayList(carModelsObs));
    }

    /**
     *
     * @param event
     */
    @FXML
    public void handleTopButtonClicked(ActionEvent event) {
        String type = topType.getSelectionModel().getSelectedItem();
        int carNumber = Integer.parseInt(car.getSelectionModel().getSelectedItem());
        Date topTime = null;
        Double raceTime = null;
        Integer lap = null;
        String comment = "";
        CarModel carModel = findCar(carNumber);
        if (checkTopLogic(type, carModel.getTopList().get(carModel.getTopList().size() - 1).getTopType())) {
            TopModel topModel = null;
            if(raceModel instanceof TimeRaceModel) {
                topModel = new TopModel(topTime, type, raceTime, comment);
                System.out.println("top time");
            }
            else {
                topModel = new TopModel(topTime, type, lap, comment);
                System.out.println("top lap");
            }
            findCar(carNumber).getTopList().add(topModel);
        }
    }



    /**
     * Checks if the data input for a {@link com.kronos.api.Car car} is correct.
     * @param carModel the model
     * @return true if the data input is correct, false otherwise
     */
    public boolean checkCar(CarModel carModel) {

        boolean checked = true;

        if (!Mask.isNumeric(String.valueOf(carModel.getId()))) {
            checked = false;
            System.out.println("là");
            //mettre une alerte
        }
        if (!Mask.isNumeric(String.valueOf(carModel.getNumber()))) {
            checked = false;
            System.out.println("non là");
            //mettre une alerte
        }
        return checked;
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
     * @param carNumber
     * @return
     */
    public CarModel findCar(int carNumber) {
        int i = 0;
        boolean found = false;
        CarModel ret = null;
        while(i < carModels.size() && !found) {
            if(carModels.get(i).getNumber() == carNumber) {
                ret = carModels.get(i);
                found = true;
            }
            i++;
        }
        return ret;
    }

    /**
     *
     * @return
     */
    public RaceModel getRaceModel() {
        return raceModel;
    }

    /**
     *
     * @param raceModel
     */
    public void setRaceModel(RaceModel raceModel) {
        this.raceModel = raceModel;
    }

    /**
     *
     * @return
     */
    public ArrayList<CarModel> getCarModels() {
        return carModels;
    }

    /**
     *
     * @param carModels
     */
    public void setCarModels(ArrayList<CarModel> carModels) {
        this.carModels = carModels;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getCarNumbers() {
        return carNumbers;
    }

    /**
     *
     * @param carNumbers
     */
    public void setCarNumbers(ArrayList<String> carNumbers) {
        this.carNumbers = carNumbers;
    }
}
