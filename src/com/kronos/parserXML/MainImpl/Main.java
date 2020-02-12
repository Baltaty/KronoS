package com.kronos.parserXML.MainImpl;

import com.kronos.api.Pilote;
import com.kronos.api.Top;
import com.kronos.model.PilotModel;
import com.kronos.model.TopModel;

import java.util.Date;

public class Main {


    public static void main(String[] args) {

        Pilote pilote = new PilotModel("test", "test", "Yeaver", new Date(), 3, 3);
        Top top = new TopModel(new Date(), "ROR");
        SaveManagerImpl saveManager = SaveManagerImpl.getInstance();
        saveManager.persist(pilote);
        saveManager.persist(top);
        saveManager.saveFile();

    }
}
