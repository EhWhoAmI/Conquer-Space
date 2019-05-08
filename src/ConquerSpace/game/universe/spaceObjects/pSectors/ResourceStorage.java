package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.game.universe.resources.RawResourceTypes;
import ConquerSpace.game.universe.resources.ResourceStockpile;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author EhWhoAmI
 */
public class ResourceStorage extends PlanetSector implements ResourceStockpile {

    private int upkeep;

    private HashMap<Integer, Integer> resources;

    public ResourceStorage() {
        resources = new HashMap<>();
        upkeep = 0;
    }

    @Override
    public void addResourceTypeStore(int type) {
        resources.put(type, 0);
    }

    public boolean getHasResource(int type) {
        return resources.containsKey(type);
    }

    @Override
    public int getResourceAmount(int type) {
        return resources.get(type);
    }

    @Override
    public void addResource(int type, int amount) {
        resources.put(type, resources.get(type) + amount);
    }

    public void setUpkeep(int upkeep) {
        this.upkeep = upkeep;
    }

    public int getUpkeep() {
        return upkeep;
    }

    @Override
    public JPanel getInfoPanel() {
        JPanel root = new JPanel();
        //Add tables and stuff to show the amount of resources
        String[] colunmNames = {"Resource", "Amount"};
        DefaultTableModel model = new DefaultTableModel(colunmNames, 0);
        JTable table = new JTable(model) {
            //Disable cell editing
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //Add the things
        //model.addRow({});
        for (Map.Entry<Integer, Integer> resource : resources.entrySet()) {
            Object key = resource.getKey();
            Object value = resource.getValue();
            String resourceName = "";
            switch ((Integer) key) {
                case RawResourceTypes.GAS:
                    resourceName = "Gas";
                    break;
                case RawResourceTypes.ROCK:
                    resourceName = "Rock";
                    break;
                case RawResourceTypes.METAL:
                    resourceName = "Metal";
                    break;
                case RawResourceTypes.FOOD:
                    resourceName = "Food";
                    break;
                case RawResourceTypes.ENERGY:
                    resourceName = "Energy";
                    break;
            }
            model.addRow(new Object[]{resourceName, value});
        }
        JScrollPane scrollPane = new JScrollPane(table);

        root.add(scrollPane);
        return root;
    }
}
