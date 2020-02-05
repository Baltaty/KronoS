package com.kronos.api;

import com.kronos.parserXML.api.Parceleable;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents a car and its properties.
 */
public interface Car extends Parceleable {

    /**
     * Gets the remaining laps for this car before the end of the race.
     * @return the number of remaining laps for the car
     */
    public int getRemainingLaps();
}
