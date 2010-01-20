package eu.scy.scyplanner.components.demo;

import eu.scy.scymapper.api.diagram.model.*;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.impl.model.NodeModel;
import eu.scy.scymapper.impl.shapes.concepts.RoundRectangle;
import eu.scy.scymapper.impl.shapes.concepts.SVGConcept;
import eu.scy.scymapper.impl.shapes.concepts.Star;
import eu.scy.scymapper.impl.shapes.links.Arrow;
import eu.scy.scyplanner.application.Strings;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramController;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramModel;
import eu.scy.scyplanner.impl.diagram.SCYPlannerDiagramView;
import eu.scy.scyplanner.impl.model.LearningActivitySpaceLinkModel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * User: Bjoerge Naess
 * Date: 28.aug.2009
 * Time: 12:28:20
 */
public class SCYPlannerDemo extends JPanel {
    private IDiagramModel diagramModel;

    private INodeModel selectedNode;
    private JLabel selectedLabel;

    public SCYPlannerDemo() {
        final JTabbedPane tabbedPane = new JTabbedPane();

        diagramModel = getInitialPedagogicalPlanModel();

		// The view should be created before nodes are added to the model because it creates its node views when
		// IDiagramListener.nodeAdded is invoked by the model
        SCYPlannerDiagramView view = new SCYPlannerDiagramView(new SCYPlannerDiagramController(diagramModel), diagramModel);
        tabbedPane.addTab(Strings.getString("Test"), view);
        view.addObserver(new Observer() {
            public void update(Observable observable, Object object) {
                INodeModel model = (INodeModel) object;
                tabbedPane.addTab(model.getLabel(), new JPanel());
            }
        });

        /*JButton testButton = new JButton("Click me to change the shape, style and size of the last selected concept");
        testButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedNode != null) {
                    selectedNode.setShape(new Star());
                    selectedNode.getStyle().setOpaque(INodeStyle.FILLSTYLE_FILLED);
                    selectedNode.getStyle().setBackground(new Color(0xff9900));
                }
            }
        });*/
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        //add(testButton, BorderLayout.PAGE_START);

        selectedLabel = new JLabel(Strings.getString("No node selected yet"));
        add(selectedLabel, BorderLayout.PAGE_END);
    }

    private IDiagramModel getInitialPedagogicalPlanModel() {
        SCYPlannerDiagramModel pedagogicalPlan = new SCYPlannerDiagramModel();
        INodeModel orientationLas = createLASElement("Orientation", 10, 100, 50, 50);

        INodeModel conseptualisationLas  = createLASElement("Conceptualisation", 300, 10, 50, 100);

        INodeModel designLas  = createLASElement("Design", 550, 10, 50, 100);
        INodeModel buildLas  = createLASElement("Build", 700, 220, 50, 100);
        INodeModel experimentLas  = createLASElement("ExperimentExperiment", 650, 400, 50, 100);
        INodeModel evaluationLas  = createLASElement("Evaluation", 300, 300, 50, 100);
        INodeModel reflectionLas  = createLASElement("Reflection", 250, 500, 50, 100);
        INodeModel reportingLas  = createLASElement("Reporting", 100, 600, 50, 100);

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

    private void addNode(IDiagramModel pedagogicalPlan, INodeModel node) {
        pedagogicalPlan.addNode(node);
    }

    private INodeModel createLASElement(String name, int x, int y, int h, int w) {

        INodeModel las = new NodeModel();
        URL theurl = getClass().getResource("/eu/scy/scyplanner/impl/shapes/LASShape.svg");
        try {
            INodeShape s = new SVGConcept(theurl);
            las.setShape(s);
        } catch (IOException e) {
            System.err.println("File not found: "+e.getLocalizedMessage());
        }

		las.setShape(new RoundRectangle());
        //las.setShape(new LASShape());
        las.setLabel(name);

        las.setLocation(new Point(x, y));
        las.setSize(new Dimension(h, w));
        return las;
    }

    private INodeModel createELOElement(String name, Integer xPos, Integer yPos) {

        INodeModel eloNodeModel = new NodeModel();

        URL theurl = getClass().getResource("/eu/scy/scyplanner/impl/shapes/ELOShape.svg");

        try {
            INodeShape s = new SVGConcept(theurl);
            eloNodeModel.setShape(s);
        } catch (IOException e) {
            System.err.println("File not found: "+e.getLocalizedMessage());
        }
		
		eloNodeModel.setShape(new Star());

        eloNodeModel.setLocation(new Point(xPos, yPos));
        eloNodeModel.setSize(new Dimension(50+(int)(Math.random() *100),50+(int)(Math.random() *100)));
        eloNodeModel.setLabelHidden(true);

        return eloNodeModel;
    }

    private void connectNodes(IDiagramModel diagramModel, INodeModel from, INodeModel to) {
        INodeLinkModel link = new LearningActivitySpaceLinkModel(from, to);
        link.setLabelHidden(true);
        link.getStyle().setColor(new Color(0x4f81bc));
        link.getStyle().setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 6.0f));

        link.setShape(new Arrow());
        diagramModel.addLink(link);
    }
}