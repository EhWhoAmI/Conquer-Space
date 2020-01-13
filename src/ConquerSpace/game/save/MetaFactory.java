package ConquerSpace.game.save;

import ConquerSpace.ConquerSpace;
import ConquerSpace.game.StarDate;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;

/**
 * Creates the metadata of the game
 *
 * @author zyunl
 */
public class MetaFactory {

    private Universe u;
    private StarDate date;

    public MetaFactory(Universe u, StarDate date) {
        this.u = u;
        this.date = date;
    }

    public void create(File folder) throws FileNotFoundException, IOException {
        File metaFile = new File(folder, "meta");
        //Add content
        metaFile.createNewFile();
        //JSON
        JSONObject root = new JSONObject();

        root.put("date", date.bigint);
        root.put("seed", u.getSeed());
        root.put("size", u.getStarSystemCount());
        root.put("civs", u.getCivilizationCount());
        root.put("version", (ConquerSpace.VERSION.getMajor() + "." + ConquerSpace.VERSION.getMinor() + "." + ConquerSpace.VERSION.getPatch()));

        PrintWriter pw = new PrintWriter(metaFile);
        pw.print(root.toString(2).replace("\n", System.getProperty("line.separator")));
        pw.close();
    }
}
