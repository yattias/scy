/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.saveProcXml;

import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * materiel utilise
 * @author Marjolaine
 */
public class Procedure {
    /* tag names */
    public static final String TAG_PROCEDURE = "procedure";

    /* question*/
    private Question question;

    public Procedure(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public Procedure(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_PROCEDURE)) {
			question = new Question(xmlElem.getChild(Question.TAG_QUESTION));
		} else {
			throw(new JDOMException("Procedure expects <"+TAG_PROCEDURE+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_PROCEDURE);
        element.addContent(question.toXML());
		return element;
    }
}
