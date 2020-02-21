package com.kronos.model;

import com.kronos.api.MainCar;
import com.kronos.api.TimeRace;
import com.sun.xml.internal.txw2.annotation.XmlElement;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents the main car properties. The main car is the principal car we want to observe (e.g : the car of our team).
 */
@XmlRootElement
public class MainCarModel extends CarModel implements MainCar {


    private ArrayList<Integer> stopList;

    /**
     * Constructor.
     */
    public MainCarModel() {

    }

    /**
     * Constructor.
     * @param number car number
     * @param team car team
     * @param model car model
     * @param brand car brand
     * @param pilotModel car pilot
     */
    public MainCarModel(int number, String team, String model, String brand, PilotModel pilotModel) {
        super(number, team, model, brand, pilotModel);
        stopList = new ArrayList<>();
    }

    /**
     * Gets the remaining laps of the car before the next pit-stop.
     * @return the remaining laps of the car before the next pit-stop
     */
    @Override
    public int getRemainingLapsBeforeStops() {
        return stopList.get(0) - getCompletedLaps();
    }

    /**
     * Gets the list of pit-stops (contains the laps where the pit-stop occurs).
     * @return the list of laps where the pit-stop occurs
     */
    @Override
    public ArrayList<Integer> getStopList() {
        return stopList;
    }

    /**
     * Sets the list of pit-stops
     * @param stopList the list of pit-stops
     */
    @Override
    public void setStopList(ArrayList<Integer> stopList) {
        this.stopList = stopList;
    }
}
