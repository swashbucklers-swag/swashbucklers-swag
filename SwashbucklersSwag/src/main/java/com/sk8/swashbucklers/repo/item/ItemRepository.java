package com.sk8.swashbucklers.repo.item;

import com.sk8.swashbucklers.model.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents the Repository for Item Model
 *
 * @author Nick Zimmerman
 * */

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {



}
