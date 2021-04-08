package com.SwashbucklersSwag.model.location;

import jakarta.validation.constraints.Size;

import javax.persistence.*;
import java.util.Objects;

/**
 * Represents a location with necessary information for the delivery of a package
 *
 * @author Edson Rodriguez
 * @author Daniel Bernier
 */

@Entity
@Table(name = "locations")
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

    public Location() {}

    public Location(int locationId, @Size(min = 1) String address, @Size(min = 1) String city, State state, @Size(min = 1) String zip) {
        this.locationId = locationId;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return locationId == location.locationId && Objects.equals(address, location.address) && Objects.equals(city, location.city) && state == location.state && Objects.equals(zip, location.zip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, address, city, state, zip);
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationId=" + locationId +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state=" + state +
                ", zip='" + zip + '\'' +
                '}';
    }
}
