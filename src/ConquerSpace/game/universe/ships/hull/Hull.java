package ConquerSpace.game.universe.ships.hull;

/**
 *
 * @author Zyun
 */
public class Hull {
    //KGS
    //Also determines hull hp and strength
    private int mass;
    //Meters cube
    private int space;
    
    private HullMaterial material;

    public Hull(int mass, int space, HullMaterial material) {
        this.mass = mass;
        this.space = space;
        this.material = material;
    }
    
    
    
    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public HullMaterial getMaterial() {
        return material;
    }
    
    public float getMassToSpaceRatio() {
        return ((float)mass/(float)space);
    }
    
    public float getStrength() {
        return (getMassToSpaceRatio() * material.getStrength());
    }
    
    public boolean isValid() {
        return (getStrength()>=1);
    }
}
