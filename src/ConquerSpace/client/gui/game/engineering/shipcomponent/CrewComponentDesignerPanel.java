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
package ConquerSpace.client.gui.game.engineering.shipcomponent;

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.ships.components.CrewComponent;
import ConquerSpace.common.game.ships.components.ShipComponent;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author EhWhoAmI
 */
public class CrewComponentDesignerPanel extends ShipComponentDesignerPanel {

    JSpinner crewCount;
    GameState gameState;

    public CrewComponentDesignerPanel(GameState gameState) {
        this.gameState = gameState;

        setLayout(new VerticalFlowLayout());
        crewCount = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));

        JPanel crewCountContainer = new JPanel(new HorizontalFlowLayout());
        crewCountContainer.add(new JLabel("Rated Crew Count: "));
        crewCountContainer.add(crewCount);
        add(crewCountContainer);
    }

    @Override
    ShipComponent generateComponent() {
        CrewComponent crewComponent = new CrewComponent(gameState);
        crewComponent.setMaxCrew((Integer)crewCount.getValue());
        return crewComponent; 
    }

    @Override
    public void clearUI() {
        crewCount.setValue(1);
    }

    @Override
    public void loadComponent(ShipComponent comp) {
        if(comp instanceof CrewComponent) {
            crewCount.setValue(((CrewComponent) comp).getMaxCrew());
        }
    }
}
