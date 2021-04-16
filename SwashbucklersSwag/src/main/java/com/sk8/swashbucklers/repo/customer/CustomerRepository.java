package com.sk8.swashbucklers.repo.customer;

import com.sk8.swashbucklers.model.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Represents the Repository for Customer Model
 *
 * @author Nick Zimmerman
 * @author John Stone
 * */

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmail(String customerEmail);
    Optional<Customer> findByPhoneNumber(String customerPhoneNumber);
    Page<Customer> findByLocation_LocationId(int locationId, Pageable pageable);
}
