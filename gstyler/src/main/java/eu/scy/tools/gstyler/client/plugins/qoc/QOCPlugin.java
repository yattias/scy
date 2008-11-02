package eu.scy.tools.gstyler.client.plugins.qoc;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.GWTGraph.InteractionMode;
import eu.scy.tools.gstyler.client.graph.application.AbstractGraphPlugin;
import eu.scy.tools.gstyler.client.graph.application.GraphApplication;
import eu.scy.tools.gstyler.client.plugins.qoc.edges.PositiveEdge;
import eu.scy.tools.gstyler.client.plugins.qoc.nodes.CriterionNode;
import eu.scy.tools.gstyler.client.plugins.qoc.nodes.OptionNode;
import eu.scy.tools.gstyler.client.plugins.qoc.nodes.QuestionNode;

public class QOCPlugin extends AbstractGraphPlugin {

    public QOCPlugin(final GraphApplication graphApplication) {
        super(graphApplication);

        getGraph().addNode(new QuestionNode(), 5, 5);
        getGraph().addNode(new OptionNode(), 5, 60);
        getGraph().addNode(new CriterionNode(), 5, 130);

        final Button drawEdgeButton = new Button("Edit edges");
        final Button moveNodesButton = new Button("Move nodes");

        drawEdgeButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                drawEdgeButton.setEnabled(false);
                moveNodesButton.setEnabled(true);
                graphApplication.getGraph().setInteractionMode(InteractionMode.EDIT_EDGES);
            }
        });
        getActionsPanel().add(drawEdgeButton);

        moveNodesButton.setEnabled(false);
        moveNodesButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                drawEdgeButton.setEnabled(true);
                moveNodesButton.setEnabled(false);
                graphApplication.getGraph().setInteractionMode(InteractionMode.MOVE_NODES);
            }

        });
        getActionsPanel().add(moveNodesButton);
        
        final Button exampleButton = new Button("Show example");
        exampleButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                addExampleDocument(graphApplication.getGraph());
            }
            
        });
        getActionsPanel().add(exampleButton);
    }

    @Override
    public String getName() {
        return "QOC";
    }

    public void addExampleDocument(GWTGraph graph) {
        QuestionNode n1 = new QuestionNode();
        n1.getModel().setQuestion("What to eat?");
        n1.getNodeView().updateFromModel();
        OptionNode n2 = new OptionNode();
        n2.getModel().setOption("Falafel");
        n2.getNodeView().updateFromModel();
        OptionNode n3 = new OptionNode();
        OptionNode n4 = new OptionNode();
        CriterionNode n5 = new CriterionNode();
        n5.getModel().setCriterion("Vegetarian");
        n5.getNodeView().updateFromModel();
        CriterionNode n6 = new CriterionNode();
        CriterionNode n7 = new CriterionNode();
        graph.addNode(n1, 400, 10);
        graph.addNode(n2, 100, 120);
        graph.addNode(n3, 400, 120);
        graph.addNode(n4, 700, 120);
        graph.addNode(n5, 50, 300);
        graph.addNode(n6, 300, 300);
        graph.addNode(n7, 600, 300);
        PositiveEdge e1 = new PositiveEdge(n2, n5);
        graph.addEdge(e1);
    }
}
