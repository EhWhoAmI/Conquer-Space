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
package ConquerSpace.gui.game;

import ConquerSpace.util.logging.CQSPLogger;
import ConquerSpace.util.ExceptionHandling;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import org.apache.logging.log4j.Logger;

/**
 * The content of the manuals.
 *
 * @author Zyun
 */
public class InternalManualContent extends JInternalFrame {

    private static final Logger LOGGER = CQSPLogger.getLogger(InternalManualContent.class.getName());
    private JTextPane text;

    public InternalManualContent(String file) {
        setSize(500, 400);
        text = new JTextPane();
        text.setContentType("text/html");
        text.setEditable(false);
        setResizable(true);
        setClosable(true);
        
        JScrollPane pane = new JScrollPane(text);

        add(pane);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            //Parse text so that images show
            Builder b = new Builder();
            Document d = b.build(file, null);
            Element e = d.getRootElement();
            //Get all img tags.
            Elements imgs = e.getChildElements("img");
            for (int i = 0; i < imgs.size(); i++) {
                Element imgE = imgs.get(i);
                Attribute attri = imgE.getAttribute("src");
                String imgPathName = System.getProperty("user.dir") + File.separator + "assets" + File.separator + "manuals" + File.separator + attri.getValue();
                if(System.getProperty("os.name").toLowerCase().contains("win")) {
                    //Windows, remove the disk
                    imgPathName = imgPathName.replaceAll(".*:", "");
                }
                attri.setValue("file://" + imgPathName);
            }
            file = e.toXML();
        } catch (ParsingException ex) {
            LOGGER.warn("Parsing exception:" + ex.toString(), ex);
            ExceptionHandling.ExceptionMessageBox("We could not open the manual. Not a problem.\nJust don\'t use it.", ex);
        } catch (IOException ex) {
            LOGGER.warn("IO Exception:" + ex.getMessage(), ex);
            ExceptionHandling.ExceptionMessageBox("We could not open the manual. Not a problem.\nJust don\'t use it.", ex);
        }
        this.setText(file);
    }

    private void setText(String content) {
        if (text != null) {
            text.setText(content);
        }
    }
}
