package com.kronos.model;

import com.kronos.api.Race;
import com.kronos.global.enums.RaceType;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;
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

    @XmlElement
    private long id;

    @XmlElement
    private Date startingTime;

    @XmlElement
    private String racewayName;
    private ArrayList<CarModel> carsList;


    /**
     * Constructor.
     */
    public RaceModel() {

    }

    /**
     * Constructor.
     * @param startingTime race starting time
     * @param racewayName raceway name
     */
    public RaceModel(Date startingTime, String racewayName) {
        this.id = System.currentTimeMillis();
        this.startingTime = startingTime;
        this.racewayName = racewayName;
        this.carsList = new ArrayList<>();
    }

    /**
     * Gets the race id.
     * @return the race id
     */
    public long getid() {
        return this.id;
    }

    /**
     * Gets the race starting time.
     * @return the race starting time
     */
    @Override
    public Date getStartingTime() {
        return startingTime;
    }

    /**
     * Gets the raceway name.
     * @return the raceway name
     */
    @Override
    public String getRacewayName() {
        return racewayName;
    }
}
