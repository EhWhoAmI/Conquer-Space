package ConquerSpace.game.jobs;

import ConquerSpace.game.economy.Currency;

/**
 *
 * @author zyunl
 */
public interface Employer {

    public Currency getCurrency();
    public long getMoney();
    public void changeMoney(long amount);
}
