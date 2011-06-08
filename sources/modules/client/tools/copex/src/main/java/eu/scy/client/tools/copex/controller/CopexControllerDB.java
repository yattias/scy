/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.controller;


import org.jdom.Document;
import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.db.*;
import eu.scy.client.tools.copex.dnd.SubTree;
import eu.scy.client.tools.copex.main.CopexPanel;
import eu.scy.client.tools.copex.edp.TaskSelected;
import eu.scy.client.tools.copex.logger.CopexProperty;
import eu.scy.client.tools.copex.logger.TaskTreePosition;
import eu.scy.client.tools.copex.print.CopexHTML;
import eu.scy.client.tools.copex.print.PrintPDF;
import eu.scy.client.tools.copex.synchro.Locker;
import eu.scy.client.tools.copex.utilities.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * 03/03/09 : a mission can contain many initial proc.
 * copex controller labbook
 * @author Marjolaine
 */
public class CopexControllerDB implements ControllerInterface {
    /*locale */
    private Locale locale;
    /* database access */
    private AccesDB db;
    /* copex */
    private CopexPanel copex;
    /*URL */
    private URL copexURL;
    /* user group */
    private long dbKeyGroup;
    /* user */
    private CopexGroup group;
    private long dbKeyUser;
    private long dbKeyLabDoc;
    /* list of physical quantities managed in copex */
    private ArrayList<PhysicalQuantity> listPhysicalQuantity ;
    /*  list of material strategy managed in COPEX  */
    private ArrayList<MaterialStrategy> listMaterialStrategy;
    // main mission
    // Be careful, proc can be open, not in this mission
    private CopexMission mission = null;
    // list of proc open
    private ArrayList<LearnerProcedure> listProc = null;
    /* list of mission of the user  */
    private ArrayList<CopexMission> listMissionsUser = null;
    /* list of proc. of missions of the user  */
    private ArrayList<ArrayList<LearnerProcedure>> listProcMissionUser = null;
    private long dbKeyTrace = -1;
    
    /*help mission*/
    private CopexMission helpMission;
    /* list of materials for the help proc. */
    private ArrayList<Material> listHelpMaterial;
    /* help proc.*/
    private LearnerProcedure helpProc;

    /* locker */
    private Locker locker;
    private TypeMaterial defaultTypeMaterial;

    private CopexHTML copexHtml;

    private static final Logger logger = Logger.getLogger(CopexControllerDB.class.getName());
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
    
    public CopexControllerDB(CopexPanel copex, URL copexURL) {
        this.copex = copex;
        this.copexURL = copexURL;
        this.copexHtml = new CopexHTML(copex);
    }

    

    /* logging? */
    private boolean setTrace(){
        return true   ;
    }

    private Locale getLocale(){
        return copex.getLocale();
    }

