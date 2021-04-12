package com.sk8.swashbucklers.repo.employee;

import com.sk8.swashbucklers.model.employee.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents the Repository for Timesheet Model
 *
 * @author Nick Zimmerman
 * */

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Integer> {

    /**
     *
     * @param email
     * @return Timesheet requested from employee email
     */
    Timesheet findByEmployee_Email(String email);
}
