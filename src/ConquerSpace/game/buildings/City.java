package ConquerSpace.game.buildings;

import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.PersonEnterable;
import ConquerSpace.game.population.Job;
import ConquerSpace.game.universe.UniversePath;
import java.util.ArrayList;

/**
 *
 * @author zyunl
 */
public class City implements PersonEnterable{
    
    private Person governor;
    private String name;
    public ArrayList<PopulationStorage> storages;
    public ArrayList<Job> jobs;

    private UniversePath location;
    //% to completing a unit
    private float populationUnitPercentage = 0;

    public City(UniversePath location) {
        storages = new ArrayList<>();
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
    
    public void addDistrict(PopulationStorage stor) {
        storages.add(stor);
        if(stor instanceof CityDistrict) {
            ((CityDistrict) stor).setCity(this);
        }
    }

    public Person getGovernor() {
        return governor;
    }

    public void setGovernor(Person governor) {
        governor.setRole("Governing " + name);
        this.governor = governor;
    }
}
