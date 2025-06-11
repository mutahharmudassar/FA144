package com.example.modifiedcinemasystem;

import java.io.Serializable;

public class MovieData implements Serializable {
    private static final long serialVersionUID = 1L;

    private int movieId;
    private String title;
    private String genre;
    private String director;
    private String cast;
    private double rating;
    private String duration;
    private String releaseDate;
    private String description;
    private String imagePath;

    // Additional fields for compatibility
    private String ratingString; // For string-based ratings like "PG-13", "R"
    private boolean displayed = false; // For tracking display status
    private String dateTimeAdded; // For tracking when movie was added

    // Default Constructor
    public MovieData() {
        this.rating = 0.0;
        this.displayed = false;
    }

    // Constructor for SearchMoviesController (title, genre, director, cast, rating)
    public MovieData(String title, String genre, String director, String cast, double rating) {
        this();
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.cast = cast;
        this.rating = rating;
    }

    // Constructor for AddMoviesScreenController (title, duration, genre, cast, ratingString, description, imagePath)
    public MovieData(String title, String duration, String genre, String cast, String ratingString, String description, String imagePath) {
        this();
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.cast = cast;
        this.ratingString = ratingString;
        this.description = description;
        this.imagePath = imagePath;

        // Try to parse rating string to double for compatibility
        this.rating = parseRatingFromString(ratingString);
    }

    // Full constructor with all fields
    public MovieData(int movieId, String title, String genre, String director, String cast,
                     double rating, String duration, String releaseDate, String description, String imagePath) {
        this();
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.director = director;
        this.cast = cast;
        this.rating = rating;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.description = description;
        this.imagePath = imagePath;
    }

    // Helper method to parse rating from string
    private double parseRatingFromString(String ratingStr) {
        if (ratingStr == null || ratingStr.trim().isEmpty()) {
            return 0.0;
        }

        // Try to extract numeric rating
        try {
            // If it's already a number, parse it
            return Double.parseDouble(ratingStr);
        } catch (NumberFormatException e) {
            // If it's a string rating like "PG-13", assign default values
            switch (ratingStr.toUpperCase()) {
                case "G": return 6.0;
                case "PG": return 6.5;
                case "PG-13": return 7.0;
                case "R": return 7.5;
                case "NC-17": return 8.0;
                default: return 7.0; // Default rating
            }
        }
    }

    // Getters and Setters
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public String getTitle() { return title != null ? title : ""; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre != null ? genre : ""; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getDirector() { return director != null ? director : ""; }
    public void setDirector(String director) { this.director = director; }

    public String getCast() { return cast != null ? cast : ""; }
    public void setCast(String cast) { this.cast = cast; }

    public double getRating() { return rating; }
    public void setRating(double rating) {
        this.rating = rating;
        this.ratingString = String.valueOf(rating);
    }

    public String getRatingString() { return ratingString != null ? ratingString : String.valueOf(rating); }
    public void setRatingString(String ratingString) {
        this.ratingString = ratingString;
        this.rating = parseRatingFromString(ratingString);
    }

    public String getDuration() { return duration != null ? duration : ""; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getReleaseDate() { return releaseDate != null ? releaseDate : ""; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getDescription() { return description != null ? description : ""; }
    public void setDescription(String description) { this.description = description; }

    public String getImagePath() { return imagePath != null ? imagePath : ""; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public boolean isDisplayed() { return displayed; }
    public void setDisplayed(boolean displayed) { this.displayed = displayed; }

    public String getDateTimeAdded() { return dateTimeAdded; }
    public void setDateTimeAdded(String dateTimeAdded) { this.dateTimeAdded = dateTimeAdded; }

    // Utility methods for rating conversion
    public String getRatingAsString() {
        if (ratingString != null && !ratingString.trim().isEmpty()) {
            return ratingString;
        }
        return String.format("%.1f", rating);
    }

    public void setRatingFromString(String rating) {
        this.ratingString = rating;
        this.rating = parseRatingFromString(rating);
    }

    // Method to get formatted rating for display
    public String getDisplayRating() {
        if (ratingString != null && !ratingString.matches("\\d+(\\.\\d+)?")) {
            // If it's a text rating like "PG-13"
            return ratingString + " (" + String.format("%.1f", rating) + "/10)";
        } else {
            // If it's a numeric rating
            return String.format("%.1f/10", rating);
        }
    }

    // Helper method to check if movie has complete information
    public boolean isComplete() {
        return title != null && !title.trim().isEmpty() &&
                genre != null && !genre.trim().isEmpty() &&
                duration != null && !duration.trim().isEmpty();
    }

    // Method to create a copy of the movie
    public MovieData copy() {
        MovieData copy = new MovieData();
        copy.movieId = this.movieId;
        copy.title = this.title;
        copy.genre = this.genre;
        copy.director = this.director;
        copy.cast = this.cast;
        copy.rating = this.rating;
        copy.ratingString = this.ratingString;
        copy.duration = this.duration;
        copy.releaseDate = this.releaseDate;
        copy.description = this.description;
        copy.imagePath = this.imagePath;
        copy.displayed = this.displayed;
        copy.dateTimeAdded = this.dateTimeAdded;
        return copy;
    }

    // Static factory methods for common use cases
    public static MovieData createBasicMovie(String title, String genre, String duration) {
        MovieData movie = new MovieData();
        movie.setTitle(title);
        movie.setGenre(genre);
        movie.setDuration(duration);
        return movie;
    }

    public static MovieData createMovieWithRating(String title, String genre, String duration, double rating) {
        MovieData movie = createBasicMovie(title, genre, duration);
        movie.setRating(rating);
        return movie;
    }

    public static MovieData createMovieWithStringRating(String title, String genre, String duration, String rating) {
        MovieData movie = createBasicMovie(title, genre, duration);
        movie.setRatingString(rating);
        return movie;
    }

    // Override toString for debugging
    @Override
    public String toString() {
        return "MovieData{" +
                "movieId=" + movieId +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", director='" + director + '\'' +
                ", rating=" + rating +
                ", ratingString='" + ratingString + '\'' +
                ", duration='" + duration + '\'' +
                ", displayed=" + displayed +
                '}';
    }

    // Override equals and hashCode for proper comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        MovieData movieData = (MovieData) obj;
        return movieId == movieData.movieId ||
                (title != null && title.equals(movieData.title));
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }
}