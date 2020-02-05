package com.kronos.model;

import com.kronos.api.Top;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class TopModel implements Top {

    private Date horaire ;
    private String typeTop;


    public TopModel() { };

    public TopModel (Date horaire, String typeTop){
        this.horaire = horaire;
        this.typeTop = typeTop;
    }

    public Date getHoraire() {
        return horaire;
    }

    public void setHoraire(Date horaire) {
        this.horaire = horaire;
    }

    public String getTypeTop() {
        return typeTop;
    }

    public void setTypeTop(String typeTop) {
        this.typeTop = typeTop;
    }


}
