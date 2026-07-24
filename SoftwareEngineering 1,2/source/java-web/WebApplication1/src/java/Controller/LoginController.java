package Controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.dbconnect;

@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("loginName");
        String password = request.getParameter("loginPassword");

        dbconnect db = new dbconnect();

        try {
            boolean isValidUser = db.isValidCustomer(username, password);

            if (isValidUser) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);

                request.setAttribute("successMessage", "Login successful!");
                request.getRequestDispatcher("homePage.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Invalid username or password. Please try again.");
                request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            log("Database error: " + e.getMessage()); 
            request.setAttribute("errorMessage", "An error occurred while processing your request.");
            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
        } catch (Exception e) {
            log("Unexpected error: " + e.getMessage()); 
            request.setAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
            request.getRequestDispatcher("LoginPage.jsp").forward(request, response);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Login Controller Servlet";
    }
}
