package eu.scy.scymapper.impl.ui.palette;

import eu.scy.scymapper.api.*;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.api.diagram.IDiagramSelectionListener;
import eu.scy.scymapper.api.diagram.IDiagramSelectionModel;
import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.impl.controller.LinkController;
import eu.scy.scymapper.impl.model.NodeLinkModel;
import eu.scy.scymapper.impl.model.NodeModel;
import eu.scy.scymapper.impl.model.SimpleLink;
import eu.scy.scymapper.impl.ui.ConceptMapPanel;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;
import eu.scy.scymapper.impl.ui.diagram.LinkView;
import eu.scy.scymapper.impl.ui.diagram.NodeView;
import eu.scy.scymapper.impl.ui.diagram.modes.DragMode;
import eu.scy.scymapper.impl.ui.diagram.modes.IDiagramMode;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * User: Bjoerge Naess
 * Date: 03.sep.2009
 * Time: 13:24:59
 */
public class PalettePane extends JPanel {

    private IConceptMap conceptMap;
    private ConceptMapPanel conceptMapPanel;
    private List<ILinkType> linkProtoTypes;
    private List<IConceptPrototype> conceptPrototypes;
    private AddLinkButton selectedButton;
    //private FillStyleCheckbox opaqueCheckbox;
    //private volatile NodeColorChooserPanel nodeColorChooser;

    public PalettePane(IConceptMap conceptMap, ISCYMapperToolConfiguration conf, ConceptMapPanel conceptMapPanel) {
        this.conceptMap = conceptMap;
        this.conceptMapPanel = conceptMapPanel;
        this.linkProtoTypes = conf.getAvailableLinkTypes();
        this.conceptPrototypes = conf.getAvailableConceptTypes();
        initComponents();
    }

