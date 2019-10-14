package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Universe;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author zyunl
 */
public class EconomyWindow extends JPanel {

    private JTabbedPane tabs;
    private JPanel civilizationEconomy;
    private JPanel galaticEconomy;

    private JLabel civilizationCurrency;
    private JLabel civilizationTradeValue;

    private JPanel civEconomicReservePanel;
    private JLabel civEconomicReserves;
    private JLabel currencyInflationRate;

    private Civilization c;
    private Universe u;

    public EconomyWindow(Civilization c, Universe u) {
        setLayout(new BorderLayout());
        this.c = c;
        this.u = u;
        tabs = new JTabbedPane();
        //Create the components
        civilizationEconomy = new JPanel();
        civilizationEconomy.setLayout(new VerticalFlowLayout());
        //Add the stuff
        civilizationCurrency = new JLabel();
        civilizationTradeValue = new JLabel();

        civEconomicReservePanel = new JPanel();
        civEconomicReservePanel.setLayout(new VerticalFlowLayout());

        civEconomicReservePanel.setBorder(new TitledBorder("Civilization Economic Reserves"));

        civEconomicReserves = new JLabel();
        currencyInflationRate = new JLabel();

        civEconomicReservePanel.add(civEconomicReserves);
        civEconomicReservePanel.add(currencyInflationRate);

        civilizationEconomy.add(civilizationCurrency);
        civilizationEconomy.add(civilizationTradeValue);
        civilizationEconomy.add(civEconomicReservePanel);

        galaticEconomy = new JPanel();
        tabs.add(civilizationEconomy, "Internal Economy");
        tabs.add(galaticEconomy, "Galatic Economy");
        add(tabs, BorderLayout.CENTER);
        update();
    }

    public void update() {
        civilizationCurrency.setText("Civilization currency: " + c.getNationalCurrency().getName());

        civilizationTradeValue.setText("Trade Value: <Not implemented yet>");
        
        civEconomicReserves.setText("Reserves: " + c.getNationalCurrency().getSymbol() + " " + c.getMoneyReserves());
        currencyInflationRate.setText("Inflation rate: " + c.getNationalCurrency().getInflation()*100 + "%");
    }
}
