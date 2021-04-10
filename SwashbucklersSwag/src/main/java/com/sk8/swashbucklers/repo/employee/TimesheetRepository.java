package com.sk8.swashbucklers.repo.employee;

import com.sk8.swashbucklers.model.employee.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Integer> {

}
