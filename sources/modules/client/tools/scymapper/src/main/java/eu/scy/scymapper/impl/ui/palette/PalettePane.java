package eu.scy.scymapper.impl.ui.palette;

import java.awt.AlphaComposite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.ILinkFactory;
import eu.scy.scymapper.api.INodeFactory;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.shapes.ILinkShape;
import eu.scy.scymapper.api.styling.ILinkStyle;
import eu.scy.scymapper.impl.controller.LinkConnectorController;
import eu.scy.scymapper.impl.model.SimpleLink;
import eu.scy.scymapper.impl.ui.ConceptMapPanel;
import eu.scy.scymapper.impl.ui.diagram.LinkView;
import eu.scy.scymapper.impl.ui.diagram.modes.ConnectMode;
import eu.scy.scymapper.impl.ui.diagram.modes.DragMode;
import eu.scy.scymapper.impl.ui.toolbar.BackgroundColorButton;
import eu.scy.scymapper.impl.ui.toolbar.ForegroundColorButton;
import eu.scy.scymapper.impl.ui.toolbar.RemoveConceptButton;

/**
 * User: Bjoerge Naess Date: 03.sep.2009 Time: 13:24:59
 */
public class PalettePane extends JToolBar {

    private final static Logger logger = Logger.getLogger(PalettePane.class);

    private ConceptMapPanel conceptMapPanel;

    private List<ILinkFactory> linkFactories;

    private List<INodeFactory> nodeFactories;

    private JToggleButton selectedButton;

    private MouseListener clickListener;

    private List<INodeFactory> connectorFactories;

    private IConceptMap conceptMap;

    // private FillStyleCheckbox opaqueCheckbox;
    // private volatile NodeColorChooserPanel nodeColorChooser;

    public PalettePane(IConceptMap conceptMap, ISCYMapperToolConfiguration conf, ConceptMapPanel conceptMapPanel) {
        this.setFloatable(false);
        this.conceptMap = conceptMap;
        this.conceptMapPanel = conceptMapPanel;
        this.linkFactories = conf.getLinkFactories();
        this.nodeFactories = conf.getNodeFactories();
        this.connectorFactories = conf.getConnectorFactories();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        createNodeButtons();
        createConnectorButtons();
        createLinkButtons();

        addSeparator();
        addSeparator();
        add(new BackgroundColorButton(conceptMapPanel.getDiagramView(), conceptMap.getDiagramSelectionModel()));
        addSeparator();
        add(new RemoveConceptButton(conceptMap, conceptMapPanel.getDiagramView()));

        // add(nodeStylePanel);
        JScrollPane nodeScrollPane = new JScrollPane(this);
    }

