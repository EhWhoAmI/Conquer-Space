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
package ConquerSpace.client.gui.game;

import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.science.Field;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 *
 * @author EhWhoAmI
 */
public class FieldViewer extends JPanel implements MouseMotionListener, MouseListener {

    private Civilization civ;
    double translateX = 0;
    double translateY = 0;
    double scale = 1;
    private boolean isDragging = false;
    private Point startPoint;

    private HashMap<Integer, Integer> colCount = new HashMap<>();

    /*
    Note to future me:
    I want to add colors for the different values, so here is a list of what you can add:
    Ranking in the universe
    Discovered or not, IDK
     */
    public FieldViewer(Civilization civ) {
        this.civ = civ;
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        Graphics2D g2d = (Graphics2D) g;
        //Do fields
        colCount.clear();
        //colCount.add(0);
        doField(g2d, 0, 0, civ.fields, new Point(0, 0));
    }

    private void doField(Graphics2D g2d, int i, int col, Field f, Point previous) {
        int rowHeight = 100;
        int rowLength = 100;
        int blockHeight = 40;
        int blockWidth = 80;
        if (!colCount.containsKey((i))) {
            colCount.put(i, 0);
        } else {
            colCount.put(i, colCount.get(i) + 1);
        }
        col = colCount.get(i);
        //Ignore all root

        //Draw it
        //Draw square
        int height = g2d.getFontMetrics().getHeight();
        Rectangle2D.Double rect2d = new Rectangle2D.Double(rowLength * col + translateX, i * rowHeight + translateY, blockWidth, blockHeight);
        //Background color.
        g2d.setColor(Color.CYAN);
        g2d.fill(rect2d);
        g2d.setColor(Color.BLACK);
        g2d.draw(rect2d);

        g2d.drawString(f.getName(), (float) (5 + translateX + rowLength * col), (float) (i * rowHeight + height + translateY));
        g2d.drawString("Level: " + f.getLevel(), (float) (5 + translateX + rowLength * col), (float) (i * rowHeight + 2 * height + translateY));

        //Draw line
        //g2d.drawLine
        Line2D.Float ln = new Line2D.Float((int) (previous.x + translateX), (int) (previous.y + translateY), (int) (rowLength * col + blockWidth / 2 + translateX), (int) (i * rowHeight + translateY));
        g2d.draw(ln);

        //Do sub stuff
        i++;
        //Add for this row...
        for (int k = 0; k < f.getChildCount(); k++) {
            if (f.getNode(k).getLevel() > -1) {
                doField(g2d, i, k, f.getNode(k), new Point(rowLength * col + blockWidth / 2, (i - 1) * rowHeight + blockHeight));
            }
        }
    }

    public void setTranslateX(double translateX) {
        this.translateX = translateX;
    }

    public void setTranslateY(double translateY) {
        this.translateY = translateY;
    }

    public double getTranslateX() {
        return translateX;
    }

    public double getTranslateY() {
        return translateY;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getScale() {
        return scale;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Started
        isDragging = true;
        startPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragging = false;
        if (SwingUtilities.isRightMouseButton(e)) {
            //Show right click menu
            JPopupMenu popupMenu = new JPopupMenu();
            //Leave dormant for now.
            popupMenu.show(this, e.getX(), e.getY());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isDragging && SwingUtilities.isLeftMouseButton(e)) {
            translateX -= ((startPoint.x - e.getX()) * (scale));
            translateY -= ((startPoint.y - e.getY()) * (scale));
            startPoint = e.getPoint();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }
}
