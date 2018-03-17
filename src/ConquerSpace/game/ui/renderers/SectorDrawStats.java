package ConquerSpace.game.ui.renderers;

import java.awt.Point;
import java.util.ArrayList;

/**
 * 
 * @author Zyun
 */
public class SectorDrawStats {

        private Point pos;
        private int radius;
        public ArrayList<SystemDrawStats> systems;

        public SectorDrawStats(Point pos, int circumference) {
            this.pos = pos;
            this.radius = circumference;
            this.systems = new ArrayList<>();
        }

        public int getRadius() {
            return radius;
        }

        public Point getPosition() {
            return pos;
        }

        public void addSystemStats(SystemDrawStats s) {
            systems.add(s);
        }
    }