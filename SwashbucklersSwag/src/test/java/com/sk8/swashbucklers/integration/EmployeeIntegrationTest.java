package com.sk8.swashbucklers.integration;

import com.sk8.swashbucklers.controller.EmployeeController;
import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Rank;
import com.sk8.swashbucklers.repo.employee.EmployeeRepository;
import com.sk8.swashbucklers.repo.location.LocationRepository;
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

/**
 * Testing {@link EmployeeController}
 * @author Nick Zimmerman
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
public class EmployeeIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeController employeeController;


    @Test
    void whenDefaultMapping_thenDirectoryDisplayed() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();
        Assertions.assertEquals("<h3>\n" +
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
                "</ul>", result.getResponse().getContentAsString());
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenEmployee_whenGetAll_thenEmployeeRetrieved() throws Exception {
        Employee temp = new Employee(0,"Michael","Scott", Rank.CAPTAIN, "password", "5704289173", null, "mkscott@dunder.com");

        employeeRepository.save(temp);

        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/all").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employeeId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Michael"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value("Scott"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value("mkscott@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].phoneNumber").value("5704289173"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].rank").value("CAPTAIN"))
                .andReturn();
        System.out.println("\n"+result.getResponse().getContentAsString()+"\n");
    }

    @Test
    void givenEmployee_whenGetAll_thenEmployeeRetrievedWithPagination() throws Exception {
        Employee emp1 = new Employee(0,"Michael","Scott", Rank.CAPTAIN, "password", "5704289173", null, "mkscott@dunder.com");
        Employee emp2 = new Employee(0,"Dwight","Schrute", Rank.CREW, "beets", "5704577896", null, "dwschrute@dunder.com");

        employeeRepository.save(emp1);
        employeeRepository.save(emp2);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/all?page=0&offset=100&sortby=\"firstName\"&order=\"DESC\"")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].employeeId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Michael"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value("Scott"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value("mkscott@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].phoneNumber").value("5704289173"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].rank").value("CAPTAIN"))

                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[2].employeeId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].firstName").value("Dwight"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].lastName").value("Schrute"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].email").value("dwschrute@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].phoneNumber").value("5704577896"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].rank").value("CREW"))


                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.sorted").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.unsorted").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.empty").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.paged").value(true))
                .andReturn();

        System.out.println("\n"+result.getResponse().getContentAsString()+"\n");

    }

    @Test
    void givenEmployee_whenGetAll_thenEmployeeRetrievedWithPagination1() throws Exception {
        Employee emp1 = new Employee(0,"Michael","Scott", Rank.CAPTAIN, "password", "5704289173", null, "mkscott@dunder.com");
        Employee emp2 = new Employee(0,"Dwight","Schrute", Rank.CREW, "beets", "5704577896", null, "dwschrute@dunder.com");

        employeeRepository.save(emp1);
        employeeRepository.save(emp2);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/all?page=0&offset=25&sortby=\"email\"&order=\"ASC\"")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].employeeId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Michael"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value("Scott"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value("mkscott@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].phoneNumber").value("5704289173"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].rank").value("CAPTAIN"))

                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[2].employeeId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].firstName").value("Dwight"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].lastName").value("Schrute"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].email").value("dwschrute@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].phoneNumber").value("5704577896"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].rank").value("CREW"))


                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.sorted").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.unsorted").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.empty").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value(25))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.paged").value(true))
                .andReturn();

        System.out.println("\n"+result.getResponse().getContentAsString()+"\n");

    }

    @Test
    void givenEmployee_whenGetAll_thenEmployeeRetrievedWithPagination2() throws Exception {
        Employee emp1 = new Employee(0,"Michael","Scott", Rank.CAPTAIN, "password", "5704289173", null, "mkscott@dunder.com");
        Employee emp2 = new Employee(0,"Dwight","Schrute", Rank.CREW, "beets", "5704577896", null, "dwschrute@dunder.com");

        employeeRepository.save(emp1);
        employeeRepository.save(emp2);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/all?page=0&offset=10&sortby=\"lastName\"&order=\"DESC\"")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].employeeId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Michael"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value("Scott"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value("mkscott@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].phoneNumber").value("5704289173"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].rank").value("CAPTAIN"))

                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[2].employeeId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].firstName").value("Dwight"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].lastName").value("Schrute"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].email").value("dwschrute@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].phoneNumber").value("5704577896"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].rank").value("CREW"))


                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.sorted").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.unsorted").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.empty").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.paged").value(true))
                .andReturn();

        System.out.println("\n"+result.getResponse().getContentAsString()+"\n");

    }

    @Test
    void givenEmployee_whenGetAll_thenEmployeeRetrievedWithPagination3() throws Exception {
        Employee emp1 = new Employee(0,"Michael","Scott", Rank.CAPTAIN, "password", "5704289173", null, "mkscott@dunder.com");
        Employee emp2 = new Employee(0,"Dwight","Schrute", Rank.CREW, "beets", "5704577896", null, "dwschrute@dunder.com");

        employeeRepository.save(emp1);
        employeeRepository.save(emp2);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/all?page=0&offset=5&sortby=\"phoneNumber\"&order=\"ASC\"")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].employeeId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Michael"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value("Scott"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value("mkscott@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].phoneNumber").value("5704289173"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].rank").value("CAPTAIN"))

                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[2].employeeId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].firstName").value("Dwight"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].lastName").value("Schrute"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].email").value("dwschrute@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].phoneNumber").value("5704577896"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].rank").value("CREW"))


                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.sorted").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.unsorted").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.empty").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.paged").value(true))
                .andReturn();

        System.out.println("\n"+result.getResponse().getContentAsString()+"\n");

    }

    @Test
    void givenEmployee_whenGetAll_thenEmployeeRetrievedWithPagination4() throws Exception {
//        Location location = new Location(0,"3324 Berton","Scranton", State.PA,"18505");
//        Location location2 = new Location(0,"3296 Kiplin","Utica", State.PA,"18505");



        Employee emp1 = new Employee(0,"Michael","Scott", Rank.CAPTAIN, "password", "5704289173", null, "mkscott@dunder.com");
        Employee emp2 = new Employee(0,"Dwight","Schrute", Rank.CREW, "beets", "5704577896", null, "dwschrute@dunder.com");

        employeeRepository.save(emp1);
        employeeRepository.save(emp2);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/all?page=0&offset=5&sortby=\"city\"&order=\"ASC\"")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].employeeId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Michael"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value("Scott"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value("mkscott@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].phoneNumber").value("5704289173"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].rank").value("CAPTAIN"))

                //.andExpect(MockMvcResultMatchers.jsonPath("$.content[2].employeeId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].firstName").value("Dwight"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].lastName").value("Schrute"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].email").value("dwschrute@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].phoneNumber").value("5704577896"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].rank").value("CREW"))


                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.sorted").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.unsorted").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.empty").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.paged").value(true))
                .andReturn();

        System.out.println("\n"+result.getResponse().getContentAsString()+"\n");

    }

    @Test
    void givenEmployee_whenGetById_thenEmployeeRetrieved() throws Exception {
        Employee temp = new Employee(0,"Michael","Scott", Rank.CAPTAIN, "password", "5704289173", null, "mkscott@dunder.com");
        employeeRepository.save(temp);


        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/id/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.employeeId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Michael"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Scott"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("mkscott@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("5704289173"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rank").value("CAPTAIN"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenEmployee_whenGetByEmail_thenEmployeeRetrieved() throws Exception {
        Employee temp = new Employee(0,"Ryan","Howard", Rank.LANDLUBBER, "Bobcat", "5704289173", null, "ryhoward@dunder.com");
        employeeRepository.save(temp);


        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/email?email=ryhoward@dunder.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.employeeId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Ryan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Howard"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("ryhoward@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("5704289173"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rank").value("LANDLUBBER"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void givenEmployee_whenGetByRank_thenEmployeeRetrieved() throws Exception {
        Employee emp1 = new Employee(0,"Michael","Scott", Rank.CREW, "password", "5704289173", null, "mkscott@dunder.com");
        Employee emp2 = new Employee(0,"Dwight","Schrute", Rank.CREW, "beets", "5704577896", null, "dwschrute@dunder.com");

        employeeRepository.save(emp1);
        employeeRepository.save(emp2);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/employee/rank/CREW")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employeeId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Michael"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value("Scott"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value("mkscott@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].phoneNumber").value("5704289173"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].rank").value("CREW"))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].employeeId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].firstName").value("Dwight"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].lastName").value("Schrute"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].email").value("dwschrute@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].phoneNumber").value("5704577896"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].rank").value("CREW"))


                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.sorted").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.unsorted").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.sort.empty").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value(25))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.paged").value(true))
                .andReturn();

        System.out.println("\n"+result.getResponse().getContentAsString()+"\n");

    }

    @Test
    void createEmployee_thenEmployeeRetrieved() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/employee/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Toby\",\"lastName\" : \"Flenderson\", \"email\" : \"tbf@dunder.com\", \"password\" : \"scranton\",\"phoneNumber\" : \"5701234567\",\"rank\" : \"CREW\",\"location\" : {\"address\" : \"123 Fake St\",\"city\" : \"Scranton\",\"state\" : \"PA\",\"zip\" : \"18505\"}}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Toby"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Flenderson"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("tbf@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rank").value("CREW"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("5701234567"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.locationId").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.address").value("123 Fake St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.city").value("Scranton"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.state").value("PA"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.location.zip").value("18505"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }


    @Test
    void updateEmployee_thenEmployeeRetrieved() throws Exception {
        employeeRepository.save(new Employee(0,"Bryan","Howard", Rank.LANDLUBBER, "Bobcat", "5704289173", null, "ryhoward@dunder.com"));

        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/employee/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"employeeId\" : 1, \"firstName\" : \"Ryan\",\"lastName\" : \"Howard\", \"email\" : \"ryhoward@dunder.com\", \"password\" : \"Bobcat\",\"phoneNumber\" : \"5704289173\",\"rank\" : \"LANDLUBBER\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Ryan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Howard"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("ryhoward@dunder.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("5704289173"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rank").value("LANDLUBBER"))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}
