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
