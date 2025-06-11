package com.example.modifiedcinemasystem;

import com.example.modifiedcinemasystem.AddMoviesScreenController;
import javafx.application.Platform;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

public class AvailableMoviesScreenController implements Initializable {

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

    @FXML
    private AnchorPane centerPane;

    @FXML
    private ScrollPane moviesScrollPane;

    // Static list to store movie data (persistent across sessions)
    private static List<MovieData> moviesList = new ArrayList<>();

    // Static variable to track the next Y position for new movies
    private static double nextMovieYPosition = 2324.0;

    // Static flag to track if movies have been loaded from file
    private static boolean moviesLoadedFromFile = false;

    // Set to track existing movie titles from FXML to prevent duplicates
    private static final Set<String> existingMovieTitles = new HashSet<>();

    static {
        // Initialize with hardcoded movie titles from FXML
        existingMovieTitles.add("28 Years Later");
        existingMovieTitles.add("Final Destination Bloodlines");
        existingMovieTitles.add("Mission Impossible");
        existingMovieTitles.add("Sinners");
        existingMovieTitles.add("Warfare");
        existingMovieTitles.add("Mickey 17");
        existingMovieTitles.add("Avengers Endgame");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Available Movies Screen initialized at " + getCurrentDateTime());
        System.out.println("Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): " + getCurrentDateTime());
        System.out.println("Current User: mutahharmudassar");

        // Load movies from file if not already loaded
        if (!moviesLoadedFromFile) {
            loadMoviesFromFile();
            moviesLoadedFromFile = true;
        }

        // Configure scroll pane after a short delay to ensure UI is loaded
        Platform.runLater(() -> {
            configureScrollPane();
            loadNewMovies();
        });
    }

    // Load movies from file (only new movies not in FXML)
    private void loadMoviesFromFile() {
        try {
            List<MovieData> fileMovies = AddMoviesScreenController.loadMoviesFromFile();

            // Clear existing dynamic movies (keep static ones from FXML)
            moviesList.removeIf(movie -> !existingMovieTitles.contains(movie.getTitle()));

            // Add only new movies that are not hardcoded in FXML
            for (MovieData movie : fileMovies) {
                if (!existingMovieTitles.contains(movie.getTitle()) && !isDuplicateMovie(movie)) {
                    movie.setDisplayed(false); // Mark as not displayed so they will be shown
                    moviesList.add(movie);
                    System.out.println("Added new movie from file: " + movie.getTitle());
                }
            }

            System.out.println("Loaded " + moviesList.size() + " total movies (" +
                    (moviesList.size() - countNewMovies()) + " from FXML + " +
                    countNewMovies() + " from file)");

        } catch (Exception e) {
            System.err.println("Error loading movies from file: " + e.getMessage());
        }
    }

    // Check if movie is already in the list
    private boolean isDuplicateMovie(MovieData newMovie) {
        for (MovieData existingMovie : moviesList) {
            if (existingMovie.getTitle().equals(newMovie.getTitle())) {
                return true;
            }
        }
        return false;
    }

    // Count new movies (not from FXML)
    private int countNewMovies() {
        int count = 0;
        for (MovieData movie : moviesList) {
            if (!existingMovieTitles.contains(movie.getTitle())) {
                count++;
            }
        }
        return count;
    }

    // Configure scroll pane properties
    private void configureScrollPane() {
        if (moviesScrollPane != null) {
            moviesScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            moviesScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            moviesScrollPane.setFitToWidth(true);
            moviesScrollPane.setFitToHeight(false);
            moviesScrollPane.setPannable(true);

            // Set proper content height
            if (centerPane != null) {
                centerPane.setMinHeight(2500.0);
                centerPane.setPrefHeight(2500.0);
                centerPane.setMaxHeight(Double.MAX_VALUE);
            }

            System.out.println("ScrollPane configured successfully");
        }
    }

