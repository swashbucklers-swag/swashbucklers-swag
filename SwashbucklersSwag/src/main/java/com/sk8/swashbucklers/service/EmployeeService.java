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

import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Optional;

/**
 * Represents the methods that can be executed for Employee's
 * @author Nick Zimmerman
 */
@Service
public class EmployeeService {

    private final EmployeeRepository EMPLOYEE_REPO;

    private final PasswordHashingUtil passwordHashingUtil;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PasswordHashingUtil passwordHashingUtil){
        this.EMPLOYEE_REPO = employeeRepository;
        this.passwordHashingUtil = passwordHashingUtil;
    }

    /**
     * Returns list of all employee's
     * @param page which page to display
     * @param offset how many entries per page
     * @param sortBy attribute to sort by
     * @param order asc or desc
     * @return page to be displayed
     */
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
     * Locates single employee by their Id
     * @param employeeId the employee's id
     * @return Employee or null
     *
     */
    public EmployeeDTO getEmployeeById(int employeeId){
        Optional<Employee> requested = EMPLOYEE_REPO.findById(employeeId);
        return requested.map(employee -> EmployeeDTO.employeeToDTO().apply(employee)).orElse(null);
    }


    /**
     * Locates single employee by their email
     * @param email the employee's email
     * @return Employee or null
     */
    public EmployeeDTO getEmployeeByEmail(String email){
        Optional<Employee> requested = EMPLOYEE_REPO.findByEmail(email);
        return requested.map(employee -> EmployeeDTO.employeeToDTO().apply(employee)).orElse(null);
    }

    /**
     * Returns list of employee's of specified rank
     * @param rank Employee level (i.e. Manager / Janitor)
     * @param page which page to display
     * @param offset how many entries per page
     * @param sortBy attribute to sort by
     * @param order asc or desc
     * @return page to be displayed
     */
    public Page<EmployeeDTO> getEmployeeByRank(String rank, int page, int offset, String sortBy, String order){
        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);
        //Rank r;

        try{
            Rank r = Rank.valueOf(rank.toUpperCase());

            Page<Employee> employees;
            if(order.equalsIgnoreCase("DESC"))
                employees = EMPLOYEE_REPO.findByRankEquals(r,PageRequest.of(page,offset,Sort.by(sortBy).descending()));
            else
                employees = EMPLOYEE_REPO.findByRankEquals(r,PageRequest.of(page,offset,Sort.by(sortBy).ascending()));

            return employees.map(EmployeeDTO.employeeToDTO());
        } catch (Exception ignored){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not understand Rank (CAPTAIN,CREW,LANDLUBBER)");
        }


    }

    /**
     * Create Employee
     * @param employee new employee to be persisted
     * @return EmployeeDTO upon successful persistence
     */
    public EmployeeDTO createEmployee(Employee employee){
        try {
            employee.setPassword(passwordHashingUtil.hashPasswordWithEmail(employee.getEmail(), employee.getPassword()));
        } catch (NoSuchAlgorithmException ignored) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Employee saved = EMPLOYEE_REPO.save(employee);
        return EmployeeDTO.employeeToDTO().apply(saved);
    }


    /**
     * Updates given Employee's information
     * @param employeeDTO data transfer object for employee
     * @return mutated employee
     */
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO){
        Optional<Employee> employeeOptional = EMPLOYEE_REPO.findById(employeeDTO.getEmployeeId());

        if(!employeeOptional.isPresent())
            return null;

        //updating fields
        Employee employee = employeeOptional.get();
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setRank(employeeDTO.getRank());
        try {
            employee.setPassword(passwordHashingUtil.hashPasswordWithEmail(employeeDTO.getEmail(),employeeDTO.getPassword()));
        } catch (NoSuchAlgorithmException ignored) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        employee.setPhoneNumber(employeeDTO.getPhoneNumber());
        employee.setLocation(employeeDTO.getLocation());

        return EmployeeDTO.employeeToDTO().apply(EMPLOYEE_REPO.save(employee));
    }

    /**
     * Validates page argument
     * @param page the page number
     * @return any positive page number
     */
    private int validatePage(int page){
        if(page < 0)
            page = 0;
        return page;
    }

    /**
     * Validates offset argument
     * @param offset the number of entries per page
     * @return an int of accepted number of entries per page
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
            case "phonenumber":
                return "phoneNumber";
            default:
                return "employeeId";
        }
    }

}
