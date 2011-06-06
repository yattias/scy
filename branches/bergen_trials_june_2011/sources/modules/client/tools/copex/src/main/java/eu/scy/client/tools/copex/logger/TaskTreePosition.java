/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.logger;

import org.jdom.Element;

/**
 * position of a task in a tree
 * the task is identified by its parent task and by its index in the list of children
 * @author Marjolaine
 */
public class TaskTreePosition {
    private long dbKeyTaskParent;
    private int idChild;

    public TaskTreePosition(long dbKeyTaskParent, int idChild) {
        this.dbKeyTaskParent = dbKeyTaskParent;
        this.idChild = idChild;
    }

    public long getDbKeyTaskParent() {
        return dbKeyTaskParent;
    }

    public void setDbKeyTaskParent(long dbKeyTaskParent) {
        this.dbKeyTaskParent = dbKeyTaskParent;
    }

    public int getIdChild() {
        return idChild;
    }

    public void setIdChild(int idChild) {
        this.idChild = idChild;
    }
    public Element toXML(){
        Element element = new Element("task_tree_position");
        element.addContent(new Element("id_parent").setText(Long.toString(dbKeyTaskParent)));
        element.addContent(new Element("id_child").setText(Integer.toString(idChild)));
        return element;
    }
}
