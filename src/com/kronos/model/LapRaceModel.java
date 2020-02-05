package com.kronos.model;

import com.kronos.api.LapRace;

import java.util.Date;

public class LapRaceModel extends RaceModel implements LapRace {

    private int numberOfLaps;

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
}
