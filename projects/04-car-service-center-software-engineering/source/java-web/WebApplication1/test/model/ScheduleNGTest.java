/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author gamal
 */
public class ScheduleNGTest {
    
    public ScheduleNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of getScheduleID method, of class Schedule.
     */
    @Test
    public void testGetScheduleID() {
        System.out.println("getScheduleID");
        Schedule instance = null;
        int expResult = 0;
        int result = instance.getScheduleID();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTechnicianID method, of class Schedule.
     */
    @Test
    public void testGetTechnicianID() {
        System.out.println("getTechnicianID");
        Schedule instance = null;
        int expResult = 0;
        int result = instance.getTechnicianID();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWorkDays method, of class Schedule.
     */
    @Test
    public void testGetWorkDays() {
        System.out.println("getWorkDays");
        Schedule instance = null;
        String[] expResult = null;
        String[] result = instance.getWorkDays();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStartTime method, of class Schedule.
     */
    @Test
    public void testGetStartTime() {
        System.out.println("getStartTime");
        Schedule instance = null;
        String expResult = "";
        String result = instance.getStartTime();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEndTime method, of class Schedule.
     */
    @Test
    public void testGetEndTime() {
        System.out.println("getEndTime");
        Schedule instance = null;
        String expResult = "";
        String result = instance.getEndTime();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addBooking method, of class Schedule.
     */
    @Test
    public void testAddBooking() {
        System.out.println("addBooking");
        Booking booking = null;
        Schedule instance = null;
        instance.addBooking(booking);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBookings method, of class Schedule.
     */
    @Test
    public void testGetBookings() {
        System.out.println("getBookings");
        Schedule instance = null;
        List expResult = null;
        List result = instance.getBookings();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setWorkDays method, of class Schedule.
     */
    @Test
    public void testSetWorkDays() {
        System.out.println("setWorkDays");
        String[] workDays = null;
        Schedule instance = null;
        instance.setWorkDays(workDays);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStartTime method, of class Schedule.
     */
    @Test
    public void testSetStartTime() {
        System.out.println("setStartTime");
        String startTime = "";
        Schedule instance = null;
        instance.setStartTime(startTime);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEndTime method, of class Schedule.
     */
    @Test
    public void testSetEndTime() {
        System.out.println("setEndTime");
        String endTime = "";
        Schedule instance = null;
        instance.setEndTime(endTime);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createSchedule method, of class Schedule.
     */
    @Test
    public void testCreateSchedule() {
        System.out.println("createSchedule");
        int scheduleID = 0;
        int technicianID = 0;
        String[] workDays = null;
        String startTime = "";
        String endTime = "";
        boolean expResult = false;
        boolean result = Schedule.createSchedule(scheduleID, technicianID, workDays, startTime, endTime);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateSchedule method, of class Schedule.
     */
    @Test
    public void testUpdateSchedule() {
        System.out.println("updateSchedule");
        int scheduleID = 0;
        String[] newWorkDays = null;
        String newStartTime = "";
        String newEndTime = "";
        boolean expResult = false;
        boolean result = Schedule.updateSchedule(scheduleID, newWorkDays, newStartTime, newEndTime);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteSchedule method, of class Schedule.
     */
    @Test
    public void testDeleteSchedule() {
        System.out.println("deleteSchedule");
        int scheduleID = 0;
        boolean expResult = false;
        boolean result = Schedule.deleteSchedule(scheduleID);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSchedule method, of class Schedule.
     */
    @Test
    public void testGetSchedule() {
        System.out.println("getSchedule");
        int technicianID = 0;
        Schedule expResult = null;
        Schedule result = Schedule.getSchedule(technicianID);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
