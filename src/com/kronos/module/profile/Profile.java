 
package com.kronos.module.profile;

import  com.kronos.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.controlsfx.control.Rating;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

 /**
 * @author TeamKronos
 * 
 * Version 1.0
 */
public class Profile implements Initializable {

    @FXML private Label note;
    @FXML private Rating rating;
    @FXML private Label fullName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        note.textProperty().bind(rating.ratingProperty().asString(Locale.ENGLISH, "%.2f"));
        fullName.textProperty().bind(App.getUserDetail().textProperty());
    }
}
