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
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.Civilization;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.json.JSONObject;

/**
 *
 * @author EhWhoAmI
 */
public class SaveGame {
    private File saveFile;

    private JSONObject saveData;

    public SaveGame(File file) {
        this.saveFile = file;
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
        String zipFileName = saveFile.getPath();//folder.getName() + "/save.zip";
        
        //Create if it doesnt exist
        if(!saveFile.exists()) {
            saveFile.getParentFile().mkdirs();
            saveFile.createNewFile();
        }
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
        String zipFileName = saveFile.getPath();//
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
        ObjectReference playerCiv = gameState.playerCiv;
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            ObjectReference civId = gameState.getCivilization(i);
            Civilization civ = gameState.getObject(civId, Civilization.class);
        }
    }

    public static String getSaveFolder() {
        String baseSave = ConquerSpace.USER_DIR + "/save/";

        String saveText = String.format("%07d_save.sav", new Random().nextInt(10000000));
        return baseSave + saveText;
    }

    public static String defaultSerialize(Object obj) {
        return obj.toString();
    }
}
