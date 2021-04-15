package com.sk8.swashbucklers.model.employee;

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
 * Represents an Employee
 *
 * @author Daniel Bernier
 */
@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue
    @Column(name = "employee_id")
    private int employeeId;
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
    @Column(name = "phone", nullable = false, unique = true)
    private String phoneNumber;
    @ManyToOne(cascade=CascadeType.ALL)
    private Location location;
    @Column(name = "rank", nullable = false)
    private Rank rank;

    /**
     * @param employeeId
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @param phoneNumber
     * @param location
     * @param rank
     */
    public Employee(int employeeId, @Size(min = 1) String firstName, @Size(min = 1) String lastName, @Email String email, @Size(min = 1) String password, @Size(min = 10, max = 10) String phoneNumber, Location location, Rank rank) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.rank = rank;
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
