package ConquerSpace.game.corp;

import ConquerSpace.game.buildings.City;
import ConquerSpace.game.economy.Currency;
import ConquerSpace.game.population.Employer;
import ConquerSpace.game.universe.spaceObjects.Planet;

/**
 *
 * @author zyunl
 */
public class Company implements Employer{
    private String name;
    private int affiliation;
    private City cityBasedIn;
    private Planet planetBasedIn;

    public Company() {
    }

    public String getName() {
        return name;
    }

    public int getAffiliation() {
        return affiliation;
    }

    public City getCityBasedIn() {
        return cityBasedIn;
    }

    public Planet getPlanetBasedIn() {
        return planetBasedIn;
    }

    public void setAffiliation(int affiliation) {
        this.affiliation = affiliation;
    }

    public void setCityBasedIn(City cityBasedIn) {
        this.cityBasedIn = cityBasedIn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlanetBasedIn(Planet planetBasedIn) {
        this.planetBasedIn = planetBasedIn;
    }

    @Override
    public Currency getCurrency() {
        return null;
    }

    @Override
    public long getMoney() {
        return 0;
    }

    @Override
    public void changeMoney(long amount) {
        //Do nothing!
    }
}
