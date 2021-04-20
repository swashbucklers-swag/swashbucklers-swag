package com.sk8.swashbucklers.model.order;

import com.sk8.swashbucklers.model.customer.Customer;
import com.sk8.swashbucklers.model.location.Location;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private int orderId;
    @ManyToOne
    private Customer customer;
    @ManyToOne(cascade=CascadeType.ALL)
    private Location location;
    @CreationTimestamp
    @Column(name = "date_of_order", nullable = false)
    private Timestamp dateOfOrder;
    @OneToMany(mappedBy = "historyId", cascade = CascadeType.ALL)
    @Column(name = "history_id", nullable = false)
    private List<StatusHistory> statusHistory;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<OrderDetails> orderDetails;
}
