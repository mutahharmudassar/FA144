package com.example.modifiedcinemasystem;

import java.io.Serializable;

public class PaymentData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String paymentId;
    private String customerName;
    private String paymentMethod;
    private double amount;
    private String paymentDate;
    private String paymentStatus;
    private String transactionId;
    private String bookingId;

    // Constructors
    public PaymentData() {
        this.paymentStatus = "Pending";
    }

    public PaymentData(String customerName, String paymentMethod, double amount) {
        this();
        this.customerName = customerName;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }

    // Getters and Setters
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    @Override
    public String toString() {
        return "PaymentData{" +
                "paymentId='" + paymentId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", amount=" + amount +
                ", status='" + paymentStatus + '\'' +
                '}';
    }
}