    /* initialization of the application - load data */
    @Override
    public CopexReturn initEdP(Locale locale, String idUser, long dbKeyMission, long dbKeyGroup, long dbKeyLabDoc, String labDocName, String fileName) {
        String msgError = "";
        this.locale = locale;
        this.dbKeyLabDoc = dbKeyLabDoc;
        // gets the user
        ArrayList v = new ArrayList();
        // initialization of the access to database
        db = new AccesDB(copexURL, dbKeyMission, idUser);
        this.dbKeyUser = Long.parseLong(idUser);
        db.setIdUser(""+dbKeyUser);
        this.dbKeyGroup = dbKeyGroup;
        // LOCKER
        DataBaseCommunication dbLabBook = new DataBaseCommunication(copexURL, MyConstants.DB_LABBOOK, dbKeyMission, idUser);
        this.locker = new Locker(copex, dbLabBook, dbKeyUser);
        // load parameters
        // load physical quantities
        v = new ArrayList();
        CopexReturn cr = ParamFromDB.getAllPhysicalQuantitiesFromDB(db.getDbC(), locale, v) ;
        if (cr.isError())
            return cr;
        copex.setProgress(5);
        listPhysicalQuantity = (ArrayList<PhysicalQuantity>)v.get(0);
        //  load material type by default
        v = new ArrayList();
        cr = ParamFromDB.getDefaultTypeMaterialFromDB(db.getDbC(), locale, v);
        if(cr.isError())
            return cr;
        copex.setProgress(10);
        defaultTypeMaterial = (TypeMaterial)v.get(0);
        // load material strategy
        v = new ArrayList();
        cr = ParamFromDB.getAllStrategyMaterialFromDB(db.getDbC(), locale, v);
        if(cr.isError())
            return cr;
        copex.setProgress(15);
        listMaterialStrategy = (ArrayList<MaterialStrategy>)v.get(0);
        // load user: name and first name
        v = new ArrayList();
        cr = db.getGroupFromDB(dbKeyGroup, v);
        if (cr.isError()){
            msgError += cr.getText();
        }else{
            group = (CopexGroup)v.get(0);
        }
        copex.setProgress(20);
        // load mission
        v = new ArrayList();
        cr = db.getMissionFromDB(dbKeyMission, v);
        if (cr.isError()){
            msgError += cr.getText();
        }else{
            mission = (CopexMission)v.get(0);
        }
        if (mission == null)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_NULL_MISSION"), false);
        copex.setProgress(25);
        // logging
        if(setTrace()){
            v = new ArrayList();
            cr = TraceFromDB.getIdTrace(db.getDbC(), dbKeyMission, dbKeyUser, v);
            if(cr.isError())
                return cr;
            dbKeyTrace = (Long)v.get(0); 
        }
        copex.setProgress(30);
        // load the list of proc. of the mission
        v = new ArrayList();
        // MBO  27/05/09 : load proc.
        // do procedures exist?
        // => if yes : load initial proc. and proc.
        // => if not : Are they more initial proc. ? or only one?
        //    =>if yes : load init proc.
        //    => if not : load code & name of the init. proc  and ask user which proc. he wants open and load the correct init. proc
        boolean allProcLocked =false;
        ArrayList<String> listProcLocked = new ArrayList();
        ArrayList<InitialProcedure> listInitialProc = new ArrayList();
        cr = ExperimentalProcedureFromDB.controlLearnerProcInDB(db.getDbC(),dbKeyLabDoc,  v );
        copex.setProgress(35);
        if (cr.isError()){
            msgError += cr.getText();
        }else{
            boolean isLearnerProc = (Boolean)v.get(0);
            if(isLearnerProc){
                ArrayList<Long> listIdInitProc = (ArrayList<Long>)v.get(1);
                // load proc.
                v = new ArrayList();
                cr = ExperimentalProcedureFromDB.getProcMissionFromDB(db.getDbC(), locker, locale, dbKeyMission, dbKeyLabDoc, listIdInitProc, listPhysicalQuantity,listMaterialStrategy, v);
                if (cr.isError()){
                    msgError += cr.getText();
                }else{
                    listProc = (ArrayList<LearnerProcedure>)v.get(0);
                    listInitialProc = (ArrayList<InitialProcedure>)v.get(1);
                    listProcLocked = (ArrayList<String>)v.get(2);
                    allProcLocked = (Boolean)v.get(3);
                }
            }else{
                // Are they many init proc in this mission?
                listProc = new ArrayList();
                v = new ArrayList();
                cr = ExperimentalProcedureFromDB.controlInitialProcInDB(db.getDbC(), dbKeyMission,  v);
                copex.setProgress(60);
                if (cr.isError()){
                    msgError += cr.getText();
                }else{
                    boolean oneInitProc = (Boolean)v.get(0);
                    ArrayList<Long> listIdInitProc = (ArrayList<Long>)v.get(1);
                    if (oneInitProc){
                        // only one init. proc.  => load and create a learner proc by default
                        v = new ArrayList();
                        cr = ExperimentalProcedureFromDB.getInitialProcFromDB(db.getDbC(), locale, dbKeyMission, listIdInitProc, listPhysicalQuantity, listMaterialStrategy, v);
                        if (cr.isError()){
                            msgError += cr.getText();
                        }else{
                            listInitialProc = (ArrayList<InitialProcedure>)v.get(0);
                        }
                        copex.setProgress(70);
                    }else{
                        // many init. proc. => load only code&name and ask user which init. proc he wants load 
                        v = new ArrayList();
                        cr = ExperimentalProcedureFromDB.getSimpleInitialProcFromDB(db.getDbC(), getLocale(), dbKeyMission,  listIdInitProc, v);
                        if (cr.isError()){
                            msgError += cr.getText();
                        }else{
                            listInitialProc = (ArrayList<InitialProcedure>)v.get(0);
                        }
                        copex.setProgress(70);
                    }
                }

            }
        }
        copex.setProgress(80);
        mission.setListInitialProc(listInitialProc);
        mission.setListType(getListType(listInitialProc));
        setLockers(listProc);
        int nbP = listProc.size();
        for (int k=0; k<nbP; k++){
            listProc.get(k).setMission(mission);
            cr = db.updateProcName(listProc.get(k).getDbKey(), labDocName);
            if (cr.isError())
                return cr;
            listProc.get(k).setName(labDocName);
            listProc.get(k).lockMaterialUsed();
        }
        if (listInitialProc == null || listInitialProc.isEmpty())
            return new CopexReturn(copex.getBundleString("MSG_ERROR_LOAD_DATA"), false);
        int nbIP = listInitialProc.size();
//        cr = db.getProcMissionFromDB(this.locker, dbKeyMission,locale,  dbKeyUser,   listPhysicalQuantity,  v);
//        boolean allProcLocked =false;
//        ArrayList<String> listProcLocked = new ArrayList();
//        if (cr.isError()){
//            msgError += cr.getText();
//        }else{
//            listProc = (ArrayList<LearnerProcedure>)v.get(0);
//            setLockers(listProc);
//            int nbP = listProc.size();
//            for (int k=0; k<nbP; k++){
//                listProc.get(k).setMission(mission);
//            }
//            listInitialProc = (ArrayList<InitialProcedure>)v.get(1);
//            if (listInitialProc == null || listInitialProc.size() == 0)
//                return new CopexReturn(edP.getBundleString("MSG_ERROR_LOAD_DATA"), false);
//            int nbIP = listInitialProc.size();
//            for (int i=0; i<nbIP; i++){
//                listInitialProc.get(i).setMission(mission);
//            }
//            listProcLocked = (ArrayList<String>)v.get(2);
//            allProcLocked = (Boolean)v.get(3);
//        }
        // load  datasheet : not for the init. proc
        v = new ArrayList();
        if (listProc == null){
            listProc = new ArrayList();
        }
        copex.setProgress(90);
        // load missions and proc. of the user
        v = new ArrayList();
        if(canAddProc()){
            cr = db.getAllMissionsFromDB(getLocale(),dbKeyUser, dbKeyMission, v);
            if (cr.isError()){
                msgError += cr.getText();
            }else{
                this.listMissionsUser = (ArrayList)v.get(0);
                this.listProcMissionUser = (ArrayList)v.get(1);
            }
            int n1 = this.listProcMissionUser.size();
            for (int i=0; i<n1; i++){
                int n2 = this.listProcMissionUser.get(i).size();
                for (int j=0; j<n2; j++){
                    this.listProcMissionUser.get(i).get(j).setMission(this.listMissionsUser.get(i));
                }
            }
        }else{
            this.listMissionsUser = new ArrayList();
            this.listProcMissionUser = new ArrayList();
        }
        cr = loadHelpProc();
        if (cr.isError()){
            msgError += cr.getText();
        }
        
        // return result
        cr = new CopexReturn();
        if(msgError.length() > 0)
            return  new CopexReturn(msgError, false);

        // logging
        if (setTrace()){
            copex.logStartTool();
        }
        // clone objects
        CopexMission m = (CopexMission)mission.clone();
        ArrayList<ExperimentalProcedure> listP = new ArrayList();
        for (int k=0; k<nbP; k++)
            listP.add((LearnerProcedure)listProc.get(k).clone());
        
        ArrayList<PhysicalQuantity> listPhysicalQuantityC = new ArrayList();
        int nbPhysQ = this.listPhysicalQuantity.size();
        for (int k=0; k<nbPhysQ; k++){
            listPhysicalQuantityC.add((PhysicalQuantity)this.listPhysicalQuantity.get(k).clone());
        }
        copex.setProgress(100);
        copex.initEdp(m, listP,  listPhysicalQuantityC);
        // if there is any proc, create one:
        // MBo 27/02/2009 : if all proc of the mission are locked, don't create a new 
        boolean askForInitProc = false;
        if (listProc.isEmpty() && !allProcLocked){
            if (nbIP == 1){
                cr = createProc(listInitialProc.get(0).getName(getLocale()), listInitialProc.get(0), false );
                if (cr.isError()){
                    msgError += cr.getText();
                }
                if(listProc.size() > 0){
                    cr = db.updateProcName(listProc.get(0).getDbKey(), labDocName);
                    if (cr.isError())
                        return cr;
                    listProc.get(0).setName(labDocName);
                    copex.updateProcName((LearnerProcedure)listProc.get(0).clone(), new String(labDocName));
                }
            }else{
                askForInitProc = true;
            }
        }
        if (listProc.size() > 0)
            printRecap(listProc.get(0));
        
        copex.displayProcLocked(listProcLocked);
        if (askForInitProc)
            copex.askForInitialProc();
        return cr;
    }


    private boolean canAddProc(){
        return false;
    }

  
    private static List<TypeMaterial> getListType(ArrayList<InitialProcedure> listInitProc){
        List<TypeMaterial> list = new LinkedList();
        for(Iterator<InitialProcedure> p = listInitProc.iterator();p.hasNext();){
            list.addAll(p.next().getListTypeMaterial());
        }
        return list;
    }

    /* load a proc and its mission  */
    private CopexReturn loadProc(LearnerProcedure p, boolean controlLock, ArrayList v){
        // load mission
        CopexMission missionToLoad = p.getMission();
        // load proc
        ArrayList v2 = new ArrayList();
        CopexReturn cr = db.getProcMissionFromDB(locker, controlLock,  missionToLoad.getDbKey(), dbKeyUser, p.getDbKey(),  locale,p.getInitialProc().getDbKey(),  listPhysicalQuantity, listMaterialStrategy, v2);
        if (cr.isError())
            return cr;
        if(controlLock && v2.get(0) == null){
            String msg = copex.getBundleString("MSG_ERROR_LOAD_PROC") ;
            msg  = CopexUtilities.replace(msg, 0, p.getName(getLocale()));
            return new CopexReturn(msg, false);
        }
        LearnerProcedure exp = (LearnerProcedure)v2.get(0);
        exp.setMission(missionToLoad);
        exp.setRight(MyConstants.NONE_RIGHT);
        
        v.add(exp);
        return new CopexReturn();
    }


