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
package ConquerSpace.common.actions;

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.resources.ResourceStockpile;

/**
 * Org transports good.
 * @author EhWhoAmI
 */
public class ResourceTransportAction extends OrganizationAction {

    private ResourceStockpile from;
    private ResourceStockpile to;
    private double amount;
    private int good;

    public ResourceTransportAction(int good, double amount, ResourceStockpile from, ResourceStockpile to) {
        this.from = from;
        this.to = to;
        this.good = good;
        this.amount = amount;
    }

    @Override
    public ActionStatus doAction(GameState gameState) {
        //public static boolean sendResources(Integer resourceType, Double amount, int owner, City from, City to) {
        return Actions.sendResources(good, amount, from, to) ? ActionStatus.Success : ActionStatus.Failure;
    }

    @Override
    public String toString() {
        return from.toString() + " to " + to.toString() + " " + good + " " + amount;
    }
}
