package eu.scy.scymapper.api.diagram;

import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.api.diagram.INodeModel;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 11:26:58
 * This is the Diagram model
 */
public interface IDiagramModel extends IDiagramModelObservable {

    public void setName(String name);

    public String getName();

    public void addNode(INodeModel n);

    public void removeNode(INodeModel n);

    public void addLink(ILinkModel n);

    public Set<ILinkModel> getLinks();

    public Set<INodeModel> getNodes();

}
