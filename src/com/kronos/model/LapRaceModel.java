package com.kronos.model;

import com.kronos.api.LapRace;
import com.kronos.global.enums.RaceType;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Represents a lap race and its properties. A lap race is a race whose end is determined by a fix number of laps.
 */
@XmlRootElement
public class LapRaceModel extends RaceModel implements LapRace {

    private int numberOfLaps;

    /**
     * Constructor.
     */
    public LapRaceModel() {
    }


    /**
     * Constructor.
     * @param startingTime race starting time
     * @param racewayName raceway name
     * @param numberOfLaps number of laps
     */
    public LapRaceModel(String raceName, Date startingTime, String racewayName, int numberOfLaps) {
        super(raceName, startingTime, racewayName);
        this.numberOfLaps = numberOfLaps;
    }

    /**
     * Gets the number of laps of the race.
     * @return the number of laps of the race
     */
    @Override
    public int getNumberOfLaps() {
        return numberOfLaps;
    }

    /**
     * Sets the number of laps of the race.
     * @param numberOfLaps number of laps
     */
    @Override
    public void setNumberOfLaps(int numberOfLaps) {
        this.numberOfLaps = numberOfLaps;
    }
}
