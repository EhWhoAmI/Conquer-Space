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

package ConquerSpace.server;

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.city.area.AreaDispatcher;
import ConquerSpace.common.game.city.area.CapitolArea;
import ConquerSpace.common.game.city.area.CommercialArea;
import ConquerSpace.common.game.city.area.ConstructingArea;
import ConquerSpace.common.game.city.area.ConsumerArea;
import ConquerSpace.common.game.city.area.CustomComponentFactoryManufacturerArea;
import ConquerSpace.common.game.city.area.FarmFieldArea;
import ConquerSpace.common.game.city.area.InfrastructureArea;
import ConquerSpace.common.game.city.area.LogisticsHubArea;
import ConquerSpace.common.game.city.area.ManufacturerArea;
import ConquerSpace.common.game.city.area.MineArea;
import ConquerSpace.common.game.city.area.ObservatoryArea;
import ConquerSpace.common.game.city.area.PopulationUpkeepArea;
import ConquerSpace.common.game.city.area.PortArea;
import ConquerSpace.common.game.city.area.PowerPlantArea;
import ConquerSpace.common.game.city.area.ResearchArea;
import ConquerSpace.common.game.city.area.ResidentialArea;
import ConquerSpace.common.game.city.area.ResourceStockpileArea;
import ConquerSpace.common.game.city.area.SpacePortArea;
import ConquerSpace.common.game.city.area.TimedManufacturerArea;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.organizations.Organization;
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.game.resources.ResourceTransfer;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.universe.bodies.Planet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author EhWhoAmI
 */
public class AreaBehaviorDispatcher implements AreaDispatcher {

    private GameState gameState;

    private City city;
    private Planet planet;

    public AreaBehaviorDispatcher(GameState gameState, City city, Planet planet) {
        this.gameState = gameState;
        this.city = city;
        this.planet = planet;
    }

    @Override
    public void dispatch(CommercialArea area) {
        //Empty
    }

    @Override
    public void dispatch(ConstructingArea area) {
        //Remove resources
        HashMap<StoreableReference, Double> cost = area.getCostPerTurn();
        for (Map.Entry<StoreableReference, Double> entry : cost.entrySet()) {
            StoreableReference key = entry.getKey();
            Double val = entry.getValue();
            //Drain resources somehow
            ResourceTransfer resourceTransferer = new ResourceTransfer(city, city, key, val);
            resourceTransferer.doTransferResource();
        }
        area.tickConstruction();
    }

    @Override
    public void dispatch(ConsumerArea area) {
        //Empty
    }

    @Override
    public void dispatch(CustomComponentFactoryManufacturerArea area) {
        //Empty
    }

    @Override
    public void dispatch(TimedManufacturerArea area) {
        int removed = area.tick();

        if (areaIsProducing(area) && removed > 0) {
            ProductionProcess factoryProcess = area.getProcess();
            factoryProcess.getOutput().entrySet().forEach(entry -> {
                StoreableReference key = entry.getKey();
                Double val = entry.getValue();

                //Get percentage
                city.getPrimaryProduction().add(key);
                city.getPreviousQuarterProduction().addValue(key, val);
                ResourceTransfer transferer = new ResourceTransfer(area, city, key, val);
                if (transferer.canTransferResources() == ResourceTransfer.ResourceTransferViability.TRANSFER_POSSIBLE) {
                    //Transfer resources
                    transferer.doTransferResource();
                }
            });
        }
    }

    @Override
    public void dispatch(ManufacturerArea area) {
        //Process resources used
        ProductionProcess process = area.getProcess();
        if (areaIsProducing(area)) {
            //Query resources
            process.getInput().entrySet().forEach(entry -> {
                StoreableReference key = entry.getKey();
                Double val = entry.getValue();
                //Transfer resources from city to area
                ResourceTransfer resourceTransferer = new ResourceTransfer(city, area, key, val * area.getProductivity());
                resourceTransferer.doTransferResource();
                //Resource gained by each manufacturer is recipe amount * game refresh rate * productivity.
                city.resourceDemands.addValue(key, val);
            });
            //Check recipes, create resources
            process.getOutput().entrySet().forEach(entry -> {
                StoreableReference key = entry.getKey();
                Double val = entry.getValue();
                city.getPrimaryProduction().add(key);
                city.getPreviousQuarterProduction().addValue(key, val);
                ResourceTransfer resourceTransferer = new ResourceTransfer(area, city, key, area.getResourceAmount(key));
                resourceTransferer.doTransferResource();
            });
            //Gonna produce next tick   
            area.setProducedLastTick(true);
        } else {
            area.setProducedLastTick(false);
        }
    }

