package com.kronos.model;

import com.kronos.api.TimeRace;

import java.util.Date;

public class TimeRaceModel extends RaceModel implements TimeRace {

    private Long duration;

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
}
