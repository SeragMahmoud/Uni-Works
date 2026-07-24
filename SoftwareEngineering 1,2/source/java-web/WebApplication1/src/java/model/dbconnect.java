/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class dbconnect {
    private static final String DB_URL = "jdbc:derby://localhost:1527/ProjectSoftware";
    private static final String DB_USER = "admin1";
    private static final String DB_PASSWORD = "123";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }


    public boolean signupCustomer(Customer c) throws SQLException {
        String sql = "INSERT INTO CUSTOMER (CUSTOMERNAME, PASSWORD, EMAIL) VALUES (?, ?, ?)";
        Connection con = getConnection();
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, c.getCuserName());
        pst.setString(2, c.getCpassword());
        pst.setString(3, c.getCemail());

        pst.executeUpdate();
        con.close();

        return true;
    }

    public boolean isValidCustomer(String username, String password) throws SQLException {
    String sql = "SELECT * FROM CUSTOMER WHERE CUSTOMERNAME = ? AND PASSWORD = ?";

    try (Connection con = getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {


        pst.setString(1, username);
        pst.setString(2, password);

        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        }
    } catch (SQLException e) {
        System.out.println("SQL Error: " + e.getMessage());
        throw e;
    }
}



 public String createBooking(String customerUsername, String serviceName, String bookingDate, String startTime, String endTime) {
    String url = "jdbc:derby://localhost:1527/ProjectSoftware";
    String dbuname = "admin1";
    String dbpass = "123";
    Connection con = null;

    try {
        con = DriverManager.getConnection(url, dbuname, dbpass);

        // Get Customer ID
        String customerQuery = "SELECT CUSTOMERID FROM CUSTOMER WHERE CUSTOMERUSERNAME = ?";
        PreparedStatement customerStmt = con.prepareStatement(customerQuery);
        customerStmt.setString(1, customerUsername);
        ResultSet customerRs = customerStmt.executeQuery();
        if (!customerRs.next()) {
            return "Customer not found.";
        }
        int customerID = customerRs.getInt("CUSTOMERID");

        // Get Service ID
        String serviceQuery = "SELECT SERVICEID FROM SERVICE WHERE SERVICENAME = ?";
        PreparedStatement serviceStmt = con.prepareStatement(serviceQuery);
        serviceStmt.setString(1, serviceName);
        ResultSet serviceRs = serviceStmt.executeQuery();
        if (!serviceRs.next()) {
            return "Service not found.";
        }
        int serviceID = serviceRs.getInt("SERVICEID");

        // Insert booking
        String bookingQuery = "INSERT INTO BOOKING (BOOKINGDATE, STARTTIME, ENDTIME, CUSTOMERID, SERVICEID, STATUS) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement bookingStmt = con.prepareStatement(bookingQuery);
        bookingStmt.setString(1, bookingDate);
        bookingStmt.setString(2, startTime);
        bookingStmt.setString(3, endTime);
        bookingStmt.setInt(4, customerID);
        bookingStmt.setInt(5, serviceID);
        bookingStmt.setString(6, "Successfully created");

        int rowsAffected = bookingStmt.executeUpdate();
        if (rowsAffected > 0) {
            return "Booking created successfully.";
        } else {
            return "Failed to create booking.";
        }

    } catch (SQLException e) {
        return "Database error: " + e.getMessage();
    } finally {
        try {
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// Method to check schedule availability
public String checkSchedule(java.util.Date startTime, java.util.Date endTime) throws SQLException {
    String url = "jdbc:derby://localhost:1527/ProjectSoftware";
    String dbuname = "admin1";
    String dbpass = "123";
    Connection con = DriverManager.getConnection(url, dbuname, dbpass);

    try {
        String scheduleCheckQuery = "SELECT * FROM BOOKING WHERE (STARTTIME < ? AND ENDTIME > ?) OR (STARTTIME < ? AND ENDTIME > ?)";
        PreparedStatement stmt = con.prepareStatement(scheduleCheckQuery);
        stmt.setTime(1, new java.sql.Time(startTime.getTime()));
        stmt.setTime(2, new java.sql.Time(endTime.getTime()));
        stmt.setTime(3, new java.sql.Time(startTime.getTime()));
        stmt.setTime(4, new java.sql.Time(endTime.getTime()));

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return "The selected time slot is already booked.";
        } else {
            return "Schedule available";
        }
    } finally {
        con.close();
    }
}
public List<Schedule> getAvailableSchedules() throws SQLException {
    String url = "jdbc:derby://localhost:1527/ProjectSoftware";
    String dbuname = "admin1";
    String dbpass = "123";
    Connection con = DriverManager.getConnection(url, dbuname, dbpass);

    String getsc = "SELECT * FROM SCHEDULE WHERE STARTTIME IS NOT NULL AND ENDTIME IS NOT NULL";
    PreparedStatement scheduleStmt = con.prepareStatement(getsc);
    ResultSet rs = scheduleStmt.executeQuery();

    List<Schedule> sc = new ArrayList<>();
    while (rs.next()) {
        String startTime = rs.getString("STARTTIME");
        String endTime = rs.getString("ENDTIME");

        // Create Schedule object and add to the list
        Schedule schedule = new Schedule(startTime, endTime);
        sc.add(schedule);
    }

    rs.close();
    scheduleStmt.close();
    con.close();

    return sc;
}

public boolean makePayment2 (int bookingID, String cardNumber, String cardExpiry, String cardCVV) throws SQLException {
    String url = "jdbc:derby://localhost:1527/ProjectSoftware";
        String dbuname = "admin1";
        String dbpass = "123";
        Connection con = DriverManager.getConnection(url, dbuname, dbpass);

        String cardPaymentQuery = "INSERT INTO PAYMENTTEST (BOOKINGID,CVV, EXPIRY, CARDNUMBER) VALUES (?, ?, ?,?)";
                PreparedStatement cardPaymentStmt = con.prepareStatement(cardPaymentQuery);
                cardPaymentStmt.setInt(1, bookingID); // Set Card Number
                cardPaymentStmt.setString(2, cardCVV); // Set Card Expiry
                cardPaymentStmt.setString(3, cardExpiry); // Set Card CVV
                cardPaymentStmt.setString(4, cardNumber);


// Set Card CVV
                cardPaymentStmt.executeUpdate();
                con.close();


        return true;
}

public double CASHPAYMENT(double amount) throws SQLException {
    // Database connection details
    String url = "jdbc:derby://localhost:1527/ProjectSoftware";
    String dbuname = "admin1";
    String dbpass = "123";

    // Initialize connection and statement
    Connection con = null;
    PreparedStatement cashPaymentStmt = null;

    try {
        // Establish connection to the database
        con = DriverManager.getConnection(url, dbuname, dbpass);

        // SQL query to insert the cash payment amount
        String cashPaymentQuery = "INSERT INTO CASHPAYMENT (Amount) VALUES (?)";

        // Prepare the statement
        cashPaymentStmt = con.prepareStatement(cashPaymentQuery);

        // Set the amount parameter
        cashPaymentStmt.setDouble(1, amount);

        // Execute the statement
        cashPaymentStmt.executeUpdate();

    } catch (SQLException e) {
        // Log the exception and rethrow it for higher-level handling
        Logger.getLogger(dbconnect.class.getName()).log(Level.SEVERE, "Error processing cash payment", e);
        throw e;  // Re-throw the exception
    } finally {
        // Always close the resources in the finally block to prevent leaks
        try {
            if (cashPaymentStmt != null) {
                cashPaymentStmt.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            // Log the exception if resources can't be closed
            Logger.getLogger(dbconnect.class.getName()).log(Level.SEVERE, "Error closing resources", ex);
        }
    }

    // Return the amount processed
    return amount;
}




  public String makePayment(int bookingID, String paymentType, String cardNumber, String cardExpiry, String cardCVV) throws SQLException {
    String url = "jdbc:derby://localhost:1527/ProjectSoftware";
    String dbuname = "admin1";
    String dbpass = "123";
    Connection con = DriverManager.getConnection(url, dbuname, dbpass);

    int paymentID = (int) (Math.random() * 1000); // Generate a random payment ID
    double paymentAmount = 100.0; // Fixed payment amount of $100
    String status = "Payment successful"; // Payment status
    java.util.Date paymentDate = new java.util.Date(); // Get the current payment date

    try {
        // Check if the booking exists before making payment
        String bookingQuery = "SELECT BOOKINGID FROM BOOKING WHERE BOOKINGID = ?";
        PreparedStatement bookingStmt = con.prepareStatement(bookingQuery);
        bookingStmt.setInt(1, bookingID);
        ResultSet bookingRs = bookingStmt.executeQuery();

        if (!bookingRs.next()) {
            return "Booking not found.";
        }

        // Insert Payment record into the PAYMENT table
        String paymentQuery = "INSERT INTO PAYMENT (PAYMENTID, BOOKINGID, AMOUNT, PAYMENTDATE, STATUS, PAYMENTTYPE) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement paymentStmt = con.prepareStatement(paymentQuery);
        paymentStmt.setInt(1, paymentID); // Set PAYMENTID
        paymentStmt.setInt(2, bookingID); // Set BOOKINGID
        paymentStmt.setDouble(3, paymentAmount); // Set AMOUNT
        paymentStmt.setDate(4, new java.sql.Date(paymentDate.getTime())); // Set PAYMENTDATE
        paymentStmt.setString(5, status); // Set STATUS
        paymentStmt.setString(6, paymentType); // Set PAYMENTTYPE (e.g., "Card" or "Cash")

        int rowsAffected = paymentStmt.executeUpdate();
        if (rowsAffected > 0) {
            if ("Card".equalsIgnoreCase(paymentType)) {
                // Insert Card Payment details into the CARDPAYMENT table
                String cardPaymentQuery = "INSERT INTO PAYMENTTEST (BOOKINGID,CVV, EXPIRY, CARDNUMBER) VALUES (?, ?, ?,?)";
                PreparedStatement cardPaymentStmt = con.prepareStatement(cardPaymentQuery);
                cardPaymentStmt.setInt(1, bookingID); // Set Card Number
                cardPaymentStmt.setString(2, cardCVV); // Set Card Expiry
                cardPaymentStmt.setString(3, cardExpiry); // Set Card CVV
                cardPaymentStmt.setString(3, cardNumber); // Set Card CVV

               cardPaymentStmt.executeUpdate();


                int cardRowsAffected = cardPaymentStmt.executeUpdate();
                if (cardRowsAffected > 0) {
                    cardPaymentStmt.close();
                    return "Payment ID: " + paymentID + ", Booking ID: " + bookingID + ", Amount: " + paymentAmount + " USD, Payment Date: " + paymentDate + ", Status: " + status + ", Payment Type: Card.";
                } else {
                    return "Failed to save card payment details.";
                }
            } else if ("Cash".equalsIgnoreCase(paymentType)) {
                return "Payment ID: " + paymentID + ", Booking ID: " + bookingID + ", Amount: " + paymentAmount + " USD, Payment Date: " + paymentDate + ", Status: " + status + ", Payment Type: Cash.";
            } else {
                return "Invalid payment type. Please specify 'Card' or 'Cash'.";
            }
        } else {
            return "Failed to process payment.";
        }

    } catch (SQLException e) {
        return "Failed to process payment: " + e.getMessage();
    } finally {
        con.close();
    }
}




   public static void main(String[] args) throws SQLException {
   // Customer c = new Customer("gamal", "123");
//    Administrator a = new Administrator("seraj", "1234");
//    Customer newCustomer = new Customer("john_doe", "password123", "john.doe@example.com");
//    dbconnect d = new dbconnect();
//
//    // Signup a new customer
//    boolean isSignedUp = d.signupCustomer(newCustomer);
//    if (isSignedUp) {
//        System.out.println("Customer signed up successfully!");
//    } else {
//        System.out.println("Signup failed. Please try again.");
//    }
//
//    // Validate existing customer and admin
////    System.out.println(d.validcustomer(c));
////    System.out.println(d.validAdmin(a));
//
//    // Create a booking
//
////    String bookingResult2 = d.createBooking("gamal", "booking", new java.util.Date());
////    System.out.println(bookingResult2);
//
//
//    boolean PaymentResult = d.makePayment2(7416,"1234567890","06/27","567");
//    System.out.println(PaymentResult);
//
////    int cash =d.CASHPAYMENT(100);
////    System.out.println(cash);
//
//
////      try {
////            // Define the date format to match the schedule
////            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
////
////            // Test 1: Date within the schedule range
////            Date testDate1 = dateFormat.parse("2024-12-10");
////            String result1 = checkSchedule(testDate1);
////            System.out.println("Test Date 1 (2024-12-10): " + result1);
////
////            // Test 2: Date outside the schedule range
////            Date testDate2 = dateFormat.parse("2024-12-16");
////            String result2 = checkSchedule(testDate2);
////            System.out.println("Test Date 2 (2024-12-16): " + result2);
////
////            // Test 3: Date exactly matching the start of a schedule
////            Date testDate3 = dateFormat.parse("2024-12-09");
////            String result3 = checkSchedule(testDate3);
////            System.out.println("Test Date 3 (2024-12-09): " + result3);
////
////            // Test 4: Date exactly matching the end of a schedule
////            Date testDate4 = dateFormat.parse("2024-12-15");
////            String result4 = checkSchedule(testDate4);
////            System.out.println("Test Date 4 (2024-12-15): " + result4);
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//         int scheduleID = 1033; // Example schedule ID
//        String startDate = "2025-12-13"; // Example start date in "yyyy-MM-dd" format
//        String endDate = "2025-12-14";   // Example end date in "yyyy-MM-dd" format

        // Call the method to insert the schedule into the database
       // boolean result = createScheduleInDB(scheduleID, startDate, endDate);

        // Check the result
//        if (result) {
//            System.out.println("Test passed: Schedule created successfully.");
//        } else {
//            System.out.println("Test failed: Schedule creation failed.");
//        }

    }

}
