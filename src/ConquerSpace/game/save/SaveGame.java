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
package ConquerSpace.game.save;

import ConquerSpace.game.StarDate;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.hull.Hull;
import ConquerSpace.game.universe.ships.hull.HullMaterial;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.Utilities;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author zyunl
 */
public class SaveGame {

    private File folder;

    public SaveGame(File file) {
        this.folder = file;
    }

    public SaveGame(String s) {
        folder = new File(s);
    }

    public void save(Universe u, StarDate date) throws IOException {
        //Get the file
        if (!folder.exists()) {
            folder.mkdirs();
        }

        //Check for files, and delete
        if (folder.listFiles().length > 0) {
            Utilities.deleteFolder(folder);
        }

        //Now create items
        //Create info file 
        MetaFactory metaFactory = new MetaFactory(u, date);
        metaFactory.create(folder);

        {
            File systemFile = new File(folder, "systems");
            //Add content
            systemFile.createNewFile();

            JSONArray systemArray = new JSONArray();
            //Do the universe
            for (int sys = 0; sys < u.getStarSystemCount(); sys++) {
                StarSystem system = u.getStarSystem(sys);

                StarSystemSaveHandler starSystemSaveHandler = new StarSystemSaveHandler(system);
                JSONObject obj = starSystemSaveHandler.getJSONObject();
                systemArray.put(obj);
            }
            PrintWriter pw = new PrintWriter(systemFile);
            pw.print(systemArray.toString(2).replace("\n", System.getProperty("line.separator")));
            pw.close();
        }

        {
            File civFile = new File(folder, "civ");
            civFile.createNewFile();
            JSONArray civArray = new JSONArray();

            //Civs
            for (int civ = 0; civ < u.getCivilizationCount(); civ++) {
                Civilization civilization = u.getCivilization(civ);
                JSONObject object = new JSONObject();

                //There is a lot to add here.... halp
                object.put("id", civilization.getID());
                object.put("name", civilization.getName());

                JSONArray materialArray = new JSONArray();
                for (HullMaterial mat : civilization.hullMaterials) {
                    JSONObject materialObject = new JSONObject();
                    materialObject.put("id", mat.getId());
                    materialObject.put("cost", mat.getCost());
                    materialObject.put("density", mat.getDensity());
                    materialObject.put("name", mat.getName());
                    materialObject.put("strength", mat.getStrength());
                    materialArray.put(materialObject);
                }
                object.put("hull-materials", materialArray);

                JSONArray hullArray = new JSONArray();
                for (Hull h : civilization.hulls) {
                    JSONObject hullObject = new JSONObject();
                    hullObject.put("mass", h.getMass());
                    hullObject.put("material", h.getMaterial().getId());
                    hullObject.put("space", h.getSpace());
                    hullObject.put("type", h.getShipType());
                    hullObject.put("thrust", h.getThrust());
                    hullObject.put("strength", h.getStrength());
                    hullArray.put(hullObject);
                }
                object.put("hulls", hullArray);

                civArray.put(object);
            }
            PrintWriter pw = new PrintWriter(civFile);
            pw.print(civArray.toString(2).replace("\n", System.getProperty("line.separator")));
            pw.close();
        }

        {
            //Politics and relations
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
                int saveCount = Integer.parseInt(number);
                if(saveCount > highest) {
                    highest = saveCount;
                }
            }
        }
        highest++;
        return baseSave + "/save" + highest;
    }
}
