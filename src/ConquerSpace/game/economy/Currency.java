package ConquerSpace.game.economy;

import ConquerSpace.game.universe.civilization.Civilization;

/**
 * A nations currency
 *
 * @author zyunl
 */
public class Currency {

    //Amount of isk you print
    private float inflation;

    //Total amount in circulation of that currency, all over the galaxy
    private long inCirculation;
    
    private String name;
    
    private String symbol;
    
    /**
     * The people who print the money...
     */
    private Civilization controller;
   
    public long getInCirculation() {
        return inCirculation;
    }

    public float getInflation() {
        return inflation;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setInCirculation(long inCirculation) {
        this.inCirculation = inCirculation;
    }

    public void setInflation(float inflation) {
        this.inflation = inflation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setController(Civilization controller) {
        this.controller = controller;
    }

    public Civilization getController() {
        return controller;
    }
}
