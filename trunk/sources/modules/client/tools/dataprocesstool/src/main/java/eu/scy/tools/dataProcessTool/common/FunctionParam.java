/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

/**
 * parameter of a function
 * @author Marjolaine
 */
public class FunctionParam implements Cloneable{
    /* identifiant */
    private long dbKey ;
    /* nom */
    private String param;
    /* valeur */
    private double value;

    public FunctionParam(long dbKey, String param, double value) {
        this.dbKey = dbKey;
        this.param = param;
        this.value = value;
    }

    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


    // CLONE
    @Override
    public Object clone()  {
        try {
            FunctionParam fp = (FunctionParam)super.clone();
            fp.setDbKey(dbKey);
            fp.setParam(new String(this.param));
            fp.setValue(new Double(this.value));

            return fp;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

}
