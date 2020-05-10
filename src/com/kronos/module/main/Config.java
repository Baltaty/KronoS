 
package com.kronos.module.main;

import  com.kronos.App;
import com.kronos.controller.HomeController;
import com.kronos.controller.RaceResumeController;
import  com.kronos.global.plugin.ViewManager;
import   com.gn.decorator.GNDecorator;
import com.jfoenix.controls.JFXButton;
import com.kronos.parserXML.MainImpl.SaveManagerImpl;
import com.kronos.parserXML.api.SaveManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

 /**
 * @author TeamKronos
 * 
 * Version 1.0
 */ 
public class Config implements Initializable {

//    @FXML
//    private JFXButton btn_themeActived;
//    @FXML
//    private JFXButton btn_themeDisabled;
     @FXML
     private JFXButton btn_theme;
    
    @FXML
    private JFXButton btn_fr;
    
    @FXML
    private JFXButton btn_eng;

    @FXML
    private JFXButton  btnSaveUnderFile;

//     public String getDarkIn() {
//         return darkIn;
//     }
//
//     public void setDarkIn(String darkIn) {
//         this.darkIn = darkIn;
//     }
//
//     public String getDarkOut() {
//         return darkOut;
//     }
//
//     public void setDarkOut(String darkOut) {
//         this.darkOut = darkOut;
//     }
//
//    public String darkIn;
//    public String darkOut;
    

    @FXML
    public VBox options;

    public static Config ctrl;
    
