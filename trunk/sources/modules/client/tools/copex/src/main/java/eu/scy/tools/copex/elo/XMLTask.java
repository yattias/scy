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
 * task : question, step or action
 * @author Marjolaine
 */
public class XMLTask {
    /*tag names */
    public final static String TAG_TASK = "task";
    private final static String TAG_TASK_ID = "id_task";
    private final static String TAG_TASK_NATURE = "nature";
    private final static String TAG_TASK_NAME = "task_name";
    private final static String TAG_TASK_DESCRIPTION = "description";
    private final static String TAG_TASK_COMMENTS = "comments";
    private final static String TAG_TASK_HYPOTHESIS = "hypothesis";
    private final static String TAG_TASK_PRINCIPLE = "principle";
    private final static String TAG_TASK_IMAGE = "image";
    private final static String TAG_TASK_DRAW="draw";
    private final static String TAG_TASK_NAMED_ACTION = "action_named";

    public final static String TASK_NATURE_QUESTION = "question";
    public final static String TASK_NATURE_STEP = "step";
    public final static String TASK_NATURE_ACTION = "action";

    /* id Task*/
    private String idTask;
    /* nature : question, step or action*/
    private String nature;
    /* name */
    private String taskName;
    /* description */
    private String description;
    /* comments */
    private String comments;
    /* hypothesis */
    private String hypothesis;
    /* principle */
    private String principle;
    /* image */
    private String image;
    /* draw*/
    private Element draw;
    /* droit */
    private XMLTaskRight right;
    /* action named */
    private String actionNamed;
    /* liste des sous taches */
    private List<XMLTask> listTask;

    public XMLTask(String idTask, String nature, String taskName, String description, String comments, String hypothesis, String principle, String image,Element draw,  XMLTaskRight right, String actionNamed, List<XMLTask> listTask) {
        this.idTask = idTask;
        this.nature = nature;
        this.taskName = taskName;
        this.description = description;
        this.comments = comments;
        this.hypothesis = hypothesis;
        this.principle = principle;
        this.image = image;
        this.draw = draw;
        this.right = right;
        this.actionNamed = actionNamed;
        this.listTask = listTask;
    }

    public XMLTask(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_TASK)) {
			idTask = xmlElem.getChild(TAG_TASK_ID).getText();
            nature = xmlElem.getChildText(TAG_TASK_NATURE);
            taskName = xmlElem.getChildText(TAG_TASK_NAME);
            description = xmlElem.getChildText(TAG_TASK_DESCRIPTION);
            comments = xmlElem.getChildText(TAG_TASK_COMMENTS);
            hypothesis = xmlElem.getChildText(TAG_TASK_HYPOTHESIS);
            principle = xmlElem.getChildText(TAG_TASK_PRINCIPLE);
            image = xmlElem.getChildText(TAG_TASK_IMAGE);
            draw = xmlElem.getChild(TAG_TASK_DRAW);
            right = new XMLTaskRight(xmlElem.getChild(XMLTaskRight.TAG_TASK_RIGHT));
            listTask = new LinkedList<XMLTask>();
			for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_TASK).iterator(); variableElem.hasNext();) {
				listTask.add(new XMLTask(variableElem.next()));
			}
		} else {
			throw(new JDOMException("Task expects <"+TAG_TASK+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    public String getActionNamed() {
        return actionNamed;
    }

    public String getComments() {
        return comments;
    }

    public String getDescription() {
        return description;
    }

    public Element getDraw() {
        return draw;
    }

    public String getHypothesis() {
        return hypothesis;
    }

    public String getIdTask() {
        return idTask;
    }

    public String getImage() {
        return image;
    }

    public String getNature() {
        return nature;
    }

    public List<XMLTask> getListTask() {
        return listTask;
    }

    public String getPrinciple() {
        return principle;
    }

    public XMLTaskRight getRight() {
        return right;
    }

    public String getTaskName() {
        return taskName;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_TASK);
		element.addContent(new Element(TAG_TASK_ID).setText(idTask));
        element.addContent(new Element(TAG_TASK_NATURE).setText(nature));
        if (taskName != null)
            element.addContent(new Element(TAG_TASK_NAME).setText(taskName));
        if (description != null)
            element.addContent(new Element(TAG_TASK_DESCRIPTION).setText(description));
        if (hypothesis != null)
            element.addContent(new Element(TAG_TASK_HYPOTHESIS).setText(hypothesis));
        if (principle != null)
            element.addContent(new Element(TAG_TASK_PRINCIPLE).setText(principle));
        if (comments != null)
            element.addContent(new Element(TAG_TASK_COMMENTS).setText(comments));
        if (image != null)
            element.addContent(new Element(TAG_TASK_IMAGE).setText(image));
        if(draw != null){
            Element e = new Element(TAG_TASK_DRAW) ;
            e.addContent(draw);
            element.addContent(e);
        }
        if (actionNamed != null)
            element.addContent(new Element(TAG_TASK_NAMED_ACTION).setText(actionNamed));
        element.addContent(right.toXML());
        if (listTask != null){
            for (Iterator<XMLTask> t = listTask.iterator(); t.hasNext();) {
				element.addContent(t.next().toXML());
            }
        }
		return element;
    }



}
