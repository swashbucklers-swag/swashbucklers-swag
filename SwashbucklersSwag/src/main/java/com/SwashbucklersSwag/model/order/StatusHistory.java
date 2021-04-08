package com.SwashbucklersSwag.model.order;

import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Represents a status history with a status and associated timestamp
 *
 * @author Daniel Bernier
 */
@Entity
@Table(name = "status_history")
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

    public StatusHistory(){}

    public StatusHistory(int historyId, OrderStatus orderStatus, Timestamp statusTime) {
        this.historyId = historyId;
        this.orderStatus = orderStatus;
        this.statusTime = statusTime;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Timestamp getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(Timestamp statusTime) {
        this.statusTime = statusTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusHistory that = (StatusHistory) o;
        return historyId == that.historyId && orderStatus == that.orderStatus && Objects.equals(statusTime, that.statusTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(historyId, orderStatus, statusTime);
    }

    @Override
    public String toString() {
        return "StatusHistory{" +
                "id=" + historyId +
                ", orderStatus=" + orderStatus +
                ", statusTime=" + statusTime +
                '}';
    }
}
