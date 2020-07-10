package com.kronos.parserXML.MainImpl;

import com.kronos.api.MainCar;
import com.kronos.api.Race;
import com.kronos.api.TimeRace;
import com.kronos.model.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CompleteSaveStrategy {

    private static String PATH = System.getProperty("user.dir") + File.separator + "data" + File.separator;
    private static ArrayList<TopModel> topsToSave = new ArrayList<>();

    public static void executeCompleteSave(RaceModel raceToSave, Map<Integer, ArrayList<TopModel>> topsToSaveMap, ArrayList<CarModel> carsToSave, ArrayList<PilotModel> pilotsToSave) {
        completeSave(raceToSave, topsToSaveMap, carsToSave, pilotsToSave);
    }

    private static void completeSave(RaceModel raceToSave, Map<Integer, ArrayList<TopModel>> topsToSaveMap, ArrayList<CarModel> carsToSave, ArrayList<PilotModel> pilotsToSave) {
        Document doc = new Document();
        Element saveElem = new Element("Save");
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        //saveRace
        saveRace(raceToSave, saveElem);
        //saveCars
        saveCars(carsToSave, raceToSave, saveElem);
        //savePilots
        savePilots(pilotsToSave, saveElem);
        //saveTops
        saveTops(topsToSaveMap, saveElem);
        doc.setRootElement(saveElem);
        try {
            LocalDateTime ldt = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss", Locale.FRENCH);
            xmlOutputter.output(doc, new FileOutputStream(PATH + "course_numero-" + System.currentTimeMillis() + "-" + formatter.format(ldt) + ".xml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void saveRace(RaceModel raceToSave, Element rootElem) {
        Element raceElem;
        if(raceToSave instanceof LapRaceModel) {
            raceElem = new Element("LapRace");
        }
        else {
            raceElem = new Element("TimeRace");
        }
        raceElem.addContent(new Element(("Name")).setText(raceToSave.getRaceName()));
        raceElem.addContent(new Element("Id").setText(raceToSave.getId().toString()));
        raceElem.addContent(new Element("StartingTime").setText(raceToSave.getStartingTime().toString()));
        raceElem.addContent(new Element("RacewayName").setText(raceToSave.getRacewayName()));
        raceElem.addContent(new Element("RaceState").setText(raceToSave.getRaceState().toString()));
        raceElem.addContent(new Element("TimeLapsRemaining").setText(raceToSave.getTimeLapsRemaining()));
        raceElem.addContent(new Element("TimeLapsSpent").setText(raceToSave.getTimeLapsSpent()));
        raceElem.addContent(new Element("RelayInterval").setText(Integer.toString(raceToSave.getRelayInterval())));
        raceElem.addContent(new Element("DefaultMeanLapTime").setText(Integer.toString(raceToSave.getDefaultMeanLapTime())));
        rootElem.addContent(raceElem);
        if(raceToSave instanceof LapRaceModel) {
            raceElem.addContent(new Element("NumberOfLaps").setText(Integer.toString(((LapRaceModel) raceToSave).getNumberOfLaps())));
        }
        else {
            raceElem.addContent(new Element("EndTime").setText(((TimeRaceModel) raceToSave).getEndTime().toString()));
        }
    }

    private static void saveCars(ArrayList<CarModel> carsToSave, RaceModel raceModel, Element rootElem) {
        for(CarModel car : carsToSave) {
            Element carElem;
            if(car instanceof MainCarModel) {
                carElem = new Element("MainCar");
            }
            else {
                carElem = new Element("RivalCar");
            }
            carElem.addContent(new Element("Id").setText(car.getId().toString()));
            carElem.addContent(new Element("Number").setText(Integer.toString(car.getNumber())));
            carElem.addContent(new Element("Team").setText(car.getTeam()));
            carElem.addContent(new Element("Model").setText(car.getModel()));
            carElem.addContent(new Element("Brand").setText(car.getBrand()));
            carElem.addContent(new Element("CompletedLaps").setText(Integer.toString(car.getCompletedLaps())));
            carElem.addContent(new Element("RaceId").setText(raceModel.getId().toString()));
            carElem.addContent(new Element("PilotId").setText(car.getPilotModel().getId().toString()));
            rootElem.addContent(carElem);
        }
    }

    private static void savePilots(ArrayList<PilotModel> pilotsToSave, Element rootElem) {
        for(PilotModel pilot : pilotsToSave) {
            Element pilotElem = new Element("Pilot");
            pilotElem.addContent(new Element("Id").setText(pilot.getId().toString()));
            pilotElem.addContent(new Element("FirstName").setText(pilot.getFirstName()));
            pilotElem.addContent(new Element("LastName").setText(pilot.getLastName()));
            pilotElem.addContent(new Element("Comment").setText(pilot.getComment()));
            pilotElem.addContent(new Element("DateOfBirth").setText(pilot.getDateOfBirth().toString()));
            pilotElem.addContent(new Element("Height").setText(Double.toString(pilot.getHeight())));
            pilotElem.addContent(new Element("Weight").setText(Double.toString(pilot.getWeight())));
            rootElem.addContent(pilotElem);
        }
    }

    private static void saveTops(Map<Integer, ArrayList<TopModel>> topsToSaveMap, Element rootElem) {
        for(ArrayList<TopModel> value : topsToSaveMap.values()) {
            for(TopModel top : value) {
                Element topElem = new Element("Top");
                topElem.addContent(new Element("Id").setText(top.getId().toString()));
                topElem.addContent(new Element("CarNumber").setText(Integer.toString(top.getCarNumber())));
                topElem.addContent(new Element("Position").setText(Integer.toString(top.getTopPosition())));
                topElem.addContent(new Element("Time").setText(top.getTime()));
                topElem.addContent(new Element("Type").setText(top.getTopType()));
                topElem.addContent(new Element("RaceTime").setText(top.getRaceTime()));
                topElem.addContent(new Element("Lap").setText(Integer.toString(top.getLap())));
                topElem.addContent(new Element("LapTime").setText(top.getLapTime()));
                topElem.addContent(new Element("Comment").setText(top.getComment()));
                rootElem.addContent(topElem);
            }
        }
    }

}
