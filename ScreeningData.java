package com.example.modifiedcinemasystem;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScreeningData {
    private int screeningId;
    private String movieTitle;
    private LocalDate screeningDate;
    private LocalTime screeningTime;
    private String screenType;
    private String cinemaHall;
    private int totalSeats;
    private int availableSeats;
    private double pricePerTicket;
    private String status;
    private String createdDateTime;

    // Constructors
    public ScreeningData() {
        this.status = "Active";
        this.availableSeats = 0;
        this.screeningId = generateId();
    }

    public ScreeningData(String movieTitle, LocalDate screeningDate, LocalTime screeningTime,
                         String screenType, String cinemaHall, int totalSeats, double pricePerTicket) {
        this();
        this.movieTitle = movieTitle;
        this.screeningDate = screeningDate;
        this.screeningTime = screeningTime;
        this.screenType = screenType;
        this.cinemaHall = cinemaHall;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.pricePerTicket = pricePerTicket;
    }

    // Generate unique ID
    private int generateId() {
        return (int) (System.currentTimeMillis() % 100000);
    }

    // Getters and Setters
    public int getScreeningId() { return screeningId; }
    public void setScreeningId(int screeningId) { this.screeningId = screeningId; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public LocalDate getScreeningDate() { return screeningDate; }
    public void setScreeningDate(LocalDate screeningDate) { this.screeningDate = screeningDate; }

    public LocalTime getScreeningTime() { return screeningTime; }
    public void setScreeningTime(LocalTime screeningTime) { this.screeningTime = screeningTime; }

    public String getScreenType() { return screenType; }
    public void setScreenType(String screenType) { this.screenType = screenType; }

    public String getCinemaHall() { return cinemaHall; }
    public void setCinemaHall(String cinemaHall) { this.cinemaHall = cinemaHall; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public double getPricePerTicket() { return pricePerTicket; }
    public void setPricePerTicket(double pricePerTicket) { this.pricePerTicket = pricePerTicket; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedDateTime() { return createdDateTime; }
    public void setCreatedDateTime(String createdDateTime) { this.createdDateTime = createdDateTime; }

    // Business methods
    public boolean bookSeats(int numberOfSeats) {
        if (availableSeats >= numberOfSeats) {
            availableSeats -= numberOfSeats;
            return true;
        }
        return false;
    }

    public void releaseSeats(int numberOfSeats) {
        availableSeats = Math.min(totalSeats, availableSeats + numberOfSeats);
    }

    public boolean isFull() {
        return availableSeats == 0;
    }

    public double getOccupancyPercentage() {
        if (totalSeats == 0) return 0.0;
        return ((double) (totalSeats - availableSeats) / totalSeats) * 100.0;
    }

    @Override
    public String toString() {
        return "ScreeningData{" +
                "screeningId=" + screeningId +
                ", movieTitle='" + movieTitle + '\'' +
                ", screeningDate=" + screeningDate +
                ", screeningTime=" + screeningTime +
                ", screenType='" + screenType + '\'' +
                ", cinemaHall='" + cinemaHall + '\'' +
                ", availableSeats=" + availableSeats + "/" + totalSeats +
                ", price=" + pricePerTicket +
                '}';
    }
}
