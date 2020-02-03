package ConquerSpace.game.buildings;

import ConquerSpace.game.jobs.Job;
import ConquerSpace.game.jobs.Workable;
import java.awt.Color;

/**
 * Admin center. Is a center for admin stuff, and also stores peeps. because it
 * extends pop storage, and it also is used for controlling politics and things
 * like that. Peeps live in them. Can be different between governments.
 *
 * @author
 */
public class AdministrativeCenter extends CityDistrict implements Workable{

    @Override
    public Color getColor() {
        return Color.red;
    }

    @Override
    public void processJob(Job j) {
    }

    @Override
    public String getType() {
        return "Administrative Center";
    }
}
