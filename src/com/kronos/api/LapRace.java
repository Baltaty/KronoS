package com.kronos.api;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents a {@link LapRace lap race}, a race which end is determined by a maximal number of laps.
 */
public interface LapRace {

    /**
     * Get the number of laps for a lap race.
     * @return the number of laps
     */
    public int getNumberOfLaps();

    /**
     * Sets the number of laps for the race
     * @param numberOfLaps the number of laps
     */
    void setNumberOfLaps(int numberOfLaps);
}
