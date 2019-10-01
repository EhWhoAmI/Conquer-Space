package ConquerSpace.game.science;

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
            File fieldFile = new File(System.getProperty("user.dir") + "/assets/tech/fields.xml");

            Builder builder = new Builder();
            Document doc = builder.build(fieldFile);
            //Get root node
            Element root = doc.getRootElement();
            
            //Get the base knowledge branch
            /*Elements knowledgeTree = root.getChildElements();
            FieldNode rootNode = new FieldNode(knowledgeTree.get(0).getAttribute("name").getValue());
            Elements layer2 = knowledgeTree.get(0).getChildElements();
            for (int i = 0; i < layer2.size(); i++) {
                Element topTree = layer2.get(i);
                String name = topTree.getAttribute("name").getValue();
                FieldNode node = new FieldNode(name);
                rootNode.addChild(node);
                Elements fieldNamesElements = topTree.getChildElements();
                for(int n = 0; n < fieldNamesElements.size(); n ++) {
                    Element fieldNameE = fieldNamesElements.get(n);
                    FieldNode bottomNode = new FieldNode(fieldNameE.getAttribute("name").getValue());
                    //System.out.println("Adding " + fieldNameE.getAttribute("name").getValue());
                    node.addChild(bottomNode);
                }
            }
             */
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
