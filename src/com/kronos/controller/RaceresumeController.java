package com.kronos.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;

import java.util.ArrayList;

public class RaceresumeController {
    @FXML
    ProgressBar meanTimeBar;
    @FXML
    Button TopBtn;

   private static ArrayList<Double> listOfMeanTime = new ArrayList<>();
   private static Double meantime = 0.00;


    @FXML
    public void handleMeanTimeBar(ActionEvent actionEvent) {
        listOfMeanTime.add((double) 2);
        meantime = getMeanTime(listOfMeanTime);
        System.out.println(meantime);
        int timeToUpload = (int) (meantime * 60);

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                meanTimeBar.setStyle("-fx-accent: blue");
                updateProgress(1, timeToUpload);
                for (int i = 0; i < timeToUpload; i++) {
                    updateProgress(i + 1, timeToUpload);
                    Thread.sleep(1000);
                }
                meanTimeBar.setStyle("-fx-accent: red");
                /*pulse.setCycleCount(PulseTransition.INDEFINITE);
                pulse.play();
*/

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
     * function to calculate the mean time of laps
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


}
