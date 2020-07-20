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
package ConquerSpace.client.gui.encyclopedia;

import java.util.ArrayList;

/**
 *
 * @author EhWhoAmI
 */
public class HtmlEncyclopediaBuilder implements EncyclopediaBuilder {

    ArrayList<EncyclopediaElement> encyclopediaElements;

    public HtmlEncyclopediaBuilder() {
        encyclopediaElements = new ArrayList<>();
    }

    @Override
    public void addElement(EncyclopediaElement e) {
        encyclopediaElements.add(e);
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        
        for(EncyclopediaElement element : encyclopediaElements){
            if(element instanceof EncyclopediaParagraph) {
                EncyclopediaParagraph paragraph = (EncyclopediaParagraph) element;
                builder.append("<p>");
                builder.append(paragraph.getText());
                builder.append("</p><br/>");
            } else if (element instanceof EncyclopediaSeparator) {
                builder.append("<hr/>");
            }
        }
        builder.append("</html>");
        return builder.toString();
    }
}
