package com.kronos.global.factory;

import com.kronos.global.enums.RaceType;
import com.kronos.model.LapRaceModel;
import com.kronos.model.RaceModel;
import com.kronos.model.TimeRaceModel;

import java.util.Date;

public class RaceFactory {

    public RaceFactory() {

    }

    public RaceModel createRace(String raceType, long id, Date startingTime, String racewayName, Date endTime, int numberOfLaps) {
        RaceModel raceModel = null;
        if(raceType.equals(RaceType.TIME_RACE)) {
            raceModel = new TimeRaceModel(id, startingTime, racewayName, endTime);
        }
        else if(raceType.equals(RaceType.LAP_RACE)) {
            raceModel = new LapRaceModel(id, startingTime, racewayName, numberOfLaps);
        }
        return raceModel;
    }
}
