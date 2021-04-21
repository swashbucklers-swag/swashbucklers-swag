package com.sk8.swashbucklers.repo.order;

import com.sk8.swashbucklers.model.item.Inventory;
import com.sk8.swashbucklers.model.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * Represents the Repository for Order Model
 *
 * @author Nick Zimmerman
 * @author Steven Ceglarek
 * */

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Page<Order> getByCustomer_CustomerId(final int customerId, Pageable pageable);

    Page<Order> getByLocation_LocationId(final int locationId, Pageable pageable);

    Page<Order> getByDateOfOrder(Timestamp dateOfOrder, Pageable pageable);

    Optional<Order> findByStatusHistory_historyId(final int historyId);

}
