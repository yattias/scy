/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.logger.TaskTreePosition;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * experimental procedure: can be an initial procedure or a learner procedure
 * @author Marjolaine
 */
public class ExperimentalProcedure implements Cloneable {
    /*tag name  */
    public static final String TAG_EXPERIMENTAL_PROCEDURE = "experimental_procedure";
    public static final String TAG_EXP_PROC_NAME = "name" ;
    public static final String TAG_EXP_PROC_RIGHT = "right";
    public static final String TAG_EXP_MATERIAL_PROD = "material_prod";


    /* identifiant base de donnees */
    protected long dbKey;
    /*
     * nom du protocole
     */
    protected List<LocalText> listName;
    /* date de derniere modif */
    protected java.sql.Date dateLastModification;


    /*
     * question associee
     */
    protected Question question;
    /* hypotheses */
    protected Hypothesis hypothesis;
    /* general principle */
    protected GeneralPrinciple generalPrinciple;
    /* liste du materiel utilise pour le protocole */
    protected MaterialProc materials;
    /* liste des taches */
    protected Manipulation manipulation;
    /* Feuille de donnees */
    protected DataSheet dataSheet;
    /*evaluation */
    protected Evaluation evaluation;

    /* actif */
    protected boolean activ;
    /* droit */
    protected char right;
    /* protocole ouvert ou feme */
    protected boolean open = true;

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public GeneralPrinciple getGeneralPrinciple() {
        return generalPrinciple;
    }

    public void setGeneralPrinciple(GeneralPrinciple generalPrinciple) {
        this.generalPrinciple = generalPrinciple;
    }

    public Hypothesis getHypothesis() {
        return hypothesis;
    }

    public void setHypothesis(Hypothesis hypothesis) {
        this.hypothesis = hypothesis;
    }
    
    
    // CONSTRUCTEURS
    public ExperimentalProcedure(long dbKey, List<LocalText> listName, java.sql.Date dateLastModification, boolean isActiv, char right ) {
        this.dbKey = dbKey;
        this.listName = listName;
        this.dateLastModification = dateLastModification;
        this.activ = isActiv;
        this.right = right;
        this.open = true;
        this.question = null;
        this.hypothesis = null;
        this.generalPrinciple = null;
        this.materials = new MaterialProc(new LinkedList());
        this.manipulation = null;
        this.dataSheet = null;
        this.evaluation = null;
    }



    /*creation d'un nouveau protocole */
    public ExperimentalProcedure(List<LocalText> listName,  java.sql.Date dateLastModification){
        this.dbKey = -1;
        this.listName = listName;
        this.dateLastModification = dateLastModification;
        this.activ = true;
        this.right = MyConstants.EXECUTE_RIGHT;
        this.open = true;
        this.question = null;
        this.hypothesis = null;
        this.generalPrinciple = null;
        this.materials = new MaterialProc(new LinkedList());
        this.manipulation = null;
        this.dataSheet = null;
        this.evaluation = null;
    }

    public ExperimentalProcedure(ExperimentalProcedure proc) {
        this.dbKey = proc.getDbKey();
        this.listName = proc.getListName();
        this.dateLastModification = proc.getDateLastModification();
        this.activ = proc.isActiv();
        this.right = proc.getRight();
        this.open = proc.isOpen();
        this.question = proc.getQuestion();
        this.hypothesis = proc.getHypothesis();
        this.generalPrinciple = proc.getGeneralPrinciple();
        this.materials = proc.getMaterials();
        if(materials == null)
            materials= new MaterialProc(new LinkedList());
        this.manipulation = proc.getManipulation();
        this.dataSheet = proc.getDataSheet();
        this.evaluation = proc.getEvaluation();
    }

    public ExperimentalProcedure(Element xmlElem) throws JDOMException {
		
	}

    public Manipulation getManipulation() {
        return manipulation;
    }

