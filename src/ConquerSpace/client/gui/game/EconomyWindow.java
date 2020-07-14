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
package ConquerSpace.client.gui.game;

import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author EhWhoAmI
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
    private Galaxy u;

    public EconomyWindow(Civilization c, Galaxy u) {
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

        civEconomicReserves.setText("Reserves: " + c.getNationalCurrency().getSymbol() + " " + c.getMoneyReserves() + " million ");
        currencyInflationRate.setText("Inflation rate: " + c.getNationalCurrency().getInflation() * 100 + "%");
    }
}
