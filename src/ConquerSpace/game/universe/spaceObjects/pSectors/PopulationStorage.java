package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.game.buildings.Buildable;
import ConquerSpace.game.universe.civilization.stats.Economy;
import ConquerSpace.game.universe.civilization.stats.Population;
import java.util.Random;

/**
 * AKA apartments
 *
 * @author Zyun
 */
public class PopulationStorage extends PlanetSector  implements Buildable{

    private long maxStorage;
    private long currentStorage;
    private byte happiness;
    public Population pop;

    public PopulationStorage(long maxStorage, long currentStorage, byte happiness) {
        this.maxStorage = maxStorage;
        this.currentStorage = currentStorage;
        this.happiness = happiness;
        pop = new Population();
        economy = new Economy();
    }
    
    /**
     * Compatability purposes
     * @param maxStorage
     * @param currentStorage
     * @param happiness 
     */
    public PopulationStorage(Long maxStorage, Long currentStorage, Byte happiness) {
        this.maxStorage = maxStorage;
        this.currentStorage = currentStorage;
        this.happiness = happiness;
        pop = new Population();
        economy = new Economy();
    }


    public byte getHappiness() {
        return happiness;
    }

    public long getMaxPopulation() {
        return maxStorage;
    }

    public long getCurrentPopulation() {
        return currentStorage;
    }

    public void setCurrentPopulation(int pop) {
        currentStorage = pop;
    }

    public void setHappiness(byte happiness) {
        this.happiness = happiness;
    }

    @Override
    public void processTurn() {
//        if (turn == 0) {
//            //Just push back default values for all that.
//            pop.population.add((Long) currentStorage);
//            pop.mortalityRate.add((9f / 100f));
//            pop.birthsPer1k.add((19f / 100f));
//            //This is 0 because there is no previous population
//            
//            pop.populationGrowth.add((float) (0));
//            //100% happy for now
//            pop.happiness.add((float) 100);
//            return;
//        }
        //Increase and subtract population
        //According to: https://en.wikipedia.org/wiki/Birth_rate, we will use the world average of 19 in the years 2010 - 2015
        //And according to https://en.wikipedia.org/wiki/Mortality_rate we will use 9.

        //Using those values, we will do a little math and make it change about 10%.
//        float birthRate = 19;
//        float deathRate = 9;
//        Random rand = new Random();
//        int alterB = rand.nextInt((10 - (-10)) + 1);
//        int alterD = rand.nextInt((10 - (-10)) + 1);
//        birthRate = birthRate + (birthRate * (alterB / 100));
//        deathRate = deathRate + (deathRate * (alterD / 100));
//        int toAdd = Math.round((birthRate / 1000) * currentStorage);
//        int toSubtract = Math.round((deathRate / 1000) * currentStorage);
//        long pastPop = currentStorage;
//
//        //Add and subtract population
//        currentStorage += toAdd;
//        currentStorage -= toSubtract;
//
//        //Add all the thingies
//        pop.population.add((Long) currentStorage);
//        pop.mortalityRate.add(deathRate);
//        pop.birthsPer1k.add(birthRate);
//
//        //Calculate population change
//        //Equation:
//        //(Vpr - Vpa)
//        // ---------  * 100
//        //  Vpa
//        //Where Vpr is present value
//        //Vpa is past value
//        float popChange = (((currentStorage - pastPop) / pastPop) * 100);
//        pop.populationGrowth.add(popChange);
//
//        //100% happy for now
//        pop.happiness.add((float) 100);
    }
}
