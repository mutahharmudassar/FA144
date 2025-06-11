

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

public class AdminScreenController implements Initializable {

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
    private Label welcomeLabel;

    @FXML
    private Label adminLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Admin Screen initialized at " + getCurrentDateTime());
        System.out.println("Current User: Admin");
        System.out.println("Status: System Online");
    }

    // Navigation Methods

    @FXML
    private void handleDashboard(ActionEvent event) {
        navigateToScreen("DashboardScreen.fxml", "Dashboard - Cinema Management System");
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

    // Helper method to get current date and time in UTC format
    private String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Method to display current system information
    public void displaySystemInfo() {
        System.out.println("=== System Information ===");
        System.out.println("Current Date and Time (UTC): " + getCurrentDateTime());
        System.out.println("Current User: Admin");
        System.out.println("Application: Cinema Management System");
        System.out.println("Version: 1.0.0");
        System.out.println("Status: Online");
        System.out.println("========================");
    }

    // Method to refresh admin screen
    public void refreshAdminScreen() {
        displaySystemInfo();
        System.out.println("Admin screen refreshed at " + getCurrentDateTime());
    }
}
