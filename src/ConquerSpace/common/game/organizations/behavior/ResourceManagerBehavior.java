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
package ConquerSpace.common.game.organizations.behavior;

import ConquerSpace.common.game.organizations.Organization;

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
        for (Integer ad : org.region.bodies) {
            //Administrable ad = gameStaaa
//            if (ad instanceof City) {
//                City fromCity = (City) ad;
//                //Resources needed, keep
//                HashMap<Integer, Double> resourcesToSpend = new HashMap<>();
//                for (Map.Entry<Integer, Double> entry : fromCity.resourceDemands.entrySet()) {
//                    Integer key = entry.getKey();
//                    Double val = entry.getValue();
//                    if (fromCity.resources.containsKey(key)) {
//                        double amount = fromCity.resources.get(key) - val;
//                        if (amount > 0) {
//                            resourcesToSpend.put(key, amount);
//                        }
//                    }
//                }
//
//                //Then, distribute resources
//                for (Administrable ad2 : org.region.bodies) {
//                    if (ad2 instanceof City && !ad2.equals(fromCity)) {
//                        City candidateResourceCity = (City) ad2;
//                        //Send the resources to other places
//                        for (Map.Entry<Integer, Double> entry : candidateResourceCity.resourceDemands.entrySet()) {
//                            Integer key = entry.getKey();
//                            Double val = entry.getValue();
//                            //If have enough resources to put in
//                            if (resourcesToSpend.containsKey(key)) {
//                                double toSpendAmount = resourcesToSpend.get(key);
//                                if (val > 0 && toSpendAmount > 0) {
//                                    if (val > toSpendAmount) {
//                                        //Send the resources
//                                        ResourceTransportAction act = new ResourceTransportAction(key, toSpendAmount, fromCity, candidateResourceCity);
//                                        org.actionList.add(act);
//                                        //Subtract resources
//                                        resourcesToSpend.put(key, 0d);
//                                    } else {
//                                        ResourceTransportAction act = new ResourceTransportAction(key, val, fromCity, candidateResourceCity);
//                                        org.actionList.add(act);
//                                        resourcesToSpend.put(key, (toSpendAmount - val));
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }
        //Done
    }
    
}
