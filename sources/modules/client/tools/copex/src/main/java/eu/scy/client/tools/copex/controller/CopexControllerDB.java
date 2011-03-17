/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.controller;


import org.jdom.Document;
import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.db.*;
import eu.scy.client.tools.copex.dnd.SubTree;
import eu.scy.client.tools.copex.edp.CopexPanel;
import eu.scy.client.tools.copex.edp.TaskSelected;
import eu.scy.client.tools.copex.logger.CopexProperty;
import eu.scy.client.tools.copex.logger.TaskTreePosition;
import eu.scy.client.tools.copex.print.CopexHTML;
import eu.scy.client.tools.copex.print.PrintPDF;
import eu.scy.client.tools.copex.profiler.Profiler;
import eu.scy.client.tools.copex.synchro.Locker;
import eu.scy.client.tools.copex.utilities.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
 * 03/03/09 : plusieurs proc initiaux lies a une mission
 * copex controller labbook
 * @author Marjolaine
 */
public class CopexControllerDB implements ControllerInterface {
    /*locale */
    private Locale locale;
    /* acces bd */
    private AccesDB db;
    /* copex */
    private CopexPanel copex;
    /*URL */
    private URL copexURL;
    /* utilisateur */
    private long dbKeyGroup;
    /* utilisateur */
    private CopexGroup group;
    private long dbKeyUser;
    private long dbKeyLabDoc;
    /* liste des grandeurs physiques gerees dans COPEX */
    private ArrayList<PhysicalQuantity> listPhysicalQuantity ;
    /* liste de sstrategy de material dans copex */
    private ArrayList<MaterialStrategy> listMaterialStrategy;
    // mission principale 
    // Attention des protocoles peuvent etre ouverts sans se rapporter a cette mission
    private CopexMission mission = null;
    // liste des protocoles ouverts
    private ArrayList<LearnerProcedure> listProc = null;
    /* liste des missions de l'utilisateur */
    private ArrayList<CopexMission> listMissionsUser = null;
    /* liste des protocoles des missions de l'utilisateur */
    private ArrayList<ArrayList<LearnerProcedure>> listProcMissionUser = null;
    private long dbKeyTrace = -1;
    
    /* mission aide */
    private CopexMission helpMission;
    /*liste du materiel aide */
    private ArrayList<Material> listHelpMaterial;
    /* protocole aide */
    private LearnerProcedure helpProc;

    /* locker */
    private Locker locker;

    private boolean openQuestionDialog;

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

    

    /* trace ? */
    private boolean setTrace(){
        return true   ;
    }

    private Locale getLocale(){
        return copex.getLocale();
    }

