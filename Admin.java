package com.example.modifiedcinemasystem;

public class Admin {
    private String username;
    private String password;

    // Constructor
    public Admin() {
        this.username = "OOPSTERS";
        this.password = "FA24-BCS";
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Method to validate admin credentials
    public boolean validateCredentials(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }
}
