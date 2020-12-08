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
package ConquerSpace.server;

import ConquerSpace.common.GameState;
import ConquerSpace.common.StarDate;
import ConquerSpace.common.game.characters.Administrator;
import ConquerSpace.common.game.characters.Person;
import ConquerSpace.common.game.characters.PersonEnterable;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.organizations.civilization.government.GovernmentPosition;
import ConquerSpace.common.game.organizations.civilization.government.HeritableGovernmentPosition;
import ConquerSpace.common.game.population.Race;
import ConquerSpace.common.game.universe.bodies.Galaxy;

/**
 *
 * @author EhWhoAmI
 */
public class PeopleProcessor {
    
    GameState gameState;

    Galaxy universe;
    StarDate date;
    private StarDate before;

    public void createPeople() {
    }

    public Person createPerson(Race s) {
        //Set age...
        int age = (int) (Math.random() * 60);

        Person p = new Person(gameState, "Bobby Tables", age);
        return p;
    }

    public void processPeople(int delta) {
//        for(Person person: gameState.people) {
//            if (!person.isDead()) {                    
//                    //Increment age
//                    person.age += (int) delta;
//                    
//                    if (person.age > 50_000) {
//                        processDeath(person);
//                    }
//                }
//        }
//TODO process people
        //Set previous date
        resetDate();
    }

    public PeopleProcessor(GameState gameState) {
        this.universe = gameState.getUniverse();
        this.date = gameState.date;
        this.gameState = gameState;
        resetDate();
    }

    private void resetDate() {
        before = new StarDate(date);
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
            Administrator admin = (Administrator) p;
            if (admin.governmentPosition instanceof HeritableGovernmentPosition) {
                HeritableGovernmentPosition pos = (HeritableGovernmentPosition) admin.governmentPosition;
                GovernmentPosition nextPosition = pos.nextInLine;
                //Get person
                Person next = gameState.getObject(admin.employer, Civilization.class).government.officials.get(nextPosition);
                gameState.getObject(admin.employer, Civilization.class).government.officials.put(pos, next);
                next.setRole("GOT EM");
            }
        }
    }
}
