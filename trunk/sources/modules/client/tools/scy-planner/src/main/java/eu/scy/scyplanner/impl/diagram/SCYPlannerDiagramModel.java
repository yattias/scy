package eu.scy.scyplanner.impl.diagram;

import eu.scy.scymapper.api.diagram.IDiagramModel;
import eu.scy.scymapper.api.diagram.IDiagramModelObserver;
import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.api.diagram.ILinkModel;

import java.util.Set;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * User: Bjoerge Naess
 * Date: 28.aug.2009
 * Time: 10:58:49
 */
public class SCYPlannerDiagramModel implements IDiagramModel {
private String name;
    private Set<INodeModel> nodes;
    private Set<ILinkModel> links;
    private Collection<IDiagramModelObserver> observers;
    private INodeModel selectedNode;

    public SCYPlannerDiagramModel() {
        observers = new ArrayList<IDiagramModelObserver>();
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
    public void addObserver(IDiagramModelObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IDiagramModelObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyUpdated() {
        for (IDiagramModelObserver observer : observers) {
            observer.updated(this);
        }
    }

    @Override
    public void notifyNodeAdded(INodeModel node) {
        for (IDiagramModelObserver observer : observers) {
            observer.nodeAdded(node);
        }
    }

    @Override
    public void notifyNodeRemoved(INodeModel n) {
        for (IDiagramModelObserver observer : observers) {
            observer.nodeRemoved(n);
        }
    }

    @Override
    public void notifyLinkAdded(ILinkModel link) {
        for (IDiagramModelObserver observer : observers) {
            observer.linkAdded(link);
        }
    }

    @Override
    public void notifyLinkRemoved(ILinkModel link) {
        for (IDiagramModelObserver observer : observers) {
            observer.linkRemoved(link);
        }
    }

    @Override
    public void notifyNodeSelected(INodeModel node) {
        for (IDiagramModelObserver observer : observers) {
            observer.nodeSelected(node);
        }
    }
}
