package com.kronos.model;

import com.kronos.api.Car;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents a generic car. This class is abstract. It contains common properties to all cars.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class CarModel implements Car {

    @XmlElement
    private long id;

    @XmlElement
    private int number;


    @XmlElement
    private String team;


    @XmlElement
    private String model;


    @XmlElement
    private String brand;


    @XmlElement
    private ArrayList<TopModel> topList;


    @XmlElement
    private LapRaceModel lapRace;


    @XmlElement
    private TimeRaceModel timeRace;

    @XmlElement
    private int completedLaps;


    @XmlElement
    private PilotModel pilotModel;

    /**
     * Constructor.
     */
    public CarModel() {

    }

    /**
     * Constructor.
     * @param number car number
     * @param team car team
     * @param model car model
     * @param brand car brand
     * @param pilotModel car pilot
     */
    public CarModel(int number, String team, String model, String brand, PilotModel pilotModel) {
        this.id = System.currentTimeMillis();
        this.number = number;
        this.team = team;
        this.model = model;
        this.brand = brand;
        this.pilotModel = pilotModel;
    }

    /**
     * Gets the {@link LapRaceModel lap race} associated with this car (if it is competing in a lap race).
     * @return the {@link LapRaceModel lap race} where this car is competing. If it is not competing in a lap race, returns null.
     */
    public LapRaceModel getLapRace() {
        return lapRace;
    }

    /**
     * Sets the {@link LapRaceModel lap race} associated with this car.
     * @param lapRace {@link LapRaceModel lap race}
     */
    public void setLapRace(LapRaceModel lapRace) {
        this.lapRace = lapRace;
    }

    /**
     * Gets the {@link TimeRaceModel time race} associated with this car (if it is competing in a time race).
     * @return the {@link TimeRaceModel time race} where this car is competing. If it is not competing in a time race, returns null.
     */
    public TimeRaceModel getTimeRace() {
        return timeRace;
    }

    /**
     * Sets the {@link TimeRaceModel time race} associated with this car.
     * @param timeRace {@link TimeRaceModel time race}
     */
    public void setTimeRace(TimeRaceModel timeRace) {
        this.timeRace = timeRace;
    }

    /**
     * Gets the car id.
     * @return car id
     */
    public long getId() {
        return this.id;
    }

    /**
     * Sets the car id.
     * @param id id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the car number.
     * @return the car number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets the car number.
     * @param number car number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Gets the car team.
     * @return the car team
     */
    public String getTeam() {
        return team;
    }

    /**
     * Sets the car team.
     * @param team car team
     */
    public void setTeam(String team) {
        this.team = team;
    }

    /**
     * Gets the car model (for example: Ferrari F50, F50 is the car model).
     * @return the car model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the car model.
     * @param model car model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets the car brand (for example: Ferrari F50, Ferrari is the car brand).
     * @return the car brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the car brand.
     * @param brand car brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Gets the list of {@link TopModel tops} associated with this car.
     * @return the list of {@link TopModel tops} associated with this car.
     */
    public ArrayList<TopModel> getTopList() {
        return topList;
    }

    /**
     * Sets the list of {@link TopModel tops} associated with this car.
     * @param topList the list of {@link TopModel tops} associated with this car.
     */
    public void setTopList(ArrayList<TopModel> topList) {
        this.topList = topList;
    }

    /**
     * Gets the completed laps of the car.
     * @return the completed laps of the car
     */
    public int getCompletedLaps() {
        return completedLaps;
    }

    /**
     * Sets the completed laps of the car.
     * @param completedLaps completed laps of the car
     */
    public void setCompletedLaps(int completedLaps) {
        this.completedLaps = completedLaps;
    }

    /**
     * Gets the remaining laps of the car.
     * @return the remaining laps of the car, -1 if this car is not doing a lap race (and subsequently does not have a remaining number of laps)
     */
    @Override
    public int getRemainingLaps() {
        int remainingLaps = -1;
        try {
            remainingLaps = this.lapRace.getNumberOfLaps() - this.getCompletedLaps();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return remainingLaps;
    }

    public PilotModel getPilotModel() {
        return pilotModel;
    }

    public void setPilotModel(PilotModel pilotModel) {
        this.pilotModel = pilotModel;
    }
}
