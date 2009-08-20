/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.elo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * type actions
 * @author Marjolaine
 */
public class XMLActionType {
    /*tag names */
    public final static String TAG_ACTION_TYPE = "action_type";
    private final static String TAG_FREE_ACTION = "free_action";
    private final static String TAG_TASK_REPEAT = "task_repeat";

    /*is Free Action */
    private boolean freeAction;
    private boolean taskRepeat;
    /* liste d'actions nommees */
    private List<XMLNamedAction> listNamedAction;
    /* liste d'actions parametrees */

    public XMLActionType(boolean freeAction,boolean taskRepeat, List<XMLNamedAction> listNamedAction) {
        this.freeAction = freeAction;
        this.taskRepeat = taskRepeat;
        this.listNamedAction = listNamedAction;
    }

    public XMLActionType(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_ACTION_TYPE)) {
			freeAction = xmlElem.getChild(TAG_FREE_ACTION).getText().equals("true");
            taskRepeat = xmlElem.getChild(TAG_TASK_REPEAT).getText().equals("true");
            if (xmlElem.getChild(XMLNamedAction.TAG_NAMED_ACTION) != null){
                listNamedAction = new LinkedList<XMLNamedAction>();
                for (Iterator<Element> variableElem = xmlElem.getChildren(XMLNamedAction.TAG_NAMED_ACTION).iterator(); variableElem.hasNext();) {
                    listNamedAction.add(new XMLNamedAction(variableElem.next()));
                }
            }
		} else {
			throw(new JDOMException("Action Type expects <"+TAG_ACTION_TYPE+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public boolean isFreeAction() {
        return freeAction;
    }
    public boolean isTaskRepeat() {
        return taskRepeat;
    }
    public List<XMLNamedAction> getListNamedAction() {
        return listNamedAction;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_ACTION_TYPE);
		element.addContent(new Element(TAG_FREE_ACTION).setText(freeAction ? "true" : "false"));
        element.addContent(new Element(TAG_TASK_REPEAT).setText(taskRepeat ? "true" : "false"));
        if (listNamedAction != null){
            for (Iterator<XMLNamedAction> a = listNamedAction.iterator(); a.hasNext();) {
                element.addContent(a.next().toXML());
            }
        }
		return element;
    }




}
