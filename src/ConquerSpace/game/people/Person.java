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
package ConquerSpace.game.people;

import ConquerSpace.game.jobs.Employer;
import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class Person {
    private String name;
    public int age;
    private PersonEnterable position;
    public ArrayList<PersonalityTrait> traits;
    private final Role role;
    private int wealth;
    public Employer employer;
    private boolean dead;
    
    //Not sure what to add to this for now
    //private ArrayList<?> multipliers;
    //Job??

    public Person(String name, int age) {
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
        return position;
    }

    public void setPosition(PersonEnterable position) {
        this.position = position;
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
