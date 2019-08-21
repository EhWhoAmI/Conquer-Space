package ConquerSpace.game.science;

import java.util.ArrayList;

/**
 * for use of civs and stuff like that.
 *
 * @author Zyun
 */
public class Field {

    private String name;
    private int level;
    private Field parent;
    private ArrayList<Field> nodes;

    public Field(String name) {
        this.name = name;
        //-1 is undiscovered, 0 is possible, anything more than that is it exists.
        this.level = -1;
        nodes = new ArrayList();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public Field getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return name;
    }

    public void incrementLevel(int amount) {
        this.level += amount;
    }

    public void addChild(Field n) {
        Field parentNode = n;
        for (; parentNode != null; parentNode = parentNode.parent) {
            if (hashCode() == parentNode.hashCode()) {
                throw new IllegalArgumentException("Cannot add child to parent!");
            }
        }
        if (this.hashCode() == n.hashCode()) {
            throw new IllegalArgumentException("Cannot add child to parent!");
        }

        if (nodes.contains(n)) {
            throw new IllegalArgumentException("Already contains " + n.name);
        }
        //Then add
        n.parent = this;
        nodes.add(n);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Field getNode(String name) {
        return findNode(new Field(name));
    }

    public Field findNode(Field node) {
        if (this.name.toLowerCase().equals(node.name.toLowerCase())) {
            return this;
        }
        for (Field f : nodes) {
            if (f.name.toLowerCase().equals(node.name.toLowerCase())) {
                return f;
            } else {
                //Search children
                Field field = f.findNode(node);
                if (field != null) {
                    return field;
                }
            }
        }
        return null;
    }

    public int getChildCount() {
        return (nodes.size());
    }

    @Override
    public boolean equals(Object obj) {
        return (((Field) obj).name.equals(this.name));
    }

    public Field getNode(int i) {
        return (nodes.get(i));
    }
}
