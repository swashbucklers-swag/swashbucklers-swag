package com.sk8.swashbucklers.repo.order;

import com.sk8.swashbucklers.model.order.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents the Repository for StatusHistory Model
 *
 * @author Nick Zimmerman
 * @author Steven Ceglarek
 * */

@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory,Integer> {

}
