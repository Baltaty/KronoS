package com.kronos.global.util;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

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

}
