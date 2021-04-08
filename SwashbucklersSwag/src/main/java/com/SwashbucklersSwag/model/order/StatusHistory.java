package com.SwashbucklersSwag.model.order;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Represents a status history with a status and associated timestamp
 *
 * @author Daniel Bernier
 */
@Entity
@Table(name = "status_history")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class StatusHistory {
    @Id
    @GeneratedValue
    @Column(name = "history_id")
    private int historyId;
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;
    @CreationTimestamp
    @Column(name = "status_time", nullable = false)
    private Timestamp statusTime;
}
