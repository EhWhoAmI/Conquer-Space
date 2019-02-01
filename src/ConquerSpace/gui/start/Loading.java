package ConquerSpace.gui.start;

import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author Zyun
 */
public class Loading extends JFrame {

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
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Loading.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Loading.class.getName()).log(Level.SEVERE, null, ex);
        }
        setLayout(new VerticalFlowLayout());
        add(jpb);
        add(quoteLabel);
        pack();
        setVisible(true);
        
    }
}
