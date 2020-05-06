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

import java.io.*;
import java.util.Scanner;

public class PrinterModel {


    private static String PATH = System.getProperty("user.dir") + File.separator + "data" + File.separator;
    private static String css = System.getProperty("user.dir") + File.separator +
            "src" + File.separator + "com" + File.separator + "kronos" + File.separator + "printview" + File.separator + "style.css";


    private StringBuilder cssContent;


    public PrinterModel() {
        try (InputStream inputStream = new FileInputStream(css)) {
            //Creating a Scanner object
            cssContent = new StringBuilder();
            Scanner sc = new Scanner(inputStream);
            //Reading line by line from scanner to StringBuffer
            StringBuffer sb = new StringBuffer();
            while (sc.hasNext()) {
                cssContent.append(sc.nextLine());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private String htmlcontent;


    public String getHtmlcontent() {
        return htmlcontent;
    }


    public void print() {


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
                "      <div id=\"company\">\n" +
                "        <h2 class=\"name\">Kronos</h2>\n" +
                "        <div>Feuille de temps</div>\n" +
                "      </div>\n" +
                "       <div id=\"invoice\">\n" +
                "          <h2>Course numero 1-3-2-1jvfejnefjnefjnefjneirvie</h2>\n" +
                "          <div class=\"date\">Date de la course : 01/06/2014</div>\n" +
                "       </div>\n" +
                "    </header>\n" +
                "    <br/>\n" +
                "    <main>\n" +
                "      <div id=\"details\" class=\"\">\n" +
                "        <div id=\"client\">\n" +
                "          <div class=\"to\">TYPE : INFO PILOTES </div>\n" +
                "          <h2 class=\"name\">John Doe</h2>\n" +
                "          <div class=\"address\">Equipe : salavador</div>\n" +
                "          <div class=\"address\">Voiture : Bugatti</div>\n" +
                "          <div class=\"address\">Model : Verronr</div>\n" +
                "          <div class=\"address\">Numero : 360 </div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "      <br/>\n" +
                "      <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "        <thead>\n" +
                "          <tr>\n" +
                "            <th class=\"no\">#</th>\n" +
                "            <th class=\"desc\">DESCRIPTION</th>\n" +
                "            <th class=\"unit\">UNIT PRICE</th>\n" +
                "            <th class=\"qty\">QUANTITY</th>\n" +
                "            <th class=\"total\">TOTAL</th>\n" +
                "          </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n" +
                "          <tr>\n" +
                "            <td class=\"no\">01</td>\n" +
                "            <td class=\"desc\"><h3>Website Design</h3>Creating a recognizable design solution based on the company's existing visual identity</td>\n" +
                "            <td class=\"unit\">$40.00</td>\n" +
                "            <td class=\"qty\">30</td>\n" +
                "            <td class=\"total\">$1,200.00</td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td class=\"no\">02</td>\n" +
                "            <td class=\"desc\"><h3>Website Development</h3>Developing a Content Management System-based Website</td>\n" +
                "            <td class=\"unit\">$40.00</td>\n" +
                "            <td class=\"qty\">80</td>\n" +
                "            <td class=\"total\">$3,200.00</td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td class=\"no\">03</td>\n" +
                "            <td class=\"desc\"><h3>Search Engines Optimization</h3>Optimize the site for search engines (SEO)</td>\n" +
                "            <td class=\"unit\">$40.00</td>\n" +
                "            <td class=\"qty\">20</td>\n" +
                "            <td class=\"total\">$800.00</td>\n" +
                "          </tr>\n" +
                "        </tbody>\n" +
                "        <tfoot>\n" +
                "          <tr>\n" +
                "            <td colspan=\"2\"></td>\n" +
                "            <td colspan=\"2\">SUBTOTAL</td>\n" +
                "            <td>$5,200.00</td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td colspan=\"2\"></td>\n" +
                "            <td colspan=\"2\">TAX 25%</td>\n" +
                "            <td>$1,300.00</td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td colspan=\"2\"></td>\n" +
                "            <td colspan=\"2\">GRAND TOTAL</td>\n" +
                "            <td>$6,500.00</td>\n" +
                "          </tr>\n" +
                "        </tfoot>\n" +
                "      </table>\n" +
                "      <div id=\"thanks\">Thank you!</div>\n" +
                "      <div id=\"notices\">\n" +
                "        <div>NOTICE:</div>\n" +
                "        <div class=\"notice\">A finance charge of 1.5% will be made on unpaid balances after 30 days.</div>\n" +
                "      </div>\n" +
                "    </main>\n" +
                "    <footer>\n" +
                "      Invoice was created on a computer and is valid without the signature and seal.\n" +
                "    </footer>\n" +
                "  </body>\n" +
                "</html>";

        return this.htmlcontent;
    }


    public static void main(String[] args) {
        PrinterModel pt = new PrinterModel();
        pt.print();
    }
}
