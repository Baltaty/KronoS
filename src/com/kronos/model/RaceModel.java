package com.kronos.model;

import com.kronos.api.Race;
import com.kronos.global.enums.RaceType;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public abstract class RaceModel implements Race {


    private String raceName;


    private long id;


    private Date startingTime;


    private String racewayName;
    private ArrayList<CarModel> carsList;


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

    @XmlElement
    public long getid() {
        return 0;
    }

    @XmlElement
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
