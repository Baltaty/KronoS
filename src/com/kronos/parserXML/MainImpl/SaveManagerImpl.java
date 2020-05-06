package com.kronos.parserXML.MainImpl;

import com.kronos.api.Observer;
import com.kronos.api.Subject;
import com.kronos.parserXML.api.SaveManager;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.transform.JDOMSource;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
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

    /**
     *
     */
    private Map<Object, Boolean> mapOfbeans;


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
        mapOfbeans = new HashMap<>();

        parser = new ModelParser();
        PATH += "course_numero-" + new Date().getTime() + ".xml";
        importManager = new ImportManagerImpl(PATH);


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
    public void persist(final Object modelToSave) {
        Objects.requireNonNull(modelToSave);
        if (this.updateObject(modelToSave))
        mapOfbeans.put(modelToSave, Boolean.FALSE);
    }

    /**
     *
     * @param collections
     */
    public void persist(final Collection<? extends Object> collections) {
        Objects.requireNonNull(collections);
        collections.forEach(items -> mapOfbeans.put(items, Boolean.FALSE));

    }



    private boolean updateObject(Object object){
        boolean isDone =  true;
        try {
            if (mapOfbeans.containsKey(object)) {

                String model = this.importManager.getModelName(object.getClass().getName());
                this.updateTagInXML(model);
                mapOfbeans.put(object, false);

            }

        }catch (Exception e){
            isDone =false;
        }

        return isDone;
    }


    protected void updateTagInXML(String tag) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SAXBuilder saxBuilder = new SAXBuilder();
                    Document doc = saxBuilder.build(new StringReader(importManager.getXtratable()));
//                    System.out.println(importManager.getXtratable());
                    doc.getRootElement().removeChild(tag);

                    TransformerFactory tf = TransformerFactory.newInstance();
                    Transformer transformer = tf.newTransformer();
                    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                    StringWriter writer = new StringWriter();
                    transformer.transform(new JDOMSource(doc), new StreamResult(writer));
                    String output = writer.getBuffer().toString();


//                    System.out.println("************ after delete data *****************");
                    output = output.replaceAll("<data>", "");
                    output = output.replaceAll("</data>", "");
                    output = XML_STANDARD_TAG + "\n" + output;
//                    System.out.println(output);
                    BufferedWriter bufferedWriterwriter = new BufferedWriter(new FileWriter(PATH));
                    bufferedWriterwriter.write(output);
                    bufferedWriterwriter.close();


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();

    }

    /**
     *
     * @param collections
     * @param modePersit
     */
    public void persist(final Collection<? extends Object> collections, Boolean modePersit) {
        Objects.requireNonNull(collections);
        collections.forEach(items -> mapOfbeans.put(items, modePersit));

    }



    /**
     * Disconnects the object to the @param model which will no longer be taken into account when saving in XML.
     *
     * @param modelToUnlink {@link Object}
     * @return boolean
     */
    public boolean remove(final Object modelToUnlink) {
        Objects.requireNonNull(modelToUnlink);
        return mapOfbeans.remove(modelToUnlink);
    }


    /**
     * disconnects all model objects connected to the manager that will no longer be taken into account when saving in XML.
     */
    public void clear() {
        mapOfbeans.clear();
    }

    /**
     * Get all models linked to saveManager
     *
     * @return List
     */
    public List<Object> getListOfBeans() {

        List<Object> listOfObject = new ArrayList<>(mapOfbeans.keySet());
        return Collections.unmodifiableList(listOfObject);
    }


    /**
     * @param fileXML
     * @return boolean
     */
    private boolean processSave(File fileXML, Boolean allOjectPersist) {
        if (fileXML == null)
            return false;

        try {
            stringBuilder = new StringBuilder();

            /*  si le fichier  n'existe pas c'est à créer une premiere fois on ajoute le tag standarisation XML au fichier*/
            if (!fileXML.exists()) {
                stringBuilder.append(XML_STANDARD_TAG);
            }

            /* stringBuilder.append("\n" + CONTENT_TAG + "\n"); */
            Iterator it = mapOfbeans.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry couple = (Map.Entry) it.next();
                if(allOjectPersist){
                    stringBuilder.append(parser.parseModel(couple.getKey()));;
                } else if (couple.getValue().equals(Boolean.FALSE)) {
                    stringBuilder.append(parser.parseModel(couple.getKey()));
                    couple.setValue(Boolean.TRUE);
                }
            }
            /* stringBuilder.append("\n" + CONTENT_END_TAG + "\n"); */


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
        return processSave(fileXML, Boolean.FALSE);
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
        return processSave(selectedFile, Boolean.TRUE);
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
        for (Object model : mapOfbeans.keySet()) {
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////                                 /////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
