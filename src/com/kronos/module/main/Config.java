 
package com.kronos.module.main;

import  com.kronos.App;
import com.kronos.controller.HomeController;
import com.kronos.controller.DashboardController;
import  com.kronos.global.plugin.ViewManager;
import   com.gn.decorator.GNDecorator;
import com.jfoenix.controls.JFXButton;
import com.kronos.model.RaceModel;
import com.kronos.parserXML.MainImpl.CompleteSaveStrategy;
import com.kronos.parserXML.MainImpl.SaveManagerImpl;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

    @FXML
    private JFXButton btnCompleteSave;

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
        // From DashboardController
        DashboardController.ctrl.i18n_raceinfo.setText((bundle.getString("raceInfo")));
        DashboardController.ctrl.i18n_departureHour.setText((bundle.getString("departureHour")));
        DashboardController.ctrl.i18n_actualHour.setText((bundle.getString("actualHour")));
        DashboardController.ctrl.i18n_elapsedTime.setText((bundle.getString("elapsedTime")));
        DashboardController.ctrl.i18n_remainingTime.setText((bundle.getString("remainingTime")));
        DashboardController.ctrl.i18n_remainingLaps.setText((bundle.getString("elapsedLaps")));
        DashboardController.ctrl.i18n_elapsedLaps.setText((bundle.getString("remainingLaps")));
        DashboardController.ctrl.startRace.setText((bundle.getString("start")));
        DashboardController.ctrl.pauseRace.setText((bundle.getString("pause")));
        DashboardController.ctrl.stopRace.setText((bundle.getString("stop")));
        DashboardController.ctrl.i18n_details.setText(bundle.getString("details"));
        DashboardController.ctrl.i18n_currentPilot.setText(bundle.getString("pilot"));
        DashboardController.ctrl.i18n_currentPilotLastName.setText(bundle.getString("currentPilotLastName"));
        DashboardController.ctrl.i18n_currentPilotFirstname.setText(bundle.getString("currentPilotFirstname"));
        DashboardController.ctrl.i18n_currentPilotDateOfBirth.setText(bundle.getString("currentPilotDateOfBirth"));
        DashboardController.ctrl.i18n_currentCar.setText(bundle.getString("car"));
        DashboardController.ctrl.i18n_currentCarBrand.setText(bundle.getString("currentCarBrand"));
        DashboardController.ctrl.i18n_currentCarModel.setText(bundle.getString("currentCarModel"));
        DashboardController.ctrl.i18n_currentCarTeam.setText(bundle.getString("currentCarTeam"));
        DashboardController.ctrl.i18n_averageLapTime.setText(bundle.getString("averageLapTime"));
        DashboardController.ctrl.i18n_timerOpponentCar.setText(bundle.getString("timerOpponentCar"));
        DashboardController.ctrl.i18n_type.setText(bundle.getString("type"));
        DashboardController.ctrl.i18n_car.setText(bundle.getString("car"));
        DashboardController.ctrl.i18n_comment.setText(bundle.getString("commentMaj"));
        DashboardController.ctrl.i18n_raceRanking.setText(bundle.getString("raceRanking"));
        DashboardController.ctrl.i18n_previous.setText(bundle.getString("previous"));
        DashboardController.ctrl.i18n_inProgress.setText(bundle.getString("inProgress"));
        DashboardController.ctrl.toogleedit.setText(bundle.getString("modifyTop"));
        DashboardController.ctrl.colCarNumber.setText(bundle.getString("colCarNumber"));
        DashboardController.ctrl.col_time.setText(bundle.getString("colTime"));

        DashboardController.ctrl.col_racetime.setText(bundle.getString("colRacetime"));
        DashboardController.ctrl.col_laptime.setText(bundle.getString("colLaptime"));
        DashboardController.ctrl.colLapNumber.setText(bundle.getString("colLapNumber"));
        DashboardController.ctrl.col_typetop.setText(bundle.getString("colTypetop"));
        DashboardController.ctrl.col_comment.setText(bundle.getString("colComment"));

        DashboardController.ctrl.i18n_panelTitle.setText(bundle.getString("panelCarNumber"));
        DashboardController.ctrl.i18n_panelRemainingLaps.setText(bundle.getString("panelRemainingLaps"));
        DashboardController.ctrl.i18n_panelTime.setText(bundle.getString("panelTime"));
        DashboardController.ctrl.i18n_panelState.setText(bundle.getString("panelState"));



    }

    @FXML
     public void saveUnderFile(ActionEvent event) {

        SaveManagerImpl saveManager =  App.getDataManager();
        saveManager.saveFileUnder((Stage) App.getDecorator().getScene().getWindow());
    }

    @FXML
     public void completeSave(ActionEvent event) {
        CompleteSaveStrategy.executeCompleteSave(DashboardController.getRaceModel(), DashboardController.getRaceModel().getTopsMap(), DashboardController.getRaceModel().getCarsList(), DashboardController.getRaceModel().getPilotsList());
    }
}
