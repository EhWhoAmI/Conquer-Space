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
import ConquerSpace.common.util.Utilities;
import ConquerSpace.common.util.Version;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author EhWhoAmI
 */
public class SaveGame {

    private Version earliestCompatableVersion = new Version(0, 0, 3);

    private File folder;

    private Class[] primitives = new Class[]{Integer.class, Double.class, Float.class, Long.class, String.class, List.class, Map.class};

    private JSONObject saveData;

    public SaveGame(File file) {
        this.folder = file;

        saveData = new JSONObject();
    }

    public SaveGame(String s) {
        this(new File(s));
    }

    public void save(GameState gameState) throws IOException, IllegalArgumentException, IllegalAccessException {
        //Get the file
        if (!folder.exists()) {
            folder.mkdirs();
        }

        //Check for files, and delete
        if (folder.listFiles().length > 0) {
            Utilities.deleteFolder(folder);
        }

        //Save metadata...
        JSONObject meta = new JSONObject();
        meta.put("version", ConquerSpace.VERSION.getVersionCore());
        meta.put("date", gameState.date.getDate());
        
        saveObject(saveData, gameState);
        String text = saveData.toString(4);//JsonValue.readHjson().toString(Stringify.HJSON);
        System.out.println(text.length());
        FileWriter writer = new FileWriter("save.json");
        writer.write(text);
        writer.flush();
    }

    private void saveObject(JSONObject saveObject, Object obj) throws IllegalArgumentException, IllegalAccessException {
        //Check if primitive
        List<Field> fields = getAllFields(new ArrayList<>(), obj.getClass());
        //Loop through stuff
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Serialize.class)) {
                Serialize s = field.getAnnotation(Serialize.class);
                String key = s.key();
                if (isSaveable(field.get(obj))) {
                    save(saveObject, key, field.get(obj));
                } else {
                    //Serialize children
                    JSONObject childObject = new JSONObject();
                    saveObject(childObject, field.get(obj));

                    //Put the stuff
                    saveObject.put(key, childObject);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void save(JSONObject saveObject, String key, Object obj) throws IllegalArgumentException, IllegalAccessException {
        if (obj instanceof List) {
            //Go through list
            List list = (List) obj;
            JSONArray array = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                if (isSaveable(list.get(i))) {
                    array.put(list.get(i));
                } else {
                    JSONObject objectInArray = new JSONObject();
                    saveObject(objectInArray, list.get(i));
                    array.put(objectInArray);
                }
            }
            saveObject.put(key, array);
        } else if (obj instanceof Map) {
            //
            JSONObject mapSave = new JSONObject();
            JSONObject mapData = new JSONObject();
            //Go through map 
            Map map = (Map) obj;
            JSONArray ar = new JSONArray();

            map.forEach(new BiConsumer() {
                @Override
                public void accept(Object k, Object v) {
                    if (isSaveable(v)) {
                        mapData.put(k.toString(), v);
                    } else {
                        try {
                            JSONObject objectInArray = new JSONObject();
                            saveObject(objectInArray, v);
                            mapData.put(k.toString(), objectInArray);
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(SaveGame.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(SaveGame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            
            mapSave.put("map", mapData);
            saveObject.put(key, mapSave);
        } else {
            saveObject.put(key, obj);
        }
    }

    private boolean isSaveable(Object object) {
        if (object instanceof Long) {
            System.out.println(object.getClass().isInstance(Long.class));
        }
        for (int i = 0; i < primitives.length; i++) {
            //System.out.println("passing " + primitives[i].toString());
            //System.out.println(object.getClass().isInstance(java.lang.Long.class));
            Class<?> c = primitives[i];
            if (c.isInstance(object)) {
                return true;
            }
        }
        return false;
    }

    private List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }
        return fields;
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
                if (saveCount > highest) {
                    highest = saveCount;
                }
            }
        }
        highest++;
        return baseSave + "/save" + highest;
    }
}