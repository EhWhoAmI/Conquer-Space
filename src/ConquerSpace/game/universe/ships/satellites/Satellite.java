package ConquerSpace.game.universe.ships.satellites;

import ConquerSpace.game.universe.ships.Launchable;

/**
 *
 * @author Zyun
 */
public class Satellite implements Launchable{
    
    protected int mass;
    protected String name = "";
    protected int id;
    //We'll skip the ksp orbital stuff and detail
    //You know: Apoasis, Perapsis, orbital period... etc...
    //Because no time
    /**
     * Orbital altitude from center of planet, in hundreds of kilometers.
     */
    protected int orbitalAltitude = 0;
        
    public Satellite(int mass) {
        this.mass = mass;
    }

    public int getMass() {
        return mass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return getName();
    }

    public void setOrbitalAltitude(int orbitalAltitude) {
        this.orbitalAltitude = orbitalAltitude;
    }

    public int getOrbitalAltitude() {
        return orbitalAltitude;
    }
}