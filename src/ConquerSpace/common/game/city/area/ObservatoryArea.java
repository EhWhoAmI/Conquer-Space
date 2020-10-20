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
package ConquerSpace.common.game.city.area;

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.civilization.vision.VisionPoint;
import ConquerSpace.common.game.population.jobs.JobType;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.save.SerializeClassName;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("observatory-area")
public class ObservatoryArea extends Area implements VisionPoint {

    private ObjectReference civilization;
    private int range;
    private UniversePath position;

    ObservatoryArea(GameState gameState, ObjectReference civilization, int range, UniversePath position) {
        super(gameState);
        this.civilization = civilization;
        this.range = range;
        this.position = position;
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public ObjectReference getCivilization() {
        return civilization;
    }

    @Override
    public UniversePath getPosition() {
        return position;
    }

    public void setCivilization(ObjectReference civilization) {
        this.civilization = civilization;
    }

    public void setRange(int range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return "Observatory";
    }
    
    @Override
    public void accept(AreaDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
    
    @Override
    public JobType getJobClassification() {
        return JobType.Researcher;
    }

    @Override
    public AreaClassification getAreaType() {
        return AreaClassification.Research;
    }
}
