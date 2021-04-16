package com.sk8.swashbucklers.dto;

import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Rank;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmployeeDTOTest {

    @Test
    void whenConvertingToDTO_DTOFieldsMatchOriginalObject(){
        Employee employee = new Employee(0,"Michael","Scott", Rank.CAPTAIN, "password", "5704289173", null, "mkscott@dunder.com");
        EmployeeDTO d = EmployeeDTO.employeeToDTO().apply(employee);
        Assertions.assertEquals(0, d.getEmployeeId());
        Assertions.assertEquals("Michael", d.getFirstName());
        Assertions.assertEquals("Scott", d.getLastName());
        Assertions.assertEquals("mkscott@dunder.com", d.getEmail());
        Assertions.assertEquals("", d.getPassword());
        Assertions.assertEquals("5704289173",d.getPhoneNumber());
    }

    @Test
    void whenConvertingFromDTO_ObjectFieldsMatchOriginalDTO(){

        EmployeeDTO d = new EmployeeDTO(0,"Michael","Scott","mkscott@dunder.com","password","5704289173",null, Rank.CAPTAIN);
        Employee c = EmployeeDTO.DTOToEmployee().apply(d);
        Assertions.assertEquals(0, c.getEmployeeId());
        Assertions.assertEquals("Michael", c.getFirstName());
        Assertions.assertEquals("Scott", c.getLastName());
        Assertions.assertEquals("mkscott@dunder.com", c.getEmail());
        Assertions.assertEquals("password", c.getPassword());
        Assertions.assertEquals("5704289173",c.getPhoneNumber());
    }
}
