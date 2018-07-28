package ConquerSpace.game.universe.spaceObjects.pSectors;


/**
 *
 * @author Zyun
 */
public class SpacePortBuilding extends PlanetSector{
    private int techLevel;
    private int ports;  
    public SpacePortLaunchPad[] launchPads;
    
    public SpacePortBuilding(int techLevel, int ports) {
        this.techLevel = techLevel;
        this.ports = ports;
        launchPads = new SpacePortLaunchPad[ports];
    }
}
