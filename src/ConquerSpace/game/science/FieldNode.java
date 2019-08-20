package ConquerSpace.game.science;

import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class FieldNode {

    private FieldNode parent;
    private ArrayList<FieldNode> fieldNodes;
    private String name;

    public FieldNode(String name) {
        parent = null;
        this.name = name;
        fieldNodes = new ArrayList<>();
    }

    public void addChild(FieldNode n) {
        FieldNode parentNode = n;
        for (; parentNode != null; parentNode = parentNode.parent) {
            if (hashCode() == parentNode.hashCode()) {
                throw new IllegalArgumentException("Cannot add child to parent!");
            }
        }
        if (this.hashCode() == n.hashCode()) {
            throw new IllegalArgumentException("Cannot add child to parent!");
        }

        if (fieldNodes.contains(n)) {
            throw new IllegalArgumentException("Already contains " + n.name);
        }
        //Then add
        n.parent = this;
        fieldNodes.add(n);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public FieldNode getParent() {
        return parent;
    }

    public FieldNode getNode(String name) {
        return fieldNodes.stream().filter(e -> (e.name.equals(name))).findFirst().get();
    }

    public int getChildCount() {
        return (fieldNodes.size());
    }

    @Override
    public boolean equals(Object obj) {
        return (((FieldNode) obj).name.equals(this.name));
    }

    public FieldNode getNode(int i) {
        return (fieldNodes.get(i));
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
    
    public FieldNode findNode(FieldNode node) {
        for (FieldNode f : fieldNodes) {
            if (f.name.equals(node.name)) {
                return f;
            } else {
                //Search children
                FieldNode field = f.findNode(node);
                if(field != null)
                    return field;
                
            }
        }
        return null;
    }
}
