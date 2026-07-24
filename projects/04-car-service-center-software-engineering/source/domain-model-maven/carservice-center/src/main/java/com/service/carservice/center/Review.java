/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.service.carservice.center;

/**
 *
 * @author dodo5
 */



import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Review {
    private int reviewID;
    private int userID;
    private int serviceID;
    private Booking bookingID;
    private int rating;
    private String comment;
    private String datePosted;

    private static List<Review> reviewList = new ArrayList<>();

    public Review(int reviewID, int userID, int serviceID, int bookingID, int rating, String comment, String datePosted) {
        this.reviewID = reviewID;
        this.userID = userID;
        this.serviceID = serviceID;
        this.bookingID = new Booking(bookingID, userID, serviceID); 
        this.rating = rating;
        this.comment = comment;
        this.datePosted = datePosted;
    }


    public int getReviewID() {
        return reviewID;
    }

    public int getUserID() {
        return userID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean createReview(int reviewID, int userID, int serviceID, Booking booking, int rating, String comment, String datePosted) {
        
        for (Review review : reviewList) {
            if (booking.getBookingID() == review.bookingID.getBookingID()) {
                System.out.println("Review for this booking already exists.");
                return false;
            }
        }

        Review newReview = new Review(reviewID, userID, serviceID, booking.getBookingID(), rating, comment, datePosted);
        reviewList.add(newReview);
        System.out.println("Review created successfully.");
        return true;
    }

    public boolean editReview(int reviewID, int newRating, String newComment) {
        for (Review review : reviewList) {
            if (review.getReviewID() == reviewID) {
                review.setRating(newRating);
                review.setComment(newComment);
                System.out.println("Review edited successfully.");
                return true;
            }
        }
        System.out.println("Review not found.");
        return false;
    }

    public boolean deleteReview(int reviewID) {
        for (Review review : reviewList) {
            if (review.getReviewID() == reviewID) {
                reviewList.remove(review);
                System.out.println("Review deleted successfully.");
                return true;
            }
        }
        System.out.println("Review not found.");
        return false;
    }

    public static Review getReviewDetails(int reviewID) {
        for (Review review : reviewList) {
            if (review.getReviewID() == reviewID) {
                return review;
            }
        }
        System.out.println("Review not found.");
        return null;
    }

    public List<Review> listReviewsByService(int serviceID) {
        List<Review> reviewsByService = new ArrayList<>();
        for (Review review : reviewList) {
            if (review.getServiceID() == serviceID) {
                reviewsByService.add(review);
            }
        }
        return reviewsByService;
    }
    public void setResponse(String response){



    }
}
