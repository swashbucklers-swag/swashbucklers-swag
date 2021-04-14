package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.dto.CustomerDTO;
import com.sk8.swashbucklers.model.customer.Customer;
import com.sk8.swashbucklers.model.location.Location;
import com.sk8.swashbucklers.model.location.State;
import com.sk8.swashbucklers.repo.customer.CustomerRepository;
import com.sk8.swashbucklers.services.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Tests for {@link CustomerService}
 */

@SpringBootTest
public class CustomerServiceTest {

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Test
    void whenGetAllCustomers_callRepository_getsCustomersDTOPage(){
        List<Customer> customerArrayList = new ArrayList<>();
        customerArrayList.add(new Customer(0, "Bob", "Smith", "bob@mail.com", "Pass", "5551234567", new Location(0, "123 Fake St", "Springfield", State.NY, "01234")));
        Page<Customer> customers = new PageImpl<>(customerArrayList);

        Mockito.when(customerRepository.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(customers);

        Page<CustomerDTO> response = customerService.getAllCustomers(0, 5, "email", "DESC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getContent().get(0).getCustomerId());
        Assertions.assertEquals("Bob", response.getContent().get(0).getFirstName());
        Assertions.assertEquals("Smith", response.getContent().get(0).getLastName());
        Assertions.assertEquals("bob@mail.com", response.getContent().get(0).getEmail());
        Assertions.assertEquals("", response.getContent().get(0).getPassword());
        Assertions.assertEquals("5551234567", response.getContent().get(0).getPhoneNumber());
        Assertions.assertEquals("123 Fake St", response.getContent().get(0).getLocation().getAddress());
        Assertions.assertEquals("Springfield", response.getContent().get(0).getLocation().getCity());
        Assertions.assertEquals(State.NY, response.getContent().get(0).getLocation().getState());
        Assertions.assertEquals("01234", response.getContent().get(0).getLocation().getZip());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertEquals(1, response.getNumberOfElements());
        System.out.println(response.getContent());
    }
    @Test
    void whenGetCustomerById_callRepository_getsCorrectCustomer(){
        Optional<Customer> customer = Optional.of(new Customer(0, "Bob", "Smith", "bob@mail.com", "Pass", "5551234567", new Location(0, "123 Fake St", "Springfield", State.NY, "01234")));
        Mockito.when(customerRepository.findById(1)).thenReturn(customer);
        Mockito.when(customerRepository.findById(255)).thenReturn(Optional.empty());

        CustomerDTO response = customerService.getCustomerById(1);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getCustomerId());
        Assertions.assertEquals("Bob", response.getFirstName());
        Assertions.assertEquals("Smith", response.getLastName());
        Assertions.assertEquals("bob@mail.com", response.getEmail());
        Assertions.assertEquals("", response.getPassword());
        Assertions.assertEquals("5551234567", response.getPhoneNumber());
        Assertions.assertEquals("123 Fake St", response.getLocation().getAddress());
        Assertions.assertEquals("Springfield", response.getLocation().getCity());
        Assertions.assertEquals(State.NY, response.getLocation().getState());
        Assertions.assertEquals("01234", response.getLocation().getZip());

        System.out.println(response);

        response = customerService.getCustomerById(255);
        Assertions.assertNull(response);
        System.out.println(response);
    }
    @Test
    void whenGetCustomerByEmail_callRepository_getsCorrectCustomer(){
        Optional<Customer> customer = Optional.of(new Customer(0, "Bob", "Smith", "bob@mail.com", "Pass", "5551234567", new Location(0, "123 Fake St", "Springfield", State.NY, "01234")));
        Mockito.when(customerRepository.findByEmail("bob@mail.com")).thenReturn(customer);
        Mockito.when(customerRepository.findByEmail("nothere@mail.com")).thenReturn(Optional.empty());

        CustomerDTO response = customerService.getCustomerByEmail("bob@mail.com");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getCustomerId());
        Assertions.assertEquals("Bob", response.getFirstName());
        Assertions.assertEquals("Smith", response.getLastName());
        Assertions.assertEquals("bob@mail.com", response.getEmail());
        Assertions.assertEquals("", response.getPassword());
        Assertions.assertEquals("5551234567", response.getPhoneNumber());
        Assertions.assertEquals("123 Fake St", response.getLocation().getAddress());
        Assertions.assertEquals("Springfield", response.getLocation().getCity());
        Assertions.assertEquals(State.NY, response.getLocation().getState());
        Assertions.assertEquals("01234", response.getLocation().getZip());

        System.out.println(response);

        response = customerService.getCustomerById(255);
        Assertions.assertNull(response);
        System.out.println(response);
    }
    @Test
    void whenGetCustomerByPhone_callRepository_getsCorrectCustomer(){
        Optional<Customer> customer = Optional.of(new Customer(0, "Bob", "Smith", "bob@mail.com", "Pass", "5551234567", new Location(0, "123 Fake St", "Springfield", State.NY, "01234")));
        Mockito.when(customerRepository.findByPhoneNumber("5551234567")).thenReturn(customer);
        Mockito.when(customerRepository.findByPhoneNumber("5550000000")).thenReturn(Optional.empty());

        CustomerDTO response = customerService.getCustomerByPhoneNumber("5551234567");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getCustomerId());
        Assertions.assertEquals("Bob", response.getFirstName());
        Assertions.assertEquals("Smith", response.getLastName());
        Assertions.assertEquals("bob@mail.com", response.getEmail());
        Assertions.assertEquals("", response.getPassword());
        Assertions.assertEquals("5551234567", response.getPhoneNumber());
        Assertions.assertEquals("123 Fake St", response.getLocation().getAddress());
        Assertions.assertEquals("Springfield", response.getLocation().getCity());
        Assertions.assertEquals(State.NY, response.getLocation().getState());
        Assertions.assertEquals("01234", response.getLocation().getZip());

