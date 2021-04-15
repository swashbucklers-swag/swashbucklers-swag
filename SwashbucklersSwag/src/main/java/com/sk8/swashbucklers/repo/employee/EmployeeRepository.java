package com.sk8.swashbucklers.repo.employee;

import com.sk8.swashbucklers.model.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Represents the Repository for Employee Model
 *
 * @author Nick Zimmerman
 * */

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    /**
     *
     * @param employeeEmail the email of desired employee
     * @return
     */
    Optional<Employee> findByEmail(final String employeeEmail);

    /**
     *
     * @param employeeEmail
     * @return {@link Employee#getEmail() get email}
     */
    Employee findByEmailAndPassword(final String employeeEmail, final String password);

}
