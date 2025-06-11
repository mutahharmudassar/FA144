package com.example.modifiedcinemasystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SignUpScreenController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpButton;

    @FXML
    private Hyperlink loginLink;

    @FXML
    private Label messageLabel;

    // Initialize method
    @FXML
    public void initialize() {
        messageLabel.setText("");
        messageLabel.setWrapText(true);

        // Add placeholder text
        emailField.setPromptText("Enter your email");
        usernameField.setPromptText("Choose a username");
        passwordField.setPromptText("Create a password");

        // Style the fields
        emailField.setStyle("-fx-font-size: 14px;");
        usernameField.setStyle("-fx-font-size: 14px;");
        passwordField.setStyle("-fx-font-size: 14px;");

        System.out.println("SignUpScreen initialized at " + getCurrentDateTime());
    }

    // Sign up button action
    @FXML
    private void handleSignUp(ActionEvent event) {
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        System.out.println("SignUp attempt at " + getCurrentDateTime());
        System.out.println("Attempting to register user: " + username);

        // Validation
        if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showMessage("Please fill all fields", "red");
            return;
        }

        if (!isValidEmail(email)) {
            showMessage("Please enter a valid email address", "red");
            return;
        }

        if (username.length() < 3) {
            showMessage("Username must be at least 3 characters long", "red");
            return;
        }

        if (password.length() < 6) {
            showMessage("Password must be at least 6 characters long", "red");
            return;
        }

        // Check for special characters in username
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            showMessage("Username can only contain letters, numbers, and underscores", "red");
            return;
        }

        // Check if username already exists (case-insensitive)
        if (Customer.usernameExists(username)) {
            showMessage("Username '" + username + "' already exists. Please choose another.", "red");
            return;
        }

        // Check if email already exists
        if (Customer.emailExists(email)) {
            showMessage("Email already exists. Please use another email.", "red");
            return;
        }

        // Create and save new customer
        Customer newCustomer = new Customer(email.toLowerCase(), username, password);
        newCustomer.saveToFile();

        // Success message
        showMessage("Account created successfully! Welcome " + username + "!", "green");

        System.out.println("User registered successfully: " + username + " at " + getCurrentDateTime());

        // Navigate to Customer Screen after successful signup
        navigateToCustomerScreen();
    }

    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") &&
                email.indexOf("@") < email.lastIndexOf(".") &&
                email.indexOf("@") > 0 &&
                email.lastIndexOf(".") < email.length() - 1;
    }

    // Helper method to show messages with color
    private void showMessage(String message, String color) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 12px;");
    }

    // Navigate to Customer Screen
    private void navigateToCustomerScreen() {
        try {
            System.out.println("Navigating to CustomerScreen at " + getCurrentDateTime());

            Parent root = FXMLLoader.load(getClass().getResource("CustomerScreen.fxml"));
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Customer Panel - Cinema Management System");

        } catch (IOException e) {
            showMessage("Error loading customer screen", "red");
            System.err.println("Error navigating to CustomerScreen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Handle login hyperlink click
    @FXML
    private void handleLoginLink(ActionEvent event) {
        navigateToLoginScreen();
    }

    // Navigate to Login Screen
    private void navigateToLoginScreen() {
        try {
            System.out.println("Navigating to LoginScreen at " + getCurrentDateTime());

            Parent root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - Cinema Management System");

        } catch (IOException e) {
            showMessage("Error loading login screen", "red");
            System.err.println("Error navigating to LoginScreen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Clear fields method (optional - can be called if you add a clear button)
    @FXML
    private void clearFields() {
        emailField.clear();
        usernameField.clear();
        passwordField.clear();
        messageLabel.setText("");

        System.out.println("Fields cleared at " + getCurrentDateTime());
    }

    // Helper method to get current date and time in required format
    private String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Method to handle Enter key press on fields
    @FXML
    private void handleEnterPressed(javafx.scene.input.KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            handleSignUp(new ActionEvent());
        }
    }
}