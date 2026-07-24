package com.service.carservice.center;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Administrator extends User  {
   private int adminID;

    public Administrator( String userName, String password, String name, String email, String phone, int adminID) {
        super( userName, password, name, email, phone);
        this.adminID = adminID;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public void addService(String name, String description, double price) {
        Service newService = new Service(name, description, price);

        System.out.println("Service added: " + newService);
    }


    public static void editService(int serviceID, String newDetails) {
        Service service = Service.getService(serviceID);
        if (service != null) {
            service.setDescription(newDetails);
            System.out.println("Service updated: " + service);
        } else {
            System.out.println("Service not found.");
        }
    }


   public boolean deleteService(int serviceID) {
        Service serviceList = new Service(serviceID);
        Iterator<Service> iterator = serviceList.listServices().iterator();
        while (iterator.hasNext()) {
            Service service = iterator.next();
            if (service.getServiceID() == serviceID) {
                iterator.remove();
                System.out.println("Service with ID " + serviceID + " deleted successfully.");
                return true;
            }
        }
        System.out.println("Service with ID " + serviceID + " not found.");
        return false;
    }


   public void viewAllBookings(Booking bookingInstance) {
        ArrayList<Booking> allBookings = bookingInstance.getBookingHistory();
        
        if (allBookings.isEmpty()) {
            System.out.println("No bookings available.");
        } else {
            System.out.println("All Bookings:");
            for (Booking booking : allBookings) {
                System.out.println(booking.toString());
            }
        }
    }


    public void assignTechnician(int bookingID, int technicianID) {
    Booking booking = Booking.getBookingDetails(bookingID);
    Technician technician = Technician.getTechnician(technicianID);
    if (booking != null && technician != null && technician.getStatus() == Technician.TechnicianStatus.AVAILABLE) {
        booking.assignTechnician(technicianID);
        technician.setStatus(Technician.TechnicianStatus.BUSY);  // Set technician status to BUSY
        System.out.println("Technician " + technicianID + " assigned to booking ID: " + bookingID);
    } else {
        System.out.println("Booking or Technician not found, or Technician is not available.");
    }
}


    public void updateBookingStatus(int bookingID, String status) {
        Booking booking = Booking.getBookingDetails(bookingID);
        if (booking != null) {
            booking.setStatus(status);
            System.out.println("Booking status updated: " + booking);
        } else {
            System.out.println("Booking not found.");
        }
    }


    public void manageSchedule(int technicianID, int scheduleID, String[] newWorkDays, String startTime, String endTime) {
        Schedule schedule = Schedule.getSchedule(scheduleID);
        if (schedule != null) {
            schedule.updateSchedule(scheduleID, newWorkDays, startTime, endTime);
            System.out.println("Schedule updated: " + schedule);
        } else {
            System.out.println("Schedule not found.");
        }
    }


    public String trackPaymentStatus(int bookingID) {


        Booking booking = Booking.getBookingDetails(bookingID);
        Payment payment = booking.getPayment();
        if (payment != null) {
            System.out.println("Payment status: " + payment.getStatus());
            return payment.getStatus();
        }
        System.out.println("Payment not found.");
        return "Not Found";
    }


    public void respondToFeedback(int feedbackID, String response) {
        Review review = Review.getReviewDetails(feedbackID);
        if (review != null) {
            review.setResponse(response);
            System.out.println("Response added to feedback: " + review);
        } else {
            System.out.println("Feedback not found.");
        }
    }


    public File generateReports(String reportType) {
        File report = new File(reportType + "_report.txt");
        System.out.println("Report generated: " + report.getName());
        return report;
    }


    public boolean deleteTechnicianSchedule(int scheduleID) {
        boolean result = Schedule.deleteSchedule(scheduleID);
        if (result) {
            System.out.println("Technician schedule deleted.");
        } else {
            System.out.println("Schedule not found or could not be deleted.");
        }
        return result;
    }
}