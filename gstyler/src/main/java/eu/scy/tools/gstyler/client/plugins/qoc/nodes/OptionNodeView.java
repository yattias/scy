package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import eu.scy.tools.gstyler.client.graph.edge.EdgeCreationHandle;
import eu.scy.tools.gstyler.client.graph.node.NodeView;
import eu.scy.tools.gstyler.client.plugins.qoc.CSSConstants;


public class OptionNodeView extends NodeView<OptionNode> {

    private TextBox optionBox;
    private Label scoreLabel;
    
    public OptionNodeView(OptionNode node) {
        super(node);
        optionBox = new TextBox();
        add(optionBox);
        setStylePrimaryName(CSSConstants.CSS_OPTION);
        titleLabel.setStylePrimaryName(CSSConstants.CSS_OPTION);
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
        return null;
    }

    @Override
    public void updateFromModel() {
        optionBox.setText(getNode().getModel().getOption());
        scoreLabel.setText(Integer.toString(getNode().getModel().getScore()));
    }

}
