package com.sk8.swashbucklers.controller;

import com.sk8.swashbucklers.dto.LoginDTO;
import com.sk8.swashbucklers.dto.TimesheetDTO;
import com.sk8.swashbucklers.dto.UpdateTimesheetDTO;
import com.sk8.swashbucklers.service.TimesheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for timesheet resource handling using {@link TimesheetService}
 * @author Edson Rodriguez
 */

@RestController
@RequestMapping("/employee/clock")
public class TimesheetController {

    private final TimesheetService TIMESHEET_SERVICE;

    @Autowired
    public TimesheetController(TimesheetService timesheetService){
        this.TIMESHEET_SERVICE = timesheetService;
    }

    /**
     * Default landing page for /clock giving more information about requests and HTTP verbs
     * @return String with information supported endpoints
     */
    @GetMapping
    @PostMapping
    @PutMapping
    @DeleteMapping
    @RequestMapping
    @PatchMapping
    public String information(){
        return "<h3>\n" +
                "  Supported Endpoints for /clock:\n" +
                "</h3>\n" +
                "<ul>\n" +
                "  <li>/all :: GET</li>\n" +
                "  <li>/id :: GET</li>\n" +
                "  <li>/employee-id :: GET</li>\n" +
                "  <li>/in :: POST</li>\n" +
                "  <li>/out :: POST</li>\n" +
                "  <li>/update :: PUT</li>\n" +
                "</ul>";
    }

    /**
     *  Get all timesheets, with pagination and sorting
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["clockin"|"clockout"|"id"|"firstname"|"lastname"|"email"|"timesheetId"] defaults to timesheetId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return A page of data transfer object representation of all timesheet objects with pagination and sorting applied
     */
    @GetMapping("/all")
    public Page<TimesheetDTO> getAllTimesheet(
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "timesheetId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){

        return TIMESHEET_SERVICE.getAllTimesheets(page,offset,sortBy,order);
    }

    /**
     * Get the timesheet object by timesheetId
     * @param id The timesheetId used as criteria
     * @return A data transfer object that represents the timesheet object converted with {@link TimesheetDTO#timesheetToDTO()}
     */
    @GetMapping("/id/{id}")
    public TimesheetDTO getTimesheetById(@PathVariable(name = "id") int id){
        return TIMESHEET_SERVICE.getTimesheetById(id);
    }

    /**
     * Get all timesheets by the specified employeeId
     * @param id The employeeId that is going to use to filter the timesheets
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["clockin"|"clockout"|"id"|"firstname"|"lastname"|"email"|"timesheetId"] defaults to timesheetId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return A data transfer object that represents all the timesheets objects matching the specified employeeId with pagination and sorting applied
     */
    @GetMapping("/employee-id/{id}")
    public Page<TimesheetDTO> getTimesheetByEmployeeId(
            @PathVariable(name = "id") int id,
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "timesheetId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){
        return TIMESHEET_SERVICE.getAllTimesheetsByEmployeeId(id,page,offset,sortBy,order);
    }

    /**
     * Method used to clock in an employee
     * @param loginDTO A data transfer object that contains the email and password of the employee to clock in
     * @return A data transfer object that represents the newly created timesheet object
     */
    @PostMapping("/in")
    public TimesheetDTO clockIn(@RequestBody LoginDTO loginDTO){
        return TIMESHEET_SERVICE.clockIn(loginDTO);
    }

    /**
     *  Method used to clock out an employee
     * @param loginDTO A data transfer object that contains the email and password of the employee to clock in
     * @return A data transfer object that represents the timesheet object that was updated with the clock out information
     */
    @PostMapping("/out")
    public TimesheetDTO clockOut(@RequestBody LoginDTO loginDTO){
        return TIMESHEET_SERVICE.clockOut(loginDTO);
    }

    /**
     *  Method used to update a timesheet given a timesheetId
     * @param updateTimesheetDTO The data transfer object with the timesheet id, the new clock in and clock out information
     * @return A data transfer object that represents the updated timesheet with the new clock in and clock out information
     */
    @PutMapping("/update")
    public TimesheetDTO updateTimesheet(@RequestBody UpdateTimesheetDTO updateTimesheetDTO){
        return TIMESHEET_SERVICE.updateTimesheet(updateTimesheetDTO.getTimesheetId(),
                updateTimesheetDTO.getClockIn(),updateTimesheetDTO.getClockOut());
    }
}
