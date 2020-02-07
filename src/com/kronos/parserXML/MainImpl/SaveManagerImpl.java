package com.kronos.parserXML.MainImpl;

import com.kronos.parserXML.api.SaveManager;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
 * * @author TeamKronoS
 *
 * @version 1.0
 * Implementation of the Savemanager interface, this class is a singleton
 */
public class SaveManagerImpl implements SaveManager {


    /**
     * collection of models to be persisted in the XML file
     */
    private List<Object> listOfBeans;


    /**
     * StringBuilder in charge of containing all the XML tags of the objects to be persisted.
     * It will be updated each time we want to save, keeping the current state of the objects when saving.
     */
    private StringBuilder stringBuilder = null;

    /**
     * Single instance of the class
     */
    private static SaveManagerImpl INSTANCE;

    /**
     * Model parser converter from model class to XML tag
     */
    ModelParser parser;

    /**
     *
     */
    private static String PATH = ".." + File.separator;

    /**
     * name of File
     */
    String nameFile;

    /**
     * XML TAG to be added at the beginning of each XML file for file standardization
     */
    private static String  XML_STANDARD_TAG =  "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";


    /**
     * private constructor
     */
    private SaveManagerImpl() {
        listOfBeans = new ArrayList<Object>();
        stringBuilder = new StringBuilder();
        stringBuilder.append(XML_STANDARD_TAG);
        parser = new ModelParser();
        String [] date_to_format_string = new Date().toString().split(":");
        for (String character : date_to_format_string){
            nameFile +=character;
        }

    }


    /**
     * @return singleton instance of SaveManagerImpl
     */
    public synchronized static SaveManagerImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SaveManagerImpl();
        }
        return INSTANCE;
    }


    /**
     * Link the model to the saveManager to be taken into account for the backup
     *
     * @param modelToSave {@link Object}
     * @return boolean
     */
    public boolean persist(final Object modelToSave) {
        Objects.requireNonNull(modelToSave);
        return listOfBeans.add(modelToSave);
    }

    /**
     * Disconnects the object to the @param model which will no longer be taken into account when saving in XML.
     *
     * @param modelToUnlink {@link Object}
     * @return boolean
     */
    public boolean remove(final Object modelToUnlink) {
        Objects.requireNonNull(modelToUnlink);
        return listOfBeans.remove(modelToUnlink);
    }


    /**
     * disconnects all model objects connected to the manager that will no longer be taken into account when saving in XML.
     */
    public void clear() {
        listOfBeans.clear();
    }

    /**
     * Get all models linked to saveManager
     *
     * @return List
     */
    public List<Object> getListOfBeans() {
        return Collections.unmodifiableList(listOfBeans);
    }


    /**
     * @return String
     */
    public String getNameFile() {
        return nameFile;
    }

    /**
     * @param nameFile
     */
    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }


    @Override
    public boolean saveFile() {
        for (Object beans : listOfBeans) {
            stringBuilder.append(parser.parseModel(beans));
        }

        try {
            String file = PATH + getNameFile() + ".xml";
            File fileXML = new File(file);
            if(fileXML.exists()){
                fileXML.delete();
            }
            FileWriter fileWriter = new FileWriter(fileXML, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.flush();
            bufferedWriter.close();

        } catch (Exception ex) {
            ex.printStackTrace();
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