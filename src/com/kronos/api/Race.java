package com.kronos.api;

import com.kronos.global.enums.RaceType;
import com.kronos.model.PilotModel;

import java.util.ArrayList;
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

    /**
     * Gets the relay interval in the {@link Race race};
     * @return the relay interval.
     */
    int getRelayInterval();

    /**
     * Gets the average lap time in the {@link Race race};
     * @return the average lap time.
     */
    int getDefaultMeanLapTime();

    ArrayList<PilotModel> getPilotsList();

}
