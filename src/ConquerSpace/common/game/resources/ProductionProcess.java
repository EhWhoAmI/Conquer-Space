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
package ConquerSpace.common.game.resources;

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author EhWhoAmI
 */
public class ProductionProcess extends ConquerSpaceGameObject implements Serializable {
    public String name;
    public String identifier;
    public HashMap<StoreableReference, Double> input;
    public HashMap<StoreableReference, Double> output;
    /**
     * Can add the stuff you need to construct this
     */
    public ArrayList<Integer> catalyst;
    //How difficult it is to extract. Will replace with the parts for the factory in the futute.
    public int difficulty;

    public ProductionProcess(GameState gameState) {
        super(gameState);
        input = new HashMap<>();
        output = new HashMap<>();
        catalyst = new ArrayList<>();
    }

    public ProductionProcess(GameState gameState, StoreableReference outputGood) {
        super(gameState);
        input = new HashMap<>();
        output = new HashMap<>();
        output.put(outputGood, 1d);
        catalyst = new ArrayList<>();
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
