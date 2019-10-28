package ConquerSpace.game.population;

/**
 * Species or race
 * @author zyunl
 */
public class Species {
    //Usage of food per month
    private int foodPerMonth;
    //Base increase in population per year. Will be incremented per every couple of ticks
    private float breedingRate;

    private String name;
    
    //The amount of support a pop unit needs (as in pop)
    private float upkeep = 0;

    public Species(int foodPerMonth, float breedingRate, String name) {
        this.foodPerMonth = foodPerMonth;
        this.breedingRate = breedingRate;
        this.name = name;
    }

    public float getBreedingRate() {
        return breedingRate;
    }

    public int getFoodPerMonth() {
        return foodPerMonth;
    }

    public String getName() {
        return name;
    }

    public void setBreedingRate(float breedingRate) {
        this.breedingRate = breedingRate;
    }

    public void setFoodPerMonth(int foodPerMonth) {
        this.foodPerMonth = foodPerMonth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getUpkeep() {
        return upkeep;
    }

    public void setUpkeep(float upkeep) {
        this.upkeep = upkeep;
    }
}

