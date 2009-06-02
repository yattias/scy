/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.edp.EdPPanel;
import org.jdom.Element;


/**
 * action nommee
 * @author Marjolaine
 */
public class CopexActionNamed extends CopexAction implements Cloneable {
    // PROPERTY 
    /* action nommee */
    protected InitialNamedAction namedAction;

    // CONSTRUCTOR
    public CopexActionNamed(String description, String comments, InitialNamedAction namedAction) {
        super(description, comments);
        this.namedAction = namedAction;
        setName();
    }

    public CopexActionNamed(long dbKey, String name, String description, String comments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, InitialNamedAction namedAction, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage,draw,  isVisible, taskRight, dbkeyBrother, dbKeyChild, taskRepeat);
        this.namedAction = namedAction;
        setName();
    }

    public CopexActionNamed(long dbKey, String name, String description, String comments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, taskRepeat);
        this.namedAction = namedAction;
        setName();
    }

    // GETTER AND SETTER
    public InitialNamedAction getNamedAction() {
        return namedAction;
    }

    public void setNamedAction(InitialNamedAction namedAction) {
        this.namedAction = namedAction;
    }

    // OVERRIDE
    @Override
    public Object clone() {
        CopexActionNamed a = (CopexActionNamed) super.clone() ;

        a.setNamedAction((InitialNamedAction)this.namedAction.clone());
        return a;
    }

    // METHOD
    /* mise à jour du nom */
     private void setName(){
        if (this.namedAction != null)
            this.name = namedAction.getLibelle() ;
    }
     /* construction de la description de l'action dans le cas d'une action parametree */
    @Override
     public String toDescription(EdPPanel edP){
         return this.getName() + (this.description == null || this.description.length() == 0 ? "" : " : "+this.description);

     }
}
