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
package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.util.ResourceLoader;
import java.awt.BorderLayout;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Zyun
 */
public class PlanetInfoSheet extends JPanel {

    JTabbedPane tpane;
    PlanetOverview overview;
    PlanetPopulation population;
    SpacePortMenuSheet spacePort;
    AtmosphereInfo atmosphere;
    PlanetIndustry industry;
    LocalLifeMenu localLifeMenu;
    PlanetMap planetMap;
    PlanetEconomy planetEconomy;
    PlanetGeology planetGeology;
    PlanetResources planetResources;

    private Civilization c;
    private Planet p;

    private final int spacePortIndex = 4;

    public PlanetInfoSheet(Universe u, Planet p, Civilization c) {
        this.c = c;
        this.p = p;
        setLayout(new BorderLayout());
        tpane = new JTabbedPane();

        overview = new PlanetOverview(u, p, c);
        atmosphere = new AtmosphereInfo(p, c);
        population = new PlanetPopulation(u, p, 0);
        spacePort = new SpacePortMenuSheet(p, c);
        planetGeology = new PlanetGeology(p);
        //building = new ConstructionMenu(u, p, c);
        industry = new PlanetIndustry(p, c);
        localLifeMenu = new LocalLifeMenu(p, c);
        planetMap = new PlanetMap(p, c, u, this);
        planetEconomy = new PlanetEconomy();
        planetResources = new PlanetResources(p);

        tpane.add("Overview", overview);
        tpane.add("Map", planetMap);
        tpane.add("Geology", planetGeology);
        tpane.add("Cities", population);
        tpane.add("Space Port", spacePort);
        tpane.add("Atmosphere", atmosphere);
        //Add satellite tabs
        tpane.add("Industry", industry);
        tpane.add("Local Life", localLifeMenu);
        tpane.add("Resources", planetResources);

        ImageIcon overview = ResourceLoader.getIcon("overview.icon");
        ImageIcon map = ResourceLoader.getIcon("globe.icon");
        ImageIcon geo = ResourceLoader.getIcon("rock.icon");
        ImageIcon indus = ResourceLoader.getIcon("factory.icon");
        ImageIcon city = ResourceLoader.getIcon("city.icon");
        ImageIcon life = ResourceLoader.getIcon("life.icon");
        ImageIcon spaceport = ResourceLoader.getIcon("spaceport.icon");
        ImageIcon atmosphereIco = ResourceLoader.getIcon("atmosphere.icon");
        ImageIcon goods = ResourceLoader.getIcon("goods.icon");
        
        tpane.setIconAt(0, overview);
        tpane.setIconAt(1, map);
        tpane.setIconAt(2, geo);
        tpane.setIconAt(3, city);
        tpane.setIconAt(4, spaceport);
        tpane.setIconAt(5, atmosphereIco);
        tpane.setIconAt(6, indus);
        tpane.setIconAt(7, life);
        tpane.setIconAt(8, goods);

        tpane.setEnabledAt(spacePortIndex, false);
        //Check if planet contains space port
        for (Map.Entry<GeographicPoint, Building> entry : p.buildings.entrySet()) {
            GeographicPoint key = entry.getKey();
            Building value = entry.getValue();

            //Do stuff...
            if (value instanceof SpacePort) {
                tpane.setEnabledAt(spacePortIndex, true);
                break;
            }
        }
        if (c.values.get("haslaunch") != 1) {
            tpane.setEnabledAt(spacePortIndex, false);
        }

        add(tpane, BorderLayout.CENTER);
    }

    public void init() {

    }

    public void update() {
        //spacePort.update();
        industry.update();
        population.update();

        tpane.setEnabledAt(spacePortIndex, false);
        //Check if planet contains space port
        for (Map.Entry<GeographicPoint, Building> entry : p.buildings.entrySet()) {
            GeographicPoint key = entry.getKey();
            Building value = entry.getValue();

            //Enable space port tab
            if (value instanceof SpacePort) {
                tpane.setEnabledAt(spacePortIndex, true);
                break;
            }
        }
        if (c.values.get("haslaunch") != 1) {
            tpane.setEnabledAt(spacePortIndex, false);
        }
    }
}
