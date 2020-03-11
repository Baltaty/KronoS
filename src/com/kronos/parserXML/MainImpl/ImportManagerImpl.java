package com.kronos.parserXML.MainImpl;

import com.kronos.model.LapRaceModel;
import com.kronos.parserXML.api.ImportManager;
import com.kronos.parserXML.api.SaveManager;
import org.jdom2.Document;

import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;


/**
 * * @author TeamKronoS
 *
 * @version 1.0
 * Implementation of the ImportManagerImpl interface, this class is a singleton
 */
public class ImportManagerImpl implements ImportManager {

    public static String PATH = "data" + File.separator + "fichier1.xml";

    private File fileXML;

    /**
     * @param path
     * @return
     */
    @Override
    public List<Object> loadFileXml(String path) {

        try {

            fileXML = new File(PATH);
            if (!fileXML.exists()) {
                throw new FileNotFoundException(" fichier non trouvé");
            } else {
                System.out.println(fileXML.getAbsolutePath());
            }

            String xmlTag = this.getSelectedTagXml("lapRaceModel");

            if (xmlTag != null) {
                System.out.println(xmlTag);
                LapRaceModel lapRaceModel = (LapRaceModel) this.reconstructData(xmlTag, LapRaceModel.class);
                System.out.println( lapRaceModel.getRaceName());
                System.out.println("=========");
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param tag
     * @return
     */
    private String getSelectedTagXml(String tag) {

        String content;
        StringBuilder contentBuilder;
        StringBuffer sb = new StringBuffer();

        try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(fileXML);
            Element rootNode = document.getRootElement();

            List<Element> elements = rootNode.getChildren(tag);
            for (Element element : elements) {
                XMLOutputter outp = new XMLOutputter();
                outp.setFormat(Format.getCompactFormat());
                StringWriter sw = new StringWriter();
                outp.output(element.getContent(), sw);
                sb = sw.getBuffer();

            }

            if (sb == null) {
                throw new ClassNotFoundException(" le model rechercher n'existe pas dans le fichier XML");
            }
            contentBuilder = new StringBuilder();
            contentBuilder.append("<" + tag + ">");
            contentBuilder.append(sb);
            contentBuilder.append("</" + tag + ">");
            content = contentBuilder.toString();


        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return content;
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

    public static void main(String[] args) {
        ImportManager manager = new ImportManagerImpl();
        manager.loadFileXml(PATH);
    }
}
