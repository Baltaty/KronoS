package com.kronos.model;

import java.util.ArrayList;

public abstract class CarModel {

    private long id;
    private int number;
    private String name;
    private String model;
    private String brand;
    private ArrayList<TopModel> topList;
    private RaceModel race;
    private int completedLaps;

    public CarModel(long id, int number, String name, String model, String brand, ArrayList<TopModel> topList, RaceModel race, int completedLaps) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.model = model;
        this.brand = brand;
        this.topList = new ArrayList<TopModel>();
        this.race = race;
        this.completedLaps = completedLaps;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ArrayList<TopModel> getTopList() {
        return topList;
    }

    public void setTopList(ArrayList<TopModel> topList) {
        this.topList = topList;
    }

    public RaceModel getRace() {
        return race;
    }

    public void setRace(RaceModel race) {
        this.race = race;
    }

    public int getCompletedLaps() {
        return completedLaps;
    }

    public void setCompletedLaps(int completedLaps) {
        this.completedLaps = completedLaps;
    }
}
