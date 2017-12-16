package ConquerSpace.game.universe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

/**
 *
 * @author Zyun
 */
public class Universe {
    private ArrayList<StarSystem> starSystems;
    
    public void parse(String file) throws FileNotFoundException, ParsingException, ValidityException, IOException {
        // Open file
        File xmlFile = new File(file);
        if (!xmlFile.exists()) {
            throw new FileNotFoundException("The file " + file + " was not found");
        }
        Builder xmlBuilder = new Builder();
        Document build = xmlBuilder.build(xmlFile);
        //Get xml
        Element root = build.getRootElement();
        Element universeElement = root.getFirstChildElement("universe");
        Elements starSystemElements = universeElement.getChildElements("star-system");
        
        //Star systems
        for (int i = 0; i < starSystemElements.size(); i++) {
            Element starSystemElement = starSystemElements.get(i);
            String idStr = starSystemElement.getAttribute("id").getValue();
            
            StarSystem starSystem = new StarSystem(Integer.parseInt(idStr));
            
            //Get stars
            Elements stars = starSystemElement.getChildElements("star");
            for (int s = 0; s < stars.size(); s ++) {
                Element starElement = stars.get(i);
                Element starTypeElement = starElement.getFirstChildElement("star-type");
                String starTypeString = starTypeElement.getValue();
                StarTypes starType;
                switch(starTypeString) {
                    case "1":
                        starType = StarTypes.brown;
                        break;
                    case "2":
                        starType = StarTypes.yellow;
                        break;
                    case "3":
                        starType = StarTypes.red;
                        break;
                    case "4":
                        starType = StarTypes.blue;
                }
            }
        }
        
    }
}
