/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import org.jdom.Element;
import org.jdom.JDOMException;


/**
 * droit d'une tache
 * @author MBO
 */
public class TaskRight implements Cloneable {
    /* tag names */
    public final static String TAG_TASK_RIGHT = "task_right";
    private final static String TAG_EDIT_RIGHT = "edit_right";
    private final static String TAG_DELETE_RIGHT = "delete_right";
    private final static String TAG_MOVE_RIGHT = "move_right";
    private final static String TAG_PARENT_RIGHT = "parent_right";
    private final static String TAG_COPY_RIGHT = "copy_right";
    private final static String TAG_DRAW_RIGHT = "draw_right";
    private final static String TAG_REPEAT_RIGHT = "repeat_right";

    
    /* droit edition */
    private char editRight;
    /* droit suppression */
    private char deleteRight;
    /* droit copie */
    private char copyRight;
    /* droit de deplacer */
    private char moveRight;
    /* droit ajouter sous tache */
    private char parentRight;
    /* droit de creer un dessin associe a la tache */
    private char drawRight;
    /* droit a la repetition */
    private char repeatRight;

    // CONSTRUCTEURS
    public TaskRight(char editRight, char deleteRight, char copyRight, char moveRight, char parentRight, char drawRight, char repeatRight ) {
        this.editRight = editRight;
        this.deleteRight = deleteRight;
        this.copyRight = copyRight;
        this.moveRight = moveRight;
        this.parentRight = parentRight;
        this.drawRight = drawRight ;
        this.repeatRight = repeatRight;
    }

    public TaskRight(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_TASK_RIGHT)) {
			editRight = xmlElem.getChild(TAG_EDIT_RIGHT).getText().charAt(0);
            deleteRight = xmlElem.getChild(TAG_DELETE_RIGHT).getText().charAt(0);
            copyRight = xmlElem.getChild(TAG_COPY_RIGHT).getText().charAt(0);
            moveRight = xmlElem.getChild(TAG_MOVE_RIGHT).getText().charAt(0);
            parentRight = xmlElem.getChild(TAG_PARENT_RIGHT).getText().charAt(0);
            drawRight = xmlElem.getChild(TAG_DRAW_RIGHT).getText().charAt(0);
            repeatRight = xmlElem.getChild(TAG_REPEAT_RIGHT).getText().charAt(0);
		} else {
			throw(new JDOMException("Task Right expects <"+TAG_TASK_RIGHT+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    // GETTER AND SETTER
    public char getCopyRight() {
        return copyRight;
    }

    public void setCopyRight(char copyRight) {
        this.copyRight = copyRight;
    }

    public char getDeleteRight() {
        return deleteRight;
    }

    public void setDeleteRight(char deleteRight) {
        this.deleteRight = deleteRight;
    }

    public char getEditRight() {
        return editRight;
    }

    public void setEditRight(char editRight) {
        this.editRight = editRight;
    }

    public char getMoveRight() {
        return moveRight;
    }

    public void setMoveRight(char moveRight) {
        this.moveRight = moveRight;
    }

    public char getParentRight() {
        return parentRight;
    }

    public void setParentRight(char parentRight) {
        this.parentRight = parentRight;
    }

    public char getDrawRight() {
        return drawRight;
    }

    public void setDrawRight(char drawRight) {
        this.drawRight = drawRight;
    }

    public char getRepeatRight() {
        return repeatRight;
    }

    public void setRepeatRight(char repeatRight) {
        this.repeatRight = repeatRight;
    }

    // OVERRIDE
    @Override
    protected Object clone() throws CloneNotSupportedException {
       try {
            TaskRight taskRight = (TaskRight) super.clone() ;
            
            taskRight.setEditRight(new Character(this.getEditRight()));
            taskRight.setDeleteRight(new Character(this.getDeleteRight()));
            taskRight.setCopyRight(new Character(this.getCopyRight()));
            taskRight.setMoveRight(new Character(this.getMoveRight()));
            taskRight.setParentRight(new Character(this.getParentRight()));
            taskRight.setDrawRight(new Character(this.getDrawRight()));
            taskRight.setRepeatRight(new Character(this.getRepeatRight()));
            
            return taskRight;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }

    @Override
    public String toString() {
        String s = "Droits de la tache : ";
        s += "editer : "+getEditRight();
        s += "supprimer : "+getDeleteRight();
        s += "copier : "+getCopyRight();
        s += "deplacer : "+getMoveRight();
        s += "ajouter des sous taches : "+getParentRight();
        s += "creer dessin : "+getDrawRight() ;
        s += "repeter : "+getRepeatRight() ;
        return s;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_TASK_RIGHT);
		element.addContent(new Element(TAG_EDIT_RIGHT).setText(Character.toString(editRight)));
        element.addContent(new Element(TAG_DELETE_RIGHT).setText(Character.toString(deleteRight)));
        element.addContent(new Element(TAG_COPY_RIGHT).setText(Character.toString(copyRight)));
        element.addContent(new Element(TAG_MOVE_RIGHT).setText(Character.toString(moveRight)));
        element.addContent(new Element(TAG_PARENT_RIGHT).setText(Character.toString(parentRight)));
        element.addContent(new Element(TAG_DRAW_RIGHT).setText(Character.toString(drawRight)));
        element.addContent(new Element(TAG_REPEAT_RIGHT).setText(Character.toString(repeatRight)));
		return element;
    }
    
    
    
}
