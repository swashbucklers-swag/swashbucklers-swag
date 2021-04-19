package com.sk8.swashbucklers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDTO {

    @Positive
    private int itemID;
    @Positive
    private int quantity;
}
