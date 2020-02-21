package com.kronos.model;

import com.kronos.api.TimeRace;
import com.kronos.global.enums.RaceType;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents a time race and its properties. A time race is a race whose ends is determined by an end date.
 */
@XmlRootElement
public class TimeRaceModel extends RaceModel implements TimeRace {

    private long duration;
    private Date endTime;

    /**
     * Constructor.
     */
    public TimeRaceModel() {

    }

    /**
     * Constructor.
     * @param startingTime starting time
     * @param racewayName raceway name
     * @param endTime end time
     * @param raceName name of race
     */
    public TimeRaceModel(String raceName, Date startingTime, String racewayName, Date endTime) {
        super(raceName, startingTime, racewayName);
        this.endTime = endTime;
        this.duration = (endTime.getTime() - startingTime.getTime()) / (60 * 1000) % 60;
    }

    /**
     * Gets the end time of the race.
     * @return the end time of the race.
     */
    @Override
    public Date getEndTime() {
        return this.endTime;
    }

    /**
     * Gets the duration of the race.
     * @return the duration of the race.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the race.
     * @param duration race duration
     */
    public void setDuration(final long duration){
        this.duration = duration;
    }
}
