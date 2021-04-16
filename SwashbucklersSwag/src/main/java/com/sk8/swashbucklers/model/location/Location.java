package com.sk8.swashbucklers.model.location;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Represents a location with necessary information for the delivery of a package
 *
 * @author Daniel Bernier
 * @author Edson Rodriguez
 */
@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue
    @Column(name="location_id")
    private int locationId;
    @Size(min = 1)
    @Column(nullable = false)
    private String address;
    @Size(min = 1)
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private State state;
    @Size(min = 1)
    @Column(nullable = false)
    private String zip;

    public Location(int locationId, @Size(min = 1) String address, @Size(min = 1) String city, State state, @Size(min = 1) String zip) {
        this.locationId = locationId;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
}
