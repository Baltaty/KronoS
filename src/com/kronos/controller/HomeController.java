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
    import com.kronos.global.factory.RaceFactory;
    import com.kronos.global.util.Alerts;
    import com.kronos.global.util.Mask;
    import com.kronos.model.*;
    import com.kronos.parserXML.MainImpl.SaveManagerImpl;
    import com.kronos.parserXML.api.SaveManager;
    import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
    import javafx.animation.RotateTransition;
    import javafx.animation.ScaleTransition;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.event.ActionEvent;
    import javafx.event.EventHandler;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.fxml.Initializable;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.image.ImageView;
    import javafx.scene.input.KeyCode;
    import javafx.scene.input.KeyEvent;
    import javafx.scene.layout.StackPane;
    import javafx.stage.Stage;
    import javafx.util.Duration;

    import java.io.*;
    import java.net.URL;
    import java.time.LocalDate;
    import java.time.ZoneId;
    import java.util.ArrayList;
    import java.text.SimpleDateFormat;
    import java.util.Date;
    import java.util.Properties;
    import java.util.ResourceBundle;
    import java.util.logging.Level;
    import java.util.logging.Logger;

    import com.kronos.global.enums.RaceType;
    import com.kronos.global.util.Alerts;
    import com.kronos.model.CarModel;
    import com.kronos.model.PilotModel;
    import com.kronos.module.main.Main;
    import javafx.collections.FXCollections;
    import javafx.event.Event;
    import javafx.event.EventHandler;
    import javafx.fxml.FXML;
    import javafx.fxml.Initializable;
    import javafx.animation.*;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.image.ImageView;
    import javafx.scene.input.KeyCode;
    import javafx.scene.input.KeyEvent;
    import javafx.scene.layout.StackPane;
    import javafx.stage.Stage;
    import javafx.util.Duration;

    /**
     * @author TeamKronos
     */
    public class HomeController implements Initializable {
        private static PilotController pilotcontroller = new PilotController();
        private static CarController carController = new CarController();
        private ArrayList<PilotModel> pilotsList = new ArrayList<>();
        private ArrayList<CarModel> carsList = new ArrayList();
        private boolean mainCarCreated;

        @FXML
        private JFXButton startbtn;

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
        private JFXDialogLayout dialog_select_key;


        @FXML
        private JFXButton end_para;

        @FXML
        private JFXDialogLayout dialayout;

        @FXML
        private Label top_key;

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

        @FXML
        private TextField carNumber;
        @FXML
        private TextField carTeam;
        @FXML
        private TextField carModel;
        @FXML
        private TextField carBrand;
        @FXML
        private ComboBox<String> carPilot;
        @FXML
        private ComboBox<String> carType;
        @FXML
        private JFXTextField lastnamepilot;
        @FXML
        private JFXTextField firstname;
        @FXML
        private JFXTextField pilotweight;
        @FXML
        private JFXTextField pilotheight;
        @FXML
        private JFXTextArea commentpilot;
        @FXML
        JFXDatePicker dateofbirthpilot;


        //////////////////////////////////////// Attributes of races data /////////////////////////////////////

        @FXML
        private JFXDatePicker startingTime_date;
        @FXML
        private JFXTextField racewayName_text;
        @FXML
        private JFXTextField race_duration;
        @FXML
        private Label race_duration_label;
        @FXML
        private JFXTextField race_numberof_tour;
        @FXML
        private Label race_numberof_tour_label;
        @FXML
        private JFXComboBox<String> race_type_combo;

        private RaceType typeOfRace;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////


        private static Boolean changeRequest;

        @FXML
        private void handleNewRaceClicked(ActionEvent event) {
            System.out.println("amorçage du processus de démarrage d'une course");
            dialog_new_race.setVisible(true);
            JFXDialog alert1 = new JFXDialog(homestack, dialog_new_race, JFXDialog.DialogTransition.TOP);
            alert1.show();

        }

        @FXML
        private void handleNewRaceEntered() {
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
        private void handleSettingClicked() {
            System.out.print("Ouverture des paramètres\n");
            dialog_para.setVisible(true);
            JFXDialog alert = new JFXDialog(homestack, dialog_para, JFXDialog.DialogTransition.CENTER);

            end_para.setOnAction((ActionEvent event) -> {
                alert.close();
            });

            alert.show();

        }

        @FXML
        private void handleSettingEntered() {
            ////// ROTATION BOUTON SETTINGS
            RotateTransition rotatepara = new RotateTransition(Duration.seconds(1), settingbtn);
            RotateTransition rotateparaicon = new RotateTransition(Duration.seconds(1), setingicon);

            rotatepara.setByAngle(360);
            rotateparaicon.setByAngle(360);


            rotatepara.setCycleCount(1);
            rotateparaicon.setCycleCount(1);

            rotatepara.play();
            rotateparaicon.play();

            //////
        }

        @FXML
        private void handleOldRaceClicked(ActionEvent event) {
            try {
                Stage stage = (Stage) startbtn.getScene().getWindow();
                StackPane test = FXMLLoader.load(getClass().getResource("Racechoice.fxml"));
                stage.setScene(new Scene(test));
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @FXML
        private void handleOldRaceEntered(ActionEvent event) {
            try {
                Stage stage = (Stage) startbtn.getScene().getWindow();
                StackPane test = FXMLLoader.load(getClass().getResource("Racechoice.fxml"));
                stage.setScene(new Scene(test));
                stage.show();

            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @FXML
        private void handleSwitchToCarTab(ActionEvent event) {
            if (pilotsList.size() !=0) {
                SingleSelectionModel<Tab> selectionModel = NewRaceTabPane.getSelectionModel();
                tab_voiture.setDisable(false);
                selectionModel.select(tab_voiture);
            }
            else {
                Alerts.error("ERREUR", "Veuillez créer au moins un pilote");
            }
        }

        @FXML
        private void handleSwitchToLapTab(ActionEvent event) {

            if (carsList.size() != 0) {
                SingleSelectionModel<Tab> selectionModel = NewRaceTabPane.getSelectionModel();
                tab_course.setDisable(false);
                selectionModel.select(tab_course);
            } else {
                Alerts.error("ERREUR", "Veuillez créer au moins une voiture");
            }
        }


        @FXML
        private void handleChangeTopTouch(ActionEvent event) {
            changeRequest = true;
            Alerts.info("CHANGEMENT TOP KEY", "Veuillez appuyer sur la nouvelle touche puis sour ok");
            //scene.setOnKeyPressed();
//        dialog_select_key.setVisible(true);
//        JFXDialog alertkey= new JFXDialog(homestack,dialog_select_key,JFXDialog.DialogTransition.CENTER);
            Scene scene = homestack.getScene();
            EventHandler<KeyEvent> e = new EventHandler<KeyEvent>() {
                @Override
                public void handle(javafx.scene.input.KeyEvent event) {
                    if (changeRequest) {
                        KeyCode keyCode = event.getCode();
                        System.out.println(keyCode.toString());
                        File file = new File("top.properties");
                        Properties properties = new Properties();
                        try {
                            if (!file.exists()) {

                                file.createNewFile();
                            } else {

                                FileInputStream fileInputStream = new FileInputStream(file);
                                properties.load(fileInputStream);
                                properties.put("key", keyCode.toString());
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                properties.store(fileOutputStream, "Top properties");
                                System.out.println(" saved !");
                                top_key.setText(properties.getProperty("key"));
                                changeRequest = false;
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            scene.addEventHandler(KeyEvent.KEY_PRESSED, e);
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
            ScaleTransition scalestart = new ScaleTransition(Duration.seconds(1), startbtn);
            scalestart.setToX(1.2);
            scalestart.setToY(1.2);
            scalestart.setAutoReverse(true);
            scalestart.setCycleCount(ScaleTransition.INDEFINITE);
            scalestart.play();

            ////// SCALE BOUTON SETTINGS
            ScaleTransition scalepara = new ScaleTransition(Duration.seconds(1), settingbtn);
            scalepara.setToX(1.2);
            scalepara.setToY(1.2);
            scalepara.setAutoReverse(true);
            scalepara.setCycleCount(ScaleTransition.INDEFINITE);
            scalepara.play();
            //////

            ////// SCALE BOUTON  BASE DE DONNEES
            ScaleTransition scalebd = new ScaleTransition(Duration.seconds(1), bdbtn);
            scalebd.setToX(1.2);
            scalebd.setToY(1.2);
            scalebd.setAutoReverse(true);
            scalebd.setCycleCount(ScaleTransition.INDEFINITE);
            scalebd.play();
            //////

            changeRequest = false;
            File file = new File("top.properties");
            Properties properties = new Properties();
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                properties.load(fileInputStream);
                //FileOutputStream fileOutputStream = new FileOutputStream(file);
                top_key.setText(properties.getProperty("key"));
            } catch (IOException io) {
            }


            carType.setItems(FXCollections.observableArrayList("Voiture principale", "Voiture concurrente"));
            race_type_combo.setItems(FXCollections.observableArrayList(RaceType.LAP_RACE.toString(), RaceType.TIME_RACE.toString()));

            //top_touch_field

        }

        /**
         * Finds the {@link PilotModel pilot} object corresponding to the pilot selected in the {@link ComboBox combo box} of pilots.
         *
         * @param index pilot index in the {@link ComboBox combo box} of pilots
         * @return the correct {@link PilotModel pilot} object
         */
        private PilotModel findPilot(int index) {
            PilotModel pilot = null;
            try {
                pilot = pilotsList.get(index);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return pilot;
        }

        /**
         * Handles the creation of a new car on click on the "add" button in the car creation interface.
         *
         * @param event the {@link ActionEvent action event}
         */
        @FXML
        public void handleClickNewCar(ActionEvent event) {
            if(carPilot.getSelectionModel().getSelectedItem() != null || carType.getSelectionModel().getSelectedItem() != null) {
                if(!mainCarCreated && carType.getSelectionModel().getSelectedItem().equals("Voiture principale")) {
                    MainCarModel mainCarModel = new MainCarModel(Integer.parseInt(carNumber.getText()), carTeam.getText(), carModel.getText(), carBrand.getText(), findPilot(carPilot.getSelectionModel().getSelectedIndex()));
                    if(checkNewCarFields(mainCarModel)) {
                        carsList.add(mainCarModel);
                        mainCarCreated = true;
                        carPilot.getItems().remove(carPilot.getSelectionModel().getSelectedIndex());
                        Alerts.success("SUCCÈS", "Nouvelle voiture créée");
                        clearNewCarFields();
                    }
                }
                else if(mainCarCreated && carType.getSelectionModel().getSelectedItem().equals("Voiture concurrente")) {
                    RivalCarModel rivalCarModel = new RivalCarModel(Integer.parseInt(carNumber.getText()), carTeam.getText(), carModel.getText(), carBrand.getText(), findPilot(carPilot.getSelectionModel().getSelectedIndex()));
                    if(checkNewCarFields(rivalCarModel)) {
                        carsList.add(rivalCarModel);
                        carPilot.getItems().remove(carPilot.getSelectionModel().getSelectedIndex());
                        Alerts.success("SUCCÈS", "Nouvelle voiture créée");
                        clearNewCarFields();
                    }
                }
                else if(mainCarCreated) {
                    Alerts.error("ERREUR", "Une voiture principale existe déjà");
                }
                else {
                    Alerts.error("ERREUR", "Veuillez commencer par créer une voiture principale");
                }
            }
            else {
                Alerts.error("ERREUR", "Veuillez vérifier les champs");
            }
        }

        /**
         * Resets the fields in the new car interface.
         */
        private void clearNewCarFields() {
            carNumber.clear();
            carTeam.clear();
            carModel.clear();
            carBrand.clear();
            carPilot.setValue(null);
            carType.setValue(null);
        }

        /**
         * Checks if the field values are valid (numeric {@link String strings} are numbers and fields are not empty).
         * If an error occurs, shows an error {@link Alerts alert}.
         * @param carModel the {@link CarModel car} object to check
         * @return true if the rules are respected and fiel values are valid, false otherwise.
         */
        private boolean checkNewCarFields(CarModel carModel) {
            boolean isValid = true;
            if (!Mask.isNumeric(carNumber.getText())) {
                isValid = false;
                Alerts.error("ERREUR", "Veuillez vérifier les champs");
            }
            else if(!carController.checkCar(carModel)) {
                isValid = false;
                Alerts.error("ERREUR", "Veuillez vérifier les champs");
            }
            else if(pilotsList.size() == carsList.size()) {
                isValid = false;
                Alerts.error("ERREUR", "Il n'est pas possible d'avoir plus de voitures que de pilotes");
            }
            else if(carsList.size() == 4) {
                isValid = false;
                Alerts.error("ERREUR", "Il n'est possible d'observer que 4 voitures maximum à la fois");
            }
            return isValid;
        }

        /*/**
         * Checks if the field values are valid (numeric {@link String strings} are numbers and fields are not empty).
         *
         * @param num   the car number
         * @param team  the car team
         * @param model the car model
         * @param brand the car brand
         * @param pilot the car pilot
         * @param type  the car type (main car or rival car)
         * @return true if the field values are valid, false otherwise
         */
        /*private boolean checkNewCarFields(String num, String team, String model, String brand, String pilot, String type) {
            boolean isValid = true;
            if(pilotsList.size() == carsList.size()) {
                isValid = false;
                Alerts.error("ERREUR", "Il n'est pas possible d'avoir plus de voitures que de pilotes");
            }
            if(carsList.size() == 4) {
                isValid = false;
                Alerts.error("ERREUR", "Il n'est possible d'observer que 4 voitures maximum à la fois");
            }
            if (num.trim().isEmpty() || !Mask.isNumeric(num)) {
                isValid = false;
                carNumber.setStyle("-fx-accent: red;");
            }
            if (team.trim().isEmpty()) {
                isValid = false;
                carTeam.setStyle("-fx-accent: red;");
            }
            if (model.trim().isEmpty()) {
                isValid = false;
                carModel.setStyle("-fx-accent: red;");
            }
            if (brand.trim().isEmpty()) {
                isValid = false;
                carBrand.setStyle("-fx-accent: red;");
            }
            if (pilot == null) {
                isValid = false;
                carPilot.setStyle("-fx-accent: red;");
            }
            if (type == null) {
                isValid = false;
                carType.setStyle("-fx-accent: red;");
            }
            return isValid;
        }*/


        @FXML
        public void addingofpilot() throws ParseException, java.text.ParseException {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String firstnamecont = firstname.getText();
            String lastnamecont = lastnamepilot.getText();
            String commentcont = commentpilot.getText();
            double pilotheightcont = 0.00;
            double pilotweightcont = 0.00;
            Date pilotdatofbirthcont = null;
            int count = 0;
            LocalDate localDate = dateofbirthpilot.getValue();

            if (localDate != null) {
                pilotdatofbirthcont = Date.from(dateofbirthpilot.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            }

            if (Mask.isDouble(pilotweight.getText())) {
                pilotweightcont = Double.parseDouble(pilotweight.getText());
                count++;
            }
            if (Mask.isDouble(pilotheight.getText())) {
                pilotheightcont = Double.parseDouble(pilotheight.getText());
                count++;
            }

            PilotModel pilotcont = new PilotModel(lastnamecont, firstnamecont, commentcont, pilotdatofbirthcont, pilotweightcont, pilotweightcont);
            if (dateofbirthpilot.getValue() != null) {

                if (pilotcontroller.checkingofpilot(pilotcont) && count == 2) {
                    pilotsList.add(pilotcont);
                    carPilot.getItems().add(firstnamecont + " " + lastnamecont);
                    Alerts.success("SUCCÈS", "Pilote ajouté");
                    firstname.setText("");
                    lastnamepilot.setText("");
                    pilotheight.setText("");
                    pilotweight.setText("");
                    dateofbirthpilot.setValue(null);
                    commentpilot.setText("");
                } else {
                    Alerts.error("ERREUR", "Veuillez vérifier les champs");
                }
            } else {
                Alerts.error("ERREUR", "Veuillez vérifier les champs");
            }

        }


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////   Methods and data processing for creating a race ///////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


        /**
         * @param event
         */
        @FXML
        private void handleRaceTypeSelected(ActionEvent event) {

            int RaceType = race_type_combo.getSelectionModel().getSelectedIndex();

            if (RaceType == 0) {
                race_numberof_tour_label.setVisible(true);
                race_numberof_tour.setVisible(true);
                race_duration_label.setVisible(false);
                race_duration.setVisible(false);
                typeOfRace = com.kronos.global.enums.RaceType.LAP_RACE;

            } else {
                race_numberof_tour_label.setVisible(false);
                race_numberof_tour.setVisible(false);
                race_duration_label.setVisible(true);
                race_duration.setVisible(true);
                typeOfRace = com.kronos.global.enums.RaceType.TIME_RACE;


            }
        }

        /**
         * @param actionEvent
         */
        @FXML
        public void createRace(ActionEvent actionEvent) {

            RaceController raceController = new RaceController();
            int race_duration = 0, race_numberOf_tour = 0;
            if (typeOfRace == RaceType.TIME_RACE) {
                if (!this.race_duration.getText().isEmpty()) {
                    race_duration = Integer.parseInt(this.race_duration.getText());

                } else {
                    // Bloquer le bouton Commencer,
                }

            } else {

                if (!this.race_numberof_tour.getText().isEmpty()) {
                    race_numberOf_tour = Integer.parseInt(this.race_numberof_tour.getText());

                } else {
                    // Bloquer le bouton Commencer,
                }

            }
            RaceModel race = raceController.createRace(typeOfRace,
                    Date.from(startingTime_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    racewayName_text.getText(), race_duration, race_numberOf_tour);

            System.out.println("======================");
            System.out.println(typeOfRace.toString());
            System.out.println("======================");

            if (race != null) {

                // Save test save manager
                SaveManagerImpl saveManager = SaveManagerImpl.getInstance();
                saveManager.persist(pilotsList);
                saveManager.persist(carsList);
                saveManager.persist(race);
                System.out.println(saveManager.saveFile());

            }


        }


    }