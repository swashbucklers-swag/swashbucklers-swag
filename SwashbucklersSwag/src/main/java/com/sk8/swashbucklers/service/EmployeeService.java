package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.dto.EmployeeDTO;
import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Rank;
import com.sk8.swashbucklers.repo.employee.EmployeeRepository;
import com.sk8.swashbucklers.util.hashing.PasswordHashingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Email;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository EMPLOYEE_REPO;

    private final PasswordHashingUtil passwordHashingUtil;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PasswordHashingUtil passwordHashingUtil){
        this.EMPLOYEE_REPO = employeeRepository;
        this.passwordHashingUtil = passwordHashingUtil;
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


    /**
     *
     * @param employeeId the employee's id
     * @return Employee or null
     */
    public EmployeeDTO getEmployeeById(int employeeId){
        Optional<Employee> requested = EMPLOYEE_REPO.findById(employeeId);
        return requested.map(employee -> EmployeeDTO.employeeToDTO().apply(employee)).orElse(null);
    }


    /**
     *
     * @param email the employee's email
     * @return Employee or null
     */
    public EmployeeDTO getEmployeeByEmail(@Email String email){
        Optional<Employee> requested = EMPLOYEE_REPO.findByEmail(email);
        return requested.map(employee -> EmployeeDTO.employeeToDTO().apply(employee)).orElse(null);
    }


    public Page<EmployeeDTO> getEmployeeByRank(Rank rank, int page, int offset, String sortBy, String order){
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


    public EmployeeDTO createEmployee(Employee employee){
//        try {
//            employee.setPassword(passwordHashingUtil.hashPasswordWithEmail(employee.getEmail(), employee.getPassword()));
//        } catch (NoSuchAlgorithmException ignored) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        Employee saved = EMPLOYEE_REPO.save(employee);
//        return EmployeeDTO.employeeToDTO().apply(saved);
        return null;
    }


    public EmployeeDTO loginEmployee(EmployeeDTO employeeDTO){
        return null;
    }


    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO){
//        Optional<Employee> employeeOptional = EMPLOYEE_REPO.findById(employeeDTO.getEmployeeId());
//
//        if(!employeeOptional.isPresent())
//            return null;
//
//        //updating fields
//        Employee employee = employeeOptional.get();
//        employee.setFirstName(employeeDTO.getFirstName());
//        employee.setLastName(employeeDTO.getLastName());
//        employee.setEmail(employeeDTO.getEmail());
//        try {
//            employee.setPassword(passwordHashingUtil.hashPasswordWithEmail(employeeDTO.getEmail(),employeeDTO.getPassword()));
//        } catch (NoSuchAlgorithmException ignored) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        employee.setPhoneNumber(employeeDTO.getPhoneNumber());
//        employee.setLocation(employeeDTO.getLocation());
//
//        return EmployeeDTO.employeeToDTO().apply(EMPLOYEE_REPO.save(employee));
        return null;
    }

    /**
     *
     * @param page
     * @return
     */
    private int validatePage(int page){
        if(page < 0)
            page = 0;
        return page;
    }

    /**
     *
     * @param offset
     * @return
     */
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
            case "firstname":
                return "firstName";
            case "lastname":
                return "lastName";
            default:
                return "employeeId";
        }
    }

}
