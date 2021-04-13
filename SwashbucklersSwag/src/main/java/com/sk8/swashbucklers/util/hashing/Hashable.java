package com.sk8.swashbucklers.util;

import java.security.NoSuchAlgorithmException;

/**
 * Hashable functional interface for hashing passwords
 * @author Daniel Bernier
 */
public interface Hashable {
    public String hashPasswordWithEmail(String email, String password) throws NoSuchAlgorithmException;
}
