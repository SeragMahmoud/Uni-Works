package com.service.carservice.center;


import java.util.ArrayList;
import java.util.Date;

public class Booking {
    private int bookingID;
    private int userID;
    private int serviceID;
    private Date date; 
    private String status;
    private int assignedTechnicianID;  // Added field to store assigned technician's ID
    private ArrayList<Service> services;    // Added list to store service details
    private static ArrayList<Booking> bookingHistory;
    private Payment payment;// Static list to store all bookings

    // Constructor with complete details
    public Booking(int bookingID, int userID, int serviceID, Date date, String status) {
        this.bookingID = bookingID;
        this.userID = userID;
        this.serviceID = serviceID;
        this.date = date;
        this.status = status;
        this.services = new ArrayList<>();
        this.bookingHistory = new ArrayList<>();
        bookingHistory.add(this);  // Add the booking to history when created
    }

    // Constructor with only bookingID, userID, and serviceID
    public Booking(int bookingID, int userID, int serviceID) {
        this.bookingID = bookingID;
        this.userID = userID;
        this.serviceID = serviceID;
        this.services = new ArrayList<>();
    }

    public Booking(int serviceID, Date date) {
        this.serviceID = serviceID;
        this.date = date;
    }
    
    

    // Getters and Setters
    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAssignedTechnicianID() {
        return assignedTechnicianID;
    }

    public void assignTechnician(int technicianID) {
        this.assignedTechnicianID = technicianID;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void addService(Service service) {
        services.add(service);
    }

    // Static method to get a booking by ID
    public static Booking getBookingDetails(int bookingID) {
        for (Booking booking : bookingHistory) {
            if (booking.getBookingID() == bookingID) {
                return booking;
            }
        }
        System.out.println("Booking with ID " + bookingID + " not found.");
        return null;
    }

    // Method to create a new booking
    public int createBooking(int customerID, int serviceID, Date date, String status) {
        this.bookingID = (int) (Math.random() * 1000); 
        this.userID = customerID;
        this.serviceID = serviceID;
        this.date = date;
        this.status = status;
        bookingHistory.add(this);
        System.out.println("Booking created successfully with ID: " + bookingID);
        return bookingID;
    }

    public Payment getPayment() {
        return payment;
    }

    // Method to cancel a booking
    public boolean cancelBooking(int bookingID) {
        if (this.bookingID == bookingID && !status.equals("Completed")) {
            this.status = "Cancelled";
            System.out.println("Booking cancelled successfully.");
            return true;
        }
        System.out.println("Booking cancellation failed.");
        return false;
    }

    // Method to update a booking's date
    public boolean updateBooking(int bookingID, Date newDate) {
        if (this.bookingID == bookingID) {
            this.date = newDate; 
            System.out.println("Booking updated successfully.");
            return true;
        }
        System.out.println("Booking update failed.");
        return false;
    }

    // Method to confirm a booking
    public boolean confirmBooking(int bookingID) {
        if (this.bookingID == bookingID && status.equals("Created")) {
            this.status = "Confirmed";
            System.out.println("Booking confirmed successfully.");
            return true;
        }
        System.out.println("Booking confirmation failed.");
        return false;
    }
    
    public ArrayList<Booking> getBookingHistory() {
        return bookingHistory;
    }
    
    public void updateServiceDetails(int serviceID, String newDetails) {
        for (Service service : services) {
            if (service.getServiceID() == serviceID) {
                service.setServicedetails(newDetails);
                System.out.println("Service details updated for Service ID: " + serviceID);
                return;
            }
        }
        System.out.println("Service with ID " + serviceID + " not found.");
    }
    
    // Overriding toString to return booking details as a string
    @Override
    public String toString() {
        StringBuilder bookingDetails = new StringBuilder();
        bookingDetails.append("Booking ID: ").append(this.bookingID).append("\n");
        bookingDetails.append("User ID: ").append(this.userID).append("\n");
        bookingDetails.append("Service ID: ").append(this.serviceID).append("\n");
        bookingDetails.append("Booking Date: ").append(this.date).append("\n");
        bookingDetails.append("Status: ").append(this.status).append("\n");
        bookingDetails.append("Assigned Technician ID: ").append(this.assignedTechnicianID).append("\n");

        if (this.services != null && !this.services.isEmpty()) {
            bookingDetails.append("Services:\n");
            for (Service service : this.services) {
                bookingDetails.append("  Service ID: ").append(service.getServiceID()).append("\n");
                bookingDetails.append("  Service Name: ").append(service.getName()).append("\n");
                bookingDetails.append("  Service Description: ").append(service.getDescription()).append("\n");
                bookingDetails.append("  Service Price: ").append(service.getPrice()).append("\n");
            }
        } else {
            bookingDetails.append("No services associated with this booking.\n");
        }

        return bookingDetails.toString();
    }
}

