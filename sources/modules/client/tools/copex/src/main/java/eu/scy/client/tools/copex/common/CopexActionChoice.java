/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * action - choice type
 * @author Marjolaine
 */
public class CopexActionChoice extends CopexActionParam implements Cloneable {
    public final static String TAG_ACTION_CHOICE = "action_choice";

    public CopexActionChoice(List<LocalText> listDescription, List<LocalText> listComments, InitialNamedAction namedAction, Object[] tabParam) {
        super(listDescription, listComments, namedAction, tabParam);
    }

    public CopexActionChoice(long dbKey, List<LocalText> listName, List<LocalText> listDescription, List<LocalText> listComments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, InitialNamedAction namedAction, Object[] tabParam, TaskRepeat taskRepeat) {
        super(dbKey, listName, listDescription, listComments, taskImage, draw, isVisible, taskRight, dbkeyBrother, dbKeyChild, namedAction, tabParam, taskRepeat);
    }

    public CopexActionChoice(long dbKey, List<LocalText> listName, List<LocalText> listDescription, List<LocalText> listComments, String taskImage,Element draw,  boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, Object[] tabParam, TaskRepeat taskRepeat) {
        super(dbKey, listName, listDescription, listComments, taskImage, draw, isVisible, taskRight, namedAction, tabParam, taskRepeat);
    }

    public CopexActionChoice(long dbKey, Locale locale, String name, String description, String comment, String taskImage,Element draw,  boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, Object[] tabParam, TaskRepeat taskRepeat) {
        super(dbKey, locale, name, description, comment, taskImage, draw, isVisible, taskRight, namedAction, tabParam, taskRepeat);
    }

    public CopexActionChoice(Element xmlElem, long idTask, List<InitialNamedAction> listInitialNamedAction, long idRepeat, long idParam, long idValue, long idActionParam, long idQuantity, List<PhysicalQuantity> listPhysicalQuantity, List<TypeMaterial> listTypeMaterial, List<InitialParamData> listInitialParamData, List<InitialParamMaterial> listInitialParamMaterial, List<InitialParamQuantity> listInitialParamQuantity, List<InitialAcquisitionOutput> listInitialAcquisitionOutput, List<InitialManipulationOutput> listInitialManipulationOutput, List<InitialTreatmentOutput> listInitialTreatmentOutput, List<Material> listMaterial, List<ActionParamQuantity> listActionParamQuantity) throws JDOMException {
        super(xmlElem);
        this.dbKeyBrother = -1;
        this.dbKeyChild = -1;
        if (xmlElem.getName().equals(TAG_ACTION_CHOICE)) {
            dbKey = idTask;
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
            if(xmlElem.getChild(TaskRepeat.TAG_REPEAT_TASK) != null){
                taskRepeat = new TaskRepeat(xmlElem.getChild(TaskRepeat.TAG_REPEAT_TASK), idRepeat++, idParam++, idValue++, idActionParam++, idQuantity++,  listPhysicalQuantity, listTypeMaterial, listInitialParamData, listInitialParamMaterial, listInitialParamQuantity, listInitialAcquisitionOutput, listInitialManipulationOutput, listInitialTreatmentOutput, listMaterial, listActionParamQuantity);
            }
            namedAction = new InitialNamedAction(xmlElem.getChild(InitialNamedAction.TAG_INITIAL_NAMED_ACTION), listInitialNamedAction);
            
	} else {
            throw(new JDOMException("Action Choice expects <"+TAG_ACTION_CHOICE+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }



    @Override
    public Element toXML(){
        Element element = super.toXML(new Element(TAG_ACTION_CHOICE));
        for(int l=0; l<listDescription.size(); l++){
            Locale loc = listDescription.get(l).getLocale();
            String d = toDescription(loc);
            Element el = new Element(TAG_ACTION_NAMED_TOSTRING).setText(d);
            el.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, loc.getLanguage()));
            element.addContent(el);
        }
        return element;
    }

}
