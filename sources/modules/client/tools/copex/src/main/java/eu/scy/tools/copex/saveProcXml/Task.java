/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.saveProcXml;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author Marjolaine
 */
public class Task {
    /* tag names */
    public static final String TAG_TASK = "task";
    public static final String TAG_TASK_ID = "id";
    public static final String TAG_TASK_NATURE = "nature";
    public static final String TAG_TASK_NAME = "name";
    public static final String TAG_TASK_DESCRIPTION = "description";
    public static final String TAG_TASK_HYPOTHESIS = "hypothesis";
    public static final String TAG_TASK_COMMENTS = "comments";
    public static final String TAG_TASK_PRINCIPLE = "principle";
    public static final String TAG_TASK_IMAGE = "image";

    /* id*/
    private String idTask;
    /* nature : question / step / action */
    private String nature;
    /* name */
    private String name;
    /* description */
    private String description ;
    /* comments*/
    private String comments;
    /* image*/
    private String image;
    /* hypothesis */
    private String hypothesis;
    /* principe */
    private String principle;
    /* liste des sous taches */
    private List<Task> listSubTask;

    public Task(String idTask, String nature, String name, String description, String comments, String image, String hypothesis, String principle, List<Task> listSubTask) {
        this.idTask = idTask;
        this.nature = nature;
        this.name = name;
        this.description = description;
        this.comments = comments;
        this.image = image;
        this.hypothesis = hypothesis;
        this.principle = principle;
        this.listSubTask = listSubTask ;
    }

     public Task(Element xmlElem) throws JDOMException {
		if (xmlElem.getName().equals(TAG_TASK)) {
			idTask = xmlElem.getChild(TAG_TASK_ID).getText();
            nature = xmlElem.getChild(TAG_TASK_NATURE).getText();
            if(xmlElem.getChild(TAG_TASK_NAME) != null){
                name = xmlElem.getChild(TAG_TASK_NAME).getText() ;
            }
            if(xmlElem.getChild(TAG_TASK_DESCRIPTION) != null){
                description = xmlElem.getChild(TAG_TASK_DESCRIPTION).getText() ;
            }
            if(xmlElem.getChild(TAG_TASK_COMMENTS) != null){
                comments = xmlElem.getChild(TAG_TASK_COMMENTS).getText() ;
            }
            if(xmlElem.getChild(TAG_TASK_HYPOTHESIS) != null){
                hypothesis = xmlElem.getChild(TAG_TASK_HYPOTHESIS).getText() ;
            }
            if(xmlElem.getChild(TAG_TASK_PRINCIPLE) != null){
                principle = xmlElem.getChild(TAG_TASK_PRINCIPLE).getText() ;
            }
            if(xmlElem.getChild(TAG_TASK_IMAGE) != null){
                image = xmlElem.getChild(TAG_TASK_IMAGE).getText() ;
            }
            if (xmlElem.getChildren(TAG_TASK) != null){
                listSubTask = new LinkedList<Task>();
                for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_TASK).iterator(); variableElem.hasNext();) {
                    listSubTask.add(new Task(variableElem.next()));
                }
            }
		} else {
			throw(new JDOMException("Task expects <"+TAG_TASK+"> as root element, but found <"+xmlElem.getName()+">."));
		}
	}

    
    public String getComments() {
        return comments;
    }

    public String getDescription() {
        return description;
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

    public String getName() {
        return name;
    }

    public String getNature() {
        return nature;
    }

    public String getPrinciple() {
        return principle;
    }

    public List<Task> getListSubTask() {
        return listSubTask;
    }

    // toXML
    public Element toXML(){
        Element element = new Element(TAG_TASK);
		element.addContent(new Element(TAG_TASK_ID).setText(idTask));
        element.addContent(new Element(TAG_TASK_NATURE).setText(nature));
        if(name != null && !name.equals(""))
            element.addContent(new Element(TAG_TASK_NAME).setText(name));
        element.addContent(new Element(TAG_TASK_DESCRIPTION).setText(description));
        if(comments != null && !comments.equals(""))
            element.addContent(new Element(TAG_TASK_COMMENTS).setText(comments));
        if(hypothesis != null && !hypothesis.equals(""))
            element.addContent(new Element(TAG_TASK_HYPOTHESIS).setText(hypothesis));
        if(principle != null && !principle.equals(""))
            element.addContent(new Element(TAG_TASK_PRINCIPLE).setText(principle));
        if(image != null && !image.equals(""))
            element.addContent(new Element(TAG_TASK_IMAGE).setText(image));
        if(listSubTask != null && listSubTask.size() > 0){
            for (Iterator<Task> t = listSubTask.iterator(); t.hasNext();) {
                    element.addContent(t.next().toXML());
            }
        }
		return element;
    }
}
