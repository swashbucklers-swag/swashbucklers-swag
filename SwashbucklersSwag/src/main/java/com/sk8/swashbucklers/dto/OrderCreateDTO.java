package com.sk8.swashbucklers.dto;

import com.sk8.swashbucklers.model.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDTO {

    @Positive
    private int customerId;
    @NotNull
    private Location location;
    @NotNull
    private Set<OrderDetailsDTO> orderDetailsDTOSet;
}
