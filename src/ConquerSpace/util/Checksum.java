package ConquerSpace.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author zyunl
 */
public class Checksum {

    public static String hash(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        DigestInputStream digestInputStream
                = new DigestInputStream(new FileInputStream(file), messageDigest);
        while (digestInputStream.read() >= 0) ;
        byte[] byteArray = messageDigest.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteArray.length; ++i) {
            sb.append(Integer.toHexString((byteArray[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public static String hashFolder(File file) throws NoSuchAlgorithmException, IOException {
        String text = "";
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                text += hashFolder(f);
            }
        } else {
            text += hash(file);
            return text;
        }
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] byteArray = messageDigest.digest(text.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteArray.length; ++i) {
            sb.append(Integer.toHexString((byteArray[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }
}
