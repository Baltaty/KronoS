package com.kronos.api;

/**
 * @author TeamKronoS
 * @version 1.0
 *
 * Manages the generation of time sheet
 */
public interface Print {

    /**
     * Gets the tops of each pilot
     */
    void getTopsForPilots();

    /**
     * Gets the tops of each car
     */
    void getTopsForCars();

    /**
     * Gets all the tops of the races
     */
    void getTopsForRaces();

}
