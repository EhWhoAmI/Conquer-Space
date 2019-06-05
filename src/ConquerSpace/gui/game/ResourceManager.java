package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.RawResourceTypes;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author
 */
public class ResourceManager extends JInternalFrame {

    private DefaultTableModel resourceTableModel;
    private JTable resourceTable;
    private String[] colunmNames = {"Resource Type", "Amount"};
    private Civilization c;

    public ResourceManager(Civilization c) {
        this.c = c;
        init();
        update();
        setClosable(true);
        setVisible(true);
        setResizable(true);
        //setSize(100, 100);
        pack();
    }

    public void init() {
        resourceTableModel = new DefaultTableModel(colunmNames, 0);
        resourceTable = new JTable(resourceTableModel);

        //Initalize with default values
        for (String s : RawResourceTypes.RESOURCE_NAMES) {
            resourceTableModel.addRow(new String[]{s, "0"});
        }
        JScrollPane pane = new JScrollPane(resourceTable);
        add(pane);
    }

    public void update() {
        //fill table
        for (int i = 0; i < RawResourceTypes.RESOURCE_NAMES.length; i++) {
            resourceTableModel.setValueAt(c.resourceList.get(i), i, 1);
        }
    }
}
