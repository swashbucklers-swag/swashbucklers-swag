package com.sk8.swashbucklers.controller;

import com.sk8.swashbucklers.dto.EmployeeDTO;
import com.sk8.swashbucklers.model.authentication.AuthenticationRequest;
import com.sk8.swashbucklers.model.authentication.AuthenticationResponse;
import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.service.SwagUserDetailsService;
import com.sk8.swashbucklers.util.JwtUtil;
import com.sk8.swashbucklers.util.MyUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SwagUserDetailsService userDetailsService;

    public LoginController(AuthenticationManager authenticationManager,
                   JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password", e);
        }


        final MyUserPrincipal userDetails = userDetailsService
                .loadUserByUsername(request.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails);
        final EmployeeDTO emp = EmployeeDTO.employeeToDTO().apply(userDetails.getEmployee());

        return ResponseEntity.ok(new AuthenticationResponse(jwt,emp));
    }

}
