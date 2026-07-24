package model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.List;

public class User {
    private int userID;
    private String userName;
    private String password;
    private String Name;
    private String Email;
    private String Phone;
    private static int cuont=0;

    

    public User(String password, String userName) {
        this.password = password;
        this.userName = userName;
    }
    
    

    public User( String userName, String password, String name, String email, String phone) {
        this.userID = cuont++;
        this.userName = userName;
        this.password = password;
        Name = name;
        Email = email;
        Phone = phone;
    }

    public User( String name, String email, String phone) {
        this.userID = cuont++;
        Name = name;
        Email = email;
        Phone = phone;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }


    public static boolean login(String username, String password, List<User> users) {
        for (User user : users) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }


    public static User register(String userName, String password, String email, List<User> users) {

        for (User user : users) {
            if (user.getUserName().equals(userName)) {
                System.out.println("Username already exists.");
                return null;
            }
            if (user.getEmail().equals(email)) {
                System.out.println("Email already in use.");
                return null;
            }
        }


        int newUserID = users.size() + 1;
        User newUser = new User( userName, password, "", email, "");
        users.add(newUser);

        System.out.println("User registered successfully.");
        return newUser;
    }

}