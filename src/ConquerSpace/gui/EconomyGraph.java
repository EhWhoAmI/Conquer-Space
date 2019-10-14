package ConquerSpace.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author zyunl
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
