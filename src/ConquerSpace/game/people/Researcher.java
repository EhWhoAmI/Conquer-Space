package ConquerSpace.game.people;

/**
 *
 * @author Zyun
 */
public class Researcher extends Person{
    private int skill;
    public Researcher(String name, int age) {
        super(name, age);
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }
}
