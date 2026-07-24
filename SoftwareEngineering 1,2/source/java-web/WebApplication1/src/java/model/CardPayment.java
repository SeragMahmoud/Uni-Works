/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


/**
 *
 * @author dodo5
 */



public class CardPayment extends Payment {
    private String cardNumber;
    private String cardExpiry;
    private String cardCVV;

    public CardPayment(int paymentID, Booking booking, double amount, String paymentDate, String status, String cardNumber, String cardExpiry, String cardCVV) {
        super(paymentID, booking, amount, paymentDate, status); // Call parent constructor
        this.cardNumber = cardNumber;
        this.cardExpiry = cardExpiry;
        this.cardCVV = cardCVV;
    }

    public boolean validateCardDetails() {
        if (this.cardNumber == null || !this.cardNumber.matches("\\d{16}")) {
            System.out.println("Invalid card number.");
            return false;
        }
        
        if (this.cardExpiry == null || !this.cardExpiry.matches("\\d{2}/\\d{2}")) {
            System.out.println("Invalid card expiry date.");
            return false;
        }

        if (this.cardCVV == null || !this.cardCVV.matches("\\d{3}")) {
            System.out.println("Invalid card CVV.");
            return false;
        }

        System.out.println("Card details are valid.");
        return true;
    }

    public boolean validateAccount() {
        System.out.println("Validating card account for payment...");
        
        return validateCardDetails();
    }

    @Override
    public boolean processPayment(double amount, String paymentType) {
        if (!validateAccount()) {
            System.out.println("Card account validation failed. Payment cannot be processed.");
            return false;
        }

        System.out.println("Processing card payment for booking ID: " + getBooking().getBookingID());
        this.setAmount(amount); 
        this.setStatus("Processed"); 
        this.setPaymentDate(java.time.LocalDate.now().toString());         System.out.println("Card payment processed successfully.");
        
        return true;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpiry() {
        return cardExpiry;
    }

    public void setCardExpiry(String cardExpiry) {
        this.cardExpiry = cardExpiry;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public void setCardCVV(String cardCVV) {
        this.cardCVV = cardCVV;
    }
}
