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

import ConquerSpace.ConquerSpace;
import ConquerSpace.game.StarDate;
import ConquerSpace.game.universe.bodies.Universe;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;

/**
 * Creates the metadata of the game
 *
 * @author EhWhoAmI
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
