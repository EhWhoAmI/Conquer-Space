package ConquerSpace.game.people;

/**
 *
 * @author
 */
public class Administrator extends Person {

    public Administrator(String name, int age) {
        super(name, age);
    }

    @Override
    public String getJobName() {
        return "Administrator";
    }
}
