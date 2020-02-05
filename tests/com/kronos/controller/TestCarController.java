package com.kronos.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestCarController {

    CarController carController = new CarController();

    /**
     * Last top is "RACE", previous top was "RACE"
     * Expected : true, logic respected
     */
    @Test
    void testCheckTopLogic1() {
        assertTrue(carController.checkTopLogic("RACE", "RACE"), "Should return true");
    }

    /**
     * Last top is "RACE", previous top was "OUT"
     * Expected : true, logic respected
     */
    @Test
    void testCheckTopLogic2() {
        assertTrue(carController.checkTopLogic("RACE", "OUT"), "Should return true");
    }

    /**
     * Last top is "RACE", previous top was "IN"
     * Expected : false, logic not respected
     */
    @Test
    void testCheckTopLogic3() {
        assertFalse(carController.checkTopLogic("RACE", "IN"), "Should return false");
    }

    /**
     * Last top is "OUT", previous top was "IN"
     * Expected : true, logic respected
     */
    @Test
    void testCheckTopLogic4() {
        assertTrue(carController.checkTopLogic("OUT", "IN"), "Should return true");
    }

    /**
     * Last top is "OUT", previous top was "RACE"
     * Expected : false, logic not respected
     */
    @Test
    void testCheckTopLogic5() {
        assertFalse(carController.checkTopLogic("OUT", "RACE"), "Should return false");
    }

    /**
     * Last top is "OUT", previous top was "OUT"
     * Expected : false, logic not respected
     */
    @Test
    void testCheckTopLogic6() {
        assertFalse(carController.checkTopLogic("OUT", "OUT"), "Should return false");
    }

    /**
     * Last top is "IN", previous top was "RACE"
     * Expected : true, logic respected
     */
    @Test
    void testCheckTopLogic7() {
        assertTrue(carController.checkTopLogic("IN", "RACE"), "Should return true");
    }

    /**
     * Last top is "IN", previous top was "OUT"
     * Expected : true, logic respected
     */
    @Test
    void testCheckTopLogic8() {
        assertTrue(carController.checkTopLogic("IN", "OUT"), "Should return true");
    }

    /**
     * Last top is "IN", previous top was "IN"
     * Expected : false, logic not respected
     */
    @Test
    void testCheckTopLogic9() {
        assertFalse(carController.checkTopLogic("IN", "IN"), "Should return false");
    }
}