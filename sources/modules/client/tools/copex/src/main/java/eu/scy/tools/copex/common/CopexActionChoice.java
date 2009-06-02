/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import org.jdom.Element;

/**
 *
 * @author Marjolaine
 */
public class CopexActionChoice extends CopexActionParam implements Cloneable {

    // CONSTRUCTOR
    public CopexActionChoice(String description, String comments, InitialNamedAction namedAction, ActionParam[] tabParam) {
        super(description, comments, namedAction, tabParam);
    }

    public CopexActionChoice(long dbKey, String name, String description, String comments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, InitialNamedAction namedAction, ActionParam[] tabParam, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, dbkeyBrother, dbKeyChild, namedAction, tabParam, taskRepeat);
    }

    public CopexActionChoice(long dbKey, String name, String description, String comments, String taskImage,Element draw,  boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, ActionParam[] tabParam, TaskRepeat taskRepeat) {
        super(dbKey, name, description, comments, taskImage, draw, isVisible, taskRight, namedAction, tabParam, taskRepeat);
    }

}
