package com.sk8.swashbucklers.model.customer;

import com.sk8.swashbucklers.model.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.util.Assert;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.function.Function;

/**
 * Inventory data transfer object for {@link com.sk8.swashbucklers.model.customer}
 *
 * @author John Stone
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    @NotNull
    private int customerId;
    @NotNull
    @Size(min = 1)
    private String firstName;
    @NotNull
    @Size(min = 1)
    private String lastName;
    @NotNull
    @Size(min = 1)
    private String password;
    @NotNull @UniqueElements
    @Email
    private String email;
    @NotNull
    @Size(min = 10, max = 10)
    private String phoneNumber;
    @NotNull
    private Location location;

    public static Function<Customer, CustomerDTO> customerToDTO(){
        return (customer) -> {
            Assert.notNull(customer);
            return new CustomerDTO(customer.getCustomerId(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    "",
                    customer.getEmail(),
                    customer.getPhoneNumber(),
                    customer.getLocation());};
    }
    public static Function<CustomerDTO, Customer> DTOToCustomer(){
        return (customerDTO) -> {
            Customer customer = new Customer(customerDTO.getCustomerId(),
                    customerDTO.getFirstName(),
                    customerDTO.getLastName(),
                    customerDTO.getPassword(),
                    customerDTO.getEmail(),
                    customerDTO.getPhoneNumber(),
                    customerDTO.getLocation());
            return customer;
        };
    }
}
