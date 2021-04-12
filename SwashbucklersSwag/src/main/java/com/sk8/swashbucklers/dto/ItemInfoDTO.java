package com.sk8.swashbucklers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfoDTO {
    @Positive
    private int itemId;
    @Size(min = 1, max = 255)
    private String name;
    @Size(min = 1)
    private String description;
    @Positive
    @Digits(integer = 10, fraction = 2)
    private double price;
}
