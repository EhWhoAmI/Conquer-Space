package ConquerSpace.game;

import ConquerSpace.game.buildings.City;
import ConquerSpace.game.buildings.PopulationStorage;
import ConquerSpace.game.life.Species;
import ConquerSpace.game.people.Person;
import ConquerSpace.game.population.Race;
import ConquerSpace.game.universe.civilization.stats.Population;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;

/**
 *
 * @author zyunl
 */
public class PeopleProcessor {

    Universe u;
    StarDate date;

    public void createPeople() {
        //Iterate through planets and check for peopl
        for (int starSystemCount = 0; starSystemCount < u.getStarSystemCount(); starSystemCount++) {
            StarSystem starSystem = u.getStarSystem(starSystemCount);
            //Check for ships or whatever
            for (int planetCount = 0; planetCount < starSystem.getPlanetCount(); planetCount++) {
                Planet planet = starSystem.getPlanet(planetCount);

                //If city is populated
                if (planet.isHabitated()) {
                    for (City c : planet.cities) {
                        //Process cities
                        //Depending on the population size, add things
                        int populationSize = c.getPopulationSize();
                        //Calculate the percentage chance to create someone
                        int peopleCount = (int) ((double) populationSize * 0.05d);
                        //Dew it
                        for (int k = 0; k < peopleCount; k++) {
                            //Get first thing
                            if (c.buildings.get(0) instanceof PopulationStorage) {
                                Race s = ((PopulationStorage) c.buildings.get(0)).getPopulationArrayList().get(0).species;
                                Person people = createPerson(s);
                                //c.peopleAtCity.add(people);
                            }
                        }
                    }
                }
            }
        }
    }

    public Person createPerson(Race s) {
        //Set age...
        int age = (int) (Math.random()*60);
        
        Person p = new Person("Bobby Tables", age);
        return p;
    }
    
    public void processPeople() {
        for (int starSystemCount = 0; starSystemCount < u.getStarSystemCount(); starSystemCount++) {
            StarSystem starSystem = u.getStarSystem(starSystemCount);
            //Check for ships or whatever
            for (int planetCount = 0; planetCount < starSystem.getPlanetCount(); planetCount++) {
                Planet planet = starSystem.getPlanet(planetCount);

                //If city is populated
                if (planet.isHabitated()) {
                    for (City c : planet.cities) {
                        for(Person p : c.peopleAtCity) {
                            //Process stuff
                        }
                    }
                }
            }
        }
    }

    public PeopleProcessor(Universe u) {
        this.u = u;
    }
}
