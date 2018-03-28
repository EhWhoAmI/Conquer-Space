package ConquerSpace.game;

import ConquerSpace.game.universe.GalaticLocation;

/**
 * Path of an object in a universe. To write path:
 * "sectorid:star systemid:planet id" for a planet
 * "sectorid" for a sector
 * "sectorid:star systemid" for a star system
 * "whateverpath:[r, d]" for an object in space, eg a spaceship or satellite. 
 * @author Zyun
 */
public class UniversePath {
    public String path;
    int sectorID;
    int systemID;
    int planetID;
    GalaticLocation loc;
    public UniversePath(String path) {
        this.path = path;
    }
    
    public void parse() {
        String[] text = path.split(":");
        if(text.length == 0) {
            //Incorrect format.
        }
    }
}
