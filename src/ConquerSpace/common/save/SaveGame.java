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
import ConquerSpace.common.game.resources.Good;
import ConquerSpace.common.util.Utilities;
import ConquerSpace.common.util.Version;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.json.JSONArray;
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

    private GameState gameState;

    public SaveGame(File file) {
        this.folder = file;
    }

    public SaveGame(String s) {
        this(new File(s));
    }

    public void save(GameState gameState) throws IOException, IllegalArgumentException, IllegalAccessException {
        this.gameState = gameState;
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

        //bytes = saveData.toString().getBytes();
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

        //loop through the object
        //readObject(json, gameState);
        //Get goods
    }

    private void saveObject(JSONObject saveObject, Object obj) throws IllegalArgumentException, IllegalAccessException {
        //Check if primitive
        if (obj != null) {
            //Add class things
            if (obj.getClass().isAnnotationPresent(SerializeClassName.class)) {
                SerializeClassName serializeName = obj.getClass().getAnnotation(SerializeClassName.class);
                saveObject.put("class", serializeName.value());
            }

            List<Field> fields = getAllFields(new ArrayList<>(), obj.getClass());

            //Loop through stuff
            for (Field field : fields) {
                field.setAccessible(true);
                if (!Modifier.isTransient(field.getModifiers())) {//isAnnotationPresent(Serialize.class)) {
                    //Serialize s = field.getAnnotation(Serialize.class);
                    String key = UUID.randomUUID().toString();

                    Object object = field.get(obj);
                    boolean isGoodIdentifier = false;
                    if (isSaveable(field.get(obj))) {
//                        //if (s.special() == SaveStuff.Good) {
//                            isGoodIdentifier = true;
//                            if (object instanceof Integer) {
//                                //Has to be an int...
//                                Integer val = (Integer) object;
//                                String stringIdentifier = gameState.goodIdentifiers.getKey(val);
//                                object = stringIdentifier;
//                            }
//                        }
                        save(saveObject, key, object, isGoodIdentifier);
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
    }

    @SuppressWarnings("unchecked")
    private void save(JSONObject saveObject, String key, Object obj, boolean isGood) throws IllegalArgumentException, IllegalAccessException {
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
                    String keyText = k.toString();
                    if (k instanceof CustomSerializer) {
                        keyText = ((CustomSerializer) k).getString();
                    }
                    if (isGood && k instanceof Integer) {
                        keyText = gameState.goodIdentifiers.getKey((Integer) k);
                    }
                    if (isSaveable(v)) {
                        mapData.put(keyText, v);
                    } else {
                        try {
                            JSONObject objectInArray = new JSONObject();
                            saveObject(objectInArray, v);
                            mapData.put(keyText, objectInArray);
                        } catch (IllegalArgumentException ex) {
                            Logger.getLogger(SaveGame.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IllegalAccessException ex) {
                            Logger.getLogger(SaveGame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });

            //mapSave.put("map", mapData);
            saveObject.put(key, mapData);
        } else {
            saveObject.put(key, obj);
        }
    }

    @SuppressWarnings("unchecked")
    private void readObject(JSONObject data, Object obj) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException {
        if (obj != null) {
            List<Field> fields = getAllFields(new ArrayList<>(), obj.getClass());
            //Loop through stuff
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Serialize.class)) {
                    Serialize s = field.getAnnotation(Serialize.class);
                    String key = s.value();

                    if (data.has(key)) {
                        if (isSaveable(field.getType())) {
                            Class<?> ct = field.getType();
                            //Save the thing
                            field.setAccessible(true);
                            if (ct.isAssignableFrom(Number.class)) {
                                Number val = (Number) data.getInt(key);
                                field.set(obj, val);
                            } else if (List.class.isAssignableFrom(ct)) {
                                JSONArray arr = data.getJSONArray(key);
                                List list = (List) field.getType().newInstance();
                                //Loop through
                                //Most arrays are number references
                                for (int i = 0; i < arr.length(); i++) {
                                    Object arrayValue = arr.get(i);
                                    list.add(arrayValue);
                                }
                                field.set(obj, list);
                            } else if (Map.class.isAssignableFrom(ct)) {
                                JSONObject dataObject = data.getJSONObject(key);
                                Map map = (Map) field.getType().newInstance();

                                //Loop through
                                //Most arrays are number references
                                Iterator it = dataObject.keys();
                                while (it.hasNext()) {
                                    String text = (String) it.next();
                                    JSONObject object = dataObject.getJSONObject(text);
                                    //Parse object
                                    //readObject(object, )
                                    //Have to read the class...
                                    if (key.equals("objects")) {
                                        int value = Integer.parseInt(text);
                                        //Parse the object...
                                        String className = object.getString("class");
                                        Class ckClass = getClassFromString(className);
                                        System.out.println(ckClass.newInstance());
                                        map.put(value, dataObject.get(text));
                                    } else {
                                        map.put(text, dataObject.get(text));
                                    }
                                }
                                System.out.println(map.size());
                                FileWriter fw;
                                try {
                                    fw = new FileWriter("texting.txt");
                                    fw.write(map.toString());
                                } catch (IOException ex) {
                                    Logger.getLogger(SaveGame.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } else {
                            //Do something else
                        }
                    }
                }
            }
        }
    }

    private Class getClassFromString(String s) {
        for (int i = 0; i < ClassList.classes.length; i++) {
            Class<?> c = ClassList.classes[i];
            if (c.isAnnotationPresent(SerializeClassName.class)) {
                if (c.getAnnotation(SerializeClassName.class).value().equals(s)) {
                    return c;
                }
            }
        }
        return null;
    }

    private void loadObject(JSONObject input, Object output, Field field) {
        Type generic = field.getGenericType();

        if (generic instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) generic;
        } else {
        }
    }

    private boolean isSaveable(String object) throws ClassNotFoundException {
        //Check if the thing...
        for (int i = 0; i < primitives.length; i++) {
            Class<?> c = primitives[i];
            if (Class.forName(object).isAssignableFrom(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSaveable(Class<?> object) {
        for (int i = 0; i < primitives.length; i++) {
            //System.out.println("passing " + primitives[i].toString());
            //System.out.println(object.getClass().isInstance(java.lang.Long.class));
            Class<?> c = primitives[i];
            if (c.isAssignableFrom(object)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSaveable(Object object) {
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
