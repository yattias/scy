/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.elo;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.omg.IOP.TAG_INTERNET_IOP;

/**
 * named action
 * @author Marjolaine
 */
public class XMLNamedAction {
    /* tag names */
    public final static String TAG_NAMED_ACTION = "named_action";
    private final static String TAG_NAMED_ACTION_ID = "id_action";
    private final static String TAG_NAMED_ACTION_NAME = "action_name";
    private final static String TAG_NAMED_ACTION_DRAW = "draw";
    private final static String TAG_NAMED_ACTION_REPEAT = "repeat";

    /*id action */
    private String idAction;
    /* action name */
    private String nameAction;
    /* can draw */
    private boolean  draw;
    /* can repeat */
    private boolean  repeat;

    public XMLNamedAction(String idAction, String nameAction, boolean draw, boolean repeat) {
        this.idAction = idAction;
        this.nameAction = nameAction;
        this.draw = draw;
        this.repeat = repeat;
    }

     public XMLNamedAction(Element xmlElem) throws JDOMException {
        if (xmlElem.getName().equals(TAG_NAMED_ACTION)) {
            idAction = xmlElem.getChild(TAG_NAMED_ACTION_ID).getText();
            nameAction = xmlElem.getChild(TAG_NAMED_ACTION_NAME).getText();
            draw = xmlElem.getChild(TAG_NAMED_ACTION_DRAW).getText().equals("true");
            repeat = xmlElem.getChild(TAG_NAMED_ACTION_REPEAT).getText().equals("true");
		} else {
			throw(new JDOMException("Named Action expects <"+TAG_NAMED_ACTION+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public String getIdAction() {
        return idAction;
    }

    public String getNameAction() {
        return nameAction;
    }

    public boolean isDraw() {
        return draw;
    }

    public boolean isRepeat() {
        return repeat;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_NAMED_ACTION);
		element.addContent(new Element(TAG_NAMED_ACTION_ID).setText(idAction));
        element.addContent(new Element(TAG_NAMED_ACTION_NAME).setText(nameAction));
        element.addContent(new Element(TAG_NAMED_ACTION_DRAW).setText(draw ? "true" : "false"));
        element.addContent(new Element(TAG_NAMED_ACTION_REPEAT).setText(repeat ? "true" : "false"));
		return element;
    }

}
