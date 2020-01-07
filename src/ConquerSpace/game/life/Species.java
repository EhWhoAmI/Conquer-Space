package ConquerSpace.game.life;

import java.util.ArrayList;

/**
 * Species on a planet, can go to other planets
 * @author zyunl
 */
public class Species {
    public ArrayList<LifeTrait> lifeTraits;

    //Breeding rate
    private float baseBreedingRate = 0;
    //Breeding method
    //Etc...
    private String name;
    public Species() {
        lifeTraits = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public float getBaseBreedingRate() {
        return baseBreedingRate;
    }

    public void setBaseBreedingRate(float baseBreedingRate) {
        this.baseBreedingRate = baseBreedingRate;
    }
    
    
}
