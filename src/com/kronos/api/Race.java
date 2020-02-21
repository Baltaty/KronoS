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
     *  each race has the Time that it will start
     * @return startingTime it represent when the race will start
     */
    Date getstartingTime();

    /**
     * the name of the race
     * @return raceWayName
     */
    String getRacewayName();

    /**
     * getting the type of the race  because we have two type of race
     * @return  the type of the race
     */
    RaceType getRaceType();







}
