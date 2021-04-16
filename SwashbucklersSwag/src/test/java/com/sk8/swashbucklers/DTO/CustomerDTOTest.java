package com.sk8.swashbucklers.DTO;

import com.sk8.swashbucklers.dto.CustomerDTO;
import com.sk8.swashbucklers.model.customer.Customer;
import com.sk8.swashbucklers.model.location.Location;
import com.sk8.swashbucklers.model.location.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests for {@link CustomerDTO}
 * @author John Stone
 */
@SpringBootTest
public class CustomerDTOTest {
    @Test
    void whenConvertingToDTO_DTOFieldsMatchOriginalObject(){
        Customer customer = new Customer(0, "Bob", "Smith", "bob@mail.com", "Pass", "5551234567", new Location(0, "123 Fake St", "Springfield", State.NY, "01234"));
        CustomerDTO d = CustomerDTO.customerToDTO().apply(customer);
        Assertions.assertEquals(0, d.getCustomerId());
        Assertions.assertEquals("Bob", d.getFirstName());
        Assertions.assertEquals("Smith", d.getLastName());
        Assertions.assertEquals("bob@mail.com", d.getEmail());
        Assertions.assertEquals("", d.getPassword());
        Assertions.assertEquals("5551234567",d.getPhoneNumber());
        Location l = new Location(0, "123 Fake St", "Springfield", State.NY, "01234");
        Assertions.assertEquals(l,d.getLocation());
    }

    @Test
    void whenConvertingFromDTO_ObjectFieldsMatchOriginalDTO(){

        CustomerDTO d = new CustomerDTO(0,"Bob","Smith","bob@mail.com","Pass","5551234567",new Location(0, "123 Fake St", "Springfield", State.NY, "01234"));
        Customer c = CustomerDTO.DTOToCustomer().apply(d);
        Assertions.assertEquals(0, c.getCustomerId());
        Assertions.assertEquals("Bob", c.getFirstName());
        Assertions.assertEquals("Smith", c.getLastName());
        Assertions.assertEquals("bob@mail.com", c.getEmail());
        Assertions.assertEquals("Pass", c.getPassword());
        Assertions.assertEquals("5551234567",c.getPhoneNumber());
        Location l = new Location(0, "123 Fake St", "Springfield", State.NY, "01234");
        Assertions.assertEquals(l,c.getLocation());
    }
}
