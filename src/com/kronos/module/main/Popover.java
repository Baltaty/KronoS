 
package com.kronos.module.main;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

 /**
 * @author TeamKronos
 * 
 * Version 1.0
 */ 
public class Popover implements Initializable {

    @FXML
    private JFXButton theme;

    @FXML
    public VBox options;

    public static Popover ctrl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ctrl = this;
    }

}