package ConquerSpace.game.ui.renderers;

import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author Zyun
 */
public class SystemDrawStats {

        private Point pos;
        private Color color;
        private int id;
        public SystemDrawStats(Point pos, Color color, int id) {
            this.pos = pos;
            this.color = color;
            this.id = id;
        }

        public Point getPosition() {
            return pos;
        }

        public Color getColor() {
            return color;
        }

    public int getId() {
        return id;
    }
}