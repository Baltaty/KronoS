package com.kronos.api;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents the {@link MainCar main car} followed by the team.
 */
public interface MainCar extends Car {

    /**
     * Gets the remaining number of laps before the next pit-stop.
     * The number displayed on the lap counter board will be equal to this number minus one.
     * @return the remaining number of laps before the next pit-stop
     */
    public int getRemainingLapsBeforeStops();
}
