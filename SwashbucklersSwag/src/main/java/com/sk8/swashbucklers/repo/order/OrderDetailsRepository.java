package com.sk8.swashbucklers.repo.order;

import com.sk8.swashbucklers.model.order.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents the Repository for OrderDetails Model
 *
 * @author Nick Zimmerman
 * @author Steven Ceglarek
 * */

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {

    OrderDetails findByOrder_OrderId(Integer orderId);

}
