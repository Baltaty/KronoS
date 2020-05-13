package com.kronos.parserXML.MainImpl;

import com.kronos.App;
import com.kronos.model.LapRaceModel;
import com.kronos.model.TopModel;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.transform.JDOMSource;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;

public class ProccessSave extends  Thread {

     private SaveManagerImpl saveManager = App.getDataManager();
     private Object object ;
     private String tag;
     private String  id_object;

    public ProccessSave(final Object object, final String tag, final String id_object) {
        SaveManagerImpl saveManager = App.getDataManager();
        this.object = object;
        this.tag = tag;
        this.id_object =  id_object;
    }


    public void run(){
        this.save();
    }

    public void save() {
        this.saveManager.updateTagInXML(this.object,this.tag, this.id_object);
    }
}
