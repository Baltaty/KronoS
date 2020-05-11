package com.kronos.controller;

import com.kronos.global.enums.RaceType;
import com.kronos.global.factory.RaceFactory;
import com.kronos.global.util.Alerts;
import com.kronos.global.util.Mask;
import com.kronos.model.LapRaceModel;
import com.kronos.model.RaceModel;
import com.kronos.model.TimeRaceModel;

import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author TeamKronoS
 * @version 1.0
 * Controller of the {@link com.kronos.model.RaceModel race} objects.
 */
public class RaceController {

    private static final int INVALID_INDEX = -1;

    /**
     * Constructor.
     */
    public RaceController() {
    }


    /**
     * Controls if the fields required for the creation of a race instance are valid,
     * otherwise an alert is sent to the view to call the user.
     *
     * @param raceModel
     * @return
     * @throws com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException
     */
    private boolean checkRaceControl(RaceModel raceModel) throws ParseException {
        boolean verify = true;

        if (!(Mask.isDate(new SimpleDateFormat("dd-MM-yyyy").format(raceModel.getStartingTime()))) || (Mask.validateDate(raceModel.getStartingTime()) == 1)) {

            verify = false;
            //Alerts sur l'element en question
        }


        if (raceModel instanceof TimeRaceModel) {
            if (!(Mask.isNumeric((String.valueOf(((TimeRaceModel) raceModel).getDuration()))))){
                verify = false;
                //Alerts sur l'element en question
            }


        } else {
            if (!(Mask.isNumeric((String.valueOf(((LapRaceModel) raceModel).getNumberOfLaps()))))
                    || ((LapRaceModel) raceModel).getNumberOfLaps() == INVALID_INDEX) {
                verify = false;

            }

        }

        return verify;
    }


    /**
     * Computes the end date of the race.
     *
     * @param beginOfRace date of beginning of th race
     * @param minutes     duration of the race in minutes
     * @return the end date of the race
     */
    private Date convertDate(final Date beginOfRace, int minutes) {

        if (minutes == INVALID_INDEX)
            return beginOfRace;

        Date endRace;
        long timestamp = beginOfRace.getTime();
        timestamp += (minutes * 60) * 1000;
        Timestamp timestamp_object = new Timestamp(timestamp);
        endRace = new Date(timestamp_object.getTime());
        return endRace;

    }

    /**
     * Creates a new {@link RaceModel race} object. It creates a factory which is able to create different types of race.
     * It can create two race types : a time race whose end is determined by a duration or a lap race whose end is determined by a number of laps.
     *
     * @param typeOfRace  a lap race or a time race
     * @param debutRace   beginning of the race
     * @param racewayName the name of the raceway
     * @param timeOfRace  duration of race (in case of a time race)
     * @param numbeOfLap  the number of laps (in case of a lap race)
     * @return the new {@link RaceModel race}
     */
    public RaceModel createRace(RaceType typeOfRace, String raceName, Date debutRace, String racewayName, int timeOfRace, int numbeOfLap, int relayInterval, int defaultMeanLapTime) {

        RaceModel race;
        RaceFactory raceFactory = new RaceFactory();

        this.convertDate(debutRace, timeOfRace);
        race = raceFactory.createRace(typeOfRace, raceName, debutRace, racewayName, this.convertDate(debutRace, timeOfRace), numbeOfLap, relayInterval, defaultMeanLapTime);


        try {
            if (!this.checkRaceControl(race)) {
                Alerts.error("ERREUR", "Veuillez verifier les champs");
                return null;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return race;
    }


}
