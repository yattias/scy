package eu.scy.tools.gstyler.client.test;

import com.google.gwt.junit.client.GWTTestCase;

import eu.scy.tools.gstyler.client.graph.node.Node;
import eu.scy.tools.gstyler.client.graph.node.NodeView;


public class GwtTestNode extends GWTTestCase {

    protected Node<?, ?> testNode;

    @Override
    public String getModuleName() {
        return "eu.scy.tools.gstyler.GStyler";
    }
    
    @Override
    public void gwtSetUp() {
        testNode = new NodeMockup();
    }
    
    public void testCreateClone() {
        Node<?, ?> clone = testNode.clone();
        assertNotNull(clone);
        assertEquals(testNode.getModel().getTitle(), clone.getModel().getTitle());
    }
    
    public void testView() {
        NodeView<?> view = testNode.createView();
        assertNotNull(view);
        assertNotNull(view.getDragHandle());
        assertNotNull(view.getEdgeCreationHandles());
    }
    
}
