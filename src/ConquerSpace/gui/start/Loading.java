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
package ConquerSpace.gui.start;

import ConquerSpace.util.logging.CQSPLogger;
import com.alee.extended.layout.VerticalFlowLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author EhWhoAmI
 */
public class Loading extends JFrame {
    private static final Logger LOGGER = CQSPLogger.getLogger(Loading.class.getName());
    private JProgressBar jpb;
    private JLabel quoteLabel;

    public Loading() {
        jpb = new JProgressBar();
        jpb.setIndeterminate(true);

        quoteLabel = new JLabel();
        try {
            //Set quote
            List<String> lines = Files.readAllLines(Paths.get(System.getProperty("user.dir") + "/assets/quotelist.txt"));
            Random rand = new Random();
            int index = rand.nextInt(lines.size());
            String quote = lines.get(index);
            quoteLabel.setText(quote);
            LOGGER.info("Quote: " + quote);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        setLayout(new VerticalFlowLayout());
        add(jpb);
        add(quoteLabel);
        pack();
        setVisible(true);
        repaint();
    }
}
