package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.repo.employee.EmployeeRepository;
import com.sk8.swashbucklers.util.MyUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User details handler
*/

@Service
public class SwagUserDetailsService implements UserDetailsService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public MyUserPrincipal loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Employee> employee = employeeRepository.findByEmail(email);

        if(employee.isPresent()){
            return new MyUserPrincipal(employee.get());
        } else {
            throw new UsernameNotFoundException(email);
        }
    }
}
