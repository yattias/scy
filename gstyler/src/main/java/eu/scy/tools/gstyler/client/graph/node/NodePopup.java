package eu.scy.tools.gstyler.client.graph.node;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NodePopup extends PopupPanel {

    @SuppressWarnings("unused")
    private Node<?, ?> node;

    public NodePopup(final Node<?, ?> node) {
        // Close the Panel if the user clicks outside of it
        super(true);
        this.node = node;

        VerticalPanel verticalPanel = new VerticalPanel();
        Button deleteButton = new Button("Delete this Node");
        final PopupPanel panel = this;
        deleteButton.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                node.getParentGraph().removeNode(node);
                panel.hide();
            }
            
        });
        verticalPanel.add(deleteButton);
        setWidget(verticalPanel);
    }

}
