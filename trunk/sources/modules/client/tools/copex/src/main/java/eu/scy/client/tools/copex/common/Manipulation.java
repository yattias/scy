/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author Marjolaine
 */
public class Manipulation implements Cloneable{
    public final static String TAG_MANIPULATION = "manipulation" ;

    private long idDbKey;
    private List<CopexTask> listTask;

    public Manipulation(List<CopexTask> listTask) {
        this.listTask = listTask;
    }

    public Manipulation(Element xmlElem, Question question, long idDbKey,long idRepeat, long idParam, long idValue, long idActionParam, long idQuantity, long idMaterial,
            InitialProcedure initProc,
            List<PhysicalQuantity> listPhysicalQuantity, List<TypeMaterial> listTypeMaterial, List<Material> listMaterial) throws JDOMException {
        this.idDbKey = idDbKey;
        List<InitialNamedAction> listInitialNamedAction = initProc.getListNamedAction();
        List<InitialParamData> listInitialParamData = initProc.getListInitialParamData();
        List<InitialParamMaterial> listInitialParamMaterial = initProc.getListInitialParamMaterial();
        List<InitialParamQuantity> listInitialParamQuantity = initProc.getListInitialParamQuantity();
        List<InitialAcquisitionOutput> listInitialAcquisitionOutput = initProc.getListInitialAcquisitionOutput();
        List<InitialManipulationOutput> listInitialManipulationOutput = initProc.getListInitialManipulationOutput();
        List<InitialTreatmentOutput> listInitialTreatmentOutput = initProc.getListInitialTreatmentOutput();
        listTask = new LinkedList<CopexTask>();
        if (xmlElem.getName().equals(TAG_MANIPULATION)) {
            List<Element> listC = xmlElem.getChildren();
            List<ActionParamQuantity> listActionParamQuantity = new LinkedList();
            int nbC = listC.size();
            long dbKeyB = -1;
            for (int i=nbC-1; i>-1; i--){
                if(isXMLTaskNameValid(listC.get(i).getName())){
                    CopexTask task = addTask(listC.get(i), idRepeat, idParam, idValue, idActionParam, idQuantity,idMaterial,listInitialNamedAction,  listPhysicalQuantity, listTypeMaterial, listInitialParamData, listInitialParamMaterial, listInitialParamQuantity, listInitialAcquisitionOutput, listInitialManipulationOutput, listInitialTreatmentOutput, listMaterial, listActionParamQuantity);
                    task.setDbKeyBrother(dbKeyB);
                    dbKeyB = task.getDbKey();
                    if(i==0)
                        question.setDbKeyChild(task.getDbKey());
                    if(task instanceof CopexActionParam){
                        Object[] tabParam = ((CopexActionParam)task).getTabParam();
                        for (int j=0; j<tabParam.length; j++){
                            if(tabParam[j] instanceof ActionParamQuantity){
                                listActionParamQuantity.add((ActionParamQuantity)tabParam[j]);
                            }else if(tabParam[j] instanceof ArrayList){
                                int nb = ((ArrayList)tabParam[j]).size();
                                for (int k=0; k<nb; k++){
                                    if(((ArrayList)tabParam[j]).get(k) instanceof ActionParamQuantity){
                                        listActionParamQuantity.add((ActionParamQuantity)((ArrayList)tabParam[j]).get(k));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            throw(new JDOMException("Manipulation expects <"+TAG_MANIPULATION+"> as root element, but found <"+xmlElem.getName()+">."));
	}

    }


    private CopexTask addTask (Element xmlElem,long idRepeat, long idParam, long idValue, long idActionParam, long idQuantity, long idMaterial,List<InitialNamedAction> listInitialNamedAction, List<PhysicalQuantity> listPhysicalQuantity, List<TypeMaterial> listTypeMaterial, List<InitialParamData> listInitialParamData, List<InitialParamMaterial> listInitialParamMaterial, List<InitialParamQuantity> listInitialParamQuantity, List<InitialAcquisitionOutput> listInitialAcquisitionOutput, List<InitialManipulationOutput> listInitialManipulationOutput, List<InitialTreatmentOutput> listInitialTreatmentOutput, List<Material> listMaterial, List<ActionParamQuantity> listActionParamQuantity) throws JDOMException{
        CopexTask task = getTask(xmlElem, idRepeat, idParam, idValue, idActionParam, idQuantity, idMaterial,listInitialNamedAction, listPhysicalQuantity, listTypeMaterial, listInitialParamData, listInitialParamMaterial, listInitialParamQuantity, listInitialAcquisitionOutput, listInitialManipulationOutput, listInitialTreatmentOutput, listMaterial, listActionParamQuantity);
        task.setDbKeyBrother(-1);
        task.setDbKeyChild(-1);
        if(task != null)
            listTask.add(task);

        List<Element> listC = xmlElem.getChildren();
        int nbC = listC.size();
        long dbKeyB = -1;
        for (int i=nbC-1; i>-1; i--){
            if(isXMLTaskNameValid(listC.get(i).getName())){
                CopexTask t = addTask(listC.get(i), idRepeat, idParam, idValue, idActionParam, idQuantity,idMaterial,listInitialNamedAction,  listPhysicalQuantity, listTypeMaterial, listInitialParamData, listInitialParamMaterial, listInitialParamQuantity, listInitialAcquisitionOutput, listInitialManipulationOutput, listInitialTreatmentOutput, listMaterial, listActionParamQuantity);
                t.setDbKeyBrother(dbKeyB);
                dbKeyB = t.getDbKey();
                task.setDbKeyChild(t.getDbKey());
            }
        }
        return task;
    }

    private boolean isXMLTaskNameValid(String name){
        return (name.equals(Step.TAG_STEP) || name.equals(CopexAction.TAG_ACTION) || name.equals(CopexActionNamed.TAG_ACTION_NAMED) || name.equals(CopexActionAcquisition.TAG_ACTION_ACQUISITION) || name.equals(CopexActionChoice.TAG_ACTION_CHOICE) || name.equals(CopexActionManipulation.TAG_ACTION_MANIPULATION) ||name.equals(CopexActionTreatment.TAG_ACTION_TREATMENT)) ?true:false;
    }

    private CopexTask getTask (Element xmlElem,long idRepeat, long idParam, long idValue, long idActionParam, long idQuantity,long idMaterial, List<InitialNamedAction> listInitialNamedAction,List<PhysicalQuantity> listPhysicalQuantity, List<TypeMaterial> listTypeMaterial, List<InitialParamData> listInitialParamData, List<InitialParamMaterial> listInitialParamMaterial, List<InitialParamQuantity> listInitialParamQuantity, List<InitialAcquisitionOutput> listInitialAcquisitionOutput, List<InitialManipulationOutput> listInitialManipulationOutput, List<InitialTreatmentOutput> listInitialTreatmentOutput, List<Material> listMaterial, List<ActionParamQuantity> listActionParamQuantity )  throws JDOMException{
        if(xmlElem.getName().equals(Step.TAG_STEP)){
            Step step = new Step(xmlElem, idDbKey++, idRepeat++, idParam++, idValue++, idActionParam++, idQuantity++, listPhysicalQuantity, listTypeMaterial, listInitialParamData, listInitialParamMaterial, listInitialParamQuantity, listInitialAcquisitionOutput, listInitialManipulationOutput, listInitialTreatmentOutput, listMaterial, listActionParamQuantity);
            return step;
        }else if(xmlElem.getName().equals(CopexAction.TAG_ACTION)){
            CopexAction action = new CopexAction(xmlElem, idDbKey++, idRepeat++, idParam++, idValue++, idActionParam++, idQuantity++, listPhysicalQuantity, listTypeMaterial, listInitialParamData, listInitialParamMaterial, listInitialParamQuantity, listInitialAcquisitionOutput, listInitialManipulationOutput, listInitialTreatmentOutput, listMaterial, listActionParamQuantity);
            return action;
        }else if (xmlElem.getName().equals(CopexActionNamed.TAG_ACTION_NAMED)){
            CopexActionNamed aNamed = new CopexActionNamed(xmlElem, idDbKey++, listInitialNamedAction, idRepeat++, idParam++, idValue++, idActionParam++, idQuantity++, listPhysicalQuantity, listTypeMaterial, listInitialParamData, listInitialParamMaterial, listInitialParamQuantity, listInitialAcquisitionOutput, listInitialManipulationOutput, listInitialTreatmentOutput, listMaterial, listActionParamQuantity);
            return aNamed;
        }else if (xmlElem.getName().equals(CopexActionChoice.TAG_ACTION_CHOICE)){
            CopexActionChoice choice = new CopexActionChoice(xmlElem, idDbKey++, listInitialNamedAction, idRepeat++, idParam++, idValue++, idActionParam++, idQuantity++, listPhysicalQuantity, listTypeMaterial, listInitialParamData, listInitialParamMaterial, listInitialParamQuantity, listInitialAcquisitionOutput, listInitialManipulationOutput, listInitialTreatmentOutput, listMaterial, listActionParamQuantity);
            return choice;
        }else if (xmlElem.getName().equals(CopexActionAcquisition.TAG_ACTION_ACQUISITION)){
            CopexActionAcquisition acq = new CopexActionAcquisition(xmlElem, idDbKey++, listInitialNamedAction, idRepeat++, idParam++, idValue++, idActionParam++, idQuantity++, listPhysicalQuantity, listTypeMaterial, listInitialParamData, listInitialParamMaterial, listInitialParamQuantity, listInitialAcquisitionOutput, listInitialManipulationOutput, listInitialTreatmentOutput, listMaterial, listActionParamQuantity);
            return acq;
        }else if (xmlElem.getName().equals(CopexActionManipulation.TAG_ACTION_MANIPULATION)){
            CopexActionManipulation manip = new CopexActionManipulation(xmlElem, idDbKey++, listInitialNamedAction, idMaterial++, idRepeat++, idParam++, idValue++, idActionParam++, idQuantity++, listPhysicalQuantity, listTypeMaterial, listInitialParamData, listInitialParamMaterial, listInitialParamQuantity, listInitialAcquisitionOutput, listInitialManipulationOutput, listInitialTreatmentOutput, listMaterial, listActionParamQuantity);
            return manip;
        }else if(xmlElem.getName().equals(CopexActionTreatment.TAG_ACTION_TREATMENT)){
            CopexActionTreatment treat = new CopexActionTreatment(xmlElem, idDbKey++, listInitialNamedAction, idRepeat++, idParam++, idValue++, idActionParam++, idQuantity++, listPhysicalQuantity, listTypeMaterial, listInitialParamData, listInitialParamMaterial, listInitialParamQuantity, listInitialAcquisitionOutput, listInitialManipulationOutput, listInitialTreatmentOutput, listMaterial, listActionParamQuantity);
            return treat;
        }
        return null;
    }


    
    public List<CopexTask> getListTask() {
        return listTask;
    }

    public void setListTask(ArrayList<CopexTask> listTask) {
        this.listTask = listTask;
    }


    @Override
    public Object clone()  {
        try {
            Manipulation m = (Manipulation) super.clone() ;
            ArrayList<CopexTask> l = null;
            if(this.listTask !=null){
                l = new ArrayList();
                int nb = listTask.size();
                for (int i=0; i<nb; i++){
                    l.add((CopexTask)listTask.get(i).clone());
                }
            }
            m.setListTask(l);
            return m;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    public void add(CopexTask task){
        this.listTask.add(task);
    }

     // toXML
    public Element toXML(){
        Element element = new Element(TAG_MANIPULATION);
        List<CopexTask> listT = getChilds(getQuestion());
        for (Iterator<CopexTask> t = listT.iterator(); t.hasNext();){
            Element e = toXML(t.next());
            element.addContent(e);
        }
        return element;
    }

    private Element toXML(CopexTask task){
        List<CopexTask> listT = getChilds(task);
        Element element = task.toXML();
        for (Iterator<CopexTask> t = listT.iterator(); t.hasNext();){
            Element e = toXML(t.next());
            element.addContent(e);
        }
        return element;
    }

    private Question getQuestion(){
        for (Iterator<CopexTask> t = listTask.iterator(); t.hasNext();){
            CopexTask task = t.next();
            if(task.isQuestionRoot())
                return (Question)task;
        }
        return null;
    }

    private List<CopexTask> getChilds(CopexTask task){
        ArrayList<CopexTask> listT = new ArrayList();
        if(task == null)
            return listT;
        long dbKeyC = task.getDbKeyChild() ;
        if(dbKeyC != -1){
            CopexTask firstC = getTask(dbKeyC);
            if(firstC != null)
                listT.add(firstC);
            List<CopexTask> listB = getBrothers(firstC);
            int nbB = listB.size() ;
            for (int i=0; i<nbB; i++){
                listT.add(listB.get(i));
            }
        }
        return listT;
    }

    private  List<CopexTask> getBrothers(CopexTask task){
        List<CopexTask> listB = new LinkedList();
        if(task == null)
            return listB;
        long dbKeyB = task.getDbKeyBrother() ;
        if (dbKeyB != -1){
            CopexTask t = getTask(dbKeyB);
            if(t!=null){
                listB.add(t);
                List<CopexTask> l = getBrothers(t);
                int nb = l.size() ;
                for (int i=0; i<nb; i++){
                    listB.add(l.get(i));
                }
            }
        }
        return listB ;
    }

    // returns the task with this dbkey
    private CopexTask getTask(long dbKey){
        for (Iterator<CopexTask> t = listTask.iterator(); t.hasNext();){
            CopexTask task = t.next();
            if(task.getDbKey() == dbKey)
                return task;
        }
        return null;
    }

}
