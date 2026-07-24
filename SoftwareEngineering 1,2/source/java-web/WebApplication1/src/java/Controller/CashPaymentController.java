/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dbconnect;

/**
 *
 * @author gamal
 */
@WebServlet(name = "CashPayment", urlPatterns = {"/CashPayment"})
public class CashPaymentController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");

        // Retrieve the amount from the request
        String amountString = request.getParameter("amount");

        // Check if the amount is valid
        if (amountString != null && !amountString.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountString);

                // Call the method in dbconnect to handle the cash payment
                dbconnect d = new dbconnect();
                d.CASHPAYMENT(amount);

                // Redirect or forward to the success page
                request.setAttribute("successMessage", "Cash payment processed successfully.");
                request.getRequestDispatcher("successPage.jsp").forward(request, response);

            } catch (NumberFormatException e) {
                // Handle invalid amount format
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid amount format.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Amount is required.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(CashPaymentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(CashPaymentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Controller for handling cash payments";
    }
}
