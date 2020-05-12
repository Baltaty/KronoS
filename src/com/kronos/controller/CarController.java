package com.kronos.controller;


import com.kronos.global.util.Mask;
import com.kronos.model.CarModel;
import com.kronos.model.RaceModel;

import java.util.ArrayList;

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
     *
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
     * Finds a {@link CarModel car}.
     *
     * @param carNumber the {@link CarModel car} number.
     * @return the appropriate {@link CarModel car}.
     */
    public CarModel findCar(ArrayList<CarModel> carModels, int carNumber) {
        int i = 0;
        boolean found = false;
        CarModel ret = null;
        while (i < carModels.size() && !found) {
            if (carModels.get(i).getNumber() == carNumber) {
                ret = carModels.get(i);
                found = true;
            }
            i++;
        }
        return ret;
    }

    /**
     * Gets the current {@link RaceModel race}.
     *
     * @return the current {@link RaceModel race}.
     */
    public RaceModel getRaceModel() {
        return raceModel;
    }

    /**
     * Sets the current {@link RaceModel race}.
     *
     * @param raceModel the {@link RaceModel race}.
     */
    public void setRaceModel(RaceModel raceModel) {
        this.raceModel = raceModel;
    }
}
