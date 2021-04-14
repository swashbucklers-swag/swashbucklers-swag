package com.sk8.swashbucklers.integration;

import com.sk8.swashbucklers.controller.LocationController;
import com.sk8.swashbucklers.model.location.Location;
import com.sk8.swashbucklers.model.location.State;
import com.sk8.swashbucklers.repo.location.LocationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
class LocationIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationController locationController;

    @Test
    void whenDefaultMapping_thenDirectoryDisplayed() throws Exception{
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/location")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();
        Assertions.assertEquals("<h3>\n" +
        "  Supported Endpoints for /location:\n" +
                "</h3>\n" +
                "<ul>\n" +
                "  <li>/all :: GET</li>\n" +
                "  <li>/id :: GET</li>\n" +
                "  <li>/city :: GET</li>\n" +
                "  <li>/state :: GET</li>\n" +
                "  <li>/zip :: GET</li>\n" +
                "</ul>", result.getResponse().getContentAsString());

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenLocation_whenGetAll_thenLocationRetrieved() throws Exception{
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
    void givenLocation_whenGetAllWithPagination_thenLocationRetrievedWithPagination() throws Exception{
        locationRepository.save(new Location(0, "123 address St", "Paris", State.NY, "55125"));

        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/location/all?page=0&offset=50&sortby=\"address\"&order=\"DESC\"")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].locationId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].address").value("123 address St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].city").value("Paris"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].state").value("NY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].zip").value("55125"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.sorted").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.unsorted").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.empty").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value(50))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.paged").value(true))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenLocation_whenGetLocationId_thenLocationRetrieved() throws Exception{
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
    void givenLocation_whenGetByState_thenLocationRetrieved() throws Exception{
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
    void givenLocation_whenGetByZip_thenLocationRetrieved() throws Exception{
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
    void givenLocation_whenGetByCity_thenLocationRetrieved() throws Exception{
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
