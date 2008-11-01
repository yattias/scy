package eu.scy.tools.gstyler.client.test;

import eu.scy.tools.gstyler.client.plugins.mindmap.MindmapNode;


public class GwtTestMindMapNode extends GwtTestNode {

    @Override
    public void gwtSetUp() {
        testNode = new MindmapNode();
    }
}
