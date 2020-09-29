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
package ConquerSpace.common.game.organizations;

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.actions.OrganizationAction;
import ConquerSpace.common.game.organizations.behavior.Behavior;
import ConquerSpace.common.game.organizations.behavior.EmptyBehavior;
import ConquerSpace.common.save.Serialize;
import ConquerSpace.common.save.SerializeClassName;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("organization")
public class Organization extends ConquerSpaceGameObject {

    @Serialize("children")
    private ArrayList<ObjectReference> children;
    public ArrayList<OrganizationAction> actionList;
    public AdministrativeRegion region;

    @Serialize("name")
    protected String name;
    protected ObjectReference behavior;

    public Organization(GameState gameState, String name) {
        super(gameState);
        
        this.name = name;
        children = new ArrayList<>();
        actionList = new ArrayList<>();
        region = new AdministrativeRegion();
        behavior = new EmptyBehavior(gameState, this).getReference();
    }

    public String getName() {
        return name;
    }

    public ObjectReference[] getChildren() {
        return Arrays.copyOf(children.toArray(), children.size(), ObjectReference[].class);
    }

    public int getChildrenCount() {
        return children.size();
    }

    public void addChild(ObjectReference org) {
        children.add(org);
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior.getReference();
    }

    public Behavior getBehavior() {
        return gameState.getObject(behavior, Behavior.class);
    }
    
    public ObjectReference getBehaviorId(){
        return behavior;
    }
}
