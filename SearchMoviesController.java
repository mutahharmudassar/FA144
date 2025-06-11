package com.example.modifiedcinemasystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

public class SearchMoviesController implements Initializable {

    // Navigation buttons
    @FXML private Button viewScreeningsButton;
    @FXML private Button bookTicketButton;
    @FXML private Button searchMoviesButton;
    @FXML private Button writeReviewsButton;
    @FXML private Button signOutButton;
    @FXML private TextField customerNameField;

    // Labels
    @FXML private Label welcomeUserLabel;
    @FXML private Label statusLabel;
    @FXML private Label resultsLabel;
    @FXML private Label ratingLabel;

    // Search controls
    @FXML private TextField movieNameField;
    @FXML private ComboBox<String> genreComboBox;
    @FXML private TextField castField;
    @FXML private Slider ratingSlider;

    // Action buttons
    @FXML private Button searchButton;
    @FXML private Button clearButton;
    @FXML private Button showAllButton;

    // Display components
    @FXML private ScrollPane moviesScrollPane;
    @FXML private FlowPane moviesFlowPane;
    @FXML private AnchorPane noResultsPane;

    // Data - Using Set to prevent duplicates
    private List<MovieData> allMovies = new ArrayList<>();
    private List<MovieData> filteredMovies = new ArrayList<>();
    private Set<String> loadedMovieTitles = new HashSet<>(); // Track loaded movies

    // Static flag to prevent multiple loading
    private static boolean moviesAlreadyLoaded = false;
    private static List<MovieData> staticMovieCache = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("=== Search Movies Screen Initialize Start ===");
        System.out.println("Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): " + getCurrentDateTime());
        System.out.println("Current User's Login: " + getCurrentLoggedInCustomer());

        try {
            // Set customer name
            String loggedInCustomer = getCurrentLoggedInCustomer();
            if (customerNameField != null) {
                customerNameField.setText(loggedInCustomer);
            }
            if (welcomeUserLabel != null) {
                welcomeUserLabel.setText("Welcome, " + loggedInCustomer);
            }

            // Initialize components
            initializeGenreComboBox();
            initializeRatingSlider();

            // FIXED: Load movies only once using static cache
            loadMoviesFromFileWithCache();

            // Display all movies initially
            displayMovies(allMovies);

            updateStatus("Ready to search movies - Loaded " + allMovies.size() + " movies");

        } catch (Exception e) {
            System.err.println("Error during initialization: " + e.getMessage());
            e.printStackTrace();
            updateStatus("Error loading movies - Using fallback data");
            createSampleMovies();
            displayMovies(allMovies);
        }

