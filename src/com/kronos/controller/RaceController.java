package com.kronos.controller;

import com.kronos.global.enums.RaceType;
import com.kronos.global.factory.RaceFactory;
import com.kronos.global.util.Alerts;
import com.kronos.global.util.Mask;
import com.kronos.model.LapRaceModel;
import com.kronos.model.RaceModel;
import com.kronos.model.TimeRaceModel;
import com.kronos.parserXML.MainImpl.SaveManagerImpl;
import com.kronos.parserXML.api.SaveManager;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;

public class RaceController {

    /**
     *
     */
    public RaceController() {
    }


    /**
     * Control if the fields required for the creation of a race instance are valid,
     * otherwise an alert is sent to the view to call the user.
     *
     * @param raceModel
     * @return
     * @throws com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException
     */
    private boolean checkRaceControl(RaceModel raceModel) throws ParseException {
        boolean verify = true;

        if (!(Mask.isDate(new SimpleDateFormat("dd-MM-yyyy").format(raceModel.getstartingTime()))) || (Mask.validateDate(raceModel.getstartingTime()) == 1)) {

            verify = false;
            //Alerts sur l'element en question
        }

        if (!(Mask.isSimpleString(raceModel.getRacewayName()))) {
            verify = false;
            //Alerts sur l'element en question
        }


        if (raceModel instanceof TimeRaceModel) {
            if (!(Mask.isNumeric((String.valueOf(((TimeRaceModel) raceModel).getDuration()))))) {
                verify = false;
                //Alerts sur l'element en question
            }

        } else {
            if (!(Mask.isNumeric((String.valueOf(((LapRaceModel) raceModel).getNumberOfLaps()))))) {
                verify = false;
            }

        }

        return verify;
    }


    /**
     *
     * @param beginOfRace
     * @param minutes
     * @return
     */
    private Date convertDate(final Date beginOfRace, int minutes) {
        Date endRace;
        long timestamp = beginOfRace.getTime();
        timestamp += (minutes * 60) * 1000;
        Timestamp timestamp_object = new Timestamp(timestamp);
        endRace = new Date(timestamp_object.getTime());
        return endRace;

    }

    /**
     * @param type
     * @param debutRace
     * @param racewayName
     * @param timeOfRace
     * @param numbeOfLap
     * @return
     */
    public RaceModel createRace(RaceType type, Date debutRace, String racewayName, int timeOfRace, int numbeOfLap) {

        RaceModel race;
        RaceFactory raceFactory = new RaceFactory();

        this.convertDate(debutRace, timeOfRace);
        race = raceFactory.createRace(RaceType.LAP_RACE, debutRace, racewayName, this.convertDate(debutRace, timeOfRace), numbeOfLap);

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
