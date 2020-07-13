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

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.server.GameController;
import ConquerSpace.common.GameState;
import ConquerSpace.client.gui.encyclopedia.EncyclopediaParagraph;
import ConquerSpace.client.gui.encyclopedia.EncyclopediaSeparator;
import ConquerSpace.common.game.resources.Good;
import ConquerSpace.client.gui.encyclopedia.HtmlEncyclopediaBuilder;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author EhWhoAmI
 */
public class Encyclopedia extends JInternalFrame {

    private JEditorPane pane;
    private JList infoList;

    @SuppressWarnings("unchecked")
    public Encyclopedia(GameState gameState) {
        setTitle(LOCALE_MESSAGES.getMessage("game.encyclopedia.title"));
        //XD
        if ((int) (Math.random() * 42000) == 0) {
            setTitle(LOCALE_MESSAGES.getMessage("game.encyclopedia.backup"));
        }
        
        infoList = new JList(new Vector(gameState.goodHashMap.values()));
        infoList.addListSelectionListener(l -> {
            Good g = (Good) infoList.getSelectedValue();
            HtmlEncyclopediaBuilder builder = new HtmlEncyclopediaBuilder();
            builder.addElement(new EncyclopediaParagraph(g.getName()));
            builder.addElement(new EncyclopediaSeparator());
            builder.addElement(new EncyclopediaParagraph("Density: " + (g.getMass()/g.getVolume()) + " kg/m3"));
            pane.setText(builder.build());
        });

        pane = new JEditorPane("text/html", null);

        pane.setEditable(false);
        
        //Set default stuff
        pane.setBackground(UIManager.getDefaults().getColor("Panel.background"));

        pane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        pane.setFont(UIManager.getDefaults().getFont("Label.font"));

        pane.addHyperlinkListener((s) -> {
            if (s.getInputEvent() instanceof MouseEvent) {
                MouseEvent me = (MouseEvent) s.getInputEvent();
                if (SwingUtilities.isLeftMouseButton(me)) {
                }
            }
        });
        
        setLayout(new BorderLayout());
        add(new JScrollPane(infoList), BorderLayout.WEST);
        add(new JScrollPane(pane), BorderLayout.CENTER);
        
        setSize(350, 600);
        setClosable(true);
        setResizable(true);
    }

}
