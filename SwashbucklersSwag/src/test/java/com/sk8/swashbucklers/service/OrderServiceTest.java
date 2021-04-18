package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.dto.InventoryDTO;
import com.sk8.swashbucklers.dto.OrderDTO;
import com.sk8.swashbucklers.model.customer.Customer;
import com.sk8.swashbucklers.model.item.Inventory;
import com.sk8.swashbucklers.model.item.Item;
import com.sk8.swashbucklers.model.location.Location;
import com.sk8.swashbucklers.model.location.State;
import com.sk8.swashbucklers.model.order.Order;
import com.sk8.swashbucklers.model.order.OrderDetails;
import com.sk8.swashbucklers.model.order.OrderStatus;
import com.sk8.swashbucklers.model.order.StatusHistory;
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

    @Autowired
    private OrderService orderService;

    @Test
    public void whenGetAllOrder_returnsPageOfOrderDTO() {
        List<Order> orderArrayList = new ArrayList<>();
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        OrderDetails orderDetails = new OrderDetails(
                0, new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25), 10 );
        Set<OrderDetails> orderDetailsSet = new HashSet<>();
        orderDetailsSet.add(orderDetails);
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567",
                new Location(0, "123 Fake St", "Springfield", State.FL, "33426"));
        Location l = new Location(0, "123 Fake St", "Springfield", State.FL, "33426");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Order order = new Order(0, c, l, ts, statusHistoryList, orderDetailsSet);
        orderArrayList.add(order);
        Page<Order> orderPage = new PageImpl<>(orderArrayList);
        System.out.println(orderPage.getContent());

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
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        OrderDetails orderDetails = new OrderDetails(
                0, new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25), 10 );
        Set<OrderDetails> orderDetailsSet = new HashSet<>();
        orderDetailsSet.add(orderDetails);
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567",
                new Location(0, "123 Fake St", "Springfield", State.FL, "33426"));
        Location l = new Location(0, "123 Fake St", "Springfield", State.FL, "33426");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Order order = new Order(0, c, l, ts, statusHistoryList, orderDetailsSet);

        Mockito.when(orderRepository.save(org.mockito.ArgumentMatchers.isA(Order.class))).thenReturn(order);

        OrderDTO response = orderService.createNewOrder(order);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getOrderId());
        Assertions.assertEquals(c, response.getCustomer());
        Assertions.assertEquals(l, response.getLocation());
        Assertions.assertEquals(ts, response.getDateOfOrder());
        Assertions.assertEquals(statusHistoryList, response.getStatusHistory());
        Assertions.assertEquals(orderDetailsSet, response.getOrderDetails());
        System.out.println(response);
    }

    @Test
    void whenGetByOrderID_returnsOrderDTO(){
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        OrderDetails orderDetails = new OrderDetails(
                0, new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25), 10 );
        Set<OrderDetails> orderDetailsSet = new HashSet<>();
        orderDetailsSet.add(orderDetails);
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567",
                new Location(0, "123 Fake St", "Springfield", State.FL, "33426"));
        Location l = new Location(0, "123 Fake St", "Springfield", State.FL, "33426");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Order order = new Order(0, c, l, ts, statusHistoryList, orderDetailsSet);

        Mockito.when(orderRepository.findById(0)).thenReturn(Optional.of(order));
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
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        OrderDetails orderDetails = new OrderDetails(
                0, new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25), 10 );
        Set<OrderDetails> orderDetailsSet = new HashSet<>();
        orderDetailsSet.add(orderDetails);
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567",
                new Location(0, "123 Fake St", "Springfield", State.FL, "33426"));
        Location l = new Location(0, "123 Fake St", "Springfield", State.FL, "33426");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Order order = new Order(0, c, l, ts, statusHistoryList, orderDetailsSet);
        orderArrayList.add(order);
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

        response = orderService.getOrdersByCustomerId(0, 0, 5, "email", "ASC");
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