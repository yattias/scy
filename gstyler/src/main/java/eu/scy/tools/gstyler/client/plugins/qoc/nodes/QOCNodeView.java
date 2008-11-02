package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import com.google.gwt.user.client.ui.TextBox;

import eu.scy.tools.gstyler.client.graph.node.NodeView;


@SuppressWarnings("unchecked")
public abstract class QOCNodeView extends NodeView<QOCNode>{

    private TextBox descriptionBox;
    
    public QOCNodeView(QOCNode node) {
        super(node);
        descriptionBox = new TextBox();
        add(descriptionBox);
    }

    @Override
    public void updateFromModel() {
        descriptionBox.setText(getNode().getModel().getDescription());
    }
}
