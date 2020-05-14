package com.kronos.api;

import java.util.Date;

/**
 * @author TeamKronos
 * @version 1.0
 * Object representing a pilot,
 * which contains the attributes of the pilot and information such as (id, last name, first name, comments, date of birth, height, weight)
 */
public interface Pilot {


    /**
     * Get the unique identifier of the driver that will be generated in the constructor.
     *
     * @return long
     */
    long getId();


    /**
     * Get the name of the driver
     *
     * @return String {@link}
     */
    String getFirstName();


    /**
     * Get the first name of the driver
     *
     * @return String {@link}
     */
    String getLastName();


    /**
     * Get the comments added by the panel on driver during the race
     *
     * @return String {@link}
     */
    String getComment();


    /**
     * Get the pilot's date of birth
     *
     * @return Date
     */
    Date getDateOfBirth();


    /**
     * Get the pilot's height of birth
     *
     * @return double
     */
    double getHeight();


    /**
     * Get the pilot's date of birth
     *
     * @return double
     */
    double getWeight();

    /**
     * the car of our pilote
     *
     * @return pilotecar
     */


    MainCar getPilotCar();

}