    private ResourceBundle bundle;
    private Locale locale;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ctrl = this;
    }

    private boolean invert = false;
    @FXML
    private void altTheme() {
        invertTheme(!invert);
    }

    @FXML
    private void ToFR(){
        loadLang("fr");
    }
    
    @FXML
    private void ToENG(){
        loadLang("en");
    }
    
    private void invertTheme(boolean dark) {
        String theme;
        App.stylesheets.clear();
        String path = "/com/kronos/theme/css/";
        
        if (dark) {
            App.decorator.initTheme(GNDecorator.Theme.DARKULA);
            theme = "dark.css";
            btn_theme.setText("Mode nuit : activé");
//            btn_themeActived.setVisible(true);
//            btn_themeDisabled.setVisible(false);
            invert = true;
        } else {
            App.decorator.initTheme(GNDecorator.Theme.DEFAULT);
            theme = "light.css";
            btn_theme.setText("Mode nuit : desactivé");
//            btn_themeActived.setVisible(false);
//            btn_themeDisabled.setVisible(true);
            invert = false;
        }

        ObservableList<String> stylesheets = App.decorator.getStage().getScene().getStylesheets();

        stylesheets.addAll(
//                getClass().getResource(path + "fonts.css").toExternalForm(),
//                getClass().getResource(path + "material-color.css").toExternalForm(),
//                getClass().getResource(path + "skeleton.css").toExternalForm(),
                  getClass().getResource(path + "" + theme).toExternalForm()//,
//                getClass().getResource(path + "bootstrap.css").toExternalForm(),
//                getClass().getResource(path + "simple-green.css").toExternalForm(),
//                getClass().getResource(path + "shape.css").toExternalForm(),
//                getClass().getResource(path + "typographic.css").toExternalForm(),
//                getClass().getResource(path + "helpers.css").toExternalForm(),
//                getClass().getResource(path + "master.css").toExternalForm()
        );

        App.getUserDetail().getStylesheets().setAll(stylesheets);

        for (Node node : ViewManager.getInstance().getAll()) {
            ((StackPane) node).getStylesheets().clear();
            ((StackPane) node).getStylesheets().setAll(stylesheets);
        }

        Main.popConfig.hide();

        Platform.runLater(() -> {
//          force pop's transition

          //  Main.popup.getRoot().getStylesheets().remove(Main.popup.getRoot().getStylesheets().size() - 1);
            Main.popConfig.getRoot().getStylesheets().remove(Main.popConfig.getRoot().getStylesheets().size() - 1);

           // Main.popup.getRoot().getStylesheets().add(path + "pop" + theme);
            Main.popConfig.getRoot().getStylesheets().add(path + "pop" + theme);
        });

    }

    public void loadLang(String lang){
        locale = new Locale(lang);
        bundle = ResourceBundle.getBundle("com.kronos.lang",locale);

        //From HomeController
        HomeController.ctrl.tabPilot.setText(bundle.getString("pilot"));
        HomeController.ctrl.tabCar.setText(bundle.getString("car"));
        HomeController.ctrl.tabCourse.setText(bundle.getString("race"));
        HomeController.ctrl.btnaddpilot.setText(bundle.getString("add"));
        HomeController.ctrl.btnNextCar.setText(bundle.getString("next"));
        HomeController.ctrl.add_car_btn.setText(bundle.getString("add"));
        HomeController.ctrl.btnNextLap.setText(bundle.getString("next"));
        HomeController.ctrl.btn_start_race.setText(bundle.getString("frontPanel"));
        HomeController.ctrl.raceNumberOfLapsLabel.setText(bundle.getString("tourNumber"));
        HomeController.ctrl.i18n_createRace.setText(bundle.getString("createRace"));
        HomeController.ctrl.i18n_chargeOldRace.setText(bundle.getString("chargeRace"));
        HomeController.ctrl.i18n_parameters.setText(bundle.getString("parameters"));
        HomeController.ctrl.i18n_touchOfTop.setText(bundle.getString("touch"));
        HomeController.ctrl.i18n_changeTop.setText(bundle.getString("change"));

        HomeController.ctrl.i18n_lastNamePilot.setText(bundle.getString("lastName"));
        HomeController.ctrl.i18n_firstNamePilot.setText(bundle.getString("firstName"));
        HomeController.ctrl.i18n_dateOfBirthPilot.setText(bundle.getString("dateOfBirth"));
        HomeController.ctrl.i18n_weightPilot.setText(bundle.getString("weight"));
        HomeController.ctrl.i18n_commentPilot.setText(bundle.getString("comment"));
        HomeController.ctrl.i18n_titlePilot.setText(bundle.getString("titlePilotPage"));
        HomeController.ctrl.i18n_sizePilot.setText(bundle.getString("size"));

        HomeController.ctrl.i18n_titleCar.setText(bundle.getString("titleCarPage"));
        HomeController.ctrl.i18n_numberCar.setText(bundle.getString("number"));
        HomeController.ctrl.i18n_teamCar.setText(bundle.getString("team"));
        HomeController.ctrl.i18n_modelCar.setText(bundle.getString("model"));
        HomeController.ctrl.i18n_brandCar.setText(bundle.getString("brand"));
        HomeController.ctrl.i18n_pilotCar.setText(bundle.getString("pilotCar"));
        HomeController.ctrl.i18n_typeOfCar.setText(bundle.getString("carType"));

        HomeController.ctrl.i18n_titleRace.setText(bundle.getString("titleRacePage"));
        HomeController.ctrl.i18n_nameOfRace.setText(bundle.getString("raceName"));
        HomeController.ctrl.i18n_beginningRace.setText(bundle.getString("startDate"));
        HomeController.ctrl.i18n_nameOfTrack.setText(bundle.getString("circuitName"));
        HomeController.ctrl.i18n_raceType.setText(bundle.getString("raceType"));
//        HomeController.ctrl.i18n_lapTime.setText(bundle.getString("tourNumber"));
//        HomeController.ctrl.i18n_lapsBeforeRelay.setText(bundle.getString("tourNumber"));

        // From config
//        ctrl.btn_themeActived.setText(bundle.getString("darkModeIn"));
//        ctrl.btn_themeDisabled.setText(bundle.getString("darkModeOut"));
        ctrl.btn_eng.setText(bundle.getString("english"));
        ctrl.btn_fr.setText(bundle.getString("french"));
        ctrl.btnSaveUnderFile.setText(bundle.getString("saveAs"));

        // From main
        Main.ctrl.title.setText(bundle.getString("title"));
        Main.ctrl.FeuilleTemps.setText(bundle.getString("timeSheet"));
        Main.ctrl.home.setText(bundle.getString("title"));
        // From RaceResumeController
        RaceResumeController.ctrl.i18n_raceinfo.setText((bundle.getString("raceInfo")));
        RaceResumeController.ctrl.i18n_departureHour.setText((bundle.getString("departureHour")));
        RaceResumeController.ctrl.i18n_actualHour.setText((bundle.getString("actualHour")));
        RaceResumeController.ctrl.i18n_elapsedTime.setText((bundle.getString("elapsedTime")));
        RaceResumeController.ctrl.i18n_remainingTime.setText((bundle.getString("remainingTime")));
        RaceResumeController.ctrl.i18n_remainingLaps.setText((bundle.getString("elapsedLaps")));
        RaceResumeController.ctrl.i18n_elapsedLaps.setText((bundle.getString("remainingLaps")));
        RaceResumeController.ctrl.startRace.setText((bundle.getString("start")));
        RaceResumeController.ctrl.pauseRace.setText((bundle.getString("pause")));
        RaceResumeController.ctrl.stopRace.setText((bundle.getString("stop")));
        RaceResumeController.ctrl.i18n_panel.setText(bundle.getString("panel"));
        RaceResumeController.ctrl.i18n_details.setText(bundle.getString("details"));
        RaceResumeController.ctrl.i18n_currentPilot.setText(bundle.getString("pilot"));
        RaceResumeController.ctrl.i18n_currentPilotLastName.setText(bundle.getString("currentPilotLastName"));
        RaceResumeController.ctrl.i18n_currentPilotFirstname.setText(bundle.getString("currentPilotFirstname"));
        RaceResumeController.ctrl.i18n_currentPilotDateOfBirth.setText(bundle.getString("currentPilotDateOfBirth"));
        RaceResumeController.ctrl.i18n_currentCar.setText(bundle.getString("car"));
        RaceResumeController.ctrl.i18n_currentCarBrand.setText(bundle.getString("currentCarBrand"));
        RaceResumeController.ctrl.i18n_currentCarModel.setText(bundle.getString("currentCarModel"));
        RaceResumeController.ctrl.i18n_currentCarTeam.setText(bundle.getString("currentCarTeam"));
        RaceResumeController.ctrl.i18n_averageLapTime.setText(bundle.getString("averageLapTime"));
        RaceResumeController.ctrl.i18n_timerOpponentCar.setText(bundle.getString("timerOpponentCar"));
        RaceResumeController.ctrl.i18n_type.setText(bundle.getString("type"));
        RaceResumeController.ctrl.i18n_car.setText(bundle.getString("car"));
        RaceResumeController.ctrl.i18n_comment.setText(bundle.getString("commentMaj"));
        RaceResumeController.ctrl.i18n_raceRanking.setText(bundle.getString("raceRanking"));
        RaceResumeController.ctrl.i18n_previous.setText(bundle.getString("previous"));
        RaceResumeController.ctrl.i18n_inProgress.setText(bundle.getString("inProgress"));
        RaceResumeController.ctrl.toogleedit.setText(bundle.getString("modifyTop"));
        RaceResumeController.ctrl.colCarNumber.setText(bundle.getString("colCarNumber"));
        RaceResumeController.ctrl.col_time.setText(bundle.getString("colTime"));
        RaceResumeController.ctrl.col_racetime.setText(bundle.getString("colRacetime"));
        RaceResumeController.ctrl.col_laptime.setText(bundle.getString("colLaptime"));
        RaceResumeController.ctrl.colLapNumber.setText(bundle.getString("colLapNumber"));
        RaceResumeController.ctrl.col_typetop.setText(bundle.getString("colTypetop"));
        RaceResumeController.ctrl.col_comment.setText(bundle.getString("colComment"));



    }

    @FXML
     public void saveUnderFile(ActionEvent event) {

        SaveManagerImpl saveManager =  App.getDataManager();
        saveManager.saveFileUnder((Stage) App.getDecorator().getScene().getWindow());
    }
}
