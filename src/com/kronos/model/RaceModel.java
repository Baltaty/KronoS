package com.kronos.model;

import com.kronos.api.Race;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public abstract class RaceModel implements Race {

    @XmlElement
    private String raceName;

    @XmlElement
    private long id;

    @XmlElement
    private Date startingTime;

    @XmlElement
    private String racewayName;
    private ArrayList<CarModel> carsList;


    public RaceModel() {

    }

    public RaceModel(String raceName, Date startingTime, String racewayName) {
        this.raceName = raceName;
        this.id = System.currentTimeMillis();
        this.startingTime = startingTime;
        this.racewayName = racewayName;
        this.carsList = new ArrayList<>();
    }

    public long getid() {
        return 0;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }


    public Date getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Date startingTime) {
        this.startingTime = startingTime;
    }


    public void setRacewayName(String racewayName) {
        this.racewayName = racewayName;
    }

    @Override
    public String getRacewayName() {
        return racewayName;
    }

    public ArrayList<CarModel> getCarsList() {
        return carsList;
    }

    public void setCarsList(ArrayList<CarModel> carsList) {
        this.carsList = carsList;
    }




}
