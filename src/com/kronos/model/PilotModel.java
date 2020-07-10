package com.kronos.model;

import com.kronos.api.MainCar;
import com.kronos.api.Pilot;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents the pilot and his properties.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PilotModel implements Pilot {

    private Long id;
    private String firstName;
    private String lastName;
    private String comment;
    private Date dateOfBirth;
    private double height;
    private double weight;
    private MainCarModel pilotecar;


    /**
     * Constructor.
     */
    public PilotModel() {
    }

    /**
     * Constructor.
     *
     * @param firstName pilot first name
     * @param lastName pilot last name
     * @param comment comment
     * @param dateOfBirth pilot date of birth
     * @param height pilot height
     * @param weight pilot weight
     */
    public PilotModel(String firstName, String lastName, String comment, Date dateOfBirth, double height, double weight) {
        this.id = System.currentTimeMillis();
        this.firstName = firstName;
        this.lastName = lastName;
        this.comment = comment;
        this.dateOfBirth = dateOfBirth;
        this.height = height;
        this.weight = weight;
    }

    /**
     * Gets the unique identifier of the driver that will be generated in the constructor.
     * @return the pilot id
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Gets the first name of the pilot.
     * @return the pilot's first name
     */
    @Override
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the pilot.
     * @return the pilot's last name
     */
    @Override
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the comments about the pilot.
     * @return the comment about the pilot
     */
    @Override
    public String getComment() {
        return comment;
    }

    /**
     * Gets the pilot's date of birth.
     * @return the pilot's date of birth
     */
    @Override
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Gets the pilot's height.
     * @return the pilot's height
     */
    @Override
    public double getHeight() {
        return height;
    }

    /**
     * Gets the pilot weight.
     * @return the pilot's weight
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Gets the pilot's car.
     * @return the pilot's car
     */
    @Override
    public MainCar getPilotCar() {
        return pilotecar;
    }


    /**
     * Sets the pilot id.
     * @param id pilot id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Sets the pilot first name.
     * @param firstName pilot's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the pilot's last name.
     * @param lastName pilot's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the comment about the pilot.
     * @param comment the comments pilot
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Sets the pilot's date of birth.
     * @param dateOfBirth pilot's date of birth
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Sets the pilot's height.
     * @param height pilot's height
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Sets the pilot's weight.
     * @param weight pilot's weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Sets the pilot's car.
     * @param pilotecar pilot's car
     */
    public void setPilotecar(MainCarModel pilotecar) {
        this.pilotecar = pilotecar;
    }
}
