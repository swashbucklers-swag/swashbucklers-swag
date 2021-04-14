package com.sk8.swashbucklers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Data transfer object for inventory quantity information
 *
 * @author Daniel Bernier
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryQuantityDTO {
    @Positive
    private int itemId;
    @PositiveOrZero
    private int quantity;
}
