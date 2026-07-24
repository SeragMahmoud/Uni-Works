package controller;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import model.dbconnect;

public class PaymentController extends HttpServlet {

    // Method to process payment and interact with the database
    public String makePayment(int bookingID, String paymentType, String cardNumber, String cardExpiry, String cardCVV) throws SQLException {
        // Database connection details
        String url = "jdbc:derby://localhost:1527/ProjectSoftware";
        String dbuname = "admin1";
        String dbpass = "123";
        dbconnect d =new dbconnect();
        d.makePayment(bookingID, paymentType, cardNumber, cardExpiry, cardCVV);
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
                    String cardPaymentQuery = "INSERT INTO PAYMENTTEST (BOOKINGID,CARDNUMBER, CARDEXPIRY, CARDCVV) VALUES (?, ?, ?)";
                    PreparedStatement cardPaymentStmt = con.prepareStatement(cardPaymentQuery);
                    cardPaymentStmt.setInt(1, bookingID); // Set Card Number
                    cardPaymentStmt.setString(2, cardCVV); // Set Card Number
                    cardPaymentStmt.setString(3, cardExpiry); // Set Card Expiry
                    cardPaymentStmt.setString(4, cardNumber); // Set Card CVV

                    int cardRowsAffected = cardPaymentStmt.executeUpdate();
                    if (cardRowsAffected > 0) {
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

    // Handle POST requests
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve payment data from the form
            int bookingID = Integer.parseInt(request.getParameter("bookingID"));
            String paymentType = request.getParameter("paymentType");
            String cardNumber = request.getParameter("cardNumber");
            String cardExpiry = request.getParameter("cardExpiry");
            String cardCVV = request.getParameter("cardCVV");

            // Process payment
            dbconnect dbConn = new dbconnect();
            String result = dbConn.makePayment(bookingID, paymentType, cardNumber, cardExpiry, cardCVV);

            // Set the response message
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body><h2>" + result + "</h2></body></html>");

        } catch (Exception e) {
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    // Handle GET requests
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login.jsp"); // Redirect to login page for now
    }
}
