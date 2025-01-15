package internal.andreiva.socialnetwork.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher
{
    public static String hashPassword(String password, String username)
    {
        byte[] salt = new byte[16];
        System.arraycopy(username.getBytes(), 0, salt, 0, Math.min(username.getBytes().length, 16));

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
