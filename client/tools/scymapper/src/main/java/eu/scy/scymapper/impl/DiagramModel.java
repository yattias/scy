package eu.scy.scymapper.impl;

import eu.scy.scymapper.api.diagram.model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;

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
    public synchronized void removeNode(INodeModel n) {
        nodes.remove(n);
        System.out.println("#######################REMOVED NOW NOTIFYING LISTENERS = " + n);
        notifyNodeRemoved(n);
    }

    @Override
    public synchronized void addLink(ILinkModel l) {
        links.add(l);
        notifyLinkAdded(l);
    }

	@Override
	public synchronized void removeLink(ILinkModel l) {
		links.remove(l);
		notifyLinkRemoved(l);
	}

	@Override
    public synchronized Set<ILinkModel> getLinks() {
        return links;
    }

    @Override
    public synchronized Set<INodeModel> getNodes() {
        return nodes;
    }

	@Override
	public IDiagramElement getElementById(String id) {
		for (INodeModel node : nodes) {
			if (node.getId().equals(id)) return node;
		}
		for (ILinkModel link : links) {
			if (link.getId().equals(id)) return link;
		}
		return null;
	}

	@Override
    public void addDiagramListener(IDiagramListener l) {
        listeners.add(l);
    }

    @Override
    public void removeDiagramListener(IDiagramListener l) {
        listeners.remove(l);
    }

    @Override
    public void notifyUpdated() {
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (IDiagramListener listener : listeners) {
					listener.updated(DiagramModel.this);
				}
			}
		});
    }

    @Override
    public void notifyNodeAdded(final INodeModel node) {
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (IDiagramListener listener : listeners) {
					listener.nodeAdded(node);
				}
			}
		});
    }

    @Override
    public void notifyNodeRemoved(final INodeModel n) {
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (IDiagramListener listener : listeners) {
					listener.nodeRemoved(n);
				}
			}
		});
    }

    @Override
    public void notifyLinkAdded(final ILinkModel link) {
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (IDiagramListener listener : listeners) {
					listener.linkAdded(link);
				}
			}
		});
    }

    @Override
    public void notifyLinkRemoved(final ILinkModel link) {
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (IDiagramListener listener : listeners) {
					listener.linkRemoved(link);
				}
			}
		});
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
        for (INodeModel node : nodes) {
			Rectangle b = new Rectangle(node.getLocation(), node.getSize());
			if (b.contains(point)) return node;
		}
        return null;
    }

    @Override
    public void addNodes(List<INodeModel> nodes) {
        for (INodeModel node : nodes) addNode(node);
    }
}
