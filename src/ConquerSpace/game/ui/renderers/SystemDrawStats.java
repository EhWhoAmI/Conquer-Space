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

        public SystemDrawStats(Point pos, Color color) {
            this.pos = pos;
            this.color = color;
        }

        public Point getPos() {
            return pos;
        }

        public Color getColor() {
            return color;
        }
    }