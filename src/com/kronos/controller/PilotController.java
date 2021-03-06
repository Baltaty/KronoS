package com.kronos.controller;

import com.kronos.global.util.Mask;
import com.kronos.model.PilotModel;
import javafx.fxml.Initializable;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * @author TeamKronoS
 * @version 1.0
 * Controller of the {@link com.kronos.model.PilotModel pilot} objects.
 */
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
     * checked pilot informations
     *
     * @param pilot
     * @return true if all information about the pilot is correct
     */

    public Boolean checkPilot(PilotModel pilot) throws ParseException {
        boolean verify = true;
        if (!(Mask.isNumeric(String.valueOf(pilot.getId())))) {
            verify = false;
            // Alerts.error("error","mylong ");
        }
        if (pilot.getFirstName().isEmpty()) {
            verify = false;
        }
        if (!(pilot.getDateOfBirth() == null)) {
            if (!(Mask.isDate(new SimpleDateFormat("dd-MM-yyyy").format(pilot.getDateOfBirth()))) || (Mask.validateDate(pilot.getDateOfBirth()) == -1)) {

                verify = false;
                //Alerts sur l'element en question

            }
        }
        return verify;
    }


}

