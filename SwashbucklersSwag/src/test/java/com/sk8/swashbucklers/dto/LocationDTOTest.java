package com.sk8.swashbucklers.dto;

import com.sk8.swashbucklers.model.location.Location;
import com.sk8.swashbucklers.model.location.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * tests for {@link LocationDTO}
 *
 * @author Daniel Bernier
 */
@SpringBootTest
class LocationDTOTest {

    @Test
    void whenConvertingToDTO_dtoFieldsReflectOriginalObject(){
        Location l = new Location(1, "address", "city", State.NY, "12345");
        LocationDTO d = LocationDTO.locationToDTO().apply(l);
        Assertions.assertEquals(1, d.getLocationId());
        Assertions.assertEquals("address", d.getAddress());
        Assertions.assertEquals("city", d.getCity());
        Assertions.assertEquals("NY", d.getState());
        Assertions.assertEquals("12345", d.getZip());
    }

    @Test
    void whenConvertingFromDTO_locationFieldsReflectOriginalDTO(){
        LocationDTO d = new LocationDTO(1, "address", "city", "NY", "12345");
        Location l = LocationDTO.DTOToLocation().apply(d);
        Assertions.assertEquals(1, l.getLocationId());
        Assertions.assertEquals("address", l.getAddress());
        Assertions.assertEquals("city", l.getCity());
        Assertions.assertEquals(State.NY, l.getState());
        Assertions.assertEquals("12345", l.getZip());
    }
}
