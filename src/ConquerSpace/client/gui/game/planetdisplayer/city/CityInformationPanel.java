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
package ConquerSpace.client.gui.game.planetdisplayer.city;

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.client.gui.GraphicsUtil;
import ConquerSpace.client.gui.game.planetdisplayer.PlanetCities;
import ConquerSpace.client.gui.game.planetdisplayer.PlanetMapProvider;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.CityType;
import ConquerSpace.common.game.logistics.SupplyNode;
import ConquerSpace.common.game.logistics.SupplySegment;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.population.Population;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.universe.GeographicPoint;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.util.Utilities;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringJoiner;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author EhWhoAmI
 */
public class CityInformationPanel extends JPanel {

    private City selectedCity;
    private Civilization civilization;
    private GameState gameState;
    private PlanetMapProvider provider;
    private GridBagLayout layout;
    private Planet planet;

    private CitySkylinePanel citySkylinePanel;
    private CityOverviewPanel cityOverviewPanel;
    private MapMinimap mapMinimap;
    private JobInformationPanel jobInformationPanel;
    private CityIndustryPanel cityIndustryPanel;
    private CityEconomyPanel cityEconomyPanel;

    public CityInformationPanel(GameState gameState, PlanetCities parent, City selectedCity, Planet planet, Civilization owner, PlanetMapProvider provider) {
        this.selectedCity = selectedCity;
        this.gameState = gameState;
        this.provider = provider;
        this.planet = planet;
        this.civilization = owner;

        layout = new GridBagLayout();
        setLayout(layout);

        //Init components
        citySkylinePanel = new CitySkylinePanel();
        cityOverviewPanel = new CityOverviewPanel();
        mapMinimap = new MapMinimap();
        //Minimap needs a container because of how the drawing works
        JPanel minimapContainer = new JPanel(new BorderLayout());
        minimapContainer.add(mapMinimap, BorderLayout.CENTER);
        minimapContainer.setBorder(new TitledBorder(new LineBorder(Color.gray), "Position on Map"));

        jobInformationPanel = new JobInformationPanel(selectedCity, gameState);
        cityIndustryPanel = new CityIndustryPanel(selectedCity, civilization, planet, gameState);
        cityEconomyPanel = new CityEconomyPanel(parent, selectedCity, gameState);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0;
        constraints.weighty = 0;
        add(citySkylinePanel, constraints);

        constraints.gridy = 0;
        constraints.gridx = 1;
        add(minimapContainer, constraints);

        constraints.gridy = 1;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        add(cityOverviewPanel, constraints);

        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.gridy = 2;
        constraints.gridx = 2;
        constraints.gridwidth = 1;
        add(jobInformationPanel, constraints);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridy = 0;
        constraints.gridx = 2;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.gridheight = 2;
        add(cityIndustryPanel, constraints);

        constraints.gridy = 2;
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        add(cityEconomyPanel, constraints);
    }

