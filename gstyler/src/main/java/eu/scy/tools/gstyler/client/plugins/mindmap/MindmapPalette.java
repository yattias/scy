package eu.scy.tools.gstyler.client.plugins.mindmap;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.GStyler;
import eu.scy.tools.gstyler.client.common.AbstractPalette;
import eu.scy.tools.gstyler.client.graph.GWTGraph.InteractionMode;

public class MindmapPalette extends AbstractPalette {

    public MindmapPalette(final GStyler gstyler) {
        super(gstyler);

        getGraph().addNode(new MindmapNode(), 5, 5);

        final Button drawEdgeButton = new Button("Edit edges");
        final Button moveNodesButton = new Button("Move nodes");

        drawEdgeButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                drawEdgeButton.setEnabled(false);
                moveNodesButton.setEnabled(true);
                gstyler.getGraph().setInteractionMode(InteractionMode.EDIT_EDGES);
            }
        });
        getActionsPanel().add(drawEdgeButton);
        
        
        moveNodesButton.setEnabled(false);
        moveNodesButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                drawEdgeButton.setEnabled(true);
                moveNodesButton.setEnabled(false);
                gstyler.getGraph().setInteractionMode(InteractionMode.MOVE_NODES);
            }
            
        });
        getActionsPanel().add(moveNodesButton);
    }

    @Override
    public String getName() {
        return "Mindmap";
    }
}
