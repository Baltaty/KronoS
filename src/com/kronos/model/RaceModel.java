package com.kronos.model;

import com.kronos.api.Race;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public abstract class RaceModel implements Race {

    @XmlElement
    private long id;

    @XmlElement
    private Date startingTime;

    @XmlElement
    private String racewayName;
    private ArrayList<CarModel> carsList;


    public RaceModel() {

    }

    public RaceModel(Date startingTime, String racewayName) {
        this.id = System.currentTimeMillis();
        this.startingTime = startingTime;
        this.racewayName = racewayName;
        this.carsList = new ArrayList<>();
    }

    public long getid() {
        return 0;
    }

    @Override
    public Date getstartingTime() {
        return startingTime;
    }

    @Override
    public String getRacewayName() {
        return racewayName;
    }
}
