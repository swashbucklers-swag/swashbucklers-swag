package com.sk8.swashbucklers.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sk8.swashbucklers.model.item.Item;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.Positive;

/**
 * Represents the details of an order
 * @author Daniel Bernier
 */

@Entity
@Table(name = "order_details")
@Data
@NoArgsConstructor
public class OrderDetails {

    @Id
    @GeneratedValue
    @Column(name = "order_details_id")
    private int orderDetailsId;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;
    @ManyToOne
    private Item item;
    @Positive
    @Column(nullable = false)
    private int quantity;

    public OrderDetails(int orderDetailsId, Item item, @Positive int quantity) {
        this.orderDetailsId = orderDetailsId;
        this.item = item;
        this.quantity = quantity;
    }
}
