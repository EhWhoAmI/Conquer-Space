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

import ConquerSpace.game.universe.Vector;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Creates a checksum
 * @author EhWhoAmI
 */
public class Checksum {

    public static String hash(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
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

    public static void hash(File file, MessageDigest digest) throws NoSuchAlgorithmException, IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        digest.update(fileContent);
    }

    public static String hashFolder(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                hashFolder(f, messageDigest);
            }
        } else {
            hash(file, messageDigest);
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteArray.length; ++i) {
            sb.append(Integer.toHexString((byteArray[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public static void hashFolder(File file, MessageDigest digest) throws NoSuchAlgorithmException, IOException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                hashFolder(f, digest);
            }
        } else {
            hash(file, digest);
        }
    }

    public static String hashToString(MessageDigest digest) {
        byte[] byteArray = digest.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteArray.length; ++i) {
            sb.append(Integer.toHexString((byteArray[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }
}