    /* initialisation de l'application - chargement des donnees*/
    @Override
    public CopexReturn initEdP(Locale locale, String idUser, long dbKeyMission, long dbKeyGroup, long dbKeyLabDoc, String labDocName, String fileName) {
        Profiler.start();
        Profiler.start("loadData");
        String msgError = "";
        this.locale = locale;
        this.dbKeyLabDoc = dbKeyLabDoc;
        // recuperation utilisateur externe
        ArrayList v = new ArrayList();
        // initialisation de la connexion a la base
        db = new AccesDB(copexURL, dbKeyMission, idUser);
        this.dbKeyUser = Long.parseLong(idUser);
        db.setIdUser(""+dbKeyUser);
        this.dbKeyGroup = dbKeyGroup;
        // LOCKER
        DataBaseCommunication dbLabBook = new DataBaseCommunication(copexURL, MyConstants.DB_LABBOOK, dbKeyMission, idUser);
        this.locker = new Locker(copex, dbLabBook, dbKeyUser);
        // chargement des parametres :
        // chargement des grandeurs physiques
        v = new ArrayList();
        CopexReturn cr = ParamFromDB.getAllPhysicalQuantitiesFromDB(db.getDbC(), locale, v) ;
        if (cr.isError())
            return cr;
        listPhysicalQuantity = (ArrayList<PhysicalQuantity>)v.get(0);
        //  chargement du type de material par defaut
        v = new ArrayList();
        cr = ParamFromDB.getDefaultTypeMaterialFromDB(db.getDbC(), locale, v);
        if(cr.isError())
            return cr;
        defaultTypeMaterial = (TypeMaterial)v.get(0);
        // chargement de la liste des strategy de material
        v = new ArrayList();
        cr = ParamFromDB.getAllStrategyMaterialFromDB(db.getDbC(), locale, v);
        if(cr.isError())
            return cr;
        listMaterialStrategy = (ArrayList<MaterialStrategy>)v.get(0);
        // chargement users : nom et prenom
        v = new ArrayList();
        cr = db.getGroupFromDB(dbKeyGroup, v);
        if (cr.isError()){
            msgError += cr.getText();
        }else{
            group = (CopexGroup)v.get(0);
        }
        // chargement de la mission
        v = new ArrayList();
         Profiler.start("loadMission");
        cr = db.getMissionFromDB(dbKeyMission, v);
        if (cr.isError()){
            msgError += cr.getText();
        }else{
            mission = (CopexMission)v.get(0);
        }
        Profiler.end("loadMission");
        if (mission == null)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_NULL_MISSION"), false);

        // chargement trace
        if(setTrace()){
            v = new ArrayList();
            cr = TraceFromDB.getIdTrace(db.getDbC(), dbKeyMission, dbKeyUser, v);
            if(cr.isError())
                return cr;
            dbKeyTrace = (Long)v.get(0); 
        }
        // chargement de la liste des protocoles de la mission
        v = new ArrayList();
        Profiler.start("loadProtocole");
        // MBO le 27/05/09 : chargement des proc :
        // on regarde s'il existe des learner proc :
        // => si oui : on charge les proc init / proc correspondants
        // => si non : existe t il plusieurs proc initiaux ?
        //    => si oui : charge proc init
        //    => si non : charge les codes / nom des proc initiaux, on demande a l'utilisateur quel proc il veut ouvrir et on charge le bon proc initial
        boolean allProcLocked =false;
        ArrayList<String> listProcLocked = new ArrayList();
        ArrayList<InitialProcedure> listInitialProc = new ArrayList();
        cr = ExperimentalProcedureFromDB.controlLearnerProcInDB(db.getDbC(),dbKeyLabDoc,  v );
        if (cr.isError()){
            msgError += cr.getText();
        }else{
            boolean isLearnerProc = (Boolean)v.get(0);
            if(isLearnerProc){
                ArrayList<Long> listIdInitProc = (ArrayList<Long>)v.get(1);
                // chargement des proc
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
                // y a t il plusieurs proc initiaux dans cette mission ?
                listProc = new ArrayList();
                v = new ArrayList();
                cr = ExperimentalProcedureFromDB.controlInitialProcInDB(db.getDbC(), dbKeyMission,  v);
                if (cr.isError()){
                    msgError += cr.getText();
                }else{
                    boolean oneInitProc = (Boolean)v.get(0);
                    ArrayList<Long> listIdInitProc = (ArrayList<Long>)v.get(1);
                    if (oneInitProc){
                        // un seul proc init => charge et on cree un proc par defaut
                        v = new ArrayList();
                        cr = ExperimentalProcedureFromDB.getInitialProcFromDB(db.getDbC(), locale, dbKeyMission, listIdInitProc, listPhysicalQuantity, listMaterialStrategy, v);
                        if (cr.isError()){
                            msgError += cr.getText();
                        }else{
                            listInitialProc = (ArrayList<InitialProcedure>)v.get(0);
                        }
                    }else{
                        // plusieurs proc init => charge les codes/ nom seulement et on propose a l'utilisateur celui qu'il veut charger
                        v = new ArrayList();
                        cr = ExperimentalProcedureFromDB.getSimpleInitialProcFromDB(db.getDbC(), getLocale(), dbKeyMission,  listIdInitProc, v);
                        if (cr.isError()){
                            msgError += cr.getText();
                        }else{
                            listInitialProc = (ArrayList<InitialProcedure>)v.get(0);
                        }
                    }
                }

            }
        }
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
        Profiler.end("loadProtocole");
        // chargement datasheet : pas pour le protocole initial
        v = new ArrayList();
        if (listProc == null){
            listProc = new ArrayList();
        }
        
        // charge les missions et protocoles de l'utilisateur
        v = new ArrayList();
        Profiler.start("loadMissionUser");
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
            copex.logStartTool();
        }
        // clone les objets 
        CopexMission m = (CopexMission)mission.clone();
        ArrayList<ExperimentalProcedure> listP = new ArrayList();
        for (int k=0; k<nbP; k++)
            listP.add((LearnerProcedure)listProc.get(k).clone());
        
        ArrayList<PhysicalQuantity> listPhysicalQuantityC = new ArrayList();
        int nbPhysQ = this.listPhysicalQuantity.size();
        for (int k=0; k<nbPhysQ; k++){
            listPhysicalQuantityC.add((PhysicalQuantity)this.listPhysicalQuantity.get(k).clone());
        }
        Profiler.start("initEdp");
        copex.initEdp(m, listP,  listPhysicalQuantityC);
        // s'il n'y a pas de protocole on en cree un  :
        // MBo le 27/02/2009 : si ts les proc de la mission sont lockes on n'en cree pas d'autres
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
        Profiler.end("initEdp");

        Profiler.end("loadData");
        // System.out.println("Resultat :\n"+Profiler.display());
        // System.out.println("\nStats  :\n"+Profiler.getStats());
        Profiler.reset();
        if (listProc.size() > 0)
            printRecap(listProc.get(0));
        
