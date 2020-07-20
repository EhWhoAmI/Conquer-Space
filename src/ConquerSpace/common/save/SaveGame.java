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
package ConquerSpace.common.save;

import ConquerSpace.ConquerSpace;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.organizations.civilization.controllers.AIController;
import ConquerSpace.common.game.organizations.civilization.controllers.PlayerController;
import ConquerSpace.common.util.Version;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.json.JSONObject;

/**
 *
 * @author EhWhoAmI
 */
public class SaveGame {

    private Version earliestCompatableVersion = new Version(0, 0, 3);

    private File folder;

    private Class[] primitives = new Class[]{Integer.class, Boolean.class, Double.class, Float.class, Long.class, String.class, List.class, Map.class};

    private JSONObject saveData;

    public SaveGame(File file) {
        this.folder = file;
    }

    public SaveGame(String s) {
        this(new File(s));
    }

    public void save(GameState gameState) throws IOException, IllegalArgumentException, IllegalAccessException {
        //Get the file
        JSONObject meta = new JSONObject();
        meta.put("version", ConquerSpace.VERSION.getVersionCore());
        meta.put("date", gameState.date.getDate());

        saveData = new JSONObject();

        //Save version
        saveData.put("version", ConquerSpace.VERSION.getVersionCore());

        //Zip
        String zipFileName = folder.getPath();//folder.getName() + "/save.zip";
        FileOutputStream fos = new FileOutputStream(zipFileName);
        ZipOutputStream zos = new ZipOutputStream(fos);

        //Meta
        zos.putNextEntry(new ZipEntry("meta"));

        byte[] bytes = meta.toString().getBytes();
        zos.write(bytes, 0, bytes.length);
        zos.closeEntry();

        zos.putNextEntry(new ZipEntry("data"));

        ObjectOutputStream out = new ObjectOutputStream(zos);

        out.writeObject(gameState);

        zos.closeEntry();
        out.close();
        zos.close();
    }

    public void read(GameState gameState) throws FileNotFoundException, IOException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException {
        String zipFileName = folder.getPath();//
        ZipFile zipFile = new ZipFile(zipFileName);

        StringBuilder builder = new StringBuilder();
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.getName().equals("data")) {
                InputStream stream = zipFile.getInputStream(entry);
                ObjectInputStream ois = new ObjectInputStream(stream);
                Object getted = ois.readObject();
                gameState.convert((GameState) getted);
            }
        }
        zipFile.close();

        //Other initialization actions
        Integer playerCiv = gameState.playerCiv;
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            Integer civId = gameState.getCivilization(i);
            Civilization civ = gameState.getObject(civId, Civilization.class);
            if (civId.equals(playerCiv)) {
                //Put in playerCiv
                civ.controller = new PlayerController();
            } else {
                civ.controller = new AIController();
            }
        }
    }

    public static String getSaveFolder() {
        String baseSave = System.getProperty("user.dir") + "/save";
        //Get the other files
        File baseSaveFile = new File(baseSave);
        if (baseSaveFile.exists() && baseSaveFile.isFile()) {
            //Delete the file
            baseSaveFile.delete();
            return (baseSave + "/save0");
        } else if (!baseSaveFile.exists() || baseSaveFile.listFiles().length <= 0) {
            return (baseSave + "/save0");
        }

        String[] fileList = baseSaveFile.list();
        int highest = 0;
        for (String folder : fileList) {
            //Get the name
            if (folder.startsWith("save")) {
                //Get the number
                String number = folder.substring(4);
                try {
                    int saveCount = Integer.parseInt(number);
                    if (saveCount > highest) {
                        highest = saveCount;
                    }
                } catch (NumberFormatException nfe) {

                }
            }
        }
        highest++;
        return baseSave + "/save" + highest;
    }

    public static String defaultSerialize(Object obj) {
        return obj.toString();
    }
}
