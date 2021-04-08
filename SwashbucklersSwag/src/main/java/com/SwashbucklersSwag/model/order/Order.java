package com.SwashbucklersSwag.model.order;

import com.SwashbucklersSwag.model.customer.Customer;
import com.SwashbucklersSwag.model.location.Location;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an order consisting of multiple order details:
 * <p>
 *     The customer who made the order,
 *     the location the order will ship to,
 *     and the status history of the order.
 * </p>
 *
 * @author Daniel Bernier
 */

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private int orderId;
    @ManyToOne
    @Column(name = "customer_id", nullable = false)
    private Customer customer;
    @ManyToOne
    @Column(name = "location_id", nullable = false)
    private Location location;
    @CreationTimestamp
    @Column(name = "date_of_order", nullable = false)
    private Timestamp dateOfOrder;
    @OneToMany(mappedBy = "historyId")
    @Column(name = "history_id", nullable = false)
    private StatusHistory statusHistory;
    @OneToMany(mappedBy = "order")
    private Set<OrderDetails> orderDetails;

    public Order() {}

    public Order(int orderId, Customer customer, Location location, Timestamp dateOfOrder, StatusHistory statusHistory, Set<OrderDetails> orderDetails) {
        this.orderId = orderId;
        this.customer = customer;
        this.location = location;
        this.dateOfOrder = dateOfOrder;
        this.statusHistory = statusHistory;
        this.orderDetails = orderDetails;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Timestamp getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(Timestamp dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public StatusHistory getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(StatusHistory statusHistory) {
        this.statusHistory = statusHistory;
    }

    public Set<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Set<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId == order.orderId && Objects.equals(customer, order.customer) && Objects.equals(location, order.location) && Objects.equals(dateOfOrder, order.dateOfOrder) && Objects.equals(statusHistory, order.statusHistory) && Objects.equals(orderDetails, order.orderDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, customer, location, dateOfOrder, statusHistory, orderDetails);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customer=" + customer +
                ", location=" + location +
                ", dateOfOrder=" + dateOfOrder +
                ", statusHistory=" + statusHistory +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
