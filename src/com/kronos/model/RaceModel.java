package com.kronos.model;

import com.kronos.api.Race;

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


    public RaceModel() {

    }

    public RaceModel(Date startingTime, String racewayName) {
        this.id = System.currentTimeMillis();
        this.startingTime = startingTime;
        this.racewayName = racewayName;
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
