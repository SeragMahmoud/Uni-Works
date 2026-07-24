package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


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

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public boolean validcustomer(Customer c) throws SQLException {
        Connection con = getConnection();
        String sqlQuery = "SELECT * FROM CUSTOMER WHERE CUSTOMERUSERNAME='" + c.getCuserName() + "' AND CUSTOMERPASSWORD='" + c.getCpassword() + "'";
        PreparedStatement pst = con.prepareStatement(sqlQuery);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return true;
        }
        return false;
    }
    
    public boolean validAdmin(Administrator a) throws SQLException {
        Connection con = getConnection();
        String sqlQuery = "SELECT * FROM Administrator WHERE ADMINISTRATORUSERNAME='" + a.getAuserName() + "' AND ADMINISTRATORPASSWORD='" + a.getApassword() + "'";
        PreparedStatement pst = con.prepareStatement(sqlQuery);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return true;
        }
        return false;
    }
    
    public boolean signupCustomer(Customer c) throws SQLException {
    Connection con = getConnection();

    String sqlQuery = "INSERT INTO CUSTOMER (CUSTOMERUSERNAME, CUSTOMERPASSWORD, EMAIL) VALUES (?, ?, ?)";
    PreparedStatement pst = con.prepareStatement(sqlQuery);

    try {
        pst.setString(1, c.getCuserName()); // Set CUSTOMERUSERNAME
        pst.setString(2, c.getCpassword()); // Set CUSTOMERPASSWORD
        pst.setString(3, c.getCemail());    // Set CUSTOMEREMAIL (assuming the Customer class has an email field)

        int rowsAffected = pst.executeUpdate();

        return rowsAffected > 0; // Return true if insertion was successful
    } catch (SQLException e) {
        System.out.println("Error during signup: " + e.getMessage());
        return false;
    } finally {
        pst.close();
        con.close();
    }
}
 
