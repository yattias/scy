/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.controller;


import eu.scy.tools.copex.common.*;
import eu.scy.tools.copex.db.*;
import eu.scy.tools.copex.dnd.SubTree;
import eu.scy.tools.copex.edp.CopexApplet;
import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.edp.TaskSelected;
import eu.scy.tools.copex.print.PrintPDF;
import eu.scy.tools.copex.profiler.Profiler;
import eu.scy.tools.copex.saveProcXml.SaveXMLProc;
import eu.scy.tools.copex.synchro.Locker;
import eu.scy.tools.copex.trace.*;
import eu.scy.tools.copex.utilities.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.jdom.Element;

/**
 * MBO le 03/03/09 : plusieurs proc initiaux liés à une mission
 * controller de l'applet
 * @author MBO
 */
public class CopexControllerAppletDB implements ControllerInterface {

    // CONSTANTES 
    
    // ATTRIBUTS
    /*locale */
    private Locale locale;
    /* acces bd */
    private AccesDB db;
    /* edP */
    private EdPPanel edP;
    /*applet */
    private CopexApplet applet;
    /* utilisateur */
    private long dbKeyUser;
    /* trace */
    private ManageTrace trace;
    /* utilisateur */
    private CopexUser copexUser;
    /* liste des grandeurs physiques gérées dans COPEX */
    private ArrayList<PhysicalQuantity> listPhysicalQuantity ;
    // mission principale 
    // Attention des protocoles peuvent être ouverts sans se rapporter à cette mission
    private CopexMission mission = null;
    /* liste de protocole initial de la mission */
    private ArrayList<InitialProcedure> listInitialProc = null;
    // liste des protocoles ouverts
    private ArrayList<LearnerProcedure> listProc = null;
    /* liste des missions de l'utilisateur */
    private ArrayList<CopexMission> listMissionsUser = null;
    /* liste des protocoles des missions de l'utilisateur */
    private ArrayList<ArrayList<LearnerProcedure>> listProcMissionUser = null;
    /* nom du fichier trace */
    private String logFileName;
    
    /* mission aide */
    private CopexMission helpMission;
    /*liste du materiel aide */
    private ArrayList<Material> listHelpMaterial;
    /* protocole aide */
    private LearnerProcedure helpProc;

    /* locker */
    private Locker locker;

    private boolean openQuestionDialog;

    
    // CONSTRUCTEURS
    public CopexControllerAppletDB(EdPPanel edP, CopexApplet applet) {
        this.edP = edP;
        this.applet = applet;
    }

    @Override
    public void addQuestion() {
        edP.openDialogAddQ();
    }

    @Override
    public void addEtape() {
        edP.openDialogAddE();
    }

    @Override
    public void addAction() {
        edP.openDialogAddA();
    }
    @Override
    public void print(){
        edP.openDialogPrint();
    }

    /* trace ? */
    private boolean setTrace(){
        return mission == null ? true : (mission.getOptions() == null ? true : mission.getOptions().isTrace())   ;
    }

