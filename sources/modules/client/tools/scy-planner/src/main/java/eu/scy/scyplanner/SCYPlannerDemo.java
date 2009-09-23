package eu.scy.scyplanner;

import eu.scy.scymapper.api.diagram.*;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.IConceptLinkModel;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.impl.model.NodeModel;
import eu.scy.scymapper.impl.model.DefaultNodeStyle;
import eu.scy.scymapper.impl.shapes.concepts.*;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramModel;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramView;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramController;
import eu.scy.scyplanner.impl.model.LearningActivitySpaceNodeModel;
import eu.scy.scyplanner.impl.model.LearningActivitySpaceLinkModel;
import eu.scy.scyplanner.impl.shapes.LASShape;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.net.URL;
import java.io.IOException;

/**
 * User: Bjoerge Naess
 * Date: 28.aug.2009
 * Time: 12:28:20
 */
public class SCYPlannerDemo extends JFrame implements IDiagramModelObserver, INodeModelObserver {
    private IDiagramModel diagramModel;

    private INodeModel selectedNode;
    private JLabel selectedLabel;

    public SCYPlannerDemo() {
        super("SCYPlannerDemo Test");

        JTabbedPane tabbedPane = new JTabbedPane();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 748);
        this.setLocationRelativeTo(null);

        diagramModel = getInitialPedagogicalPlanModel();

        diagramModel.addObserver(this);

        SCYPlannerDiagramView view = new SCYPlannerDiagramView(new SCYPlannerDiagramController(diagramModel), diagramModel);
        tabbedPane.addTab("Test", view);

