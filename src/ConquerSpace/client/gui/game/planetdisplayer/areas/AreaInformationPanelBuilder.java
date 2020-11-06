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

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.city.area.AreaDispatcher;
import ConquerSpace.common.game.city.area.CapitolArea;
import ConquerSpace.common.game.city.area.CommercialArea;
import ConquerSpace.common.game.city.area.ConstructingArea;
import ConquerSpace.common.game.city.area.ConsumerArea;
import ConquerSpace.common.game.city.area.CustomComponentFactoryManufacturerArea;
import ConquerSpace.common.game.city.area.FarmFieldArea;
import ConquerSpace.common.game.city.area.InfrastructureArea;
import ConquerSpace.common.game.city.area.LogisticsHubArea;
import ConquerSpace.common.game.city.area.ManufacturerArea;
import ConquerSpace.common.game.city.area.MineArea;
import ConquerSpace.common.game.city.area.ObservatoryArea;
import ConquerSpace.common.game.city.area.PopulationUpkeepArea;
import ConquerSpace.common.game.city.area.PortArea;
import ConquerSpace.common.game.city.area.PowerPlantArea;
import ConquerSpace.common.game.city.area.ResearchArea;
import ConquerSpace.common.game.city.area.ResidentialArea;
import ConquerSpace.common.game.city.area.ResourceStockpileArea;
import ConquerSpace.common.game.city.area.SpacePortArea;
import ConquerSpace.common.game.city.area.TimedManufacturerArea;
import javax.swing.JPanel;

/**
 *
 * @author EhWhoAmI
 */
public class AreaInformationPanelBuilder implements AreaDispatcher {

    JPanel panel;
    GameState gameState;

    public AreaInformationPanelBuilder(GameState gameState) {
        this.gameState = gameState;
        panel = null;
    }

    @Override
    public void dispatch(CommercialArea area) {
        panel = new CommercialAreaInformationPanel(area, gameState);
    }

    @Override
    public void dispatch(ConstructingArea area) {
        panel = new ConstructionAreaInformationPanel(area, gameState);
    }

    @Override
    public void dispatch(ConsumerArea area) {

    }

    @Override
    public void dispatch(CustomComponentFactoryManufacturerArea area) {
        panel = new CustomComponentFactoryManufacturerAreaPanel(area, gameState);
    }

    @Override
    public void dispatch(TimedManufacturerArea area) {

    }

    @Override
    public void dispatch(ManufacturerArea area) {
        panel = new ManufacturerAreaInformationPanel(area, gameState);
    }

    @Override
    public void dispatch(MineArea area) {
        panel = new MineAreaInformationPanel(area, gameState);
    }

    @Override
    public void dispatch(SpacePortArea area) {
        panel = new SpacePortAreaInformationPanel(area, gameState);
    }

    @Override
    public void dispatch(LogisticsHubArea area) {
        
    }

    @Override
    public void dispatch(ObservatoryArea area) {

    }

    @Override
    public void dispatch(ResourceStockpileArea area) {

    }

    @Override
    public void dispatch(FarmFieldArea area) {
        panel = new FarmFieldAreaInformationPanel(area, gameState);
    }

    @Override
    public void dispatch(PowerPlantArea area) {
        panel = new PowerPlantAreaInformationPanel(area, gameState);
    }

    @Override
    public void dispatch(ResearchArea area) {
        panel = new ResearchAreaInformationPanel(area, gameState);
    }

    @Override
    public void dispatch(ResidentialArea area) {
        panel = new ResidentialAreaInformationPanel(area, gameState);
    }

    @Override
    public void dispatch(InfrastructureArea area) {
        panel = new InfrastructureAreaInformationPanel(area, gameState);
    }

    @Override
    public void dispatch(CapitolArea area) {
        panel = new CapitolAreaInformationPanel(area, gameState);
    }

    public JPanel getPanel(Area area) {
        if(panel == null) {
            panel = new EmptyAreaPanel(area, gameState);
        }
        return panel;
    }

    @Override
    public void dispatch(PortArea area) {
    }

    @Override
    public void dispatch(PopulationUpkeepArea area) {
    }
}
