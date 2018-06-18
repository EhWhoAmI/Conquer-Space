package ConquerSpace.game.actions;

/**
 *
 * @author Zyun
 */
public class Alert {
    private int type;
    private int level;
    private String desc;

    public Alert(int type, int level, String desc) {
        this.type = type;
        this.level = level;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int getLevel() {
        return level;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return desc;
    }    
}