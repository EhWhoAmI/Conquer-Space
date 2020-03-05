/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author EhWhoAmI
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
