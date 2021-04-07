package com.SwashbucklersSwag.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Order {
    @Id
    @Column(name = "order_id")
    private int orderId;
    @Column(name = "customer_id")
    private int customerId;

    public Order() {
    }

    public Order(int orderId, int customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }
}
