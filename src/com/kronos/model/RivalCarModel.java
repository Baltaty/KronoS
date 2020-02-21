package com.kronos.model;

import java.util.ArrayList;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents a rival car and its properties. Rival cars are cars of contesting teams.
 */
public class RivalCarModel extends CarModel {

    /**
     * Constructor.
     */
    public RivalCarModel() {

    }

    /**
     * Constructor.
     * @param number car number
     * @param team car team
     * @param model car model
     * @param brand car brand
     * @param pilot car pilot
     */
    public RivalCarModel(int number, String team, String model, String brand, PilotModel pilot) {
        super(number, team, model, brand, pilot);
    }
}

