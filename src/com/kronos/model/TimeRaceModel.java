package com.kronos.model;

import com.kronos.api.TimeRace;
import com.kronos.global.enums.RaceType;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class TimeRaceModel extends RaceModel implements TimeRace {

    private long duration;

    public TimeRaceModel() {

    }

    public TimeRaceModel(Date startingTime, String racewayName, Date endTime) {
        super(startingTime, racewayName);
        this.duration = endTime.getTime() - startingTime.getTime() / (60 * 1000) % 60;
    }

    @Override
    public Date getEndTime() {
        return null;
    }

    public long getDuration() {
        return duration;
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
