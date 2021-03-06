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
import com.kronos.App;
import com.kronos.global.enums.RaceType;
import com.kronos.global.plugin.ViewManager;
import com.kronos.global.util.Alerts;
import com.kronos.global.util.Mask;
import com.kronos.model.*;
import com.kronos.parserXML.MainImpl.SaveManagerImpl;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller of the home window. Manages the actions and events when configuring a race.
 *
 * @author TeamKronos
 * @version 1.0
 */
public class HomeController implements Initializable {
    public static ObservableList<String> stylesheets;
    public static HomeController ctrl;
    private static PilotController pilotcontroller = new PilotController();
    private static CarController carController = new CarController();
    private static Boolean changeRequest;
    @FXML
    public Tab tabPilot;
    @FXML
    public Tab tabCar;
    @FXML
    public Tab tabCourse;
    @FXML
    public JFXButton btnNextCar;
    @FXML
    public JFXButton btnNextLap;
    //////////////////////////////////// Attributes for internationalization  /////////////////////////////////////
    @FXML
    public JFXButton btnaddpilot;
    @FXML
    public JFXButton add_car_btn;
    @FXML
    public JFXButton btn_start_race;
    @FXML
    public Text i18n_createRace;
    @FXML
    public Text i18n_chargeOldRace;
    @FXML
    public Text i18n_parameters;
    @FXML
    public Text i18n_touchOfTop;
    @FXML
    public JFXButton i18n_changeTop;
    @FXML
    public Label i18n_lastNamePilot;
    @FXML
    public Label i18n_firstNamePilot;
    @FXML
    public Label i18n_dateOfBirthPilot;
    @FXML
    public Label i18n_weightPilot;
    @FXML
    public Label i18n_commentPilot;
    @FXML
    public Label i18n_titlePilot;
    @FXML
    public Label i18n_sizePilot;
    @FXML
    public Label i18n_titleCar;
    @FXML
    public Label i18n_numberCar;
    @FXML
    public Label i18n_teamCar;
    @FXML
    public Label i18n_modelCar;
    @FXML
    public Label i18n_brandCar;
    @FXML
    public Label i18n_pilotCar;
    @FXML
    public Label i18n_typeOfCar;
    @FXML
    public Label i18n_beginningRace;
    @FXML
    public Label i18n_nameOfTrack;
    @FXML
    public Label i18n_raceType;
    @FXML
    public Label i18n_titleRace;
    @FXML
    public Label i18n_nameOfRace;
    @FXML
    public Label i18n_lapTime;
    @FXML
    public Label i18n_lapsBeforeRelay;
    @FXML
    public Label raceNumberOfLapsLabel;
    @FXML
    JFXDatePicker dateOfBirthPilot;
    private ArrayList<PilotModel> pilotsList = new ArrayList<>();
    private ArrayList<CarModel> carsList = new ArrayList();
    private boolean mainCarCreated;
    @FXML
    private JFXButton startBtn;
    @FXML
    private JFXButton bdBtn;
    @FXML
    private JFXButton settingBtn;
    @FXML
    private ImageView newRaceIcon;
    @FXML
    private ImageView settingIcon;
    @FXML
    private ImageView bdIcon;
    @FXML
    private ImageView appName1;
    @FXML
    private StackPane homeStack;
    @FXML
    private JFXDialogLayout dialogPara;
    @FXML
    private JFXDialogLayout dialogSelectKey;
    @FXML
    private JFXButton endPara;
    @FXML
    private JFXDialogLayout dialLayout;
    @FXML
    private Label topKey;
    @FXML
    private JFXDialogLayout dialogNewRace;
    @FXML
    private JFXTabPane newRaceTabPane;
    @FXML
    private ImageView bolt;
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
    private JFXTextField lastNamePilot;


