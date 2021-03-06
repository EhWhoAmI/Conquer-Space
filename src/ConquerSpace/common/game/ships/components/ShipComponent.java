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
package ConquerSpace.common.game.ships.components;

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.resources.ManufacturedGood;
import ConquerSpace.common.game.resources.StoreableReference;

/**
 *
 * @author EhWhoAmI
 */
public abstract class ShipComponent extends ConquerSpaceGameObject implements Cloneable {

    //Mass in KG
    protected int mass;
    protected int volume;

    protected String name;

    protected StoreableReference good;

    public ShipComponent(GameState gameState) {
        super(gameState);
        ManufacturedGood manufacturedGood = new ManufacturedGood();
        manufacturedGood.tags = new String[]{"component"};
        gameState.addGood(manufacturedGood);
        this.good = manufacturedGood.getId();
    }

    public void setMass(int mass) {
        this.mass = mass;
        ((ManufacturedGood) gameState.getGood(good)).setMass(mass);
    }

    public void setName(String name) {
        this.name = name;
        ((ManufacturedGood) gameState.getGood(good)).setName(name);
    }

    public int getMass() {
        return mass;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public void setVolume(int volume) {
        this.volume = volume;
        ((ManufacturedGood) gameState.getGood(good)).setVolume(volume);
    }

    public int getVolume() {
        return volume;
    }

    public StoreableReference getGood() {
        return good;
    }

    public void createRecipe(ObjectReference civilization) {
        //Calculate the recipe, based on various factors, including tech
    }

    public abstract ShipComponentType getShipComponentType();
}
