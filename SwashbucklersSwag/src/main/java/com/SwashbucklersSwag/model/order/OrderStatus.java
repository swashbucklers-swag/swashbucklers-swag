package com.SwashbucklersSwag.model.order;

/**
 * Enumeration representing the state of an order
 * <p>
 *     PROCESSING_ORDER - when an order is being processed and has not yet been shipped
 *     IN_TRANSIT - when an order has been shipped and has not yet arrived
 *     DELIVERED - when an order has arrived at the destination address
 *     CANCELLED - when an order has been cancelled and will not be delivered
 * </p>
 *
 * @author Daniel Bernier
 */
public enum OrderStatus {
    PROCESSING_ORDER,
    IN_TRANSIT,
    DELIVERED,
    CANCELLED
}
