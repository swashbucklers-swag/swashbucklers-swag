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
                    "  <li>/update :: PUT</li>\n" +
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
        public Page<OrderDTO> getAllInventory(
                @RequestParam(value="page", required = false, defaultValue = "0") int page,
                @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
                @RequestParam(value = "sortby", required = false, defaultValue = "dateOfOrder") String sortBy,
                @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){

            return ORDER_SERVICE.getAllOrders(page, offset, sortBy, order);
        }


        /**
         * Gets order who's item matches provided order id
         * @param id The order id of the specific order
         * @return The data transfer representation of the requested order
         */
        @GetMapping("/order-id/{id}")
        public OrderDTO getOrderByOrderIId(@PathVariable(name = "id") int id){
            return ORDER_SERVICE.getOrderById(id);
        }

        /**
         * Gets inventory items with names containing the given text, applies pagination and sorting
         * @param text The text being searched for in item names
         * @param page The page to be selected
         * @param offset The number of elements per page
         * @param sortBy The property/field to sort by
         * @param order The order in which the list is displayed ["ASC"|"DESC"]
         * @return The page of data transfer representations of all inventory objects who's names contain the given text with pagination and sorting applied
         */
        @GetMapping("/customer-id/{id}")
        public Page<OrderDTO> getOrdersByCustomerIId(
                @PathVariable(name = "id") int id,
                @RequestParam(value="page", required = false, defaultValue = "0") int page,
                @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
                @RequestParam(value = "sortby", required = false, defaultValue = "dateOfOrder") String sortBy,
                @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){
            return ORDER_SERVICE.getOrdersByCustomerId(id, page, offset, sortBy, order);
        }



        /**
         * Gets inventory items with names containing the given text, applies pagination and sorting
         * @param text The text being searched for in item names
         * @param page The page to be selected
         * @param offset The number of elements per page
         * @param sortBy The property/field to sort by
         * @param order The order in which the list is displayed ["ASC"|"DESC"]
         * @return The page of data transfer representations of all inventory objects who's names contain the given text with pagination and sorting applied
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
         * Adds a new inventory object
         * @param orderDTO The inventory to be added as a data transfer object
         * @return The data transfer representation of the newly added inventory object
         */
        @PostMapping("/create")
        public OrderDTO createNewOrder(@RequestBody OrderCreateDTO orderCreateDTO){
            return ORDER_SERVICE.createNewOrder(orderCreateDTO);
        }

        /**
         * Updates inventory's quantity field
         * @param inventoryQuantityDTO The inventory info to be updated
         * @return The data transfer representation of the newly updated inventory
         */
        @PutMapping("/update-status/{historyId}")
        public OrderDTO updateOrderStatus(@PathVariable(name = "historyId") int historyId, OrderStatus orderStatus){
            return ORDER_SERVICE.updateOrderStatus(orderStatus, historyId);
        }



    }
