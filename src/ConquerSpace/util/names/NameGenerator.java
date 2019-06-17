package ConquerSpace.util.names;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Creates a name generator from a json file
 *
 * @author zyunl
 */
public class NameGenerator {

    private File source;
    private JSONObject nameGeneratorObject;
    private String[] rules;
    private HashMap<String, Integer> counters;

    public NameGenerator(File source) throws FileNotFoundException, IOException {
        this.source = source;
        //Process
        FileInputStream fis = new FileInputStream(source);
        byte[] data = new byte[(int) source.length()];
        fis.read(data);
        fis.close();
        String text = new String(data);

        nameGeneratorObject = new JSONObject(text);
        //Get the info
        Object[] obj = nameGeneratorObject.getJSONArray("rules").toList().toArray();
        rules = new String[obj.length];
        int i = 0;
        for (Object o : obj) {
            rules[i] = (String) o;
            i++;
        }

        counters = new HashMap<>();
        Object[] counterObjects = nameGeneratorObject.getJSONArray("counters").toList().toArray();
        for (Object o : counterObjects) {
            //Remove the things
            String toAdd = (String) o;
            counters.put(toAdd, 1);
        }
    }

    public NameGenerator(String source) throws IOException {
        this(new File(source));
        //Process
    }

    public String getName(int scheme) {
        String[] rule = rules[scheme].split("\\\\");
        //Get the data
        StringBuilder b = new StringBuilder();
        Random rand = new Random();
        for (String side : rule) {
            if (side.startsWith("[")) {
                //Counters
                String counterID = side.replace("[", "").replace("]", "");
                String[] arr = counterID.split("/");
                int num = (arr.length / 2);
                String letter = arr[num];
                int i = counters.get(letter);
                String appending = "" + i;

                if (counterID.endsWith("/o")) {
                    appending = ordinal(i);
                } else if (counterID.endsWith("/t")) {
                    appending = EnglishNumberToWord.convert(i);
                    int start = appending.lastIndexOf(" ");
                    if (start > -1) {
                        String s = appending.substring(0, start);
                        s += "-";
                        s += appending.substring(start + 1);
                        appending = s;
                    }
                } else if (counterID.endsWith("/r")) {
                    //Roman
                    appending = toRoman(i);
                }

                //Caps
                if (counterID.startsWith("c/")) {
                    appending = appending.substring(0, 1).toUpperCase() + appending.substring(1);
                }
                b.append(appending);
                counters.put(letter, ++i);
            } else {
                JSONArray array = nameGeneratorObject.getJSONArray(side);
                int i = rand.nextInt(array.length());
                b.append(array.get(i));
            }
        }
        return b.toString();
    }

    public static String ordinal(int i) {
        String[] sufixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];
        }
    }

    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    public final static String toRoman(int number) {
        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number - l);
    }
    
    public static NameGenerator getNameGenerator(String name) throws FileNotFoundException, IOException {
        //Get from list
        Properties prop = new Properties();
        prop.load(new FileInputStream("assets/data/names/names.properties"));
        String fn = "assets/data/names/" + (String)prop.get(name);
        return (new NameGenerator(fn));
    }
}
