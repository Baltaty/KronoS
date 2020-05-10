package com.kronos.model;

import com.kronos.api.Race;
import com.kronos.global.enums.RaceState;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private RaceState raceState;
    private String timeLapsRemaining;
    private String timeLapsSpent;

    private int relayInterval;
    private int defaultMeanLapTime;
    private ArrayList<CarModel> carsList;
    private Map<Integer, ArrayList<TopModel>> topsMap = new HashMap<>();


    /**
     * Constructor.
     */
    public RaceModel() {

    }

    /**
     * @param raceName
     * @param startingTime
     * @param racewayName
     */
    public RaceModel(String raceName, Date startingTime, String racewayName, int relayInterval, int defaultMeanLapTime) {
        this.raceName = raceName;
        this.id = System.currentTimeMillis();
        this.startingTime = startingTime;
        this.racewayName = racewayName;
        this.carsList = new ArrayList<>();
        this.raceState= RaceState.CREATION;
        this.timeLapsRemaining="00";
        this.timeLapsSpent="00";
        this.relayInterval = relayInterval;
        this.defaultMeanLapTime = defaultMeanLapTime;
        System.out.println("relayInterval"+relayInterval);
    }



    /**
     * Gets the race id.
     *
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
     *
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

    public Map<Integer, ArrayList<TopModel>> getTopsMap() {
        return topsMap;
    }

    public void setTopsMap(Map<Integer, ArrayList<TopModel>> topsMap) {
        this.topsMap = topsMap;
    }

    @XmlElement
    public RaceState getRaceState() {
        return raceState;
    }

    public void setRaceState(RaceState raceState) {
        this.raceState = raceState;
    }

    @XmlElement
    public String getTimeLapsRemaining() {
        return timeLapsRemaining;
    }

    public void setTimeLapsRemaining(String timeLapsRemaining) {
        this.timeLapsRemaining = timeLapsRemaining;
    }

    @XmlElement
    public String getTimeLapsSpent() {
        return timeLapsSpent;
    }

    public void setTimeLapsSpent(String timeLapsSpent) {
        this.timeLapsSpent = timeLapsSpent;
    }

    @Override
    public int getRelayInterval() {
        return relayInterval;
    }

    @Override
    public int getDefaultMeanLapTime() {
        return defaultMeanLapTime;
    }

}
