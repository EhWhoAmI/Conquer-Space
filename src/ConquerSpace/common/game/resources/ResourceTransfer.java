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

package ConquerSpace.common.game.resources;

/**
 * This is in charge of transferring a resource from A to B.
 *
 * @author EhWhoAmI
 */
public class ResourceTransfer {

    private ResourceStockpile from;
    private ResourceStockpile to;
    private StoreableReference what;
    private Double amount;

    private ResourceTransferType transferType;

    public ResourceTransfer() {
        from = null;
        to = null;
        what = null;
        amount = 0d;

        transferType = ResourceTransferType.DENY_IF_INSUFFICIENT;
    }

    public ResourceTransfer(ResourceStockpile from, ResourceStockpile to, StoreableReference what, Double amount) {
        this.from = from;
        this.to = to;
        this.what = what;
        this.amount = amount;
    }

    public ResourceTransferViability canTransferResources() {
        if (from == null || to == null || what == null || amount <= 0) {
            return ResourceTransferViability.NO_RESOURCES;
        }
        if (from.hasResource(what)) {
            //Check if it's enough
            return (from.getResourceAmount(what) >= amount) ? ResourceTransferViability.TRANSFER_POSSIBLE : ResourceTransferViability.INSUFFICIENT_RESOURCES;
        }
        return ResourceTransferViability.NO_RESOURCES;
    }

    public ResourceTransferViability doTransferResource() {
        if (from == null || to == null || what == null || amount <= 0
                || (transferType == ResourceTransferType.DENY_IF_INSUFFICIENT && from.getResourceAmount(what) < amount)) {
            return ResourceTransferViability.NO_RESOURCES;
        }
        if (from.hasResource(what)) {
            //Check if it's enough to transfer            
            if (from.getResourceAmount(what) >= amount) {
                from.removeResource(what, amount);
                to.addResource(what, amount);

            } else {
                //Not enough resources
                double resources = from.getResourceAmount(what);
                from.removeResource(what, resources);
                from.addResource(what, resources);
            }
            return ResourceTransferViability.TRANSFER_POSSIBLE;
        }
        return ResourceTransferViability.NO_RESOURCES;
    }

    /**
     * Get amount of resources left needed to get to satisfy the order after these resources are
     * transferred.
     *
     * @return
     */
    public double getResourcesLeft() {
        if (!from.hasResource(what)) {
            return amount;
        }

        if (from.getResourceAmount(what) < amount) {
            return amount - from.getResourceAmount(what);
        }

        return 0;
    }

    public void setTransferType(ResourceTransferType transferType) {
        this.transferType = transferType;
    }

    public static enum ResourceTransferViability {
        /**
         * Look somewhere else.
         */
        NO_RESOURCES,
        /**
         * Look somewhere else too.
         */
        NO_PERMISSIONS,
        /**
         * Not enough resources to transfer, but still allows part of the demand to be transferred.
         */
        INSUFFICIENT_RESOURCES,
        /**
         * 100% possible, you can transfer
         */
        TRANSFER_POSSIBLE;
    }

    public static enum ResourceTransferType {
        ALLOW_IF_INSUFFICIENT,
        DENY_IF_INSUFFICIENT;
    }

    /**
     * @return the from
     */
    public ResourceStockpile getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(ResourceStockpile from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public ResourceStockpile getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(ResourceStockpile to) {
        this.to = to;
    }

    /**
     * @return the what
     */
    public StoreableReference getWhat() {
        return what;
    }

    /**
     * @param what the what to set
     */
    public void setWhat(StoreableReference what) {
        this.what = what;
    }

    /**
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
