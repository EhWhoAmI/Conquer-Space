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
import ConquerSpace.common.actions.ExitStarSystemAction;
import ConquerSpace.common.actions.InterstellarTravelAction;
import ConquerSpace.common.actions.ShipMoveAction;
import ConquerSpace.common.actions.ShipSurveyAction;
import ConquerSpace.common.actions.ToOrbitAction;
import ConquerSpace.common.game.events.Event;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.organizations.civilization.vision.VisionTypes;
import ConquerSpace.common.game.ships.Ship;
import ConquerSpace.common.game.universe.SpacePoint;
import ConquerSpace.common.game.universe.UniversePath;
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
public class GameWindow extends JFrame implements WindowListener, ComponentListener {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameWindow.class.getName());

    public TurnSaveWindow tsWindow;

    private CQSPDesktop desktopPane;
    private JMenuBar menuBar;

    private Civilization civ;

    private MainInterfaceWindow mainInterfaceWindow;

    private EncyclopediaWindow encyclopedia;

    private Galaxy universe;

    private Timer gameTickTimer;

    private StarDate date;

    private GameState gameState;

    private PlayerRegister playerRegister;

    public GameWindow(GameState gameState, Civilization c) {
        this.civ = c;
        this.universe = gameState.getUniverse();
        this.date = gameState.date;
        this.gameState = gameState;

        playerRegister = new PlayerRegister();

        //Edit menu bar
        addWindowListener(this);
        init();

        setTitle(LOCALE_MESSAGES.getMessage("GameName") + " " + ConquerSpace.VERSION.getVersionCore());

        //Debug stuff
        //addFrame(new DegreeSetter());
        //A window to greet the user
        JOptionPane.showMessageDialog(this, LOCALE_MESSAGES.getMessage("game.start.message"), LOCALE_MESSAGES.getMessage("game.start.message.title"), JOptionPane.INFORMATION_MESSAGE);
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
        menuBar = new JMenuBar();

        AtomicLong mainInterfaceWindowInitStart = new AtomicLong();
        SwingWorker<MainInterfaceWindow, Void> interfaceWorker = new SwingWorker<MainInterfaceWindow, Void>() {
            @Override
            protected MainInterfaceWindow doInBackground() {
                mainInterfaceWindowInitStart.set(System.currentTimeMillis());
                return new MainInterfaceWindow(gameState, civ, playerRegister);

            }

            @Override
            protected void done() {
                try {
                    mainInterfaceWindow = get();
                    addFrame(mainInterfaceWindow);
                } catch (InterruptedException ex) {
                    LOGGER.warn("oops couldn't create window concurrently, making it conventionally.", ex);
                    createFrame();
                } catch (ExecutionException ex) {
                    LOGGER.warn("oops couldn't create window concurrently, making it conventionally.", ex);
                    createFrame();
                }
                long end = System.currentTimeMillis();
                LOGGER.info("Done with making interface " + (end - mainInterfaceWindowInitStart.get()) + " ms");
            }

            /**
             * When concurrency doesn't work.
             */
            private void createFrame() {
                mainInterfaceWindow = new MainInterfaceWindow(gameState, civ, playerRegister);
                addFrame(mainInterfaceWindow);
            }
        };

        interfaceWorker.execute();

        tsWindow = new TurnSaveWindow(gameState);

        //Remove mouse listeners for the turnsave window so that it can't be moved
        for (MouseListener listener : ((javax.swing.plaf.basic.BasicInternalFrameUI) tsWindow.getUI()).getNorthPane().getMouseListeners()) {
            ((javax.swing.plaf.basic.BasicInternalFrameUI) tsWindow.getUI()).getNorthPane().removeMouseListener(listener);
        }

        addFrame(tsWindow);

        //addFrame(newsWindow);
        JMenu windows = new JMenu(LOCALE_MESSAGES.getMessage("game.main.windows"));
        JMenuItem mainInterfaceWindowMenuButton = new JMenuItem(LOCALE_MESSAGES.getMessage("game.main.interface"));
        mainInterfaceWindowMenuButton.addActionListener(a -> {
            if (mainInterfaceWindow != null) {
                mainInterfaceWindow.setVisible(true);
            }
        });
        mainInterfaceWindowMenuButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        JMenuItem encyclopediaWindowMenuButton = new JMenuItem(LOCALE_MESSAGES.getMessage("game.encyclopedia.title"));
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
        JMenuItem pauseplayButton = new JMenuItem(LOCALE_MESSAGES.getMessage("game.already.paused"));
        pauseplayButton.addActionListener(a -> {
            if (tsWindow.isPaused()) {
                pauseplayButton.setText(LOCALE_MESSAGES.getMessage("game.pause"));
            } else {
                pauseplayButton.setText(LOCALE_MESSAGES.getMessage("game.already.paused"));
            }
            a = new ActionEvent(pauseplayButton, 0, "pauseplay");
            tsWindow.actionPerformed(a);
        });
        pauseplayButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
        game.add(pauseplayButton);

        JMenu views = new JMenu(LOCALE_MESSAGES.getMessage("game.view.background.view"));
        JMenuItem setToUniverseView = new JMenuItem(LOCALE_MESSAGES.getMessage("game.view.background.universe.view"));
        setToUniverseView.addActionListener(a -> {
            desktopPane.drawing = CQSPDesktop.DRAW_UNIVERSE;
            desktopPane.repaint();
        });
        setToUniverseView.setAccelerator(KeyStroke.getKeyStroke((int) '1', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        JMenuItem seeHomePlanet = new JMenuItem(LOCALE_MESSAGES.getMessage("game.view.home.planet"));
        seeHomePlanet.addActionListener(a -> {
            desktopPane.see(civ.getStartingPlanet().getSystemID());
        });
        seeHomePlanet.setAccelerator(KeyStroke.getKeyStroke((int) '9', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        JMenuItem recenter = new JMenuItem(LOCALE_MESSAGES.getMessage("game.view.recenter"));
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

        //Set timer
        gameTickTimer = new Timer(50, a -> {
            if (this.isActive()) {
                try {
                    //Only update when visible, and mouse is moving into it, saves performance
                    if (mainInterfaceWindow != null && mainInterfaceWindow.isVisible()) {
                        mainInterfaceWindow.update();
                    }
                    if (allowTick()) {
                        desktopPane.repaint();
                    }
                } catch (Exception e) {
                    LOGGER.warn("Exception!", e);
                }
            }
        });

        gameTickTimer.setRepeats(true);

        desktopPane.setDragMode(JDesktopPane.LIVE_DRAG_MODE);

        //Listeners
        addComponentListener(this);

        //desktopPane.setBackground(Color.cyan);
        setJMenuBar(menuBar);
        setContentPane(desktopPane);

        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        setSize(rect.width, rect.height);

        //Set Icon
        setIconImage(ResourceLoader.getIcon("game.icon").getImage());

        setVisible(true);

        gameTickTimer.start();
        changeTurnSaveWindowPosition();
        //See home planet
        desktopPane.see(civ.getStartingPlanet().getSystemID());
    }

    public void reload() {
        init();
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        if (JOptionPane.showConfirmDialog(this,
                LOCALE_MESSAGES.getMessage("game.save.query"), LOCALE_MESSAGES.getMessage("game.save"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            SaveGame game = new SaveGame(SaveGame.getSaveFolder());
            long before = System.currentTimeMillis();
            try {
                game.save(gameState);
            } catch (IOException ex) {
                ExceptionHandling.ExceptionMessageBox("IO exception while saving!", ex);
            } catch (IllegalArgumentException ex) {
                ExceptionHandling.ExceptionMessageBox("Illegal Argument exception while saving!", ex);
            } catch (IllegalAccessException ex) {
                ExceptionHandling.ExceptionMessageBox("Illegal Access exception while saving!", ex);
            }
            LOGGER.info("Time to save " + (System.currentTimeMillis() - before));
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
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        changeTurnSaveWindowPosition();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
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

    /**
     * Renders window and stuff.
     */
    public class CQSPDesktop extends JDesktopPane implements MouseMotionListener, MouseListener, MouseWheelListener {

        public static final int SIZE_OF_STAR_ON_SECTOR = 10;
        UniverseRenderer universeRenderer;
        private boolean isDragging = false;
        private Point startPoint;
        private double translateX = 0;
        private double translateY = 0;
        static final int DRAW_UNIVERSE = 0;
        static final int DRAW_STAR_SYSTEM = 1;
        int drawing = DRAW_UNIVERSE;
        private int drawingStarSystem = 0;
        private Galaxy universe;
        SystemRenderer systemRenderer;
        public static final int BOUNDS_SIZE = 1500;

        private int currentStarSystemSizeOfAU = 0;
        /**
         * Scale for the zoom. A scale of 1 is the current universe view, and it
         * can zoom to a max of 5.
         *
         */
        private double scale = 1.0f;

        public CQSPDesktop() {
            universe = gameState.getUniverse();
            universeRenderer = new UniverseRenderer(gameState, new Dimension(1500, 1500), civ);
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            //Turn on AA.
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            switch (drawing) {
                case DRAW_UNIVERSE:
                    setBackground(new Color(0, 0, 255));
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
            if (isDragging && SwingUtilities.isLeftMouseButton(e)) {
                translateX -= ((startPoint.x - e.getX()) * (scale));
                translateY -= ((startPoint.y - e.getY()) * (scale));
                startPoint = e.getPoint();
                repaint();
            } else if (isDragging && SwingUtilities.isRightMouseButton(e)) {
                //Set point of the start and end
                if (drawing == DRAW_STAR_SYSTEM) {
                    systemRenderer.setMeasureDistance(startPoint, e.getPoint());
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && mainInterfaceWindow != null) {
                //If universe, click
                switch (drawing) {
                    case DRAW_UNIVERSE:
                        //Get sector..
                        LOGGER.info("Checking for click");
                        sectorit:
                        for (int i = 0; i < universe.getStarSystemCount(); i++) {
                            //Check for vision
                            StarSystem sys = universe.getStarSystemObject(i);
                            if (Math.hypot((convertPointUniverse(sys.getX(), translateX) - e.getX()),
                                    (convertPointUniverse(sys.getY(), translateY) - e.getY())) < (SIZE_OF_STAR_ON_SECTOR / scale)) {
                                for (UniversePath p : civ.vision.keySet()) {
                                    if (p.getSystemID() == sys.getIndex() && civ.vision.get(p) > VisionTypes.UNDISCOVERED) {
                                        see(sys.getIndex());
                                        repaint();
                                        break sectorit;
                                    }
                                }
                            }
                        }
                        break;
                    case DRAW_STAR_SYSTEM:
                        StarSystem selected = universe.getStarSystemObject(drawingStarSystem);
                        for (int i = 0; i < selected.getBodyCount(); i++) {
                            Body body = selected.getBodyObject(i);
                            if (body instanceof Planet) {
                                Planet planet = (Planet) body;
                                double x = (translateX + planet.getX() * currentStarSystemSizeOfAU / 10_000_000 + BOUNDS_SIZE / 2) / scale;
                                double y = (translateY + planet.getY() * currentStarSystemSizeOfAU / 10_000_000 + BOUNDS_SIZE / 2) / scale;
                                if (Math.hypot(x - e.getX(), y - e.getY()) < 
                                        planet.getPlanetHeight() * 1.1 / SystemRenderer.PLANET_DIVISOR) {
                                    //PlanetInfoSheet d = new PlanetInfoSheet(planet, c);
                                    //add(d);
                                    //Check if scanned
                                    mainInterfaceWindow.setSelectedPlanet(planet, planet.hasScanned(civ.getId()));
                                    mainInterfaceWindow.setSelectedTab(1);

                                    break;
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
                if (mainInterfaceWindow != null) {
                    JPopupMenu popupMenu = generatePopupMenu(e);
                    popupMenu.show(this, e.getX(), e.getY());
                }
            }
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
                //Set point of the start and end
                if (drawing == DRAW_STAR_SYSTEM) {
                    systemRenderer.endMeasureDistance();
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        void see(int system) {
            drawingStarSystem = system;
            drawing = DRAW_STAR_SYSTEM;
            systemRenderer = new SystemRenderer(gameState, universe.getStarSystemObject(drawingStarSystem), universe, new Dimension(1500, 1500));
            currentStarSystemSizeOfAU = systemRenderer.sizeofAU;
            scale = 1;
            //Get Star position and position it like that...
            translateX = -1500 / 2 + getSize().width / 2;
            translateY = -1500 / 2 + getSize().height / 2;
            //Set the window size
            systemRenderer.setWindowSize(new Dimension(getWidth(), getHeight()));

            repaint();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            //Change scroll
            double scroll = (double) e.getUnitsToScroll();
            double scrollBefore = scale;
            double newScale = (Math.exp(scroll * 0.01) * scale);
            //Limit scale
            if (newScale > 0.00001 && newScale < 6) {
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
            translateX = -1500 / 2 + getSize().width / 2;
            translateY = -1500 / 2 + getSize().height / 2;
            scale = 1f;
        }

        private JPopupMenu generatePopupMenu(MouseEvent e) {
            //Show popup menu
            JPopupMenu popupMenu = new JPopupMenu();

            boolean overPlanet = false;
            Planet overWhat = null;
            //Show info and specific information of the sectors and stuff
            switch (drawing) {
                case DRAW_UNIVERSE:
                    //Get sector..
                    LOGGER.info("Checking for click");
                    sectorit:
                    for (int i = 0; i < universe.getStarSystemCount(); i++) {
                        //Check for vision
                        StarSystem sys = universe.getStarSystemObject(i);

                        if (Math.hypot((convertPointUniverse(sys.getX(), translateX) - e.getX()),
                                (convertPointUniverse(sys.getY(), translateY) - e.getY())) < (SIZE_OF_STAR_ON_SECTOR / scale)) {
                            
                            if (civ.vision.containsKey(new UniversePath(sys.getId())) && 
                                    civ.vision.get(new UniversePath(sys.getId())) > VisionTypes.UNDISCOVERED) {
                                JMenuItem systemInfo = 
                                        new JMenuItem(String.format(LOCALE_MESSAGES.getMessage("game.click.popup.starsystem"), sys.getId()));
                                systemInfo.addActionListener(a -> {
                                    see(sys.getId());
                                    repaint();
                                });
                                popupMenu.add(systemInfo);
                                break sectorit;

                            }
                        }
                    }
                    break;
                case DRAW_STAR_SYSTEM:
                    StarSystem selected = universe.getStarSystemObject(drawingStarSystem);
                    for (int i = 0; i < selected.getBodyCount(); i++) {
                        Body body = selected.getBodyObject(i);
                        if (body instanceof Planet) {
                            Planet planet = (Planet) body;
                            //Convert point...
                            double x = (translateX + (planet.getX()) * currentStarSystemSizeOfAU / 10_000_000 + BOUNDS_SIZE / 2) / scale;
                            double y = (translateY - (planet.getY()) * currentStarSystemSizeOfAU / 10_000_000 + BOUNDS_SIZE / 2) / scale;
                            
                            if (Math.hypot(x - e.getX(), y - e.getY())
                                    < (planet.getPlanetSize() / SystemRenderer.PLANET_DIVISOR)) {
                                
                                JMenuItem planetName = new JMenuItem();
                                planetName.addActionListener(a -> {
                                    mainInterfaceWindow.setSelectedPlanet(planet, true);
                                    mainInterfaceWindow.setSelectedTab(1);
                                });

                                if (planet.hasScanned(civ.getId())) {
                                    planetName.setText(String.format(LOCALE_MESSAGES.getMessage("game.click.popup.planet"), planet.getId()));
                                } else {
                                    planetName.setText(LOCALE_MESSAGES.getMessage("game.click.popup.planet.unexplored"));
                                }
                                popupMenu.add(planetName);
                                overPlanet = true;
                                overWhat = planet;
                                break;
                            }
                        }
                    }
                    break;
                //Also get ship
                default:
                    break;
            }

            JMenu selectedShips = new JMenu(LOCALE_MESSAGES.getMessage("game.click.popup.ship.selected"));
            //Get currently selected ships
            //Need to add a register...
            for (Ship s : playerRegister.getSelectedShips()) {
                JMenu men = new JMenu(s.toString());
                JMenuItem gohereMenu = new JMenuItem(LOCALE_MESSAGES.getMessage("game.click.popup.ship.goto"));
                gohereMenu.addActionListener(a -> {
                    //Move position
                    //Convert
                    //Check the location and where it is
                    if (drawing == DRAW_UNIVERSE) {
                        //Check if inside star system
                        //Convert
                        double gotoX = (e.getX() * scale - translateX - BOUNDS_SIZE / 2) / universeRenderer.sizeOfLTYR;
                        double gotoY = (e.getY() * scale - translateY - BOUNDS_SIZE / 2) / universeRenderer.sizeOfLTYR;

                        if (s.getLocation().getSystemID() > -1) {
                            //Then get quickest route to out of system, and do the things
                            //Ah well, get slope because the path can be direct...
                            StarSystem system = universe.getStarSystemObject(s.getLocation().getSystemID());
                            double slopeX = gotoX - system.getX();
                            double slopeY = gotoY - system.getY();
                            double slope = (slopeY / slopeX);
                            //Add move action

                            //Get distance to intersect
                            long x = (long) (slope * 10_000_000_000l);
                            //Get distance of ship to 
                            ShipMoveAction action = new ShipMoveAction(s);

                            //TODO: FIX SHIPS
                            action.setPosition(new SpacePoint(100l, x));

                            s.addAction(gameState, action);
                            //Add exit star system action
                            ExitStarSystemAction act = new ExitStarSystemAction(s);
                            s.addAction(gameState, act);
                        }
                        InterstellarTravelAction action = new InterstellarTravelAction(s);
                        action.setPositionX(gotoX);
                        action.setPositionY(gotoY);
                        s.addAction(gameState, action);
                    } else if (drawing == DRAW_STAR_SYSTEM) {
                        //Check if in star system
                        //Then check the goto position

                        long gotoX = (long) ((((e.getX() * scale) - BOUNDS_SIZE / 2 - translateX) * 10_000_000) / currentStarSystemSizeOfAU);
                        long gotoY = (long) ((((e.getY() * scale) - BOUNDS_SIZE / 2 - translateY) * 10_000_000) / currentStarSystemSizeOfAU);

                        //Get Location
                        ShipMoveAction action = new ShipMoveAction(s);
                        action.setPosition(new SpacePoint(gotoX, gotoY));

                        s.addAction(gameState, action);

                        //Add an extra action to move...
                        StarSystem sys = universe.getStarSystemObject(drawingStarSystem);
                        //if (Math.hypot(gotoX, gotoY) > (sys.getPlanet(sys.getPlanetCount() - 1).getOrbitalDistance() + 10 * 10_000_000)) {
                        // ExitStarSystemAction act = new ExitStarSystemAction(s);
                        //s.addAction(act);
                        //}
                    }
                });
                men.add(gohereMenu);

                //Check if orbiting a planet...
                if (overPlanet && overWhat != null) {
                    //Orbit it! 
                    JMenuItem orbiting = new JMenuItem(String.format(LOCALE_MESSAGES.getMessage("game.click.popup.ship.orbit"), overWhat.getName()));
                    final Planet p = overWhat;
                    orbiting.addActionListener(a -> {
                        //Move position
                        //Convert

                        //Get Location
                        ToOrbitAction action = new ToOrbitAction(s);
                        action.setPlanet(p);
                        s.addAction(gameState, action);
                    });
                    men.add(orbiting);
                }
                //If science ship
                long stype = s.getHull().getShipType();
                //Survey ship and over planet
                if (stype == 70 && overPlanet && !overWhat.hasScanned(civ.getId())) {
                    JMenuItem surveryor = new JMenuItem(LOCALE_MESSAGES.getMessage("game.click.popup.ship.survey"));
                    final Planet p = overWhat;

                    surveryor.addActionListener(a -> {
                        //Move position
                        //Get Location
                        ToOrbitAction orbitAction = new ToOrbitAction(s);
                        orbitAction.setPlanet(p);

                        ShipSurveyAction survey = new ShipSurveyAction(s);
                        survey.setProgressPerTick(5);
                        survey.setFinishedProgress(100);
                        survey.setToSurvey(p);
                        survey.setCivID(civ.getId());

                        s.addAction(gameState, orbitAction);
                        s.addAction(gameState, survey);
                    });
                    men.add(surveryor);
                }

                //Add a delete ship action thing
                JMenuItem deleteShipAction = new JMenuItem(LOCALE_MESSAGES.getMessage("game.click.popup.ship.remove.actions"));
                deleteShipAction.addActionListener(l -> {
                    s.commands.clear();
                });
                men.add(deleteShipAction);

                selectedShips.add(men);
            }

            //Add a delete all selected ships
            JMenuItem deleteSelectedShips = new JMenuItem(LOCALE_MESSAGES.getMessage("game.click.popup.ship.remove.ships"));
            deleteSelectedShips.addActionListener(a -> {
                playerRegister.getSelectedShips().clear();
            });

            popupMenu.add(selectedShips);
            popupMenu.add(deleteSelectedShips);
            return popupMenu;
        }

        private double convertPointUniverse(double pos, double translate) {
            return (pos * universeRenderer.sizeOfLTYR + translate + BOUNDS_SIZE / 2) / scale;
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
            StarSystem sys = universe.getStarSystemObject(desktopPane.drawingStarSystem);
            for (int i = 0; i < sys.getBodyCount(); i++) {
                Body planet = sys.getBodyObject(i);
                planet.orbit.setDegrees(degree);
                planet.setPoint(planet.orbit.toSpacePoint());
            }
        }
    }
}
