package com.kronos.model;

import java.util.ArrayList;

public class RivalCarModel extends CarModel {

    public RivalCarModel() {

    }

    public RivalCarModel(long id, int number, String team, String model, String brand, ArrayList<TopModel> topList, LapRaceModel lapRace, int completedLaps) {
        super(id, number, team, model, brand, topList, lapRace, completedLaps);
    }

    public RivalCarModel(long id, int number, String team, String model, String brand, ArrayList<TopModel> topList, TimeRaceModel timeRace, int completedLaps) {
        super(id, number, team, model, brand, topList, timeRace, completedLaps);
    }
}

