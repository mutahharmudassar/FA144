package com.example.modifiedcinemasystem;

import com.example.modifiedcinemasystem.AvailableMoviesScreenController;
import com.example.modifiedcinemasystem.MovieData;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ScreeningController implements Initializable {

    // Navigation buttons
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

    // Form controls
    @FXML
    private ComboBox<String> movieComboBox;

    @FXML
    private DatePicker screeningDatePicker;

    @FXML
    private TextField screeningTimeField;

    @FXML
    private ComboBox<String> screenTypeComboBox;

    @FXML
    private ComboBox<String> cinemaHallComboBox;

    @FXML
    private Spinner<Integer> totalSeatsSpinner;

    @FXML
    private TextField availableSeatsField;

    @FXML
    private TextField priceField;

    // Action buttons
    @FXML
    private Button addScreeningButton;

    @FXML
    private Button updateScreeningButton;

    @FXML
    private Button deleteScreeningButton;

    @FXML
    private Button clearFormButton;

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
    private TableColumn<ScreeningData, Integer> totalSeatsColumn;

    @FXML
    private TableColumn<ScreeningData, Integer> availableSeatsColumn;

    @FXML
    private TableColumn<ScreeningData, Double> priceColumn;

    @FXML
    private TableColumn<ScreeningData, String> statusColumn;

    // Labels
    @FXML
    private Label statusLabel;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label adminLabel;

    // Data
    private ObservableList<ScreeningData> screeningsList = FXCollections.observableArrayList();
    private ScreeningData selectedScreening = null;

    // File path
    private static final String SCREENINGS_FILE_PATH = "Screenings.txt";

    // Flag to track if default screenings have been added
    private static boolean defaultScreeningsAdded = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Screening Screen initialized at " + getCurrentDateTime());
        System.out.println("Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): " + getCurrentDateTime());
        System.out.println("Current User: Admin"); // Using generic term as requested

        // Initialize components
        initializeComboBoxes();
        initializeSpinner();
        initializeTable();

        // Load data
        loadAvailableMovies();
        loadScreeningsFromFile();

        // Add default screenings if file is empty or doesn't exist
        if (!defaultScreeningsAdded && screeningsList.isEmpty()) {
            addDefaultScreenings();
            defaultScreeningsAdded = true;
        }

        updateStatus("Screening management ready with " + screeningsList.size() + " screenings");
    }

    // Add default screenings from June 12th onwards
    private void addDefaultScreenings() {
        System.out.println("Adding default screenings from June 12th onwards...");

        try {
            // Create default screenings
            List<ScreeningData> defaultScreenings = createDefaultScreenings();

            // Add to list
            screeningsList.addAll(defaultScreenings);

            // Save to file
            saveScreeningsToFile();

            System.out.println("Added " + defaultScreenings.size() + " default screenings");
            updateStatus("Default screenings loaded: " + defaultScreenings.size() + " screenings available");

        } catch (Exception e) {
            System.err.println("Error adding default screenings: " + e.getMessage());
        }
    }

    // Create default screening data
    private List<ScreeningData> createDefaultScreenings() {
        List<ScreeningData> defaultScreenings = new ArrayList<>();

        // Screening 1: 28 Years Later - June 12th
        ScreeningData screening1 = new ScreeningData();
        screening1.setScreeningId(10001);
        screening1.setMovieTitle("28 Years Later");
        screening1.setScreeningDate(LocalDate.of(2025, 6, 12));
        screening1.setScreeningTime(LocalTime.of(19, 30));
        screening1.setScreenType("Standard");
        screening1.setCinemaHall("Hall 1");
        screening1.setTotalSeats(150);
        screening1.setAvailableSeats(125);
        screening1.setPricePerTicket(12.99);
        screening1.setStatus("Active");
        screening1.setCreatedDateTime("2025-06-05 16:28:45");
        defaultScreenings.add(screening1);

        // Screening 2: Mission Impossible - June 12th
        ScreeningData screening2 = new ScreeningData();
        screening2.setScreeningId(10002);
        screening2.setMovieTitle("Mission Impossible");
        screening2.setScreeningDate(LocalDate.of(2025, 6, 12));
        screening2.setScreeningTime(LocalTime.of(21, 45));
        screening2.setScreenType("IMAX");
        screening2.setCinemaHall("IMAX Hall");
        screening2.setTotalSeats(200);
        screening2.setAvailableSeats(180);
        screening2.setPricePerTicket(18.99);
        screening2.setStatus("Active");
        screening2.setCreatedDateTime("2025-06-05 16:28:45");
        defaultScreenings.add(screening2);

        // Screening 3: Avengers Endgame - June 13th
        ScreeningData screening3 = new ScreeningData();
        screening3.setScreeningId(10003);
        screening3.setMovieTitle("Avengers Endgame");
        screening3.setScreeningDate(LocalDate.of(2025, 6, 13));
        screening3.setScreeningTime(LocalTime.of(18, 0));
        screening3.setScreenType("3D");
        screening3.setCinemaHall("Hall 2");
        screening3.setTotalSeats(180);
        screening3.setAvailableSeats(145);
        screening3.setPricePerTicket(15.99);
        screening3.setStatus("Active");
        screening3.setCreatedDateTime("2025-06-05 16:28:45");
        defaultScreenings.add(screening3);

        // Screening 4: Mickey 17 - June 14th
        ScreeningData screening4 = new ScreeningData();
        screening4.setScreeningId(10004);
        screening4.setMovieTitle("Mickey 17");
        screening4.setScreeningDate(LocalDate.of(2025, 6, 14));
        screening4.setScreeningTime(LocalTime.of(16, 30));
        screening4.setScreenType("Standard");
        screening4.setCinemaHall("Hall 3");
        screening4.setTotalSeats(120);
        screening4.setAvailableSeats(95);
        screening4.setPricePerTicket(11.99);
        screening4.setStatus("Active");
        screening4.setCreatedDateTime("2025-06-05 16:28:45");
        defaultScreenings.add(screening4);

        // Screening 5: Warfare - June 15th
        ScreeningData screening5 = new ScreeningData();
        screening5.setScreeningId(10005);
        screening5.setMovieTitle("Warfare");
        screening5.setScreeningDate(LocalDate.of(2025, 6, 15));
        screening5.setScreeningTime(LocalTime.of(20, 15));
        screening5.setScreenType("Dolby Atmos");
        screening5.setCinemaHall("Premium Hall");
        screening5.setTotalSeats(100);
        screening5.setAvailableSeats(78);
        screening5.setPricePerTicket(16.99);
        screening5.setStatus("Active");
        screening5.setCreatedDateTime("2025-06-05 16:28:45");
        defaultScreenings.add(screening5);

        // Screening 6: Sinners - June 16th
        ScreeningData screening6 = new ScreeningData();
        screening6.setScreeningId(10006);
        screening6.setMovieTitle("Sinners");
        screening6.setScreeningDate(LocalDate.of(2025, 6, 16));
        screening6.setScreeningTime(LocalTime.of(22, 0));
        screening6.setScreenType("VIP");
        screening6.setCinemaHall("VIP Hall");
        screening6.setTotalSeats(50);
        screening6.setAvailableSeats(32);
        screening6.setPricePerTicket(24.99);
        screening6.setStatus("Active");
        screening6.setCreatedDateTime("2025-06-05 16:28:45");
        defaultScreenings.add(screening6);

        // Screening 7: Final Destination Bloodlines - June 17th
        ScreeningData screening7 = new ScreeningData();
        screening7.setScreeningId(10007);
        screening7.setMovieTitle("Final Destination Bloodlines");
        screening7.setScreeningDate(LocalDate.of(2025, 6, 17));
        screening7.setScreeningTime(LocalTime.of(19, 0));
        screening7.setScreenType("4DX");
        screening7.setCinemaHall("Hall 4");
        screening7.setTotalSeats(80);
        screening7.setAvailableSeats(65);
        screening7.setPricePerTicket(22.99);
        screening7.setStatus("Active");
        screening7.setCreatedDateTime("2025-06-05 16:28:45");
        defaultScreenings.add(screening7);

        // Screening 8: 28 Years Later - June 18th (Second showing)
        ScreeningData screening8 = new ScreeningData();
        screening8.setScreeningId(10008);
        screening8.setMovieTitle("28 Years Later");
        screening8.setScreeningDate(LocalDate.of(2025, 6, 18));
        screening8.setScreeningTime(LocalTime.of(17, 45));
        screening8.setScreenType("3D");
        screening8.setCinemaHall("Hall 5");
        screening8.setTotalSeats(160);
        screening8.setAvailableSeats(140);
        screening8.setPricePerTicket(14.99);
        screening8.setStatus("Active");
        screening8.setCreatedDateTime("2025-06-05 16:28:45");
        defaultScreenings.add(screening8);

        System.out.println("Created " + defaultScreenings.size() + " default screenings");
        for (ScreeningData screening : defaultScreenings) {
            System.out.println("- " + screening.getMovieTitle() + " on " + screening.getScreeningDate() +
                    " at " + screening.getScreeningTime() + " (" + screening.getScreenType() + ")");
        }

        return defaultScreenings;
    }

    // Initialize combo boxes with default values
    private void initializeComboBoxes() {
        // Screen types
        screenTypeComboBox.getItems().addAll(
                "Standard", "3D", "IMAX", "4DX", "VIP", "Dolby Atmos"
        );
        screenTypeComboBox.setValue("Standard");

        // Cinema halls
        cinemaHallComboBox.getItems().addAll(
                "Hall 1", "Hall 2", "Hall 3", "Hall 4", "Hall 5",
                "IMAX Hall", "VIP Hall", "Premium Hall"
        );
        cinemaHallComboBox.setValue("Hall 1");
    }

    // Initialize spinner for seat count
    private void initializeSpinner() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 500, 100, 10);
        totalSeatsSpinner.setValueFactory(valueFactory);

        // Listen for changes to update available seats
        totalSeatsSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (selectedScreening == null) {
                availableSeatsField.setText(String.valueOf(newValue));
            }
        });
    }

    // Initialize table columns
    private void initializeTable() {
        movieTitleColumn.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
        screeningDateColumn.setCellValueFactory(new PropertyValueFactory<>("screeningDate"));
        screeningTimeColumn.setCellValueFactory(new PropertyValueFactory<>("screeningTime"));
        screenTypeColumn.setCellValueFactory(new PropertyValueFactory<>("screenType"));
        cinemaHallColumn.setCellValueFactory(new PropertyValueFactory<>("cinemaHall"));
        totalSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("totalSeats"));
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerTicket"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        screeningsTable.setItems(screeningsList);

        // Add selection listener
        screeningsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedScreening = newSelection;
                populateFormWithScreening(newSelection);
            }
        });
    }

    // Load available movies from MovieData
    private void loadAvailableMovies() {
        try {
            List<MovieData> movies = AvailableMoviesScreenController.getAllMovies();
            movieComboBox.getItems().clear();

            // Add default movie titles if no movies are loaded from MovieData
            if (movies.isEmpty()) {
                movieComboBox.getItems().addAll(
                        "28 Years Later",
                        "Final Destination Bloodlines",
                        "Mission Impossible",
                        "Sinners",
                        "Warfare",
                        "Mickey 17",
                        "Avengers Endgame"
                );
                System.out.println("Loaded default movie titles for screenings");
            } else {
                for (MovieData movie : movies) {
                    movieComboBox.getItems().add(movie.getTitle());
                }
                System.out.println("Loaded " + movies.size() + " available movies from MovieData");
            }

        } catch (Exception e) {
            System.err.println("Error loading movies: " + e.getMessage());
            // Add default movies as fallback
            movieComboBox.getItems().addAll(
                    "28 Years Later",
                    "Final Destination Bloodlines",
                    "Mission Impossible",
                    "Sinners",
                    "Warfare",
                    "Mickey 17",
                    "Avengers Endgame"
            );
            updateStatus("Loaded default movies for screening selection");
        }
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
        System.out.println("Already on Screening screen - refreshing at " + getCurrentDateTime());
        handleClearForm(null);
        loadAvailableMovies();
        loadScreeningsFromFile();
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

    // Screening Management Methods

    @FXML
    private void handleAddScreening(ActionEvent event) {
        if (validateScreeningData()) {
            try {
                ScreeningData newScreening = createScreeningFromForm();

                // Check for conflicts
                if (hasScheduleConflict(newScreening)) {
                    showError("Schedule Conflict", "This time slot is already occupied in the selected hall.");
                    return;
                }

                // Add to list and save
                screeningsList.add(newScreening);
                saveScreeningsToFile();

                updateStatus("Screening added successfully for " + newScreening.getMovieTitle());
                System.out.println("New screening added: " + newScreening);

                showSuccess("Screening Added", "Screening has been successfully added to the system.");
                handleClearForm(null);

            } catch (Exception e) {
                System.err.println("Error adding screening: " + e.getMessage());
                showError("Error", "Failed to add screening: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleUpdateScreening(ActionEvent event) {
        if (selectedScreening != null && validateScreeningData()) {
            try {
                ScreeningData updatedScreening = createScreeningFromForm();
                updatedScreening.setScreeningId(selectedScreening.getScreeningId());
                updatedScreening.setCreatedDateTime(selectedScreening.getCreatedDateTime());

                // Update in list
                int index = screeningsList.indexOf(selectedScreening);
                screeningsList.set(index, updatedScreening);
                saveScreeningsToFile();

                updateStatus("Screening updated successfully");
                System.out.println("Screening updated: " + updatedScreening);

                showSuccess("Screening Updated", "Screening has been successfully updated.");
                handleClearForm(null);

            } catch (Exception e) {
                System.err.println("Error updating screening: " + e.getMessage());
                showError("Error", "Failed to update screening: " + e.getMessage());
            }
        } else {
            showError("No Selection", "Please select a screening to update.");
        }
    }

    @FXML
    private void handleDeleteScreening(ActionEvent event) {
        if (selectedScreening != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Screening");
            alert.setHeaderText("Delete Confirmation");
            alert.setContentText("Are you sure you want to delete this screening?\n" +
                    "Movie: " + selectedScreening.getMovieTitle() + "\n" +
                    "Date: " + selectedScreening.getScreeningDate() + "\n" +
                    "Time: " + selectedScreening.getScreeningTime());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    screeningsList.remove(selectedScreening);
                    saveScreeningsToFile();

                    updateStatus("Screening deleted successfully");
                    System.out.println("Screening deleted: " + selectedScreening.getMovieTitle());

                    handleClearForm(null);

                } catch (Exception e) {
                    System.err.println("Error deleting screening: " + e.getMessage());
                    showError("Error", "Failed to delete screening: " + e.getMessage());
                }
            }
        } else {
            showError("No Selection", "Please select a screening to delete.");
        }
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        movieComboBox.setValue(null);
        screeningDatePicker.setValue(null);
        screeningTimeField.clear();
        screenTypeComboBox.setValue("Standard");
        cinemaHallComboBox.setValue("Hall 1");
        totalSeatsSpinner.getValueFactory().setValue(100);
        availableSeatsField.setText("100");
        priceField.clear();

        selectedScreening = null;
        screeningsTable.getSelectionModel().clearSelection();

        updateStatus("Form cleared - Ready for new screening");
        System.out.println("Screening form cleared at " + getCurrentDateTime());
    }

    // Helper Methods

    private boolean validateScreeningData() {
        if (movieComboBox.getValue() == null) {
            showError("Validation Error", "Please select a movie.");
            return false;
        }

        if (screeningDatePicker.getValue() == null) {
            showError("Validation Error", "Please select a screening date.");
            return false;
        }

        if (screeningDatePicker.getValue().isBefore(LocalDate.now())) {
            showError("Validation Error", "Screening date cannot be in the past.");
            return false;
        }

        if (screeningTimeField.getText().trim().isEmpty()) {
            showError("Validation Error", "Please enter screening time.");
            return false;
        }

        try {
            LocalTime.parse(screeningTimeField.getText().trim());
        } catch (DateTimeParseException e) {
            showError("Validation Error", "Invalid time format. Use HH:MM (e.g., 19:30).");
            return false;
        }

        if (priceField.getText().trim().isEmpty()) {
            showError("Validation Error", "Please enter ticket price.");
            return false;
        }

        try {
            double price = Double.parseDouble(priceField.getText().trim());
            if (price <= 0) {
                showError("Validation Error", "Ticket price must be greater than 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Validation Error", "Invalid price format.");
            return false;
        }

        return true;
    }

    private ScreeningData createScreeningFromForm() {
        ScreeningData screening = new ScreeningData();
        screening.setMovieTitle(movieComboBox.getValue());
        screening.setScreeningDate(screeningDatePicker.getValue());
        screening.setScreeningTime(LocalTime.parse(screeningTimeField.getText().trim()));
        screening.setScreenType(screenTypeComboBox.getValue());
        screening.setCinemaHall(cinemaHallComboBox.getValue());
        screening.setTotalSeats(totalSeatsSpinner.getValue());
        screening.setAvailableSeats(Integer.parseInt(availableSeatsField.getText()));
        screening.setPricePerTicket(Double.parseDouble(priceField.getText().trim()));
        screening.setCreatedDateTime(getCurrentDateTime());

        return screening;
    }

    private void populateFormWithScreening(ScreeningData screening) {
        movieComboBox.setValue(screening.getMovieTitle());
        screeningDatePicker.setValue(screening.getScreeningDate());
        screeningTimeField.setText(screening.getScreeningTime().toString());
        screenTypeComboBox.setValue(screening.getScreenType());
        cinemaHallComboBox.setValue(screening.getCinemaHall());
        totalSeatsSpinner.getValueFactory().setValue(screening.getTotalSeats());
        availableSeatsField.setText(String.valueOf(screening.getAvailableSeats()));
        priceField.setText(String.valueOf(screening.getPricePerTicket()));
    }

    private boolean hasScheduleConflict(ScreeningData newScreening) {
        for (ScreeningData existing : screeningsList) {
            if (existing.getCinemaHall().equals(newScreening.getCinemaHall()) &&
                    existing.getScreeningDate().equals(newScreening.getScreeningDate()) &&
                    existing.getScreeningTime().equals(newScreening.getScreeningTime()) &&
                    (selectedScreening == null || !existing.equals(selectedScreening))) {
                return true;
            }
        }
        return false;
    }

    // File Operations

    private void saveScreeningsToFile() throws IOException {
        File screeningsFile = new File(SCREENINGS_FILE_PATH);
        if (!screeningsFile.exists()) {
            screeningsFile.createNewFile();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCREENINGS_FILE_PATH))) {
            for (ScreeningData screening : screeningsList) {
                String line = String.format("%d|%s|%s|%s|%s|%s|%d|%d|%.2f|%s|%s",
                        screening.getScreeningId(),
                        screening.getMovieTitle(),
                        screening.getScreeningDate().toString(),
                        screening.getScreeningTime().toString(),
                        screening.getScreenType(),
                        screening.getCinemaHall(),
                        screening.getTotalSeats(),
                        screening.getAvailableSeats(),
                        screening.getPricePerTicket(),
                        screening.getStatus(),
                        screening.getCreatedDateTime()
                );
                writer.write(line);
                writer.newLine();
            }
        }

        System.out.println("Screenings saved to file: " + SCREENINGS_FILE_PATH);
    }

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
            System.out.println("Loaded " + screeningsList.size() + " screenings from file");

        } catch (IOException e) {
            System.out.println("No existing screenings file found or error reading file: " + e.getMessage());
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
        if (addScreeningButton != null && addScreeningButton.getScene() != null) {
            return (Stage) addScreeningButton.getScene().getWindow();
        } else if (dashboardButton != null && dashboardButton.getScene() != null) {
            return (Stage) dashboardButton.getScene().getWindow();
        }
        return null;
    }

    private String getCurrentDateTime() {
        return "2025-06-05 16:28:45"; // Using your provided timestamp
    }

    // Public methods for external access
    public static List<ScreeningData> loadScreeningsFromFile(String filePath) {
        List<ScreeningData> screenings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // Parse screening data similar to parseScreeningFromLine method
                    // Implementation would be similar to the private method above
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading screenings file: " + e.getMessage());
        }

        return screenings;
    }

    // Method to get all screenings (for external access)
    public static List<ScreeningData> getAllScreenings() {
        // This method can be called from other controllers to get screening data
        return new ArrayList<>(); // Implementation would load from file
    }
}