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
package ConquerSpace.gui.game;

import ConquerSpace.ConquerSpace;
import ConquerSpace.game.GameController;
import ConquerSpace.game.StarDate;
import ConquerSpace.game.actions.ExitStarSystemAction;
import ConquerSpace.game.actions.InterstellarTravelAction;
import ConquerSpace.game.actions.ShipMoveAction;
import ConquerSpace.game.actions.ShipSurveyAction;
import ConquerSpace.game.actions.ToOrbitAction;
import ConquerSpace.game.civilization.Civilization;
import ConquerSpace.game.civilization.controllers.PlayerController;
import ConquerSpace.game.civilization.vision.VisionTypes;
import ConquerSpace.game.events.Event;
import ConquerSpace.game.save.SaveGame;
import ConquerSpace.game.ships.Ship;
import ConquerSpace.game.universe.SpacePoint;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.bodies.Body;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.StarSystem;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.gui.GUI;
import ConquerSpace.gui.renderers.SystemRenderer;
import ConquerSpace.gui.renderers.UniverseRenderer;
import ConquerSpace.util.ExceptionHandling;
import ConquerSpace.util.logging.CQSPLogger;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
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
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
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
 *
 * @author EhWhoAmI
 */
public class GameWindow extends JFrame implements GUI, WindowListener, ComponentListener {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameWindow.class.getName());

    public TurnSaveWindow tsWindow;

    private CQSPDesktop desktopPane;
    private JMenuBar menuBar;

    private Civilization c;

    private PlayerController controller;

    private MainInterfaceWindow mainInterfaceWindow;

    private Universe u;

    private Timer gameTickTimer;

    private StarDate d;

    public GameWindow(Universe u, PlayerController controller, Civilization c, StarDate d) {
        this.controller = controller;
        this.c = c;
        this.u = u;
        this.d = d;
        //Edit menu bar
        addWindowListener(this);
        init();
        
        setTitle("Conquer Space " + ConquerSpace.VERSION.getVersionCore());

        //Debug stuff
        //addFrame(new DegreeSetter());
        //A window to greet the user
        JOptionPane.showMessageDialog(this, "We have come to the technological stage where we can Conquer Space.\nOur destiny is with the stars.\n"
                + "May we live and prosper in these exciting new times.");
    }

    public void addFrame(JInternalFrame frame) {
        desktopPane.add(frame);
    }

    public void passEvent(Event e) {
        mainInterfaceWindow.passEvent(e);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void init() {
        desktopPane = new CQSPDesktop(u);
        menuBar = new JMenuBar();
        SwingWorker<MainInterfaceWindow, Void> interfaceWorker = new SwingWorker<MainInterfaceWindow, Void>() {
            @Override
            protected MainInterfaceWindow doInBackground() {
                return new MainInterfaceWindow(c, u);

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
                LOGGER.info("Done with making interface.");
            }

            /**
             * When concurrency doesn't work.
             */
            private void createFrame() {
                mainInterfaceWindow = new MainInterfaceWindow(c, u);
                addFrame(mainInterfaceWindow);
            }
        };

        interfaceWorker.execute();

        tsWindow = new TurnSaveWindow(d, u);

        //Remove mouse listeners for the turnsave window so that it can't be moved
        for (MouseListener listener : ((javax.swing.plaf.basic.BasicInternalFrameUI) tsWindow.getUI()).getNorthPane().getMouseListeners()) {
            ((javax.swing.plaf.basic.BasicInternalFrameUI) tsWindow.getUI()).getNorthPane().removeMouseListener(listener);
        }

        addFrame(tsWindow);

        //addFrame(newsWindow);
        JMenu windows = new JMenu("Windows");
        JMenuItem timeIncrementwindow = new JMenuItem("Main Window");
        timeIncrementwindow.addActionListener(a -> {
            if (mainInterfaceWindow != null) {
                mainInterfaceWindow.setVisible(true);
            } else {
                //Create it, ah well.
                mainInterfaceWindow = new MainInterfaceWindow(c, u);
                addFrame(mainInterfaceWindow);
                mainInterfaceWindow.setVisible(true);
            }
        });

        JMenuItem reloadWindows = new JMenuItem("Reload Windows");
        reloadWindows.addActionListener(a -> {
            //reload();
        });

        windows.add(timeIncrementwindow);
        //windows.add(reloadWindows);

        JMenu game = new JMenu("Game");
        JMenuItem pauseplayButton = new JMenuItem("Paused");
        pauseplayButton.addActionListener(a -> {
            if (tsWindow.isPaused()) {
                pauseplayButton.setText("Pause");
            } else {
                pauseplayButton.setText("Paused");
            }
            a = new ActionEvent(pauseplayButton, 0, "pauseplay");
            tsWindow.actionPerformed(a);
        });
        pauseplayButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
        game.add(pauseplayButton);

        JMenu views = new JMenu("View");
        JMenuItem setToUniverseView = new JMenuItem("Go to Universe View");
        setToUniverseView.addActionListener(a -> {
            desktopPane.drawing = CQSPDesktop.DRAW_UNIVERSE;
            desktopPane.repaint();
        });
        setToUniverseView.setAccelerator(KeyStroke.getKeyStroke((int) '1', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        JMenuItem seeHomePlanet = new JMenuItem("Home Planet");
        seeHomePlanet.addActionListener(a -> {
            desktopPane.see(GameController.playerCiv.getStartingPlanet().getSystemID());
        });
        seeHomePlanet.setAccelerator(KeyStroke.getKeyStroke((int) '9', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        JMenuItem recenter = new JMenuItem("Recenter");
        recenter.addActionListener(a -> {
            desktopPane.recenter();
            desktopPane.repaint();
        });

        views.add(setToUniverseView);
        views.add(seeHomePlanet);
        views.add(recenter);

        JMenu menu = new JMenu("Alerts");

        JMenuItem viewAlert = new JMenuItem("View Alerts");
        viewAlert.setAccelerator(KeyStroke.getKeyStroke('l'));
        viewAlert.addActionListener((e) -> {
            addFrame(AlertDisplayer.getInstance());
        });
        menu.add(viewAlert);

        JMenu ownCivInfo = new JMenu("Civilization");

        JMenuItem allCivInfo = new JMenuItem("My Civilization");
        allCivInfo.addActionListener((e) -> {
            //addFrame(new CivInfoOverview(u.getCivilization(0), u));
        });
        ownCivInfo.add(allCivInfo);

        JMenu techonology = new JMenu("Techonology");
        JMenuItem seetechs = new JMenuItem("See Researched Techs");
        seetechs.addActionListener((e) -> {
            //TechonologyViewer viewer = new TechonologyViewer(u, u.getCivilization(0));
            //addFrame(viewer);
        });

        JMenuItem techResearcher = new JMenuItem("Research Techonologies");
        techResearcher.addActionListener(e -> {
            //addFrame(viewer);
        });

        JMenu ships = new JMenu("Ships");

        JMenuItem allShips = new JMenuItem("All Ships");
        allShips.addActionListener(a -> {
            //addFrame(new ShipListManager(u, c));
        });

//        JMenuItem fleets = new JMenuItem("Fleets");
//
//        JMenuItem shipDesigner = new JMenuItem("Ship designer");
//        shipDesigner.addActionListener(a -> {
//            // addFrame(new ShipDesigner(c));
//        });
//
//        JMenuItem shipComponentDesigner = new JMenuItem("Ship Component Designer");
//        shipComponentDesigner.addActionListener(a -> {
//        });
//
//        JMenuItem satelliteDesigner = new JMenuItem("Satellite designer");
//        satelliteDesigner.addActionListener(a -> {
//            //addFrame(new SatelliteDesigner(c));
//        });
//
//        JMenuItem hullDesigner = new JMenuItem("Create new hull type");
//        hullDesigner.addActionListener(a -> {
//            //addFrame(new HullCreator(c));
//        });
        //ships.add(allShips);
        //ships.add(fleets);
        //ships.add(shipDesigner);
        //ships.add(shipComponentDesigner);
        //ships.add(satelliteDesigner);
        //ships.add(hullDesigner);
        JMenu resources = new JMenu("Resources");
        JMenuItem resourceIndex = new JMenuItem("Resources");
        resourceIndex.addActionListener(a -> {
            //addFrame(new ResourceManager(c));
        });

        resources.add(resourceIndex);

        menuBar.add(windows);
        menuBar.add(game);
        menuBar.add(views);
        menuBar.add(menu);
        //menuBar.add(ownCivInfo);
        //menuBar.add(techonology);
        menuBar.add(ships);
        menuBar.add(resources);

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

        //Set Icon6
        try {
            setIconImage(ImageIO.read(new File("assets/img/icon.png")));
        } catch (IOException ioe) {
        }

        setVisible(true);

        gameTickTimer.start();
        changeTurnSaveWindowPosition();
        //See home planet
        desktopPane.see(GameController.playerCiv.getStartingPlanet().getSystemID());
    }

    @Override
    public void refresh() {
    }

    @Override
    public void clean() {
        ///c.controller.
    }

    @Override
    public void reload() {
        init();
        clean();
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        if (JOptionPane.showConfirmDialog(this,
                "Do you want to save the game before exiting?", "Save game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            SaveGame game = new SaveGame(SaveGame.getSaveFolder());
            long before = System.currentTimeMillis();
            try {
                game.save(u, d);
            } catch (IOException ex) {
                ExceptionHandling.ExceptionMessageBox("IO EXCEPTION!", ex);
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
        private Universe universe;
        SystemRenderer systemRenderer;
        public static final int BOUNDS_SIZE = 1500;

        private int currentStarSystemSizeOfAU = 0;
        /**
         * Scale for the zoom. A scale of 1 is the current universe view, and it
         * can zoom to a max of 5.
         *
         */
        private double scale = 1.0f;

        public CQSPDesktop(Universe u) {
            universe = u;
            universeRenderer = new UniverseRenderer(new Dimension(1500, 1500), u, c);
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
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
            } else if (isDragging && SwingUtilities.isMiddleMouseButton(e)) {
                //Set point of the start and end
                if (drawing == DRAW_STAR_SYSTEM) {
                    systemRenderer.setMeasureDistance(startPoint, e.getPoint());
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            //Get location
//            if (drawing == DRAW_STAR_SYSTEM && systemRenderer != null) {
//                systemRenderer.setMousePosition(e.getPoint());
//            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                //If universe, click
                switch (drawing) {
                    case DRAW_UNIVERSE:
                        //Get sector..
                        LOGGER.info("Checking for click");
                        sectorit:
                        for (int i = 0; i < universe.getStarSystemCount(); i++) {
                            //Check for vision
                            StarSystem sys = universe.getStarSystem(i);
                            if (Math.hypot(((sys.getX() * universeRenderer.sizeOfLTYR + translateX + BOUNDS_SIZE / 2) / scale - e.getX()),
                                    ((sys.getY() * universeRenderer.sizeOfLTYR + translateY + BOUNDS_SIZE / 2) / scale - e.getY())) < (SIZE_OF_STAR_ON_SECTOR / scale)) {
                                for (UniversePath p : universe.getCivilization(0).vision.keySet()) {
                                    if (p.getSystemID() == sys.getId() && universe.getCivilization(0).vision.get(p) > VisionTypes.UNDISCOVERED) {
                                        see(sys.getId());
                                        repaint();
                                        break sectorit;
                                    }
                                }
                            }
                        }
                        break;
                    case DRAW_STAR_SYSTEM:
                        StarSystem selected = universe.getStarSystem(drawingStarSystem);
                        for (int i = 0; i < selected.bodies.size(); i++) {
                            Body body = selected.bodies.get(i);
                            if (body instanceof Planet) {
                                Planet planet = (Planet) body;
                                if (Math.hypot((translateX + (planet.getX()) * currentStarSystemSizeOfAU / 10_000_000 + BOUNDS_SIZE / 2) / scale - e.getX(),
                                        (translateY + (planet.getY()) * currentStarSystemSizeOfAU / 10_000_000 + BOUNDS_SIZE / 2) / scale - e.getY()) < planet.getPlanetHeight() * 1.1 / SystemRenderer.PLANET_DIVISOR) {
                                    //PlanetInfoSheet d = new PlanetInfoSheet(planet, c);
                                    //add(d);
                                    //Check if scanned
                                    mainInterfaceWindow.setSelectedPlanet(planet, planet.scanned.contains(c.getID()));
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
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //Started
            //System.out.println(MouseEvent.BUTTON1);
            isDragging = true;
            startPoint = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            isDragging = false;
            if (e.isPopupTrigger()) {
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
                            StarSystem sys = universe.getStarSystem(i);
                            if (Math.hypot(((sys.getX() * universeRenderer.sizeOfLTYR + translateX + BOUNDS_SIZE / 2) / scale - e.getX()),
                                    ((sys.getY() * universeRenderer.sizeOfLTYR + translateY + BOUNDS_SIZE / 2) / scale - e.getY())) < (SIZE_OF_STAR_ON_SECTOR / scale)) {
                                for (UniversePath p : universe.getCivilization(0).vision.keySet()) {
                                    if (p.getSystemID() == sys.getId() && universe.getCivilization(0).vision.get(p) > VisionTypes.UNDISCOVERED) {
                                        JMenuItem systemInfo = new JMenuItem("Star system: " + sys.getId());
                                        systemInfo.addActionListener(a -> {
                                            see(sys.getId());
                                            repaint();
                                        });
                                        popupMenu.add(systemInfo);
                                        break sectorit;
                                    }
                                }
                            }
                        }
                        break;
                    case DRAW_STAR_SYSTEM:
                        StarSystem selected = universe.getStarSystem(drawingStarSystem);
                        for (int i = 0; i < selected.bodies.size(); i++) {
                            Body body = selected.bodies.get(i);
                            if (body instanceof Planet) {
                                Planet planet = (Planet) body;
                                if (Math.hypot((translateX + (planet.getX()) * currentStarSystemSizeOfAU / 10_000_000 + BOUNDS_SIZE / 2) / scale - e.getX(),
                                        (translateY - (planet.getY()) * currentStarSystemSizeOfAU / 10_000_000 + BOUNDS_SIZE / 2) / scale - e.getY()) < (planet.getPlanetSize() / SystemRenderer.PLANET_DIVISOR)) {
                                    if (planet.scanned.contains(c.getID())) {
                                        JMenuItem planetName = new JMenuItem("Planet " + planet.getID());
                                        planetName.addActionListener(a -> {
                                            mainInterfaceWindow.setSelectedPlanet(planet, true);
                                            mainInterfaceWindow.setSelectedTab(1);
                                        });
                                        popupMenu.add(planetName);
                                    } else {
                                        JMenuItem text = new JMenuItem("Planet Unexplored");
                                        text.addActionListener(a -> {
                                            mainInterfaceWindow.setSelectedPlanet(planet, false);
                                            mainInterfaceWindow.setSelectedTab(1);
                                        });
                                        popupMenu.add(text);
                                    }
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
                JMenu selectedShips = new JMenu("Selected Ships");
                //Get currently selected ships
                for (Ship s : ((PlayerController) c.controller).selectedShips) {
                    JMenu men = new JMenu(s.toString());
                    JMenuItem gohereMenu = new JMenuItem("Go here");
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
                                StarSystem system = universe.getStarSystem(s.getLocation().getSystemID());
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

                                s.addAction(action);
                                //Add exit star system action
                                ExitStarSystemAction act = new ExitStarSystemAction(s);
                                s.addAction(act);
                            }
                            InterstellarTravelAction action = new InterstellarTravelAction(s);
                            action.setPositionX(gotoX);
                            action.setPositionY(gotoY);
                            s.addAction(action);
                        } else if (drawing == DRAW_STAR_SYSTEM) {
                            //Check if in star system
                            //Then check the goto position

                            long gotoX = (long) (((e.getX() * scale) - BOUNDS_SIZE / 2 - translateX) * 10_000_000) / currentStarSystemSizeOfAU;
                            long gotoY = (long) ((((e.getY() * scale) - BOUNDS_SIZE / 2 - translateY) * 10_000_000) / currentStarSystemSizeOfAU);

                            //Get Location
                            ShipMoveAction action = new ShipMoveAction(s);
                            action.setPosition(new SpacePoint(gotoX, gotoY));

                            s.addAction(action);

                            //Add an extra action to move...
                            StarSystem sys = universe.getStarSystem(drawingStarSystem);
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
                        JMenuItem orbiting = new JMenuItem("Orbit " + overWhat.getName());
                        final Planet p = overWhat;
                        orbiting.addActionListener(a -> {
                            //Move position
                            //Convert

                            //Get Location
                            ToOrbitAction action = new ToOrbitAction(s);
                            action.setPlanet(p);
                            s.addAction(action);
                        });
                        men.add(orbiting);
                    }
                    //If science ship
                    long stype = s.getHull().getShipType();
                    //Survey ship and over planet
                    if (stype == 70 && overPlanet && !overWhat.scanned.contains(c.getID())) {
                        JMenuItem surveryor = new JMenuItem("Survey planet");
                        final Planet p = overWhat;

                        surveryor.addActionListener(a -> {
                            //Move position
                            //Convert

                            //Get Location
                            ShipSurveyAction survey = new ShipSurveyAction(s);
                            survey.setProgressPerTick(5);
                            survey.setFinishedProgress(100);
                            survey.setToSurvey(p);
                            survey.setCivID(c.getID());

                            //Also orbit planet
                            //Actions.moveShip(s, c, gotoX, gotoY, universe);
                            ToOrbitAction orbitAction = new ToOrbitAction(s);
                            orbitAction.setPlanet(p);
                            s.addAction(orbitAction);
                            s.addAction(survey);
                        });
                        men.add(surveryor);
                    }

                    //Add a delete ship action thing
                    JMenuItem deleteShipAction = new JMenuItem("Delete Ship Actions");
                    deleteShipAction.addActionListener(l -> {
                        s.commands.clear();
                    });
                    men.add(deleteShipAction);

                    selectedShips.add(men);
                }

                //Add a delete all selected ships
                JMenuItem deleteSelectedShips = new JMenuItem("Delete Selected Ships");
                deleteSelectedShips.addActionListener(a -> {
                    ((PlayerController) c.controller).selectedShips.clear();
                });

                popupMenu.add(selectedShips);
                popupMenu.add(deleteSelectedShips);

                popupMenu.show(this, e.getX(), e.getY());
            }
            if (SwingUtilities.isMiddleMouseButton(e)) {
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
            systemRenderer = new SystemRenderer(universe.getStarSystem(drawingStarSystem), universe, new Dimension(1500, 1500));
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
            if (newScale > 0.000001) {
                scale = newScale;
                double msX = ((e.getX() * scale));
                double msY = ((e.getY() * scale));
                double scaleChanged = scale - scrollBefore;

                translateX += ((msX * scaleChanged)) / scale;
                translateY += ((msY * scaleChanged)) / scale;
            }
            //Now repaint
            repaint();
        }

        public void recenter() {
            translateX = -1500 / 2 + getSize().width / 2;
            translateY = -1500 / 2 + getSize().height / 2;
            scale = 1f;
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
            StarSystem sys = u.getStarSystem(desktopPane.drawingStarSystem);
            for (int i = 0; i < sys.bodies.size(); i++) {
                Body planet = sys.bodies.get(i);
                planet.orbit.setDegrees(degree);
                planet.setPoint(planet.orbit.toSpacePoint());
            }
        }
    }
}
