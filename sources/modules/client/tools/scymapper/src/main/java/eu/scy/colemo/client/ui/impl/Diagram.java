package eu.scy.colemo.client.ui.impl;

import eu.scy.colemo.client.ui.api.nodes.IConceptNode;
import eu.scy.colemo.client.ui.api.links.IConceptLink;
import eu.scy.colemo.client.ui.api.diagram.IDiagram;
import eu.scy.colemo.client.ui.api.diagram.IDiagramObserver;

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
    private Set<IConceptNode> nodes;
    private Set<IConceptLink> links;
    private Collection<IDiagramObserver> observers;
    private IConceptNode selectedNode;

    public Diagram() {
        observers = new ArrayList<IDiagramObserver>();
        nodes = new HashSet<IConceptNode>();
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
    public void addNode(IConceptNode n) {
        nodes.add(n);
        notifyNodeAdded(n);
    }

    @Override
    public void removeNode(IConceptNode n) {
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
    public Set<IConceptNode> getNodes() {
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
    public void notifyNodeAdded(IConceptNode node) {
        for (IDiagramObserver observer : observers) {
            observer.nodeAdded(node);
        }
    }

    @Override
    public void notifyNodeRemoved(IConceptNode n) {
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
    public void notifyNodeSelected(IConceptNode node) {
        for (IDiagramObserver observer : observers) {
            observer.nodeSelected(node);
        }
    }
}
