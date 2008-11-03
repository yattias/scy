package eu.scy.tools.gstyler.client.plugins.qoc.nodes;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.graph.GWTGraphCSSConstants;
import eu.scy.tools.gstyler.client.graph.edge.EdgeCreationHandle;
import eu.scy.tools.gstyler.client.graph.node.NodeView;
import eu.scy.tools.gstyler.client.plugins.qoc.QOCCSSConstants;
import eu.scy.tools.gstyler.client.plugins.qoc.edges.NegativeEdge;
import eu.scy.tools.gstyler.client.plugins.qoc.edges.PositiveEdge;


public class CriterionNodeView extends NodeView<CriterionNode> {

    private TextBox criterionBox;
    private TextBox relevanceBox;
    private Label edgeHandleNegative;
    private Label edgeHandlePositive;

    public CriterionNodeView(CriterionNode node) {
        super(node);
        setTitle("Criterion");
        setStylePrimaryName(QOCCSSConstants.CSS_CRITERION);
        titleLabel.setStylePrimaryName(QOCCSSConstants.CSS_CRITERION);
        
        edgeHandleNegative = new Label("-->");
        edgeHandleNegative.setTitle("Create a negative Edge");
        edgeHandleNegative.setStyleName(GWTGraphCSSConstants.CSS_CLICKABLE_WIDGET);
        topPanel.add(edgeHandleNegative);
        
        edgeHandlePositive = new Label("+->");
        edgeHandlePositive.setTitle("Create a positive Edge");
        edgeHandlePositive.setStyleName(GWTGraphCSSConstants.CSS_CLICKABLE_WIDGET);
        topPanel.add(edgeHandlePositive);
        
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
        relevanceBox.addChangeListener(new ChangeListener() {

            public void onChange(Widget sender) {
              int relevance = 100;
              try {
                  relevance = Integer.parseInt(relevanceBox.getText());
              } catch (NumberFormatException e) {
                  System.out.println("Exception: " + e);
              }
              getNode().setRelevance(relevance);
            }
            
        });
        
        p.add(relevanceBox);
        p.add(new Label("/ 100"));
    }

    @Override
    public Collection<EdgeCreationHandle> getEdgeCreationHandles() {
        Collection<EdgeCreationHandle> c = new ArrayList<EdgeCreationHandle>(); 
        c.add(new EdgeCreationHandle(edgeHandleNegative, new NegativeEdge()));
        c.add(new EdgeCreationHandle(edgeHandlePositive, new PositiveEdge()));
        return c;
    }

    @Override
    public void updateFromModel() {
        criterionBox.setText(getNode().getModel().getCriterion());
        relevanceBox.setText(Integer.toString(getNode().getModel().getRelevance()));
    } 

}
