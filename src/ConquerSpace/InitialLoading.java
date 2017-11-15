package ConquerSpace;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author Zyun
 */
public class InitialLoading extends JFrame{
    private Runnable r;
    private JProgressBar progressBar;
    private JLabel text;
    
    public InitialLoading(Runnable r){
        this.r = r;
        setTitle("Conquer Space");
        setLayout(new GridLayout(2, 1, 10, 10));
        
        text = new JLabel("Loading...");
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        add(text);
        add(progressBar);
        pack();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    
    public void run() {
        r.run();
    }
}
