package ConquerSpace.game;

import java.math.BigInteger;
import java.util.Calendar;

/**
 *
 * @author Zyun
 */
public class StarDate {
    /**
     * Ticker...
     * in hours...
     */
    public BigInteger bigint = BigInteger.valueOf(24L);
    
    public void increment(long ticks) {
        bigint = bigint.add(BigInteger.valueOf(ticks));
    }
    
    @Override
    public String toString() {
        BigInteger days = bigint.divide(BigInteger.valueOf(24L));
        days = days.mod(BigInteger.valueOf(365L));
        
        return null;
    }
    
    public int getDays() {
        BigInteger days = bigint.divide(BigInteger.valueOf(24L));
        days = days.mod(BigInteger.valueOf(365L));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, days.intValue());
        //2018 because it is not a leap year. Also because it is this year.
        cal.set(Calendar.YEAR, 2018);
        
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    
    public int getDaysOfYear() {
        BigInteger days = bigint.divide(BigInteger.valueOf(24L));
        days = days.mod(BigInteger.valueOf(365L));
        return days.intValue();
    }
    
    public int getYears() {
        BigInteger days = bigint.divide(BigInteger.valueOf(24L));
        BigInteger years = days.divide(BigInteger.valueOf(356L));
        return years.intValue();
    }
    
    public int getMonthNumber() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, getDaysOfYear());
        //2018 because it is not a leap year. Also because it is this year.
        cal.set(Calendar.YEAR, 2018);
        return (cal.get(Calendar.MONTH) + 1);
    }
}
