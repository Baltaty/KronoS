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


    /**
     * Constructor.
     */
    public TopModel() { };

    /**
     * Constructor.
     * @param time top time
     * @param topType top type.
     */
    public TopModel (Date time, String topType){
        this.time = time;
        this.topType = topType;
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


}
