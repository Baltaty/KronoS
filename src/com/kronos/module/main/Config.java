 
package com.kronos.module.main;

import  com.kronos.App;
import  com.kronos.global.plugin.ViewManager;
import   com.gn.decorator.GNDecorator;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

 /**
 * @author TeamKronos
 * 
 * Version 1.0
 */ 
public class Config implements Initializable {

    @FXML
    private JFXButton btn_theme;
    
    @FXML
    private JFXButton btn_fr;
    
    @FXML
    private JFXButton btn_eng;
    

    @FXML
    public VBox options;

    public static  Config ctrl;
    
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
            invert = true;
        } else {
            App.decorator.initTheme(GNDecorator.Theme.DEFAULT);
            theme = "light.css";
            btn_theme.setText("Mode nuit : desactivé");
            invert = false;
        }

        ObservableList<String> stylesheets = App.decorator.getStage().getScene().getStylesheets();

        stylesheets.addAll(
                getClass().getResource(path + "fonts.css").toExternalForm(),
                getClass().getResource(path + "material-color.css").toExternalForm(),
                getClass().getResource(path + "skeleton.css").toExternalForm(),
                getClass().getResource(path + "" + theme).toExternalForm(),
                getClass().getResource(path + "bootstrap.css").toExternalForm(),
                getClass().getResource(path + "simple-green.css").toExternalForm(),
                getClass().getResource(path + "shape.css").toExternalForm(),
                getClass().getResource(path + "typographic.css").toExternalForm(),
                getClass().getResource(path + "helpers.css").toExternalForm(),
                getClass().getResource(path + "master.css").toExternalForm()
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
        Main.ctrl.title.setText(bundle.getString("title"));
    }
}
