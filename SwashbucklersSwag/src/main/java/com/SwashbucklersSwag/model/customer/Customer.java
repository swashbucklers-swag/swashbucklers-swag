package com.SwashbucklersSwag.model.customer;

import com.SwashbucklersSwag.model.location.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

/**
 * Represents a customer
 *
 * @author Daniel Bernier
 */
public class Customer {

    @Id
    @GeneratedValue
    @Column(name = "customer_id")
    private int customerId;
    @Size(min = 1)
    @Column(name = "firstname", nullable = false)
    private String firstName;
    @Size(min = 1)
    @Column(name = "lastname", nullable = false)
    private String lastName;
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    @Size(min = 1)
    @Column(nullable = false)
    private String password;
    @Size(min = 10, max = 10)
    @Column(nullable = false)
    private String phoneNumber;
    @ManyToOne
    @Column(nullable = false)
    private Location location;

    public Customer(){}

    public Customer(int customerId, @Size(min = 1) String firstName, @Size(min = 1) String lastName, @Email String email, @Size(min = 1) String password, @Size(min = 10, max = 10) String phoneNumber, Location location) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //Jackson annotations to disallow the getting of password through JSON
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    //Jackson annotations to allow the setting of password through JSON
    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerId == customer.customerId && Objects.equals(firstName, customer.firstName) && Objects.equals(lastName, customer.lastName) && Objects.equals(email, customer.email) && Objects.equals(password, customer.password) && Objects.equals(phoneNumber, customer.phoneNumber) && Objects.equals(location, customer.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, firstName, lastName, email, password, phoneNumber, location);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", location=" + location +
                '}';
    }
}
