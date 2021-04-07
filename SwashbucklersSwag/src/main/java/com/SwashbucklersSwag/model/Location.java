package com.SwashbucklersSwag.model;

import javax.persistence.*;

@Entity
public class Location {
    @Id
    @GeneratedValue
    @Column(name="location_id")
    private int locationId;
    @Column
    private String address;
    @Column
    private String city;
    @Column
//    @OneToMany(mappedBy = "state_id") //TODO: Do we need mapped??
    private State state;
    @Column
    private String zip;

    public Location() {
    }

    public Location(int locationId, String address, String city, State state, String zip) {
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
}
