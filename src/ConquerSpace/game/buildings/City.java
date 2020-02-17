package ConquerSpace.game.buildings;

import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.PersonEnterable;
import ConquerSpace.game.jobs.Job;
import ConquerSpace.game.jobs.JobProcessor;
import ConquerSpace.game.population.Race;
import ConquerSpace.game.jobs.Workable;
import ConquerSpace.game.universe.UniversePath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author zyunl
 */
public class City implements PersonEnterable, Workable {

    private Person governor;
    private String name;
    public ArrayList<Building> buildings;
    public ArrayList<Job> jobs;
    public ArrayList<Person> peopleAtCity;

    private UniversePath location;

    //% to completing a unit
    private float populationUnitPercentage = 0;

    //Growth rates of the species...
    private HashMap<Race, Float> speciesRates;

    private boolean resetJobs = false;

    private JobProcessor jobProcessor;

    public City(UniversePath location) {
        buildings = new ArrayList<>();
        jobs = new ArrayList<>();
        //jobProcessor = new JobProcessor();
        this.location = location;
        peopleAtCity = new ArrayList<>();
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

    @Override
    public Job[] jobsNeeded() {
        ArrayList<Job> jobsNeeded = new ArrayList<>();
        //Add city needed jobs

        //Add all children jobs
        for (Building b : buildings) {
            Job[] jobs = b.jobsNeeded();
            for (Job j : jobs) {
                jobsNeeded.add(j);
            }
        }

        Job[] jobArray = Arrays.copyOf(jobsNeeded.toArray(), jobsNeeded.size(), Job[].class);
        return jobArray;
    }

    @Override
    public void processJob(Job j) {
        //Working for the city itself...
    }

    public boolean toResetJobs() {
        return resetJobs;
    }

    public void resetJobs() {
        resetJobs = true;
    }

    public void doneResettingJobs() {
        resetJobs = false;
    }
    
    public int getPopulationSize() {
        int i = 0;
        for (Building b : buildings) {
            if (b instanceof PopulationStorage) {
                i += ((PopulationStorage) b).getPopulationArrayList().size();
            }
        }
        return i;
    }

    @Override
    public ArrayList<Person> getPeopleArrayList() {
        return peopleAtCity;
    }
}
