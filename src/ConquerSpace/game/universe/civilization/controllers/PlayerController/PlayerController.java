package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.StarDate;
import ConquerSpace.game.actions.Action;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.universe.civilization.controllers.CivilizationController;
import ConquerSpace.game.universe.civilizations.Civilization;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.logging.log4j.Logger;

/**
 * Controller for player.
 *
 * @author Zyun
 */
public class PlayerController implements CivilizationController {

    private static final Logger LOGGER = CQSPLogger.getLogger(PlayerController.class.getName());
    public GameWindow mainwindow;
    public UniverseDisplayer displayer;
    public UserInterface userInterface;
    public TurnSaveWindow tsWindow;
    public AlertDisplayer alertDisplayer;

    @Override
    public ArrayList<Action> doTurn(Civilization c) {
        displayer.setVisible(false);
        userInterface.setVisible(false);
        return null;
    }

    @Override
    public void alert(Alert a) {
        alertDisplayer.addAlert(a);
    }

    @Override
    public void init(Universe u, StarDate d) {
        mainwindow = new GameWindow(u);

        //displayer = new UniverseDisplayer(u);
        //userInterface = new UserInterface(u);
        tsWindow = new TurnSaveWindow(d, u);
        alertDisplayer = AlertDisplayer.getInstance();

        tsWindow.setLocation(mainwindow.getWidth() - tsWindow.getWidth(), 0);
        for (MouseListener listener : ((javax.swing.plaf.basic.BasicInternalFrameUI) tsWindow.getUI()).getNorthPane().getMouseListeners()) {
            ((javax.swing.plaf.basic.BasicInternalFrameUI) tsWindow.getUI()).getNorthPane().removeMouseListener(listener);
        }

        //mainwindow.add(displayer);
        //mainwindow.add(userInterface);
        mainwindow.add(tsWindow);
        //mainwindow.add(alertDisplayer);

    }
}
