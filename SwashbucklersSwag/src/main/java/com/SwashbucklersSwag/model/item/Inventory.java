package com.SwashbucklersSwag.model.item;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import javax.persistence.*;

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
    @OneToOne(mappedBy = "itemId")
    @Column(name = "item_id", nullable = false)
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
