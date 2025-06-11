package com.example.modifiedcinemasystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

public class ViewScreeningController implements Initializable {

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

    @FXML
    private TextField customerNameField;

    // Filter controls
    @FXML
    private ComboBox<String> movieFilterComboBox;

    @FXML
    private DatePicker dateFilterPicker;

    @FXML
    private ComboBox<String> screenTypeFilterComboBox;

    @FXML
    private Button filterButton;

    @FXML
    private Button refreshButton;

    // Action buttons
    @FXML
    private Button bookSelectedScreeningButton;

    @FXML
    private Button backButton;

    // Statistics labels
    @FXML
    private Label totalScreeningsLabel;

    @FXML
    private Label todayScreeningsLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label welcomeUserLabel;

    // Table and columns
    @FXML
    private TableView<ScreeningData> screeningsTable;

    @FXML
    private TableColumn<ScreeningData, String> movieTitleColumn;

    @FXML
    private TableColumn<ScreeningData, LocalDate> screeningDateColumn;

    @FXML
    private TableColumn<ScreeningData, LocalTime> screeningTimeColumn;

    @FXML
    private TableColumn<ScreeningData, String> screenTypeColumn;

    @FXML
    private TableColumn<ScreeningData, String> cinemaHallColumn;

    @FXML
    private TableColumn<ScreeningData, Integer> availableSeatsColumn;

    @FXML
    private TableColumn<ScreeningData, Double> priceColumn;

    @FXML
    private TableColumn<ScreeningData, String> statusColumn;

    // Data
    private ObservableList<ScreeningData> screeningsList = FXCollections.observableArrayList();
    private ObservableList<ScreeningData> filteredList = FXCollections.observableArrayList();
    private ScreeningData selectedScreening = null;

    // File path
    private static final String SCREENINGS_FILE_PATH = "Screenings.txt";

    // Static variable to store selected screening for booking
    private static ScreeningData selectedScreeningForBooking = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("View Screening Screen initialized at " + getCurrentDateTime());
        System.out.println("Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): " + getCurrentDateTime());
        System.out.println("Current User's Login: " + getCurrentLoggedInCustomer());

        // Initialize components
        initializeTable();
        initializeFilters();

        // Set customer name
        String loggedInCustomer = getCurrentLoggedInCustomer();
        if (customerNameField != null) {
            customerNameField.setText(loggedInCustomer);
        }
        if (welcomeUserLabel != null) {
            welcomeUserLabel.setText("Welcome, " + loggedInCustomer);
        }

        // Load data
        loadScreeningsFromFile();
        updateStatistics();

