package com.sk8.swashbucklers.repo.order;

import com.sk8.swashbucklers.model.order.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {


}
