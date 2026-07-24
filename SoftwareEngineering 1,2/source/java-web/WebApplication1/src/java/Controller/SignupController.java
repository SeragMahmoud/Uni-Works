package Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dbconnect;
import model.Customer;

@WebServlet(name = "SignupController", urlPatterns = {"/signup"})
public class SignupController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String username = request.getParameter("signupName");
        String email = request.getParameter("signupEmail");
        String password = request.getParameter("signupPassword");

        if (username == null || email == null || password == null || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            response.getWriter().println("Invalid input. All fields are required.");
            return;
        }

        Customer customer = new Customer(username, password, email);
        dbconnect db = new dbconnect();

       try {
            boolean isSignedUp = db.signupCustomer(customer);
            if (isSignedUp) {
                response.sendRedirect("LoginPage.jsp");
            } else {
                response.getWriter().println("Signup failed. Please try again.");
            }
        } catch (SQLException e) {
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, e);
            response.getWriter().println("An error occurred while processing your request.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Signup Controller Servlet";
    }
}