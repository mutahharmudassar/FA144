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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class TicketScreenController implements Initializable {

    // Navigation buttons
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

    // Labels
    @FXML
    private Label customerNameLabel;

    @FXML
    private Label welcomeUserLabel;

    @FXML
    private Label ticketCodeHeaderLabel;

    @FXML
    private Label statusLabel;

    // Ticket content
    @FXML
    private TextArea ticketContentArea;

    // Action buttons
    @FXML
    private Button takeScreenshotButton;

    @FXML
    private Button printTicketButton;

    @FXML
    private Button bookAnotherButton;

    @FXML
    private Button backToHomeButton;

    // Data
    private BookingData currentBooking;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Ticket Screen initialized at " + getCurrentDateTime());
        System.out.println("Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): " + getCurrentDateTime());
        System.out.println("Current User's Login: " + getCurrentLoggedInCustomer());

        // Set customer name
        String loggedInCustomer = getCurrentLoggedInCustomer();
        if (customerNameLabel != null) {
            customerNameLabel.setText(loggedInCustomer);
        }
        if (welcomeUserLabel != null) {
            welcomeUserLabel.setText("Welcome, " + loggedInCustomer);
        }

        // Load booking data
        currentBooking = BookTicketScreenController.getCompletedBooking();
        if (currentBooking != null) {
            populateTicketContent();
            updateStatus("Digital ticket generated successfully - Ready for screenshot");
            System.out.println("Ticket loaded for booking: " + currentBooking.getBookingId() + " at " + getCurrentDateTime());
        } else {
            updateStatus("No booking data found - Please complete a booking first");
            showError("No Booking Data", "No booking information found. Please complete a booking first.");
        }
    }

    // Navigation Methods

    @FXML
    private void handleViewScreenings(ActionEvent event) {
        System.out.println("View Screenings clicked by " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());
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
        navigateToScreen("SearchMoviesScreen.fxml", "Search Movies - Cinema Management System");
    }

    @FXML
    private void handleWriteReviews(ActionEvent event) {
        System.out.println("Write Reviews clicked by " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());
        navigateToScreen("WriteReviewsScreen.fxml", "Write Reviews - Cinema Management System");
    }

    @FXML
    private void handleSignOut(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setHeaderText("Sign Out Confirmation");
        alert.setContentText("Are you sure you want to sign out?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Customer " + getCurrentLoggedInCustomer() + " signed out at " + getCurrentDateTime());
            CustomerScreenController.clearCurrentLoggedInCustomer();
            BookTicketScreenController.clearCompletedBooking();
            navigateToScreen("LoginScreen.fxml", "Login - Cinema Management System");
        }
    }

    // Ticket Methods

    @FXML
    private void handleTakeScreenshot(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Screenshot Instructions");
        alert.setHeaderText("Take Screenshot of Digital Ticket");
        alert.setContentText("Please take a screenshot of this window using:\n\n" +
                "• Windows: Press 'Windows Key + Shift + S' or 'Print Screen'\n" +
                "• Mac: Press 'Cmd + Shift + 4'\n" +
                "• Linux: Press 'Print Screen' or use screenshot tool\n\n" +
                "Then show the screenshot at the cinema ticket counter to collect your original ticket.");
        alert.showAndWait();

        updateStatus("Screenshot instructions provided - Please take screenshot and show at cinema");
        System.out.println("Screenshot instructions shown to " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());
    }

    @FXML
    private void handlePrintTicket(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Print Instructions");
        alert.setHeaderText("Print Digital Ticket");
        alert.setContentText("To print this ticket:\n\n" +
                "1. Take a screenshot of this window\n" +
                "2. Open the screenshot in an image viewer or document editor\n" +
                "3. Print the screenshot\n" +
                "4. Bring the printed ticket to the cinema\n\n" +
                "Note: Digital ticket on phone is also accepted at most cinemas.");
        alert.showAndWait();

        updateStatus("Print instructions provided - Screenshot and print for backup");
        System.out.println("Print instructions shown to " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());
    }

    @FXML
    private void handleBookAnother(ActionEvent event) {
        System.out.println("Book Another Ticket clicked by " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());
        navigateToScreen("ViewScreening.fxml", "View Screening - Cinema Management System");
    }

    @FXML
    private void handleBackToHome(ActionEvent event) {
        System.out.println("Back to Home clicked by " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());
        BookTicketScreenController.clearCompletedBooking();
        navigateToScreen("CustomerScreen.fxml", "Customer Portal - Cinema Management System");
    }

    // Helper Methods

    private void populateTicketContent() {
        if (currentBooking != null) {
            ticketCodeHeaderLabel.setText(currentBooking.getTicketCode());

            StringBuilder ticket = new StringBuilder();
            ticket.append("═══════════════════════════════════════════════════════════════\n");
            ticket.append("                    🎬 CINEMA DIGITAL TICKET 🎬\n");
            ticket.append("                   OOPsters Cinema Management\n");
            ticket.append("═══════════════════════════════════════════════════════════════\n\n");

            ticket.append("🎫 TICKET INFORMATION:\n");
            ticket.append("───────────────────────────────────────────────────────────────\n");
            ticket.append("Ticket Code:      ").append(currentBooking.getTicketCode()).append("\n");
            ticket.append("Booking ID:       ").append(currentBooking.getBookingId()).append("\n");
            ticket.append("Booking Date:     ").append(currentBooking.getBookingDate()).append("\n");
            ticket.append("Status:           ").append(currentBooking.getBookingStatus()).append("\n\n");

            ticket.append("👤 CUSTOMER INFORMATION:\n");
            ticket.append("───────────────────────────────────────────────────────────────\n");
            ticket.append("Name:             ").append(currentBooking.getCustomerName()).append("\n");
            ticket.append("Phone:            ").append(currentBooking.getPhoneNumber()).append("\n");
            ticket.append("Email:            ").append(currentBooking.getEmail()).append("\n\n");

            ticket.append("🎬 MOVIE & SCREENING DETAILS:\n");
            ticket.append("───────────────────────────────────────────────────────────────\n");
            ticket.append("Movie Title:      ").append(currentBooking.getMovieTitle()).append("\n");
            ticket.append("Screening Date:   ").append(currentBooking.getScreeningDate()).append("\n");
            ticket.append("Screening Time:   ").append(currentBooking.getScreeningTime()).append("\n");
            ticket.append("Cinema Hall:      ").append(currentBooking.getCinemaHall()).append("\n");
            ticket.append("Seat Numbers:     ").append(currentBooking.getSeatNumbers()).append("\n");
            ticket.append("Number of Tickets: ").append(currentBooking.getNumberOfTickets()).append("\n\n");

            ticket.append("💰 PAYMENT INFORMATION:\n");
            ticket.append("───────────────────────────────────────────────────────────────\n");
            ticket.append("Total Amount:     $").append(String.format("%.2f", currentBooking.getTotalAmount())).append("\n");
            ticket.append("Payment Method:   ").append(currentBooking.getPaymentMethod()).append("\n");
            ticket.append("Payment Status:   Completed\n\n");

            ticket.append("📱 QR CODE AREA (Simulated):\n");
            ticket.append("───────────────────────────────────────────────────────────────\n");
            ticket.append("█████ █   █ ██████ █   █ ██████\n");
            ticket.append("█   █ ██ ██ █    █ ██ ██ █    █\n");
            ticket.append("█   █ █ █ █ ██████ █ █ █ ██████\n");
            ticket.append("█████ █   █ █    █ █   █ █    █\n");
            ticket.append("█████ █   █ ██████ █   █ ██████\n\n");

            ticket.append("⚠️  IMPORTANT NOTICE:\n");
            ticket.append("───────────────────────────────────────────────────────────────\n");
            ticket.append("• This is a DIGITAL TICKET - Take a screenshot\n");
            ticket.append("• Show this screenshot at the cinema ticket counter\n");
            ticket.append("• Collect your original ticket at the cinema entrance\n");
            ticket.append("• Arrive at least 15 minutes before show time\n");
            ticket.append("• Ticket is non-refundable after show time\n");
            ticket.append("• Lost tickets cannot be replaced\n\n");

            ticket.append("═══════════════════════════════════════════════════════════════\n");
            ticket.append("              Thank you for choosing OOPsters Cinema!\n");
            ticket.append("                     Enjoy your movie! 🍿\n");
            ticket.append("═══════════════════════════════════════════════════════════════\n");
            ticket.append("\nGenerated at: ").append(getCurrentDateTime());

            ticketContentArea.setText(ticket.toString());

            System.out.println("Digital ticket content populated for: " + currentBooking.getTicketCode());
        }
    }

    // Utility Methods

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

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

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Failed to load screen");
            alert.setContentText("Could not load " + fxmlFile + ". Please check if the file exists.");
            alert.showAndWait();
        }
    }

    private Stage getCurrentStage() {
        if (takeScreenshotButton != null && takeScreenshotButton.getScene() != null) {
            return (Stage) takeScreenshotButton.getScene().getWindow();
        }
        return null;
    }

    private String getCurrentDateTime() {
        return "2025-06-05 19:08:09"; // Updated timestamp
    }

    private String getCurrentLoggedInCustomer() {
        return CustomerScreenController.getCurrentLoggedInCustomer();
    }
}