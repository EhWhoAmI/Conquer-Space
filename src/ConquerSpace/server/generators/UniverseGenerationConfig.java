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
package ConquerSpace.server.generators;

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;

/**
 * Config of the Universe to be passed on to the scripts.
 *
 * @author EhWhoAmI
 */
public class UniverseGenerationConfig {
    //Everything is in string because python is a jerk and there are no enums.
    //So we pass it off as strings to parse it later.

    /**
     * Size of universe
     */
    public UniverseSize universeSize;

    public static enum UniverseSize {
        Small("universe.size.small"),
        Medium("universe.size.medium"),
        Large("universe.size.large");

        String text;

        private UniverseSize(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return LOCALE_MESSAGES.getMessage(text);
        }
    }
    /**
     * Shape of universe
     */
    public UniverseShape universeShape;

    public static enum UniverseShape {
        //Remove spiral and elliptical for now because it is easier
        Irregular("universe.shape.irregular");
        String text;

        private UniverseShape(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return LOCALE_MESSAGES.getMessage(text);
        }
    }

    /**
     * Age of universe
     */
    public UniverseAge universeAge;

    public static enum UniverseAge {
        Short("universe.age.short"),
        Medium("universe.age.medium"),
        Long("universe.age.long"),
        Ancient("universe.age.ancient");

        String text;

        private UniverseAge(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return LOCALE_MESSAGES.getMessage(text);
        }
    }

    /**
     * How common planets are
     */
    public PlanetRarity planetCommonality;

    public static enum PlanetRarity {
        Common("univerese.planet.rarity.common"),
        Sparse("univerese.planet.rarity.sparse");

        String text;

        private PlanetRarity(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return LOCALE_MESSAGES.getMessage(text);
        }
    }

    /**
     * Number of civilizations
     */
    public CivilizationCount civilizationCount;

    public static enum CivilizationCount {
        Common("universe.civilization.count.common"),
        Sparse("universe.civilization.count.sparse");

        String text;

        private CivilizationCount(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return LOCALE_MESSAGES.getMessage(text);
        }
    }

    /**
     * Seed of universe generation
     */
    public long seed;

    /**
     * Civilization config for player
     */
    public CivilizationConfig civConfig;
}
