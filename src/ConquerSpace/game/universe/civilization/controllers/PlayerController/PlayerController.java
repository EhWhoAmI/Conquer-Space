package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.StarDate;
import ConquerSpace.game.actions.Action;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.controllers.CivilizationController;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.gui.game.AlertDisplayer;
import ConquerSpace.gui.game.AlertNotification;
import ConquerSpace.gui.game.GameWindow;
import ConquerSpace.gui.game.TurnSaveWindow;
import ConquerSpace.util.CQSPLogger;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;

/**
 * Controller for player.
 *
 * @author Zyun
 */
public class PlayerController implements CivilizationController {
    private Civilization c;
    
    private static final Logger LOGGER = CQSPLogger.getLogger(PlayerController.class.getName());
    public GameWindow mainwindow;
    public TurnSaveWindow tsWindow;
    public AlertDisplayer alertDisplayer;
    //public TimeIncrementWindow timeIncrementWindow;

    @Override
    public ArrayList<Action> doTurn(Civilization c) {
        return null;
    }

    @Override
    public void alert(Alert a) {
        alertDisplayer.addAlert(a);
        AlertNotification notification = new AlertNotification(a.toString(), a.getDesc());
        notification.setLocation(mainwindow.getWidth()/2 - notification.getWidth()/2, 0);
        mainwindow.addFrame(notification);
        notification.setVisible(true);
    }

    @Override
    public void init(Universe u, StarDate d, Civilization c) {
        mainwindow = new GameWindow(u, this, c);

        tsWindow = new TurnSaveWindow(d, u);
        alertDisplayer = AlertDisplayer.getInstance();
        //timeIncrementWindow = new TimeIncrementWindow();
        tsWindow.setLocation(mainwindow.getWidth() - tsWindow.getWidth(), 0);
        //Remove mouse listeners for the turnsave window.
        for (MouseListener listener : ((javax.swing.plaf.basic.BasicInternalFrameUI) tsWindow.getUI()).getNorthPane().getMouseListeners()) {
            ((javax.swing.plaf.basic.BasicInternalFrameUI) tsWindow.getUI()).getNorthPane().removeMouseListener(listener);
        }

        mainwindow.add(tsWindow);
        //mainwindow.add(timeIncrementWindow);
    }
}
