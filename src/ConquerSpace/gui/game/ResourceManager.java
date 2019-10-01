package ConquerSpace.gui.game;

import ConquerSpace.game.GameController;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.Resource;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author
 */
public class ResourceManager extends JPanel {

    private DefaultTableModel resourceTableModel;
    private JTable resourceTable;
    private String[] colunmNames = {"Resource Type", "Amount"};
    private Civilization c;

    public ResourceManager(Civilization c) {
        this.c = c;
        init();
        update();
        //setClosable(true);
        setVisible(true);
        //setResizable(true);
        //setSize(100, 100);
        //pack();
    }

    public void init() {
        resourceTableModel = new DefaultTableModel(colunmNames, 0);
        resourceTable = new JTable(resourceTableModel);

        //Initalize with default values
        for (Resource s : GameController.resources) {
            resourceTableModel.addRow(new String[]{s.toString(), "0"});
        }
        JScrollPane pane = new JScrollPane(resourceTable);
        add(pane);
    }

    public void update() {
        //fill table
        
        int x = 0;
        for (Resource s : GameController.resources) {
            resourceTableModel.setValueAt(c.resourceList.get(s), x, 1);
            System.out.println(c.resourceList.get(s));
            x++;
        }
    }
}