/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import org.jdom.Element;
import org.jdom.JDOMException;



/**
 * repeat parameter of an iterative task
 * @author Marjolaine
 */
public abstract class TaskRepeatParam implements Cloneable {
     public final static String TAG_TASK_REPEAT_PARAM_ACTION = "action_param";
    /* database dbkey */
    protected long dbKey;

    protected long dbKeyActionParam = -1;

    public TaskRepeatParam(long dbKey, long dbKeyActionParam) {
        this.dbKey = dbKey;
        this.dbKeyActionParam = dbKeyActionParam;
    }

    public TaskRepeatParam(Element xmlElem) throws JDOMException {

    }
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public long getDbKeyActionParam() {
        return dbKeyActionParam;
    }

    public void setDbKeyActionParam(long dbKeyActionParam) {
        this.dbKeyActionParam = dbKeyActionParam;
    }

    
   
    
    @Override
    protected Object clone() {
       try {
            TaskRepeatParam p = (TaskRepeatParam) super.clone() ;

            p.setDbKey(this.dbKey);
            p.setDbKeyActionParam(dbKeyActionParam);
            return p;
       } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

     // toXML
    public Element toXML(){
        return null;
    }
}
