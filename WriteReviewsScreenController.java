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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class WriteReviewsScreenController implements Initializable {

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

    // Labels
    @FXML
    private Label welcomeUserLabel;

    @FXML
    private Label statusLabel;

    // Form controls
    @FXML
    private ComboBox<String> movieComboBox;

    @FXML
    private Slider cinemaQualitySlider;

    @FXML
    private Label cinemaQualityLabel;

    @FXML
    private Slider websiteQualitySlider;

    @FXML
    private Label websiteQualityLabel;

    @FXML
    private Slider filmRatingSlider;

    @FXML
    private Label filmRatingLabel;

    @FXML
    private Slider overallRatingSlider;

    @FXML
    private Label overallRatingLabel;

    @FXML
    private TextField customerEmailField;

    @FXML
    private TextField reviewTitleField;

    @FXML
    private TextArea reviewTextArea;

    @FXML
    private Label averageRatingLabel;

    // Action buttons
    @FXML
    private Button submitReviewButton;

    @FXML
    private Button clearFormButton;

    @FXML
    private Button viewReviewsButton;

    @FXML
    private Button backButton;

    // Data
    private ReviewData currentReview;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Write Review Screen initialized at " + getCurrentDateTime());
        System.out.println("Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): " + getCurrentDateTime());
        System.out.println("Current User's Login: " + getCurrentLoggedInCustomer());

        // Set customer name
        String loggedInCustomer = getCurrentLoggedInCustomer();
        if (customerNameField != null) {
            customerNameField.setText(loggedInCustomer);
        }
        if (welcomeUserLabel != null) {
            welcomeUserLabel.setText("Welcome, " + loggedInCustomer);
        }

        // Initialize form components
        initializeMovieComboBox();
        initializeSliders();

        updateStatus("Ready to write your review - All fields are optional!");
    }

    // Initialize movie selection dropdown
    private void initializeMovieComboBox() {
        movieComboBox.getItems().addAll(
                "28 Years Later",
                "Mission Impossible",
                "Avengers Endgame",
                "Mickey 17",
                "Warfare",
                "Sinners",
                "Final Destination Bloodlines",
                "The Batman",
                "Spider-Man: No Way Home",
                "Dune: Part Two",
                "Top Gun: Maverick",
                "Black Panther: Wakanda Forever"
        );
        movieComboBox.setPromptText("Select a movie...");

        System.out.println("Movie selection dropdown initialized with " + movieComboBox.getItems().size() + " movies");
    }

    // Initialize rating sliders with listeners
    private void initializeSliders() {
        // Cinema Quality Slider
        cinemaQualitySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double rating = Math.round(newVal.doubleValue() * 10.0) / 10.0;
            cinemaQualityLabel.setText(String.format("%.1f / 5.0", rating));
            updateRatingLabelColor(cinemaQualityLabel, rating);
            updateAverageRating();
        });

        // Website Quality Slider
        websiteQualitySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double rating = Math.round(newVal.doubleValue() * 10.0) / 10.0;
            websiteQualityLabel.setText(String.format("%.1f / 5.0", rating));
            updateRatingLabelColor(websiteQualityLabel, rating);
            updateAverageRating();
        });

        // Film Rating Slider
        filmRatingSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double rating = Math.round(newVal.doubleValue() * 10.0) / 10.0;
            filmRatingLabel.setText(String.format("%.1f / 5.0", rating));
            updateRatingLabelColor(filmRatingLabel, rating);
            updateAverageRating();
        });

        // Overall Rating Slider
        overallRatingSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double rating = Math.round(newVal.doubleValue() * 10.0) / 10.0;
            overallRatingLabel.setText(String.format("%.1f / 5.0", rating));
            updateRatingLabelColor(overallRatingLabel, rating);
            updateAverageRating();
        });

        System.out.println("Rating sliders initialized with listeners");
    }

    // Update rating label color based on value
    private void updateRatingLabelColor(Label label, double rating) {
        if (rating >= 4.0) {
            label.setStyle("-fx-text-fill: #27ae60;"); // Green for excellent
        } else if (rating >= 3.0) {
            label.setStyle("-fx-text-fill: #f39c12;"); // Orange for good
        } else if (rating >= 2.0) {
            label.setStyle("-fx-text-fill: #e67e22;"); // Orange-red for fair
        } else if (rating > 0.0) {
            label.setStyle("-fx-text-fill: #e74c3c;"); // Red for poor
        } else {
            label.setStyle("-fx-text-fill: #95a5a6;"); // Gray for no rating
        }
    }

    // Calculate and display average rating
    private void updateAverageRating() {
        double total = cinemaQualitySlider.getValue() + websiteQualitySlider.getValue() +
                filmRatingSlider.getValue() + overallRatingSlider.getValue();
        double average = total / 4.0;
        average = Math.round(average * 10.0) / 10.0;

        averageRatingLabel.setText(String.format("Average Rating: %.1f/5.0", average));
        updateRatingLabelColor(averageRatingLabel, average);
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

    // In WriteReviewsScreenController.java, update this method:
    @FXML
    private void handleSearchMovies(ActionEvent event) {
        System.out.println("Search Movies clicked by " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());

        // Try different possible filenames
        String[] possibleFiles = {
                "SearchMoviesScreen.fxml",
                "SearchMovies.fxml",
                "searchMoviesScreen.fxml"
        };

        boolean loaded = false;
        for (String filename : possibleFiles) {
            try {
                URL fxmlUrl = getClass().getResource(filename);
                if (fxmlUrl != null) {
                    System.out.println("Found FXML file: " + filename);
                    Parent root = FXMLLoader.load(fxmlUrl);
                    Stage stage = getCurrentStage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Search Movies - Cinema Management System");
                    loaded = true;
                    break;
                } else {
                    System.out.println("File not found: " + filename);
                }
            } catch (IOException e) {
                System.out.println("Error loading " + filename + ": " + e.getMessage());
            }
        }

        if (!loaded) {
            System.err.println("Could not find SearchMoviesScreen.fxml file!");
            showFileNotFoundAlert();
        }
    }

    // Add this helper method
    private void showFileNotFoundAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("File Not Found");
        alert.setHeaderText("SearchMoviesScreen.fxml not found");
        alert.setContentText("The SearchMoviesScreen.fxml file is missing from the resources folder.\n" +
                "Please ensure the file exists in the same directory as other FXML files.");
        alert.showAndWait();
    }

    @FXML
    private void handleWriteReviews(ActionEvent event) {
        System.out.println("Already on Write Reviews screen - refreshing at " + getCurrentDateTime());
        handleClearForm(null);
    }

    @FXML
    private void handleSignOut(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setHeaderText("Sign Out Confirmation");
        alert.setContentText("Are you sure you want to sign out? Any unsaved review will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Customer " + getCurrentLoggedInCustomer() + " signed out at " + getCurrentDateTime());
            CustomerScreenController.clearCurrentLoggedInCustomer();
            navigateToScreen("LoginScreen.fxml", "Login - Cinema Management System");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        System.out.println("Back to Customer Portal clicked at " + getCurrentDateTime());
        navigateToScreen("CustomerScreen.fxml", "Customer Portal - Cinema Management System");
    }

    // Review Methods
    @FXML
    private void handleSubmitReview(ActionEvent event) {
        System.out.println("Submit Review clicked by " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());

        // Check if at least one field is filled
        if (isFormEmpty()) {
            showWarning("Empty Review", "Please fill at least one field to submit your review.");
            return;
        }

        try {
            // Create review data
            createReviewData();

            // Save to file
            saveReviewToFile();

            // Show success message
            showSuccess("Review Submitted",
                    "Thank you for your review!\n" +
                            "Your feedback has been saved successfully.\n" +
                            "Review ID: " + currentReview.getReviewId());

            // Clear form after successful submission
            handleClearForm(null);

            updateStatus("Review submitted successfully - Thank you for your feedback!");

        } catch (Exception e) {
            System.err.println("Error submitting review: " + e.getMessage());
            e.printStackTrace();
            showError("Submission Error", "Failed to submit review. Please try again.");
        }
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        System.out.println("Clear Form clicked at " + getCurrentDateTime());

        movieComboBox.setValue(null);
        cinemaQualitySlider.setValue(0.0);
        websiteQualitySlider.setValue(0.0);
        filmRatingSlider.setValue(0.0);
        overallRatingSlider.setValue(0.0);
        customerEmailField.clear();
        reviewTitleField.clear();
        reviewTextArea.clear();

        updateStatus("Form cleared - Ready for new review");
    }

    @FXML
    private void handleViewReviews(ActionEvent event) {
        System.out.println("View Reviews clicked by " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("View Reviews");
        alert.setHeaderText("Review Information");
        alert.setContentText("Review viewing feature will be available soon!\n" +
                "All reviews are being saved to Reviews.txt file.\n" +
                "Thank you for your patience.");
        alert.showAndWait();

        updateStatus("Review viewing feature coming soon");
    }

    // Helper Methods
    private boolean isFormEmpty() {
        return (movieComboBox.getValue() == null || movieComboBox.getValue().isEmpty()) &&
                cinemaQualitySlider.getValue() == 0.0 &&
                websiteQualitySlider.getValue() == 0.0 &&
                filmRatingSlider.getValue() == 0.0 &&
                overallRatingSlider.getValue() == 0.0 &&
                (customerEmailField.getText() == null || customerEmailField.getText().trim().isEmpty()) &&
                (reviewTitleField.getText() == null || reviewTitleField.getText().trim().isEmpty()) &&
                (reviewTextArea.getText() == null || reviewTextArea.getText().trim().isEmpty());
    }

    private void createReviewData() {
        currentReview = new ReviewData();
        currentReview.setReviewId(generateReviewId());
        currentReview.setCustomerName(getCurrentLoggedInCustomer());
        currentReview.setMovieTitle(movieComboBox.getValue() != null ? movieComboBox.getValue() : "");
        currentReview.setCinemaQualityRating(cinemaQualitySlider.getValue());
        currentReview.setWebsiteQualityRating(websiteQualitySlider.getValue());
        currentReview.setFilmRating(filmRatingSlider.getValue());
        currentReview.setOverallRating(overallRatingSlider.getValue());
        currentReview.setCustomerEmail(customerEmailField.getText() != null ? customerEmailField.getText().trim() : "");
        currentReview.setReviewTitle(reviewTitleField.getText() != null ? reviewTitleField.getText().trim() : "");
        currentReview.setReviewText(reviewTextArea.getText() != null ? reviewTextArea.getText().trim() : "");
        currentReview.setReviewDate(getCurrentDateTime());

        // Calculate average rating
        double avgRating = (currentReview.getCinemaQualityRating() + currentReview.getWebsiteQualityRating() +
                currentReview.getFilmRating() + currentReview.getOverallRating()) / 4.0;
        currentReview.setAverageRating(Math.round(avgRating * 10.0) / 10.0);

        System.out.println("Review data created for: " + currentReview.getCustomerName() +
                " - Average Rating: " + currentReview.getAverageRating());
    }

    private void saveReviewToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Reviews.txt", true))) {
            // Format: ReviewID|CustomerName|MovieTitle|CinemaRating|WebsiteRating|FilmRating|OverallRating|AverageRating|Email|Title|ReviewText|ReviewDate
            String reviewLine = String.format("%s|%s|%s|%.1f|%.1f|%.1f|%.1f|%.1f|%s|%s|%s|%s",
                    currentReview.getReviewId(),
                    currentReview.getCustomerName(),
                    currentReview.getMovieTitle(),
                    currentReview.getCinemaQualityRating(),
                    currentReview.getWebsiteQualityRating(),
                    currentReview.getFilmRating(),
                    currentReview.getOverallRating(),
                    currentReview.getAverageRating(),
                    currentReview.getCustomerEmail(),
                    currentReview.getReviewTitle().replace("|", ""), // Remove pipe chars to avoid format issues
                    currentReview.getReviewText().replace("|", "").replace("\n", " "), // Remove pipes and newlines
                    currentReview.getReviewDate()
            );

            writer.write(reviewLine);
            writer.newLine();
            writer.flush();

            System.out.println("Review saved to Reviews.txt: " + currentReview.getReviewId());
            System.out.println("Review entry: " + reviewLine);
        }
    }

    // Utility Methods
    private String generateReviewId() {
        Random random = new Random();
        return "REV" + System.currentTimeMillis() + random.nextInt(1000);
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

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
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
        if (submitReviewButton != null && submitReviewButton.getScene() != null) {
            return (Stage) submitReviewButton.getScene().getWindow();
        }
        return null;
    }

    private String getCurrentDateTime() {
        return "2025-06-05 20:06:11"; // Updated timestamp
    }

    private String getCurrentLoggedInCustomer() {
        return CustomerScreenController.getCurrentLoggedInCustomer();
    }
}
