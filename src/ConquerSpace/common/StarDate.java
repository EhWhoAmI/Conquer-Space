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
package ConquerSpace.common;

import ConquerSpace.common.save.Serialize;
import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author EhWhoAmI
 */
public class StarDate implements Serializable{

    /**
     * Ticker is in hours.
     */
    @Serialize("value")
    private long date;

    public StarDate() {
        date = 1L;
    }

    public StarDate(long date) {
        this.date = date;
    }

    public StarDate(StarDate date) {
        this.date = date.date;
    }

    /**
     * Increment the ticker by <code>ticks</code> amount.
     * Can only increment
     * @param ticks Amount to increment.
     */
    public void increment(long ticks) {
        if (ticks > 0) {
            date += ticks;
        }
    }

    /**
     * Get number of days from start of month.
     *
     * @return Days from start of month.
     */
    public int getDays() {
        long days = date / 24;
        days = days % 365L;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, (int) days);
        //2018 because it is not a leap year. Also because it is this year.
        cal.set(Calendar.YEAR, 2018);

        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get days from start of year.
     *
     * @return Days from start of year.
     */
    public int getDaysOfYear() {
        long days = date / 24L;
        days = days % 365L;
        return (int) days;
    }

    /**
     * Get years from Star Date 1.
     *
     * @return Years from star date 1
     */
    public int getYears() {
        long days = date / 24L;
        long years = days / 356L;
        return (int) years;
    }

    /**
     * Get the month number. You know, 1 is January, 2 is February, etc.
     *
     * @return Month number of this year.
     */
    public int getMonthNumber() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, getDaysOfYear());
        //2018 because it is not a leap year. Also because it is this year.
        cal.set(Calendar.YEAR, 2018);
        return (cal.get(Calendar.MONTH) + 1);
    }

    public long getDate() {
        return date;
    }
}
