package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.dto.OrderCreateDTO;
import com.sk8.swashbucklers.dto.OrderDTO;
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
import com.sk8.swashbucklers.repo.order.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

/**
 * tests for {@link OrderService}
 *
 * @author Steven Ceglarek
 */
@SpringBootTest
public class OrderServiceTest {

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private InventoryRepository inventoryRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private InventoryService inventoryService;

    @Test
    public void whenGetAllOrder_returnsPageOfOrderDTO() {
        List<Order> orderArrayList = new ArrayList<>();
        Item item = new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        Set<OrderDetailsDTO> orderDetailsDTOSet = new HashSet<>();
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567",
                new Location(0, "123 Fake St", "Springfield", State.FL, "33426"));
        Location l = new Location(1, "954 Broward blvd", "Hollywood", State.FL, "33026");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        orderDetailsDTOSet.add(new OrderDetailsDTO(0, 10));
        Set<OrderDetails> orderDetailsSet = new HashSet<>(Collections.singletonList(new OrderDetails(0, item, 10)));
        OrderCreateDTO order = new OrderCreateDTO(0, l, orderDetailsDTOSet);
        Order newOrder = new Order(0, c, l, Timestamp.from(Instant.now()), statusHistoryList, orderDetailsSet);
        for (OrderDetails orderDetails : orderDetailsSet) {
            orderDetails.setOrder(newOrder);
        }
        orderArrayList.add(newOrder);

        Page<Order> orderPage = new PageImpl<>(orderArrayList);

        Mockito.when(orderRepository.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(orderPage);

        Page<OrderDTO> response = orderService.getAllOrders(0, 5, "customer", "DESC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getContent().get(0).getOrderId());
        Assertions.assertEquals(c, response.getContent().get(0).getCustomer());
        Assertions.assertEquals(l, response.getContent().get(0).getLocation());
        Assertions.assertEquals(ts, response.getContent().get(0).getDateOfOrder());
        Assertions.assertEquals(statusHistoryList, response.getContent().get(0).getStatusHistory());
        Assertions.assertEquals(orderDetailsSet, response.getContent().get(0).getOrderDetails());
        System.out.println(response.getContent());

        response = orderService.getAllOrders(0, 10, "city", "ASC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getContent().get(0).getOrderId());
        Assertions.assertEquals(c, response.getContent().get(0).getCustomer());
        Assertions.assertEquals(l, response.getContent().get(0).getLocation());
        Assertions.assertEquals(ts, response.getContent().get(0).getDateOfOrder());
        Assertions.assertEquals(statusHistoryList, response.getContent().get(0).getStatusHistory());
        Assertions.assertEquals(orderDetailsSet, response.getContent().get(0).getOrderDetails());
        System.out.println(response.getContent());
    }

    @Test
    void whenCreateNewOrder_returnsNewlyCreatedOrderDTO(){
        Item item = new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        Set<OrderDetailsDTO> orderDetailsDTOSet = new HashSet<>();
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567",
                new Location(0, "123 Fake St", "Springfield", State.FL, "33426"));
        Location l = new Location(1, "954 Broward blvd", "Hollywood", State.FL, "33026");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        orderDetailsDTOSet.add(new OrderDetailsDTO(0, 10));
        Set<OrderDetails> orderDetailsSet = new HashSet<>(Collections.singletonList(new OrderDetails(0, item, 10)));
        OrderCreateDTO order = new OrderCreateDTO(0, l, orderDetailsDTOSet);
        Order newOrder = new Order(0, c, l, Timestamp.from(Instant.now()), statusHistoryList, orderDetailsSet);
        for (OrderDetails orderDetails : orderDetailsSet) {
            orderDetails.setOrder(newOrder);
        }

        Mockito.when(inventoryRepository.findByItem_ItemId(org.mockito.ArgumentMatchers.isA(Integer.class))).thenReturn(Optional.of(inventory));
        Mockito.when(orderRepository.save(org.mockito.ArgumentMatchers.isA(Order.class))).thenReturn(newOrder);
        Mockito.when(inventoryRepository.save(org.mockito.ArgumentMatchers.isA(Inventory.class))).thenReturn(inventory);
        Mockito.when(customerRepository.findById(org.mockito.ArgumentMatchers.isA(Integer.class))).thenReturn(Optional.of(c));

        OrderDTO response = orderService.createNewOrder(order);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getOrderId());
        Assertions.assertEquals(c, response.getCustomer());
        Assertions.assertEquals(l, response.getLocation());
        Assertions.assertNotNull(response.getDateOfOrder());
        Assertions.assertEquals(statusHistoryList, response.getStatusHistory());
        Assertions.assertEquals(orderDetailsSet, response.getOrderDetails());
        System.out.println(response);
    }

    @Test
    void whenGetByOrderID_returnsOrderDTO(){
        Item item = new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        Set<OrderDetailsDTO> orderDetailsDTOSet = new HashSet<>();
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567",
                new Location(0, "123 Fake St", "Springfield", State.FL, "33426"));
        Location l = new Location(1, "954 Broward blvd", "Hollywood", State.FL, "33026");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        orderDetailsDTOSet.add(new OrderDetailsDTO(0, 10));
        Set<OrderDetails> orderDetailsSet = new HashSet<>(Collections.singletonList(new OrderDetails(0, item, 10)));
        OrderCreateDTO order = new OrderCreateDTO(0, l, orderDetailsDTOSet);
        Order newOrder = new Order(0, c, l, Timestamp.from(Instant.now()), statusHistoryList, orderDetailsSet);
        for (OrderDetails orderDetails : orderDetailsSet) {
            orderDetails.setOrder(newOrder);
        }

