package com.sk8.swashbucklers.dto;

import com.sk8.swashbucklers.model.location.Location;
import com.sk8.swashbucklers.model.location.State;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.function.Function;

/**
 * Location data transfer object for {@link Location}
 *
 * @author Daniel Bernier
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    @PositiveOrZero
    private int locationId;
    @Size(min = 1)
    private String address;
    @Size(min = 1)
    private String city;
    @Size(min = 2, max = 2)
    private String state;
    @Size(min = 1)
    private String zip;

    /**
     * Converts a Location to a data transfer object
     * @return Data transfer object representing a location
     */
    public static Function<Location, LocationDTO> locationToDTO(){
        return (location) -> {
            Assert.notNull(location);
            return new LocationDTO(location.getLocationId(),
                    location.getAddress(),
                    location.getCity(),
                    location.getState().name(),
                    location.getZip());
        };
    }

    /**
     * Converts a data transfer object to a Location
     * @return Location from a data transfer object
     */
    public static Function<LocationDTO, Location> DTOToLocation(){
        return (locationDTO) -> {
            Assert.notNull(locationDTO);
            return new Location(locationDTO.getLocationId(),
                    locationDTO.getAddress(),
                    locationDTO.getCity(),
                    State.valueOf(locationDTO.getState()),
                    locationDTO.getZip());
        };
    }
}

