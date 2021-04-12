package com.sk8.swashbucklers.repo.employee;

import com.sk8.swashbucklers.model.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents the Repository for Employee Model
 *
 * @author Nick Zimmerman
 * */

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    /**
     *
     * @param email
     * @return {@link Employee#getEmail() get email}
     */
    Employee findByEmail(String email);

}
