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
package ConquerSpace.common.game.characters;

import ConquerSpace.Globals;
import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.population.jobs.Employer;
import ConquerSpace.common.save.Serialize;
import java.util.ArrayList;

/**
 *
 * @author EhWhoAmI
 */
public class Person extends ConquerSpaceGameObject{
    @Serialize(key = "name")
    private String name;
    
    @Serialize(key = "age")
    public int age;
    
    //@Serialize(key = "location")
    //How to serialize person enterable
    private PersonEnterable location;
    
    @Serialize(key = "traits")
    public ArrayList<PersonalityTrait> traits;
    
    private final Role role;
    
    @Serialize(key = "wealth")
    private int wealth;
    public Employer employer;
    
    @Serialize(key = "dead")
    private boolean dead;
    
    //Not sure what to add to this for now
    //private ArrayList<?> multipliers;
    //Job??

    public Person(GameState gameState, String name, int age) {
        super(gameState);
        this.name = name;
        this.dead = false;
        this.age = age;
        traits = new ArrayList<>();
        role = new Role();
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
    /**
     * Basically become older.
     */
    public void age() {
        age++;
    }

    @Override
    public String toString() {
        return getName();
    }
    
    public String getJobName() {
        return "None";
    }

    public PersonEnterable getPosition() {
        return location;
    }

    public void setPosition(PersonEnterable position) {
        this.location = position;
    }
    
    public void setRole(String text) {
        role.setText(text);
    }
    
    public String roleText() {
        return role.getText();
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
