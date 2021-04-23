package com.sk8.swashbucklers.util;

import com.sk8.swashbucklers.model.employee.Employee;
import com.sk8.swashbucklers.model.employee.Rank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;

public class MyUserPrincipal implements UserDetails {

    private Employee employee;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        SimpleGrantedAuthority auth = Rank.toAuth(employee.getRank());
        ArrayList<SimpleGrantedAuthority> arrayList = new ArrayList<>();
        arrayList.add(auth);

        return arrayList;
    }

    public Employee getEmployee(){
        return employee;
    }

    public MyUserPrincipal(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