    public void setManipulation(Manipulation manipulation) {
        this.manipulation = manipulation;
    }

    public MaterialProc getMaterials() {
        return materials;
    }

    public void setMaterials(MaterialProc materials) {
        this.materials = materials;
    }
    public String getName(Locale locale) {
        return CopexUtilities.getText(listName, locale);
    }


    public List<LocalText> getListName() {
        return listName;
    }

    public void setListName(List<LocalText> listName) {
        this.listName = listName;
    }

    public void setDbKey(long dbKey) {
        this.dbKey = dbKey;
    }

    public DataSheet getDataSheet() {
        return dataSheet;
    }

    public void setDataSheet(DataSheet dataSheet) {
        this.dataSheet = dataSheet;
    }

    public void setDateLastModification(java.sql.Date dateLastModification) {
        this.dateLastModification = dateLastModification;
    }

    public void setRight(char right) {
        this.right = right;
    }

    public void setName(LocalText text) {
        int id = CopexUtilities.getIdText(text.getLocale(), listName);
        if(id ==-1){
            this.listName.add(text);
        }else{
            this.listName.set(id, text);
        }
    }
    public void setName(String text){
        for(Iterator<LocalText> t = listName.iterator();t.hasNext();){
            t.next().setText(text);
        }
    }

    public Question getQuestion() {
        return question;
    }

    

    public java.sql.Date getDateLastModification() {
        return dateLastModification;
    }

    public char getRight() {
        return right;
    }

