package com.kronos.model;

import com.kronos.api.Top;
import com.kronos.parserXML.MainImpl.IncrementalSaveStrategy;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents a top and its properties. The top can be of three different types : R for Race (for a normal lap), I for In (when the car is entering into the pit-stop)
 * and O for Out (when the car is leaving the pit-stop).
 */
@XmlRootElement
public class TopModel implements Top, Comparable<TopModel> {

    private Long id;
    private int carNumber;
    private String time;
    private String topType;
    private String comment;
    private String lapTime;
    private String raceTime;    // Time Of Top
    private int lap;    // Nombre de tour
    private int topPosition;

    /**
     * Constructor.
     */
    public TopModel() { };

    /**
     * Constructor.
     * @param carNumber car number associated with this top
     * @param time top real time (date + hour)
     * @param topType top type
     * @param raceTime time in minutes since the beginning of the race
     * @param comment comment
     */
    public TopModel (int carNumber, int topPosition, String time, String topType, String raceTime, String lapTime, String comment){
        this.id = System.currentTimeMillis();
        this.carNumber = carNumber;
        this.time = time;
        this.topType = topType;
        this.raceTime = raceTime;
        this.lapTime = lapTime;
        this.comment = comment;
        this.topPosition = topPosition;
    }

    /**
     * Constructor.
     * @param carNumber car number associated with this top
     * @param time top real time (date + hour)
     * @param topType top type
     * @param lap lap when the top was triggered
     * @param comment comment
     */
    public TopModel (int carNumber, int topPosition, String time, String topType, int lap, String lapTime, String comment) {
        this.id = System.currentTimeMillis();
        this.carNumber = carNumber;
        this.time = time;
        this.topType = topType;
        this.lap = lap;
        this.lapTime = lapTime;
        this.comment = comment;
        this.topPosition = topPosition;
    }



    /**
     * Gets the top id.
     * @return the top id
     */
    public Long getId() { return id;  }

    /**
     * Sets the top id.
     * @param id top id
     */
    public void setId(Long id) { this.id = id;
        IncrementalSaveStrategy.logTopToSave(this);
    }

    /**
     * Gets the top car number.
     * @return carNumber
     */
    public int getCarNumber() {
        return carNumber;
    }

    /**
     * Sets the top car number
     * @param carNumber car number associated with this top
     */
    public void setCarNumber(int carNumber) {
        this.carNumber = carNumber;
        IncrementalSaveStrategy.logTopToSave(this);
    }

    /**
     * Gets the top time.
     * @return the top time
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the top time.
     * @param time top time
     */
    public void setTime(String time) {
        this.time = time;
        IncrementalSaveStrategy.logTopToSave(this);
    }

    /**
     * Gets the top type.
     * @return the top type
     */
    public String getTopType() {
        return topType;
    }

    /**
     * Sets the top type.
     * @param topType top type
     */
    public void setTopType(String topType) {
        this.topType = topType;
        IncrementalSaveStrategy.logTopToSave(this);
    }

    /**
     * Gets the comment.
     * @return the top comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment.
     * @param comment top comment
     */
    public void setComment(String comment) {
        this.comment = comment;
        IncrementalSaveStrategy.logTopToSave(this);
    }

    /**
     * Gets the race time, the time in minutes elapsed since the beginning of the race.
     * @return the race time
     */
    public String getRaceTime() {
        return raceTime;
    }

    /**
     * Sets the race time, the time in minutes elapsed since the beginning of the race.
     * @param raceTime race time
     */
    public void setRaceTime(String raceTime) {
        this.raceTime = raceTime;
        IncrementalSaveStrategy.logTopToSave(this);
    }

    /**
     * Gets the lap time, the time in minutes elapsed during the lap
     * @return the lap time
     */
    public String getLapTime() {
        return lapTime;
    }

    /**
     * Sets the lap time, the time in minutes elapsed during the lap.
     * @param lapTime lap time
     */
    public void setLapTime(String lapTime) {
        this.lapTime = lapTime;
        IncrementalSaveStrategy.logTopToSave(this);
    }

    /**
     * Gets the lap number.
     * @return the lap number
     */
    public int getLap() {
        return lap;
    }

    /**
     * Sets the lap number.
     * @param lap lap number
     */
    public void setLap(int lap) {
        this.lap = lap;
        IncrementalSaveStrategy.logTopToSave(this);
    }


    @Override
    public int compareTo(TopModel o) {
        return (Integer.compare(this.getTopPosition(), o.getTopPosition()));
    }

    public int getTopPosition() {
        return topPosition;
    }

    public void setTopPosition(int topPosition) {
        this.topPosition = topPosition;
        IncrementalSaveStrategy.logTopToSave(this);
    }

}
