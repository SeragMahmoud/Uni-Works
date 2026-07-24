/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.ArrayList;
import java.util.Date;
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
public class BookingNGTest {
    
    public BookingNGTest() {
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
     * Test of getBookingID method, of class Booking.
     */
    @Test
    public void testGetBookingID() {
        System.out.println("getBookingID");
        Booking instance = null;
        int expResult = 0;
        int result = instance.getBookingID();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setBookingID method, of class Booking.
     */
    @Test
    public void testSetBookingID() {
        System.out.println("setBookingID");
        int bookingID = 0;
        Booking instance = null;
        instance.setBookingID(bookingID);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserID method, of class Booking.
     */
    @Test
    public void testGetUserID() {
        System.out.println("getUserID");
        Booking instance = null;
        int expResult = 0;
        int result = instance.getUserID();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUserID method, of class Booking.
     */
    @Test
    public void testSetUserID() {
        System.out.println("setUserID");
        int userID = 0;
        Booking instance = null;
        instance.setUserID(userID);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServiceID method, of class Booking.
     */
    @Test
    public void testGetServiceID() {
        System.out.println("getServiceID");
        Booking instance = null;
        int expResult = 0;
        int result = instance.getServiceID();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setServiceID method, of class Booking.
     */
    @Test
    public void testSetServiceID() {
        System.out.println("setServiceID");
        int serviceID = 0;
        Booking instance = null;
        instance.setServiceID(serviceID);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDate method, of class Booking.
     */
    @Test
    public void testGetDate() {
        System.out.println("getDate");
        Booking instance = null;
        Date expResult = null;
        Date result = instance.getDate();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDate method, of class Booking.
     */
    @Test
    public void testSetDate() {
        System.out.println("setDate");
        Date date = null;
        Booking instance = null;
        instance.setDate(date);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStatus method, of class Booking.
     */
    @Test
    public void testGetStatus() {
        System.out.println("getStatus");
        Booking instance = null;
        String expResult = "";
        String result = instance.getStatus();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStatus method, of class Booking.
     */
    @Test
    public void testSetStatus() {
        System.out.println("setStatus");
        String status = "";
        Booking instance = null;
        instance.setStatus(status);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAssignedTechnicianID method, of class Booking.
     */
    @Test
    public void testGetAssignedTechnicianID() {
        System.out.println("getAssignedTechnicianID");
        Booking instance = null;
        int expResult = 0;
        int result = instance.getAssignedTechnicianID();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of assignTechnician method, of class Booking.
     */
    @Test
    public void testAssignTechnician() {
        System.out.println("assignTechnician");
        int technicianID = 0;
        Booking instance = null;
        instance.assignTechnician(technicianID);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServices method, of class Booking.
     */
    @Test
    public void testGetServices() {
        System.out.println("getServices");
        Booking instance = null;
        ArrayList expResult = null;
        ArrayList result = instance.getServices();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addService method, of class Booking.
     */
    @Test
    public void testAddService() {
        System.out.println("addService");
        Service service = null;
        Booking instance = null;
        instance.addService(service);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBookingDetails method, of class Booking.
     */
    @Test
    public void testGetBookingDetails() {
        System.out.println("getBookingDetails");
        int bookingID = 0;
        Booking expResult = null;
        Booking result = Booking.getBookingDetails(bookingID);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createBooking method, of class Booking.
     */
    @Test
    public void testCreateBooking() {
        System.out.println("createBooking");
        int customerID = 0;
        int serviceID = 0;
        Date date = null;
        String status = "";
        Booking instance = null;
        int expResult = 0;
        int result = instance.createBooking(customerID, serviceID, date, status);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPayment method, of class Booking.
     */
    @Test
    public void testGetPayment() {
        System.out.println("getPayment");
        Booking instance = null;
        Payment expResult = null;
        Payment result = instance.getPayment();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of cancelBooking method, of class Booking.
     */
    @Test
    public void testCancelBooking() {
        System.out.println("cancelBooking");
        int bookingID = 0;
        Booking instance = null;
        boolean expResult = false;
        boolean result = instance.cancelBooking(bookingID);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateBooking method, of class Booking.
     */
    @Test
    public void testUpdateBooking() {
        System.out.println("updateBooking");
        int bookingID = 0;
        Date newDate = null;
        Booking instance = null;
        boolean expResult = false;
        boolean result = instance.updateBooking(bookingID, newDate);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of confirmBooking method, of class Booking.
     */
    @Test
    public void testConfirmBooking() {
        System.out.println("confirmBooking");
        int bookingID = 0;
        Booking instance = null;
        boolean expResult = false;
        boolean result = instance.confirmBooking(bookingID);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBookingHistory method, of class Booking.
     */
    @Test
    public void testGetBookingHistory() {
        System.out.println("getBookingHistory");
        Booking instance = null;
        ArrayList expResult = null;
        ArrayList result = instance.getBookingHistory();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateServiceDetails method, of class Booking.
     */
    @Test
    public void testUpdateServiceDetails() {
        System.out.println("updateServiceDetails");
        int serviceID = 0;
        String newDetails = "";
        Booking instance = null;
        instance.updateServiceDetails(serviceID, newDetails);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Booking.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Booking instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
