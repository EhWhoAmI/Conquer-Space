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
package ConquerSpace.game.universe;

import ConquerSpace.game.civilization.CivilizationConfig;
/**
 * Config of the Universe to be passed on to the scripts.
 * @see ConquerSpace.start.gui.NewGame
 * @author EhWhoAmI
 */
public class UniverseConfig{
    //Everything is in string because python is a jerk and there are no enums.
    //So we pass it off as strings to parse it later.
    
    /**
     * Size of universe
     */
    public String universeSize;
    
    /**
     * Shape of universe
     */
    public String universeShape;
    
    /**
     * Age of universe
     */
    public String universeAge;
    
    /**
     * How common planets are
     */
    public String planetCommonality;
    
    /**
     * Number of civilizations
     */
    public String civilizationCount;
    
    /**
     * Seed of universe generation
     */
    public long seed;
    
    /**
     * Civilization config for player
     */
    public CivilizationConfig civConfig;

    /**
     * Set the number of civilizations
     * @param civilizationCount Amount of civilizations as said in the new game class.
     */
    public void setCivilizationCount(String civilizationCount) {
        this.civilizationCount = civilizationCount;
    }

    /**
     * Set number of planets
     * @param planetCommonality Amount of planet as said in the new game class.
     */
    public void setPlanetCommonality(String planetCommonality) {
        this.planetCommonality = planetCommonality;
    }

    /**
     * Set age of galaxy
     * @param universeAge Age of galaxy
     */
    public void setUniverseAge(String universeAge) {
        this.universeAge = universeAge;
    }

    /**
     * Shape of galaxy, Elliptical, irregular and spiral.
     * @param universeShape Shape of galaxy.
     */
    public void setUniverseShape(String universeShape) {
        this.universeShape = universeShape;
    }

    /**
     * Set size of universe
     * @param universeSize Size of universe 
     */
    public void setUniverseSize(String universeSize) {
        this.universeSize = universeSize;
    }
    
    /**
     * Seed as long value
     * @param seed seed.
     */
    public void setSeed(long seed) {
        this.seed = seed;
    }
    
    /**
     * Set the player civilization config
     * @param conf civilization config
     */
    public void setCivilizationConfig(CivilizationConfig conf) {
        this.civConfig = conf;
    }

    /**
     * Get number of civilizations.
     * @return Number of civilizations
     */
    public String getCivilizationCount() {
        return civilizationCount;
    }

    /**
     * Get number of planets.
     * @return number of planets
     */
    public String getPlanetCommonality() {
        return planetCommonality;
    }

    /**
     * Get galaxy age
     * @return Age of galaxy
     */
    public String getUniverseAge() {
        return universeAge;
    }

    /**
     * Get shape of universe
     * @return Shape of universe
     */
    public String getUniverseShape() {
        return universeShape;
    }

    /**
     * Get galaxy size
     * @return galaxy size
     */
    public String getUniverseSize() {
        return universeSize;
    }
    
    /**
     * Get seed
     * @return seed
     */
    public long getSeed() {
        return seed;
    }
    
    /**
     * Get civilization config
     * @return configuration of civilization
     */
    public CivilizationConfig getCivilizationConfig() {
        return (civConfig);
    }
}
