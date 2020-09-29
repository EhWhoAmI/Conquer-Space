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
package ConquerSpace.client.gui.game.planetdisplayer.areas;

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.city.area.CapitolArea;
import ConquerSpace.common.game.city.area.CommercialArea;
import ConquerSpace.common.game.city.area.ConstructingArea;
import ConquerSpace.common.game.city.area.CustomComponentFactoryManufacturerArea;
import ConquerSpace.common.game.city.area.FarmFieldArea;
import ConquerSpace.common.game.city.area.InfrastructureArea;
import ConquerSpace.common.game.city.area.ManufacturerArea;
import ConquerSpace.common.game.city.area.MineArea;
import ConquerSpace.common.game.city.area.ResearchArea;
import ConquerSpace.common.game.city.area.SpacePortArea;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.organizations.Organization;
import ConquerSpace.common.util.logging.CQSPLogger;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author EhWhoAmI
 */
public class AreaInformationPanel<T extends Area> extends JPanel {

    private static final Logger LOGGER = CQSPLogger.getLogger(AreaInformationPanel.class.getName());
    protected T area;
    protected GameState gameState;

    private static final HashMap<Class, Class> areaInfoMap = new HashMap<>();

    static {
        areaInfoMap.put(ResearchArea.class, ResearchAreaInformationPanel.class);
        areaInfoMap.put(CapitolArea.class, CapitolAreaInformationPanel.class);
        areaInfoMap.put(InfrastructureArea.class, InfrastructureAreaInformationPanel.class);
        areaInfoMap.put(ManufacturerArea.class, ManufacturerAreaInformationPanel.class);
        areaInfoMap.put(FarmFieldArea.class, FarmFieldAreaInformationPanel.class);
        areaInfoMap.put(CommercialArea.class, CommercialAreaInformationPanel.class);
        areaInfoMap.put(SpacePortArea.class, SpacePortAreaInformationPanel.class);
        areaInfoMap.put(ConstructingArea.class, ConstructionAreaInformationPanel.class);
        areaInfoMap.put(MineArea.class, MineAreaInformationPanel.class);
        areaInfoMap.put(CustomComponentFactoryManufacturerArea.class, CustomComponentFactoryManufacturerAreaPanel.class);
    }

    public AreaInformationPanel(T area, GameState gameState) {
        this.area = area;
        this.gameState = gameState;

        setLayout(new VerticalFlowLayout());
    }

    protected void genericInformation() {
        JLabel owner = new JLabel("Owner: None");

        Organization org = gameState.getOrganizationObjectByReference(area.getOwner());
        if (org != null) {
            if (org instanceof Civilization) {
                owner.setText("Owner: State owned by " + org.getName());
            } else {
                owner.setText("Owner: " + org.getName());
            }
        }

        JLabel currentJobs = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.manpower.current", area.getCurrentlyManningJobs()));
        JLabel minimumJobs = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.manpower.minimum", area.operatingJobsNeeded()));
        JLabel maximumJobs = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.manpower.maximum", area.getMaxJobsProvided()));
        if (area.getCurrentlyManningJobs() < area.operatingJobsNeeded()) {
            currentJobs.setForeground(Color.red);
            currentJobs.setToolTipText(LOCALE_MESSAGES.getMessage("game.planet.areas.manpower.tooltip"));
        }
        add(owner);
        add(currentJobs);
        add(minimumJobs);
        add(maximumJobs);
    }

    public void update() {

    }

    @SuppressWarnings("unchecked")
    public static AreaInformationPanel getPanel(GameState gameState, Area a) {
        try {
            if (areaInfoMap.containsKey(a.getClass())) {
                return (AreaInformationPanel) areaInfoMap.get(a.getClass()).getConstructor(a.getClass(), GameState.class).newInstance(a, gameState);
            } else {
                //Return empty 
                return new EmptyAreaPanel(a, gameState);
            }
        } catch (InstantiationException 
                | IllegalAccessException 
                | IllegalArgumentException 
                | InvocationTargetException 
                | NoSuchMethodException 
                | SecurityException ex) {
            LOGGER.warn("Unable to open area information panel", ex);
            return new EmptyAreaPanel(a, gameState);
        }
    }
}