        System.out.println("=== Search Movies Screen Initialize Complete ===");
    }

    // Initialize genre dropdown
    private void initializeGenreComboBox() {
        try {
            genreComboBox.getItems().clear();
            genreComboBox.getItems().add("All Genres");
            genreComboBox.setValue("All Genres");

            System.out.println("Genre dropdown initialized");
        } catch (Exception e) {
            System.err.println("Error initializing genre combo box: " + e.getMessage());
        }
    }

    // Initialize rating slider
    private void initializeRatingSlider() {
        try {
            ratingSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                double rating = Math.round(newVal.doubleValue() * 10.0) / 10.0;
                ratingLabel.setText(String.format("%.1f+", rating));

                // Update label color based on rating
                if (rating >= 8.0) {
                    ratingLabel.setStyle("-fx-text-fill: #27ae60;");
                } else if (rating >= 6.0) {
                    ratingLabel.setStyle("-fx-text-fill: #f39c12;");
                } else if (rating >= 4.0) {
                    ratingLabel.setStyle("-fx-text-fill: #e67e22;");
                } else {
                    ratingLabel.setStyle("-fx-text-fill: #e74c3c;");
                }
            });

            System.out.println("Rating slider initialized");
        } catch (Exception e) {
            System.err.println("Error initializing rating slider: " + e.getMessage());
        }
    }

    // FIXED: Load movies with caching to prevent duplicates
    private void loadMoviesFromFileWithCache() {
        // Clear current data
        allMovies.clear();
        loadedMovieTitles.clear();

        // Use cached data if available
        if (moviesAlreadyLoaded && !staticMovieCache.isEmpty()) {
            System.out.println("Using cached movie data (" + staticMovieCache.size() + " movies)");
            allMovies.addAll(staticMovieCache);

            // Rebuild title set for duplicate checking
            for (MovieData movie : allMovies) {
                loadedMovieTitles.add(movie.getTitle().toLowerCase());
            }

            updateGenreDropdown();
            return;
        }

        // Load from file
        System.out.println("Loading movies from Movies.txt file...");

        try (BufferedReader reader = new BufferedReader(new FileReader("Movies.txt"))) {
            String line;
            int lineNumber = 0;
            int duplicatesSkipped = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!line.trim().isEmpty()) {
                    MovieData movie = parseMovieFromLine(line, lineNumber);
                    if (movie != null) {
                        // Check for duplicates
                        String movieTitleLower = movie.getTitle().toLowerCase();
                        if (!loadedMovieTitles.contains(movieTitleLower)) {
                            allMovies.add(movie);
                            loadedMovieTitles.add(movieTitleLower);
                            System.out.println("Loaded: " + movie.getTitle() + " (" + movie.getGenre() + ")");
                        } else {
                            duplicatesSkipped++;
                            System.out.println("Skipped duplicate: " + movie.getTitle());
                        }
                    }
                }
            }

            System.out.println("Successfully loaded " + allMovies.size() + " unique movies from Movies.txt");
            if (duplicatesSkipped > 0) {
                System.out.println("Skipped " + duplicatesSkipped + " duplicate movies");
            }

            // Cache the loaded movies
            staticMovieCache.clear();
            staticMovieCache.addAll(allMovies);
            moviesAlreadyLoaded = true;

            // Update genre dropdown
            updateGenreDropdown();

        } catch (IOException e) {
            System.err.println("Error loading movies file: " + e.getMessage());
            createSampleMovies();
        }
    }

    // Parse movie data from file line - handle both formats
    private MovieData parseMovieFromLine(String line, int lineNumber) {
        try {
            String[] parts = line.split("\\|");

            MovieData movie = new MovieData();

            if (parts.length >= 8) {
                // Handle AddMoviesScreen format: Title|Genre|Duration|Rating|Cast|Description|ImagePath|DateTime
                movie.setTitle(parts[0].trim());
                movie.setGenre(parts[1].trim());
                movie.setDuration(parts[2].trim());
                movie.setRatingString(parts[3].trim());
                movie.setCast(parts[4].trim());
                movie.setDescription(parts[5].trim());
                movie.setImagePath(parts[6].trim());

                // Set default values
                movie.setDirector("Various Directors");
                movie.setReleaseDate("2025");

                return movie;

            } else if (parts.length >= 10) {
                // Handle SearchMovies format: ID|Title|Genre|Director|Cast|Rating|Duration|ReleaseDate|Description|ImagePath
                movie.setMovieId(Integer.parseInt(parts[0].trim()));
                movie.setTitle(parts[1].trim());
                movie.setGenre(parts[2].trim());
                movie.setDirector(parts[3].trim());
                movie.setCast(parts[4].trim());
                movie.setRating(Double.parseDouble(parts[5].trim()));
                movie.setDuration(parts[6].trim());
                movie.setReleaseDate(parts[7].trim());
                movie.setDescription(parts[8].trim());
                movie.setImagePath(parts[9].trim());

                return movie;
            } else {
                System.out.println("WARNING: Line " + lineNumber + " has insufficient parts (" + parts.length + ")");
            }
        } catch (Exception e) {
            System.err.println("ERROR parsing line " + lineNumber + ": " + e.getMessage());
        }
        return null;
    }

    // Update genre dropdown with actual genres from loaded movies
    private void updateGenreDropdown() {
        try {
            Set<String> genres = new HashSet<>();

            for (MovieData movie : allMovies) {
                String movieGenre = movie.getGenre();
                if (movieGenre != null && !movieGenre.trim().isEmpty()) {
                    // Handle multiple genres separated by comma, slash, or space
                    String[] movieGenres = movieGenre.split("[,/\\s]+");
                    for (String genre : movieGenres) {
                        String cleanGenre = genre.trim();
                        if (!cleanGenre.isEmpty() && cleanGenre.length() > 1) {
                            genres.add(cleanGenre);
                        }
                    }
                }
            }

            String currentSelection = genreComboBox.getValue();
            genreComboBox.getItems().clear();
            genreComboBox.getItems().add("All Genres");

            // Add sorted genres
            List<String> sortedGenres = new ArrayList<>(genres);
            sortedGenres.sort(String::compareToIgnoreCase);
            genreComboBox.getItems().addAll(sortedGenres);

            genreComboBox.setValue(currentSelection != null ? currentSelection : "All Genres");

            System.out.println("Updated genre dropdown with " + genres.size() + " genres: " + genres);
        } catch (Exception e) {
            System.err.println("Error updating genre dropdown: " + e.getMessage());
        }
    }

    // Create sample movies if file not found
    private void createSampleMovies() {
        System.out.println("Creating sample movie data...");

        allMovies.clear();
        loadedMovieTitles.clear();

        // Create sample movies using the correct constructor
        addSampleMovie("28 Years Later", "Horror/Thriller", "Danny Boyle",
                "Cillian Murphy, Jodie Comer", 8.2, "118 min", "2025",
                "A post-apocalyptic horror thriller continuing the zombie saga.", "/images/28years.jpg");

        addSampleMovie("Mission Impossible", "Action/Adventure", "Christopher McQuarrie",
                "Tom Cruise, Rebecca Ferguson", 8.5, "147 min", "2023",
                "Ethan Hunt faces his most dangerous mission yet.", "/images/mission.jpg");

        addSampleMovie("Avengers Endgame", "Action/Adventure/Sci-Fi", "Anthony Russo",
                "Robert Downey Jr., Chris Evans", 8.9, "181 min", "2019",
                "The epic conclusion to the Infinity Saga.", "/images/avengers.jpg");

        updateGenreDropdown();
        System.out.println("Created " + allMovies.size() + " sample movies");
    }

    // Helper method to add sample movie without duplicates
    private void addSampleMovie(String title, String genre, String director, String cast,
                                double rating, String duration, String releaseDate, String description, String imagePath) {
        String titleLower = title.toLowerCase();
        if (!loadedMovieTitles.contains(titleLower)) {
            MovieData movie = new MovieData();
            movie.setTitle(title);
            movie.setGenre(genre);
            movie.setDirector(director);
            movie.setCast(cast);
            movie.setRating(rating);
            movie.setDuration(duration);
            movie.setReleaseDate(releaseDate);
            movie.setDescription(description);
            movie.setImagePath(imagePath);

            allMovies.add(movie);
            loadedMovieTitles.add(titleLower);
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
        System.out.println("Already on Search Movies screen - refreshing at " + getCurrentDateTime());
        handleClear(null);
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

    // Search Methods
    @FXML
    private void handleSearch(ActionEvent event) {
        System.out.println("Search clicked by " + getCurrentLoggedInCustomer() + " at " + getCurrentDateTime());
        performSearch();
    }

    @FXML
    private void handleClear(ActionEvent event) {
        System.out.println("Clear filters clicked at " + getCurrentDateTime());

        movieNameField.clear();
        genreComboBox.setValue("All Genres");
        castField.clear();
        ratingSlider.setValue(0.0);

        displayMovies(allMovies);
        updateStatus("Filters cleared - Showing all " + allMovies.size() + " movies");
    }

    @FXML
    private void handleShowAll(ActionEvent event) {
        System.out.println("Show All Movies clicked at " + getCurrentDateTime());
        displayMovies(allMovies);
        updateStatus("Showing all " + allMovies.size() + " available movies");
    }

    // Perform search based on filters
    private void performSearch() {
        try {
            filteredMovies.clear();

            String nameQuery = movieNameField.getText().toLowerCase().trim();
            String selectedGenre = genreComboBox.getValue();
            String castQuery = castField.getText().toLowerCase().trim();
            double minRating = ratingSlider.getValue();

            System.out.println("Searching with criteria:");
            System.out.println("- Name: '" + nameQuery + "'");
            System.out.println("- Genre: '" + selectedGenre + "'");
            System.out.println("- Cast: '" + castQuery + "'");
            System.out.println("- Min Rating: " + minRating);

            for (MovieData movie : allMovies) {
                boolean matches = true;

                // Check movie name (case-insensitive)
                if (!nameQuery.isEmpty()) {
                    String movieTitle = movie.getTitle().toLowerCase();
                    matches = movieTitle.contains(nameQuery);
                }

                // Check genre (case-insensitive)
                if (matches && selectedGenre != null && !selectedGenre.equals("All Genres")) {
                    String movieGenre = movie.getGenre().toLowerCase();
                    matches = movieGenre.contains(selectedGenre.toLowerCase());
                }

                // Check cast (case-insensitive)
                if (matches && !castQuery.isEmpty()) {
                    String searchText = (movie.getCast() + " " + movie.getDirector()).toLowerCase();
                    matches = searchText.contains(castQuery);
                }

                // Check rating
                if (matches && minRating > 0.0) {
                    matches = movie.getRating() >= minRating;
                }

                if (matches) {
                    filteredMovies.add(movie);
                }
            }

            displayMovies(filteredMovies);

            String searchSummary = String.format("Found %d movie(s) matching your criteria", filteredMovies.size());
            updateStatus(searchSummary);

            System.out.println("Search completed: " + searchSummary);

        } catch (Exception e) {
            System.err.println("Error during search: " + e.getMessage());
            e.printStackTrace();
            updateStatus("Error during search - Please try again");
        }
    }

    // Display movies in the UI
    private void displayMovies(List<MovieData> movies) {
        try {
            moviesFlowPane.getChildren().clear();

            if (movies.isEmpty()) {
                noResultsPane.setVisible(true);
                resultsLabel.setText("Found 0 movies");
            } else {
                noResultsPane.setVisible(false);
                resultsLabel.setText(String.format("Found %d movie(s)", movies.size()));

                for (MovieData movie : movies) {
                    VBox movieCard = createMovieCard(movie);
                    moviesFlowPane.getChildren().add(movieCard);
                }
            }

            System.out.println("Displayed " + movies.size() + " movies in UI");

        } catch (Exception e) {
            System.err.println("Error displaying movies: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Create individual movie card
    private VBox createMovieCard(MovieData movie) {
        VBox movieCard = new VBox();
        movieCard.setSpacing(8);
        movieCard.setPrefWidth(180);
        movieCard.setMaxWidth(180);
        movieCard.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 1; " +
                "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10;");
        movieCard.setAlignment(Pos.TOP_CENTER);

        // Movie Image
        ImageView imageView = new ImageView();
        imageView.setFitWidth(160);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(false);

        try {
            Image image = new Image(getClass().getResourceAsStream(movie.getImagePath()));
            imageView.setImage(image);
        } catch (Exception e) {
            imageView.setStyle("-fx-background-color: #6c757d;");
        }

        // Movie Title
        Label titleLabel = new Label(movie.getTitle());
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(160);

        // Genre
        Label genreLabel = new Label("Genre: " + movie.getGenre());
        genreLabel.setFont(Font.font("System", 10));
        genreLabel.setStyle("-fx-text-fill: #6c757d;");
        genreLabel.setWrapText(true);
        genreLabel.setMaxWidth(160);

        // Rating
        Label ratingLabel = new Label(String.format("⭐ %.1f/10", movie.getRating()));
        ratingLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
        if (movie.getRating() >= 8.0) {
            ratingLabel.setStyle("-fx-text-fill: #27ae60;");
        } else if (movie.getRating() >= 6.0) {
            ratingLabel.setStyle("-fx-text-fill: #f39c12;");
        } else {
            ratingLabel.setStyle("-fx-text-fill: #e74c3c;");
        }

        // Duration and Year
        Label detailsLabel = new Label(movie.getDuration() + " • " + movie.getReleaseDate());
        detailsLabel.setFont(Font.font("System", 9));
        detailsLabel.setStyle("-fx-text-fill: #95a5a6;");

        // Director
        Label directorLabel = new Label("Dir: " + movie.getDirector());
        directorLabel.setFont(Font.font("System", 9));
        directorLabel.setStyle("-fx-text-fill: #7f8c8d;");
        directorLabel.setWrapText(true);
        directorLabel.setMaxWidth(160);

        // Cast
        String castText = movie.getCast();
        if (castText.length() > 40) {
            castText = castText.substring(0, 37) + "...";
        }
        Label castLabel = new Label("Cast: " + castText);
        castLabel.setFont(Font.font("System", 9));
        castLabel.setStyle("-fx-text-fill: #7f8c8d;");
        castLabel.setWrapText(true);
        castLabel.setMaxWidth(160);

        // Book Ticket Button
        Button bookButton = new Button("🎫 Book Ticket");
        bookButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
                "-fx-border-radius: 4; -fx-background-radius: 4; -fx-font-weight: bold;");
        bookButton.setPrefWidth(160);
        bookButton.setOnAction(e -> {
            System.out.println("Book ticket clicked for: " + movie.getTitle());
            navigateToScreen("BookTicketScreen.fxml", "Book Ticket - Cinema Management System");
        });

        movieCard.getChildren().addAll(imageView, titleLabel, genreLabel, ratingLabel,
                detailsLabel, directorLabel, castLabel, bookButton);

        return movieCard;
    }

    // Utility Methods
    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
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
        if (searchButton != null && searchButton.getScene() != null) {
            return (Stage) searchButton.getScene().getWindow();
        }
        return null;
    }

    private String getCurrentDateTime() {
        return "2025-06-05 20:40:57";
    }

    private String getCurrentLoggedInCustomer() {
        return CustomerScreenController.getCurrentLoggedInCustomer();
    }

    // Static method to clear cache when needed
    public static void clearMovieCache() {
        moviesAlreadyLoaded = false;
        staticMovieCache.clear();
        System.out.println("Movie cache cleared");
    }
}