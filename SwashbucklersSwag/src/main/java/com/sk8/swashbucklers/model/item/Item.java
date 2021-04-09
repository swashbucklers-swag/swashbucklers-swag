package com.sk8.swashbucklers.model.item;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private int itemId;
    @Size(min = 1, max = 255)
    @Column(nullable = false)
    private String name;
    @Size(min = 1)
    @Column(nullable = false)
    private String description;
    @Positive
    @Digits(integer = 10, fraction = 2)
    @Column(nullable = false)
    private double price;
    @PositiveOrZero
    @DecimalMax(value = "100", inclusive = false)
    @Column
    private int discount;

    public Item(int itemId, @Size(min = 1) String description, @Size(min = 1, max = 255) String name, @Positive @Digits(integer = 10, fraction = 2) double price, @PositiveOrZero @DecimalMax(value = "100", inclusive = false) int discount) {
        this.itemId = itemId;
        this.description = description;
        this.name = name;
        this.price = price;
        this.discount = discount;
    }
}
