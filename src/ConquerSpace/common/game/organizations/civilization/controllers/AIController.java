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
package ConquerSpace.common.game.organizations.civilization.controllers;

import ConquerSpace.common.GameState;
import ConquerSpace.common.StarDate;
import ConquerSpace.common.actions.Action;
import ConquerSpace.common.actions.Alert;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.events.Event;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.util.logging.CQSPLogger;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;

/**
 * Controller of the AI. Will need to link to scripts.
 *
 * @author EhWhoAmI
 */
public class AIController extends CivilizationController {
    private static final Logger LOGGER = CQSPLogger.getLogger(AIController.class.getName());

    @Override
    public ArrayList<Action> doTurn(Civilization c) {
        //For now comment it out... Until we do the ai
        //actions = (ArrayList <Action>) script.getObject("actions");
        ArrayList<Action> actions = new ArrayList<>();
        return actions;
    }

    @Override
    public void alert(Alert a) {
        //Skip for AI, at least for now.
    }

    @Override
    public void init(GameState state, Civilization c) {
        LOGGER.info("initialized the ai for " + c.getName());
    }

    @Override
    public void refreshUI() {
        //Ignore
    }

    @Override
    public void passEvent(Event e) {
    }
}
