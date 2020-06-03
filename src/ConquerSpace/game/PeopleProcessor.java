/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.game;

import ConquerSpace.game.civilization.government.GovernmentPosition;
import ConquerSpace.game.civilization.government.HeritableGovernmentPosition;
import ConquerSpace.game.city.City;
import ConquerSpace.game.people.Administrator;
import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.PersonEnterable;
import ConquerSpace.game.population.Race;
import ConquerSpace.game.universe.bodies.Body;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.StarSystem;
import ConquerSpace.game.universe.bodies.Universe;

/**
 *
 * @author EhWhoAmI
 */
public class PeopleProcessor {

    Universe u;
    StarDate date;
    private StarDate before;

    public void createPeople() {
        //Iterate through planets and check for peopl
        for (int starSystemCount = 0; starSystemCount < u.getStarSystemCount(); starSystemCount++) {
            StarSystem starSystem = u.getStarSystem(starSystemCount);
            //Check for ships or whatever
            for (int i = 0; i < starSystem.bodies.size(); i++) {
                Body body = starSystem.bodies.get(i);
                if (body instanceof Planet) {
                    Planet planet = (Planet) body;
                    //If city is populated
                    if (planet.isHabitated()) {
                        for (City c : planet.cities) {
                            //Process cities
                            //Depending on the population size, add things
                            int populationSize = c.getPopulationSize();
                            //Calculate the percentage chance to create someone
                            int peopleCount = (int) ((double) populationSize * 0.05d);
                            //Create people
                            //TODO
                        }
                    }
                }
            }
        }
    }

    public Person createPerson(Race s) {
        //Set age...
        int age = (int) (Math.random() * 60);

        Person p = new Person("Bobby Tables", age);
        return p;
    }

    public void processPeople() {
        for (int starSystemCount = 0; starSystemCount < u.getStarSystemCount(); starSystemCount++) {
            StarSystem starSystem = u.getStarSystem(starSystemCount);
            //Check for ships or whatever
            for (int planetCount = 0; planetCount < starSystem.bodies.size(); planetCount++) {
                Body body = starSystem.bodies.get(planetCount);
                if (body instanceof Planet) {
                    Planet planet = (Planet) body;
                    //If city is populated
                    if (planet.isHabitated()) {
                        processPlanet(planet);
                    }
                }
            }
        }
        //Set previous date
        resetDate();
    }

    public PeopleProcessor(Universe u, StarDate date) {
        this.u = u;
        this.date = date;
        resetDate();
    }

    private void resetDate() {
        before = new StarDate();
        before.bigint = date.bigint;
    }

    public static void placePerson(PersonEnterable enter, Person who) {
        enter.getPeopleArrayList().add(who);
        who.setPosition(enter);
    }

    private void processDeath(Person p) {
        //Kill him
        //Remove from everywhere
        p.setDead(true);
        if (p instanceof Administrator) {
            if (((Administrator) p).position instanceof HeritableGovernmentPosition) {
                HeritableGovernmentPosition pos = (HeritableGovernmentPosition) ((Administrator) p).position;
                GovernmentPosition nextPosition = pos.nextInLine;
                //Get person
                Person next = ((Administrator) p).employer.government.officials.get(nextPosition);
                ((Administrator) p).employer.government.officials.put(pos, next);
                next.setRole("GOT EM");
            }
        }
    }

    private void processPlanet(Planet planet) {
        for (City c : planet.cities) {
            for (Person p : c.peopleAtCity) {
                if (!p.isDead()) {
                    //Process stuff
                    //Increment age

                    long previous = date.bigint - before.bigint;
                    p.age += (int) previous;

                    if (p.age > 500) {
                        processDeath(p);
                    }
                }
            }
        }
    }
}
