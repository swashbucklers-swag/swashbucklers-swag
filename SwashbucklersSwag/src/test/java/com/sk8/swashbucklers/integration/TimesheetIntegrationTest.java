package com.sk8.swashbucklers.integration;

import com.sk8.swashbucklers.controller.TimesheetController;
import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Rank;
import com.sk8.swashbucklers.model.employee.Timesheet;
import com.sk8.swashbucklers.repo.employee.EmployeeRepository;
import com.sk8.swashbucklers.repo.employee.TimesheetRepository;
import com.sk8.swashbucklers.util.hashing.PasswordHashingUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
public class TimesheetIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private TimesheetController timesheetController;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordHashingUtil passwordHashingUtil;

    @Test
    void whenDefaultMapping_thenDirectoryDisplayed() throws Exception{
        mockMvc = MockMvcBuilders.standaloneSetup(timesheetController).build();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/clock")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();

        Assertions.assertEquals("<h3>\n" +
                "  Supported Endpoints for /clock:\n" +
                "</h3>\n" +
                "<ul>\n" +
                "  <li>/all :: GET</li>\n" +
                "  <li>/id :: GET</li>\n" +
                "  <li>/employee-id :: GET</li>\n" +
                "  <li>/in :: POST</li>\n" +
                "  <li>/out :: POST</li>\n" +
                "  <li>/update :: PUT</li>\n" +
                "</ul>",result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenTimesheet_whenGetAllTimesheet_thenTimesheetRetrieved() throws Exception{
        String pass = "";
        try {
            pass  = passwordHashingUtil.hashPasswordWithEmail("mkscott@dunder.com", "password");

        } catch (NoSuchAlgorithmException ignored) {
            Assertions.fail();
        }

        Employee employee = new Employee(0,"Michael","Scott", Rank.CAPTAIN, pass, "5704289173", null, "mkscott@dunder.com");
        Timestamp ci = new Timestamp(System.currentTimeMillis());
        Timestamp co = new Timestamp(System.currentTimeMillis());

        employeeRepository.save(employee);

        Timesheet timesheet = new Timesheet(0, co, ci,employee);

        timesheetRepository.save(timesheet);
        employee.setPassword(null);
        mockMvc = MockMvcBuilders.standaloneSetup(timesheetController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/clock/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].timesheetId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clockIn").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clockOut").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employee").value(employee))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenTimesheet_whenGetAllTimesheetWithPagination_thenTimesheetRetrievedWithPagination() throws Exception{
        String pass = "";
        try {
            pass  = passwordHashingUtil.hashPasswordWithEmail("mkscott@dunder.com", "password");

        } catch (NoSuchAlgorithmException ignored) {
            Assertions.fail();
        }

        Employee employee = new Employee(1,"Michael","Scott", Rank.CAPTAIN, pass, "5704289173", null, "mkscott@dunder.com");

        Timestamp ci = new Timestamp(System.currentTimeMillis());
        Timestamp co = new Timestamp(System.currentTimeMillis());

        employeeRepository.save(employee);


        Timesheet timesheet = new Timesheet(2, ci, co,employee);
        Timesheet timesheet2 = new Timesheet(3, ci, co,employee);

        timesheetRepository.save(timesheet);
        timesheetRepository.save(timesheet2);
        employee.setPassword(null);
        mockMvc = MockMvcBuilders.standaloneSetup(timesheetController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/clock/all?page=0&offset=50&sortby=\"timesheetId\"&order=\"DESC\"")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].timesheetId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clockIn").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clockOut").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employee").value(employee))



                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].timesheetId").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clockIn").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].clockOut").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].employee").value(employee))


                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.sorted").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.unsorted").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.empty").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value(50))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.paged").value(true))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenTimesheet_whenGetTimesheetById_thenTimesheetRetrieved() throws Exception{
        String pass = "";
        try {
            pass  = passwordHashingUtil.hashPasswordWithEmail("mkscott@dunder.com", "password");

        } catch (NoSuchAlgorithmException ignored) {
            Assertions.fail();
        }

        Employee employee = new Employee(0,"Michael","Scott", Rank.CAPTAIN, pass, "5704289173", null, "mkscott@dunder.com");
        Timestamp ci = new Timestamp(System.currentTimeMillis());
        Timestamp co = new Timestamp(System.currentTimeMillis());

        employeeRepository.save(employee);
        Timesheet timesheet = new Timesheet(0, ci, co,employee);

        timesheetRepository.save(timesheet);
        employee.setPassword(null);

        mockMvc = MockMvcBuilders.standaloneSetup(timesheetController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/clock/id/2")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timesheetId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clockIn").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clockOut").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee").value(employee))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenTimesheet_whenGetTimesheetByEmployeeId_thenTimesheetRetrieved() throws Exception{
        String pass = "";
        try {
            pass  = passwordHashingUtil.hashPasswordWithEmail("mkscott@dunder.com", "password");

        } catch (NoSuchAlgorithmException ignored) {
            Assertions.fail();
        }
        Employee employee = new Employee(0,"Michael","Scott", Rank.CAPTAIN, pass, "5704289173", null, "mkscott@dunder.com");
        Timestamp ci = new Timestamp(System.currentTimeMillis());
        Timestamp co = new Timestamp(System.currentTimeMillis());

        employeeRepository.save(employee);
        Timesheet timesheet = new Timesheet(0, ci, co,employee);

        timesheetRepository.save(timesheet);
        employee.setPassword(null);
        mockMvc = MockMvcBuilders.standaloneSetup(timesheetController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/clock/employee-id/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].timesheetId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clockIn").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].clockOut").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employee").value(employee))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenTimesheet_whenClockIn_thenTimesheetRetrieved() throws Exception{
        String pass = "";
        try {
            pass  = passwordHashingUtil.hashPasswordWithEmail("mkscott@dunder.com", "password");

        } catch (NoSuchAlgorithmException ignored) {
            Assertions.fail();
        }

        Employee employee = new Employee(0,"Michael","Scott", Rank.CAPTAIN, pass, "5704289173", null, "mkscott@dunder.com");
        employeeRepository.save(employee);
        employee.setPassword(null);
        mockMvc = MockMvcBuilders.standaloneSetup(timesheetController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/employee/clock/in")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\" : \"mkscott@dunder.com\", \"password\" : \"password\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timesheetId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clockIn").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clockOut").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee").value(employee))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenTimesheet_whenClockOut_thenTimesheetRetrieved() throws Exception{
        String pass = "";
        try {
            pass  = passwordHashingUtil.hashPasswordWithEmail("mkscott@dunder.com", "password");

        } catch (NoSuchAlgorithmException ignored) {
            Assertions.fail();
        }
        Employee employee = new Employee(0,"Michael","Scott", Rank.CAPTAIN, pass, "5704289173", null, "mkscott@dunder.com");
        Timestamp ci = new Timestamp(System.currentTimeMillis());
        Timesheet timesheet = new Timesheet(0, ci, null,employee);

        employeeRepository.save(employee);
        timesheetRepository.save(timesheet);
        employee.setPassword(null);
        mockMvc = MockMvcBuilders.standaloneSetup(timesheetController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/employee/clock/out")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\" : \"mkscott@dunder.com\", \"password\" : \"password\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timesheetId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clockIn").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clockOut").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee").value(employee))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenTimesheet_whenUpdateTimesheet_thenTimesheetRetrieved() throws Exception{
        String pass = "";
        try {
            pass  = passwordHashingUtil.hashPasswordWithEmail("mkscott@dunder.com", "password");

        } catch (NoSuchAlgorithmException ignored) {
            Assertions.fail();
        }
        Employee employee = new Employee(0,"Michael","Scott", Rank.CAPTAIN, pass, "5704289173", null, "mkscott@dunder.com");
        Timestamp ci = new Timestamp(System.currentTimeMillis());
        Timestamp co = new Timestamp(System.currentTimeMillis());

        employeeRepository.save(employee);
        Timesheet timesheet = new Timesheet(0, ci, null,employee);


        timesheetRepository.save(timesheet);
        employee.setPassword(null);
        mockMvc = MockMvcBuilders.standaloneSetup(timesheetController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/employee/clock/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"timesheetId\" : \"2\", \"clockIn\" : \"2021-04-17 07:34:00\", \"clockOut\" : \"2021-04-17 10:34:00\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timesheetId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clockIn").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clockOut").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee").value(employee))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

}
