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
package ConquerSpace.common.game.science;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * for use of civs and stuff like that.
 *
 * @author EhWhoAmI
 */
public class Field implements Serializable{

    private String name;
    private double level;
    private Field parent;
    private ArrayList<Field> nodes;

    public Field(String name) {
        this.name = name;
        //-1 is undiscovered, 0 is possible, anything more than that is it exists.
        this.level = -1;
        nodes = new ArrayList<>();
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
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

    public void incrementLevel(double amount) {
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

    public Field findNode(String node) {
        if (this.name.toLowerCase().equals(node.toLowerCase())) {
            return this;
        }
        for (Field f : nodes) {
            if (f.name.toLowerCase().equals(node.toLowerCase())) {
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
        if (obj instanceof Field) {
            return (((Field) obj).name.equals(this.name));
        }
        return false;
    }

    public Field getNode(int i) {
        return (nodes.get(i));
    }

    /**
     * Does include current field
     *
     * @param f
     */
    public void getFields(ArrayList<Field> f) {
        f.add(this);
        for (int i = 0; i < this.getChildCount(); i++) {
            //Sort through children
            Field child = this.getNode(i);
            child.getFields(f);
        }
    }

    /**
     * Does not include this field
     *
     * @param f
     */
    public void getFieldsExclusivse(ArrayList<Field> f) {
        for (int i = 0; i < this.getChildCount(); i++) {
            //Sort through children
            Field child = this.getNode(i);
            child.getFields(f);
        }
    }
}
