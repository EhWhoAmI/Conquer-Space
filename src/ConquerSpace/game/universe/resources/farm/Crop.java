package ConquerSpace.game.universe.resources.farm;

import ConquerSpace.game.life.Species;

/**
 * A crop that grows stuff.
 * @author zyunl
 */
public class Crop {
    private int timeLeft;
    private Species species;
    private int yield;
    
    public Crop(Species species) {
        this.species = species;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public Species getSpecies() {
        return species;
    }

    public int getYield() {
        return yield;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void setYield(int yield) {
        this.yield = yield;
    }
    
    public void subtractTime() {
        timeLeft--;
    }

    @Override
    public String toString() {
        return species.getName();
    }
    
    
}
