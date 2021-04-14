package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.dto.EmployeeDTO;
import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.repo.employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class EmployeeService {

    private final EmployeeRepository EMPLOYEE_REPO;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository){
        this.EMPLOYEE_REPO = employeeRepository;
    }


    public Page<EmployeeDTO> getAllEmployee(int page, int offset, String sortBy, String order){
        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);

        Page<Employee> employees;
        if(order.equalsIgnoreCase("DESC"))
            employees = EMPLOYEE_REPO.findAll(PageRequest.of(page, offset, Sort.by(sortBy).descending()));
        else
            employees = EMPLOYEE_REPO.findAll(PageRequest.of(page, offset, Sort.by(sortBy).ascending()));

        return employees.map(EmployeeDTO.employeeToDTO());
    }




    private int validatePage(int page){
        if(page < 0)
            page = 0;
        return page;
    }

    private int validateOffset(int offset) {
        if(offset != 5 && offset != 10 && offset != 25 && offset != 50 && offset != 100)
            offset = 25;
        return offset;
    }


    /**
     * Ensures permitted sortby format
     * @param sortBy The sortby value being validated
     * @return A valid sortby value
     */
    private String validateSortBy(String sortBy){
        switch (sortBy.toLowerCase(Locale.ROOT)){
            case "email":
                return "email";
            case "rank":
                return "rank";
            default:
                return "employeeId";
        }
    }

}
