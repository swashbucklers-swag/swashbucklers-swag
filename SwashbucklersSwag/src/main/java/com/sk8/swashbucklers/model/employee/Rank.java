package com.sk8.swashbucklers.model.employee;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Represents an employees rank, and thus their level of permissions
 *
 * @author Edson Rodriguez
 */
public enum Rank{
    CREW,
    CAPTAIN,
    LANDLUBBER;
    public static SimpleGrantedAuthority toAuth(Rank r) {
        return new SimpleGrantedAuthority(r.toString());
    }
}
