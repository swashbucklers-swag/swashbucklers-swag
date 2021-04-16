package com.sk8.swashbucklers.dto;

import com.sk8.swashbucklers.model.customer.Customer;
import com.sk8.swashbucklers.model.location.Location;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.function.Function;

/**
 * Customer data transfer object for {@link com.sk8.swashbucklers.model.customer}
 *
 * @author John Stone
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private int customerId;
    @Size(min = 1)
    private String firstName;
    @Size(min = 1)
    private String lastName;
    @UniqueElements
    @Email
    private String email;
    @Size(min = 1)
    private String password;
    @Size(min = 10, max = 10)
    private String phoneNumber;
    private Location location;

    public static Function<Customer, CustomerDTO> customerToDTO(){
        return customer -> {
            Assert.notNull(customer);
            return new CustomerDTO(customer.getCustomerId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getEmail(),
                    "",
                    customer.getPhoneNumber(),
                    customer.getLocation());};
    }
    public static Function<CustomerDTO, Customer> DTOToCustomer(){
        return customerDTO -> {
            Assert.notNull(customerDTO);
            return new Customer(customerDTO.getCustomerId(),
                    customerDTO.getFirstName(),
                    customerDTO.getLastName(),
                    customerDTO.getEmail(),
                    customerDTO.getPassword(),
                    customerDTO.getPhoneNumber(),
                    customerDTO.getLocation());
        };
    }
}
