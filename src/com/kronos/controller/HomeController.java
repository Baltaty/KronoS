    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kronos.controller;


//import com.fxexperience.javafx.animation.FlashTransition;
//import com.fxexperience.javafx.animation.FlipTransition;
//import com.fxexperience.javafx.animation.*;

import com.jfoenix.controls.*;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.kronos.module.main.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 *
 * @author TeamKronos
 */
public class HomeController implements Initializable {
    @FXML
    private JFXButton  startbtn;

    @FXML
    private JFXButton bdbtn;

    @FXML
    private JFXButton settingbtn;

    @FXML
    private ImageView newraceicon;

    @FXML
    private ImageView setingicon;

    @FXML
    private ImageView bdicon;

    @FXML
    private ImageView appname1;

    @FXML
    private StackPane homestack;

    @FXML
    private JFXDialogLayout dialog_para;

    @FXML
    private JFXButton end_para;

    @FXML
    private JFXDialogLayout dialayout;

    @FXML
    private JFXTextField top_touch_field;

    @FXML
    private JFXDialogLayout dialog_new_race;

    @FXML
    private JFXTabPane NewRaceTabPane;

    @FXML
    private Tab tab_pilote;

    @FXML
    private Tab tab_voiture;

    @FXML
    private Tab tab_course;

    @FXML
    private JFXButton btn_next_car;

    @FXML
    private JFXButton btn_next_lap;

    @FXML
    private ImageView boulon;

    private boolean changeRequest;

