package ConquerSpace.game.tech;

/**
 * Class for tech.
 *
 * @author Zyun
 */
public class Techonology {

    private String name;
    private String[] deps;
    private int type;
    private int level;
    private String[] fields;
    private String[] tags;
    private String[] actions;
    private int floor;
    private int difficulty;

    public Techonology(String name, String[] deps, int type, int level, String[] fields, String[] tags, String[] actions, int floor, int difficulty) {
        this.name = name;
        this.deps = deps;
        this.type = type;
        this.level = level;
        this.fields = fields;
        this.tags = tags;
        this.actions = actions;
        this.floor = floor;
        this.difficulty = difficulty;
    }

    public String[] getDeps() {
        return deps;
    }

    public String[] getFields() {
        return fields;
    }

    public int getFloor() {
        return floor;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public String[] getTags() {
        return tags;
    }

    public int getType() {
        return type;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
    /**
     * A technology if only the floor, level, name, and type are equal. Not the 
     * dependencies and the tags.
     * @param obj The object to compare
     * @return is Equal
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Techonology && 
                this.floor == ((Techonology) obj).floor
                && this.level == ((Techonology) obj).level
                && this.name.equals(((Techonology) obj).name)
                && this.type == ((Techonology) obj).type);
    }

    @Override
    public int hashCode() {
        return ((name + floor + level + type).hashCode());
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String[] getActions() {
        return actions;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
