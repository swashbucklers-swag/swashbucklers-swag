package com.sk8.swashbucklers.util.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hashable functional interface for hashing passwords
 * @author Daniel Bernier
 */
public interface Hashable {

    /**
            * Takes the users email and password and returns a hash
     * <p>
     *     Implementations of hashPasswordWithEmail should combine email with
     *     password and should include salt of some kind before hashing
     * </p>
            * @param email The user's email
            * @param password The user's password
            * @return The hash of email with password
     * @throws NoSuchAlgorithmException Thrown by {@link MessageDigest#getInstance(String)}
     */
    public String hashPasswordWithEmail(String email, String password) throws NoSuchAlgorithmException;
}