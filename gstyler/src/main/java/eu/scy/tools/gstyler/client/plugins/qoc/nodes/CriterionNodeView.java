package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

import eu.scy.tools.gstyler.client.graph.edge.EdgeCreationHandle;
import eu.scy.tools.gstyler.client.graph.node.NodeView;
import eu.scy.tools.gstyler.client.plugins.qoc.CSSConstants;


public class CriterionNodeView extends NodeView<CriterionNode> {

    private TextBox criterionBox;
    private TextBox relevanceBox;

    public CriterionNodeView(CriterionNode node) {
        super(node);
        setTitle("Criterion");
        setStylePrimaryName(CSSConstants.CSS_CRITERION);
        titleLabel.setStylePrimaryName(CSSConstants.CSS_CRITERION);
        criterionBox = new TextBox();
        add(criterionBox);
        HorizontalPanel p = new HorizontalPanel();
        p.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        p.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        p.setWidth("90%");
        add(p);
        p.add(new Label("Relevance: "));
        relevanceBox = new TextBox();
        relevanceBox.setWidth("30px");
        p.add(relevanceBox);
        p.add(new Label("/ 100"));
    }

    @Override
    public Collection<EdgeCreationHandle> getEdgeCreationHandles() {
        return null;
    }

    @Override
    public void updateFromModel() {
        criterionBox.setText(getNode().getModel().getCriterion());
        relevanceBox.setText(Integer.toString(getNode().getModel().getRelevance()));
    } 

}
