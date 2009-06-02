/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.common;

import eu.scy.tools.copex.utilities.MyConstants;
import java.util.ArrayList;

/**
 * represente un protocole
 * @author MBO
 */
public class ExperimentalProcedure implements Cloneable {

    // ATTRIBUTS
    /* identifiant base de données */
    protected long dbKey;
    /*
     * nom du protocole
     */
    protected String name;
    /* date de dernière modif */
    protected java.sql.Date dateLastModification;
    /*
     * question associée
     */
    protected Question question;
    /* mission */
    protected CopexMission mission;
    /* actif */
    protected boolean activ;
    /* droit */
    protected char right;
    
    /* liste des taches */
    protected ArrayList<CopexTask> listTask;
    
    /* Feuille de données */
    private DataSheet dataSheet;
    
    /* protocole ouvert ou femé */
    protected boolean open = true;
    /* liste du materiel utilise pour le protocole */
    private ArrayList<MaterialUseForProc> listMaterialUse;
    
    // GETTER AND SETTER
    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
    }

    public Question getQuestion() {
        return question;
    }

    public CopexMission getMission() {
        return mission;
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

    public ArrayList<CopexTask> getListTask() {
        return listTask;
    }

    public void setListTask(ArrayList<CopexTask> listTask) {
        this.listTask = listTask;
    }

    public long getDbKey() {
        return dbKey;
    }
    public void setMission(CopexMission mission) {
        this.mission = mission;
    }
    // CONSTRUCTEURS 
    public ExperimentalProcedure(long dbKey, String name, java.sql.Date dateLastModification, boolean isActiv, char right) {
        this.dbKey = dbKey;
        this.name = name;
        this.dateLastModification = dateLastModification;
        this.activ = isActiv;
        this.right = right;
        this.question = null;
        this.open = true;
        this.dataSheet = null;
        this.listMaterialUse = new ArrayList();
    }

    
    
    /*creation d'un nouveau protocole */ 
    public ExperimentalProcedure(String name, CopexMission mission, java.sql.Date dateLastModification){
        this.dbKey = -1;
        this.name = name;
        this.mission = mission;
        this.dateLastModification = dateLastModification;
        this.activ = true;
        this.right = MyConstants.EXECUTE_RIGHT;
        this.question = null;
        this.open = true;
        this.dataSheet = null;
        this.listMaterialUse = new ArrayList();
    }

    public ExperimentalProcedure(ExperimentalProcedure proc) {
        this.dbKey = proc.getDbKey();
        this.name = proc.getName();
        this.mission = proc.getMission();
        this.dateLastModification = proc.getDateLastModification();
        this.activ = proc.isActiv();
        this.right = proc.getRight();
        this.question = proc.getQuestion();
        this.open = proc.isOpen();
        this.dataSheet = proc.getDataSheet();
        this.listMaterialUse = proc.getListMaterialUse();
        this.listTask = proc.getListTask() ;
    }


    @Override
    public Object clone()  {
        try {
            ExperimentalProcedure proc = (ExperimentalProcedure) super.clone() ;
            long dbKeyC = this.dbKey;
            String nameC = new String(this.name);
            java.sql.Date  dateLastModifC = null;
            if (this.dateLastModification != null)
                dateLastModifC = (java.sql.Date)this.dateLastModification.clone();
            Question questionC = null;
            if(question != null){
                questionC = (Question)this.question.clone();
            }
            CopexMission missionC = null;
            if (mission != null)
                missionC  =(CopexMission)this.mission.clone();
            char taskRightC = new Character(this.right);
            boolean activC = this.activ;
            boolean openC = this.open;
            DataSheet dataSheetC = null;
            if (this.dataSheet != null)
                dataSheetC = (DataSheet)this.dataSheet.clone();
            ArrayList<CopexTask> listTaskC = null;
            if (listTask != null){
                int nbT = this.listTask.size();
                listTaskC = new ArrayList();
                for (int i=0; i<nbT; i++){
                    listTaskC.add((CopexTask)listTask.get(i).clone());
                }
            }
            ArrayList<MaterialUseForProc> listMatC = null;
            if (listMaterialUse != null){
                int nb = this.listMaterialUse.size();
                listMatC = new ArrayList();
                for (int i=0; i<nb; i++){
                    listMatC.add((MaterialUseForProc)listMaterialUse.get(i).clone());
                }
            }
            proc.setDbKey(dbKeyC);
            proc.setName(nameC);
            proc.setDateLastModification(dateLastModifC);
            proc.setQuestion(questionC);
            proc.setMission(missionC);
            proc.setActiv(activC);
            proc.setRight(taskRightC);
            proc.setListTask(listTaskC);
            proc.setOpen(openC);
            proc.setDataSheet(dataSheetC);
            proc.setListMaterialUse(listMatC);
            return proc;
        } catch (CloneNotSupportedException e) { 
	    // this shouldn't happen, since we are Cloneable
	    throw new InternalError();
	}
    }
    
    // METHODES
    
    /* retourne true si le protocole est actif pour sa mission */
    public boolean isActiv(CopexMission m){
        return (m.getDbKey() == this.mission.getDbKey() && activ);
    }
    
    /* retourne true si le protocole est actif  */
    public boolean isActiv(){
        return  activ;
    }

    public ArrayList<MaterialUseForProc> getListMaterialUse() {
        return listMaterialUse;
    }

    public void setListMaterialUse(ArrayList<MaterialUseForProc> listMaterialUse) {
        this.listMaterialUse = listMaterialUse;
    }
    
    /* suppression de taches - retourne vrai si ca s'est bien passé */
    public boolean deleteTasks(ArrayList<CopexTask> listT){
        boolean isOk = true;
        int nbToDel = listT.size();
        int nbT = listTask.size();
        for (int k=0; k<nbToDel; k++){
            int index = -1;
            for (int i=0; i<nbT; i++){
                if (listTask.get(i).getDbKey() == listT.get(k).getDbKey()){
                    index = i;
                    break;
                }
            }
            if (index != -1)
                listTask.remove(index);
            else
                isOk = false;
            
        }
        return isOk;
    }
    
    /* ajout d'une tache */
    public void addAction(CopexAction a){
        this.listTask.add(a);
    }
    public void addStep(Step s){
        this.listTask.add(s);
    }
    public void addQuestion(Question q){
        this.listTask.add(q);
    }
    
    
    /* ajout de taches */
    public void addTasks(ArrayList<CopexTask> listT){
        int nbT = listT.size();
        for (int i=0; i<nbT; i++){
            this.listTask.add(listT.get(i));
        }
    }
    
    public boolean isInitialProcedure(){
        return this instanceof InitialProcedure;
    }
    
    public boolean isLearnerProcedure(){
        return this instanceof LearnerProcedure;
    }
    
     // suppression d'un materiel
    public void removeMaterialUse(Material m){
        int id = getIdMaterialUse(m.getDbKey());
        if (id != -1)
            this.listMaterialUse.remove(id);
    }
    
     // ajout d'un materiel
    public void addMaterialUse(MaterialUseForProc m){
        if (this.listMaterialUse == null)
            this.listMaterialUse = new ArrayList();
        this.listMaterialUse.add(m);
    }
    
     // modification  d'un materiel
    public void updateMaterialUse(MaterialUseForProc m){
        int id = getIdMaterialUse(m.getMaterial().getDbKey());
        if (id != -1){
            this.listMaterialUse.set(id, m);
        }
    }
    private int getIdMaterialUse(long dbKey){
        int nb = this.listMaterialUse.size();
        for (int i=0; i<nb; i++){
            if (this.listMaterialUse.get(i).getMaterial().getDbKey() == dbKey)
                return i;
        }
        return -1;
    }
    
    /* retourne le materiel utlise pour un mat donné */
    public MaterialUseForProc getMaterialUse(Material m){
        int id = getIdMaterialUse(m.getDbKey());
        if (id != -1){
            return this.listMaterialUse.get(id);
        }
        return null;
    }
}
