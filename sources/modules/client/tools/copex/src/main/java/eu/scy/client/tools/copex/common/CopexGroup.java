/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * group of leaners
 * @author Marjolaine
 */
public class CopexGroup implements Cloneable{
    private long dbKey;
    private List<CopexLearner> learners;

    public CopexGroup(long dbKey, List<CopexLearner> learners) {
        this.dbKey = dbKey;
        this.learners = learners;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public List<CopexLearner> getLearners() {
        return learners;
    }

    public void setLearners(List<CopexLearner> learners) {
        this.learners = learners;
    }
    @Override
    public Object clone()  {
        try {
            CopexGroup g = (CopexGroup) super.clone() ;
            g.setDbKey(dbKey);
            List<CopexUser> list = null;
            if(this.learners != null){
                list = new LinkedList();
                for(Iterator<CopexLearner> l = learners.iterator(); l.hasNext();){
                    list.add((CopexLearner)l.next().clone());
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
