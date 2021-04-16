package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.dto.LocationDTO;
import com.sk8.swashbucklers.model.location.Location;
import com.sk8.swashbucklers.model.location.State;
import com.sk8.swashbucklers.repo.location.LocationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * tests for {@link LocationService}
 *
 * @author Daniel Bernier
 */
@SpringBootTest
public class LocationServiceTest {

    @MockBean
    private LocationRepository locationRepository;

    @Autowired
    private LocationService locationService;

    @Test
    void whenGetAllLocations_callRepository_getsLocationDTOPage(){
        List<Location> locationArrayList = new ArrayList<>();
        locationArrayList.add(new Location(1, "address", "city", State.NY, "12345"));
        Page<Location> locations = new PageImpl<>(locationArrayList);

        Mockito.when(locationRepository.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(locations);

        Page<LocationDTO> response = locationService.getAllLocations(0, 5, "address", "DESC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getContent().get(0).getLocationId());
        Assertions.assertEquals("address", response.getContent().get(0).getAddress());
        Assertions.assertEquals("city", response.getContent().get(0).getCity());
        Assertions.assertEquals(State.NY.toString(), response.getContent().get(0).getState());
        Assertions.assertEquals("12345", response.getContent().get(0).getZip());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertEquals(1, response.getNumberOfElements());
        System.out.println(response.getContent());

        response = locationService.getAllLocations(0, 10, "city", "ASC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getContent().get(0).getLocationId());
        Assertions.assertEquals("address", response.getContent().get(0).getAddress());
        Assertions.assertEquals("city", response.getContent().get(0).getCity());
        Assertions.assertEquals(State.NY.toString(), response.getContent().get(0).getState());
        Assertions.assertEquals("12345", response.getContent().get(0).getZip());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertEquals(1, response.getNumberOfElements());
        System.out.println(response.getContent());
    }

    @Test
    void whenGetLocationById_callRepository_getsCorrectLocation(){
        Optional<Location> location = Optional.of(new Location(1, "address", "city", State.NY, "12345"));
        Mockito.when(locationRepository.findById(1)).thenReturn(location);
        Mockito.when(locationRepository.findById(255)).thenReturn(Optional.empty());

        LocationDTO response = locationService.getLocationById(1);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getLocationId());
        Assertions.assertEquals("address", response.getAddress());
        Assertions.assertEquals("city", response.getCity());
        Assertions.assertEquals(State.NY.toString(), response.getState());
        Assertions.assertEquals("12345", response.getZip());

        System.out.println(response);

        response = locationService.getLocationById(255);
        Assertions.assertNull(response);

        System.out.println(response);
    }

    @Test
    void whenGetAllLocationsByState_callRepository_getsLocationWithMatchingStateDTOPage(){
        List<Location> locationArrayList = new ArrayList<>();
        locationArrayList.add(new Location(1, "address a", "city1", State.NY, "12345"));
        Page<Location> locations = new PageImpl<>(locationArrayList);

        Mockito.when(locationRepository.findByState(org.mockito.ArgumentMatchers.isA(State.class), org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(locations);

        Page<LocationDTO> response = locationService.getAllLocationsByState("NY", 0, 25, "state", "DESC");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getNumberOfElements());

        Assertions.assertEquals(1, response.getContent().get(0).getLocationId());
        Assertions.assertEquals("address a", response.getContent().get(0).getAddress());
        Assertions.assertEquals("city1", response.getContent().get(0).getCity());
        Assertions.assertEquals(State.NY.toString(), response.getContent().get(0).getState());
        Assertions.assertEquals("12345", response.getContent().get(0).getZip());

        Assertions.assertEquals(1, response.getTotalPages());

        System.out.println(response.getContent());

        response = locationService.getAllLocationsByState("NY", 0, 50, "zip", "ASC");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getNumberOfElements());

        Assertions.assertEquals(1, response.getContent().get(0).getLocationId());
        Assertions.assertEquals("address a", response.getContent().get(0).getAddress());
        Assertions.assertEquals("city1", response.getContent().get(0).getCity());
        Assertions.assertEquals(State.NY.toString(), response.getContent().get(0).getState());
        Assertions.assertEquals("12345", response.getContent().get(0).getZip());

        Assertions.assertEquals(1, response.getTotalPages());

        System.out.println(response.getContent());
    }

    @Test
    void whenGetAllLocationsByCity_callRepository_getsLocationWithMatchingCityDTOPage(){
        List<Location> locationArrayList = new ArrayList<>();
        locationArrayList.add(new Location(1, "address a", "city1", State.NY, "12345"));
        Page<Location> locations = new PageImpl<>(locationArrayList);

        Mockito.when(locationRepository.findByCityContainingIgnoreCase(org.mockito.ArgumentMatchers.isA(String.class), org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(locations);

        Page<LocationDTO> response = locationService.getAllLocationsByCity("city1", 0, 100, "locationId", "ASC");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getNumberOfElements());

        Assertions.assertEquals(1, response.getContent().get(0).getLocationId());
        Assertions.assertEquals("address a", response.getContent().get(0).getAddress());
        Assertions.assertEquals("city1", response.getContent().get(0).getCity());
        Assertions.assertEquals(State.NY.toString(), response.getContent().get(0).getState());
        Assertions.assertEquals("12345", response.getContent().get(0).getZip());

        Assertions.assertEquals(1, response.getTotalPages());

        System.out.println(response.getContent());

        response = locationService.getAllLocationsByCity("city1", 0, 25, "locationId", "DESC");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getNumberOfElements());

        Assertions.assertEquals(1, response.getContent().get(0).getLocationId());
        Assertions.assertEquals("address a", response.getContent().get(0).getAddress());
        Assertions.assertEquals("city1", response.getContent().get(0).getCity());
        Assertions.assertEquals(State.NY.toString(), response.getContent().get(0).getState());
        Assertions.assertEquals("12345", response.getContent().get(0).getZip());

        Assertions.assertEquals(1, response.getTotalPages());

        System.out.println(response.getContent());
    }

    @Test
    void whenGetAllLocationsByZip_callRepository_getsLocationWithMatchingZipDTOPage(){
        List<Location> locationArrayList = new ArrayList<>();
        locationArrayList.add(new Location(1, "address a", "city1", State.NY, "12345"));
        Page<Location> locations = new PageImpl<>(locationArrayList);

        Mockito.when(locationRepository.findByZip(org.mockito.ArgumentMatchers.isA(String.class), org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(locations);

        Page<LocationDTO> response = locationService.getAllLocationsByZip("12345", 0, 25, "locationId", "DESC");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getNumberOfElements());

        Assertions.assertEquals(1, response.getContent().get(0).getLocationId());
        Assertions.assertEquals("address a", response.getContent().get(0).getAddress());
        Assertions.assertEquals("city1", response.getContent().get(0).getCity());
        Assertions.assertEquals(State.NY.toString(), response.getContent().get(0).getState());
        Assertions.assertEquals("12345", response.getContent().get(0).getZip());

        Assertions.assertEquals(1, response.getTotalPages());

        System.out.println(response.getContent());

        response = locationService.getAllLocationsByZip("12345", 0, 25, "locationId", "ASC");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getNumberOfElements());

        Assertions.assertEquals(1, response.getContent().get(0).getLocationId());
        Assertions.assertEquals("address a", response.getContent().get(0).getAddress());
        Assertions.assertEquals("city1", response.getContent().get(0).getCity());
        Assertions.assertEquals(State.NY.toString(), response.getContent().get(0).getState());
        Assertions.assertEquals("12345", response.getContent().get(0).getZip());

        Assertions.assertEquals(1, response.getTotalPages());

        System.out.println(response.getContent());
    }
}
