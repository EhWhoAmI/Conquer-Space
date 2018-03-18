package ConquerSpace.game.ui;

import ConquerSpace.Globals;

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
        
        UniverseDisplayer displayer = new UniverseDisplayer();
        UserInterface userInterface = new UserInterface();
        TurnSaveWindow tsWindow = new TurnSaveWindow();
    }
}
