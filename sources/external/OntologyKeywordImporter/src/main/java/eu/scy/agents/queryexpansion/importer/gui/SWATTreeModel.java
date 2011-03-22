package eu.scy.agents.queryexpansion.importer.gui;

import info.collide.swat.SWATClient;
import info.collide.swat.model.Class;
import info.collide.swat.model.DatatypeAnnotation;
import info.collide.swat.model.DatatypeAnnotation.Type;
import info.collide.swat.model.Entity;
import info.collide.swat.model.Instance;
import info.collide.swat.model.SWATException;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public class SWATTreeModel extends DefaultTreeModel {

    private Class[] rootClasses;

    private SWATClient swatClient;

    private boolean displayInstances = true;
    
    private Hashtable<String, Entity> id2Class = new Hashtable<String, Entity>();

    private Hashtable<Entity, String> class2Id = new Hashtable<Entity, String>();

    private Hashtable<String, SWATTreeNode> nodes = new Hashtable<String, SWATTreeNode>();

    public SWATTreeModel(Class[] rootClasses, SWATClient swatClient) {
        super(null);
        this.swatClient = swatClient;
        this.rootClasses = rootClasses;
        Arrays.sort(this.rootClasses, new IDComparator());
        setRoot(new SWATTreeNode(this, displayInstances));
    }

    @Override
    public Object getChild(Object parent, int index) {
        SWATTreeNode n = (SWATTreeNode) parent;
        // System.out.print("GetChild " + n.dump() + " " + index + ": ");
        TreeNode node = null;
        if (n.isRoot()) {
            node = new SWATTreeNode(rootClasses[index], ((DefaultMutableTreeNode) parent), this, displayInstances);
        } else {
            node = n.getChildAt(index);
        }
        // System.out.println(node);
        return node;
    }

    @Override
    public int getChildCount(Object parent) {
        SWATTreeNode n = (SWATTreeNode) parent;
        // System.out.print("GetChildCount " + n.dump() + ": ");
        int count = 0;
        if (n.isRoot()) {
            count = rootClasses.length;
        } else {
            count = n.getChildCount();
        }
        // System.out.println(count);
        return count;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        SWATTreeNode p = (SWATTreeNode) parent;
        SWATTreeNode c = (SWATTreeNode) child;
        // System.out.print("GetIndexOfChild " + p.dump() + " / " + c.dump() +
        // ": ");
        int index = p.getIndex(c);
        // System.out.println(index);
        return index;
    }

    @Override
    public boolean isLeaf(Object node) {
        return ((SWATTreeNode) node).isLeaf();
    }

    @Override
    public void removeNodeFromParent(MutableTreeNode node) {
        TreeNode parent = ((SWATTreeNode) node).getOldParent();
        if (parent == null)
            throw new IllegalArgumentException("node does not have a parent.");

        int[] childIndex = new int[1];
        Object[] removedArray = new Object[1];

        childIndex[0] = ((SWATTreeNode) node).getOldParentIndex();
        removedArray[0] = node;
        nodesWereRemoved(parent, childIndex, removedArray);
    }

    class IDComparator implements Comparator<Entity> {

        public int compare(Entity o1, Entity o2) {
            return o1.getId().toString().compareTo(o2.getId().toString());
        }

    }

    public void setRootClasses(Class[] rootClasses) {
        this.rootClasses = rootClasses;
        nodeStructureChanged(root);
    }

    public Entity getClassFor(String internalId) {
        return id2Class.get(internalId);
    }

    public SWATClient getSwatClient() {
        return swatClient;
    }

    public String getIdFor(Entity classObject) {
        String id = class2Id.get(classObject);
        if (id == null) {
            id = new UID().toString();
            class2Id.put(classObject, id);
            id2Class.put(id, classObject);
        }
        return id;
    }

    public Class[] getRootClasses() {
        return rootClasses;
    }

    public SWATTreeNode getNodeForLabel(String label, String lang) {
        SWATTreeNode node = nodes.get(label);
        if (node == null) {
            try {
                List<String> path = new ArrayList<String>();
                Entity e = swatClient.getOntology().getEntityForLabel(new DatatypeAnnotation(Type.LABEL, lang, label));
                Class c = null;
                if (e instanceof Instance) {
                    path.add(e.getLabels());
                    Instance i = (Instance) e;
                    c = i.getInstancesOf()[0];
                } else {
                    c = (Class) e;
                }
                String l = c.getLabels();
                while (!nodes.containsKey(l)) {
                    path.add(l);
                    c = c.getDirectSuperClasses()[0];
                    l = c.getLabels();
                }
                SWATTreeNode n = getNodeForLabel(l, lang);
                for (int i = path.size() - 1; i >= 0; i--) {
                    String nextNodeLabel = path.get(i);
                    for (int j = 0; j < n.getChildCount(); j++) {
                        SWATTreeNode childNode = (SWATTreeNode) n.getChildAt(j);
                        if (childNode.getLabel().equals(nextNodeLabel)) {
                            n = childNode;
                            continue;
                        }
                    }
                }
                node = n;
            } catch (SWATException e) {
                e.printStackTrace();
            }
        }
        return node; 
    }
    
    public void addNode(SWATTreeNode node) {
        if (node.getLabel() != null) {
            nodes.put(node.getLabel(), node);
        }
    }

}
