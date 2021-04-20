package com.sk8.swashbucklers.dto;

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * tests for {@link OrderDTO}
 *
 * @author Steven Ceglarek
 */
@SpringBootTest
public class OrderDTOTest {

    @Test
    void whenConvertingToDTO_DTOFieldsMatchOriginalObject(){
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        OrderDetails orderDetails = new OrderDetails(0, new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25), 10 );
        Set<OrderDetails> orderDetailsSet = new HashSet<>();
        orderDetailsSet.add(orderDetails);
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567",
                new Location(0, "123 Fake St", "Springfield", State.FL, "33426"));
        Location l = new Location(0, "123 Fake St", "Springfield", State.FL, "33426");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Order order = new Order(0, c, l, ts, statusHistoryList, orderDetailsSet);
        OrderDTO o = OrderDTO.OrderToDTO().apply(order);
        Assertions.assertEquals(0, o.getOrderId());
        Assertions.assertEquals(c, o.getCustomer());
        Assertions.assertEquals(l, o.getLocation());
        Assertions.assertEquals(ts, o.getDateOfOrder());
        Assertions.assertEquals(statusHistoryList, o.getStatusHistory());
        Assertions.assertEquals(orderDetailsSet, o.getOrderDetails());
    }

    @Test
    void whenConvertingFromDTO_DTOFieldsMatchOriginalDTO(){
        StatusHistory statusHistory = new StatusHistory(0, OrderStatus.PROCESSING_ORDER, new Timestamp(System.currentTimeMillis()));
        List<StatusHistory> statusHistoryList = new ArrayList<>();
        statusHistoryList.add(statusHistory);
        OrderDetails orderDetails = new OrderDetails(0, new Item(0, "Xbox Series X", "Microsoft Game System", 499.99, 25), 10 );
        Set<OrderDetails> orderDetailsSet = new HashSet<>();
        orderDetailsSet.add(orderDetails);
        Customer c = new Customer(0, "Lebron", "James", "lbj@mail.com", "pass123", "7861234567",
                new Location(0, "123 Fake St", "Springfield", State.FL, "33426"));
        Location l = new Location(0, "123 Fake St", "Springfield", State.FL, "33426");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        OrderDTO orderDTO = new OrderDTO(0, c, l, ts, statusHistoryList, orderDetailsSet);
        Order o = OrderDTO.DTOToOrder().apply(orderDTO);
        Assertions.assertEquals(0, o.getOrderId());
        Assertions.assertEquals(c, o.getCustomer());
        Assertions.assertEquals(l, o.getLocation());
        Assertions.assertEquals(ts, o.getDateOfOrder());
        Assertions.assertEquals(statusHistoryList, o.getStatusHistory());
        Assertions.assertEquals(orderDetailsSet, o.getOrderDetails());
    }
}
