/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.elo;

import eu.scy.tools.copex.utilities.CopexUtilities;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * question
 * @author Marjolaine
 */
public class XMLQuestion {
    /* tag names */
    public final static String TAG_QUESTION = "question";
    private final static String TAG_QUESTION_ID = "id_question";
    private final static String TAG_QUESTION_DESCRIPTION = "description";
    private final static String TAG_QUESTION_HYPOTHESIS = "hypothesis";
    private final static String TAG_QUESTION_PRINCIPLE = "general_principle";
    private final static String TAG_QUESTION_COMMENTS = "comments";
    private final static String TAG_QUESTION_IMAGE = "image";
    private final static String TAG_QUESTION_DRAW = "draw";

    /*id question */
    private String idQuestion;
    /* description */
    private String description;
    /* hypothesis */
    private String hypothesis;
    /*general principle */
    private String generalPrinciple;
    /* comments */
    private String comments;
    /* image */
    private String image;
    /* dessin */
    private Element draw;
    /* droits */
    private XMLTaskRight questionRight;
    /*liste des taches */
    private List<XMLTask> listTask;

    public XMLQuestion(String idQuestion, String description, String hypothesis, String generalPrinciple, String comments, String image, Element draw, XMLTaskRight questionRight, List<XMLTask> listTask) {
        this.idQuestion = idQuestion;
        this.description = description;
        this.hypothesis = hypothesis;
        this.generalPrinciple = generalPrinciple;
        this.comments = comments;
        this.image = image;
        this.draw = draw;
        this.questionRight = questionRight;
        this.listTask = listTask;
    }

    public XMLQuestion(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_QUESTION)) {
			idQuestion = xmlElem.getChild(TAG_QUESTION_ID).getText();
            description = xmlElem.getChildText(TAG_QUESTION_DESCRIPTION);
            comments = xmlElem.getChildText(TAG_QUESTION_COMMENTS);
            hypothesis = xmlElem.getChildText(TAG_QUESTION_HYPOTHESIS);
            generalPrinciple = xmlElem.getChildText(TAG_QUESTION_PRINCIPLE);
            image = xmlElem.getChildText(TAG_QUESTION_IMAGE);
            draw = xmlElem.getChild(TAG_QUESTION_DRAW);
            questionRight = new XMLTaskRight(xmlElem.getChild(XMLTaskRight.TAG_TASK_RIGHT));
            listTask = new LinkedList<XMLTask>();
			for (Iterator<Element> variableElem = xmlElem.getChildren(XMLTask.TAG_TASK).iterator(); variableElem.hasNext();) {
				listTask.add(new XMLTask(variableElem.next()));
			}
		} else {
			throw(new JDOMException("Question expects <"+TAG_QUESTION+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public String getComments() {
        return comments;
    }

    public String getDescription() {
        return description;
    }

    public String getGeneralPrinciple() {
        return generalPrinciple;
    }

    public String getHypothesis() {
        return hypothesis;
    }

    public String getImage() {
        return image;
    }

    public Element getDraw() {
        return draw;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public List<XMLTask> getListTask() {
        return listTask;
    }

    public XMLTaskRight getQuestionRight() {
        return questionRight;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_QUESTION);
		element.addContent(new Element(TAG_QUESTION_ID).setText(idQuestion));
        if (description != null){
            description = CopexUtilities.getUTF8String(description);
            element.addContent(new Element(TAG_QUESTION_DESCRIPTION).setText(description));
        }
        if (hypothesis != null){
            hypothesis = CopexUtilities.getUTF8String(hypothesis);
            element.addContent(new Element(TAG_QUESTION_HYPOTHESIS).setText(hypothesis));
        }
        if (generalPrinciple != null){
            generalPrinciple = CopexUtilities.getUTF8String(generalPrinciple);
            element.addContent(new Element(TAG_QUESTION_PRINCIPLE).setText(generalPrinciple));
        }
        if (comments != null){
            comments = CopexUtilities.getUTF8String(comments);
            element.addContent(new Element(TAG_QUESTION_COMMENTS).setText(comments));
        }
        if (image != null)
            element.addContent(new Element(TAG_QUESTION_IMAGE).setText(image));
        if(draw != null){
            Element e = new Element(TAG_QUESTION_DRAW);
            e.addContent(draw);
            element.addContent(e);
        }
        element.addContent(questionRight.toXML());
        if (listTask != null){
            for (Iterator<XMLTask> t = listTask.iterator(); t.hasNext();) {
				element.addContent(t.next().toXML());
            }
        }
		return element;
    }


}
