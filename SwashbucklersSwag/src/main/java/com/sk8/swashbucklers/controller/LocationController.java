package com.sk8.swashbucklers.controller;

import com.sk8.swashbucklers.dto.LocationDTO;
import com.sk8.swashbucklers.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for location resource utilizing {@link LocationService}
 * @author Daniel Bernier
 */
@RestController
@RequestMapping("/location")
@Secured({"ROLE_CAPTAIN","ROLE_CREW"})
public class LocationController {

    private final LocationService LOCATION_SERVICE;

    @Autowired
    public LocationController(LocationService locationService){
        this.LOCATION_SERVICE = locationService;
    }

    /**
     * Default landing page for /location giving more information about requests and HTTP verbs
     * @return String with information supported endpoints
     */
    @GetMapping
    @PostMapping
    @PutMapping
    @DeleteMapping
    @RequestMapping
    @PatchMapping
    public String information(){
        return "<h3>\n" +
                "  Supported Endpoints for /location:\n" +
                "</h3>\n" +
                "<ul>\n" +
                "  <li>/all :: GET</li>\n" +
                "  <li>/id :: GET</li>\n" +
                "  <li>/city :: GET</li>\n" +
                "  <li>/state :: GET</li>\n" +
                "  <li>/zip :: GET</li>\n" +
                "</ul>";
    }

    /**
     * Gets all locations from location service
     * @param page The page to be selected
     * @param offset The number of elements per page
     * @param sortBy The property/field to sort by
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all location objects with pagination and sorting applied
     */
    @GetMapping("/all")
    public Page<LocationDTO> getAllLocations(
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "locationId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){

        return LOCATION_SERVICE.getAllLocations(page, offset, sortBy, order);
    }

    /**
     * Gets a location object based on its locationId
     * @param id The id of the location being requested
     * @return The data transfer representations of the location object
     */
    @GetMapping("/id/{id}")
    public LocationDTO getLocationById(@PathVariable(name = "id") int id){
        return LOCATION_SERVICE.getLocationById(id);
    }

    /**
     * Gets all locations with city like provided string
     * @param city The city to filter by
     * @param page The page to be selected
     * @param offset The number of elements per page
     * @param sortBy The property/field to sort by
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all location objects with pagination and sorting applied
     */
    @GetMapping("/city")
    public Page<LocationDTO> getAllLocationsByCity(
            @RequestParam(value = "city") String city,
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "locationId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){

        return LOCATION_SERVICE.getAllLocationsByCity(city, page, offset, sortBy, order);
    }

    /**
     * Gets all locations with state like provided string
     * @param state The two character abbreviation of the US state to filter by
     * @param page The page to be selected
     * @param offset The number of elements per page
     * @param sortBy The property/field to sort by
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all location objects with pagination and sorting applied
     */
    @GetMapping("/state/{state}")
    public Page<LocationDTO> getAllLocationsByState(
            @PathVariable(name = "state") String state,
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "locationId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){

        return LOCATION_SERVICE.getAllLocationsByState(state, page, offset, sortBy, order);
    }

    /**
     * Gets all locations with zip like provided string
     * @param zip The zip to filter by
     * @param page The page to be selected
     * @param offset The number of elements per page
     * @param sortBy The property/field to sort by
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all location objects with pagination and sorting applied
     */
    @GetMapping("/zip/{zip}")
    public Page<LocationDTO> getAllLocationsByZip(
            @PathVariable(name = "zip") String zip,
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "locationId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){

        return LOCATION_SERVICE.getAllLocationsByZip(zip, page, offset, sortBy, order);
    }
}
