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
package ConquerSpace.game.population;

import ConquerSpace.game.population.jobs.Job;
import ConquerSpace.game.population.jobs.JobType;

/**
 * one pop unit is about 10 million-ish.
 *
 * @author EhWhoAmI
 */
public class PopulationUnit {

    public byte happiness;
    public Race species;
    public Job job;

    public PopulationUnit(Race species) {
        job = new Job(JobType.Jobless);
        this.species = species;
    }
    
    public Race getSpecies() {
        return species;
    }

    public void setSpecies(Race species) {
        this.species = species;
    }

    public byte getHappiness() {
        return happiness;
    }

    public void setHappiness(byte happiness) {
        this.happiness = happiness;
    }
    
    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
