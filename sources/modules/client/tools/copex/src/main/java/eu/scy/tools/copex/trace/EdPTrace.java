/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.trace;

/**
 * trace de l'utilisateur dans l'éditeur de proc
 * @author Marjolaine
 */
public abstract class EdPTrace {
    // ATTRIBUTS
    /* type de l'action */
    protected String type;

    // CONSTRUCTOR
    public EdPTrace(String type) {
        this.type = type;
    }

    // GETTER AND SETTER
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    


}
