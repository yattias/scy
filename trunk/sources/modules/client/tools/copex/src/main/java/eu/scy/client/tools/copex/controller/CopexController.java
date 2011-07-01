/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.controller;


import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.dnd.SubTree;
import eu.scy.client.tools.copex.main.CopexPanel;
import eu.scy.client.tools.copex.edp.TaskSelected;
import eu.scy.client.tools.copex.logger.CopexProperty;
import eu.scy.client.tools.copex.logger.TaskTreePosition;
import eu.scy.client.tools.copex.print.PrintPDF;
import eu.scy.client.tools.copex.utilities.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import java.net.URL;

/**
 * le 03/03/09 : many initial proc linked to a mission
 * copex controller, for scy or standalone app
 * @author Marjolaine
 */
public class CopexController implements ControllerInterface {
    private final static boolean mission1= true;

    /*locale */
    private Locale locale;
    /* copex */
    private CopexPanel copex;
    /* user group - labbook */
    private long dbKeyGroup;
    /* user - labbook */
    private CopexGroup group;
    private long dbKeyUser;
    private long dbKeyLabDoc;
    /*list of the physical quantities in COPEX  */
    private List<PhysicalQuantity> listPhysicalQuantity ;
    /* list of material strategy in Copex */
    private List<MaterialStrategy> listMaterialStrategy;
    // main mission
    private CopexMission mission;
    // list of experimental proc. open
    private ArrayList<LearnerProcedure> listProc = null;
    /* list of missions of the user */
    private ArrayList<CopexMission> listMissionsUser = null;
    /* list of proc /mission of the user */
    private ArrayList<ArrayList<LearnerProcedure>> listProcMissionUser = null;
    /*mission file name */
    private String fileMission;
    private CopexConfig config;
    
    /*help mission */
    private CopexMission helpMission;
    /*material list help */
    private ArrayList<Material> listHelpMaterial;
    /* help proc*/
    private LearnerProcedure helpProc;
    private boolean openQuestionDialog;

    // material type by default
    private TypeMaterial defaultMaterialType;

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

    private String copexConfigFileName = "copex.xml";


    public CopexController(CopexPanel copex) {
        this.copex = copex;
    }

    /* loggin? */
    private boolean setTrace(){
        if(this.mission == null)
                return false;
        return true;
    }

    private Locale getLocale(){
        return copex.getLocale();
    }

    /* initialization of the application, load data*/
    @Override
    public CopexReturn initEdP(Locale locale, String idUser, long dbKeyMission, long dbKeyGroup, long dbKeyLabDoc, String labDocName, String fileMission) {
        String msgError = "";
        this.locale = locale;
        this.fileMission = fileMission;
        this.dbKeyUser = 1;
        // load parameters
        // read copex file config
        CopexReturn cr = loadCopexConfig();
        if(cr.isError())
            return cr;
        // load material type
        //defaultMaterialType = new TypeMaterial(idTypeMaterial++, CopexUtilities.getLocalText(copex.getBundleString("DEFAULT_TYPE_MATERIAL"), getLocale()));
        for(Iterator<TypeMaterial> type = config.getListTypeMaterial().iterator();type.hasNext();){
            TypeMaterial t = type.next();
            if(t.getCode().equals("TYPE_DEFAULT")){
                defaultMaterialType = t;
                break;
            }
        }
        // load user : name, first name
        group = new CopexGroup(1, new LinkedList());

        // load mission
        cr = loadCopexMission();
        if(cr.isError())
            return cr;
        List<InitialProcedure> listInitialProc = mission.getListInitialProc();
        
        listProc = new ArrayList();
        boolean allProcLocked = false;
        // load datasheet : none for the initial proc
        
        // laod missions and proc of the user
        this.listMissionsUser = new ArrayList();
        this.listProcMissionUser = new ArrayList();
        cr = loadHelpProc();
        if (cr.isError()){
            msgError += cr.getText();
        }
        
        // result
        cr = new CopexReturn();
        if(msgError.length() > 0)
            return  new CopexReturn(msgError, false);

        //logging
        if (setTrace()){
            copex.logStartTool();
        }
        // objects clone
        CopexMission m = (CopexMission)mission.clone();
        int nbP = listProc.size();
        ArrayList<ExperimentalProcedure> listP = new ArrayList();
        for (int k=0; k<nbP; k++)
            listP.add((LearnerProcedure)listProc.get(k).clone());
        int nbIP = listInitialProc.size();
        
        ArrayList<PhysicalQuantity> listPhysicalQuantityC = new ArrayList();
        int nbPhysQ = this.listPhysicalQuantity.size();
        for (int k=0; k<nbPhysQ; k++){
            listPhysicalQuantityC.add((PhysicalQuantity)this.listPhysicalQuantity.get(k).clone());
        }
        copex.initEdp(m, listP, listPhysicalQuantityC);
        // if there is no proc, create
        // MBo - 27/02/2009 : if all proc. of the mission are locked => don't create a new
        boolean askForInitProc = false;
        if (listProc.isEmpty() && !allProcLocked){
            if (nbIP == 1){
                cr = createProc(listInitialProc.get(0).getName(getLocale()), listInitialProc.get(0), false );
                if (cr.isError()){
                    msgError += cr.getText();
                }
            }else{
                askForInitProc = true;
            }
        }
        if (listProc.size() > 0)
            printRecap(listProc.get(0));
        ArrayList<String> listProcLocked = new ArrayList();
        copex.displayProcLocked(listProcLocked);
        if (askForInitProc)
            copex.askForInitialProc();
        return cr;
    }

    
    private boolean canAddProc(){
        if(mission == null)
            return true;
        return true;
    }
   

    /* load proc and mission */
    private CopexReturn loadProc(LearnerProcedure p, boolean controlLock, ArrayList v){
        return new CopexReturn();

    }


    

     /* cut /undo-redo*/
    @Override
    public CopexReturn cut(ArrayList<TaskSelected> listTs, SubTree subTree, ArrayList v,char undoRedo){
        CopexReturn cr = suppr(listTs, v, false, undoRedo);
        if (cr.isError())
            return cr;
        if (setTrace()){
            ExperimentalProcedure proc = (listTs.get(0)).getProc() ;
            ArrayList<CopexTask> listTask = subTree.getListTask();
            List<TaskTreePosition> listPositionTask = getTaskPosition(proc,listTask);
            copex.logRedoCut(proc, listTask, listPositionTask);
        }
        return cr;
    }


