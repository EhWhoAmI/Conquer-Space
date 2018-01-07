/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConquerSpace.game.universe;

/**
 *
 * @author Zyun
 */
public class GalaticLocation {
    private float degrees;
    private int distance;

    public GalaticLocation(float degrees, int distance) {
        this.degrees = degrees;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return("(Degrees: " + degrees + " Distance: " + distance + ")");
    }
}
