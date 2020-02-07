package com.kronos.controller;

import com.kronos.model.LapRaceModel;
import com.kronos.model.MainCarModel;
import com.kronos.model.TimeRaceModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestCarController {

    CarController carController1 = new CarController(null);
    CarController carController2 = new CarController(new MainCarModel(45257245, 1, "Toyota", "TS050 Hybrid", "Toyota", null, (LapRaceModel) null, 0));


    /**
     * Last top is "RACE", previous top was "RACE"
     * Expected : true, logic respected
     */
    @Test
    void testCheckTopLogic1() {
        assertTrue(carController1.checkTopLogic("RACE", "RACE"), "Should return true");
    }

    /**
     * Last top is "RACE", previous top was "OUT"
     * Expected : true, logic respected
     */
    @Test
    void testCheckTopLogic2() {
        assertTrue(carController1.checkTopLogic("RACE", "OUT"), "Should return true");
    }

    /**
     * Last top is "RACE", previous top was "IN"
     * Expected : false, logic not respected
     */
    @Test
    void testCheckTopLogic3() {
        assertFalse(carController1.checkTopLogic("RACE", "IN"), "Should return false");
    }

    /**
     * Last top is "OUT", previous top was "IN"
     * Expected : true, logic respected
     */
    @Test
    void testCheckTopLogic4() {
        assertTrue(carController1.checkTopLogic("OUT", "IN"), "Should return true");
    }

    /**
     * Last top is "OUT", previous top was "RACE"
     * Expected : false, logic not respected
     */
    @Test
    void testCheckTopLogic5() {
        assertFalse(carController1.checkTopLogic("OUT", "RACE"), "Should return false");
    }

    /**
     * Last top is "OUT", previous top was "OUT"
     * Expected : false, logic not respected
     */
    @Test
    void testCheckTopLogic6() {
        assertFalse(carController1.checkTopLogic("OUT", "OUT"), "Should return false");
    }

    /**
     * Last top is "IN", previous top was "RACE"
     * Expected : true, logic respected
     */
    @Test
    void testCheckTopLogic7() {
        assertTrue(carController1.checkTopLogic("IN", "RACE"), "Should return true");
    }

    /**
     * Last top is "IN", previous top was "OUT"
     * Expected : true, logic respected
     */
    @Test
    void testCheckTopLogic8() {
        assertTrue(carController1.checkTopLogic("IN", "OUT"), "Should return true");
    }

    /**
     * Last top is "IN", previous top was "IN"
     * Expected : false, logic not respected
     */
    @Test
    void testCheckTopLogic9() {
        assertFalse(carController1.checkTopLogic("IN", "IN"), "Should return false");
    }

    /**
     * Car arguments have the correct format.
     * Expected : checkCar should be true
     */
    @Test
    void testCheckCar1() {
        assertTrue(carController2.checkCar(carController2.getCarModel()), "Should return true");
    }
}