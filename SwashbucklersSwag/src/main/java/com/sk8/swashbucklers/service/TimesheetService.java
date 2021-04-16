package com.sk8.swashbucklers.services;

import com.sk8.swashbucklers.dto.TimesheetDTO;
import com.sk8.swashbucklers.model.employee.Timesheet;
import com.sk8.swashbucklers.repo.employee.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Locale;
import java.util.Optional;

@Service
public class TimesheetService {

    private final TimesheetRepository TIMESHEET_REPO;

    @Autowired
    public TimesheetService(TimesheetRepository timesheetRepo){
        this.TIMESHEET_REPO = timesheetRepo;
    }

    public Page<TimesheetDTO> getAllTimesheets(int page, int offset, String sortBy, String order){

        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);

        Page<Timesheet> timesheets;
        if(order.equalsIgnoreCase("DESC"))
            timesheets = TIMESHEET_REPO.findAll(PageRequest.of(page, offset, Sort.by(sortBy).descending()));
        else
            timesheets = TIMESHEET_REPO.findAll(PageRequest.of(page, offset, Sort.by(sortBy).ascending()));

        return timesheets.map(TimesheetDTO.timesheetToDTO());
    }

    public TimesheetDTO getTimesheetById(int timesheetId){
        Optional<Timesheet> req = TIMESHEET_REPO.findById(timesheetId);
        return req.map(timesheet -> TimesheetDTO.timesheetToDTO().apply(timesheet)).orElse(null);
    }

    public Page<TimesheetDTO> getAllTimesheetsByEmployeeId(int employeeId, int page, int offset, String sortBy, String order){
        page = validatePage(page);
        offset = validateOffset(offset);
        sortBy = validateSortBy(sortBy);

        Page<Timesheet> timesheets;

        if(order.equalsIgnoreCase("DESC"))
            timesheets = TIMESHEET_REPO.findByEmployee_EmployeeId(employeeId, PageRequest.of(page, offset, Sort.by(sortBy).descending()));
        else
            timesheets = TIMESHEET_REPO.findByEmployee_EmployeeId(employeeId, PageRequest.of(page, offset, Sort.by(sortBy).ascending()));
        return timesheets.map(TimesheetDTO.timesheetToDTO());
    }

    public TimesheetDTO clockIn(String username, String password){

        TIMESHEET_REPO.findByClockOutIsNull()

        Timesheet saved = TIMESHEET_REPO.save(timesheet);
        return TimesheetDTO.timesheetToDTO().apply(saved);
    }

    public TimesheetDTO clockOut(Timesheet timesheet){
        Timesheet saved = TIMESHEET_REPO.save(timesheet);
        return TimesheetDTO.timesheetToDTO().apply(saved);
    }



    private int validateOffset(int offset){
        if(offset != 5 && offset != 10 && offset != 25 && offset != 50 && offset != 100)
            offset = 25;
        return offset;
    }

    private int validatePage(int page){
        if(page < 0)
            page = 0;
        return page;
    }

    private String validateSortBy(String sortBy){
        switch (sortBy.toLowerCase(Locale.ROOT)){
            case "clockIn":
                return "clockIn";
            case "clockOut":
                return "clockOut";
            case "id":
                return "employee.employeeId";
            case "name":
                return "employee.name";
            case "email":
                return "employee.email";
            default:
                return "timesheetId";
        }
    }
}
