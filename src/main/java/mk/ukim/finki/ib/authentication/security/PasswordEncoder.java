package mk.ukim.finki.ib.authentication.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoder {
    public static String hashPassword(String plainPassword) {
        // Generate a salt and hash the password
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        // Compare the plain password with the hashed password
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
