package com.sk8.swashbucklers.model.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Represents an item entry, holding various properties about an item
 *
 * @author Daniel Bernier
 */
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
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "item")
    private Inventory inventory;

    public Item(int itemId, @Size(min = 1, max = 255) String name, @Size(min = 1) String description, @Positive @Digits(integer = 10, fraction = 2) double price, @PositiveOrZero @DecimalMax(value = "100", inclusive = false) int discount) {
        this.itemId = itemId;
        this.description = description;
        this.name = name;
        this.price = price;
        this.discount = discount;
    }
}
