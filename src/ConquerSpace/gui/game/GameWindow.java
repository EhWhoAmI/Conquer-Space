package ConquerSpace.gui.game;

import ConquerSpace.game.GameController;
import ConquerSpace.game.StarDate;
import ConquerSpace.game.actions.Actions;
import ConquerSpace.game.actions.ExitStarSystemAction;
import ConquerSpace.game.actions.InterstellarTravelAction;
import ConquerSpace.game.actions.ShipMoveAction;
import ConquerSpace.game.actions.ShipSurveyAction;
import ConquerSpace.game.actions.ToOrbitAction;
import ConquerSpace.game.save.SaveGame;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.PlayerController;
import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.gui.GUI;
import ConquerSpace.gui.renderers.SystemRenderer;
import ConquerSpace.gui.renderers.UniverseRenderer;
import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.ExceptionHandling;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Zyun
 */
public class GameWindow extends JFrame implements GUI, WindowListener {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameWindow.class.getName());

    private CQSPDesktop desktopPane;
    private JMenuBar menuBar;

    private Civilization c;

    private PlayerController controller;

    private MainInterfaceWindow mainInterfaceWindow;

    private NewsWindow newsWindow;

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
        //A window to greet the user
        JOptionPane.showMessageDialog(this, "We have come to the technological stage where we can Conquer Space.\nOur destiny is with the stars.\n"
                + "May we live and prosper in these exciting new times.");
    }

    public void addFrame(JInternalFrame frame) {
        desktopPane.add(frame);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void init() {
        desktopPane = new CQSPDesktop(u);
        menuBar = new JMenuBar();

        mainInterfaceWindow = new MainInterfaceWindow(c, u);
        newsWindow = new NewsWindow(c);
        addFrame(mainInterfaceWindow);
        addFrame(newsWindow);
        JMenu windows = new JMenu("Windows");
        JMenuItem timeIncrementwindow = new JMenuItem("Main Window");
        timeIncrementwindow.addActionListener(a -> {
            mainInterfaceWindow.setVisible(true);
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
            if (controller.tsWindow.isPaused()) {
                pauseplayButton.setText("Pause");
            } else {
                pauseplayButton.setText("Paused");
            }
            a = new ActionEvent(pauseplayButton, 0, "pauseplay");
            controller.tsWindow.actionPerformed(a);
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
            desktopPane.see(u.getCivilization(0).getStartingPlanet().getSystemID());
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

        //techResearcher.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
        //techonology.add(techResearcher);
        //techonology.add(seetechs);
        JMenu ships = new JMenu("Ships");

        JMenuItem allShips = new JMenuItem("All Ships");
        allShips.addActionListener(a -> {
            //addFrame(new ShipListManager(u, c));
        });

        JMenuItem fleets = new JMenuItem("Fleets");

        JMenuItem shipDesigner = new JMenuItem("Ship designer");
        shipDesigner.addActionListener(a -> {
            // addFrame(new ShipDesigner(c));
        });

        JMenuItem shipComponentDesigner = new JMenuItem("Ship Component Designer");
        shipComponentDesigner.addActionListener(a -> {
        });

        JMenuItem satelliteDesigner = new JMenuItem("Satellite designer");
        satelliteDesigner.addActionListener(a -> {
            //addFrame(new SatelliteDesigner(c));
        });

        JMenuItem hullDesigner = new JMenuItem("Create new hull type");
        hullDesigner.addActionListener(a -> {
            //addFrame(new HullCreator(c));
        });

        //ships.add(allShips);
        ships.add(fleets);
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
        gameTickTimer = new Timer(100, a -> {
            mainInterfaceWindow.update();
            newsWindow.update();
            desktopPane.repaint();
        });

        gameTickTimer.setRepeats(true);
        gameTickTimer.start();

        desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        //desktopPane.setBackground(Color.cyan);
        setJMenuBar(menuBar);
        setContentPane(desktopPane);
        Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();

        setSize(rect.width, rect.height);
        setVisible(true);

        //See home planet
        desktopPane.see(u.getCivilization(0).getStartingPlanet().getSystemID());
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
            SaveGame game = new SaveGame(System.getProperty("user.dir") + "/save/");
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
        }
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
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

        @Override
        protected void paintComponent(Graphics g) {
            switch (drawing) {
                case DRAW_UNIVERSE:
                    setBackground(new Color(0, 0, 255));
                    universeRenderer.drawUniverse(g, translateX, translateY, scale);
                    break;
                case DRAW_STAR_SYSTEM:
                    assert systemRenderer == null;
                    setBackground(Color.BLACK);
                    systemRenderer.drawStarSystem(g, translateX, translateY, scale);
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
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
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
                        for (int i = 0; i < universe.getStarSystem(drawingStarSystem).getPlanetCount(); i++) {
                            Planet planet = universe.getStarSystem(drawingStarSystem).getPlanet(i);
                            if (Math.hypot((translateX + (planet.getX()) * currentStarSystemSizeOfAU / 10_000_000 + BOUNDS_SIZE / 2) / scale - e.getX(),
                                    (translateY + (planet.getY()) * currentStarSystemSizeOfAU / 10_000_000 + BOUNDS_SIZE / 2) / scale - e.getY()) < planet.getPlanetSize() / SystemRenderer.PLANET_DIVISOR) {
                                //PlanetInfoSheet d = new PlanetInfoSheet(planet, c);
                                //add(d);
                                //Check if scanned
                                mainInterfaceWindow.setSelectedPlanet(planet, planet.scanned.contains(c.getID()));
                                mainInterfaceWindow.setSelectedTab(1);

                                break;
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
                        for (int i = 0; i < universe.getStarSystem(drawingStarSystem).getPlanetCount(); i++) {
                            Planet planet = universe.getStarSystem(drawingStarSystem).getPlanet(i);
                            if (Math.hypot((translateX + (planet.getX()) * currentStarSystemSizeOfAU / 10_000_000 + BOUNDS_SIZE / 2) / scale - e.getX(),
                                    (translateY + (planet.getY()) * currentStarSystemSizeOfAU / 10_000_000 + BOUNDS_SIZE / 2) / scale - e.getY()) < (planet.getPlanetSize() / SystemRenderer.PLANET_DIVISOR)) {
                                if (planet.scanned.contains(c.getID())) {
                                    JMenuItem planetName = new JMenuItem("Planet " + planet.getId());
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
                                action.setPosition(new ConquerSpace.game.universe.Point(100l, x));

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
                            action.setPosition(new ConquerSpace.game.universe.Point(gotoX, gotoY));

                            s.addAction(action);

                            //Add an extra action to move...
                            StarSystem sys = universe.getStarSystem(drawingStarSystem);
                            if (Math.hypot(gotoX, gotoY) > (sys.getPlanet(sys.getPlanetCount() - 1).getOrbitalDistance() + 10 * 10_000_000)) {
                                ExitStarSystemAction act = new ExitStarSystemAction(s);
                                s.addAction(act);
                            }
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
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        public CQSPDesktop(Universe u) {
            universe = u;
            universeRenderer = new UniverseRenderer(new Dimension(1500, 1500), u);
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
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
            repaint();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            //Change scroll
            double scroll = (double) e.getUnitsToScroll();
            double scrollBefore = scale;
            double newScale = (Math.exp(scroll * 0.01) * scale);
            if (newScale > 0) {
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
}
