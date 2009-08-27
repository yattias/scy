package eu.scy.scymapper.impl;

import eu.scy.scymapper.api.links.IConceptLink;
import eu.scy.scymapper.api.diagram.IDiagram;
import eu.scy.scymapper.api.diagram.IDiagramObserver;
import eu.scy.scymapper.api.nodes.INode;

import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 11.jun.2009
 * Time: 12:53:40
 */
public class Diagram implements IDiagram {
    private String name;
    private Set<INode> nodes;
    private Set<IConceptLink> links;
    private Collection<IDiagramObserver> observers;
    private INode selectedNode;

    public Diagram() {
        observers = new ArrayList<IDiagramObserver>();
        nodes = new HashSet<INode>();
        links = new HashSet<IConceptLink>();
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
    public void addNode(INode n) {
        nodes.add(n);
        notifyNodeAdded(n);
    }

    @Override
    public void removeNode(INode n) {
        nodes.add(n);
        notifyNodeRemoved(n);
    }

    @Override
    public void addLink(IConceptLink l) {
        links.add(l);
        notifyLinkAdded(l);
    }

    @Override
    public Set<IConceptLink> getLinks() {
        return links;
    }

    @Override
    public Set<INode> getNodes() {
        return nodes;
    }

    @Override
    public void addObserver(IDiagramObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IDiagramObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyUpdated() {
        for (IDiagramObserver observer : observers) {
            observer.updated(this);
        }
    }

    @Override
    public void notifyNodeAdded(INode node) {
        for (IDiagramObserver observer : observers) {
            observer.nodeAdded(node);
        }
    }

    @Override
    public void notifyNodeRemoved(INode n) {
        for (IDiagramObserver observer : observers) {
            observer.nodeRemoved(n);
        }
    }

    @Override
    public void notifyLinkAdded(IConceptLink link) {
        for (IDiagramObserver observer : observers) {
            observer.linkAdded(link);
        }
    }

    @Override
    public void notifyLinkRemoved(IConceptLink link) {
        for (IDiagramObserver observer : observers) {
            observer.linkRemoved(link);
        }
    }

    @Override
    public void notifyNodeSelected(INode node) {
        for (IDiagramObserver observer : observers) {
            observer.nodeSelected(node);
        }
    }
}
