package com.kronos.model;

import com.kronos.api.Race;
import com.kronos.global.enums.RaceType;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents a race and its properties. This class is abstract. It contains common properties to all races.
 */
@XmlRootElement
public abstract class RaceModel implements Race {


    private String raceName;


    private long id;


    private Date startingTime;


    private String racewayName;
    private ArrayList<CarModel> carsList;


    /**
     * Constructor.
     */
    public RaceModel() {

    }

    /**
     *
     * @param raceName
     * @param startingTime
     * @param racewayName
     */
    public RaceModel(String raceName, Date startingTime, String racewayName) {
        this.raceName = raceName;
        this.id = System.currentTimeMillis();
        this.startingTime = startingTime;
        this.racewayName = racewayName;
        this.carsList = new ArrayList<>();
    }

    /**
     * Gets the race id.
     * @return the race id
     */
    @XmlElement
    public long getid() {
        return this.id;
    }

    @XmlElement
    /**
     * Gets the race starting time.
     * @return the race starting time
     */
    @Override
    public String getRaceName() {
        return raceName;
    }


    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }


    @XmlElement
    @Override
    public Date getStartingTime() {
        return startingTime;
    }

    /**
     * Gets the raceway name.
     * @return the raceway name
     */
    public void setStartingTime(Date startingTime) {
        this.startingTime = startingTime;
    }


    public void setRacewayName(String racewayName) {
        this.racewayName = racewayName;
    }

    @XmlElement
    @Override
    public String getRacewayName() {
        return racewayName;
    }

    @XmlElement
    public ArrayList<CarModel> getCarsList() {
        return carsList;
    }

    public void setCarsList(ArrayList<CarModel> carsList) {
        this.carsList = carsList;
    }




}