        System.out.println(response);

        response = customerService.getCustomerById(255);
        Assertions.assertNull(response);
        System.out.println(response);
    }
    @Test
    void whenGetAllCustomersByLocation_callRepository_getsCustomersWithMatchingLocationDTOPage(){
        List<Customer> customerArrayList = new ArrayList<>();
        customerArrayList.add(new Customer(0, "Bob", "Smith", "bob@mail.com", "Pass", "5551234567", new Location(0, "123 Fake St", "Springfield", State.NY, "01234")));
        Page<Customer> customers = new PageImpl<>(customerArrayList);

        Mockito.when(customerRepository.findByLocation_LocationId(org.mockito.ArgumentMatchers.isA(Integer.class), org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(customers);

        Page<CustomerDTO> response = customerService.getAllCustomersByLocation(1, 0, 25, "email", "DESC");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getContent().get(0).getCustomerId());
        Assertions.assertEquals("Bob", response.getContent().get(0).getFirstName());
        Assertions.assertEquals("Smith", response.getContent().get(0).getLastName());
        Assertions.assertEquals("bob@mail.com", response.getContent().get(0).getEmail());
        Assertions.assertEquals("", response.getContent().get(0).getPassword());
        Assertions.assertEquals("5551234567", response.getContent().get(0).getPhoneNumber());
        Assertions.assertEquals("123 Fake St", response.getContent().get(0).getLocation().getAddress());
        Assertions.assertEquals("Springfield", response.getContent().get(0).getLocation().getCity());
        Assertions.assertEquals(State.NY, response.getContent().get(0).getLocation().getState());
        Assertions.assertEquals("01234", response.getContent().get(0).getLocation().getZip());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertEquals(1, response.getNumberOfElements());

        Assertions.assertEquals(1, response.getTotalPages());

        System.out.println(response.getContent());

        response = customerService.getAllCustomersByLocation(255, 0, 50, "firstName", "ASC");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getContent().get(0).getCustomerId());
        Assertions.assertEquals("Bob", response.getContent().get(0).getFirstName());
        Assertions.assertEquals("Smith", response.getContent().get(0).getLastName());
        Assertions.assertEquals("bob@mail.com", response.getContent().get(0).getEmail());
        Assertions.assertEquals("", response.getContent().get(0).getPassword());
        Assertions.assertEquals("5551234567", response.getContent().get(0).getPhoneNumber());
        Assertions.assertEquals("123 Fake St", response.getContent().get(0).getLocation().getAddress());
        Assertions.assertEquals("Springfield", response.getContent().get(0).getLocation().getCity());
        Assertions.assertEquals(State.NY, response.getContent().get(0).getLocation().getState());
        Assertions.assertEquals("01234", response.getContent().get(0).getLocation().getZip());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertEquals(1, response.getNumberOfElements());

        Assertions.assertEquals(1, response.getTotalPages());

        System.out.println(response.getContent());
    }
    @Test
    void whenCreateCustomer_callRepository_returnsCorrectCustomer(){
        Customer c = new Customer(0, "Bob", "Smith", "bob@mail.com", "Pass", "5551234567", new Location(0, "123 Fake St", "Springfield", State.NY, "01234"));
        Mockito.when(customerRepository.save(c)).thenReturn(c);

        CustomerDTO response = customerService.createNewCustomer(c);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getCustomerId());
        Assertions.assertEquals("Bob", response.getFirstName());
        Assertions.assertEquals("Smith", response.getLastName());
        Assertions.assertEquals("bob@mail.com", response.getEmail());
        Assertions.assertEquals("", response.getPassword());
        Assertions.assertEquals("5551234567", response.getPhoneNumber());
        Assertions.assertEquals("123 Fake St", response.getLocation().getAddress());
        Assertions.assertEquals("Springfield", response.getLocation().getCity());
        Assertions.assertEquals(State.NY, response.getLocation().getState());
        Assertions.assertEquals("01234", response.getLocation().getZip());

        System.out.println(response);
    }
    @Test
    void whenUpdateCustomer_callRepository_returnsCorrectCustomer(){
        CustomerDTO cdto = new CustomerDTO(0, "Bob", "Smith", "bob@mail.com", "Pass", "5551234567", new Location(0, "123 Fake St", "Springfield", State.NY, "01234"));
        Customer c = new Customer(0, "Bob", "Smith", "bob@mail.com", "Pass", "5551234567", new Location(0, "123 Fake St", "Springfield", State.NY, "01234"));
        Mockito.when(customerRepository.save(c)).thenReturn(c);
        Mockito.when(customerRepository.findById(0)).thenReturn(Optional.of(c));

        CustomerDTO response = customerService.updateCustomerInfo(cdto);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getCustomerId());
        Assertions.assertEquals("Bob", response.getFirstName());
        Assertions.assertEquals("Smith", response.getLastName());
        Assertions.assertEquals("bob@mail.com", response.getEmail());
        Assertions.assertEquals("", response.getPassword());
        Assertions.assertEquals("5551234567", response.getPhoneNumber());
        Assertions.assertEquals("123 Fake St", response.getLocation().getAddress());
        Assertions.assertEquals("Springfield", response.getLocation().getCity());
        Assertions.assertEquals(State.NY, response.getLocation().getState());
        Assertions.assertEquals("01234", response.getLocation().getZip());

        System.out.println(response);
    }
}
