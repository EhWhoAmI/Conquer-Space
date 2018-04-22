package ConquerSpace.game.ui;

import ConquerSpace.Globals;
import ConquerSpace.game.universe.civilizations.Civilization;

/**
 * The controller of the game UI.
 *
 * @author Zyun
 */
public class GameController {

    /**
     * Constructor. Inits all components.
     */
    public GameController() {
        Globals.turn = 0;
        //Do first process turn to set everything
        Globals.universe.processTurn(Globals.turn);
        System.gc();
        Globals.turn++;
        main:
        //Game loop
        while (true) {
            for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
                Civilization c = Globals.universe.getCivilization(i);
                c.controller.doTurn();
                //Wait until done...

                System.gc();
                break main;
            }
            Globals.universe.processTurn(Globals.turn);
            Globals.turn++;
            System.gc();
        }

    }
}
