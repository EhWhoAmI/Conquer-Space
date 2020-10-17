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

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.save.Serialize;
import ConquerSpace.common.save.SerializeClassName;
import java.util.ArrayList;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("person")
public class Person extends ConquerSpaceGameObject {

    @Serialize("name")
    private String name;

    @Serialize("age")
    public int age;

    //@Serialize(key = "location")
    //How to serialize person enterable
    private ObjectReference location;

    @Serialize("traits")
    public ArrayList<PersonalityTrait> traits;

    private final Role role;

    @Serialize("wealth")
    private int wealth;
    public ObjectReference employer;

    @Serialize("dead")
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
        return (PersonEnterable) gameState.getObject(location);
    }

    public void setPosition(PersonEnterable position) {
        if (position instanceof ConquerSpaceGameObject) {
            this.location = ((ConquerSpaceGameObject) position).getReference();
        }
    }
    
    public void setPosition(ObjectReference reference) {
        this.location = reference;
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
