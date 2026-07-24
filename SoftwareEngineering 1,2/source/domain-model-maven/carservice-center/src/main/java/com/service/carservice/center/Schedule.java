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
import java.util.List;

public class Schedule {
    private int scheduleID;
    private int technicianID;
    private String[] workDays;  
    private String startTime;
    private String endTime;
    private List<Booking> bookings;
    private static List<Schedule> scheduleList; 

    public Schedule(int scheduleID, int technicianID, String[] workDays, String startTime, String endTime) {
        this.scheduleID = scheduleID;
        this.technicianID = technicianID;
        this.workDays = workDays;
        this.startTime = startTime;
        this.endTime = endTime;
        bookings = new ArrayList<>();
        scheduleList = new ArrayList<>(); 
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public int getTechnicianID() {
        return technicianID;
    }

    public String[] getWorkDays() {
        return workDays;
    }

    public String getStartTime() {
        return endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setWorkDays(String[] workDays) {
        this.workDays = workDays;
    }

    public void setStartTime(String startTime) {
        this.endTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public static boolean createSchedule(int scheduleID, int technicianID, String[] workDays, String startTime, String endTime) {
    
        for (Schedule schedule : scheduleList) {
            if (schedule.getTechnicianID() == technicianID) {
                System.out.println("Schedule for this technician already exists.");
                return false;
            }
        }
        
        Schedule newSchedule = new Schedule(scheduleID, technicianID, workDays, startTime, endTime);
        scheduleList.add(newSchedule);
        System.out.println("Schedule created successfully.");
        return true;
    }

    public static boolean updateSchedule(int scheduleID, String[] newWorkDays, String newStartTime, String newEndTime) {
        for (Schedule schedule : scheduleList) {
            if (schedule.getScheduleID() == scheduleID) {
               
                schedule.setWorkDays(newWorkDays);
                schedule.setStartTime(newStartTime);
                schedule.setEndTime(newEndTime);
                System.out.println("Schedule updated successfully.");
                return true;
            }
        }
        System.out.println("Schedule not found.");
        return false;
    }

    public static boolean deleteSchedule(int scheduleID) {
        for (Schedule schedule : scheduleList) {
            if (schedule.getScheduleID() == scheduleID) {
                scheduleList.remove(schedule);
                System.out.println("Schedule deleted successfully.");
                return true;
            }
        }
        System.out.println("Schedule not found.");
        return false;
    }

    public static Schedule getSchedule(int technicianID) {
        for (Schedule schedule : scheduleList) {
            if (schedule.getTechnicianID() == technicianID) {
                return schedule;
            }
        }
        System.out.println("Schedule not found for technician.");
        return null;
    }

//    public ArrayList<Booking> getBookingsByTechnician(String technicianName) {
//        ArrayList<Booking> technicianBookings = new ArrayList<>();
//        for (Booking booking : bookings) {
//            if (booking.getTechnicianName().equals(technicianName)) {
//                technicianBookings.add(booking);
//            }
//        }
//        return technicianBookings;
//    }
}

