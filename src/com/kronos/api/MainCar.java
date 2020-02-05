package com.kronos.api;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.util.ArrayList;

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

    /**
     * Gets the pit-stop list for the race.
     * @return the pit-stop list
     */
    public ArrayList<Integer> getStopList();

    /**
     * Sets a list of lap numbers. Each lap number corresponds to a pit-stop.
     * @param stopList the list of pit-stops
     */
    public void setStopList(ArrayList<Integer> stopList);

}
