package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import eu.scy.tools.gstyler.client.graph.GWTGraphCSSConstants;
import eu.scy.tools.gstyler.client.graph.edge.EdgeCreationHandle;
import eu.scy.tools.gstyler.client.graph.node.NodeView;
import eu.scy.tools.gstyler.client.plugins.qoc.QOCCSSConstants;
import eu.scy.tools.gstyler.client.plugins.qoc.edges.QuestionEdge;


public class OptionNodeView extends NodeView<OptionNode> {

    private TextBox optionBox;
    private Label scoreLabel;
    private Label edgeHandle;
    
    public OptionNodeView(OptionNode node) {
        super(node);
        setTitle("Option");
        setStylePrimaryName(QOCCSSConstants.CSS_OPTION);
        titleLabel.setStylePrimaryName(QOCCSSConstants.CSS_OPTION);
        
        edgeHandle = new Label("->");
        edgeHandle.setTitle("Create a new QuestionEdge");
        edgeHandle.setStyleName(GWTGraphCSSConstants.CSS_CLICKABLE_WIDGET);
        topPanel.add(edgeHandle);
        
        optionBox = new TextBox();
        add(optionBox);
        HorizontalPanel p = new HorizontalPanel();
        p.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        p.setWidth("90%");
        add(p);
        p.add(new Label("Score:"));
        scoreLabel = new Label("0");
        p.add(scoreLabel);
        updateFromModel();
    }

    @Override
    public Collection<EdgeCreationHandle> getEdgeCreationHandles() {
        Collection<EdgeCreationHandle> c = new ArrayList<EdgeCreationHandle>(); 
        c.add(new EdgeCreationHandle(edgeHandle, new QuestionEdge()));
        return c;
    }

    @Override
    public void updateFromModel() {
        titleLabel.setText(getNode().getModel().getTitle());
        optionBox.setText(getNode().getModel().getOption());
        scoreLabel.setText(Integer.toString(getNode().getModel().getScore()));
    }
}
