package com.kronos.module.main;

import com.gn.GNAvatarView;
import com.jfoenix.controls.*;
import com.kronos.global.plugin.ViewManager;
import com.kronos.global.util.Alerts;
/*import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.skins.LeaderBoardItem;
import eu.hansolo.tilesfx.tools.FlowGridPane;*/
import com.kronos.module.dashboard.Dashboard;
import com.kronos.printview.PrinterModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.TextAlignment;
import org.controlsfx.control.PopOver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

 /**
 * @author TeamKronos
 *
 * Version 1.0
 */
public class Main implements Initializable {

     public JFXButton btnaddpilot1;
     public JFXTextField pilotHeight1;
     public JFXTextArea commentPilot1;
     public JFXTextField pilotWeight1;
     public JFXTextField firstName1;
     public JFXTextField lastNamePilot1;
     public Tab tabPilot1;
     public JFXTabPane newRaceTabPane1;
     public JFXButton btnNextCar1;
     public JFXDatePicker dateOfBirthPilot1;
     public Tab tabCar1;
     public JFXTextField carNumber1;
     public JFXTextField carTeam1;
     public JFXTextField carModel1;
     public JFXTextField carBrand1;
     public JFXButton btnNextLap1;
     public JFXButton add_car_btn1;
     public JFXComboBox carPilot1;
     public JFXComboBox carType1;
     public JFXButton changeTopkeyButton;
     public JFXButton endPara;
     public Label topKey;
     public JFXDialogLayout dialogSelectKey;
     @FXML private GNAvatarView avatar;
    @FXML public  VBox sideBar;
    @FXML private HBox searchBox;
    @FXML private HBox boxStatus;
    @FXML private VBox views;
    @FXML public  ScrollPane body;
    @FXML public  Label title;
    @FXML private TextField search;
    @FXML private ScrollPane scroll;
    @FXML public Button home;
    @FXML private Button  about;
    @FXML public Button  FeuilleTemps;
    @FXML private Button hamburger;
    @FXML private SVGPath searchIcon;
    @FXML private StackPane root;
    @FXML private Button clear;
    @FXML private JFXButton config;
    @FXML private VBox drawer;
    @FXML private RadioButton available;
    @FXML private JFXDialogLayout dialogNew;
    @FXML private JFXDialogLayout dialogEdit;
    @FXML private JFXDialogLayout dialogPara   = new JFXDialogLayout();
     public static ObservableList<String> stylesheets;

     private static final    double TILE_WIDTH  = 300;
     private static final    double TILE_HEIGHT = 200;
     private GridPane grid;

     private FilteredList<Button> filteredList = null;

    public static  final PopOver popConfig = new PopOver();

    private ObservableList<Button> items         = FXCollections.observableArrayList();


    private JFXDialog       dialog          = new JFXDialog();
    private JFXDialogLayout dialog_layout   = new JFXDialogLayout();


    private String path = "/com/kronos/theme/css/";
    boolean scrolling   = false;

    private Parent popContent;
    public static Main ctrl;
    public static boolean isControlSet;
    private boolean changeRequest = false;
    private boolean isTopControlSelected = true;


     @Override
    public void initialize(URL location, ResourceBundle resources)  {
        ctrl = this;
        loadContentPopup();
        body.setContent(ViewManager.getInstance().get("Dashboard"));
        body.fitToHeightProperty().set(true);
        filteredList = new FilteredList<>(items, s -> true);
        search.textProperty().addListener(obs -> {

            String filter = search.getText();
            if (filter == null || filter.length() == 0) {
                barInitial();
                clear.setMouseTransparent(true);
                searchIcon.setContent("M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z");
            } else {
                barFiltered(filter);
                clear.setMouseTransparent(false);
                searchIcon.setContent("M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z");

            }
        });
    }

    @FXML
    private void altLayout() {

        int minimum = 70;
        int max = 250;

        if(drawer.getPrefWidth() == max){

            drawer.setPrefWidth(minimum);
            drawer.getChildren().remove(searchBox);

            for(Node node : views.getChildren()) {
                if(node instanceof Button){
                    ((Button) node).setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    ((Button) node).setAlignment(Pos.BASELINE_CENTER);
                } else if(node instanceof TitledPane){
                    ((TitledPane) node).setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    ((TitledPane) node).setAlignment(Pos.BASELINE_CENTER);
                    ((TitledPane) node).setExpanded(false);
                    ((TitledPane) node).setCollapsible(false);
                } else {
                    break;
                }
            }

            avatar.setStrokeWidth(0);
            addEvents();
        } else {
            drawer.setPrefWidth(max);
            searchBox.toBack();
            avatar.toBack();
            avatar.setStrokeWidth(2);
            for(Node node : views.getChildren()){
                if(node instanceof Button){
                    ((Button) node).setContentDisplay(ContentDisplay.LEFT);
                    ((Button) node).setAlignment(Pos.BASELINE_LEFT);
                } else if(node instanceof TitledPane){
                    ((TitledPane) node).setContentDisplay(ContentDisplay.RIGHT);
                    ((TitledPane) node).setAlignment(Pos.BASELINE_RIGHT);
                    ((TitledPane) node).setCollapsible(true);
                } else {
                    break;
                }
            }
        }
    }

