package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.dto.*;
import com.sk8.swashbucklers.model.customer.Customer;
import com.sk8.swashbucklers.model.item.Inventory;
import com.sk8.swashbucklers.model.item.Item;
import com.sk8.swashbucklers.model.order.Order;
import com.sk8.swashbucklers.model.order.OrderDetails;
import com.sk8.swashbucklers.model.order.OrderStatus;
import com.sk8.swashbucklers.model.order.StatusHistory;
import com.sk8.swashbucklers.repo.customer.CustomerRepository;
import com.sk8.swashbucklers.repo.item.InventoryRepository;
import com.sk8.swashbucklers.repo.order.OrderDetailsRepository;
import com.sk8.swashbucklers.repo.order.OrderRepository;
import com.sk8.swashbucklers.repo.order.StatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

/**
 * Service for getting locations with various constraints
 *
 * @author Steven Ceglarek
 */
@Service
public class OrderService {

    private final OrderRepository ORDER_REPO;
    private final OrderDetailsRepository ORDER_DETAILS_REPO;
    private final StatusHistoryRepository STATUS_HISTORY_REPO;
    private final InventoryRepository INVENTORY_REPO;
    private final CustomerRepository CUSTOMER_REPO;



    /**
     * Constructor with order repository injected with spring
     * @param orderRepository The order repository to be used throughout
     */
    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderDetailsRepository orderDetailsRepository,
                        StatusHistoryRepository statusHistoryRepository,
                        InventoryRepository inventoryRepository,
                        CustomerRepository customerRepository){
        this.ORDER_REPO = orderRepository;
        this.ORDER_DETAILS_REPO = orderDetailsRepository;
        this.STATUS_HISTORY_REPO = statusHistoryRepository;
        this.INVENTORY_REPO = inventoryRepository;
        this.CUSTOMER_REPO = customerRepository;
    }

    /**
     * Gets all locations using {@link OrderRepository}
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["quantity"|"name"|"description"|"price"|"discount"|"itemId"] defaults to itemId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all locations with pagination and sorting applied
     */
    public Page<OrderDTO> getAllOrders(int page, int offset, String sortBy, String order){
        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);

        Page<Order> orders;
        if(order.equalsIgnoreCase("DESC"))
            orders = ORDER_REPO.findAll(PageRequest.of(page, offset, Sort.by(sortBy).descending()));
        else
            orders = ORDER_REPO.findAll(PageRequest.of(page, offset, Sort.by(sortBy).ascending()));

        return orders.map(OrderDTO.OrderToDTO());
    }

    /**
     * Gets order by order id
     * @param id The id of the location being requested
     * @return data transfer representation of location
     */
    public OrderDTO getOrderById(int id){
        Optional<Order> order = ORDER_REPO.findById(id);
        return order.map(value -> OrderDTO.OrderToDTO().apply(value)).orElse(null);
    }

    /**
     * Gets all locations using {@link OrderRepository}
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["quantity"|"name"|"description"|"price"|"discount"|"itemId"] defaults to itemId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all locations with pagination and sorting applied
     */
    public Page<OrderDTO> getOrdersByCustomerId(int id, int page, int offset, String sortBy, String order){
        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);

        Page<Order> orders;
        if(order.equalsIgnoreCase("desc"))
            orders = ORDER_REPO.getByCustomer_CustomerId(id, PageRequest.of(page, offset, Sort.by(sortBy).descending()));
        else
            orders = ORDER_REPO.getByCustomer_CustomerId(id, PageRequest.of(page, offset, Sort.by(sortBy).ascending()));

        return orders.map(OrderDTO.OrderToDTO());
    }

    /**
     * Gets all locations using {@link OrderRepository}
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["quantity"|"name"|"description"|"price"|"discount"|"itemId"] defaults to itemId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all locations with pagination and sorting applied
     */
    public Page<OrderDTO> getOrdersByLocationId(int id, int page, int offset, String sortBy, String order){
        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);

        Page<Order> orders;
        if(order.equalsIgnoreCase("desc"))
            orders = ORDER_REPO.getByLocation_LocationId(id, PageRequest.of(page, offset, Sort.by(sortBy).descending()));
        else
            orders = ORDER_REPO.getByLocation_LocationId(id, PageRequest.of(page, offset, Sort.by(sortBy).ascending()));

        return orders.map(OrderDTO.OrderToDTO());
    }

    /**
     * Persists an inventory object by calling {@link OrderRepository#save(Object)} and returns the newly saved inventory object as its data transfer representation
     * @param order The Inventory object being persisted
     * @return The newly persisted inventory object converted to its data transfer representation using {@link OrderDTO#OrderToDTO()}
     */
    public OrderDTO createNewOrder(OrderCreateDTO orderCreateDTO){
        Set<OrderDetails> orderDetailsSet = detailsConversion(orderCreateDTO.getOrderDetailsDTOSet());
        for (OrderDetails orderDetails : orderDetailsSet) {
            Optional<Inventory> optionalInventory = INVENTORY_REPO.findByItem_ItemId(orderDetails.getItem().getItemId());
            if (!optionalInventory.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find a valid item id with the order given");
            }
            if (orderDetails.getItem().getItemId() == optionalInventory.get().getItem().getItemId()) {
                Inventory inventory = optionalInventory.get();
                int q1 = orderDetails.getQuantity();
                int q2 = inventory.getQuantity();
                q2 -= q1;
                if (q2 < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient quantity on item requested");
                } else {
                    inventory.setQuantity(q2);
                    INVENTORY_REPO.save(inventory);
                }
            }
        }
        Optional<Customer> customerOptional = CUSTOMER_REPO.findById(orderCreateDTO.getCustomerId());
        if (!customerOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find a valid customer id with the order given");
        }
        Order newOrder = new Order(0, customerOptional.get(), orderCreateDTO.getLocation(),
                Timestamp.from(Instant.now()), new ArrayList<>(Collections.singletonList(new StatusHistory(0,
                OrderStatus.PROCESSING_ORDER, Timestamp.from(Instant.now())))), orderDetailsSet);
        for (OrderDetails orderDetails : newOrder.getOrderDetails()) {
            orderDetails.setOrder(newOrder);
        }
        Order saved = ORDER_REPO.save(newOrder);
        return OrderDTO.OrderToDTO().apply(saved);
    }

    /**
     * Updates order status
     * @param statusHistoryDTO order status being updated
     * @return Data transfer representation of updated object
     */
    public OrderDTO updateOrderStatus(OrderStatus orderStatus, int orderId){
        Optional<Order> orderOptional = ORDER_REPO.findById(orderId);

        if(!orderOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find a valid order with the order id given");
        }
        Order order = orderOptional.get();
        order.getStatusHistory().add(new StatusHistory(0, orderStatus, Timestamp.from(Instant.now())));
        return OrderDTO.OrderToDTO().apply(ORDER_REPO.save(order));
    }


    public Set<OrderDetails> detailsConversion(Set<OrderDetailsDTO> orderDetailsDTOSet) {
        Set<OrderDetails> orderDetailsSet = new HashSet<>();
        for (OrderDetailsDTO o : orderDetailsDTOSet) {
            Optional<Inventory> optionalInventory = INVENTORY_REPO.findByItem_ItemId(o.getItemID());
            if (!optionalInventory.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find a valid item id with the order given");
            }
            Item i = optionalInventory.get().getItem();
            orderDetailsSet.add(new OrderDetails(0, i, o.getQuantity()));
        }
        return orderDetailsSet;

    }


    /**
     * Ensures permitted page format
     * @param page The page number value being validated
     * @return A valid page number value
     */
    private int validatePage(int page){
        if(page < 0)
            page = 0;
        return page;
    }


    /**
     * Ensures permitted offset format
     * @param offset The offset value being validated
     * @return A valid offset value
     */
    private int validateOffset(int offset){
        if(offset != 5 && offset != 10 && offset != 25 && offset != 50 && offset != 100)
            offset = 25;
        return offset;
    }

    /**
     * Ensures permitted sortby format
     * @param sortBy The sortby value being validated
     * @return A valid sortby value
     */
    private String validateSortBy(String sortBy){
        switch (sortBy.toLowerCase()){
            case "firstName":
                return "customer.firstName";
            case "lastName":
                return "customer.lastName";
            case "email":
                return "customer.email";
            case "phoneNumber":
                return "customer.phoneNumber";
            case "city":
                return "location.city";
            case "state":
                return "location.state";
            case "zip":
                return "location.zip";
            default:
                return "datOfOrder";
        }
    }

}
