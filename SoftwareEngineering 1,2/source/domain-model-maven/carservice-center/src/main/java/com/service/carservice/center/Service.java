package com.service.carservice.center;


import java.util.ArrayList;

public class Service {
    private int serviceID;
    private String name;
    private String description;
    private double price;
    private String servicedetails;

    // Instance-level list to store services related to this Service object
    private static ArrayList<Service> serviceList;

    // Constructor
    public Service(int serviceID, String name, String description, double price) {
        this.serviceID = serviceID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.serviceList = new ArrayList<>();
    }

    public Service(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Service(String servicedetails) {
        this.servicedetails = servicedetails;
    }
    
    public Service(int serviceID) {
        this.serviceID = serviceID;
    }
    
	// Getters and Setters
    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServicedetails() {
        return servicedetails;
    }

    public void setServicedetails(String servicedetails) {
        this.servicedetails = servicedetails;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    // Method to add a new service to the list
    public void addService(Service service) {
        serviceList.add(service);
        System.out.println("Service added: " + service.getName());
    }

    // Method to edit a service in the list
    public boolean editService(int serviceID, String newDescription) {
        for (Service service : serviceList) {
            if (service.getServiceID() == serviceID) {
                service.setDescription(newDescription);
                System.out.println("Service updated successfully.");
                return true;
            }
        }
        System.out.println("Service update failed: Service ID not found.");
        return false;
    }

    // Method to delete a service from the list
    public boolean deleteService(int serviceID) {
        for (Service service : serviceList) {
            if (service.getServiceID() == serviceID) {
                serviceList.remove(service);
                System.out.println("Service deleted successfully.");
                return true;
            }
        }
        System.out.println("Service deletion failed: Service ID not found.");
        return false;
    }

    // Method to retrieve a service from the list
    public static Service getService(int serviceID) {
        for (Service service : serviceList) {
            if (service.getServiceID() == serviceID) {
                System.out.println("Service details retrieved successfully.");
                return service;
            }
        }
        System.out.println("Service details not found.");
        return null;
    }

    // Method to list all services
    public ArrayList<Service> listServices() {
        System.out.println("List of services retrieved.");
        return new ArrayList<>(serviceList);
    }

    // Method to mark a service as completed (if needed)
    public boolean serviceCompleted(int serviceID) {
        for (Service service : serviceList) {
            if (service.getServiceID() == serviceID) {
                System.out.println("Service marked as completed.");
                return true;
            }
        }
        System.out.println("Service completion marking failed: Service ID not found.");
        return false;
    }
}

