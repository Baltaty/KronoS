 
package com.kronos.global.factory;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

 /**
 * @author TeamKronos
 * 
 * Version 1.0
 */
public class AlertFactory<T> implements Callback<ListView<T>, ListCell<AlertCell>> {

    @Override
    public ListCell<AlertCell> call(ListView<T> param) {
        return new ListCell<AlertCell>(){
          @Override
          protected void updateItem(AlertCell item, boolean empty) {
              super.updateItem(item, empty);
              if(item == null || empty) {
                  setItem(null);
                  setGraphic(null);
                  setText(null);
              } else {
                  setItem(item);
//                  setText(item.get);
                  System.out.println(item.getIcon());
                  setGraphic(item.getIcon());
              }
          }
        };
    }
}
