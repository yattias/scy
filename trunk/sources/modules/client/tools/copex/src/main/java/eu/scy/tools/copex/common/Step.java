/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.utilities.MyConstants;
import org.jdom.Element;


/**
 * represente une etape
 * @author MBO
 */
public class Step extends CopexTask{
    // CONSTRUCTEURS
    public Step(long dbKey ,String name, String description, String comments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, false, taskRepeat);
    }
    
    /* constructeur appele suite a la saisie par l'utilisateur */
    public Step(String description, String comments){
        super(-1, "", description, comments, null, null, true, new TaskRight(MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT), false, null );
    }
     public Step(long dbKey, String name, String description, String comments, String taskImage,Element draw, boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage,  draw, isVisible, taskRight, false, dbkeyBrother, dbKeyChild, taskRepeat);
    }
    
}
