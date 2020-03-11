package com.kronos.model;

import com.kronos.api.Top;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents a top and its properties. The top can be of three different types : R for Race (for a normal lap), I for In (when the car is entering into the pit-stop)
 * and O for Out (when the car is leaving the pit-stop).
 */
@XmlRootElement
public class TopModel implements Top {


    private Date time;
    private String topType;
    private String comment;
    private double raceTime;
    private int lap;


    /**
     * Constructor.
     */
    public TopModel() { };

    /**
     * Constructor.
     * @param time top real time (date + hour)
     * @param topType top type
     * @param raceTime time in minutes since the beginning of the race
     * @param comment comment
     */
    public TopModel (Date time, String topType, double raceTime, String comment){
        this.time = time;
        this.topType = topType;
        this.raceTime = raceTime;
        this.comment = comment;
    }

    /**
     * Constructor.
     * @param time top real time (date + hour)
     * @param topType top type
     * @param lap lap when the top was triggered
     * @param comment comment
     */
    public TopModel (Date time, String topType, int lap, String comment) {
        this.time = time;
        this.topType = topType;
        this.lap = lap;
        this.comment = comment;
    }

    /**
     * Gets the top time.
     * @return the top time
     */
    public Date getTime() {
        return time;
    }

    /**
     * Sets the top time.
     * @param time top time
     */
    public void setTime(Date time) {
        this.time = time;
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
    }

    /**
     * Gets the race time, the time in minutes elapsed since the beginning of the race.
     * @return the race time
     */
    public double getRaceTime() {
        return raceTime;
    }

    /**
     * Sets the race time, the time in minutes elapsed since the beginning of the race.
     * @param raceTime race time
     */
    public void setRaceTime(double raceTime) {
        this.raceTime = raceTime;
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
    }


}
