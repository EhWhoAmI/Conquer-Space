package ConquerSpace.game.ui;

import ConquerSpace.Globals;
import ConquerSpace.game.Action;
import ConquerSpace.game.universe.civilizations.Civilization;
import java.util.ArrayList;

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
        Thread t = new Thread(new Thread() {

            @Override
            public void run() {
                main:
                //Game loop
                while (true) {
                    for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
                        Civilization c = Globals.universe.getCivilization(i);
                        ArrayList<Action> actions = c.controller.doTurn();
                        System.out.println("actions: " + actions.size());
                        System.gc();
                        break main;
                    }
                    Globals.universe.processTurn(Globals.turn);
                    Globals.turn++;
                    System.gc();
                }
            }

        });
        t.start();

    }
}