    private void createConnectorButtons() {

        for (final INodeFactory connectorFactory : connectorFactories) {
            final JToggleButton button = new JToggleButton(connectorFactory.getIcon());
            button.setHorizontalAlignment(JButton.CENTER);

            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    if (selectedButton != null)
                        selectedButton.setSelected(false);
                    selectedButton = button;
                    if (clickListener != null)
                        conceptMapPanel.getDiagramView().removeMouseListener(clickListener);

                    clickListener = new MouseAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            INodeModel node = connectorFactory.create();
                            int w = node.getWidth();
                            int h = node.getHeight();
                            node.setSize(new Dimension(w, h));
                            Point loc = new Point(e.getPoint());
                            loc.translate(w / -2, h / -2);
                            node.setLocation(loc);

                            conceptMapPanel.getDiagramView().getController().add(node);
                            conceptMapPanel.getDiagramView().removeMouseListener(this);
                            conceptMapPanel.getDiagramView().setCursor(null);
                            button.setSelected(false);
                        }
                    };
                    conceptMapPanel.getDiagramView().addMouseListener(clickListener);
                    conceptMapPanel.getDiagramView().setCursor(createNodeShapedCursor(connectorFactory));
                }
            });
            add(button);
        }
    }

    private void createLinkButtons() {
        for (final ILinkFactory linkFactory : linkFactories) {
            ILinkModel btnLink = linkFactory.create();

            final AddLinkButton button = new AddLinkButton(btnLink.getLabel(), btnLink.getShape());
            button.setHorizontalAlignment(JButton.CENTER);
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    if (selectedButton != null && selectedButton.equals(e.getSource())) {
                        selectedButton.setSelected(false);
                        conceptMapPanel.getDiagramView().setMode(new DragMode(conceptMapPanel.getDiagramView()));
                        selectedButton = null;
                        return;
                    }

                    if (selectedButton != null)
                        selectedButton.setSelected(false);

                    selectedButton = button;

                    ILinkModel link = new SimpleLink();
                    link.setTo(new Point(0, 0));
                    ILinkModel model = linkFactory.create();
                    link.setLabel(model.getLabel());
                    link.setShape(model.getShape());

                    final ConnectMode connectMode = new ConnectMode(conceptMapPanel.getDiagramView(), new LinkView(new LinkConnectorController(link), link), linkFactory);

                    conceptMapPanel.getDiagramView().setMode(connectMode);

                    connectMode.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (e.getID() == ConnectMode.CONNECTION_MADE) {
                                selectedButton.setSelected(false);
                                selectedButton = null;
                                conceptMapPanel.getDiagramView().setMode(new DragMode(conceptMapPanel.getDiagramView()));
                                connectMode.getTargetComponent().setCursor(new Cursor(Cursor.MOVE_CURSOR));
                            }
                        }
                    });
                }
            });
            add(button);
        }
    }

    public static ImageIcon getImageIcon(URL url) {
        // DEBUGLOGGER.info("loading "+url);
        if (url != null) {
            ImageIcon image = new ImageIcon(url);
            while (image.getImageLoadStatus() != MediaTracker.COMPLETE) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {}
            }
            return image;
        } else {
            return null;
        }
    }

    private void createNodeButtons() {

        for (final INodeFactory nodeFactory : nodeFactories) {

            final JToggleButton button = new JToggleButton(nodeFactory.getIcon());
            button.setFocusPainted(false);
            button.setHorizontalAlignment(JButton.CENTER);

            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    conceptMapPanel.getDiagramView().setNodeMode();

                    if (selectedButton != null)
                        selectedButton.setSelected(false);
                    selectedButton = button;
                    if (clickListener != null)
                        conceptMapPanel.getDiagramView().removeMouseListener(clickListener);

                    clickListener = new MouseAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            INodeModel node = nodeFactory.create();
                            int w = node.getWidth();
                            int h = node.getHeight();
                            node.setSize(new Dimension(w, h));
                            Point loc = new Point(e.getPoint());
                            loc.translate(w / -2, h / -2);
                            node.setLocation(loc);

                            conceptMapPanel.getDiagramView().getController().add(node);
                            conceptMapPanel.getDiagramView().removeMouseListener(this);
                            conceptMapPanel.getDiagramView().setCursor(null);
                            button.setSelected(false);
                        }
                    };
                    conceptMapPanel.getDiagramView().addMouseListener(clickListener);
                    conceptMapPanel.getDiagramView().setCursor(createNodeShapedCursor(nodeFactory));
                }
            });
            add(button);
        }

    }

    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return (AlphaComposite.getInstance(type, alpha));
    }

    Cursor createNodeShapedCursor(INodeFactory nodeFactory) {

        Toolkit tk = Toolkit.getDefaultToolkit();

        Icon icon = nodeFactory.getIcon();

        Dimension size = tk.getBestCursorSize(icon.getIconWidth(), icon.getIconWidth());

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();

        // Create an image that supports arbitrary levels of transparency
        BufferedImage i = gc.createCompatibleImage(size.width, size.height, Transparency.BITMASK);

        Graphics2D g2d = (Graphics2D) i.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Rectangle rect = new Rectangle(0, 0, size.width, size.height);
        icon.paintIcon(null, g2d, 0, 0);

        return tk.createCustomCursor(i, new Point(rect.width / 2, rect.height / 2), "Place shape here");
    }

    Cursor createLinkShapedCursor(ILinkFactory linkFactory) {

        Toolkit tk = Toolkit.getDefaultToolkit();

        ILinkModel linkModel = linkFactory.create();

        Dimension size = tk.getBestCursorSize(25, 25);

        ILinkStyle style = linkModel.getStyle();
        ILinkShape shape = linkModel.getShape();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();

        // Create an image that supports arbitrary levels of transparency
        BufferedImage i = gc.createCompatibleImage(size.width, size.height, Transparency.BITMASK);

        Graphics2D g2d = (Graphics2D) i.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(style.getForeground());
        // g2d.translate(x, y);

        shape.paint(g2d, new Point(0, (25 / 2) - 5), new Point(25, (25 / 2) + 5));

        return tk.createCustomCursor(i, new Point(size.width / 2, size.height / 2), "Place shape here");
    }
}
