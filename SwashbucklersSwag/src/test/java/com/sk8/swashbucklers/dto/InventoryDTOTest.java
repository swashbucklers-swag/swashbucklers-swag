package com.sk8.swashbucklers.dto;

import com.sk8.swashbucklers.model.item.Inventory;
import com.sk8.swashbucklers.model.item.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * tests for {@link InventoryDTO}
 *
 * @author Daniel Bernier
 */
@SpringBootTest
class InventoryDTOTest {

    @Test
    void whenConvertingToDTO_DTOFieldsMatchOriginalObject(){
        Item item = new Item(1, "Boat", "Cool red boat", 255.99, 25);
        Inventory inventory = new Inventory(0, item, 13);
        item.setInventory(inventory);
        inventory.setItem(item);

        InventoryDTO d = InventoryDTO.inventoryToDTO().apply(inventory);
        Assertions.assertEquals(1, d.getItemId());
        Assertions.assertEquals("Boat", d.getName());
        Assertions.assertEquals("Cool red boat", d.getDescription());
        Assertions.assertEquals(255.99, d.getPrice());
        Assertions.assertEquals(25, d.getDiscount());
        Assertions.assertEquals(13, d.getQuantity());
    }

    @Test
    void whenConvertingFromDTO_ObjectFieldsMatchOriginalDTO(){
        InventoryDTO d = new InventoryDTO(1, "Boat", "Cool red boat", 255.99, 25, 13);
        Inventory i = InventoryDTO.DTOToInventory().apply(d);

        Assertions.assertEquals(0, i.getInventoryId());
        Assertions.assertEquals(0, i.getItem().getItemId());
        Assertions.assertEquals("Boat", i.getItem().getName());
        Assertions.assertEquals("Cool red boat", i.getItem().getDescription());
        Assertions.assertEquals(255.99, i.getItem().getPrice());
        Assertions.assertEquals(25, i.getItem().getDiscount());
        Assertions.assertEquals(13, i.getQuantity());
        Assertions.assertEquals(i, i.getItem().getInventory());
    }
}
