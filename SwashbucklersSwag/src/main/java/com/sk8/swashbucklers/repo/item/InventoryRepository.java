package com.sk8.swashbucklers.repo.item;

import com.sk8.swashbucklers.model.item.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

}