        JButton testButton = new JButton("Click me to change the shape, style and size of the last selected concept");
        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedNode != null) {
                    selectedNode.setShape(new Star());
                    selectedNode.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
                    selectedNode.getStyle().setBackground(new Color(0xff9900));
                }
            }
        });
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        getContentPane().add(testButton, BorderLayout.PAGE_START);

        selectedLabel = new JLabel("No node selected yet");
        getContentPane().add(selectedLabel, BorderLayout.PAGE_END);
    }

    private IDiagramModel getInitialPedagogicalPlanModel() {
        SCYPlannerDiagramModel pedagogicalPlan = new SCYPlannerDiagramModel();
        INodeModel orientationLas = createLASElement("Orientation", 10, 100);
        INodeModel conseptualisationLas  = createLASElement("Conceptualisation", 300, 10);
        INodeModel designLas  = createLASElement("Design", 550, 10);
        INodeModel buildLas  = createLASElement("Build", 700, 220);
        INodeModel experimentLas  = createLASElement("Experiment", 650, 400);
        INodeModel evaluationLas  = createLASElement("Evaluation", 300, 300);
        INodeModel reflectionLas  = createLASElement("Reflection", 250, 500);
        INodeModel reportingLas  = createLASElement("Reporting", 100, 600);

        INodeModel producedByOrientationELO = createELOElement("Orientation", 150, 100);
        INodeModel producedByConceptualisationInputToDesign = createELOElement("ELO", 450, 10);
        INodeModel producedByConceptualisationInputToExperiment = createELOElement("", 500, 300);
        INodeModel producedByDesignELO = createELOElement("", 650, 100);
        INodeModel producedByBuildELO = createELOElement("", 700, 350);
        INodeModel producedByExperimentELO = createELOElement("", 450, 400);
        INodeModel producedByEvaluationELO = createELOElement("", 350, 450);
        INodeModel producedByReflectionELO = createELOElement("", 200, 500);
        INodeModel producedByReportingELO = createELOElement("", 120, 700);

        addNode(pedagogicalPlan, orientationLas);
        addNode(pedagogicalPlan, conseptualisationLas);
        addNode(pedagogicalPlan, designLas);
        addNode(pedagogicalPlan, buildLas);
        addNode(pedagogicalPlan, experimentLas);
        addNode(pedagogicalPlan, evaluationLas);
        addNode(pedagogicalPlan, reflectionLas);
        addNode(pedagogicalPlan, reportingLas);
        addNode(pedagogicalPlan, producedByOrientationELO);
        addNode(pedagogicalPlan, producedByConceptualisationInputToDesign);
        addNode(pedagogicalPlan, producedByDesignELO);
        addNode(pedagogicalPlan, producedByConceptualisationInputToExperiment);
        addNode(pedagogicalPlan, producedByBuildELO);
        addNode(pedagogicalPlan, producedByExperimentELO);
        addNode(pedagogicalPlan, producedByEvaluationELO);
        addNode(pedagogicalPlan, producedByReflectionELO);
        addNode(pedagogicalPlan, producedByReportingELO);

        connectNodes(pedagogicalPlan, orientationLas, producedByOrientationELO);
        connectNodes(pedagogicalPlan, producedByOrientationELO, conseptualisationLas);
        connectNodes(pedagogicalPlan, producedByConceptualisationInputToDesign, conseptualisationLas);
        connectNodes(pedagogicalPlan, producedByConceptualisationInputToDesign, designLas);
        connectNodes(pedagogicalPlan, designLas, producedByDesignELO);
        connectNodes(pedagogicalPlan, producedByDesignELO, buildLas);
        connectNodes(pedagogicalPlan, conseptualisationLas, producedByConceptualisationInputToExperiment);
        connectNodes(pedagogicalPlan, producedByConceptualisationInputToExperiment, experimentLas);
        connectNodes(pedagogicalPlan, buildLas, producedByBuildELO);
        connectNodes(pedagogicalPlan, producedByBuildELO, experimentLas);
        connectNodes(pedagogicalPlan, producedByConceptualisationInputToExperiment, experimentLas);
        connectNodes(pedagogicalPlan, producedByExperimentELO, experimentLas);
        connectNodes(pedagogicalPlan, producedByExperimentELO, evaluationLas);
        connectNodes(pedagogicalPlan, evaluationLas, designLas);
        connectNodes(pedagogicalPlan, evaluationLas, producedByEvaluationELO);
        connectNodes(pedagogicalPlan, producedByEvaluationELO, reflectionLas);
        connectNodes(pedagogicalPlan, reflectionLas, producedByReflectionELO);
        connectNodes(pedagogicalPlan, producedByReflectionELO, reportingLas);
        connectNodes(pedagogicalPlan, reportingLas, producedByReportingELO);

        return pedagogicalPlan;
    }

    private void addNode(SCYPlannerDiagramModel pedagogicalPlan, INodeModel node) {
        node.addObserver(this);
        pedagogicalPlan.addNode(node);
    }


    private INodeModel createLASElement(String name, Integer xPos, Integer yPos) {
        //INodeModel las = new LearningActivitySpaceNodeModel();
        //las.setStyle(new DefaultNodeStyle());
        //las.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
        //las.getStyle().setBackground(new Color(0xcc0000));

        INodeModel las = new NodeModel();
        //eloNodeModel.setLabel("I'm a fried SVG egg");
        //eloNodeModel.setStyle(new DefaultNodeStyle());
        //eloNodeModel.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
        URL theurl = getClass().getResource("impl/shapes/LASShape.svg");

        try {

            INodeShape s = new SVGConcept(theurl);
            las.setShape(s);
        } catch (IOException e) {
            System.err.println("File not found: "+theurl);
        }

        //las.setShape(new LASShape());
        las.setLabel(name);
        
        las.setLocation(new Point(xPos, yPos));
        las.setSize(new Dimension(142, 73));
        return las;
    }

    private INodeModel createELOElement(String name, Integer xPos, Integer yPos) {

        INodeModel eloNodeModel = new NodeModel();

        URL theurl = getClass().getResource("impl/shapes/ELOShape.svg");

        try {
            INodeShape s = new SVGConcept(theurl);
            eloNodeModel.setShape(s);
        } catch (IOException e) {
            System.err.println("File not found: "+theurl);
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
    public void nodeSelected(INodeModel n) {
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
    public void nodeSelected(NodeModel conceptNode) {
        selectedNode = conceptNode;
        selectedLabel.setText("You clicked: " + conceptNode.getLabel());
    }
}