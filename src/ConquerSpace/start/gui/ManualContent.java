package ConquerSpace.start.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTextPane;

public class ManualContent extends JFrame {

    private static ManualContent instance;
    private JTextPane text;

    public static ManualContent getInstance(String file) {
        if (instance == null) {
            instance = new ManualContent();
        }
        instance.setText(file);
        return instance;
    }

    private ManualContent() {
        setSize(500, 400);
        text = new JTextPane();
        text.setContentType("text/html");
        text.setEditable(false);
        add(text);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent arg0) {
                instance.dispose();
                instance = null;
            }
        });
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void setText(String content) {
        if (text != null) {
            text.setText(content);
        }
    }
}
