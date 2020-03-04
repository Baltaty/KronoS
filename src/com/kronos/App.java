 

package com.kronos;

import  com.kronos.global.*;
import  com.kronos.global.plugin.SectionManager;
import  com.kronos.global.plugin.UserManager;
import  com.kronos.global.plugin.ViewManager;
import   com.gn.decorator.GNDecorator;
import   com.gn.decorator.options.ButtonType;
import  com.kronos.module.loader.Loader;
import  com.kronos.module.main.Main;
import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.scenicview.ScenicView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Init the app class.
 * @author TeamKronos
 * 
 * Version 1.0
 */
public class App extends Application {

    private float  increment = 0;
    private float  progress = 0;
    private Section section;
    private User    user;

    @Override
    public synchronized void init(){

        section = SectionManager.get();
        if(section.isLogged()){
            user = UserManager.get(section.getUserLogged());
            userDetail = new UserDetail(section.getUserLogged(), user.getFullName(), "subtitle");
        }
        else {
            userDetail = new UserDetail();
        }
        float total = 43; // the difference represents the views not loaded
        increment = 100f / total;

        load("main",     "main");
        load("profile", "profile");
        load("login", "login");
        load("login", "account");
        load2("Homescreen");
        load2("raceresume");

        // delay
        try {
            wait(300);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(){

    }

    public static final GNDecorator decorator = new GNDecorator();
    public static final Scene scene = decorator.getScene();

    public static ObservableList<String>    stylesheets;
    public static HostServices              hostServices;
    private static UserDetail userDetail = null;

    public static GNDecorator getDecorator(){
        return decorator;
    }

    private void configServices(){
        hostServices = getHostServices();
    }
    // PARAMETRGE DU DECORATEUR
    private void initialScene(){
        decorator.setTitle("Kronos v1.0");
        decorator.initTheme(GNDecorator.Theme.DEFAULT);
        decorator.setContent(ViewManager.getInstance().get("Homescreen"));
    }

    @Override
    public  void start(Stage primary) {

        configServices();
        initialScene();
        stylesheets = decorator.getScene().getStylesheets();

        stylesheets.addAll(
                getClass().getResource("/com/kronos/theme/css/fonts.css").toExternalForm()
        );
        decorator.setMaximized(false);
        decorator.setResizable(false);
        decorator.show();

    }

    public static void main(String[] args) {
        LauncherImpl.launchApplication(App.class, Loader.class, args);
    }
    private void load(String module, String name){
        try {
            ViewManager.getInstance().put(
                    name,
                    FXMLLoader.load(getClass().getResource("/com/kronos/module/" + module + "/" + name + ".fxml"))
            );
            preloaderNotify();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void load2(String name){
        try {
            ViewManager.getInstance().put(
                    name,
                    FXMLLoader.load(getClass().getResource("/com/kronos/view/"+ name + ".fxml"))
            );
            preloaderNotify();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private synchronized void preloaderNotify() {
        progress += increment;
        LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
    }
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private String logged(){
        try {
            File file = new File("dashboard.properties");
            Properties properties = new Properties();

            if(!file.exists()){
                file.createNewFile();
                return "account";
            } else {
                FileInputStream fileInputStream = new FileInputStream(file);
                properties.load(fileInputStream);
                properties.putIfAbsent("logged", "false");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                properties.store(fileOutputStream, "Dashboard properties");

                File directory = new File("user/");
                properties.load(fileInputStream);
                if(directory.exists()){
                    if(properties.getProperty("logged").equals("false"))
                        return "login";
                    else
                        return "main";
                } else
                    return "account";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static UserDetail getUserDetail() {
        return userDetail;
    }
}