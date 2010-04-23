/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import org.jdom.Element;
import org.jdom.JDOMException;



/**
 * parametre de repetition d'une tache iterative
 * @author Marjolaine
 */
public abstract class TaskRepeatParam implements Cloneable {
    /* identifiant */
    protected long dbKey;

    public TaskRepeatParam(long dbKey) {
        this.dbKey = dbKey;
    }

    public TaskRepeatParam(Element xmlElem) throws JDOMException {

    }
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

   
    
      // OVERRIDE
    @Override
    protected Object clone() {
       try {
            TaskRepeatParam p = (TaskRepeatParam) super.clone() ;

            p.setDbKey(this.dbKey);
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
