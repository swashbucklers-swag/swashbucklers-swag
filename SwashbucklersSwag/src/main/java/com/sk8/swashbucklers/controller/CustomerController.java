package com.sk8.swashbucklers.model.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService CUSTOMER_SERVICE;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.CUSTOMER_SERVICE = customerService;
    }

    /**
     * Default landing page for /inventory giving more information about requests and HTTP verbs
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
                "  Supported Endpoints for /customer:\n" +
                "</h3>\n" +
                "<ul>\n" +
                "  <li>/all :: GET</li>\n" +
                "  <li>/id :: GET</li>\n" +
                "  <li>/email :: GET</li>\n" +
                "  <li>/location :: GET</li>\n" +
                "  <li>/phonenumber :: GET</li>\n" +
                "  <li>/login :: POST</li>\n" +
                "  <li>/create :: POST</li>\n" +
                "  <li>/update :: PUT</li>\n" +
                "</ul>";
    }
    /**
     * Gets all customers with pagination and sorting
     * @param page The page to be selected
     * @param offset The number of elements per page
     * @param sortBy The property/field to sort by
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all inventory objects with pagination and sorting applied
     */
    @GetMapping("/all")
    public Page<CustomerDTO> getAllCustomers(
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "itemId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){

        return CUSTOMER_SERVICE.getAllCustomers(page, offset, sortBy, order);
    }
    /**
     * Gets customer who's id matches provided id
     * @param id The id of the customer
     * @return The data transfer representation of the requested Customer
     */
    @GetMapping("/customer-id/{id}")
    public CustomerDTO getCustomerById(@PathVariable(name = "id") int id){
        return CUSTOMER_SERVICE.getCustomerById(id);
    }
    /**
     * Gets customer who's email matches providede email
     * @param email The id of the customer
     * @return The data transfer representation of the requested Customer
     */
    @GetMapping("/customer-email/{email}")
    public CustomerDTO getCustomerByEmail(@PathVariable(name = "email") String email){
        return CUSTOMER_SERVICE.getCustomerByEmail(email);
    }
    /**
     * Gets customer who's phone number matches provided phone number
     * @param phone The id of the customer
     * @return The data transfer representation of the requested Customer
     */
    @GetMapping("/customer-phone/{phone}")
    public CustomerDTO getCustomerByPhone(@PathVariable(name = "phone") String phone){
        return CUSTOMER_SERVICE.getCustomerByPhoneNumber(phone);
    }
    /**
     * Gets customers with locations matching the given integer, applies pagination and sorting
     * @param id the location to return customers from
     * @param page The page to be selected
     * @param offset The number of elements per page
     * @param sortBy The property/field to sort by
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all inventory objects who's names contain the given text with pagination and sorting applied
     */
    @GetMapping("/location")
    public Page<CustomerDTO> getAllCustomersFromLocation(
            @RequestParam(value = "id") int id,
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "itemId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){

        return CUSTOMER_SERVICE.getAllCustomersByLocation(id, page, offset, sortBy, order);
    }
    /**
     * Adds a new customer object
     * @param customerDTO The inventory to be added as a data transfer object
     * @return The data transfer representation of the newly added inventory object
     */
    @PostMapping("/create")
    public CustomerDTO addNewInventory(@RequestBody CustomerDTO customerDTO){
        customerDTO.setCustomerId(0);
        Customer c = customerDTO.DTOToCustomer().apply(customerDTO);
        return CUSTOMER_SERVICE.createNewCustomer(c);
    }
    /**
     * Updates customer information
     * @param customerinfoDTO The customer to be updated
     * @return The data transfer representation of the newly updated customer
     */
    @PutMapping("/update")
    public CustomerDTO updateInventoryInfo(@RequestBody CustomerDTO customerinfoDTO){
        return CUSTOMER_SERVICE.updateCustomerInfo(customerinfoDTO);
    }
}
