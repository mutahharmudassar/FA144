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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

public class AllTheCustomersScreenController implements Initializable {

    // Navigation buttons
    @FXML private Button dashboardButton;
    @FXML private Button addMoviesButton;
    @FXML private Button editScreeningButton;
    @FXML private Button availableMoviesButton;
    @FXML private Button customersButton;
    @FXML private Button signOutButton;

    // Filter controls
    @FXML private TextField searchField;
    @FXML private ComboBox<String> movieFilterComboBox;
    @FXML private DatePicker dateFilterPicker;
    @FXML private Button searchButton;
    @FXML private Button refreshButton;

    // Statistics labels
    @FXML private Label totalCustomersLabel;
    @FXML private Label totalBookingsLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private Label todayBookingsLabel;

    // Table and columns
    @FXML private TableView<CustomerBookingData> customersTable;
    @FXML private TableColumn<CustomerBookingData, Integer> customerIdColumn;
    @FXML private TableColumn<CustomerBookingData, String> customerNameColumn;
    @FXML private TableColumn<CustomerBookingData, String> movieTitleColumn;
    @FXML private TableColumn<CustomerBookingData, LocalDate> screeningDateColumn;
    @FXML private TableColumn<CustomerBookingData, LocalTime> screeningTimeColumn;
    @FXML private TableColumn<CustomerBookingData, String> screenTypeColumn;
    @FXML private TableColumn<CustomerBookingData, Integer> seatsBookedColumn;
    @FXML private TableColumn<CustomerBookingData, Double> totalPriceColumn;
    @FXML private TableColumn<CustomerBookingData, LocalDateTime> bookingDateColumn;

    // Labels
    @FXML private Label welcomeLabel;
    @FXML private Label adminLabel;

    // Data
    private ObservableList<CustomerBookingData> customersList = FXCollections.observableArrayList();
    private ObservableList<CustomerBookingData> filteredList = FXCollections.observableArrayList();

    // File path
    private static final String CUSTOMERS_FILE_PATH = "AllTheCustomers.txt";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("All Customers Screen initialized at " + getCurrentDateTime());
        System.out.println("Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): " + getCurrentDateTime());
        System.out.println("Current User: mutahharmudassar");

        // Initialize components
        initializeTable();
        initializeFilters();

        // FIXED: Load data directly without creating default data
        loadCustomersFromFile();
        updateStatistics();

