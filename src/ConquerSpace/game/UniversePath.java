package ConquerSpace.game;

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

    public UniversePath(String path) {
        this.path = path;
    }
    
}
