package com.example.modifiedcinemasystem;

import java.io.Serializable;

public class ReviewData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reviewId;
    private String customerName;
    private String movieTitle;
    private double cinemaQualityRating;
    private double websiteQualityRating;
    private double filmRating;
    private double overallRating;
    private double averageRating;
    private String customerEmail;
    private String reviewTitle;
    private String reviewText;
    private String reviewDate;

    // Constructors
    public ReviewData() {
        this.cinemaQualityRating = 0.0;
        this.websiteQualityRating = 0.0;
        this.filmRating = 0.0;
        this.overallRating = 0.0;
        this.averageRating = 0.0;
    }

    public ReviewData(String customerName, String movieTitle) {
        this();
        this.customerName = customerName;
        this.movieTitle = movieTitle;
    }

    // Getters and Setters
    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public double getCinemaQualityRating() { return cinemaQualityRating; }
    public void setCinemaQualityRating(double cinemaQualityRating) { this.cinemaQualityRating = cinemaQualityRating; }

    public double getWebsiteQualityRating() { return websiteQualityRating; }
    public void setWebsiteQualityRating(double websiteQualityRating) { this.websiteQualityRating = websiteQualityRating; }

    public double getFilmRating() { return filmRating; }
    public void setFilmRating(double filmRating) { this.filmRating = filmRating; }

    public double getOverallRating() { return overallRating; }
    public void setOverallRating(double overallRating) { this.overallRating = overallRating; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getReviewTitle() { return reviewTitle; }
    public void setReviewTitle(String reviewTitle) { this.reviewTitle = reviewTitle; }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public String getReviewDate() { return reviewDate; }
    public void setReviewDate(String reviewDate) { this.reviewDate = reviewDate; }

    @Override
    public String toString() {
        return "ReviewData{" +
                "reviewId='" + reviewId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                ", averageRating=" + averageRating +
                ", reviewTitle='" + reviewTitle + '\'' +
                '}';
    }
}
