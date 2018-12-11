package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.Globals;
import ConquerSpace.game.UniversePath;
import ConquerSpace.game.ui.renderers.PlanetDrawStats;
import ConquerSpace.game.ui.renderers.SystemDrawStats;
import ConquerSpace.game.ui.renderers.SystemRenderer;
import ConquerSpace.game.ui.renderers.UniverseRenderer2;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.civilization.controllers.LimitedUniverse;
import ConquerSpace.util.CQSPLogger;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
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
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Zyun
 */
public class GameWindow extends JFrame {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameWindow.class.getName());

    private CQSPDesktop desktopPane;
    private JMenuBar menuBar;

    private Civilization c;

    private PlayerController controller;

    public GameWindow(LimitedUniverse u, PlayerController controller, Civilization c) {
        this.controller = controller;
        this.c = c;
        desktopPane = new CQSPDesktop(u);
        menuBar = new JMenuBar();

        //Edit menu bar
        JMenu windows = new JMenu("Windows");

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
            desktopPane.see(c.getStartingPlanet().getSystemID());
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
            addFrame(new CivInfoOverview(c, u));
        });
        ownCivInfo.add(allCivInfo);

        JMenu techonology = new JMenu("Techonology");
        JMenuItem seetechs = new JMenuItem("See Researched Techs");
        seetechs.addActionListener((e) -> {
            TechonologyViewer viewer = new TechonologyViewer(c, u);
            addFrame(viewer);
        });

        JMenuItem techResearcher = new JMenuItem("Research Techonologies");
        techResearcher.addActionListener(e -> {
            ResearchViewer viewer = new ResearchViewer(c);
            addFrame(viewer);
        });

        techResearcher.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
        techonology.add(techResearcher);
        techonology.add(seetechs);

        menuBar.add(windows);
        menuBar.add(game);
        menuBar.add(views);
        menuBar.add(menu);
        menuBar.add(ownCivInfo);
        menuBar.add(techonology);
        desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        //desktopPane.setBackground(Color.cyan);
        setJMenuBar(menuBar);
        setContentPane(desktopPane);

        setSize(getToolkit().getScreenSize());
        setVisible(true);

        //See home planet
        desktopPane.see(c.getStartingPlanet().getSystemID());
    }

    public void addFrame(JInternalFrame frame) {
        desktopPane.add(frame);
    }

    /**
     * Renders window and stuff.
     */
    public class CQSPDesktop extends JDesktopPane implements MouseMotionListener, MouseListener, MouseWheelListener, ComponentListener {

        public static final int SIZE_OF_STAR_ON_SECTOR = 25;
        UniverseRenderer2 universeRenderer;
        private boolean isDragging = false;
        private Point startPoint;
        private int translateX = 0;
        private int translateY = 0;
        static final int DRAW_UNIVERSE = 0;
        static final int DRAW_STAR_SYSTEM = 1;
        int drawing = DRAW_UNIVERSE;
        private int drawingStarSystem = 0;
        private LimitedUniverse universe;
        SystemRenderer systemRenderer;

        private int screenRefreshRate;
        
        private Timer updater;
        
        /**
         * Scale for the zoom. A scale of 1 is the current universe view, and it
         * can zoom to a max of 5.
         *
         */
        private float scale = 1.0f;

        @Override
        protected void paintComponent(Graphics g) {
            switch (drawing) {
                case DRAW_UNIVERSE:
                    universeRenderer.drawUniverse(g, new Point(translateX, translateY), scale);
                    break;
                case DRAW_STAR_SYSTEM:
                    assert systemRenderer == null;
                    systemRenderer.drawStarSystem(g, new Point(translateX, translateY), scale);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (isDragging && SwingUtilities.isRightMouseButton(e)) {
                translateX -= ((startPoint.x - e.getX()) * (1 / scale));
                translateY -= ((startPoint.y - e.getY()) * (1 / scale));
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
                        for (SystemDrawStats stats : universeRenderer.drawer.systemDrawings) {
                            //Check for vision
                            if (Math.hypot(((stats.getPosition().getX() + translateX) * scale - e.getX()),
                                    ((stats.getPosition().getY() + translateY) * scale - e.getY())) < (SIZE_OF_STAR_ON_SECTOR * scale)) {
                                for (UniversePath p : c.vision.keySet()) {
                                    if (p.getSystemID() == stats.getId() && c.vision.get(p) > VisionTypes.UNDISCOVERED) {
                                        LOGGER.info("Found system!" + p.getSystemID());
                                        drawingStarSystem = p.getSystemID();
                                        systemRenderer = new SystemRenderer(universe.getStarSystem(drawingStarSystem), universe, new Dimension(1500, 1500));
                                        drawing = DRAW_STAR_SYSTEM;
                                        translateX = 0;
                                        translateY = 0;
                                        repaint();
                                        break sectorit;
                                    }
                                }
                            }
                        }
                        break;
                    case DRAW_STAR_SYSTEM:
                        for (PlanetDrawStats pstats : systemRenderer.drawer.stats.planetDrawStats) {
                            if (Math.hypot((translateX + pstats.getPos().x) * scale - e.getX(),
                                    (translateY + pstats.getPos().y) * scale - e.getY()) < pstats.getSize()) {
                                LOGGER.trace("Mouse clicked in planet " + pstats.getID() + "!");
                                PlanetInfoSheet d = new PlanetInfoSheet(universe.getStarSystem(drawingStarSystem).getPlanet(pstats.getID()), c);
                                add(d);
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
            isDragging = true;
            startPoint = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            isDragging = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        public CQSPDesktop(LimitedUniverse u) {
            universe = u;
            universeRenderer = new UniverseRenderer2(new Dimension(1500, 1500), u);
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
            addComponentListener(this);

            //Set update list
            //Update the graphics every now and then.
            //Get refresh rate (milliseconds)
            screenRefreshRate = Integer.parseInt(Globals.settings.getProperty("screen.refresh"));
            updater = new Timer(screenRefreshRate, a -> {
                //Updater content here...
                //Detect the thing that is being shown
                switch (drawing) {
                    case DRAW_STAR_SYSTEM:
                        systemRenderer.refresh();
                        break;
                    case DRAW_UNIVERSE:
                        universeRenderer.refresh();
                        break;
                }
            });
            updater.setRepeats(true);
            updater.start();
        }

        void see(int system) {
            drawingStarSystem = system;
            drawing = DRAW_STAR_SYSTEM;
            systemRenderer = new SystemRenderer(universe.getStarSystem(drawingStarSystem), universe, new Dimension(1500, 1500));
            repaint();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            //Change scroll
            float scroll = (float) e.getUnitsToScroll();
            if ((scale + (scroll / 10)) > 0) {
                scale += (scroll / 10);
                //Recenter scroll so that it scrolls on the center of the window.
                //getWidth()/2;
                //getHeight()/2;
            }

            //Now repaint
            repaint();
        }

        public void recenter() {
            translateX = 0;
            translateY = 0;
            scale = 1f;
        }

        @Override
        public void componentResized(ComponentEvent e) {
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
    }
}