    @Override
    public void dispatch(MineArea area) {
        if (areaIsProducing(area)) {
            area.getNecessaryGoods().entrySet().forEach(entry -> {
                StoreableReference key = entry.getKey();
                Double val = entry.getValue();

                ResourceTransfer transferer = new ResourceTransfer(city, area, key, val);
                //Check viability
                transferer.doTransferResource();
            });

            double multiplier = getMultiplier(area);
            city.getPrimaryProduction().add(area.getResourceMinedId());
            city.getPreviousQuarterProduction().addValue(area.getResourceMinedId(), area.getResourceAmount(area.getResourceMinedId()));

            ResourceTransfer transferer = new ResourceTransfer(area, city, area.getResourceMinedId(), area.getResourceAmount(area.getResourceMinedId()));
            transferer.doTransferResource();
        }
    }

    @Override
    public void dispatch(SpacePortArea area) {
        //Empty
    }

    @Override
    public void dispatch(LogisticsHubArea area) {
        //Empty
    }

    @Override
    public void dispatch(ObservatoryArea area) {
        //Empty
    }

    @Override
    public void dispatch(ResourceStockpileArea area) {
        //Empty
    }

    @Override
    public void dispatch(FarmFieldArea area) {
        int removed = area.tick();
        if (removed <= 0 && areaIsProducing(area)) {
            //Calculate percentage
            city.getPrimaryProduction().add(area.getGrown().getFoodGood());
            //storeResource(area.getGrown().getFoodGood(), (area.getProductivity() * (double) area.getFieldSize()), city);
            //Add resources to area, then transfer it to somewhere else
            city.getPreviousQuarterProduction().addValue(area.getGrown().getFoodGood(), (area.getProductivity() * (double) area.getFieldSize()));
            area.grow();
        }
    }

    @Override
    public void dispatch(PowerPlantArea area) {
        if (areaIsProducing(area)) {
            ResourceTransfer transferer = new ResourceTransfer(city, area, area.getUsedResource(), Double.valueOf(area.getMaxVolume()));
            if (transferer.canTransferResources() == ResourceTransfer.ResourceTransferViability.TRANSFER_POSSIBLE) {
                transferer.doTransferResource();
                city.incrementEnergy(area.getProduction());
            }
        }
    }

    @Override
    public void dispatch(ResearchArea area) {
        //Contribute science towards the thing
        Organization org = gameState.getObject(area.getOwner(), Organization.class);

        if (org instanceof Civilization) {
            Civilization civ = (Civilization) org;
            for (Map.Entry<String, Double> entry : area.focusFields.entrySet()) {
                String key = entry.getKey();
                Double val = entry.getValue();

                //Add the science
                //Add random amount of science
                civ.upgradeField(key, val * gameState.getRandom().nextDouble());
            }
        }
    }

    @Override
    public void dispatch(ResidentialArea area) {
        //Empty
    }

    @Override
    public void dispatch(InfrastructureArea area) {
        //Empty
    }

    @Override
    public void dispatch(CapitolArea area) {
        //Empty
    }

    private boolean areaIsProducing(Area area) {
        return area.operatingJobsNeeded() < area.getCurrentlyManningJobs();
    }

    private double getMultiplier(Area area) {
        double extraJobs = area.getCurrentlyManningJobs() - area.operatingJobsNeeded();

        double multiplier = 1;
        if (extraJobs > 0) {
            double maxExtraJobs = area.getMaxJobsProvided() - area.operatingJobsNeeded();

            float workingMultiplier = area.getWorkingmultiplier() - 1f;

            double extraJobsRatio = extraJobs / maxExtraJobs;
            if (extraJobsRatio > 1) {
                extraJobsRatio = 1;
            }
            double multiplyBy = 1d + extraJobsRatio * workingMultiplier;

            multiplier = multiplyBy;
        }
        return multiplier;
    }

    @Override
    public void dispatch(PortArea area) {
        //Do stuff
    }

    @Override
    public void dispatch(PopulationUpkeepArea area) {
        //Empty
    }

    public Planet getPlanet() {
        return planet;
    }

}
