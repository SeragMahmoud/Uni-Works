package controller;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import model.dbconnect;

@WebServlet("/PaymentController")
public class PaymentController extends HttpServlet {




    private dbconnect db = new dbconnect();

    // Handle POST requests (for payments)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve payment data from the request
            String paymentType = request.getParameter("paymentType");
            String cardNumber = request.getParameter("cardNumber");
            String cardExpiry = request.getParameter("expiry");
            String cardCVV = request.getParameter("cvv");
            int bookingID = Integer.parseInt(request.getParameter("bookingID")); // Assuming bookingID is sent from the client

            // Process payment using the model
            String result = db.makePayment(bookingID, paymentType, cardNumber, cardExpiry, cardCVV);

            // Set the response content type as JSON
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.println("{ \"message\": \"" + result + "\" }");  // Send the result back as JSON response

        } catch (Exception e) {
            // Handle any exceptions during payment processing
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // Internal server error
            response.getWriter().println("{ \"error\": \"" + e.getMessage() + "\" }");
        }
    }

    // Handle GET requests (if necessary)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login.jsp");  // Redirect to the login page (you can change this as needed)
    }



    // Method to process payment and interact with the database
    public String makePayment(String paymentType, String cardNumber, String cardExpiry, String cardCVV) throws SQLException {
        // Database connection parameters
        String url = "jdbc:derby://localhost:1527/ProjectSoftware";  // Update with your actual DB details
        String dbuname = "admin1";
        String dbpass = "123";
        Connection con = DriverManager.getConnection(url, dbuname, dbpass);

        // Simulate a random payment ID and payment amount
        int paymentID = (int) (Math.random() * 1000);
        double paymentAmount = 100.0;  // Hardcoded payment amount for simplicity
        String status = "Payment successful";
        java.util.Date paymentDate = new java.util.Date();

        try {
            // Insert the payment record into the PAYMENT table
            String paymentQuery = "INSERT INTO PAYMENT (PAYMENTID, AMOUNT, PAYMENTDATE, STATUS, PAYMENTTYPE) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement paymentStmt = con.prepareStatement(paymentQuery);
            paymentStmt.setInt(1, paymentID);  // Set PAYMENTID
            paymentStmt.setDouble(2, paymentAmount);  // Set AMOUNT
            paymentStmt.setDate(3, new java.sql.Date(paymentDate.getTime()));  // Set PAYMENTDATE
            paymentStmt.setString(4, status);  // Set STATUS
            paymentStmt.setString(5, paymentType);  // Set PAYMENTTYPE (Card or Cash)

            int rowsAffected = paymentStmt.executeUpdate();
            if (rowsAffected > 0) {
                // Process card payment if payment type is "Card"
                if ("Card".equalsIgnoreCase(paymentType)) {
                    // Insert card payment details into the CARDPAYMENT table
                    String cardPaymentQuery = "INSERT INTO CARDPAYMENT (CARDNUMBER, CARDEXPIRY, CARDCVV) VALUES (?, ?, ?)";
                    PreparedStatement cardPaymentStmt = con.prepareStatement(cardPaymentQuery);
                    cardPaymentStmt.setString(1, cardNumber);  // Set Card Number
                    cardPaymentStmt.setString(2, cardExpiry);  // Set Card Expiry
                    cardPaymentStmt.setString(3, cardCVV);  // Set Card CVV

                    int cardRowsAffected = cardPaymentStmt.executeUpdate();
                    if (cardRowsAffected > 0) {
                        return "Payment ID: " + paymentID + ", Amount: " + paymentAmount + " USD, Payment Date: " + paymentDate + ", Status: " + status + ", Payment Type: Card.";
                    } else {
                        return "Failed to save card payment details.";
                    }
                } else if ("Cash".equalsIgnoreCase(paymentType)) {
                    // Handle cash payment
                    return "Payment ID: " + paymentID + ", Amount: " + paymentAmount + " USD, Payment Date: " + paymentDate + ", Status: " + status + ", Payment Type: Cash.";
                } else {
                    return "Invalid payment type. Please specify 'Card' or 'Cash'.";
                }
            } else {
                return "Failed to process payment.";
            }

        } catch (SQLException e) {
            return "Failed to process payment: " + e.getMessage();
        } finally {
            // Close the database connection
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

    // Handle GET requests (if necessary)
 