    /* initialisation de l'application - chargement des données*/
    @Override
    public CopexReturn initEdP(Locale locale, String idUser, long dbKeyMission, int mode, String userName, String firstName, String logFileName) {
        Profiler.start();
        Profiler.start("loadData");
        System.out.println("loadData");
        String msgError = "";
        this.locale = locale;
        this.logFileName = logFileName;
        // recuperation utilisateur externe
        ArrayList v = new ArrayList();
        System.out.println("controller");
        // initialisation de la connexion à la base
        db = new AccesDB(applet, dbKeyMission, idUser);
        CopexReturn cr = loadUser(idUser, mode,userName, firstName,dbKeyMission, v);
        if(cr.isError())
            return cr;
        this.dbKeyUser = (Long)v.get(0);
        db.setIdUser(""+dbKeyUser);
         // activation de la trace
        if (setTrace()){
            //String fileNameTrace = applet.getDirectoryTrace()+this.logFileName;
            //trace = new ManageTrace(edP, fileNameTrace, 1);
            System.out.println("activation trace");
            trace = new ManageTrace(db.getDbC(),  dbKeyUser, dbKeyMission);
        }
        // LOCKER
        this.locker = new Locker(edP, db.getDbC(), dbKeyUser);
        // chargement des parametres :
        // chargement des grandeurs physiques
        v = new ArrayList();
        cr = ParamFromDB.getAllPhysicalQuantitiesFromDB(db.getDbC(), locale, v) ;
        if (cr.isError())
            return cr;
        listPhysicalQuantity = (ArrayList<PhysicalQuantity>)v.get(0);
        
        // chargement user : nom et prenom
        System.out.println("load user");
        v = new ArrayList();
        cr = db.getUserFromDB(dbKeyUser, v);
        if (cr.isError()){
            msgError += cr.getText();
        }else{
            copexUser = (CopexUser)v.get(0);
        }
        // chargement de la mission
        System.out.println("load mission");
        v = new ArrayList();
         Profiler.start("loadMission");
        cr = db.getMissionFromDB(dbKeyMission,dbKeyUser, v);
        if (cr.isError()){
            msgError += cr.getText();
        }else{
            mission = (CopexMission)v.get(0);
        }
        Profiler.end("loadMission");
        if (mission == null)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_NULL_MISSION"), false);
        
        // chargement de la liste des protocoles de la mission
        System.out.println("load proc");
        v = new ArrayList();
        Profiler.start("loadProtocole");
        // MBO le 27/05/09 : chargement des proc :
        // on regarde s'il existe des learner proc :
        // => si oui : on charge les proc init / proc correspondants
        // => si non : existe t il plusieurs proc initiaux ?
        //    => si oui : charge proc init
        //    => si non : charge les codes / nom des proc initiaux, on demande à l'utilisateur quel proc il veut ouvrir et on charge le bon proc initial
        boolean allProcLocked =false;
        ArrayList<String> listProcLocked = new ArrayList();
        cr = ExperimentalProcedureFromDB.controlLearnerProcInDB(db.getDbC(),dbKeyMission, dbKeyUser,  v );
        if (cr.isError()){
            msgError += cr.getText();
        }else{
            boolean isLearnerProc = (Boolean)v.get(0);
            if(isLearnerProc){
                ArrayList<Long> listIdInitProc = (ArrayList<Long>)v.get(1);
                // chargement des proc
                v = new ArrayList();
                cr = ExperimentalProcedureFromDB.getProcMissionFromDB_xml(db.getDbC(), locker, locale, dbKeyMission, dbKeyUser, listIdInitProc, listPhysicalQuantity, v);
                if (cr.isError()){
                    msgError += cr.getText();
                }else{
                    listProc = (ArrayList<LearnerProcedure>)v.get(0);
                    listInitialProc = (ArrayList<InitialProcedure>)v.get(1);
                    listProcLocked = (ArrayList<String>)v.get(2);
                    allProcLocked = (Boolean)v.get(3);
                }
            }else{
                // y a t il plusieurs proc initiaux dans cette mission ?
                listProc = new ArrayList();
                v = new ArrayList();
                cr = ExperimentalProcedureFromDB.controlInitialProcInDB(db.getDbC(), dbKeyMission, dbKeyUser, v);
                if (cr.isError()){
                    msgError += cr.getText();
                }else{
                    boolean oneInitProc = (Boolean)v.get(0);
                    ArrayList<Long> listIdInitProc = (ArrayList<Long>)v.get(1);
                    if (oneInitProc){
                        // un seul proc init => charge et on créé un proc par défaut
                        v = new ArrayList();
                        cr = ExperimentalProcedureFromDB.getInitialProcFromDB(db.getDbC(), locale, dbKeyMission, dbKeyUser, listIdInitProc, listPhysicalQuantity, v);
                        if (cr.isError()){
                            msgError += cr.getText();
                        }else{
                            listInitialProc = (ArrayList<InitialProcedure>)v.get(0);
                        }
                    }else{
                        // plusieurs proc init => charge les codes/ nom seulement et on propose à l'utilisateur celui qu'il veut charger
                        v = new ArrayList();
                        cr = ExperimentalProcedureFromDB.getSimpleInitialProcFromDB(db.getDbC(),  dbKeyMission, dbKeyUser, listIdInitProc, v);
                        if (cr.isError()){
                            msgError += cr.getText();
                        }else{
                            listInitialProc = (ArrayList<InitialProcedure>)v.get(0);
                        }
                    }
                }

            }
        }
        setLockers(listProc);
        int nbP = listProc.size();
        for (int k=0; k<nbP; k++){
            listProc.get(k).setMission(mission);
        }
        if (listInitialProc == null || listInitialProc.size() == 0)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_LOAD_DATA"), false);
        int nbIP = listInitialProc.size();
        for (int i=0; i<nbIP; i++){
            listInitialProc.get(i).setMission(mission);
        }

//        cr = db.getProcMissionFromDB(this.locker, dbKeyMission,locale,  dbKeyUser,  listPhysicalQuantity,  v);
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
        Profiler.end("loadProtocole");
        // chargement datasheet : pas pour le protocole initial
        System.out.println("load datasheet");
        v = new ArrayList();
        Profiler.start("loadDataSheet");
        if (listProc == null){
            listProc = new ArrayList();
            System.out.println("list proc null");
        }
        if (useDataSheet()){
            cr = db.getDataSheetFromDB(listProc,  v);
            if (cr.isError()){
                msgError += cr.getText();
            }else{
                listProc = (ArrayList<LearnerProcedure>)v.get(0);
            }
        }
       Profiler.end("loadDataSheet");
       System.out.println("loadOtherMission");
        // charge les missions et protocoles de l'utilisateur
        v = new ArrayList();
        Profiler.start("loadMissionUser");
        if(canAddProc()){
            cr = db.getAllMissionsFromDB(dbKeyUser, dbKeyMission, v);
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
        System.out.println("load help proc");
        Profiler.end("loadMissionUser");
        cr = loadHelpProc();
        if (cr.isError()){
            msgError += cr.getText();
        }
        
        // renvoit resultat : 
        cr = new CopexReturn();
        if(msgError.length() > 0)
            return  new CopexReturn(msgError, false);

        // enregistrement trace
        if (setTrace()){
            trace.addAction(new TraceFunction(TraceFunction.TRACE_OPEN_EDITOR));
        }
        System.out.println("clone");
        // clone les objets 
        CopexMission m = (CopexMission)mission.clone();
        ArrayList<LearnerProcedure> listP = new ArrayList();
        for (int k=0; k<nbP; k++)
            listP.add((LearnerProcedure)listProc.get(k).clone());
        ArrayList<InitialProcedure> listIC = new ArrayList();
        for (int k=0; k<nbIP; k++){
            listIC.add((InitialProcedure)listInitialProc.get(k).clone());
        }
        ArrayList<PhysicalQuantity> listPhysicalQuantityC = new ArrayList();
        int nbPhysQ = this.listPhysicalQuantity.size();
        for (int k=0; k<nbPhysQ; k++){
            listPhysicalQuantityC.add((PhysicalQuantity)this.listPhysicalQuantity.get(k).clone());
        }
        Profiler.start("initEdp");
        edP.initEdp(m, listP, listIC, listPhysicalQuantityC);
        // s'il n'y a pas de protocole on en créé un  :
        // MBo le 27/02/2009 : si ts les proc de la mission sont lockes on n'en créé pas d'autres
        boolean askForInitProc = false;
        openQuestionDialog = false;
        if (listProc.size() == 0 && !allProcLocked){
            System.out.println("*****pas de proc ") ;
            System.out.println("Nombre de proc initiaux : "+nbIP);
            if (nbIP == 1){
                System.out.println("   => creation d'un proc ");
                cr = createProc(listInitialProc.get(0).getName(), listInitialProc.get(0), false );
                if (cr.isError()){
                    msgError += cr.getText();
                }
                openQuestionDialog = true;
            }else{
                askForInitProc = true;
            }
        }
        Profiler.end("initEdp");

        System.out.println("fin load");
        Profiler.end("loadData");
        System.out.println("Résultat :\n"+Profiler.display());
        System.out.println("\nStats  :\n"+Profiler.getStats());
        Profiler.reset();
        if (listProc.size() > 0)
            printRecap(listProc.get(0));
        
        edP.displayProcLocked(listProcLocked);
        if (askForInitProc)
            edP.askForInitialProc();
        else if (openQuestionDialog)
            edP.setQuestionDialog();
        return cr;
    }

    /* retourne vrai si utilisation du datasheet*/
    @Override
    public boolean useDataSheet(){
       if(mission == null)
            return true;
        return this.mission.getOptions().isCanUseDataSheet() ;
    }

    private boolean canAddProc(){
        if(mission == null)
            return true;
        return this.mission.getOptions().isCanAddProc();
    }

   

    /* chargement d'un proc et de sa mission */
    private CopexReturn loadProc(LearnerProcedure p, boolean controlLock, ArrayList v){
        System.out.println("loadProc");
        // chargement de la mission
        CopexMission missionToLoad = p.getMission();
        // chargement du proc 
        ArrayList v2 = new ArrayList();
        System.out.println("chargement du proc , control : "+controlLock);
        CopexReturn cr = db.getProcMissionFromDB(locker, controlLock,  missionToLoad.getDbKey(), dbKeyUser, p.getDbKey(), locale,  p.getInitialProc().getDbKey(), listPhysicalQuantity, v2);
        if (cr.isError())
            return cr;
        if(controlLock && v2.get(0) == null){
            String msg = edP.getBundleString("MSG_ERROR_LOAD_PROC") ;
            msg  = CopexUtilities.replace(msg, 0, p.getName());
            return new CopexReturn(msg, false);
        }
        LearnerProcedure exp = (LearnerProcedure)v2.get(0);
        exp.setMission(missionToLoad);
        // chargement datasheet
        ArrayList<LearnerProcedure> listP = new ArrayList();
        listP.add(exp);
        v2 = new ArrayList();
        cr = db.getDataSheetFromDB(listP,  v2);
        if (cr.isError()){
            return cr;
        }else{
            listP = (ArrayList<LearnerProcedure>)v2.get(0);
        }
        exp = listP.get(0);
        v.add(exp);
        return new CopexReturn();

    }


    @Override
    public CopexReturn cut() {
        edP.copy();
        
        TaskSelected ts = edP.getSelectedTask();
        ExperimentalProcedure proc = ts.getProc();
        SubTree subTree = edP.getSubTreeCopy();
        ArrayList<MyTask> listXMLTask = getListXMLTask(subTree);
        
        edP.suppr(false);
        if (setTrace()){
            CopexReturn cr = trace.addAction(new CutAction(proc.getDbKey(), proc.getName(), listXMLTask));
            if (cr.isError())
                return cr;
        }
        return new CopexReturn();
        
    }

     /* couper depuis undo redo */
    @Override
    public CopexReturn cut(ArrayList<TaskSelected> listTs, SubTree subTree, ArrayList v,char undoRedo){
        CopexReturn cr = suppr(listTs, v, false, undoRedo);
        if (cr.isError())
            return cr;
        if (setTrace()){
            ExperimentalProcedure proc = (listTs.get(0)).getProc() ;
            ArrayList<MyTask> listXMLTask = getListXMLTask(subTree);
            cr = trace.addAction(new RedoCutAction(proc.getDbKey(), proc.getName(), new CutAction(proc.getDbKey(), proc.getName(), listXMLTask)));
        }
        return cr;
    }
    
    @Override
    public CopexReturn copy() {
        edP.copy();
        if (setTrace()){
            TaskSelected ts = edP.getSelectedTask();
            ExperimentalProcedure proc = ts.getProc();
            SubTree subTree = edP.getSubTreeCopy();
            ArrayList<MyTask> listXMLTask = getListXMLTask(subTree);
            CopexReturn cr = trace.addAction(new CopyAction(proc.getDbKey(), proc.getName(), listXMLTask));
            if (cr.isError())
                return cr;
        }
        return new CopexReturn();
    }

    /* retourne la liste des taches pour le xml */
    private ArrayList<MyTask> getListXMLTask(SubTree subTree){
        ArrayList<CopexTask> listTask = subTree.getListTask();
        ArrayList<MyTask> xmlList = new ArrayList();
        int nbT = listTask.size();
        for (int i=0; i<nbT; i++){
            CopexTask task = listTask.get(i);
            MyTask xmlT = new MyTask(task.getDbKey(), task.getDescription());
            xmlList.add(xmlT);
        }
        return xmlList;
    }
    
    @Override
    public CopexReturn paste(){
       // tache selectionnée et protocole ou il faut copier 
        TaskSelected ts = edP.getSelectedTask();
        LearnerProcedure proc = ts.getProc();
        // liste des taches à copier 
        SubTree subTree = edP.getSubTreeCopy();
        ArrayList v = new ArrayList();
        return paste(proc, subTree, ts, MyConstants.NOT_UNDOREDO, v);
    }
    
    @Override
    public CopexReturn paste(LearnerProcedure proc, SubTree subTree, TaskSelected ts, char undoRedo, ArrayList v2) {
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_PASTE"), false);
        ExperimentalProcedure expProc = listProc.get(idP);
        
        ArrayList<CopexTask> listTask = subTree.getListTask();
        int nbTa = listTask.size();
        ArrayList<CopexTask> listTaskC = new ArrayList();
        for (int i=0; i<nbTa; i++){
            listTaskC.add((CopexTask)listTask.get(i).clone());
        }
        // on cree les taches en base 
        // enregistre les taches 
        ArrayList v = new ArrayList();
        CopexReturn cr = TaskFromDB.createTasksInDB_xml(db.getDbC(), expProc.getDbKey(), listTaskC, proc.getQuestion(), false,  v);
        if (cr.isError()){
            return cr;
        }
        // on recupere les nouveaux id des taches
        ArrayList<CopexTask> listT = (ArrayList<CopexTask>)v.get(0);
        expProc.addTasks(listT);
        // mise à jour date de modif 
        cr = db.updateDateProc(expProc);
        if (cr.isError()){
            return cr;
        }
        
        // on branche le sous arbre au protocole, en reconnectant eventuellement les liens de la tache selectionnee
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
            // branche en parent
            taskParent.setDbKeyChild(taskBranch.getDbKey());
            // mise à jour dans la liste
            expProc.getListTask().get(idB).setDbKeyChild(taskBranch.getDbKey());
            cr = TaskFromDB.createLinkChildInDB_xml(db.getDbC(), expProc.getListTask().get(idB).getDbKey(), taskBranch.getDbKey());
            if (cr.isError()){
                return cr;
            }
        }else{
            long dbKeyOldBrother = taskBrother.getDbKeyBrother();
            if (dbKeyOldBrother != -1)
                lastTaskBranch.setDbKeyBrother(dbKeyOldBrother);
            taskBrother.setDbKeyBrother(taskBranch.getDbKey());
            // mise à jour dans la liste
            expProc.getListTask().get(idB).setDbKeyBrother(taskBranch.getDbKey());
            cr = TaskFromDB.createLinkBrotherInDB_xml(db.getDbC(), expProc.getListTask().get(idB).getDbKey(), taskBranch.getDbKey());
            if (cr.isError()){
                return cr ;
            }
            if (dbKeyOldBrother != -1){
                expProc.getListTask().get(idLTB).setDbKeyBrother(dbKeyOldBrother);
                cr = TaskFromDB.deleteLinkBrotherInDB_xml(db.getDbC(), expProc.getListTask().get(idB).getDbKey(), dbKeyOldBrother);
                if (cr.isError()){
                    return cr;
                }
                cr = TaskFromDB.createLinkBrotherInDB_xml(db.getDbC(), expProc.getListTask().get(idLTB).getDbKey(), dbKeyOldBrother);
                if (cr.isError()){
                    return cr;
                }
            }
        }
         int nbT = listT.size();
        // trace 
        if (setTrace()){
            ArrayList<MyTask> listXMLTask  = new ArrayList();
            for (int i=0; i<nbT; i++){
                CopexTask task = listT.get(i);
                long dbKeyParent = -1;
                String descP = "";
                long dbKeyB = -1;
                String descB = null;
                CopexTask tp = getParentTask(expProc.getListTask(), task);
                if (tp != null){
                    dbKeyParent = tp.getDbKey();
                    descP = tp.getDescription();
                }
                CopexTask tb = getOldBrotherTask(expProc.getListTask(), task);
                if (tb != null){
                    dbKeyB = tb.getDbKey();
                    descB = tb.getDescription();
                    
                }
                MyTask xmlT = new MyTask(task.getDbKey(), task.getDescription(), dbKeyParent, descP, dbKeyB, descB);
                listXMLTask.add(xmlT);
            }
            PasteAction action =  new PasteAction(proc.getDbKey(), proc.getName(), listXMLTask) ;
            if (undoRedo == MyConstants.NOT_UNDOREDO)
                cr = trace.addAction(action);
            else if (undoRedo == MyConstants.REDO)
                cr = trace.addAction(new RedoPasteAction(proc.getDbKey(), proc.getName(), action));
            if (cr.isError())
                return cr;
        }
        //ihm
        ArrayList<CopexTask> listTC = new ArrayList();
        for (int k=0; k<nbT;k++){
            listTC.add((CopexTask)listT.get(k).clone());
        }
        edP.paste((LearnerProcedure)proc.clone(), listTC, ts, undoRedo);
        v2.add(listTC);
        return new CopexReturn();
    }

    
    /* suppression des taches selectionnées 
     * on ne peut pas supprimer la racine, meme si elle est modifiable 
     * on supprime les taches enfant s'il y a 
     */ 
    @Override
    public CopexReturn suppr(ArrayList<TaskSelected> listTs, ArrayList v, boolean suppr, char undoRedo) {
       Profiler.start();
       Profiler.start("suppr");
        // determine la liste des taches à supprimer
        LearnerProcedure proc = getProc(listTs);
        int idPr = getIdProc(proc.getDbKey());
        if (idPr == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_SUPPR_ROOT"), false);
        LearnerProcedure expProc = listProc.get(idPr);
        printRecap(expProc);
        ArrayList<CopexTask> listTask = getListTask(listTs);
        // controle que la racine n'est pas dans le lot
        int nbT = listTask.size();
        for (int i=0;i<nbT; i++){
            CopexTask t = listTask.get(i);
            if (t.isQuestionRoot())
                return new CopexReturn(edP.getBundleString("MSG_ERROR_SUPPR_ROOT"), false);
        }
        // suppression en base
        Profiler.start("supprBD");
        CopexReturn cr = db.deleteTasksFromDB(expProc.getDbKey(), listTask);
        if (cr.isError())
            return cr;
        // mise à jour date de modif 
        cr = db.updateDateProc(expProc);
        if (cr.isError()){
            return cr;
        }
        Profiler.end("supprBD");
        Profiler.start("supprMem");
        // suppression des liens en memoire
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
        Profiler.end("supprMem");
        Profiler.start("majLien");
        // mise à jour des liens en mémoire et en base
       int nbTs = listTs.size();
        ArrayList<CopexTask> listToUpdateLinkChild = new ArrayList();
        ArrayList<CopexTask> listToUpdateLinkBrother = new ArrayList();
        for (int i=0; i<nbTs; i++){
            TaskSelected ts = listTs.get(i);
            long dbKeyBrother = ts.getSelectedTask().getDbKeyBrother();
            if (dbKeyBrother != -1){
                // on raccroche son frere  à son grand frere si il existe, sinon au parent
                CopexTask taskBrother = ts.getTaskOldBrother();
                if (taskBrother ==null){
                    // au parent
                    CopexTask taskParent = ts.getParentTask();
                    int idP = getId(expProc.getListTask(), taskParent.getDbKey());
                    if (idP == -1){
                        return new CopexReturn(edP.getBundleString("MSG_ERROR_DELETE_TASK"), false);
                    }else{
                        CopexTask tp = expProc.getListTask().get(idP);
                        tp.setDbKeyChild(dbKeyBrother);
                        listToUpdateLinkChild.add(tp);
                    }
                }else{
                    int idB = getId(expProc.getListTask(), taskBrother.getDbKey());
                    if (idB == -1){
                        return new CopexReturn(edP.getBundleString("MSG_ERROR_DELETE_TASK"), false);
                    }else{
                        CopexTask tb = expProc.getListTask().get(idB);
                        tb.setDbKeyBrother(dbKeyBrother);
                        listToUpdateLinkBrother.add(tb);
                    }
                }
            }
        }
        Profiler.end("majLien");
        Profiler.start("majLienBD");
        // mise à jour des liens dans la base : 
        cr = db.updateLinksInDB(expProc.getDbKey(), listToUpdateLinkBrother, listToUpdateLinkChild);
        if (cr.isError())
            return cr;
        Profiler.end("majLienBD");
         System.out.println("trace");
         Profiler.start("trace");
        // prepare eventuellement les donnees pour la trace
        ArrayList<MyTask> listXMLTask = new ArrayList();
        int nbTa = listTask.size();
        for (int i=0; i<nbTa; i++){
            MyTask xmlT = new MyTask(listTask.get(i).getDbKey(), listTask.get(i).getDescription());
            listXMLTask.add(xmlT);
        }
        // mise à jour des données
         System.out.println("maj données");
        boolean isOk = expProc.deleteTasks(listTask);
        if (!isOk){
            return new CopexReturn(edP.getBundleString("MSG_ERROR_DELETE_TASK"), false);
        }
        // trace 
        if (setTrace()){
            SupprAction action = new SupprAction(proc.getDbKey(), proc.getName(), listXMLTask);
            if (undoRedo == MyConstants.NOT_UNDOREDO && suppr)
                cr = trace.addAction(action);
            else if (undoRedo == MyConstants.UNDO){
                if (suppr)
                    cr = trace.addAction(new UndoAddTaskAction(proc.getDbKey(), proc.getName(), action));
                else
                    cr = trace.addAction(new UndoPasteAction(proc.getDbKey(), proc.getName(), action));
            }
            else if (undoRedo == MyConstants.REDO && suppr){
                cr = trace.addAction(new RedoDeleteTaskAction(proc.getDbKey(), proc.getName(), action));
            }
            if (cr.isError())
                return cr;
        }
        Profiler.end("trace");
         Profiler.end("suppr");
         System.out.println("Résultat :\n"+Profiler.display());
        System.out.println("\nStats  :\n"+Profiler.getStats());
        Profiler.reset();
         printRecap(expProc);
        v.add(expProc.clone());
        return new CopexReturn();
    }

    
    /* definit la liste des taches à partir d'une selection 
     * il s'agit des taches selectionnées + enfants 
     */
    private ArrayList<CopexTask> getListTask(ArrayList<TaskSelected> listTs){
        ExperimentalProcedure proc = getProc(listTs);
        ArrayList<CopexTask> listT = new ArrayList();
        int nbTs = listTs.size();
        for (int t=0; t<nbTs; t++){
            CopexTask task = listTs.get(t).getSelectedTask();
            listT.add((CopexTask)task.clone());
            if (task instanceof Step || task instanceof Question){
                // recupere les enfants
               ArrayList<CopexTask> lc = listTs.get(t).getListAllChildren();
               // on les ajoute  
               int n = lc.size();
               for (int k=0; k<n; k++){
                   // ajoute 
                   listT.add(lc.get(k));
               }
            }
            
            
        }  
        // supprimes les occurences multiples
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
    
    
    
    /* retourne le protocole associé aux taches => c'est le meme protocole 
     * pour toutes les taches, donc on retourne celui du premier element 
     */
    private LearnerProcedure getProc(ArrayList<TaskSelected> listTs){
        LearnerProcedure proc = null;
        TaskSelected ts = listTs.get(0);
        proc = ts.getProc();
        return proc;
    }
 /* cherche un indice dans la liste, -1 si non trouvé */
    private int getId(ArrayList<CopexTask> listT, long dbKey){
        int nbT = listT.size();
        for (int i=0; i<nbT; i++){
            CopexTask t = listT.get(i);
            if (t.getDbKey() == dbKey)
                return i;
        }
        return -1;
    }
   
    
    /* cherche un indice dans la liste des protocoles, -1 si non trouvé */
    private int getIdProc(long dbKey){
        return getIdProc(listProc, dbKey);
    }
    
    /* cherche un indice dans la liste des protocoles, -1 si non trouvé */
    private int getIdProc(ArrayList<LearnerProcedure> listP, long dbKey){
        int nbT = listP.size();
        for (int i=0; i<nbT; i++){
            LearnerProcedure p = listP.get(i);
            if (p.getDbKey() == dbKey)
                return i;
        }
        return -1;
    }
    
    /* ajout d'une nouvelle action : en v[0] le nouveau protocole */
    @Override
    public CopexReturn addAction(CopexAction action, LearnerProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v ) {
        Profiler.start();
        Profiler.start("addTask");
        Profiler.start("addTaskInDB");
        CopexReturn cr =  addTask(action, proc, taskBrother, taskParent, v, MyConstants.NOT_UNDOREDO, false);
        if (cr.isError())
            return cr;
        Profiler.end("addTaskInDB");
        // trace
        if (setTrace()){
            Profiler.start("addTaskInTrace");
            MyTask xmlTask = (MyTask)v.get(1);
            cr = trace.addAction(new AddActionAction(proc.getDbKey(), proc.getName(), xmlTask, action.getComments(), action.getTaskImage()));
            if (cr.isError())
                return cr;
            Profiler.end("addTaskInTrace");
        }
        Profiler.end("addTask");
        System.out.println("Résultat :\n"+Profiler.display());
        System.out.println("\nStats  :\n"+Profiler.getStats());
        Profiler.reset();
        return cr;
    }
    /* modification d'une action */
    @Override
    public CopexReturn updateAction(CopexAction newAction, LearnerProcedure proc, CopexAction oldAction, ArrayList v) {
        CopexReturn cr = updateTask(newAction, proc, oldAction, v);
        if (cr.isError())
            return cr;
        if (setTrace()){
            trace.addAction(new UpdateActionAction(proc.getDbKey(), proc.getName(), oldAction.getDbKey(), oldAction.getDescription(), newAction.getDescription(), newAction.getComments()));
        }
        return cr;
    }

    /* ajout d'une nouvelle étape */
    @Override
    public CopexReturn addStep(Step step, LearnerProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v) {
        CopexReturn cr =  addTask(step, proc, taskBrother, taskParent,v, MyConstants.NOT_UNDOREDO, false);
        if (cr.isError())
            return cr;
        // trace
        if (setTrace()){
            MyTask xmlTask = (MyTask)v.get(1);
            cr = trace.addAction(new AddStepAction(proc.getDbKey(), proc.getName(), xmlTask, step.getComments(), step.getTaskImage()));
        }
        return cr;
    }

    /* modification d'une étape*/
    @Override
    public CopexReturn updateStep(Step newStep, LearnerProcedure proc, Step oldStep, ArrayList v) {
        CopexReturn cr =  updateTask(newStep, proc, oldStep, v);
        if (cr.isError())
            return cr;
        if (setTrace()){
            trace.addAction(new UpdateStepAction(proc.getDbKey(), proc.getName(), oldStep.getDbKey(), oldStep.getDescription(), newStep.getDescription(), newStep.getComments()));
        }
        return cr;
    }

    
    /* ajout d'une sous question */
    @Override
    public CopexReturn addQuestion(Question question, LearnerProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v) {
        CopexReturn cr =  addTask(question, proc, taskBrother, taskParent, v, MyConstants.NOT_UNDOREDO, false);
        if (cr.isError())
            return cr;
        // trace
        if (setTrace()){
            MyTask xmlTask = (MyTask)v.get(1);
            cr = trace.addAction(new AddQuestionAction(proc.getDbKey(), proc.getName(), xmlTask, question.getComments(),question.getTaskImage(), question.getHypothesis()));
        }
        return cr;
    }

    /* modification d'une sous question */
    @Override
    public CopexReturn updateQuestion(Question newQuestion, LearnerProcedure proc, Question oldQuestion, ArrayList v) {
        CopexReturn cr =  updateTask(newQuestion, proc, oldQuestion, v);
        if (cr.isError())
            return cr;
        if (setTrace()){
            cr = trace.addAction(new UpdateQuestionAction(proc.getDbKey(), proc.getName(), oldQuestion.getDbKey(), oldQuestion.getDescription(), newQuestion.getDescription(), newQuestion.getComments(), newQuestion.getHypothesis(), newQuestion.getGeneralPrinciple()));
        }
        return cr;
    }
    
    /* retourne la tache parent */
    private CopexTask getParentTask(ArrayList<CopexTask> listT, CopexTask task){
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
        // on n'a pas trouve on cherche dans les grands freres 
        return getParentTask(listT, getOldBrotherTask(listT, task));
    }
    /* retourne la tache grand frere */
    private CopexTask getOldBrotherTask(ArrayList<CopexTask> listT, CopexTask task){
        long dbKey = task.getDbKey();
        int nbT = listT.size();
        for (int i=0; i<nbT; i++){
            if (listT.get(i).getDbKeyBrother() == dbKey){
                return listT.get(i);
            }
        }
        return null;
    }
    
    
    /* ajout d'une tache */
    @Override
    public CopexReturn addTask(CopexTask task, LearnerProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v, char undoRedo, boolean cut) {
        int idP = getIdProc(proc.getDbKey());
        if (idP ==  -1){
            String msg = edP.getBundleString("MSG_ERROR_ADD_STEP");
            if (task instanceof CopexAction)
                msg = edP.getBundleString("MSG_ERROR_ADD_ACTION");
            else if (task instanceof Question)
                msg = edP.getBundleString("MSG_ERROR_ADD_QUESTION");
            return new CopexReturn(msg, false);
        }
        LearnerProcedure expProc = listProc.get(idP);
        task.setDbKeyChild(-1);
        task.setDbKeyBrother(-1);
        // on cherche dans la liste des taches du proc l'indice de la tache frere
        int idB = -1;
        long dbKeyT ;
        if (taskBrother == null)
            dbKeyT = taskParent.getDbKey();
        else
            dbKeyT = taskBrother.getDbKey();
        
        idB = getId(expProc.getListTask(), dbKeyT);
        
        if (idB == -1){
            String msg = edP.getBundleString("MSG_ERROR_ADD_STEP");
            if (task instanceof CopexAction)
                msg = edP.getBundleString("MSG_ERROR_ADD_ACTION");
            else if (task instanceof Question)
                msg = edP.getBundleString("MSG_ERROR_ADD_QUESTION");
            return new CopexReturn(msg, false);
        }

        // enregistrement dans la base et recuperation du nouvel id
        ArrayList v2 = new ArrayList();
        CopexReturn cr;
        if (taskBrother == null){
            cr = db.addTaskParentInDB(task, expProc.getDbKey(), listProc.get(idP).getListTask().get(idB), v2);
        }else{
            cr = db.addTaskBrotherInDB(task, expProc.getDbKey(), listProc.get(idP).getListTask().get(idB), v2);
        }
        if (cr.isError())
            return cr;
       // mise à jour date de modif 
        cr = db.updateDateProc(expProc);
        if (cr.isError()){
            return cr;
        }
        
        long newDbKey = (Long)v2.get(0);
        // mise à jour des données 
        task.setDbKey(newDbKey);
        if (taskBrother == null){
            taskParent.setDbKeyChild(newDbKey);
            // mise à jour dans la liste
            //proc.getListTask().get(idB).setDbKeyChild(newDbKey);
            listProc.get(idP).getListTask().get(idB).setDbKeyChild(newDbKey);
            if (listProc.get(idP).getListTask().get(idB).getDbKey() == listProc.get(idP).getQuestion().getDbKey())
                listProc.get(idP).getQuestion().setDbKeyChild(newDbKey);
        }else{
            long dbKeyOldBrother = listProc.get(idP).getListTask().get(idB).getDbKeyBrother();
            task.setDbKeyBrother(dbKeyOldBrother);
            taskBrother.setDbKeyBrother(newDbKey);
            // mise à jour dans la liste
            expProc.getListTask().get(idB).setDbKeyBrother(newDbKey);
        }
        TaskRight taskRight = new TaskRight(MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT);
        if (task instanceof CopexActionChoice){
            CopexActionChoice a = new CopexActionChoice(newDbKey, "", task.getDescription(), task.getComments(), task.getTaskImage(), task.getDraw(),true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), ((CopexActionChoice)task).getTabParam(), task.getTaskRepeat());
            expProc.addAction(a);
        }else if (task instanceof CopexActionAcquisition){
            CopexActionAcquisition a = new CopexActionAcquisition(newDbKey, "", task.getDescription(), task.getComments(), task.getTaskImage(), task.getDraw(),true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), ((CopexActionAcquisition)task).getTabParam(), ((CopexActionAcquisition)task).getListDataProd(), task.getTaskRepeat());
            expProc.addAction(a);
        }else if (task instanceof CopexActionManipulation){
            CopexActionManipulation a = new CopexActionManipulation(newDbKey, "", task.getDescription(), task.getComments(), task.getTaskImage(), task.getDraw(),true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), ((CopexActionManipulation)task).getTabParam(), ((CopexActionManipulation)task).getListMaterialProd(), task.getTaskRepeat());
            expProc.addAction(a);
        }else if (task instanceof CopexActionTreatment){
            CopexActionTreatment a = new CopexActionTreatment(newDbKey, "", task.getDescription(), task.getComments(), task.getTaskImage(), task.getDraw(),true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), ((CopexActionTreatment)task).getTabParam(), ((CopexActionTreatment)task).getListDataProd(), task.getTaskRepeat());
            expProc.addAction(a);
        }else if (task instanceof CopexActionNamed){
            CopexActionNamed a = new CopexActionNamed(newDbKey, "", task.getDescription(), task.getComments(), task.getTaskImage(), task.getDraw(), true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), task.getTaskRepeat());
            expProc.addAction(a);
        }else if (task instanceof CopexAction){
            CopexAction a = new CopexAction(newDbKey, "", task.getDescription(), task.getComments(), task.getTaskImage(), task.getDraw(), true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), task.getTaskRepeat());
            expProc.addAction(a);
        }else if (task instanceof Step){
            Step s = new Step(newDbKey, "", task.getDescription(), task.getComments(), task.getTaskImage(),task.getDraw(), true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), task.getTaskRepeat());
            expProc.addStep(s);
        }else if (task instanceof Question){
            Question q = new Question(newDbKey, "", task.getDescription(), ((Question)task).getHypothesis(), task.getComments(), task.getTaskImage(),task.getDraw(), ((Question)task).getGeneralPrinciple(), true, taskRight, false, task.getDbKeyBrother(), task.getDbKeyChild() );
            expProc.addQuestion(q);
        }
        
        // en v[0] le  protocole mis à jour
        v.add((LearnerProcedure)expProc.clone());
        if (setTrace()){
            long dbKeyParent = -1;
            String parentD = "";
            long dbKeyOldBrother = -1;
            String brotherD  = null;
            CopexTask tp = getParentTask(expProc.getListTask(), task);
            if (tp != null){
                dbKeyParent = tp.getDbKey();
                parentD = tp.getDescription();
            }
            CopexTask bt = getOldBrotherTask(expProc.getListTask(), task);
            if (bt != null){
                dbKeyOldBrother = bt.getDbKey();
                brotherD = bt.getDescription();
            }
            MyTask xmlTask = new MyTask(newDbKey, task.getDescription(), dbKeyParent, parentD, dbKeyOldBrother, brotherD);
            if (undoRedo == MyConstants.REDO){
                if (task.isAction()){
                    trace.addAction(new RedoAddTaskAction(proc.getDbKey(), proc.getName(), new AddActionAction(proc.getDbKey(), proc.getName(), xmlTask, task.getComments(), task.getTaskImage())));
                }else if (task.isStep()){
                    trace.addAction(new RedoAddTaskAction(proc.getDbKey(), proc.getName(), new AddStepAction(proc.getDbKey(), proc.getName(), xmlTask, task.getComments(), task.getTaskImage())));
                }else if (task.isQuestion()){
                    trace.addAction(new RedoAddTaskAction(proc.getDbKey(), proc.getName(), new AddQuestionAction(proc.getDbKey(), proc.getName(), xmlTask, task.getComments(), task.getTaskImage(), ((Question)task).getHypothesis())));
                }
            }else if (undoRedo == MyConstants.UNDO){
                if (cut){
                    if (task.isAction()){
                        trace.addAction(new UndoCutAction(proc.getDbKey(), proc.getName(), new AddActionAction(proc.getDbKey(), proc.getName(), xmlTask, task.getComments(), task.getTaskImage())));
                    }else if (task.isStep()){
                        trace.addAction(new UndoCutAction(proc.getDbKey(), proc.getName(), new AddStepAction(proc.getDbKey(), proc.getName(), xmlTask, task.getComments(), task.getTaskImage())));
                    }else if (task.isQuestion()){
                        trace.addAction(new UndoCutAction(proc.getDbKey(), proc.getName(), new AddQuestionAction(proc.getDbKey(), proc.getName(), xmlTask, task.getComments(), task.getTaskImage(), ((Question)task).getHypothesis())));
                    }
                }else{
                    if (task.isAction()){
                        trace.addAction(new UndoDeleteTaskAction(proc.getDbKey(), proc.getName(), new AddActionAction(proc.getDbKey(), proc.getName(), xmlTask, task.getComments(), task.getTaskImage())));
                    }else if (task.isStep()){
                        trace.addAction(new UndoDeleteTaskAction(proc.getDbKey(), proc.getName(), new AddStepAction(proc.getDbKey(), proc.getName(), xmlTask, task.getComments(), task.getTaskImage())));
                    }else if (task.isQuestion()){
                        trace.addAction(new UndoDeleteTaskAction(proc.getDbKey(), proc.getName(), new AddQuestionAction(proc.getDbKey(), proc.getName(), xmlTask, task.getComments(), task.getTaskImage(), ((Question)task).getHypothesis())));
                    }
                }
            }
            v.add(xmlTask);
        }
        printRecap(expProc);
        
        return new CopexReturn();
    }

     /* modification d'une tache */
    @Override
    public CopexReturn updateTask(CopexTask newTask, LearnerProcedure proc, CopexTask oldTask, char undoRedo, ArrayList v) {
        CopexReturn cr = updateTask(newTask, proc, oldTask, v);
        if (cr.isError())
            return cr;
        if (setTrace()){
            if (undoRedo == MyConstants.UNDO){
                UpdateTaskAction updateTaskAction = null;
                if (oldTask instanceof Step)
                    updateTaskAction = new UpdateStepAction(proc.getDbKey(), proc.getName(), oldTask.getDbKey(), oldTask.getDescription(), newTask.getDescription(), newTask.getComments());
                else if (oldTask instanceof CopexAction)
                    updateTaskAction = new UpdateActionAction(proc.getDbKey(), proc.getName(), oldTask.getDbKey(), oldTask.getDescription(), newTask.getDescription(), newTask.getComments());
                else if (oldTask instanceof Question)
                    updateTaskAction = new UpdateQuestionAction(proc.getDbKey(), proc.getName(), oldTask.getDbKey(), oldTask.getDescription(), newTask.getDescription(), newTask.getComments(), ((Question)newTask).getHypothesis(), ((Question)newTask).getGeneralPrinciple());
                UndoEditTaskAction action = new UndoEditTaskAction(proc.getDbKey(), proc.getName(), updateTaskAction);
                cr = trace.addAction(action);
                if (cr.isError())
                    return cr;
            }else{ // REDO
                UpdateTaskAction updateTaskAction = null;
                if (oldTask instanceof Step)
                    updateTaskAction = new UpdateStepAction(proc.getDbKey(), proc.getName(), oldTask.getDbKey(), oldTask.getDescription(), newTask.getDescription(), newTask.getComments());
                else if (oldTask instanceof CopexAction)
                    updateTaskAction = new UpdateActionAction(proc.getDbKey(), proc.getName(), oldTask.getDbKey(), oldTask.getDescription(), newTask.getDescription(), newTask.getComments());
                else if (oldTask instanceof Question)
                    updateTaskAction = new UpdateQuestionAction(proc.getDbKey(), proc.getName(), oldTask.getDbKey(), oldTask.getDescription(), newTask.getDescription(), newTask.getComments(), ((Question)newTask).getHypothesis(), ((Question)newTask).getGeneralPrinciple());
                RedoEditTaskAction action = new RedoEditTaskAction(proc.getDbKey(), proc.getName(), updateTaskAction);
                cr = trace.addAction(action);
                if (cr.isError())
                    return cr;
            }
        }
        return new CopexReturn();
    }
    
    
    /* modification d'une tache */
    public CopexReturn updateTask(CopexTask newTask, LearnerProcedure proc, CopexTask oldTask, ArrayList v) {
       int idP = getIdProc(proc.getDbKey());
       if (idP == -1){
           String msg = edP.getBundleString("MSG_ERROR_UPDATE_STEP");
           if (newTask instanceof CopexAction)
               msg = edP.getBundleString("MSG_ERROR_UPDATE_ACTION");
           else if (newTask instanceof Question)
               msg = edP.getBundleString("MSG_ERROR_UPDATE_QUESTION");
           return new CopexReturn(msg, false);
       }
       LearnerProcedure expProc = listProc.get(idP);
        // on cherche dans la liste des taches l'indice de l'ancienne tache
        int idOld = getId(expProc.getListTask(), oldTask.getDbKey());
        if (idOld == -1){
            String msg = edP.getBundleString("MSG_ERROR_UPDATE_STEP");
            if (newTask instanceof CopexAction)
                msg = edP.getBundleString("MSG_ERROR_UPDATE_ACTION");
            else if (newTask instanceof Question)
                msg = edP.getBundleString("MSG_ERROR_UPDATE_QUESTION");
            return new CopexReturn(msg, false);
        }
        // mise à jour dans la base
        ArrayList v2 = new ArrayList();
        CopexReturn cr = db.updateTaskInDB(newTask, expProc.getDbKey(), oldTask, v2);
        if (cr.isError())
            return cr;
        newTask = (CopexTask)v2.get(0);
        // mise à jour date de modif 
        cr = db.updateDateProc(expProc);
        if (cr.isError()){
            return cr;
        }
       
        // mise à jour en mémoire 
        //oldTask.setComments(newTask.getComments());
        //oldTask.setDescription(newTask.getDescription());
        newTask.setDbKey(oldTask.getDbKey());
        newTask.setDbKeyBrother(oldTask.getDbKeyBrother());
        newTask.setDbKeyChild(oldTask.getDbKeyChild());
        newTask.setTaskImage(oldTask.getTaskImage());
        newTask.setTaskRight(oldTask.getTaskRight());
        newTask.setVisible(oldTask.isVisible());
        newTask.setRoot(oldTask.isQuestionRoot());
        if (newTask instanceof Question){
            //((Question)oldTask).setHypothesis(((Question)newTask).getHypothesis());
            ((Question)expProc.getListTask().get(idOld)).setHypothesis(((Question)newTask).getHypothesis());
            ((Question)expProc.getListTask().get(idOld)).setGeneralPrinciple(((Question)newTask).getGeneralPrinciple());
        }
        if (newTask instanceof CopexActionNamed){
            if ( expProc.getListTask().get(idOld) instanceof CopexActionNamed)
                ((CopexActionNamed)expProc.getListTask().get(idOld)).setNamedAction(((CopexActionNamed)newTask).getNamedAction());
            else{
                CopexTask ot = expProc.getListTask().get(idOld) ;
                CopexActionNamed an = new CopexActionNamed(ot.getDbKey(), ot.getName(), ot.getDescription(), ot.getComments(), ot.getTaskImage(),ot.getDraw(),  ot.isVisible(), ot.getTaskRight(),((CopexActionNamed)newTask).getNamedAction(), ot.getTaskRepeat() );
                expProc.getListTask().set(idOld, an);
            }
        }else if (newTask instanceof CopexAction){
            if ( expProc.getListTask().get(idOld) instanceof CopexActionNamed){
                CopexTask ot = expProc.getListTask().get(idOld) ;
                CopexAction a = new CopexAction(ot.getDbKey(), "", ot.getDescription(), ot.getComments(), ot.getTaskImage(),ot.getDraw(),  ot.isVisible(),ot.getTaskRight(), ot.getTaskRepeat());
                expProc.getListTask().set(idOld, a);
            }
        }
        expProc.getListTask().get(idOld).setDescription(newTask.getDescription());
        expProc.getListTask().get(idOld).setComments(newTask.getComments());
        if (expProc.getListTask().get(idOld).getDbKey() == expProc.getQuestion().getDbKey()){
            // on met à jour la question du proc egalement
            expProc.getQuestion().setDescription(newTask.getDescription());
            expProc.getQuestion().setComments(newTask.getComments());
            if (newTask instanceof Question){
                expProc.getQuestion().setHypothesis(((Question)newTask).getHypothesis());
                expProc.getQuestion().setGeneralPrinciple(((Question)newTask).getGeneralPrinciple());
            }
        }
        
        // en v[0] le protocole mis à jour 
        v.add((LearnerProcedure)expProc.clone());
        return new CopexReturn();
    }

    /* creation d'un nouveau protocole vierge */
    @Override
    public CopexReturn createProc(String procName, InitialProcedure initProc ) {
        // charge le proc initial
        CopexMission m =  initProc.getMission();
        ArrayList v = new ArrayList();
        ArrayList<Long> listIdInitProc = new ArrayList();
        listIdInitProc.add(initProc.getDbKey());
        CopexReturn cr = ExperimentalProcedureFromDB.getInitialProcFromDB(db.getDbC(), locale,m.getDbKey(), dbKeyUser, listIdInitProc, listPhysicalQuantity, v);
        if(cr.isError())
            return cr;
        ArrayList<InitialProcedure> l = (ArrayList<InitialProcedure>)v.get(0);
        initProc = l.get(0);
        initProc.setMission(m);
        return copyProc(procName, initProc, false, true, false);
    }
    
    /* creation d'un nouveau protocole vierge */
    public CopexReturn createProc(String procName, InitialProcedure initProc, boolean setTrace ) {
        return copyProc(procName, initProc, false, setTrace, false);
    }
    
    
    /* copie d'un protocole existant */
    @Override
    public CopexReturn copyProc(String name, LearnerProcedure procToCopy){
        return copyProc(name, procToCopy, true, true, true);
    }

    public CopexReturn copyProc(String name, InitialProcedure initProc, boolean copy, boolean setTrance, boolean loadProcCopy){
        ExperimentalProcedure proc = (ExperimentalProcedure)initProc.clone();
        LearnerProcedure learnerProc = new LearnerProcedure(proc,  initProc);
        return copyProc(name, learnerProc, copy, setTrance, loadProcCopy);
    }

    /* copie d'un protocole existant */
    public CopexReturn copyProc(String name, LearnerProcedure procToCopy, boolean copy,boolean setTrace, boolean loadProcCopy) {
        if (loadProcCopy){
            ArrayList v = new ArrayList();
            CopexReturn cr = loadProc(procToCopy, false, v);
            if (cr.isError())
                return cr;
            procToCopy = (LearnerProcedure)v.get(0);
        }
        LearnerProcedure proc = (LearnerProcedure)procToCopy.clone();
        proc.setName(name);
        proc.setRight(MyConstants.EXECUTE_RIGHT);
        // enregistre en base et on recupere son id
        // enregistrement du protocole
        ArrayList v = new ArrayList();
        CopexReturn cr = ExperimentalProcedureFromDB.createProcedureInDB_xml(db.getDbC(), proc, dbKeyUser, v);
        if (cr.isError()){
            return cr;
        }
        
        long dbKeyProc = (Long)v.get(0);
        proc.setDbKey(dbKeyProc);
        long dbKeyQuestion = (Long)v.get(1);
        proc.getQuestion().setDbKey(dbKeyQuestion);
        proc.getListTask().get(0).setDbKey(dbKeyQuestion);
        // lock
        setLocker(dbKeyProc);
        // enregistre les taches 
        v = new ArrayList();
        cr = TaskFromDB.createTasksInDB_xml(db.getDbC(), proc.getDbKey(), proc.getListTask(), proc.getQuestion(), false,  v);
        if (cr.isError()){
            return cr;
        }
        // on recupere les nouveaux id des taches
        ArrayList<CopexTask> listT = (ArrayList<CopexTask>)v.get(0);
        proc.setListTask(listT);
        // on cree le nouveau dataSheet s'il existe 
        if (proc.getDataSheet() != null){
            v = new ArrayList();
            cr = DataSheetFromDB.createDataSheetInDB_xml(db.getDbC(), dbKeyProc, proc.getDataSheet(), v);
            if (cr.isError()){
                return cr;
            }
            // recupere le nouvel id
            long dbKeyDataSheet = (Long)v.get(0);
            proc.getDataSheet().setDbKey(dbKeyDataSheet);
            // creation des data eventuelles
            int nbR = proc.getDataSheet().getNbRows();
            int nbC = proc.getDataSheet().getNbColumns();
            for (int i=0; i<nbR; i++){
                for (int j=0; j<nbC; j++){
                    if (proc.getDataSheet().getDataAt(i, j) != null){
                        v = new ArrayList();
                        cr = DataSheetFromDB.updateDataSheetInDB_xml(db.getDbC(), proc.getDataSheet().getDbKey(), -1, proc.getDataSheet().getDataAt(i, j).getData(), i, j, v);
                        if (cr.isError()){
                            return cr;
                        }
                        CopexData newData = (CopexData)v.get(0);
                        proc.getDataSheet().setValueAt(newData, i, j);
                    }
                }
            }
        }
        // mise à jour date de modif 
        cr = db.updateDateProc(proc);
        if (cr.isError()){
            return cr;
        }
        
        
        // memoire
        proc.setOpen(true);
        this.listProc.add(proc);
        // trace
        if (setTrace() && setTrace){
            CopyProcAction copyProcAction = new CopyProcAction(proc.getDbKey(), proc.getName(), procToCopy.getDbKey(), procToCopy.getName());
            CreateProcAction createProcAction = new CreateProcAction(proc.getDbKey(), proc.getName());
            if (copy){
                cr = trace.addAction(copyProcAction);
             }else{
               // creation de protocole
                cr = trace.addAction(createProcAction);
            }
        }
        //ihm
        edP.createProc((LearnerProcedure)proc.clone());
        return cr;
    }

    /* ouverture d'un protocole existant */
    @Override
    public CopexReturn openProc(CopexMission missionToOpen, LearnerProcedure procToOpen) {
        System.out.println("controller : openProc");
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
            // c'est peut etre un proc deja ouvert
            if (missionToOpen.getDbKey() == this.mission.getDbKey()){
                int idP = getIdProc(procToOpen.getDbKey());
                if (idP == -1)
                    return new CopexReturn(edP.getBundleString("MSG_ERROR_OPEN_PROC"), false);
                listProc.get(idP).setOpen(true);
                p = (LearnerProcedure)listProc.get(idP).clone();
            }else 
                return new CopexReturn(edP.getBundleString("MSG_ERROR_OPEN_PROC"), false);
        }else{
            ArrayList<LearnerProcedure> listP = this.listProcMissionUser.get(idM);
            int idP = getIdProc(listP, procToOpen.getDbKey());
            if (idP == -1)
                return new CopexReturn(edP.getBundleString("MSG_ERROR_OPEN_PROC"), false);
            // on l'ajoute à la liste si il n'y est pas deja
            p = listP.get(idP);
            System.out.println("openProc chargement du proc");
            ArrayList v = new ArrayList();
            CopexReturn cr = loadProc(p, true, v);
            if (cr.isError())
                return cr;
            p = (LearnerProcedure)v.get(0);
            p.setOpen(true);
            this.listProc.add(p);
        }
        // trace 
        CopexReturn cr = new CopexReturn();
        if (setTrace()){
            cr = trace.addAction(new OpenProcAction(p.getDbKey(), p.getName(), p.getMission().getDbKey(), p.getMission().getCode()));
        }
        //ihm
        edP.createProc((LearnerProcedure)p.clone());
        return cr;
    }

    /* fermeture d'un protocole */
    @Override
    public CopexReturn closeProc(LearnerProcedure proc) {
        if (proc.getDbKey() == -2){
            edP.closeProc(proc);
            return new CopexReturn();
        }
        int id = getIdProc(proc.getDbKey());
        if (id == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_CLOSE_PROC"), false);
        //listProc.remove(id);
        listProc.get(id).setOpen(false);
        unsetLocker(listProc.get(id));
        // IHM
        edP.closeProc((LearnerProcedure)proc.clone());
        return new CopexReturn();
    }

    /* suppression d'un protocole */
    @Override
    public CopexReturn deleteProc(LearnerProcedure proc) {
       int id = getIdProc(proc.getDbKey());
       if (id == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_DELETE_PROC"), false);
       // suprression en base
       // deverouille
       unsetLocker(proc);
       CopexReturn cr = db.removeProcInDB(listProc.get(id), dbKeyUser);
       if (cr.isError())
           return cr;
       // mise à jour date de modif 
        cr = db.updateDateMission(proc.getMission().getDbKey());
        if (cr.isError()){
            return cr;
        }
        
       // suppression en memoire 
       listProc.remove(id);
       // trace 
       if (setTrace()){
           DeleteProcAction action = new DeleteProcAction(proc.getDbKey(), proc.getName());
           cr = trace.addAction(action);
           
       }
        // IHM
       edP.deleteProc((LearnerProcedure)proc.clone());
       return cr;
    }

     

    /* retourne en v[0] la liste des protocoles qui peuvent être copiés : 
     * liste des protocoles de la mission en cours
     * retourne en v[1] la liste des missions de l'utilisateur et en v[2]) la 
     * liste des protocoles pour chacune de ces missions
     */
    @Override
    public CopexReturn getListProcToCopyOrOpen(ArrayList v) {
        ArrayList<LearnerProcedure> listProcToCopy = new ArrayList();
        ArrayList<CopexMission> listMission = new ArrayList();
        ArrayList<ArrayList<LearnerProcedure>> listProcToOpen = new ArrayList();
        //  liste des protocoles de la mission : listProc
        int nbP = listProc.size();
        for (int k=0; k<nbP; k++){
            listProcToCopy.add((LearnerProcedure)listProc.get(k).clone());
        }
        // liste des missions / protocoles associés 
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
        // on ajoute les protocoles de la mission en cours qui sont fermés
        int nbPClose = 0;
        ArrayList<LearnerProcedure> procClose = new ArrayList();
        int n = listProc.size();
        for (int k=0; k<n; k++){
            if (!listProc.get(k).isOpen() && listProc.get(k).getMission().getDbKey() == mission.getDbKey()){
                procClose.add((LearnerProcedure)listProc.get(k).clone());
                nbPClose++;
            }
        }
        // on ajoute la mission en cours
        if (nbPClose > 0){
            listMission.add((CopexMission)this.mission.clone());
            listProcToOpen.add(procClose);
        }
        
        
        v.add(listProcToCopy);
        v.add(listMission);
        v.add(listProcToOpen);
        return new CopexReturn();
    }

    /* mise à jour du nom du protocole */
    @Override
    public CopexReturn updateProcName(LearnerProcedure proc, String name, char undoRedo) {
        String oldName = name;
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_RENAME_PROC"), false);
        LearnerProcedure procC = listProc.get(idP);
        // modification en base
        CopexReturn cr = db.updateProcName(procC.getDbKey(), name);
        if (cr.isError())
            return cr;
        // mise à jour date de modif 
        cr = db.updateDateProc(procC);
        if (cr.isError()){
            return cr;
        }
        
        // en memoire
        listProc.get(idP).setName(name);
        
        // trace 
        if (setTrace()){
            RenameProcAction action = new RenameProcAction(proc.getDbKey(), oldName, name);
            if (undoRedo == MyConstants.NOT_UNDOREDO){
                cr = trace.addAction(action);    
            }else if (undoRedo == MyConstants.UNDO){
                cr = trace.addAction(new UndoRenameProcAction(proc.getDbKey(), oldName, action));
            }else if (undoRedo == MyConstants.REDO){
                cr = trace.addAction(new RedoRenameProcAction(proc.getDbKey(), oldName, action));
            }
        }
        //ihm
        edP.updateProcName((LearnerProcedure)procC.clone(), new String(name));
        return cr;
    }
    
    /* mise à jour du statut des protocoles */
    @Override
    public CopexReturn setProcActiv(LearnerProcedure proc){
        if (proc.getMission().getDbKey() == mission.getDbKey()){
            int idP = getIdProc(proc.getDbKey());
            if (idP == -1)
                return new CopexReturn(edP.getBundleString("MSG_ERROR_UPDATE_PROC"), false);
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

    /* creation d'une feuille de données vierge pour le protocole */
    @Override
    public CopexReturn createDataSheet(LearnerProcedure proc, int nbR, int nbC, char undoRedo, ArrayList v) {
        DataSheet dataSheet = new DataSheet(nbR, nbC);
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_CREATE_DATASHEET"), false);
        // enregistre dans la base 
        
        // enregistrement du protocole
        ArrayList v2 = new ArrayList();
        CopexReturn cr = DataSheetFromDB.createDataSheetInDB_xml(db.getDbC(), proc.getDbKey(), dataSheet, v2);
        if (cr.isError()){
            return cr;
        }
        // recupere l'id
        long dbKeyDataSheet = (Long)v2.get(0);
        dataSheet.setDbKey(dbKeyDataSheet);
        // mise à jour date de modif 
        cr = db.updateDateProc(proc);
        if (cr.isError()){
            return cr;
        }
        
        // memoire
        listProc.get(idP).setDataSheet(dataSheet);
        // trace
        if (setTrace()){
            CreateDataSheetAction action = new CreateDataSheetAction(listProc.get(idP).getDbKey(), listProc.get(idP).getName(), nbR, nbC) ;
            if (undoRedo == MyConstants.NOT_UNDOREDO)
                cr = trace.addAction(action);
            //else if (undoRedo == CopexUtilities.UNDO)
              //  cr = trace.addAction(action);
            else if (undoRedo == MyConstants.REDO)
                cr = trace.addAction(new RedoCreateDataSheetAction(listProc.get(idP).getDbKey(), listProc.get(idP).getName(),action));
        }
        // ihm
        v.add((DataSheet)dataSheet.clone());
        edP.createDataSheet((LearnerProcedure)listProc.get(idP).clone());
        return cr;
    }

    /* suppression dataSheet */
    @Override
    public CopexReturn deleteDataSheet(LearnerProcedure proc, long dbkeyDataSheet, char undoRedo){
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_CREATE_DATASHEET"), false);
        // enregistre dans la base 
        ArrayList v2 = new ArrayList();
        CopexReturn cr = DataSheetFromDB.deleteDataSheetFromDB_xml(db.getDbC(), dbkeyDataSheet);
        if (cr.isError()){
            return cr;
        }
        // memoire
        listProc.get(idP).setDataSheet(null);
        // trace
        if (setTrace()){
            DeleteDataSheetAction action = new DeleteDataSheetAction(listProc.get(idP).getDbKey(), listProc.get(idP).getName()) ;
            if (undoRedo == MyConstants.UNDO)
                cr = trace.addAction(new UndoCreateDataSheetAction(listProc.get(idP).getDbKey(), listProc.get(idP).getName(),action));
        }
        // ihm
        edP.deleteDataSheet((LearnerProcedure)listProc.get(idP).clone());
        return cr;
    }
    
    
    /* modification de la feuille de données */
    @Override
    public CopexReturn modifyDataSheet(LearnerProcedure proc, int nbR, int nbC, char undoRedo) {
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_UPDATE_DATASHEET"), false);
        DataSheet dataSheet = proc.getDataSheet();
        int oldNbRow = dataSheet.getNbRows();
        int oldNbCol = dataSheet.getNbColumns();
        // enregistre dans la base 
        // ouvre la connection
        CopexReturn cr = DataSheetFromDB.updateRowAndColInDB_xml(db.getDbC(), dataSheet.getDbKey(), nbR, nbC);
        if (cr.isError())
           return cr;
        
        // au besoin on supprime les lignes et / ou colonnes sup
        if (nbR < oldNbRow){
            // suppression des lignes sup
            for (int i=nbR; i<oldNbRow; i++){
                for (int j=0; j<oldNbCol; j++){
                    if (dataSheet.getDataAt(i, j) != null){
                         cr = DataSheetFromDB.removeDataInDB_xml(db.getDbC(), dataSheet.getDbKeyData(i, j)) ;
                         if (cr.isError()){
                             return cr;
                         }
                    }
                }
            }
        }
        if (nbC < oldNbCol){
            // suppression des col sup
            for (int j=nbC; j<oldNbCol; j++){
                for (int i=0; i<oldNbRow; i++){
                    if (dataSheet.getDataAt(i, j) != null){
                        cr = DataSheetFromDB.removeDataInDB_xml(db.getDbC(), dataSheet.getDbKeyData(i, j)) ;
                        if (cr.isError()){
                            return cr;
                       }
                    }
                }
            }
        }
        // mise à jour date de modif 
        cr = db.updateDateProc(proc);
        if (cr.isError()){
           return cr;
        }
        
        // memoire
        dataSheet.update(nbR, nbC);
        listProc.get(idP).setDataSheet(dataSheet);
        // trace 
        if (setTrace()){
            UpdateDataSheetAction action = new UpdateDataSheetAction(proc.getDbKey(), proc.getName(), nbR, nbC) ;
            if (undoRedo == MyConstants.NOT_UNDOREDO)
                cr = trace.addAction(action);
            else if (undoRedo == MyConstants.UNDO)
                cr = trace.addAction(new UndoUpdateDataSheetAction(proc.getDbKey(), proc.getName(),action));
            else if (undoRedo == MyConstants.REDO)
                cr = trace.addAction(new RedoUpdateDataSheetAction(proc.getDbKey(), proc.getName(),action));
        }
        // ihm
        edP.updateDataSheet((LearnerProcedure)listProc.get(idP).clone());
        return cr;
    }

    /* modification d'une cellule d'une feuille de données*/
    @Override
    public CopexReturn updateDataSheet(LearnerProcedure proc, String value, int noRow, int noCol, ArrayList v, char undoRedo) {
        System.out.println("UDPATE DATASHEET");
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_UPDATE_DATASHEET"), false);
        // enregistre dans la base 
        
        // enregistrement du protocole
        DataSheet ds = proc.getDataSheet();
        long dbKeyData = ds.getDbKeyData(noRow, noCol);
        ArrayList v2 = new ArrayList();
        CopexReturn cr = DataSheetFromDB.updateDataSheetInDB_xml(db.getDbC(), ds.getDbKey(), dbKeyData, value, noRow, noCol, v2);
        if (cr.isError()){
            return cr;
        }
        // recupere eventuellement le nouvel data 
        CopexData newData = (CopexData)v2.get(0);
        // mise à jour date de modif 
        cr = db.updateDateProc(proc);
        if (cr.isError()){
             return cr;
        }
        
        // memoire
        listProc.get(idP).getDataSheet().setValueAt(newData, noRow, noCol);
        v.add(newData.clone());
        // trace
        if (setTrace()){
            ArrayList<DataXML> listXMLData = new ArrayList();
            DataXML xmlD = new DataXML(newData.getDbKey(), newData.getData(), noRow, noCol);
            listXMLData.add(xmlD);
            EditDataSheetAction action = new EditDataSheetAction(proc.getDbKey(), proc.getName(), listXMLData);
            if (undoRedo == MyConstants.NOT_UNDOREDO)
                cr = trace.addAction(action);
            else if (undoRedo == MyConstants.UNDO)
                cr = trace.addAction(new UndoEditDataSheetAction(proc.getDbKey(), proc.getName(),action));
            else if (undoRedo == MyConstants.REDO)
                cr = trace.addAction(new RedoEditDataSheetAction(proc.getDbKey(), proc.getName(),action));
        }
        return cr;
    }

    /* impression :
     * @param : boolean printProc : impression du protocole actif
     * @param : boolean printComments : impression des commentaires
     * @param : boolean printDataSheet : impression de la feuille de données associée au protocole
     */
    @Override
    public CopexReturn printCopex(LearnerProcedure procToPrint, boolean printComments, boolean printDataSheet) {
        CopexMission missionToPrint  =(CopexMission)mission.clone();
        ExperimentalProcedure proc = null;
        if (procToPrint != null){
            int idP = getIdProc(procToPrint.getDbKey());
            if (idP== -1)
                return new CopexReturn(edP.getBundleString("MSG_ERROR_PRINT"), false);
            proc = (ExperimentalProcedure)(listProc.get(idP).clone());
        }
        // enregistrement trace
        if (setTrace()){
            trace.addAction(new TraceFunction(TraceFunction.TRACE_PRINT));
        }
        //String fileName = "copexPrint"+dbKeyUser+"-"+mission.getDbKey() ;
        String fileName = "copex-"+mission.getCode();
        
        PrintPDF pdfPrint = new PrintPDF(edP, fileName, copexUser, missionToPrint,proc, printComments, printDataSheet);
        CopexReturn cr = pdfPrint.printDocument();
        return cr;
    }

    /* drag and drop */
    @Override
    public CopexReturn move(TaskSelected taskSel, SubTree subTree, char undoRedo) {
        System.out.println("***MOVE CONTROLLER***");
        System.out.println(" => taskSel : "+taskSel.getSelectedTask().getDescription());
        System.out.println(" => subTree : "+subTree.getFirstTask().getDescription());
        if (taskSel.getTaskBrother() != null){
            System.out.println(" => branche frere : "+taskSel.getTaskBrother().getDescription());
        }else{
            System.out.println(" => branche parent : "+taskSel.getTaskParent().getDescription());
        }
        ExperimentalProcedure proc = taskSel.getProc();
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_DRAG_AND_DROP"), false);
      
        LearnerProcedure expProc = listProc.get(idP);
           printRecap(expProc);
        ArrayList<CopexTask> listTask = subTree.getListTask();
        // on modifie les liens des taches en base 
        // enregistre les taches 
        // mise à jour date de modif 
        CopexReturn cr = db.updateDateProc(expProc);
        if (cr.isError()){
             return cr;
        }
        // on branche le sous arbre au protocole, en reconnectant eventuellement les liens de la tache selectionnee
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
            System.out.println("tache parent : "+taskParent.getDescription());
        }else{
            dbKeyT = taskBrother.getDbKey();
            System.out.println("tache frere : "+taskBrother.getDescription());
        }
        
        idB = getId(expProc.getListTask(), dbKeyT);
        // supprimer les anciens liens de la premiere et derniere tache 
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
        // premiere tache  
        TaskFromDB.deleteLinkParentInDB_xml(db.getDbC(), taskBranch.getDbKey());
        if (cr.isError()){
           return cr;
        }
        TaskFromDB.deleteLinkSubBrotherInDB_xml(db.getDbC(), taskBranch.getDbKey());
        if (cr.isError()){
            return cr;
        }
        // derniere tache 
        TaskFromDB.deleteLinkBrotherInDB_xml(db.getDbC(), lastTaskBranch.getDbKey());
        if (cr.isError()){
            return cr;
        }
        // reconnecte eventuellement un lien parent 
        
        if (parent != null){
             expProc.getListTask().get(idParent).setDbKeyChild(-1);
            // on branche le premier petit frere 
            if (subTree.getLastBrother() != -1){
                
                TaskFromDB.createLinkChildInDB_xml(db.getDbC(), parent.getDbKey(), subTree.getLastBrother());
                if (cr.isError()){
                    return cr;
                }
                expProc.getListTask().get(idParent).setDbKeyChild(subTree.getLastBrother());
            }
        }
        // on rebranche eventuellement les freres
        if (oldBrother != null){
            expProc.getListTask().get(idOldBrother).setDbKeyBrother(-1);
            if (subTree.getLastBrother() != -1){
                TaskFromDB.createLinkBrotherInDB_xml(db.getDbC(), oldBrother.getDbKey(), subTree.getLastBrother());
                if (cr.isError()){
                    return cr;
                }
                expProc.getListTask().get(idOldBrother).setDbKeyBrother(subTree.getLastBrother());
            }
        }
        expProc.getListTask().get(idLastTaskBranch).setDbKeyBrother(-1);
        printRecap(expProc);
        // le sous arbre est deconnecte => on l'insere au bon endroit 
        if (taskBrother == null){
            // branche en parent
            long firstChild = expProc.getListTask().get(idB).getDbKeyChild();
            taskParent.setDbKeyChild(taskBranch.getDbKey());
            // mise à jour dans la liste
            expProc.getListTask().get(idB).setDbKeyChild(taskBranch.getDbKey());
            cr = TaskFromDB.createLinkChildInDB_xml(db.getDbC(), expProc.getListTask().get(idB).getDbKey(), taskBranch.getDbKey());
            if (cr.isError()){
                return cr;
            }
            //si il y avait un fils on supprime le lien : 
            if (firstChild != -1){
                cr = TaskFromDB.deleteLinkParentInDB_xml(db.getDbC(), firstChild);
                if (cr.isError()){
                    return cr;
                }
                // on cree un nouveau lien frere entre lastTaskBranch et firstChild
                lastTaskBranch.setDbKeyBrother(firstChild);
                cr = TaskFromDB.createLinkBrotherInDB_xml(db.getDbC(), lastTaskBranch.getDbKey(), firstChild);
                if (cr.isError()){
                    return cr;
                }
                expProc.getListTask().get(idLastTaskBranch).setDbKeyBrother(firstChild);
                lastTaskBranch.setDbKeyBrother(firstChild);
            }
        }else{ // branche en frere 
            long dbKeyOldBrother = expProc.getListTask().get(idB).getDbKeyBrother();
            if (dbKeyOldBrother != -1)
                lastTaskBranch.setDbKeyBrother(dbKeyOldBrother);
            taskBrother.setDbKeyBrother(taskBranch.getDbKey());
            // mise à jour dans la liste
            expProc.getListTask().get(idB).setDbKeyBrother(taskBranch.getDbKey());
            cr = TaskFromDB.createLinkBrotherInDB_xml(db.getDbC(), expProc.getListTask().get(idB).getDbKey(), taskBranch.getDbKey());
            if (cr.isError()){
                return cr;
            }
            if (dbKeyOldBrother != -1){
                 expProc.getListTask().get(idLastTaskBranch).setDbKeyBrother(dbKeyOldBrother);
                lastTaskBranch.setDbKeyBrother(dbKeyOldBrother);
                cr = TaskFromDB.deleteLinkBrotherInDB_xml(db.getDbC(), expProc.getListTask().get(idB).getDbKey(), dbKeyOldBrother);
                if (cr.isError()){
                    return cr;
                }
                cr = TaskFromDB.createLinkBrotherInDB_xml(db.getDbC(), expProc.getListTask().get(idLastTaskBranch).getDbKey(), dbKeyOldBrother);
                if (cr.isError()){
                    return cr;
                }
            }
        }
        subTree.setLastBrother(lastTaskBranch.getDbKeyBrother());
        System.out.println("*** FIN MOVE CONTROLLER***");
        // trace 
        if (setTrace()){
            ArrayList<MyTask> listXMLTask  = new ArrayList();
            int nb = listTask.size();
            for (int i=0; i<nb; i++){
                CopexTask task = listTask.get(i);
                long dbKeyParent = -1;
                String descP = "";
                long dbKeyB = -1;
                String descB = null;
                CopexTask tp = getParentTask(expProc.getListTask(), task);
                if (tp != null){
                    dbKeyParent = tp.getDbKey();
                    descP = tp.getDescription();
                }
                CopexTask tb = getOldBrotherTask(expProc.getListTask(), task);
                if (tb != null){
                    dbKeyB = tb.getDbKey();
                    descB = tb.getDescription();
                    
                }
                MyTask xmlT = new MyTask(task.getDbKey(), task.getDescription(), dbKeyParent, descP, dbKeyB, descB);
                listXMLTask.add(xmlT);
            }
            DragDropAction action = new DragDropAction(proc.getDbKey(), proc.getName(), listXMLTask) ;
            if (undoRedo == MyConstants.NOT_UNDOREDO)
                cr = trace.addAction(action);
            else if (undoRedo == MyConstants.UNDO)
                cr = trace.addAction(new UndoDragAndDropAction(proc.getDbKey(), proc.getName(), action));
            else if (undoRedo == MyConstants.REDO)
                cr = trace.addAction(new RedoDragAndDropAction(proc.getDbKey(), proc.getName(), action));
           
            if (cr.isError())
                return cr;
        }
        //ihm
         printRecap(expProc);
        return new CopexReturn();
    }

    private void printRecap(LearnerProcedure proc){
        ArrayList<CopexTask> listTask = proc.getListTask();
        int n = listTask.size();
        System.out.println("************RECAP DES "+n+" TACHES DU PROC "+proc.getName()+" *****************");
        for (int k=0; k<n; k++){
            CopexTask task = listTask.get(k);
            String frere = " sans frere ";
            String enfant = " sans enfant ";
            if (task.getDbKeyBrother() != -1)
                frere = " "+task.getDbKeyBrother()+" ";
            if (task.getDbKeyChild() != -1)
                enfant = " "+task.getDbKeyChild()+" ";
            String visible = task.isVisible() ? "visible" :"cachée";
            System.out.println("  - Tâche "+task.getDescription()+" ("+task.getDbKey()+") : "+frere+" / "+enfant+ " ("+visible+")");
           
        }
        System.out.println("********************************************************");
        
    }
    
    
    @Override
    public CopexReturn finalizeDragAndDrop(LearnerProcedure proc) {
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_DRAG_AND_DROP"), false);
        
        edP.updateProc((LearnerProcedure)listProc.get(idP).clone());
        return new CopexReturn();
    }
    
    /* sauvegarde des protocoles */
    public CopexReturn saveProcXML(){
        if (listProc == null)
            return new CopexReturn();
        int nb = listProc.size();
        for (int i=0; i<nb; i++){
            ArrayList v = new ArrayList();
            CopexReturn cr = db.getDateAndTime(v);
            String date = CopexUtilities.getCurrentDate().toString();
            String time = CopexUtilities.getCurrentTime().toString();
            if (cr.isError()){
                System.out.println("Error Date/Time : "+cr.getText());
            }else{
                date = (String)v.get(0);
                time = (String)v.get(1);
            }
            cr = SaveXMLProc.saveProc(applet, dbKeyUser, listProc.get(i), date, time);
            if (cr.isError())
                return cr;
        }
        return new CopexReturn();
    }
    
    /* arret de l'edp */
    @Override
    public CopexReturn stopEdP(){
        // deverouille les proc
        unsetLockers(this.listProc);
        this.locker.stop();

        // sauvegarde des taches vis ou non
        CopexReturn cr = saveTaskVisible();
        if (cr.isError())
               return cr;
        // enregistrement trace
        if (setTrace()){
            trace.addAction(new TraceFunction(TraceFunction.TRACE_CLOSE_EDITOR));
        }
        // sauvegarde proc intermediaire
        return saveProcXML();
    }

    /* mise à jour des taches visibles */
    private CopexReturn saveTaskVisible(){
        if (listProc == null)
            return new CopexReturn();
        int nb = listProc.size();
        CopexReturn cr = new CopexReturn();
        for (int i=0; i<nb; i++){
            cr = TaskFromDB.updateTaskVisibleInDB_xml(db.getDbC(), listProc.get(i).getListTask());
            if (cr.isError())
                return cr;
        }
        return cr;
    }
    
    
    /* mise à jour de l'etat visible des taches du proc */
    // MBO le 22/10/08 : on ne fera la maj en base qu'en sortie sinon ralentissement de l'appli
    @Override
    public CopexReturn updateTaskVisible(LearnerProcedure proc, ArrayList<CopexTask> listTask){
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_TASK_VISIBLE"), false);
        LearnerProcedure expProc = listProc.get(idP);
        // mise à jour dans la base
       // CopexReturn cr = TaskFromDB.updateTaskVisibleInDB_xml(db.getDbC(), listTask);
       // if (cr.isError())
       //     return cr;
        // mise à jour en memoire
        int nb = listTask.size();
        for (int i=0; i<nb; i++){
            int idTask = getId(expProc.getListTask(), listTask.get(i).getDbKey());
            if (listTask.get(i).getDbKey() == 103)
                System.out.println("updateTaskVisible : "+listTask.get(i).isVisible());
            if (idTask == -1)
                return new CopexReturn(edP.getBundleString("MSG_ERROR_TASK_VISIBLE"), false);
            expProc.getListTask().get(idTask).setVisible(listTask.get(i).isVisible());
        }
        return new CopexReturn();
    }
    
    
    
    
    
    
   
    
    /* retourne les donnees pour l'aide */
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
   /* protocole aide */
    private CopexReturn loadHelpProc(){
        // mission
        OptionMission options = new OptionMission(false, false, false);
        helpMission = new CopexMission(edP.getBundleString("HELP_MISSION_CODE"), edP.getBundleString("HELP_MISSION_NAME"), "", "", options);
        helpMission.setDbKey(-2);
        //protocole
        InitialProcedure initProc = new InitialProcedure(-2, "help proc", null, false,MyConstants.NONE_RIGHT, "help proc", true,null);
        helpProc = new LearnerProcedure(edP.getBundleString("PROC_HELP_PROC_NAME"), helpMission, CopexUtilities.getCurrentDate(), initProc);
        helpProc.setRight(MyConstants.NONE_RIGHT);
        helpProc.setDbKey(-2);
        // liste des taches
        ArrayList<CopexTask> listTask = new ArrayList();
        TaskRight tr = new TaskRight(MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT);
        Question question = new Question(1, "question", edP.getBundleString("PROC_HELP_QUESTION"), "", "", null, null,edP.getBundleString("PROC_HELP_GENERAL_PRINCIPLE"), true, tr, true, -1, 2);
        listTask.add(question);
        helpProc.setQuestion(question);
        Step step1 = new Step(2, "step1", edP.getBundleString("PROC_HELP_STEP_1"), "", null, null,true, tr, 9, 3, null);
        listTask.add(step1);
        CopexAction action1_1 = new CopexAction(3, "action1-1", edP.getBundleString("PROC_HELP_ACTION_1_1"), "", null,  null,true, tr, 4, -1, null);
        listTask.add(action1_1);
        CopexAction action1_2 = new CopexAction(4, "action1-2", edP.getBundleString("PROC_HELP_ACTION_1_2"), "", null, null,true, tr, 5, -1, null);
        listTask.add(action1_2);
        CopexAction action1_3 = new CopexAction(5, "action1-3", edP.getBundleString("PROC_HELP_ACTION_1_3"), "", null,null,true, tr, 6, -1, null);
        listTask.add(action1_3);
        CopexAction action1_4 = new CopexAction(6, "action1-4", edP.getBundleString("PROC_HELP_ACTION_1_4"), "", null, null,true, tr, 7, -1, null);
        listTask.add(action1_4);
        CopexAction action1_5 = new CopexAction(7, "action1-5", edP.getBundleString("PROC_HELP_ACTION_1_5"), "", null, null,true, tr, 8, -1, null);
        listTask.add(action1_5);
        CopexAction action1_6 = new CopexAction(8, "action1-6", edP.getBundleString("PROC_HELP_ACTION_1_6"), "", null, null,true, tr, -1, -1, null);
        listTask.add(action1_6);
        Step step2 = new Step(9, "step2", edP.getBundleString("PROC_HELP_STEP_2"), "", null, null,true, tr, 14, 10, null);
        listTask.add(step2);
        CopexAction action2_1 = new CopexAction(10, "action2-1", edP.getBundleString("PROC_HELP_ACTION_2_1"), "", null, null,true, tr, 11, -1, null);
        listTask.add(action2_1);
        CopexAction action2_2 = new CopexAction(11, "action2-2", edP.getBundleString("PROC_HELP_ACTION_2_2"), "", null, null,true, tr, 12, -1, null);
        listTask.add(action2_2);
        CopexAction action2_3 = new CopexAction(12, "action2-3", edP.getBundleString("PROC_HELP_ACTION_2_3"), "", null, null,true, tr, 13, -1, null);
        listTask.add(action2_3);
        CopexAction action2_4 = new CopexAction(13, "action2-4", edP.getBundleString("PROC_HELP_ACTION_2_4"), "", null, null,true, tr, -1, -1, null);
        listTask.add(action2_4);
        CopexAction action_3 = new CopexAction(14, "action_3", edP.getBundleString("PROC_HELP_ACTION_3"), "", null,null,true, tr, 15, -1, null);
        listTask.add(action_3);
        CopexAction action_4 = new CopexAction(15, "action_4", edP.getBundleString("PROC_HELP_ACTION_4"), "", null, null,true, tr, -1, -1, null);
        listTask.add(action_4);
        helpProc.setListTask(listTask);
        // type de materiel
        TypeMaterial typeUstensil = new TypeMaterial(1, edP.getBundleString("HELP_TYPE_MATERIAL_USTENSIL"));
        TypeMaterial typeIngredient = new TypeMaterial(2, edP.getBundleString("HELP_TYPE_MATERIAL_USTENSIL"));
        // material
        listHelpMaterial = new ArrayList();
        Material m = new Material(1, edP.getBundleString("HELP_MATERIAL_CUP"), "");
        m.setAdvisedLearner(true);
        m.addType(typeUstensil);
        MaterialUseForProc matUse = new MaterialUseForProc(m);
        helpProc.addMaterialUse(matUse);
        listHelpMaterial.add(m);
        m = new Material(2, edP.getBundleString("HELP_MATERIAL_BAG"), "");
        m.setAdvisedLearner(true);
        m.addType(typeIngredient);
        matUse = new MaterialUseForProc(m);
        helpProc.addMaterialUse(matUse);
        listHelpMaterial.add(m);
        m = new Material(3, edP.getBundleString("HELP_MATERIAL_SPOON"), "");
        m.setAdvisedLearner(true);
        m.addType(typeUstensil);
        matUse = new MaterialUseForProc(m);
        helpProc.addMaterialUse(matUse);
        listHelpMaterial.add(m);
        m = new Material(4, edP.getBundleString("HELP_MATERIAL_SUGAR"), "");
        m.setAdvisedLearner(true);
        m.addType(typeIngredient);
        matUse = new MaterialUseForProc(m);
        helpProc.addMaterialUse(matUse);
        listHelpMaterial.add(m);
        m = new Material(5, edP.getBundleString("HELP_MATERIAL_WATER"), "");
        m.setAdvisedLearner(true);
        m.addType(typeIngredient);
        matUse = new MaterialUseForProc(m);
        helpProc.addMaterialUse(matUse);
        listHelpMaterial.add(m);
        m = new Material(6, edP.getBundleString("HELP_MATERIAL_KETTLE"), "");
        m.setAdvisedLearner(true);
        m.addType(typeUstensil);
        matUse = new MaterialUseForProc(m);
        helpProc.addMaterialUse(matUse);
        listHelpMaterial.add(m);
        m = new Material(7, edP.getBundleString("HELP_MATERIAL_GAZ_COOKER"), "");
        m.setAdvisedLearner(true);
        m.addType(typeUstensil);
        matUse = new MaterialUseForProc(m);
        helpProc.addMaterialUse(matUse);
        listHelpMaterial.add(m);
        initProc.setListMaterial(listHelpMaterial);
       return new CopexReturn(); 
    }

    /* ajout de l'utilisation d'un materiel pour un proc*/
    @Override
    public CopexReturn addMaterialUseForProc(LearnerProcedure p, Material m, String justification, char undoRedo) {
        int idP = getIdProc(p.getDbKey());
        if (idP == -1){
            return new CopexReturn(edP.getBundleString("MSG_ERROR_UPDATE_MATERIAL_USE"), false);
        }
        LearnerProcedure proc = listProc.get(idP);
        MaterialUseForProc matUse = new MaterialUseForProc(m, justification);
        CopexReturn cr = ExperimentalProcedureFromDB.addMaterialUseForProcInDB_xml(db.getDbC(), proc.getDbKey(), m.getDbKey(), justification);
        if (cr.isError())
            return cr;
        // mise à jour date de modif 
        cr = db.updateDateProc(proc);
        if (cr.isError()){
            return cr;
        }
        proc.addMaterialUse(matUse);
        // trace
        if (setTrace()){
            AddMaterialUseForProcAction action = new AddMaterialUseForProcAction(proc.getDbKey(), proc.getName(),m.getDbKey(), m.getName(), justification);
            if (undoRedo == MyConstants.NOT_UNDOREDO)
                cr = trace.addAction(action);
            else if (undoRedo == MyConstants.UNDO)
                cr = trace.addAction(new UndoDeleteMaterialUseForProcAction(proc.getDbKey(), proc.getName(),action));
            else if (undoRedo == MyConstants.REDO)
                cr = trace.addAction(new RedoAddMaterialUseForProcAction(proc.getDbKey(), proc.getName(),action));
        }
        return new CopexReturn();
    }

    /* modification de l'utilisation d'un materiel pour un proc*/
    @Override
    public CopexReturn updateMaterialUseForProc(LearnerProcedure p, Material m, String justification, char undoRedo) {
        int idP = getIdProc(p.getDbKey());
        if (idP == -1){
            return new CopexReturn(edP.getBundleString("MSG_ERROR_UPDATE_MATERIAL_USE"), false);
        }
        LearnerProcedure proc = listProc.get(idP);
        MaterialUseForProc matUse = new MaterialUseForProc(m, justification);
        CopexReturn cr = ExperimentalProcedureFromDB.updateMaterialUseForProcInDB_xml(db.getDbC(), proc.getDbKey(), m.getDbKey(), justification);
        if (cr.isError())
            return cr;
         // mise à jour date de modif 
        cr = db.updateDateProc(proc);
        if (cr.isError()){
            return cr;
        }
        String oldJustification = "";
        MaterialUseForProc oldM = proc.getMaterialUse(m);
        if (oldM != null && oldM.getJustification() != null)
            oldJustification = oldM.getJustification();
        proc.updateMaterialUse(matUse);
        // trace
        if (setTrace()){
            UpdateMaterialUseForProcAction action = new UpdateMaterialUseForProcAction(proc.getDbKey(), proc.getName(),m.getDbKey(), m.getName(), oldJustification, justification);
            if (undoRedo == MyConstants.NOT_UNDOREDO)
                cr = trace.addAction(action);
            else if (undoRedo == MyConstants.UNDO)
                cr = trace.addAction(new UndoUpdateMaterialUseForProcAction(proc.getDbKey(), proc.getName(),action));
            else if (undoRedo == MyConstants.REDO)
                cr = trace.addAction(new RedoUpdateMaterialUseForProcAction(proc.getDbKey(), proc.getName(),action));
        }
        return new CopexReturn();
    }
    
    /* suppression de l'utilisation d'un materiel pour un proc*/
    @Override
    public CopexReturn removeMaterialUseForProc(LearnerProcedure p, Material m, char undoRedo) {
        int idP = getIdProc(p.getDbKey());
        if (idP == -1){
            return new CopexReturn(edP.getBundleString("MSG_ERROR_UPDATE_MATERIAL_USE"), false);
        }
        LearnerProcedure proc = listProc.get(idP);
        CopexReturn cr = ExperimentalProcedureFromDB.removeMaterialUseForProcFromDB_xml(db.getDbC(), proc.getDbKey(), m.getDbKey());
        if (cr.isError())
            return cr;
         // mise à jour date de modif 
        cr = db.updateDateProc(proc);
        if (cr.isError()){
            return cr;
        }
        proc.removeMaterialUse(m);
        // trace
        if (setTrace()){
            DeleteMaterialUseForProcAction action = new DeleteMaterialUseForProcAction(proc.getDbKey(), proc.getName(),m.getDbKey(), m.getName());
            if (undoRedo == MyConstants.NOT_UNDOREDO)
                cr = trace.addAction(action);
            else if (undoRedo == MyConstants.UNDO)
                cr = trace.addAction(new UndoAddMaterialUseForProcAction(proc.getDbKey(), proc.getName(),action));
            else if (undoRedo == MyConstants.REDO)
                cr = trace.addAction(new RedoDeleteMaterialUseForProcAction(proc.getDbKey(), proc.getName(),action));
        }
        return new CopexReturn();
    }

    /* exportation */
    @Override
    public CopexReturn exportDataSheet(LearnerProcedure p, File file){
        int idP = getIdProc(p.getDbKey());
        if (idP == -1)
             return new CopexReturn(edP.getBundleString("MSG_ERROR_EXPORT_DATASHEET"), false);
        LearnerProcedure myProc = this.listProc.get(idP);
        DataSheet ds = myProc.getDataSheet() ;
        if (ds == null)
            return new CopexReturn();
        WritableWorkbook workbook = null ;
        try{
                workbook = Workbook.createWorkbook(file);
        }catch(IOException e){
            System.out.println("Erreur lors de la creation du fichier xls");
            return new CopexReturn(edP.getBundleString("MSG_ERROR_EXPORT_DATASHEET"), false);
        }
        try{
            WritableSheet sheet = workbook.createSheet(p.getName(), 0);
            int nbR = ds.getNbRows() ;
            int nbC = ds.getNbColumns() ;
            for (int i=0; i<nbR; i++){
                for (int j=0; j<nbC; j++){
                    String s = "";
                    CopexData d = ds.getDataAt(i, j) ;
                    if(d != null){
                        s = d.getData();
                    }
                    sheet.addCell(new Label(j, i, s));
                }
            }
        }catch(WriteException e){
            return new CopexReturn(edP.getBundleString("MSG_ERROR_EXPORT_DATASHEET")+" "+e.getMessage(), false);
        }


        try{
            workbook.write() ;
            workbook.close();
        }catch(IOException e){
            System.out.println("Erreur lors de la creation du fichier xls");
            return new CopexReturn(edP.getBundleString("MSG_ERROR_EXPORT_DATASHEET"), false);
        }
        return new CopexReturn();
    }
   

    /* pose les verrous sur un proc */
    private void setLocker(long dbKeyProc){
        this.locker.setProcLocker(dbKeyProc);
    }


    /* pose les verrous sur une liste proc */
    private void setLockers(ArrayList<LearnerProcedure> listP){
        int nb = listP.size();
        System.out.println("demande pose verrou "+nb);
        ArrayList listId = new ArrayList();
        for (int i=0; i<nb; i++){
            listId.add(listP.get(i).getDbKey());
        }
        this.locker.setProcLockers(listId);
    }

    /* deverouille un proc */
    private void unsetLocker(LearnerProcedure proc){
        this.locker.unsetProcLocker(proc.getDbKey());
    }
    /* deverouille une liste de proc */
    private void unsetLockers(ArrayList<LearnerProcedure> listP){
        int nb = listP.size();
        System.out.println("deverouille : "+nb);
        ArrayList listId = new ArrayList();
        for (int i=0; i<nb; i++){
            listId.add(listP.get(i).getDbKey());
        }
        this.locker.unsetProcLockers(listId);
    }

    /* chargement de l'utilisateur selon le mode - retourne en v[0] le dbKeyUser de COPEX */
    private CopexReturn loadUser(String idUser, int mode, String userName, String firstName, long dbKeyMission, ArrayList v){
        System.out.println("loadUser : "+mode+", "+idUser);
        if(mode == MyConstants.MODE_APPLET_COPEX){
            long id = -1;
            try{
                id = Long.parseLong(idUser);
            }catch(NumberFormatException e){
                return new CopexReturn(edP.getBundleString("MSG_ERROR_APPLET_PARAM"), false);
            }
            v.add(id);
            return new CopexReturn();
        }else if (mode == MyConstants.MODE_APPLET_SCY){
           long id = -1;
            try{
                id = Long.parseLong(idUser);
            }catch(NumberFormatException e){
                return new CopexReturn(edP.getBundleString("MSG_ERROR_APPLET_PARAM"), false);
            }
            v.add(id);
            return new CopexReturn();
        }else if (mode == MyConstants.MODE_APPLET_LOE){
            // recherche utilisateur correspondant
            ArrayList v2 = new ArrayList();
            CopexReturn cr = UserFromDB.getEdPUserFromDB(db.getDbC(), idUser, MyConstants.BD_CHAMP_LOE, v2);
            if (cr.isError())
                return cr;
            long idU = (Long)v2.get(0);
            if(idU == -1){
                // creation d'un nouvel utilisateur
                CopexLearner learner = new CopexLearner(idUser, "copex", userName, firstName);
                v2 = new ArrayList();
                cr = UserFromDB.createUserInDB(db.getDbC(), learner, v2);
                if(cr.isError())
                    return cr;
                idU = (Long)v2.get(0);
                // creation lien loe / edp
                cr = UserFromDB.createEdPUsersInDB(db.getDbC(), idU, idUser, MyConstants.BD_CHAMP_LOE);
                if(cr.isError())
                    return cr;
                // creation d'un lien vers la mission
                cr = MissionFromDB.createUserMissionInDB(db.getDbC(),idU, dbKeyMission );
                if (cr.isError())
                    return cr;
                v.add(idU);
                return new CopexReturn();
            }else{
                v.add(idU);
                return new CopexReturn();
            }
        }
        return new CopexReturn(edP.getBundleString("MSG_ERROR_APPLET_PARAM"), false);
    }

    /* ouverture fenetre dialogue*/
    @Override
    public CopexReturn openHelpDialog() {
        if(setTrace()){
            return trace.addAction(new TraceFunction(TraceFunction.TRACE_OPEN_HELP));
        }
        return new CopexReturn();
    }

    /* fermeture fenetre aide */
    @Override
    public CopexReturn closeHelpDialog() {
        if(setTrace()){
            return trace.addAction(new TraceFunction(TraceFunction.TRACE_CLOSE_HELP));
        }
        return new CopexReturn();
    }

    /* ouverture proc aide*/
    @Override
    public CopexReturn openHelpProc() {
        if(setTrace()){
            return trace.addAction(new TraceFunction(TraceFunction.TRACE_OPEN_PROC_HELP));
        }
        return new CopexReturn();
    }
    /* retourne l'elo exp proc */
    @Override
    public Element getExperimentalProcedure(LearnerProcedure proc){
        return null;
    }

    /* chargement d'un ELO*/
    @Override
    public CopexReturn loadELO(Element xmlContent){
        return new CopexReturn();
    }

     /*  creation d'un nouvel ELO*/
    @Override
    public CopexReturn newELO(){
        return new CopexReturn();
    }

}
