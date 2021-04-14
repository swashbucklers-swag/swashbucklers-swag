package com.sk8.swashbucklers.model.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sk8.swashbucklers.model.location.Location;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * Represents a customer
 *
 * @author Daniel Bernier
 */
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
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
    @Setter(AccessLevel.NONE)
    private String password;
    @Size(min = 10, max = 10)
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @ManyToOne
    private Location location;

    public Customer(int customerId, @Size(min = 1) String firstName, @Size(min = 1) String lastName, @Email String email, @Size(min = 1) String password, @Size(min = 10, max = 10) String phoneNumber, Location location) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.location = location;
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
}
