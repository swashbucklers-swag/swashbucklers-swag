package com.sk8.swashbucklers.controller;


import com.sk8.swashbucklers.dto.EmployeeDTO;
import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Rank;
import com.sk8.swashbucklers.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService EMPLOYEE_SERVICE;


    @Autowired
    public EmployeeController(EmployeeService employeeService){
        this.EMPLOYEE_SERVICE = employeeService;
    }


    @GetMapping("/all")
    public Page<EmployeeDTO> getAllEmployee(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "employeeId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order)
    {
        return EMPLOYEE_SERVICE.getAllEmployee(page,offset,sortBy,order);
    }


    @GetMapping("/id")
    public EmployeeDTO getEmployeeById(@PathVariable(name = "employeeId") int id){
        return EMPLOYEE_SERVICE.getEmployeeById(id);
    }


    @GetMapping("/email")
    public EmployeeDTO getEmployeeByEmail(String email){
        return EMPLOYEE_SERVICE.getEmployeeByEmail(email);
    }


    @GetMapping("/rank")
    public Page<EmployeeDTO> getEmployeeByRank(
            @PathVariable(name = "rank") Rank rank,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "employeeId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order)
    {
        return EMPLOYEE_SERVICE.getEmployeeByRank(rank,page,offset,sortBy,order);
    }


    @PostMapping("/create")
    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO employeeDTO){
        employeeDTO.setEmployeeId(0);
        Employee e = EmployeeDTO.DTOToEmployee().apply(employeeDTO);
        return EMPLOYEE_SERVICE.createEmployee(e);
    }


    @PostMapping("/login")
    public EmployeeDTO loginEmployee(EmployeeDTO employeeDTO){
        return EMPLOYEE_SERVICE.loginEmployee(employeeDTO);
    }


    @PutMapping("/update")
    public EmployeeDTO updateEmployeeInfo(@RequestBody EmployeeDTO employeeDTO) {
        return EMPLOYEE_SERVICE.updateEmployee(employeeDTO);
    }
}
