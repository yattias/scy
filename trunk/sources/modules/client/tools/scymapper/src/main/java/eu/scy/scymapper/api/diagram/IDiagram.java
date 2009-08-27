package eu.scy.scymapper.api.diagram;

import eu.scy.scymapper.api.links.IConceptLink;
import eu.scy.scymapper.api.nodes.INode;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 11:26:58
 * This is the Diagram model
 */
public interface IDiagram extends IDiagramObservable {

    public void setName(String name);

    public String getName();

    public void addNode(INode n);

    public void removeNode(INode n);

    public void addLink(IConceptLink n);

    public Set<IConceptLink> getLinks();

    public Set<INode> getNodes();

}
