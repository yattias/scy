/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

/**
 * parametre d'une action de type data
 * @author Marjolaine
 */
public class ActionParamData extends ActionParam implements Cloneable{
    // PROPERTY
    /*parametre/quantité lié */
    private QData data;

    // CONSTRUCTOR
    public ActionParamData(long dbKey, InitialActionParam initialParam, QData data) {
        super(dbKey, initialParam);
        this.data = data;
    }

    // GETTER AND SETTER
    public QData getData() {
        return data;
    }

    public void setData(QData data) {
        this.data = data;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        ActionParamData p = (ActionParamData) super.clone() ;

        p.setData((QData)this.data.clone());
        return p;
    }


}
