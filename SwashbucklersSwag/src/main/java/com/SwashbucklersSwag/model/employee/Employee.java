package com.SwashbucklersSwag.model.employee;

import com.SwashbucklersSwag.model.location.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import javax.persistence.*;

/**
 * Represents an Employee
 *
 * @author Daniel Bernier
 */
@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
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
    @Column(name = "phone", nullable = false)
    private String phoneNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private Location location;
    @Column(nullable = false)
    private Rank rank;

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
