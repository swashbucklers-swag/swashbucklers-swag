package com.sk8.swashbucklers.repo.order;

import com.sk8.swashbucklers.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Represents the Repository for Order Model
 *
 * @author Nick Zimmerman
 * */

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> getByCustomer(Integer customerId);
    List<Order> getByDate(Timestamp dateOfOrder);

}
