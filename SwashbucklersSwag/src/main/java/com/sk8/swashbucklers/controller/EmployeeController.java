package com.sk8.swashbucklers.controller;


import com.sk8.swashbucklers.dto.EmployeeDTO;
import com.sk8.swashbucklers.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService EMPLOYEE_SERVICE;

    @Autowired
    public EmployeeController(EmployeeService employeeService){
        this.EMPLOYEE_SERVICE = employeeService;
    }

    public Page<EmployeeDTO> getAllEmployee(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "employee_id") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order)
    {
        return EMPLOYEE_SERVICE.getAllEmployee(page,offset,sortBy,order);
    }

}
