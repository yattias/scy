/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;



/**
 * question, goal of the procedure
 * @author MBO
 */
public class Question extends CopexTask implements Cloneable{
    public final static String TAG_QUESTION = "question";

    
    public Question(long dbKey, List<LocalText> listQuestion, List<LocalText> listDescription, List<LocalText> listComments, String taskImage, Element draw,  boolean isVisible, TaskRight taskRight, boolean root) {
        super(dbKey, listQuestion, listDescription, listComments, taskImage, draw, isVisible, taskRight, root, null) ;
    }
    public Question(long dbKey, Locale locale,String question, String description, String comment, String taskImage, Element draw,  boolean isVisible, TaskRight taskRight, boolean root) {
        super(dbKey, CopexUtilities.getLocalText(question, locale), CopexUtilities.getLocalText(description, locale), CopexUtilities.getLocalText(comment, locale), taskImage, draw, isVisible, taskRight, root, null) ;
    }

    public Question(long dbKey, List<LocalText> listQuestion, List<LocalText> listDescription, List<LocalText> listComments, String taskImage, Element draw,  boolean isVisible, TaskRight taskRight, boolean root, long dbKeyBrother, long dbKeyChild) {
        super(dbKey, listQuestion, listDescription, listComments, taskImage, draw, isVisible, taskRight,  root, dbKeyBrother, dbKeyChild, null) ;
    }

    public Question(long dbKey, Locale locale, String question, String description, String comment, String taskImage, Element draw,  boolean isVisible, TaskRight taskRight, boolean root, long dbKeyBrother, long dbKeyChild) {
        super(dbKey, CopexUtilities.getLocalText(question, locale), CopexUtilities.getLocalText(description, locale), CopexUtilities.getLocalText(comment, locale), taskImage, draw, isVisible, taskRight,  root, dbKeyBrother, dbKeyChild, null) ;
    }

    /*sub question */
    public Question(List<LocalText> listDescription, List<LocalText> listComments){
        super(-1, new LinkedList(), listDescription, listComments, null, null, true, new TaskRight(MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT), false, null) ;
    }

    public Question(Locale locale,String description, String comment){
        super(-1, new LinkedList(), CopexUtilities.getLocalText(description, locale), CopexUtilities.getLocalText(comment, locale), null, null, true, new TaskRight(MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT), false, null) ;
    }


    /* fictiv root */
    public Question(Locale locale){
        super(locale);
    }

    public Question(Element xmlElem, long idTask) throws JDOMException {
        super(xmlElem);
        this.dbKeyBrother = -1;
        this.dbKeyChild = -1;
        if (xmlElem.getName().equals(TAG_QUESTION)) {
            dbKey = idTask;
            setRoot(true);
            listName = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_TASK_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listName.add(new LocalText(e.getText(), l));
            }
            listDescription = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_TASK_DESCRIPTION).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listDescription.add(new LocalText(e.getText(), l));
            }
            listComments = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_TASK_COMMENTS).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listComments.add(new LocalText(e.getText(), l));
            }
            taskImage = xmlElem.getChildText(TAG_TASK_IMAGE);
            draw = xmlElem.getChild(TAG_TASK_DRAW);
            taskRight = new TaskRight(xmlElem.getChild(TaskRight.TAG_TASK_RIGHT));
        } else {
            throw(new JDOMException("Question expects <"+TAG_QUESTION+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }


    @Override
    public Element toXML(){
        Element element = super.toXML(new Element(TAG_QUESTION));
        return element;
    }

   
    
    
    
}
