/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;


/**
 * parametre d'une action de type quantite
 * @author Marjolaine
 */
public class ActionParamQuantity extends ActionParam{
    // PROPERTY
    /*parametre/quantité lié */
    private Parameter parameter;

    // CONSTRUCTOR
    public ActionParamQuantity(long dbKey, InitialActionParam initialParam, Parameter parameter) {
        super(dbKey, initialParam);
        this.parameter = parameter;
    }

    // GETTER AND SETTER
    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        ActionParamQuantity p = (ActionParamQuantity) super.clone() ;

        p.setParameter((Parameter)this.parameter.clone());
        return p;
    }


}
