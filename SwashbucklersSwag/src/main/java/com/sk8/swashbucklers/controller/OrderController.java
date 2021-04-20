package com.sk8.swashbucklers.controller;

import com.sk8.swashbucklers.dto.*;
import com.sk8.swashbucklers.model.order.OrderStatus;
import com.sk8.swashbucklers.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for order resource utilizing {@link OrderService}
 * @author Steven Ceglarek
 */
@RestController
@RequestMapping("/order")
public class OrderController {

        private final OrderService ORDER_SERVICE;

        @Autowired
        public OrderController(OrderService orderService){
            this.ORDER_SERVICE = orderService;
        }

        /**
         * Default landing page for /order giving more information about requests and HTTP verbs
         * @return String with information supported endpoints
         */
        @GetMapping
        @PostMapping
        @PutMapping
        @DeleteMapping
        @RequestMapping
        @PatchMapping
        public String information(){
            return "<h3>\n" +
                    "  Supported Endpoints for /order:\n" +
                    "</h3>\n" +
                    "<ul>\n" +
                    "  <li>/all :: GET</li>\n" +
                    "  <li>/order-id :: GET</li>\n" +
                    "  <li>/customer-id :: GET</li>\n" +
                    "  <li>/location-id :: GET</li>\n" +
                    "  <li>/create :: POST</li>\n" +
                    "  <li>/update-status :: PUT</li>\n" +
                    "</ul>";
        }

        /**
         * Gets all order with pagination and sorting
         * @param page The page to be selected
         * @param offset The number of elements per page
         * @param sortBy The property/field to sort by
         * @param order The order in which the list is displayed ["ASC"|"DESC"]
         * @return The page of data transfer representations of all order objects with pagination and sorting applied
         */
        @GetMapping("/all")
        public Page<OrderDTO> getAllOrders(
                @RequestParam(value="page", required = false, defaultValue = "0") int page,
                @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
                @RequestParam(value = "sortby", required = false, defaultValue = "dateOfOrder") String sortBy,
                @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){

            return ORDER_SERVICE.getAllOrders(page, offset, sortBy, order);
        }

        /**
         * Gets order who's order matches provided order id
         * @param id The order id of the specific order
         * @return The data transfer representation of the requested order
         */
        @GetMapping("/order-id/{id}")
        public OrderDTO getOrderByOrderId(@PathVariable(name = "id") int id){
            return ORDER_SERVICE.getOrderById(id);
        }

        /**
         * Gets orders who's orders matches provided customer id
         * @param id The id of the Customer whos orders are being retrieved
         * @param page The page to be selected
         * @param offset The number of elements per page
         * @param sortBy The property/field to sort by
         * @param order The order in which the list is displayed ["ASC"|"DESC"]
         * @return The page of data transfer representations of all order objects who's customer id was provided with pagination and sorting applied
         */
        @GetMapping("/customer-id/{id}")
        public Page<OrderDTO> getOrdersByCustomerId(
                @PathVariable(name = "id") int id,
                @RequestParam(value="page", required = false, defaultValue = "0") int page,
                @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
                @RequestParam(value = "sortby", required = false, defaultValue = "dateOfOrder") String sortBy,
                @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){
            return ORDER_SERVICE.getOrdersByCustomerId(id, page, offset, sortBy, order);
        }

        /**
         * Gets orders who's orders matches provided location id
         * @param id The id of the Location where the orders are being retrieved
         * @param page The page to be selected
         * @param offset The number of elements per page
         * @param sortBy The property/field to sort by
         * @param order The order in which the list is displayed ["ASC"|"DESC"]
         * @return The page of data transfer representations of all order objects who's location id was provided with pagination and sorting applied
         */
        @GetMapping("/location-id/{id}")
        public Page<OrderDTO> getOrdersByLocationIId(
                @PathVariable(name = "id") int id,
                @RequestParam(value="page", required = false, defaultValue = "0") int page,
                @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
                @RequestParam(value = "sortby", required = false, defaultValue = "dateOfOrder") String sortBy,
                @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){
            return ORDER_SERVICE.getOrdersByLocationId(id, page, offset, sortBy, order);
        }

        /**
         * Adds a new order object
         * @param orderCreateDTO The order to be added as a data transfer object
         * @return The data transfer representation of the newly added order object
         */
        @PostMapping("/create")
        public OrderDTO createNewOrder(@RequestBody OrderCreateDTO orderCreateDTO){
            return ORDER_SERVICE.createNewOrder(orderCreateDTO);
        }

        /**
         * Updates order status with the given orderId
         * @param orderID the id of the order who's status is being updated
         * @param orderStatus The new status of the order
         * @return The data transfer representation of the newly updated order
         */
        @PutMapping("/update-status/{orderId}")
        public OrderDTO updateOrderStatus(@PathVariable(name = "orderId") int orderID, @RequestBody OrderStatus orderStatus){
            return ORDER_SERVICE.updateOrderStatus(orderStatus, orderID);
        }



    }