    public void setActiv(boolean activ) {
        this.activ = activ;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public long getDbKey() {
        return dbKey;
    }
    
    


    @Override
    public Object clone()  {
        try {
            ExperimentalProcedure proc = (ExperimentalProcedure) super.clone() ;
            long dbKeyC = this.dbKey;
            List<LocalText> listNameC = new LinkedList();
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                listNameC.add((LocalText)t.next().clone());
            }
            proc.setListName(listNameC);
            java.sql.Date  dateLastModifC = null;
            if (this.dateLastModification != null)
                dateLastModifC = (java.sql.Date)this.dateLastModification.clone();
            char taskRightC = new Character(this.right);
            boolean activC = this.activ;
            boolean openC = this.open;

            Question questionC = null;
            if(question != null){
                questionC = (Question)this.question.clone();
            }
            Hypothesis hypothesisC = null;
            if(this.hypothesis != null)
                hypothesisC = (Hypothesis)this.hypothesis.clone();
            GeneralPrinciple generalPrincipleC = null;
            if(this.generalPrinciple != null)
                generalPrincipleC = (GeneralPrinciple)this.generalPrinciple.clone();
            MaterialProc materialsC= null;
            if(this.materials != null)
                materialsC = (MaterialProc)this.materials.clone();
            Manipulation manipulationC = null;
            if(this.manipulation != null)
                manipulationC = (Manipulation)this.manipulation.clone();
            DataSheet dataSheetC = null;
            if (this.dataSheet != null)
                dataSheetC = (DataSheet)this.dataSheet.clone();
            Evaluation evaluationC = null;
            if(this.evaluation != null)
                evaluationC = (Evaluation)this.evaluation.clone();
            proc.setDbKey(dbKeyC);
            proc.setDateLastModification(dateLastModifC);
            proc.setActiv(activC);
            proc.setRight(taskRightC);
            proc.setOpen(openC);
            proc.setQuestion(questionC);
            proc.setHypothesis(hypothesisC);
            proc.setGeneralPrinciple(generalPrincipleC);
            proc.setMaterials(materialsC);
            proc.setManipulation(manipulationC);
            proc.setDataSheet(dataSheetC);
            proc.setEvaluation(evaluationC);
            return proc;
        } catch (CloneNotSupportedException e) { 
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }
    
    // METHODES
    
    
    /* retourne true si le protocole est actif  */
    public boolean isActiv(){
        return  activ;
    }

    
    /* suppression de taches - retourne vrai si ca s'est bien passe */
    public boolean deleteTasks(ArrayList<CopexTask> listT){
        boolean isOk = true;
        int nbToDel = listT.size();
        int nbT = manipulation.getListTask().size();
        for (int k=0; k<nbToDel; k++){
            int index = -1;
            for (int i=0; i<nbT; i++){
                if (manipulation.getListTask().get(i).getDbKey() == listT.get(k).getDbKey()){
                    index = i;
                    break;
                }
            }
            if (index != -1)
                manipulation.getListTask().remove(index);
            else
                isOk = false;
            
        }
        return isOk;
    }
    
    /* ajout d'une tache */
    public void addAction(CopexAction a){
        this.manipulation.add(a);
    }
    public void addStep(Step s){
        this.manipulation.add(s);
    }
    public void addQuestion(Question q){
        this.manipulation.add(q);
    }
    
    
    /* ajout de taches */
    public void addTasks(ArrayList<CopexTask> listT){
        int nbT = listT.size();
        for (int i=0; i<nbT; i++){
            this.manipulation.add(listT.get(i));
        }
    }
    
    public boolean isInitialProcedure(){
        return this instanceof InitialProcedure;
    }
    
    public boolean isLearnerProcedure(){
        return this instanceof LearnerProcedure;
    }
    
    public List<CopexTask> getListTask(){
        if(manipulation == null)
            return new LinkedList();
        else
            return manipulation.getListTask();
    }

    public void setListTask(ArrayList<CopexTask> listTask){
        manipulation = new Manipulation(listTask);
    }

     // toXML
    public Element toXML(){
        return null;
    }

    public Element toXML(Element element){
        if(listName != null && listName.size() > 0){
            for (Iterator<LocalText> t = listName.iterator(); t.hasNext();) {
                LocalText l = t.next();
                Element e = new Element(TAG_EXP_PROC_NAME);
                e.setText(l.getText());
                e.setAttribute(new Attribute(MyConstants.XMLNAME_LANGUAGE, l.getLocale().getLanguage()));
                element.addContent(e);
            }
        }
        //element.addContent(new Element(TAG_EXP_PROC_RIGHT).setText(Character.toString(right)));
        if(question != null)
            element.addContent(question.toXML());
        if(hypothesis != null)
            element.addContent(hypothesis.toXML());
        if(generalPrinciple != null)
            element.addContent(generalPrinciple.toXML());
        if(evaluation != null)
            element.addContent(evaluation.toXML());
        List<Material> listMaterialProd  = getListMaterialProd();
        if(listMaterialProd.size() > 0){
            Element e = new Element(TAG_EXP_MATERIAL_PROD);
            for(Iterator<Material> m = listMaterialProd.iterator();m.hasNext();){
                e.addContent(m.next().toXML());
            }
            element.addContent(e);
        }
        element.addContent(manipulation.toXML());
        return element;
    }

    public List<Material> getListMaterialProd(){
        List<Material> list = new LinkedList();
        for(Iterator<CopexTask> t = getListTask().iterator();t.hasNext();){
            List<Material> l = t.next().getAllMaterialProd();
            for(Iterator<Material> m = l.iterator();m.hasNext();){
                Material mat = m.next();
                int idM = getIdMaterial(list,mat);
                if(idM == -1)
                    list.add(mat);
            }
        }
        return list;
    }

    private int getIdMaterial(List<Material> list, Material m){
        int nb = list.size();
        for (int i=0; i<nb; i++){
            if(m.getDbKey()==list.get(i).getDbKey() || m.getCode().equals(list.get(i).getCode())){
                return i;
            }
        }
        return -1;
    }

     public List<QData> getListDataProd(){
        List<QData> list = new LinkedList();
        for(Iterator<CopexTask> t = getListTask().iterator();t.hasNext();){
            List<QData> l = t.next().getAllDataProd();
            for(Iterator<QData> m = l.iterator();m.hasNext();){
                QData data = m.next();
                int idD = getIdData(list,data);
                if(idD == -1)
                    list.add(data);
            }
        }
        return list;
    }

    private int getIdData(List<QData> list, QData d){
        int nb = list.size();
        for (int i=0; i<nb; i++){
           // if(d.getDbKey()==list.get(i).getDbKey() || d.getCode().equals(list.get(i).getCode())){
            if( d.getCode().equals(list.get(i).getCode())){
            }
        }
        return -1;
    }

    public void printRecap(Locale locale){
//        List<CopexTask> listTask = getListTask();
//        int n = listTask.size();
//        System.out.println("************RECAP DES "+n+" TACHES DU PROC "+getName(locale)+" *****************");
//        System.out.println("question : "+getQuestion().getDescription(locale)+" ("+getQuestion().getDbKey()+") : "+getQuestion().getDbKeyChild());
//        for (int k=0; k<n; k++){
//            CopexTask task = listTask.get(k);
//            String frere = " sans frere ";
//            String enfant = " sans enfant ";
//            if (task.getDbKeyBrother() != -1)
//                frere = " "+task.getDbKeyBrother()+" ";
//            if (task.getDbKeyChild() != -1)
//                enfant = " "+task.getDbKeyChild()+" ";
//            String visible = task.isVisible() ? "visible" :"cachee";
//            System.out.println("  - Tache "+task.getDescription(locale)+" ("+task.getDbKey()+") : "+frere+" / "+enfant+ " ("+visible+")");
//
//        }
//        System.out.println("********************************************************");

    }

    public boolean isTaskProc(){
        return false;
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
    
    public List<MaterialUsed> getListMaterialUsed() {
        return materials.getListMaterialUsed();
    }

    public void setListMaterialUsed(List<MaterialUsed> listMaterialUsed) {
        this.materials.setListMaterialUsed(listMaterialUsed);
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
        return true;
    }

    public char getHypothesisMode(){
        return MyConstants.MODE_MENU_NO;
    }
    public char getPrincipleMode(){
        return MyConstants.MODE_MENU_NO;
    }
    public char getEvaluationMode(){
        return MyConstants.MODE_MENU_NO;
    }

    public Element toELO(){
        Element e = new Element(ExperimentalProcedure.TAG_EXPERIMENTAL_PROCEDURE);
        e.addContent(toXML());
        return e;
    }

    public  List<CopexTask> getAllChilds(CopexTask task){
        List<CopexTask> listAllChilds = new LinkedList();
        List<CopexTask> listC = getListChild(task);
        int nb = listC.size();
        for (int i=0; i<nb; i++){
            listAllChilds.add(listC.get(i));
            // ajout des enfants
            List<CopexTask> l = getAllChilds(listC.get(i));
            int n = l.size();
            for (int j=0; j<n; j++){
                listAllChilds.add(l.get(j));
            }
        }
        return listAllChilds ;
    }

   public ArrayList[] getTaskInitialParam( CopexTask task){
       ArrayList[] tabList = new ArrayList[2];
       ArrayList<InitialActionParam> list = new ArrayList();
       ArrayList<Long> listDbKey = new ArrayList();
       List<CopexTask> listChilds = getAllChilds(task);
        int nb = listChilds.size();
        for (int i=0; i<nb; i++){
            CopexTask t = listChilds.get(i);
            if (t instanceof CopexActionManipulation){
                InitialNamedAction a = ((CopexActionManipulation)t).getNamedAction();
                if (a != null && a.getVariable() != null){
                    InitialActionParam[] tab = a.getVariable().getTabParam();
                    if(tab != null){
                        for(int j=0; j<tab.length; j++){
                            list.add(tab[j]);
                            listDbKey.add(t.getDbKey());
                        }
                    }
                }
            }else if (t instanceof CopexActionAcquisition){
                InitialNamedAction a = ((CopexActionAcquisition)t).getNamedAction();
                if (a != null && a.getVariable() != null){
                    InitialActionParam[] tab = a.getVariable().getTabParam();
                    if(tab != null){
                        for(int j=0; j<tab.length; j++){
                            list.add(tab[j]);
                            listDbKey.add(t.getDbKey());
                        }
                    }
                }
            }else if (t instanceof CopexActionTreatment){
                InitialNamedAction a = ((CopexActionTreatment)t).getNamedAction();
                if (a != null && a.getVariable() != null){
                    InitialActionParam[] tab = a.getVariable().getTabParam();
                    if(tab != null){
                        for(int j=0; j<tab.length; j++){
                            list.add(tab[j]);
                            listDbKey.add(t.getDbKey());
                        }
                    }
                }
            }
        }
        tabList[0] = list;
        tabList[1] = listDbKey;
        return tabList;
    }


    /* retourne la liste des output des actions de l'etape */
    public ArrayList[] getTaskInitialOutput(CopexTask task){
        ArrayList[] tabList = new ArrayList[2];
       ArrayList<InitialOutput> list = new ArrayList();
       ArrayList<Long> listDbKey = new ArrayList();
        List<CopexTask> listChilds = getAllChilds(task);
        int nb = listChilds.size();
        for (int i=0; i<nb; i++){
            CopexTask t = listChilds.get(i);
            if (t instanceof CopexActionManipulation){
                InitialNamedAction a = ((CopexActionManipulation)t).getNamedAction();
                if (a != null && a instanceof InitialActionManipulation){
                    ArrayList<InitialManipulationOutput> l = ((InitialActionManipulation)a).getListOutput() ;
                    int n = l.size();
                    for (int j=0; j<n; j++){
                        list.add(l.get(j));
                    }
                }
            }else if (t instanceof CopexActionAcquisition){
                InitialNamedAction a = ((CopexActionAcquisition)t).getNamedAction();
                if (a != null && a instanceof InitialActionAcquisition){
                    ArrayList<InitialAcquisitionOutput> l = ((InitialActionAcquisition)a).getListOutput() ;
                    int n = l.size();
                    for (int j=0; j<n; j++){
                        list.add(l.get(j));
                    }
                }
            }else if (t instanceof CopexActionTreatment){
                InitialNamedAction a = ((CopexActionTreatment)t).getNamedAction();
                if (a != null && a instanceof InitialActionTreatment){
                    ArrayList<InitialTreatmentOutput> l = ((InitialActionTreatment)a).getListOutput() ;
                    int n = l.size();
                    for (int j=0; j<n; j++){
                        list.add(l.get(j));
                    }
                }
            }
        }
        tabList[0] = list;
        tabList[1] = listDbKey;
        return tabList;
    }

    // retourne la tache avec ce dbKey
    public  CopexTask getTask(long dbKey){
        int nbT = getListTask().size() ;
        for (int i=0; i<nbT; i++){
            if(getListTask().get(i).getDbKey() == dbKey)
                return getListTask().get(i);
        }
        return null;
    }

    public void applyStepRepeat(CopexTask step){
        if(step.getTaskRepeat() != null){
            ArrayList<TaskRepeatParam> listTaskRepeatParam = step.getTaskRepeat().getListParam();
            for(Iterator<TaskRepeatParam> trp = listTaskRepeatParam.iterator(); trp.hasNext();){
                TaskRepeatParam taskRepeatParam = trp.next();
                long dbKeyAction = taskRepeatParam.getDbKeyActionParam();
                CopexTask task = getTask(dbKeyAction);
                if(task != null){
                    TaskRepeat tr = new TaskRepeat(-1, 1);
                    tr.addParam(taskRepeatParam);
                    task.addTaskRepeatParent(tr);
                }
            }
        }
    }
}
