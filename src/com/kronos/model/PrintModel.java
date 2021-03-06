package com.kronos.model;

import com.kronos.api.Print;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents the properties necessary to generate the time sheets.
 */
@XmlRootElement
public class PrintModel implements Print {

    /**
     * Constructor.
     */
    public PrintModel() {

    }

    /**
     * Gets the tops of each pilot.
     */
    @Override
    public void getTopsForPilots() {

    }

    /**
     * Gets the tops of each car.
     */
    @Override
    public void getTopsForCars() {

    }

    /**
     * Gets all the tops of each race.
     */
    @Override
    public void getTopsForRaces() {

    }
}