        copex.displayProcLocked(listProcLocked);
        if (askForInitProc)
            copex.askForInitialProc();
        return cr;
    }


    private boolean canAddProc(){
        return true;
    }

  
    private static List<TypeMaterial> getListType(ArrayList<InitialProcedure> listInitProc){
        List<TypeMaterial> list = new LinkedList();
        for(Iterator<InitialProcedure> p = listInitProc.iterator();p.hasNext();){
            list.addAll(p.next().getListTypeMaterial());
        }
        return list;
    }

    /* chargement d'un proc et de sa mission */
    private CopexReturn loadProc(LearnerProcedure p, boolean controlLock, ArrayList v){
        // chargement de la mission
        CopexMission missionToLoad = p.getMission();
        // chargement du proc 
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


     /* couper depuis undo redo */
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
        // on cree les taches en base 
        // enregistre les taches 
        ArrayList v = new ArrayList();
        CopexReturn cr = TaskFromDB.createTasksInDB(db.getDbC(),getLocale(), expProc.getDbKey(), listTaskC, proc.getQuestion(), false,  v);
        if (cr.isError()){
            return cr;
        }
        // on recupere les nouveaux id des taches
        ArrayList<CopexTask> listT = (ArrayList<CopexTask>)v.get(0);
        expProc.addTasks(listT);
        
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
            // mise a jour dans la liste
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
            // mise a jour dans la liste
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
        // trace 
        if (setTrace()){
            List<TaskTreePosition> listPosition = getTaskPosition(proc, listTaskC);
            if (undoRedo == MyConstants.NOT_UNDOREDO)
                copex.logPaste(proc,listTaskC, listPosition);
            else if (undoRedo == MyConstants.REDO)
                copex.logRedoPaste(proc, listTask, listPosition);
        }
        //ihm
        ArrayList<CopexTask> listTC = new ArrayList();
        for (int k=0; k<nbT;k++){
            listTC.add((CopexTask)listT.get(k).clone());
        }
        copex.paste((LearnerProcedure)proc.clone(), listTC, ts, undoRedo);
        // mise a jour date de modif
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

    
    /* suppression des taches selectionnees 
     * on ne peut pas supprimer la racine, meme si elle est modifiable 
     * on supprime les taches enfant s'il y a 
     */ 
    @Override
    public CopexReturn suppr(ArrayList<TaskSelected> listTs, ArrayList v, boolean suppr, char undoRedo) {
       Profiler.start();
       Profiler.start("suppr");
        // determine la liste des taches a supprimer
        LearnerProcedure proc = getProc(listTs);
        int idPr = getIdProc(proc.getDbKey());
        if (idPr == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_SUPPR_ROOT"), false);
        LearnerProcedure expProc = listProc.get(idPr);
        printRecap(expProc);
        ArrayList<CopexTask> listTask = getListTask(listTs);
        // controle que la racine n'est pas dans le lot
        int nbT = listTask.size();
        for (int i=0;i<nbT; i++){
            CopexTask t = listTask.get(i);
            if (t.isQuestionRoot())
                return new CopexReturn(copex.getBundleString("MSG_ERROR_SUPPR_ROOT"), false);
        }
        // suppression en base
        Profiler.start("supprBD");
        CopexReturn cr = db.deleteTasksFromDB(expProc.getDbKey(), listTask);
        if (cr.isError())
            return cr;
        List<TaskTreePosition> listPositionTask = getTaskPosition(expProc, listTask);
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
        // mise a jour des liens en memoire et en base
       int nbTs = listTs.size();
        ArrayList<CopexTask> listToUpdateLinkChild = new ArrayList();
        ArrayList<CopexTask> listToUpdateLinkBrother = new ArrayList();
        for (int i=0; i<nbTs; i++){
            TaskSelected ts = listTs.get(i);
            long dbKeyBrother = ts.getSelectedTask().getDbKeyBrother();
            if (dbKeyBrother != -1){
                // on raccroche son frere  a son grand frere si il existe, sinon au parent
                CopexTask taskBrother = ts.getTaskOldBrother();
                if (taskBrother ==null){
                    // au parent
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
        Profiler.end("majLien");
        Profiler.start("majLienBD");
        // mise a jour des liens dans la base : 
        cr = db.updateLinksInDB(expProc.getDbKey(), listToUpdateLinkBrother, listToUpdateLinkChild);
        if (cr.isError())
            return cr;
        Profiler.end("majLienBD");
        Profiler.start("trace");
        updateQuestion(proc);
        
        // mise a jour des donnees
        boolean isOk = expProc.deleteTasks(listTask);
        if (!isOk){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_DELETE_TASK"), false);
        }
        updateDatasheetProd(expProc);
        expProc.lockMaterialUsed();
        // trace 
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
        // mise a jour date de modif
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
        Profiler.end("trace");
        Profiler.end("suppr");
        // System.out.println("Resultat :\n"+Profiler.display());
        // System.out.println("\nStats  :\n"+Profiler.getStats());
        Profiler.reset();
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
    
    /* definit la liste des taches a partir d'une selection 
     * il s'agit des taches selectionnees + enfants 
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
    
    
    
    /* retourne le protocole associe aux taches => c'est le meme protocole 
     * pour toutes les taches, donc on retourne celui du premier element 
     */
    private LearnerProcedure getProc(ArrayList<TaskSelected> listTs){
        LearnerProcedure proc = null;
        TaskSelected ts = listTs.get(0);
        proc = (LearnerProcedure)ts.getProc();
        return proc;
    }
 /* cherche un indice dans la liste, -1 si non trouve */
    private int getId(List<CopexTask> listT, long dbKey){
        int nbT = listT.size();
        for (int i=0; i<nbT; i++){
            CopexTask t = listT.get(i);
            if (t.getDbKey() == dbKey)
                return i;
        }
        return -1;
    }
   
    
    /* cherche un indice dans la liste des protocoles, -1 si non trouve */
    private int getIdProc(long dbKey){
        return getIdProc(listProc, dbKey);
    }
    
    /* cherche un indice dans la liste des protocoles, -1 si non trouve */
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
    public CopexReturn addAction(CopexAction action, ExperimentalProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v ) {
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
            TaskTreePosition position = (TaskTreePosition)v.get(1);
            copex.logAddAction(proc, action, position);
            Profiler.end("addTaskInTrace");
        }
        Profiler.end("addTask");
        // System.out.println("Resultat :\n"+Profiler.display());
        // System.out.println("\nStats  :\n"+Profiler.getStats());
        Profiler.reset();
        return cr;
    }
    /* modification d'une action */
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

    /* ajout d'une nouvelle etape */
    @Override
    public CopexReturn addStep(Step step, ExperimentalProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v) {
        CopexReturn cr =  addTask(step, proc, taskBrother, taskParent,v, MyConstants.NOT_UNDOREDO, false);
        if (cr.isError())
            return cr;
        // trace
        if (setTrace()){
            TaskTreePosition position = (TaskTreePosition)v.get(1);
            copex.logAddStep(proc, step, position);
        }
        return cr;
    }

    /* modification d'une etape*/
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

    
    

    /* modification d'une sous question */
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
    
    /* retourne la tache parent */
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
        // on n'a pas trouve on cherche dans les grands freres 
        return getParentTask(listT, getOldBrotherTask(listT, task));
    }
    /* retourne la tache grand frere */
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
    
    
    /* ajout d'une tache */
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
        // on cherche dans la liste des taches du proc l'indice de la tache frere
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

        // enregistrement dans la base et recuperation du nouvel id
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
        // mise a jour des donnees 
        task.setDbKey(newDbKey);
        if (taskBrother == null){
            long oldFC = listProc.get(idP).getListTask().get(idB).getDbKeyChild();
            taskParent.setDbKeyChild(newDbKey);
            // mise a jour dans la liste
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
            // mise a jour dans la liste
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
         // mise a jour date de modif
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
        // en v[0] le  protocole mis a jour
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

     /* modification d'une tache */
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
    
    
    /* modification d'une tache */
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
        // on cherche dans la liste des taches l'indice de l'ancienne tache
        int idOld = getId(expProc.getListTask(), oldTask.getDbKey());
        if (idOld == -1){
            String msg = copex.getBundleString("MSG_ERROR_UPDATE_STEP");
            if (newTask instanceof CopexAction)
                msg = copex.getBundleString("MSG_ERROR_UPDATE_ACTION");
            else if (newTask instanceof Question)
                msg = copex.getBundleString("MSG_ERROR_UPDATE_QUESTION");
            return new CopexReturn(msg, false);
        }
        // mise a jour dans la base
        ArrayList v2 = new ArrayList();
        CopexReturn cr = db.updateTaskInDB(getLocale(),newTask, expProc.getDbKey(), oldTask, v2);
        if (cr.isError())
            return cr;
        newTask = (CopexTask)v2.get(0);
        // mise a jour de la repetition de la tache
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
         
        // mise a jour en memoire 
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
            // on met a jour la question du proc egalement
            expProc.getQuestion().setListDescription(newTask.getListDescription());
            expProc.getQuestion().setListComments(newTask.getListComments());
        }
        updateDatasheetProd(expProc);
         expProc.lockMaterialUsed();
        // mise a jour date de modif
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
        // en v[0] le protocole mis a jour 
        v.add((LearnerProcedure)expProc.clone());
        return new CopexReturn();
    }

    /* creation d'un nouveau protocole vierge */
    @Override
    public CopexReturn createProc(String procName, InitialProcedure initProc ) {
        // charge le proc initial
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
        learnerProc.setMission(mission);
        learnerProc.setDbKeyLabDoc(dbKeyLabDoc);
        learnerProc.setListMaterialUsed(getListMaterialUsed(learnerProc));
        learnerProc.setMaterials(new MaterialProc(learnerProc.getListMaterialUsed()));
        CopexReturn cr = ExperimentalProcedureFromDB.createListMaterialUsedInDB(db.getDbC(),getLocale(), learnerProc.getDbKey(), learnerProc.getListMaterialUsed());
            if(cr.isError())
                return cr;
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
        //proc.setName(CopexUtilities.getTextLocal(name, getLocale()));
        proc.setName(name);
        proc.setRight(MyConstants.EXECUTE_RIGHT);
        // enregistre en base et on recupere son id
        // enregistrement du protocole
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
        // enregistre les taches 
        v = new ArrayList();
        cr = TaskFromDB.createTasksInDB(db.getDbC(), getLocale(), proc.getDbKey(), proc.getListTask(), proc.getQuestion(), false,  v);
        if (cr.isError()){
            return cr;
        }
        // on recupere les nouveaux id des taches
        ArrayList<CopexTask> listT = (ArrayList<CopexTask>)v.get(0);
        proc.setListTask(listT);
         updateDatasheetProd(proc);
         proc.lockMaterialUsed();
        // mise a jour date de modif 
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
        // memoire
        proc.setOpen(true);
        this.listProc.add(proc);
        // trace
        if (setTrace() && setTrace){
            if (copy){
                copex.logCopyProc(proc, procToCopy);
             }else{
               // creation de protocole
                copex.logCreateProc(proc);
            }
        }
        //ihm
        copex.createProc((LearnerProcedure)proc.clone());
        return cr;
    }

    /* ouverture d'un protocole existant */
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
            // c'est peut etre un proc deja ouvert
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
            // on l'ajoute a la liste si il n'y est pas deja
            p = listP.get(idP);
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
            copex.logOpenProc(p, p.getMission().getDbKey(),  p.getMission().getCode());
        }
        //ihm
        copex.createProc((LearnerProcedure)p.clone());
        return cr;
    }

    /* fermeture d'un protocole */
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
        // IHM
        copex.closeProc((LearnerProcedure)proc.clone());
        return new CopexReturn();
    }

    /* suppression d'un protocole */
    @Override
    public CopexReturn deleteProc(ExperimentalProcedure proc) {
       int id = getIdProc(proc.getDbKey());
       if (id == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_DELETE_PROC"), false);
       // suprression en base
       // deverouille
       unsetLocker(proc);
       CopexReturn cr = db.removeProcInDB(listProc.get(id));
       if (cr.isError())
           return cr;
       
       // suppression en memoire 
       listProc.remove(id);
       // trace 
       if (setTrace()){
           copex.logDeleteProc(proc);
           
       }
        // IHM
       copex.deleteProc((LearnerProcedure)proc.clone());
       return cr;
    }

     

    /* retourne en v[0] la liste des protocoles qui peuvent etre copies : 
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
        // liste des missions / protocoles associes 
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
        // on ajoute les protocoles de la mission en cours qui sont fermes
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

    /* mise a jour du nom du protocole */
    @Override
    public CopexReturn updateProcName(ExperimentalProcedure proc, String name, char undoRedo) {
        String oldName = name;
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_RENAME_PROC"), false);
        LearnerProcedure procC = listProc.get(idP);
        // modification en base
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
        // en memoire
        //listProc.get(idP).setName(CopexUtilities.getTextLocal(name, getLocale()));
        listProc.get(idP).setName(name);
        
        // trace 
        if (setTrace()){
            if (undoRedo == MyConstants.NOT_UNDOREDO){
                copex.logRenameProc(proc, oldName, name)   ;
            }else if (undoRedo == MyConstants.UNDO){
                copex.logUndoRenameProc(proc, oldName, name);
            }else if (undoRedo == MyConstants.REDO){
                copex.logRedoRenameProc(proc, oldName, name);
            }
        }
        //ihm
        copex.updateProcName((LearnerProcedure)procC.clone(), new String(name));
        return cr;
    }
    
    /* mise a jour du statut des protocoles */
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

    
    

    /* impression :
     * @param : boolean printProc : impression du protocole actif
     * @param : boolean printComments : impression des commentaires
     * @param : boolean printDataSheet : impression de la feuille de donnees associee au protocole
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
        // enregistrement trace
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
        // on modifie les liens des taches en base 
        // enregistre les taches 
        // mise a jour date de modif 
        CopexReturn cr = db.updateDateProc(expProc);
        if (cr.isError()){
             return cr;
        }
        List<TaskTreePosition> listPosition = getTaskPosition(expProc, listTask);
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
            //// System.out.println("tache parent : "+taskParent.getDescription(getLocale()));
        }else{
            dbKeyT = taskBrother.getDbKey();
            //// System.out.println("tache frere : "+taskBrother.getDescription(getLocale()));
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
        TaskFromDB.deleteLinkParentInDB(db.getDbC(), taskBranch.getDbKey());
        if (cr.isError()){
           return cr;
        }
        TaskFromDB.deleteLinkSubBrotherInDB(db.getDbC(), taskBranch.getDbKey());
        if (cr.isError()){
            return cr;
        }
        // derniere tache 
        TaskFromDB.deleteLinkBrotherInDB(db.getDbC(), lastTaskBranch.getDbKey());
        if (cr.isError()){
            return cr;
        }
        // reconnecte eventuellement un lien parent 
        
        if (parent != null){
             expProc.getListTask().get(idParent).setDbKeyChild(-1);
            // on branche le premier petit frere 
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
        // on rebranche eventuellement les freres
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
        // le sous arbre est deconnecte => on l'insere au bon endroit 
        if (taskBrother == null){
            // branche en parent
            long firstChild = expProc.getListTask().get(idB).getDbKeyChild();
            taskParent.setDbKeyChild(taskBranch.getDbKey());
            // mise a jour dans la liste
            expProc.getListTask().get(idB).setDbKeyChild(taskBranch.getDbKey());
            cr = TaskFromDB.createLinkChildInDB(db.getDbC(), expProc.getListTask().get(idB).getDbKey(), taskBranch.getDbKey());
            if (cr.isError()){
                return cr;
            }
            //si il y avait un fils on supprime le lien : 
            if (firstChild != -1){
                cr = TaskFromDB.deleteLinkParentInDB(db.getDbC(), firstChild);
                if (cr.isError()){
                    return cr;
                }
                // on cree un nouveau lien frere entre lastTaskBranch et firstChild
                lastTaskBranch.setDbKeyBrother(firstChild);
                cr = TaskFromDB.createLinkBrotherInDB(db.getDbC(), lastTaskBranch.getDbKey(), firstChild);
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
            // mise a jour dans la liste
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
        // trace 
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
        //ihm
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
    
   
    /* arret de l'edp */
    @Override
    public CopexReturn stopEdP(){
        // deverouille les proc
        try{
            unsetLockers(this.listProc);
            this.locker.stop();
            // sauvegarde des taches vis ou non
            CopexReturn cr = saveTaskVisible();
            if (cr.isError())
               return cr;
            // enregistrement trace
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

    /* mise a jour des taches visibles */
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
    
    
    /* mise a jour de l'etat visible des taches du proc */
    // MBO le 22/10/08 : on ne fera la maj en base qu'en sortie sinon ralentissement de l'appli
    @Override
    public CopexReturn updateTaskVisible(ExperimentalProcedure proc, ArrayList<CopexTask> listTask){
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(copex.getBundleString("MSG_ERROR_TASK_VISIBLE"), false);
        LearnerProcedure expProc = listProc.get(idP);
        // mise a jour dans la base
       // CopexReturn cr = TaskFromDB.updateTaskVisibleInDB_xml(db.getDbC(), listTask);
       // if (cr.isError())
       //     return cr;
        // mise a jour en memoire
        int nb = listTask.size();
        for (int i=0; i<nb; i++){
            int idTask = getId(expProc.getListTask(), listTask.get(i).getDbKey());
            if (idTask == -1)
                return new CopexReturn(copex.getBundleString("MSG_ERROR_TASK_VISIBLE"), false);
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

    // lecture du fichier de config copex
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
            // grandeurs physiques
            //listPhysicalQuantity = config.getListQuantities();
            // strategies du materiel
            //listMaterialStrategy = config.getListMaterialStrategy();
        }catch(IOException e1){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_LOAD_COPEX_CONFIG")+" "+e1, false);
        }catch(JDOMException e2){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_LOAD_COPEX_CONFIG")+" "+e2, false);
        }
        return new CopexReturn();
    }

   /* protocole aide */
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
            // chargement du proc
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

//        // mission
//        helpMission = new CopexMission(copex.getBundleString("HELP_MISSION_CODE"), copex.getBundleString("HELP_MISSION_NAME"), "");
//        helpMission.setDbKey(-2);
//        //protocole
//        MaterialStrategy helpMaterialStrategy = listMaterialStrategy.get(0);
//        int nb = listMaterialStrategy.size();
//        for (int i=0; i<nb; i++){
//            if(listMaterialStrategy.get(i).getCode().equals("S1")){
//                helpMaterialStrategy = listMaterialStrategy.get(i);
//                break;
//            }
//        }
//
//        InitialProcedure initProc = new InitialProcedure(-2, CopexUtilities.getLocalText("help proc", getLocale()), null, false,MyConstants.NONE_RIGHT, "help proc", true,false,false, null,
//                MyConstants.MODE_MENU_NO, MyConstants.MODE_MENU_NO, false, MyConstants.MODE_MENU_NO, helpMaterialStrategy);
//        // type de materiel
//        TypeMaterial typeUstensil = new TypeMaterial(1, CopexUtilities.getLocalText(copex.getBundleString("HELP_TYPE_MATERIAL_USTENSIL"), getLocale()));
//        TypeMaterial typeIngredient = new TypeMaterial(2, CopexUtilities.getLocalText(copex.getBundleString("HELP_TYPE_MATERIAL_INGREDIENT"), getLocale()));
//        // material
//        listHelpMaterial = new ArrayList();
//        Material m = new Material(1, CopexUtilities.getLocalText(copex.getBundleString("HELP_MATERIAL_CUP"), getLocale()), CopexUtilities.getLocalText("", getLocale()), null, new MaterialSourceCopex());
//        m.addType(typeUstensil);
//        listHelpMaterial.add(m);
//        m = new Material(2, CopexUtilities.getLocalText(copex.getBundleString("HELP_MATERIAL_BAG"), getLocale()), CopexUtilities.getLocalText("", getLocale()), null, new MaterialSourceCopex());
//        m.addType(typeIngredient);
//        listHelpMaterial.add(m);
//        m = new Material(3,CopexUtilities.getLocalText(copex.getBundleString("HELP_MATERIAL_SPOON"), getLocale()),  CopexUtilities.getLocalText("", getLocale()), null, new MaterialSourceCopex());
//        m.addType(typeUstensil);
//        listHelpMaterial.add(m);
//        m = new Material(4,CopexUtilities.getLocalText(copex.getBundleString("HELP_MATERIAL_SUGAR"), getLocale()),   CopexUtilities.getLocalText("", getLocale()), null, new MaterialSourceCopex());
//        m.addType(typeIngredient);
//        listHelpMaterial.add(m);
//        m = new Material(5, CopexUtilities.getLocalText(copex.getBundleString("HELP_MATERIAL_WATER"), getLocale()),  CopexUtilities.getLocalText("", getLocale()), null, new MaterialSourceCopex());
//        m.addType(typeIngredient);
//        listHelpMaterial.add(m);
//        m = new Material(6, CopexUtilities.getLocalText(copex.getBundleString("HELP_MATERIAL_KETTLE"), getLocale()),  CopexUtilities.getLocalText("", getLocale()), null, new MaterialSourceCopex());
//        m.addType(typeUstensil);
//        listHelpMaterial.add(m);
//        m = new Material(7, CopexUtilities.getLocalText(copex.getBundleString("HELP_MATERIAL_GAZ_COOKER"), getLocale()), CopexUtilities.getLocalText("", getLocale()), null, new MaterialSourceCopex());
//        m.addType(typeUstensil);
//        listHelpMaterial.add(m);
//        initProc.setListMaterial(listHelpMaterial);
//        helpProc = new LearnerProcedure(CopexUtilities.getLocalText(copex.getBundleString("PROC_HELP_PROC_NAME"), getLocale()), helpMission, CopexUtilities.getCurrentDate(), initProc, null);
//        ArrayList<MaterialUsed> listMaterialUsed = getListMaterialUsed(helpProc);
//        helpProc.setListMaterialUsed(listMaterialUsed);
//        helpProc.setRight(MyConstants.NONE_RIGHT);
//        helpProc.setDbKey(-2);
//        // liste des taches
//        ArrayList<CopexTask> listTask = new ArrayList();
//        TaskRight tr = new TaskRight(MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT);
//        Question question = new Question(1, getLocale(),"question", copex.getBundleString("PROC_HELP_QUESTION"), "",  null, null,true, tr, true, -1, 2);
//        listTask.add(question);
//        helpProc.setQuestion(question);
//        Step step1 = new Step(2, getLocale(), "step1", copex.getBundleString("PROC_HELP_STEP_1"), "", null, null,true, tr, 9, 3, null);
//        listTask.add(step1);
//        CopexAction action1_1 = new CopexAction(3,getLocale(), "action1-1", copex.getBundleString("PROC_HELP_ACTION_1_1"), "", null,  null,true, tr, 4, -1, null);
//        listTask.add(action1_1);
//        CopexAction action1_2 = new CopexAction(4, getLocale(),"action1-2", copex.getBundleString("PROC_HELP_ACTION_1_2"), "", null, null,true, tr, 5, -1, null);
//        listTask.add(action1_2);
//        CopexAction action1_3 = new CopexAction(5,getLocale(), "action1-3", copex.getBundleString("PROC_HELP_ACTION_1_3"), "", null,null,true, tr, 6, -1, null);
//        listTask.add(action1_3);
//        CopexAction action1_4 = new CopexAction(6, getLocale(),"action1-4", copex.getBundleString("PROC_HELP_ACTION_1_4"), "", null, null,true, tr, 7, -1, null);
//        listTask.add(action1_4);
//        CopexAction action1_5 = new CopexAction(7, getLocale(),"action1-5", copex.getBundleString("PROC_HELP_ACTION_1_5"), "", null, null,true, tr, 8, -1, null);
//        listTask.add(action1_5);
//        CopexAction action1_6 = new CopexAction(8,getLocale(), "action1-6", copex.getBundleString("PROC_HELP_ACTION_1_6"), "", null, null,true, tr, -1, -1, null);
//        listTask.add(action1_6);
//        Step step2 = new Step(9, getLocale(), "step2", copex.getBundleString("PROC_HELP_STEP_2"), "", null, null,true, tr, 14, 10, null);
//        listTask.add(step2);
//        CopexAction action2_1 = new CopexAction(10, getLocale(),"action2-1", copex.getBundleString("PROC_HELP_ACTION_2_1"), "", null, null,true, tr, 11, -1, null);
//        listTask.add(action2_1);
//        CopexAction action2_2 = new CopexAction(11,getLocale(), "action2-2", copex.getBundleString("PROC_HELP_ACTION_2_2"), "", null, null,true, tr, 12, -1, null);
//        listTask.add(action2_2);
//        CopexAction action2_3 = new CopexAction(12, getLocale(),"action2-3", copex.getBundleString("PROC_HELP_ACTION_2_3"), "", null, null,true, tr, 13, -1, null);
//        listTask.add(action2_3);
//        CopexAction action2_4 = new CopexAction(13,getLocale(), "action2-4", copex.getBundleString("PROC_HELP_ACTION_2_4"), "", null, null,true, tr, -1, -1, null);
//        listTask.add(action2_4);
//        CopexAction action_3 = new CopexAction(14, getLocale(),"action_3", copex.getBundleString("PROC_HELP_ACTION_3"), "", null,null,true, tr, 15, -1, null);
//        listTask.add(action_3);
//        CopexAction action_4 = new CopexAction(15,getLocale(), "action_4", copex.getBundleString("PROC_HELP_ACTION_4"), "", null, null,true, tr, -1, -1, null);
//        listTask.add(action_4);
//        helpProc.setListTask(listTask);
//
//
//        //helpProc.setHypothesis(new Hypothesis(1, CopexUtilities.getLocalText(copex.getBundleString("HELP_HYPOTHESIS"), getLocale()),new LinkedList(), false));
//        helpProc.setGeneralPrinciple(new GeneralPrinciple(1, CopexUtilities.getLocalText(copex.getBundleString("HELP_GENERAL_PRINCIPLE"), getLocale()),new LinkedList(), null, false));
//        helpProc.setMaterials(new MaterialProc(listMaterialUsed));
//        helpProc.setDataSheet(null);
//        helpProc.setEvaluation(new Evaluation(1, CopexUtilities.getLocalText(copex.getBundleString("HELP_EVALUATION"), getLocale()),new LinkedList(), false));
//       return new CopexReturn();
    }

    
    /* pose les verrous sur un labdoc */
    private void setLocker(long dbKeylabdoc){
        this.locker.setLabdocLocker(dbKeylabdoc);
    }


    /* pose les verrous sur une liste proc */
    private void setLockers(ArrayList<LearnerProcedure> listP){
        int nb = listP.size();
        ArrayList listId = new ArrayList();
        for (int i=0; i<nb; i++){
            listId.add(listP.get(i).getDbKeyLabDoc());
        }
        this.locker.setLabdocLockers(listId);
    }

    /* deverouille un proc */
    private void unsetLocker(ExperimentalProcedure proc){
        if(proc instanceof LearnerProcedure){
            this.locker.unsetLabdocLocker(((LearnerProcedure)proc).getDbKeyLabDoc());
        }
    }
    /* deverouille une liste de proc */
    private void unsetLockers(ArrayList<LearnerProcedure> listP){
        int nb = listP.size();
        ArrayList listId = new ArrayList();
        for (int i=0; i<nb; i++){
            listId.add(listP.get(i).getDbKeyLabDoc());
        }
        this.locker.unsetLabdocLockers(listId);
    }


    /* ouverture fenetre dialogue*/
    @Override
    public CopexReturn openHelpDialog() {
        if(setTrace()){
            copex.logOpenHelp();
        }
        return new CopexReturn();
    }

    /* fermeture fenetre aide */
    @Override
    public CopexReturn closeHelpDialog() {
        if(setTrace()){
            copex.logCloseHelp();
        }
        return new CopexReturn();
    }

    /* ouverture proc aide*/
    @Override
    public CopexReturn openHelpProc() {
        if(setTrace()){
            copex.logOpenHelpProc();
        }
        return new CopexReturn();
    }
    /* retourne l'elo exp proc */
    @Override
    public Element getExperimentalProcedure(ExperimentalProcedure proc){
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

    /* retourne la liste des parametres des actions de l'etape */
    @Override
    public CopexReturn getTaskInitialParam(ExperimentalProcedure proc, CopexTask task, ArrayList v){
        v.add(proc.getTaskInitialParam(task));
        return new CopexReturn();
    }


    /* retourne la liste des output des actions de l'etape */
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

     

    /* hypotheses du proc*/
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

    /* principe general du proc*/
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
    
    /* evaluation du proc*/
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

    /* mise a jour du material used */
    @Override
    public CopexReturn setMaterialUsed(ExperimentalProcedure proc, ArrayList<MaterialUsed> listMaterialToCreate, ArrayList<MaterialUsed> listMaterialToDelete, ArrayList<MaterialUsed> listMaterialToUpdate, ArrayList v){
        int idProc = getIdProc(proc.getDbKey());
        if(idProc == -1){
            return new CopexReturn(copex.getBundleString("MSG_ERROR_SET_MATERIAL_USED"), false);
        }
        // creation du nouveau mat
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
        // suppression du mat
        cr = ExperimentalProcedureFromDB.deleteMaterialUsedFromDB(db.getDbC(), listProc.get(idProc).getDbKey(), listMaterialToDelete);
        if(cr.isError())
            return cr;
        listProc.get(idProc).deleteMaterialUsed(listMaterialToDelete);
        if(setTrace() && listMaterialToDelete.size() >0)
            copex.logDeleteMaterialUsed(proc, listMaterialToDelete);
        // mise a jour du mat
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
