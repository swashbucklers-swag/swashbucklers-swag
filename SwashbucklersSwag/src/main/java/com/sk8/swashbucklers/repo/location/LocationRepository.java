package com.sk8.swashbucklers.repo.location;

import com.sk8.swashbucklers.model.location.Location;
import com.sk8.swashbucklers.model.location.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents the Repository for Location Model
 *
 * @author Nick Zimmerman
 * @author Daniel Bernier
 * */

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Page<Location> findByCityContainingIgnoreCase(final String text, Pageable pageable);
    Page<Location> findByZip(final String text, Pageable pageable);
    Page<Location> findByState(final State state, Pageable pageable);
}