    //////////////////////////////////////// Attributes of races data /////////////////////////////////////
    @FXML
    private JFXTextField firstName;
    @FXML
    private JFXTextField pilotWeight;
    @FXML
    private JFXTextField pilotHeight;
    @FXML
    private JFXTextField raceName;
    @FXML
    private JFXTextArea commentPilot;
    @FXML
    private JFXDatePicker startingTimeDate;
    @FXML
    private JFXTextField racewayNameText;
    @FXML
    private JFXTextField raceDuration;
    @FXML
    private Label raceDurationLabel;
    @FXML
    private JFXTextField raceNumberOfLaps;
    @FXML
    private JFXComboBox<String> raceTypeCombo;
    @FXML
    private Label t_m_autour_label;
    @FXML
    private Label tour_relai_label;
    @FXML
    private JFXTextField t_m_autour;
    @FXML
    private JFXTextField tour_relai;
    @FXML
    private JFXButton changeTopkeyButton;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private RaceType typeOfRace;

    /**
     * Handles the startup of the race creation process.
     *
     * @param event the event (click on the new race button)
     */
    @FXML
    private void handleNewRaceClicked(ActionEvent event) {
        dialogNewRace.setVisible(true);
        JFXDialog alert1 = new JFXDialog(homeStack, dialogNewRace, JFXDialog.DialogTransition.TOP);
        alert1.show();
    }

    /**
     * Handles the animation of the new race button of the home window when it is hovered.
     */
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

    /**
     * Handles the opening of the settings window.
     */
    @FXML
    private void handleSettingClicked() {
        System.out.print("Ouverture des paramètres\n");
        dialogPara.setVisible(true);
        JFXDialog alert = new JFXDialog(homeStack, dialogPara, JFXDialog.DialogTransition.CENTER);

        endPara.setOnAction((ActionEvent event) -> {
            alert.close();
        });

        alert.show();

    }

    /**
     * Handles the rotate animation when the settings button on the home window is hovered.
     */
    @FXML
    private void handleSettingEntered() {
        ////// ROTATION BOUTON SETTINGS
        RotateTransition rotatepara = new RotateTransition(Duration.seconds(1), settingBtn);
        RotateTransition rotateparaicon = new RotateTransition(Duration.seconds(1), settingIcon);

        rotatepara.setByAngle(360);
        rotateparaicon.setByAngle(360);


        rotatepara.setCycleCount(1);
        rotateparaicon.setCycleCount(1);

        rotatepara.play();
        rotateparaicon.play();

        //////
    }

    /**
     * Navigates to the interface used reload an old race.
     *
     * @param event the event
     */
    @FXML
    private void handleOldRaceClicked(ActionEvent event) {

        SaveManagerImpl saveManager = App.getDataManager();
        boolean isUpload = saveManager.getImportManager().importFile((Stage) App.getDecorator().getScene().getWindow());
        if (isUpload)
            handleToControlPanel();
        else
            //Alerts.error("ERREUR DE CHARGEMENT", "Le Fichier n'as pu être chargé");
            Alerts.AlertError("ERREUR DE CHARGEMENT", "Le Fichier n'as pu être chargé");

    }

