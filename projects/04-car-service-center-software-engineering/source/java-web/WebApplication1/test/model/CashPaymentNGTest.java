/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

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
public class CashPaymentNGTest {
    
    public CashPaymentNGTest() {
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
     * Test of processPayment method, of class CashPayment.
     */
    @Test
    public void testProcessPayment() {
        System.out.println("processPayment");
        double amount = 0.0;
        String paymentType = "";
        CashPayment instance = null;
        boolean expResult = false;
        boolean result = instance.processPayment(amount, paymentType);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of confirmReceipt method, of class CashPayment.
     */
    @Test
    public void testConfirmReceipt() {
        System.out.println("confirmReceipt");
        CashPayment instance = null;
        boolean expResult = false;
        boolean result = instance.confirmReceipt();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAmount method, of class CashPayment.
     */
    @Test
    public void testGetAmount() {
        System.out.println("getAmount");
        CashPayment instance = null;
        double expResult = 0.0;
        double result = instance.getAmount();
        assertEquals(result, expResult, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAmount method, of class CashPayment.
     */
    @Test
    public void testSetAmount() {
        System.out.println("setAmount");
        double amount = 0.0;
        CashPayment instance = null;
        instance.setAmount(amount);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPaymentDate method, of class CashPayment.
     */
    @Test
    public void testGetPaymentDate() {
        System.out.println("getPaymentDate");
        CashPayment instance = null;
        String expResult = "";
        String result = instance.getPaymentDate();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPaymentDate method, of class CashPayment.
     */
    @Test
    public void testSetPaymentDate() {
        System.out.println("setPaymentDate");
        String paymentDate = "";
        CashPayment instance = null;
        instance.setPaymentDate(paymentDate);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStatus method, of class CashPayment.
     */
    @Test
    public void testGetStatus() {
        System.out.println("getStatus");
        CashPayment instance = null;
        String expResult = "";
        String result = instance.getStatus();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStatus method, of class CashPayment.
     */
    @Test
    public void testSetStatus() {
        System.out.println("setStatus");
        String status = "";
        CashPayment instance = null;
        instance.setStatus(status);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBooking method, of class CashPayment.
     */
    @Test
    public void testGetBooking() {
        System.out.println("getBooking");
        CashPayment instance = null;
        Booking expResult = null;
        Booking result = instance.getBooking();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setBooking method, of class CashPayment.
     */
    @Test
    public void testSetBooking() {
        System.out.println("setBooking");
        Booking booking = null;
        CashPayment instance = null;
        instance.setBooking(booking);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
