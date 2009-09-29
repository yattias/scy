package eu.scy.scyplanner.impl.diagram;

import eu.scy.scymapper.api.diagram.IDiagramListener;
import eu.scy.scymapper.api.diagram.IDiagramModel;
import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.api.diagram.INodeModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Bjoerge Naess
 * Date: 28.aug.2009
 * Time: 10:58:49
 */
public class SCYPlannerDiagramModel implements IDiagramModel {
private String name;
    private Set<INodeModel> nodes;
    private Set<ILinkModel> links;
    private Collection<IDiagramListener> listeners;
    private INodeModel selectedNode;

	public SCYPlannerDiagramModel() {
        listeners = new ArrayList<IDiagramListener>();
        nodes = new HashSet<INodeModel>();
        links = new HashSet<ILinkModel>();
    }

    @Override
    public void setName(String name) {
        this.name = name;
        notifyUpdated();
    }

    public String getName() {
        return name;
    }
    @Override
    public void addNode(INodeModel n) {
        nodes.add(n);
        notifyNodeAdded(n);
    }

	@Override
	public void addNode(INodeModel n, boolean preventOverlap) {
		addNode(n);
	}

	@Override
    public void removeNode(INodeModel n) {
        nodes.add(n);
        notifyNodeRemoved(n);
    }

    @Override
    public void addLink(ILinkModel l) {
        links.add(l);
        notifyLinkAdded(l);
    }

    @Override
    public Set<ILinkModel> getLinks() {
        return links;
    }

    @Override
    public Set<INodeModel> getNodes() {
        return nodes;
    }

    @Override
    public void addDiagramListener(IDiagramListener o) {
        listeners.add(o);
    }

    @Override
    public void removeDiagramListener(IDiagramListener o) {
        listeners.remove(o);
    }

    @Override
    public void notifyUpdated() {
        for (IDiagramListener listener : listeners) {
            listener.updated(this);
        }
    }

    @Override
    public void notifyNodeAdded(INodeModel node) {
        for (IDiagramListener listener : listeners) {
            listener.nodeAdded(node);
        }
    }

    @Override
    public void notifyNodeRemoved(INodeModel n) {
        for (IDiagramListener listener : listeners) {
            listener.nodeRemoved(n);
        }
    }

    @Override
    public void notifyLinkAdded(ILinkModel link) {
        for (IDiagramListener listener : listeners) {
            listener.linkAdded(link);
        }
    }

    @Override
    public void notifyLinkRemoved(ILinkModel link) {
        for (IDiagramListener listener : listeners) {
            listener.linkRemoved(link);
        }
    }
}
