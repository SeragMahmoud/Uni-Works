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
public class CardPaymentNGTest {
    
    public CardPaymentNGTest() {
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
     * Test of validateCardDetails method, of class CardPayment.
     */
    @Test
    public void testValidateCardDetails() {
        System.out.println("validateCardDetails");
        CardPayment instance = null;
        boolean expResult = false;
        boolean result = instance.validateCardDetails();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validateAccount method, of class CardPayment.
     */
    @Test
    public void testValidateAccount() {
        System.out.println("validateAccount");
        CardPayment instance = null;
        boolean expResult = false;
        boolean result = instance.validateAccount();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of processPayment method, of class CardPayment.
     */
    @Test
    public void testProcessPayment() {
        System.out.println("processPayment");
        double amount = 0.0;
        String paymentType = "";
        CardPayment instance = null;
        boolean expResult = false;
        boolean result = instance.processPayment(amount, paymentType);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCardNumber method, of class CardPayment.
     */
    @Test
    public void testGetCardNumber() {
        System.out.println("getCardNumber");
        CardPayment instance = null;
        String expResult = "";
        String result = instance.getCardNumber();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCardNumber method, of class CardPayment.
     */
    @Test
    public void testSetCardNumber() {
        System.out.println("setCardNumber");
        String cardNumber = "";
        CardPayment instance = null;
        instance.setCardNumber(cardNumber);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCardExpiry method, of class CardPayment.
     */
    @Test
    public void testGetCardExpiry() {
        System.out.println("getCardExpiry");
        CardPayment instance = null;
        String expResult = "";
        String result = instance.getCardExpiry();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCardExpiry method, of class CardPayment.
     */
    @Test
    public void testSetCardExpiry() {
        System.out.println("setCardExpiry");
        String cardExpiry = "";
        CardPayment instance = null;
        instance.setCardExpiry(cardExpiry);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCardCVV method, of class CardPayment.
     */
    @Test
    public void testGetCardCVV() {
        System.out.println("getCardCVV");
        CardPayment instance = null;
        String expResult = "";
        String result = instance.getCardCVV();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCardCVV method, of class CardPayment.
     */
    @Test
    public void testSetCardCVV() {
        System.out.println("setCardCVV");
        String cardCVV = "";
        CardPayment instance = null;
        instance.setCardCVV(cardCVV);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
