package eu.scy.agents.queryexpansion.importer.gui;

import info.collide.swat.SWATClient;
import info.collide.swat.model.Class;
import info.collide.swat.model.Entity;
import info.collide.swat.model.ID;
import info.collide.swat.model.Instance;
import info.collide.swat.model.NamedClass;
import info.collide.swat.model.Property;
import info.collide.swat.model.SWATException;
import info.collide.swat.model.XSDValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.event.EventConstants;
import prefuse.visual.VisualItem;

public class SWATGraph extends Graph {

    private SWATClient swatClient;

    private int radius;

    private Class selectedEntity;

    private Hashtable<ID, Long> entity2Row;

    private Set<ID> markedEntities;

    private GraphPanel graphPanel;

    private Set<ID> ignoredEntities;

    private Set<ID> displayedProperties;

    private Set<ID> nodes2Fold;

    public SWATGraph(SWATClient swatClient, GraphPanel graphPanel) throws SWATException {
        this(swatClient, graphPanel, swatClient.getOntology().getRoot(), Integer.MAX_VALUE);
    }

    public SWATGraph(SWATClient swatClient, GraphPanel graphPanel, Set<ID> ignoredEntities, Set<ID> displayedProperties, Set<ID> nodes2Fold) throws SWATException {
        this(swatClient, swatClient.getOntology().getRoot(), Integer.MAX_VALUE, graphPanel, ignoredEntities, displayedProperties, nodes2Fold);
    }

    public SWATGraph(SWATClient swatClient, GraphPanel graphPanel, Class selectedEntity, int radius) throws SWATException {
        this(swatClient, selectedEntity, radius, graphPanel, new HashSet<ID>(), new HashSet<ID>(), new HashSet<ID>());
    }

    public SWATGraph(SWATClient swatClient, Class selectedEntity, int radius, GraphPanel graphPanel, Set<ID> ignoredEntities, Set<ID> displayedProperties, Set<ID> nodes2Fold) throws SWATException {
        super(true);
        this.graphPanel = graphPanel;
        this.swatClient = swatClient;
        this.selectedEntity = selectedEntity;
        addColumn("name", String.class);
        addColumn("type", String.class);
        entity2Row = new Hashtable<ID, Long>();
        markedEntities = Collections.synchronizedSet(new HashSet<ID>());
        this.radius = radius;
        this.ignoredEntities = ignoredEntities;
        this.displayedProperties = displayedProperties;
        this.nodes2Fold = nodes2Fold;
        spanTree();
    }

    private Node spanTree(Entity e, int radius) throws SWATException {
        // System.out.println("Spanning " + e.getId().getName());
        Long key = entity2Row.get(e.getId());
        Node n = null;
        markedEntities.add(e.getId());
        if (key == null) {
            n = addNode();
            fireGraphEvent(getNodeTable(), n.getRow(), n.getRow(), EventConstants.ALL_COLUMNS, EventConstants.INSERT);
            n.setString("name", e.getId().getName());
            if (e instanceof Class) {
                n.setString("type", "class");
            } else if (e instanceof Instance) {
                n.setString("type", "instance");
            }
            entity2Row.put(e.getId(), getKey(n.getRow()));
        } else {
            n = getNodeFromKey(key.longValue());
        }
        if (e instanceof Class) {
            for (Class c : ((Class) e).getDirectSubClasses()) {
                if (c instanceof NamedClass) {
                    if (!ignoredEntities.contains(c.getId()) && radius > 0 && !markedEntities.contains(c.getId())) {
                        Node node = spanTree(c, radius - 1);
                        if (getEdge(n, node) == null) {
                            addEdge(n, node);
                        }
                    }
                }
            }
            for (Class c : ((Class) e).getDirectSuperClasses()) {
                if (c instanceof NamedClass) {
                    if (!ignoredEntities.contains(c.getId()) && radius > 0 && !markedEntities.contains(c.getId())) {
                        Node node = spanTree(c, radius - 1);
                        if (getEdge(node, n) == null) {
                            addEdge(node, n);
                        }
                    }
                }
            }
            for (Instance i : ((Class) e).getInstances()) {
                if (!ignoredEntities.contains(i.getId()) && radius > 0 && !markedEntities.contains(i.getId())) {
                    Node node = spanTree(i, radius - 1);
                    if (getEdge(node, n) == null) {
                        addEdge(node, n);
                    }
                }
            }
        }
        graphPanel.layoutGraph(true);
        return n;
    }

    public int getRadius() {
        return radius;
    }

