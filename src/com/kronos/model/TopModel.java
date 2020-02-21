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


    private Date horaire ;
    private String typeTop;


    /**
     * Constructor.
     */
    public TopModel() { };

    /**
     * Constructor.
     * @param horaire top time
     * @param typeTop top type.
     */
    public TopModel (Date horaire, String typeTop){
        this.horaire = horaire;
        this.typeTop = typeTop;
    }

    /**
     * Gets the top time.
     * @return the top time
     */
    public Date getHoraire() {
        return horaire;
    }

    /**
     * Sets the top time.
     * @param horaire top time
     */
    public void setHoraire(Date horaire) {
        this.horaire = horaire;
    }

    /**
     * Gets the top type
     * @return the top type
     */
    public String getTypeTop() {
        return typeTop;
    }

    /**
     * Sets the top type.
     * @param typeTop top type
     */
    public void setTypeTop(String typeTop) {
        this.typeTop = typeTop;
    }


}
