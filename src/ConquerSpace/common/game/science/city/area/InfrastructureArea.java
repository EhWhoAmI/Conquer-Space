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
package ConquerSpace.common.game.science.city.area;

import ConquerSpace.common.game.population.jobs.JobType;

/**
 *
 * @author EhWhoAmI
 */
public class InfrastructureArea extends ConsumerArea {

    //The jobs provided
    private int jobsProvided;
    private int effectiveness;

    public int getEffectiveness() {
        return effectiveness;
    }

    public void setEffectiveness(int effectiveness) {
        this.effectiveness = effectiveness;
    }

    public int getJobsProvided() {
        return jobsProvided;
    }

    public void setJobsProvided(int jobsProvided) {
        this.jobsProvided = jobsProvided;
    }

    @Override
    public String toString() {
        return "Infrastructure";
    }

    @Override
    public JobType getJobClassification() {
        return (JobType.Infrastructure);
    }
}
