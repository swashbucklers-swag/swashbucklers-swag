package com.sk8.swashbucklers.dto;

import com.sk8.swashbucklers.model.item.Inventory;
import com.sk8.swashbucklers.model.item.Item;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.function.Function;

/**
 * Inventory data transfer object for {@link com.sk8.swashbucklers.model.item.Inventory}
 *
 * @author Daniel Bernier
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {

    private int itemId;
    @Size(min = 1, max = 255)
    private String name;
    @Size(min = 1)
    private String description;
    @Digits(integer = 10, fraction = 2)
    private double price;
    @PositiveOrZero
    @DecimalMax(value = "100", inclusive = false)
    private int discount;
    @PositiveOrZero
    private int quantity;

    public static Function<Inventory, InventoryDTO> inventoryToDTO(){
        return (inventory) -> {
            Assert.notNull(inventory.getItem());
            return new InventoryDTO(inventory.getItem().getItemId(),
                                    inventory.getItem().getName(),
                                    inventory.getItem().getDescription(),
                                    inventory.getItem().getPrice(),
                                    inventory.getItem().getDiscount(),
                                    inventory.getQuantity());};
    }

    public static Function<InventoryDTO, Inventory> DTOToInventory(){
        return (inventoryDTO) -> {
            Item item = new Item(0,
                    inventoryDTO.getName(),
                    inventoryDTO.getDescription(),
                    inventoryDTO.getPrice(),
                    inventoryDTO.getDiscount());
            Inventory inventory = new Inventory(0, item, inventoryDTO.getQuantity());
            inventory.getItem().setInventory(inventory);
            return inventory;
        };
    }
}
