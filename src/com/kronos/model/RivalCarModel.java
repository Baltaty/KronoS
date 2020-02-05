package com.kronos.model;

import java.util.ArrayList;

public class RivalCarModel extends CarModel {

    public RivalCarModel(long id, int number, String name, String model, String brand, ArrayList<TopModel> topList, RaceModel race, int completedLaps) {
        super(id, number, name, model, brand, topList, race, completedLaps);
    }
}