    @FXML
    private void handleNewRaceClicked(ActionEvent event)
    {
          System.out.println("amorçage du processus de démarrage d'une course");
        dialog_new_race.setVisible(true);
            JFXDialog alert1= new JFXDialog(homestack,dialog_new_race,JFXDialog.DialogTransition.TOP);
            alert1.show();
      
          }
    @FXML
    private void handleNewRaceEntered()
    {
        //////SCALE ICONE BASE DE DONNEES

       /* ScaleTransition scalebdicon=new ScaleTransition(Duration.seconds(2), bdicon);
        scalebdicon.setToX(1.2);
        scalebdicon.setToY(1.2);
        scalebdicon.setCycleCount(ScaleTransition.INDEFINITE);
        scalebdicon.setAutoReverse(true);
        scalebdicon.play();*/

//        FlashTransition flash=new FlashTransition(bdicon) ;
//        flash.setCycleCount(3);
//        flash.play();

        //////

       /*  ////// ROTATION BOUTON BASE DE DONNEES

        RotateTransition rotatebd=new RotateTransition(Duration.seconds(1), bdbtn);
        RotateTransition rotatebdicon=new RotateTransition(Duration.seconds(1),bdicon);

        rotatebd.setByAngle(360);
        rotatebdicon.setByAngle(360);


         rotatebd.setCycleCount(1);
         rotatebdicon.setCycleCount(1);

         rotatebd.play();
         rotatebdicon.play();

        //////*/
    }
    @FXML
    private void handleSettingClicked()
    {
        System.out.print("Ouverture des paramètres\n");
        dialog_para.setVisible(true);
        JFXDialog alert= new JFXDialog(homestack,dialog_para,JFXDialog.DialogTransition.CENTER);

        end_para.setOnAction((ActionEvent event) -> {
            alert.close();
        });

        alert.show();

        ////// ROTATION BOULON
        RotateTransition rotateboulon=new RotateTransition(Duration.seconds(5), boulon);
        rotateboulon.setByAngle(360);
        rotateboulon.setCycleCount(RotateTransition.INDEFINITE);
        rotateboulon.play();
        //////

    }
    @FXML
    private void handleSettingEntered()
    {
        ////// ROTATION BOUTON SETTINGS
        RotateTransition rotatepara=new RotateTransition(Duration.seconds(1), settingbtn);
        RotateTransition rotateparaicon=new RotateTransition(Duration.seconds(1),setingicon);

        rotatepara.setByAngle(360);
        rotateparaicon.setByAngle(360);


        rotatepara.setCycleCount(1);
        rotateparaicon.setCycleCount(1);

        rotatepara.play();
        rotateparaicon.play();

        //////
    }
    @FXML
    private void handleOldRaceClicked(ActionEvent event)
    {
         try {
            Stage stage= (Stage)startbtn.getScene().getWindow();
            StackPane test=FXMLLoader.load(getClass().getResource("Racechoice.fxml"));
            stage.setScene(new Scene(test));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    @FXML
    private void handleOldRaceEntered(ActionEvent event)
    {
        try {
            Stage stage= (Stage)startbtn.getScene().getWindow();
            StackPane test=FXMLLoader.load(getClass().getResource("Racechoice.fxml"));
            stage.setScene(new Scene(test));
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleSwitchToCarTab(ActionEvent event)
    {
        SingleSelectionModel<Tab> selectionModel = NewRaceTabPane.getSelectionModel();
        tab_voiture.setDisable(false);
        selectionModel.select(tab_voiture);
    }
    @FXML
    private void handleSwitchToLapTab(ActionEvent event)
    {
        SingleSelectionModel<Tab> selectionModel = NewRaceTabPane.getSelectionModel();
        tab_course.setDisable(false);
        selectionModel.select(tab_course);
    }

    @FXML
    private void handleChangeTopTouch(ActionEvent event) {
        changeRequest = true ;
        Scene scene = homestack.getScene();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                if (changeRequest){
                    KeyCode keyCode = event.getCode();
                    System.out.println(keyCode.toString());
                    File file = new File("top.properties");
                    Properties properties = new Properties();
                    try {
                        if(!file.exists()){

                            file.createNewFile();
                        }
                        else {

                            FileInputStream fileInputStream = new FileInputStream(file);
                            properties.load(fileInputStream);
                            properties.put("key", keyCode.toString());
                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            properties.store(fileOutputStream, "Top properties");
                            System.out.println(" saved !");
                            top_touch_field.setText(properties.getProperty("key"));
                            changeRequest = false ;
                        }

                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

//             WobbleTransition fade=new WobbleTransition(appname1);
//             fade.setDelay(Duration.seconds(3));
//             fade.setRate(0.1);
//             fade.setCycleCount(WobbleTransition.INDEFINITE);
//             fade.play();
//

            ////// SCALE BOUTON DEMARRER
              ScaleTransition scalestart =new ScaleTransition(Duration.seconds(1), startbtn);
              scalestart.setToX(1.2);
              scalestart.setToY(1.2);
              scalestart.setAutoReverse(true);
              scalestart.setCycleCount(ScaleTransition.INDEFINITE);
              scalestart.play();
              
            ////// SCALE BOUTON SETTINGS
              ScaleTransition scalepara=new ScaleTransition(Duration.seconds(1), settingbtn);
              scalepara.setToX(1.2);
              scalepara.setToY(1.2);
              scalepara.setAutoReverse(true);
              scalepara.setCycleCount(ScaleTransition.INDEFINITE);
              scalepara.play();
            //////

            ////// SCALE BOUTON  BASE DE DONNEES
              ScaleTransition scalebd=new ScaleTransition(Duration.seconds(1), bdbtn);
              scalebd.setToX(1.2);
              scalebd.setToY(1.2);
              scalebd.setAutoReverse(true);
              scalebd.setCycleCount(ScaleTransition.INDEFINITE);
              scalebd.play();
            //////
            changeRequest = false;
        File file = new File("top.properties");
        Properties properties = new Properties();
            try{
                FileInputStream fileInputStream = new FileInputStream(file);
                properties.load(fileInputStream);
                //FileOutputStream fileOutputStream = new FileOutputStream(file);
                top_touch_field.setText(properties.getProperty("key"));
            }
            catch (IOException io){

            }

        //top_touch_field

        }
    }