//   public String createBooking(String customerUsername, String serviceName, java.util.Date date) throws SQLException {
//    Connection con = getConnection();
//
//    int customerID = -1;
//    int serviceID = -1;
//    int bookingID = (int) (Math.random() * 1000); // Generate a random booking ID
//    String status;
//
//    try {
//        // Retrieve Customer ID
//        String customerQuery = "SELECT CUSTOMERID FROM CUSTOMER WHERE CUSTOMERUSERNAME = ?";
//        PreparedStatement customerStmt = con.prepareStatement(customerQuery);
//        customerStmt.setString(1, customerUsername); // Replace ? with customerUsername
//        ResultSet customerRs = customerStmt.executeQuery();
//        if (customerRs.next()) {
//            customerID = customerRs.getInt("CUSTOMERID");
//        } else {
//            return "Customer not found.";
//        }
//
//        // Retrieve Service ID
//        String serviceQuery = "SELECT SERVICEID FROM SERVICE WHERE SERVICENAME = ?";
//        PreparedStatement serviceStmt = con.prepareStatement(serviceQuery);
//        serviceStmt.setString(1, serviceName); // Replace ? with serviceName
//        ResultSet serviceRs = serviceStmt.executeQuery();
//        if (serviceRs.next()) {
//            serviceID = serviceRs.getInt("SERVICEID");
//        } else {
//            return "Service not found.";
//        }
//
//        // Insert Booking
//        String bookingQuery = "INSERT INTO BOOKING (BOOKINGID, CUSTOMERID, SERVICEID, BOOKINGDATE, STATUS) VALUES (?, ?, ?, ?, ?)";
//        PreparedStatement bookingStmt = con.prepareStatement(bookingQuery);
//        bookingStmt.setInt(1, bookingID); // Set BOOKINGID
//        bookingStmt.setInt(2, customerID); // Set CUSTOMERID
//        bookingStmt.setInt(3, serviceID); // Set SERVICEID
//        bookingStmt.setDate(4, new java.sql.Date(date.getTime())); // Set BOOKINGDATE
//        bookingStmt.setString(5, "Successfully created"); // Set STATUS
//
//        int rowsAffected = bookingStmt.executeUpdate();
//        if (rowsAffected > 0) {
//            return "Booking ID: " + bookingID + ", Customer ID: " + customerID + ", Service ID: " + serviceID + ", Date: " + date + ", Status: Successfully created.";
//        } else {
//            return "Failed to create booking.";
//        }
//
//    } catch (SQLException e) {
//        return "Failed to create booking: " + e.getMessage();
//    } finally {
//        con.close();
//    }
//}

    
  public String createBooking(String customerUsername, String serviceName, java.util.Date date) throws SQLException {
        Connection con = getConnection();

        int customerID = -1;
        int serviceID = -1;
        int bookingID = (int) (Math.random() * 1000); // Generate a random booking ID

        try {
            // Retrieve Customer ID
            String customerQuery = "SELECT CUSTOMERID FROM CUSTOMER WHERE CUSTOMERUSERNAME = ?";
            PreparedStatement customerStmt = con.prepareStatement(customerQuery);
            customerStmt.setString(1, customerUsername);
            ResultSet customerRs = customerStmt.executeQuery();
            if (customerRs.next()) {
                customerID = customerRs.getInt("CUSTOMERID");
            } else {
                return "Customer not found.";
            }

            // Retrieve Service ID
            String serviceQuery = "SELECT SERVICEID FROM SERVICE WHERE SERVICENAME = ?";
            PreparedStatement serviceStmt = con.prepareStatement(serviceQuery);
            serviceStmt.setString(1, serviceName);
            ResultSet serviceRs = serviceStmt.executeQuery();
            if (serviceRs.next()) {
                serviceID = serviceRs.getInt("SERVICEID");
            } else {
                return "Service not found.";
            }

            // Check if the date fits within the schedule
            String scheduleCheckResult = checkSchedule(date);
            if (!"Schedule available".equals(scheduleCheckResult)) {
                return scheduleCheckResult;
            }

            // Insert Booking
            String bookingQuery = "INSERT INTO BOOKING (BOOKINGID, CUSTOMERID, SERVICEID, BOOKINGDATE, STATUS) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement bookingStmt = con.prepareStatement(bookingQuery);
            bookingStmt.setInt(1, bookingID);
            bookingStmt.setInt(2, customerID);
            bookingStmt.setInt(3, serviceID);
            bookingStmt.setDate(4, new java.sql.Date(date.getTime()));
            bookingStmt.setString(5, "Successfully created");

            int rowsAffected = bookingStmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Booking ID: " + bookingID + ", Customer ID: " + customerID + ", Service ID: " + serviceID + ", Date: " + date + ", Status: Successfully created.";
            } else {
                return "Failed to create booking.";
            }

        } catch (SQLException e) {
            return "Failed to create booking: " + e.getMessage();
        } finally {
            con.close();
        }
    }

   public static String checkSchedule(java.util.Date bookingDate) throws SQLException {
    String scheduleQuery = "SELECT SCHEDULEID, STARTTIME, ENDTIME FROM SCHEDULE";
    Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    PreparedStatement scheduleStmt = con.prepareStatement(scheduleQuery);
    ResultSet scheduleRs = scheduleStmt.executeQuery();

    // Convert the booking date to only match date precision (strip time part)
    java.sql.Date bookingDateOnly = new java.sql.Date(bookingDate.getTime()); 

    while (scheduleRs.next()) {
        // Retrieve STARTTIME and ENDTIME as java.sql.Date for date-only comparison
        java.sql.Date startTime = scheduleRs.getDate("STARTTIME");
        java.sql.Date endTime = scheduleRs.getDate("ENDTIME");

        // Check if booking date is within the range (inclusive)
        if (!bookingDateOnly.before(startTime) && !bookingDateOnly.after(endTime)) {
            return "Schedule available";
        }
    }

    return "The booking date does not fit the schedule.";
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
                String cardPaymentQuery = "INSERT INTO CARDPAYMENT (CARDNUMBER, CARDEXPIRY, CARDCVV) VALUES (?, ?, ?)";
                PreparedStatement cardPaymentStmt = con.prepareStatement(cardPaymentQuery);
                cardPaymentStmt.setString(1, cardNumber); // Set Card Number
                cardPaymentStmt.setString(2, cardExpiry); // Set Card Expiry
                cardPaymentStmt.setString(3, cardCVV); // Set Card CVV

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
