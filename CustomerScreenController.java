package com.example.modifiedcinemasystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerScreenController implements Initializable {

    @FXML
    private Button viewScreeningsButton;

    @FXML
    private Button bookTicketButton;

    @FXML
    private Button searchMoviesButton;

    @FXML
    private Button writeReviewsButton;

    @FXML
    private Button signOutButton;

    @FXML
    private TextField customerNameField;
    @FXML
    private TextField customer;

    // Static variable to store current logged-in customer (will be set by LoginScreen)
    private static String currentLoggedInCustomer = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Customer Screen initialized at " + getCurrentDateTime());
        System.out.println("Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): " + getCurrentDateTime());

        // Get the actual logged-in customer name
        String loggedInCustomer = getCurrentLoggedInCustomer();
        System.out.println("Current User's Login: " + loggedInCustomer);

        // Set the logged-in customer name in the text field
        if (customerNameField != null) {
            customerNameField.setText(loggedInCustomer);
            System.out.println("Customer name field set to: " + loggedInCustomer);
            customer.setText(loggedInCustomer);
        }

        System.out.println("Customer screen ready for navigation");
    }

    // Navigation Methods

    @FXML
    private void handleViewScreenings(ActionEvent event) {
        System.out.println("View All Screenings clicked by " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());
        navigateToScreen("ViewScreening.fxml", "View Screening - Cinema Management System");
    }

    @FXML
    private void handleBookTicket(ActionEvent event) {
        System.out.println("Book Ticket clicked by " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());
        navigateToScreen("BookTicketScreen.fxml", "Book Ticket - Cinema Management System");
    }

    @FXML
    private void handleSearchMovies(ActionEvent event) {
        System.out.println("Search Movies clicked by " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());
        navigateToScreen("SearchMovies.fxml", "Search Movies - Cinema Management System");
    }

    @FXML
    private void handleWriteReviews(ActionEvent event) {
        System.out.println("Write Reviews clicked by " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());
        navigateToScreen("WriteReviewsScreen.fxml", "Write Reviews - Cinema Management System");
    }

    @FXML
    private void handleSignOut(ActionEvent event) {
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setHeaderText("Sign Out Confirmation");
        alert.setContentText("Are you sure you want to sign out?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Customer " + getCurrentLoggedInCustomer() + " signed out at " + getCurrentDateTime());

            // Clear the current logged-in customer when signing out
            clearCurrentLoggedInCustomer();

            navigateToScreen("LoginScreen.fxml", "Login - Cinema Management System");
        }
    }

    // Helper method to navigate to different screens
    private void navigateToScreen(String fxmlFile, String title) {
        try {
            System.out.println("Navigating to " + fxmlFile + " at " + getCurrentDateTime());

            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = getCurrentStage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);

        } catch (IOException e) {
            System.err.println("Error loading " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();

            // Show error dialog
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Failed to load screen");
            alert.setContentText("Could not load " + fxmlFile + ". Please check if the file exists.");
            alert.showAndWait();
        }
    }

    // Get current stage
    private Stage getCurrentStage() {
        if (viewScreeningsButton != null && viewScreeningsButton.getScene() != null) {
            return (Stage) viewScreeningsButton.getScene().getWindow();
        } else if (signOutButton != null && signOutButton.getScene() != null) {
            return (Stage) signOutButton.getScene().getWindow();
        } else if (bookTicketButton != null && bookTicketButton.getScene() != null) {
            return (Stage) bookTicketButton.getScene().getWindow();
        }
        return null;
    }

    // Helper method to get current date and time
    private String getCurrentDateTime() {
        return "2025-06-05 17:57:07"; // Using your provided timestamp
    }

    // Method to get customer name from field
    public String getCustomerName() {
        if (customerNameField != null && !customerNameField.getText().trim().isEmpty()) {
            return customerNameField.getText().trim();
        }
        return getCurrentLoggedInCustomer(); // Return current logged-in customer as default
    }

    // Method to set customer name
    public void setCustomerName(String name) {
        if (customerNameField != null) {
            customerNameField.setText(name != null ? name : getCurrentLoggedInCustomer());
        }
    }

    // Method to refresh customer screen
    public void refreshCustomerScreen() {
        System.out.println("Customer screen refreshed at " + getCurrentDateTime());
        System.out.println("Current customer name: " + getCustomerName());
        System.out.println("Logged-in customer: " + getCurrentLoggedInCustomer());

        // Refresh the customer name field with current logged-in user
        if (customerNameField != null) {
            customerNameField.setText(getCurrentLoggedInCustomer());
        }
    }

    // Method to display customer information
    public void displayCustomerInfo() {
        System.out.println("=== Customer Information ===");
        System.out.println("Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): " + getCurrentDateTime());
        System.out.println("Current User's Login: " + getCurrentLoggedInCustomer());
        System.out.println("Customer Name: " + getCustomerName());
        System.out.println("Screen: Customer Portal");
        System.out.println("Status: Online");
        System.out.println("============================");
    }

    // Method to validate customer data before navigation
    private boolean validateCustomerData() {
        String customerName = getCustomerName();
        if (customerName == null || customerName.trim().isEmpty() || customerName.equals("Guest")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Customer Name Required");
            alert.setContentText("Please enter a customer name before proceeding.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    // Enhanced navigation methods with validation

    public void navigateToViewScreenings() {
        if (validateCustomerData()) {
            handleViewScreenings(null);
        }
    }

    public void navigateToBookTicket() {
        if (validateCustomerData()) {
            handleBookTicket(null);
        }
    }

    public void navigateToSearchMovies() {
        if (validateCustomerData()) {
            handleSearchMovies(null);
        }
    }

    public void navigateToWriteReviews() {
        if (validateCustomerData()) {
            handleWriteReviews(null);
        }
    }

    // Static methods to manage logged-in customer across the application

    /**
     * Set the current logged-in customer (to be called from LoginScreen)
     * @param customerName The name of the customer who logged in
     */
    public static void setCurrentLoggedInCustomer(String customerName) {
        currentLoggedInCustomer = customerName;
        System.out.println("Current logged-in customer set to: " + customerName);
    }

    /**
     * Get the current logged-in customer
     * @return The name of the currently logged-in customer
     */
    public static String getCurrentLoggedInCustomer() {
        return currentLoggedInCustomer != null ? currentLoggedInCustomer : "Guest";
    }

    /**
     * Clear the current logged-in customer (called on sign out)
     */
    public static void clearCurrentLoggedInCustomer() {
        System.out.println("Clearing logged-in customer: " + currentLoggedInCustomer);
        currentLoggedInCustomer = null;
    }

    /**
     * Check if a customer is currently logged in
     * @return true if a customer is logged in, false otherwise
     */
    public static boolean isCustomerLoggedIn() {
        return currentLoggedInCustomer != null && !currentLoggedInCustomer.trim().isEmpty();
    }

    /**
     * Update the customer name field with current logged-in user
     * (useful when returning to customer screen from other screens)
     */
    public void updateCustomerNameField() {
        if (customerNameField != null && getCurrentLoggedInCustomer() != null) {
            customerNameField.setText(getCurrentLoggedInCustomer());
            System.out.println("Customer name field updated to: " + getCurrentLoggedInCustomer());
        }
    }

    /**
     * Method to be called when customer logs in from LoginScreen
     * @param customerName The customer's login name (as entered by user)
     */
    public static void onCustomerLogin(String customerName) {
        setCurrentLoggedInCustomer(customerName);
        System.out.println("Customer login recorded: " + customerName + " at " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    /**
     * Method to be called when customer logs out
     */
    public static void onCustomerLogout() {
        String previousCustomer = getCurrentLoggedInCustomer();
        clearCurrentLoggedInCustomer();
        System.out.println("Customer logout recorded: " + previousCustomer + " at " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}