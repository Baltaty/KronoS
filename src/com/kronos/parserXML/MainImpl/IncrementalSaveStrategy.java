package com.kronos.parserXML.MainImpl;

import com.kronos.model.TopModel;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IncrementalSaveStrategy {

    private static String PATH = System.getProperty("user.dir") + File.separator + "data" + File.separator + "hotReloadBackup" + File.separator + "tops" + File.separator;
    private static ArrayList<TopModel> topsToSave = new ArrayList<>();

    public static void executeIncrementalSave() {
        saveTopsIncremental(topsToSave);
    }

    private static void saveTopsIncremental(List<TopModel> topsToSave) {
        Document doc = new Document();
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        for(TopModel top : topsToSave) {
            Element topElem = new Element("Top");
            topElem.addContent(new Element("Id").setText(top.getId().toString()));
            topElem.addContent(new Element("CarNumber").setText(Integer.toString(top.getCarNumber())));
            topElem.addContent(new Element("Position").setText(Integer.toString(top.getTopPosition())));
            topElem.addContent(new Element("Time").setText(top.getTime()));
            topElem.addContent(new Element("Type").setText(top.getTopType()));
            topElem.addContent(new Element("RaceTime").setText(top.getRaceTime()));
            topElem.addContent(new Element("Lap").setText(Integer.toString(top.getLap())));
            topElem.addContent(new Element("LapTime").setText(top.getLapTime()));
            topElem.addContent(new Element("Comment").setText(top.getComment()));
            doc.setRootElement(topElem);
            try {
                xmlOutputter.output(doc, new FileOutputStream(PATH + top.getId().toString() + ".xml"));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        topsToSave.clear();
    }

    public static void removeTop(TopModel top) {
        File f = new File(PATH + top.getId().toString() + ".xml");
        if(f.delete()) {
            System.out.println("Top deleted successfully !");
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(PATH + "removedTops.txt", true));
                File file = new File(PATH + "removedTops.txt");
                if(file.length() != 0) {
                    bw.newLine();
                }
                bw.write(top.getId().toString());
                bw.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Fail to delete top !");
        }
    }

    public static void logTopToSave(TopModel top) {
        topsToSave.add(top);
    }

}
