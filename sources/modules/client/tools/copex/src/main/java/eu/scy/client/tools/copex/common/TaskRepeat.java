/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;


/**
 * repeat of an action or a step (group of task)
 * @author Marjolaine
 */
public class TaskRepeat implements Cloneable {
    /*tag names */
    public final static String TAG_REPEAT_TASK = "repeat_task";
    private final static String TAG_REPEAT_TASK_NB = "nb_repeat_task";

    /* db identifier */
    private long dbKey;
    /*nb repeat */
    private int nbRepeat;
    /* list of paramters to modify: type  InitialActionParam or InitialOutput- if null and nbrepeat > 1 => none */
    private ArrayList<TaskRepeatParam> listParam;
   

    public TaskRepeat(long dbKey, int nbRepeat) {
        this.dbKey = dbKey;
        this.nbRepeat = nbRepeat;
        this.listParam = new ArrayList();
    }

    public TaskRepeat(long dbKey, int nbRepeat, ArrayList<TaskRepeatParam> listParam) {
        this.dbKey = dbKey;
        this.nbRepeat = nbRepeat;
        this.listParam = listParam;
    }

    public TaskRepeat(Element xmlElem, long idRepeat, long idParam, long idValue, long idActionParam, long idQuantity, List<PhysicalQuantity> listPhysicalQuantity, List<TypeMaterial> listTypeMaterial, List<InitialParamData> listInitialParamData, List<InitialParamMaterial> listInitialParamMaterial, List<InitialParamQuantity> listInitialParamQuantity, List<InitialAcquisitionOutput> listInitialAcquisitionOutput, List<InitialManipulationOutput> listInitialManipulationOutput, List<InitialTreatmentOutput> listInitialTreatmentOutput, List<Material> listMaterial, List<ActionParamQuantity> listActionParamQuantity) throws JDOMException {
        if (xmlElem.getName().equals(TAG_REPEAT_TASK)) {
            this.dbKey = idRepeat;
            nbRepeat = Integer.parseInt(xmlElem.getChild(TAG_REPEAT_TASK_NB).getText());
            listParam = new ArrayList();
            for (Iterator<Element> variableElem = xmlElem.getChildren().iterator(); variableElem.hasNext();){
                Element e =variableElem.next();
                if(e.getName().equals(TaskRepeatParamData.TAG_TASK_REPEAT_PARAM_DATA)){
                    TaskRepeatParamData p = new TaskRepeatParamData(e, idParam++, listInitialParamData, idValue, idActionParam, idQuantity,listPhysicalQuantity );
                    listParam.add(p);
                }else if(e.getName().equals(TaskRepeatParamMaterial.TAG_TASK_REPEAT_PARAM_MATERIAL)){
                    TaskRepeatParamMaterial p = new TaskRepeatParamMaterial(e, idParam++, listInitialParamMaterial, idValue++, idActionParam++, listMaterial, listPhysicalQuantity, listActionParamQuantity);
                    listParam.add(p);
                }else if(e.getName().equals(TaskRepeatParamQuantity.TAG_TASK_REPEAT_PARAM_QUANTITY)){
                    TaskRepeatParamQuantity p = new TaskRepeatParamQuantity(e, idParam++, listInitialParamQuantity, idValue++, idActionParam++, idQuantity++, listPhysicalQuantity);
                    listParam.add(p);
                }else if (e.getName().equals(TaskRepeatParamOutputAcquisition.TAG_TASK_REPEAT_PARAM_OUTPUT_ACQUISITION)){
                    TaskRepeatParamOutputAcquisition p = new TaskRepeatParamOutputAcquisition(e, idParam++, listInitialAcquisitionOutput, idValue++, idQuantity++, listPhysicalQuantity);
                    listParam.add(p);
                }else if(e.getName().equals(TaskRepeatParamOutputManipulation.TAG_TASK_REPEAT_PARAM_OUTPUT_MANIPULATION)){
                    TaskRepeatParamOutputManipulation p  = new TaskRepeatParamOutputManipulation(e, idParam++, listInitialManipulationOutput, idValue++, idQuantity++, idParam++, listPhysicalQuantity, listTypeMaterial);
                    listParam.add(p);
                }else if(e.getName().equals(TaskRepeatParamOutputTreatment.TAG_TASK_REPEAT_PARAM_OUTPUT_TREATMENT)){
                    TaskRepeatParamOutputTreatment p = new TaskRepeatParamOutputTreatment(e, idParam, listInitialTreatmentOutput, idValue++, idQuantity++, listPhysicalQuantity);
                    listParam.add(p);
                }
            }
        } else {
            throw(new JDOMException("Task repeat expects <"+TAG_REPEAT_TASK+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }
   

   
    public long getDbKey() {
        return dbKey;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

   
    public int getNbRepeat() {
        return nbRepeat;
    }

    public void setNbRepeat(int nbRepeat) {
        this.nbRepeat = nbRepeat;
    }

    public ArrayList<TaskRepeatParam> getListParam() {
        return listParam;
    }

    public void setListParam(ArrayList<TaskRepeatParam> listParam) {
        this.listParam = listParam;
    }

    public void addParam(TaskRepeatParam param){
        this.listParam.add(param);
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
       try {
            TaskRepeat tr = (TaskRepeat) super.clone() ;

            tr.setDbKey(this.dbKey);
            tr.setNbRepeat(this.nbRepeat);
            if(listParam == null){
                tr.setListParam(null);
            }else{
                ArrayList<TaskRepeatParam> list = new ArrayList();
                for (int i=0; i<this.listParam.size(); i++){
                    list.add((TaskRepeatParam)this.listParam.get(i).clone());
                }
                tr.setListParam(list);
            }
            
            return tr;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    /* returns the list of repetitions for a specified param, otherwise null*/
    public ArrayList<ActionParam> getListActionParam(InitialActionParam initParam){
        ArrayList<ActionParam> l = null;
        if (listParam == null)
            return l;
        l = new ArrayList();
        int nb = this.listParam.size();
        for (int i=0; i<nb; i++){
            if(listParam.get(i) instanceof TaskRepeatParamData){
                TaskRepeatParamData t = (TaskRepeatParamData)listParam.get(i);
                if (t.getInitialParamData().getDbKey() == initParam.getDbKey()){
                    ArrayList<TaskRepeatValueParamData> listValue = t.getListValue();
                    int n = listValue.size();
                    for (int j=0; j<n; j++){
                        l.add(listValue.get(j).getActionParamData());
                    }
                }
            }else if (listParam.get(i) instanceof TaskRepeatParamMaterial){
                TaskRepeatParamMaterial t = (TaskRepeatParamMaterial)listParam.get(i);
                if (t.getInitialParamMaterial().getDbKey() == initParam.getDbKey()){
                    ArrayList<TaskRepeatValueParamMaterial> listValue = t.getListValue();
                    int n = listValue.size();
                    for (int j=0; j<n; j++){
                        l.add(listValue.get(j).getActionParamMaterial());
                    }
                }
            }else if (listParam.get(i) instanceof TaskRepeatParamQuantity){
                TaskRepeatParamQuantity t = (TaskRepeatParamQuantity)listParam.get(i);
                if (t.getInitialParamQuantity().getDbKey() == initParam.getDbKey()){
                    ArrayList<TaskRepeatValueParamQuantity> listValue = t.getListValue();
                    int n = listValue.size();
                    for (int j=0; j<n; j++){
                        l.add(listValue.get(j).getActionParamQuantity());
                    }
                }
            }
        }
        return l;
    }


     // toXML
    public Element toXML(){
        Element element = new Element(TAG_REPEAT_TASK);
        element.addContent(new Element(TAG_REPEAT_TASK_NB).setText(Integer.toString(nbRepeat)));
        for(Iterator<TaskRepeatParam> t = listParam.iterator();t.hasNext();){
            element.addContent(t.next().toXML());
        }
	return element;
    }

    
}
