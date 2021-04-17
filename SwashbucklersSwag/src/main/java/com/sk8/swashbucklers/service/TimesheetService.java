package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.dto.LoginDTO;
import com.sk8.swashbucklers.dto.TimesheetDTO;
import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Timesheet;
import com.sk8.swashbucklers.repo.employee.EmployeeRepository;
import com.sk8.swashbucklers.repo.employee.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;


/**
 * Services for timesheet repository {@link TimesheetRepository}
 * @author Edson Rodriguez
 */
@Service
public class TimesheetService {

    private final TimesheetRepository TIMESHEET_REPO;
    private final EmployeeRepository EMPLOYEE_REPO;

    @Autowired
    public TimesheetService(TimesheetRepository timesheetRepo, EmployeeRepository employeeRepository){
        this.TIMESHEET_REPO = timesheetRepo;
        this.EMPLOYEE_REPO = employeeRepository;
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

    public TimesheetDTO clockIn(LoginDTO loginDTO){
        //first check if username and pass are correct
        //TODO: hash pass to compare
        Optional<Employee> empOptional = EMPLOYEE_REPO.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
        if(empOptional.isPresent()){
            //check if it he clockOut the day before
            Optional<Timesheet> shouldBeNullTimesheet = TIMESHEET_REPO.findByClockOutIsNull(empOptional.get());
            if(!shouldBeNullTimesheet.isPresent()){
                Timesheet timesheet = new Timesheet();
                timesheet.setEmployee(empOptional.get());
                timesheet.setClockIn(new Timestamp(System.currentTimeMillis()));
                Timesheet saved = TIMESHEET_REPO.save(timesheet);
                return TimesheetDTO.timesheetToDTO().apply(saved);
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Employee haves a pending clock out, check with a manager.");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username/Password not found");
        }
    }

    public TimesheetDTO clockOut(LoginDTO loginDTO){
        //first check if username and pass are correct
        //TODO: hash pass to compare
        Optional<Employee> empOptional = EMPLOYEE_REPO.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
        if(empOptional.isPresent()){
            Optional<Timesheet> timesheetOptional = TIMESHEET_REPO.findByClockOutIsNull(empOptional.get());
            if(timesheetOptional.isPresent()){
                timesheetOptional.get().setClockOut(new Timestamp(System.currentTimeMillis()));
                return TimesheetDTO.timesheetToDTO().apply(timesheetOptional.get());
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Employee not clock in, please check with a manager.");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username/Password not found");
        }
    }

    public TimesheetDTO updateTimesheet(int timesheetId, Timestamp clockIn, Timestamp clockOut){
        Optional<Timesheet> optionalTimesheet = TIMESHEET_REPO.findById(timesheetId);

        if(optionalTimesheet.isPresent()){
            if(isTimeStampValid(clockIn.toString()) && isTimeStampValid(clockOut.toString())){
                optionalTimesheet.get().setClockIn(clockIn);
                optionalTimesheet.get().setClockOut(clockOut);
                return TimesheetDTO.timesheetToDTO().apply(optionalTimesheet.get());
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Timestamp is not in the right format");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Timesheet not found");
        }
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

    public boolean isTimeStampValid(String inputString)
    {
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        try{
            format.parse(inputString);
            Pattern p = Pattern.compile("^\\d{4}[-]?\\d{1,2}[-]?\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}[.]?\\d{1,6}$");
            return p.matcher(inputString).matches();
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
