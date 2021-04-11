package com.sk8.swashbucklers.repo.customer;

import com.sk8.swashbucklers.model.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents the Repository for Customer Model
 *
 * @author Nick Zimmerman
 * */

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    /**
     *
     * @param customerEmail
     * @return Customer
     */
    Customer findByEmail(String customerEmail);


}
