package com.sk8.swashbucklers.repo.customer;

import com.sk8.swashbucklers.model.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {


}
