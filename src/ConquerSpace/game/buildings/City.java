package ConquerSpace.game.buildings;

import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.PersonEnterable;
import ConquerSpace.game.population.Job;
import ConquerSpace.game.population.Race;
import ConquerSpace.game.universe.UniversePath;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author zyunl
 */
public class City implements PersonEnterable{
    
    private Person governor;
    private String name;
    public ArrayList<Building> buildings;
    public ArrayList<Job> jobs;

    private UniversePath location;
    
    //% to completing a unit
    private float populationUnitPercentage = 0;
    
    private HashMap<Race, Float> speciesRates;

    public City(UniversePath location) {
        buildings = new ArrayList<>();
        this.location = location;
    }

    public void setPopulationUnitPercentage(float populationUnitPercentage) {
        this.populationUnitPercentage = populationUnitPercentage;
    }

    public float getPopulationUnitPercentage() {
        return populationUnitPercentage;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public UniversePath getUniversePath() {
        return location;
    }
    
    public void addDistrict(Building stor) {
        buildings.add(stor);
        stor.setCity(this);
    }

    public Person getGovernor() {
        return governor;
    }

    public void setGovernor(Person governor) {
        governor.setRole("Governing " + name);
        this.governor = governor;
    }
}
