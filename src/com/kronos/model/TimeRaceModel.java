package com.kronos.model;

import com.kronos.api.TimeRace;
import com.kronos.global.enums.RaceType;

import java.util.Date;

public class TimeRaceModel extends RaceModel implements TimeRace {

    private Long duration;

    public TimeRaceModel() {

    }

    public TimeRaceModel(long id, Date startingTime, String racewayName, Date endTime) {
        super(id, startingTime, racewayName);
        this.duration = endTime.getTime() - startingTime.getTime() / (60 * 1000) % 60;
    }

    @Override
    public Date getEndTime() {
        return null;
    }

    public String getDuration() {
        return duration.toString();
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
