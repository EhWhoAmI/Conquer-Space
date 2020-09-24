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
import ConquerSpace.common.save.SerializeClassName;
import java.util.HashMap;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("construction-area")
public class ConstructingArea extends Area{
    private int ticksLeft;
    private ObjectReference toBuild;
    private HashMap<Integer, Double> costPerTurn;

    /**
     * This is the only constructor that can be public, because it is supposed to be easy to make it.
     * @param gameState
     * @param ticks
     * @param toBuild 
     */
    public ConstructingArea(GameState gameState, int ticks, Area toBuild) {
        super(gameState);
        this.ticksLeft = ticks;
        this.toBuild = toBuild.getReference();
        costPerTurn = new HashMap<>();
    }

    public ObjectReference getToBuild() {
        return toBuild;
    }

    public int getTicksLeft() {
        return ticksLeft;
    }

    public void setTicksLeft(int ticksLeft) {
        this.ticksLeft = ticksLeft;
    }

    public void setCostPerTurn(HashMap<Integer, Double> costPerTurn) {
        this.costPerTurn = costPerTurn;
    }

    public void tickConstruction(int value) {
        ticksLeft -= value;
    }

    @Override
    public String toString() {
        return "Constructing " + toBuild.toString();
    }
    
    public HashMap<Integer, Double> getCostPerTurn() {
        return costPerTurn;
    }
}