        System.out.println("AllTheCustomersScreen loaded - Ready to display real customer bookings");
    }

    // Initialize table columns
    private void initializeTable() {
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        movieTitleColumn.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        screeningDateColumn.setCellValueFactory(new PropertyValueFactory<>("screeningDate"));
        screeningTimeColumn.setCellValueFactory(new PropertyValueFactory<>("screeningTime"));
        screenTypeColumn.setCellValueFactory(new PropertyValueFactory<>("screenType"));
        seatsBookedColumn.setCellValueFactory(new PropertyValueFactory<>("seatsBooked"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        bookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDateTime"));

        customersTable.setItems(filteredList);
    }

    // Initialize filter components
    private void initializeFilters() {
        movieFilterComboBox.getItems().add("All Movies");
        movieFilterComboBox.setValue("All Movies");
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
        System.out.println("Already on Customers screen - refreshing at " + getCurrentDateTime());
        handleRefresh(null);
    }

    @FXML
    private void handleSignOut(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setHeaderText("Sign Out Confirmation");
        alert.setContentText("Are you sure you want to sign out?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("mutahharmudassar signed out at " + getCurrentDateTime());
            navigateToScreen("LoginScreen.fxml", "Login - Cinema Management System");
        }
    }

    // Customer Management Methods
    @FXML
    private void handleSearch(ActionEvent event) {
        filterCustomers();
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        System.out.println("Refreshing customer data at " + getCurrentDateTime());
        loadCustomersFromFile();
        updateStatistics();
        clearFilters();
    }

    // Filter customers based on search criteria
    private void filterCustomers() {
        filteredList.clear();

        String searchText = searchField.getText().toLowerCase().trim();
        String selectedMovie = movieFilterComboBox.getValue();
        LocalDate selectedDate = dateFilterPicker.getValue();

        for (CustomerBookingData customer : customersList) {
            boolean matches = true;

            // Name filter
            if (!searchText.isEmpty()) {
                matches = customer.getCustomerName().toLowerCase().contains(searchText);
            }

            // Movie filter
            if (matches && selectedMovie != null && !selectedMovie.equals("All Movies")) {
                matches = customer.getMovieTitle().equals(selectedMovie);
            }

            // Date filter
            if (matches && selectedDate != null) {
                matches = customer.getScreeningDate().equals(selectedDate);
            }

            if (matches) {
                filteredList.add(customer);
            }
        }

        System.out.println("Filtered " + filteredList.size() + " customers from " + customersList.size() + " total");
    }

    // Clear all filters
    private void clearFilters() {
        searchField.clear();
        movieFilterComboBox.setValue("All Movies");
        dateFilterPicker.setValue(null);
        filteredList.setAll(customersList);
    }

    // Update statistics labels
    private void updateStatistics() {
        Set<String> uniqueCustomers = new HashSet<>();
        double totalRevenue = 0.0;
        int todayBookings = 0;
        LocalDate today = LocalDate.now();

        for (CustomerBookingData customer : customersList) {
            uniqueCustomers.add(customer.getCustomerName());
            totalRevenue += customer.getTotalPrice();

            if (customer.getBookingDateTime() != null &&
                    customer.getBookingDateTime().toLocalDate().equals(today)) {
                todayBookings++;
            }
        }

        totalCustomersLabel.setText(String.valueOf(uniqueCustomers.size()));
        totalBookingsLabel.setText(String.valueOf(customersList.size()));
        totalRevenueLabel.setText(String.format("$%.2f", totalRevenue));
        todayBookingsLabel.setText(String.valueOf(todayBookings));

        // Update movie filter options with actual movies from data
        Set<String> movieTitles = new HashSet<>();
        for (CustomerBookingData customer : customersList) {
            movieTitles.add(customer.getMovieTitle());
        }

        String currentSelection = movieFilterComboBox.getValue();
        movieFilterComboBox.getItems().clear();
        movieFilterComboBox.getItems().add("All Movies");
        movieFilterComboBox.getItems().addAll(movieTitles);
        movieFilterComboBox.setValue(currentSelection != null ? currentSelection : "All Movies");

        System.out.println("Statistics updated: " + uniqueCustomers.size() + " customers, " +
                customersList.size() + " bookings, $" + String.format("%.2f", totalRevenue) + " revenue");
    }

    // FIXED: Load customers from file without overwriting existing data
    private void loadCustomersFromFile() {
        customersList.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_FILE_PATH))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!line.trim().isEmpty()) {
                    CustomerBookingData customer = parseCustomerFromLine(line, lineNumber);
                    if (customer != null) {
                        customersList.add(customer);
                    }
                }
            }

            filteredList.setAll(customersList);
            System.out.println("Successfully loaded " + customersList.size() + " customer bookings from file");

            if (customersList.size() > 0) {
                System.out.println("Recent bookings:");
                for (int i = Math.max(0, customersList.size() - 3); i < customersList.size(); i++) {
                    CustomerBookingData recent = customersList.get(i);
                    System.out.println("- ID:" + recent.getCustomerId() + " " + recent.getCustomerName() +
                            " -> " + recent.getMovieTitle() + " on " + recent.getScreeningDate());
                }
            }

        } catch (IOException e) {
            System.out.println("No existing customers file found or error reading file: " + e.getMessage());
            System.out.println("File path: " + CUSTOMERS_FILE_PATH);
        }
    }

    // FIXED: Enhanced parsing method with better error handling and format support
    private CustomerBookingData parseCustomerFromLine(String line, int lineNumber) {
        try {
            String[] parts = line.split("\\|");

            // Debug output
            System.out.println("Parsing line " + lineNumber + ": " + line);
            System.out.println("Parts count: " + parts.length);

            if (parts.length >= 13) {
                CustomerBookingData customer = new CustomerBookingData();

                // Parse each field with error handling
                customer.setCustomerId(Integer.parseInt(parts[0].trim()));
                customer.setCustomerName(parts[1].trim());
                customer.setMovieTitle(parts[2].trim());
                customer.setScreeningDate(LocalDate.parse(parts[3].trim()));
                customer.setScreeningTime(LocalTime.parse(parts[4].trim()));
                customer.setScreenType(parts[5].trim());
                customer.setCinemaHall(parts[6].trim());
                customer.setSeatsBooked(Integer.parseInt(parts[7].trim()));
                customer.setTotalPrice(Double.parseDouble(parts[8].trim()));

                // FIXED: Parse booking date time with flexible format support
                String bookingDateTimeStr = parts[9].trim();
                LocalDateTime bookingDateTime = parseBookingDateTime(bookingDateTimeStr);
                customer.setBookingDateTime(bookingDateTime);

                customer.setCustomerEmail(parts[10].trim());
                customer.setCustomerPhone(parts[11].trim());
                customer.setBookingStatus(parts[12].trim());

                System.out.println("Successfully parsed: " + customer.getCustomerName() +
                        " - " + customer.getMovieTitle());
                return customer;
            } else {
                System.out.println("WARNING: Line " + lineNumber + " has insufficient parts (" +
                        parts.length + " instead of 13)");
            }
        } catch (Exception e) {
            System.err.println("ERROR parsing line " + lineNumber + ": " + e.getMessage());
            System.err.println("Line content: " + line);
            e.printStackTrace();
        }
        return null;
    }

    // FIXED: Flexible date time parsing to handle different formats
    private LocalDateTime parseBookingDateTime(String dateTimeStr) {
        // Try different date time formats
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),           // 2025-06-05 19:58:33
                DateTimeFormatter.ISO_LOCAL_DATE_TIME,                        // 2025-06-05T19:58:33
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),        // 2025-06-05T19:58:33
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),              // 2025-06-05 19:58
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(dateTimeStr, formatter);
            } catch (DateTimeParseException e) {
                // Try next format
            }
        }

        // If all formats fail, return current time and log warning
        System.out.println("WARNING: Could not parse date time '" + dateTimeStr +
                "', using current time");
        return LocalDateTime.now();
    }

    // Utility Methods
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
        if (refreshButton != null && refreshButton.getScene() != null) {
            return (Stage) refreshButton.getScene().getWindow();
        } else if (dashboardButton != null && dashboardButton.getScene() != null) {
            return (Stage) dashboardButton.getScene().getWindow();
        }
        return null;
    }

    private String getCurrentDateTime() {
        return "2025-06-05 19:58:33"; // Updated timestamp
    }
}