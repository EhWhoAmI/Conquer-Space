package ConquerSpace.game;

import ConquerSpace.game.people.Researcher;
import ConquerSpace.game.tech.Fields;
import ConquerSpace.game.tech.Techonologies;
import ConquerSpace.game.tech.Techonology;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.VisionTypes;
import ConquerSpace.game.universe.spaceObjects.ControlTypes;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.SpaceObject;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.game.universe.spaceObjects.pSectors.PopulationStorage;
import ConquerSpace.util.CQSPLogger;
import java.util.Random;
import org.apache.logging.log4j.Logger;

/**
 * This actually controls the game. If you take out this class, too bad...
 *
 * @author Zyun
 */
public class GameUpdater {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameUpdater.class.getName());

    private Universe universe;

    public GameUpdater(Universe u, StarDate s) {
        universe = u;
    }

    public void calculateControl() {
        for (UniversePath p : universe.control.keySet()) {
            SpaceObject spaceObject = universe.getSpaceObject(p);

            //Do a run through of all planets.
            if (spaceObject instanceof Planet) {
                Planet planet = (Planet) spaceObject;
                //Get the control.
                universe.control.put(p, planet.getOwnerID());
//                if (universe.control.containsKey(new UniversePath(p.getSectorID(), p.getSystemID()))) {
//                    if (planet.getOwnerID() > -1) {
//                        if (planet.getOwnerID() != universe.control.get(new UniversePath(p.getSectorID(), p.getSystemID()))) {
//                            universe.control.put(new UniversePath(p.getSectorID(), p.getSystemID()), -1);
//                        }
//                    }
//                } else {
//                    universe.control.put(new UniversePath(p.getSectorID(), p.getSystemID()), planet.getOwnerID());
//                }
            } else if (spaceObject instanceof Star) {
                Star star = (Star) spaceObject;
                //Get the control.
                universe.control.put(p, star.getOwnerID());
//                if (universe.control.containsKey(new UniversePath(p.getSectorID(), p.getSystemID()))) {
//                    if (star.getOwnerID() > -1) {
//                        if (star.getOwnerID() != universe.control.get(new UniversePath(p.getSectorID(), p.getSystemID()))) {
//                            universe.control.put(new UniversePath(p.getSectorID(), p.getSystemID()), ControlTypes.NONE_CONTROLLED);
//                        }
//                        //numofcivsinsystem.put(new UniversePath(p.getSectorID(), p.getSystemID()), numofcivsinsystem.get(new UniversePath(p.getSectorID(), p.getSystemID())) | star.getOwnerID());
//                    }
//                } else {
//                    universe.control.put(new UniversePath(p.getSectorID(), p.getSystemID()), star.getOwnerID());
//                }
            } else if (spaceObject instanceof StarSystem) {
                StarSystem starsystem = (StarSystem) spaceObject;
                int owner = -1;
                for (int i = 0; i < starsystem.getPlanetCount(); i++) {
                    Planet planet = starsystem.getPlanet(i);
                    if (owner == -1 && planet.getOwnerID() > -1) {
                        owner = planet.getOwnerID();
                    } else if (owner != planet.getOwnerID() && planet.getOwnerID() != -1) {
                        owner = ControlTypes.DISPUTED;
                    }
                }
                universe.control.put(p, owner);

            }
        }
    }

    public void calculateVision() {
        for (UniversePath p : universe.control.keySet()) {
            //Get the vision, do it...
            int civIndex = universe.control.get(p);
            if (civIndex > -1) {
                //System.out.println("Putting vision for civ " + civIndex + " at " + p);
                //System.out.println("Planet size: " + ((Planet)universe.getSpaceObject(p)).planetSectors.length);
                universe.getCivilization(civIndex).vision.put(p, VisionTypes.KNOWS_ALL);
                //Set the parent star system visibility to true.
                universe.getCivilization(civIndex).vision.put(new UniversePath(p.getSectorID(), p.getSystemID()), VisionTypes.KNOWS_ALL);
                //Set sector to visible
                universe.getCivilization(civIndex).vision.put(new UniversePath(p.getSectorID()), VisionTypes.KNOWS_ALL);

                //Get the stars around it.
            }
        }
    }

    public void initGame() {
        //Init tech and fields
        Fields.readFields();
        Techonologies.readTech();

        //All the home planets of the civs are theirs.
        //Set home planet and sector
        Random selector = new Random(universe.getSeed());

        for (int i = 0; i < universe.getCivilizationCount(); i++) {
            Civilization c = universe.getCivilization(i);
            //Add templates

            //Add the starting techs
            c.researchTech(Techonologies.getTechByName("life"));

            //Add all starting techs
            for (Techonology tech : Techonologies.getTechsByTag("Starting")) {
                c.researchTech(tech);
            }

            //Select one of the space travel sciences
            Techonology[] teks = Techonologies.getTechsByTag("space travel base");
            //To research this
            c.civTechs.put(teks[selector.nextInt(teks.length)], 100);
            c.calculateTechLevel();

            //Add researchers
            //Only one.
            Researcher r = new Researcher("Person", 20);
            r.setSkill(1);
            c.people.add(r);

            UniversePath p = c.getStartingPlanet();
            if (universe.getSpaceObject(p) instanceof Planet) {
                Planet starting = (Planet) universe.getSpaceObject(p);
                int sectorCount = starting.getPlanetSectorCount();
                int id = selector.nextInt(sectorCount);
                PopulationStorage storage = new PopulationStorage(100l, 100l, (byte)100);
                starting.setPlanetSector(id, storage);

                starting.setName(c.getHomePlanetName());

                //Set ownership
                starting.setOwnerID(c.getID());

                LOGGER.info("Civ " + c.getName() + " Starting planet: " + starting.getUniversePath());
            }
        }
        calculateControl();
        calculateVision();
    }
}