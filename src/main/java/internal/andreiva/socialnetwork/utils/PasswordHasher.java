package internal.andreiva.socialnetwork.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher
{
    public static String hashPassword(String password, String username)
    {
        byte[] salt = username.getBytes();
        byte[] newSalt = new byte[16];
        System.arraycopy(salt, 0, newSalt, 0, Math.min(salt.length, 16));
        salt = newSalt;

        String hashedPassword = "";
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes)
            {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16)
                        .substring(1));
            }
            hashedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return hashedPassword;
    }
}
