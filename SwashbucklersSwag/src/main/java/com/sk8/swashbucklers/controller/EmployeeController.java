package com.sk8.swashbucklers.controller;


import com.sk8.swashbucklers.dto.EmployeeDTO;
import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for HTTP requests pertaining to employee class
 * @author Nick Zimmerman
 */

@RestController
@RequestMapping("/employee")
@Secured("ROLE_CAPTAIN")
public class EmployeeController {

    private final EmployeeService EMPLOYEE_SERVICE;


    @Autowired
    public EmployeeController(EmployeeService employeeService){
        this.EMPLOYEE_SERVICE = employeeService;
    }


    /**
     * Default landing page
     * @return String with information on employee endpoints
     */
    @GetMapping
    @PostMapping
    @PutMapping
    @DeleteMapping
    @RequestMapping
    @PatchMapping
    public String information(){
        return "<h3>\n" +
                "  Supported Endpoints for /employee:\n" +
                "</h3>\n" +
                "<ul>\n" +
                "  <li>/all :: GET</li>\n" +
                "  <li>/id :: GET</li>\n" +
                "  <li>/email :: GET</li>\n" +
                "  <li>/rank :: GET</li>\n" +
                "  <li>/phone :: GET</li>\n" +
                "  <li>/create :: POST</li>\n" +
                "  <li>/update :: PUT</li>\n" +
                "</ul>";
    }

    /**
     * Gets all employeeDTO's with pagination and sorting
     * @param page which page to display
     * @param offset how many entries per page
     * @param sortBy attribute to sort by
     * @param order asc or desc
     * @return page of EmployeeDTO entries to be displayed
     */
    @GetMapping("/all")
    public Page<EmployeeDTO> getAllEmployee(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "employeeId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order)
    {
        return EMPLOYEE_SERVICE.getAllEmployee(page,offset,sortBy,order);
    }

    /**
     * Gets employeeDTO with matching Id
     * @param id the employee's unique Id
     * @return The data transfer representation of the requested employee
     */
    @GetMapping("/id/{employeeId}")
    public EmployeeDTO getEmployeeById(@PathVariable(name = "employeeId") int id){
        return EMPLOYEE_SERVICE.getEmployeeById(id);
    }


    /**
     * Locates single employeeDTO by their email
     * @param email the employee's email
     * @return EmployeeDTO or null
     */
    @GetMapping("/email")
    public EmployeeDTO getEmployeeByEmail(@RequestParam(name="email") String email){
        return EMPLOYEE_SERVICE.getEmployeeByEmail(email);
    }


    /**
     * Retrieves page of EmployeeDTO entries filtered by rank
     * @param rank Employee level (i.e. Manager / Janitor)
     * @param page which page to display
     * @param offset how many entries per page
     * @param sortBy attribute to sort by
     * @param order asc or desc
     * @return page of EmployeeDTO entries to be displayed
     */
    @GetMapping("/rank/{rank}")
    public Page<EmployeeDTO> getEmployeeByRank(
            @PathVariable(name = "rank") String rank,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "employeeId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order)
    {
        return EMPLOYEE_SERVICE.getEmployeeByRank(rank,page,offset,sortBy,order);
    }

    /**
     * Forwards create employeeDTO request to service class
     * @param employeeDTO dto for incoming employee
     * @return new EmployeeDTO
     */
    @PostMapping("/create")
    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO employeeDTO){
        employeeDTO.setEmployeeId(0);
        Employee e = EmployeeDTO.DTOToEmployee().apply(employeeDTO);
        return EMPLOYEE_SERVICE.createEmployee(e);
    }

    /**
     * Updates given EmployeeDTO such that employeeId is specified
     * @param employeeDTO EmployeeDTO with employeeId of the Employee to be updated
     * @return updated EmployeeDTO
     */
    @PutMapping("/update")
    public EmployeeDTO updateEmployeeInfo(@RequestBody EmployeeDTO employeeDTO) {
        return EMPLOYEE_SERVICE.updateEmployee(employeeDTO);
    }
}
