package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceVein;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.pSectors.PlanetSector;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Zyun
 */
public class PlanetResources extends JPanel {

    //JList<String> resourceList;
    private Planet p;

    public PlanetResources(Planet p) {
        this.p = p;
        ResourceWindow win = new ResourceWindow();
        add(win);
    }

    private class ResourceWindow extends JPanel {

        private Image planetImage;

        public ResourceWindow() {

            BufferedImage bimg = new BufferedImage(p.terrain.terrainColor.length, p.terrain.terrainColor[0].length, BufferedImage.TYPE_3BYTE_BGR);

            for (int x = 0; x < p.terrain.terrainColor.length; x++) {
                for (int y = 0; y < p.terrain.terrainColor[x].length; y++) {
                    //if(p.terrain.terrainColor[x][y])
                    bimg.setRGB(x, y, p.terrain.terrainColor[x][y].color.getRGB());
                }
            }
            planetImage = bimg.getScaledInstance(p.terrain.terrainColor.length * 2, p.terrain.terrainColor[0].length * 2, Image.SCALE_DEFAULT);
            setPreferredSize(new Dimension(p.terrain.terrainColor.length * 2, p.terrain.terrainColor[0].length * 2));
        }

        //Show the image with some alpha
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            //Render the various resource veins
            for(ResourceVein v : p.resourceVeins) {
                Ellipse2D.Float circe = new Ellipse2D.Float(v.getX(), v.getY(), v.getRadius()*2, v.getRadius()*2);
                g2d.fill(circe);
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            //Draw Image
            g2d.drawImage(planetImage, 0, 0, null);
        }

    }
}
