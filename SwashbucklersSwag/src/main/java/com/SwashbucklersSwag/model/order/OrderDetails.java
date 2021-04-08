package com.SwashbucklersSwag.model.order;

import com.SwashbucklersSwag.model.item.Item;
import jakarta.validation.constraints.Positive;
import lombok.*;
import javax.persistence.*;

/**
 * Represents the details of an order
 * @author Daniel Bernier
 */

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class OrderDetails {

    @Id
    @GeneratedValue
    @Column(name = "order_details_id")
    private int orderDetailsId;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @ManyToOne
    @Column(name = "item_id", nullable = false)
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
