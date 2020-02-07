package com.kronos.controller;


import com.kronos.api.RivalCar;
import com.kronos.model.CarModel;

/**
 * @author TeamKronoS
 * @version 1.0
 * Controller of the {@link com.kronos.model.CarModel car} objects.
 */
public class CarController {


    public CarController (CarModel carModel) {

    }

    /**
     * Checks if {@link com.kronos.model.TopModel top} for this car respects the job logic.
     * A "RACE" top should only happen after a "RACE" or a "OUT" top.
     * An "IN" top should only happen after a "RACE" or an "OUT" top.
     * An "OUT" top should only happen after an "IN" top.
     * @param lastTopType the last top type entered by the board man
     * @param previousTopType the previous top entered by the board man
     * @return true if the job logic is respected, false otherwise
     */
    public boolean checkTopLogic(String lastTopType, String previousTopType) {
        boolean respectsLogic = false;
        if(previousTopType.equals("RACE") && (lastTopType.equals("RACE") || lastTopType.equals("IN"))) {
            respectsLogic = true;
        }
        else if(previousTopType.equals("IN") && lastTopType.equals("OUT")) {
            respectsLogic = true;
        }
        else if(previousTopType.equals("OUT") && (lastTopType.equals("RACE") || lastTopType.equals("IN"))) {
            respectsLogic = true;
        }

        return respectsLogic;
    }

    public boolean checkCar(CarModel carModel) {

        return false;
    }
}
