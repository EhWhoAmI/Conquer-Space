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
package ConquerSpace.game.organizations;

import ConquerSpace.game.actions.Action;
import ConquerSpace.game.organizations.behavior.Behavior;
import ConquerSpace.game.organizations.behavior.EmptyBehavior;
import ConquerSpace.game.save.Serialize;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author EhWhoAmI
 */
public class Organization {
    
    @Serialize(key = "children")
    private ArrayList<Integer> children;
    public ArrayList<Action> actionList;
    public AdministrativeRegion region;
    private int id = 0;
    
    @Serialize(key = "name")
    protected String name;
    protected Behavior behavior;

    public Organization(String name) {
        this.name = name;
        children = new ArrayList<>();
        actionList = new ArrayList<>();
        region = new AdministrativeRegion();
        behavior = new EmptyBehavior(this);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
    
    public Integer[] getChildren() {
        return Arrays.copyOf(children.toArray(), children.size(), Integer[].class);
    }
    
    public int getChildrenCount() {
        return children.size(); 
    }
    
    public void addChild(Integer org) {
        children.add(org);
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public void setId(int id) {
        this.id = id;
    }
}
