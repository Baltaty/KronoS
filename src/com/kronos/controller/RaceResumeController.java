package com.kronos.controller;

import com.kronos.App;
import com.kronos.global.animation.PulseTransition;
import com.kronos.model.GenericParser;
import com.kronos.model.MainCarModel;
import com.kronos.module.main.Main;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RaceResumeController {
    @FXML
    ProgressBar meanTimeBar;
    @FXML
    Button TopBtn;
    @FXML
    Label lastNamePilotMainCar, firstNamePilotMainCar, dateOfBirthPilot, maincarBrand, mainCarModel, mainCarTeam;


    private static ArrayList<Double> listOfMeanTime = new ArrayList<>();
    private static Double meantime = 0.00;
    PulseTransition pulseTransition;

    @FXML
    public void handleMeanTimeBar(ActionEvent actionEvent) {

        maincarinformation();
        pulseTransition = new PulseTransition(meanTimeBar);
        listOfMeanTime.add(0.2);
        meantime = getMeanTime(listOfMeanTime);
        int timeToUpload = (int) (meantime * 60);
        stopanimation();

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                meanTimeBar.setStyle("-fx-accent: blue;");
                updateProgress(1, timeToUpload);
                for (int i = 0; i < timeToUpload; i++) {
                    updateProgress(i + 1, timeToUpload);
                    Thread.sleep(1000);
                }

                meanTimeBar.setStyle("-fx-accent: red;");
                pulseTransition.setCycleCount(PulseTransition.INDEFINITE);
                pulseTransition.play();
                return null;
            }
        };

        meanTimeBar.progressProperty().unbind();
        meanTimeBar.progressProperty().bind(task.progressProperty());
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();


    }

    /**
     * Pulse animation
     * stop animation on the progress Bar
     */

    public void stopanimation() {

        pulseTransition.stop();
        System.out.println("bouton pressed");

    }

    /**
     * Get's the mean Time to upload the progess Bar
     *
     * @param mylistoftime
     * @return meanTime  of laps
     */

    public double getMeanTime(ArrayList<Double> mylistoftime) {
        double meantimeaux = 0.00;
        if (mylistoftime.size() == 1) {
            meantimeaux = mylistoftime.get(0);
        } else if (mylistoftime.size() == 2) {
            meantimeaux = ((mylistoftime.get(0) + mylistoftime.get(1)) / 2);
        } else {
            int start = mylistoftime.size() - 2;
            meantimeaux = ((mylistoftime.get(start - 1) + mylistoftime.get(start) + mylistoftime.get(start + 1)) / 3);


        }

        return meantimeaux;
    }

    /**
     * Display main car information and the currently pilot
     * pilot information
     * first name
     * last name
     * birthday
     * <p>
     * car information
     * car model
     * car Brand
     * car Team
     */

    public void maincarinformation() {

        List<GenericParser> maincarinformation = App.getDataManager().getModels(MainCarModel.class);
        MainCarModel maincar = (MainCarModel) maincarinformation.get(0).getObjectToGenerify();
        lastNamePilotMainCar.setText(maincar.getPilotModel().getLastName());
        firstNamePilotMainCar.setText(maincar.getPilotModel().getFirstName());
        dateOfBirthPilot.setText(new SimpleDateFormat("dd-MM-yyyy").format(maincar.getPilotModel().getDateOfBirth()));
        maincarBrand.setText(maincar.getBrand());
        mainCarModel.setText(maincar.getModel());
        mainCarTeam.setText(maincar.getTeam());


    }


}
