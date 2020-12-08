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
import ConquerSpace.common.game.organizations.Civilization;

/**
 *
 * @author EhWhoAmI
 */
public class FarmFieldFactory extends AreaFactory {

    private ObjectReference grownCrop;

    /**
     * Time until next harvest.
     */
    private int time;

    /**
     * Size of fields in km^2.
     */
    private int fieldSize;

    /**
     * metric tons of food per km^ of land per harvest.
     */
    private int productivity;

    public FarmFieldFactory(Civilization builder) {
        super(builder);
    }

    public void setGrownCrop(ObjectReference grown) {
        this.grownCrop = grown;
    }

    public ObjectReference getGrownCrop() {
        return grownCrop;
    }

    public void setFieldSize(int fieldSize) {
        this.fieldSize = fieldSize;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    @Override
    public Area build(GameState gameState) {
        FarmFieldArea area = new FarmFieldArea(gameState, grownCrop);
        setDefaultInformation(gameState, area);
        area.setTime(time);
        area.setFieldSize(fieldSize);
        area.setProductivity(productivity);
        return area; //To change body of generated methods, choose Tools | Templates.
    }

    public int getProductivity() {
        return productivity;
    }

    public void setProductivity(int productivity) {
        this.productivity = productivity;
    }
}
