package com.sk8.swashbucklers.repo.employee;

import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Rank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Represents the Repository for Employee Model
 * @author Nick Zimmerman
 * */

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Employee> findByEmail(final String employeeEmail);
    Page<Employee> findByRankEquals(final Rank rank, Pageable pageable);
    Optional<Employee> findByEmailAndPassword(final String employeeEmail, final String password);

}
