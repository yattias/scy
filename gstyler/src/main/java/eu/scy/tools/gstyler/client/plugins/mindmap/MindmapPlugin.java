package eu.scy.tools.gstyler.client.plugins.mindmap;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.GWTGraph;
import eu.scy.tools.gstyler.client.graph.application.AbstractGraphPlugin;
import eu.scy.tools.gstyler.client.graph.application.GraphApplication;
import eu.scy.tools.gstyler.client.graph.edge.Edge;

public class MindmapPlugin extends AbstractGraphPlugin {

    public MindmapPlugin(final GraphApplication graphApplication) {
        super(graphApplication);

        getGraph().addNode(new MindmapNode(), 5, 5);

        final Button exampleButton = new Button("Show example");
        exampleButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                addExampleDocument(graphApplication.getGraph());
            }
            
        });
        getActionsPanel().add(exampleButton);
        
        final Button drawEdgeButton = new Button("Edit edges");
        final Button moveNodesButton = new Button("Move nodes");

        drawEdgeButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                drawEdgeButton.setEnabled(false);
                moveNodesButton.setEnabled(true);
                graphApplication.getGraph().enterEdgeMode(new Edge());
            }
        });
        getActionsPanel().add(drawEdgeButton);
        
        
        moveNodesButton.setEnabled(false);
        moveNodesButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                drawEdgeButton.setEnabled(true);
                moveNodesButton.setEnabled(false);
                graphApplication.getGraph().enterNodeMode();
            }
            
        });
        getActionsPanel().add(moveNodesButton);
    }

    @Override
    public String getName() {
        return "Mindmap";
    }

    public void addExampleDocument(GWTGraph graph) {
        MindmapNode n1 = new MindmapNode();
        n1.getModel().setNote("Beer");
        n1.getNodeView().updateFromModel();
        MindmapNode n2 = new MindmapNode();
        n2.getModel().setNote("Pilsener");
        n2.getNodeView().updateFromModel();
        MindmapNode n3 = new MindmapNode();
        n3.getModel().setNote("Ale");
        n3.getNodeView().updateFromModel();
        
        graph.addNode(n1, 200, 50);
        graph.addNode(n2, 100, 300);
        graph.addNode(n3, 400, 200);

        Edge e1 = new Edge(n1, n2);
        graph.addEdge(e1);
        
        Edge e2 = new Edge(n1, n3);
        graph.addEdge(e2);
    }
}
