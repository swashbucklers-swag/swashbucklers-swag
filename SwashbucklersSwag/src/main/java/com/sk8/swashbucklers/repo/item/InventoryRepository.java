package com.sk8.swashbucklers.repo.item;

import com.sk8.swashbucklers.model.item.Inventory;
import com.sk8.swashbucklers.model.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository for Inventory Model
 *
 * @author Nick Zimmerman
 * @author Daniel Bernier
 * */

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    Optional<Inventory> findByItem(final Item item);
    Optional<Inventory> findByItem_ItemId(final int itemId);
    Page<Inventory> findByItem_NameContainingIgnoreCase(final String text, Pageable pageable);
    Page<Inventory> findByItem_DescriptionContainingIgnoreCase(final String text, Pageable pageable);
    void deleteByItem_ItemId(final int itemId);
}
