package com.example.modifiedcinemasystem;

import java.io.Serializable;

public class BookingData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bookingId;
    private String customerName;
    private String phoneNumber;
    private String email;
    private String movieTitle;
    private String screeningDate;
    private String screeningTime;
    private String cinemaHall;
    private String seatNumbers;
    private int numberOfTickets;
    private double totalAmount;
    private String bookingDate;
    private String bookingStatus;
    private String paymentMethod;
    private String ticketCode;

    // Constructors
    public BookingData() {
        this.bookingStatus = "Pending";
    }

    public BookingData(String customerName, String movieTitle, String screeningDate,
                       String screeningTime, int numberOfTickets, double totalAmount) {
        this();
        this.customerName = customerName;
        this.movieTitle = movieTitle;
        this.screeningDate = screeningDate;
        this.screeningTime = screeningTime;
        this.numberOfTickets = numberOfTickets;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }

    public String getScreeningDate() { return screeningDate; }
    public void setScreeningDate(String screeningDate) { this.screeningDate = screeningDate; }

    public String getScreeningTime() { return screeningTime; }
    public void setScreeningTime(String screeningTime) { this.screeningTime = screeningTime; }

    public String getCinemaHall() { return cinemaHall; }
    public void setCinemaHall(String cinemaHall) { this.cinemaHall = cinemaHall; }

    public String getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(String seatNumbers) { this.seatNumbers = seatNumbers; }

    public int getNumberOfTickets() { return numberOfTickets; }
    public void setNumberOfTickets(int numberOfTickets) { this.numberOfTickets = numberOfTickets; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getTicketCode() { return ticketCode; }
    public void setTicketCode(String ticketCode) { this.ticketCode = ticketCode; }

    @Override
    public String toString() {
        return "BookingData{" +
                "bookingId='" + bookingId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                ", screeningDate='" + screeningDate + '\'' +
                ", numberOfTickets=" + numberOfTickets +
                ", totalAmount=" + totalAmount +
                ", status='" + bookingStatus + '\'' +
                '}';
    }
}
