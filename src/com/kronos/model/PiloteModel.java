package com.kronos.model;

import com.kronos.api.Pilote;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class PiloteModel implements Pilote {

    private  long id;
    private String firstName;
    private String lastName;
    private String comment;
    private Date dateOfBirth;
    private  double height;
    private double weight;

    public  PiloteModel(){}

    /**
     * contructor of the class PiloteModel
     * @param id
     * @param firstName
     * @param lastName
     * @param comment
     * @param dateOfBirth
     * @param height
     * @param weight
     */
    public PiloteModel(long id, String firstName, String lastName, String comment, Date dateOfBirth, double height, double weight) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.comment = comment;
        this.dateOfBirth = dateOfBirth;
        this.height = height;
        this.weight = weight;
    }

    /**
     * Get the unique identifier of the driver that will be generated in the constructor.
     *
     * @return long
     */
    @Override
    public long getId() {
        return id;
    }

    /**
     * Get the name of the driver
     *
     * @return String {@link}
     */
    @Override
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the first name of the driver
     *
     * @return String {@link}
     */
    @Override
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the comments added by the panel on driver during the race
     *
     * @return String {@link}
     */
    @Override
    public String getComment() {
        return comment;
    }

    /**
     * Get the pilot's date of birth
     *
     * @return Date
     */
    @Override
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Get the pilot's height of birth
     *
     * @return double
     */
    @Override
    public double getHeight() {
        return height;
    }

    /**
     * Get the pilot's date of birth
     *
     * @return double
     */
    @Override
    public double getWeight() {
        return weight;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
