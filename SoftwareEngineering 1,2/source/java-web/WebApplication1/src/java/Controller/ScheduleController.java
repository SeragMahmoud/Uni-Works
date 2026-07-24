package controller;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.servlet.*;
import javax.servlet.http.*;

public class ScheduleController extends HttpServlet {

    // Handle POST requests (for schedule creation)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve schedule data from the form (parameters from the request)
            int scheduleID = Integer.parseInt(request.getParameter("scheduleID"));
            String startDate = request.getParameter("startDate");  // format "yyyy-MM-dd"
            String endDate = request.getParameter("endDate");      // format "yyyy-MM-dd"

            // Call method to create the schedule in the database
            boolean isScheduleCreated = createScheduleInDB(scheduleID, startDate, endDate);

            // Set the response message
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            // Provide feedback based on the result
            if (isScheduleCreated) {
                out.println("<html><body><h2>Schedule created successfully!</h2></body></html>");
            } else {
                out.println("<html><body><h2>Schedule creation failed.</h2></body></html>");
            }
        } catch (Exception e) {
            response.getWriter().println("<html><body><h2>Error: " + e.getMessage() + "</h2></body></html>");
        }
    }

    // Handle GET requests (redirects or fetching specific data)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // You can add logic here to show schedule details or redirect as needed
        response.sendRedirect("scheduleForm.jsp"); // Redirect to a form where users can input schedule data
    }

    // Method to create a schedule in the database
    public boolean createScheduleInDB(int scheduleID, String startDate, String endDate) {
        String url = "jdbc:derby://localhost:1527/ProjectSoftware";
        String dbuname = "admin1";
        String dbpass = "123";
        Connection con = null;
        PreparedStatement pst = null;

        try {
            // Establish connection
            con = DriverManager.getConnection(url, dbuname, dbpass);

            // Convert String to java.util.Date using SimpleDateFormat
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Date format
            java.util.Date startUtilDate = sdf.parse(startDate);
            java.util.Date endUtilDate = sdf.parse(endDate);
            
            // Convert java.util.Date to java.sql.Date
            Date startSqlDate = new Date(startUtilDate.getTime());  // Convert java.util.Date to java.sql.Date
            Date endSqlDate = new Date(endUtilDate.getTime());      // Convert java.util.Date to java.sql.Date

            // Prepare SQL query to insert schedule data
            String bookingQuery = "INSERT INTO SCHEDULE(SCHEDULEID, STARTTIME, ENDTIME) VALUES (?, ?, ?)";

            // Create PreparedStatement and set parameters
            pst = con.prepareStatement(bookingQuery);
            pst.setInt(1, scheduleID);
            pst.setDate(2, startSqlDate);  // Set the start date as java.sql.Date
            pst.setDate(3, endSqlDate);    // Set the end date as java.sql.Date

            // Execute the query
            int rowsAffected = pst.executeUpdate();

            // Check if insertion was successful
            if (rowsAffected > 0) {
                return true;  // Schedule created successfully
            } else {
                return false;  // Failed to create schedule
            }

        } catch (SQLException | java.text.ParseException e) {
            e.printStackTrace();
            return false;  // Error occurred during creation
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
