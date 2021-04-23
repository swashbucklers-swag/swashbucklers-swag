package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.dto.LoginDTO;
import com.sk8.swashbucklers.dto.TimesheetDTO;
import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Timesheet;
import com.sk8.swashbucklers.repo.employee.EmployeeRepository;
import com.sk8.swashbucklers.repo.employee.TimesheetRepository;
import com.sk8.swashbucklers.util.hashing.PasswordHashingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
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
    private final PasswordHashingUtil PASSWORD_HASHING;

    /**
     *  Constructor use to instantiate the repository's and the PasswordHashingUtil
     * @param timesheetRepo TimesheetRepository use throughout the TimesheetService
     * @param employeeRepository EmployeeRepository use throughout the TimesheetService
     * @param passwordHashingUtil PasswordHashingUtil use throughout the TimesheetService
     */
    @Autowired
    public TimesheetService(TimesheetRepository timesheetRepo, EmployeeRepository employeeRepository,PasswordHashingUtil passwordHashingUtil){
        this.TIMESHEET_REPO = timesheetRepo;
        this.EMPLOYEE_REPO = employeeRepository;
        this.PASSWORD_HASHING = passwordHashingUtil;
    }

    /**
     *  Get all timesheets, with pagination and sorting
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["clockin"|"clockout"|"id"|"firstname"|"lastname"|"email"|"timesheetId"] defaults to timesheetId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return A page of data transfer object representation of all timesheet objects with pagination and sorting applied
     */
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

    /**
     * Get the timesheet object by timesheetId
     * @param timesheetId The timesheetId used as criteria
     * @return A data transfer object that represents the timesheet object converted with {@link TimesheetDTO#timesheetToDTO()}
     */
    public TimesheetDTO getTimesheetById(int timesheetId){
        Optional<Timesheet> req = TIMESHEET_REPO.findById(timesheetId);
        return req.map(timesheet -> TimesheetDTO.timesheetToDTO().apply(timesheet)).orElse(null);
    }

    /**
     * Get all timesheets by the specified employeeId
     * @param employeeId The employeeId that is going to use to filter the timesheets
     * @param page The page to be selected defaults to 0 if page could not be understood
     * @param offset The number of elements per page [5|10|25|50|100] defaults to 25 if offset could not be understood
     * @param sortBy The property/field to sort by ["clockin"|"clockout"|"id"|"firstname"|"lastname"|"email"|"timesheetId"] defaults to timesheetId if sortby could not be understood
     * @param order The order in which the list is displayed ["ASC"|"DESC"]
     * @return A data transfer object that represents all the timesheets objects matching the specified employeeId with pagination and sorting applied
     */
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

    /**
     * Method used to clock in an employee
     * @param loginDTO A data transfer object that contains the email and password of the employee to clock in
     * @return A data transfer object that represents the newly created timesheet object
     */
    public TimesheetDTO clockIn(LoginDTO loginDTO){
        Optional<Employee> empOptional = EMPLOYEE_REPO.findByEmail(loginDTO.getEmail());

        if(empOptional.isPresent()){
            Optional<Timesheet> shouldBeNullTimesheet;

            if(PASSWORD_HASHING.comparePassword(empOptional.get().getPassword(),loginDTO.getPassword())){
                shouldBeNullTimesheet = TIMESHEET_REPO.findByEmployeeAndClockOutIsNull(empOptional.get());
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username/Password not found");
            }

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

    /**
     *  Method used to clock out an employee
     * @param loginDTO A data transfer object that contains the email and password of the employee to clock in
     * @return A data transfer object that represents the timesheet object that was updated with the clock out information
     */
    public TimesheetDTO clockOut(LoginDTO loginDTO){
        Optional<Employee> empOptional = EMPLOYEE_REPO.findByEmail(loginDTO.getEmail());

        if(empOptional.isPresent()){
            Optional<Timesheet> timesheetOptional;
            if(PASSWORD_HASHING.comparePassword(empOptional.get().getPassword(),loginDTO.getPassword())){
                timesheetOptional = TIMESHEET_REPO.findByEmployeeAndClockOutIsNull(empOptional.get());
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username/Password not found");
            }
            if(timesheetOptional.isPresent()){
                timesheetOptional.get().setClockOut(new Timestamp(System.currentTimeMillis()));
                TIMESHEET_REPO.save(timesheetOptional.get());
                return TimesheetDTO.timesheetToDTO().apply(timesheetOptional.get());
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Employee not clock in, please check with a manager.");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username/Password not found");
        }
    }

    /**
     *  Method used to update a timesheet given a timesheetId
     * @param timesheetId The id of the timesheet to modify
     * @param clockIn the new value for clockIn
     * @param clockOut the new value for clockOut
     * @return A data transfer object that represents the updated timesheet with the new clock in and clock out information
     */
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

    /**
     * Ensures permitted offset format
     * @param offset The offset value being validated
     * @return A valid offset value
     */
    private int validateOffset(int offset){
        if(offset != 5 && offset != 10 && offset != 25 && offset != 50 && offset != 100)
            offset = 25;
        return offset;
    }

    /**
     * Ensures permitted page format
     * @param page The page number value being validated
     * @return A valid page number value
     */
    private int validatePage(int page){
        if(page < 0)
            page = 0;
        return page;
    }

    /**
     * Ensures permitted sortby format
     * @param sortBy The sortby value being validated
     * @return A valid sortby value
     */
    private String validateSortBy(String sortBy){
        switch (sortBy.toLowerCase(Locale.ROOT)){
            case "clockin":
                return "clockIn";
            case "clockout":
                return "clockOut";
            case "id":
                return "employee.employeeId";
            case "firstname":
                return "employee.firstName";
            case "lastname":
                return "employee.lastName";
            case "email":
                return "employee.email";
            default:
                return "timesheetId";

        }
    }

    /**
     * Ensures permitted timestamp/date format
     * @param inputString the timestamp/date format being validated
     * @return true if the input string is in the right format, otherwise false.
     */
    public boolean isTimeStampValid(String inputString)
    {
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            format.parse(inputString);
            Pattern p = Pattern.compile("^\\d{4}[-]?\\d{1,2}[-]?\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}[.]?\\d{1,4}$");
            return p.matcher(inputString).matches();
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
