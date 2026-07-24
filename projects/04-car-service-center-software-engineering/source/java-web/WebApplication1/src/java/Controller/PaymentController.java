package controller;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import model.dbconnect;

public class PaymentController extends HttpServlet {

    // Method to process payment and interact with the database
    public String makePayment(int bookingID, String paymentType, String cardNumber, String cardExpiry, String cardCVV) throws SQLException {
        dbconnect db = new dbconnect();
        return db.makePayment(bookingID, paymentType, cardNumber, cardExpiry, cardCVV);
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
