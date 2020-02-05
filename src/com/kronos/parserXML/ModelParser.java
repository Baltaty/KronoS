package com.kronos.parserXML;

import com.kronos.parserXML.api.Parceleable;
import com.kronos.api.Pilote;
import com.kronos.model.PiloteModel;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Date;
import java.util.regex.Pattern;

public class ModelParser {

    public ModelParser(){};


     public StringBuilder parseModel(Object parceleable){

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
            String pattern = "<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>";
            Pattern boldPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE );
            xmlContent = boldPattern.matcher( xmlContent ).replaceAll( "");
            return new StringBuilder(xmlContent);

        }catch (Exception ex){ ex.printStackTrace(); }

        return null;
    }


}