    private synchronized void spanTree() throws SWATException {
        System.out.println("Rebuilding tree");
        markedEntities.clear();
        Long keyRoot = entity2Row.get(selectedEntity.getId());
        if (keyRoot != null) {
            Node root = getNodeFromKey(keyRoot.longValue());
            getSpanningTree(root);
        }
        spanTree(selectedEntity, radius);
        TreeSet<Node> tm = new TreeSet<Node>(new Comparator<Node>() {

            public int compare(Node o1, Node o2) {
                return o1.getRow() - o2.getRow();
            }

        });
        for (ID propertyId : displayedProperties) {
            Property property = (Property) swatClient.getOntology().getEntity(propertyId.getFullId());
            Instance[] instances = property.getInstances();
            for (Instance i : instances) {
                ID sourceId = i.getId();
                for (Entity e : property.getValues(i)) {
                    ID targetId = e.getId();
                    if (e instanceof XSDValue) {
                        targetId = new ID(((XSDValue) e).getValue());
                    }
                    Long key = entity2Row.get(sourceId);
                    Node sourceNode = null;
                    markedEntities.add(sourceId);
                    if (key == null) {
                        sourceNode = createNodeForId(sourceId);
                    } else {
                        sourceNode = getNodeFromKey(key.longValue());
                    }
                    Long targetKey = entity2Row.get(targetId);
                    Node targetNode = null;
                    if (targetKey != null) {
                        targetNode = getNodeFromKey(targetKey.longValue());
                    } else {
                        targetNode = createNodeForId(targetId);
                    }
                    if (getEdge(targetNode, sourceNode) == null) {
                        addEdge(targetNode, sourceNode);
                    }
                }
            }
        }
        for (ID n2f : nodes2Fold) {
            ArrayList<Edge> edgesToDelete = new ArrayList<Edge>();
            for (Instance i : ((Class) (swatClient.getOntology().getEntity(n2f.getFullId()))).getInstances()) {
                Long nodeKey = entity2Row.get(i.getId());
                if (nodeKey != null) {
                    Node node = getNodeFromKey(nodeKey.longValue());
                    markedEntities.remove(i.getId());
                    Iterator<?> inEdgesIterator = node.inEdges();
                    while (inEdgesIterator.hasNext()) {
                        Edge in = (Edge) inEdgesIterator.next();
                        edgesToDelete.add(in);
                        Iterator<?> outEdgesIterator = node.outEdges();
                        while (outEdgesIterator.hasNext()) {
                            Edge out = (Edge) outEdgesIterator.next();
                            edgesToDelete.add(out);
                            addEdge(in.getSourceNode(), out.getTargetNode());
                        }
                    }
                    for (Edge e : edgesToDelete) {
                        removeEdge(e);
                    }
                    entity2Row.remove(i.getId());
                    removeNode(nodeKey.intValue());
                }
            }
        }
        for (Iterator<ID> it = entity2Row.keySet().iterator(); it.hasNext();) {
            ID id = it.next();
            if (!markedEntities.contains(id)) {
                Long key = entity2Row.get(id);
                Node n = getNodeFromKey(key);
                if (n != null) {
                    tm.add(n);
                }
                it.remove();
            }
        }
        for (Node n : tm) {
            removeNode(n);
        }
        graphPanel.layoutGraph(false);
    }

    private Node createNodeForId(ID nodeId) {
        Node newNode;
        newNode = addNode();
        fireGraphEvent(getNodeTable(), newNode.getRow(), newNode.getRow(), EventConstants.ALL_COLUMNS, EventConstants.INSERT);
        newNode.setString("name", nodeId.getName());
        newNode.setString("type", "keyword");
        System.out.println(nodeId);
        entity2Row.put(nodeId, getKey(newNode.getRow()));
        return newNode;
    }

    public void setRadius(int radius) throws SWATException {
        this.radius = radius;
        refresh();
    }

    public void refresh() {
        new Thread() {

            @Override
            public void run() {
                try {
                    spanTree();
                } catch (SWATException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public Class getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(Class selectedEntity) throws SWATException {
        this.selectedEntity = selectedEntity;
        new Thread() {

            @Override
            public void run() {
                try {
                    spanTree();
                } catch (SWATException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void setSelectedNode(VisualItem item) throws SWATException {
        for (Entry<ID, Long> e : entity2Row.entrySet()) {
            if (e.getValue().longValue() == getKey(item.getRow())) {
                Entity ent = swatClient.getOntology().getEntity(e.getKey().toString());
                setSelectedEntity((Class) ent);
            }
        }
    }

}
