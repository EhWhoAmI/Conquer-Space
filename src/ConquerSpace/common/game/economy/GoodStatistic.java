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

package ConquerSpace.common.game.economy;

/**
 *
 * @author EhWhoAmI
 */
public class GoodStatistic {

    private double maximum;
    private double minimum;
    private double q1;
    private double q3;
    private double median;
    private int buyVolume;
    private int sellVolume;

    public GoodStatistic() {
    }

    public double getMaximum() {
        return maximum;
    }

    public double getMedian() {
        return median;
    }

    public double getMinimum() {
        return minimum;
    }

    public double getQ1() {
        return q1;
    }

    public double getQ3() {
        return q3;
    }

    public int getBuyVolume() {
        return buyVolume;
    }

    public int getSellVolume() {
        return sellVolume;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    public void setQ1(double q1) {
        this.q1 = q1;
    }

    public void setQ3(double q3) {
        this.q3 = q3;
    }

    public void setBuyVolume(int buyVolume) {
        this.buyVolume = buyVolume;
    }

    public void setSellVolume(int sellVolume) {
        this.sellVolume = sellVolume;
    }

}
