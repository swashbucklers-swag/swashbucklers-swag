package com.sk8.swashbucklers.integration;

import com.sk8.swashbucklers.controller.CustomerController;
import com.sk8.swashbucklers.model.customer.Customer;
import com.sk8.swashbucklers.model.location.Location;
import com.sk8.swashbucklers.model.location.State;
import com.sk8.swashbucklers.repo.customer.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Integration tests for customers module
 * @author John Stone
 */

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
public class CustomerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerController customerController;

    @Test
    @WithMockUser(username = "firstmate",password = "firstmatePass",roles = {"CREW"})
    void whenDefaultMapping_thenDirectoryDisplayed() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/customer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();
        Assertions.assertEquals("<h3>\n" +
                "  Supported Endpoints for /customer:\n" +
                "</h3>\n" +
                "<ul>\n" +
                "  <li>/all :: GET</li>\n" +
                "  <li>/id :: GET</li>\n" +
                "  <li>/email :: GET</li>\n" +
                "  <li>/location :: GET</li>\n" +
                "  <li>/phonenumber :: GET</li>\n" +
                "  <li>/create :: POST</li>\n" +
                "  <li>/update :: PUT</li>\n" +
                "</ul>", result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "firstmate",password = "firstmatePass",roles = {"CREW"})
    public void givenCustomer_whenGetAll_thenCustomerRetrieved() throws Exception {
        customerRepository.save(new Customer(0, "Bob", "Smith", "bob@mail.com", "Pass", "5551234567", new Location(0, "123 Fake St", "Springfield", State.NY, "01234")));
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/customer/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value("bob@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].phoneNumber").value("5551234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.locationId").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.address").value("123 Fake St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.city").value("Springfield"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.state").value("NY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.zip").value("01234"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "firstmate",password = "firstmatePass",roles = {"CREW"})
    public void givenCustomer_whenGetById_thenCustomerRetrieved() throws Exception {
        customerRepository.save(new Customer(0, "Bob", "Smith", "bob@mail.com", "Pass", "5551234567", new Location(0, "123 Fake St", "Springfield", State.NY, "01234")));
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/customer/id/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("bob@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("5551234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.locationId").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.address").value("123 Fake St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.city").value("Springfield"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.state").value("NY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.zip").value("01234"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "firstmate",password = "firstmatePass",roles = {"CREW"})
    public void givenCustomer_whenGetByEmail_thenCustomerRetrieved() throws Exception {
        customerRepository.save(new Customer(0, "Bob", "Smith", "bob@mail.com", "Pass", "5551234567", new Location(0, "123 Fake St", "Springfield", State.NY, "01234")));
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/customer/email?email=bob@mail.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("bob@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("5551234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.locationId").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.address").value("123 Fake St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.city").value("Springfield"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.state").value("NY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.zip").value("01234"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "firstmate",password = "firstmatePass",roles = {"CREW"})
    public void givenCustomer_whenGetByPhone_thenCustomerRetrieved() throws Exception {
        customerRepository.save(new Customer(0, "Bob", "Smith", "bob@mail.com", "Pass", "5551234567", new Location(0, "123 Fake St", "Springfield", State.NY, "01234")));
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/customer/phone?phone=5551234567")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("bob@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("5551234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.locationId").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.address").value("123 Fake St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.city").value("Springfield"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.state").value("NY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.zip").value("01234"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "firstmate",password = "firstmatePass",roles = {"CREW"})
    void createCustomer_thenCustomerRetrieved() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/customer/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Bob\",\"lastName\" : \"Smith\", \"email\" : \"bob@mail.com\", \"password\" : \"Pass\",\"phoneNumber\" : \"5551234567\",\"location\" : {\"address\" : \"123 Fake St\",\"city\" : \"Springfield\",\"state\" : \"0\",\"zip\" : \"01234\"}}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("bob@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("5551234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.locationId").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.address").value("123 Fake St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.city").value("Springfield"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.state").value("AL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.zip").value("01234"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
    @Test
    @WithMockUser(username = "firstmate",password = "firstmatePass",roles = {"CREW"})
    void updateCustomer_thenCustomerRetrieved() throws Exception {
        customerRepository.save(new Customer(0, "Bob", "Smith", "bob@mail.com", "Pass", "5551234567", new Location(0, "123 Fake St", "Springfield", State.NY, "01234")));
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/customer/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customerId\" : 1, \"firstName\" : \"John\",\"lastName\" : \"Doe\", \"email\" : \"john@mail.com\", \"password\" : \"Pass\",\"phoneNumber\" : \"6661234567\",\"location\" : {\"address\" : \"321 Fake St\",\"city\" : \"Fallfield\",\"state\" : \"0\",\"zip\" : \"12340\"}}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("6661234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.locationId").value("3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.address").value("321 Fake St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.city").value("Fallfield"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.state").value("AL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.zip").value("12340"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}