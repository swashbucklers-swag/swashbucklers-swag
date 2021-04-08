package com.SwashbucklersSwag.model.order;

import com.SwashbucklersSwag.model.item.Item;
import jakarta.validation.constraints.Positive;
import javax.persistence.*;
import java.util.Objects;

/**
 * Represents the details of an order
 * @author Daniel Bernier
 */

@Entity
@Table(name = "order_details")
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

    public OrderDetails() {}

    public OrderDetails(int orderDetailsId, Item item, @Positive int quantity) {
        this.orderDetailsId = orderDetailsId;
        this.item = item;
        this.quantity = quantity;
    }

    public int getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(int orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
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
        OrderDetails that = (OrderDetails) o;
        return orderDetailsId == that.orderDetailsId && quantity == that.quantity && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderDetailsId, item, quantity);
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderDetailsId=" + orderDetailsId +
                ", item=" + item +
                ", quantity=" + quantity +
                '}';
    }
}
