package service;

import org.mindrot.jbcrypt.BCrypt;

public class Encryptor {
    public static String encrypt(String password) {
        return BCrypt.hashpw(
                password,
                BCrypt.gensalt());
    }
    public static boolean verify(
            String password,
            String encryptedPassword) {
        return BCrypt.checkpw(
                password,
                encryptedPassword);
    }
}