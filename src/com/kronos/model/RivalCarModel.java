package com.kronos.model;

import java.util.ArrayList;

public class RivalCarModel extends CarModel {

    public RivalCarModel() {

    }

    public RivalCarModel(long id, int number, String name, String model, String brand, ArrayList<TopModel> topList, LapRaceModel lapRace, int completedLaps) {
        super(id, number, name, model, brand, topList, lapRace, completedLaps);
    }

    public RivalCarModel(long id, int number, String name, String model, String brand, ArrayList<TopModel> topList, TimeRaceModel timeRace, int completedLaps) {
        super(id, number, name, model, brand, topList, timeRace, completedLaps);
    }
}

