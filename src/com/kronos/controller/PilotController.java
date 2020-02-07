package com.kronos.controller;

import com.kronos.global.util.Mask;
import com.kronos.model.PilotModel;
import com.kronos.parserXML.MainImpl.SaveManagerImpl;
import javafx.fxml.Initializable;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class PilotController implements Initializable {


    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     *  checked pilot informations
     * @param pilot
     * @return true if all information about the pilot is correct
     */

    public Boolean checkingofpilot(PilotModel pilot) throws ParseException {
        boolean verify = true;
        if (!(Mask.isNumeric(String.valueOf(pilot.getId())))) {
            verify = false;
            // Alerts.error("error","mylong ");
        }
        if (!(Mask.isSimpleString(pilot.getLastName()))) {
            verify = false;
        }

        if (!(Mask.isDate(new SimpleDateFormat("dd-MM-yyyy").format(pilot.getDateOfBirth()))) || (Mask.validateDate(pilot.getDateOfBirth())==-1)) {

            verify = false;
            //Alerts sur l'element en question
        }
        if (!(Mask.isDouble(Double.toString(pilot.getWeight())))) {

            verify = false;
            //action a faire su IHM
        }
        if (!(Mask.isDouble(Double.toString(pilot.getHeight())))) {
            verify = false;

        }

        return verify;
    }

    /**
     * created and register pilot informations in the file
     * @param pilot
     * @return true if  all informations about the pilot is ok
     */
    public Boolean creationfpilot(PilotModel pilot) throws ParseException {

        if (checkingofpilot(pilot)) {
            //register of the pilot
            SaveManagerImpl saveManager = SaveManagerImpl.getInstance();
            saveManager.persist(pilot);
            saveManager.saveFile();
            return true;

        }

        return false;
    }



    public static void main(String[] args) throws ParseException {
        PilotModel pilote = new PilotModel(1, "test ", "test", "Yeaver", new Date(), 3.0, 3.0);
        PilotController ptmanager =  new PilotController();
        System.out.println(ptmanager.creationfpilot(pilote));
    }




}
