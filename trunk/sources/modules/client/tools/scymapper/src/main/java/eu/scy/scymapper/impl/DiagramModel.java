package eu.scy.scymapper.impl;

import eu.scy.scymapper.api.diagram.IDiagramListener;
import eu.scy.scymapper.api.diagram.IDiagramModel;
import eu.scy.scymapper.api.diagram.ILinkModel;
import eu.scy.scymapper.api.diagram.INodeModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 11.jun.2009
 * Time: 12:53:40
 */
public class DiagramModel implements IDiagramModel {
	private String name;
	private Set<INodeModel> nodes = new HashSet<INodeModel>();
    private Set<ILinkModel> links = new HashSet<ILinkModel>();

    private transient java.util.List<IDiagramListener> listeners = new ArrayList<IDiagramListener>();

	private Object readResolve() {
		listeners = new ArrayList<IDiagramListener>();
		return this;
	}

	public DiagramModel() {
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
    public void addNode(INodeModel node) {
		nodes.add(node);
        notifyNodeAdded(node);
    }

	@Override
	public void addNode(INodeModel node, boolean preventOverlap) {
		if (preventOverlap) {
			Rectangle b = new Rectangle(node.getLocation(), node.getSize());
			Point location = getFreeSpace(b).getLocation();
			node.setLocation(location);
		}
		addNode(node);
	}

	private Rectangle getFreeSpace(Rectangle bounds) {
		while (hasNodes(bounds)) {
			bounds.setLocation(bounds.x+10, bounds.y+10);
		}
		return bounds;
	}
	private boolean hasNodes(Rectangle rect) {
		for (INodeModel node : nodes) {
			Rectangle b = new Rectangle(node.getLocation(), node.getSize());
			if (b.contains(rect.getLocation())) return true;
		}
		return false;
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
