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
    private static final String DB_URL = getRequiredEnv("CAR_SERVICE_DB_URL");
    private static final String DB_USER = getRequiredEnv("CAR_SERVICE_DB_USER");
    private static final String DB_PASSWORD = getRequiredEnv("CAR_SERVICE_DB_PASSWORD");

    private static String getRequiredEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Set " + name + " before running the application.");
        }
        return value;
    }

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
    Connection con = null;

    try {
        con = getConnection();

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
    Connection con = getConnection();

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
    Connection con = getConnection();

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
        Connection con = getConnection();
        
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
    // Initialize connection and statement
    Connection con = null;
    PreparedStatement cashPaymentStmt = null;

    try {
        // Establish connection to the database
        con = getConnection();
        
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
    Connection con = getConnection();

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
        System.out.println("dbconnect is configured through environment variables.");
    
    }

}