        Mockito.when(orderRepository.findById(0)).thenReturn(Optional.of(newOrder));
        Mockito.when(orderRepository.findById(255)).thenReturn(Optional.empty());

        OrderDTO response = orderService.getOrderById(0);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getOrderId());
        Assertions.assertEquals(c, response.getCustomer());
        Assertions.assertEquals(l, response.getLocation());
        Assertions.assertEquals(ts, response.getDateOfOrder());
        Assertions.assertEquals(statusHistoryList, response.getStatusHistory());
        Assertions.assertEquals(orderDetailsSet, response.getOrderDetails());
        System.out.println(response);

        response = orderService.getOrderById(255);
        Assertions.assertNull(response);
        System.out.println(response);
    }

    @Test
    void whenGetByCustomerID_returnsOrderDTO(){
        List<Order> orderArrayList = new ArrayList<>();
        Item item = new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        Set<OrderDetailsDTO> orderDetailsDTOSet = new HashSet<>();
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567",
                new Location(0, "123 Fake St", "Springfield", State.FL, "33426"));
        Location l = new Location(1, "954 Broward blvd", "Hollywood", State.FL, "33026");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        orderDetailsDTOSet.add(new OrderDetailsDTO(0, 10));
        Set<OrderDetails> orderDetailsSet = new HashSet<>(Collections.singletonList(new OrderDetails(0, item, 10)));
        OrderCreateDTO order = new OrderCreateDTO(0, l, orderDetailsDTOSet);
        Order newOrder = new Order(0, c, l, Timestamp.from(Instant.now()), statusHistoryList, orderDetailsSet);
        for (OrderDetails orderDetails : orderDetailsSet) {
            orderDetails.setOrder(newOrder);
        }
        orderArrayList.add(newOrder);
        Page<Order> orderPage = new PageImpl<>(orderArrayList);

        Mockito.when(orderRepository.getByCustomer_CustomerId(org.mockito.ArgumentMatchers.isA(Integer.class), org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(orderPage);

        Page<OrderDTO> response = orderService.getOrdersByCustomerId(0, 0, 5, "firstName", "DESC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getContent().get(0).getOrderId());
        Assertions.assertEquals(c, response.getContent().get(0).getCustomer());
        Assertions.assertEquals(l, response.getContent().get(0).getLocation());
        Assertions.assertEquals(ts, response.getContent().get(0).getDateOfOrder());
        Assertions.assertEquals(statusHistoryList, response.getContent().get(0).getStatusHistory());
        Assertions.assertEquals(orderDetailsSet, response.getContent().get(0).getOrderDetails());
        System.out.println(response.getContent());

        response = orderService.getOrdersByCustomerId(0, 0, 10, "email", "ASC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getContent().get(0).getOrderId());
        Assertions.assertEquals(c, response.getContent().get(0).getCustomer());
        Assertions.assertEquals(l, response.getContent().get(0).getLocation());
        Assertions.assertEquals(ts, response.getContent().get(0).getDateOfOrder());
        Assertions.assertEquals(statusHistoryList, response.getContent().get(0).getStatusHistory());
        Assertions.assertEquals(orderDetailsSet, response.getContent().get(0).getOrderDetails());
        System.out.println(response.getContent());
    }

    @Test
    void whenGetByLocationID_returnsOrderDTO(){
        List<Order> orderArrayList = new ArrayList<>();
        Item item = new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        Set<OrderDetailsDTO> orderDetailsDTOSet = new HashSet<>();
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567",
                new Location(0, "123 Fake St", "Springfield", State.FL, "33426"));
        Location l = new Location(1, "954 Broward blvd", "Hollywood", State.FL, "33026");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        orderDetailsDTOSet.add(new OrderDetailsDTO(0, 10));
        Set<OrderDetails> orderDetailsSet = new HashSet<>(Collections.singletonList(new OrderDetails(0, item, 10)));
        OrderCreateDTO order = new OrderCreateDTO(0, l, orderDetailsDTOSet);
        Order newOrder = new Order(0, c, l, Timestamp.from(Instant.now()), statusHistoryList, orderDetailsSet);
        for (OrderDetails orderDetails : orderDetailsSet) {
            orderDetails.setOrder(newOrder);
        }
        orderArrayList.add(newOrder);
        Page<Order> orderPage = new PageImpl<>(orderArrayList);

        Mockito.when(orderRepository.getByLocation_LocationId(org.mockito.ArgumentMatchers.isA(Integer.class), org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(orderPage);

        Page<OrderDTO> response = orderService.getOrdersByLocationId(0, 0, 5, "address", "DESC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getContent().get(0).getOrderId());
        Assertions.assertEquals(c, response.getContent().get(0).getCustomer());
        Assertions.assertEquals(l, response.getContent().get(0).getLocation());
        Assertions.assertEquals(ts, response.getContent().get(0).getDateOfOrder());
        Assertions.assertEquals(statusHistoryList, response.getContent().get(0).getStatusHistory());
        Assertions.assertEquals(orderDetailsSet, response.getContent().get(0).getOrderDetails());
        System.out.println(response.getContent());

        response = orderService.getOrdersByLocationId(0, 0, 10, "city", "ASC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getContent().get(0).getOrderId());
        Assertions.assertEquals(c, response.getContent().get(0).getCustomer());
        Assertions.assertEquals(l, response.getContent().get(0).getLocation());
        Assertions.assertEquals(ts, response.getContent().get(0).getDateOfOrder());
        Assertions.assertEquals(statusHistoryList, response.getContent().get(0).getStatusHistory());
        Assertions.assertEquals(orderDetailsSet, response.getContent().get(0).getOrderDetails());
        System.out.println(response.getContent());
    }
}