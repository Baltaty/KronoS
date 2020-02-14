package com.kronos.model;

import com.kronos.api.MainCar;
import com.kronos.api.TimeRace;
import com.sun.xml.internal.txw2.annotation.XmlElement;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement
public class MainCarModel extends CarModel implements MainCar {


    private ArrayList<Integer> stopList;

    public MainCarModel() {

    }

    public MainCarModel(int number, String team, String model, String brand, PilotModel pilotModel) {
        super(number, team, model, brand, pilotModel);
        stopList = new ArrayList<>();
    }

    @Override
    public int getRemainingLapsBeforeStops() {
        return stopList.get(0) - getCompletedLaps();
    }

    @Override
    public ArrayList<Integer> getStopList() {
        return stopList;
    }

    @Override
    public void setStopList(ArrayList<Integer> stopList) {
        this.stopList = stopList;
    }
}
