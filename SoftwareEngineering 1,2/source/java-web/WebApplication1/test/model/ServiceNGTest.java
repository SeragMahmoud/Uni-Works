/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.ArrayList;
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
public class ServiceNGTest {
    
    public ServiceNGTest() {
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
     * Test of getServiceID method, of class Service.
     */
    @Test
    public void testGetServiceID() {
        System.out.println("getServiceID");
        Service instance = null;
        int expResult = 0;
        int result = instance.getServiceID();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setServiceID method, of class Service.
     */
    @Test
    public void testSetServiceID() {
        System.out.println("setServiceID");
        int serviceID = 0;
        Service instance = null;
        instance.setServiceID(serviceID);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class Service.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Service instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setName method, of class Service.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "";
        Service instance = null;
        instance.setName(name);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServicedetails method, of class Service.
     */
    @Test
    public void testGetServicedetails() {
        System.out.println("getServicedetails");
        Service instance = null;
        String expResult = "";
        String result = instance.getServicedetails();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setServicedetails method, of class Service.
     */
    @Test
    public void testSetServicedetails() {
        System.out.println("setServicedetails");
        String servicedetails = "";
        Service instance = null;
        instance.setServicedetails(servicedetails);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDescription method, of class Service.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        Service instance = null;
        String expResult = "";
        String result = instance.getDescription();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDescription method, of class Service.
     */
    @Test
    public void testSetDescription() {
        System.out.println("setDescription");
        String description = "";
        Service instance = null;
        instance.setDescription(description);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPrice method, of class Service.
     */
    @Test
    public void testGetPrice() {
        System.out.println("getPrice");
        Service instance = null;
        double expResult = 0.0;
        double result = instance.getPrice();
        assertEquals(result, expResult, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPrice method, of class Service.
     */
    @Test
    public void testSetPrice() {
        System.out.println("setPrice");
        int price = 0;
        Service instance = null;
        instance.setPrice(price);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addService method, of class Service.
     */
    @Test
    public void testAddService() {
        System.out.println("addService");
        Service service = null;
        Service instance = null;
        instance.addService(service);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of editService method, of class Service.
     */
    @Test
    public void testEditService() {
        System.out.println("editService");
        int serviceID = 0;
        String newDescription = "";
        Service instance = null;
        boolean expResult = false;
        boolean result = instance.editService(serviceID, newDescription);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteService method, of class Service.
     */
    @Test
    public void testDeleteService() {
        System.out.println("deleteService");
        int serviceID = 0;
        Service instance = null;
        boolean expResult = false;
        boolean result = instance.deleteService(serviceID);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getService method, of class Service.
     */
    @Test
    public void testGetService() {
        System.out.println("getService");
        int serviceID = 0;
        Service expResult = null;
        Service result = Service.getService(serviceID);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listServices method, of class Service.
     */
    @Test
    public void testListServices() {
        System.out.println("listServices");
        Service instance = null;
        ArrayList expResult = null;
        ArrayList result = instance.listServices();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of serviceCompleted method, of class Service.
     */
    @Test
    public void testServiceCompleted() {
        System.out.println("serviceCompleted");
        int serviceID = 0;
        Service instance = null;
        boolean expResult = false;
        boolean result = instance.serviceCompleted(serviceID);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
