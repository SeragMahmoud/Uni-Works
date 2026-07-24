package Controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dbconnect;

@WebServlet(name = "CreateBooking", urlPatterns = {"/CreateBooking"})
public class BookingController extends HttpServlet {

    // The processRequest method handles both GET and POST requests
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        dbconnect d = new dbconnect();
        List<model.Schedule> s = d.getAvailableSchedules();  // Use the method to fetch schedules
        
        request.setAttribute("schedule", s); // Set schedule attribute for JSP
        RequestDispatcher req = request.getRequestDispatcher("booking.jsp");
        req.forward(request, response); // Forward the request to the JSP
    }

    // Do GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(BookingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Do POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle form submission here
        // If needed, you can capture the selected date and process booking here.
        String selectedDate = request.getParameter("bookingDate");
        // Process the booking logic here (e.g., saving to database or further processing)
        // Redirect or forward to another page after processing
        response.sendRedirect("bookingSuccess.jsp"); // Redirect to a success page after booking
    }
}
