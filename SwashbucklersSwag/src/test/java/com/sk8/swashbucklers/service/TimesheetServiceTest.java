package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.dto.LoginDTO;
import com.sk8.swashbucklers.dto.TimesheetDTO;
import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Rank;
import com.sk8.swashbucklers.model.employee.Timesheet;
import com.sk8.swashbucklers.repo.employee.EmployeeRepository;
import com.sk8.swashbucklers.repo.employee.TimesheetRepository;
import com.sk8.swashbucklers.util.hashing.PasswordHashingUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import javax.jws.Oneway;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Edson Rodriguez
 */

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:test-application.properties")
class TimesheetServiceTest {
    @MockBean
    private TimesheetRepository timesheetRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private TimesheetService timesheetService;

    @Autowired
    private PasswordHashingUtil PASSWORD_HASHING;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
     void whenGetAllTimesheets_returnsPageOfTimesheetDTO(){
        List<Timesheet> timesheetArrayList = new ArrayList<>();
        Employee employee = new Employee(0,"Michael","Scott", Rank.CAPTAIN, "password", "5704289173", null, "mkscott@dunder.com");
        Timestamp ci = new Timestamp(System.currentTimeMillis());
        Timestamp co = new Timestamp(System.currentTimeMillis());
        Timesheet timesheet = new Timesheet(0, ci, co,employee);

        timesheetArrayList.add(timesheet);
        Page<Timesheet> timesheetPage = new PageImpl<>(timesheetArrayList);

        Mockito.when(timesheetRepository.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(timesheetPage);

        Page<TimesheetDTO> response = timesheetService.getAllTimesheets(0,5,"timesheetId","DESC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0,response.getContent().get(0).getTimesheetId());
        Assertions.assertEquals(ci,response.getContent().get(0).getClockIn());
        Assertions.assertEquals(co,response.getContent().get(0).getClockOut());
        Assertions.assertEquals(employee,response.getContent().get(0).getEmployee());
        System.out.println(response.getContent());

        response = timesheetService.getAllTimesheets(0,10,"email","ASC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0,response.getContent().get(0).getTimesheetId());
        Assertions.assertEquals(ci,response.getContent().get(0).getClockIn());
        Assertions.assertEquals(co,response.getContent().get(0).getClockOut());
        Assertions.assertEquals(employee,response.getContent().get(0).getEmployee());
        System.out.println(response.getContent());
    }

    @Test
     void whenGetTimesheetById_returnsTimesheetDTO(){
        Employee employee = new Employee(0,"Michael","Scott", Rank.CAPTAIN, "password", "5704289173", null, "mkscott@dunder.com");
        Timestamp ci = new Timestamp(System.currentTimeMillis());
        Timestamp co = new Timestamp(System.currentTimeMillis());
        Timesheet timesheet = new Timesheet(0, ci, co,employee);

        Mockito.when(timesheetRepository.findById(0)).thenReturn(Optional.of(timesheet));
        Mockito.when(timesheetRepository.findById(255)).thenReturn(Optional.empty());

        TimesheetDTO response = timesheetService.getTimesheetById(0);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0,response.getTimesheetId());
        Assertions.assertEquals(ci,response.getClockIn());
        Assertions.assertEquals(co,response.getClockOut());
        Assertions.assertEquals(employee,response.getEmployee());
        System.out.println(response);

        response = timesheetService.getTimesheetById(255);
        Assertions.assertNull(response);
        System.out.println(response);
    }

    @Test
     void whenGetAllTimesheetsByEmployeeId_returnsPageOfTimesheetDTO(){
        List<Timesheet> timesheetArrayList = new ArrayList<>();
        Employee employee = new Employee(0,"Michael","Scott", Rank.CAPTAIN, "password", "5704289173", null, "mkscott@dunder.com");
        Timestamp ci = new Timestamp(System.currentTimeMillis());
        Timestamp co = new Timestamp(System.currentTimeMillis());
        Timesheet timesheet = new Timesheet(0, ci, co,employee);

        timesheetArrayList.add(timesheet);
        Page<Timesheet> timesheetPage = new PageImpl<>(timesheetArrayList);


        Mockito.when(timesheetRepository.findByEmployee_EmployeeId(org.mockito.ArgumentMatchers.isA(Integer.class),org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(timesheetPage);
        Page<TimesheetDTO> response = timesheetService.getAllTimesheetsByEmployeeId(0,25,5,"clockin","DESC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0,response.getContent().get(0).getTimesheetId());
        Assertions.assertEquals(ci,response.getContent().get(0).getClockIn());
        Assertions.assertEquals(co,response.getContent().get(0).getClockOut());
        Assertions.assertEquals(employee,response.getContent().get(0).getEmployee());
        System.out.println(response.getContent());

        response = timesheetService.getAllTimesheetsByEmployeeId(0,50,10,"clockOut","DESC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0,response.getContent().get(0).getTimesheetId());
        Assertions.assertEquals(ci,response.getContent().get(0).getClockIn());
        Assertions.assertEquals(co,response.getContent().get(0).getClockOut());
        Assertions.assertEquals(employee,response.getContent().get(0).getEmployee());
        System.out.println(response.getContent());
    }

    @Test
    void whenClockIn_returnsTimesheetDTO(){
        Employee employee = new Employee(0,"Michael","Scott", Rank.CAPTAIN, passwordEncoder.encode("password"), "5704289173", null, "mkscott@dunder.com");
        LoginDTO loginDTO = new LoginDTO("mkscott@dunder.com", "password");
        Timestamp ci = new Timestamp(System.currentTimeMillis());
        Timestamp co = new Timestamp(System.currentTimeMillis());
        Timesheet timesheet = new Timesheet(0, ci, co,employee);

        Mockito.when(employeeRepository.findByEmail("mkscott@dunder.com")).thenReturn(Optional.of(employee));
        Mockito.when(timesheetRepository.findByEmployeeAndClockOutIsNull(employee)).thenReturn(Optional.empty());
        Mockito.when(timesheetRepository.save(org.mockito.ArgumentMatchers.isA(Timesheet.class))).thenReturn(timesheet);

        TimesheetDTO response = timesheetService.clockIn(loginDTO);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0,response.getTimesheetId());
        Assertions.assertEquals(ci,response.getClockIn());
        Assertions.assertEquals(co,response.getClockOut());
        Assertions.assertEquals(employee,response.getEmployee());
        System.out.println(response);
    }

    @Test
    void whenClockOut_returnsTimesheetDTO(){
        Employee employee = new Employee(0,"Michael","Scott", Rank.CAPTAIN, passwordEncoder.encode("password"), "5704289173", null, "mkscott@dunder.com");
        LoginDTO loginDTO = new LoginDTO("mkscott@dunder.com","password");
        Timestamp ci = new Timestamp(System.currentTimeMillis());
        Timestamp co = new Timestamp(System.currentTimeMillis());
        Timesheet timesheet = new Timesheet(0, ci, null,employee);

        Mockito.when(employeeRepository.findByEmail("mkscott@dunder.com")).thenReturn(Optional.of(employee));
        Mockito.when(timesheetRepository.findByEmployeeAndClockOutIsNull(employee)).thenReturn(Optional.of(timesheet));

        TimesheetDTO response = timesheetService.clockOut(loginDTO);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0,response.getTimesheetId());
        Assertions.assertEquals(ci,response.getClockIn());
        Assertions.assertNotNull(response.getClockOut());
        Assertions.assertEquals(employee,response.getEmployee());
        System.out.println(response);
    }

    @Test
     void whenUpdateTimesheet_returnsTimesheetDTO(){
        Employee employee = new Employee(0,"Michael","Scott", Rank.CAPTAIN, "password", "5704289173", null, "mkscott@dunder.com");
        Timestamp ci = new Timestamp(System.currentTimeMillis());
        Timestamp co = new Timestamp(System.currentTimeMillis());
        Timesheet timesheet = new Timesheet(0, ci, co,employee);

        Mockito.when(timesheetRepository.findById(0)).thenReturn(Optional.of(timesheet));

        ci = new Timestamp(System.currentTimeMillis());
        co = new Timestamp(System.currentTimeMillis());

        TimesheetDTO response = timesheetService.updateTimesheet(0,ci,co);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0,response.getTimesheetId());
        Assertions.assertEquals(ci,response.getClockIn());
        Assertions.assertEquals(co,response.getClockOut());
        Assertions.assertEquals(employee,response.getEmployee());
        System.out.println(response);
    }

}
