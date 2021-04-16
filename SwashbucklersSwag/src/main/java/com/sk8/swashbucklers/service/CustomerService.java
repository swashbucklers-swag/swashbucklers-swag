package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.dto.CustomerDTO;
import com.sk8.swashbucklers.model.customer.Customer;
import com.sk8.swashbucklers.repo.customer.CustomerRepository;
import com.sk8.swashbucklers.util.hashing.PasswordHashingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Optional;

/**
 * This CustomerService allows for communication with {@link CustomerRepository} and enforces data constraints on requests to repository
 * @author John Stone
 */

@Service
public class CustomerService {
    private final CustomerRepository CUSTOMER_REPO;

    private final PasswordHashingUtil passwordHashingUtil;

    @Autowired
    public CustomerService(CustomerRepository customerRepo, PasswordHashingUtil passwordHashingUtil){
        this.CUSTOMER_REPO = customerRepo;
        this.passwordHashingUtil = passwordHashingUtil;
    }

    /**
     * Gets all customers, applying pagination and sorting
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["firstname"|"lastname"|"email"|"phonenumber"] defaults to custiomerId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all customer objects with pagination and sorting applied
     */
    public Page<CustomerDTO> getAllCustomers(int page, int offset, String sortBy, String order){

        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);

        Page<Customer> customers;
        if(order.equalsIgnoreCase("DESC"))
            customers = CUSTOMER_REPO.findAll(PageRequest.of(page, offset, Sort.by(sortBy).descending()));
        else
            customers = CUSTOMER_REPO.findAll(PageRequest.of(page, offset, Sort.by(sortBy).ascending()));

        return customers.map(CustomerDTO.customerToDTO());
    }
    /**
     * Gets a customer by customer id using {@link CustomerRepository#findById(Object)}
     * @param customerId The customer id of the customer being requested
     * @return Data transfer object representation of Customer object converted using {@link CustomerDTO#customerToDTO()}
     */
    public CustomerDTO getCustomerById(int customerId){
        Optional<Customer> requested = CUSTOMER_REPO.findById(customerId);
        return requested.map(customer -> CustomerDTO.customerToDTO().apply(customer)).orElse(null);
    }
    /**
     * Gets a customer by email id using {@link CustomerRepository#findByEmail(String)}
     * @param email The email of the customer being requested
     * @return Data transfer object representation of Customer object converted using {@link CustomerDTO#customerToDTO()}
     */
    public CustomerDTO getCustomerByEmail(String email){
        Optional<Customer> requested = CUSTOMER_REPO.findByEmail(email);
        return requested.map(customer -> CustomerDTO.customerToDTO().apply(customer)).orElse(null);
    }
    /**
     * Gets a customer by phone number id using {@link CustomerRepository#findByPhoneNumber(String)}
     * @param phoneNumber The email of the customer being requested
     * @return Data transfer object representation of Customer object converted using {@link CustomerDTO#customerToDTO()}
     */
    public CustomerDTO getCustomerByPhoneNumber(String phoneNumber){
        Optional<Customer> requested = CUSTOMER_REPO.findByPhoneNumber(phoneNumber);
        return requested.map(customer -> CustomerDTO.customerToDTO().apply(customer)).orElse(null);
    }
    /**
     * Gets customers from the given location, applies pagination and sorting
     * @param id The location being searched from
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["firstname"|"lastname"|"email"|"phonenumber"] defaults to custiomerId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return The page of data transfer representations of all customer objects who's location matches the given location Id with pagination and sorting applied
     */
    public Page<CustomerDTO> getAllCustomersByLocation(int id, int page, int offset, String sortBy, String order){
        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);

        Page<Customer> customers;
        if(order.equalsIgnoreCase("DESC"))
            customers = CUSTOMER_REPO.findByLocation_LocationId(id, PageRequest.of(page, offset, Sort.by(sortBy).descending()));
        else
            customers = CUSTOMER_REPO.findByLocation_LocationId(id, PageRequest.of(page, offset, Sort.by(sortBy).ascending()));

        return customers.map(CustomerDTO.customerToDTO());
    }
    /**
     * Persists a customer object by calling {@link CustomerRepository#save(Object)} and returns the newly saved customer object as its data transfer representation
     * @param customer The Customer object being persisted
     * @return The newly persisted customer object converted to its data transfer representation using {@link CustomerDTO#customerToDTO()}
     */
    public CustomerDTO createNewCustomer(Customer customer) {
        try {
            customer.setPassword(passwordHashingUtil.hashPasswordWithEmail(customer.getEmail(), customer.getPassword()));
        } catch (NoSuchAlgorithmException ignored) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Customer saved = CUSTOMER_REPO.save(customer);
        return CustomerDTO.customerToDTO().apply(saved);
    }
    /**
     * Updates a customer's info
     * @param customerDTO Customer info to be updated
     * @return Data transfer representation of updated object
     */
    public CustomerDTO updateCustomerInfo(CustomerDTO customerDTO){
        Optional<Customer> customerOptional = CUSTOMER_REPO.findById(customerDTO.getCustomerId());

        if(!customerOptional.isPresent())
            return null;

        Customer customer = customerOptional.get();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        try {
            customer.setPassword(passwordHashingUtil.hashPasswordWithEmail(customerDTO.getEmail(),customerDTO.getPassword()));
        } catch (NoSuchAlgorithmException ignored) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setLocation(customerDTO.getLocation());

        return CustomerDTO.customerToDTO().apply(CUSTOMER_REPO.save(customer));
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
     *  Ensures a permitted offset valye
     * @param offset The offset number being validated
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
        switch (sortBy.toLowerCase(Locale.ROOT)){
            case "firstname":
                return "firstName";
            case "lastname":
                return "lastName";
            case "email":
                return "email";
            case "location":
                return "location.locationId";
            case "phonenumber":
                return "phoneNumber";
            default:
                return "customerId";
        }
    }
}
