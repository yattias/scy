/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * group of learners
 * @author Marjolaine
 */
public class Group implements Cloneable {
    private long dbKey;
    private List<ToolUser> learners;

    public Group(long dbKey, List<ToolUser> learners) {
        this.dbKey = dbKey;
        this.learners = learners;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public List<ToolUser> getLearners() {
        return learners;
    }

    public void setLearners(List<ToolUser> learners) {
        this.learners = learners;
    }
    @Override
    public Object clone()  {
        try {
            Group g = (Group) super.clone() ;
            g.setDbKey(dbKey);
            List<ToolUser> list = null;
            if(this.learners != null){
                list = new LinkedList();
                for(Iterator<ToolUser> l = learners.iterator(); l.hasNext();){
                    list.add((ToolUser)l.next().clone());
                }
            }
            g.setLearners(learners);
            return g;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
}
