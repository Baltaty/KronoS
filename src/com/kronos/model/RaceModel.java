package com.kronos.model;

import com.kronos.api.Race;

import java.util.Date;

public abstract class RaceModel implements Race {

    private long id;
    private Date startingTime;
    private String racewayName;

    public RaceModel(long id, Date startingTime, String racewayName) {
        this.id = id;
        this.startingTime = startingTime;
        this.racewayName = racewayName;
    }

    public long getid() {
        return 0;
    }

    @Override
    public Date getstartingTime() {
        return null;
    }

    @Override
    public String getRacewayName() {
        return null;
    }
}