    //Draws the borders of the borderlayout
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (false) {
            int[][] dims = layout.getLayoutDimensions();
            g.setColor(Color.BLUE);
            int x = 0;
            for (int add : dims[0]) {
                x += add;
                g.drawLine(x, 0, x, getHeight());
            }
            int y = 0;
            for (int add : dims[1]) {
                y += add;
                g.drawLine(0, y, getWidth(), y);
            }
        }
    }

    private class CitySkylinePanel extends JPanel {

        private Image bg = null;

        public CitySkylinePanel() {
            int height = 150;
            if (imageCount > 0) {
                int id = selectedCity.getReference().getId() % imageCount;

                double ratio = height / (double) citySkylineImageMap.get(id).getHeight(null);
                int width = (int) ((double) citySkylineImageMap.get(id).getWidth(null) * ratio);
                bg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D graphics = (Graphics2D) bg.getGraphics();
                graphics.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.drawImage(citySkylineImageMap.get(id).getScaledInstance(width, height, Image.SCALE_DEFAULT), 0, 0, null);
                GraphicsUtil.paintTextWithOutline(selectedCity.getName(), graphics, 30, 0, 150 / 2);

            }
            setPreferredSize(new Dimension(500, height));
        }

        @Override
        protected void paintComponent(Graphics g) {
            //Draw background
            if (bg != null) {
                g.drawImage(bg, 0, 0, null);
            } else {
                //Load image...
            }

        }
    }

    private class CityOverviewPanel extends JPanel {

        public CityOverviewPanel() {
            setLayout(new VerticalFlowLayout());
            //Check if capital city
            for (int i = 0; i < gameState.getCivilizationCount(); i++) {
                Civilization civilization = gameState.getCivilizationObject(i);
                if (civilization.getCapitalCity().equals(selectedCity.getReference())) {
                    JLabel isCapital = new JLabel(
                            LOCALE_MESSAGES.getMessage("game.planet.cities.capital", civilization.getName()));
                    add(isCapital);
                    break;
                }
            }

            //Population
            JLabel popCount = new JLabel(
                    LOCALE_MESSAGES.getMessage("game.planet.cities.population", 
                            Utilities.longToHumanString(gameState.getObject(selectedCity.population,
                                    Population.class).getPopulationSize())));
            add(popCount);

            JLabel priindustry = new JLabel(
                    LOCALE_MESSAGES.getMessage("game.planet.cities.table.priindustry", selectedCity.getCityType()));
            add(priindustry);

            JLabel mainProduction = new JLabel("");
            StringJoiner joiner = new StringJoiner(", ");

            int i = 0;
            for (StoreableReference ref : selectedCity.primaryProduction) {
                joiner.add(gameState.getGood(ref).toString());
                i++;
                if (i > 3) {
                    break;
                }
            }
            //Limit length of the goods
            mainProduction.setText("Produces Goods: " + joiner.toString());
            add(mainProduction);

            JLabel growthAmount = new JLabel(
                    LOCALE_MESSAGES.getMessage("game.planet.cities.growth", 0));//new JLabel("Growth: " + (selected.getPopulationUnitPercentage()) + "% done, " + increment + "% within the next 40 days.");
            add(growthAmount);

            double unemploymentRate = selectedCity.getUnemploymentRate();
            JLabel unemployment = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.cities.unempoymentrate", Math.round(unemploymentRate * 100)));
            if (unemploymentRate < 1f && unemploymentRate > 0f) {
                unemployment.setForeground(new Color((float) unemploymentRate, 0f, 0f));
            }
            add(unemployment);

            JLabel wealth = new JLabel("Wealth: " + Utilities.longToHumanString(selectedCity.getWealth()));
            add(wealth);

            //Check for govenor
            if (selectedCity.getGovernor() != null) {
                JLabel governorLabel = new JLabel(
                        LOCALE_MESSAGES.getMessage("game.planet.cities.governor", selectedCity.getGovernor().getName()));
                add(governorLabel);
            }

            setBorder(new TitledBorder(new LineBorder(Color.gray), LOCALE_MESSAGES.getMessage("game.planet.cities.overview")));
        }
    }

    private class MapMinimap extends JComponent {

        double translateX;
        double translateY;
        boolean isLoaded;
        int enlargementSize = 2;
        private Image planetSurfaceMap = null;
        private double tileSize = 8;

        public MapMinimap() {
            setPreferredSize(new Dimension(150, 150));
            translateX = -selectedCity.getInitialPoint().getX() * tileSize + getWidth() / 2;
            translateY = -selectedCity.getInitialPoint().getY() * tileSize + getWidth() / 2;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            //Turn on AA.
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            translateX = -selectedCity.getInitialPoint().getX() * tileSize + getWidth() / 2;
            translateY = -selectedCity.getInitialPoint().getY() * tileSize + getWidth() / 2;
            g2d.translate(translateX, translateY);

            //Terrain
            if (provider.isLoaded() && !isLoaded) {
                Image img = provider.getImage();
                planetSurfaceMap = img.getScaledInstance(img.getWidth(null) * enlargementSize, img.getHeight(null) * enlargementSize, Image.SCALE_DEFAULT);
                isLoaded = true;
            }

            if (isLoaded) {
                g2d.drawImage(planetSurfaceMap, 0, 0, null);
            }

            //g2d
            Graphics2D mapGraphics = g2d;

            Iterator<GeographicPoint> distIterator = planet.cityDistributions.keySet().iterator();

            //for (int i = 0; i < size(); i++) {
            Rectangle r = g.getClipBounds();
            while (distIterator.hasNext()) {
                GeographicPoint point = distIterator.next();
                City c = planet.getCity(point);
                if (c != null) {
                    int xPos = (int) (point.getX() * tileSize);
                    int yPos = (int) (point.getY() * tileSize);
                    if (r.contains(new Point(xPos, yPos))) {
                        //Draw supply lines
                        ArrayList<ObjectReference> ref = c.getSupplyConnections();
                        for (int i = 0; i < ref.size(); i++) {
                            SupplySegment segment = gameState.getObject(ref.get(i), SupplySegment.class);
                            SupplyNode pt1 = gameState.getObject(segment.getPoint1(), SupplyNode.class);
                            SupplyNode pt2 = gameState.getObject(segment.getPoint2(), SupplyNode.class);
                            GeographicPoint city1Pt = null;
                            GeographicPoint city2Pt = null;
                            if (pt1 instanceof City) {
                                City originCity = (City) pt1;
                                city1Pt = originCity.getInitialPoint();
                            }
                            if (pt2 instanceof City) {
                                City destCity = (City) pt2;
                                city2Pt = destCity.getInitialPoint();
                            }
                            if (city1Pt != null && city2Pt != null) {
                                //Draw line
                                Line2D.Double line = new Line2D.Double(
                                        city1Pt.getX() * tileSize + tileSize / 2,
                                        city1Pt.getY() * tileSize + tileSize / 2,
                                        city2Pt.getX() * tileSize + tileSize / 2,
                                        city2Pt.getY() * tileSize + tileSize / 2);
                                g2d.setColor(Color.red);
                                g2d.draw(line);
                            }

                        }

                        //Draw city
                        Rectangle2D.Double rect = new Rectangle2D.Double(xPos, yPos, tileSize, tileSize);

                        //Draw tile color
                        mapGraphics.setColor(CityType.getDistrictColor(c.getCityType()));
                        mapGraphics.fill(rect);

                        float fontSize = 16;
                        Font derivedFont = getFont().deriveFont(fontSize);
                        int width = getFontMetrics(derivedFont).stringWidth(c.getName());

                        //Draw city name
                        //Draw fancy text
                        GraphicsUtil.paintTextWithOutline(c.getName(), g, fontSize, xPos - width / 2, yPos + getFontMetrics(derivedFont).getHeight() / 2);
                    }
                }
            }
        }
    }




    private static final HashMap<Integer, Image> citySkylineImageMap = new HashMap<>();
    private static int imageCount = 0;

    static {
        int height = 150;
        Thread t = new Thread(() -> {
            try {
                //Choose random image from dir
                File panoFiles = new File("assets/img/pano/");
                File[] imageList = panoFiles.listFiles();
                int i = 0;
                for (File imgfile : imageList) {
                    Image img = ImageIO.read(imgfile);

                    double ratio = height / (double) img.getHeight(null);
                    int width = (int) ((double) img.getWidth(null) * ratio);
                    Image bg = new BufferedImage(500, height, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D graphics = (Graphics2D) bg.getGraphics();
                    graphics.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    graphics.drawImage(img.getScaledInstance(width, height, Image.SCALE_DEFAULT), 0, 0, null);

                    //Save image
                    citySkylineImageMap.put(i, bg);
                    i++;
                }
                imageCount = i;

            } catch (IOException ex) {
                //Ignore for now 
                //FIXME
            }
        });
        t.start();
    }

}
