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
import static ConquerSpace.common.actions.Actions.removeResource;
import static ConquerSpace.common.actions.Actions.storeResource;
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
import ConquerSpace.common.game.resources.StorableReference;
import ConquerSpace.common.game.universe.bodies.Planet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author EhWhoAmI
 */
public class AreaBehaviorDispatcher implements AreaDispatcher {

    private GameState gameState;

    private final int GameRefreshRate;

    private City city;
    private Planet planet;

    public AreaBehaviorDispatcher(GameState gameState, int GameRefreshRate, City city, Planet planet) {
        this.gameState = gameState;
        this.GameRefreshRate = GameRefreshRate;
        this.city = city;
        this.planet = planet;
    }

    @Override
    public void dispatch(CommercialArea area) {

    }

    @Override
    public void dispatch(ConstructingArea area) {
        //Remove resources
        HashMap<StorableReference, Double> cost = area.getCostPerTurn();
        for (Map.Entry<StorableReference, Double> entry : cost.entrySet()) {
            StorableReference key = entry.getKey();
            Double val = entry.getValue();
            removeResource(key, val * GameRefreshRate, city);
        }
        area.tickConstruction(GameRefreshRate);
    }

    @Override
    public void dispatch(ConsumerArea area) {

    }

    @Override
    public void dispatch(CustomComponentFactoryManufacturerArea area) {

    }

    @Override
    public void dispatch(TimedManufacturerArea area) {
        int removed = area.tick(GameRefreshRate);
        ProductionProcess factoryProcess = area.getProcess();
        if (areaIsProducing(area)) {
            factoryProcess.output.entrySet().forEach(entry -> {
                StorableReference key = entry.getKey();
                Double val = entry.getValue();

                //Get percentage
                city.primaryProduction.add(key);
                storeResource(key, val * removed, city);
            });
        }
    }

    @Override
    public void dispatch(ManufacturerArea area) {
        //Process resources used
        ProductionProcess process = area.getProcess();
        if (areaIsProducing(area)) {
            //Query resources
            process.input.entrySet().forEach(entry -> {
                StorableReference key = entry.getKey();
                Double val = entry.getValue();
                Double amountInCity = city.resources.get(key);
                removeResource(key, val * GameRefreshRate * ((ManufacturerArea) area).getProductivity(), city);
                city.resourceDemands.addValue(key, val);
            });

            process.output.entrySet().forEach(entry -> {
                StorableReference key = entry.getKey();
                Double val = entry.getValue();
                city.primaryProduction.add(key);
                storeResource(key, val * GameRefreshRate * ((ManufacturerArea) area).getProductivity(), city);
            });
            ((ManufacturerArea) area).setProducedLastTick(true);
        } else {
            ((ManufacturerArea) area).setProducedLastTick(false);
        }
    }

    @Override
    public void dispatch(MineArea area) {
        if (areaIsProducing(area)) {
            area.getNecessaryGoods().entrySet().forEach(entry -> {
                StorableReference key = entry.getKey();
                Double val = entry.getValue();
                removeResource(key, val * GameRefreshRate, city);
            });

            double multiplier = getMultiplier(area);
            city.primaryProduction.add(area.getResourceMinedId());
            storeResource(area.getResourceMinedId(), Double.valueOf(area.getProductivity() * GameRefreshRate) * multiplier, city);
        }
    }

    @Override
    public void dispatch(SpacePortArea area) {

    }

    @Override
    public void dispatch(LogisticsHubArea area) {

    }

    @Override
    public void dispatch(ObservatoryArea area) {

    }

    @Override
    public void dispatch(ResourceStockpileArea area) {

    }

    @Override
    public void dispatch(FarmFieldArea area) {
        int removed = area.tick(GameRefreshRate);
        if (removed > 0 && areaIsProducing(area)) {
            //Calculate percentage
             city.primaryProduction.add(area.getGrown().getFoodGood());
            storeResource(area.getGrown().getFoodGood(), (removed * (double) area.getFieldSize()), city);

            area.grow();
        }
    }

    @Override
    public void dispatch(PowerPlantArea area) {
        if (areaIsProducing(area)) {
            if (removeResource(area.getUsedResource(), Double.valueOf(area.getMaxVolume()), city)) {
                //Add energy to the city
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

    }

    @Override
    public void dispatch(InfrastructureArea area) {

    }

    @Override
    public void dispatch(CapitolArea area) {

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

    }
}
