package com.kronos.parserXML.api;

import com.kronos.model.GenericParser;

/**
 * @author TeamKronoS
 * @version 1.0
 * Represents data save manager.
 */
public interface SaveManager {


    /**
     *
     */
    void persist(final Object modelToSave);

    /**
     * saves all java objects related to the class in xml format.
     * @return true
     */
    boolean saveFile();


    /**
     * Reverse process of the method undoState
     */
    void redoState();

    /**
     * Returns to the previous state of the object.
     */
    void undoState();



}
