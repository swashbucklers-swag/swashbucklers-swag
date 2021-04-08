package com.SwashbucklersSwag.model.item;

import jakarta.validation.constraints.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private int itemId;
    @Size(min = 1)
    @Column(nullable = false)
    private String description;
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String name;
    @Positive
    @Digits(integer = 10, fraction = 2)
    @Column(nullable = false)
    private double price;
    @PositiveOrZero
    @DecimalMax(value = "100", inclusive = false)
    @Column
    private int discount;

    public Item() {}

    public Item(int itemId, @Size(min = 1) String description, @Size(min = 1, max = 255) String name, @Positive @Digits(integer = 10, fraction = 2) double price, @PositiveOrZero @DecimalMax(value = "100", inclusive = false) int discount) {
        this.itemId = itemId;
        this.description = description;
        this.name = name;
        this.price = price;
        this.discount = discount;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return itemId == item.itemId && Double.compare(item.price, price) == 0 && discount == item.discount && Objects.equals(description, item.description) && Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, description, name, price, discount);
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                '}';
    }
}
