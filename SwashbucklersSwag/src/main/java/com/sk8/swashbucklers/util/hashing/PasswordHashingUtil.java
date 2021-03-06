package com.sk8.swashbucklers.util.hashing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hashing utility for hashing passwords
 * @author Daniel Bernier
 */
@Component
public class PasswordHashingUtil implements Hashable {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Takes the users email and password and returns a hash, this method is basically deprecated
     * @param email The user's email
     * @param password The user's password
     * @return The hash of email with password and some salt
     * @throws NoSuchAlgorithmException Thrown by {@link MessageDigest#getInstance(String)}
     */
    @Override
    public String hashPasswordWithEmail(String email, String password) throws NoSuchAlgorithmException {
        return passwordEncoder.encode(password);
    }

    public boolean comparePassword(String old, String newPass){
        return passwordEncoder.matches(newPass,old);
    }
}
