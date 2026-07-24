package com.service.carservice.center;

import java.sql.Date;

class Customer extends User {

    private static int idCounter = 1; 
    private int customerID;
    private String customerLocation;

    public Customer(String name, String email, String phone, String customerLocation) {
        super(name, email, phone);
        this.customerID = idCounter++; 
        this.customerLocation = customerLocation;
    }

    public enum PaymentStatus {
        SUCCESS, FAILURE, PENDING
    }

   public Service selectService(Service service) {
        int serviceID = service.getServiceID();
        System.out.println("Service selected: ID = " + service.getServiceID() + ", Name = " + service.getName());
        return service;
    }


    public PaymentStatus makePayment(Payment payment) {
        double paymentAmount = payment.getAmount();
        System.out.println("Attempting to make a payment of $" + paymentAmount);
        if (paymentAmount > 0) {
            System.out.println("Payment successful!");
            return PaymentStatus.SUCCESS;
        } else {
            System.out.println("Payment failed. Invalid amount.");
            return PaymentStatus.FAILURE;
        }
    }

    public void leaveFeedback(Service service, Review review) {
        int serviceID = service.getServiceID();
        int rating = review.getRating();
        String comment = review.getComment();
        if (rating < 1 || rating > 5) {
            System.out.println("Invalid rating. Rating must be between 1 and 5.");
            return;
        }
        System.out.println("Feedback left for Service ID " + serviceID + ": Rating = " + rating + ", Comment = '" + comment + "'");
    }

    public boolean makeBooking(Service service, Date date) {
        int serviceID = service.getServiceID();
        Booking newBooking = new Booking(serviceID, date);
        bookingHistory.add(newBooking);
        System.out.println("Booking created: Service ID = " + serviceID + ", Date = " + date);
        return true;
    }

    public boolean cancelBooking(Booking booking) {
        int bookingID = booking.getBookingID();
        for (Booking booking : bookingHistory) {
            if (booking.getBookingID() == bookingID) {
                bookingHistory.remove(booking);
                System.out.println("Booking with ID " + bookingID + " has been canceled.");
                return true;
            }
        }
        System.out.println("Booking with ID " + bookingID + " not found.");
        return false;
    }

    public void viewBookingHistory() {
        System.out.println("Booking history:");
        for (Booking booking : bookingHistory) {
            System.out.println("ID: " + booking.getBookingID() + ", Date: " + booking.getDate() + ", Service: " + booking.getServiceName());
        }
    }
    
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setCustomerLocation(String customerLocation) {
        this.customerLocation = customerLocation;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerLocation() {
        return customerLocation;
    }

    public void updateProfile(String newName, String newEmail, String newPhone) {
        if (validateName(newName) && validateEmail(newEmail) && validatePhone(newPhone)) {
            super.setName(newName);
            super.setEmail(newEmail);
            super.setPhone(newPhone);
            System.out.println("Profile updated successfully!");
        } else {
            System.out.println("Profile update failed. Please check the input values.");
        }
    }
    
    private boolean validateName(String name) {
        return name != null && !name.trim().isEmpty();
    }
    
    private boolean validateEmail(String email) {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
    
    private boolean validatePhone(String phone) {
        return phone != null && phone.matches("\\d{20}");
    }

}
