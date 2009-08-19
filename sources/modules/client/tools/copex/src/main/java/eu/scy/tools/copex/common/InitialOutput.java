/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * output of a task
 * @author Marjolaine
 */
public class InitialOutput implements Cloneable{
    /* identifiant */
    protected long dbKey;
    /*text prod */
    protected String textProd;
    /*nom du param */
    protected String name;

    public InitialOutput(long dbKey, String textProd, String name) {
        this.dbKey = dbKey;
        this.textProd = textProd;
        this.name = name;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTextProd() {
        return textProd;
    }

    public void setTextProd(String textProd) {
        this.textProd = textProd;
    }
    @Override
    protected Object clone(){
        try {
            InitialOutput o = (InitialOutput) super.clone() ;

            o.setDbKey(this.dbKey);
            o.setTextProd(new String (this.textProd));
            o.setName(new String(this.name));

            return o;
        }catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

}
