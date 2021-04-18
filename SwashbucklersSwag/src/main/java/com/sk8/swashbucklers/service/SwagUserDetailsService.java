package com.sk8.swashbucklers.service;

import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Rank;
import com.sk8.swashbucklers.repo.employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * User details handler
*/

@Service
public class SwagUserDetailsService implements UserDetailsService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Employee> e = employeeRepository.findByEmail(s);
        if (e.isPresent()) {
            SimpleGrantedAuthority auth = Rank.toAuth(e.get().getRank());
            ArrayList<SimpleGrantedAuthority> arrayList = new ArrayList<>();
            arrayList.add(auth);
            return new User(e.get().getEmail(),e.get().getPassword(), arrayList);
        } else {
            throw new UsernameNotFoundException("Email not found");
        }
    }
}
