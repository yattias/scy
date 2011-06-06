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
 * initial procedure
 * @author Marjolaine
 */
public class InitialProcedure extends ExperimentalProcedure {
    /* tag names */
    public final static String TAG_INITIAL_PROC = "initial_proc";
    public final static String TAG_INITIAL_PROC_REF = "initial_proc_ref";
    private final static String TAG_INITIAL_PROC_CODE = "code";
    private final static String TAG_INITIAL_PROC_HYPOTHESIS_MODE = "hypothesis_mode";
    private final static String TAG_INITIAL_PROC_PRINCIPLE_MODE = "principle_mode";
    private final static String TAG_INITIAL_PROC_DRAW_PRINCIPLE = "draw_principle";
    private final static String TAG_INITIAL_PROC_EVALUATION_MODE = "evaluation_mode";
    private final static String TAG_INITIAL_PROC_TASK_REPEAT = "is_task_repeat";
    private final static String TAG_INITIAL_PROC_FREE_ACTION = "is_free_action";
    private final static String TAG_INITIAL_PROC_TASK = "is_task";
     private final static String TAG_INITIAL_PROC_TASK_DRAW = "is_task_draw";


    /*code */
    private String code;
    /* liste action nommees */
    private ArrayList<InitialNamedAction> listNamedAction;
    /* action libre autorisee */
    private boolean isFreeAction;
    private boolean taskMode;
    /* tache repeat */
    private boolean isTaskRepeat;
    /* tache draw */
    private boolean isTaskDraw;
     /* liste du materiel disponible associe */
    private ArrayList<Material> listMaterial ;

    /*hypothese mode cf CST MODE_MENU*/
    private char hypothesisMode;
    /* general principle */
    private char principleMode;
    private boolean drawPrinciple;
    /* evaluation */
    private char evaluationMode;

    private MaterialStrategy materialStrategy;

    // CONSTRUCTOR
    public InitialProcedure(long dbKey, List<LocalText> listName, Date dateLastModification, boolean isActiv, char right, String code, boolean isFreeAction, boolean isTaskMode, boolean isTaskRepeat, ArrayList<InitialNamedAction> listNamedAction,
            char hypothesisMode, char principleMode, boolean drawPrinciple, char evaluationMode, MaterialStrategy materialStrategy, boolean isTaskDraw) {
        super(dbKey, listName, dateLastModification, isActiv, right);
        this.isFreeAction = isFreeAction ;
        this.taskMode = isTaskMode;
        this.isTaskRepeat = isTaskRepeat ;
        this.code = code;
        this.listNamedAction = listNamedAction ;
        this.hypothesisMode = hypothesisMode;
        this.principleMode = principleMode;
        this.drawPrinciple = drawPrinciple;
        this.evaluationMode = evaluationMode;
        this.materialStrategy = materialStrategy;
        this.isTaskDraw = isTaskDraw;
    }


