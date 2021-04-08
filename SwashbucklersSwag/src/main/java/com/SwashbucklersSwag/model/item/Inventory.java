package com.SwashbucklersSwag.model.item;

import jakarta.validation.constraints.PositiveOrZero;

import javax.persistence.*;
import java.util.Objects;

/**
 * Represents an inventory entry, tracking quantity
 *
 * @author Daniel Bernier
 */
@Entity
@Table(name = "inventory")
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

    public Inventory() {}

    public Inventory(int inventoryId, Item item, @PositiveOrZero int quantity) {
        this.inventoryId = inventoryId;
        this.item = item;
        this.quantity = quantity;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return inventoryId == inventory.inventoryId && quantity == inventory.quantity && Objects.equals(item, inventory.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventoryId, item, quantity);
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", item=" + item +
                ", quantity=" + quantity +
                '}';
    }
}
