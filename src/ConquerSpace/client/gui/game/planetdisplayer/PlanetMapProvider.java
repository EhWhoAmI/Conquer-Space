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
package ConquerSpace.client.gui.game.planetdisplayer;

import ConquerSpace.client.gui.renderers.TerrainRenderer;
import ConquerSpace.common.game.universe.bodies.Planet;
import java.awt.Image;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetMapProvider {

    private boolean loaded;
    private Image image;

    public PlanetMapProvider(Planet p) {
        loaded = false;
        image = null;
        Thread t = new Thread(() -> {
            long start = System.currentTimeMillis();
            TerrainRenderer renderer = new TerrainRenderer(p);
            image = renderer.getImage();
            long end = System.currentTimeMillis();

            loaded = true;
        });
        t.setName("Terrain renderer");
        t.start();
    }

    public boolean isLoaded() {
        return loaded;
    }

    public Image getImage() {
        return image;
    }
}
