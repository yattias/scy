/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.logger.TaskTreePosition;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * experimental procedure of a learner
 * @author marjolaine
 */
public class LearnerProcedure extends ExperimentalProcedure{
    /** tag names */
    public final static String TAG_LEARNER_PROC = "learner_proc";

    /** initial procedure associate */
    private InitialProcedure initialProc;
    /** mission*/
    protected CopexMission mission;
    /** labDoc */
    protected long dbKeyLabDoc;

    public LearnerProcedure(List<LocalText> listName, CopexMission mission,  Date dateLastModification, InitialProcedure initialProc, ArrayList<MaterialUsed> listMaterialUsed) {
        super(listName, dateLastModification);
        this.initialProc = initialProc ;
        this.mission = mission;
        this.dbKeyLabDoc = -1;
        this.materials.setListMaterialUsed(listMaterialUsed);
        lockMaterialUsed();
    }

    public LearnerProcedure(long dbKey, List<LocalText> listName, Date dateLastModification, boolean isActiv, char right, InitialProcedure initialProc, ArrayList<MaterialUsed> listMaterialUsed) {
        super(dbKey, listName, dateLastModification, isActiv, right);
        this.initialProc = initialProc ;
        this.materials.setListMaterialUsed(listMaterialUsed);
        this.dbKeyLabDoc = -1;
        lockMaterialUsed();
    }

    public LearnerProcedure(ExperimentalProcedure proc, InitialProcedure initialProc) {
        super(proc);
        this.initialProc = initialProc;
        this.materials.setListMaterialUsed(new LinkedList());
        this.dbKeyLabDoc = -1;
        lockMaterialUsed();
    }

