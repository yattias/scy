package eu.scy.tools.gstyler.client.plugins;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.application.GraphPlugin;


public class DeleteEdgeButton extends Button {

    public DeleteEdgeButton(final GraphPlugin plugin) {
        super("Delete Edges");
        addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                plugin.getGraphApplication().getGraph().enterEdgeMode(null);
            }
        });
    }
}
