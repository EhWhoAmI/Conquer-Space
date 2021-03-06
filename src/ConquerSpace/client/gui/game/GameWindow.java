/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.client.gui.game;

import ConquerSpace.ConquerSpace;
import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.client.gui.renderers.SystemRenderer;
import ConquerSpace.client.gui.renderers.UniverseRenderer;
import ConquerSpace.common.GameState;
import ConquerSpace.common.StarDate;
import ConquerSpace.common.actions.Alert;
import ConquerSpace.common.actions.ExitStarSystemAction;
import ConquerSpace.common.actions.InterstellarTravelAction;
import ConquerSpace.common.actions.ShipMoveAction;
import ConquerSpace.common.actions.ShipSurveyAction;
import ConquerSpace.common.actions.ShipToOrbitAction;
import ConquerSpace.common.game.events.Event;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.organizations.civilization.vision.VisionTypes;
import ConquerSpace.common.game.ships.Hull;
import ConquerSpace.common.game.ships.Ship;
import ConquerSpace.common.game.ships.ShipType;
import ConquerSpace.common.game.universe.SpacePoint;
import ConquerSpace.common.game.universe.bodies.Body;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.game.universe.bodies.StarSystem;
import ConquerSpace.common.save.SaveGame;
import ConquerSpace.common.util.ExceptionHandling;
import ConquerSpace.common.util.ResourceLoader;
import ConquerSpace.common.util.logging.CQSPLogger;
import ConquerSpace.server.GameController;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import org.apache.logging.log4j.Logger;

/**
 * Game UI entrypoint
 *
 * @author EhWhoAmI
 */
