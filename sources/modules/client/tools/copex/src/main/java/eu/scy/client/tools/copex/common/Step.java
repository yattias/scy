/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.edp.CopexPanel;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;


/**
 * represente une etape
 * @author MBO
 */
public class Step extends CopexTask{
    public final static String TAG_STEP = "step";

    public Step(long dbKey ,List<LocalText> listName, List<LocalText> listDescription, List<LocalText> listComments, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, TaskRepeat taskRepeat) {
        super(dbKey, listName, listDescription, listComments, taskImage, draw, isVisible, taskRight, false, taskRepeat);
    }


    public Step(long dbKey ,Locale locale, String name, String description, String comment, String taskImage, Element draw, boolean isVisible, TaskRight taskRight, TaskRepeat taskRepeat) {
        super(dbKey, CopexUtilities.getLocalText(name, locale), CopexUtilities.getLocalText(description, locale), CopexUtilities.getLocalText(comment, locale), taskImage, draw, isVisible, taskRight, false, taskRepeat);
    }

    /* constructeur appele suite a la saisie par l'utilisateur */
    public Step(List<LocalText> listDescription, List<LocalText> listComments){
        super(-1, new LinkedList(), listDescription, listComments, null, null, true, new TaskRight(MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT), false, null );
    }
     public Step(long dbKey, List<LocalText> listName, List<LocalText> listDescription, List<LocalText> listComments, String taskImage,Element draw, boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, TaskRepeat taskRepeat) {
        super(dbKey, listName, listDescription, listComments, taskImage,  draw, isVisible, taskRight, false, dbkeyBrother, dbKeyChild, taskRepeat);
    }

      public Step(long dbKey, Locale locale, String name, String description, String comment, String taskImage,Element draw, boolean isVisible, TaskRight taskRight, long dbkeyBrother, long dbKeyChild, TaskRepeat taskRepeat) {
        super(dbKey, CopexUtilities.getLocalText(name, locale), CopexUtilities.getLocalText(description, locale), CopexUtilities.getLocalText(comment, locale), taskImage,  draw, isVisible, taskRight, false, dbkeyBrother, dbKeyChild, taskRepeat);
    }
    public Step(Element xmlElem, long idTask, long idRepeat, long idParam, long idValue, long idActionParam, long idQuantity, List<PhysicalQuantity> listPhysicalQuantity, List<TypeMaterial> listTypeMaterial, List<InitialParamData> listInitialParamData, List<InitialParamMaterial> listInitialParamMaterial, List<InitialParamQuantity> listInitialParamQuantity, List<InitialAcquisitionOutput> listInitialAcquisitionOutput, List<InitialManipulationOutput> listInitialManipulationOutput, List<InitialTreatmentOutput> listInitialTreatmentOutput, List<Material> listMaterial, List<ActionParamQuantity> listActionParamQuantity) throws JDOMException {
        super(xmlElem);
        this.dbKeyBrother = -1;
        this.dbKeyChild = -1;
        if (xmlElem.getName().equals(TAG_STEP)) {
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
		} else {
			throw(new JDOMException("Step expects <"+TAG_STEP+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }

    @Override
    public Element toXML(){
        Element element = super.toXML(new Element(TAG_STEP));
        return element;
    }
    
}
