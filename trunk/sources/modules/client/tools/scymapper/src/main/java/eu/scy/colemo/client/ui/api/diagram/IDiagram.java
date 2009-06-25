package eu.scy.colemo.client.ui.api.diagram;

import eu.scy.colemo.client.ui.api.nodes.IConceptNode;
import eu.scy.colemo.client.ui.api.links.IConceptLink;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 24.jun.2009
 * Time: 11:26:58
 * This is the Diagram model
 */
public interface IDiagram extends IDiagramObservable {

    public void setName(String name);

    public String getName();

    public void addNode(IConceptNode n);

    public void removeNode(IConceptNode n);

    public void addLink(IConceptLink n);

    public Set<IConceptLink> getLinks();

    public Set<IConceptNode> getNodes();

}
