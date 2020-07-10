package com.kronos.global.util;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

/**
 * @author TeamKronos
 * @version 1.0
 * Creates alert windows reacting to different events (success, info, warning or error).
 */
public class Alerts {

    /**
     * Creates a warning alert.
     * @param title alert tile
     * @param content alert content
     */
    public static void warning(String title, String content) {
        Dialog.createAlert(Dialog.Type.WARNING, title, content);
    }

    /**
     * Creates a warning alert with the ability to receive a {@link MouseEvent mouse event}.
     * @param title alert title
     * @param content alert content
     * @param confirm alert {@link MouseEvent mouse event} confirmation
     */
    @SafeVarargs
    public static void warning(String title, String content, EventHandler<MouseEvent>... confirm) {
        Dialog.createAlert(Dialog.Type.WARNING, title, content, confirm);
    }

    /**
     * Creates an error alert.
     * @param title alert title
     * @param content alert content
     */
    public static void error(String title, String content) {
        Dialog.createAlert(Dialog.Type.ERROR, title, content);
    }

    /**
     * Creates an error alert with the ability to receive a {@link MouseEvent mouse event}.
     * @param title alert title
     * @param content alert content
     * @param confirm alert {@link MouseEvent mouse event} confirmation
     */
    @SafeVarargs
    public static void error(String title, String content, EventHandler<MouseEvent>... confirm) {
        Dialog.createAlert(Dialog.Type.ERROR, title, content, confirm);
    }

    /**
     * Creates an info alert.
     * @param title alert title
     * @param content alert content
     */
    public static void info(String title, String content) {
        Dialog.createAlert(Dialog.Type.INFO, title, content);
    }

    /**
     * Creates an info alert with the ability to receive a {@link MouseEvent mouse event}.
     * @param title alert title
     * @param content alert content
     * @param confirm alert {@link MouseEvent mouse event} confirmation
     */
    @SafeVarargs
    public static void info(String title, String content, EventHandler<MouseEvent>... confirm) {
        Dialog.createAlert(Dialog.Type.INFO, title, content, confirm);
    }

    /**
     * Creates a success alert.
     * @param title alert title
     * @param content alert content
     */
    public static void success(String title, String content) {
        Dialog.createAlert(Dialog.Type.SUCCESS, title, content);
    }

    /**
     * Creates a success alert with the ability to receive a {@link MouseEvent mouse event}.
     * @param title alert title
     * @param content alert content
     * @param confirm alert {@link MouseEvent mouse event} confirmation
     */
    @SafeVarargs
    public static void success(String title, String content, EventHandler<MouseEvent>... confirm) {
        Dialog.createAlert(Dialog.Type.SUCCESS, title, content, confirm);
    }

    public static   void  AlertError( String title,  String Content){
        Alert dialogW = new Alert(Alert.AlertType.ERROR);
        dialogW.setTitle(title);
        dialogW.setHeaderText(null);
        dialogW.setContentText(Content);
        dialogW.showAndWait();

    }

    public static   void  AlertSuccess( String title,  String Content){
        Alert dialogW = new Alert(Alert.AlertType.INFORMATION);
        dialogW.setTitle(title);
        dialogW.setHeaderText(null);
        dialogW.setContentText(Content);
        dialogW.showAndWait();

    }
    public static   void  AlertWarning( String title,  String Content){
        Alert dialogW = new Alert(Alert.AlertType.WARNING);
        dialogW.setTitle(title);
        dialogW.setHeaderText(null);
        dialogW.setContentText(Content);
        dialogW.showAndWait();

    }
    public  static Optional<ButtonType> AlertConfirmation (String tiltle, String content){

        Alert dialogC = new Alert(Alert.AlertType.CONFIRMATION);
        dialogC.setTitle(tiltle);
        dialogC.setHeaderText(null);
        dialogC.setContentText(content);

        return dialogC.showAndWait();

    }

}
