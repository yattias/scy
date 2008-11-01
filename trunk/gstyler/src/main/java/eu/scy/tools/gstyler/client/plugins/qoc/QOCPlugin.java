package eu.scy.tools.gstyler.client.plugins.qoc;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.GWTGraph.InteractionMode;
import eu.scy.tools.gstyler.client.graph.application.AbstractGraphPlugin;
import eu.scy.tools.gstyler.client.graph.application.GraphApplication;
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
        OptionNode n2 = new OptionNode();
        CriterionNode n3 = new CriterionNode();
        graph.addNode(n3, 400, 100);
        graph.addNode(n1, 100, 100);
        graph.addNode(n2, 200, 300);
    }
}
