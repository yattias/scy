package eu.scy.scymapper.api.diagram;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 11:26:58
 * This is the Diagram model
 */
public interface IDiagramModel {

    void setName(String name);

    String getName();

    void addNode(INodeModel n);

    void removeNode(INodeModel n);

    void addLink(ILinkModel n);

    Set<ILinkModel> getLinks();
	
    Set<INodeModel> getNodes();

	void addObserver(IDiagramModelListener o);

    void removeObserver(IDiagramModelListener o);

    void notifyUpdated();

    void notifyNodeAdded(INodeModel node);

    void notifyNodeRemoved(INodeModel node);

    void notifyLinkAdded(ILinkModel link);

    void notifyLinkRemoved(ILinkModel link);
}
