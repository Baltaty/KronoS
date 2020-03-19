package com.kronos.parserXML.MainImpl;

import com.kronos.api.Observer;
import com.kronos.api.Subject;
import com.kronos.parserXML.api.SaveManager;
import com.sun.xml.internal.ws.message.ProblemActionHeader;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.*;

/**
 * * @author TeamKronoS
 *
 * @version 1.0
 * Implementation of the Savemanager interface, this class is a singleton
 */
public class SaveManagerImpl implements SaveManager, Subject {

    private List<Observer> observers = new ArrayList<>();

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
    private ModelParser parser;

    /**
     *
     */

    private static String PATH = System.getProperty("user.dir") + File.separator + "data" + File.separator;


    /**
     * XML TAG to be added at the beginning of each XML file for file standardization
     */
    private static String XML_STANDARD_TAG = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

    /**
     *
     */
    private ImportManagerImpl importManager;


    private static final String CONTENT_TAG = "<data>", CONTENT_END_TAG = "</data>";


    public ImportManagerImpl getImportManager() {
        return this.importManager;
    }

    /**
     * private constructor
     */
    private SaveManagerImpl() {
        listOfBeans = new ArrayList<Object>();

        parser = new ModelParser();
        PATH += "course_numero-" + new Date().getTime() + ".xml";
        importManager = new ImportManagerImpl();


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
    @Override
    public boolean persist(final Object modelToSave) {
        Objects.requireNonNull(modelToSave);
        return listOfBeans.add(modelToSave);
    }


    public void persist(final Collection<? extends Object> collections) {
        Objects.requireNonNull(collections);
        listOfBeans.addAll(collections);
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
     * @param fileXML
     * @return boolean
     */
    private boolean processSave(File fileXML) {
        if (fileXML == null)
            return false;

        try {
            stringBuilder = new StringBuilder();
            stringBuilder.append(XML_STANDARD_TAG);
            stringBuilder.append("\n" + CONTENT_TAG + "\n");
            for (Object beans : listOfBeans) {
                stringBuilder.append(parser.parseModel(beans));
            }
            stringBuilder.append("\n" + CONTENT_END_TAG + "\n");

            if (fileXML.exists()) {
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

        return false;
    }


    /**
     * @return boolean
     */
    @Override
    public boolean saveFile() {
        File fileXML = new File(PATH);
        return processSave(fileXML);
    }


    /**
     * @param stage
     * @return boolean
     */
    public boolean saveFileUnder(Stage stage) {
        String file = getPATH();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(file);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.xml")
        );
        File selectedFile = fileChooser.showSaveDialog(stage);
        setPATH(selectedFile.getAbsolutePath());
        return processSave(selectedFile);
    }


    @Override
    public void undoState() {

    }

    @Override
    public void redoState() {

    }

    /**
     * @param typeClass
     * @return
     */
    public List<Object> getModels(final Class typeClass) {

        List<Object> objects = new ArrayList<>();

        for (Object model : listOfBeans) {
            if (typeClass.isInstance(model)) {
                objects.add(model);
            }
        }

        return objects;
    }


    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public static String getPATH() {
        return PATH;
    }

    public static void setPATH(String PATH) {
        SaveManagerImpl.PATH = PATH;
    }
}
