package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.dto.EmployeeDTO;
import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Rank;
import com.sk8.swashbucklers.repo.employee.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * EmployeeServiceTest class holds the tests for all the methods declared in EmployeeService class
 */
@SpringBootTest
public class EmployeeServiceTest {


    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Test
    void whenGetAllEmployees_callRepository_getsEmployeesDTOPage(){
        List<Employee> employeeArrayList = new ArrayList<>();
        employeeArrayList.add(new Employee(0, "Michael", "Scott", Rank.CREW, "Pass", "5701234567", null, "mkscott@dunder.com"));
        Page<Employee> employees = new PageImpl<>(employeeArrayList);

        Mockito.when(employeeRepository.findAll(org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(employees);

        Page<EmployeeDTO> response = employeeService.getAllEmployee(0, 5, "email", "DESC");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getContent().get(0).getEmployeeId());
        Assertions.assertEquals("Michael", response.getContent().get(0).getFirstName());
        Assertions.assertEquals("Scott", response.getContent().get(0).getLastName());
        Assertions.assertEquals("mkscott@dunder.com", response.getContent().get(0).getEmail());
        Assertions.assertEquals("", response.getContent().get(0).getPassword());
        Assertions.assertEquals("5701234567", response.getContent().get(0).getPhoneNumber());
        Assertions.assertEquals(Rank.CREW, response.getContent().get(0).getRank());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertEquals(1, response.getNumberOfElements());
        System.out.println(response.getContent());
    }

    @Test
    void whenGetEmployeeById_callRepository_getsCorrectEmployee(){
        Optional<Employee> employee = Optional.of(new Employee(0, "Michael", "Scott", Rank.CREW, "Pass", "5701234567", null, "mkscott@dunder.com"));
        Mockito.when(employeeRepository.findById(1)).thenReturn(employee);
        Mockito.when(employeeRepository.findById(255)).thenReturn(Optional.empty());

        EmployeeDTO response = employeeService.getEmployeeById(1);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getEmployeeId());
        Assertions.assertEquals("Michael", response.getFirstName());
        Assertions.assertEquals("Scott", response.getLastName());
        Assertions.assertEquals("mkscott@dunder.com", response.getEmail());
        Assertions.assertEquals("", response.getPassword());
        Assertions.assertEquals("5701234567", response.getPhoneNumber());
        Assertions.assertEquals(Rank.CREW, response.getRank());
        System.out.println(response);

        response = employeeService.getEmployeeById(255);
        Assertions.assertNull(response);
    }

    @Test
    void whenGetEmployeeByEmail_callRepository_getsCorrectEmployee(){
        Optional<Employee> employee = Optional.of(new Employee(0, "Michael", "Scott", Rank.CREW, "Pass", "5701234567", null, "mkscott@dunder.com"));
        Mockito.when(employeeRepository.findByEmail("mkscott@dunder.com")).thenReturn(employee);
        Mockito.when(employeeRepository.findByEmail("nothere@mail.com")).thenReturn(Optional.empty());

        EmployeeDTO response = employeeService.getEmployeeByEmail("mkscott@dunder.com");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getEmployeeId());
        Assertions.assertEquals("Michael", response.getFirstName());
        Assertions.assertEquals("Scott", response.getLastName());
        Assertions.assertEquals("mkscott@dunder.com", response.getEmail());
        Assertions.assertEquals("", response.getPassword());
        Assertions.assertEquals("5701234567", response.getPhoneNumber());
        Assertions.assertEquals(Rank.CREW, response.getRank());
        System.out.println(response);

