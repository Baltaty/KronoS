package com.kronos.model;

import com.kronos.api.MainCar;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents the main car properties. The main car is the principal car we want to observe (e.g : the car of our team).
 */
@XmlRootElement
public class MainCarModel extends CarModel implements MainCar {

    /**
     * Constructor.
     */
    public MainCarModel() {

    }

    /**
     * Constructor.
     *
     * @param number     car number
     * @param team       car team
     * @param model      car model
     * @param brand      car brand
     * @param pilotModel car pilot
     */
    public MainCarModel(int number, String team, String model, String brand, PilotModel pilotModel) {
        super(number, team, model, brand, pilotModel);
    }


}
