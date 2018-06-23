package ConquerSpace.game;

import ConquerSpace.game.templates.DefaultTemplates;
import ConquerSpace.game.templates.Template;
import ConquerSpace.game.universe.civilizations.Civilization;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.SpaceObject;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.game.universe.spaceObjects.pSectors.PopulationStorage;
import ConquerSpace.util.ExceptionHandling;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This actually controls the game. If you take out this class, too bad...
 *
 * @author Zyun
 */
public class GameUpdater {

    private Universe universe;

    public GameUpdater(Universe u, StarDate s) {
        universe = u;
    }

    public void calculateControl() {
        HashMap<UniversePath, Integer> numofcivsinsystem = new HashMap<>();

        for (UniversePath p : universe.control.keySet()) {
            SpaceObject spaceObject = universe.getSpaceObject(p);

            //Do a run through of all planets.
            if (spaceObject instanceof Planet) {
                Planet planet = (Planet) spaceObject;
                //Get the control.
                universe.control.put(p, planet.getOwnerID());
                if (numofcivsinsystem.containsKey(new UniversePath(p.getSectorID(), p.getSystemID()))) {
                    if (planet.getOwnerID() > -1) {
                        numofcivsinsystem.put(new UniversePath(p.getSectorID(), p.getSystemID()), (numofcivsinsystem.get(new UniversePath(p.getSectorID(), p.getSystemID())) | planet.getOwnerID()));
                    }
                } else {
                    numofcivsinsystem.put(new UniversePath(p.getSectorID(), p.getSystemID()), planet.getOwnerID());

                }
            } else if (spaceObject instanceof Star) {
                Star star = (Star) spaceObject;
                //Get the control.
                universe.control.put(p, star.getOwnerID());
                if (numofcivsinsystem.containsKey(new UniversePath(p.getSectorID(), p.getSystemID()))) {
                    if (star.getOwnerID() > -1) {
                        numofcivsinsystem.put(new UniversePath(p.getSectorID(), p.getSystemID()), numofcivsinsystem.get(new UniversePath(p.getSectorID(), p.getSystemID())) | star.getOwnerID());
                    }
                } else {
                    numofcivsinsystem.put(new UniversePath(p.getSectorID(), p.getSystemID()), planet.getOwnerID());
                }
            }
        }

        //Loop thru civs in system
        for (UniversePath p : numofcivsinsystem.keySet()) {
            if (numofcivsinsystem.get(p) > 1) {

            }
        }
    }

    public void initGame() {
        //All the home planets of the civs are theirs.
        //Set home planet and sector
        Random selector = new Random();
        for (int i = 0; i < universe.getCivilizationCount(); i++) {
            Civilization c = universe.getCivilization(i);
            //Add templates
            Template[] t = DefaultTemplates.createDefaultTemplates();

            for (Template template : t) {
                c.addTemplate(template, template.getName());
            }
            for (Template template : t) {
                System.out.println(template.getName());
            }
            UniversePath p = c.getStartingPlanet();
            if (universe.getSpaceObject(p) instanceof Planet) {
                try {
                    Planet starting = (Planet) universe.getSpaceObject(p);
                    int sectorCount = starting.getPlanetSectorCount();
                    int id = selector.nextInt(sectorCount);
                    PopulationStorage storage = (PopulationStorage) c.getTemplate("Basic residence").create();
                    starting.setPlanetSector(id, storage);

                    //Set ownership
                    starting.setOwnerID(c.getID());
                } catch (ClassCastException ex) {
                    ExceptionHandling.ExceptionMessageBox("Class cast exception! " + ex.getMessage(), ex);
                } catch (InstantiationException ex) {
                    ExceptionHandling.ExceptionMessageBox("Instantiation Exception! " + ex.getMessage(), ex);
                } catch (IllegalAccessException ex) {
                    ExceptionHandling.ExceptionMessageBox("Illegal Access Exception! " + ex.getMessage(), ex);
                } catch (IllegalArgumentException ex) {
                    ExceptionHandling.ExceptionMessageBox("Illegal Argument Exception! " + ex.getMessage(), ex);
                } catch (InvocationTargetException ex) {
                    ExceptionHandling.ExceptionMessageBox("Invocation Target Exception! " + ex.getMessage(), ex);
                } catch (NoSuchMethodException ex) {
                    ExceptionHandling.ExceptionMessageBox("No Such Method Exception! " + ex.getMessage(), ex);
                }
            }
        }
    }
}
