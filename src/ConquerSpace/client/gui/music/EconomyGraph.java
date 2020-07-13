/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.client.gui.music;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author EhWhoAmI
 */
public class EconomyGraph extends JPanel {

    private ArrayList<Long> values;
    private float scale;
    private int height = 100;
    //Draw it

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int i = 0;
        float xPrevious = 0;
        float yPrevious = 0;
        for (Long value : values) {
            int xPosition = i*10;
            int yPosition = height - value.intValue();
            g2d.drawOval(xPosition-5/2, yPosition-5/2, 5, 5);
            Line2D.Float line = new Line2D.Float(xPosition, yPosition, xPrevious, yPrevious);
            xPrevious = xPosition;
            yPrevious = yPosition;
            g2d.draw(line);
            i++;
        }
    }

    public EconomyGraph() {
        values = new ArrayList<>();
    }

    public void addValue(long value) {
        values.add(value);
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
