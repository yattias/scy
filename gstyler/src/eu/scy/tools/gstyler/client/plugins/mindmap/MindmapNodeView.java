package eu.scy.tools.gstyler.client.plugins.mindmap;


import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.CSSConstants;
import eu.scy.tools.gstyler.client.graph.NodeView;


public class MindmapNodeView extends NodeView<MindmapNode> {

    private TextArea textArea;
    private Label footer;

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
        footer = new Label("+");
        footer.setStyleName(CSSConstants.CSS_DRAW_EDGES);
        add(footer);
    }

    @Override
    public void updateFromModel() {
        header.setText( getNode().getModel().getHeader() );
        textArea.setText( getNode().getModel().getNote() );
    }

    @Override
    public SourcesMouseEvents getEdgeHandle() {
        return footer;
    }

}
