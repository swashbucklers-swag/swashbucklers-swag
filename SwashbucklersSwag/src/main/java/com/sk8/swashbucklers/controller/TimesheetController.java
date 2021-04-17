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

    @GetMapping("/all")
    public Page<TimesheetDTO> getAllTimesheet(
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "timesheetId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){

        return TIMESHEET_SERVICE.getAllTimesheets(page,offset,sortBy,order);
    }

    @GetMapping("/id/{id}")
    public TimesheetDTO getTimesheetById(@PathVariable(name = "id") int id){
        return TIMESHEET_SERVICE.getTimesheetById(id);
    }

    @GetMapping("/employee-id/{id}")
    public Page<TimesheetDTO> getTimesheetByEmployeeId(
            @PathVariable(name = "id") int id,
            @RequestParam(value="page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "offset", required = false, defaultValue = "25") int offset,
            @RequestParam(value = "sortby", required = false, defaultValue = "timesheetId") String sortBy,
            @RequestParam(value = "order", required = false, defaultValue = "ASC") String order){
        return TIMESHEET_SERVICE.getAllTimesheetsByEmployeeId(id,page,offset,sortBy,order);
    }


    @PostMapping("/in")
    public TimesheetDTO clockIn(@RequestBody LoginDTO loginDTO){
        return TIMESHEET_SERVICE.clockIn(loginDTO);
    }

    @PostMapping("/out")
    public TimesheetDTO clockOut(@RequestBody LoginDTO loginDTO){
        return TIMESHEET_SERVICE.clockOut(loginDTO);
    }

    @PutMapping("/update")
    public TimesheetDTO updateTimesheet(@RequestBody UpdateTimesheetDTO updateTimesheetDTO){
        return TIMESHEET_SERVICE.updateTimesheet(updateTimesheetDTO.getTimesheetId(),
                updateTimesheetDTO.getClockIn(),updateTimesheetDTO.getClockOut());
    }
}
