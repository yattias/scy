package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import java.util.Collection;

import com.google.gwt.user.client.ui.TextBox;

import eu.scy.tools.gstyler.client.graph.edge.EdgeCreationHandle;
import eu.scy.tools.gstyler.client.graph.node.NodeView;
import eu.scy.tools.gstyler.client.plugins.qoc.CSSConstants;


public class QuestionNodeView extends NodeView<QuestionNode>{

    private TextBox questionBox;

    public QuestionNodeView(QuestionNode node) {
        super(node);
        questionBox = new TextBox();
        add(questionBox);
        setStylePrimaryName(CSSConstants.CSS_QUESTION);
        titleLabel.setStylePrimaryName(CSSConstants.CSS_QUESTION);
    }

    @Override
    public Collection<EdgeCreationHandle> getEdgeCreationHandles() {
        return null;
    }

    @Override
    public void updateFromModel() {
        questionBox.setText(getNode().getModel().getQuestion());
    }

}
