package ConquerSpace.game.universe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import nu.xom.Attribute;
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

    private ArrayList<Sector> sectors;

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
        Elements sectorElements = universeElement.getChildElements("sector");

        //Sectors
        for (int secCount = 0; secCount < sectorElements.size(); secCount++) {
            
            Element sectorElement = sectorElements.get(secCount);
            Attribute dist = sectorElement.getAttribute("dist");
            Attribute degs = sectorElement.getAttribute("degs");
            Sector sector = new Sector();
            //Get position
            
            Elements starSystemElements = sectorElement.getChildElements();
            //Star systems
            for (int i = 0; i < starSystemElements.size(); i++) {
                Element starSystemElement = starSystemElements.get(i);
                String idStr = starSystemElement.getAttribute("id").getValue();

                StarSystem starSystem = new StarSystem(Integer.parseInt(idStr));
                sector.addStarSystem(starSystem);
                //Get stars
                Elements stars = starSystemElement.getChildElements("star");
                for (int s = 0; s < stars.size(); s++) {
                    Element starElement = stars.get(i);
                    Element starTypeElement = starElement.getFirstChildElement("star-type");
                    String starTypeString = starTypeElement.getValue();
                    StarTypes starType;
                    switch (starTypeString) {
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

                    //Read star size
                }
            }
        }

    }
}
