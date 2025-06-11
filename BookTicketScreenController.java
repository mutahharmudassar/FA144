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
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class BookTicketScreenController implements Initializable {

    // All FXML fields (same as before)
    @FXML private Button viewScreeningsButton;
    @FXML private Button bookTicketButton;
    @FXML private Button searchMoviesButton;
    @FXML private Button writeReviewsButton;
    @FXML private Button signOutButton;
    @FXML private TextField customerNameField;
    @FXML private Label welcomeUserLabel;
    @FXML private Label movieTitleLabel;
    @FXML private Label screeningDateLabel;
    @FXML private Label screeningTimeLabel;
    @FXML private Label cinemaHallLabel;
    @FXML private Label screenTypeLabel;
    @FXML private Label availableSeatsLabel;
    @FXML private Label priceLabel;
    @FXML private TextField customerBookingNameField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField emailField;
    @FXML private Spinner<Integer> numberOfTicketsSpinner;
    @FXML private TextField seatNumbersField;
    @FXML private Label totalCostLabel;
    @FXML private RadioButton cashPaymentRadio;
    @FXML private RadioButton bankDepositRadio;
    @FXML private ToggleGroup paymentMethodGroup;
    @FXML private Label paymentStatusLabel;
    @FXML private Button generateSeatsButton;
    @FXML private Button showSeatingButton;
    @FXML private Button calculateTotalButton;
    @FXML private Button processPaymentButton;
    @FXML private Button confirmBookingButton;
    @FXML private Button generateTicketButton;
    @FXML private Button cancelBookingButton;
    @FXML private Button backToScreeningsButton;
    @FXML private Label statusLabel;

    // Data
    private ScreeningData selectedScreening;
    private BookingData currentBooking;
    private PaymentData currentPayment;
    private double totalCost = 0.0;
    private List<String> selectedSeats = new ArrayList<>();
    private static BookingData completedBooking = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Book Ticket Screen initialized at " + getCurrentDateTime());
        System.out.println("Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): " + getCurrentDateTime());
        System.out.println("Current User's Login: " + getCurrentLoggedInCustomer());

        // Initialize spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        numberOfTicketsSpinner.setValueFactory(valueFactory);

        // Set customer name
        String loggedInCustomer = getCurrentLoggedInCustomer();
        if (customerNameField != null) {
            customerNameField.setText(loggedInCustomer);
        }
        if (customerBookingNameField != null) {
            customerBookingNameField.setText(loggedInCustomer);
        }
        if (welcomeUserLabel != null) {
            welcomeUserLabel.setText("Welcome, " + loggedInCustomer);
        }

        // Load selected screening
        selectedScreening = ViewScreeningController.getSelectedScreeningForBooking();
        if (selectedScreening != null) {
            populateScreeningDetails();
            updateStatus("Screening loaded - Fill in your details to proceed");
        } else {
            updateStatus("No screening selected - Please go back and select a screening");
            showError("No Selection", "No screening selected. Please go back and select a screening first.");
        }

        // Set default payment method
        cashPaymentRadio.setSelected(true);

        // Add listeners
        numberOfTicketsSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                calculateTotalCost();
            }
        });
    }

    // Navigation Methods (same as before - keeping them concise)
    @FXML
    private void handleViewScreenings(ActionEvent event) {
        System.out.println("View Screenings clicked by " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());
        navigateToScreen("ViewScreening.fxml", "View Screening - Cinema Management System");
    }

    @FXML
    private void handleBookTicket(ActionEvent event) {
        System.out.println("Already on Book Ticket screen - refreshing at " + getCurrentDateTime());
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setHeaderText("Sign Out Confirmation");
        alert.setContentText("Are you sure you want to sign out? Any unsaved booking will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Customer " + getCurrentLoggedInCustomer() + " signed out at " + getCurrentDateTime());
            CustomerScreenController.clearCurrentLoggedInCustomer();
            navigateToScreen("LoginScreen.fxml", "Login - Cinema Management System");
        }
    }

    @FXML
    private void handleBackToScreenings(ActionEvent event) {
        System.out.println("Back to Screenings clicked at " + getCurrentDateTime());
        navigateToScreen("ViewScreening.fxml", "View Screening - Cinema Management System");
    }

    // Booking Methods
    @FXML
    private void handleGenerateSeats(ActionEvent event) {
        int numberOfTickets = numberOfTicketsSpinner.getValue();
        List<String> autoSeats = generateRandomSeats(numberOfTickets);

        String seatsString = String.join(", ", autoSeats);
        seatNumbersField.setText(seatsString);
        selectedSeats = autoSeats;

        updateStatus("Auto-generated " + numberOfTickets + " seat(s): " + seatsString);
        System.out.println("Auto-generated seats: " + seatsString + " at " + getCurrentDateTime());
    }

    @FXML
    private void handleShowSeating(ActionEvent event) {
        if (selectedScreening != null) {
            showSeatingChart();
            updateStatus("Seating chart displayed for " + selectedScreening.getCinemaHall());
        }
    }

    @FXML
    private void handleCalculateTotal(ActionEvent event) {
        calculateTotalCost();
        updateStatus("Total cost calculated: " + totalCostLabel.getText());
        System.out.println("Total cost calculated: " + totalCost + " at " + getCurrentDateTime());
    }

    @FXML
    private void handleProcessPayment(ActionEvent event) {
        if (validateBookingForm()) {
            processPayment();
        }
    }

    @FXML
    private void handleConfirmBooking(ActionEvent event) {
        if (validateBookingForm() && currentPayment != null && currentPayment.getPaymentStatus().equals("Completed")) {
            confirmBooking();
        } else {
            showError("Payment Required", "Please complete payment before confirming booking.");
        }
    }

    @FXML
    private void handleGenerateTicket(ActionEvent event) {
        if (currentBooking != null && currentBooking.getBookingStatus().equals("Confirmed")) {
            completedBooking = currentBooking;
            System.out.println("Navigating to TicketScreen for booking: " + currentBooking.getBookingId() + " at " + getCurrentDateTime());
            navigateToScreen("TicketScreen.fxml", "Digital Ticket - Cinema Management System");
        } else {
            showError("Booking Required", "Please confirm booking before generating ticket.");
        }
    }

    @FXML
    private void handleCancelBooking(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Booking");
        alert.setHeaderText("Cancel Confirmation");
        alert.setContentText("Are you sure you want to cancel this booking?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Booking cancelled by " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());
            clearBookingForm();
            updateStatus("Booking cancelled");
        }
    }

    // CRITICAL: Updated confirm booking method with robust file handling
    private void confirmBooking() {
        try {
            // Create booking data
            currentBooking = new BookingData();
            currentBooking.setBookingId(generateBookingId());
            currentBooking.setCustomerName(customerBookingNameField.getText().trim());
            currentBooking.setPhoneNumber(phoneNumberField.getText().trim());
            currentBooking.setEmail(emailField.getText().trim());
            currentBooking.setMovieTitle(selectedScreening.getMovieTitle());
            currentBooking.setScreeningDate(selectedScreening.getScreeningDate().toString());
            currentBooking.setScreeningTime(selectedScreening.getScreeningTime().toString());
            currentBooking.setCinemaHall(selectedScreening.getCinemaHall());
            currentBooking.setSeatNumbers(seatNumbersField.getText().trim());
            currentBooking.setNumberOfTickets(numberOfTicketsSpinner.getValue());
            currentBooking.setTotalAmount(totalCost);
            currentBooking.setBookingDate(getCurrentDateTime());
            currentBooking.setBookingStatus("Confirmed");
            currentBooking.setPaymentMethod(currentPayment.getPaymentMethod());
            currentBooking.setTicketCode(generateTicketCode());

            // Save booking to separate bookings file (for detailed tracking)
            saveBookingToFile();

            // CRITICAL: Append to AllTheCustomers.txt for admin visibility
            updateExistingAllTheCustomersFile();

            updateStatus("Booking confirmed successfully - Booking ID: " + currentBooking.getBookingId());
            System.out.println("Booking confirmed: " + currentBooking.getBookingId() + " for " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());

            showSuccess("Booking Confirmed",
                    "Your booking has been confirmed!\n" +
                            "Booking ID: " + currentBooking.getBookingId() + "\n" +
                            "Ticket Code: " + currentBooking.getTicketCode() + "\n\n" +
                            "Click 'Generate Ticket' to view your digital ticket.\n" +
                            "Your booking is now visible to admin in AllTheCustomers screen.");

        } catch (Exception e) {
            System.err.println("CRITICAL ERROR confirming booking: " + e.getMessage());
            e.printStackTrace();
            showError("Booking Error", "Failed to confirm booking: " + e.getMessage());
        }
    }

    // FIXED: Robust method to append to AllTheCustomers.txt
    private void updateExistingAllTheCustomersFile() throws IOException {
        File customerFile = new File("AllTheCustomers.txt");
        int nextId = 20001; // Default starting ID

        // Read existing file to determine next ID
        if (customerFile.exists()) {
            System.out.println("Reading existing AllTheCustomers.txt file to determine next ID at " + getCurrentDateTime());

            try (BufferedReader reader = new BufferedReader(new FileReader(customerFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        try {
                            String[] parts = line.split("\\|");
                            if (parts.length > 0) {
                                int currentId = Integer.parseInt(parts[0].trim());
                                if (currentId >= nextId) {
                                    nextId = currentId + 1;
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Warning: Could not parse ID from line: " + line);
                        }
                    }
                }
            }
            System.out.println("Determined next ID: " + nextId);
        } else {
            System.out.println("AllTheCustomers.txt file does not exist, will be created with starting ID: " + nextId);
        }

        // Create new booking entry in the EXACT format matching your example
        // Format: ID|CustomerName|MovieTitle|ScreeningDate|ScreeningTime|ScreenType|CinemaHall|NumberOfTickets|TotalAmount|BookingDateTime|Email|Phone|Status
        String newBookingEntry = String.format("%d|%s|%s|%s|%s|%s|%s|%d|%.2f|%s|%s|%s|%s",
                nextId,                                          // ID (e.g., 20001, 20002, etc.)
                currentBooking.getCustomerName(),                // CustomerName (e.g., "Ahmed Hassan")
                currentBooking.getMovieTitle(),                  // MovieTitle (e.g., "28 Years Later")
                currentBooking.getScreeningDate(),               // ScreeningDate (e.g., "2025-06-12")
                currentBooking.getScreeningTime(),               // ScreeningTime (e.g., "16:00")
                selectedScreening.getScreenType(),               // ScreenType (e.g., "Standard")
                currentBooking.getCinemaHall(),                  // CinemaHall (e.g., "Hall 1")
                currentBooking.getNumberOfTickets(),             // NumberOfTickets (e.g., 1)
                currentBooking.getTotalAmount(),                 // TotalAmount (e.g., 12.99)
                getCurrentDateTime(),                            // BookingDateTime (e.g., "2025-06-05 19:52:18")
                currentBooking.getEmail(),                       // Email (e.g., "ahmed.hassan@email.com")
                currentBooking.getPhoneNumber(),                 // Phone (e.g., "03100000000")
                currentBooking.getBookingStatus()                // Status (e.g., "Confirmed")
        );

        // Append new entry to existing file (DO NOT overwrite existing content)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(customerFile, true))) { // true = append mode
            writer.write(newBookingEntry);
            writer.newLine();
            writer.flush(); // Ensure data is written immediately

            System.out.println("Successfully APPENDED new booking entry to AllTheCustomers.txt at " + getCurrentDateTime());
            System.out.println("Entry added: " + newBookingEntry);
            System.out.println("Assigned ID: " + nextId);

            // Verify the file was written
            if (customerFile.exists() && customerFile.length() > 0) {
                System.out.println("File verification: AllTheCustomers.txt exists and has content");
                System.out.println("Admin should now be able to see this booking in AllTheCustomersScreen.fxml");
            } else {
                System.out.println("WARNING: File verification failed!");
            }
        }
    }

    // Helper Methods (keeping essential ones)
    private void populateScreeningDetails() {
        if (selectedScreening != null) {
            movieTitleLabel.setText(selectedScreening.getMovieTitle());
            screeningDateLabel.setText(selectedScreening.getScreeningDate().toString());
            screeningTimeLabel.setText(selectedScreening.getScreeningTime().toString());
            cinemaHallLabel.setText(selectedScreening.getCinemaHall());
            screenTypeLabel.setText(selectedScreening.getScreenType());
            availableSeatsLabel.setText(String.valueOf(selectedScreening.getAvailableSeats()));
            priceLabel.setText("$" + String.format("%.2f", selectedScreening.getPricePerTicket()));

            System.out.println("Screening details populated for: " + selectedScreening.getMovieTitle());
        }
    }

    private List<String> generateRandomSeats(int numberOfTickets) {
        List<String> seats = new ArrayList<>();
        Random random = new Random();
        char[] rows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};

        for (int i = 0; i < numberOfTickets; i++) {
            char row = rows[random.nextInt(rows.length)];
            int seatNumber = random.nextInt(20) + 1;
            String seat = row + String.valueOf(seatNumber);

            while (seats.contains(seat)) {
                row = rows[random.nextInt(rows.length)];
                seatNumber = random.nextInt(20) + 1;
                seat = row + String.valueOf(seatNumber);
            }

            seats.add(seat);
        }

        return seats;
    }

    private void showSeatingChart() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Seating Chart");
        alert.setHeaderText(selectedScreening.getCinemaHall() + " - " + selectedScreening.getMovieTitle());

        StringBuilder seatingChart = new StringBuilder();
        seatingChart.append("SCREEN\n");
        seatingChart.append("═══════════════════════════════\n\n");

        char[] rows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        for (char row : rows) {
            seatingChart.append(row).append(" ");
            for (int i = 1; i <= 20; i++) {
                String seat = row + String.valueOf(i);
                if (selectedSeats.contains(seat)) {
                    seatingChart.append("[X]");
                } else {
                    seatingChart.append("[ ]");
                }
                if (i % 5 == 0) seatingChart.append(" ");
            }
            seatingChart.append("\n");
        }

        seatingChart.append("\nLegend: [ ] = Available, [X] = Your Selection");
        seatingChart.append("\nAvailable Seats: ").append(selectedScreening.getAvailableSeats());

        alert.setContentText(seatingChart.toString());
        alert.showAndWait();
    }

    private void calculateTotalCost() {
        if (selectedScreening != null) {
            int tickets = numberOfTicketsSpinner.getValue();
            totalCost = tickets * selectedScreening.getPricePerTicket();
            totalCostLabel.setText("$" + String.format("%.2f", totalCost));
        }
    }

    private boolean validateBookingForm() {
        if (customerBookingNameField.getText().trim().isEmpty()) {
            showError("Validation Error", "Please enter customer name.");
            return false;
        }

        if (phoneNumberField.getText().trim().isEmpty()) {
            showError("Validation Error", "Please enter phone number.");
            return false;
        }

        if (emailField.getText().trim().isEmpty()) {
            showError("Validation Error", "Please enter email address.");
            return false;
        }

        if (seatNumbersField.getText().trim().isEmpty()) {
            showError("Validation Error", "Please select seat numbers.");
            return false;
        }

        if (!cashPaymentRadio.isSelected() && !bankDepositRadio.isSelected()) {
            showError("Validation Error", "Please select a payment method.");
            return false;
        }

        return true;
    }

    private void processPayment() {
        if (bankDepositRadio.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Payment Method Unavailable");
            alert.setHeaderText("Online Payment Currently Unavailable");
            alert.setContentText("Online payment method is currently in process. Please choose another method (Cash Payment).");
            alert.showAndWait();

            cashPaymentRadio.setSelected(true);
            bankDepositRadio.setSelected(false);

            updateStatus("Bank deposit unavailable - Please use cash payment");
            return;
        }

        if (cashPaymentRadio.isSelected()) {
            currentPayment = new PaymentData();
            currentPayment.setPaymentId(generatePaymentId());
            currentPayment.setPaymentMethod("Cash");
            currentPayment.setAmount(totalCost);
            currentPayment.setPaymentDate(getCurrentDateTime());
            currentPayment.setPaymentStatus("Completed");
            currentPayment.setCustomerName(customerBookingNameField.getText().trim());

            paymentStatusLabel.setText("Completed");
            paymentStatusLabel.setStyle("-fx-text-fill: #28a745;");

            updateStatus("Cash payment processed successfully - Amount: $" + String.format("%.2f", totalCost));
            System.out.println("Cash payment processed: $" + totalCost + " for " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());
        }
    }

    private void saveBookingToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Bookings.txt", true))) {
            String bookingLine = String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%d|%.2f|%s|%s|%s|%s",
                    currentBooking.getBookingId(),
                    currentBooking.getCustomerName(),
                    currentBooking.getPhoneNumber(),
                    currentBooking.getEmail(),
                    currentBooking.getMovieTitle(),
                    currentBooking.getScreeningDate(),
                    currentBooking.getScreeningTime(),
                    currentBooking.getCinemaHall(),
                    currentBooking.getSeatNumbers(),
                    currentBooking.getNumberOfTickets(),
                    currentBooking.getTotalAmount(),
                    currentBooking.getBookingDate(),
                    currentBooking.getBookingStatus(),
                    currentBooking.getPaymentMethod(),
                    currentBooking.getTicketCode()
            );
            writer.write(bookingLine);
            writer.newLine();
            System.out.println("Booking saved to Bookings.txt at " + getCurrentDateTime());
        }
    }

    private void clearBookingForm() {
        customerBookingNameField.setText(getCurrentLoggedInCustomer());
        phoneNumberField.clear();
        emailField.clear();
        numberOfTicketsSpinner.getValueFactory().setValue(1);
        seatNumbersField.clear();
        totalCostLabel.setText("$0.00");
        paymentStatusLabel.setText("Pending");
        paymentStatusLabel.setStyle("-fx-text-fill: #f39c12;");
        cashPaymentRadio.setSelected(true);
        selectedSeats.clear();
        currentBooking = null;
        currentPayment = null;
        totalCost = 0.0;
    }

    // Utility Methods
    private String generateBookingId() {
        return "BKG" + System.currentTimeMillis();
    }

    private String generatePaymentId() {
        return "PAY" + System.currentTimeMillis();
    }

    private String generateTicketCode() {
        Random random = new Random();
        return "TKT" + (random.nextInt(900000) + 100000);
    }

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

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
        if (confirmBookingButton != null && confirmBookingButton.getScene() != null) {
            return (Stage) confirmBookingButton.getScene().getWindow();
        }
        return null;
    }

    private String getCurrentDateTime() {
        return "2025-06-05 19:52:18"; // Updated timestamp
    }

    private String getCurrentLoggedInCustomer() {
        return CustomerScreenController.getCurrentLoggedInCustomer();
    }

    // Static methods for TicketScreen
    public static BookingData getCompletedBooking() {
        return completedBooking;
    }

    public static void clearCompletedBooking() {
        completedBooking = null;
    }
}