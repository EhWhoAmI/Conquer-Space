package ConquerSpace.gui.game;

import ConquerSpace.game.universe.Vector;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Universe;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Zyun
 */
public class ShipListManager extends JInternalFrame{

    private String[] colunms = {"Name", "Class", "Location", "Speed", "Status"};
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    private JTable table;
    
    public ShipListManager(Universe u, Civilization c) {        
        setTitle("All Ships");
        model = new DefaultTableModel(colunms, 1);
        
        table = new JTable(model) {
            //Disable cell editing
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        scrollPane = new JScrollPane(table);
        add(scrollPane);
        pack();
        setVisible(true);
        setResizable(true);
    }
    
    public void addRow(String[] col) {
        model.addRow(col);
    }
}
