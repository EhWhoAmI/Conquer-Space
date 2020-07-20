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
package ConquerSpace.common.game.science;

import ConquerSpace.common.util.ResourceLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;

/**
 *
 * @author EhWhoAmI
 */
public class Fields implements Serializable{

    public static FieldNode fieldNodeRoot;

    private Fields() {

    }

    public static FieldNode readFields() {
        try {
            File fieldFile = ResourceLoader.getResourceByFile("text.tech.fields");

            Builder builder = new Builder();
            Document doc = builder.build(fieldFile);
            //Get root node
            Element root = doc.getRootElement();
            
            fieldNodeRoot = getNode(root);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Fields.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParsingException ex) {
            Logger.getLogger(Fields.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Fields.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fieldNodeRoot;
    }

    public static FieldNode getNode(Element n) {
        String name = n.getAttribute("name").getValue();
        FieldNode node = new FieldNode(name);
        for (int i = 0; i < n.getChildElements().size(); i++) {
            Element ele = n.getChildElements().get(i);
            node.addChild(getNode(ele));
        }
        return node;
    }

    public static Field toField(FieldNode n) {
        String name = n.getName();
        Field node = new Field(name);
        for (int i = 0; i < n.getChildCount(); i++) {
            FieldNode ele = n.getNode(i);
            node.addChild(toField(ele));
        }
        return node;
    }
}
