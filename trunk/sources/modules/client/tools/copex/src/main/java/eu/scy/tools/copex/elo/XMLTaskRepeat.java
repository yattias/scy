/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.elo;

import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * repeat task
 * @author Marjolaine
 */
public class XMLTaskRepeat {
    /*tag names */
    public final static String TAG_REPEAT_TASK = "repeat_task";
    private final static String TAG_REPEAT_TASK_ID = "id_repeat_task";
    private final static String TAG_REPEAT_TASK_NB = "nb_repeat_task";

    /* id repeat */
    private String idRepeat;
    /* nb repeat */
    private String nbRepeat;
    /* liste des parametres et de leurs valeurs */

     public XMLTaskRepeat(String idRepeat, String nbRepeat) {
        this.idRepeat = idRepeat;
        this.nbRepeat = nbRepeat;
    }

     public XMLTaskRepeat(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_REPEAT_TASK)) {
			idRepeat = xmlElem.getChild(TAG_REPEAT_TASK_ID).getText();
            nbRepeat = xmlElem.getChild(TAG_REPEAT_TASK_NB).getText();
		} else {
			throw(new JDOMException("Task expects <"+TAG_REPEAT_TASK+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public String getIdRepeat() {
        return idRepeat;
    }

    public String getNbRepeat() {
        return nbRepeat;
    }

     // toXML
    public Element toXML(){
        Element element = new Element(TAG_REPEAT_TASK);
		element.addContent(new Element(TAG_REPEAT_TASK_ID).setText(idRepeat));
        element.addContent(new Element(TAG_REPEAT_TASK_NB).setText(nbRepeat));
		return element;
    }
}
