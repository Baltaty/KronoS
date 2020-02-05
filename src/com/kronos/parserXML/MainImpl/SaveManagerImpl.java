package com.kronos.parserXML.MainImpl;

import com.kronos.parserXML.ModelParser;
import com.kronos.parserXML.api.SaveManager;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * * @author TeamKronoS
 * @version 1.0
 * Implementation of the Savemanager interface, this class is a singleton
 */
public class SaveManagerImpl implements SaveManager {



    /**
     * collection of models to be persisted in the XML file
     */
    private List<Object> listOfBeans;


    /**
     *
     */
    private StringBuilder stringBuilder = null;

    /**
     *
     */
    private static SaveManagerImpl INSTANCE;

    /**
     *
     */
    ModelParser parser;


    /**
     * private constructor
     */
    private SaveManagerImpl(){
        listOfBeans = new ArrayList<Object>();
        stringBuilder = new StringBuilder();
        parser = new ModelParser();
    };



    /**
     *
     * @return SaveManagerImpl
     */
    public synchronized static SaveManagerImpl getInstance(){
        if(INSTANCE == null){
            INSTANCE = new SaveManagerImpl();
        }
        return INSTANCE;
    }

    /**
     * Link the model to the saveManager to be taken into account for the backup
     * @param modelToSave {@link Object}
     * @return boolean
     */
    public boolean persist(final Object modelToSave){
        Objects.requireNonNull(modelToSave);
        return listOfBeans.add(modelToSave);
    }

    /**
     * Disconnects the object to the @param model which will no longer be taken into account when saving in XML.
     * @param modelToUnlink {@link Object}
     * @return boolean
     */
    public boolean remove(final Object modelToUnlink){
        Objects.requireNonNull(modelToUnlink);
        return listOfBeans.remove(modelToUnlink);
    }


    /**
     * disconnects all model objects connected to the manager that will no longer be taken into account when saving in XML.
     */
    public void clear(){
        listOfBeans.clear();
    }

    /**
     * Get all models linked to saveManager
     * @return List
     */
    public List <Object> getListOfBeans(){
        return Collections.unmodifiableList(listOfBeans);
    }


    @Override
    public boolean saveFile() {
        for (Object beans: listOfBeans) {
            stringBuilder.append(parser.parseModel(beans));
        }
        System.out.println(stringBuilder.toString());
        return false;
    }

    @Override
    public void undoState() {

    }

    @Override
    public void redoState() {

    }

}
