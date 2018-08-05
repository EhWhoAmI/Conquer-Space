package ConquerSpace.game;

import ConquerSpace.Globals;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.tech.Techonologies;
import ConquerSpace.game.tech.Techonology;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.PlayerController;
import ConquerSpace.util.CQSPLogger;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.Timer;
import org.apache.logging.log4j.Logger;

/**
 * The controller of the game UI.
 *
 * @author Zyun
 */
public class GameController {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameController.class.getName());

    /**
     * Constructor. Inits all components.
     */
    public GameController() {
        //Process the 0th turn and initalize the universe.
        Globals.universe.processTurn();

        //Init universe
        GameUpdater updater = new GameUpdater(Globals.universe, Globals.date);
        updater.initGame();
        //Load the player
        Globals.universe.getCivilization(0).controller.init(Globals.universe, Globals.date, Globals.universe.getCivilization(0));

        //Atomic integer so that we can edit it in a lambada.
        AtomicInteger lastMonth = new AtomicInteger(Globals.date.getMonthNumber());

        int tickerSpeed = 1;
        Timer ticker = new Timer(tickerSpeed, (e) -> {
            if (!((PlayerController) Globals.universe.getCivilization(0).controller).tsWindow.isPaused()) {
                //DO ticks
                Globals.date.increment(1);
                //Check for month increase

                if (Globals.date.getMonthNumber() != lastMonth.get()) {
                    lastMonth.set(Globals.date.getMonthNumber());
                    long start = System.currentTimeMillis();
                    Globals.universe.processTurn();
                    for (int i = 0; i < Globals.universe.getCivilizationCount(); i++)
                        Globals.universe.getCivilization(i).calculateTechLevel();
                    //Increment tech
                    for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
                        Civilization c = Globals.universe.getCivilization(i);
                        for (Techonology t : c.currentlyResearchingTechonologys.keySet()) {
                            System.out.println("processing tech " + t + " " + (Techonologies.estFinishTime(t) - c.civResearch.get(t)) + " " + lastMonth.intValue());
                            if ((Techonologies.estFinishTime(t) - c.civResearch.get(t)) <= 0) {
                                //Then tech is finished
                                c.researchTech(t);
                                c.civResearch.remove(t);
                                c.currentlyResearchingTechonologys.remove(t);
                                //Alert civ
                                c.controller.alert(new Alert(0, 0, "Tech " + t.getName() + " is finished"));
                            } else {
                                c.civResearch.put(t, c.civResearch.get(t) + c.currentlyResearchingTechonologys.get(t).getSkill());
                            }
                        }
                    }
                    long end = System.currentTimeMillis();

                    LOGGER.info("Took " + (end - start) + " ms");
                }
            }
        });

        //Start ticker
        ticker.start();
    }
}