        response = employeeService.getEmployeeById(255);
        Assertions.assertNull(response);
    }


    @Test
    void whenGetEmployeesByRank_callRepository_getsEmployeesWithMatchingRankDTOPage(){
        List<Employee> captainArrayList = new ArrayList<>();
        List<Employee> crewArrayList = new ArrayList<>();
        captainArrayList.add(new Employee(1, "Michael", "Scott", Rank.CAPTAIN, "password", "5701234567", null, "mkscott@dunder.com"));
        crewArrayList.add(new Employee(2, "Dwight", "Schrute", Rank.CREW, "beets", "5704534367", null, "dwschrute@dunder.com"));
        Page<Employee> captains = new PageImpl<>(captainArrayList);
        Page<Employee> crews = new PageImpl<>(crewArrayList);

        Mockito.when(employeeRepository.findByRankEquals(org.mockito.ArgumentMatchers.eq(Rank.CAPTAIN), org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(captains);
        Mockito.when(employeeRepository.findByRankEquals(org.mockito.ArgumentMatchers.eq(Rank.CREW), org.mockito.ArgumentMatchers.isA(Pageable.class))).thenReturn(crews);

        Page<EmployeeDTO> response = employeeService.getEmployeeByRank("CAPTAIN", 0, 25, "email", "ASC");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getContent().get(0).getEmployeeId());
        Assertions.assertEquals("Michael", response.getContent().get(0).getFirstName());
        Assertions.assertEquals("Scott", response.getContent().get(0).getLastName());
        Assertions.assertEquals("mkscott@dunder.com", response.getContent().get(0).getEmail());
        Assertions.assertEquals("", response.getContent().get(0).getPassword());
        Assertions.assertEquals("5701234567", response.getContent().get(0).getPhoneNumber());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertEquals(1, response.getNumberOfElements());

        Assertions.assertEquals(1, response.getTotalPages());

        System.out.println(response.getContent());

        response = employeeService.getEmployeeByRank("CREW", 0, 100, "email", "DESC");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.getContent().get(0).getEmployeeId());
        Assertions.assertEquals("Dwight", response.getContent().get(0).getFirstName());
        Assertions.assertEquals("Schrute", response.getContent().get(0).getLastName());
        Assertions.assertEquals("dwschrute@dunder.com", response.getContent().get(0).getEmail());
        Assertions.assertEquals("", response.getContent().get(0).getPassword());
        Assertions.assertEquals("5704534367", response.getContent().get(0).getPhoneNumber());
        Assertions.assertEquals(1, response.getTotalPages());
        Assertions.assertEquals(1, response.getNumberOfElements());

        Assertions.assertEquals(1, response.getTotalPages());

        System.out.println(response.getContent());
    }

    @Test
    void whenCreateEmployee_callRepository_returnsCorrectEmployee(){
        Employee e = new Employee(0, "Michael", "Scott", Rank.CREW, "Pass", "5701234567", null, "mkscott@dunder.com");
        Mockito.when(employeeRepository.save(e)).thenReturn(e);

        EmployeeDTO response = employeeService.createEmployee(e);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getEmployeeId());
        Assertions.assertEquals("Michael", response.getFirstName());
        Assertions.assertEquals("Scott", response.getLastName());
        Assertions.assertEquals("mkscott@dunder.com", response.getEmail());
        Assertions.assertEquals("", response.getPassword());
        Assertions.assertEquals("5701234567", response.getPhoneNumber());
        Assertions.assertEquals(Rank.CREW, response.getRank());

        System.out.println(response);
    }

    @Test
    void whenUpdateEmployee_callRepository_returnsCorrectEmployee(){
        EmployeeDTO employeeDTO = new EmployeeDTO(0, "Michael", "Scott", "mkscott@dunder.com", "password", "5701234567",null,Rank.CAPTAIN);
        Employee e = new Employee(0, "Michael", "Scott", Rank.CAPTAIN, "password", "5701234567", null, "mkscott@dunder.com");
        Mockito.when(employeeRepository.save(e)).thenReturn(e);
        Mockito.when(employeeRepository.findById(0)).thenReturn(Optional.of(e));

        EmployeeDTO response = employeeService.updateEmployee(employeeDTO);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(0, response.getEmployeeId());
        Assertions.assertEquals("Michael", response.getFirstName());
        Assertions.assertEquals("Scott", response.getLastName());
        Assertions.assertEquals("mkscott@dunder.com", response.getEmail());
        Assertions.assertEquals("", response.getPassword());
        Assertions.assertEquals("5701234567", response.getPhoneNumber());
        Assertions.assertEquals(Rank.CAPTAIN, response.getRank());
        System.out.println(response);
    }



}
