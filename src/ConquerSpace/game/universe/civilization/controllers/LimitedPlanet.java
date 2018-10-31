package ConquerSpace.game.universe.civilization.controllers;

import ConquerSpace.game.universe.spaceObjects.Planet;

/**
 *
 * @author Zyun
 */
public class LimitedPlanet {
    private Planet planet;
    private int visionType;
    
    public LimitedPlanet(Planet planet, int visionType) {
        this.planet = planet;
        this.visionType = visionType;
    }
    
    public int getID() {
        return planet.getId();
    }
           
    public int getVisionType() {
        return visionType;
    }
}
