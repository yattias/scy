/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.common;

import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * represente un protocole
 * @author MBO
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
}
