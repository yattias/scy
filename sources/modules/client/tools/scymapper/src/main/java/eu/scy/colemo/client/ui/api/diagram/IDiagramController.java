package eu.scy.colemo.client.ui.api.diagram;

import eu.scy.colemo.client.ui.api.nodes.IConceptNode;
import eu.scy.colemo.client.ui.api.links.IConceptLink;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 24.jun.2009
 * Time: 11:43:03
 * To change this template use File | Settings | File Templates.
 */
public interface IDiagramController {
    public void setName(String name);
    public void addNode(IConceptNode n, boolean preventOverlap);
    public void addNode(IConceptNode n);
    public void addLink(IConceptLink l);
}
