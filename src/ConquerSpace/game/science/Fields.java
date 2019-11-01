package ConquerSpace.game.science;

import ConquerSpace.game.AssetReader;
import ConquerSpace.util.ResourceLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.ParsingException;

/**
 *
 * @author Zyun
 */
public class Fields {

    public static FieldNode fieldNodeRoot;

    public Fields() {

    }

    public static void readFields() {
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
