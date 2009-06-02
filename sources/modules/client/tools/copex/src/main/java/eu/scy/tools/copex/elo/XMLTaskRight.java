/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.elo;

import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * task right
 * @author Marjolaine
 */
public class XMLTaskRight {
    /* tag names */
    public final static String TAG_TASK_RIGHT = "task_right";
    private final static String TAG_EDIT_RIGHT = "edit_right";
    private final static String TAG_DELETE_RIGHT = "delete_right";
    private final static String TAG_MOVE_RIGHT = "move_right";
    private final static String TAG_PARENT_RIGHT = "parent_right";
    private final static String TAG_COPY_RIGHT = "copy_right";
    private final static String TAG_DRAW_RIGHT = "draw_right";
    private final static String TAG_REPEAT_RIGHT = "repeat_right";

    /* edit right */
    private String editRight;
    /*delete right */
    private String deleteRight;
    /*copy right */
    private String copyRight;
    /* move right */
    private String moveRight;
    /*parent right */
    private String parentRight;
    /* draw right */
    private String drawRight;
    /* repeat right */
    private String repeatRight;

    public XMLTaskRight(String editRight, String deleteRight, String copyRight, String moveRight, String parentRight, String drawRight, String repeatRight) {
        this.editRight = editRight;
        this.deleteRight = deleteRight;
        this.copyRight = copyRight;
        this.moveRight = moveRight;
        this.parentRight = parentRight;
        this.drawRight = drawRight ;
        this.repeatRight = repeatRight ;
    }

    public XMLTaskRight(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_TASK_RIGHT)) {
			editRight = xmlElem.getChild(TAG_EDIT_RIGHT).getText();
            deleteRight = xmlElem.getChild(TAG_DELETE_RIGHT).getText();
            copyRight = xmlElem.getChild(TAG_COPY_RIGHT).getText();
            moveRight = xmlElem.getChild(TAG_MOVE_RIGHT).getText();
            parentRight = xmlElem.getChild(TAG_PARENT_RIGHT).getText();
            drawRight = xmlElem.getChild(TAG_DRAW_RIGHT).getText();
            repeatRight = xmlElem.getChild(TAG_REPEAT_RIGHT).getText();
		} else {
			throw(new JDOMException("Task Right expects <"+TAG_TASK_RIGHT+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public String getCopyRight() {
        return copyRight;
    }

    public String getDeleteRight() {
        return deleteRight;
    }

    public String getEditRight() {
        return editRight;
    }

    public String getMoveRight() {
        return moveRight;
    }

    public String getParentRight() {
        return parentRight;
    }

    public String getDrawRight() {
        return drawRight;
    }

    public String getRepeatRight() {
        return repeatRight;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_TASK_RIGHT);
		element.addContent(new Element(TAG_EDIT_RIGHT).setText(editRight));
        element.addContent(new Element(TAG_DELETE_RIGHT).setText(deleteRight));
        element.addContent(new Element(TAG_COPY_RIGHT).setText(copyRight));
        element.addContent(new Element(TAG_MOVE_RIGHT).setText(moveRight));
        element.addContent(new Element(TAG_PARENT_RIGHT).setText(parentRight));
        element.addContent(new Element(TAG_DRAW_RIGHT).setText(drawRight));
        element.addContent(new Element(TAG_REPEAT_RIGHT).setText(repeatRight));
		return element;
    }

}