        updateStatus("Screening list loaded - Select a screening to book tickets");
    }

    // Initialize table columns
    private void initializeTable() {
        movieTitleColumn.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        screeningDateColumn.setCellValueFactory(new PropertyValueFactory<>("screeningDate"));
        screeningTimeColumn.setCellValueFactory(new PropertyValueFactory<>("screeningTime"));
        screenTypeColumn.setCellValueFactory(new PropertyValueFactory<>("screenType"));
        cinemaHallColumn.setCellValueFactory(new PropertyValueFactory<>("cinemaHall"));
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerTicket"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        screeningsTable.setItems(filteredList);

        // Add selection listener
        screeningsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedScreening = newSelection;
                updateStatus("Selected: " + newSelection.getMovieTitle() + " on " +
                        newSelection.getScreeningDate() + " at " + newSelection.getScreeningTime());
                System.out.println("Screening selected: " + newSelection.getMovieTitle() + " at " + getCurrentDateTime());
            }
        });
    }

    // Initialize filter components
    private void initializeFilters() {
        movieFilterComboBox.getItems().add("All Movies");
        movieFilterComboBox.setValue("All Movies");

        screenTypeFilterComboBox.getItems().addAll("All Types", "Standard", "3D", "IMAX", "4DX", "VIP", "Dolby Atmos");
        screenTypeFilterComboBox.setValue("All Types");
    }

    // Navigation Methods

    @FXML
    private void handleViewScreenings(ActionEvent event) {
        System.out.println("Already on View Screenings screen - refreshing at " + getCurrentDateTime());
        handleRefresh(null);
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setHeaderText("Sign Out Confirmation");
        alert.setContentText("Are you sure you want to sign out?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Customer " + getCurrentLoggedInCustomer() + " signed out at " + getCurrentDateTime());
            CustomerScreenController.clearCurrentLoggedInCustomer();
            navigateToScreen("LoginScreen.fxml", "Login - Cinema Management System");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        System.out.println("Back to Customer Screen clicked at " + getCurrentDateTime());
        navigateToScreen("CustomerScreen.fxml", "Customer Portal - Cinema Management System");
    }

    // Screening Management Methods

    @FXML
    private void handleFilter(ActionEvent event) {
        filterScreenings();
        updateStatistics();
        updateStatus("Filter applied - " + filteredList.size() + " screenings found");
        System.out.println("Screening filter applied at " + getCurrentDateTime());
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        System.out.println("Refreshing screenings data at " + getCurrentDateTime());
        loadScreeningsFromFile();
        updateStatistics();
        clearFilters();
        updateStatus("Screenings refreshed - " + screeningsList.size() + " total screenings available");
    }

    @FXML
    private void handleBookSelectedScreening(ActionEvent event) {
        if (selectedScreening != null) {
            if (selectedScreening.getAvailableSeats() > 0) {
                // Store selected screening for booking
                selectedScreeningForBooking = selectedScreening;

                System.out.println("Booking screening: " + selectedScreening.getMovieTitle() +
                        " for customer: " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());

                // Navigate to BookTicket screen
                navigateToScreen("BookTicketScreen.fxml", "Book Ticket - Cinema Management System");

            } else {
                showError("Sold Out", "This screening is completely sold out. Please select another screening.");
            }
        } else {
            showError("No Selection", "Please select a screening from the table to book tickets.");
        }
    }

    // Filter screenings based on criteria
    private void filterScreenings() {
        filteredList.clear();

        String selectedMovie = movieFilterComboBox.getValue();
        LocalDate selectedDate = dateFilterPicker.getValue();
        String selectedScreenType = screenTypeFilterComboBox.getValue();

        for (ScreeningData screening : screeningsList) {
            boolean matches = true;

            // Movie filter
            if (selectedMovie != null && !selectedMovie.equals("All Movies")) {
                matches = screening.getMovieTitle().equals(selectedMovie);
            }

            // Date filter
            if (matches && selectedDate != null) {
                matches = screening.getScreeningDate().equals(selectedDate);
            }

            // Screen type filter
            if (matches && selectedScreenType != null && !selectedScreenType.equals("All Types")) {
                matches = screening.getScreenType().equals(selectedScreenType);
            }

            // Only show active screenings with available seats
            if (matches && screening.getStatus().equals("Active") && screening.getAvailableSeats() > 0) {
                filteredList.add(screening);
            }
        }
    }

    // Clear all filters
    private void clearFilters() {
        movieFilterComboBox.setValue("All Movies");
        dateFilterPicker.setValue(null);
        screenTypeFilterComboBox.setValue("All Types");

        // Show only active screenings with available seats
        filteredList.clear();
        for (ScreeningData screening : screeningsList) {
            if (screening.getStatus().equals("Active") && screening.getAvailableSeats() > 0) {
                filteredList.add(screening);
            }
        }
    }

    // Update statistics labels
    private void updateStatistics() {
        int totalScreenings = filteredList.size();
        int todayScreenings = 0;
        LocalDate today = LocalDate.now();

        for (ScreeningData screening : filteredList) {
            if (screening.getScreeningDate().equals(today)) {
                todayScreenings++;
            }
        }

        totalScreeningsLabel.setText(String.valueOf(totalScreenings));
        todayScreeningsLabel.setText(String.valueOf(todayScreenings));

        // Update movie filter options
        Set<String> movieTitles = new HashSet<>();
        for (ScreeningData screening : screeningsList) {
            if (screening.getStatus().equals("Active") && screening.getAvailableSeats() > 0) {
                movieTitles.add(screening.getMovieTitle());
            }
        }

        String currentSelection = movieFilterComboBox.getValue();
        movieFilterComboBox.getItems().clear();
        movieFilterComboBox.getItems().add("All Movies");
        movieFilterComboBox.getItems().addAll(movieTitles);
        movieFilterComboBox.setValue(currentSelection != null ? currentSelection : "All Movies");
    }

    // File Operations

    private void loadScreeningsFromFile() {
        screeningsList.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(SCREENINGS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    ScreeningData screening = parseScreeningFromLine(line);
                    if (screening != null) {
                        screeningsList.add(screening);
                    }
                }
            }

            // Filter to show only active screenings with available seats
            clearFilters();
            System.out.println("Loaded " + screeningsList.size() + " screenings from file");

        } catch (IOException e) {
            System.out.println("No existing screenings file found or error reading file: " + e.getMessage());
            showError("File Error", "Could not load screenings data. Please contact administrator.");
        }
    }

    private ScreeningData parseScreeningFromLine(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length >= 11) {
                ScreeningData screening = new ScreeningData();
                screening.setScreeningId(Integer.parseInt(parts[0]));
                screening.setMovieTitle(parts[1]);
                screening.setScreeningDate(LocalDate.parse(parts[2]));
                screening.setScreeningTime(LocalTime.parse(parts[3]));
                screening.setScreenType(parts[4]);
                screening.setCinemaHall(parts[5]);
                screening.setTotalSeats(Integer.parseInt(parts[6]));
                screening.setAvailableSeats(Integer.parseInt(parts[7]));
                screening.setPricePerTicket(Double.parseDouble(parts[8]));
                screening.setStatus(parts[9]);
                screening.setCreatedDateTime(parts[10]);
                return screening;
            }
        } catch (Exception e) {
            System.err.println("Error parsing screening line: " + e.getMessage());
        }
        return null;
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
        if (bookSelectedScreeningButton != null && bookSelectedScreeningButton.getScene() != null) {
            return (Stage) bookSelectedScreeningButton.getScene().getWindow();
        } else if (refreshButton != null && refreshButton.getScene() != null) {
            return (Stage) refreshButton.getScene().getWindow();
        }
        return null;
    }

    private String getCurrentDateTime() {
        return "2025-06-05 18:32:57"; // Updated timestamp
    }

    private String getCurrentLoggedInCustomer() {
        return CustomerScreenController.getCurrentLoggedInCustomer();
    }

    // Static method to get selected screening for booking
    public static ScreeningData getSelectedScreeningForBooking() {
        return selectedScreeningForBooking;
    }

    // Static method to clear selected screening
    public static void clearSelectedScreeningForBooking() {
        selectedScreeningForBooking = null;
    }
}
