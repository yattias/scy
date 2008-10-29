package eu.scy.tools.gstyler.client.plugins.mindmap;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.GStyler;
import eu.scy.tools.gstyler.client.plugins.Palette;

public class MindmapPalette extends Palette {

    public MindmapPalette(final GStyler gstyler) {
        super(gstyler);

        getGraph().addNode(new MindmapNode(), 5, 5);

        final Button drawEdgeButton = new Button("Draw edges");
        drawEdgeButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                if (gstyler.getGraph().isEdgeMode()) {
                    gstyler.getGraph().leaveEdgeMode();
                    drawEdgeButton.setText("Draw edges");
                } else {
                    gstyler.getGraph().enterEdgeMode();
                    drawEdgeButton.setText("Move nodes");
                }
            }
        });
        getActionsPanel().add(drawEdgeButton);
        getActionsPanel().add(new Button("Delete edge"));
    }

    @Override
    public String getName() {
        return "Mindmap";
    }
}
