package com.sk8.swashbucklers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * Data transfer object for item discount information
 *
 * @author Daniel Bernier
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDiscountDTO {
    @Positive
    public int itemId;
    @PositiveOrZero
    @DecimalMax(value = "100", inclusive = false)
    public int discount;
}
