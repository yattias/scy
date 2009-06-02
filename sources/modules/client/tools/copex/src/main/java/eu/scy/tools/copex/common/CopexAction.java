/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.utilities.MyConstants;
import org.jdom.Element;

/**
 * represente une action
 * @author MBO
 */
public class CopexAction extends CopexTask implements Cloneable {

    // CONSTRUCTEURS
    public CopexAction(long dbKey, String name, String description, String comments, String taskImage, Element draw,  boolean isVisible, TaskRight taskRight, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, false, taskRepeat);
    }
     public CopexAction(long dbKey, String name, String description, String comments, String taskImage,Element draw,  boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, false, dbkeyBrother, dbKeyChild, taskRepeat);
        
    }
    /* constructeur d'une action suite à une saisie utilisateur */
    public CopexAction(String description, String comments){
        super(-1, "", description, comments, null, null, true, new TaskRight(MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT), false, null);
        
    }
     /* construction de la description de l'action dans le cas d'une action parametree */
     public String toDescription(EdPPanel edP){
         return this.description ;
     }

}
