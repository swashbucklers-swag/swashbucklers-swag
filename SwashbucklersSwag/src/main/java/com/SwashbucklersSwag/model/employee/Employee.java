package com.SwashbucklersSwag.model.employee;

import com.SwashbucklersSwag.model.location.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import javax.persistence.*;
import java.util.Objects;

/**
 * Represents an Employee
 *
 * @author Daniel Bernier
 */
@Entity
@Table(name = "employees")
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
    private String password;
    @Size(min = 10, max = 10)
    @Column(name = "phone", nullable = false)
    private String phoneNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Location location;
    @Column(nullable = false)
    private Rank rank;

    public Employee(){}

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

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
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

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return employeeId == employee.employeeId && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && Objects.equals(email, employee.email) && Objects.equals(password, employee.password) && Objects.equals(phoneNumber, employee.phoneNumber) && Objects.equals(location, employee.location) && rank == employee.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, firstName, lastName, email, password, phoneNumber, location, rank);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", location=" + location +
                ", rank=" + rank +
                '}';
    }
}
