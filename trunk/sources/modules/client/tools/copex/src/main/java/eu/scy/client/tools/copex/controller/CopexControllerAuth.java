/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.controller;

import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.db.*;
import eu.scy.client.tools.copex.dnd.SubTree;
import eu.scy.client.tools.copex.main.CopexPanel;
import eu.scy.client.tools.copex.edp.TaskSelected;
import eu.scy.client.tools.copex.logger.CopexProperty;
import eu.scy.client.tools.copex.print.PrintPDF;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
import java.net.URL;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * copex controller for copex authoring
 * @author Marjolaine
 */
public class CopexControllerAuth implements ControllerInterface{
    /** dbMode */
    private boolean dbMode;
    /**Copex Panel */
    private CopexPanel copex;
    /**Locale */
    private Locale locale;
    /**physical quantities list managed in COPEX */
    private ArrayList<PhysicalQuantity> listPhysicalQuantity ;
    /** strategy material list maanged in COPEX */
    private ArrayList<MaterialStrategy> listMaterialStrategy;
    /** type of material by default */
    private TypeMaterial defaultTypeMaterial;
    /** initial procedure*/
    private InitialProcedure initProc;

    /** user*/
    private CopexTeacher teacher;
    /** mission */
    private CopexMission mission;
    /** help mission */
    private CopexMission helpMission;
    /** material list for the help proc */
    private ArrayList<Material> listHelpMaterial;
    /* help procedure*/
    private LearnerProcedure helpProc;

    /**dbC */
    private DataBaseCommunication dbC;

    private CopexConfig config;
    private String copexConfigFileName = "copex.xml";

    //id
    private long idMission = 1 ;
    private long idProc= 1 ;
    private long idTask= 1 ;
    private long idParam= 1 ;
    private long idMaterial= 1 ;
    private long idQuantity= 1 ;
    private long idTypeMaterial= 1 ;
    private long idHypothesis= 1 ;
    private long idGeneralPrinciple= 1 ;
    private long idEvaluation= 1 ;
    private long idMaterialStrategy= 1 ;
    private long idPhysicalQtt= 1  ;
    private long idUnit= 1 ;
    private long idRepeat= 1 ;
    private long idValue= 1 ;
    private long idActionParam= 1 ;
    private long idInitialAction= 1 ;
    private long idAction= 1 ;
    private long idOutput= 1 ;

    public CopexControllerAuth(CopexPanel copex, ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList<MaterialStrategy> listMaterialStrategy, TypeMaterial defaultTypeMaterial, InitialProcedure initProc, CopexTeacher teacher, CopexMission mission, DataBaseCommunication dbC) {
        this.copex = copex;
        this.listPhysicalQuantity = listPhysicalQuantity;
        this.listMaterialStrategy = listMaterialStrategy;
        this.defaultTypeMaterial = defaultTypeMaterial;
        this.initProc = initProc;
        this.teacher = teacher;
        this.mission = mission;
        this.dbC = dbC;
        this.dbMode = true;
    }

