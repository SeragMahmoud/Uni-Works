package controller;




import com.google.gson.Gson;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


   


@WebServlet(name = "BookingController", urlPatterns = {"/BookingController"})
public class BookingController extends HttpServlet {

    private static String requiredEnv(String name) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Set " + name + " before running the application.");
        }
        return value;
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                requiredEnv("CAR_SERVICE_DB_URL"),
                requiredEnv("CAR_SERVICE_DB_USER"),
                requiredEnv("CAR_SERVICE_DB_PASSWORD"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            processLogin(request, response);
        } else if ("createBooking".equals(action)) {
            processBooking(request, response);
        } else if ("fetchSchedule".equals(action)) {
            fetchSchedule(request, response);
        } else {
            response.getWriter().write("Invalid action.");
        }
    }

    private void processLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("name");
        String password = request.getParameter("pass");

        boolean valid = true; // Replace with actual validation logic
        if (valid) {
            RequestDispatcher rp = request.getRequestDispatcher("home.jsp");
            rp.forward(request, response);
        } else {
            RequestDispatcher rp = request.getRequestDispatcher("login.jsp");
            rp.forward(request, response);
        }
    }

    private void processBooking(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
        String customerUsername = request.getParameter("customerUsername");
        String serviceName = request.getParameter("serviceName");
        String bookingDateStr = request.getParameter("bookingDate");
        System.out.println("Received: " + customerUsername + ", " + serviceName + ", " + bookingDateStr);

        if (customerUsername == null || serviceName == null || bookingDateStr == null) {
            response.getWriter().write("Missing required parameters.");
            return;
        }

        java.util.Date bookingDate = java.sql.Date.valueOf(bookingDateStr);
        String bookingMessage = createBooking(customerUsername, serviceName, bookingDate);
        response.getWriter().write(bookingMessage);
    } catch (Exception e) {
        e.printStackTrace();
        response.getWriter().write("Error: " + e.getMessage());
    }
}


    public String createBooking(String customerUsername, String serviceName, java.util.Date date) throws SQLException {
        Connection con = getConnection();

        int customerID = -1;
        int serviceID = -1;
        int bookingID = (int) (Math.random() * 1000);

        try {
            String customerQuery = "SELECT CUSTOMERID FROM CUSTOMER WHERE CUSTOMERUSERNAME = ?";
            PreparedStatement customerStmt = con.prepareStatement(customerQuery);
            customerStmt.setString(1, customerUsername);
            ResultSet customerRs = customerStmt.executeQuery();
            if (customerRs.next()) {
                customerID = customerRs.getInt("CUSTOMERID");
            } else {
                return "Customer not found.";
            }

            String serviceQuery = "SELECT SERVICEID FROM SERVICE WHERE SERVICENAME = ?";
            PreparedStatement serviceStmt = con.prepareStatement(serviceQuery);
            serviceStmt.setString(1, serviceName);
            ResultSet serviceRs = serviceStmt.executeQuery();
            if (serviceRs.next()) {
                serviceID = serviceRs.getInt("SERVICEID");
            } else {
                return "Service not found.";
            }

            String bookingQuery = "INSERT INTO BOOKING (BOOKINGID, CUSTOMERID, SERVICEID, BOOKINGDATE, STATUS) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement bookingStmt = con.prepareStatement(bookingQuery);
            bookingStmt.setInt(1, bookingID);
            bookingStmt.setInt(2, customerID);
            bookingStmt.setInt(3, serviceID);
            bookingStmt.setDate(4, new java.sql.Date(date.getTime()));
            bookingStmt.setString(5, "Successfully created");

            int rowsAffected = bookingStmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Booking created successfully with ID: " + bookingID;
            }
            return "Booking creation failed.";
        } finally {
            con.close();
        }
    }

private void fetchSchedule(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try (Connection con = getConnection()) {
        // Query to fetch schedule data from the database
        String query = "SELECT STARTTIME, ENDTIME FROM SCHEDULE";
        PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        // Prepare a list of maps to store the schedule data
        List<Map<String, String>> scheduleList = new ArrayList<>();
        while (rs.next()) {
            Map<String, String> schedule = new HashMap<>();
            schedule.put("startTime", rs.getString("STARTTIME"));
            schedule.put("endTime", rs.getString("ENDTIME"));
            scheduleList.add(schedule);
        }

        // Set the response type to JSON and send the result
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(scheduleList));
    } catch (SQLException e) {
        response.getWriter().write("{\"error\": \"Error fetching schedule: " + e.getMessage() + "\"}");
    }
}

}


