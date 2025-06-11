

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
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardScreenController implements Initializable {

    @FXML
    private Button dashboardButton;

    @FXML
    private Button addMoviesButton;

    @FXML
    private Button editScreeningButton;

    @FXML
    private Button availableMoviesButton;

    @FXML
    private Button customersButton;

    @FXML
    private Button signOutButton;

    @FXML
    private Label totalTicketsLabel;

    @FXML
    private Label totalEarningsLabel;

    @FXML
    private Label totalMoviesLabel;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label adminLabel;

    @FXML
    private Label currentDateLabel;

    @FXML
    private Label currentUserLabel;

    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Dashboard initialized at " + getCurrentDateTime());

        // Update dashboard data
        updateDashboardData();

        // Update date and time
        updateDateTime();
    }

    // Navigation Methods

    @FXML
    private void handleDashboard(ActionEvent event) {
        // Already on dashboard, maybe refresh data
        updateDashboardData();
        updateDateTime();
        System.out.println("Dashboard refreshed at " + getCurrentDateTime());
    }

    @FXML
    private void handleAddMovies(ActionEvent event) {
        navigateToScreen("AddMoviesScreen.fxml", "Add Movies - Cinema Management System");
    }

    @FXML
    private void handleEditScreening(ActionEvent event) {
        navigateToScreen("Screening.fxml", "Edit Screening - Cinema Management System");
    }

    @FXML
    private void handleAvailableMovies(ActionEvent event) {
        navigateToScreen("AvailableMoviesScreen.fxml", "Available Movies - Cinema Management System");
    }

    @FXML
    private void handleCustomers(ActionEvent event) {
        navigateToScreen("AllTheCustomersScreen.fxml", "All Customers - Cinema Management System");
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
            System.out.println("Admin signed out at " + getCurrentDateTime());
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
        // Try to get stage from any available button
        if (dashboardButton != null && dashboardButton.getScene() != null) {
            return (Stage) dashboardButton.getScene().getWindow();
        } else if (signOutButton != null && signOutButton.getScene() != null) {
            return (Stage) signOutButton.getScene().getWindow();
        } else if (addMoviesButton != null && addMoviesButton.getScene() != null) {
            return (Stage) addMoviesButton.getScene().getWindow();
        }
        return null;
    }

    // Update dashboard statistics
    private void updateDashboardData() {
        try {
            // Get real data from your data sources
            int totalTickets = getTotalSoldTickets();
            double totalEarnings = getTotalEarningsToday();
            int totalMovies = getTotalMoviesAvailable();

            // Update labels if they exist in FXML (labels might not have fx:id)
            System.out.println("Dashboard data updated:");
            System.out.println("- Total Tickets: " + totalTickets);
            System.out.println("- Total Earnings: $" + totalEarnings);
            System.out.println("- Total Movies: " + totalMovies);
            System.out.println("- Updated at: " + getCurrentDateTime());

        } catch (Exception e) {
            System.err.println("Error updating dashboard data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Update date and time display
    private void updateDateTime() {
        String currentDateTime = getCurrentDateTime();
        System.out.println("Current Date and Time (UTC): " + currentDateTime);
        System.out.println("Current User: Admin");
        System.out.println("Status: System Online");
    }

    // Data retrieval methods (implement these based on your data sources)

    private int getTotalSoldTickets() {
        // TODO: Implement actual ticket counting from your database/file
        // For now, return a sample value
        return 1500;
    }

    private double getTotalEarningsToday() {
        // TODO: Implement actual earnings calculation from your database/file
        // For now, return a sample value
        return 15000.00;
    }

    private int getTotalMoviesAvailable() {
        // TODO: Implement actual movie counting from your database/file
        // For now, return a sample value
        return 150;
    }

    // Helper method to get current date and time in UTC format
    private String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Method to refresh dashboard (can be called from other screens)
    public void refreshDashboard() {
        updateDashboardData();
        updateDateTime();
        System.out.println("Dashboard manually refreshed at " + getCurrentDateTime());
    }

    // Method to simulate real-time updates (optional)
    public void startRealTimeUpdates() {
        // You can implement a Timer or Timeline here to update data every few seconds
        System.out.println("Real-time updates started at " + getCurrentDateTime());
    }
}