    private void addEvents(){
        VBox drawerContent;

        for (Node node : drawer.getChildren()) { // root
            if (node instanceof ScrollPane){

            }

            else {
                // for others layouts
            }
        }
    }



    private void barInitial(){
        filteredList.setPredicate(s -> true);
        scroll.setContent(views);
        hamburger.setMouseTransparent(false);
    }

    private void barFiltered(String filter){
        views.getChildren().removeAll(home, about);
        filteredList.setPredicate(s -> s.getText().toUpperCase().contains(filter.toUpperCase()));
        scroll.setContent(filter(filteredList));
        hamburger.setMouseTransparent(true);
    }

    private VBox filter(ObservableList<Button>  nodes){
        VBox vBox = new VBox();
        vBox.getStyleClass().add("drawer-content");
        vBox.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        vBox.setAlignment(Pos.TOP_RIGHT);
        VBox.setVgrow(vBox, Priority.ALWAYS);
        for (Button node : nodes){
            if (node.getGraphic() != null) node.setContentDisplay(ContentDisplay.TEXT_ONLY);
            node.setAlignment(Pos.CENTER_LEFT);
        }
        vBox.getChildren().setAll(nodes);
        return vBox;
    }

    private void loadContentPopup(){
        try {
            popContent = FXMLLoader.load(getClass().getResource("/com/kronos/module/main/Config.fxml"));
            popConfig.getRoot().getStylesheets().add(getClass().getResource("/com/kronos/theme/css/poplight.css").toExternalForm());
            popConfig.setContentNode(popContent);
            popConfig.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
            popConfig.setArrowIndent(0);
            popConfig.setArrowSize(0);
            popConfig.setCornerRadius(0);
            popConfig.setAutoFix(true);
            popConfig.setAnimated(false);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(String name) {
         try {
             ViewManager.getInstance().put(
                     name,
                     FXMLLoader.load(getClass().getResource("/com/kronos/view/" + name + ".fxml"))
             );
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

    @FXML
    private void openConfig(){
        if(popConfig.isShowing()){
            popConfig.hide();
        } else {
            popConfig.show(config, 0);
            popConfig.getRoot().setFocusTraversable(true);
        }
    }
    @FXML
    private void clearText(){
        search.clear();
    }
    @FXML
    private void dashboard(){
         title.setText("Dashboard");
         body.setContent(ViewManager.getInstance().get("Dashboard"));
     }
     @FXML
     private void feuilletemps(){
         title.setText("FeuilleTemps");
         loadView("TimeSheet");
         body.setContent(ViewManager.getInstance().get("TimeSheet"));
         System.out.println("App: load method --- PrinterModel");
         PrinterModel pt = new PrinterModel();
         pt.print();
     }
    @FXML
    private void AddSomething(){
        dialogNew.setVisible(true);
        JFXDialog alert1 = new JFXDialog(root, dialogNew, JFXDialog.DialogTransition.TOP);
        alert1.show();
     }
     @FXML
     private void EditSomething(){
         dialogEdit.setVisible(true);
         JFXDialog alert1 = new JFXDialog(root, dialogEdit, JFXDialog.DialogTransition.TOP);
         alert1.show();
     }
     @FXML
     private void EditPara(){
         isControlSet = true;
         dialogPara.setVisible(true);
         JFXDialog alert1 = new JFXDialog(root, dialogPara, JFXDialog.DialogTransition.TOP);
         endPara.setOnAction((ActionEvent event) -> {
             alert1.close();
             isControlSet = false;
         });
         alert1.show();
     }

     /**
      * Handles the change of top control.
      *
      * @param event the event
      */
     @FXML
     private void handleChangeTopControl(ActionEvent event) {
         changeRequest = true;
         Alerts.info("CHANGEMENT TOP KEY", "Veuillez appuyer sur la nouvelle touche puis sour ok");
         Scene scene = root.getScene();
         EventHandler<javafx.scene.input.KeyEvent> e = new EventHandler<javafx.scene.input.KeyEvent>() {
             @Override
             public void handle(javafx.scene.input.KeyEvent event) {
                 if (changeRequest) {
                     KeyCode keyCode = event.getCode();
                     File file = new File("top.properties");
                     Properties properties = new Properties();
                     try {
                         if (!file.exists()) {

                             file.createNewFile();
                         } else {

                             FileInputStream fileInputStream = new FileInputStream(file);
                             properties.load(fileInputStream);
                             properties.put("key", keyCode.toString());
                             FileOutputStream fileOutputStream = new FileOutputStream(file);
                             properties.store(fileOutputStream, "Top properties");
                             topKey.setText(properties.getProperty("key"));
                             changeRequest = false;
                         }

                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
             }
         };
         scene.addEventHandler(KeyEvent.KEY_PRESSED, e);
     }
 }