    private void initComponents() {

        setBorder(new TitledBorder("Palette"));
        setLayout(new GridLayout(0, 1));

//        nodeColorChooser = new NodeColorChooserPanel();
//        JPanel nodeStylePanel = new JPanel();
//        nodeStylePanel.add(nodeColorChooser);
//        nodeStylePanel.setBorder(BorderFactory.createTitledBorder("Style"));
//
//        opaqueCheckbox = new FillStyleCheckbox();
//        nodeStylePanel.add(opaqueCheckbox);

        JPanel nodePanel = new JPanel(new MigLayout("wrap 2", "[grow,fill]"));
        nodePanel.setBorder(BorderFactory.createTitledBorder("Add concept"));

        for (final IConceptPrototype conceptPrototype : conceptPrototypes) {
            final AddConceptButton button = new AddConceptButton(conceptPrototype);
            button.setHorizontalAlignment(JButton.LEFT);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    conceptMapPanel.getDiagramView().addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            INodeModel node = new NodeModel();
                            node.setLabel(conceptPrototype.getName());
                            node.setShape(conceptPrototype.getNodeShape());
                            int w = conceptPrototype.getWidth();
                            int h = conceptPrototype.getHeight();
                            node.setSize(new Dimension(w, h));
                            node.setStyle(conceptPrototype.getNodeStyle());
                            Point loc = new Point(e.getPoint());
                            loc.translate(w/-2, h/-2);
                            node.setLocation(loc);
                            conceptMap.getDiagram().addNode(node);
                            conceptMapPanel.getDiagramView().removeMouseListener(this);
                            conceptMapPanel.getDiagramView().setCursor(null);
                            button.setSelected(false);
                        }
                    });
                    conceptMapPanel.getDiagramView().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                }
            });
            nodePanel.add(button);
        }

        JPanel linkPanel = new JPanel(new MigLayout("wrap 2", "[grow,fill]"));
        linkPanel.setBorder(BorderFactory.createTitledBorder("Add link"));
        for (final ILinkType linkType : linkProtoTypes) {
            final AddLinkButton button = new AddLinkButton(linkType.getLabel(), linkType.getLinkShape());
            button.setHorizontalAlignment(JButton.LEFT);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    if (selectedButton != null) selectedButton.setSelected(false);

                    if (selectedButton != null && selectedButton.equals(e.getSource())) {
                        selectedButton.setSelected(false);
                        conceptMapPanel.getDiagramView().setMode(new DragMode(conceptMapPanel.getDiagramView()));
                        selectedButton = null;
                        return;
                    }

                    selectedButton = button;

                    ILinkModel link = new SimpleLink();
                    link.setTo(new Point(0, 0));
                    link.setLabel(linkType.getLabel());
                    link.setShape(linkType.getLinkShape());

                    conceptMapPanel.getDiagramView().setMode(new ConnectMode(conceptMapPanel.getDiagramView(), new LinkView(new LinkController(link), link)));

                    conceptMapPanel.getDiagramView().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
                }
            });
            linkPanel.add(button);
        }

        //add(nodeStylePanel);
        add(nodePanel);
        add(linkPanel);

    }

    private static class NodeColorChooserPanel extends JPanel implements ActionListener, IDiagramSelectionListener {
        private JButton bgColorButton;
        private JButton fgColorButton;
        private IDiagramSelectionModel selectionModel;

        public NodeColorChooserPanel() {
            setLayout(null);

            ColorChooser colorChooser = new ColorChooser();
            add(colorChooser);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if (source == fgColorButton)
                selectFgColor();
            else if (source == bgColorButton)
                selectBgColor();
        }

        @Override
        public void selectionChanged(IDiagramSelectionModel s) {

//            bgColorButton.setEnabled(selectionModel.hasSelection());
//            fgColorButton.setEnabled(selectionModel.hasSelection());
//            if (selectionModel.hasNodeSelection()) {
//                bgColorButton.setBackground(selectionModel.getSelectedNode().getStyle().getBackground());
//                fgColorButton.setBackground(selectionModel.getSelectedNode().getStyle().getForeground());
//            } else {
//                bgColorButton.setBackground(null);
//                fgColorButton.setBackground(null);
//            }
        }

        void selectFgColor() {
            if (selectionModel.hasSelection()) {
                Stack<INodeModel> selectedNodes = selectionModel.getSelectedNodes();
                Color c = JColorChooser.showDialog(this, "Choose a color", selectionModel.getSelectedNode().getStyle().getForeground());
                if (c == null) return;
                for (INodeModel node : selectedNodes) {
                    node.getStyle().setForeground(c);
                }
                fgColorButton.setBackground(c);
            }
        }

        void selectBgColor() {
            if (selectionModel.hasSelection()) {
                Stack<INodeModel> selectedNodes = selectionModel.getSelectedNodes();
                Color c = JColorChooser.showDialog(this, "Choose a color", selectionModel.getSelectedNode().getStyle().getBackground());
                if (c == null) return;
                for (INodeModel node : selectedNodes) {
                    node.getStyle().setBackground(c);
                }
                bgColorButton.setBackground(c);
            }
        }

        public void setSelectionModel(IDiagramSelectionModel selectionModel) {
            this.selectionModel = selectionModel;
            this.selectionModel.removeSelectionListener(this);
            this.selectionModel.addSelectionListener(this);
            selectionChanged(selectionModel);
        }
    }

    private static class FillStyleCheckbox extends JCheckBox implements ActionListener, IDiagramSelectionListener {
        private IDiagramSelectionModel selectionModel;

        public FillStyleCheckbox() {
            setText("Opaque");
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }

        @Override
        public void selectionChanged(IDiagramSelectionModel s) {
            setEnabled(selectionModel.hasNodeSelection());
            if (selectionModel.hasNodeSelection()) {

            }
        }

        public void setSelectionModel(IDiagramSelectionModel selectionModel) {
            this.selectionModel = selectionModel;
            this.selectionModel.removeSelectionListener(this);
            this.selectionModel.addSelectionListener(this);
            selectionChanged(selectionModel);
        }
    }

    class ConnectMode implements IDiagramMode {

        private ConceptDiagramView view;
        LinkView connector = null;

        public ConnectMode(ConceptDiagramView view, LinkView connector) {
            this.view = view;
            this.connector = connector;
            this.view.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            view.add(connector);
            view.setComponentZOrder(connector, 0);
        }

        private INodeModel sourceNode;
        private final MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                NodeView comp = (NodeView) e.getSource();
                sourceNode = comp.getModel();
                comp.setBorder(BorderFactory.createLineBorder(Color.green, 1));
                Point relPoint = e.getPoint();
                Point loc = new Point(sourceNode.getLocation());
                loc.translate(relPoint.x, relPoint.y);
                connector.setFrom(loc);
                connector.setTo(loc);
                connector.setVisible(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                connector.setVisible(false);
                NodeView node = (NodeView) e.getSource();
                node.setBorder(BorderFactory.createEmptyBorder());

                if (currentTarget != null) {
                    NodeLinkModel link = new NodeLinkModel(sourceNode, currentTarget);
                    ILinkModel connectorLink = connector.getModel();
                    link.setLabel(connectorLink.getLabel());
                    link.setShape(connectorLink.getShape());
                    link.setStyle(connectorLink.getStyle());
                    view.getModel().addLink(link);
                    view.remove(connector);
                    view.setMode(new DragMode(view));
                    if (PalettePane.this.selectedButton != null) {
                        PalettePane.this.selectedButton.setSelected(false);
                    }
                    node.setBorder(BorderFactory.createEmptyBorder());
                    getNodeViewForModel(currentTarget).setBorder(BorderFactory.createEmptyBorder());
                }
            }
        };
        private INodeModel currentTarget;
        private final MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                // The relative mouse position from the component x,y
                Point relPoint = e.getPoint();

                NodeView node = (NodeView) e.getSource();

                // Create the new location
                Point newLocation = node.getLocation();
                // Translate the newLocation with the relative point
                //newLocation.translate(relPoint.x, relPoint.y);
                newLocation.translate(relPoint.x, relPoint.y);
                connector.setTo(newLocation);

                INodeModel nodeAt = view.getModel().getNodeAt(newLocation);

                if (nodeAt != null && !nodeAt.equals(sourceNode)) {
                    currentTarget = nodeAt;
                    // Get the component for target node in order to paint its border
                    getNodeViewForModel(currentTarget).setBorder(BorderFactory.createLineBorder(Color.green, 1));

                    Point snap = currentTarget.getConnectionPoint(sourceNode.getCenterLocation());
                    //targetSnap.translate(relCenter.x, relCenter.y);
                    connector.setTo(snap);
                    connector.setFrom(sourceNode.getConnectionPoint(snap));
                }
                else if (currentTarget != null) {
                    getNodeViewForModel(currentTarget).setBorder(BorderFactory.createEmptyBorder());
                    connector.setTo(newLocation);
                }
                else connector.setTo(newLocation);
                if (nodeAt == null) currentTarget = null;
            }
        };

        private NodeView getNodeViewForModel(INodeModel node) {
            for (Component c : view.getComponents()) {
                if (c instanceof NodeView && ((NodeView) c).getModel().equals(node)) {
                    return (NodeView) c;
                }
            }
            return null;
        }
        @Override
        public MouseListener getMouseListener() {
            return mouseListener;
        }

        @Override
        public MouseMotionListener getMouseMotionListener() {
            return mouseMotionListener;
        }

        @Override
        public FocusListener getFocusListener() {
            return new FocusAdapter() {};
        }
    }

}
