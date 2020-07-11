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
package ConquerSpace.game.organizations.behavior;

import static ConquerSpace.game.actions.Actions.sendResources;
import ConquerSpace.game.actions.ResourceTransportAction;
import ConquerSpace.game.city.City;
import ConquerSpace.game.organizations.Administrable;
import ConquerSpace.game.organizations.Organization;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author EhWhoAmI
 */
public class ResourceManagerBehavior extends Behavior {

    public ResourceManagerBehavior(Organization org) {
        super(org);
    }

    @Override
    public void doBehavior() {
        //Find stuff
        for (Administrable ad : org.region.bodies) {
            if (ad instanceof City) {
                City c = (City) ad;
                //Resources needed, keep
                HashMap<Integer, Double> resourcesToSpend = new HashMap<>();
                for (Map.Entry<Integer, Double> entry : c.resourceDemands.entrySet()) {
                    Integer key = entry.getKey();
                    Double val = entry.getValue();
                    if (c.resources.containsKey(key)) {
                        double amount = c.resources.get(key) - val;
                        if (amount > 0) {
                            resourcesToSpend.put(key, amount);
                        }
                    }
                }

                //Then, distribute resources
                for (Administrable ad2 : org.region.bodies) {
                    if (ad2 instanceof City && !ad2.equals(c)) {
                        City cit = (City) ad2;
                        //Send the resources to other places
                        for (Map.Entry<Integer, Double> entry : cit.resourceDemands.entrySet()) {
                            Integer key = entry.getKey();
                            Double val = entry.getValue();
                            //If have enough resources to put in
                            if (resourcesToSpend.containsKey(key)) {
                                double toSpendAmount = resourcesToSpend.get(key);
                                if (val > 0 && toSpendAmount > 0) {
                                    if (val > toSpendAmount) {
                                        //Send the resources
                                        ResourceTransportAction act = new ResourceTransportAction(key, toSpendAmount, c, cit);
                                        org.actionList.add(act);
                                        //Subtract resources
                                        resourcesToSpend.put(key, 0d);
                                    } else {
                                        ResourceTransportAction act = new ResourceTransportAction(key, val, c, cit);
                                        org.actionList.add(act);
                                        resourcesToSpend.put(key, (toSpendAmount - val));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //Done
    }
    
}
