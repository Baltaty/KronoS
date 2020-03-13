package com.kronos.controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.kronos.App;
import com.kronos.api.Observer;
import com.kronos.api.TimeRace;
import com.kronos.api.Top;
import com.kronos.global.util.Mask;
import com.kronos.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author TeamKronoS
 * @version 1.0
 * Controller of the {@link com.kronos.model.CarModel car} objects.
 */
public class CarController {

    private RaceModel raceModel;



    public CarController() {
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
            //mettre une alerte
        }
        if (!Mask.isNumeric(String.valueOf(carModel.getNumber()))) {
            checked = false;
            //mettre une alerte
        }
        return checked;
    }

    /**
     *
     * @param carNumber
     * @return
     */
    public CarModel findCar(ArrayList<CarModel> carModels, int carNumber) {
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
}
