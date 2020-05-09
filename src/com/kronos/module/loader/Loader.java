 
package com.kronos.module.loader;

import com.jfoenix.controls.JFXProgressBar;
import com.kronos.App;
import com.kronos.parserXML.MainImpl.SaveManagerImpl;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

 /**
 * @author TeamKronos
 * 
 * Version 1.0
 */
public class Loader extends Preloader {

    private JFXProgressBar progressBar;
    private Parent view;
    private Stage stage;

    @Override
    public void init(){
        try {
            App.dataManager =  SaveManagerImpl.getInstance();
            System.out.println("Loader.java : init-methode - initialization de datamanager");
            view = FXMLLoader.load(getClass().getResource("/com/kronos/module/loader/loader.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primary){
        stage = primary;
        primary.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(view);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("/com/kronos/theme/css/fonts.css").toExternalForm());
        progressBar = (JFXProgressBar) scene.lookup("#progressBar");
        primary.getIcons().add(new Image("/com/kronos/module/media/icon.png"));
        primary.setScene(scene);
//        primary.setAlwaysOnTop(true);
        primary.show();

    }

    @Override
    public synchronized void handleApplicationNotification(Preloader.PreloaderNotification info) {
        // Handle application notification in this point (see MyApplication#Init).

        if (info instanceof Preloader.ProgressNotification) {
            double x = ((Preloader.ProgressNotification) info).getProgress();

            double percent = x / 100f;
            progressBar.progressProperty().set(percent > 1 ? 1 : percent);
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        // Handle state change notifications.

        StateChangeNotification.Type type = info.getType();
        switch (type) {
            case BEFORE_LOAD:
                break;
            case BEFORE_INIT:
                break;
            case BEFORE_START:
                stage.close();
        }
    }
}
