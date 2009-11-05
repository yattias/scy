package eu.scy.scymapper.impl.ui.palette;

import eu.scy.scymapper.api.*;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.api.diagram.IDiagramSelectionListener;
import eu.scy.scymapper.api.diagram.IDiagramSelectionModel;
import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.impl.controller.LinkController;
import eu.scy.scymapper.impl.model.NodeModel;
import eu.scy.scymapper.impl.model.SimpleLink;
import eu.scy.scymapper.impl.ui.ConceptMapPanel;
import eu.scy.scymapper.impl.ui.diagram.LinkView;
import eu.scy.scymapper.impl.ui.diagram.modes.ConnectMode;
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
    private ConceptMapPanel diagramView;
    private List<ILinkType> linkProtoTypes;
    private List<IConceptPrototype> conceptPrototypes;
    private AddLinkButton selectedButton;
    //private FillStyleCheckbox opaqueCheckbox;
    //private volatile NodeColorChooserPanel nodeColorChooser;

    public PalettePane(IConceptMap conceptMap, ISCYMapperToolConfiguration conf, ConceptMapPanel diagramView) {
        this.conceptMap = conceptMap;
        this.diagramView = diagramView;
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
                    diagramView.getDiagramView().addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            INodeModel node = new NodeModel();
                            node.setLabel(conceptPrototype.getName());
                            node.setShape(conceptPrototype.getNodeShape());
                            node.setSize(new Dimension(conceptPrototype.getWidth(), conceptPrototype.getHeight()));
                            node.setStyle(conceptPrototype.getNodeStyle());
                            node.setLocation(new Point(e.getPoint()));
                            conceptMap.getDiagram().addNode(node);
                            diagramView.getDiagramView().removeMouseListener(this);
                            diagramView.getDiagramView().setCursor(null);
                            button.setSelected(false);
                        }
                    });
                    diagramView.getDiagramView().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
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

                    button.setSelected(true);

                    selectedButton = button;

                    ILinkModel link = new SimpleLink();
                    link.setTo(new Point(0, 0));
                    link.setLabel(linkType.getLabel());
                    link.setShape(linkType.getLinkShape());

                    diagramView.getDiagramView().setMode(new ConnectMode(diagramView.getDiagramView(), new LinkView(new LinkController(link), link)));

                    diagramView.getDiagramView().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
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
}
