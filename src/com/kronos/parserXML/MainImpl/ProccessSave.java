package com.kronos.parserXML.MainImpl;

import com.kronos.App;

public class ProccessSave extends  Thread {

     private SaveManagerImpl saveManager = App.getDataManager();
     private Object object ;
     private String tag;
     private String  id_object;

    public ProccessSave(final Object object, final String tag, final String id_object) {
        SaveManagerImpl saveManager = App.getDataManager();
        this.object = object;
        this.tag = tag;
        this.id_object =  id_object;
    }


    public void run(){
        this.save();
    }

    public void save() {
        this.saveManager.updateTagInXML(this.object,this.tag, this.id_object , true);
    }
}
