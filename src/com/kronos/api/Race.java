package com.kronos.api;

import java.util.Date;

/**
 * @author TeamsKronos
 * @version 1.0
 */

/**
 * the class is a singleton and we will get all informations  about the current
 * race  with this class
 */


public interface Race {
    /**
     * each race has an unique id  this id will help us to
     * know which race that we are following
     * @return id of the race
     */
    long getid();

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


}
