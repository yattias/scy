/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.common;

/**
 * parametres d'une operation
 * de type double
 * pour les operations de type TypeOperationParam
 * @author Marjolaine
 */
public class ParamOperation implements Cloneable {
    // PROPERTY
    /* identifiant */
    private long dbKey;
    /* valeur */
    private Double value;
    /* indice dans le libelle */
    private int index;

    // CONSTRUCTOR
    public ParamOperation(long dbKey, Double value, int index) {
        this.dbKey = dbKey;
        this.value = value;
        this.index = index;
    }

    // GETTER AND SETTER
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }


    // CLONE
    @Override
    public Object clone()  {
        try {
            ParamOperation param = (ParamOperation) super.clone() ;
            param.setDbKey(this.dbKey);
            param.setValue(this.value == null ? null : new Double(this.value));
            param.setIndex(this.index);

            return param;
        } catch (CloneNotSupportedException e) {
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

}
