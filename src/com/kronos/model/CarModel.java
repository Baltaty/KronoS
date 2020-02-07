package com.kronos.model;

import com.kronos.api.Car;
import java.util.ArrayList;

public abstract class CarModel implements Car {

    private long id;
    private int number;
    private String name;
    private String model;
    private String brand;
    private ArrayList<TopModel> topList;
    private LapRaceModel lapRace;
    private TimeRaceModel timeRace;
    private int completedLaps;

    public CarModel() {

    }

    public CarModel(long id, int number, String name, String model, String brand, ArrayList<TopModel> topList, LapRaceModel lapRace, int completedLaps) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.model = model;
        this.brand = brand;
        this.topList = new ArrayList<TopModel>();
        this.lapRace = lapRace;
        this.completedLaps = completedLaps;
    }

    public CarModel(long id, int number, String name, String model, String brand, ArrayList<TopModel> topList, TimeRaceModel timeRace, int completedLaps) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.model = model;
        this.brand = brand;
        this.topList = new ArrayList<TopModel>();
        this.timeRace = timeRace;
        this.completedLaps = completedLaps;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        return remainingLaps;
    }
}
