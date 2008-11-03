package eu.scy.tools.gstyler.client.plugins;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.application.GraphPlugin;
import eu.scy.tools.gstyler.client.graph.edge.Edge;


public class CreateEdgeButton extends Button {

    public CreateEdgeButton(String title, final GraphPlugin plugin, final Edge edge) {
        super(title);
        addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                plugin.getGraphApplication().getGraph().enterEdgeMode(edge);
            }
        });
    }

}
