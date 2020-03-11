package com.kronos.api;

import com.kronos.global.enums.RaceType;

import java.util.Date;

/**
 * @author TeamsKronos
 * @version 1.0
 * Represents a race and its properties.
 */

/**
 * The class is a singleton and we will get all informations  about the current
 * race  with this class
 */
public interface Race {

    /**
     * Each race has the Time that it will start
     * @return startingTime it represent when the race will start
     */
    Date getStartingTime();

    /**
     * The name of the race
     * @return raceWayName
     */
    String getRacewayName();

    /**
     *
     * @return
     */
    String getRaceName();


}
