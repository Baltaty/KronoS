package com.kronos.controller;

import com.jfoenix.controls.JFXButton;
import com.kronos.global.animation.PulseTransition;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RaceResumeController implements Initializable {
    @FXML
    ProgressBar meanTimeBar;
    @FXML
    Button TopBtn;
    @FXML
    private Label departureHour;

    @FXML
    private Label currentHour;

    @FXML
    private Label spentTime;

    @FXML
    private Label remainingTime;

    @FXML
    private JFXButton startRace;

    @FXML
    private JFXButton pauseRace;

    @FXML
    private JFXButton stopRace;

    Timeline spentTimeline;
    Timeline remainingTimeline;
    LocalTime time = LocalTime.parse("00:00:00");
    LocalTime localRemainningTime = LocalTime.parse("00:00:05");
    LocalTime time2 = LocalTime.parse("00:00");
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    LocalTime currentTime;



    private static ArrayList<Double> listOfMeanTime = new ArrayList<>();
    private static Double meantime = 0.00;
    PulseTransition pulseTransition;


    @FXML
    public void handleMeanTimeBar(ActionEvent actionEvent) {

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

    public void stopanimation() {

        pulseTransition.stop();
        System.out.println("bouton pressed");

    }

    /**
     * function to calculate the mean time of laps
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

    private void incrementTime() {
        time = time.plusSeconds(1);
        spentTime.setText(time.format(dtf));
    }
    private void decrementTime() {
        localRemainningTime = localRemainningTime.minusSeconds(1);
        if(localRemainningTime.equals(LocalTime.parse("00:00:00"))){
            stopRace.setDisable(true);
            startRace.setDisable(false);
            remainingTimeline.stop();
            spentTimeline.stop();
        }
        remainingTime.setText(localRemainningTime.format(dtf));
    }
    private void getCurrentTime() {
        currentTime = LocalTime.now();
        currentHour.setText(currentTime.format(dtf));
    }

    @FXML
    private void startTimer(ActionEvent event) {
        if(!localRemainningTime.equals(LocalTime.parse("00:00:00"))) {
            spentTimeline.play();
            remainingTimeline.play();
            departureHour.setText(currentTime.format(dtf));
            startRace.setDisable(true);
        }
    }

    @FXML
    private void pauseTimer(ActionEvent event) {
        if (spentTimeline.getStatus().equals(Animation.Status.PAUSED)) {
            spentTimeline.play();
            pauseRace.setText("Pause");
        } else if (spentTimeline.getStatus().equals(Animation.Status.RUNNING)) {
            spentTimeline.pause();
            pauseRace.setText("Continue");
        }
    }

    @FXML
    private void endTimer(ActionEvent event) {
        if (startRace.isDisable()) {
            spentTimeline.stop();
            remainingTimeline.stop();
            startRace.setDisable(false);
            time = LocalTime.parse("00:00:00");
            spentTime.setText(time.format(dtf));
            remainingTime.setText(time.format(dtf));

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        time = LocalTime.parse("00:00:00");
        time2 = LocalTime.parse("00:00");

        departureHour.setText(time2.format(dtf));
        spentTime.setText(time.format(dtf));
        remainingTime.setText(localRemainningTime.format(dtf));

        spentTimeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> incrementTime()));
        spentTimeline.setCycleCount(Animation.INDEFINITE);

        Timeline clock = new Timeline(new KeyFrame(Duration.millis(1000), e -> getCurrentTime()));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        remainingTimeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> decrementTime()));
        remainingTimeline.setCycleCount(Animation.INDEFINITE);

    }

}
