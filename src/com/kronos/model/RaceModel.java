package com.kronos.model;

import com.kronos.api.Race;

import java.util.ArrayList;
import java.util.Date;

public abstract class RaceModel implements Race {

    private long id;
    private Date startingTime;
    private String racewayName;
    private ArrayList<CarModel> carsList;


    public RaceModel() {

    }

    public RaceModel(long id, Date startingTime, String racewayName) {
        this.id = id;
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
