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
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.CityType;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.city.area.AreaClassification;
import ConquerSpace.common.game.city.area.ConstructingArea;
import ConquerSpace.common.game.city.modifier.CityModifier;
import ConquerSpace.common.game.city.modifier.RiotModifier;
import ConquerSpace.common.game.city.modifier.StarvationModifier;
import ConquerSpace.common.game.city.modifier.UnemployedModifier;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.population.Population;
import ConquerSpace.common.game.population.PopulationSegment;
import ConquerSpace.common.game.population.Race;
import ConquerSpace.common.game.resources.ResourceTransfer;
import ConquerSpace.common.game.universe.bodies.Planet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author EhWhoAmI
 */
public class CityProcessor {

    private City city;
    private Planet planet;
    private GameState gameState;

    private int GameRefreshRate;

    public CityProcessor(City city, Planet planet, GameState gameState, int GameRefreshRate) {
        this.city = city;
        this.planet = planet;
        this.gameState = gameState;
        this.GameRefreshRate = GameRefreshRate;
    }

    public void process() {
        city.clearLedgers();

        city.resourceDemands.clear();

        //Clear energy use
        city.setEnergyProvided(0);

        //Process areas, add all resources...
        for (ObjectReference areaId : city.getAreas()) {
            Area area = gameState.getObject(areaId, Area.class);
            //if not owned, becomes owned by the planet owner
            if (area.getOwner() == ObjectReference.INVALID_REFERENCE) {
                area.setOwner(city.getOwner());
            }

            area.accept(new AreaBehaviorDispatcher(gameState, city, planet));
        }

        processConstruction();

        //Process population events
        processPopulationEvents();

        allocateCityJobs();

        classifyCity();
    }

    private boolean doSegmentConsume(PopulationSegment seg) {
        long amount = (seg.size / 1000);
        //Each person eats about 250 kg of food a year , so 
        double consume = ((double) amount) * 4d;

        double foodAmount = 0;

        Race race = gameState.getObject(seg.species, Race.class);
        //Race race = city.resources.containsKey(gameState.getObject(seg.species, Race.class);
        if (city.getResources().containsKey(race.getConsumableResource())) {
            foodAmount = city.getResourceAmount(race.getConsumableResource());
        }

        //Add resource demand        
        city.resourceDemands.addValue(race.getConsumableResource(), consume);
        seg.upkeep.addValue(race.getConsumableResource(), consume);

        //Black hole resources, somehow
        ResourceTransfer raceEater = new ResourceTransfer(city, seg, race.getConsumableResource(), consume);
        boolean success = raceEater.doTransferResource() == ResourceTransfer.ResourceTransferViability.TRANSFER_POSSIBLE;
        //Not enough food
        boolean starving = false;
        if (!success) {
            //can probably calculate other stuff, but who cares for now
            //Calculate ratio of food
            double populationConsumption = foodAmount * 2;
            double percentage = (populationConsumption / consume);
            percentage = 1d - percentage;
            if (!city.getCityModifiers().contains(CityModifier.CityModifierEnum.STARVATION_MODIFIER)) {
                city.getCityModifiers().add(new StarvationModifier(gameState.date));
            }
            starving = true;
        } else {
            city.getCityModifiers().remove(CityModifier.CityModifierEnum.STARVATION_MODIFIER);
        }
        return starving;
    }

    private void processPopulationEvents() {
        //Process population upkeep
        Population pop = gameState.getObject(city.getPopulation(), Population.class);

        for (ObjectReference segid : pop.segments) {
            PopulationSegment seg = gameState.getObject(segid, PopulationSegment.class);
            //Request resources
            boolean starving = doSegmentConsume(seg);

            //Process riots for starvation
            if (city.getCityModifiers().contains(CityModifier.CityModifierEnum.STARVATION_MODIFIER)) {
                StarvationModifier modifier = (StarvationModifier) city.getCityModifiers().get(city.getCityModifiers().indexOf(CityModifier.CityModifierEnum.STARVATION_MODIFIER));
                if ((gameState.date.getDate() - modifier.getStartDate().getDate()) > 1000) {
                    RiotModifier riotModifier = new RiotModifier(gameState.date);
                    if (!city.getCityModifiers().contains(riotModifier)) {
                        city.getCityModifiers().add(riotModifier);
                    }
                }
            }

            //Get population wealth, however that is defined
            //Increment population
            double fraction = ((double) GameRefreshRate) / 10000d;

            //if starving, then don't grow population
            if (!starving) {
                seg.size = (long) ((double) seg.size * ((1 + seg.populationIncrease * fraction)));
            }
        }

        //Calculate unemployment rate
        double unemploymentRate = city.getUnemploymentRate();
        if (unemploymentRate > 0.1d) {
            if (!city.getCityModifiers().contains(CityModifier.CityModifierEnum.UNEMPLOYED_MODIFIER)) {
                city.getCityModifiers().add(new UnemployedModifier(gameState.date));
            }
        } else {
            city.getCityModifiers().remove(CityModifier.CityModifierEnum.UNEMPLOYED_MODIFIER);
        }

        //if unemployed for a long time, complain
        if (city.getCityModifiers().contains(CityModifier.CityModifierEnum.UNEMPLOYED_MODIFIER)) {
            UnemployedModifier modifier = (UnemployedModifier) city.getCityModifiers().get(city.getCityModifiers().indexOf(CityModifier.CityModifierEnum.UNEMPLOYED_MODIFIER));
            if (gameState.date.getDate() - modifier.getStartDate().getDate() > 1000) {
                RiotModifier riotModifier = new RiotModifier(gameState.date);
                if (!city.getCityModifiers().contains(riotModifier)) {
                    city.getCityModifiers().add(riotModifier);
                }
            }
        }

        //Tax, the tax is paid to population
        long popSize = pop.getPopulationSize();
        long tax = popSize * 5;

        Object obj = gameState.getObject(city.getOwner());
        if (obj instanceof Civilization) {
            ((Civilization) obj).changeMoney(tax);
        }
    }

