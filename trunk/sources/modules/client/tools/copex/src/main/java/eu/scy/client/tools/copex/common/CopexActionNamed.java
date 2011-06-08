/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.main.CopexPanel;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;


/**
 * named actions
 * @author Marjolaine
 */
public class CopexActionNamed extends CopexAction implements Cloneable {
    public final static String TAG_ACTION_NAMED = "action_named";
    public final static String TAG_ACTION_NAMED_TOSTRING="all_description";
    /* initial named action */
    protected InitialNamedAction namedAction;

    public CopexActionNamed(List<LocalText> listDescription, List<LocalText> listComments, InitialNamedAction namedAction) {
        super(listDescription, listComments);
        this.namedAction = namedAction;
        setName();
    }


    public CopexActionNamed(long dbKey, List<LocalText> listName, List<LocalText> listDescription, List<LocalText> listComments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, InitialNamedAction namedAction, TaskRepeat taskRepeat) {
        super(dbKey, listName, listDescription, listComments, taskImage,draw,  isVisible, taskRight, dbkeyBrother, dbKeyChild, taskRepeat);
        this.namedAction = namedAction;
        setName();
    }

    public CopexActionNamed(long dbKey, List<LocalText> listName, List<LocalText> listDescription, List<LocalText> listComments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, TaskRepeat taskRepeat) {
        super(dbKey, listName, listDescription, listComments, taskImage, draw, isVisible, taskRight, taskRepeat);
        this.namedAction = namedAction;
        setName();
    }

    public CopexActionNamed(long dbKey,Locale locale, String name,String description, String comment, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, InitialNamedAction namedAction, TaskRepeat taskRepeat) {
        super(dbKey, locale,name, description, comment, taskImage, draw, isVisible, taskRight, taskRepeat);
        this.namedAction = namedAction;
        setName();
    }

    public CopexActionNamed(Element xmlElem) throws JDOMException {
        super(xmlElem);
    }

    public CopexActionNamed(Element xmlElem, long idTask, List<InitialNamedAction> listInitialNamedAction, long idRepeat, long idParam, long idValue, long idActionParam, long idQuantity, List<PhysicalQuantity> listPhysicalQuantity, List<TypeMaterial> listTypeMaterial, List<InitialParamData> listInitialParamData, List<InitialParamMaterial> listInitialParamMaterial, List<InitialParamQuantity> listInitialParamQuantity, List<InitialAcquisitionOutput> listInitialAcquisitionOutput, List<InitialManipulationOutput> listInitialManipulationOutput, List<InitialTreatmentOutput> listInitialTreatmentOutput, List<Material> listMaterial, List<ActionParamQuantity> listActionParamQuantity) throws JDOMException {
        super(xmlElem);
        this.dbKeyBrother = -1;
        this.dbKeyChild = -1;
        if (xmlElem.getName().equals(TAG_ACTION_NAMED)) {
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
            if(xmlElem.getChild(InitialNamedAction.TAG_INITIAL_NAMED_ACTION_REF) != null)
                namedAction = new InitialNamedAction(xmlElem.getChild(InitialNamedAction.TAG_INITIAL_NAMED_ACTION_REF), listInitialNamedAction);
            if(xmlElem.getChild(InitialActionAcquisition.TAG_INITIAL_ACTION_ACQUISITION_REF) != null)
                namedAction = new InitialActionAcquisition(xmlElem.getChild(InitialActionAcquisition.TAG_INITIAL_ACTION_ACQUISITION_REF), listInitialNamedAction) ;
            if(xmlElem.getChild(InitialActionChoice.TAG_INITIAL_ACTION_CHOICE_REF) != null)
                namedAction = new InitialActionChoice(xmlElem.getChild(InitialActionChoice.TAG_INITIAL_ACTION_CHOICE_REF), listInitialNamedAction) ;
            if(xmlElem.getChild(InitialActionManipulation.TAG_INITIAL_ACTION_MANIPULATION_REF) != null)
                namedAction = new InitialActionManipulation(xmlElem.getChild(InitialActionManipulation.TAG_INITIAL_ACTION_MANIPULATION_REF), listInitialNamedAction) ;
            if(xmlElem.getChild(InitialActionTreatment.TAG_INITIAL_ACTION_TREATMENT) != null)
                namedAction = new InitialActionTreatment(xmlElem.getChild(InitialActionTreatment.TAG_INITIAL_ACTION_TREATMENT), listInitialNamedAction) ;

		} else {
			throw(new JDOMException("Named Action expects <"+TAG_ACTION_NAMED+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }

    public InitialNamedAction getNamedAction() {
        return namedAction;
    }

    public void setNamedAction(InitialNamedAction namedAction) {
        this.namedAction = namedAction;
    }

    @Override
    public Object clone() {
        CopexActionNamed a = (CopexActionNamed) super.clone() ;

        a.setNamedAction((InitialNamedAction)this.namedAction.clone());
        return a;
    }

    /* update the name of the action */
     private void setName(){
        if (this.namedAction != null){
            for (int i=0; i<listDescription.size(); i++){
                Locale l = listDescription.get(i).getLocale();
                setName(new LocalText(namedAction.getLibelle(l), l));
            }
        }
    }

    /*description of the action */
    @Override
     public String toDescription(CopexPanel edP){
        return toDescription(edP.getLocale());
     }

    public String toDescription(Locale locale){
         return this.getName(locale) + (this.getDescription(locale) == null || this.getDescription(locale).length() == 0 ? "" : " : "+this.getDescription(locale));
    }

    @Override
    public Element toXML(){
        Element element = super.toXML(new Element(TAG_ACTION_NAMED));
        if(namedAction != null)
            element.addContent(namedAction.toXMLRef());
        for(int l=0; l<listDescription.size(); l++){
            Locale loc = listDescription.get(l).getLocale();
            String d = toDescription(loc);
            Element el = new Element(TAG_ACTION_NAMED_TOSTRING).setText(d);
            el.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, loc.getLanguage()));
            element.addContent(el);
        }
        return element;
    }

    @Override
    public Element toXML(Element element){
        Element e = super.toXML(element);
        if(namedAction != null)
            e.addContent(namedAction.toXMLRef());
        return e;
    }
}
