package com.service.carservice.center;

/**
 *
 * @author dodo5
 */

import java.util.List;
import java.util.ArrayList;

public class Technician extends User {
    public Technician(String userName, String password, String name, String email, String phone) {
		super(userName, password, name, email, phone);
                technicianList.add(this);
		// TODO Auto-generated constructor stub
	}

    private int technicianID;
    private String specialization;
    private Schedule workSchedule;
    private TechnicianStatus status;
    private static List<Technician> technicianList = new ArrayList<>();

    // Getters and setters
    public int getTechnicianID() {
        return technicianID;
    }

    public void setTechnicianID(int technicianID) {
        this.technicianID = technicianID;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Schedule getWorkSchedule() {
        return workSchedule;
    }
    
    public static Technician getTechnician(int technicianID) {
        for (Technician technician : technicianList) {
            if (technician.getTechnicianID() == technicianID) {
                return technician;  // Return the technician if the ID matches
            }
        }
        return null;  // Return null if no technician with the given ID is found
    }

    public void setWorkSchedule(Schedule workSchedule) {
        this.workSchedule = workSchedule;
    }

    public TechnicianStatus getStatus() {
        return status;
    }

    public void setStatus(TechnicianStatus status) {
        this.status = status;
    }

    // TechnicianStatus Enum
    public enum TechnicianStatus {
        AVAILABLE,
        BUSY,
        OFFLINE,
        ON_BREAK
    }

    // Method to view all assigned bookings
    public void viewAssignedBookings() {
        if (workSchedule != null && workSchedule.getBookings() != null) {
            List<Booking> bookings = workSchedule.getBookings();
            for (Booking booking : bookings) {
                System.out.println("Booking ID: " + booking.getBookingID() + ", Service: " + booking.getServices());
            }
        } else {
            System.out.println("No bookings assigned to this technician.");
        }
    }

    // Method to update technician's availability status
    public void updateAvailability(Technician technician) {
        this.status = technician.getStatus();
        System.out.println("Technician status updated to: " + technician.getStatus());
    }

    // Method to record service details for a specific booking
    public void recordServiceDetails(Booking booking, int serviceID, String serviceDetails) {
        if (booking != null) {
            booking.updateServiceDetails(serviceID, serviceDetails);
        } else {
            System.out.println("Booking not found.");
        }
    }
    
//    public boolean isEmpty(Schedule schedule) {
//        if (schedule == null) {
//            return true;  
//        }
//
//        if (schedule.getWorkDays() == null || schedule.getWorkDays().length == 0) {
//            return true;
//        }
//
//        if (schedule.getStartTime() != null && !schedule.getStartTime().isEmpty() &&
//            schedule.getEndTime() != null && !schedule.getEndTime().isEmpty()) {
//        } else {
//            return true;
//        }
//
//        if (schedule.getBookings() == null || schedule.getBookings().isEmpty()) {
//            return true;
//        }
//
//        return false;
//}
    
    public void viewSchedule() {
    if (workSchedule == null) {
        System.out.println("No schedule assigned to this technician.");
        return;
    }

    System.out.println("Technician " + User.getUsername() + "'s Schedule:");  // Corrected typo: getUsername()
    List<Booking> bookings = workSchedule.getBookings();  // Assuming getBookings() returns the list of bookings for this schedule

    if (bookings.isEmpty()) {
        System.out.println("No appointments scheduled.");
    } else {
        for (Booking booking : bookings) {
            System.out.println(booking.toString());  // Assuming you have a meaningful toString() in Booking
        }
    }
}


    // Method to assign technician to a booking
    public void assignTechnicianToBooking(Booking booking) {
        if (booking != null && this.status == TechnicianStatus.AVAILABLE) {
            booking.assignTechnician(this.technicianID);
            System.out.println("Technician " + this.technicianID + " assigned to Booking ID: " + booking.getBookingID());
        } else {
            System.out.println("Technician cannot be assigned to the booking. Technician is either unavailable or booking is invalid.");
        }
    }
}
