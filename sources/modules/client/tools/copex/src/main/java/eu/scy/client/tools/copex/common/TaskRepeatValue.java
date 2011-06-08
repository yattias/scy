/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * value of the parameter for a task repeat
 * @author Marjolaine
 */
public abstract class TaskRepeatValue implements Cloneable{
    public final static String TAG_TASK_REPEAT_NO_REPEAT = "no_repeat";
    protected long dbKey ;
    protected int noRepeat;

    public TaskRepeatValue(long dbKey, int noRepeat) {
        this.dbKey = dbKey;
        this.noRepeat = noRepeat;
    }

    public TaskRepeatValue(Element xmlElem) throws JDOMException {

    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public int getNoRepeat() {
        return noRepeat;
    }

    public void setNoRepeat(int noRepeat) {
        this.noRepeat = noRepeat;
    }

    @Override
    protected Object clone() {
       try {
            TaskRepeatValue v = (TaskRepeatValue) super.clone() ;

            v.setDbKey(this.dbKey);
            v.setNoRepeat(noRepeat);
            return v;
       } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    public Element toXML(){
        return null;
    }

    public Element toXML(Element element){
        element.addContent(new Element(TAG_TASK_REPEAT_NO_REPEAT).setText(Integer.toString(noRepeat)));
        return element;
    }
}
