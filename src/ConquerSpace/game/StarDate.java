package ConquerSpace.game;

import java.math.BigInteger;
import java.util.Calendar;

/**
 *
 * @author Zyun
 */
public class StarDate {
    /**
     * Ticker is in hours.
     * Is initalized to 24 for day 1.
     */
    public BigInteger bigint = BigInteger.valueOf(24L);
    
    /**
     * Increment the ticker by <code>ticks</code> amount.
     * @param ticks Amount to increment.
     */
    public void increment(long ticks) {
        bigint = bigint.add(BigInteger.valueOf(ticks));
    }
    
    /**
     * Just don't use this
     * @deprecated 
     * @return Null
     */
    @Override
    public String toString() {
        BigInteger days = bigint.divide(BigInteger.valueOf(24L));
        days = days.mod(BigInteger.valueOf(365L));
        
        return null;
    }
    
    /**
     * Get number of days from start of month.
     * @return Days from start of month.
     */
    public int getDays() {
        BigInteger days = bigint.divide(BigInteger.valueOf(24L));
        days = days.mod(BigInteger.valueOf(365L));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, days.intValue());
        //2018 because it is not a leap year. Also because it is this year.
        cal.set(Calendar.YEAR, 2018);
        
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
     * Get days from start of year.
     * @return Days from start of year.
     */
    public int getDaysOfYear() {
        BigInteger days = bigint.divide(BigInteger.valueOf(24L));
        days = days.mod(BigInteger.valueOf(365L));
        return days.intValue();
    }
    
    /**
     * Get years from Star Date 1.
     * @return Years from star date 1
     */
    public int getYears() {
        BigInteger days = bigint.divide(BigInteger.valueOf(24L));
        BigInteger years = days.divide(BigInteger.valueOf(356L));
        return years.intValue();
    }
    
    /**
     * Get the month number. You know, 1 is January, 2 is February, etc.
     * @return Month number of this year.
     */
    public int getMonthNumber() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, getDaysOfYear());
        //2018 because it is not a leap year. Also because it is this year.
        cal.set(Calendar.YEAR, 2018);
        return (cal.get(Calendar.MONTH) + 1);
    }
}
