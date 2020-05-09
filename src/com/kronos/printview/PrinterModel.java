package com.kronos.printview;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.kronos.App;
import com.kronos.model.CarModel;
import com.kronos.model.PilotModel;
import com.kronos.model.RaceModel;
import com.kronos.model.TopModel;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PrinterModel {


    private static String PATH = System.getProperty("user.dir") + File.separator + "data" + File.separator;
    private static String css = System.getProperty("user.dir") + File.separator +
            "src" + File.separator + "com" + File.separator + "kronos" + File.separator + "printview" + File.separator + "style.css";


    private StringBuilder cssContent;
    private StringBuilder dynamicContent;
    private StringBuilder infSup;
    private StringBuilder courseContent;
    private String htmlcontent;
    private StringBuilder[] pilotInfo;
    private StringBuilder finalContent;


    public PrinterModel() {
        dynamicContent = new StringBuilder();
        infSup = new StringBuilder();
        courseContent = new StringBuilder();
        finalContent = new StringBuilder();
        try (InputStream inputStream = new FileInputStream(css)) {
            cssContent = new StringBuilder();
            Scanner sc = new Scanner(inputStream);
            StringBuffer sb = new StringBuffer();
            while (sc.hasNext()) {
                cssContent.append(sc.nextLine());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void print() {

        List<PilotModel> pilotModelList = (List<PilotModel>) (List<?>) App.getDataManager().getModels(PilotModel.class);
        pilotInfo = new StringBuilder[pilotModelList.size()];
        RaceModel raceModel = (RaceModel) App.getDataManager().getModels(RaceModel.class).get(0);

        if (raceModel != null) {
            System.out.println(" not null");
            courseContent = new StringBuilder();
            courseContent.append("<div class=\"name\">Course : " + raceModel.getRaceName() + "</div>\n");
            courseContent.append("<div class=\"name\">Circuit : " + raceModel.getRacewayName() + "</div>\n");
            courseContent.append("<div class=\"date\">Date course : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(raceModel.getStartingTime()) + "</div>\n");
            courseContent.append("<div class=\"name\">Nombre de tour effectué : " + raceModel.getTimeLapsSpent() + "</div>\n");

            String etat = "Terminée";
            switch (raceModel.getRaceState()) {
                case CREATION:
                    etat = "Crée en attente de top";
                case IN_PROGRESS:
                    etat = "En cours d'execution";
                    break;
                case DONE:
                case BREAK:
                    etat = " Terminée";
                    break;
                default:
                    break;
            }
            courseContent.append("<div class=\"name\">Etat course : " + etat + "</div>\n");


        }

        if (!pilotModelList.isEmpty()) {

            pilotInfo =  new StringBuilder[pilotModelList.size()];
            for (PilotModel pilot : pilotModelList) {
                infSup = new StringBuilder();
                infSup.append("<h2 class=\"name\">" + pilot.getLastName() + " " + pilot.getFirstName() + ", </h2> \n");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = simpleDateFormat.format(pilot.getDateOfBirth());
                infSup.append("<div class=\"address\">Date de Naissance : " + date + "</div>\n");
                infSup.append("<div class=\"address\">Poids : " + pilot.getWeight() + " kg </div>\n");
                infSup.append("<div class=\"address\">Taille : " + pilot.getHeight() + " cm </div>\n");
                infSup.append("<div class=\"address\">Commentaire : " + pilot.getComment() + "</div>\n");

                dynamicContent = new StringBuilder();
                for (TopModel top : findTopByPilot(pilot)) {

                    dynamicContent.append(" <tr> \n ");
                    dynamicContent.append("<td class=\"total\"> " + top.getLap() + " </td>\n");
                    dynamicContent.append("<td class=\"desc\"> " + top.getCarNumber() + " </td>\n");
                    dynamicContent.append("<td class=\"unit\"> " + top.getTopType() + " </td>\n");
                    dynamicContent.append("<td class=\"unit\"> " + top.getTime()+ " </td>\n");
                    dynamicContent.append("<td class=\"qty\"> " + top.getLapTime() + " </td>\n");
                    dynamicContent.append("<td class=\"desc\"> " + top.getComment() + " </td>\n");
                    dynamicContent.append("</tr> \n ");
                }

                if(pilotModelList.indexOf(pilot) > 0 ) {
                    courseContent.setLength(0);
                }

                StringBuilder str = new StringBuilder();
                str.append(
               "     <div id=\"details\" class=\"\">\n" +
               "       <div id=\"client\">\n" +
                 infSup.toString() +
               "       </div>\n" +
               "       <div id=\"invoice\">\n" +
               courseContent.toString() +
               "       </div>\n" +
               "     </div>\n" +
               "      <br/>\n" +
               "      <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
               "        <thead>\n" +
               "          <tr>\n" +
               "            <th class=\"total \">Tour</th>\n" +
               "            <th class=\"desc\">Voiture</th>\n" +
               "            <th class=\"unit\">Type Top</th>\n" +
               "            <th class=\"unit\">Heure</th>\n" +
               "            <th class=\"qty\">Temps</th>\n" +
               "            <th class=\"\">Commentaires</th>\n" +
               "          </tr>\n" +
               "        </thead>\n" +
               "        <tbody>\n" +
               dynamicContent.toString() +
               "        </tbody>\n" +
               "      </table>\n"
                );
                pilotInfo[pilotModelList.indexOf(pilot)] =  str;

            }

            finalContent.setLength(0);
            for(StringBuilder st : pilotInfo){
                finalContent.append(st.toString());
            }

            System.out.println("PrinterModel : print-methode");


        }


        try {

            // step 1

            Document document = new Document();
            document.setPageSize(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(PATH + "test.pdf"));
            document.open();
            InputStream is = new ByteArrayInputStream(generateContent().getBytes());

            // CSS
            CSSResolver cssResolver = new StyleAttrCSSResolver();
            CssFile cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream(cssContent.toString().getBytes()));
            cssResolver.addCss(cssFile);
            // HTML
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            // Pipelines
            PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
            HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline csspipeline = new CssResolverPipeline(cssResolver, html);
            // XML Worker
            XMLWorker worker = new XMLWorker(csspipeline, true);
            XMLParser p = new XMLParser(worker);
            p.parse(is);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public List<TopModel> findTopByPilot(PilotModel pilot) {
        List<TopModel> listToSend = new ArrayList<>();
        List<TopModel> topModelList = (List<TopModel>) (List<?>) App.getDataManager().getModels(TopModel.class);
        List<CarModel> carModelList = (List<CarModel>) (List<?>) App.getDataManager().getModels(CarModel.class);

        for (CarModel car : carModelList) {
            if (car.getPilotModel().getId() == pilot.getId()) {
                System.out.println("PrinterModel : findByMethode-methode");
                System.out.println("is equal " + true);
                for (TopModel top : topModelList) {
                    if (car.getNumber() == top.getCarNumber())
                        listToSend.add(top);
                }
            }
        }
        Collections.sort(listToSend);
        return listToSend;
    }




    public String generateContent() {
        this.htmlcontent = "\n" +
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"utf-8\"/>\n" +
                "    <title>Example 2</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <header class=\"clearfix\">\n" +
                "      <hr/>\n" +
                "      <div id=\"head\">\n" +
                "        <h1> KRONOS FEUILLE DE TEMPS  </h1>\n" +
                "        <div class=\"my-2\"><h1> INFO PLIOTES  </h1></div>\n" +
                "      </div>\n" +
                "    </header>\n" +
                "    <br/>\n" +
                "    <main>\n" +
                finalContent.toString() +
                "    </main>\n" +
                "  </body>\n" +
                "</html>";

        return this.htmlcontent;
    }


}
