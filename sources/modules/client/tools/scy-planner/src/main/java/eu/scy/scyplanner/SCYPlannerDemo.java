package eu.scy.scyplanner;

import eu.scy.scymapper.api.diagram.*;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.api.IConceptLinkModel;
import eu.scy.scymapper.impl.model.NodeModel;
import eu.scy.scymapper.impl.model.DefaultNodeStyle;
import eu.scy.scymapper.impl.shapes.concepts.*;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.scymapper.impl.DiagramModel;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramModel;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramView;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramController;
import eu.scy.scyplanner.impl.model.LearningActivitySpaceNodeModel;
import eu.scy.scyplanner.impl.model.ELONodeModel;
import eu.scy.scyplanner.impl.model.LearningActivitySpaceLinkModel;
import eu.scy.scyplanner.impl.shapes.LASShape;
import eu.scy.scyplanner.impl.shapes.ELOShape;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

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
    }

    public void start() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 900);

        diagramModel = getInitialPedagogicalPlanModel();

        diagramModel.addObserver(this);

        SCYPlannerDiagramView view = new SCYPlannerDiagramView(new SCYPlannerDiagramController(diagramModel), diagramModel);

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
        getContentPane().add(view, BorderLayout.CENTER);
        getContentPane().add(testButton, BorderLayout.PAGE_START);
        setVisible(true);

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

        connectNodes(pedagogicalPlan, orientationLas, producedByOrientationELO, "");
        connectNodes(pedagogicalPlan, producedByOrientationELO, conseptualisationLas, "");
        connectNodes(pedagogicalPlan, producedByConceptualisationInputToDesign, conseptualisationLas, "");
        connectNodes(pedagogicalPlan, producedByConceptualisationInputToDesign, designLas, "");
        connectNodes(pedagogicalPlan, designLas, producedByDesignELO, "");
        connectNodes(pedagogicalPlan, producedByDesignELO, buildLas, "");
        connectNodes(pedagogicalPlan, conseptualisationLas, producedByConceptualisationInputToExperiment, "");
        connectNodes(pedagogicalPlan, producedByConceptualisationInputToExperiment, experimentLas, "");
        connectNodes(pedagogicalPlan, buildLas, producedByBuildELO, "");
        connectNodes(pedagogicalPlan, producedByBuildELO, experimentLas, "");
        connectNodes(pedagogicalPlan, producedByConceptualisationInputToExperiment, experimentLas, "");
        connectNodes(pedagogicalPlan, producedByExperimentELO, experimentLas, "");
        connectNodes(pedagogicalPlan, producedByExperimentELO, evaluationLas, "");
        connectNodes(pedagogicalPlan, evaluationLas, designLas, "");
        connectNodes(pedagogicalPlan, evaluationLas, producedByEvaluationELO, "");
        connectNodes(pedagogicalPlan, producedByEvaluationELO, reflectionLas, "");
        connectNodes(pedagogicalPlan, reflectionLas, producedByReflectionELO, "");
        connectNodes(pedagogicalPlan, producedByReflectionELO, reportingLas, "");
        connectNodes(pedagogicalPlan, reportingLas, producedByReportingELO, "");


        return pedagogicalPlan;

    }

    private void addNode(SCYPlannerDiagramModel pedagogicalPlan, INodeModel node) {
        node.addObserver(this);
        pedagogicalPlan.addNode(node);
    }


    private INodeModel createLASElement(String name, Integer xPos, Integer yPos) {
        INodeModel las = new LearningActivitySpaceNodeModel();
        las.setStyle(new DefaultNodeStyle());
        las.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
        las.getStyle().setBackground(new Color(0xcc0000));
        //las.getStyle().setBackground(new GradientPaint(0, 0, 0xe3ecfe, 100, 100, 0x7da4dd, true));
        las.setShape(new LASShape());
        las.setLabel(name);
        las.setLocation(new Point(xPos, yPos));
        las.setSize(new Dimension(100, 100));
        return las;

    }

    private INodeModel createELOElement(String name, Integer xPos, Integer yPos) {
        INodeModel eloNodeModel = new ELONodeModel();
        eloNodeModel.setStyle(new DefaultNodeStyle());
        eloNodeModel.getStyle().setFillStyle(INodeStyle.FILLSTYLE_FILLED);
        //eloNodeModel.getStyle().setBackground(Color.GRAY);
        eloNodeModel.getStyle().setBackground(new Color(0x90b444)); 
        eloNodeModel.setShape(new ELOShape());
        eloNodeModel.setLabel(name);
        eloNodeModel.setLocation(new Point(xPos, yPos));
        eloNodeModel.setSize(new Dimension(30,30));
        
        return eloNodeModel;
    }

    private void connectNodes(SCYPlannerDiagramModel diagramModel, INodeModel from, INodeModel to, String label) {
        IConceptLinkModel link = new LearningActivitySpaceLinkModel(from, to);
        link.getStyle().setColor(new Color(0x4f81bc));
        link.getStyle().setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 6.0f));

        link.setShape(new Arrow());
        link.setLabel(label);
        diagramModel.addLink(link);
    }


    @Override
    public void linkAdded(ILinkModel link) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void linkRemoved(ILinkModel link) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nodeAdded(INodeModel n) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nodeRemoved(INodeModel n) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updated(IDiagramModel diagramModel) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nodeSelected(INodeModel n) {
        System.out.println("DiagramImplTest.nodeSelected");
    }

    @Override
    public void moved(INodeModel node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resized(INodeModel node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void labelChanged(INodeModel node) {
    }

    @Override
    public void styleChanged(INodeModel node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void shapeChanged(INodeModel node) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void nodeSelected(NodeModel conceptNode) {
        selectedNode = conceptNode;
        selectedLabel.setText("You clicked: " + conceptNode.getLabel());
    }
}

