package com.sk8.swashbucklers.model.item;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

/**
 * Represents an inventory entry, tracking quantity
 *
 * @author Daniel Bernier
 */
@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue
    @Column(name = "inventory_id")
    private int inventoryId;
    @OneToOne(mappedBy = "inventory")
    private Item item;
    @PositiveOrZero
    @Column(nullable = false)
    private int quantity;

    public Inventory(int inventoryId, Item item, @PositiveOrZero int quantity) {
        this.inventoryId = inventoryId;
        this.item = item;
        this.quantity = quantity;
    }
}
