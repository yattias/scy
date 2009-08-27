package eu.scy.scymapper.api.diagram;

import eu.scy.scymapper.api.links.IConceptLink;
import eu.scy.scymapper.api.nodes.INode;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 11:43:03
 * To change this template use File | Settings | File Templates.
 */
public interface IDiagramController {
    public void setName(String name);
    public void addNode(INode n, boolean preventOverlap);
    public void addNode(INode n);
    public void addLink(IConceptLink l);
}
