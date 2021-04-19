package com.sk8.swashbucklers.dto;

import com.sk8.swashbucklers.model.customer.Customer;
import com.sk8.swashbucklers.model.item.Item;
import com.sk8.swashbucklers.model.location.Location;
import com.sk8.swashbucklers.model.location.State;
import com.sk8.swashbucklers.model.order.Order;
import com.sk8.swashbucklers.model.order.OrderDetails;
import com.sk8.swashbucklers.model.order.StatusHistory;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Order data transfer object for {@link Order}
 *
 * @author Steven Ceglarek
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    @PositiveOrZero
    private int orderId;
    private Customer customer;
    private Location location;
    private Timestamp dateOfOrder;
    private List<StatusHistory> statusHistory;
    private Set<OrderDetails> orderDetails;

    /**
     * Converts an Order to a data transfer object
     * @return Data transfer object representing an order
     */
    public static Function<Order, OrderDTO> OrderToDTO() {
        return (order) -> {
            Assert.notNull(order);
            return new OrderDTO(order.getOrderId(),
                    order.getCustomer(),
                    order.getLocation(),
                    order.getDateOfOrder(),
                    order.getStatusHistory(),
                    order.getOrderDetails());
        };
    }

        /**
         * Converts a data transfer object to an Order
         * @return Order from a data transfer object
         */
        public static Function<OrderDTO, Order> DTOToOrder () {
            return (orderDTO) -> {
                Assert.notNull(orderDTO);
                return new Order(orderDTO.getOrderId(),
                        orderDTO.getCustomer(),
                        orderDTO.getLocation(),
                        orderDTO.getDateOfOrder(),
                        orderDTO.getStatusHistory(),
                        orderDTO.getOrderDetails());
            };
        }


    }