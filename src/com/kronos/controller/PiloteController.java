package com.kronos.controller;

import com.kronos.api.Pilote;
import com.kronos.global.util.Alerts;
import com.kronos.global.util.Mask;
import com.kronos.model.PiloteModel;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class PiloteController implements Initializable {


    String regExpressionchaine = "[a-zA-Z]*";
    String regExpressiondate="[0-9 * {1,2} [a-z]+ [0-9]{4}]";
    String regExpressionNumerique="[0-9]*";
    String regexEmail="^[A-Za-z0-9+_.-]+@(.+)$";

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
   Boolean checkname( ){

        return  true;
   }
    void createpilote( ) throws ParseException {

    long id= System.currentTimeMillis();
    String lastname="mon name";
    String firstname="firstname";
    String comment="comment  la vie ";
    Date date= new Date();
    double weight=222.00;
    double height=1111.00;

    String email="kouadioyaonarcisse2@gmail.com";

      boolean test= firstname.matches(regExpressionchaine);
      System.out.println(email.matches(regexEmail));
      System.out.println(test);
      System.out.println(String.valueOf(id).matches(regExpressionNumerique));
      System.out.println( new SimpleDateFormat("dd-MM-yyyy").format(date));
      System.out.println( new SimpleDateFormat("dd-MM-yyyy").format(date).matches(regExpressiondate));


    }

    public Boolean createPilote1(PiloteModel pilote){
        Boolean verify=true;
        if(!(String.valueOf(pilote.getId()).matches(regExpressionNumerique))){
            verify=false;
           // Alerts.error("error","mylong ");
        }else  if (!(pilote.getFirstName().matches(regExpressionchaine))){
            verify=false;
            //Alerts.error("ERROR"," firstname is not a string");
        }
        else if (!(pilote.getLastName().matches(regExpressionchaine))){

            verify=false;
            //Alerts.error("ERROR"," firstname is not a string");
        }

        return  verify;
    }



    public static void main(String[] args) throws ParseException {
        PiloteController piloteController= new PiloteController();
        piloteController.createpilote();
        long id= System.currentTimeMillis();
        String lastname="monname";
        String firstname="firstname";
        String comment="comment  la vie ";
        Date date= new Date();
        double weight=222.00;
        double height=1111.00;
        PiloteModel pilote= new PiloteModel(id,lastname,firstname,comment,date,weight,height);

        System.out.println("checking information about the pilot "+piloteController.createPilote1(pilote));
    }

}