    public InitialProcedure(Element xmlElem, Locale locale,long idProc, long idRepeat, long idParam, long idValue, long idAction, long idActionParam, long idQuantity,
            long idMaterial, long idTask,long idHypothesis, long idPrinciple,long idEvaluation, long idOutput,
            List<Material> listM,  List<TypeMaterial> listTypeMaterial, List<PhysicalQuantity> listPhysicalQuantity, List<InitialNamedAction> listInitialNamedAction, List<MaterialStrategy> listMaterialStrategy) throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_INITIAL_PROC)) {
			dbKey = idProc;
            listName = new LinkedList<LocalText>();
            for (Iterator<Element> variableElem = xmlElem.getChildren(TAG_EXP_PROC_NAME).iterator(); variableElem.hasNext();) {
                Element e = variableElem.next();
                Locale l = new Locale(e.getAttribute(MyConstants.XMLNAME_LANGUAGE).getValue());
                listName.add(new LocalText(e.getText(), l));
            }
            //right = xmlElem.getChild(TAG_EXP_PROC_RIGHT).getText().charAt(0);
            right = MyConstants.EXECUTE_RIGHT;
            code = xmlElem.getChild(TAG_INITIAL_PROC_CODE).getText();
            question = new Question(xmlElem.getChild(Question.TAG_QUESTION), idTask++);
            if(xmlElem.getChild(Hypothesis.TAG_PROC_HYPOTHESIS) != null)
                hypothesis = new Hypothesis(xmlElem.getChild(Hypothesis.TAG_PROC_HYPOTHESIS), idHypothesis++);
            if(xmlElem.getChild(GeneralPrinciple.TAG_PROC_GENERAL_PRINCIPLE) != null)
                generalPrinciple = new GeneralPrinciple(xmlElem.getChild(GeneralPrinciple.TAG_PROC_GENERAL_PRINCIPLE), idPrinciple++);
            if(xmlElem.getChild(Evaluation.TAG_PROC_EVALUTATION) != null)
                evaluation = new Evaluation(xmlElem.getChild(Evaluation.TAG_PROC_EVALUTATION), idEvaluation++);
            hypothesisMode = xmlElem.getChild(TAG_INITIAL_PROC_HYPOTHESIS_MODE).getText().charAt(0);
            principleMode = xmlElem.getChild(TAG_INITIAL_PROC_PRINCIPLE_MODE).getText().charAt(0);
            drawPrinciple = xmlElem.getChild(TAG_INITIAL_PROC_DRAW_PRINCIPLE).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            evaluationMode = xmlElem.getChild(TAG_INITIAL_PROC_EVALUATION_MODE).getText().charAt(0);
            // at the moment evaluation is not take into account
            evaluationMode = MyConstants.MODE_MENU_NO;
            //manipulation = new Manipulation(xmlElem.getChild(Manipulation.TAG_MANIPULATION), idTask);
            materials = new MaterialProc(new LinkedList());
            listMaterial = new ArrayList<Material>();
            if (xmlElem.getChild(Material.TAG_MATERIAL) != null){
                for (Iterator<Element> variablElem = xmlElem.getChildren(Material.TAG_MATERIAL).iterator(); variablElem.hasNext();) {
                    listMaterial.add(new Material(variablElem.next(), idMaterial++, listTypeMaterial, listPhysicalQuantity, idQuantity++));
                }
            }
            if (xmlElem.getChild(Material.TAG_MATERIAL_REF) != null){
                for (Iterator<Element> variablElem = xmlElem.getChildren(Material.TAG_MATERIAL_REF).iterator(); variablElem.hasNext();) {
                    listMaterial.add(new Material(variablElem.next(),listM));
                }
            }
            List<Material> listMatC = new LinkedList();
            for(Iterator<Material> mat = listMaterial.iterator();mat.hasNext();){
                listMatC.add((Material)mat.next().clone());
            }
            if(xmlElem.getChild(TAG_EXP_MATERIAL_PROD) != null){
                for (Iterator<Element> variablElem = xmlElem.getChild(TAG_EXP_MATERIAL_PROD).getChildren(Material.TAG_MATERIAL).iterator(); variablElem.hasNext();) {
                    listMatC.add(new Material(variablElem.next(), idMaterial++, listTypeMaterial, listPhysicalQuantity, idQuantity++));
                }
            }
            listNamedAction = new ArrayList();
            if (xmlElem.getChild(InitialNamedAction.TAG_INITIAL_NAMED_ACTION) != null){
                for (Iterator<Element> variablElem = xmlElem.getChildren(InitialNamedAction.TAG_INITIAL_NAMED_ACTION).iterator(); variablElem.hasNext();) {
                    listNamedAction.add(new InitialNamedAction(variablElem.next(), idAction++,locale, idActionParam++,listPhysicalQuantity, listTypeMaterial));
                }
            }
            
            if (xmlElem.getChild(InitialNamedAction.TAG_INITIAL_NAMED_ACTION_REF) != null){
                for (Iterator<Element> variablElem = xmlElem.getChildren(InitialNamedAction.TAG_INITIAL_NAMED_ACTION_REF).iterator(); variablElem.hasNext();) {
                    listNamedAction.add(new InitialNamedAction(variablElem.next(), listInitialNamedAction));
                }
            }
            if (xmlElem.getChild(InitialActionAcquisition.TAG_INITIAL_ACTION_ACQUISITION_REF) != null){
                for (Iterator<Element> variablElem = xmlElem.getChildren(InitialActionAcquisition.TAG_INITIAL_ACTION_ACQUISITION_REF).iterator(); variablElem.hasNext();) {
                    listNamedAction.add(new InitialActionAcquisition(variablElem.next(), listInitialNamedAction));
                }
            }
            if (xmlElem.getChild(InitialActionChoice.TAG_INITIAL_ACTION_CHOICE_REF) != null){
                for (Iterator<Element> variablElem = xmlElem.getChildren(InitialActionChoice.TAG_INITIAL_ACTION_CHOICE_REF).iterator(); variablElem.hasNext();) {
                    listNamedAction.add(new InitialActionChoice(variablElem.next(), listInitialNamedAction));
                }
            }
            if (xmlElem.getChild(InitialActionManipulation.TAG_INITIAL_ACTION_MANIPULATION_REF) != null){
                for (Iterator<Element> variablElem = xmlElem.getChildren(InitialActionManipulation.TAG_INITIAL_ACTION_MANIPULATION_REF).iterator(); variablElem.hasNext();) {
                    listNamedAction.add(new InitialActionManipulation(variablElem.next(), listInitialNamedAction));
                }
            }
            if (xmlElem.getChild(InitialActionTreatment.TAG_INITIAL_ACTION_TREATMENT_REF) != null){
                for (Iterator<Element> variablElem = xmlElem.getChildren(InitialActionTreatment.TAG_INITIAL_ACTION_TREATMENT_REF).iterator(); variablElem.hasNext();) {
                    listNamedAction.add(new InitialActionTreatment(variablElem.next(), listInitialNamedAction));
                }
            }
            if(xmlElem.getChild(InitialActionChoice.TAG_INITIAL_ACTION_CHOICE) != null){
                for (Iterator<Element> variablElem = xmlElem.getChildren(InitialActionChoice.TAG_INITIAL_ACTION_CHOICE).iterator(); variablElem.hasNext();) {
                    listNamedAction.add(new InitialActionChoice(variablElem.next(), idAction++, locale, idActionParam++, listPhysicalQuantity, listTypeMaterial, idOutput++));
                }
            }
            if(xmlElem.getChild(InitialActionAcquisition.TAG_INITIAL_ACTION_ACQUISITION) != null){
                for (Iterator<Element> variablElem = xmlElem.getChildren(InitialActionAcquisition.TAG_INITIAL_ACTION_ACQUISITION).iterator(); variablElem.hasNext();) {
                    listNamedAction.add(new InitialActionAcquisition(variablElem.next(), idAction++, locale, idActionParam++, listPhysicalQuantity, listTypeMaterial, idOutput++));
                }
            }
            if(xmlElem.getChild(InitialActionManipulation.TAG_INITIAL_ACTION_MANIPULATION) != null){
                for (Iterator<Element> variablElem = xmlElem.getChildren(InitialActionManipulation.TAG_INITIAL_ACTION_MANIPULATION).iterator(); variablElem.hasNext();) {
                    listNamedAction.add(new InitialActionManipulation(variablElem.next(), idAction++, locale, idActionParam++, listPhysicalQuantity, listTypeMaterial, idOutput++));
                }
            }
            if(xmlElem.getChild(InitialActionTreatment.TAG_INITIAL_ACTION_TREATMENT) != null){
                for (Iterator<Element> variablElem = xmlElem.getChildren(InitialActionTreatment.TAG_INITIAL_ACTION_TREATMENT).iterator(); variablElem.hasNext();) {
                    listNamedAction.add(new InitialActionTreatment(variablElem.next(), idAction++, locale, idActionParam++, listPhysicalQuantity, listTypeMaterial, idOutput++));
                }
            }
            isFreeAction = xmlElem.getChild(TAG_INITIAL_PROC_FREE_ACTION).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            taskMode = isFreeAction && (listNamedAction == null || listNamedAction.size() == 0);
            if(xmlElem.getChild(TAG_INITIAL_PROC_TASK) != null){
                taskMode = xmlElem.getChild(TAG_INITIAL_PROC_TASK).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            }
            isTaskRepeat = xmlElem.getChild(TAG_INITIAL_PROC_TASK_REPEAT).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            manipulation = new Manipulation(xmlElem.getChild(Manipulation.TAG_MANIPULATION), question,idTask++, idRepeat++, idParam++, idValue++, idActionParam++, idQuantity++, idMaterial++,
                    this,  listPhysicalQuantity, listTypeMaterial, listMatC);
            manipulation.add(question);
            dataSheet = null;
            this.materialStrategy = new MaterialStrategy(xmlElem.getChild(MaterialStrategy.TAG_MATERIAL_STRATEGY_REF), listMaterialStrategy);
            ArrayList<MaterialUsed> listMaterialUsed = new ArrayList();
            if(materialStrategy.hasMaterial()){
                for(Iterator<Material> m = listMaterial.iterator(); m.hasNext();){
                    Material mat = m.next();
                    boolean used = materialStrategy.canChooseMaterial()?false:true;
                    listMaterialUsed.add(new MaterialUsed(mat, new LinkedList(), used));
                }
            }
            materials = new MaterialProc(listMaterialUsed);
            isTaskDraw = false;
            if(xmlElem.getChild(TAG_INITIAL_PROC_TASK_DRAW) != null){
                isTaskDraw = xmlElem.getChild(TAG_INITIAL_PROC_TASK_DRAW).getText().equals(MyConstants.XML_BOOLEAN_TRUE);
            }
	} else {
            throw(new JDOMException("Initial proc expects <"+TAG_INITIAL_PROC+"> as root element, but found <"+xmlElem.getName()+">."));
	}
    }

    public InitialProcedure(Element xmlElem, List<InitialProcedure> listInitProc)throws JDOMException {
        super(xmlElem);
        if (xmlElem.getName().equals(TAG_INITIAL_PROC_REF)) {
            code = xmlElem.getChild(TAG_INITIAL_PROC_CODE).getText();
            for(Iterator<InitialProcedure> p = listInitProc.iterator();p.hasNext();){
                InitialProcedure proc = p.next();
                if(proc.getCode().equals(code)){
                    this.dbKey = proc.getDbKey();
                    this.listName = proc.getListName();
                    this.isFreeAction = proc.isFreeAction() ;
                    this.taskMode = proc.isTaskMode();
                    this.isTaskRepeat = proc.isTaskRepeat ;
                    this.listNamedAction = proc.getListNamedAction() ;
                    this.hypothesisMode = proc.getHypothesisMode();
                    this.principleMode = proc.getPrincipleMode();
                    this.drawPrinciple = proc.isDrawPrinciple();
                    this.evaluationMode = proc.getEvaluationMode();
                    this.materialStrategy = proc.getMaterialStrategy();
                    this.listMaterial = proc.getListMaterial();
                    this.question = proc.getQuestion();
                    this.hypothesis = proc.getHypothesis();
                    this.generalPrinciple = proc.getGeneralPrinciple();
                    this.materials = proc.getMaterials();
                    this.manipulation = proc.getManipulation();
                    this.dataSheet = proc.getDataSheet();
                    this.evaluation = proc.getEvaluation();
                    this.isTaskDraw = proc.isTaskDraw();
                }
            }
        } else {
			throw(new JDOMException("Initial proc expects <"+TAG_INITIAL_PROC_REF+"> as root element, but found <"+xmlElem.getName()+">."));
		}
    }

    public boolean isFreeAction() {
        return isFreeAction;
    }

    public void setFreeAction(boolean isFreeAction) {
        this.isFreeAction = isFreeAction;
    }

    public boolean isTaskMode() {
        return taskMode;
    }

    public boolean isTaskDraw() {
        return isTaskDraw;
    }

    public void setTaskDraw(boolean isTaskDraw) {
        this.isTaskDraw = isTaskDraw;
    }

    public void setTaskMode(boolean taskMode) {
        this.taskMode = taskMode;
    }

    public boolean isTaskRepeat() {
        return isTaskRepeat;
    }

    public void setTaskRepeat(boolean isTaskRepeat) {
        this.isTaskRepeat = isTaskRepeat;
    }

    public ArrayList<InitialNamedAction> getListNamedAction() {
        return listNamedAction;
    }

    public void setListNamedAction(ArrayList<InitialNamedAction> listNamedAction) {
        this.listNamedAction = listNamedAction;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Material> getListMaterial() {
        return listMaterial;
    }

    public void setListMaterial(ArrayList<Material> listMaterial) {
        this.listMaterial = listMaterial;
    }

    
    public boolean isDrawPrinciple() {
        return drawPrinciple;
    }

    public void setDrawPrinciple(boolean drawPrinciple) {
        this.drawPrinciple = drawPrinciple;
    }

    @Override
    public char getEvaluationMode() {
        return evaluationMode;
    }

    public void setEvaluationMode(char evaluationMode) {
        this.evaluationMode = evaluationMode;
    }

    @Override
    public char getHypothesisMode() {
        return hypothesisMode;
    }

    public void setHypothesisMode(char hypothesisMode) {
        this.hypothesisMode = hypothesisMode;
    }

    public boolean isIsFreeAction() {
        return isFreeAction;
    }

    public void setIsFreeAction(boolean isFreeAction) {
        this.isFreeAction = isFreeAction;
    }

    public boolean isIsTaskRepeat() {
        return isTaskRepeat;
    }

    public void setIsTaskRepeat(boolean isTaskRepeat) {
        this.isTaskRepeat = isTaskRepeat;
    }

    @Override
    public char getPrincipleMode() {
        return principleMode;
    }

    public void setPrincipleMode(char principleMode) {
        this.principleMode = principleMode;
    }

    public MaterialStrategy getMaterialStrategy() {
        return materialStrategy;
    }

    public void setMaterialStrategy(MaterialStrategy materialStrategy) {
        this.materialStrategy = materialStrategy;
    }

    @Override
    public Object clone()  {
         InitialProcedure p = (InitialProcedure) super.clone() ;
            p.setFreeAction(this.isFreeAction);
            p.setTaskMode(this.taskMode);
            p.setTaskRepeat(this.isTaskRepeat);
            p.setCode(new String(this.code));
            ArrayList<InitialNamedAction> l = null;
            if (this.listNamedAction != null){
                l = new ArrayList();
                int nb = this.listNamedAction.size() ;
                for (int i=0; i<nb; i++){
                    l.add((InitialNamedAction)this.listNamedAction.get(i).clone());
                }
            }
            p.setListNamedAction(l);
            if (listMaterial != null){
                ArrayList<Material> listMC = new ArrayList();
                int nb = listMaterial.size();
                for (int i=0; i<nb; i++){
                    listMC.add((Material)listMaterial.get(i).clone());
                }
                p.setListMaterial(listMC);
            }
            p.setHypothesisMode(this.hypothesisMode);
            p.setPrincipleMode(this.principleMode);
            p.setDrawPrinciple(this.drawPrinciple);
            p.setEvaluationMode(this.evaluationMode);
            MaterialStrategy s = null;
            if(materialStrategy != null)
                s = (MaterialStrategy)materialStrategy.clone();
            p.setMaterialStrategy(s);
            p.setTaskDraw(this.isTaskDraw);
            return p;
        
    }

    public List<InitialParamData> getListInitialParamData(){
        List<InitialParamData> listInitialParamData = new LinkedList();
        if(listNamedAction == null)
            return listInitialParamData;
        for (Iterator<InitialNamedAction> namedAction = listNamedAction.iterator(); namedAction.hasNext();){
            InitialNamedAction a = namedAction.next();
            if(a.getVariable() != null){
                InitialActionParam[] tabParam = a.getVariable().getTabParam();
                for (int i=0; i<tabParam.length; i++){
                    if(tabParam[i] instanceof InitialParamData){
                        listInitialParamData.add((InitialParamData)tabParam[i]);
                    }
                }
            }
        }
        return listInitialParamData;
    }

    public List<InitialParamMaterial> getListInitialParamMaterial(){
        List<InitialParamMaterial> listInitialParamMaterial = new LinkedList();
        if(listNamedAction == null)
            return listInitialParamMaterial;
        for (Iterator<InitialNamedAction> namedAction = listNamedAction.iterator(); namedAction.hasNext();){
            InitialNamedAction a = namedAction.next();
            if(a.getVariable() != null){
                InitialActionParam[] tabParam = a.getVariable().getTabParam();
                for (int i=0; i<tabParam.length; i++){
                    if(tabParam[i] instanceof InitialParamMaterial){
                        listInitialParamMaterial.add((InitialParamMaterial)tabParam[i]);
                    }
                }
            }
        }
        return listInitialParamMaterial;
    }

    public List<InitialParamQuantity> getListInitialParamQuantity(){
        List<InitialParamQuantity> listInitialParamQuantity = new LinkedList();
        if(listNamedAction == null)
            return listInitialParamQuantity;
        for (Iterator<InitialNamedAction> namedAction = listNamedAction.iterator(); namedAction.hasNext();){
            InitialNamedAction a = namedAction.next();
            if(a.getVariable() != null){
                InitialActionParam[] tabParam = a.getVariable().getTabParam();
                for (int i=0; i<tabParam.length; i++){
                    if(tabParam[i] instanceof InitialParamQuantity){
                        listInitialParamQuantity.add((InitialParamQuantity)tabParam[i]);
                    }
                }
            }
        }
        return listInitialParamQuantity;
    }

    public List<InitialAcquisitionOutput> getListInitialAcquisitionOutput(){
        List<InitialAcquisitionOutput> listInitialAcquisitionOutput = new LinkedList();
        if(listNamedAction == null)
            return listInitialAcquisitionOutput;
        for (Iterator<InitialNamedAction> namedAction = listNamedAction.iterator(); namedAction.hasNext();){
            InitialNamedAction a = namedAction.next();
            if(a instanceof InitialActionAcquisition){
                ArrayList<InitialAcquisitionOutput> list = ((InitialActionAcquisition)a).getListOutput();
                int nb = list.size();
                for (int i=0; i<nb; i++){
                    listInitialAcquisitionOutput.add(list.get(i));
                }
            }
        }
        return listInitialAcquisitionOutput;
    }

    public List<InitialManipulationOutput> getListInitialManipulationOutput(){
        List<InitialManipulationOutput> listInitialManipulationOutput = new LinkedList();
        if(listNamedAction == null)
            return listInitialManipulationOutput;
        for (Iterator<InitialNamedAction> namedAction = listNamedAction.iterator(); namedAction.hasNext();){
            InitialNamedAction a = namedAction.next();
            if(a instanceof InitialActionManipulation){
                ArrayList<InitialManipulationOutput> list = ((InitialActionManipulation)a).getListOutput();
                int nb = list.size();
                for (int i=0; i<nb; i++){
                    listInitialManipulationOutput.add(list.get(i));
                }
            }
        }
        return listInitialManipulationOutput;
    }

    public List<InitialTreatmentOutput> getListInitialTreatmentOutput(){
        List<InitialTreatmentOutput> listInitialTreatmentOutput = new LinkedList();
        if(listNamedAction == null)
            return listInitialTreatmentOutput;
        for (Iterator<InitialNamedAction> namedAction = listNamedAction.iterator(); namedAction.hasNext();){
            InitialNamedAction a = namedAction.next();
            if(a instanceof InitialActionTreatment){
                ArrayList<InitialTreatmentOutput> list = ((InitialActionTreatment)a).getListOutput();
                int nb = list.size();
                for (int i=0; i<nb; i++){
                    listInitialTreatmentOutput.add(list.get(i));
                }
            }
        }
        return listInitialTreatmentOutput;
    }



     // toXML
    @Override
    public Element toXML(){
        Element el = new Element(TAG_INITIAL_PROC);
        Element element = super.toXML(el);
        element.addContent(new Element(TAG_INITIAL_PROC_CODE).setText(code));
        element.addContent(new Element(TAG_INITIAL_PROC_FREE_ACTION).setText(isFreeAction ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        element.addContent(new Element(TAG_INITIAL_PROC_TASK).setText(taskMode ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        element.addContent(new Element(TAG_INITIAL_PROC_TASK_REPEAT).setText(isTaskRepeat ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        element.addContent(new Element(TAG_INITIAL_PROC_HYPOTHESIS_MODE).setText(Character.toString(hypothesisMode)));
        element.addContent(new Element(TAG_INITIAL_PROC_PRINCIPLE_MODE).setText(Character.toString(principleMode)));
        element.addContent(new Element(TAG_INITIAL_PROC_DRAW_PRINCIPLE).setText(drawPrinciple ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        element.addContent(new Element(TAG_INITIAL_PROC_EVALUATION_MODE).setText(Character.toString(evaluationMode)));
        element.addContent(materialStrategy.toXMLRef());
        for(Iterator<Material> m = listMaterial.iterator();m.hasNext();){
            element.addContent(m.next().toXML());
        }
        for(Iterator<InitialNamedAction> a = listNamedAction.iterator();a.hasNext();){
            element.addContent(a.next().toXML());
        }
        element.addContent(new Element(TAG_INITIAL_PROC_TASK_DRAW).setText(isTaskDraw ? MyConstants.XML_BOOLEAN_TRUE : MyConstants.XML_BOOLEAN_FALSE));
        return element;
    }

    public Element toXMLRef(){
        Element element = new Element(TAG_INITIAL_PROC_REF);
        element.addContent(new Element(TAG_INITIAL_PROC_CODE).setText(code));
        return element;
    }

    
    @Override
    public boolean isTaskProc(){
        return taskMode;
    }

    /* return true if material strategy  != S0 */
    @Override
    public boolean hasMaterial(){
        return getMaterialStrategy().hasMaterial();
    }

    @Override
    public Element toELO(){
        Element e = new Element(ExperimentalProcedure.TAG_EXPERIMENTAL_PROCEDURE);
        e.addContent(toXML());
        return e;
    }

    public void addMaterialProc(ArrayList<Material> listToAdd){
        for(Iterator<Material> m = listToAdd.iterator();m.hasNext();){
            listMaterial.add(m.next());
        }
    }

    public List<TypeMaterial> getListTypeMaterial(){
        List<TypeMaterial> list = new LinkedList();
        if(listMaterial == null)
            return list;
        for(Iterator<Material> m = listMaterial.iterator();m.hasNext();){
            List<TypeMaterial> lt = m.next().getListType();
            for(Iterator<TypeMaterial> t = lt.iterator(); t.hasNext();){
                TypeMaterial type = t.next();
                if(!list.contains(type))
                    list.add(type);
            }
        }
        return list;
    }

}
