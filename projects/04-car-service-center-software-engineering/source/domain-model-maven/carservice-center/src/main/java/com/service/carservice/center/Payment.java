/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.service.carservice.center;

/**
 *
 * @author dodo5
 */

public class Payment {
    private int paymentID;
    private Booking booking; // Composition with Booking
    private double amount;
    private String paymentDate;
    private String status;

    // Constructor with composition
    public Payment(int paymentID, Booking booking, double amount, String paymentDate, String status) {
        this.paymentID = paymentID;
        this.booking = booking; 
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    // Getters and Setters
    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

  
    public boolean processPayment(double amount, String paymentType) {
        if (amount <= 0) {
            System.out.println("Invalid amount. Payment failed.");
            return false;
        }

        System.out.println("Processing " + paymentType + " payment for booking ID: " + booking.getBookingID());
        this.amount = amount;
        this.status = "Processed";
        this.paymentDate = java.time.LocalDate.now().toString(); 
        System.out.println("Payment processed successfully.");
        
        return true;
    }

    public boolean refundPayment() {
        if (this.status.equals("Processed")) {
            System.out.println("Refunding payment ID: " + paymentID);
            this.status = "Refunded";
            System.out.println("Refund successful.");
            return true;
        } else {
            System.out.println("Refund failed. Payment not processed yet or already refunded.");
            return false;
        }
    }

    public Payment getPaymentDetails() {
        System.out.println("Fetching payment details for payment ID: " + paymentID);
        System.out.println("Booking ID: " + booking.getBookingID());
        System.out.println("Amount: " + amount);
        System.out.println("Payment Date: " + paymentDate);
        System.out.println("Status: " + status);
        return this;
    }

    public boolean updatePaymentStatus(String newStatus) {
        if (newStatus == null || newStatus.isEmpty()) {
            System.out.println("Invalid status. Update failed.");
            return false;
        }
        
        System.out.println("Updating payment status to: " + newStatus);
        this.status = newStatus;
        System.out.println("Status updated successfully.");
        
        return true;
    }
}

