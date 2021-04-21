package com.sk8.swashbucklers.dto;

import com.sk8.swashbucklers.model.order.OrderStatus;
import com.sk8.swashbucklers.model.order.StatusHistory;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;
import java.util.function.Function;

/**
 * Data transfer object for inventory quantity information
 *
 * @author Steven Ceglarek
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusHistoryDTO {

    @PositiveOrZero
    private int history_id;
    private String orderStatus;
    private Timestamp statusTime;


    /**
     * Converts a Location to a data transfer object
     * @return Data transfer object representing a location
     */
    public static Function<StatusHistory, StatusHistoryDTO> StatusHistoryToDTO() {
        return (statusHistory) -> {
            Assert.notNull(statusHistory);
            return new StatusHistoryDTO(statusHistory.getHistoryId(),
                    statusHistory.getOrderStatus().name(),
                    statusHistory.getStatusTime());
        };
    }

    /**
     * Converts a data transfer object to a Location
     * @return Location from a data transfer object
     */
    public static Function<StatusHistoryDTO, StatusHistory> DTOToStatusHistory () {
        return (statusHistoryDTO) -> {
            Assert.notNull(statusHistoryDTO);
            return new StatusHistory(statusHistoryDTO.getHistory_id(),
                    OrderStatus.valueOf(statusHistoryDTO.getOrderStatus()),
                    statusHistoryDTO.getStatusTime());
        };
    }
}
