package com.sk8.swashbucklers.util.hashing;

import org.springframework.stereotype.Component;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hashing utility for hashing passwords
 * @author Daniel Bernier
 */
@Component
public class PasswordHashingUtil implements Hashable {

    /**
     * Takes the users email and password and returns a hash
     * @param email The user's email
     * @param password The user's password
     * @return The hash of email with password and some salt
     * @throws NoSuchAlgorithmException Thrown by {@link MessageDigest#getInstance(String)}
     */
    @Override
    public String hashPasswordWithEmail(String email, String password) throws NoSuchAlgorithmException {
        String full = email + password + "salt";

        MessageDigest m = MessageDigest.getInstance("md5");
        byte[] messageDigest = m.digest(full.getBytes());
        BigInteger n = new BigInteger(1, messageDigest);

        //Convert the whole array into a hexadecimal string.
        String hash = n.toString(16);
        while (hash.length() < 32) {
            hash = "0" + hash;
        }

        return hash;
    }
}