    /**
     * Handles the animation of the old race button on the home window.
     *
     * @param event the event
     */
    @FXML
    private void handleOldRaceEntered(ActionEvent event) {
        try {
            Stage stage = (Stage) startBtn.getScene().getWindow();
            StackPane test = FXMLLoader.load(getClass().getResource("Racechoice.fxml"));
            stage.setScene(new Scene(test));
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the navigation to the {@link CarModel car} creation tab.
     *
     * @param event the event
     */
    @FXML
    private void handleSwitchToCarTab(ActionEvent event) {
        if (pilotsList.size() != 0) {
            SingleSelectionModel<Tab> selectionModel = newRaceTabPane.getSelectionModel();
            tabCar.setDisable(false);
            selectionModel.select(tabCar);
        } else {
            Alerts.error("ERREUR", "Veuillez créer au moins un pilote");
        }
    }

    /**
     * Handles the navigation to the {@link RaceModel race} final creation tab.
     *
     * @param event the event
     */
    @FXML
    private void handleSwitchToRaceTab(ActionEvent event) {
        if (carsList.size() != 0) {
            SingleSelectionModel<Tab> selectionModel = newRaceTabPane.getSelectionModel();
            tabCourse.setDisable(false);
            selectionModel.select(tabCourse);
        } else {
            //Alerts.error("ERREUR", "Veuillez créer au moins une voiture");
            Alerts.AlertError("ERREUR", "Veuillez créer au moins une voiture");
        }
    }


    /**
     * Handles the change of top control.
     *
     * @param event the event
     */
    @FXML
    private void handleChangeTopControl(ActionEvent event) {
        changeRequest = true;
        //Alerts.info("CHANGEMENT TOP KEY", "Veuillez appuyer sur la nouvelle touche puis sour ok");
        Alerts.AlertWarning("CHANGEMENT TOP KEY", "Veuillez appuyer sur la nouvelle touche puis sour ok");
        //scene.setOnKeyPressed();
//        dialog_select_key.setVisible(true);
//        JFXDialog alertkey= new JFXDialog(homestack,dialog_select_key,JFXDialog.DialogTransition.CENTER);
        Scene scene = homeStack.getScene();
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
                            topKey.setText(properties.getProperty("key"));
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

    /**
     * Navigates to the interface used to control a race.
     */

    private void handleToControlPanel() {
        loadDashBoardController("Dashboard");
        //Stage stage = (Stage) startBtn.getScene().getWindow();
        stylesheets = App.getDecorator().getScene().getStylesheets();
        App.getDecorator().setMaximized(true);
        App.getDecorator().setResizable(true);
        App.getDecorator().setContent(ViewManager.getInstance().get("main"));
    }

    private void loadDashBoardController(String name) {
        try {
            ViewManager.getInstance().put(
                    name,
                    FXMLLoader.load(getClass().getResource("/com/kronos/view/" + name + ".fxml"))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes parameters of JFX components which needs to be initialized upon startup.
     *
     * @param url creates an {@link URL url} object from the {@link String} representation
     * @param rb  contains local specific objects.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ctrl = this;

        ////// SCALE BOUTON DEMARRER
        ScaleTransition scalestart = new ScaleTransition(Duration.seconds(1), startBtn);
        scalestart.setToX(1.2);
        scalestart.setToY(1.2);
        scalestart.setAutoReverse(true);
        scalestart.setCycleCount(ScaleTransition.INDEFINITE);
        scalestart.play();

        ////// SCALE BOUTON SETTINGS
        ScaleTransition scalepara = new ScaleTransition(Duration.seconds(1), settingBtn);
        scalepara.setToX(1.2);
        scalepara.setToY(1.2);
        scalepara.setAutoReverse(true);
        scalepara.setCycleCount(ScaleTransition.INDEFINITE);
        scalepara.play();
        //////

        ////// SCALE BOUTON  BASE DE DONNEES
        ScaleTransition scalebd = new ScaleTransition(Duration.seconds(1), bdBtn);
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
            topKey.setText(properties.getProperty("key"));
        } catch (IOException io) {
        }


        carType.setItems(FXCollections.observableArrayList("Voiture principale", "Voiture concurrente"));
        raceTypeCombo.setItems(FXCollections.observableArrayList("Course au nombre de tours", "Course au temps"));


        stylesheets = App.getDecorator().getScene().getStylesheets();
        stylesheets.addAll(
                getClass().getResource("/com/kronos/theme/css/fonts.css").toExternalForm(),
                getClass().getResource("/com/kronos/theme/css/material-color.css").toExternalForm(),
                getClass().getResource("/com/kronos/theme/css/skeleton.css").toExternalForm(),
                getClass().getResource("/com/kronos/theme/css/light.css").toExternalForm(),
                getClass().getResource("/com/kronos/theme/css/bootstrap.css").toExternalForm(),
                getClass().getResource("/com/kronos/theme/css/shape.css").toExternalForm(),
                getClass().getResource("/com/kronos/theme/css/typographic.css").toExternalForm(),
                getClass().getResource("/com/kronos/theme/css/helpers.css").toExternalForm()
                // getClass().getResource("/com/kronos/theme/css/master.css").toExternalForm()
        );

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
        if (carPilot.getSelectionModel().getSelectedItem() != null && carType.getSelectionModel().getSelectedItem() != null && Mask.isNumeric(carNumber.getText())) {
            if (!mainCarCreated && carType.getSelectionModel().getSelectedItem().equals("Voiture principale")) {
                MainCarModel mainCarModel = new MainCarModel(Integer.parseInt(carNumber.getText()), carTeam.getText(), carModel.getText(), carBrand.getText(), findPilot(carPilot.getSelectionModel().getSelectedIndex()));
                if (checkNewCarFields(mainCarModel) && carIsExist(mainCarModel.getNumber())) {
                    carsList.add(mainCarModel);
                    App.getDataManager().persist(mainCarModel);
                    mainCarCreated = true;
                    carPilot.getItems().remove(carPilot.getSelectionModel().getSelectedIndex());
                    //.success("SUCCÈS", "Nouvelle voiture créée");
                    Alerts.AlertSuccess("SUCCÈS", "Nouvelle voiture créée");
                    clearNewCarFields();
                } else {
                    //Alerts.error("ERREUR", "Veuillez vérifier les champs");
                    Alerts.AlertError("ERREUR", "Veuillez vérifier les champs");
                }
            } else if (mainCarCreated && carType.getSelectionModel().getSelectedItem().equals("Voiture concurrente")) {
                RivalCarModel rivalCarModel = new RivalCarModel(Integer.parseInt(carNumber.getText()), carTeam.getText(), carModel.getText(), carBrand.getText(), findPilot(carPilot.getSelectionModel().getSelectedIndex()));
                if (checkNewCarFields(rivalCarModel) && carIsExist(rivalCarModel.getNumber())) {
                    carsList.add(rivalCarModel);
                    App.getDataManager().persist(rivalCarModel);
                    carPilot.getItems().remove(carPilot.getSelectionModel().getSelectedIndex());
                    //Alerts.success("SUCCÈS", "Nouvelle voiture créée");
                    Alerts.AlertSuccess("SUCCÈS", "Nouvelle voiture créée");
                    clearNewCarFields();
                } else {
                    //Alerts.error("ERREUR", "Veuillez vérifier les champs");
                    Alerts.AlertError("ERREUR", "Veuillez vérifier les champs");
                }
            } else if (mainCarCreated) {
                //Alerts.error("ERREUR", "Une voiture principale existe déjà");
                Alerts.AlertError("ERREUR", "Une voiture principale existe déjà");
            } else {
                //Alerts.error("ERREUR", "Veuillez commencer par créer une voiture principale");
                Alerts.AlertError("ERREUR", "Veuillez commencer par créer une voiture principale");
            }
        } else {
            //Alerts.error("ERREUR", "Veuillez vérifier les champs");
            Alerts.AlertError("ERREUR", "Veuillez commencer par créer une voiture principale");
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
     * Resets the fields in the new pilot interface
     */
    private void clearNewPilotFields() {
        firstName.setText("");
        lastNamePilot.setText("");
        pilotHeight.setText("");
        pilotWeight.setText("");
        dateOfBirthPilot.setValue(null);
        commentPilot.setText("");

    }

    /**
     * Checks if the field values are valid (numeric {@link String strings} are numbers and fields are not empty).
     * If an error occurs, shows an error {@link Alerts alert}.
     *
     * @param carModel the {@link CarModel car} object to check
     * @return true if the rules are respected and fiel values are valid, false otherwise.
     */
    private boolean checkNewCarFields(CarModel carModel) {
        boolean isValid = true;
        if (!Mask.isNumeric(carNumber.getText())) {
            isValid = false;
            //Alerts.error("ERREUR", "Veuillez vérifier les champs");
            Alerts.AlertError("ERREUR", "Veuillez vérifier les champs");
        } else if (!carController.checkCar(carModel)) {
            isValid = false;
            //Alerts.error("ERREUR", "Veuillez vérifier les champs");
            Alerts.AlertError("ERREUR", "Veuillez vérifier les champs");
        } else if (pilotsList.size() == carsList.size()) {
            isValid = false;
            //Alerts.error("ERREUR", "Il n'est pas possible d'avoir plus de voitures que de pilotes");
            Alerts.AlertError("ERREUR", "Il est interdit d'avoir plus de voitures que de pilotes");
        } else if (carsList.size() == 4) {
            isValid = false;
            //Alerts.error("ERREUR", "Il n'est possible d'observer que 4 voitures maximum à la fois");
            Alerts.AlertError("ERREUR", "Il n'est possible d'observer que 4 voitures maximum à la fois");
        }
        return isValid;
    }

    /**
     * checks if the number of the number of the car does not exist already
     *
     * @param carNumber the number of the car that we would like to create {@link CarModel number }
     * @return true if the number of the currently car does not exist
     */
    private boolean carIsExist(int carNumber) {
        List<CarModel> carModels = (List<CarModel>) (List<?>) App.getDataManager().getModels(CarModel.class);
        for (CarModel model : carModels) {
            if (model.getNumber() == carNumber) {
                return false;

            }
        }
        return true;
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

    /**
     * Handles the creation of a new pilot on click on the "add" button in the pilot creation interface.
     *
     * @throws ParseException           occurs when failing to parse
     * @throws java.text.ParseException occurs when failing to parse a string
     */
    @FXML
    public void handleClickNewPilot() throws ParseException, java.text.ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String firstnamecont = firstName.getText();
        String lastnamecont = lastNamePilot.getText();
        String commentcont = commentPilot.getText();
        double pilotheightcont = 0.00;
        double pilotweightcont = 0.00;
        Date pilotdatofbirthcont = null;
        int count = 0;
        boolean check = true;
        LocalDate localDate = dateOfBirthPilot.getValue();

        if (localDate != null) {
            pilotdatofbirthcont = Date.from(dateOfBirthPilot.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        }
        if (!(pilotWeight.getText().isEmpty())) {
            if (Mask.isDouble(pilotWeight.getText())) {
                pilotweightcont = Double.parseDouble(pilotWeight.getText());
            } else {
                check = false;
            }
        }
        if (!(pilotHeight.getText().isEmpty())) {
            if (Mask.isDouble(pilotHeight.getText())) {
                pilotheightcont = Double.parseDouble(pilotHeight.getText());
            } else {
                check = false;
            }
        }


        PilotModel pilotcont = new PilotModel(firstnamecont, lastnamecont, commentcont, pilotdatofbirthcont, pilotheightcont, pilotweightcont);


        if (pilotcontroller.checkPilot(pilotcont) && check) {
            if (pilotsList.size() < 4) {
                pilotsList.add(pilotcont);
                carPilot.getItems().add(firstnamecont + " " + lastnamecont);
                //Alerts.success("SUCCÈS", "Pilote ajouté");
                Alerts.AlertSuccess("SUCCES", "Pilote ajouté");
                App.getDataManager().persist(pilotcont);
                clearNewPilotFields();
            } else {
                //Alerts.error("ERREUR", "Création 4 pilotes maximum");
                Alerts.AlertError("ERREUR", "Création 4 pilotes maximum");
            }

        } else {
            //Alerts.error("ERREUR", "Veuillez vérifier les champs");
            Alerts.AlertError("ERREUR", "Veuillez vérifier les champs");

        }


    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////   Methods and data processing for creating a race ///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Handles the selection of the race type.
     *
     * @param event the event
     */
    @FXML
    private void handleRaceTypeSelected(ActionEvent event) {

        int RaceType = raceTypeCombo.getSelectionModel().getSelectedIndex();

        if (RaceType == 0) {
            raceNumberOfLapsLabel.setVisible(true);
            raceNumberOfLaps.setVisible(true);
            raceDurationLabel.setVisible(false);
            raceDuration.setVisible(false);
            t_m_autour_label.setVisible(true);
            t_m_autour.setVisible(true);
            tour_relai_label.setVisible(true);
            tour_relai.setVisible(true);
            typeOfRace = com.kronos.global.enums.RaceType.LAP_RACE;

        } else {
            raceNumberOfLapsLabel.setVisible(false);
            raceNumberOfLaps.setVisible(false);
            raceDurationLabel.setVisible(true);
            raceDuration.setVisible(true);
            t_m_autour_label.setVisible(true);
            t_m_autour.setVisible(true);
            tour_relai_label.setVisible(true);
            tour_relai.setVisible(true);
            typeOfRace = com.kronos.global.enums.RaceType.TIME_RACE;


        }
    }

    /**
     * Handles the race creation after adding pilots and cars.
     *
     * @param actionEvent the event
     */
    @FXML
    public void createRace(ActionEvent actionEvent) {
        boolean timelaps = false;
        RaceStartupController raceStartupController = new RaceStartupController();
        int race_duration = -1, race_numberOf_tour = -1, race_interval_relays = -1, race_mean_lap_time = 0;
        if (typeOfRace == RaceType.TIME_RACE) {
            if (!this.raceDuration.getText().isEmpty()) {
                if (Mask.isNumeric(this.raceDuration.getText())) {
                    if (Integer.parseInt(this.raceDuration.getText()) > 0) {
                        race_duration = Integer.parseInt(raceDuration.getText());
                        if (!this.tour_relai.getText().isEmpty()) {
                            if (Mask.isNumeric(this.tour_relai.getText())) {
                                if (Integer.parseInt(this.tour_relai.getText()) > 0) {
                                    race_interval_relays = Integer.parseInt(this.tour_relai.getText());
                                    if (!this.t_m_autour.getText().isEmpty()) {
                                        if (Mask.isNumeric(this.t_m_autour.getText())) {
                                            if (Integer.parseInt(this.t_m_autour.getText()) > 0) {
                                                race_mean_lap_time = Integer.parseInt(this.tour_relai.getText());
                                                timelaps = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } else {

            if (!this.raceNumberOfLaps.getText().isEmpty()) {
                if (Mask.isNumeric(this.raceNumberOfLaps.getText())) {
                    if (Integer.parseInt(this.raceNumberOfLaps.getText()) > 0) {
                        race_numberOf_tour = Integer.parseInt(this.raceNumberOfLaps.getText());
                        if (!this.tour_relai.getText().isEmpty()) {
                            if (Mask.isNumeric(this.tour_relai.getText())) {
                                if (Integer.parseInt(this.tour_relai.getText()) > 0 && Integer.parseInt(this.tour_relai.getText()) <= Integer.parseInt(this.raceNumberOfLaps.getText())) {
                                    race_interval_relays = Integer.parseInt(this.tour_relai.getText());
                                    if (!this.t_m_autour.getText().isEmpty()) {
                                        if (Mask.isNumeric(this.t_m_autour.getText())) {
                                            if (Integer.parseInt(this.t_m_autour.getText()) > 0) {
                                                race_mean_lap_time = Integer.parseInt(this.t_m_autour.getText());
                                                timelaps = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (timelaps) {
            RaceModel race = raceStartupController.createRace(typeOfRace, raceName.getText(),
                    Date.from(startingTimeDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    racewayNameText.getText(), race_duration, race_numberOf_tour, race_interval_relays, race_mean_lap_time);

            if (typeOfRace.equals(RaceType.TIME_RACE)) {
                for (CarModel carModel : carsList) {
                    carModel.setTimeRace((TimeRaceModel) race);
                }
            } else {
                for (CarModel carModel : carsList) {
                    carModel.setLapRace((LapRaceModel) race);
                }
            }
            race.setPilotsList(pilotsList);
            if (race != null) {
                // Save test save manager

                App.getDataManager().persist(race);
                carController.setRaceModel(race);
                SaveManagerImpl saveManager = App.getDataManager();
                saveManager.saveFile();
                handleToControlPanel();
            }
        } else {
            //Alerts.error("ERREUR", "Veuillez vérifier les champs");
            Alerts.AlertError("ERREUR", "Veuillez vérifier les champs");
        }


    }


}
