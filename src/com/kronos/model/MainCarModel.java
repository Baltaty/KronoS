package com.kronos.model;

import com.kronos.api.MainCar;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents the main car properties. The main car is the principal car we want to observe (e.g : the car of our team).
 */
@XmlRootElement
public class MainCarModel extends CarModel implements MainCar {

    int number;
    String team;
    String model;
    String brand;
    PilotModel pilotModel;


    private ArrayList<Integer> stopList;

    /**
     * Constructor.
     */
    public MainCarModel() {

    }

    /**
     * Constructor.
     *
     * @param number     car number
     * @param team       car team
     * @param model      car model
     * @param brand      car brand
     * @param pilotModel car pilot
     */
    public MainCarModel(int number, String team, String model, String brand, PilotModel pilotModel) {
        super(number, team, model, brand, pilotModel);
        stopList = new ArrayList<>();
    }

    /**
     * Gets the remaining laps of the car before the next pit-stop.
     *
     * @return the remaining laps of the car before the next pit-stop
     */
    @Override
    public int getRemainingLapsBeforeStops() {
        return stopList.get(0) - getCompletedLaps();
    }

    /**
     * Gets the list of pit-stops (contains the laps where the pit-stop occurs).
     *
     * @return the list of laps where the pit-stop occurs
     */
    @Override
    public ArrayList<Integer> getStopList() {
        return stopList;
    }

    /**
     * Sets the list of pit-stops
     *
     * @param stopList the list of pit-stops
     */
    @Override
    public void setStopList(ArrayList<Integer> stopList) {
        this.stopList = stopList;
    }


    /**
     * Get's main car number
     * @return main car number
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * set's  main Car Number
     * @param number main car number
     */
    @Override
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Get's main  Team
     * @return main car team
     */
    @Override
    public String getTeam() {
        return team;
    }

    /**
     * Set's main car team
     * @param team car team
     */
    @Override
    public void setTeam(String team) {
        this.team = team;
    }

    /**
     * Get's main car model
     * @return main car model
     */
    @Override
    public String getModel() {
        return model;
    }

    /**
     * Set's main car model
     * @param model car model
     */
    @Override
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Get's main car brand
     * @return main car brand
     */
    @Override
    public String getBrand() {
        return brand;
    }

    /**
     * Set's main car brand
     * @param brand car brand
     */
    @Override
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Get's main car pilot
     * @return main car pilot
     */
    public PilotModel getPilotModel() {
        return pilotModel;
    }

    /**
     * Set's main car pilot
     * @param pilotModel
     */
    public void setPilotModel(PilotModel pilotModel) {
        this.pilotModel = pilotModel;
    }
}
