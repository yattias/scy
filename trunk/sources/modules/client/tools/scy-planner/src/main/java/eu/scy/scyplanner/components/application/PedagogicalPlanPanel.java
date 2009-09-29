package eu.scy.scyplanner.components.application;

import eu.scy.scymapper.api.diagram.*;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.IConceptLinkModel;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.impl.model.NodeModel;
import eu.scy.scymapper.impl.shapes.concepts.*;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramModel;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramView;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramController;
import eu.scy.scyplanner.impl.model.LearningActivitySpaceLinkModel;
import eu.scy.core.model.pedagogicalplan.Mission;
import eu.scy.core.model.pedagogicalplan.Scenario;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.net.URL;
import java.io.IOException;
import java.util.Observer;
import java.util.Observable;

/**
 * User: Bjoerge Naess
 * Date: 28.aug.2009
 * Time: 12:28:20
 */
public class PedagogicalPlanPanel extends JPanel implements IDiagramModelListener, INodeModelListener {
    private IDiagramModel diagramModel;

    private INodeModel selectedNode;
    private JLabel selectedLabel;

    public PedagogicalPlanPanel(final Mission mission, final Scenario scenario) {
        final JTabbedPane tabbedPane = new JTabbedPane();

        diagramModel = getInitialPedagogicalPlanModel(mission, scenario);

        diagramModel.addObserver(this);

        SCYPlannerDiagramView view = new SCYPlannerDiagramView(new SCYPlannerDiagramController(diagramModel), diagramModel);
        tabbedPane.addTab("Overview", view);
        tabbedPane.addTab("Mission", new JPanel());
        tabbedPane.addTab("Scenario", new JPanel());
        view.addObserver(new Observer() {
            public void update(Observable observable, Object object) {
                INodeModel model = (INodeModel) object;
                tabbedPane.addTab(model.getLabel(), new JPanel());
            }
        });

        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);

        selectedLabel = new JLabel("No node selected yet");
        add(selectedLabel, BorderLayout.PAGE_END);
    }

    private IDiagramModel getInitialPedagogicalPlanModel(final Mission mission, final Scenario scenario) {
        SCYPlannerDiagramModel pedagogicalPlan = new SCYPlannerDiagramModel();
        INodeModel orientationLas = createLASElement(scenario.getLearningActivitySpace().getName(), 10, 100);

        INodeModel producedByOrientationELO = createELOElement("ELO", 150, 100);

        addNode(pedagogicalPlan, orientationLas);
        addNode(pedagogicalPlan, producedByOrientationELO);

        connectNodes(pedagogicalPlan, orientationLas, producedByOrientationELO);

        return pedagogicalPlan;
    }

    private void addNode(SCYPlannerDiagramModel pedagogicalPlan, INodeModel node) {
        node.addObserver(this);
        pedagogicalPlan.addNode(node);
    }

    private INodeModel createLASElement(String name, Integer xPos, Integer yPos) {
        INodeModel las = new NodeModel();
        URL theurl = getClass().getResource("/eu/scy/scyplanner/impl/shapes/LASShape.svg");

        try {

            INodeShape s = new SVGConcept(theurl);
            las.setShape(s);
        } catch (IOException e) {
            System.err.println("File not found: /eu/scy/scyplanner/impl/shapes/LASShape.svg");
        }

        las.setLabel(name);

        las.setLocation(new Point(xPos, yPos));
        las.setSize(new Dimension(142, 73));
        return las;
    }

    private INodeModel createELOElement(String name, Integer xPos, Integer yPos) {

        INodeModel eloNodeModel = new NodeModel();

        URL theurl = getClass().getResource("/eu/scy/scyplanner/impl/shapes/ELOShape.svg");

        try {
            INodeShape s = new SVGConcept(theurl);
            eloNodeModel.setShape(s);
        } catch (IOException e) {
            System.err.println("File not found /eu/scy/scyplanner/impl/shapes/ELOShape.svg");
        }

        eloNodeModel.setLocation(new Point(xPos, yPos));
        eloNodeModel.setSize(new Dimension(75,75));
        eloNodeModel.setLabelHidden(true);

        return eloNodeModel;
    }

    private void connectNodes(SCYPlannerDiagramModel diagramModel, INodeModel from, INodeModel to) {
        IConceptLinkModel link = new LearningActivitySpaceLinkModel(from, to);
        link.setLabelHidden(true);
        link.getStyle().setColor(new Color(0x4f81bc));
        link.getStyle().setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 6.0f));

        link.setShape(new Arrow());
        diagramModel.addLink(link);
    }

    @Override
    public void linkAdded(ILinkModel link) {
    }

    @Override
    public void linkRemoved(ILinkModel link) {
    }

    @Override
    public void nodeAdded(INodeModel n) {
    }

    @Override
    public void nodeRemoved(INodeModel n) {
    }

    @Override
    public void updated(IDiagramModel diagramModel) {
    }

    @Override
    public void moved(INodeModel node) {
    }

    @Override
    public void resized(INodeModel node) {
    }

    @Override
    public void labelChanged(INodeModel node) {
    }

    @Override
    public void styleChanged(INodeModel node) {
    }

    @Override
    public void shapeChanged(INodeModel node) {
    }

    @Override
    public void nodeSelected(INodeModel conceptNode) {
    }
}