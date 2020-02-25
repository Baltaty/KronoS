package com.kronos.global.plugin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.HashMap;

/**
 * @author TeamKronos
 * @version 1.0
 *
 */
public class ViewManager {

    private static final HashMap<String, Node> SCREENS = new HashMap<>();
    private static ViewManager instance;
    private static String nameView;

    /**
     * Constructor.
     */
    private ViewManager() {
    }

    /**
     *
     * @return
     */
    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    /**
     *
     * @param name
     * @param node
     */
    public void put(String name, Node node) {
        nameView = name;
        SCREENS.put(name, node);
    }

    /**
     *
     * @param view
     * @return
     */
    public Node get(String view) {
        return SCREENS.get(view);
    }

    /**
     *
     * @return
     */
    public int getSize() {
        return SCREENS.size();
    }

    /**
     *
     * @return
     */
    Node getCurrentView() {
        return SCREENS.get(nameView);
    }

    /**
     *
     * @return
     */
    public ObservableList<Node> getAll() {
        return FXCollections.observableArrayList(SCREENS.values());
    }
}
