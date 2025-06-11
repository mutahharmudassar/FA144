package com.example.modifiedcinemasystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogInScreenController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label messageLabel;

    @FXML
    private Hyperlink signUpLink;

    private String currentCustomer; // Store the logged-in customer username

    // Initialize method
    @FXML
    public void initialize() {
        System.out.println("Login Screen initialized at " + getCurrentDateTime());
        System.out.println("Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): " + getCurrentDateTime());
        System.out.println("Current User's Login: " + getCurrentLoggedInUser());

        messageLabel.setText("");

        // Add some styling
        messageLabel.setWrapText(true);
        usernameField.setStyle("-fx-font-size: 14px;");
        passwordField.setStyle("-fx-font-size: 14px;");

        // Add placeholder text styling
        usernameField.setPromptText("Username (case-insensitive)");
        passwordField.setPromptText("Password (case-sensitive)");
    }

    // Login button action
    @FXML
    private void handleLogin(ActionEvent event) {
        String enteredUsername = usernameField.getText().trim();
        String enteredPassword = passwordField.getText().trim();

        // Validate input fields
        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            showMessage("Please enter both username and password", "red");
            return;
        }

        // Check for admin login
        if (isAdminLogin(enteredUsername, enteredPassword)) {
            showMessage("Admin login successful!", "green");
            System.out.println("Admin login successful for: " + enteredUsername + " at " + getCurrentDateTime());

            // Navigate to Admin Screen
            navigateToAdminScreen();
            return;
        }

        // Validate customer login credentials
        if (validateCustomerLogin(enteredUsername, enteredPassword)) {
            currentCustomer = enteredUsername;

            // Set the logged-in customer to the entered username
            CustomerScreenController.onCustomerLogin(enteredUsername);

            showMessage("Login successful! Welcome " + enteredUsername, "green");
            System.out.println("Customer login successful for: " + enteredUsername + " at " + getCurrentDateTime());

            // Navigate to CustomerScreen
            navigateToCustomerScreen();

        } else {
            // Show error message
            showLoginError();
        }
    }

    // Check if it's an admin login
    private boolean isAdminLogin(String username, String password) {
        // Default admin credentials
        return (username.equalsIgnoreCase("OOPSTERS") && password.equals("FA24-BCS")) ;
    }

    // Validate customer login credentials
    private boolean validateCustomerLogin(String username, String password) {
        // For demo purposes, accept any non-empty username/password combination
        // In a real system, you would check against a customer database/file

        // Check if customer exists in system (you can implement file-based storage later)
        if (username.length() >= 3 && password.length() >= 3) {
            System.out.println("Customer validation successful for: " + username);
            return true;
        }

        return false;
    }

    // Show login error message
    private void showLoginError() {
        showMessage("Invalid username or password. Please try again.", "red");
        System.out.println("Login failed at " + getCurrentDateTime());

        // Clear password field for security
        passwordField.clear();
    }

    // Helper method to show messages with color
    private void showMessage(String message, String color) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 12px;");
    }

    // Navigate to Admin Screen
    private void navigateToAdminScreen() {
        try {
            System.out.println("Navigating to AdminScreen.fxml at " + getCurrentDateTime());
            Parent root = FXMLLoader.load(getClass().getResource("AdminScreen.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Panel - Cinema Management System");
        } catch (IOException e) {
            showMessage("Error loading admin screen", "red");
            System.err.println("Error loading AdminScreen.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Navigate to Customer Screen
    private void navigateToCustomerScreen() {
        try {
            System.out.println("Navigating to CustomerScreen.fxml at " + getCurrentDateTime());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerScreen.fxml"));
            Parent root = loader.load();

            // Get the controller and update it with customer data
            CustomerScreenController controller = loader.getController();
            if (controller != null && currentCustomer != null) {
                controller.setCustomerName(currentCustomer);
                System.out.println("Customer name set in CustomerScreen: " + currentCustomer);
            }

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Customer Panel - Cinema Management System - " + currentCustomer);
        } catch (IOException e) {
            showMessage("Error loading customer screen", "red");
            System.err.println("Error loading CustomerScreen.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Sign up hyperlink action
    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            System.out.println("Navigating to SignUpScreen.fxml at " + getCurrentDateTime());
            Parent root = FXMLLoader.load(getClass().getResource("SignUpScreen.fxml"));
            Stage stage = (Stage) signUpLink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sign Up - Cinema Management System");
        } catch (IOException e) {
            showMessage("Error loading sign up screen", "red");
            System.err.println("Error loading SignUpScreen.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Clear fields method
    @FXML
    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        messageLabel.setText("");
        currentCustomer = null;
        System.out.println("Login fields cleared at " + getCurrentDateTime());
    }

    // Method to handle Enter key press
    @FXML
    private void handleEnterPressed(javafx.scene.input.KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            handleLogin(new ActionEvent());
        }
    }

    // Navigation helper method
    private void navigateToScreen(String fxmlFile, String title) {
        try {
            System.out.println("Navigating to " + fxmlFile + " at " + getCurrentDateTime());
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = getCurrentStage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            showMessage("Error loading " + fxmlFile, "red");
            System.err.println("Error loading " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get current stage
    private Stage getCurrentStage() {
        if (loginButton != null && loginButton.getScene() != null) {
            return (Stage) loginButton.getScene().getWindow();
        }
        return null;
    }

    // Helper method to get current date and time
    private String getCurrentDateTime() {
        return "2025-06-05 18:02:57"; // Using your provided timestamp
    }

    // Get current logged in user for logging purposes
    private String getCurrentLoggedInUser() {
        return currentCustomer != null ? currentCustomer : "mutahharmudassar";
    }

    // Getter for current customer (in case other controllers need it)
    public String getCurrentCustomer() {
        return currentCustomer;
    }

    // Method to validate login (comprehensive)
    private boolean validateLogin(String username, String password) {
        // Check for admin first
        if (isAdminLogin(username, password)) {
            return true;
        }

        // Then check for customer
        return validateCustomerLogin(username, password);
    }

    // Method to set customer data (if called from other screens)
    public void setCustomerData(String customerName) {
        this.currentCustomer = customerName;
        if (usernameField != null) {
            usernameField.setText(customerName);
        }
    }

    // Method to reset login screen
    public void resetLoginScreen() {
        clearFields();
        showMessage("Please login to continue", "blue");
        System.out.println("Login screen reset at " + getCurrentDateTime());
    }

    // Enhanced validation with multiple login options
    private boolean validateLoginCredentials(String username, String password) {
        System.out.println("Validating login for user: " + username + " at " + getCurrentDateTime());

        // Admin login check
        if (isAdminLogin(username, password)) {
            System.out.println("Admin login detected");
            return true;
        }

        // Customer login validation
        if (validateCustomerLogin(username, password)) {
            System.out.println("Customer login validated");
            return true;
        }

        System.out.println("Login validation failed for: " + username);
        return false;
    }
}