package com.SwashbucklersSwag.model.location;

import jakarta.validation.constraints.Size;
import lombok.*;
import javax.persistence.*;

/**
 * Represents a location with necessary information for the delivery of a package
 *
 * @author Edson Rodriguez
 * @author Daniel Bernier
 */

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
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
