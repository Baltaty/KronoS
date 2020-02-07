package com.kronos.model;

import com.kronos.api.MainCar;
import com.kronos.api.TimeRace;

import java.util.ArrayList;

public class MainCarModel extends CarModel implements MainCar {

    private ArrayList<Integer> stopList;

    public MainCarModel() {

    }

    public MainCarModel(long id, int number, String name, String model, String brand, ArrayList<TopModel> topList, LapRaceModel lapRace, int completedLaps) {
        super(id, number, name, model, brand, topList, lapRace, completedLaps);
        stopList = new ArrayList<>();
    }

    public MainCarModel(long id, int number, String name, String model, String brand, ArrayList<TopModel> topList, TimeRaceModel timeRace, int completedLaps) {
        super(id, number, name, model, brand, topList, timeRace, completedLaps);
        stopList = new ArrayList<>();
    }

    @Override
    public int getRemainingLapsBeforeStops() {
        return stopList.get(0) - getCompletedLaps();
    }

    @Override
    public ArrayList<Integer> getStopList() {
        return stopList;
    }

    @Override
    public void setStopList(ArrayList<Integer> stopList) {
        this.stopList = stopList;
    }
}
