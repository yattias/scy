package eu.scy.scyplanner.impl.diagram;

import eu.scy.scymapper.api.diagram.IDiagramListener;
import eu.scy.scymapper.api.diagram.IDiagramModel;
import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.api.diagram.INodeModel;

import java.awt.*;
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
    private transient Collection<IDiagramListener> listeners;
    private INodeModel selectedNode;
    private transient Image backgroundImage;

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
        nodes.remove(n);
        notifyNodeRemoved(n);
    }

    @Override
    public void addLink(ILinkModel l) {
        links.add(l);
        notifyLinkAdded(l);
    }

	@Override
	public void removeLink(ILinkModel l) {
        links.remove(l);
        notifyLinkRemoved(l);
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

    @Override
    public void removeAll() {
        INodeModel[] nodesCopy = nodes.toArray(new INodeModel[nodes.size()]);
        ILinkModel[] linksCopy = links.toArray(new ILinkModel[nodes.size()]);
        nodes.clear();
        links.clear();

        for (INodeModel n : nodesCopy) notifyNodeRemoved(n);
        for (ILinkModel l : linksCopy) notifyLinkRemoved(l);
    }

    @Override
    public INodeModel getNodeAt(Point point) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addNodes(java.util.List<INodeModel> nodes) {
        for (INodeModel node : nodes) addNode(node);
    }
}
