package com.sk8.swashbucklers.repo.employee;

import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Timesheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.Optional;

/**
 * Represents the Repository for Timesheet Model
 *
 * @author Nick Zimmerman
 * */

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Integer> {

    /**
     *
     * @param employeeEmail
     * @return Timesheet requested from employee email
     */


    Optional<Timesheet> findByClockOutIsNull(Employee emp);
    Optional<Timesheet> findByClockInIsNull(Employee emp);
    Optional<Timesheet> findByEmployee_Email(String employeeEmail);
    Page<Timesheet> findByEmployee_EmployeeId(int employeeId, Pageable pagable);


}
