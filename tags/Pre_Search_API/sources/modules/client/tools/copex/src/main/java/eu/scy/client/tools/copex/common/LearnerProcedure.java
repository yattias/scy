/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

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

    @Override
    public boolean isTaskProc(){
        return initialProc.isTaskMode();
    }

    /* return true if material strategy  != S0 */
    @Override
    public boolean hasMaterial(){
        return initialProc.getMaterialStrategy().hasMaterial();
    }

    @Override
    public char getHypothesisMode(){
        return initialProc.getHypothesisMode();
    }
    @Override
    public char getPrincipleMode(){
        return initialProc.getPrincipleMode();
    }
    @Override
    public char getEvaluationMode(){
        return initialProc.getEvaluationMode();
    }

    @Override
    public Element toELO(){
        Element e = new Element(ExperimentalProcedure.TAG_EXPERIMENTAL_PROCEDURE);
        e.addContent(getMission().toXML());
        e.addContent(toXML());
        return e;
    }
    
}
