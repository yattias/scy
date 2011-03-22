package eu.scy.agents.queryexpansion.importer.gui;

import info.collide.swat.model.Class;
import info.collide.swat.model.Entity;
import info.collide.swat.model.Instance;
import info.collide.swat.model.SWATException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class SWATTreeNode extends DefaultMutableTreeNode {

    private static final String ROOT_LABEL = "owl:Thing";

    private boolean isRoot;

    private boolean isLeaf;

    private String internalId;

    private String label;

    private int oldParentIndex;

    private transient SWATTreeModel model;

    private TreeNode oldParent;

    private boolean displayInstances;

    public SWATTreeNode(SWATTreeModel model, boolean displayInstances) {
        super(ROOT_LABEL);
        this.label = ROOT_LABEL;
        this.isRoot = true;
        setModel(model);
        this.displayInstances = displayInstances;
    }

    @Override
    public boolean isRoot() {
        return isRoot;
    }

    public SWATTreeNode(Entity classObject, DefaultMutableTreeNode parent, SWATTreeModel model, boolean displayInstances) {
        super(classObject.getLabels());
        isLeaf = classObject instanceof Instance;
        this.internalId = model.getIdFor(classObject);
        setParent(parent);
        this.label = classObject.getLabels();
        setModel(model);
        this.displayInstances = displayInstances;
    }

    public Entity getSWATClass() {
        if (model != null) {
            return model.getClassFor(internalId);
        } else {
            return null;
        }

    }

    @Override
    public int getChildCount() {
        if (isLeaf) {
            return 0;
        }
        try {
            if (model != null) {
                if (isRoot) {
                    return model.getRootClasses().length;
                } else {
                    int subclasses = ((Class) model.getClassFor(internalId)).getDirectSubClasses().length;
                    if (displayInstances) {
                        int instances = ((Class) model.getClassFor(internalId)).getInstances().length;
                        return subclasses + instances;
                    } else {
                        return subclasses;
                    }
                }
            }
        } catch (SWATException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public TreeNode getChildAt(int index) {
        try {
            if (model != null) {
                if (isRoot) {
                    return new SWATTreeNode(model.getRootClasses()[index], this, model, displayInstances);
                } else {
                    Class[] subClasses = ((Class) model.getClassFor(internalId)).getDirectSubClasses();
                    if (displayInstances && index > subClasses.length - 1) {
                        Instance[] instances = ((Class) model.getClassFor(internalId)).getInstances();
                        return new SWATTreeNode(instances[index - subClasses.length], this, model, displayInstances);
                    } else {
                        return new SWATTreeNode(subClasses[index], this, model, displayInstances);
                    }
                }
            }
        } catch (SWATException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getIndex(TreeNode aChild) {
        if (aChild == null) {
            throw new IllegalArgumentException("argument is null");
        }
        try {
            if (model != null) {
                Class[] subClasses = null;
                if (isRoot) {
                    subClasses = model.getRootClasses();
                } else {
                    subClasses = ((Class) model.getClassFor(internalId)).getDirectSubClasses();
                }
                Entity subClass = model.getClassFor(((SWATTreeNode) aChild).internalId);
                for (int i = 0; i < subClasses.length; i++) {
                    if (subClasses[i].equals(subClass)) {
                        return i;
                    }
                }
                if (displayInstances) {
                    Instance[] instances = ((Class) model.getClassFor(internalId)).getInstances();
                    for (int i = 0; i < instances.length; i++) {
                        if (instances[i].equals(subClass)) {
                            return i + subClasses.length;
                        }
                    }
                }
            }
        } catch (SWATException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String dump() {
        return label;
    }

    public void setModel(SWATTreeModel model) {
        this.model = model;
        model.addNode(this);
    }

    public String getInternalId() {
        return internalId;
    }

    public int getOldParentIndex() {
        return oldParentIndex;
    }

    @Override
    public boolean isLeaf() {
        return isLeaf;
    }

    public void saveOldParentInfos() {
        ((SWATTreeNode) getParent()).setModel(model);
        this.oldParentIndex = getParent().getIndex(this);
        this.oldParent = getParent();
    }

    public TreeNode getOldParent() {
        return oldParent;
    }

    public String getLabel() {
        return label;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((internalId == null) ? 0 : internalId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SWATTreeNode other = (SWATTreeNode) obj;
        if (internalId == null) {
            if (other.internalId != null)
                return false;
        } else if (!internalId.equals(other.internalId))
            return false;
        return true;
    }

}
