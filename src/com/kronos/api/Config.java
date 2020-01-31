package com.kronos.api;

/**
 * @author TeamKronoS
 * @version 1.0
 *
 * Manages the configuration of the application
 */
public interface Config {

    /**
     * Changing the language of the application
     */
    void changeLanguage();

    /**
     * Changes between the dark mode and the light mode
     */
    void changeMode();

    /**
     * Changes the assignment of a keyboard key for the {@link Top top}
     */
    void changeTopControl();


}
