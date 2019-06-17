package ConquerSpace.game.people;

/**
 *
 * @author Zyun
 */
public class Scientist extends Person{
    //Skill increments per tick.
    private int skill;
    public Scientist(String name, int age) {
        super(name, age);
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    @Override
    public String getJobName() {
        return "Scientist";
    }
    
    
}