    private void allocateCityJobs() {
        long maxJobsProviding = 0;
        long necessaryJobsProviding = 0;
        long population = gameState.getObject(city.getPopulation(), Population.class).getPopulationSize();

        //Sort them out based off piority
        //Get the area list and sort
        ArrayList<Area> areaTemp = new ArrayList<>();
        for (ObjectReference areaReference : city.getAreas()) {
            Area a = gameState.getObject(areaReference, Area.class);
            areaTemp.add(a);
        }
        Collections.sort(areaTemp);

        //Get all the total jobs
        for (Area area : areaTemp) {
            necessaryJobsProviding += area.operatingJobsNeeded();
            maxJobsProviding += area.getMaxJobsProvided();
            area.setProducedLastTick(false);
        }

        if (maxJobsProviding < population) {
            //Fill necessary jobs if there are not enough people to get the max amount of people
            for (Area area : areaTemp) {
                area.setCurrentlyManningJobs(area.getMaxJobsProvided());
                area.setProducedLastTick(true);
            }
        } else if (necessaryJobsProviding < population) {
            //Fill all the jobs needed to operate
            for (Area area : areaTemp) {
                population -= area.operatingJobsNeeded();
                area.setCurrentlyManningJobs(area.operatingJobsNeeded());
                area.setProducedLastTick(true);
            }

            //Go through again, and add the jobs
            for (Area area : areaTemp) {
                int toFill = area.getMaxJobsProvided() - area.operatingJobsNeeded();

                if ((population - toFill) > 0) {
                    area.setCurrentlyManningJobs(area.getMaxJobsProvided());
                } else {
                    area.setCurrentlyManningJobs((area.operatingJobsNeeded() + (int) population));
                    break;
                }
            }
        } else {
            //Not enough jobs, so fill stuff according to piority
            for (Area area : areaTemp) {
                int jobsToAdd = area.operatingJobsNeeded();
                if ((population - jobsToAdd) > 0) {
                    population -= jobsToAdd;
                    area.setCurrentlyManningJobs(jobsToAdd);
                    area.setProducedLastTick(true);
                } else {
                    area.setCurrentlyManningJobs((int) population);
                    break;
                }
            }
        }
    }

    private void classifyCity() {
        //Get the type of areas
        HashMap<AreaClassification, Integer> areaType = new HashMap<>();
        for (ObjectReference areaId : city.getAreas()) {
            Area a = gameState.getObject(areaId, Area.class);
            if (areaType.containsKey(a.getAreaType())) {
                Integer num = areaType.get(a.getAreaType());
                num++;
                areaType.put(a.getAreaType(), num);
            } else {
                areaType.put(a.getAreaType(), 1);
            }
        }

        //Calulate stuff
        int highest = 0;
        AreaClassification highestArea = AreaClassification.Generic;
        for (Map.Entry<AreaClassification, Integer> entry : areaType.entrySet()) {
            AreaClassification key = entry.getKey();
            Integer val = entry.getValue();
            if (val > highest && key != AreaClassification.Generic) {
                highest = val;
                highestArea = key;
            }
        }
        CityType cityType;
        switch (highestArea) {
            case Financial:
                cityType = CityType.City;
            case Generic:
                cityType = CityType.Generic;
            case Infrastructure:
                cityType = CityType.Infrastructure;
            case Research:
                cityType = CityType.Research;
            case Residential:
                cityType = CityType.City;
            case Manufacturing:
                //City because it represents it better
                cityType = CityType.City;
            case Farm:
                cityType = CityType.Farm;
            case Mine:
                cityType = CityType.Mine;
        }
        cityType = CityType.Generic;

        city.setCityType(cityType);
    }

    private void processConstruction() {
        //Replace constructing areas
        Iterator<ObjectReference> areaIterator = city.getAreas().iterator();
        ArrayList<ObjectReference> areasToAdd = new ArrayList<>();
        while (areaIterator.hasNext()) {
            Area area = gameState.getObject(areaIterator.next(), Area.class);

            if (area instanceof ConstructingArea) {
                ConstructingArea constructingArea = ((ConstructingArea) area);
                if (constructingArea.getConstructionTimeLeft() <= 0) {
                    areasToAdd.add(constructingArea.getToBuild());
                    areaIterator.remove();
                }
            }
        }

        city.getAreas().addAll(areasToAdd);

    }

}