    // Method to scroll to bottom
    public void scrollToBottom() {
        if (moviesScrollPane != null) {
            Platform.runLater(() -> {
                moviesScrollPane.setVvalue(1.0);
                System.out.println("Scrolled to bottom of movies list");
            });
        }
    }

    // Method to scroll to top
    public void scrollToTop() {
        if (moviesScrollPane != null) {
            Platform.runLater(() -> {
                moviesScrollPane.setVvalue(0.0);
                System.out.println("Scrolled to top of movies list");
            });
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
        navigateToScreen("Screening.fxml", "Edit Screening - Cinema Management System");
    }

    @FXML
    private void handleAvailableMovies(ActionEvent event) {
        System.out.println("Refreshing Available Movies screen at " + getCurrentDateTime());

        // Reload movies from file to get latest data
        loadMoviesFromFile();

        // Clear existing displayed dynamic movies (keep FXML ones)
        if (centerPane != null) {
            centerPane.getChildren().removeIf(node ->
                    node.getLayoutY() >= 2324.0 // Remove only dynamically added movies
            );
        }

        // Reset position and reload only new movies
        nextMovieYPosition = 2324.0;
        loadNewMovies();
        scrollToTop();
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
            System.out.println("mutahharmudassar signed out at " + getCurrentDateTime());
            navigateToScreen("LoginScreen.fxml", "Login - Cinema Management System");
        }
    }

    // Method to add a new movie panel (called from AddMoviesScreen)
    public static void addNewMovie(MovieData movieData) {
        // Check if movie already exists
        for (MovieData existingMovie : moviesList) {
            if (existingMovie.getTitle().equals(movieData.getTitle())) {
                System.out.println("Movie already exists: " + movieData.getTitle());
                return;
            }
        }

        // Check if it's a hardcoded movie from FXML
        if (existingMovieTitles.contains(movieData.getTitle())) {
            System.out.println("Movie already exists in FXML: " + movieData.getTitle());
            return;
        }

        moviesList.add(movieData);
        System.out.println("New movie added to list: " + movieData.getTitle());
        System.out.println("Total movies in system: " + (moviesList.size() + existingMovieTitles.size()));
    }

    // Method to load new movies into the UI (only movies not in FXML)
    private void loadNewMovies() {
        if (centerPane != null && !moviesList.isEmpty()) {
            for (MovieData movie : moviesList) {
                // Only create panels for new movies (not hardcoded in FXML)
                if (!existingMovieTitles.contains(movie.getTitle()) && !movie.isDisplayed()) {
                    createMoviePanel(movie);
                    movie.setDisplayed(true);
                }
            }

            // Update the center pane height to accommodate all movies
            double totalHeight = Math.max(2500.0, nextMovieYPosition + 100);
            centerPane.setPrefHeight(totalHeight);
            centerPane.setMinHeight(totalHeight);

            // Refresh scroll pane
            Platform.runLater(() -> {
                if (moviesScrollPane != null) {
                    moviesScrollPane.setContent(centerPane);
                }
            });
        }
    }

