/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.game.science;

import java.util.ArrayList;

/**
 * Temp thing for the general game.
 *
 * @author EhWhoAmI
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
        if (obj instanceof FieldNode) {
            return (((FieldNode) obj).name.equals(this.name));
        }
        return false;
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
                if (field != null) {
                    return field;
                }

            }
        }
        return null;
    }
}
