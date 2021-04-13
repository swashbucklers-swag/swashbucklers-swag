package com.sk8.swashbucklers.controller;

import com.sk8.swashbucklers.model.location.Location;
import com.sk8.swashbucklers.model.location.State;
import com.sk8.swashbucklers.repo.location.LocationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
public class LocationIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationController locationController;

    @Test
    public void givenLocation_whenGetAll_thenLocationRetrieved() throws Exception{
        locationRepository.save(new Location(0, "123 address St", "Paris", State.NY, "55125"));
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/location/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].locationId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].address").value("123 address St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].city").value("Paris"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].state").value("NY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].zip").value("55125"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void givenLocation_whenGetLocationId_thenLocationRetrieved() throws Exception{
        locationRepository.save(new Location(0, "123 address St", "Paris", State.NY, "55125"));
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/location/id/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.locationId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("123 address St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("Paris"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("NY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.zip").value("55125"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void givenLocation_whenGetByState_thenLocationRetrieved() throws Exception{
        locationRepository.save(new Location(0, "123 address St", "Paris", State.NY, "55125"));
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/location/state/NY")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].locationId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].address").value("123 address St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].city").value("Paris"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].state").value("NY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].zip").value("55125"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void givenLocation_whenGetByZip_thenLocationRetrieved() throws Exception{
        locationRepository.save(new Location(0, "123 address St", "Paris", State.NY, "55125"));
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/location/zip/55125")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].locationId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].address").value("123 address St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].city").value("Paris"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].state").value("NY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].zip").value("55125"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void givenLocation_whenGetByCity_thenLocationRetrieved() throws Exception{
        locationRepository.save(new Location(0, "123 address St", "Paris", State.NY, "55125"));
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/location/city?city=paris")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].locationId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].address").value("123 address St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].city").value("Paris"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].state").value("NY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].zip").value("55125"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}
