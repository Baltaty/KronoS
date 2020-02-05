package com.kronos.model;

import com.kronos.api.TimeRace;

import java.util.ArrayList;

public class MainCarModel extends CarModel {

    private ArrayList<Integer> stopList;
    private int remainingLapsBeforeStop;


    public MainCarModel(long id, int number, String name, String model, String brand, ArrayList<TopModel> topList, LapRaceModel lapRace, TimeRaceModel timeRace, int completedLaps) {
        super(id, number, name, model, brand, topList, lapRace, timeRace, completedLaps);
        stopList = new ArrayList<>();
    }

    public ArrayList<Integer> getStopList() {
        return stopList;
    }

    public void setStopList(ArrayList<Integer> stopList) {
        this.stopList = stopList;
    }
}
