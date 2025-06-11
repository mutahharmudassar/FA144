package com.example.modifiedcinemasystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String CUSTOMER_FILE = "Customer.txt";

    private String email;
    private String username;
    private String password;

    // Constructors
    public Customer() {}

    public Customer(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // File Methods using ObjectInputStream and ObjectOutputStream

    /**
     * Save a single customer to file
     */
    public void saveToFile() {
        List<Customer> customers = loadAllCustomers();
        customers.add(this);
        saveAllCustomers(customers);
    }

    /**
     * Save all customers to file using ObjectOutputStream
     */
    public static void saveAllCustomers(List<Customer> customers) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CUSTOMER_FILE))) {
            oos.writeObject(customers);
            System.out.println("Customers saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }

    /**
     * Load all customers from file using ObjectInputStream
     */
    @SuppressWarnings("unchecked")
    public static List<Customer> loadAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CUSTOMER_FILE))) {
            customers = (List<Customer>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Customer file not found. Creating new file.");
            // Create empty file for first time
            saveAllCustomers(new ArrayList<>());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
        return customers;
    }

    /**
     * Validate customer credentials (case-insensitive username)
     */
    public static boolean validateCustomer(String username, String password) {
        List<Customer> customers = loadAllCustomers();
        for (Customer customer : customers) {
            if (customer.getUsername().equalsIgnoreCase(username) && customer.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if username already exists (case-insensitive)
     */
    public static boolean usernameExists(String username) {
        List<Customer> customers = loadAllCustomers();
        for (Customer customer : customers) {
            if (customer.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if email already exists (case-insensitive for email too)
     */
    public static boolean emailExists(String email) {
        List<Customer> customers = loadAllCustomers();
        for (Customer customer : customers) {
            if (customer.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find customer by username (case-insensitive)
     */
    public static Customer findByUsername(String username) {
        List<Customer> customers = loadAllCustomers();
        for (Customer customer : customers) {
            if (customer.getUsername().equalsIgnoreCase(username)) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Find customer by email (case-insensitive)
     */
    public static Customer findByEmail(String email) {
        List<Customer> customers = loadAllCustomers();
        for (Customer customer : customers) {
            if (customer.getEmail().equalsIgnoreCase(email)) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Update customer information (case-insensitive username lookup)
     */
    public boolean updateCustomer() {
        List<Customer> customers = loadAllCustomers();
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getUsername().equalsIgnoreCase(this.username)) {
                customers.set(i, this);
                saveAllCustomers(customers);
                return true;
            }
        }
        return false;
    }

    /**
     * Delete customer by username (case-insensitive)
     */
    public static boolean deleteCustomer(String username) {
        List<Customer> customers = loadAllCustomers();
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getUsername().equalsIgnoreCase(username)) {
                customers.remove(i);
                saveAllCustomers(customers);
                return true;
            }
        }
        return false;
    }

    /**
     * Get total number of customers
     */
    public static int getTotalCustomers() {
        return loadAllCustomers().size();
    }

    /**
     * Display all customers (for admin purposes)
     */
    public static void displayAllCustomers() {
        List<Customer> customers = loadAllCustomers();
        System.out.println("=== All Customers ===");
        for (Customer customer : customers) {
            System.out.println("Email: " + customer.getEmail() +
                    ", Username: " + customer.getUsername());
        }
        System.out.println("Total Customers: " + customers.size());
    }

    /**
     * Get customer login credentials (case-insensitive username)
     * Returns the actual customer object for the matched username
     */
    public static Customer getCustomerCredentials(String username, String password) {
        List<Customer> customers = loadAllCustomers();
        for (Customer customer : customers) {
            if (customer.getUsername().equalsIgnoreCase(username) && customer.getPassword().equals(password)) {
                return customer;
            }
        }
        return null;
    }

    // Override toString for display purposes
    @Override
    public String toString() {
        return "Customer{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    // Override equals and hashCode for proper comparison (case-insensitive username)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Customer customer = (Customer) obj;
        return username.equalsIgnoreCase(customer.username);
    }

    @Override
    public int hashCode() {
        return username.toLowerCase().hashCode();
    }
}
