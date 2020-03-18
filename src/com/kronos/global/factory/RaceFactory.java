package com.kronos.global.factory;

import com.kronos.global.enums.RaceType;
import com.kronos.model.LapRaceModel;
import com.kronos.model.RaceModel;
import com.kronos.model.TimeRaceModel;

import java.util.Date;

/**
 * @author TeamKronoS
 * @version 1.0
 * Builds a {@link RaceModel object} of the correct type. There are two race types : the time race whose duration is determined by an end date
 * and the lap race whose end is determined by a fix number of laps.
 */
public class RaceFactory {

    /**
     * Constructor.
     */
    public RaceFactory() {

    }

    /**
     * Creates a new race.
     *
     * @param raceType     race type
     * @param startingTime starting time
     * @param raceName     name of race for save
     * @param racewayName  raceway name
     * @param endTime      end time (in case of a time race)
     * @param numberOfLaps number of laps (in case of a lap race)
     * @return the new race with the correct parameters
     */
    public RaceModel createRace(RaceType raceType, String raceName, Date startingTime, String racewayName, Date endTime, int numberOfLaps) {
        RaceModel raceModel = null;
        if (raceType.equals(RaceType.TIME_RACE)) {
            raceModel = new TimeRaceModel(raceName, startingTime, racewayName, endTime);
        } else if (raceType.equals(RaceType.LAP_RACE)) {
            raceModel = new LapRaceModel(raceName, startingTime, racewayName, numberOfLaps);
        }
        return raceModel;
    }
}
