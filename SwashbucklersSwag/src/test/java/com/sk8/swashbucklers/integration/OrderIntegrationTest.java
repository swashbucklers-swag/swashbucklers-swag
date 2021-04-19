package com.sk8.swashbucklers.integration;

import com.sk8.swashbucklers.controller.InventoryController;
import com.sk8.swashbucklers.controller.OrderController;
import com.sk8.swashbucklers.dto.OrderCreateDTO;
import com.sk8.swashbucklers.dto.OrderDetailsDTO;
import com.sk8.swashbucklers.model.customer.Customer;
import com.sk8.swashbucklers.model.item.Inventory;
import com.sk8.swashbucklers.model.item.Item;
import com.sk8.swashbucklers.model.location.Location;
import com.sk8.swashbucklers.model.location.State;
import com.sk8.swashbucklers.model.order.Order;
import com.sk8.swashbucklers.model.order.OrderDetails;
import com.sk8.swashbucklers.model.order.OrderStatus;
import com.sk8.swashbucklers.model.order.StatusHistory;
import com.sk8.swashbucklers.repo.customer.CustomerRepository;
import com.sk8.swashbucklers.repo.item.InventoryRepository;
import com.sk8.swashbucklers.repo.location.LocationRepository;
import com.sk8.swashbucklers.repo.order.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

/**
 * tests for integration of {@link OrderController}
 *
 * @author Steven Ceglarek
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
public class OrderIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderController orderController;

    @Test
    void whenDefaultMapping_thenDirectoryDisplayed() throws Exception{
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/order")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();
        Assertions.assertEquals("<h3>\n" +
                "  Supported Endpoints for /order:\n" +
                "</h3>\n" +
                "<ul>\n" +
                "  <li>/all :: GET</li>\n" +
                "  <li>/order-id :: GET</li>\n" +
                "  <li>/customer-id :: GET</li>\n" +
                "  <li>/location-id :: GET</li>\n" +
                "  <li>/create :: POST</li>\n" +
                "  <li>/update-status :: PUT</li>\n" +
                "</ul>", result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenOrder_whenGetAll_thenOrderRetrieved() throws Exception{
        Item item = new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        inventoryRepository.save(inventory);
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        Set<OrderDetailsDTO> orderDetailsDTOSet = new HashSet<>();
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567",
                new Location(0, "123 Fake St", "Springfield", State.FL, "33426"));
        customerRepository.save(c);
        Location l = new Location(1, "954 Broward blvd", "Hollywood", State.FL, "33026");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        orderDetailsDTOSet.add(new OrderDetailsDTO(0, 10));
        Set<OrderDetails> orderDetailsSet = new HashSet<>(Collections.singletonList(new OrderDetails(0, item, 10)));
        OrderCreateDTO order = new OrderCreateDTO(0, l, orderDetailsDTOSet);
        Order newOrder = new Order(0, c, l, Timestamp.from(Instant.now()), statusHistoryList, orderDetailsSet);
        for (OrderDetails orderDetails : orderDetailsSet) {
            orderDetails.setOrder(newOrder);
        }
        orderRepository.save(newOrder);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/order/all")
                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].itemId").value(2))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Boat"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].description").value("Cool red boat"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(255.99))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].discount").value(25))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].quantity").value(13))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }



}
