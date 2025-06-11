package com.example.modifiedcinemasystem;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CustomerBookingData {
    private int customerId;
    private String customerName;
    private String movieTitle;
    private LocalDate screeningDate;
    private LocalTime screeningTime;
    private String screenType;
    private String cinemaHall;
    private int seatsBooked;
    private double totalPrice;
    private LocalDateTime bookingDateTime;
    private String customerEmail;
    private String customerPhone;
    private String bookingStatus;

    // Constructors
    public CustomerBookingData() {
        this.customerId = generateId();
        this.bookingDateTime = LocalDateTime.now();
        this.bookingStatus = "Confirmed";
    }

    public CustomerBookingData(String customerName, String movieTitle, LocalDate screeningDate,
                               LocalTime screeningTime, String screenType, String cinemaHall,
                               int seatsBooked, double totalPrice) {
        this();
        this.customerName = customerName;
        this.movieTitle = movieTitle;
        this.screeningDate = screeningDate;
        this.screeningTime = screeningTime;
        this.screenType = screenType;
        this.cinemaHall = cinemaHall;
        this.seatsBooked = seatsBooked;
        this.totalPrice = totalPrice;
    }

    // Generate unique ID
    private int generateId() {
        return (int) (System.currentTimeMillis() % 100000);
    }

    // Getters and Setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

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

    public int getSeatsBooked() { return seatsBooked; }
    public void setSeatsBooked(int seatsBooked) { this.seatsBooked = seatsBooked; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public LocalDateTime getBookingDateTime() { return bookingDateTime; }
    public void setBookingDateTime(LocalDateTime bookingDateTime) { this.bookingDateTime = bookingDateTime; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }

    // Utility methods
    public double getPricePerTicket() {
        return seatsBooked > 0 ? totalPrice / seatsBooked : 0.0;
    }

    public boolean isUpcoming() {
        return screeningDate.isAfter(LocalDate.now()) ||
                (screeningDate.equals(LocalDate.now()) && screeningTime.isAfter(LocalTime.now()));
    }

    @Override
    public String toString() {
        return "CustomerBookingData{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                ", screeningDate=" + screeningDate +
                ", screeningTime=" + screeningTime +
                ", seatsBooked=" + seatsBooked +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
