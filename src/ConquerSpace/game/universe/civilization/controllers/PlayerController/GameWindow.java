package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.UniversePath;
import ConquerSpace.game.ui.renderers.PlanetDrawStats;
import ConquerSpace.game.ui.renderers.SectorDrawStats;
import ConquerSpace.game.ui.renderers.SectorRenderer;
import ConquerSpace.game.ui.renderers.SystemDrawStats;
import ConquerSpace.game.ui.renderers.SystemRenderer;
import ConquerSpace.game.ui.renderers.UniverseRenderer;
import ConquerSpace.game.universe.civilization.VisionTypes;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
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
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Zyun
 */
public class GameWindow extends JFrame {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameWindow.class.getName());

    private CQSPDesktop desktopPane;
    private JMenuBar menuBar;
    
    private PlayerController controller;
    public GameWindow(Universe u, PlayerController controller) {
        this.controller = controller;
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
            desktopPane.see(u.getCivilization(0).getStartingPlanet().getSectorID(), u.getCivilization(0).getStartingPlanet().getSystemID());
        });
        seeHomePlanet.setAccelerator(KeyStroke.getKeyStroke((int) '9', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

        views.add(setToUniverseView);
        views.add(seeHomePlanet);
        
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
            addFrame(new CivInfoOverview(u.getCivilization(0), u));
        });
        ownCivInfo.add(allCivInfo);
        
        JMenu techonology = new JMenu("Techonology");
        JMenuItem seetechs = new JMenuItem("See Researched Techs");
        seetechs.addActionListener((e) -> {
            TechonologyViewer viewer = new TechonologyViewer(u, u.getCivilization(0));
            addFrame(viewer);
        });
        
        JMenuItem techResearcher = new JMenuItem("Research Techonologies");
        techResearcher.addActionListener(e -> {
            ResearchViewer viewer = new ResearchViewer(u.getCivilization(0));
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
        desktopPane.see(u.getCivilization(0).getStartingPlanet().getSectorID(), u.getCivilization(0).getStartingPlanet().getSystemID());
    }

    public void addFrame(JInternalFrame frame) {
        desktopPane.add(frame);
    }

    public class CQSPDesktop extends JDesktopPane implements MouseMotionListener, MouseListener, MouseWheelListener {

        UniverseRenderer universeRenderer;
        SectorRenderer[] sectorRenderers;
        private boolean isDragging = false;
        private Point startPoint;
        private int translateX = 0;
        private int translateY = 0;
        static final int DRAW_UNIVERSE = 0;
        static final int DRAW_SECTOR = 1;
        static final int DRAW_STAR_SYSTEM = 2;
        int drawing = DRAW_UNIVERSE;
        private int drawingSector = 0;
        private int drawingStarSystem = 0;
        private Universe universe;
        SystemRenderer systemRenderer;

        @Override
        protected void paintComponent(Graphics g) {
            switch (drawing) {
                case DRAW_UNIVERSE:
                    universeRenderer.drawUniverse(g, new Point(translateX, translateY));
                    break;
                case DRAW_SECTOR:
                    sectorRenderers[drawingSector].drawSector(g, new Point(translateX, translateY));
                    break;
                case DRAW_STAR_SYSTEM:
                    assert systemRenderer == null;
                    systemRenderer.drawStarSystem(g, new Point(translateX, translateY));
                    break;
                default:
                    break;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (isDragging && SwingUtilities.isRightMouseButton(e)) {
                translateX -= (startPoint.x - e.getX());
                translateY -= (startPoint.y - e.getY());
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
                if (drawing == DRAW_UNIVERSE) {
                    //Get sector..
                    LOGGER.info("Checking for click");
                    sectorit:
                    for (SectorDrawStats stats : universeRenderer.drawer.sectorDrawings) {
                        //Check for vision
                        if (Math.hypot(stats.getPosition().getX() + translateX - e.getX(), stats.getPosition().getY() - e.getY() + translateY) < stats.getRadius()) {
                            for (UniversePath p : universe.getCivilization(0).vision.keySet()) {
                                if (p.getSectorID() == stats.getId() && universe.getCivilization(0).vision.get(p) > VisionTypes.UNDISCOVERED) {
                                    LOGGER.info("Found sector!" + p.getSectorID());
                                    drawingSector = p.getSectorID();
                                    drawing = DRAW_SECTOR;
                                    translateX = 0;
                                    translateY = 0;
                                    repaint();
                                    break sectorit;
                                }
                            }
                        }
                    }

                } else if (drawing == DRAW_SECTOR) {
                    //Star system
                    //Get which system clicked.
                    LOGGER.info("Double clicked. Opening system");
                    for (SystemDrawStats stat : sectorRenderers[drawingSector].drawer.stats) {
                        if (Math.hypot(translateX + stat.getPosition().getX() - e.getX(), translateY + stat.getPosition().getY() - e.getY()) < 25 && universe.getCivilization(0).vision.get(stat.getPath()) > VisionTypes.UNDISCOVERED) {
                            LOGGER.info("Mouse clicked in system " + stat.getId() + "!");
                            systemRenderer = new SystemRenderer(universe.getSector(drawingSector).getStarSystem(stat.getId()), universe, new Dimension(1500, 1500));
                            drawing = DRAW_STAR_SYSTEM;
                            drawingStarSystem = stat.getId();
                            repaint();
                            break;
                        }
                    }
                } else if (drawing == DRAW_STAR_SYSTEM) {
                    for (PlanetDrawStats pstats : systemRenderer.drawer.stats.planetDrawStats) {
                        if (Math.hypot(translateX + pstats.getPos().x - e.getX(), translateY + pstats.getPos().y - e.getY()) < pstats.getSize()) {
                            LOGGER.trace("Mouse clicked in planet " + pstats.getID() + "!");
                            PlanetInfoSheet d = new PlanetInfoSheet(universe.getSector(drawingSector).getStarSystem(drawingStarSystem).getPlanet(pstats.getID()));
                            add(d);
                            break;
                        }
                    }
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
            //Pause game?
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        public CQSPDesktop(Universe u) {
            universe = u;
            universeRenderer = new UniverseRenderer(new Dimension(1500, 1500), u);
            sectorRenderers = new SectorRenderer[u.getSectorCount()];
            for (int i = 0; i < sectorRenderers.length; i++) {
                sectorRenderers[i] = new SectorRenderer(new Dimension(1500, 1500), u.getSector(i), u);
            }
            addMouseListener(this);
            addMouseMotionListener(this);
            addMouseWheelListener(this);
        }

        void see(int sector, int system) {
            drawingSector = sector;
            drawingStarSystem = system;
            systemRenderer = new SystemRenderer(universe.getSector(sector).getStarSystem(system), universe, new Dimension(1500, 1500));
            drawing = DRAW_STAR_SYSTEM;
            repaint();
        }

        void see(int sector) {
            drawingSector = sector;
            drawing = DRAW_SECTOR;
            repaint();
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (drawing == DRAW_STAR_SYSTEM && e.getUnitsToScroll() < -1) {
                drawing = DRAW_SECTOR;
                repaint();
            } else if (drawing == DRAW_SECTOR && e.getUnitsToScroll() < -1) {
                drawing = DRAW_UNIVERSE;
                repaint();
            }
        }
    }
}