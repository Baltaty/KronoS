package com.kronos.api;

import java.util.Date;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents a {@link TimeRace time race}, a race which end is determined by a duration.
 */
public interface TimeRace {

    /**
     * the raceWaylength  it the duration  of the race
     * the time between  the start time and the end of the race
     * @return raceWaylength
     */
    String getDuration();

    /**
     * the time when the race will be finished
     * @return endTime
     */
    Date getEndTime();

}
