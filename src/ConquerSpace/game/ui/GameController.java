package ConquerSpace.game.ui;

import ConquerSpace.Globals;
import ConquerSpace.game.universe.civilizations.Civilization;

/**
 * The controller of the game UI.
 * @author Zyun
 */
public class GameController {
    /**
     * Constructor. Inits all components.
     */
    public GameController() {
        Globals.turn = 0;
        main:
        while(true) {
            for(int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
                Civilization c = Globals.universe.getCivilization(i);
                c.controller.doTurn();
                //Wait until done...
                break main;
            }
        }
        
    }
}