    public LearnerProcedure(Element xmlElem, CopexMission m, long idProc, long idRepeat, long idParam, long idValue, long idActionParam, long idQuantity,
            long idMaterial, long idTask,long idHypothesis, long idPrinciple,long idEvaluation,
            List<InitialProcedure> listInitProc, List<Material> listMaterial,  List<TypeMaterial> listTypeMaterial, List<PhysicalQuantity> listPhysicalQuantity) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_LEARNER_PROC)) {
            dbKey = idProc;
            listName = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_EXP_PROC_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listName.add(new LocalText(e.getText(), l));
            }
            //right = xmlElem.getChild(TAG_EXP_PROC_RIGHT).getText().charAt(0);
            right = 'X';
            question = new Question(xmlElem.getChild(Question.TAG_QUESTION), idTask++);
            if(xmlElem.getChild(Hypothesis.TAG_PROC_HYPOTHESIS) != null)
                hypothesis = new Hypothesis(xmlElem.getChild(Hypothesis.TAG_PROC_HYPOTHESIS), idHypothesis);
            if(xmlElem.getChild(GeneralPrinciple.TAG_PROC_GENERAL_PRINCIPLE) != null)
                generalPrinciple = new GeneralPrinciple(xmlElem.getChild(GeneralPrinciple.TAG_PROC_GENERAL_PRINCIPLE), idPrinciple);
            if(xmlElem.getChild(Evaluation.TAG_PROC_EVALUTATION) != null)
                evaluation = new Evaluation(xmlElem.getChild(Evaluation.TAG_PROC_EVALUTATION), idEvaluation);
            this.materials = null;
            mission = m;
            dbKeyLabDoc = -1;
            if(xmlElem.getChild(TAG_EXP_MATERIAL_PROD) != null){
                for (Iterator<Element> variablElem = xmlElem.getChild(TAG_EXP_MATERIAL_PROD).getChildren(Material.TAG_MATERIAL).iterator(); variablElem.hasNext();) {
                    listMaterial.add(new Material(variablElem.next(), idMaterial++, listTypeMaterial, listPhysicalQuantity, idQuantity++));
                }
            }
            initialProc = new InitialProcedure(xmlElem.getChild(InitialProcedure.TAG_INITIAL_PROC_REF),listInitProc);
            for(Iterator<Material> mat = initialProc.getListMaterial().iterator(); mat.hasNext();){
                listMaterial.add(mat.next());
            }
            List<MaterialUsed> listMaterialUsed = new LinkedList<MaterialUsed>();
            if (xmlElem.getChild(MaterialUsed.TAG_MATERIAL_USED) != null){
                for (Iterator<Element> variablElem = xmlElem.getChildren(MaterialUsed.TAG_MATERIAL_USED).iterator(); variablElem.hasNext();) {
                    listMaterialUsed.add(new MaterialUsed(variablElem.next(), listMaterial, idMaterial++, listTypeMaterial, listPhysicalQuantity, idQuantity++));
                }
            }
            materials = new MaterialProc(listMaterialUsed);
            manipulation = new Manipulation(xmlElem.getChild(Manipulation.TAG_MANIPULATION), question,idTask++, idRepeat++, idParam++, idValue++, idActionParam++, idQuantity++, idMaterial++,
                    initialProc,  listPhysicalQuantity, listTypeMaterial, listMaterial);
            manipulation.add(question);
            lockMaterialUsed();
        } else {
            throw(new JDOMException("Learner Proc expects <"+TAG_LEARNER_PROC+"> as root element, but found <"+xmlElem.getName()+">."));
	}
        
    }

    public InitialProcedure getInitialProc() {
        return initialProc;
    }

    public void setInitialProc(InitialProcedure initialProc) {
        this.initialProc = initialProc;
    }

    public CopexMission getMission() {
        return mission;
    }
    public void setMission(CopexMission mission) {
        this.mission = mission;
    }

    public long getDbKeyLabDoc() {
        return dbKeyLabDoc;
    }

    public void setDbKeyLabDoc(long dbKeyLabDoc) {
        this.dbKeyLabDoc = dbKeyLabDoc;
    }

    /** returns true if the proc is active for the mission */
    public boolean isActiv(CopexMission m){
        return (m.getDbKey() == this.mission.getDbKey() && activ);
    }

    public List<MaterialUsed> getListMaterialUsed() {
        return materials.getListMaterialUsed();
    }

    public void setListMaterialUsed(List<MaterialUsed> listMaterialUsed) {
        this.materials.setListMaterialUsed(listMaterialUsed);
    }
    
    @Override
    public Object clone() {
        LearnerProcedure p = (LearnerProcedure) super.clone() ;
        CopexMission missionC = null;
            if (mission != null)
                missionC  =(CopexMission)this.mission.clone();
        if (this.initialProc == null)
            p.setInitialProc(null);
        else
            p.setInitialProc((InitialProcedure)initialProc.clone());
        p.setMission(missionC);
        p.setDbKeyLabDoc(dbKeyLabDoc);
        return p;
    }

    public void addListMaterialUsed(ArrayList<MaterialUsed> list){
        if(getListMaterialUsed() == null)
            materials.setListMaterialUsed(new ArrayList());
        int nb = list.size();
        for (int i=0; i<nb; i++){
            materials.getListMaterialUsed().add(list.get(i));
        }
    }

    private int getIdMaterialUsed(MaterialUsed m){
        int nb = getListMaterialUsed().size();
        for (int i=0; i<nb; i++){
            if (getListMaterialUsed().get(i).getMaterial().getDbKey() == m.getMaterial().getDbKey())
                return i;
        }
        return -1;
    }
    public void deleteMaterialUsed(ArrayList<MaterialUsed> list){
        int nb = list.size();
        for (int i=0; i<nb; i++){
            int id = getIdMaterialUsed(list.get(i));
            if(id != -1){
                getListMaterialUsed().remove(id);
            }
        }
    }
    public void updateMaterialUsed(ArrayList<MaterialUsed> list){
        int nb = list.size();
        for(int i=0; i<nb; i++){
            int id = getIdMaterialUsed(list.get(i));
            if(id != -1){
                getListMaterialUsed().set(id, list.get(i));
            }
        }
    }

     // toXML
    @Override
    public Element toXML(){
        Element el = new Element(TAG_LEARNER_PROC);
        Element element = super.toXML(el);
        if (getListMaterialUsed() != null){
            for (Iterator<MaterialUsed> m = getListMaterialUsed().iterator(); m.hasNext();) {
                element.addContent(m.next().toXML());
            }
        }
        element.addContent(initialProc.toXMLRef());
        
        return element;
    }

    public boolean isTaskProc(){
        return initialProc.isTaskMode();
    }

    public boolean isValidQuestion(Locale locale){
        return question != null && question.getDescription(locale).length() > 0;
    }

    // update the material used in the proc
    public void lockMaterialUsed(){
        if(getListMaterialUsed() == null)
            return;
        for(Iterator<MaterialUsed> m = getListMaterialUsed().iterator();m.hasNext();){
            m.next().setMaterialUsed(false);
        }
        for(Iterator<CopexTask> t = getListTask().iterator();t.hasNext();){
            List<Material> listM = t.next().getMaterialUsed();
            for(Iterator<Material> m = listM.iterator();m.hasNext();){
                Material material = m.next();
                setMaterialUsed(material);
            }
        }
    }

    private void setMaterialUsed(Material material){
        for(Iterator<MaterialUsed> m = getListMaterialUsed().iterator();m.hasNext();){
            MaterialUsed mUsed = m.next();
            if(mUsed.getMaterial().getDbKey() == material.getDbKey()){
                mUsed.setMaterialUsed(true);
            }
        }
    }


    public TaskTreePosition getTaskTreePosition(CopexTask task){
        if(task==null)
            return new TaskTreePosition(-1, -1);
        CopexTask parentTask = getParentTask(task);
        if(parentTask == null)
            return new TaskTreePosition(-1, -1);
        List<CopexTask> listChild = getListChild(parentTask);
        int idChild = getIdChild(listChild, task);
        return new TaskTreePosition(parentTask.getDbKey(), idChild);
    }

    private CopexTask getParentTask(CopexTask task){
        if (task == null)
            return null;
        if (task.isQuestionRoot())
            return null;
        long id = task.getDbKey();
        int nbT = getListTask().size();
        for (int i=0; i<nbT; i++){
            if (getListTask().get(i).getDbKeyChild() == id){
                return getListTask().get(i);
            }
        }
        // on n'a pas trouve on cherche dans les grands freres
        CopexTask t = getOldBrotherTask(task);
        if(t==null)
            return null;
        else
            return getParentTask(t);
    }

    /** returns the old brother task*/
    private CopexTask getOldBrotherTask(CopexTask task){
        long id = task.getDbKey();
        int nbT = getListTask().size();
        for (int i=0; i<nbT; i++){
            if (getListTask().get(i).getDbKeyBrother() == id){
                return getListTask().get(i);
            }
        }
        return null;
    }

    /** returns the list of the children of a task, only 1 level*/
    private List<CopexTask> getListChild(CopexTask task){
        if(task == null)
            return new LinkedList();
        long dbKeyChild = task.getDbKeyChild();
        long dbKeyBrother = -2;
        List<CopexTask> listChild = new LinkedList();
        for(Iterator<CopexTask> t = getListTask().iterator();t.hasNext();){
            CopexTask aTask = t.next();
            // first child
            if(aTask.getDbKey() == dbKeyChild){
                listChild.add(aTask);
                dbKeyBrother = aTask.getDbKeyBrother();
            }
            // brohers
            if(aTask.getDbKey() == dbKeyBrother){
                listChild.add(aTask);
                dbKeyBrother = aTask.getDbKeyBrother();
            }
        }
        return listChild;
    }

    private int getIdChild(List<CopexTask> list, CopexTask task){
        int nb = list.size();
        for(int i=0; i<nb; i++){
            if(list.get(i).getDbKey() == task.getDbKey())
                return i;
        }
        return -1;
    }

    public MaterialUsed getMaterialUsedWithId(MaterialUsed m){
        int id = getIdMaterialUsed(m);
        if(id != -1)
            return getListMaterialUsed().get(id);
        return null;
    }

    /* return true if material strategy  != S0 */
    public boolean hasMaterial(){
        return initialProc.getMaterialStrategy().hasMaterial();
    }
}
