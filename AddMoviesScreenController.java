package com.example.modifiedcinemasystem;

import com.example.modifiedcinemasystem.AvailableMoviesScreenController;
import com.example.modifiedcinemasystem.MovieData;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddMoviesScreenController implements Initializable {

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

    // Movie form fields
    @FXML
    private TextField titleField;

    @FXML
    private TextField genreField;

    @FXML
    private TextField durationField;

    @FXML
    private TextField ratingField;

    @FXML
    private TextArea castArea;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField imagePathField;

    @FXML
    private ImageView movieImageView;

    @FXML
    private Label imageLabel;

    @FXML
    private Label statusLabel;

    // Action buttons
    @FXML
    private Button selectImageButton;

    @FXML
    private Button addMovieButton;

    @FXML
    private Button clearFormButton;

    @FXML
    private Button previewButton;

    // Labels
    @FXML
    private Label welcomeLabel;

    @FXML
    private Label adminLabel;

    // File paths
    private static final String MOVIES_FILE_PATH = "Movies.txt";
    private static final String IMAGES_FOLDER = "movie_images";

    // Store selected image file
    private File selectedImageFile = null;

    // Flag to track if default movies have been added
    private static boolean defaultMoviesAdded = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Add Movies Screen initialized at " + getCurrentDateTime());
        System.out.println("Current Date and Time (UTC - YYYY-MM-DD HH:MM:SS formatted): " + getCurrentDateTime());
        System.out.println("Current User: Admin"); // Using generic term as requested

        // Create movies file and images directory if they don't exist
        initializeStorage();

        // Add default movies if Movies.txt is empty or doesn't exist
        if (!defaultMoviesAdded && isMoviesFileEmpty()) {
            addDefaultMoviesToFile();
            defaultMoviesAdded = true;
        }

        // Load existing movies from file
        loadExistingMovies();

        // Set initial status
        updateStatus("Ready to add new movie");

        // Initialize image display
        clearImageDisplay();
    }

    // Check if Movies.txt is empty or doesn't exist
    private boolean isMoviesFileEmpty() {
        File moviesFile = new File(MOVIES_FILE_PATH);
        if (!moviesFile.exists()) {
            return true;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(MOVIES_FILE_PATH))) {
            String line = reader.readLine();
            return line == null || line.trim().isEmpty();
        } catch (IOException e) {
            return true;
        }
    }

    // Add default movies from AvailableMoviesScreen.fxml to Movies.txt
    private void addDefaultMoviesToFile() {
        System.out.println("Adding default movies to Movies.txt...");

        try {
            List<MovieData> defaultMovies = createDefaultMovies();

            // Save to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(MOVIES_FILE_PATH, true))) {
                for (MovieData movie : defaultMovies) {
                    String movieLine = String.format("%s|%s|%s|%s|%s|%s|%s|%s",
                            movie.getTitle(),
                            movie.getGenre(),
                            movie.getDuration(),
                            movie.getRating(),
                            movie.getCast().replace("\n", " ").replace("|", ";"),
                            movie.getDescription().replace("\n", " ").replace("|", ";"),
                            movie.getImagePath(),
                            getCurrentDateTime()
                    );
                    writer.write(movieLine);
                    writer.newLine();
                }
            }

            // Add to AvailableMoviesScreen
            for (MovieData movie : defaultMovies) {
                AvailableMoviesScreenController.addNewMovie(movie);
            }

            System.out.println("Added " + defaultMovies.size() + " default movies to Movies.txt");
            updateStatus("Default movies loaded: " + defaultMovies.size() + " movies available");

        } catch (Exception e) {
            System.err.println("Error adding default movies: " + e.getMessage());
        }
    }

    // Create default movie data matching AvailableMoviesScreen.fxml
    private List<MovieData> createDefaultMovies() {
        List<MovieData> defaultMovies = new ArrayList<>();

        // Movie 1: 28 Years Later
        MovieData movie1 = new MovieData(
                "28 Years Later",
                "120 minutes",
                "Horror",
                "Cillian Murphy, Jodie Comer, Aaron Taylor-Johnson",
                "R",
                "The rage virus has evolved. Twenty-eight years after the initial outbreak, a new chapter of terror begins as survivors face an even deadlier strain of the infection.",
                "@28 Years Later.jpg"
        );
        defaultMovies.add(movie1);

        // Movie 2: Final Destination Bloodlines
        MovieData movie2 = new MovieData(
                "Final Destination Bloodlines",
                "110 minutes",
                "Horror/Thriller",
                "Tony Todd, Devon Sawa, Candice Bergen",
                "R",
                "Death returns with a vengeance in this terrifying new chapter of the Final Destination franchise, where a group of survivors must cheat death once again.",
                "@final destination bloodlines.jpg"
        );
        defaultMovies.add(movie2);

        // Movie 3: Mission Impossible
        MovieData movie3 = new MovieData(
                "Mission Impossible",
                "147 minutes",
                "Action/Thriller",
                "Tom Cruise, Rebecca Ferguson, Hayley Atwell, Ving Rhames",
                "PG-13",
                "Ethan Hunt and his IMF team must track down a terrifying new weapon that threatens all of humanity if it falls into the wrong hands.",
                "@mission impossible.jpg"
        );
        defaultMovies.add(movie3);

        // Movie 4: Sinners
        MovieData movie4 = new MovieData(
                "Sinners",
                "105 minutes",
                "Drama/Crime",
                "Michael B. Jordan, Ryan Coogler, Hailee Steinfeld",
                "R",
                "Two brothers return to their hometown to face the demons of their past, but discover that evil still lurks in the shadows of their childhood.",
                "@sinners.jpg"
        );
        defaultMovies.add(movie4);

        // Movie 5: Warfare
        MovieData movie5 = new MovieData(
                "Warfare",
                "130 minutes",
                "War/Action",
                "Will Poulter, D'Pharaoh Woon-A-Tai, Cosmo Jarvis",
                "R",
                "A gripping war drama that follows a group of soldiers during an intense 24-hour period in a conflict zone, captured through the soldiers' helmet cameras.",
                "@warfare.jpg"
        );
        defaultMovies.add(movie5);

        // Movie 6: Mickey 17
        MovieData movie6 = new MovieData(
                "Mickey 17",
                "115 minutes",
                "Sci-Fi",
                "Robert Pattinson, Naomi Ackie, Steven Yeun, Toni Collette",
                "PG-13",
                "A sci-fi thriller about Mickey Barnes, an expendable employee on a human expedition sent to colonize the ice world Niflheim.",
                "@mickey 17.jpg"
        );
        defaultMovies.add(movie6);

        // Movie 7: Avengers Endgame
        MovieData movie7 = new MovieData(
                "Avengers Endgame",
                "181 minutes",
                "Action/Adventure",
                "Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth, Scarlett Johansson",
                "PG-13",
                "The epic conclusion to the Infinity Saga. After the devastating events of Infinity War, the Avengers assemble once more to reverse Thanos' actions and restore balance to the universe.",
                "@avengers endgame.jpg"
        );
        defaultMovies.add(movie7);

        System.out.println("Created " + defaultMovies.size() + " default movies");
        for (MovieData movie : defaultMovies) {
            System.out.println("- " + movie.getTitle() + " (" + movie.getGenre() + ", " + movie.getDuration() + ")");
        }

        return defaultMovies;
    }

    // Initialize storage directories and files
    private void initializeStorage() {
        try {
            // Create images directory
            File imagesDir = new File(IMAGES_FOLDER);
            if (!imagesDir.exists()) {
                boolean created = imagesDir.mkdirs();
                System.out.println("Images directory created: " + created);
            }

            // Create movies file if it doesn't exist
            File moviesFile = new File(MOVIES_FILE_PATH);
            if (!moviesFile.exists()) {
                boolean created = moviesFile.createNewFile();
                System.out.println("Movies file created: " + created);
            }

        } catch (IOException e) {
            System.err.println("Error initializing storage: " + e.getMessage());
        }
    }

    // Load existing movies from file to memory
    private void loadExistingMovies() {
        try (BufferedReader reader = new BufferedReader(new FileReader(MOVIES_FILE_PATH))) {
            String line;
            int movieCount = 0;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    MovieData movieData = parseMovieFromLine(line);
                    if (movieData != null) {
                        // Add to AvailableMoviesScreen without marking as displayed
                        movieData.setDisplayed(false);
                        AvailableMoviesScreenController.addNewMovie(movieData);
                        movieCount++;
                    }
                }
            }

            System.out.println("Loaded " + movieCount + " existing movies from file");

        } catch (IOException e) {
            System.out.println("No existing movies file found or error reading file: " + e.getMessage());
        }
    }

    // Parse movie data from file line
    private MovieData parseMovieFromLine(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length >= 7) {
                return new MovieData(
                        parts[0], // title
                        parts[2], // duration
                        parts[1], // genre
                        parts[4], // cast
                        parts[3], // rating
                        parts[5], // description
                        parts[6]  // imagePath
                );
            }
        } catch (Exception e) {
            System.err.println("Error parsing movie line: " + e.getMessage());
        }
        return null;
    }

    // Navigation Methods

    @FXML
    private void handleDashboard(ActionEvent event) {
        navigateToScreen("DashboardScreen.fxml", "Dashboard - Cinema Management System");
    }

    @FXML
    private void handleAddMovies(ActionEvent event) {
        System.out.println("Already on Add Movies screen - refreshing at " + getCurrentDateTime());
        handleClearForm(null);
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

    // Movie Management Methods

    @FXML
    private void handleSelectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Movie Poster");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        Stage stage = getCurrentStage();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                // Store the selected file for later copying
                selectedImageFile = selectedFile;

                // Display the image immediately
                Image image = new Image(selectedFile.toURI().toString());
                movieImageView.setImage(image);
                imageLabel.setVisible(false);

                // Show the original path in the field (will be updated when movie is saved)
                imagePathField.setText(selectedFile.getName());

                updateStatus("Image selected: " + selectedFile.getName());
                System.out.println("Image selected: " + selectedFile.getAbsolutePath());

            } catch (Exception e) {
                updateStatus("Error loading image: " + e.getMessage());
                System.err.println("Error loading image: " + e.getMessage());
                selectedImageFile = null;
            }
        }
    }

    @FXML
    private void handleAddMovieToSystem(ActionEvent event) {
        if (validateMovieData()) {
            try {
                // Create MovieData object
                MovieData movieData = createMovieData();

                // Copy image to images folder if selected
                String finalImagePath = copyImageToStorage(movieData.getTitle());
                movieData.setImagePath(finalImagePath);

                // Save to file
                saveMovieToFile(movieData);

                // Add to AvailableMoviesScreen
                AvailableMoviesScreenController.addNewMovie(movieData);

                updateStatus("Movie '" + movieData.getTitle() + "' added successfully!");
                System.out.println("Movie added to system: " + movieData.getTitle());
                System.out.println("Image path: " + finalImagePath);
                System.out.println("Movie saved to file: " + MOVIES_FILE_PATH);

                // Show success dialog
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Movie Added Successfully");
                alert.setContentText("The movie '" + movieData.getTitle() + "' has been added to the system and saved to file.");
                alert.showAndWait();

                // Clear form after successful addition
                handleClearForm(null);

            } catch (Exception e) {
                updateStatus("Error adding movie: " + e.getMessage());
                System.err.println("Error adding movie: " + e.getMessage());
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to Add Movie");
                alert.setContentText("An error occurred while adding the movie: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    // Copy selected image to storage folder
    private String copyImageToStorage(String movieTitle) {
        if (selectedImageFile == null || !selectedImageFile.exists()) {
            System.out.println("No image selected or file doesn't exist");
            return "";
        }

        try {
            // Create safe filename
            String safeTitle = movieTitle.replaceAll("[^a-zA-Z0-9]", "_");
            String extension = getFileExtension(selectedImageFile.getName());
            String newFileName = safeTitle + "_" + System.currentTimeMillis() + extension;

            // Copy file to images directory
            Path sourcePath = selectedImageFile.toPath();
            Path targetPath = Paths.get(IMAGES_FOLDER, newFileName);

            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            String relativePath = "@" + IMAGES_FOLDER + "/" + newFileName;
            System.out.println("Image copied to: " + targetPath.toAbsolutePath());
            System.out.println("Relative path for JavaFX: " + relativePath);

            return relativePath;

        } catch (IOException e) {
            System.err.println("Error copying image: " + e.getMessage());
            return "";
        }
    }

    // Get file extension
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex);
        }
        return ".jpg"; // default extension
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        titleField.clear();
        genreField.clear();
        durationField.clear();
        ratingField.clear();
        castArea.clear();
        descriptionArea.clear();
        imagePathField.clear();

        selectedImageFile = null;
        clearImageDisplay();
        updateStatus("Form cleared - Ready to add new movie");
        System.out.println("Movie form cleared at " + getCurrentDateTime());
    }

    @FXML
    private void handlePreviewMovie(ActionEvent event) {
        if (validateMovieData()) {
            MovieData movieData = createMovieData();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Movie Preview");
            alert.setHeaderText("Preview: " + movieData.getTitle());
            alert.setContentText(
                    "Genre: " + movieData.getGenre() + "\n" +
                            "Duration: " + movieData.getDuration() + "\n" +
                            "Rating: " + movieData.getRating() + "\n" +
                            "Cast: " + movieData.getCast() + "\n" +
                            "Description: " + movieData.getDescription() + "\n" +
                            "Image: " + (selectedImageFile != null ? selectedImageFile.getName() : "No image")
            );
            alert.showAndWait();

            updateStatus("Movie preview displayed");
        }
    }

    // Helper Methods

    private boolean validateMovieData() {
        if (titleField.getText().trim().isEmpty()) {
            showValidationError("Movie title is required!");
            titleField.requestFocus();
            return false;
        }

        if (genreField.getText().trim().isEmpty()) {
            showValidationError("Genre is required!");
            genreField.requestFocus();
            return false;
        }

        if (durationField.getText().trim().isEmpty()) {
            showValidationError("Duration is required!");
            durationField.requestFocus();
            return false;
        }

        if (ratingField.getText().trim().isEmpty()) {
            showValidationError("Rating is required!");
            ratingField.requestFocus();
            return false;
        }

        return true;
    }

    private void showValidationError(String message) {
        updateStatus("Validation Error: " + message);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Required Field Missing");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private MovieData createMovieData() {
        String title = titleField.getText().trim();
        String genre = genreField.getText().trim();
        String duration = durationField.getText().trim();
        String rating = ratingField.getText().trim();
        String cast = castArea.getText().trim();
        String description = descriptionArea.getText().trim();
        String imagePath = ""; // Will be set after copying

        return new MovieData(title, duration, genre, cast, rating, description, imagePath);
    }

    private void saveMovieToFile(MovieData movieData) throws IOException {
        // Ensure the file exists
        File moviesFile = new File(MOVIES_FILE_PATH);
        if (!moviesFile.exists()) {
            moviesFile.createNewFile();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MOVIES_FILE_PATH, true))) {
            // Format: Title|Genre|Duration|Rating|Cast|Description|ImagePath|DateTime
            String movieLine = String.format("%s|%s|%s|%s|%s|%s|%s|%s",
                    movieData.getTitle(),
                    movieData.getGenre(),
                    movieData.getDuration(),
                    movieData.getRating(),
                    movieData.getCast().replace("\n", " ").replace("|", ";"),
                    movieData.getDescription().replace("\n", " ").replace("|", ";"),
                    movieData.getImagePath(),
                    getCurrentDateTime()
            );

            writer.write(movieLine);
            writer.newLine();
            writer.flush();

            System.out.println("Movie data written to file: " + movieLine);
            System.out.println("File size after write: " + moviesFile.length() + " bytes");
        }
    }

    private void clearImageDisplay() {
        movieImageView.setImage(null);
        imageLabel.setVisible(true);
        imageLabel.setText("No Image Selected");
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    // Navigation helper
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
        if (addMovieButton != null && addMovieButton.getScene() != null) {
            return (Stage) addMovieButton.getScene().getWindow();
        } else if (dashboardButton != null && dashboardButton.getScene() != null) {
            return (Stage) dashboardButton.getScene().getWindow();
        }
        return null;
    }

    private String getCurrentDateTime() {
        return "2025-06-05 16:37:57"; // Using your provided timestamp
    }

    // Public method to get movies file path
    public static String getMoviesFilePath() {
        return MOVIES_FILE_PATH;
    }

    // Public method to reload movies (useful for other screens)
    public static List<MovieData> loadMoviesFromFile() {
        List<MovieData> movies = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(MOVIES_FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        String[] parts = line.split("\\|");
                        if (parts.length >= 7) {
                            MovieData movieData = new MovieData(
                                    parts[0], // title
                                    parts[2], // duration
                                    parts[1], // genre
                                    parts[4], // cast
                                    parts[3], // rating
                                    parts[5], // description
                                    parts[6]  // imagePath
                            );
                            movies.add(movieData);
                        }
                    } catch (Exception e) {
                        System.err.println("Error parsing movie line: " + e.getMessage());
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading movies file: " + e.getMessage());
        }

        return movies;
    }

    // Public method to force add default movies (can be called from other classes)
    public static void forceAddDefaultMovies() {
        AddMoviesScreenController controller = new AddMoviesScreenController();
        if (controller.isMoviesFileEmpty()) {
            controller.addDefaultMoviesToFile();
        }
    }
}