public class GameWindow extends JFrame implements WindowListener,
        ComponentListener {

    private static final Logger LOGGER
            = CQSPLogger.getLogger(GameWindow.class.getName());

    public TurnSaveWindow tsWindow;

    private CQSPDesktop desktopPane;
    private JMenuBar menuBar;

    private final Civilization civController;

    private MainInterfaceWindow mainInterfaceWindow;

    private EncyclopediaWindow encyclopedia;

    private final Galaxy universe;

    private Timer gameUIUpdater;

    private StarDate date;

    private final GameState gameState;

    private PlayerRegister playerRegister;

    public GameWindow(GameState gameState, Civilization c) {
        this.civController = c;
        this.universe = gameState.getUniverse();
        this.date = gameState.date;
        this.gameState = gameState;

        playerRegister = new PlayerRegister();

        //Edit menu bar
        addWindowListener(this);
        init();

        setTitle(
                LOCALE_MESSAGES.getMessage("GameName")
                + " " + ConquerSpace.VERSION.getVersionCore());

        //Debug stuff
        //addFrame(new DegreeSetter());
        //A window to greet the user
        JOptionPane.showMessageDialog(this,
                LOCALE_MESSAGES.getMessage("game.start.message"),
                LOCALE_MESSAGES.getMessage("game.start.message.title"),
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void addFrame(JInternalFrame frame) {
        desktopPane.add(frame);
    }

    public void passEvent(Event e) {
        mainInterfaceWindow.passEvent(e);
    }

    @SuppressWarnings("deprecation")
    public void init() {
        desktopPane = new CQSPDesktop();

        initMainInterfaceWindow();

        tsWindow = new TurnSaveWindow(gameState);

        //Remove mouse listeners for the turnsave window so that it 
        //can't be moved
        for (MouseListener listener
                : ((javax.swing.plaf.basic.BasicInternalFrameUI) tsWindow.getUI())
                        .getNorthPane().getMouseListeners()) {
            ((javax.swing.plaf.basic.BasicInternalFrameUI) tsWindow.getUI())
                    .getNorthPane().removeMouseListener(listener);
        }

        addFrame(tsWindow);

        initMenus();

        //Set timer
        gameUIUpdater = new Timer(500, a -> {
            if (this.isActive()) {
                try {
                    //Only update when visible, and mouse is moving into it, 
                    //saves performance
                    if (mainInterfaceWindow != null
                            && mainInterfaceWindow.isVisible()) {
                        mainInterfaceWindow.update();
                        mainInterfaceWindow.validate();
                        mainInterfaceWindow.repaint();
                    }
                    if (allowTick()) {
                        desktopPane.repaint();
                    }
                } catch (Exception e) {
                    LOGGER.warn("Exception!", e);
                }
            }
        });

        gameUIUpdater.setRepeats(true);

        desktopPane.setDragMode(JDesktopPane.LIVE_DRAG_MODE);

        //Listeners
        addComponentListener(this);

        //desktopPane.setBackground(Color.cyan);
        setJMenuBar(menuBar);
        setContentPane(desktopPane);

        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        setSize(rect.width, rect.height);

        //Set Icon
        setIconImage(ResourceLoader.getIcon("game.icon").getImage());

        setVisible(true);

        gameUIUpdater.start();
        changeTurnSaveWindowPosition();
        //See home planet
        desktopPane.see(civController.getStartingPlanet().getSystemIndex());
    }

    /**
     * Because the main interface window takes a long time to start up
     */
    private void initMainInterfaceWindow() {
        AtomicLong mainInterfaceWindowInitStart = new AtomicLong();
        SwingWorker<MainInterfaceWindow, Void> interfaceWorker
                = new SwingWorker<MainInterfaceWindow, Void>() {
            @Override
            protected MainInterfaceWindow doInBackground() {
                mainInterfaceWindowInitStart.set(System.currentTimeMillis());
                return new MainInterfaceWindow(gameState, civController, playerRegister);

            }

            @Override
            protected void done() {
                try {
                    mainInterfaceWindow = get();
                    addFrame(mainInterfaceWindow);
                } catch (InterruptedException ex) {
                    LOGGER.warn("oops couldn't create window concurrently,"
                            + " making it conventionally.", ex);
                    createFrame();
                } catch (ExecutionException ex) {
                    LOGGER.warn(
                            "oops couldn't create window concurrently,"
                            + " making it conventionally.", ex);
                    createFrame();
                }
                long end = System.currentTimeMillis();
                LOGGER.info(
                        "Done with making interface in "
                        + (end - mainInterfaceWindowInitStart.get())
                        + " ms");
            }

            /**
             * When concurrency doesn't work.
             */
            private void createFrame() {
                mainInterfaceWindow
                        = new MainInterfaceWindow(gameState, civController, playerRegister);
                addFrame(mainInterfaceWindow);
            }
        };

        interfaceWorker.execute();
    }

    private void initMenus() {
        menuBar = new JMenuBar();

        JMenu windows = new JMenu(
                LOCALE_MESSAGES.getMessage("game.main.windows"));

        JMenuItem mainInterfaceWindowMenuButton = new JMenuItem(
                LOCALE_MESSAGES.getMessage("game.main.interface"));
        mainInterfaceWindowMenuButton.addActionListener(a -> {
            if (mainInterfaceWindow != null) {
                mainInterfaceWindow.setVisible(true);
            }
        });
        mainInterfaceWindowMenuButton.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_D,
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        JMenuItem encyclopediaWindowMenuButton = new JMenuItem(
                LOCALE_MESSAGES.getMessage("game.encyclopedia.title"));
        encyclopediaWindowMenuButton.addActionListener(l -> {
            if (encyclopedia != null) {
                if (!encyclopedia.isVisible()) {
                    encyclopedia.setVisible(true);
                }
                encyclopedia.toFront();;
            } else {
                encyclopedia = new EncyclopediaWindow(gameState);
                addFrame(encyclopedia);
                encyclopedia.setVisible(true);
            }
        });

        windows.add(mainInterfaceWindowMenuButton);
        windows.add(encyclopediaWindowMenuButton);

        JMenu game = new JMenu(LOCALE_MESSAGES.getMessage("game.game"));
        JMenuItem pauseplayButton = new JMenuItem(
                LOCALE_MESSAGES.getMessage("game.already.paused"));
        pauseplayButton.addActionListener(a -> {
            if (tsWindow.isPaused()) {
                pauseplayButton.setText(
                        LOCALE_MESSAGES.getMessage("game.pause"));
            } else {
                pauseplayButton.setText(
                        LOCALE_MESSAGES.getMessage("game.already.paused"));
            }
            a = new ActionEvent(pauseplayButton, 0, "pauseplay");
            tsWindow.actionPerformed(a);
        });
        pauseplayButton.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
        game.add(pauseplayButton);

        JMenu views = new JMenu(
                LOCALE_MESSAGES.getMessage("game.view.background.view"));

        JMenuItem setToUniverseView = new JMenuItem(
                LOCALE_MESSAGES
                        .getMessage("game.view.background.universe.view"));
        setToUniverseView.addActionListener(a -> {
            desktopPane.currentView = CQSPDesktop.DRAW_UNIVERSE;
            desktopPane.repaint();
        });
        setToUniverseView.setAccelerator(
                KeyStroke.getKeyStroke((int) '1',
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        JMenuItem seeHomePlanet = new JMenuItem(
                LOCALE_MESSAGES.getMessage("game.view.home.planet"));
        seeHomePlanet.addActionListener(a -> {
            desktopPane.see(civController.getStartingPlanet().getSystemIndex());
        });
        seeHomePlanet.setAccelerator(
                KeyStroke.getKeyStroke((int) '9',
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        JMenuItem recenter
                = new JMenuItem(LOCALE_MESSAGES.getMessage("game.view.recenter"));
        recenter.addActionListener(a -> {
            desktopPane.recenter();
            desktopPane.repaint();
        });

        views.add(setToUniverseView);
        views.add(seeHomePlanet);
        views.add(recenter);

        menuBar.add(windows);
        menuBar.add(game);
        menuBar.add(views);
    }

    public void reload() {
        init();
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
        //Window open behavior
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        if (JOptionPane.showConfirmDialog(this,
                LOCALE_MESSAGES.getMessage("game.save.query"),
                LOCALE_MESSAGES.getMessage("game.save"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            SaveGame game = new SaveGame(SaveGame.getSaveFolder());
            long before = System.currentTimeMillis();
            try {
                game.save(gameState);
            } catch (IOException ex) {
                ExceptionHandling.exceptionMessageBox(
                        "IO exception while saving!", ex);
            } catch (IllegalArgumentException ex) {
                ExceptionHandling.exceptionMessageBox(
                        "Illegal Argument exception while saving!", ex);
            } catch (IllegalAccessException ex) {
                ExceptionHandling.exceptionMessageBox(
                        "Illegal Access exception while saving!", ex);
            }
            LOGGER.info(
                    "Time to save " + (System.currentTimeMillis() - before));
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            GameController.musicPlayer.clean();
            System.exit(0);
        } else {
            System.exit(0);
        }
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
        System.exit(0);
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
        //Window icon behavior
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
        //Window reopen behavior
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
        //Window activate behavior
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
        //Window deactivated behavior
    }

    @Override
    public void componentResized(ComponentEvent e) {
        changeTurnSaveWindowPosition();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        //Window moved behavior
    }

    @Override
    public void componentShown(ComponentEvent e) {
        //Window shown behavior
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        //Window hidden behavior
    }

    public boolean allowTick() {
        return tsWindow.isPaused();
    }

    public int getTickCount() {
        return tsWindow.getTickCount();
    }

    private void changeTurnSaveWindowPosition() {
        tsWindow.setLocation(getWidth() - tsWindow.getWidth(), 0);
    }

    public void alertNotification(Alert alert) {
        AlertNotification notification
                = new AlertNotification(alert.toString(), alert.getDesc());
        addFrame(notification);
        notification.setVisible(true);
    }

    /**
     * Renders window and stuff.
     */
    public class CQSPDesktop extends JDesktopPane implements
            MouseMotionListener, MouseListener, MouseWheelListener {

        private UniverseRenderer universeRenderer;
        private boolean isDragging = false;
        private Point startPoint;
        private double translateX;
        private double translateY;
        static final int DRAW_UNIVERSE = 0;
        static final int DRAW_STAR_SYSTEM = 1;
        private int currentView = DRAW_UNIVERSE;
        private int starSystemDisplayed = 0;
        private SystemRenderer systemRenderer;
        public static final int BOUNDS_SIZE = 1500;

        private int currentStarSystemSizeOfAU = 0;
        /**
         * Scale for the zoom. A scale of 1 is the current universe view, and it can zoom to a max
         * of 5.
         * <p>
         */
        private double scale = 1.0f;

        public CQSPDesktop() {
            universeRenderer
                    = new UniverseRenderer(
                            gameState, new Dimension(1500, 1500), civController);
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            //Turn on Anti-aliasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            switch (currentView) {
                case DRAW_UNIVERSE:
                    universeRenderer.drawUniverse(g, translateX, translateY, scale);
                    break;
                case DRAW_STAR_SYSTEM:
                    if (systemRenderer != null) {
                        setBackground(Color.BLACK);
                        systemRenderer.drawStarSystem(g, translateX, translateY, scale);
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            //Drag code
            if (isDragging && SwingUtilities.isLeftMouseButton(e)) {
                translateX -= ((startPoint.x - e.getX()) * (scale));
                translateY -= ((startPoint.y - e.getY()) * (scale));
                startPoint = e.getPoint();
                repaint();
            } else if (isDragging && SwingUtilities.isRightMouseButton(e)) {
                //Measure distance
                //Set point of the start and end
                if (currentView == DRAW_STAR_SYSTEM) {
                    systemRenderer.setMeasureDistance(startPoint, e.getPoint());
                    repaint();
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //Mouse move event
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && mainInterfaceWindow != null) {
                //If universe, click
                for (int i = 0; i < universe.getStarSystemCount(); i++) {
                    //Check for vision
                    StarSystem sys = universe.getStarSystemObject(i);
                    //Complex if statements lol
                    if (Math.hypot((universeRenderer.convertPointToScreen(sys.getX(), translateX, scale) - e.getX()),
                            (universeRenderer.convertPointToScreen(sys.getY(), translateY, scale) - e.getY())) < (universeRenderer.getSizeOfStar() / scale)
                            //If you can see it
                            && civController.getVision().containsKey(sys.getUniversePath())
                            && civController.getVision().get(sys.getUniversePath()) > VisionTypes.UNDISCOVERED
                            && !universeRenderer.drawingStarSystemDetails(scale)) {
                        see(sys);
                        //Zoom into the star system, zoom is good enough, is about 1/1 billion
                        break;
                    }
                }

                //Check if zoomed into anything
                if (universeRenderer.drawingStarSystemDetails(scale)) {
                    // Then check if clicking on anything
                    StarSystem system = universe.getStarSystemObject(universeRenderer.getStarSystemFocused());

                    for (int i = 0; i < system.getBodyCount(); i++) {
                        Body body = system.getBodyObject(i);
                        if (body instanceof Planet) {
                            Planet planet = (Planet) body;
                            int x = (int) universeRenderer.convertPointToScreen(body.getX(), translateX, scale);
                            int y = (int) universeRenderer.convertPointToScreen(body.getY(), translateY, scale);
                            double distanceFromCenter = Math.hypot(x - e.getX(), y - e.getY());

                            if (distanceFromCenter
                                    < universeRenderer.getPlanetDrawnDiameter() / 2
                                    || distanceFromCenter < (planet.getDiameter() * universeRenderer.sizeOfLTYR) / scale / 2) {
                                //Check if scanned
                                showPlanet(planet);
                                break;
                            }
                        }
                    }
                }
            }
            if ((e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e))
                    && mainInterfaceWindow != null) {
                JPopupMenu popupMenu = generatePopupMenu(e);
                popupMenu.show(this, e.getX(), e.getY());
                repaint();
            }
        }

        private void showPlanet(Planet planet) {
            mainInterfaceWindow.setSelectedPlanet(planet, planet.hasScanned(civController.getReference()));
            mainInterfaceWindow.setSelectedTab(1);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //Started
            isDragging = true;
            startPoint = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            isDragging = false;
            if (SwingUtilities.isRightMouseButton(e)) {
                //Distance measuring
                //Set point of the start and end
                if (currentView == DRAW_STAR_SYSTEM) {
                    systemRenderer.endMeasureDistance();
                    repaint();
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //Mouse entered event
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //Mouse exited event
        }

        protected void see(StarSystem system) {
            scale = (0.000000001d);
            translateX = getSize().width / 2 * scale - system.getX() * universeRenderer.sizeOfLTYR;
            translateY = getSize().height / 2 * scale - system.getY() * universeRenderer.sizeOfLTYR;
            universeRenderer.setStarSystemFocused(system.getIndex());
            repaint();
        }

        public void see(int index) {
            see(universe.getStarSystemObject(civController.getStartingPlanet().getSystemIndex()));
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            //Change scroll
            double scroll = (double) e.getUnitsToScroll();
            double scrollBefore = scale;
            double newScale = (Math.exp(scroll * 0.1) * scale);
            //Limit scale
            if (newScale > 1e-16 && newScale < 6) {
                scale = newScale;
                double msX = (e.getX() * scale);
                double msY = (e.getY() * scale);
                double scaleChanged = scale - scrollBefore;

                translateX += (msX * scaleChanged) / scale;
                translateY += (msY * scaleChanged) / scale;
            }
            //Now repaint
            repaint();
        }

        public void recenter() {
            translateX = getSize().width / 2;
            translateY = getSize().height / 2;
            scale = 1f;
        }

        private JPopupMenu generatePopupMenu(MouseEvent e) {
            //Show popup menu
            JPopupMenu popupMenu = new JPopupMenu();

            Planet overWhat = null;
            //Show info and specific information of the sectors and stuff
            LOGGER.trace("Checking for click on star system");
            for (int i = 0; i < universe.getStarSystemCount(); i++) {
                //Check for vision
                StarSystem sys = universe.getStarSystemObject(i);

                if (Math.hypot(universeRenderer.convertPointToScreen(sys.getX(), translateX, scale) - e.getX(),
                        universeRenderer.convertPointToScreen(sys.getY(), translateY, scale) - e.getY())
                        < (double) universeRenderer.getSizeOfStar()
                        && civController.getVision().containsKey(sys.getUniversePath())
                        && civController.getVision().get(sys.getUniversePath()) > VisionTypes.UNDISCOVERED
                        && !universeRenderer.drawingStarSystemDetails(scale)) {
                    //View star system menu
                    JMenuItem systemInfo
                            = new JMenuItem(LOCALE_MESSAGES.getMessage("game.click.popup.starsystem", sys.getIndex()));
                    systemInfo.addActionListener(a -> {
                        see(sys);
                        repaint();
                    });
                    popupMenu.add(systemInfo);
                    break;
                }
            }

            if (universeRenderer.drawingStarSystemDetails(scale)) {
                overWhat = createPlanetMenu(popupMenu, e);
            }

            //Ship actions
            JMenu selectedShips = new JMenu(LOCALE_MESSAGES.getMessage("game.click.popup.ship.selected"));
            //Get currently selected ships
            for (Ship ship : playerRegister.getSelectedShips()) {
                createShipMenu(selectedShips, ship, e, overWhat);
            }

            //Clear selected ships
            JMenuItem deleteSelectedShips
                    = new JMenuItem(LOCALE_MESSAGES.getMessage("game.click.popup.ship.remove.ships"));
            deleteSelectedShips.addActionListener(a -> {
                playerRegister.getSelectedShips().clear();
            });

            popupMenu.add(selectedShips);
            popupMenu.add(deleteSelectedShips);
            return popupMenu;
        }

        private Planet createPlanetMenu(JPopupMenu popupMenu, MouseEvent e) {
            StarSystem selected = universe.getStarSystemObject(universeRenderer.getStarSystemFocused());
            for (int i = 0; i < selected.getBodyCount(); i++) {
                Body body = selected.getBodyObject(i);
                if (body instanceof Planet) {
                    Planet planet = (Planet) body;
                    //Convert point to screen point
                    int x = (int) universeRenderer.convertPointToScreen(body.getX(), translateX, scale);
                    int y = (int) universeRenderer.convertPointToScreen(body.getY(), translateY, scale);

                    double distanceFromCenter = Math.hypot(x - e.getX(), y - e.getY());

                    if (distanceFromCenter
                            < universeRenderer.getPlanetDrawnDiameter() / 2
                            || distanceFromCenter < (planet.getDiameter() * universeRenderer.sizeOfLTYR) / scale / 2) {

                        JMenuItem planetName = new JMenuItem();
                        planetName.addActionListener(a -> {
                            mainInterfaceWindow.setSelectedPlanet(planet, true);
                            mainInterfaceWindow.setSelectedTab(1);
                        });

                        if (planet.hasScanned(civController.getReference())) {
                            planetName.setText(LOCALE_MESSAGES.getMessage("game.click.popup.planet", planet.getName()));
                        } else {
                            planetName.setText(LOCALE_MESSAGES.getMessage("game.click.popup.planet.unexplored"));
                        }
                        popupMenu.add(planetName);
                        return planet;
                    }
                }
            }
            return null;
        }

        private void createShipMenu(JMenu selectedShips, Ship ship, MouseEvent e, Planet overWhatPlanet) {
            JMenu men = new JMenu(ship.toString());
            JMenuItem gohereMenu = new JMenuItem(LOCALE_MESSAGES.getMessage("game.click.popup.ship.goto"));
            gohereMenu.addActionListener(a -> {
                //Move position
                if (currentView == DRAW_UNIVERSE) {
                    //Check if inside star system
                    //Convert to world point
                    double gotoX = (e.getX() * scale - translateX - BOUNDS_SIZE / 2) / universeRenderer.sizeOfLTYR;
                    double gotoY = (e.getY() * scale - translateY - BOUNDS_SIZE / 2) / universeRenderer.sizeOfLTYR;

                    if (ship.getLocation().getSystemIndex() > -1) {
                        //Then get quickest route to out of system, and do the things
                        //Ah well, get slope because the path can be direct...
                        StarSystem system = universe.getStarSystemObject(ship.getLocation().getSystemIndex());
                        double slopeX = gotoX - system.getX();
                        double slopeY = gotoY - system.getY();
                        double slope = (slopeY / slopeX);
                        //Add move action

                        //Get distance to intersect
                        long x = (long) (slope * 10_000_000_000l);
                        //Get distance of ship to 
                        ShipMoveAction action = new ShipMoveAction(ship);

                        //TODO: FIX SHIPS
                        action.setPosition(new SpacePoint(100l, x));

                        ship.addAction(gameState, action);
                        //Add exit star system action
                        ExitStarSystemAction act = new ExitStarSystemAction(ship);
                        ship.addAction(gameState, act);
                    }
                    InterstellarTravelAction action = new InterstellarTravelAction(ship);
                    action.setPositionX(gotoX);
                    action.setPositionY(gotoY);
                    ship.addAction(gameState, action);
                } else if (currentView == DRAW_STAR_SYSTEM) {
                    //Check the goto position
                    //Convert to world position
                    long gotoX = (long) (((e.getX() * scale - BOUNDS_SIZE / 2 - translateX) * 10_000_000) / currentStarSystemSizeOfAU);
                    long gotoY = (long) (((e.getY() * scale - BOUNDS_SIZE / 2 - translateY) * 10_000_000) / currentStarSystemSizeOfAU);

                    //Get Location
                    ShipMoveAction action = new ShipMoveAction(ship);
                    action.setPosition(new SpacePoint(gotoX, gotoY));

                    ship.addAction(gameState, action);

                    //Add an extra action to move, todo because removed star systme 
                    /* StarSystem sys = universe.getStarSystemObject(starSystemDisplayed);
                        if (Math.hypot(gotoX, gotoY) > (sys.getPlanet(sys.getPlanetCount() - 1).getOrbitalDistance() + 10 * 10_000_000)) {
                         ExitStarSystemAction act = new ExitStarSystemAction(s);
                        s.addAction(act);
                        }*/
                }
            });
            men.add(gohereMenu);

            //Check if orbiting a planet...
            if (overWhatPlanet != null) {
                //Orbit it! 
                JMenuItem orbiting = new JMenuItem(String.format(LOCALE_MESSAGES.getMessage("game.click.popup.ship.orbit"), overWhatPlanet.getName()));
                final Planet p = overWhatPlanet;
                orbiting.addActionListener(a -> {
                    //Move to orbit
                    ShipToOrbitAction action = new ShipToOrbitAction(ship);
                    action.setPlanet(p);
                    ship.addAction(gameState, action);
                });
                men.add(orbiting);
            }
            //If science ship
            ShipType stype = gameState.getObject(ship.getHull(), Hull.class).getShipType();
            if (stype.containsTag("science") && overWhatPlanet != null && !overWhatPlanet.hasScanned(civController.getReference())) {
                JMenuItem surveryor = createSurveyorMenu(ship, overWhatPlanet);
                men.add(surveryor);
            }

            //Add a delete ship action thing
            JMenuItem deleteShipAction
                    = new JMenuItem(LOCALE_MESSAGES.getMessage("game.click.popup.ship.remove.actions"));
            deleteShipAction.addActionListener(l -> {
                ship.commands.clear();
            });
            men.add(deleteShipAction);

            selectedShips.add(men);
        }

        private JMenuItem createSurveyorMenu(Ship ship, Planet planet) {
            JMenuItem surveryorMenu = new JMenuItem(LOCALE_MESSAGES.getMessage("game.click.popup.ship.survey"));
            final Planet p = planet;

            surveryorMenu.addActionListener(a -> {
                //Move position
                //Get Location
                ShipToOrbitAction orbitAction = new ShipToOrbitAction(ship);
                orbitAction.setPlanet(p);

                ShipSurveyAction survey = new ShipSurveyAction(ship);
                survey.setProgressPerTick(5);
                survey.setFinishedProgress(100);
                survey.setToSurvey(p);
                survey.setCivReference(civController.getReference());

                ship.addAction(gameState, orbitAction);
                ship.addAction(gameState, survey);
            });

            return surveryorMenu;
        }
    }

    private class DegreeSetter extends JInternalFrame {

        private JSlider slider;
        private JSpinner degreeSpinner;

        private double degree = 0;

        public DegreeSetter() {
            setLayout(new VerticalFlowLayout());
            slider = new JSlider(0, 360);
            degreeSpinner = new JSpinner(new SpinnerNumberModel(degree, 0, 360, 1));

            slider.addChangeListener(l -> {
                degreeSpinner.setValue(slider.getValue());
                degree = slider.getValue();
                setAngle();
            });
            slider.setPaintLabels(true);
            slider.setPaintTrack(true);
            slider.setValue((int) degree);

            degreeSpinner.addChangeListener(l -> {
                int d = ((Number) degreeSpinner.getValue()).intValue();
                slider.setValue(d);
                degree = d;

                setAngle();
            });

            add(slider);
            add(degreeSpinner);
            pack();
            setVisible(true);
        }

        private void setAngle() {
            StarSystem sys = universe.getStarSystemObject(desktopPane.universeRenderer.getStarSystemFocused());
            for (int i = 0; i < sys.getBodyCount(); i++) {
                Body planet = sys.getBodyObject(i);
                planet.orbit.setDegrees(degree);

                planet.setPoint(planet.orbit.toSpacePoint());
            }
        }
    }
}
