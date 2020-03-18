package com.kronos.controller;

import com.kronos.api.Top;
import com.kronos.module.main.Main;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Window;

import java.awt.event.KeyEvent;

/**
 * @author TeamKronoS
 * @version 1.0
 * Controller of the {@link com.kronos.model.TopModel top} objects.
 */
public class TopController<ScreenManager> {

    /*

    // Bout de code mis dans le main qui permet le top au clic

    public void keyPressed() {
        Scene scene = Main.popConfig.getScene();
        scene.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                //System.out.println(" je suis dans la fonction  ");
                KeyCode keyCode = event.getCode();

                Integer keyTop = KeyEvent.VK_F1;
                if (keyCode == KeyCode.F1)
                    System.out.println("You have pressed the F1 key ");
            }
        });
    }

     */
}