     /* cut- undo redo */
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
        // save tasks in db
        ArrayList v = new ArrayList();
        CopexReturn cr = TaskFromDB.createTasksInDB(db.getDbC(),getLocale(), expProc.getDbKey(), listTaskC, proc.getQuestion(), false,  v);
        if (cr.isError()){
            return cr;
        }
        // get the new id of the new tasks
        ArrayList<CopexTask> listT = (ArrayList<CopexTask>)v.get(0);
        expProc.addTasks(listT);
        
        // connect the subtree to proc., reconnecting possibly the links of the selected task
        CopexTask taskBranch = listT.get(0);
        CopexTask lastTaskBranch = listT.get(subTree.getIdLastTask());
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
            cr = TaskFromDB.createLinkChildInDB(db.getDbC(), expProc.getListTask().get(idB).getDbKey(), taskBranch.getDbKey());
            if (cr.isError()){
                return cr;
            }
        }else{
            long dbKeyOldBrother = taskBrother.getDbKeyBrother();
            if (dbKeyOldBrother != -1)
                lastTaskBranch.setDbKeyBrother(dbKeyOldBrother);
            taskBrother.setDbKeyBrother(taskBranch.getDbKey());
            // update in the list
            expProc.getListTask().get(idB).setDbKeyBrother(taskBranch.getDbKey());
            cr = TaskFromDB.createLinkBrotherInDB(db.getDbC(), expProc.getListTask().get(idB).getDbKey(), taskBranch.getDbKey());
            if (cr.isError()){
                return cr ;
            }
            if (dbKeyOldBrother != -1){
                expProc.getListTask().get(idLTB).setDbKeyBrother(dbKeyOldBrother);
                cr = TaskFromDB.deleteLinkBrotherInDB(db.getDbC(), expProc.getListTask().get(idB).getDbKey(), dbKeyOldBrother);
                if (cr.isError()){
                    return cr;
                }
                cr = TaskFromDB.createLinkBrotherInDB(db.getDbC(), expProc.getListTask().get(idLTB).getDbKey(), dbKeyOldBrother);
                if (cr.isError()){
                    return cr;
                }
            }
        }
        updateDatasheetProd(expProc);
        expProc.lockMaterialUsed();
         int nbT = listT.size();
        // logging
        if (setTrace()){
            List<TaskTreePosition> listPosition = getTaskPosition(proc, listTaskC);
            if (undoRedo == MyConstants.NOT_UNDOREDO)
                copex.logPaste(proc,listTaskC, listPosition);
            else if (undoRedo == MyConstants.REDO)
                copex.logRedoPaste(proc, listTask, listPosition);
        }
        //gui
        ArrayList<CopexTask> listTC = new ArrayList();
        for (int k=0; k<nbT;k++){
            listTC.add((CopexTask)listT.get(k).clone());
        }
        copex.paste((LearnerProcedure)proc.clone(), listTC, ts, undoRedo);
        // update modification date
        cr = db.updateDateProc(expProc);
        if (cr.isError()){
            return cr;
        }
        cr = exportHTML(expProc);
        if (cr.isError()){
            return cr;
        }
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        v2.add(listTC);
        return new CopexReturn();
    }

    
    /* remove selected tasks
     * the root can't be deleted, even it's editabled
     * remove the childs
     */ 
    @Override
    public CopexReturn suppr(ArrayList<TaskSelected> listTs, ArrayList v, boolean suppr, char undoRedo) {
        // define the tasks list to remove
        LearnerProcedure proc = getProc(listTs);
        int idPr = getIdProc(proc.getDbKey());
        if (idPr == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_SUPPR_ROOT"), false);
        LearnerProcedure expProc = listProc.get(idPr);
        printRecap(expProc);
        ArrayList<CopexTask> listTask = getListTask(listTs);
        // control that the root is not in the list
        int nbT = listTask.size();
        for (int i=0;i<nbT; i++){
            CopexTask t = listTask.get(i);
            if (t.isQuestionRoot())
                return new CopexReturn(copex.getBundleString("MSG_ERROR_SUPPR_ROOT"), false);
        }
        // delete in db
        CopexReturn cr = db.deleteTasksFromDB(expProc.getDbKey(), listTask);
        if (cr.isError())
            return cr;
        List<TaskTreePosition> listPositionTask = getTaskPosition(expProc, listTask);
        // remove links
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
        // update links in db
       int nbTs = listTs.size();
        ArrayList<CopexTask> listToUpdateLinkChild = new ArrayList();
        ArrayList<CopexTask> listToUpdateLinkBrother = new ArrayList();
        for (int i=0; i<nbTs; i++){
            TaskSelected ts = listTs.get(i);
            long dbKeyBrother = ts.getSelectedTask().getDbKeyBrother();
            if (dbKeyBrother != -1){
                // reconnect  the brother to old brother, or to parent
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
        // update links in db
        cr = db.updateLinksInDB(expProc.getDbKey(), listToUpdateLinkBrother, listToUpdateLinkChild);
        if (cr.isError())
            return cr;
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
        // update modification date
        cr = db.updateDateProc(expProc);
        if (cr.isError()){
            return cr;
        }
        cr = exportHTML(expProc);
        if (cr.isError()){
            return cr;
        }
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
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
    
    /* returns the list of tasks selected
     * selected tasks + childs
     */
    private ArrayList<CopexTask> getListTask(ArrayList<TaskSelected> listTs){
        ExperimentalProcedure proc = getProc(listTs);
        ArrayList<CopexTask> listT = new ArrayList();
        int nbTs = listTs.size();
        for (int t=0; t<nbTs; t++){
            CopexTask task = listTs.get(t).getSelectedTask();
            listT.add((CopexTask)task.clone());
            if (task instanceof Step || task instanceof Question){
                // gets childs
               ArrayList<CopexTask> lc = listTs.get(t).getListAllChildren();
               // add
               int n = lc.size();
               for (int k=0; k<n; k++){
                   // add
                   listT.add(lc.get(k));
               }
            }
            
            
        }  
        // remove multi-occurences
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
    
    
    
    /* returns the proc of the specified tasks => proc of the first task
     */
    private LearnerProcedure getProc(ArrayList<TaskSelected> listTs){
        LearnerProcedure proc = null;
        TaskSelected ts = listTs.get(0);
        proc = (LearnerProcedure)ts.getProc();
        return proc;
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
   
    
    /* returns the index of the proc with the specified dbkey, -1 if not found */
    private int getIdProc(long dbKey){
        return getIdProc(listProc, dbKey);
    }
    
    /*  returns the index of the proc with the specified dbkey, -1 if not found */
    private int getIdProc(ArrayList<LearnerProcedure> listP, long dbKey){
        int nbT = listP.size();
        for (int i=0; i<nbT; i++){
            LearnerProcedure p = listP.get(i);
            if (p.getDbKey() == dbKey)
                return i;
        }
        return -1;
    }
    
    /* add a new action: in v[0] the new proc */
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

    /* update action */
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

    /* update step*/
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

    
    

    /* update question */
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
    
    
    /* add task */
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
        // search in the list the index of the brother task
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

        // save in db and get the new id
        ArrayList v2 = new ArrayList();
        CopexReturn cr;
        if (taskBrother == null){
            cr = db.addTaskParentInDB(getLocale(),task, expProc.getDbKey(), listProc.get(idP).getListTask().get(idB), v2);
        }else{
            cr = db.addTaskBrotherInDB(getLocale(),task, expProc.getDbKey(), listProc.get(idP).getListTask().get(idB), v2);
        }
        if (cr.isError())
            return cr;
       
        long newDbKey = (Long)v2.get(0);
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
                cr = TaskFromDB.createLinkBrotherInDB(db.getDbC(), task.getDbKey(), oldFC);
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
            CopexActionChoice a = new CopexActionChoice(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(), task.getDraw(),true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), ((CopexActionChoice)task).getTabParam(), task.getTaskRepeat());
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
            CopexActionNamed a = new CopexActionNamed(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(), task.getDraw(), true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), task.getTaskRepeat());
            expProc.addAction(a);
        }else if (task instanceof CopexAction){
            CopexAction a = new CopexAction(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(), task.getDraw(), true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), task.getTaskRepeat());
            expProc.addAction(a);
        }else if (task instanceof Step){
            Step s = new Step(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(),task.getDraw(), true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), task.getTaskRepeat());
            expProc.addStep(s);
        }else if (task instanceof Question){
            Question q = new Question(newDbKey, task.getListName(), task.getListDescription(), task.getListComments(), task.getTaskImage(),task.getDraw(),  true, taskRight, false, task.getDbKeyBrother(), task.getDbKeyChild() );
            expProc.addQuestion(q);
        }
         updateDatasheetProd(expProc);
         expProc.lockMaterialUsed();
         // update modification date
        cr = db.updateDateProc(expProc);
        if (cr.isError()){
            return cr;
        }
        cr = exportHTML(expProc);
        if (cr.isError()){
            return cr;
        }
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        // in v[0] the new proc
        v.add((LearnerProcedure)expProc.clone());
        if (setTrace()){
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

     /* modification of the task */
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
    
    
    /* update a task */
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
        // search in the list the index of the old task
        int idOld = getId(expProc.getListTask(), oldTask.getDbKey());
        if (idOld == -1){
            String msg = copex.getBundleString("MSG_ERROR_UPDATE_STEP");
            if (newTask instanceof CopexAction)
                msg = copex.getBundleString("MSG_ERROR_UPDATE_ACTION");
            else if (newTask instanceof Question)
                msg = copex.getBundleString("MSG_ERROR_UPDATE_QUESTION");
            return new CopexReturn(msg, false);
        }
        // update in database
        ArrayList v2 = new ArrayList();
        CopexReturn cr = db.updateTaskInDB(getLocale(),newTask, expProc.getDbKey(), oldTask, v2);
        if (cr.isError())
            return cr;
        newTask = (CopexTask)v2.get(0);
        // update the repeat task
        TaskRepeat oldRepeat = oldTask.getTaskRepeat();
        TaskRepeat newRepeat = newTask.getTaskRepeat();
        v2 = new ArrayList();
        if(oldRepeat == null && newRepeat != null){
            cr = TaskFromDB.insertTaskRepeatInDB(db.getDbC(), oldTask.getDbKey(), newRepeat, v2);
            if(cr.isError())
                return cr;
            newRepeat = (TaskRepeat)v2.get(0);
        }else if (oldRepeat != null){
            if(newRepeat == null){
                cr = TaskFromDB.deleteTaskRepeatFromDB(db.getDbC(), oldTask.getDbKey(), oldRepeat);
                if(cr.isError())
                    return cr;
            }else{
                cr = TaskFromDB.updateTaskRepeatInDB(db.getDbC(), newRepeat, v2);
                if(cr.isError())
                    return cr;
                newRepeat = (TaskRepeat)v2.get(0);
            }
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
            if ( expProc.getListTask().get(idOld) instanceof CopexActionNamed){
                ((CopexActionNamed)expProc.getListTask().get(idOld)).setNamedAction(((CopexActionNamed)newTask).getNamedAction());
            if(expProc.getListTask().get(idOld) instanceof CopexActionParam){
                ((CopexActionParam)expProc.getListTask().get(idOld)).setTabParam(((CopexActionParam)newTask).getTabParam());
            }
            } else {
                CopexTask ot = expProc.getListTask().get(idOld) ;
                CopexActionNamed an = new CopexActionNamed(ot.getDbKey(), getLocale(), ot.getName(getLocale()), ot.getDescription(getLocale()), ot.getComments(getLocale()), ot.getTaskImage(),ot.getDraw(),  ot.isVisible(), ot.getTaskRight(),((CopexActionNamed)newTask).getNamedAction(), ot.getTaskRepeat() );
                expProc.getListTask().set(idOld, an);
            }
        }else if (newTask instanceof CopexAction){
            if ( expProc.getListTask().get(idOld) instanceof CopexActionNamed){
                CopexTask ot = expProc.getListTask().get(idOld) ;
                CopexAction a = new CopexAction(ot.getDbKey(), getLocale(), "", ot.getDescription(getLocale()), ot.getComments(getLocale()), ot.getTaskImage(),ot.getDraw(),  ot.isVisible(),ot.getTaskRight(), ot.getTaskRepeat());
                expProc.getListTask().set(idOld, a);
            }
        }
        expProc.getListTask().get(idOld).setListDescription(newTask.getListDescription());
        expProc.getListTask().get(idOld).setListComments(newTask.getListComments());
        if (expProc.getListTask().get(idOld).getDbKey() == expProc.getQuestion().getDbKey()){
            // update question 
            expProc.getQuestion().setListDescription(newTask.getListDescription());
            expProc.getQuestion().setListComments(newTask.getListComments());
        }
        updateDatasheetProd(expProc);
         expProc.lockMaterialUsed();
        // update modification date
        cr = db.updateDateProc(expProc);
        if (cr.isError()){
            return cr;
        }
        cr = exportHTML(expProc);
        if (cr.isError()){
            return cr;
        }
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        // in v[0] new proc
        v.add((LearnerProcedure)expProc.clone());
        return new CopexReturn();
    }

    /*  create new blank proc */
    @Override
    public CopexReturn createProc(String procName, InitialProcedure initProc ) {
        // load init. proc
        ArrayList v = new ArrayList();
        ArrayList<Long> listIdInitProc = new ArrayList();
        listIdInitProc.add(initProc.getDbKey());
        CopexReturn cr = ExperimentalProcedureFromDB.getInitialProcFromDB(db.getDbC(), locale,mission.getDbKey(), listIdInitProc, listPhysicalQuantity,listMaterialStrategy,  v);
        if(cr.isError())
            return cr;
        ArrayList<InitialProcedure> l = (ArrayList<InitialProcedure>)v.get(0);
        initProc = l.get(0);
        return copyProc(procName, initProc, false, true, false);
    }
    
    /* create new blank proc  */
    public CopexReturn createProc(String procName, InitialProcedure initProc, boolean setTrace ) {
        return copyProc(procName, initProc, false, setTrace, false);
    }
    
    
    /* copy of a proc */
    @Override
    public CopexReturn copyProc(String name, LearnerProcedure procToCopy){
        return copyProc(name, procToCopy, true, true, true);
    }

    public CopexReturn copyProc(String name, InitialProcedure initProc, boolean copy, boolean setTrance, boolean loadProcCopy){
        ExperimentalProcedure proc = (ExperimentalProcedure)initProc.clone();
        LearnerProcedure learnerProc = new LearnerProcedure(proc,  initProc);
        learnerProc.setMission(mission);
        learnerProc.setDbKeyLabDoc(dbKeyLabDoc);
        learnerProc.setListMaterialUsed(getListMaterialUsed(learnerProc));
        learnerProc.setMaterials(new MaterialProc(learnerProc.getListMaterialUsed()));
        CopexReturn cr = ExperimentalProcedureFromDB.createListMaterialUsedInDB(db.getDbC(),getLocale(), learnerProc.getDbKey(), learnerProc.getListMaterialUsed());
            if(cr.isError())
                return cr;
        return copyProc(name, learnerProc, copy, setTrance, loadProcCopy);
    }

    /*  copy of a proc */
    public CopexReturn copyProc(String name, LearnerProcedure procToCopy, boolean copy,boolean setTrace, boolean loadProcCopy) {
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
        // save in db and get the id
        // save proc
        ArrayList v = new ArrayList();
        CopexReturn cr = ExperimentalProcedureFromDB.createProcedureInDB(db.getDbC(),getLocale(), proc, dbKeyLabDoc, v);
        if (cr.isError()){
            return cr;
        }
        long dbKeyProc = (Long)v.get(0);
        proc.setDbKey(dbKeyProc);
        long dbKeyQuestion = (Long)v.get(1);
        proc.getQuestion().setDbKey(dbKeyQuestion);
        proc.getListTask().get(0).setDbKey(dbKeyQuestion);
        //hyp, princ, eval
        if(proc.getHypothesis() != null ){
            ArrayList v2 = new ArrayList();
            cr = ExperimentalProcedureFromDB.createHypothesisInDB(db.getDbC(), getLocale(),proc, proc.getHypothesis(), v2);
            if(cr.isError())
                return cr;
        }
        if(proc.getGeneralPrinciple() != null ){
            ArrayList v2 = new ArrayList();
            cr = ExperimentalProcedureFromDB.createGeneralPrincipleInDB(db.getDbC(), getLocale(),proc, proc.getGeneralPrinciple(), v2);
            if(cr.isError())
                return cr;
        }
        if(proc.getEvaluation() != null ){
            ArrayList v2 = new ArrayList();
            cr = ExperimentalProcedureFromDB.createEvaluationInDB(db.getDbC(), getLocale(),proc, proc.getEvaluation(), v2);
            if(cr.isError())
                return cr;
        }
        // lock
        setLocker(dbKeyLabDoc);
        // material to used
        if(!copy){
            proc.setListMaterialUsed(getListMaterialUsed(proc));
            cr = ExperimentalProcedureFromDB.createListMaterialUsedInDB(db.getDbC(),getLocale(), proc.getDbKey(), proc.getListMaterialUsed());
            if(cr.isError())
                return cr;
        }
        // save tasks
        v = new ArrayList();
        cr = TaskFromDB.createTasksInDB(db.getDbC(), getLocale(), proc.getDbKey(), proc.getListTask(), proc.getQuestion(), false,  v);
        if (cr.isError()){
            return cr;
        }
        // get the new id tasks
        ArrayList<CopexTask> listT = (ArrayList<CopexTask>)v.get(0);
        proc.setListTask(listT);
         updateDatasheetProd(proc);
         proc.lockMaterialUsed();
        // update modifiction date
        cr = db.updateDateProc(proc);
        if (cr.isError()){
            return cr;
        }
        cr = exportHTML(proc);
        if (cr.isError()){
            return cr;
        }
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        // update
        proc.setOpen(true);
        this.listProc.add(proc);
        // logging
        if (setTrace() && setTrace){
            if (copy){
                copex.logCopyProc(proc, procToCopy);
             }else{
               // create proc
                copex.logCreateProc(proc);
            }
        }
        //gui
        copex.createProc((LearnerProcedure)proc.clone());
        return cr;
    }

    /* open a proc*/
    @Override
    public CopexReturn openProc(CopexMission missionToOpen, LearnerProcedure procToOpen) {
        int idM = -1;
        int nbM = this.listMissionsUser.size();
        for (int i=0; i<nbM; i++){
            if (missionToOpen.getDbKey() == this.listMissionsUser.get(i).getDbKey()){
                idM = i;
                break;
            }
        }
        LearnerProcedure p = null;
        if (idM == -1){
            // maybe it's already open
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
            // add in the list
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

    /* close the proc */
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
        unsetLocker(listProc.get(id));
        // gui
        copex.closeProc((LearnerProcedure)proc.clone());
        return new CopexReturn();
    }

    /* remove a procedure */
    @Override
    public CopexReturn deleteProc(ExperimentalProcedure proc) {
       int id = getIdProc(proc.getDbKey());
       if (id == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_DELETE_PROC"), false);
       // delete in database
       // unset lockers
       unsetLocker(proc);
       CopexReturn cr = db.removeProcInDB(listProc.get(id));
       if (cr.isError())
           return cr;
       
       // remove
       listProc.remove(id);
       // logging
       if (setTrace()){
           copex.logDeleteProc(proc);
           
       }
        // gui
       copex.deleteProc((LearnerProcedure)proc.clone());
       return cr;
    }

     

    /* returns in v[0] the list of proc tant can be copied
     * list of proc. of the current mission
     * returns in v[1] the list of missions of the user and in v[2] the list of proc for each mission
     */
    @Override
    public CopexReturn getListProcToCopyOrOpen(ArrayList v) {
        ArrayList<LearnerProcedure> listProcToCopy = new ArrayList();
        ArrayList<CopexMission> listMission = new ArrayList();
        ArrayList<ArrayList<LearnerProcedure>> listProcToOpen = new ArrayList();
        //  list of proc of the current mission: listProc
        int nbP = listProc.size();
        for (int k=0; k<nbP; k++){
            listProcToCopy.add((LearnerProcedure)listProc.get(k).clone());
        }
        // list of missions/proc.
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
        // add the proc of the current mission that are closed
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

    /* update the proc. name  */
    @Override
    public CopexReturn updateProcName(ExperimentalProcedure proc, String name, char undoRedo) {
        String oldName = name;
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_RENAME_PROC"), false);
        LearnerProcedure procC = listProc.get(idP);
        // update in db
        CopexReturn cr = db.updateProcName(procC.getDbKey(), name);
        if (cr.isError())
            return cr;
        // mise a jour date de modif 
        cr = db.updateDateProc(procC);
        if (cr.isError()){
            return cr;
        }
        cr = exportHTML(procC);
        if (cr.isError()){
            return cr;
        }
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        // update
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
    
    /* update the state of a proc */
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
            CopexReturn cr = db.updateProcActiv(listProcToUpdate);
            if (cr.isError())
                return cr;
        }
        return new CopexReturn();
    }

    
    

    /* print as pdf
     */
    @Override
    public CopexReturn printCopex(ExperimentalProcedure procToPrint) {
        CopexMission missionToPrint  =(CopexMission)mission.clone();
        LearnerProcedure proc = null;
        if (procToPrint != null){
            int idP = getIdProc(procToPrint.getDbKey());
            if (idP== -1)
                return new CopexReturn(copex.getBundleString("MSG_ERROR_PRINT"), false);
            proc = (LearnerProcedure)(listProc.get(idP).clone());
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
//        }else{
//            System.out.println(" => branche parent : "+taskSel.getTaskParent().getDescription(getLocale()));
//        }
        ExperimentalProcedure proc = taskSel.getProc();
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_DRAG_AND_DROP"), false);
      
        LearnerProcedure expProc = listProc.get(idP);
           printRecap(expProc);
        ArrayList<CopexTask> listTask = subTree.getListTask();
        // update the links in database
        // save tasks
        // update modification date
        CopexReturn cr = db.updateDateProc(expProc);
        if (cr.isError()){
             return cr;
        }
        List<TaskTreePosition> listPosition = getTaskPosition(expProc, listTask);
        //connect the subtree ot proc, reonnecting possibly the links of the selected task
        CopexTask taskBranch = listTask.get(0);
        int idTaskBranch = getId(expProc.getListTask(), taskBranch.getDbKey());
        CopexTask lastTaskBranch = listTask.get(subTree.getIdLastTask());
        int idLastTaskBranch = getId(expProc.getListTask(), lastTaskBranch.getDbKey());
        CopexTask taskBrother = taskSel.getTaskBrother();
        CopexTask taskParent = taskSel.getTaskParent();
        int idB = -1;
        long dbKeyT ;
        if (taskBrother == null){
            dbKeyT = taskParent.getDbKey();
        }else{
            dbKeyT = taskBrother.getDbKey();
        }
        
        idB = getId(expProc.getListTask(), dbKeyT);
        // remove old links of the first and the last task
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
        // first task
        TaskFromDB.deleteLinkParentInDB(db.getDbC(), taskBranch.getDbKey());
        if (cr.isError()){
           return cr;
        }
        TaskFromDB.deleteLinkSubBrotherInDB(db.getDbC(), taskBranch.getDbKey());
        if (cr.isError()){
            return cr;
        }
        // last task
        TaskFromDB.deleteLinkBrotherInDB(db.getDbC(), lastTaskBranch.getDbKey());
        if (cr.isError()){
            return cr;
        }
        // reconnect possibly a parent link
        
        if (parent != null){
             expProc.getListTask().get(idParent).setDbKeyChild(-1);
            // connect the brother
            if (subTree.getLastBrother() != -1 && subTree.getLastBrother() != expProc.getListTask().get(idParent).getDbKey()){
                
                TaskFromDB.createLinkChildInDB(db.getDbC(), parent.getDbKey(), subTree.getLastBrother());
                //TaskFromDB.createLinkChildInDB(db.getDbC(), parent.getDbKey(), lastTaskBranch.getDbKeyBrother());
                if (cr.isError()){
                    return cr;
                }
                expProc.getListTask().get(idParent).setDbKeyChild(subTree.getLastBrother());
                //expProc.getListTask().get(idParent).setDbKeyChild(lastTaskBranch.getDbKeyBrother());
            }
        }
        // connect possibly the brothers
        if (oldBrother != null){
            expProc.getListTask().get(idOldBrother).setDbKeyBrother(-1);
            if (subTree.getLastBrother() != -1 && subTree.getLastBrother() != expProc.getListTask().get(idOldBrother).getDbKey()){
                TaskFromDB.createLinkBrotherInDB(db.getDbC(), oldBrother.getDbKey(), subTree.getLastBrother());
                //TaskFromDB.createLinkBrotherInDB(db.getDbC(), oldBrother.getDbKey(), lastTaskBranch.getDbKeyBrother());
                if (cr.isError()){
                    return cr;
                }
                expProc.getListTask().get(idOldBrother).setDbKeyBrother(subTree.getLastBrother());
                //expProc.getListTask().get(idOldBrother).setDbKeyBrother(lastTaskBranch.getDbKeyBrother());
            }
        }
        expProc.getListTask().get(idLastTaskBranch).setDbKeyBrother(-1);
        printRecap(expProc);
        // the sub tree is disconnected => insert now to the right place
        if (taskBrother == null){
            // connect as parent
            long firstChild = expProc.getListTask().get(idB).getDbKeyChild();
            taskParent.setDbKeyChild(taskBranch.getDbKey());
            // update in the list
            expProc.getListTask().get(idB).setDbKeyChild(taskBranch.getDbKey());
            cr = TaskFromDB.createLinkChildInDB(db.getDbC(), expProc.getListTask().get(idB).getDbKey(), taskBranch.getDbKey());
            if (cr.isError()){
                return cr;
            }
            //if there was a child, cut the link
            if (firstChild != -1){
                cr = TaskFromDB.deleteLinkParentInDB(db.getDbC(), firstChild);
                if (cr.isError()){
                    return cr;
                }
                // create a new brother link between  lastTaskBranch and firstChild
                lastTaskBranch.setDbKeyBrother(firstChild);
                cr = TaskFromDB.createLinkBrotherInDB(db.getDbC(), lastTaskBranch.getDbKey(), firstChild);
                if (cr.isError()){
                    return cr;
                }
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
            cr = TaskFromDB.createLinkBrotherInDB(db.getDbC(), expProc.getListTask().get(idB).getDbKey(), taskBranch.getDbKey());
            if (cr.isError()){
                return cr;
            }
            if (dbKeyOldBrother != -1){
                 expProc.getListTask().get(idLastTaskBranch).setDbKeyBrother(dbKeyOldBrother);
                lastTaskBranch.setDbKeyBrother(dbKeyOldBrother);
                cr = TaskFromDB.deleteLinkBrotherInDB(db.getDbC(), expProc.getListTask().get(idB).getDbKey(), dbKeyOldBrother);
                if (cr.isError()){
                    return cr;
                }
                cr = TaskFromDB.createLinkBrotherInDB(db.getDbC(), expProc.getListTask().get(idLastTaskBranch).getDbKey(), dbKeyOldBrother);
                if (cr.isError()){
                    return cr;
                }
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
        cr = exportHTML(expProc);
        if (cr.isError()){
            return cr;
        }
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
//         System.out.println("*** FIN MOVE CONTROLLER***");
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
    
   
    /* stop copex  */
    @Override
    public CopexReturn stopEdP(){
        // unset lockers
        try{
            unsetLockers(this.listProc);
            this.locker.stop();
            // save vis task
            CopexReturn cr = saveTaskVisible();
            if (cr.isError())
               return cr;
            // save logging
            if (setTrace()){
                copex.logEndTool();
            }
            // unset the locker for the labdoc !!
            //cr = MissionFromDB.unsetLabdocLockerInDB(db.getDbC(), dbKeyLabDoc);
            //if (cr.isError())
            //   return cr;
            return new CopexReturn();
        }catch(Exception e){
            logger.log(Level.SEVERE, ("stopEdp : "+e));
            return new CopexReturn();
        }
    }

    /*update visibility of tasks  */
    private CopexReturn saveTaskVisible(){
        if (listProc == null)
            return new CopexReturn();
        int nb = listProc.size();
        CopexReturn cr = new CopexReturn();
        for (int i=0; i<nb; i++){
            cr = TaskFromDB.updateTaskVisibleInDB(db.getDbC(), listProc.get(i).getListTask());
            if (cr.isError())
                return cr;
        }
        return cr;
    }
    
    
    /* update visibility of tasks */
    @Override
    public CopexReturn updateTaskVisible(ExperimentalProcedure proc, ArrayList<CopexTask> listTask){
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_TASK_VISIBLE"), false);
        LearnerProcedure expProc = listProc.get(idP);
        //update in database
       // CopexReturn cr = TaskFromDB.updateTaskVisibleInDB_xml(db.getDbC(), listTask);
       // if (cr.isError())
       //     return cr;
        // update
        int nb = listTask.size();
        for (int i=0; i<nb; i++){
            int idTask = getId(expProc.getListTask(), listTask.get(i).getDbKey());
            if (idTask == -1)
                return new CopexReturn(copex.getBundleString("MSG_ERROR_TASK_VISIBLE"), false);
            expProc.getListTask().get(idTask).setVisible(listTask.get(i).isVisible());
        }
        return new CopexReturn();
    }
    
    
    
    
    
    
   
    
    /* returns the help proc. */
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

    // read the copex config file
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
            // physical quantitites
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

   /* load help proc */
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

    
    /* set lockers on a labdoc/proc */
    private void setLocker(long dbKeylabdoc){
        this.locker.setLabdocLocker(dbKeylabdoc);
    }


    /* set lockers for a list of proc */
    private void setLockers(ArrayList<LearnerProcedure> listP){
        int nb = listP.size();
        ArrayList listId = new ArrayList();
        for (int i=0; i<nb; i++){
            listId.add(listP.get(i).getDbKeyLabDoc());
        }
        this.locker.setLabdocLockers(listId);
    }

    /* unset the proc locker */
    private void unsetLocker(ExperimentalProcedure proc){
        if(proc instanceof LearnerProcedure){
            this.locker.unsetLabdocLocker(((LearnerProcedure)proc).getDbKeyLabDoc());
        }
    }
    /* unset lockers for a list of proc. */
    private void unsetLockers(ArrayList<LearnerProcedure> listP){
        int nb = listP.size();
        ArrayList listId = new ArrayList();
        for (int i=0; i<nb; i++){
            listId.add(listP.get(i).getDbKeyLabDoc());
        }
        this.locker.unsetLabdocLockers(listId);
    }


    /* open the help dialog*/
    @Override
    public CopexReturn openHelpDialog() {
        if(setTrace()){
            copex.logOpenHelp();
        }
        return new CopexReturn();
    }

    /* close the help dialog */
    @Override
    public CopexReturn closeHelpDialog() {
        if(setTrace()){
            copex.logCloseHelp();
        }
        return new CopexReturn();
    }

    /* open help proc*/
    @Override
    public CopexReturn openHelpProc() {
        if(setTrace()){
            copex.logOpenHelpProc();
        }
        return new CopexReturn();
    }

    /* returns the proc ELO */
    @Override
    public Element getExperimentalProcedure(ExperimentalProcedure proc){
        return null;
    }

    /* load ELO */
    @Override
    public CopexReturn loadELO(Element xmlContent){
        return new CopexReturn();
    }

     /*   create new ELO */
    @Override
    public CopexReturn newELO(){
        return new CopexReturn();
    }

    /* returns the parameters list of actions of a step */
    @Override
    public CopexReturn getTaskInitialParam(ExperimentalProcedure proc, CopexTask task, ArrayList v){
        v.add(proc.getTaskInitialParam(task));
        return new CopexReturn();
    }


    /* returns the list of output of actions in a step  */
    @Override
    public CopexReturn getTaskInitialOutput(ExperimentalProcedure proc, CopexTask task, ArrayList v){
        v.add(proc.getTaskInitialOutput(task));
        return new CopexReturn();
    }

    @Override
    public TypeMaterial getDefaultMaterialType() {
        return this.defaultTypeMaterial;
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

     

    /* update the hypothesis of a proc*/
    @Override
    public CopexReturn setHypothesis(ExperimentalProcedure p, Hypothesis hypothesis, ArrayList v){
        int idP = getIdProc(p.getDbKey());
        if(idP == -1){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_HYPOTHESIS"), false);
        }
        if(hypothesis == null){
            CopexReturn cr = ExperimentalProcedureFromDB.deleteHypothesisFromDB(db.getDbC(), listProc.get(idP));
            if(cr.isError())
                return cr;
        }else{
            if(hypothesis.getDbKey() == -1){
                ArrayList v2 = new ArrayList();
                CopexReturn cr = ExperimentalProcedureFromDB.createHypothesisInDB(db.getDbC(), getLocale(),listProc.get(idP), hypothesis, v2);
                if(cr.isError())
                    return cr;
                hypothesis = (Hypothesis)v2.get(0);
            }else{
                CopexReturn cr = ExperimentalProcedureFromDB.updateHypothesisInDB(db.getDbC(), getLocale(),hypothesis);
                if(cr.isError())
                    return cr;
            }
        }
        Hypothesis oldHypothesis = null;
        if(listProc.get(idP).getHypothesis() != null)
            oldHypothesis = (Hypothesis)listProc.get(idP).getHypothesis().clone();
        listProc.get(idP).setHypothesis(hypothesis);
        if(hypothesis != null)
            v.add(hypothesis.clone());
        CopexReturn cr = exportHTML(listProc.get(idP));
        if (cr.isError()){
            return cr;
        }
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        if(setTrace())
            copex.logHypothesis(p, oldHypothesis,hypothesis);
        return new CopexReturn();
    }

    /*  update the general principle of a proc */
    @Override
    public CopexReturn setGeneralPrinciple(ExperimentalProcedure p, GeneralPrinciple principle, ArrayList v){
        int idP = getIdProc(p.getDbKey());
        if(idP == -1){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_GENERAL_PRINCIPLE"), false);
        }
        if(principle == null){
            CopexReturn cr = ExperimentalProcedureFromDB.deleteGeneralPrincipleFromDB(db.getDbC(), listProc.get(idP));
            if(cr.isError())
                return cr;
        }else{
            if(principle.getDbKey() == -1){
                ArrayList v2 = new ArrayList();
                CopexReturn cr = ExperimentalProcedureFromDB.createGeneralPrincipleInDB(db.getDbC(), getLocale(),listProc.get(idP), principle, v2);
                if(cr.isError())
                    return cr;
                principle = (GeneralPrinciple)v2.get(0);
            }else{
                CopexReturn cr = ExperimentalProcedureFromDB.updateGeneralPrincipleInDB(db.getDbC(), getLocale(),principle);
                if(cr.isError())
                    return cr;
            }
        }
        GeneralPrinciple oldPrinciple = null;
        if(listProc.get(idP).getGeneralPrinciple() != null)
            oldPrinciple = (GeneralPrinciple)listProc.get(idP).getGeneralPrinciple().clone();
        listProc.get(idP).setGeneralPrinciple(principle);
        if(principle != null)
            v.add(principle.clone());
        CopexReturn cr = exportHTML(listProc.get(idP));
        if (cr.isError()){
            return cr;
        }
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        if(setTrace())
            copex.logGeneralPrinciple(p, oldPrinciple,principle);
        return new CopexReturn();
    }
    
    /* update the evaluation of the proc*/
    @Override
    public CopexReturn setEvaluation(ExperimentalProcedure p, Evaluation evaluation, ArrayList v){
        int idP = getIdProc(p.getDbKey());
        if(idP == -1){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_EVALUTION"), false);
        }
        if(evaluation == null){
            CopexReturn cr = ExperimentalProcedureFromDB.deleteEvaluationFromDB(db.getDbC(), listProc.get(idP));
            if(cr.isError())
                return cr;
        }else{
            if(evaluation.getDbKey() == -1){
                ArrayList v2 = new ArrayList();
                CopexReturn cr = ExperimentalProcedureFromDB.createEvaluationInDB(db.getDbC(), getLocale(),listProc.get(idP), evaluation, v2);
                if(cr.isError())
                    return cr;
                evaluation = (Evaluation)v2.get(0);
            }else{
                CopexReturn cr = ExperimentalProcedureFromDB.updateEvaluationInDB(db.getDbC(), getLocale(),evaluation);
                if(cr.isError())
                    return cr;
            }
        }
        Evaluation oldEvaluation = null;
        if(listProc.get(idP).getEvaluation() != null)
            oldEvaluation = (Evaluation)listProc.get(idP).getEvaluation().clone();
        listProc.get(idP).setEvaluation(evaluation);
        if(evaluation != null)
            v.add(evaluation.clone());
        CopexReturn cr = exportHTML(listProc.get(idP));
        if (cr.isError()){
            return cr;
        }
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        if(setTrace())
            copex.logEvaluation(p, oldEvaluation,evaluation);
        return new CopexReturn();
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
        int idProc = getIdProc(proc.getDbKey());
        if(idProc == -1){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_SET_MATERIAL_USED"), false);
        }
        // create material
        int nbMat = listMaterialToCreate.size();
        for (int i=0; i<nbMat; i++){
            ArrayList v2 = new ArrayList();
            CopexReturn cr = ExperimentalProcedureFromDB.createMaterialInDB(db.getDbC(),getLocale(), listMaterialToCreate.get(i).getMaterial(), proc.getDbKey(), v2);
            if(cr.isError()){
                return cr;
            }
            Material m = (Material)v2.get(0);
            listMaterialToCreate.get(i).setMaterial(m);
        }
        CopexReturn cr = ExperimentalProcedureFromDB.createListMaterialUsedInDB(db.getDbC(), getLocale(), listProc.get(idProc).getDbKey(), listMaterialToCreate);
        if(cr.isError())
            return cr;
        listProc.get(idProc).addListMaterialUsed(listMaterialToCreate);
        if(setTrace() && listMaterialToCreate.size()> 0)
            copex.logCreateMaterialUsed(proc, listMaterialToCreate);
        // remove material
        cr = ExperimentalProcedureFromDB.deleteMaterialUsedFromDB(db.getDbC(), listProc.get(idProc).getDbKey(), listMaterialToDelete);
        if(cr.isError())
            return cr;
        listProc.get(idProc).deleteMaterialUsed(listMaterialToDelete);
        if(setTrace() && listMaterialToDelete.size() >0)
            copex.logDeleteMaterialUsed(proc, listMaterialToDelete);
        // update material
        if(setTrace() && listMaterialToUpdate.size() > 0){
            ArrayList<MaterialUsed> oldListMaterial = new ArrayList();
            for (Iterator<MaterialUsed> m = listMaterialToUpdate.iterator();m.hasNext();){
                MaterialUsed matCorr = listProc.get(idProc).getMaterialUsedWithId(m.next());
                if(matCorr != null)
                    oldListMaterial.add((MaterialUsed)matCorr.clone());
            }
            copex.logUpdateMaterialUsed(proc, oldListMaterial, listMaterialToUpdate);
        }
        cr = ExperimentalProcedureFromDB.updateMaterialUsedInDB(db.getDbC(), getLocale(), listProc.get(idProc).getDbKey(), listMaterialToUpdate);
        if(cr.isError())
            return cr;
        listProc.get(idProc).updateMaterialUsed(listMaterialToUpdate);
        ArrayList<MaterialUsed> listC = new ArrayList();
        int nb = listProc.get(idProc).getListMaterialUsed().size();
        for (int i=0; i<nb; i++){
            listC.add((MaterialUsed)listProc.get(idProc).getListMaterialUsed().get(i).clone());
        }
        cr = exportHTML(listProc.get(idProc));
        if (cr.isError()){
            return cr;
        }
        cr = updateLabdocStatus();
        if(cr.isError())
            return cr;
        v.add(listC);
        return new CopexReturn();
    }

    private void updateDatasheetProd(LearnerProcedure proc){
        List<QData> listDataProd = proc.getListDataProd();
        if(listDataProd.isEmpty())
            proc.setDataSheet(null);
        else
            proc.setDataSheet(new DataSheet(listDataProd));
    }

    /** log a user action in the db*/
    @Override
    public CopexReturn logUserActionInDB(String type, List<CopexProperty> attribute) {
        return TraceFromDB.logUserActionInDB(db.getDbC(), mission.getDbKey(), dbKeyUser, dbKeyTrace, type, attribute);
    }

    /** returns if a proc is the proc of the labdoc  */
    @Override
    public CopexReturn isLabDocProc(ExperimentalProcedure p, ArrayList v){
        if(p instanceof LearnerProcedure)
            v.add(p == null? false : ((LearnerProcedure)p).getDbKeyLabDoc() == dbKeyLabDoc);
        else
            v.add(false);
        return new CopexReturn();
    }

    /** export in html format */
    private CopexReturn exportHTML(ExperimentalProcedure proc ){
        ArrayList v = new ArrayList();
        CopexReturn cr = copexHtml.exportProcHTML(proc, v);
        if(cr.isError())
            return cr;
        String s = (String)v.get(0);
        cr = ExperimentalProcedureFromDB.setPreviewLabdocInDB(db.getDbC(), dbKeyLabDoc, s);
        return cr;
    }

    /** update the labdoc status */
    private CopexReturn updateLabdocStatus(){
        CopexReturn cr = MissionFromDB.updateLabdocStatusInDB(db.getDbC(), dbKeyLabDoc, dbKeyUser, dbKeyGroup);
        return cr;
    }

    /** returns the copex url */
    @Override
    public URL getCopexURL(){
        return this.copexURL;
    }
}
