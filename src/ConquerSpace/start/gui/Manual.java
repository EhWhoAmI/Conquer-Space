package ConquerSpace.start.gui;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import java.util.logging.Logger;
import java.awt.GridLayout;

import ConquerSpace.util.CQSPLogger;


public class Manual extends JFrame {
	private static Manual current;
	private static final Logger LOGGER = CQSPLogger.getLogger(Manual.class.getName());
	
	
	private Manual () {
		setSize(300, 200);
		setTitle("Manual");
		DefaultListModel model = new DefaultListModel();
		model.addElement("Hello");
		JList list = new JList(model);
		add(list);
	}
	
	public static Manual getInstance () {
		if (current == null) {
			current = new Manual();
		}
		return current;
	}
}