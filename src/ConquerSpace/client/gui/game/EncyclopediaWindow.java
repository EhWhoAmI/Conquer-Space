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
import ConquerSpace.client.gui.encyclopedia.EncyclopediaParagraph;
import ConquerSpace.client.gui.encyclopedia.EncyclopediaSeparator;
import ConquerSpace.client.gui.encyclopedia.HtmlEncyclopediaBuilder;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.resources.Good;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 *
 * @author EhWhoAmI
 */
public class EncyclopediaWindow extends JInternalFrame {

    private JEditorPane pane;
    private JList<Good> infoList;

    @SuppressWarnings("unchecked")
    public EncyclopediaWindow(GameState gameState) {
        setTitle(LOCALE_MESSAGES.getMessage("game.encyclopedia.title"));
        //XD
        if ((int) (Math.random() * 42000) == 0) {
            setTitle(LOCALE_MESSAGES.getMessage("game.encyclopedia.backup"));
        }
        
        infoList = new JList(gameState.getGoodList());
        infoList.addListSelectionListener(l -> {
            Good g = (Good) infoList.getSelectedValue();
            HtmlEncyclopediaBuilder builder = new HtmlEncyclopediaBuilder();
            builder.addElement(new EncyclopediaParagraph(g.getName()));
            builder.addElement(new EncyclopediaSeparator());
            builder.addElement(new EncyclopediaParagraph("Density: " + (g.getMass()/g.getVolume()) + " kg/m3"));
            builder.addElement(new EncyclopediaParagraph("Tags: "));
            
            for(String s : g.tags) {
                builder.addElement(new EncyclopediaParagraph(s));
            }
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