    public CopexControllerAuth(CopexPanel copex, ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList<MaterialStrategy> listMaterialStrategy, TypeMaterial defaultTypeMaterial, InitialProcedure initProc, CopexTeacher teacher, CopexMission mission) {
        this.copex = copex;
        this.listPhysicalQuantity = listPhysicalQuantity;
        this.listMaterialStrategy = listMaterialStrategy;
        this.defaultTypeMaterial = defaultTypeMaterial;
        this.initProc = initProc;
        this.teacher = teacher;
        this.mission = mission;
        this.dbC = null;
        this.dbMode = false;
    }


    
    /** initialization of the copex editor */
    @Override
    public CopexReturn initEdP(Locale locale, String idUser, long dbKeyMission, long dbKeyGroup, long dbKeyLabDoc, String labDocName, String fileMission) {
        this.locale = locale;
        // load the help proc
        CopexReturn cr = loadHelpProc();
        if(cr.isError())
            return cr;
        // clone objects
        CopexMission m = (CopexMission)mission.clone();
        ArrayList<ExperimentalProcedure> listP = new ArrayList();
        if(initProc != null)
            listP.add((InitialProcedure)initProc.clone());
        ArrayList<PhysicalQuantity> listPhysicalQuantityC = new ArrayList();
        int nbPhysQ = this.listPhysicalQuantity.size();
        for (int k=0; k<nbPhysQ; k++){
            listPhysicalQuantityC.add((PhysicalQuantity)this.listPhysicalQuantity.get(k).clone());
        }
        copex.initEdp(m, listP,  listPhysicalQuantityC);
        return new CopexReturn();
    }

   
    /** add a task */
    @Override
    public CopexReturn addTask(CopexTask task, ExperimentalProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v, char undoRedo, boolean cut) {
        task.setDbKeyChild(-1);
        task.setDbKeyBrother(-1);
        // search in the list of tasks the index of the brother task
        int idB = -1;
        long dbKeyT ;
        if (taskBrother == null)
            dbKeyT = taskParent.getDbKey();
        else
            dbKeyT = taskBrother.getDbKey();

        idB = getId(initProc.getListTask(), dbKeyT);

        if (idB == -1){
            String msg = copex.getBundleString("MSG_ERROR_ADD_STEP");
            if (task instanceof CopexAction)
                msg = copex.getBundleString("MSG_ERROR_ADD_ACTION");
            else if (task instanceof Question)
                msg = copex.getBundleString("MSG_ERROR_ADD_QUESTION");
            return new CopexReturn(msg, false);
        }

        // save in db and get the new id
       long newDbKey = -1;
        if(dbMode){
            ArrayList v2 = new ArrayList();
            CopexReturn cr;
            if (taskBrother == null){
                cr = TaskFromDB.addTaskParentInDB(this.dbC,getLocale(), task, initProc.getDbKey(), initProc.getListTask().get(idB), v2);
            }else{
                cr = TaskFromDB.addTaskBrotherInDB(this.dbC,getLocale(), task, initProc.getDbKey(), initProc.getListTask().get(idB), v2);
            }
            if (cr.isError())
                return cr;
             newDbKey = (Long)v2.get(0);
        }else{
           newDbKey = idTask++;
        }
       //update modification date
        CopexReturn cr = updateDateProc(initProc);
        if (cr.isError()){
            return cr;
        }

        // update data
        task.setDbKey(newDbKey);
        if (taskBrother == null){
            long oldFC = initProc.getListTask().get(idB).getDbKeyChild();
            taskParent.setDbKeyChild(newDbKey);
            // update in list
            //proc.getListTask().get(idB).setDbKeyChild(newDbKey);
            initProc.getListTask().get(idB).setDbKeyChild(newDbKey);
            if (initProc.getListTask().get(idB).getDbKey() == initProc.getQuestion().getDbKey())
                initProc.getQuestion().setDbKeyChild(newDbKey);
            if(oldFC != -1){
                task.setDbKeyBrother(oldFC);
            }
        }else{
            long dbKeyOldBrother = initProc.getListTask().get(idB).getDbKeyBrother();
            task.setDbKeyBrother(dbKeyOldBrother);
            taskBrother.setDbKeyBrother(newDbKey);
            // update in list
            initProc.getListTask().get(idB).setDbKeyBrother(newDbKey);
        }
        TaskRight taskRight = new TaskRight(MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT);
        if (task instanceof CopexActionChoice){
            CopexActionChoice a = new CopexActionChoice(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(), task.getDraw(),true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), ((CopexActionChoice)task).getTabParam(), task.getTaskRepeat());
            initProc.addAction(a);
        }else if (task instanceof CopexActionAcquisition){
            CopexActionAcquisition a = new CopexActionAcquisition(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(), task.getDraw(),true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), ((CopexActionAcquisition)task).getTabParam(), ((CopexActionAcquisition)task).getListDataProd(), task.getTaskRepeat());
            initProc.addAction(a);
        }else if (task instanceof CopexActionManipulation){
            CopexActionManipulation a = new CopexActionManipulation(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(), task.getDraw(),true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), ((CopexActionManipulation)task).getTabParam(), ((CopexActionManipulation)task).getListMaterialProd(), task.getTaskRepeat());
            initProc.addAction(a);
        }else if (task instanceof CopexActionTreatment){
            CopexActionTreatment a = new CopexActionTreatment(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(), task.getDraw(),true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), ((CopexActionTreatment)task).getTabParam(), ((CopexActionTreatment)task).getListDataProd(), task.getTaskRepeat());
            initProc.addAction(a);
        }else if (task instanceof CopexActionNamed){
            CopexActionNamed a = new CopexActionNamed(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(), task.getDraw(), true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), task.getTaskRepeat());
            initProc.addAction(a);
        }else if (task instanceof CopexAction){
            CopexAction a = new CopexAction(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(), task.getDraw(), true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), task.getTaskRepeat());
            initProc.addAction(a);
        }else if (task instanceof Step){
            Step s = new Step(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(),task.getDraw(), true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), task.getTaskRepeat());
            initProc.addStep(s);
        }else if (task instanceof Question){
            Question q = new Question(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(),task.getDraw(),  true, taskRight, false, task.getDbKeyBrother(), task.getDbKeyChild() );
            initProc.addQuestion(q);
        }
         updateDatasheetProd(initProc);
         initProc.lockMaterialUsed();
        // in v[0] the new proc.
        v.add((InitialProcedure)initProc.clone());

        return new CopexReturn();
    }

    /* returns the index in the list, -1 if not found */
    private int getId(List<CopexTask> listT, long dbKey){
        int nbT = listT.size();
        for (int i=0; i<nbT; i++){
            CopexTask t = listT.get(i);
            if (t.getDbKey() == dbKey)
                return i;
        }
        return -1;
    }

    private void updateDatasheetProd(ExperimentalProcedure proc){
        List<QData> listDataProd = proc.getListDataProd();
        if(listDataProd.isEmpty())
            proc.setDataSheet(null);
        else
            proc.setDataSheet(new DataSheet(listDataProd));
    }

    /* update proc. date*/
    private CopexReturn updateDateProc(ExperimentalProcedure p){
        java.sql.Date date = CopexUtilities.getCurrentDate();
        p.setDateLastModification(date);
        if(dbMode){
            CopexReturn cr = ExperimentalProcedureFromDB.updateDateProcInDB(dbC, p.getDbKey(), date);
            if (cr.isError()){
                return cr;
            }
        }
        return new CopexReturn();
    }
    
    /** add an action */
    @Override
    public CopexReturn addAction(CopexAction action, ExperimentalProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v) {
        return  addTask(action, proc, taskBrother, taskParent, v, MyConstants.NOT_UNDOREDO, false);
    }

    /** modify an action */
    @Override
    public CopexReturn updateAction(CopexAction newAction, ExperimentalProcedure proc, CopexAction oldAction, ArrayList v) {
        return updateTask(newAction, proc, oldAction, v);
    }

    /** update a task */
    public CopexReturn updateTask(CopexTask newTask, ExperimentalProcedure proc, CopexTask oldTask, ArrayList v) {
       // search in the tasks list the index of the old task 
        int idOld = getId(initProc.getListTask(), oldTask.getDbKey());
        if (idOld == -1){
            String msg = copex.getBundleString("MSG_ERROR_UPDATE_STEP");
            if (newTask instanceof CopexAction)
                msg = copex.getBundleString("MSG_ERROR_UPDATE_ACTION");
            else if (newTask instanceof Question)
                msg = copex.getBundleString("MSG_ERROR_UPDATE_QUESTION");
            return new CopexReturn(msg, false);
        }
        if(dbMode){
            // update in database
            ArrayList v2 = new ArrayList();
            CopexReturn cr =TaskFromDB.updateTaskInDB(this.dbC,getLocale(), newTask, initProc.getDbKey(), oldTask, v2);
            if (cr.isError())
                return cr;
            newTask = (CopexTask)v2.get(0);
            // update the task repeat
            TaskRepeat oldRepeat = oldTask.getTaskRepeat();
            TaskRepeat newRepeat = newTask.getTaskRepeat();
            v2 = new ArrayList();
            if(oldRepeat == null && newRepeat != null){
                cr = TaskFromDB.insertTaskRepeatInDB(dbC, oldTask.getDbKey(), newRepeat, v2);
                if(cr.isError())
                    return cr;
                newRepeat = (TaskRepeat)v2.get(0);
            }else if (oldRepeat != null){
                if(newRepeat == null){
                    cr = TaskFromDB.deleteTaskRepeatFromDB(dbC, oldTask.getDbKey(), oldRepeat);
                    if(cr.isError())
                        return cr;
                }else{
                    cr = TaskFromDB.updateTaskRepeatInDB(dbC, newRepeat, v2);
                    if(cr.isError())
                        return cr;
                    newRepeat = (TaskRepeat)v2.get(0);
                }
            }
        }else{
            // no db
            if(newTask instanceof CopexAction){
                InitialNamedAction newA = null;
                boolean isNewTaskActionNamed = newTask instanceof  CopexActionNamed || newTask instanceof CopexActionAcquisition || newTask instanceof CopexActionChoice || newTask instanceof CopexActionManipulation || newTask instanceof CopexActionTreatment ;
                if (isNewTaskActionNamed){
                newA = ((CopexActionNamed)newTask).getNamedAction() ;
            }
                if(newA != null){
                    Object[] newTabParam = ((CopexActionParam)newTask).getTabParam() ;
                    for (int i=0; i<newTabParam.length; i++){
                        if(newTabParam[i] instanceof ActionParam){
                            ((ActionParam)newTabParam[i]).setDbKey(idParam++);
                        }else if (newTabParam[i] instanceof ArrayList){
                            ArrayList<ActionParam> p = (ArrayList<ActionParam>)newTabParam[i];
                            int np = p.size();
                            for (int k=0; k<np; k++){
                                p.get(k).setDbKey(idParam++);
                            }
                        }
                    }
                    if(newTask instanceof CopexActionManipulation){
                        ArrayList<Object> listM = ((CopexActionManipulation)newTask).getListMaterialProd() ;
                        int nbM = listM.size();
                        for (int i=0; i<nbM; i++){
                            if (listM.get(i) instanceof Material){
                                Material m = (Material)listM.get(i);
                                m.setDbKey(idMaterial++);
                                // parameters creation
                                int nbParam = m.getListParameters().size();
                                for (int j=0; j<nbParam; j++){
                                    ((Material)listM.get(i)).getListParameters().get(j).setDbKey(idQuantity++);
                                }
                            }else if (listM.get(i) instanceof ArrayList){
                                int n = ((ArrayList)listM.get(i)).size();
                                for (int r=0; r<n;r++){
                                    Material m = ((ArrayList<Material>)listM.get(i)).get(r);
                                    m.setDbKey(idMaterial++);
                                    // parameters creation
                                    int nbParam = m.getListParameters().size();
                                    for (int j=0; j<nbParam; j++){
                                        ((ArrayList<Material>)listM.get(i)).get(r).getListParameters().get(j).setDbKey(idQuantity++);
                                    }
                                }
                            }
                        }
                    }else if (newTask instanceof CopexActionAcquisition){
                        ArrayList<Object> listD = ((CopexActionAcquisition)newTask).getListDataProd() ;
                        int nbD = listD.size() ;
                        for (int i=0; i<nbD; i++){
                            if (listD.get(i) instanceof QData){
                                QData d = (QData)listD.get(i);
                                d.setDbKey(idQuantity++);
                            }else if (listD.get(i) instanceof ArrayList){
                                int n = ((ArrayList)listD.get(i)).size();
                                for (int r=0; r<n;r++){
                                    QData d = ((ArrayList<QData>)listD.get(i)).get(r);
                                    d.setDbKey(idQuantity++);
                                }
                            }
                        }
                    }else if (newTask instanceof CopexActionTreatment){
                        ArrayList<Object> listD = ((CopexActionTreatment)newTask).getListDataProd() ;
                        int nbD = listD.size() ;
                        for (int i=0; i<nbD; i++){
                            if (listD.get(i) instanceof QData){
                                QData d = (QData)listD.get(i);
                                d.setDbKey(idQuantity++);
                            }else if (listD.get(i) instanceof ArrayList){
                                int n = ((ArrayList)listD.get(i)).size();
                                for (int r=0; r<n;r++){
                                    QData d = ((ArrayList<QData>)listD.get(i)).get(r);
                                    d.setDbKey(idQuantity++);
                                }
                            }
                        }
                    }
                }
            }
        }
         updateDatasheetProd(initProc);
         initProc.lockMaterialUsed();
        // update modification date
        CopexReturn cr = updateDateProc(initProc);
        if (cr.isError()){
            return cr;
        }

        // update
        //oldTask.setComments(newTask.getComments());
        //oldTask.setDescription(newTask.getDescription());
        newTask.setDbKey(oldTask.getDbKey());
        newTask.setDbKeyBrother(oldTask.getDbKeyBrother());
        newTask.setDbKeyChild(oldTask.getDbKeyChild());
        newTask.setTaskImage(oldTask.getTaskImage());
        newTask.setTaskRight(oldTask.getTaskRight());
        newTask.setVisible(oldTask.isVisible());
        newTask.setRoot(oldTask.isQuestionRoot());
        if (newTask instanceof CopexActionNamed){
            if ( initProc.getListTask().get(idOld) instanceof CopexActionNamed)
                ((CopexActionNamed)initProc.getListTask().get(idOld)).setNamedAction(((CopexActionNamed)newTask).getNamedAction());
            else{
                CopexTask ot = initProc.getListTask().get(idOld) ;
                CopexActionNamed an = new CopexActionNamed(ot.getDbKey(), getLocale(), ot.getName(getLocale()), ot.getDescription(getLocale()), ot.getComments(getLocale()), ot.getTaskImage(),ot.getDraw(),  ot.isVisible(), ot.getTaskRight(),((CopexActionNamed)newTask).getNamedAction(), ot.getTaskRepeat() );
                initProc.getListTask().set(idOld, an);
            }
        }else if (newTask instanceof CopexAction){
            if ( initProc.getListTask().get(idOld) instanceof CopexActionNamed){
                CopexTask ot = initProc.getListTask().get(idOld) ;
                CopexAction a = new CopexAction(ot.getDbKey(), getLocale(), "", ot.getDescription(getLocale()), ot.getComments(getLocale()), ot.getTaskImage(),ot.getDraw(),  ot.isVisible(),ot.getTaskRight(), ot.getTaskRepeat());
                initProc.getListTask().set(idOld, a);
            }
        }
        initProc.getListTask().get(idOld).setListDescription(newTask.getListDescription());
        initProc.getListTask().get(idOld).setListComments(newTask.getListComments());
        if (initProc.getListTask().get(idOld).getDbKey() == initProc.getQuestion().getDbKey()){
            // update the question
            initProc.getQuestion().setListDescription(newTask.getListDescription());
            initProc.getQuestion().setListComments(newTask.getListComments());
        }

        // in v[0] the new proc
        v.add((InitialProcedure)initProc.clone());
        return new CopexReturn();
    }

    /** add a step */
    @Override
    public CopexReturn addStep(Step step, ExperimentalProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v) {
        return addTask(step, proc, taskBrother, taskParent,v, MyConstants.NOT_UNDOREDO, false);
    }

    /** update a step */
    @Override
    public CopexReturn updateStep(Step newStep, ExperimentalProcedure proc, Step oldStep, ArrayList v) {
        return updateTask(newStep, proc, oldStep, v);
    }

    /** modify a question */
    @Override
    public CopexReturn updateQuestion(Question newQuestion, ExperimentalProcedure proc, Question oldQuestion, ArrayList v) {
        return updateTask(newQuestion, proc, oldQuestion, v);
    }

     /** modify a task */
    @Override
    public CopexReturn updateTask(CopexTask newTask, ExperimentalProcedure proc, CopexTask oldTask, char undoRedo, ArrayList v) {
       return updateTask(newTask, proc, oldTask, v);
    }

    /**paste some tasks*/
    @Override
    public CopexReturn paste(ExperimentalProcedure proc, SubTree subTree, TaskSelected ts, char undoRedo, ArrayList v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** delete some tasks*/
    @Override
    public CopexReturn suppr(ArrayList<TaskSelected> listTs, ArrayList v, boolean suppr, char undoRedo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**cut some tasks*/
    @Override
    public CopexReturn cut(ExperimentalProcedure proc, SubTree subTree, TaskSelected ts) {
        return new CopexReturn();
    }

    /** copy some tasks*/
    @Override
    public CopexReturn copy(ExperimentalProcedure proc, TaskSelected ts, SubTree subTree) {
        return new CopexReturn();
    }

    /** paste */
    @Override
    public CopexReturn paste(ExperimentalProcedure proc, TaskSelected ts, SubTree subTree) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** cut- undo redo */
    @Override
    public CopexReturn cut(ArrayList<TaskSelected> listTs, SubTree subTree, ArrayList v, char undoRedo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /** create a new procedure */
    @Override
    public CopexReturn createProc(String procName, InitialProcedure initProc) {
        return new CopexReturn();
    }

    /** copy of a proc  */
    @Override
    public CopexReturn copyProc(String name, LearnerProcedure procToCopy) {
        return new CopexReturn();
    }

    /**open an existing proc */
    @Override
    public CopexReturn openProc(CopexMission missionToOpen, LearnerProcedure procToOpen) {
        return new CopexReturn();
    }

    /**close a proc */
    @Override
    public CopexReturn closeProc(ExperimentalProcedure proc) {
        if (proc.getDbKey() == -2){
            copex.closeProc(proc);
            copex.logCloseHelpProc();
            return new CopexReturn();
        }
        initProc.setOpen(false);
        // gui
        copex.closeProc((ExperimentalProcedure)proc.clone());
        return new CopexReturn();
    }

    /** delete a proc */
    @Override
    public CopexReturn deleteProc(ExperimentalProcedure proc) {
        initProc = null;
        // IHM
       copex.deleteProc((LearnerProcedure)proc.clone());
       return new CopexReturn();
    }

    /** returns the list of the proc to be copied or opened
     * v[0] list of proc that can be copied: list of the proc of the current mission
     * v[1] list of the missions of the user
     * v[2] list of the proc of each mission
     */
    @Override
    public CopexReturn getListProcToCopyOrOpen(ArrayList v) {
        v.add(new ArrayList());
        v.add(new ArrayList());
        v.add(new ArrayList());
        return new CopexReturn();
    }

    /**update the proc name*/
    @Override
    public CopexReturn updateProcName(ExperimentalProcedure proc, String name, char undoRedo) {
        return new CopexReturn();
    }

    /**update the current proc */
    @Override
    public CopexReturn setProcActiv(ExperimentalProcedure proc) {
        initProc.setActiv(true);
        if(dbMode){
            CopexReturn cr = ExperimentalProcedureFromDB.updateActivProcInDB(dbC, initProc.getDbKey(), true);
            if (cr.isError())
                return cr;
       }
        return new CopexReturn();
    }

    /**export pdf  */
    @Override
    public CopexReturn printCopex(ExperimentalProcedure procToPrint) {
        CopexMission missionToPrint  =(CopexMission)mission.clone();
        ExperimentalProcedure proc = null;
        if (procToPrint != null && procToPrint instanceof InitialProcedure){
            proc = (InitialProcedure)(initProc.clone());
        }else if (procToPrint != null && procToPrint instanceof LearnerProcedure){
            proc = helpProc ;
        }
        String fileName = "copex-"+mission.getCode();
        PrintPDF pdfPrint = new PrintPDF(copex, fileName, teacher, missionToPrint,proc);
        CopexReturn cr = pdfPrint.printDocument();
        return cr;
    }

    /**drag and drop */
    @Override
    public CopexReturn move(TaskSelected task, SubTree tree, char undoRedo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**drag and drop, finalize */
    @Override
    public CopexReturn finalizeDragAndDrop(ExperimentalProcedure proc) {
        copex.updateProc((InitialProcedure)initProc.clone());
        return new CopexReturn();
    }

    /** quit the procedure editor */
    @Override
    public CopexReturn stopEdP() {
        // save the tasks visibility
        return saveTaskVisible();
    }

    /* update the visibility of tasks */
    private CopexReturn saveTaskVisible(){
        if(dbMode){
            CopexReturn cr = TaskFromDB.updateTaskVisibleInDB(dbC, initProc.getListTask());
            if (cr.isError())
                return cr;
        }
        return new CopexReturn();
    }

    /** update the visible tasks */
    @Override
    public CopexReturn updateTaskVisible(ExperimentalProcedure p, ArrayList<CopexTask> listTask) {
        // update
        int nb = listTask.size();
        for (int i=0; i<nb; i++){
            int idT = getId(initProc.getListTask(), listTask.get(i).getDbKey());
            if (idT == -1)
                return new CopexReturn(copex.getBundleString("MSG_ERROR_TASK_VISIBLE"), false);
            initProc.getListTask().get(idT).setVisible(listTask.get(i).isVisible());
        }
        return new CopexReturn();
    }

    /**gets the help procedure */
    @Override
    public CopexReturn getHelpProc(ArrayList v) {
        v.add(helpMission.clone());
        v.add(helpProc.clone());
        ArrayList<Material> listHelpMaterialC = new ArrayList();
        int n = listHelpMaterial.size();
        for (int i=0; i<n; i++){
            listHelpMaterialC.add((Material)listHelpMaterial.get(i).clone());
        }
        v.add(listHelpMaterialC);
        return new CopexReturn();
    }

    /** open the help dialog*/
    @Override
    public CopexReturn openHelpDialog() {
        return new CopexReturn();
    }

    /** close the help dialog */
    @Override
    public CopexReturn closeHelpDialog() {
        return new CopexReturn();
    }

    /** open the help procedure */
    @Override
    public CopexReturn openHelpProc() {
        return new CopexReturn();
    }

    /** gets the ELO xproc experimentalProcedure */
    @Override
    public Element getExperimentalProcedure(ExperimentalProcedure proc) {
        return null;
    }

    /** load an ELO*/
    @Override
    public CopexReturn loadELO(Element xmlContent) {
        return new CopexReturn();
    }

    /** create a new ELO */
    @Override
    public CopexReturn newELO() {
        return new CopexReturn();
    }

    /** returns the parameters list of the actions of a step */
    @Override
    public CopexReturn getTaskInitialParam(ExperimentalProcedure proc, CopexTask task, ArrayList v) {
        v.add(proc.getTaskInitialParam(task));
        return new CopexReturn();
    }

    /** returns the output of the actions of a step */
    @Override
    public CopexReturn getTaskInitialOutput(ExperimentalProcedure proc, CopexTask task, ArrayList v) {
        v.add(proc.getTaskInitialOutput(task));
        return new CopexReturn();
    }

    /** returns the default material type */
    @Override
    public TypeMaterial getDefaultMaterialType() {
        return this.defaultTypeMaterial;
    }

    /** update the hypothesis of a proc*/
    @Override
    public CopexReturn setHypothesis(ExperimentalProcedure proc, Hypothesis hypothesis, ArrayList v) {
        if(dbMode && hypothesis == null){
            CopexReturn cr = ExperimentalProcedureFromDB.deleteHypothesisFromDB(dbC, initProc);
            if(cr.isError())
                return cr;
        }else{
            if(dbMode){
                if(hypothesis.getDbKey() == -1){
                    ArrayList v2 = new ArrayList();
                    CopexReturn cr = ExperimentalProcedureFromDB.createHypothesisInDB(dbC, getLocale(),initProc, hypothesis, v2);
                    if(cr.isError())
                        return cr;
                    hypothesis = (Hypothesis)v2.get(0);
                }else{
                    CopexReturn cr = ExperimentalProcedureFromDB.updateHypothesisInDB(dbC, getLocale(),hypothesis);
                    if(cr.isError())
                        return cr;
                }
            }else{
                if(hypothesis != null && hypothesis.getDbKey() != -1 ){
                     hypothesis.setDbKey(idHypothesis++);
                }
            }
        }
        initProc.setHypothesis(hypothesis);
        if(hypothesis != null)
            v.add(hypothesis.clone());
        return new CopexReturn();
    }

    /** update the general principle of the proc*/
    @Override
    public CopexReturn setGeneralPrinciple(ExperimentalProcedure proc, GeneralPrinciple principle, ArrayList v) {
        if(dbMode && principle == null){
            CopexReturn cr = ExperimentalProcedureFromDB.deleteGeneralPrincipleFromDB(dbC, initProc);
            if(cr.isError())
                return cr;
        }else{
            if(dbMode){
                if(principle.getDbKey() == -1){
                    ArrayList v2 = new ArrayList();
                    CopexReturn cr = ExperimentalProcedureFromDB.createGeneralPrincipleInDB(dbC, getLocale(),initProc, principle, v2);
                    if(cr.isError())
                        return cr;
                    principle = (GeneralPrinciple)v2.get(0);
                }else{
                    CopexReturn cr = ExperimentalProcedureFromDB.updateGeneralPrincipleInDB(dbC, getLocale(),principle);
                    if(cr.isError())
                        return cr;
                }
            }else{
                if(principle != null && principle.getDbKey() != -1 ){
                     principle.setDbKey(idGeneralPrinciple++);
                }
            }
        }
        initProc.setGeneralPrinciple(principle);
        if(principle != null)
            v.add(principle.clone());
        return new CopexReturn();
    }

    /** update the evaluation of the proc */
    @Override
    public CopexReturn setEvaluation(ExperimentalProcedure proc, Evaluation evaluation, ArrayList v) {
        if(dbMode && evaluation == null){
            CopexReturn cr = ExperimentalProcedureFromDB.deleteEvaluationFromDB(dbC, initProc);
            if(cr.isError())
                return cr;
        }else{
            if(dbMode){
                if(evaluation.getDbKey() == -1){
                    ArrayList v2 = new ArrayList();
                    CopexReturn cr = ExperimentalProcedureFromDB.createEvaluationInDB(dbC, getLocale(),initProc, evaluation, v2);
                    if(cr.isError())
                        return cr;
                    evaluation = (Evaluation)v2.get(0);
                }else{
                    CopexReturn cr = ExperimentalProcedureFromDB.updateEvaluationInDB(dbC, getLocale(),evaluation);
                    if(cr.isError())
                        return cr;
                }
            }else{
                if(evaluation != null && evaluation.getDbKey() != -1 ){
                     evaluation.setDbKey(idEvaluation++);
                }
            }
        }
        initProc.setEvaluation(evaluation);
        if(evaluation != null)
            v.add(evaluation.clone());
        return new CopexReturn();
    }

    /** update the material used*/
    @Override
    public CopexReturn setMaterialUsed(ExperimentalProcedure proc, ArrayList<MaterialUsed> listMaterialToCreate, ArrayList<MaterialUsed> listMaterialToDelete, ArrayList<MaterialUsed> listMaterialToUpdate, ArrayList v) {
        // creation of the new material
        int nbMat = listMaterialToCreate.size();
        for (int i=0; i<nbMat; i++){
            if(dbMode){
                ArrayList v2 = new ArrayList();
                CopexReturn cr = ExperimentalProcedureFromDB.createMaterialInDB(dbC,getLocale(), listMaterialToCreate.get(i).getMaterial(), proc.getDbKey(), v2);
                if(cr.isError()){
                    return cr;
                }
                Material m = (Material)v2.get(0);
                listMaterialToCreate.get(i).setMaterial(m);
            }else{
                listMaterialToCreate.get(i).getMaterial().setDbKey(idMaterial++);
            }
        }
        if(dbMode){
            CopexReturn cr = ExperimentalProcedureFromDB.createListMaterialUsedInDB(dbC, getLocale(), initProc.getDbKey(), listMaterialToCreate);
            if(cr.isError())
                return cr;
        }
        initProc.addListMaterialUsed(listMaterialToCreate);
        // remove the material
        if(dbMode){
            CopexReturn cr = ExperimentalProcedureFromDB.deleteMaterialUsedFromDB(dbC, initProc.getDbKey(), listMaterialToDelete);
            if(cr.isError())
                return cr;
        }
        initProc.deleteMaterialUsed(listMaterialToDelete);
        // update material
        if(dbMode){
            CopexReturn cr = ExperimentalProcedureFromDB.updateMaterialUsedInDB(dbC, getLocale(), initProc.getDbKey(), listMaterialToUpdate);
            if(cr.isError())
                return cr;
        }
        initProc.updateMaterialUsed(listMaterialToUpdate);
        ArrayList<MaterialUsed> listC = new ArrayList();
        int nb = initProc.getListMaterialUsed().size();
        for (int i=0; i<nb; i++){
            listC.add((MaterialUsed)initProc.getListMaterialUsed().get(i).clone());
        }
        v.add(listC);
        return new CopexReturn();
    }

    /** log a user action in the db*/
    @Override
    public CopexReturn logUserActionInDB(String type, List<CopexProperty> attribute) {
        return new CopexReturn();
    }

    /** returns if a proc is the proc of the labdoc  */
    @Override
    public CopexReturn isLabDocProc(ExperimentalProcedure p, ArrayList v) {
        v.add(false);
        return new CopexReturn();
    }


    private Locale getLocale(){
        return this.locale;
    }

    // read the config copex file
    private CopexReturn loadCopexConfig(){
        InputStreamReader fileReader = null;
        SAXBuilder builder = new SAXBuilder(false);
        //File file = new File((getClass().getResource( "/" +copexConfigFileName)).getFile());
        try{
            InputStream s = this.getClass().getClassLoader().getResourceAsStream("config/"+copexConfigFileName);
            //fileReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            fileReader = new InputStreamReader(s, "utf-8");
            //Document doc = builder.build(fileReader, file.getAbsolutePath());
            Document doc = builder.build(fileReader);
            Element copexConfig = doc.getRootElement();
            config = new CopexConfig(copexConfig, getLocale(), idMaterialStrategy, idPhysicalQtt, idUnit, idTypeMaterial, idMaterial, idQuantity, idAction, idActionParam, idOutput);
            //dbKeyProblem();
            // physical quantities
            //listPhysicalQuantity = config.getListQuantities();
            // material strategy
            //listMaterialStrategy = config.getListMaterialStrategy();
        }catch(IOException e1){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_LOAD_COPEX_CONFIG")+" "+e1, false);
        }catch(JDOMException e2){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_LOAD_COPEX_CONFIG")+" "+e2, false);
        }
        return new CopexReturn();
    }

    /*help procedure  */
    private CopexReturn loadHelpProc(){
        CopexReturn cr = loadCopexConfig();
        if(cr.isError())
            return cr;
        InputStreamReader fileReader = null;
        SAXBuilder builder = new SAXBuilder(false);
        //File file = new File((getClass().getResource( "/" +fileMission)).getFile());
        try{
            //fileReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            //Document doc = builder.build(fileReader, file.getAbsolutePath());
            InputStream s = this.getClass().getClassLoader().getResourceAsStream("languages/copexHelpProc.xml");
            fileReader = new InputStreamReader(s, "utf-8");
            Document doc = builder.build(fileReader);
            Element element = doc.getRootElement();
            helpMission = new CopexMission(element.getChild(CopexMission.TAG_MISSION), getLocale(), idMission++, idProc++, idRepeat++, idParam++, idValue++,idAction++,idActionParam++, idQuantity++, idMaterial++, idTask++, idHypothesis++, idGeneralPrinciple++, idEvaluation++, idTypeMaterial++, idInitialAction++,idOutput++,
                  config.getListMaterial(), config.getListTypeMaterial(), listPhysicalQuantity, config.getListInitialNamedAction(), config.getListMaterialStrategy() );
            helpMission.setDbKey(-2);
            // load proc
            helpProc = new LearnerProcedure(element.getChild(LearnerProcedure.TAG_LEARNER_PROC), helpMission,
                    idProc++, idRepeat++, idParam++, idValue++, idActionParam++, idQuantity++, idMaterial++, idTask++,
                    idHypothesis++, idGeneralPrinciple++, idEvaluation++,
                    helpMission.getListInitialProc(), helpMission.getListMaterial(),helpMission.getListType(),  listPhysicalQuantity);
            //dbKeyProblem();
            helpProc.setRight(MyConstants.NONE_RIGHT);
            helpProc.setDbKey(-2);
            listHelpMaterial = helpProc.getInitialProc().getListMaterial();
        }catch(IOException e1){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_LOAD_COPEX_MISSION")+" "+e1, false);
        }catch(JDOMException e2){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_LOAD_COPEX_MISSION")+" "+e2, false);
        }
        return new CopexReturn();
    }

    private ArrayList<MaterialUsed> getListMaterialUsed(LearnerProcedure proc){
        if(proc == null)
            return new ArrayList();
        ArrayList<MaterialUsed> listMaterialUsed = new ArrayList();
        MaterialStrategy strategy = proc.getInitialProc().getMaterialStrategy();
        ArrayList<Material> listMaterialProc = proc.getInitialProc().getListMaterial();
        int nbMat = listMaterialProc.size();
        for (int k=0; k<nbMat; k++){
            MaterialUsed matUsed = new MaterialUsed(listMaterialProc.get(k), CopexUtilities.getLocalText("", getLocale()), !strategy.canChooseMaterial());
            listMaterialUsed.add(matUsed);
        }
        return listMaterialUsed;
    }

    /** returns the copex url */
    @Override
    public URL getCopexURL(){
        return null;
    }

    /** get export in html format */
    @Override
    public CopexReturn getPreview(ExperimentalProcedure p, ArrayList v){
        return new CopexReturn();
    }

    /* returns the material list for a proc*/
    @Override
    public List<Material> getListMaterial(ExperimentalProcedure p){
        if(p instanceof InitialProcedure){
            return ((InitialProcedure)p).getListMaterial();
        }else {
            return ((LearnerProcedure)p).getInitialProc().getListMaterial();
        }
    }

    /** returns the id material*/
    @Override
    public long getIdMaterial(){
        return this.idMaterial;
    }

    /**returns the id quantity */
    @Override
    public long getIdQuantity(){
        return this.idQuantity;
    }

    /** returns the list of physical quantity*/
    @Override
    public List<PhysicalQuantity> getListPhysicalQuantity(){
        return this.listPhysicalQuantity;
    }

    /** returns the list of material type */
    @Override
    public List<TypeMaterial> getListTypeMaterial(){
        return this.config.getListTypeMaterial();
    }
}
