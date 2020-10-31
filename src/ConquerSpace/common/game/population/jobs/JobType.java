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
package ConquerSpace.common.game.population.jobs;

import java.awt.Color;

/**
 *
 * @author EhWhoAmI
 */
public enum JobType {
    Miner("Miner", new Color(140, 3, 252)),
    Jobless("Jobless", Color.red),
    Administrator("Administrator", new Color(252, 98, 3)),
    Farmer("Farmer", Color.green),
    Construction("Construction", Color.darkGray),
    Infrastructure("Infrastructure", Color.WHITE),
    AeronauticalEngineer("Aeronautical Engineer", Color.orange),
    PopUpkeepWorker("Population Upkeep Worker", new Color(255, 0, 255)),
    SpacePortEngineer("Space Port Engineer", Color.pink),
    PowerPlantTechnician("Power Plant Technician", Color.yellow),
    FactoryWorker("Factory Worker", Color.GRAY),
    Researcher("Researcher", Color.cyan),
    Educator("Teacher", new Color(210, 105, 30)), //Brown
    Imaginary("Imaginary", Color.red),
    Independent("Independent", Color.magenta);

    private final String name;
    private final Color color;

    JobType(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }
}
