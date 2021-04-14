package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.dto.LocationDTO;
import com.sk8.swashbucklers.model.location.Location;
import com.sk8.swashbucklers.model.location.State;
import com.sk8.swashbucklers.repo.location.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Service for getting locations with various constraints
 *
 * @author Daniel Bernier
 */
@Service
public class LocationService {

    private final LocationRepository LOCATION_REPO;

    /**
     * Constructor with location repository injected with spring
     * @param locationRepository The location repository to be used throughout
     */
    @Autowired
    public LocationService(LocationRepository locationRepository){
        this.LOCATION_REPO = locationRepository;
    }

    /**
     * Gets all locations using {@link LocationRepository}
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["quantity"|"name"|"description"|"price"|"discount"|"itemId"] defaults to itemId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all locations with pagination and sorting applied
     */
    public Page<LocationDTO> getAllLocations(int page, int offset, String sortBy, String order){
        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);

        Page<Location> locations;
        if(order.equalsIgnoreCase("DESC"))
            locations = LOCATION_REPO.findAll(PageRequest.of(page, offset, Sort.by(sortBy).descending()));
        else
            locations = LOCATION_REPO.findAll(PageRequest.of(page, offset, Sort.by(sortBy).ascending()));

        return locations.map(LocationDTO.locationToDTO());
    }

    /**
     * Gets location by location id
     * @param id The id of the location being requested
     * @return data transfer representation of location
     */
    public LocationDTO getLocationById(int id){
        Optional<Location> location = LOCATION_REPO.findById(id);
        return location.map(value -> LocationDTO.locationToDTO().apply(value)).orElse(null);
    }

    /**
     * Gets all locations with city like provided city string using {@link LocationRepository}
     * @param city The city being searched for
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["quantity"|"name"|"description"|"price"|"discount"|"itemId"] defaults to itemId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all locations with pagination and sorting applied
     */
    public Page<LocationDTO> getAllLocationsByCity(String city, int page, int offset, String sortBy, String order){
        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);

        Page<Location> locations;
        if(order.equalsIgnoreCase("desc"))
            locations = LOCATION_REPO.findByCityContainingIgnoreCase(city, PageRequest.of(page, offset, Sort.by(sortBy).descending()));
        else
            locations = LOCATION_REPO.findByCityContainingIgnoreCase(city, PageRequest.of(page, offset, Sort.by(sortBy).ascending()));

        return locations.map(LocationDTO.locationToDTO());
    }

    /**
     * Gets all locations in a given state using {@link LocationRepository}
     * @param stateString Two character abbreviation of US state
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["quantity"|"name"|"description"|"price"|"discount"|"itemId"] defaults to itemId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all locations with pagination and sorting applied
     */
    public Page<LocationDTO> getAllLocationsByState(String stateString, int page, int offset, String sortBy, String order){
        State state;

        try {
            state = State.valueOf(stateString.toUpperCase());
        } catch (Exception ignored){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not understand state. State must be a two character abbreviation. Examples: AL, NY, TX, CA");
        }

        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);

        Page<Location> locations;
        if(order.equalsIgnoreCase("DESC"))
            locations = LOCATION_REPO.findByState(state, PageRequest.of(page, offset, Sort.by(sortBy).descending()));
        else
            locations = LOCATION_REPO.findByState(state, PageRequest.of(page, offset, Sort.by(sortBy).ascending()));

        return locations.map(LocationDTO.locationToDTO());
    }

    /**
     * Gets all locations with zip code matching provided value using {@link LocationRepository}
     * @param zip The zip to search for
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["quantity"|"name"|"description"|"price"|"discount"|"itemId"] defaults to itemId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all locations with pagination and sorting applied
     */
    public Page<LocationDTO> getAllLocationsByZip(String zip, int page, int offset, String sortBy, String order){
        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);

        Page<Location> locations;
        if(order.equalsIgnoreCase("DESC"))
            locations = LOCATION_REPO.findByZip(zip, PageRequest.of(page, offset, Sort.by(sortBy).descending()));
        else
            locations = LOCATION_REPO.findByZip(zip, PageRequest.of(page, offset, Sort.by(sortBy).ascending()));

        return locations.map(LocationDTO.locationToDTO());
    }

    /**
     * Ensures permitted page format
     * @param page The page number value being validated
     * @return A valid page number value
     */
    private int validatePage(int page){
        if(page < 0)
            page = 0;
        return page;
    }


    /**
     * Ensures permitted offset format
     * @param offset The offset value being validated
     * @return A valid offset value
     */
    private int validateOffset(int offset){
        if(offset != 5 && offset != 10 && offset != 25 && offset != 50 && offset != 100)
            offset = 25;
        return offset;
    }

    /**
     * Ensures permitted sortby format
     * @param sortBy The sortby value being validated
     * @return A valid sortby value
     */
    private String validateSortBy(String sortBy){
        switch (sortBy.toLowerCase()){
            case "address":
                return "address";
            case "city":
                return "city";
            case "state":
                return "state";
            case "zip":
                return "zip";
            default:
                return "locationId";
        }
    }
}
