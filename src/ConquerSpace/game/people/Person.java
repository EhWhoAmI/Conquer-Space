package ConquerSpace.game.people;

/**
 *
 * @author Zyun
 */
public class Person {
    private String name;
    private int age;
    //Not sure what to add to this for now
    //private ArrayList<?> multipliers;
    //Job??

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
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
}
