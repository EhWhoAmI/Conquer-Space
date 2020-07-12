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

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.game.GameState;
import ConquerSpace.game.city.City;
import ConquerSpace.game.city.area.Area;
import ConquerSpace.game.city.area.SpacePortArea;
import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.gui.renderers.TerrainRenderer;
import ConquerSpace.util.ResourceLoader;
import java.awt.BorderLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetInfoSheet extends JPanel {

    JTabbedPane tpane;
    PlanetOverview overview;
    PlanetCities population;
    SpacePortMenuSheet spacePort;
    AtmosphereInfo atmosphere;
    PlanetIndustry industry;
    LocalLifeMenu localLifeMenu;
    PlanetMap planetMap;
    PlanetEconomy planetEconomy;
    PlanetGeology planetGeology;
    PlanetResources planetResources;

    private Image planetImage;

    private Civilization civilization;
    private Planet planet;

    private final int spacePortIndex = 4;

    public PlanetInfoSheet(GameState gameState, Planet p, Civilization c) {
        this.civilization = c;
        this.planet = p;

        TerrainRenderer renderer = new TerrainRenderer(p);
        planetImage = renderer.getImage();
        //planetImage = (planetImage.getScaledInstance((int) (p.getPlanetWidth() * 2d), (int) (p.getPlanetHeight() * 2d), Image.SCALE_DEFAULT));
        setLayout(new BorderLayout());
        tpane = new JTabbedPane();

        overview = new PlanetOverview(gameState, p, c, planetImage);
        atmosphere = new AtmosphereInfo(p, c);
        population = new PlanetCities(gameState, p, c, this);
        spacePort = new SpacePortMenuSheet(p, c);
        planetGeology = new PlanetGeology(gameState, p);
        //building = new ConstructionMenu(u, p, c);
        industry = new PlanetIndustry(gameState, p, c);
        localLifeMenu = new LocalLifeMenu(p, c);
        planetMap = new PlanetMap(p, c, gameState.universe, this, planetImage);
        planetEconomy = new PlanetEconomy();
        planetResources = new PlanetResources(gameState, p, c, this);

        tpane.add(LOCALE_MESSAGES.getMessage("game.planet.tab.overview"), overview);
        tpane.add(LOCALE_MESSAGES.getMessage("game.planet.tab.map"), planetMap);
        tpane.add(LOCALE_MESSAGES.getMessage("game.planet.tab.geology"), planetGeology);
        tpane.add(LOCALE_MESSAGES.getMessage("game.planet.tab.cities"), population);
        tpane.add(LOCALE_MESSAGES.getMessage("game.planet.tab.space"), spacePort);
        tpane.add(LOCALE_MESSAGES.getMessage("game.planet.tab.atmosphere"), atmosphere);
        tpane.add(LOCALE_MESSAGES.getMessage("game.planet.tab.industry"), industry);
        tpane.add(LOCALE_MESSAGES.getMessage("game.planet.tab.locallife"), localLifeMenu);
        tpane.add(LOCALE_MESSAGES.getMessage("game.planet.tab.resources"), planetResources);

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

        checkSpacePortTab();

        add(tpane, BorderLayout.CENTER);
    }

    public void init() {

    }

    public void update() {
        //spacePort.update();
        industry.update();
        population.update();

        checkSpacePortTab();
    }

    private void checkSpacePortTab() {
        tpane.setEnabledAt(spacePortIndex, false);

        //Check if planet contains space port
        cityloop:
        for (City c : planet.cities) {
            for (Area a : c.areas) {
                if (a instanceof SpacePortArea) {
                    tpane.setEnabledAt(spacePortIndex, true);
                    break cityloop;
                }
            }
        }
        //Check if civ has launch capability
        if (civilization.values.get("haslaunch") != 1) {

            //Disable
            tpane.setEnabledAt(spacePortIndex, false);
        }
    }

    void setSelectedTab(int tabIntex) {
        tpane.setSelectedIndex(tabIntex);
    }
}
