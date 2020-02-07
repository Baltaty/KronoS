package com.kronos.model;

import com.kronos.api.LapRace;
import com.kronos.global.enums.RaceType;

import java.util.Date;

public class LapRaceModel extends RaceModel implements LapRace {

    private int numberOfLaps;

    public LapRaceModel() {
    }

    public LapRaceModel(long id, Date startingTime, String racewayName, int numberOfLaps) {
        super(id, startingTime, racewayName);
        this.numberOfLaps = numberOfLaps;
    }

    @Override
    public int getNumberOfLaps() {
        return numberOfLaps;
    }

    @Override
    public void setNumberOfLaps(int numberOfLaps) {
        this.numberOfLaps = numberOfLaps;
    }

    /**
     * getting the type of the race  because we have two type of race
     *
     * @return the type of the race
     */
    @Override
    public RaceType getRaceType() {
        return null;
    }
}
