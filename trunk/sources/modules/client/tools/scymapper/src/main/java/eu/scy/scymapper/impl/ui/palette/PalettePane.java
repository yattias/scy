package eu.scy.scymapper.impl.ui.palette;

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
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * User: Bjoerge Naess Date: 03.sep.2009 Time: 13:24:59
 */
public class PalettePane extends JPanel {

    private final static Logger logger = Logger.getLogger(PalettePane.class);

    private ConceptMapPanel conceptMapPanel;

    private List<ILinkFactory> linkFactories;

    private List<INodeFactory> nodeFactories;

    private JToggleButton selectedButton;

    private MouseListener clickListener;

    private List<INodeFactory> connectorFactories;

    // private FillStyleCheckbox opaqueCheckbox;
    // private volatile NodeColorChooserPanel nodeColorChooser;

    public PalettePane(IConceptMap conceptMap, ISCYMapperToolConfiguration conf, ConceptMapPanel conceptMapPanel) {
        this.conceptMapPanel = conceptMapPanel;
        this.linkFactories = conf.getLinkFactories();
        this.nodeFactories = conf.getNodeFactories();
        this.connectorFactories = conf.getConnectorFactories();
        initComponents();
    }

    private void initComponents() {
        // setLayout(new MigLayout("wrap, center", "[grow,fill]"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        createNodeButtons();
        createConnectorButtons();
        createLinkButtons();

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

    private void createNodeButtons() {

        for (final INodeFactory nodeFactory : nodeFactories) {

            final JToggleButton button = new JToggleButton(nodeFactory.getIcon());
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
