/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.service.carservice.center;

/**
 *
 * @author dodo5
 */

public class CashPayment extends Payment {

    public CashPayment(int paymentID, Booking booking, double amount, String paymentDate, String status) {
        super(paymentID, booking, amount, paymentDate, status);  
    }

    @Override
    public boolean processPayment(double amount, String paymentType) {
        if (amount <= 0) {
            System.out.println("Invalid amount. Cash payment failed.");
            return false;
        }

        System.out.println("Processing cash payment for booking ID: " + getBooking().getBookingID());
        this.setAmount(amount);  
        this.setStatus("Processed");  
        this.setPaymentDate(java.time.LocalDate.now().toString());  
        System.out.println("Cash payment processed successfully.");
        
        confirmReceipt();  
        
        return true;
    }

    public boolean confirmReceipt() {
        System.out.println("Cash payment receipt confirmed.");
        return true;
    }

    public double getAmount() {
        return super.getAmount();
    }

    public void setAmount(double amount) {
        super.setAmount(amount);
    }

    public String getPaymentDate() {
        return super.getPaymentDate();
    }

    public void setPaymentDate(String paymentDate) {
        super.setPaymentDate(paymentDate);
    }

    public String getStatus() {
        return super.getStatus();
    }

    public void setStatus(String status) {
        super.setStatus(status);
    }

    public Booking getBooking() {
        return super.getBooking();
    }

    public void setBooking(Booking booking) {
        super.setBooking(booking);
    }
}
