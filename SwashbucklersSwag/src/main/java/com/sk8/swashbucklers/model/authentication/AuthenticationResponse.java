package com.sk8.swashbucklers.model.authentication;

import com.sk8.swashbucklers.dto.EmployeeDTO;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

    private final String jwt;
    private final EmployeeDTO employee;

    public AuthenticationResponse(String jwt, EmployeeDTO employee) {
        this.jwt = jwt;
        this.employee = employee;
    }

    public String getJwt() {
        return jwt;
    }

    public EmployeeDTO getEmployee(){
        return employee;
    }
}
