package ConquerSpace.game.science;

/**
 * for use of civs and stuff like that.
 * @author Zyun
 */
public class Field {
    private FieldNode node;
    private int level;

    public Field(FieldNode node, int level) {
        this.node = node;
        this.level = level;
    }

    public FieldNode getNode() {
        return node;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return node.getName();
    }
    
    public void incrementLevel(int amount) {
        this.level+=amount;
    }
}
