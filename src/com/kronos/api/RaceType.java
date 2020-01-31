package com.kronos.api;

/**
 *in this class we define the type of race
 * because some races are defined by time
 * and others by the number of laps but not both at the same time
 */
public interface RaceType {
    /**
     *time course
     * race is dertermineted by the duration so we need to know the duration to calculate the end of
     * the Race
     * @return duration of race
     */
    int getlength();

    /**
     * number of laps of the race  because some race is defined by thier numbers of laps
     *the purpose is to know the end of the race
     * @return number of laps of the Race
     */
    int numberOfLaps();
}