    // Method to create a new movie panel (only for dynamically added movies)
    private void createMoviePanel(MovieData movieData) {
        try {
            AnchorPane moviePanel = new AnchorPane();
            moviePanel.setLayoutX(14.0);
            moviePanel.setLayoutY(nextMovieYPosition);
            moviePanel.setPrefHeight(320.0);
            moviePanel.setPrefWidth(680.0);
            moviePanel.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 1;");

            // Create ImageView
            ImageView imageView = new ImageView();
            imageView.setFitHeight(300.0);
            imageView.setFitWidth(300.0);
            imageView.setLayoutX(360.0);
            imageView.setLayoutY(10.0);
            imageView.setPickOnBounds(true);
            imageView.setPreserveRatio(true);

            // Load image with better error handling
            loadMovieImage(imageView, movieData.getImagePath());

            // Create labels
            Label titleLabel = new Label("Title:");
            titleLabel.setLayoutX(25.0);
            titleLabel.setLayoutY(32.0);

            Label durationLabel = new Label("Duration:");
            durationLabel.setLayoutX(25.0);
            durationLabel.setLayoutY(68.0);

            Label genreLabel = new Label("Genre:");
            genreLabel.setLayoutX(25.0);
            genreLabel.setLayoutY(104.0);

            Label castLabel = new Label("Cast:");
            castLabel.setLayoutX(25.0);
            castLabel.setLayoutY(140.0);

            Label ratingLabel = new Label("Rating:");
            ratingLabel.setLayoutX(25.0);
            ratingLabel.setLayoutY(200.0);

            Label descriptionLabel = new Label("Description:");
            descriptionLabel.setLayoutX(25.0);
            descriptionLabel.setLayoutY(236.0);

            // Create text fields with movie data
            TextField titleField = new TextField(movieData.getTitle());
            titleField.setLayoutX(98.0);
            titleField.setLayoutY(28.0);
            titleField.setPrefWidth(200.0);
            titleField.setEditable(false);

            TextField durationField = new TextField(movieData.getDuration());
            durationField.setLayoutX(98.0);
            durationField.setLayoutY(64.0);
            durationField.setPrefWidth(200.0);
            durationField.setEditable(false);

            TextField genreField = new TextField(movieData.getGenre());
            genreField.setLayoutX(98.0);
            genreField.setLayoutY(100.0);
            genreField.setPrefWidth(200.0);
            genreField.setEditable(false);

            TextArea castArea = new TextArea(movieData.getCast());
            castArea.setLayoutX(98.0);
            castArea.setLayoutY(136.0);
            castArea.setPrefHeight(60.0);
            castArea.setPrefWidth(200.0);
            castArea.setEditable(false);

            TextField ratingField = new TextField(String.valueOf(movieData.getRating()));
            ratingField.setLayoutX(98.0);
            ratingField.setLayoutY(196.0);
            ratingField.setPrefWidth(200.0);
            ratingField.setEditable(false);

            TextArea descriptionArea = new TextArea(movieData.getDescription());
            descriptionArea.setLayoutX(98.0);
            descriptionArea.setLayoutY(232.0);
            descriptionArea.setPrefHeight(80.0);
            descriptionArea.setPrefWidth(200.0);
            descriptionArea.setEditable(false);

            // Add all components to the movie panel
            moviePanel.getChildren().addAll(
                    imageView, titleLabel, durationLabel, genreLabel, castLabel,
                    ratingLabel, descriptionLabel, titleField, durationField,
                    genreField, castArea, ratingField, descriptionArea
            );

            // Add the movie panel to the center pane
            centerPane.getChildren().add(moviePanel);

            // Update position for next movie
            nextMovieYPosition += 330.0;

            System.out.println("Movie panel created for: " + movieData.getTitle());
            System.out.println("Next movie position: " + nextMovieYPosition);

        } catch (Exception e) {
            System.err.println("Error creating movie panel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Improved image loading method
    private void loadMovieImage(ImageView imageView, String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            System.out.println("No image path provided");
            return;
        }

        try {
            System.out.println("Attempting to load image: " + imagePath);

            Image image = null;

            // Handle different image path formats
            if (imagePath.startsWith("@")) {
                // Resource path format (@movie_images/filename.jpg)
                String resourcePath = imagePath.substring(1); // Remove @ symbol
                InputStream imageStream = getClass().getResourceAsStream("/" + resourcePath);

                if (imageStream != null) {
                    image = new Image(imageStream);
                    System.out.println("Image loaded from resources: " + resourcePath);
                } else {
                    System.out.println("Image not found in resources: " + resourcePath);
                    // Try loading from file system
                    try {
                        image = new Image("file:" + resourcePath);
                        System.out.println("Image loaded from file system: " + resourcePath);
                    } catch (Exception e) {
                        System.out.println("Could not load image from file system either");
                    }
                }
            } else {
                // Direct file path
                image = new Image("file:" + imagePath);
                System.out.println("Image loaded from direct path: " + imagePath);
            }

            if (image != null && !image.isError()) {
                imageView.setImage(image);
                System.out.println("Image successfully set to ImageView");
            } else {
                System.out.println("Failed to load image or image has error");
            }

        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
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

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Failed to load screen");
            alert.setContentText("Could not load " + fxmlFile + ". Please check if the file exists.");
            alert.showAndWait();
        }
    }

    // Get current stage
    private Stage getCurrentStage() {
        if (dashboardButton != null && dashboardButton.getScene() != null) {
            return (Stage) dashboardButton.getScene().getWindow();
        } else if (signOutButton != null && signOutButton.getScene() != null) {
            return (Stage) signOutButton.getScene().getWindow();
        }
        return null;
    }

    // Helper method to get current date and time
    private String getCurrentDateTime() {
        return "2025-06-05 16:43:15"; // Using your provided timestamp
    }

    // Static utility methods
    public static List<MovieData> getAllMovies() {
        List<MovieData> allMovies = new ArrayList<>(moviesList);

        // Add hardcoded movies from FXML if they're not already included
        for (String title : existingMovieTitles) {
            boolean found = false;
            for (MovieData movie : allMovies) {
                if (movie.getTitle().equals(title)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                // Create basic MovieData for hardcoded movies
                MovieData hardcodedMovie = createHardcodedMovieData(title);
                if (hardcodedMovie != null) {
                    allMovies.add(hardcodedMovie);
                }
            }
        }

        return allMovies;
    }

    // Create MovieData for hardcoded movies
    private static MovieData createHardcodedMovieData(String title) {
        switch (title) {
            case "28 Years Later":
                return new MovieData(title, "120 minutes", "Horror", "Cillian Murphy, Jodie Comer", "R",
                        "The rage virus has evolved. Twenty-eight years after the initial outbreak.",
                        "@28 Years Later.jpg");
            case "Final Destination Bloodlines":
                return new MovieData(title, "110 minutes", "Horror/Thriller", "Tony Todd, Devon Sawa", "R",
                        "Death returns with a vengeance in this terrifying new chapter.",
                        "@final destination bloodlines.jpg");
            case "Mission Impossible":
                return new MovieData(title, "147 minutes", "Action/Thriller", "Tom Cruise, Rebecca Ferguson", "PG-13",
                        "Ethan Hunt and his IMF team must track down a terrifying new weapon.",
                        "@mission impossible.jpg");
            case "Sinners":
                return new MovieData(title, "105 minutes", "Drama/Crime", "Michael B. Jordan, Ryan Coogler", "R",
                        "Two brothers return to their hometown to face the demons of their past.",
                        "@sinners.jpg");
            case "Warfare":
                return new MovieData(title, "130 minutes", "War/Action", "Will Poulter, D'Pharaoh Woon-A-Tai", "R",
                        "A gripping war drama following soldiers during an intense 24-hour period.",
                        "@warfare.jpg");
            case "Mickey 17":
                return new MovieData(title, "115 minutes", "Sci-Fi", "Robert Pattinson, Naomi Ackie", "PG-13",
                        "A sci-fi thriller about Mickey Barnes, an expendable employee.",
                        "@mickey 17.jpg");
            case "Avengers Endgame":
                return new MovieData(title, "181 minutes", "Action/Adventure", "Robert Downey Jr., Chris Evans", "PG-13",
                        "The epic conclusion to the Infinity Saga.",
                        "@avengers endgame.jpg");
            default:
                return null;
        }
    }

    public static void clearAllMovies() {
        moviesList.clear();
        nextMovieYPosition = 2324.0;
        System.out.println("All dynamic movies cleared from system");
    }

    public static int getMovieCount() {
        return moviesList.size() + existingMovieTitles.size();
    }

    // Force reload from file
    public static void forceReloadFromFile() {
        moviesLoadedFromFile = false;
    }
}