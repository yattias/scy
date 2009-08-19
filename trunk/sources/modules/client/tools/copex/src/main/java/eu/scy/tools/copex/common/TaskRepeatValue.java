/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * valeur d'un parametre pour une tache iterative
 * @author Marjolaine
 */
public abstract class TaskRepeatValue implements Cloneable{
    protected long dbKey ;
    protected int noRepeat;

    public TaskRepeatValue(long dbKey, int noRepeat) {
        this.dbKey = dbKey;
        this.noRepeat = noRepeat;
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

    // OVERRIDE
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
}
