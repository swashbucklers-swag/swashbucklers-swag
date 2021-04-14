package com.sk8.swashbucklers.repo.employee;

import com.sk8.swashbucklers.model.employee.Timesheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Represents the Repository for Timesheet Model
 *
 * @author Nick Zimmerman
 * */

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Integer> {
}
