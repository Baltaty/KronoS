package com.kronos.parserXML.MainImpl;

import com.kronos.App;
import com.kronos.model.*;
import com.kronos.module.main.Config;
import com.kronos.parserXML.api.ImportManager;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * * @author TeamKronoS
 *
 * @version 1.0
 * Implementation of the ImportManagerImpl interface, this class is a singleton
 */
public class ImportManagerImpl implements ImportManager {

    public static String PATH = "data" + File.separator + "fichier1.xml";

    private String[] allModels = {"ConfigModel", "LapRaceModel", "MainCarModel", "PilotModel", "PrintModel"
            , "RaceModel", "RivalCarModel", "TimeRaceModel", "TopModel"};


    private File fileXML;

    /**
     *
     */
    public ImportManagerImpl() {

    }

    /***
     *
     * @param str
     * @return
     */
    private String firstCharToLowerCase(String str) {

        if (str == null || str.length() == 0)
            return "";

        if (str.length() == 1)
            return str.toLowerCase();

        char[] chArr = str.toCharArray();
        chArr[0] = Character.toLowerCase(chArr[0]);

        return new String(chArr);
    }


    /**
     * @param fileXML
     * @return
     */
    @Override
    public List<? extends Object> loadFileXml(File fileXML) {

        ArrayList models = new ArrayList();

        try {

            if (!fileXML.exists()) {
                throw new FileNotFoundException("fichier non trouvé");
            } else {
                System.out.println(fileXML.getAbsolutePath());
            }

            for (int i = 0; i < allModels.length; ++i) {


                List<String> xmlTag = this.getSelectedTagXml(firstCharToLowerCase(allModels[i]));

                if (!xmlTag.isEmpty() && xmlTag != null) {

                    for (String modelTag : xmlTag) {
                        Class modelClass = Class.forName("com.kronos.model." + allModels[i]);
                        Object objectClass = modelClass.newInstance();
                        objectClass = modelClass.cast(this.reconstructData(modelTag, modelClass));

                        switch (i) {
                            case 0:
                                ConfigModel configModel = (ConfigModel) objectClass;
                                models.add(configModel);
                                break;
                            case 1:
                                LapRaceModel lapRaceModel = (LapRaceModel) objectClass;
                                models.add(lapRaceModel);
                                break;
                            case 2:
                                MainCarModel mainCarModel = (MainCarModel) objectClass;
                                models.add(mainCarModel);
                                break;
                            case 3:
                                PilotModel pilotModel = (PilotModel) objectClass;
                                models.add(pilotModel);
                                break;
                            case 4:
                                PrintModel printModel = (PrintModel) objectClass;
                                models.add(printModel);
                                break;
                            case 5:
                                RaceModel raceModel = (RaceModel) objectClass;
                                models.add(raceModel);
                                break;
                            case 6:
                                RivalCarModel rivalCarModel = (RivalCarModel) objectClass;
                                models.add(rivalCarModel);
                                break;

                            case 7:
                                TimeRaceModel timeRaceModel = (TimeRaceModel) objectClass;
                                models.add(timeRaceModel);
                                break;

                            case 8:
                                TopModel topModel = (TopModel) objectClass;
                                models.add(topModel);
                                break;

                            default:
                                break;

                        }


                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return models;
    }


    /**
     * @param tag
     * @return
     */
    private List<String> getSelectedTagXml(String tag) {


        StringBuilder contentBuilder;
        StringBuffer sb = new StringBuffer();
        List<String> listOfObjectToDesialize = new ArrayList<>();

        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(fileXML);
            Element rootNode = document.getRootElement();

            List<Element> elements = rootNode.getChildren(tag);
            List<Content> contentList = new ArrayList<>();
            for (Element element : elements) {
                contentList.addAll(element.getContent());
                XMLOutputter outputter = new XMLOutputter();
                outputter.setFormat(Format.getCompactFormat());
                StringWriter stringWriter = new StringWriter();
                outputter.output(element.getContent(), stringWriter);
                sb = stringWriter.getBuffer();
                contentBuilder = new StringBuilder();
                contentBuilder.append("<" + tag + ">");
                contentBuilder.append(sb);
                contentBuilder.append("</" + tag + ">");
                String content = contentBuilder.toString();
                listOfObjectToDesialize.add(content);

            }
            if (sb == null) {
                throw new ClassNotFoundException(" le model rechercher n'existe pas dans le fichier XML");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return listOfObjectToDesialize;
    }

    /**
     * @param objectToreload
     * @return
     * @throws JAXBException
     */
    private Object reconstructData(String tagElements, Class objectToreload) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(objectToreload);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        StringReader reader = new StringReader(tagElements);
        Object model = unmarshaller.unmarshal(reader);
        return model;

    }


    /**
     * @param stage
     * @return
     */
    public boolean importFile(Stage stage) {

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.xml")
            );
            this.fileXML = fileChooser.showOpenDialog(stage);
            if (this.fileXML == null) {
                return false;
            }

            List<? extends Object> models = this.loadFileXml(this.fileXML);
            if (models.isEmpty() || models == null) {
                return false;
            }
            App.getDataManager().clear();
            App.getDataManager().persist(models);
            App.getDataManager().setPATH(this.fileXML.getPath());

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }


        return true;
    }

}
