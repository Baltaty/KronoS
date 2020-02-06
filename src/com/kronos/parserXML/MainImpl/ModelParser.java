package com.kronos.parserXML.MainImpl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.regex.Pattern;

public class ModelParser {

    /**
     * constructir
     */
    public ModelParser() {
    }


    public StringBuilder parseModel(Object parceleable) {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(parceleable.getClass());
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

            // Remove tag <?xml version ... ?>  generate by the api JAXB to clean beans xml tag
            String pattern = "<\\?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"\\?>";
            Pattern boldPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            xmlContent = boldPattern.matcher(xmlContent).replaceAll("");
            return new StringBuilder(xmlContent);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


}
