/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kronos.module.loader;

import javafx.application.Preloader;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mac
 */
public class LoaderTest {
    
    public LoaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of init method, of class Loader.
     */
    @Test
    public void testInit() {
        System.out.println("init");
        Loader instance = new Loader();
        instance.init();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of start method, of class Loader.
     */
    @Test
    public void testStart() {
        System.out.println("start");
        Stage primary = null;
        Loader instance = new Loader();
        instance.start(primary);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleApplicationNotification method, of class Loader.
     */
    @Test
    public void testHandleApplicationNotification() {
        System.out.println("handleApplicationNotification");
        Preloader.PreloaderNotification info = null;
        Loader instance = new Loader();
        instance.handleApplicationNotification(info);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of handleStateChangeNotification method, of class Loader.
     */
    @Test
    public void testHandleStateChangeNotification() {
        System.out.println("handleStateChangeNotification");
        Preloader.StateChangeNotification info = null;
        Loader instance = new Loader();
        instance.handleStateChangeNotification(info);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
