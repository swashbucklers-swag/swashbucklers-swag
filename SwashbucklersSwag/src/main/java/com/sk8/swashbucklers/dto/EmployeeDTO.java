package com.sk8.swashbucklers.dto;


import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Rank;
import com.sk8.swashbucklers.model.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.function.Function;

/**
 * Represents the Data Transfer Object to encapsulate <br>how an Employee's data is handled during transfer to database
 * @author Nick Zimmerman
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private int employeeId;
    @Size(min = 1)
    private String firstName;
    @Size(min = 1)
    private String lastName;
    @Email
    private String email;
    private String password;
    @Size(min = 10, max = 10)
    private String phoneNumber;
    private Location location;
    private Rank rank;

    /**
     * Converts Employee to EmployeeDTO
     * @return EmployeeDTO
     */
    public static Function<Employee, EmployeeDTO> employeeToDTO(){
        return (employee) -> {
            Assert.notNull(employee,"Employee cannot be null");
            return new EmployeeDTO(employee.getEmployeeId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getEmail(), employee.getPassword(),
                    employee.getPhoneNumber(),
                    employee.getLocation(), employee.getRank());
        };
    }

    /**
     * Converts EmployeeDTO to Employee
     * @return Employee
     */
    public static Function<EmployeeDTO, Employee> DTOToEmployee(){
        return (employeeDTO) -> {
            Assert.notNull(employeeDTO,"employeeDTO cannot be null");
            return new Employee(employeeDTO.getEmployeeId(),
                employeeDTO.getFirstName(),
                employeeDTO.getLastName(),
                employeeDTO.getRank(),
                employeeDTO.getPassword(),
                employeeDTO.getPhoneNumber(),
                employeeDTO.getLocation(),
                employeeDTO.getEmail());
        };
    }

}
