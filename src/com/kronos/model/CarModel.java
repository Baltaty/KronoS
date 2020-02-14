package com.kronos.model;

import com.kronos.api.Car;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

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

    public CarModel() {

    }

    public CarModel(int number, String team, String model, String brand, PilotModel pilotModel) {
        this.id = System.currentTimeMillis();
        this.number = number;
        this.team = team;
        this.model = model;
        this.brand = brand;
        this.pilotModel = pilotModel;
    }

    public LapRaceModel getLapRace() {
        return lapRace;
    }

    public void setLapRace(LapRaceModel lapRace) {
        this.lapRace = lapRace;
    }

    public TimeRaceModel getTimeRace() {
        return timeRace;
    }

    public void setTimeRace(TimeRaceModel timeRace) {
        this.timeRace = timeRace;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {

    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ArrayList<TopModel> getTopList() {
        return topList;
    }

    public void setTopList(ArrayList<TopModel> topList) {
        this.topList = topList;
    }

    public int getCompletedLaps() {
        return completedLaps;
    }

    public void setCompletedLaps(int completedLaps) {
        this.completedLaps = completedLaps;
    }

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
}
