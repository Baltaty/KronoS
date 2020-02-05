package com.kronos.parserXML;

import com.kronos.parserXML.api.Parceleable;
import com.kronos.api.Pilote;
import com.kronos.model.PiloteModel;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Date;

public class ModelParser {

    public ModelParser(){};


     public boolean parseModel(Parceleable parceleable){

        try{
            JAXBContext jaxbContext =  JAXBContext.newInstance(parceleable.getClass());
            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Print XML String to Console
            StringWriter stringXml = new StringWriter();


            //Write XML to StringWriter
            jaxbMarshaller.marshal(parceleable, stringXml);

            //Verify XML Content
            String xmlContent = stringXml.toString();
            System.out.println( xmlContent );

        }catch (Exception ex){
            ex.printStackTrace();
        };

        return true;
    }

    public static void main(String [] args ){

       Pilote pts = new PiloteModel(1, "Test", "Prenom", "testComment", new Date(), 12 , 12);
       ModelParser modelParser = new ModelParser();
       modelParser.parseModel(pts);
    }
}
