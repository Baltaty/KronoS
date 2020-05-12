package com.kronos.controller;

import com.kronos.printview.PrinterModel;
import com.qoppa.pdfViewerFX.PDFViewer;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.util.FontPropertiesManager;
import org.icepdf.ri.util.PropertiesManager;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class TimeSheetController implements Initializable {

    @FXML
    AnchorPane baseAnchorPane;


    private static String PATH = System.getProperty("user.dir") + File.separator + "data" + File.separator + "test.pdf";
    private SwingController swingController;
    private JComponent viewerPanel;
    PrinterModel printer;

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        printer =  new PrinterModel();
        try{

            printer.print();
            createViewer(borderPane);
            openDocument(PATH);
        }catch (Exception x){
            x.printStackTrace();
        }


    }


    private void createViewer(BorderPane borderPane) {
        try {
            SwingUtilities.invokeAndWait(() -> {

                swingController = new SwingController();
                swingController.setIsEmbeddedComponent(true);

                PropertiesManager properties = new PropertiesManager(System.getProperties(),
                        ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE));

                properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_FIT, "false");
                properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_ROTATE, "false");
                properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_TOOL, "false");
                properties.set(PropertiesManager.PROPERTY_DEFAULT_ZOOM_LEVEL, "1.25");
                properties.setBoolean(PropertiesManager.PROPERTY_SHOW_STATUSBAR_VIEWMODE, Boolean.FALSE);
                properties.set(PropertiesManager.PROPERTY_SHOW_TOOLBAR_PAGENAV, "false");

                ResourceBundle messageBundle = ResourceBundle.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE);

                new FontPropertiesManager(properties, System.getProperties(), messageBundle);

                swingController.getDocumentViewController().setAnnotationCallback(
                        new org.icepdf.ri.common.MyAnnotationCallback(swingController.getDocumentViewController()));
                SwingViewBuilder factory = new SwingViewBuilder(swingController, properties);
                viewerPanel = factory.buildViewerPanel();
                viewerPanel.revalidate();
                SwingNode swingNode = new SwingNode();
                swingNode.setContent(viewerPanel);
                borderPane.setCenter(swingNode);
                swingController.setToolBarVisible(false);
                swingController.setUtilityPaneVisible(false);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openDocument(String document) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingController.openDocument(document);
                viewerPanel.revalidate();
            }
        });
    }
}
