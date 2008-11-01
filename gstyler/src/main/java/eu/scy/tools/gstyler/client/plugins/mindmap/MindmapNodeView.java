package eu.scy.tools.gstyler.client.plugins.mindmap;


import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.CSSConstants;
import eu.scy.tools.gstyler.client.graph.edge.Edge;
import eu.scy.tools.gstyler.client.graph.edge.EdgeCreationHandle;
import eu.scy.tools.gstyler.client.graph.node.NodeView;


public class MindmapNodeView extends NodeView<MindmapNode> {

    private TextArea textArea;
    private Label edgeHandle;

    public MindmapNodeView(MindmapNode node) {
        super(node);
        textArea = new TextArea();
        textArea.setVisibleLines(3);
        textArea.addKeyboardListener(new KeyboardListenerAdapter() {

            public void onKeyPress(Widget sender, char keyCode, int modifiers) {
               getNode().getModel().setNote(textArea.getText());
                
            }
        });
        add(textArea);
        edgeHandle = new Label("->");
        edgeHandle.setStyleName(CSSConstants.CSS_CLICKABLE_WIDGET);
        topPanel.add(edgeHandle);
    }

    @Override
    public void updateFromModel() {
        titleLabel.setText( getNode().getModel().getTitle() );
        textArea.setText( getNode().getModel().getNote() );
    }

    @Override
    public Collection<EdgeCreationHandle> getEdgeCreationHandles() {
        Collection<EdgeCreationHandle> c = new ArrayList<EdgeCreationHandle>(); 
        EdgeCreationHandle handle = new EdgeCreationHandle(edgeHandle, new Edge());
        c.add(handle);
        return c;
    }

}
