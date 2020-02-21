package com.kronos.model;

import com.kronos.api.Config;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents a configuration (language, mode, top keyboard control).
 */
public class ConfigModel  implements Config {

    /**
     * Constructor.
     */
    public ConfigModel() {

    }

    /**
     * Changes the language of the application
     */
    @Override
    public void changeLanguage() {

    }

    /**
     * Changes between the dark mode and the light mode
     */
    @Override
    public void changeMode() {

    }

    /**
     * Changes the assignment of a keyboard key for the {@link TopModel top}
     */
    @Override
    public void changeTopControl() {

    }
}
