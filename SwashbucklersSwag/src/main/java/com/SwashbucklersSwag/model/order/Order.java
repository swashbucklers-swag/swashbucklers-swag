package com.SwashbucklersSwag.model.order;

import com.SwashbucklersSwag.model.customer.Customer;
import com.SwashbucklersSwag.model.location.Location;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;
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
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@AllArgsConstructor
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

}
