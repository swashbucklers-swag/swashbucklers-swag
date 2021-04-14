package com.sk8.swashbucklers.dto;


import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Rank;
import com.sk8.swashbucklers.model.location.Location;
import com.sk8.swashbucklers.util.hashing.PasswordHashingUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private int employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Location location;
    private Rank rank;


    public static Function<Employee, EmployeeDTO> employeeToDTO(){
        PasswordHashingUtil phu = new PasswordHashingUtil();
        return (employee) -> {
            try {
                return new EmployeeDTO(employee.getEmployeeId(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getEmail(),
                        phu.hashPasswordWithEmail(employee.getEmail(), employee.getPassword()),
                        employee.getPhoneNumber(),
                        employee.getLocation(),employee.getRank());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    public static Function<EmployeeDTO, Employee> DTOToEmployee(){
        return (employeeDTO) -> new Employee(employeeDTO.getEmployeeId(),
                 employeeDTO.getFirstName(),
                employeeDTO.getLastName(),
                employeeDTO.getEmail(),
                employeeDTO.getPassword(),
                employeeDTO.getPhoneNumber(),
                employeeDTO.getLocation(),
                employeeDTO.getRank());
    }

}
