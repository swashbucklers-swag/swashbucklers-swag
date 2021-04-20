package com.sk8.swashbucklers.integration;

import com.sk8.swashbucklers.controller.OrderController;
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
import com.sk8.swashbucklers.service.OrderService;
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

import javax.transaction.Transactional;
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
    private OrderService orderService;

    @Autowired
    private LocationRepository locationRepository;

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
    @Transactional
    void givenOrder_whenGetAll_thenOrderRetrieved() throws Exception{
        Item item = new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        inventoryRepository.save(inventory);
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        Location l1 = new Location(0, "123 Fake St", "Springfield", State.FL, "33426");
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567", l1);
        customerRepository.save(c);
        Location l2 = new Location(0, "954 Broward blvd", "Hollywood", State.FL, "33026");
        Set<OrderDetails> orderDetailsSet = new HashSet<>(Collections.singletonList(new OrderDetails(0, item, 10)));
        Order newOrder = new Order(0, c, l2, Timestamp.from(Instant.now()), statusHistoryList, orderDetailsSet);
        for (OrderDetails orderDetails : orderDetailsSet) {
            orderDetails.setOrder(newOrder);
        }
        orderRepository.save(newOrder);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/order/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderId").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.customerId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.firstName").value("Lebron"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.lastName").value("James"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.email").value("lbj@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.phoneNumber").value("7861234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.locationId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.address").value("123 Fake St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.city").value("Springfield"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.state").value("FL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.zip").value("33426"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.locationId").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.address").value("954 Broward blvd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.city").value("Hollywood"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.state").value("FL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.zip").value("33026"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].dateOfOrder").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].statusHistory[0].historyId").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].statusHistory[0].orderStatus").value("PROCESSING_ORDER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].statusHistory[0].statusTime").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderDetails[0].orderDetailsId").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderDetails[0].item.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderDetails[0].quantity").value(10))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getStatus());
    }

    @Test
    @Transactional
    void givenOrder_whenGetAllWithPagination_thenOrderRetrievedWithPagination() throws Exception{
        Item item = new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        inventoryRepository.save(inventory);
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        Location l1 = new Location(0, "123 Fake St", "Springfield", State.FL, "33426");
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567", l1);
        customerRepository.save(c);
        Location l2 = new Location(0, "954 Broward blvd", "Hollywood", State.FL, "33026");
        Set<OrderDetails> orderDetailsSet = new HashSet<>(Collections.singletonList(new OrderDetails(0, item, 10)));
        Order newOrder = new Order(0, c, l2, Timestamp.from(Instant.now()), statusHistoryList, orderDetailsSet);
        for (OrderDetails orderDetails : orderDetailsSet) {
            orderDetails.setOrder(newOrder);
        }
        Item item2 = new Item(0, "Playstation 5", "Sony Game System", 499.99, 25);
        Inventory inventory2 = new Inventory(0, item2, 15);
        item.setInventory(inventory2);
        inventory.setItem(item2);
        inventoryRepository.save(inventory2);
        StatusHistory statusHistory2 = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList2 = new ArrayList<>();
        statusHistoryList2.add(statusHistory2);
        Location l3 = new Location(0, "123 Fake St", "New York", State.NY, "33426");
        Customer c2 = new Customer(0, "Dwayne", "Wade", "dw@mail.com", "pass123", "3051234567", l3);
        customerRepository.save(c2);
        Location l4 = new Location(0, "954 Broward blvd", "Boynton Beach", State.FL, "33026");
        Set<OrderDetails> orderDetailsSet2 = new HashSet<>(Collections.singletonList(new OrderDetails(0, item2, 5)));
        Order newOrder2 = new Order(0, c2, l4, Timestamp.from(Instant.now()), statusHistoryList2, orderDetailsSet2);
        for (OrderDetails orderDetails2 : orderDetailsSet2) {
            orderDetails2.setOrder(newOrder2);
        }
        orderRepository.save(newOrder);
        orderRepository.save(newOrder2);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/order/all?page=0&offset=50&sortby=\"firstName\"&order=\"DESC\"")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderId").value(9))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.customerId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.firstName").value("Lebron"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.lastName").value("James"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.email").value("lbj@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.phoneNumber").value("7861234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.locationId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.address").value("123 Fake St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.city").value("Springfield"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.state").value("FL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.zip").value("33426"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.locationId").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.address").value("954 Broward blvd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.city").value("Hollywood"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.state").value("FL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].dateOfOrder").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].statusHistory[0].historyId").value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].statusHistory[0].orderStatus").value("PROCESSING_ORDER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].statusHistory[0].statusTime").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderDetails[0].orderDetailsId").value(11))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderDetails[0].item.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderDetails[0].quantity").value(10))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].orderId").value(13))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].customer.customerId").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].customer.firstName").value("Dwayne"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].customer.lastName").value("Wade"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].customer.email").value("dw@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].customer.phoneNumber").value("3051234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].customer.location.locationId").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].customer.location.address").value("123 Fake St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].customer.location.city").value("New York"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].customer.location.state").value("NY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].customer.location.zip").value("33426"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].location.locationId").value(14))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].location.address").value("954 Broward blvd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].location.city").value("Boynton Beach"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].location.state").value("FL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].location.zip").value("33026"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].dateOfOrder").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].statusHistory[0].historyId").value(16))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].statusHistory[0].orderStatus").value("PROCESSING_ORDER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].statusHistory[0].statusTime").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].orderDetails[0].orderDetailsId").value(15))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].orderDetails[0].item.itemId").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].orderDetails[0].quantity").value(5))

                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.sorted").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.unsorted").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.empty").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value(50))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.paged").value(true))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getStatus());
    }

    @Test
    @Transactional
    void givenOrder_whenGetByOrderId_thenOrderRetrieved() throws Exception{
        Item item = new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        inventoryRepository.save(inventory);
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        Location l1 = new Location(0, "123 Fake St", "Springfield", State.FL, "33426");
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567", l1);
        customerRepository.save(c);
        Location l2 = new Location(0, "954 Broward blvd", "Hollywood", State.FL, "33026");
        Set<OrderDetails> orderDetailsSet = new HashSet<>(Collections.singletonList(new OrderDetails(0, item, 10)));
        Order newOrder = new Order(0, c, l2, Timestamp.from(Instant.now()), statusHistoryList, orderDetailsSet);
        for (OrderDetails orderDetails : orderDetailsSet) {
            orderDetails.setOrder(newOrder);
        }
        orderRepository.save(newOrder);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/order/order-id/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.firstName").value("Lebron"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.lastName").value("James"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.email").value("lbj@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.phoneNumber").value("7861234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.location.locationId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.location.address").value("123 Fake St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.location.city").value("Springfield"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.location.state").value("FL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.location.zip").value("33426"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.locationId").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.address").value("954 Broward blvd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.city").value("Hollywood"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.state").value("FL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.zip").value("33026"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfOrder").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusHistory[0].historyId").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusHistory[0].orderStatus").value("PROCESSING_ORDER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusHistory[0].statusTime").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderDetails[0].orderDetailsId").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderDetails[0].item.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderDetails[0].quantity").value(10))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getStatus());
    }

    @Test
    @Transactional
    void givenOrder_whenGetByCustomerId_thenOrderRetrieved() throws Exception{
        Item item = new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        inventoryRepository.save(inventory);
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        Location l1 = new Location(0, "123 Fake St", "Springfield", State.FL, "33426");
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567", l1);
        customerRepository.save(c);
        Location l2 = new Location(0, "954 Broward blvd", "Hollywood", State.FL, "33026");
        Set<OrderDetails> orderDetailsSet = new HashSet<>(Collections.singletonList(new OrderDetails(0, item, 10)));
        Order newOrder = new Order(0, c, l2, Timestamp.from(Instant.now()), statusHistoryList, orderDetailsSet);
        for (OrderDetails orderDetails : orderDetailsSet) {
            orderDetails.setOrder(newOrder);
        }
        orderRepository.save(newOrder);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/order/customer-id/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderId").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.customerId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.firstName").value("Lebron"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.lastName").value("James"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.email").value("lbj@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.phoneNumber").value("7861234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.locationId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.address").value("123 Fake St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.city").value("Springfield"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.state").value("FL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.zip").value("33426"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.locationId").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.address").value("954 Broward blvd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.city").value("Hollywood"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.state").value("FL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.zip").value("33026"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].dateOfOrder").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].statusHistory[0].historyId").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].statusHistory[0].orderStatus").value("PROCESSING_ORDER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].statusHistory[0].statusTime").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderDetails[0].orderDetailsId").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderDetails[0].item.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderDetails[0].quantity").value(10))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getStatus());
    }

    @Test
    @Transactional
    void givenOrder_whenGetByLocationId_thenOrderRetrieved() throws Exception{
        Item item = new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        inventoryRepository.save(inventory);
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        Location l1 = new Location(0, "123 Fake St", "Springfield", State.FL, "33426");
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567", l1);
        customerRepository.save(c);
        Location l2 = new Location(0, "954 Broward blvd", "Hollywood", State.FL, "33026");
        Set<OrderDetails> orderDetailsSet = new HashSet<>(Collections.singletonList(new OrderDetails(0, item, 10)));
        Order newOrder = new Order(0, c, l2, Timestamp.from(Instant.now()), statusHistoryList, orderDetailsSet);
        for (OrderDetails orderDetails : orderDetailsSet) {
            orderDetails.setOrder(newOrder);
        }
        orderRepository.save(newOrder);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/order/location-id/6")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderId").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.customerId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.firstName").value("Lebron"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.lastName").value("James"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.email").value("lbj@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.phoneNumber").value("7861234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.locationId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.address").value("123 Fake St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.city").value("Springfield"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.state").value("FL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.location.zip").value("33426"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.locationId").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.address").value("954 Broward blvd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.city").value("Hollywood"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.state").value("FL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location.zip").value("33026"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].dateOfOrder").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].statusHistory[0].historyId").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].statusHistory[0].orderStatus").value("PROCESSING_ORDER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].statusHistory[0].statusTime").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderDetails[0].orderDetailsId").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderDetails[0].item.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].orderDetails[0].quantity").value(10))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getStatus());
    }

    @Test
    @Transactional
    void createOrder_thenOrderRetrieved() throws Exception{
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/order/create")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderId").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.customerId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.firstName").value("Lebron"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.lastName").value("James"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.email").value("lbj@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.phoneNumber").value("7861234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.location.locationId").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.location.address").value("123 Fake St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.location.city").value("Springfield"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.location.state").value("FL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.location.zip").value("33426"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.locationId").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.address").value("954 Broward blvd"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.city").value("Hollywood"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.state").value("FL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.zip").value("33026"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfOrder").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusHistory[0].historyId").value(8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusHistory[0].orderStatus").value("PROCESSING_ORDER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusHistory[0].statusTime").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderDetails[0].orderDetailsId").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderDetails[0].item.itemId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderDetails[0].quantity").value(10))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getStatus());
    }






}