    private List<TaskTreePosition> getTaskPosition(ExperimentalProcedure proc, ArrayList<CopexTask> listTask){
        List<TaskTreePosition> listPositionTask = new LinkedList();
        int nbT = listTask.size();
        for (int i=0; i<nbT; i++){
            listPositionTask.add(proc.getTaskTreePosition(listTask.get(i)));
        }
        return listPositionTask;
    }
    

    
    @Override
    public CopexReturn paste(ExperimentalProcedure proc, SubTree subTree, TaskSelected ts, char undoRedo, ArrayList v2) {
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_PASTE"), false);
        LearnerProcedure expProc = listProc.get(idP);
        
        ArrayList<CopexTask> listTask = subTree.getListTask();
        int nbTa = listTask.size();
        ArrayList<CopexTask> listTaskC = new ArrayList();
        for (int i=0; i<nbTa; i++){
            listTaskC.add((CopexTask)listTask.get(i).clone());
        }
        // create task in db
        int nbT = listTaskC.size();
        for (int k=0; k<nbT; k++){
            if (!listTaskC.get(k).isQuestionRoot()){
                long oldDbKey = listTaskC.get(k).getDbKey();
                CopexTask task = listTaskC.get(k);
                boolean isActionSetting = task instanceof CopexActionChoice || task instanceof CopexActionAcquisition || task instanceof CopexActionManipulation || task instanceof CopexActionTreatment ;
                boolean isNamedAction = task instanceof CopexActionNamed || isActionSetting ;
                int nbparam = isActionSetting ? ((CopexActionParam)task).getNbParam() :0;
                if (isNamedAction && isActionSetting){
                    for (int j=0; j<nbparam; j++){
                        if(((CopexActionParam)task).getTabParam()[j] instanceof ActionParam){
                            ((ActionParam)((CopexActionParam)task).getTabParam()[j]).setDbKey(idParam++);
                        }else if (((CopexActionParam)task).getTabParam()[j] instanceof ArrayList){
                            ArrayList<ActionParam> p = (ArrayList<ActionParam>)((CopexActionParam)task).getTabParam()[j];
                            int np = p.size();
                            for (int m=0;m<np; m++){
                                p.get(m).setDbKey(idParam++);
                            }
                        }
                    }
                    if(task instanceof CopexActionManipulation){
                        ArrayList<Object> listM = ((CopexActionManipulation)task).getListMaterialProd() ;
                        int nbM = listM.size();
                        for (int i=0; i<nbM; i++){
                            if(listM.get(i) instanceof Material){
                                Material m = (Material)listM.get(i);
                                m.setDbKey(idMaterial++);
                                // create parameters
                                int nbParam = m.getListParameters().size();
                                for (int j=0; j<nbParam; j++){
                                    ((Material)listM.get(i)).getListParameters().get(j).setDbKey(idQuantity++);
                                }
                            }else if (listM.get(i) instanceof ArrayList){
                                int n = ((ArrayList)listM.get(i)).size();
                                for (int r=0; r<n; r++){
                                    Material m = ((ArrayList<Material>)listM.get(i)).get(r);
                                    m.setDbKey(idMaterial++);
                                    // create parameters
                                    int nbParam = m.getListParameters().size();
                                    for (int j=0; j<nbParam; j++){
                                        ((ArrayList<Material>)listM.get(i)).get(r).getListParameters().get(j).setDbKey(idQuantity++);
                                    }
                                }
                            }
                        }
                    }else if (task instanceof CopexActionAcquisition){
                        ArrayList<Object> listD = ((CopexActionAcquisition)task).getListDataProd() ;
                        int nbD = listD.size() ;
                        for (int i=0; i<nbD; i++){
                            if(listD.get(i) instanceof QData){
                                QData d = (QData)listD.get(i);
                                d.setDbKey(idQuantity++);
                            }else if (listD.get(i) instanceof ArrayList){
                                int n = ((ArrayList)listD.get(i)).size();
                                for (int r=0; r<n; r++){
                                    QData d = ((ArrayList<QData>)listD.get(i)).get(r);
                                    d.setDbKey(idQuantity++);
                                }
                            }
                        }
                    }else if (task instanceof CopexActionTreatment){
                        ArrayList<Object> listD = ((CopexActionTreatment)task).getListDataProd() ;
                        int nbD = listD.size() ;
                        for (int i=0; i<nbD; i++){
                            if(listD.get(i) instanceof QData){
                                QData d = (QData)listD.get(i);
                                d.setDbKey(idQuantity++);
                            }else if (listD.get(i) instanceof ArrayList){
                                int n = ((ArrayList)listD.get(i)).size();
                                for (int r=0; r<n; r++){
                                    QData d = ((ArrayList<QData>)listD.get(i)).get(r);
                                    d.setDbKey(idQuantity++);
                                }
                            }
                        }
                    }
                }
                // update identifiers
                long dbKey = idTask++ ;
                listTaskC.get(k).setDbKey(dbKey);
                if ( proc.getQuestion().getDbKey() == oldDbKey)
                    proc.getQuestion().setDbKey(dbKey);
                // list
                for (int m=0; m<nbT; m++){
                    CopexTask ct = listTaskC.get(m);
                    if (ct.getDbKeyBrother() == oldDbKey)
                        ct.setDbKeyBrother(dbKey);
                    else if (ct.getDbKeyChild() == oldDbKey)
                        ct.setDbKeyChild(dbKey);
                    if (proc.getQuestion().getDbKeyChild() == oldDbKey)
                        proc.getQuestion().setDbKeyChild(dbKey);

                }
            }
        }


        expProc.addTasks(listTaskC);
        updateDatasheetProd(expProc);
        expProc.lockMaterialUsed();
        // update modification date
        CopexReturn cr = updateDateProc(expProc);
        if (cr.isError()){
            return cr;
        }
        
        // connect the subtree to the proc., reconnecting possibliy links of the selected task
        CopexTask taskBranch = listTaskC.get(0);
        CopexTask lastTaskBranch = listTaskC.get(subTree.getIdLastTask());
        CopexTask taskBrother = ts.getTaskBrother();
        CopexTask taskParent = ts.getTaskParent();
        int idB = -1;
        long dbKeyT ;
        if (taskBrother == null)
            dbKeyT = taskParent.getDbKey();
        else
            dbKeyT = taskBrother.getDbKey();
        
        idB = getId(expProc.getListTask(), dbKeyT);
        int idLTB = getId(expProc.getListTask(), lastTaskBranch.getDbKey());
       if (taskBrother == null){
            // connect as parent
            taskParent.setDbKeyChild(taskBranch.getDbKey());
            // update in the list
            expProc.getListTask().get(idB).setDbKeyChild(taskBranch.getDbKey());
        }else{
            long dbKeyOldBrother = taskBrother.getDbKeyBrother();
            if (dbKeyOldBrother != -1)
                lastTaskBranch.setDbKeyBrother(dbKeyOldBrother);
            taskBrother.setDbKeyBrother(taskBranch.getDbKey());
            //update in the list
            expProc.getListTask().get(idB).setDbKeyBrother(taskBranch.getDbKey());
            if (dbKeyOldBrother != -1){
                expProc.getListTask().get(idLTB).setDbKeyBrother(dbKeyOldBrother);
            }
        }
        // logging
        if (setTrace()){
            List<TaskTreePosition> listPosition = getTaskPosition(expProc, listTaskC);
            if (undoRedo == MyConstants.NOT_UNDOREDO)
                copex.logPaste(proc,listTaskC, listPosition);
            else if (undoRedo == MyConstants.REDO)
                copex.logRedoPaste(proc, listTask, listPosition);
        }
        //gui
        ArrayList<CopexTask> listTC = new ArrayList();
        for (int k=0; k<nbT;k++){
            listTC.add((CopexTask)listTaskC.get(k).clone());
        }
        copex.paste((LearnerProcedure)proc.clone(), listTC, ts, undoRedo);
        v2.add(listTC);
        return new CopexReturn();
    }

    
    /* delete selected tasks
     * the root can't be deleted, even it's editable
     * delete the childs, if they exist
     */ 
    @Override
    public CopexReturn suppr(ArrayList<TaskSelected> listTs, ArrayList v, boolean suppr, char undoRedo) {
        // list of tasks to remove
        LearnerProcedure proc = getProc(listTs);
        int idPr = getIdProc(proc.getDbKey());
        if (idPr == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_SUPPR_ROOT"), false);
        LearnerProcedure expProc = listProc.get(idPr);
        printRecap(expProc);
        ArrayList<CopexTask> listTask = getListTask(listTs);
        // control the root
        int nbT = listTask.size();
        for (int i=0;i<nbT; i++){
            CopexTask t = listTask.get(i);
            if (t.isQuestionRoot())
                return new CopexReturn(copex.getBundleString("MSG_ERROR_SUPPR_ROOT"), false);
        }

        List<TaskTreePosition> listPositionTask = getTaskPosition(expProc, listTask);
        // update modification date
        CopexReturn cr = updateDateProc(expProc);
        if (cr.isError()){
            return cr;
        }
        // delete links
        int nbTP = expProc.getListTask().size();
        for (int k=0; k<nbTP; k++){
            long dbKeyB = expProc.getListTask().get(k).getDbKeyBrother();
            long dbKeyP = expProc.getListTask().get(k).getDbKeyChild();
            for (int i=0;i<nbT; i++){
                if (listTask.get(i).getDbKey() == dbKeyB)
                    expProc.getListTask().get(k).setDbKeyBrother(-1);
                if (listTask.get(i).getDbKey() == dbKeyP)
                    expProc.getListTask().get(k).setDbKeyChild(-1);
            }
        }
        // update links (db)
       int nbTs = listTs.size();
        ArrayList<CopexTask> listToUpdateLinkChild = new ArrayList();
        ArrayList<CopexTask> listToUpdateLinkBrother = new ArrayList();
        for (int i=0; i<nbTs; i++){
            TaskSelected ts = listTs.get(i);
            long dbKeyBrother = ts.getSelectedTask().getDbKeyBrother();
            if (dbKeyBrother != -1){
                // connect the brother to the old brother or parent if doesn't exist
                CopexTask taskBrother = ts.getTaskOldBrother();
                if (taskBrother ==null){
                    // parent
                    CopexTask taskParent = ts.getParentTask();
                    int idP = getId(expProc.getListTask(), taskParent.getDbKey());
                    if (idP == -1){
                        return new CopexReturn(copex.getBundleString("MSG_ERROR_DELETE_TASK"), false);
                    }else{
                        CopexTask tp = expProc.getListTask().get(idP);
                        tp.setDbKeyChild(dbKeyBrother);
                        listToUpdateLinkChild.add(tp);
                    }
                }else{
                    int idB = getId(expProc.getListTask(), taskBrother.getDbKey());
                    if (idB == -1){
                        return new CopexReturn(copex.getBundleString("MSG_ERROR_DELETE_TASK"), false);
                    }else{
                        CopexTask tb = expProc.getListTask().get(idB);
                        tb.setDbKeyBrother(dbKeyBrother);
                        listToUpdateLinkBrother.add(tb);
                    }
                }
            }
        }
        updateQuestion(proc);
        // update data
        boolean isOk = expProc.deleteTasks(listTask);
        if (!isOk){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_DELETE_TASK"), false);
        }
        updateDatasheetProd(expProc);
        expProc.lockMaterialUsed();
        // logging
        if (setTrace()){
            if (undoRedo == MyConstants.NOT_UNDOREDO && suppr)
                copex.logDelete(proc,listTask, listPositionTask);
            else if (undoRedo == MyConstants.UNDO){
                if (suppr)
                    copex.logUndoAddTask(proc, listTask, listPositionTask);
                else
                    copex.logUndoPaste(proc, listTask, listPositionTask);
            }
            else if (undoRedo == MyConstants.REDO && suppr){
                copex.logRedoDeleteTask(proc, listTask, listPositionTask);
            }
        }
        printRecap(expProc);
        v.add(expProc.clone());
        return new CopexReturn();
    }

    private void updateQuestion(LearnerProcedure proc){
        CopexTask question = proc.getQuestion();
        int nbT = proc.getListTask().size();
        for(int i=0; i<nbT; i++){
            if(proc.getListTask().get(i).getDbKey() == question.getDbKey()){
                question.setDbKeyBrother(proc.getListTask().get(i).getDbKeyBrother());
                question.setDbKeyChild(proc.getListTask().get(i).getDbKeyChild());
            }
        }
    }



    /* define the tasks list : selected tasks +childs  d
     */
    private ArrayList<CopexTask> getListTask(ArrayList<TaskSelected> listTs){
        ExperimentalProcedure proc = getProc(listTs);
        ArrayList<CopexTask> listT = new ArrayList();
        int nbTs = listTs.size();
        for (int t=0; t<nbTs; t++){
            CopexTask task = listTs.get(t).getSelectedTask();
            listT.add((CopexTask)task.clone());
            if (task instanceof Step || task instanceof Question){
                // get childs
               ArrayList<CopexTask> lc = listTs.get(t).getListAllChildren();
               // add
               int n = lc.size();
               for (int k=0; k<n; k++){
                   // add
                   listT.add(lc.get(k));
               }
            }
            
            
        }  
        // remove multi-occurence
        int nbT = listT.size();
        ArrayList<CopexTask> lt = new ArrayList();
        ArrayList<CopexTask> listTClean = new ArrayList();
        for (int t=0; t<nbT; t++){
            int id = getId(lt, listT.get(t).getDbKey());
            if (id == -1){
                listTClean.add(listT.get(t));
            }
            lt.add(listT.get(t));
        }
        return listTClean;
    }
    
    
    
    /* returns the proc (proc of the first task)
     */
    private LearnerProcedure getProc(ArrayList<TaskSelected> listTs){
        ExperimentalProcedure proc = null;
        TaskSelected ts = listTs.get(0);
        proc = ts.getProc();
        return (LearnerProcedure)proc;
    }

    /* search id in the list, -1 if not found */
    private int getId(List<CopexTask> listT, long dbKey){
        int nbT = listT.size();
        for (int i=0; i<nbT; i++){
            CopexTask t = listT.get(i);
            if (t.getDbKey() == dbKey)
                return i;
        }
        return -1;
    }
   
    
    /* search id proc in the list, -1 if not found */
    private int getIdProc(long dbKey){
        return getIdProc(listProc, dbKey);
    }
    
    /* search id proc in the list, -1 if not found */
    private int getIdProc(ArrayList<LearnerProcedure> listP, long dbKey){
        int nbT = listP.size();
        for (int i=0; i<nbT; i++){
            LearnerProcedure p = listP.get(i);
            if (p.getDbKey() == dbKey)
                return i;
        }
        return -1;
    }
    
    /* add new action : v[0] the new proc.*/
    @Override
    public CopexReturn addAction(CopexAction action, ExperimentalProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v ) {
        CopexReturn cr =  addTask(action, proc, taskBrother, taskParent, v, MyConstants.NOT_UNDOREDO, false);
        if (cr.isError())
            return cr;
        // logging
        if (setTrace()){
            TaskTreePosition position = (TaskTreePosition)v.get(1);
            copex.logAddAction(proc, action, position);
        }
        return cr;
    }

    /*update action */
    @Override
    public CopexReturn updateAction(CopexAction newAction, ExperimentalProcedure proc, CopexAction oldAction, ArrayList v) {
        CopexReturn cr = updateTask(newAction, proc, oldAction, v);
        if (cr.isError())
            return cr;
        if (setTrace()){
            copex.logUpdateAction(proc, oldAction, newAction);
        }
        return cr;
    }

    /* add new step */
    @Override
    public CopexReturn addStep(Step step, ExperimentalProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v) {
        CopexReturn cr =  addTask(step, proc, taskBrother, taskParent,v, MyConstants.NOT_UNDOREDO, false);
        if (cr.isError())
            return cr;
        // logging
        if (setTrace()){
            TaskTreePosition position = (TaskTreePosition)v.get(1);
            copex.logAddStep(proc, step, position);
        }
        return cr;
    }

    /* update step */
    @Override
    public CopexReturn updateStep(Step newStep, ExperimentalProcedure proc, Step oldStep, ArrayList v) {
        CopexReturn cr =  updateTask(newStep, proc, oldStep, v);
        if (cr.isError())
            return cr;
        if (setTrace()){
            copex.logUpdateStep(proc, oldStep, newStep);
        }
        return cr;
    }

    
    

    /* update question*/
    @Override
    public CopexReturn updateQuestion(Question newQuestion, ExperimentalProcedure proc, Question oldQuestion, ArrayList v) {
        CopexReturn cr =  updateTask(newQuestion, proc, oldQuestion, v);
        if (cr.isError())
            return cr;
        if (setTrace()){
            copex.logUpdateQuestion(proc, oldQuestion, newQuestion);
        }
        return cr;
    }
    
    /* returns the parent task, null otherwise */
    private CopexTask getParentTask(List<CopexTask> listT, CopexTask task){
        if (task == null)
            return null;
        if (task.isQuestionRoot())
            return null;
        long dbKey = task.getDbKey();
        int nbT = listT.size();
        for (int i=0; i<nbT; i++){
            if (listT.get(i).getDbKeyChild() == dbKey){
                return listT.get(i);
            }
        }
        //not found => serarch in the old brother
        return getParentTask(listT, getOldBrotherTask(listT, task));
    }

    /*returns the old brother */
    private CopexTask getOldBrotherTask(List<CopexTask> listT, CopexTask task){
        long dbKey = task.getDbKey();
        int nbT = listT.size();
        for (int i=0; i<nbT; i++){
            if (listT.get(i).getDbKeyBrother() == dbKey){
                return listT.get(i);
            }
        }
        return null;
    }
    
    
    /* add a task*/
    @Override
    public CopexReturn addTask(CopexTask task, ExperimentalProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v, char undoRedo, boolean cut) {
        int idP = getIdProc(proc.getDbKey());
        if (idP ==  -1){
            String msg = copex.getBundleString("MSG_ERROR_ADD_STEP");
            if (task instanceof CopexAction)
                msg = copex.getBundleString("MSG_ERROR_ADD_ACTION");
            else if (task instanceof Question)
                msg = copex.getBundleString("MSG_ERROR_ADD_QUESTION");
            return new CopexReturn(msg, false);
        }
        LearnerProcedure expProc = listProc.get(idP);
        task.setDbKeyChild(-1);
        task.setDbKeyBrother(-1);
        // search in the list , the id of the brother task
        int idB = -1;
        long dbKeyT ;
        if (taskBrother == null)
            dbKeyT = taskParent.getDbKey();
        else
            dbKeyT = taskBrother.getDbKey();
        
        idB = getId(expProc.getListTask(), dbKeyT);
        
        if (idB == -1){
            String msg = copex.getBundleString("MSG_ERROR_ADD_STEP");
            if (task instanceof CopexAction)
                msg = copex.getBundleString("MSG_ERROR_ADD_ACTION");
            else if (task instanceof Question)
                msg = copex.getBundleString("MSG_ERROR_ADD_QUESTION");
            return new CopexReturn(msg, false);
        }

        //save in db and gets the new id
        ArrayList v2 = new ArrayList();
        CopexReturn cr;
       // update modification date
        cr = updateDateProc(expProc);
        if (cr.isError()){
            return cr;
        }
        
        long newDbKey = idTask++;
        // update data
        task.setDbKey(newDbKey);
        if (taskBrother == null){
            long oldFC = listProc.get(idP).getListTask().get(idB).getDbKeyChild();
            taskParent.setDbKeyChild(newDbKey);
            // update in the list
            //proc.getListTask().get(idB).setDbKeyChild(newDbKey);
            listProc.get(idP).getListTask().get(idB).setDbKeyChild(newDbKey);
            if (listProc.get(idP).getListTask().get(idB).getDbKey() == listProc.get(idP).getQuestion().getDbKey()){
                listProc.get(idP).getQuestion().setDbKeyChild(newDbKey);
                proc.getQuestion().setDbKeyChild(newDbKey);
            }
            if(oldFC != -1){
                task.setDbKeyBrother(oldFC);
            }
        }else{
            long dbKeyOldBrother = listProc.get(idP).getListTask().get(idB).getDbKeyBrother();
            task.setDbKeyBrother(dbKeyOldBrother);
            taskBrother.setDbKeyBrother(newDbKey);
            // update in the list
            expProc.getListTask().get(idB).setDbKeyBrother(newDbKey);
        }
        TaskRight taskRight = new TaskRight(MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT);
        if (task instanceof CopexActionChoice){
            CopexActionChoice a = new CopexActionChoice(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(), task.getDraw(), true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), ((CopexActionChoice)task).getTabParam(), task.getTaskRepeat());
            expProc.addAction(a);
        }else if (task instanceof CopexActionAcquisition){
            CopexActionAcquisition a = new CopexActionAcquisition(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(), task.getDraw(),true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), ((CopexActionAcquisition)task).getTabParam(), ((CopexActionAcquisition)task).getListDataProd(), task.getTaskRepeat());
            expProc.addAction(a);
        }else if (task instanceof CopexActionManipulation){
           CopexActionManipulation a = new CopexActionManipulation(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(), task.getDraw(),true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), ((CopexActionManipulation)task).getTabParam(), ((CopexActionManipulation)task).getListMaterialProd(), task.getTaskRepeat());
           expProc.addAction(a);
        }else if (task instanceof CopexActionTreatment){
            CopexActionTreatment a = new CopexActionTreatment(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(), task.getDraw(),true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), ((CopexActionTreatment)task).getTabParam(), ((CopexActionTreatment)task).getListDataProd(), task.getTaskRepeat());
            expProc.addAction(a);
        }else if (task instanceof CopexActionNamed){
            CopexActionNamed a = new CopexActionNamed(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(),task.getDraw(),  true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), task.getTaskRepeat());
            expProc.addAction(a);
        }else if (task instanceof CopexAction){
            CopexAction a = new CopexAction(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(), task.getDraw(), true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), task.getTaskRepeat());
            expProc.addAction(a);
        }else if (task instanceof Step){
            Step s = new Step(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(),task.getDraw(),true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), task.getTaskRepeat());
            expProc.addStep(s);
        }else if (task instanceof Question){
            Question q = new Question(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(),task.getDraw(),  true, taskRight, false, task.getDbKeyBrother(), task.getDbKeyChild() );
            expProc.addQuestion(q);
        }
        updateDatasheetProd(expProc);
        expProc.lockMaterialUsed();
        // v[0] ne wproc
        v.add((LearnerProcedure)expProc.clone());
        if (setTrace()){
//            long dbKeyParent = -1;
//            String parentD = "";
//            long dbKeyOldBrother = -1;
//            String brotherD  = null;
//            CopexTask tp = getParentTask(expProc.getListTask(), task);
//            if (tp != null){
//                dbKeyParent = tp.getDbKey();
//                parentD = tp.getDescription(getLocale());
//            }
//            CopexTask bt = getOldBrotherTask(expProc.getListTask(), task);
//            if (bt != null){
//                dbKeyOldBrother = bt.getDbKey();
//                brotherD = bt.getDescription(getLocale());
//            }
//            MyTask xmlTask = new MyTask(newDbKey, task.getDescription(getLocale()), dbKeyParent, parentD, dbKeyOldBrother, brotherD);
            TaskTreePosition position = listProc.get(idP).getTaskTreePosition(task);
            if (undoRedo == MyConstants.REDO){
                if (task.isAction()){
                    copex.logRedoAddAction(proc,(CopexAction)task, position);
                }else if (task.isStep()){
                    copex.logRedoAddStep(proc,(Step)task, position);
                }
            }else if (undoRedo == MyConstants.UNDO){
                if (cut){
                    copex.logUndoCut(proc, task, position);
                }else{
                    copex.logUndoDeleteTask(proc, task, position);
                }
            }
            v.add(position);
        }
        printRecap(expProc);
        
        return new CopexReturn();
    }

     /*task modification */
    @Override
    public CopexReturn updateTask(CopexTask newTask, ExperimentalProcedure proc, CopexTask oldTask, char undoRedo, ArrayList v) {
        CopexReturn cr = updateTask(newTask, proc, oldTask, v);
        if (cr.isError())
            return cr;
        if (setTrace()){
            if (undoRedo == MyConstants.UNDO){
                if (oldTask instanceof Step)
                    copex.logUndoEditStep(proc, (Step)oldTask, (Step)newTask);
                else if (oldTask instanceof CopexAction)
                    copex.logUndoEditAction(proc, (CopexAction)oldTask, (CopexAction)newTask);
                else if (oldTask instanceof Question)
                    copex.logUndoEditQuestion(proc, (Question)oldTask, (Question)newTask);
            }else{ // REDO
                if (oldTask instanceof Step)
                    copex.logRedoEditStep(proc, (Step)oldTask, (Step)newTask);
                else if (oldTask instanceof CopexAction)
                    copex.logRedoEditAction(proc, (CopexAction)oldTask, (CopexAction)newTask);
                else if (oldTask instanceof Question)
                    copex.logRedoEditQuestion(proc, (Question)oldTask, (Question)newTask);
            }
        }
        return new CopexReturn();
    }
    
    
    /* udpate task */
    public CopexReturn updateTask(CopexTask newTask, ExperimentalProcedure proc, CopexTask oldTask, ArrayList v) {
       int idP = getIdProc(proc.getDbKey());
       if (idP == -1){
           String msg = copex.getBundleString("MSG_ERROR_UPDATE_STEP");
           if (newTask instanceof CopexAction)
               msg = copex.getBundleString("MSG_ERROR_UPDATE_ACTION");
           else if (newTask instanceof Question)
               msg = copex.getBundleString("MSG_ERROR_UPDATE_QUESTION");
           return new CopexReturn(msg, false);
       }
       LearnerProcedure expProc = listProc.get(idP);
        // search in the tasks list, the id of the old task
        int idOld = getId(expProc.getListTask(), oldTask.getDbKey());
        if (idOld == -1){
            String msg = copex.getBundleString("MSG_ERROR_UPDATE_STEP");
            if (newTask instanceof CopexAction)
                msg = copex.getBundleString("MSG_ERROR_UPDATE_ACTION");
            else if (newTask instanceof Question)
                msg = copex.getBundleString("MSG_ERROR_UPDATE_QUESTION");
            return new CopexReturn(msg, false);
        }
        if(newTask instanceof CopexAction){
            InitialNamedAction newA = null;
            boolean isNewTaskActionNamed = newTask instanceof  CopexActionNamed || newTask instanceof CopexActionAcquisition || newTask instanceof CopexActionChoice || newTask instanceof CopexActionManipulation || newTask instanceof CopexActionTreatment ;
            if (isNewTaskActionNamed){
               newA = ((CopexActionNamed)newTask).getNamedAction() ;
           }
            if(newA != null && newTask instanceof CopexActionParam){
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
        if(newTask instanceof Step && newTask.getTaskRepeat() != null && newTask.getTaskRepeat().getListParam().size() > 0){

        }
        updateDatasheetProd(expProc);
        expProc.lockMaterialUsed();
        // update modification date
        CopexReturn cr = updateDateProc(expProc);
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
            if ( expProc.getListTask().get(idOld) instanceof CopexActionNamed)
                ((CopexActionNamed)expProc.getListTask().get(idOld)).setNamedAction(((CopexActionNamed)newTask).getNamedAction());
            else{
                CopexTask ot = expProc.getListTask().get(idOld) ;
                CopexActionNamed an = new CopexActionNamed(ot.getDbKey(),  getLocale(),ot.getName(getLocale()), ot.getDescription( getLocale()), ot.getComments(getLocale()), ot.getTaskImage(),ot.getDraw(),  ot.isVisible(), ot.getTaskRight(),((CopexActionNamed)newTask).getNamedAction(), ot.getTaskRepeat() );
                expProc.getListTask().set(idOld, an);
            }
        }else if (newTask instanceof CopexAction){
            if ( expProc.getListTask().get(idOld) instanceof CopexActionNamed){
                CopexTask ot = expProc.getListTask().get(idOld) ;
                CopexAction a = new CopexAction(ot.getDbKey(),getLocale(), "", ot.getDescription(getLocale()), ot.getComments(getLocale()), ot.getTaskImage(),ot.getDraw(),  ot.isVisible(),ot.getTaskRight(), ot.getTaskRepeat());
                expProc.getListTask().set(idOld, a);
            }
        }
        expProc.getListTask().get(idOld).setListDescription(newTask.getListDescription());
        expProc.getListTask().get(idOld).setListComments(newTask.getListComments());
        if (expProc.getListTask().get(idOld).getDbKey() == expProc.getQuestion().getDbKey()){
            // update the question of the proc
            expProc.getQuestion().setListDescription(newTask.getListDescription());
            expProc.getQuestion().setListComments(newTask.getListComments());
        }
        
        //  v[0] new proc
        v.add((LearnerProcedure)expProc.clone());
        return new CopexReturn();
    }

    /* creation of a new blank proc. */
    @Override
    public CopexReturn createProc(String procName, InitialProcedure initProc ) {
        return copyProc(procName, initProc, false, true, false);
    }
    
    /* creation of a new blank proc. */
    public CopexReturn createProc(String procName, InitialProcedure initProc, boolean setTrace ) {
        return copyProc(procName, initProc, false, setTrace, false);
    }
    
    
    /* copy of a proc. */
    @Override
    public CopexReturn copyProc(String name, LearnerProcedure procToCopy){
        return copyProc(name, procToCopy, true, true, true);
    }

    public CopexReturn copyProc(String name, InitialProcedure initProc, boolean copy, boolean setTrance, boolean loadProcCopy){
        ExperimentalProcedure proc = (ExperimentalProcedure)initProc.clone();
        LearnerProcedure learnerProc = new LearnerProcedure(proc,  initProc);
        learnerProc.setMission(mission);
        learnerProc.setListMaterialUsed(getListMaterialUsed(learnerProc));
        learnerProc.setMaterials(new MaterialProc(learnerProc.getListMaterialUsed()));
        return copyProc(name, learnerProc, copy, setTrance, loadProcCopy);
    }

    /* copy of a proc. */
    public CopexReturn copyProc(String name, LearnerProcedure procToCopy, boolean copy,boolean setTrace, boolean loadProcCopy) {
        loadProcCopy = false;
        if (loadProcCopy){
            ArrayList v = new ArrayList();
            CopexReturn cr = loadProc(procToCopy, false, v);
            if (cr.isError())
                return cr;
            procToCopy = (LearnerProcedure)v.get(0);
        }
        LearnerProcedure proc = (LearnerProcedure)procToCopy.clone();
        //proc.setName(CopexUtilities.getTextLocal(name, getLocale()));
        proc.setName(name);
        proc.setRight(MyConstants.EXECUTE_RIGHT);
        long dbKeyProc = idProc++;
        proc.setDbKey(dbKeyProc);
//        long dbKeyQuestion = idTask++;
//        proc.getQuestion().setDbKey(dbKeyQuestion);
//        proc.getListTask().get(0).setDbKey(dbKeyQuestion);
//        // material to used
        if(!copy)
            proc.setListMaterialUsed(getListMaterialUsed(proc));
        // tasks
        int nbT = proc.getListTask().size();
        for (int k=0; k<nbT; k++){
            if (!proc.getListTask().get(k).isQuestionRoot()){
                long oldDbKey = proc.getListTask().get(k).getDbKey();
                CopexTask task = proc.getListTask().get(k);
                boolean isActionSetting = task instanceof CopexActionChoice || task instanceof CopexActionAcquisition || task instanceof CopexActionManipulation || task instanceof CopexActionTreatment ;
                boolean isNamedAction = task instanceof CopexActionNamed || isActionSetting ;
                int nbparam = isActionSetting ? ((CopexActionParam)task).getNbParam() :0;
                if (isNamedAction && isActionSetting){
                    for (int j=0; j<nbparam; j++){
                        if(((CopexActionParam)task).getTabParam()[j] instanceof ActionParam){
                            ((ActionParam)((CopexActionParam)task).getTabParam()[j]).setDbKey(idParam++);
                        }else if(((CopexActionParam)task).getTabParam()[j] instanceof ArrayList){
                            ArrayList<ActionParam> p = (ArrayList<ActionParam>)((CopexActionParam)task).getTabParam()[j];
                            int np = p.size();
                            for (int m=0; m<np; m++){
                                p.get(m).setDbKey(idParam++);
                            }
                        }
                    }
                    if(task instanceof CopexActionManipulation){
                        ArrayList<Object> listM = ((CopexActionManipulation)task).getListMaterialProd() ;
                        int nbM = listM.size();
                        for (int i=0; i<nbM; i++){
                            if(listM.get(i) instanceof Material){
                                Material m = (Material)listM.get(i);
                                m.setDbKey(idMaterial++);
                                // parameters creation
                                int nbParam = m.getListParameters().size();
                                for (int j=0; j<nbParam; j++){
                                    ((Material)listM.get(i)).getListParameters().get(j).setDbKey(idQuantity++);
                                }
                            }else if (listM.get(i) instanceof ArrayList){
                                int n = ((ArrayList)listM.get(i)).size();
                                for (int r=0; r<n; r++){
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
                    }else if (task instanceof CopexActionAcquisition){
                        ArrayList<Object> listD = ((CopexActionAcquisition)task).getListDataProd() ;
                        int nbD = listD.size() ;
                        for (int i=0; i<nbD; i++){
                            if(listD.get(i) instanceof QData){
                                QData q = (QData)listD.get(i);
                                q.setDbKey(idQuantity++);
                            }else if (listD.get(i) instanceof ArrayList){
                                int n = ((ArrayList)listD.get(i)).size();
                                for (int r=0; r<n; r++){
                                    QData q = ((ArrayList<QData>)listD.get(i)).get(r);
                                    q.setDbKey(idQuantity++);
                                }
                            }
                        }
                    }else if (task instanceof CopexActionTreatment){
                        ArrayList<Object> listD = ((CopexActionTreatment)task).getListDataProd() ;
                        int nbD = listD.size() ;
                        for (int i=0; i<nbD; i++){
                            if(listD.get(i) instanceof QData){
                                QData q = (QData)listD.get(i);
                                q.setDbKey(idQuantity++);
                            }else if (listD.get(i) instanceof ArrayList){
                                int n = ((ArrayList)listD.get(i)).size();
                                for (int r=0; r<n; r++){
                                    QData q = ((ArrayList<QData>)listD.get(i)).get(r);
                                    q.setDbKey(idQuantity++);
                                }
                            }
                        }
                    }
                }
                // update id
                long dbKey = idTask++ ;
                proc.getListTask().get(k).setDbKey(dbKey);
                if ( proc.getQuestion().getDbKey() == oldDbKey)
                    proc.getQuestion().setDbKey(dbKey);
                // list
                for (int m=0; m<nbT; m++){
                    CopexTask ct = proc.getListTask().get(m);
                    if (ct.getDbKeyBrother() == oldDbKey)
                        ct.setDbKeyBrother(dbKey);
                    else if (ct.getDbKeyChild() == oldDbKey)
                        ct.setDbKeyChild(dbKey);
                    if (proc.getQuestion().getDbKeyChild() == oldDbKey)
                        proc.getQuestion().setDbKeyChild(dbKey);

                }
            }
        }
       updateDatasheetProd(proc);
       proc.lockMaterialUsed();
        // updat modification date
        CopexReturn cr = updateDateProc(proc);
        if (cr.isError()){
            return cr;
        }
        
        
        // update
        proc.setOpen(true);
        this.listProc.add(proc);
        // logging
        if (setTrace() && setTrace){
            if (copy){
                copex.logCopyProc(proc, procToCopy);
             }else{
               // proc. creation
                copex.logCreateProc(proc);
            }
        }
        //gui 
        copex.createProc((LearnerProcedure)proc.clone());
        return cr;
    }

    /* open a proc  */
    @Override
    public CopexReturn openProc(CopexMission missionToOpen, LearnerProcedure procToOpen) {
        int idM = -1;
        /*int nbM = this.listMissionsUser.size();
        for (int i=0; i<nbM; i++){
            if (missionToOpen.getDbKey() == this.listMissionsUser.get(i).getDbKey()){
                idM = i;
                break;
            }
        }*/
        LearnerProcedure p = null;
        if (idM == -1){
            // maybe it's a proc already open
            if (missionToOpen.getDbKey() == this.mission.getDbKey()){
                int idP = getIdProc(procToOpen.getDbKey());
                if (idP == -1)
                    return new CopexReturn(copex.getBundleString("MSG_ERROR_OPEN_PROC"), false);
                listProc.get(idP).setOpen(true);
                p = (LearnerProcedure)listProc.get(idP).clone();
            }else 
                return new CopexReturn(copex.getBundleString("MSG_ERROR_OPEN_PROC"), false);
        }else{
            ArrayList<LearnerProcedure> listP = this.listProcMissionUser.get(idM);
            int idP = getIdProc(listP, procToOpen.getDbKey());
            if (idP == -1)
                return new CopexReturn(copex.getBundleString("MSG_ERROR_OPEN_PROC"), false);
            // add to the list
            p = listP.get(idP);
            ArrayList v = new ArrayList();
            CopexReturn cr = loadProc(p, true, v);
            if (cr.isError())
                return cr;
            p = (LearnerProcedure)v.get(0);
            p.setOpen(true);
            this.listProc.add(p);
        }
        // logging
        CopexReturn cr = new CopexReturn();
        if (setTrace()){
            copex.logOpenProc(p, p.getMission().getDbKey(),  p.getMission().getCode());
        }
        //gui
        copex.createProc((LearnerProcedure)p.clone());
        return cr;
    }

    /* close a procedure */
    @Override
    public CopexReturn closeProc(ExperimentalProcedure proc) {
        if (proc.getDbKey() == -2){
            copex.closeProc(proc);
            copex.logCloseHelpProc();
            return new CopexReturn();
        }
        int id = getIdProc(proc.getDbKey());
        if (id == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_CLOSE_PROC"), false);
        //listProc.remove(id);
        listProc.get(id).setOpen(false);
        // gui
        copex.closeProc((LearnerProcedure)proc.clone());
        return new CopexReturn();
    }

    /*remove proc*/
    @Override
    public CopexReturn deleteProc(ExperimentalProcedure proc) {
       int id = getIdProc(proc.getDbKey());
       if (id == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_DELETE_PROC"), false);
       // remove
       listProc.remove(id);
       // logging
       if (setTrace()){
           copex.logDeleteProc(proc);
           
       }
        // gui
       copex.deleteProc((LearnerProcedure)proc.clone());
       return new CopexReturn();
    }

     

    /* returns in v[0] the proc. list which can be copied
     * list of proc of the current mission
     * returns in v[1] the missions list of the user and in   v[2] the proc list for each mission
     */
    @Override
    public CopexReturn getListProcToCopyOrOpen(ArrayList v) {
        ArrayList<LearnerProcedure> listProcToCopy = new ArrayList();
        ArrayList<CopexMission> listMission = new ArrayList();
        ArrayList<ArrayList<LearnerProcedure>> listProcToOpen = new ArrayList();
        // proc. list of the current mission: listProc
        int nbP = listProc.size();
        for (int k=0; k<nbP; k++){
            listProcToCopy.add((LearnerProcedure)listProc.get(k).clone());
        }
        // list mission/proc associated
        int nbM = listMissionsUser.size();
        for (int k=0; k<nbM; k++){
            listMission.add((CopexMission)listMissionsUser.get(k).clone());
        }
        
        int nbPM = listProcMissionUser.size();
        for (int k=0; k<nbPM; k++){
            int nbPm = listProcMissionUser.get(k).size();
            ArrayList<LearnerProcedure> lp = new ArrayList();
            for (int i=0; i<nbPm; i++){
                lp.add((LearnerProcedure)listProcMissionUser.get(k).get(i).clone());
            }
            listProcToOpen.add(lp);
            
        }
        // add proc. of the current mission which are closed
        int nbPClose = 0;
        ArrayList<LearnerProcedure> procClose = new ArrayList();
        int n = listProc.size();
        for (int k=0; k<n; k++){
            if (!listProc.get(k).isOpen() && listProc.get(k).getMission().getDbKey() == mission.getDbKey()){
                procClose.add((LearnerProcedure)listProc.get(k).clone());
                nbPClose++;
            }
        }
        // add current mission
        if (nbPClose > 0){
            listMission.add((CopexMission)this.mission.clone());
            listProcToOpen.add(procClose);
        }
        
        
        v.add(listProcToCopy);
        v.add(listMission);
        v.add(listProcToOpen);
        return new CopexReturn();
    }

    /* update the proc. name */
    @Override
    public CopexReturn updateProcName(ExperimentalProcedure proc, String name, char undoRedo) {
        String oldName = name;
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_RENAME_PROC"), false);
        LearnerProcedure procC = listProc.get(idP);
        
        // update modification date
        CopexReturn cr = updateDateProc(procC);
        if (cr.isError()){
            return cr;
        }
        
        //udpate
        //listProc.get(idP).setName(CopexUtilities.getTextLocal(name, getLocale()));
        listProc.get(idP).setName(name);

        // logging
        if (setTrace()){
            if (undoRedo == MyConstants.NOT_UNDOREDO){
                copex.logRenameProc(proc, oldName, name)   ;
            }else if (undoRedo == MyConstants.UNDO){
                copex.logUndoRenameProc(proc, oldName, name);
            }else if (undoRedo == MyConstants.REDO){
                copex.logRedoRenameProc(proc, oldName, name);
            }
        }
        //gui
        copex.updateProcName((LearnerProcedure)procC.clone(), new String(name));
        return cr;
    }
    
    /* update proc. state */
    @Override
    public CopexReturn setProcActiv(ExperimentalProcedure proc){
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_UPDATE_PROC"), false);
        if (listProc.get(idP).getMission().getDbKey() == mission.getDbKey()){
            ArrayList<LearnerProcedure> listProcToUpdate = new ArrayList();
            listProc.get(idP).setActiv(true);
            listProcToUpdate.add(listProc.get(idP));
            int nbP = listProc.size();
            for (int i=0; i<nbP; i++){
                if (i != idP && listProc.get(i).getMission().getDbKey() == mission.getDbKey()){
                    listProc.get(i).setActiv(false);
                    listProcToUpdate.add(listProc.get(i));
                }
            }
        }
        return new CopexReturn();
    }

    
    /* print :as pdf file
     */
    @Override
    public CopexReturn printCopex(ExperimentalProcedure procToPrint) {
        CopexMission missionToPrint  =(CopexMission)mission.clone();
        LearnerProcedure proc = null;
        if (procToPrint != null){
            if(procToPrint.getDbKey() == -2)
                proc = helpProc;
            else{
                int idP = getIdProc(procToPrint.getDbKey());
                if (idP== -1)
                    return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
                proc = (LearnerProcedure)(listProc.get(idP).clone());
            }
        }
        // logging
        if (setTrace()){
            copex.logPrintProc(proc);
        }
        //String fileName = "copexPrint"+dbKeyUser+"-"+mission.getDbKey() ;
        String fileName = "copex-"+mission.getCode();
        
        PrintPDF pdfPrint = new PrintPDF(copex, fileName, group, missionToPrint,proc);
        CopexReturn cr = pdfPrint.printDocument();
        return cr;
    }

    /* drag and drop */
    @Override
    public CopexReturn move(TaskSelected taskSel, SubTree subTree, char undoRedo) {
//        System.out.println("***MOVE CONTROLLER***");
//        System.out.println(" => taskSel : "+taskSel.getSelectedTask().getDescription(getLocale()));
//        System.out.println(" => subTree : "+subTree.getFirstTask().getDescription(getLocale()));
//        if (taskSel.getTaskBrother() != null){
//            System.out.println(" => branche frere : "+taskSel.getTaskBrother().getDescription(getLocale()));
//        }else if (taskSel.getTaskParent() != null){
//            System.out.println(" => branche parent : "+taskSel.getTaskParent().getDescription(getLocale()));
//        }
        ExperimentalProcedure proc = taskSel.getProc();
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_DRAG_AND_DROP"), false);
      
        LearnerProcedure expProc = listProc.get(idP);
           printRecap(expProc);
        ArrayList<CopexTask> listTask = subTree.getListTask();
        // update the tasks links
        // save tasks
        // update modification date
        CopexReturn cr = updateDateProc(expProc);
        if (cr.isError()){
             return cr;
        }
        List<TaskTreePosition> listPosition = getTaskPosition(expProc, listTask);
        // connect the sub tree to the proc., reonnecting, possibly the links of the selected task
        CopexTask taskBranch = listTask.get(0);
        int idTaskBranch = getId(expProc.getListTask(), taskBranch.getDbKey());
        CopexTask lastTaskBranch = listTask.get(subTree.getIdLastTask());
        int idLastTaskBranch = getId(expProc.getListTask(), lastTaskBranch.getDbKey());
        CopexTask taskBrother = taskSel.getTaskBrother();
        CopexTask taskParent = taskSel.getTaskParent();
        int idB = -1;
        long dbKeyT ;
        boolean b = taskBrother == null;
        boolean p = taskParent == null;
        if (taskBrother == null){
            dbKeyT = taskParent.getDbKey();
        }else{
            dbKeyT = taskBrother.getDbKey();
        }
        
        idB = getId(expProc.getListTask(), dbKeyT);
        // remove old links of the first task and last task
        CopexTask parent = null;
        int idParent = -1;
        CopexTask oldBrother = null;
        int idOldBrother = -1;
        int nbT = expProc.getListTask().size();
        for (int i=0; i<nbT; i++){
            if (expProc.getListTask().get(i).getDbKeyChild() == taskBranch.getDbKey()){
                parent =  expProc.getListTask().get(i);
                idParent = i;
            }else if (expProc.getListTask().get(i).getDbKeyBrother() == taskBranch.getDbKey()){
                oldBrother = expProc.getListTask().get(i);
                idOldBrother = i;
            }
            
        }
        
        // reconnect possibly parent link
        if (parent != null){
             expProc.getListTask().get(idParent).setDbKeyChild(-1);
            // connect the first brother
            if (subTree.getLastBrother() != -1 && subTree.getLastBrother() != expProc.getListTask().get(idParent).getDbKey()){
                expProc.getListTask().get(idParent).setDbKeyChild(subTree.getLastBrother());
                //expProc.getListTask().get(idParent).setDbKeyChild(lastTaskBranch.getDbKeyBrother());
            }
        }
        //connect possibly the brothers
        if (oldBrother != null){
            expProc.getListTask().get(idOldBrother).setDbKeyBrother(-1);
            if (subTree.getLastBrother() != -1 && subTree.getLastBrother() != expProc.getListTask().get(idOldBrother).getDbKey()){
                expProc.getListTask().get(idOldBrother).setDbKeyBrother(subTree.getLastBrother());
                //expProc.getListTask().get(idOldBrother).setDbKeyBrother(lastTaskBranch.getDbKeyBrother());
            }
        }
        expProc.getListTask().get(idLastTaskBranch).setDbKeyBrother(-1);
        printRecap(expProc);
        // ths sub tree is not connected now => insert to the right place
        if (taskBrother == null){
            // connect as parent
            long firstChild = expProc.getListTask().get(idB).getDbKeyChild();
            taskParent.setDbKeyChild(taskBranch.getDbKey());
            // update in the list
            expProc.getListTask().get(idB).setDbKeyChild(taskBranch.getDbKey());
            
            //if there was a child => remove it
            if (firstChild != -1){
                // create a new brother link between lastTaskBranch and firstChild
                lastTaskBranch.setDbKeyBrother(firstChild);
                expProc.getListTask().get(idLastTaskBranch).setDbKeyBrother(firstChild);
                lastTaskBranch.setDbKeyBrother(firstChild);
            }
        }else{ // connect as brother
            long dbKeyOldBrother = expProc.getListTask().get(idB).getDbKeyBrother();
            if (dbKeyOldBrother != -1)
                lastTaskBranch.setDbKeyBrother(dbKeyOldBrother);
            taskBrother.setDbKeyBrother(taskBranch.getDbKey());
            // update in the list
            expProc.getListTask().get(idB).setDbKeyBrother(taskBranch.getDbKey());
            if (dbKeyOldBrother != -1){
                 expProc.getListTask().get(idLastTaskBranch).setDbKeyBrother(dbKeyOldBrother);
                lastTaskBranch.setDbKeyBrother(dbKeyOldBrother);
            }
        }
        for(Iterator<CopexTask> t = expProc.getListTask().iterator();t.hasNext();){
            CopexTask task = t.next();
            if(task.isQuestion()){
                expProc.getQuestion().setDbKeyBrother(-1);
                expProc.getQuestion().setDbKeyChild(task.getDbKeyChild());
                break;
            }
        }
        subTree.setLastBrother(lastTaskBranch.getDbKeyBrother());
        updateDatasheetProd(expProc);
        expProc.lockMaterialUsed();
        // logging
        if (setTrace()){
            TaskTreePosition insertPosition = expProc.getTaskTreePosition(taskBranch);
            if (undoRedo == MyConstants.NOT_UNDOREDO)
                copex.logDragDrop(expProc, listTask, listPosition, insertPosition);
                //cr = trace.addAction(action);
            else if (undoRedo == MyConstants.UNDO)
                copex.logUndoDragDrop(expProc, listTask, listPosition, insertPosition);
            else if (undoRedo == MyConstants.REDO)
                copex.logRedoDragDrop(expProc, listTask, listPosition, insertPosition);
        }
        //gui
         printRecap(expProc);
        return new CopexReturn();
    }

    private void printRecap(LearnerProcedure proc){
        proc.printRecap(getLocale());
    }
    
    
    @Override
    public CopexReturn finalizeDragAndDrop(ExperimentalProcedure proc) {
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_DRAG_AND_DROP"), false);
        
        copex.updateProc((LearnerProcedure)listProc.get(idP).clone(), false);
        return new CopexReturn();
    }
    
    /* save proc */
    public CopexReturn saveProcXML(){
        return new CopexReturn();
    }
    
    /* stop copx */
    @Override
    public CopexReturn stopEdP(){
        // save visisble tasks
        CopexReturn cr = saveTaskVisible();
        if (cr.isError())
               return cr;
        // logging
        if (setTrace()){
            copex.logEndTool();
        }
        // save intermediate  proc
        return saveProcXML();
    }

    /*update visible tasls */
    private CopexReturn saveTaskVisible(){
        return new CopexReturn();
    }
    
    
    /* udpate the visibility of the taks */
    @Override
    public CopexReturn updateTaskVisible(ExperimentalProcedure proc, ArrayList<CopexTask> listTask){
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_TASK_VISIBLE"), false);
        LearnerProcedure expProc = listProc.get(idP);
        int nb = listTask.size();
        for (int i=0; i<nb; i++){
            int idT = getId(expProc.getListTask(), listTask.get(i).getDbKey());
            if (idT == -1)
                return new CopexReturn(copex.getBundleString("MSG_ERROR_TASK_VISIBLE"), false);
            if(idT != -1)
                expProc.getListTask().get(idT).setVisible(listTask.get(i).isVisible());
        }
        return new CopexReturn();
    }
    
    
    /* rerturn the help proc. */
    @Override
    public CopexReturn getHelpProc(ArrayList v){
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

   /* load the help proc. */
    private CopexReturn loadHelpProc(){
       InputStreamReader fileReader = null;
        SAXBuilder builder = new SAXBuilder(false);
        try{
            InputStream s = this.getClass().getClassLoader().getResourceAsStream("languages/copexHelpProc.xml");
            fileReader = new InputStreamReader(s, "utf-8");
            Document doc = builder.build(fileReader);
            Element element = doc.getRootElement();
            helpMission = new CopexMission(element.getChild(CopexMission.TAG_MISSION), getLocale(), idMission++, idProc++, idRepeat++, idParam++, idValue++,idAction++,idActionParam++, idQuantity++, idMaterial++, idTask++, idHypothesis++, idGeneralPrinciple++, idEvaluation++, idTypeMaterial++, idInitialAction++,idOutput++,
                  config.getListMaterial(), config.getListTypeMaterial(), listPhysicalQuantity, config.getListInitialNamedAction(), config.getListMaterialStrategy() );
            helpMission.setDbKey(-2);
            // chargement du proc
            helpProc = new LearnerProcedure(element.getChild(LearnerProcedure.TAG_LEARNER_PROC), helpMission,
                    idProc++, idRepeat++, idParam++, idValue++, idActionParam++, idQuantity++, idMaterial++, idTask++,
                    idHypothesis++, idGeneralPrinciple++, idEvaluation++,
                    helpMission.getListInitialProc(), helpMission.getListMaterial(),helpMission.getListType(),  listPhysicalQuantity);
            dbKeyProblem();
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

    
    /* open the help dialog*/
    @Override
    public CopexReturn openHelpDialog() {
        if(setTrace()){
            copex.logOpenHelp();
        }
        return new CopexReturn();
    }

    /* close help dialog */
    @Override
    public CopexReturn closeHelpDialog() {
        if(setTrace()){
            copex.logCloseHelp();
        }
        return new CopexReturn();
    }

    /*open help proc.*/
    @Override
    public CopexReturn openHelpProc() {
        if(setTrace()){
            copex.logOpenHelpProc();
        }
        return new CopexReturn();
    }

    /* update the modification date */
    private CopexReturn updateDateProc(LearnerProcedure p){
        int id = getIdProc(listProc, p.getDbKey());
        if (id != -1)
            this.listProc.get(id).setDateLastModification(CopexUtilities.getCurrentDate());
        return new CopexReturn();
    }

    
    /* retourne l'elo exp proc */
    @Override
    public Element getExperimentalProcedure(ExperimentalProcedure p){
        if (p == null)
            return null;
        int id = getIdProc(p.getDbKey());
        if (id == -1)
            return null;
        LearnerProcedure proc = listProc.get(id);
        printRecap(proc);
        Element e = new Element(ExperimentalProcedure.TAG_EXPERIMENTAL_PROCEDURE);
        e.addContent(proc.getMission().toXML());
        e.addContent(proc.toXML());
        return e;
    }
    

     /* load  ELO*/
    @Override
    public CopexReturn loadELO(Element xmlContent){
        try {
            // load mission
            mission = new CopexMission(xmlContent.getChild(CopexMission.TAG_MISSION), getLocale(), idMission++, idProc++, idRepeat++, idParam++, idValue++,idAction++,idActionParam++, idQuantity++, idMaterial++, idTask++, idHypothesis++, idGeneralPrinciple++, idEvaluation++, idTypeMaterial++, idInitialAction++,idOutput++,
                  config.getListMaterial(), config.getListTypeMaterial(), listPhysicalQuantity, config.getListInitialNamedAction(), config.getListMaterialStrategy() );

             // load proc
            LearnerProcedure proc = new LearnerProcedure(xmlContent.getChild(LearnerProcedure.TAG_LEARNER_PROC), mission,
                    idProc++, idRepeat++, idParam++, idValue++, idActionParam++, idQuantity++, idMaterial++, idTask++,
                    idHypothesis++, idGeneralPrinciple++, idEvaluation++,
                    mission.getListInitialProc(), mission.getListMaterial(),mission.getListType(),  listPhysicalQuantity);

            // initial proc.
            List<InitialProcedure> listInitialProc = proc.getMission().getListInitialProc();
            this.listProc.add(proc);
            // gui
            int nbP = listProc.size();
            ArrayList<LearnerProcedure> listP = new ArrayList();
            for (int k=0; k<nbP; k++)
                listP.add((LearnerProcedure)listProc.get(k).clone());
            ArrayList<InitialProcedure> listIC = new ArrayList();
            int nbIP =listInitialProc.size();
            for (int k=0; k<nbIP; k++){
                listIC.add((InitialProcedure)listInitialProc.get(k).clone());
            }
            updateDatasheetProd(proc);
            proc.lockMaterialUsed();
            dbKeyProblem();
            copex.createProc((LearnerProcedure)proc.clone());
            if(setTrace())
                copex.logLoadELO(proc);
        } catch (JDOMException ex) {
            return new CopexReturn(copex.getBundleString("MSG_ERROR_OPEN_PROC"), false);
        }
        return new CopexReturn();
    }
   
     /*  create new ELO*/
    @Override
    public CopexReturn newELO(){
        if(setTrace())
            copex.logNewELO();
        return createProc(mission.getListInitialProc().get(0).getName(getLocale()), mission.getListInitialProc().get(0), false );
    }

    /* returns the list of parameters of actions in the step  */
    @Override
    public CopexReturn getTaskInitialParam(ExperimentalProcedure proc, CopexTask task, ArrayList v){
        v.add(proc.getTaskInitialParam(task));
        return new CopexReturn();
    }


    /* returns list output of actions in the step */
    @Override
    public CopexReturn getTaskInitialOutput(ExperimentalProcedure proc, CopexTask task, ArrayList v){
        v.add(proc.getTaskInitialOutput(task));
        return new CopexReturn();
    }

    

    @Override
    public TypeMaterial getDefaultMaterialType() {
        return this.defaultMaterialType;
    }

    @Override
    public CopexReturn cut(ExperimentalProcedure proc,SubTree subTree, TaskSelected ts){
        if (setTrace()){
            ArrayList<CopexTask> listTask = subTree.getListTask();
            List<TaskTreePosition> listPositionTask = getTaskPosition(proc, listTask);
            copex.logCut(proc, listTask, listPositionTask);
        }
        return new CopexReturn();
    }

    @Override
    public CopexReturn copy(ExperimentalProcedure proc , TaskSelected ts,SubTree subTree ){
        if (setTrace()){
            ArrayList<CopexTask> listTask = subTree.getListTask();
            List<TaskTreePosition> listPositionTask = getTaskPosition(proc, listTask);
            copex.logCopy(proc, listTask, listPositionTask);
        }
        return new CopexReturn();
    }

    @Override
    public CopexReturn paste(ExperimentalProcedure proc , TaskSelected ts,SubTree subTree){
        ArrayList v = new ArrayList();
        return paste(proc, subTree, ts, MyConstants.NOT_UNDOREDO, v);
    }

    
    /* update the hypothesis of the proc.*/
    @Override
    public CopexReturn setHypothesis(ExperimentalProcedure p, Hypothesis hypothesis, ArrayList v){
        int idP = getIdProc(p.getDbKey());
        if(idP == -1){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_HYPOTHESIS"), false);
        }
        if(hypothesis != null){
            hypothesis.setDbKey(idHypothesis++);
        }
        Hypothesis oldHypothesis = null;
        if(listProc.get(idP).getHypothesis() != null){
            oldHypothesis = (Hypothesis)listProc.get(idP).getHypothesis().clone();
            hypothesis.setDbKey(oldHypothesis.getDbKey());
        }
        listProc.get(idP).setHypothesis(hypothesis);
        if(hypothesis != null)
            v.add(hypothesis.clone());
        if(setTrace())
            copex.logHypothesis(p, oldHypothesis,hypothesis);
        return new CopexReturn();
    }

    /*update the general principle of the proc */
    @Override
    public CopexReturn setGeneralPrinciple(ExperimentalProcedure p, GeneralPrinciple principle, ArrayList v){
        int idP = getIdProc(p.getDbKey());
        if(idP == -1){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_GENERAL_PRINCIPLE"), false);
        }
        if(principle != null){
            principle.setDbKey(idGeneralPrinciple++);
        }
        GeneralPrinciple oldPrinciple = null;
        if(listProc.get(idP).getGeneralPrinciple() != null)
            oldPrinciple = (GeneralPrinciple)listProc.get(idP).getGeneralPrinciple().clone();
        listProc.get(idP).setGeneralPrinciple(principle);
        if(principle != null)
            v.add(principle.clone());
        if(setTrace())
            copex.logGeneralPrinciple(p, oldPrinciple,principle);
        return new CopexReturn();
    }

    /* update the evaluation if the proc*/
    @Override
    public CopexReturn setEvaluation(ExperimentalProcedure p, Evaluation evaluation, ArrayList v){
        int idP = getIdProc(p.getDbKey());
        if(idP == -1){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_EVALUATION"), false);
        }
        if(evaluation != null){
            evaluation.setDbKey(idEvaluation++);
        }
        Evaluation oldEvaluation = null;
        if(listProc.get(idP).getEvaluation() != null)
            oldEvaluation = (Evaluation)listProc.get(idP).getEvaluation().clone();
        listProc.get(idP).setEvaluation(evaluation);
        if(evaluation != null)
            v.add(evaluation.clone());
        if(setTrace())
            copex.logEvaluation(p, oldEvaluation,evaluation);
        return new CopexReturn();
    }

    // read config file
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
            dbKeyProblem();
            // physical quantities
            listPhysicalQuantity = config.getListQuantities();
            // material strategy
            listMaterialStrategy = config.getListMaterialStrategy();
        }catch(IOException e1){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_LOAD_COPEX_CONFIG")+" "+e1, false);
        }catch(JDOMException e2){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_LOAD_COPEX_CONFIG")+" "+e2, false);
        }
        return new CopexReturn();
    }

    // read mission file
    private CopexReturn loadCopexMission(){
        InputStreamReader fileReader = null;
        SAXBuilder builder = new SAXBuilder(false);
        //File file = new File((getClass().getResource( "/" +fileMission)).getFile());
        try{
            //fileReader = new InputStreamReader(new FileInputStream(file), "utf-8");
            //Document doc = builder.build(fileReader, file.getAbsolutePath());
            InputStream s = this.getClass().getClassLoader().getResourceAsStream("config/"+fileMission);
            fileReader = new InputStreamReader(s, "utf-8");
            Document doc = builder.build(fileReader);
            Element element = doc.getRootElement();
            mission = new CopexMission(element, getLocale(), idMission++, idProc++, idRepeat++, idParam++, idValue++,idAction++,idActionParam++, idQuantity++, idMaterial++, idTask++, idHypothesis++, idGeneralPrinciple++, idEvaluation++, idTypeMaterial++, idInitialAction++,idOutput++,
                  config.getListMaterial(), config.getListTypeMaterial(), listPhysicalQuantity, config.getListInitialNamedAction(), config.getListMaterialStrategy() );
            dbKeyProblem();
        }catch(IOException e1){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_LOAD_COPEX_MISSION")+" "+e1, false);
        }catch(JDOMException e2){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_LOAD_COPEX_MISSION")+" "+e2, false);
        }
        return new CopexReturn();
    }

    private void dbKeyProblem(){
        idMission += 100 ;
        idProc+= 100 ;
        idTask+= 100 ;
        idParam+= 100 ;
        idMaterial+= 100 ;
        idQuantity+= 100;
        idTypeMaterial+= 100 ;
        idHypothesis+= 100;
        idGeneralPrinciple+= 100 ;
        idEvaluation+= 100 ;
        idMaterialStrategy+= 100 ;
        idPhysicalQtt+= 100  ;
        idUnit+= 100;
        idRepeat+= 100;
        idValue+= 100 ;
        idActionParam+= 100 ;
        idInitialAction+= 100 ;
        idAction+= 100;
        idOutput+= 100 ;
    }


    private void updateDatasheetProd(LearnerProcedure proc){
        List<QData> listDataProd = proc.getListDataProd();
        if(listDataProd.isEmpty())
            proc.setDataSheet(null);
        else
            proc.setDataSheet(new DataSheet(listDataProd));
    }


    private ArrayList<MaterialUsed> getListMaterialUsed(LearnerProcedure proc){
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
    
    
    /* update material used */
    @Override
    public CopexReturn setMaterialUsed(ExperimentalProcedure proc, ArrayList<MaterialUsed> listMaterialToCreate, ArrayList<MaterialUsed> listMaterialToDelete, ArrayList<MaterialUsed> listMaterialToUpdate, ArrayList v){
        int idP = getIdProc(proc.getDbKey());
        if(idP == -1){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_SET_MATERIAL_USED"), false);
        }
        // create new material
        int nbMat = listMaterialToCreate.size();
        for (int i=0; i<nbMat; i++){
            listMaterialToCreate.get(i).getMaterial().setDbKey(idMaterial++);
        }
        listProc.get(idP).addListMaterialUsed(listMaterialToCreate);
        if(setTrace() && listMaterialToCreate.size()> 0)
            copex.logCreateMaterialUsed(proc, listMaterialToCreate);
        // remove material
        listProc.get(idP).deleteMaterialUsed(listMaterialToDelete);
        if(setTrace() && listMaterialToDelete.size() >0)
            copex.logDeleteMaterialUsed(proc, listMaterialToDelete);
        // update mat
        if(setTrace() && listMaterialToUpdate.size() > 0){
            ArrayList<MaterialUsed> oldListMaterial = new ArrayList();
            for (Iterator<MaterialUsed> m = listMaterialToUpdate.iterator();m.hasNext();){
                MaterialUsed matCorr = listProc.get(idP).getMaterialUsedWithId(m.next());
                if(matCorr != null)
                    oldListMaterial.add((MaterialUsed)matCorr.clone());
            }
            copex.logUpdateMaterialUsed(proc, oldListMaterial, listMaterialToUpdate);
        }
        listProc.get(idP).updateMaterialUsed(listMaterialToUpdate);
        ArrayList<MaterialUsed> listC = new ArrayList();
        int nb = listProc.get(idP).getListMaterialUsed().size();
        for (int i=0; i<nb; i++){
            listC.add((MaterialUsed)listProc.get(idP).getListMaterialUsed().get(i).clone());
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
    public CopexReturn isLabDocProc(ExperimentalProcedure p, ArrayList v){
        v.add(false);
        return new CopexReturn();
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
