package com.kronos.parserXML.MainImpl;

import com.kronos.App;
import com.kronos.model.*;
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
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;


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

            chekingObjects(models);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return models;
    }

    /**
     * methode charging le content
     * @param datas
     */
    private void chekingObjects(List<? extends Object> datas) {

        Map<Integer, ArrayList<TopModel>> topsMap = new HashMap<>();

        for (Object object : datas) {
            if (TopModel.class.isInstance(object)) {
                TopModel model = (TopModel) object;
                Integer numberOfcar = new Integer(model.getCarNumber());

                if (topsMap.containsKey(numberOfcar)) {
                    List<TopModel> topModelList = topsMap.get(numberOfcar);
                    topModelList.add(model);
                }
                else{
                    ArrayList<TopModel> topModelList = new ArrayList<>();
                    topModelList.add(model);
                    topsMap.put(numberOfcar, topModelList);
                }

            }
        }


        for(Object object : datas){
            if(RaceModel.class.isAssignableFrom(object.getClass())){
                RaceModel raceModel = (RaceModel) object;
                raceModel.getTopsMap().clear();
                raceModel.getTopsMap().putAll(topsMap);
//                System.out.println(" ===== ImportManager checkObject : je suis issue de Racemodel : " + object.toString()  );
//                System.out.println(raceModel.getTopsMap().toString());
                break;
            }
        }

    }


    /**
     * @param tag
     * @return
     */
    private List<String> getSelectedTagXml(String tag) {


        /**
         * content of xml convert in stringbuilder for extracting
         */
        StringBuilder contentBuilder;
        StringBuffer sb = new StringBuffer();
        List<String> listOfObjectToDesialize = new ArrayList<>();

        try {

           String extractableXML = getXtratable();
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(new StringReader(extractableXML));
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



    protected String getXtratable (){
        String extractableXML = null;
        try {
            FileInputStream fis = new FileInputStream(fileXML);
            InputStream inputofilestream = new BufferedInputStream(fis);
            StringBuilder stringBuilder = null;
            try (Scanner scanner = new Scanner(inputofilestream, StandardCharsets.UTF_8.name())) {
                extractableXML = scanner.useDelimiter("\\A").next();
                String pattern = "<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>";
                Pattern boldPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                extractableXML = boldPattern.matcher(extractableXML).replaceAll("");
                stringBuilder = new StringBuilder();
                stringBuilder.insert(0, "<data>");
                stringBuilder.append(extractableXML);
                stringBuilder.append("</data>");
                extractableXML = stringBuilder.toString();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  extractableXML;
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

            App.getDataManager().persist(models, Boolean.TRUE);
            App.getDataManager().setPATH(this.fileXML.getPath());

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }


        return true;
    }

    protected String getModelName(String name){

        for (String model : allModels){
            if(name.contains(model)){
                return firstCharToLowerCase(model);
            }
        }
        return null;
    }


    public static String getPATH() {
        return PATH;
    }

    public static void setPATH(String PATH) {
        ImportManagerImpl.PATH = PATH;
    }
}
