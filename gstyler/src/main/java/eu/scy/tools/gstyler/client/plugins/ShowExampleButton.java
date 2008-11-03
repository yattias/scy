package eu.scy.tools.gstyler.client.plugins;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.plugins.qoc.QOCPlugin;

public class ShowExampleButton extends Button {

    public ShowExampleButton(final QOCPlugin plugin) {
        super("Show Example");
        addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                plugin.addExampleDocument(plugin.getGraphApplication().getGraph());
            }

        });
    }
}
