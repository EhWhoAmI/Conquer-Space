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
package ConquerSpace.client.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;

/**
 *
 * @author EhWhoAmI
 */
public class GraphicsUtil {
    public static void paintTextWithOutline(String text, Graphics g, float fontSize, double x, double y) {
        //Empty string
        if(text.trim().isEmpty()) {
            return;
        }
        Color outlineColor = Color.black;
        Color fillColor = Color.white;
        BasicStroke outlineStroke = new BasicStroke(fontSize / 10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;

            // remember original settings
            Color originalColor = g2.getColor();
            Stroke originalStroke = g2.getStroke();
            RenderingHints originalHints = g2.getRenderingHints();
            Font originalFont = g2.getFont();

            g2.setFont(g2.getFont().deriveFont(fontSize));
            // create a glyph vector from your text
            GlyphVector glyphVector = g2.getFont().createGlyphVector(g2.getFontRenderContext(), text);
            // get the shape object
            Shape textShape = glyphVector.getOutline();

            AffineTransform tx = new AffineTransform();

            tx.translate(x, fontSize + y);
            textShape = tx.createTransformedShape(textShape);

            g2.setColor(outlineColor);
            g2.setStroke(outlineStroke);
            g2.draw(textShape); // draw outline

            g2.setColor(fillColor);
            g2.fill(textShape); // fill the shape

            // reset to original settings after painting
            g2.setColor(originalColor);
            g2.setStroke(originalStroke);
            g2.setRenderingHints(originalHints);
            g2.setFont(originalFont);
        }
    }
}
