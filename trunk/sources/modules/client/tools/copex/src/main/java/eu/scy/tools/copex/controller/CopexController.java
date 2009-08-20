/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.controller;


import eu.scy.tools.copex.common.*;
import eu.scy.tools.copex.data.MyCopexData;
import eu.scy.tools.copex.dnd.SubTree;
import eu.scy.tools.copex.edp.EdPPanel;
import eu.scy.tools.copex.edp.TaskSelected;
import eu.scy.tools.copex.elo.*;
import eu.scy.tools.copex.print.PrintPDF;
import eu.scy.tools.copex.profiler.Profiler;
import eu.scy.tools.copex.trace.*;
import eu.scy.tools.copex.utilities.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * MBO le 03/03/09 : plusieurs proc initiaux lies a  une mission
 * controller de l'applet
 * @author MBO
 */
public class CopexController implements ControllerInterface {

    // CONSTANTES 
    private final static boolean mission1= true;
    // ATTRIBUTS
    /*locale */
    private Locale locale;
    /* edP */
    private EdPPanel edP;
    /* utilisateur */
    private long dbKeyUser;
    /* trace */
    private ManageTrace trace;
    /* utilisateur */
    private CopexUser copexUser;
    /* liste des grandeurs physiques gerees dans COPEX */
    private ArrayList<PhysicalQuantity> listPhysicalQuantity ;
    // mission principale 
    // Attention des protocoles peuvent aªtre ouverts sans se rapporter a  cette mission
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
    private boolean openQuestionDialog;

    //id
    private long idMission = 1;
    private long idProc = 1;
    private long idTask = 1;
    private long idDataSheet = 1;
    private long idData = 1;
    private long idParam = 1;
    private long idMaterial = 1;
    private long idQuantity = 1;
    private long idTypeMaterial = 1;

    
    // CONSTRUCTEURS
    public CopexController(EdPPanel edP) {
        this.edP = edP;
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
        if(this.mission == null)
                return false;

        return mission.getOptions().isTrace();
    }

    /* initialisation de l'application - chargement des donnees*/
    @Override
    public CopexReturn initEdP(Locale locale, String idUser, long dbKeyMission, int mode, String userName, String firstName, String logFileName) {
        String msgError = "";
        this.locale = locale;
        this.logFileName = logFileName;
        // recuperation utilisateur externe
        ArrayList v = new ArrayList();
        System.out.println("controller");
        CopexReturn cr = loadUser(idUser, mode,userName, firstName,dbKeyMission, v);
        if(cr.isError())
            return cr;
        this.dbKeyUser = (Long)v.get(0);
        // chargement des parametres :
        // chargement des grandeurs physiques
        v = new ArrayList();
        cr = MyCopexData.getAllPhysicalQuantities(edP, v) ;
        if (cr.isError())
            return cr;
        listPhysicalQuantity = (ArrayList<PhysicalQuantity>)v.get(0);
        
        // chargement user : nom et prenom
        copexUser = new CopexUser("", "", userName, firstName);
        copexUser.setLearner(true);

        // chargement de la mission
        OptionMission options = new OptionMission(true, false, false);
        mission = new CopexMission(idMission++, "", "", "", CopexMission.STATUT_MISSION_ON, options) ;
        // chargement de la liste des protocoles de la mission
        this.listInitialProc = new ArrayList();
        listInitialProc.add(getInitialProcedure());
        listProc = new ArrayList();
        boolean allProcLocked = false;
        // chargement datasheet : pas pour le protocole initial
        
        // charge les missions et protocoles de l'utilisateur
        this.listMissionsUser = new ArrayList();
        this.listProcMissionUser = new ArrayList();
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
        // clone les objets 
        CopexMission m = (CopexMission)mission.clone();
        int nbP = listProc.size();
        ArrayList<LearnerProcedure> listP = new ArrayList();
        for (int k=0; k<nbP; k++)
            listP.add((LearnerProcedure)listProc.get(k).clone());
        ArrayList<InitialProcedure> listIC = new ArrayList();
        int nbIP = this.listInitialProc.size();
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
        // s'il n'y a pas de protocole on en cree un  :
        // MBo le 27/02/2009 : si ts les proc de la mission sont lockes on n'en cree pas d'autres
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
        if (listProc.size() > 0)
            printRecap(listProc.get(0));
        ArrayList<String> listProcLocked = new ArrayList();
        edP.displayProcLocked(listProcLocked);
        if (askForInitProc)
            edP.askForInitialProc();
        else if (openQuestionDialog)
            edP.setQuestionDialog();
        return cr;
    }

    private InitialProcedure getInitialProcedure(){
        String procName = "COPEX";
//        if(mission1)
//            procName = "thermostat setting";
        InitialProcedure initProc = new InitialProcedure(idProc++, procName,CopexUtilities.getCurrentDate(), false, MyConstants.NONE_RIGHT, procName, true, true, null ) ;
        ArrayList<CopexTask> listTask = new ArrayList();
        TaskRight taskRight = new TaskRight(MyConstants.EXECUTE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT);
        String description = "";
        if(mission1){
            //description = "How does the air-conditionning device's thermostat setting affect energy consumption ?";
            description = "Your question";
        }
        Question q = new Question(idTask++, "", description, "", "", null, null, "", true, taskRight, true);
        listTask.add(q);
        initProc.setQuestion(q);
        initProc.setListTask(listTask);
        initProc.setMission(mission);
        if(mission1){
            ArrayList<Material> listMaterial = new ArrayList();
            ArrayList<TypeMaterial> listType = new ArrayList();
            //Material m1 = new Material(idMaterial++, "electric meter", "Description of the electric meter");
            Material m1 = new Material(idMaterial++, "material 1", "Description of the material 1");
            listType.add(new TypeMaterial(idTypeMaterial++, "material of mission 1"));
            m1.setListType(listType);
            listMaterial.add(m1);
            //Material m2 = new Material(idMaterial++, "Thermometer", "Description of the thermometer");
            Material m2 = new Material(idMaterial++, "material 2", "Description of the material 2");
            m2.setListType(listType);
            listMaterial.add(m2);
            initProc.setListMaterial(listMaterial);
        }else{
            initProc.setListMaterial(new ArrayList());
        }
        return initProc;
    }

    /* retourne vrai si utilisation du datasheet*/
    @Override
    public boolean useDataSheet(){
       if(mission == null)
            return true;
        return mission.getOptions().isCanUseDataSheet();
    }

    private boolean canAddProc(){
        if(mission == null)
            return true;
        return mission.getOptions().isCanAddProc();
    }
   

    /* chargement d'un proc et de sa mission */
    private CopexReturn loadProc(LearnerProcedure p, boolean controlLock, ArrayList v){
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
       // tache selectionnee et protocole ou il faut copier 
        TaskSelected ts = edP.getSelectedTask();
        LearnerProcedure proc = ts.getProc();
        // liste des taches a  copier 
        SubTree subTree = edP.getSubTreeCopy();
        ArrayList v = new ArrayList();
        return paste(proc, subTree, ts, MyConstants.NOT_UNDOREDO, v);
    }
    
    @Override
    public CopexReturn paste(LearnerProcedure proc, SubTree subTree, TaskSelected ts, char undoRedo, ArrayList v2) {
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_PASTE"), false);
        LearnerProcedure expProc = listProc.get(idP);
        
        ArrayList<CopexTask> listTask = subTree.getListTask();
        int nbTa = listTask.size();
        ArrayList<CopexTask> listTaskC = new ArrayList();
        for (int i=0; i<nbTa; i++){
            listTaskC.add((CopexTask)listTask.get(i).clone());
        }
        // on cree les taches en base 
        // enregistre les taches
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
                        ((CopexActionParam)task).getTabParam()[j].setDbKey(idParam++);
                    }
                    if(task instanceof CopexActionManipulation){
                        ArrayList<Material> listM = ((CopexActionManipulation)task).getListMaterialProd() ;
                        int nbM = listM.size();
                        for (int i=0; i<nbM; i++){
                        Material m = listM.get(i);
                        m.setDbKey(idMaterial++);
                        // creation des parametres
                        int nbParam = m.getListParameters().size();
                        for (int j=0; j<nbParam; j++){
                            listM.get(i).getListParameters().get(j).setDbKey(idQuantity++);
                        }
                    }
                    }else if (task instanceof CopexActionAcquisition){
                        ArrayList<QData> listD = ((CopexActionAcquisition)task).getListDataProd() ;
                        int nbD = listD.size() ;
                        for (int i=0; i<nbD; i++){
                            listD.get(i).setDbKey(idQuantity++);
                        }
                    }else if (task instanceof CopexActionTreatment){
                        ArrayList<QData> listD = ((CopexActionTreatment)task).getListDataProd() ;
                        int nbD = listD.size() ;
                        for (int i=0; i<nbD; i++){
                            listD.get(i).setDbKey(idQuantity++);
                        }
                    }
                }
                // on met a  jour les identifiants
                long dbKey = idTask++ ;
                listTaskC.get(k).setDbKey(dbKey);
                if ( proc.getQuestion().getDbKey() == oldDbKey)
                    proc.getQuestion().setDbKey(dbKey);
                // parcours la liste pour mettre a  jour
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
        // mise a  jour date de modif 
        CopexReturn cr = updateDateProc(expProc);
        if (cr.isError()){
            return cr;
        }
        
        // on branche le sous arbre au protocole, en reconnectant eventuellement les liens de la tache selectionnee
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
            // branche en parent
            taskParent.setDbKeyChild(taskBranch.getDbKey());
            // mise a  jour dans la liste
            expProc.getListTask().get(idB).setDbKeyChild(taskBranch.getDbKey());
        }else{
            long dbKeyOldBrother = taskBrother.getDbKeyBrother();
            if (dbKeyOldBrother != -1)
                lastTaskBranch.setDbKeyBrother(dbKeyOldBrother);
            taskBrother.setDbKeyBrother(taskBranch.getDbKey());
            // mise a  jour dans la liste
            expProc.getListTask().get(idB).setDbKeyBrother(taskBranch.getDbKey());
            if (dbKeyOldBrother != -1){
                expProc.getListTask().get(idLTB).setDbKeyBrother(dbKeyOldBrother);
            }
        }
        // trace 
        if (setTrace()){
            ArrayList<MyTask> listXMLTask  = new ArrayList();
            for (int i=0; i<nbT; i++){
                CopexTask task = listTaskC.get(i);
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
            listTC.add((CopexTask)listTaskC.get(k).clone());
        }
        edP.paste((LearnerProcedure)proc.clone(), listTC, ts, undoRedo);
        v2.add(listTC);
        return new CopexReturn();
    }

    
    /* suppression des taches selectionnees 
     * on ne peut pas supprimer la racine, meme si elle est modifiable 
     * on supprime les taches enfant s'il y a 
     */ 
    @Override
    public CopexReturn suppr(ArrayList<TaskSelected> listTs, ArrayList v, boolean suppr, char undoRedo) {
       // determine la liste des taches a  supprimer
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
        
        // mise a  jour date de modif 
        CopexReturn cr = updateDateProc(expProc);
        if (cr.isError()){
            return cr;
        }
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
        // mise a  jour des liens en memoire et en base
       int nbTs = listTs.size();
        ArrayList<CopexTask> listToUpdateLinkChild = new ArrayList();
        ArrayList<CopexTask> listToUpdateLinkBrother = new ArrayList();
        for (int i=0; i<nbTs; i++){
            TaskSelected ts = listTs.get(i);
            long dbKeyBrother = ts.getSelectedTask().getDbKeyBrother();
            if (dbKeyBrother != -1){
                // on raccroche son frere  a  son grand frere si il existe, sinon au parent
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
        
        // prepare eventuellement les donnees pour la trace
        ArrayList<MyTask> listXMLTask = new ArrayList();
        int nbTa = listTask.size();
        for (int i=0; i<nbTa; i++){
            MyTask xmlT = new MyTask(listTask.get(i).getDbKey(), listTask.get(i).getDescription());
            listXMLTask.add(xmlT);
        }
        // mise a  jour des donnees
         System.out.println("maj donnees");
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
        printRecap(expProc);
        v.add(expProc.clone());
        return new CopexReturn();
    }

    
    /* definit la liste des taches a  partir d'une selection 
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
        proc = ts.getProc();
        return proc;
    }
 /* cherche un indice dans la liste, -1 si non trouve */
    private int getId(ArrayList<CopexTask> listT, long dbKey){
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
        System.out.println("Resultat :\n"+Profiler.display());
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

    /* ajout d'une nouvelle etape */
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

    /* modification d'une etape*/
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
       // mise a  jour date de modif 
        cr = updateDateProc(expProc);
        if (cr.isError()){
            return cr;
        }
        
        long newDbKey = idTask++;
        // mise a  jour des donnees 
        task.setDbKey(newDbKey);
        if (taskBrother == null){
            taskParent.setDbKeyChild(newDbKey);
            // mise a  jour dans la liste
            //proc.getListTask().get(idB).setDbKeyChild(newDbKey);
            listProc.get(idP).getListTask().get(idB).setDbKeyChild(newDbKey);
            if (listProc.get(idP).getListTask().get(idB).getDbKey() == listProc.get(idP).getQuestion().getDbKey())
                listProc.get(idP).getQuestion().setDbKeyChild(newDbKey);
        }else{
            long dbKeyOldBrother = listProc.get(idP).getListTask().get(idB).getDbKeyBrother();
            task.setDbKeyBrother(dbKeyOldBrother);
            taskBrother.setDbKeyBrother(newDbKey);
            // mise a  jour dans la liste
            expProc.getListTask().get(idB).setDbKeyBrother(newDbKey);
        }
        TaskRight taskRight = new TaskRight(MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.EXECUTE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT);
        if (task instanceof CopexActionChoice){
            CopexActionChoice a = new CopexActionChoice(newDbKey, "", task.getDescription(), task.getComments(), task.getTaskImage(), task.getDraw(), true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), ((CopexActionChoice)task).getTabParam(), task.getTaskRepeat());
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
            CopexActionNamed a = new CopexActionNamed(newDbKey, "", task.getDescription(), task.getComments(), task.getTaskImage(),task.getDraw(),  true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), ((CopexActionNamed)task).getNamedAction(), task.getTaskRepeat());
            expProc.addAction(a);
        }else if (task instanceof CopexAction){
            CopexAction a = new CopexAction(newDbKey, "", task.getDescription(), task.getComments(), task.getTaskImage(), task.getDraw(), true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), task.getTaskRepeat());
            expProc.addAction(a);
        }else if (task instanceof Step){
            Step s = new Step(newDbKey, "", task.getDescription(), task.getComments(), task.getTaskImage(),task.getDraw(),true, taskRight, task.getDbKeyBrother(), task.getDbKeyChild(), task.getTaskRepeat());
            expProc.addStep(s);
        }else if (task instanceof Question){
            Question q = new Question(newDbKey, "", task.getDescription(), ((Question)task).getHypothesis(), task.getComments(), task.getTaskImage(),task.getDraw(), ((Question)task).getGeneralPrinciple(), true, taskRight, false, task.getDbKeyBrother(), task.getDbKeyChild() );
            expProc.addQuestion(q);
        }
        
        // en v[0] le  protocole mis a  jour
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
        if(newTask instanceof CopexAction){
            InitialNamedAction newA = null;
            boolean isNewTaskActionNamed = newTask instanceof  CopexActionNamed || newTask instanceof CopexActionAcquisition || newTask instanceof CopexActionChoice || newTask instanceof CopexActionManipulation || newTask instanceof CopexActionTreatment ;
            if (isNewTaskActionNamed){
               newA = ((CopexActionNamed)newTask).getNamedAction() ;
           }
            if(newA != null){
                ActionParam[] newTabParam = ((CopexActionParam)newTask).getTabParam() ;
                for (int i=0; i<newTabParam.length; i++){
                    newTabParam[i].setDbKey(idParam++);
                }
                if(newTask instanceof CopexActionManipulation){
                    ArrayList<Material> listM = ((CopexActionManipulation)newTask).getListMaterialProd() ;
                    int nbM = listM.size();
                    for (int i=0; i<nbM; i++){
                        Material m = listM.get(i);
                        m.setDbKey(idMaterial++);
                        // creation des parametres
                        int nbParam = m.getListParameters().size();
                        for (int j=0; j<nbParam; j++){
                            listM.get(i).getListParameters().get(j).setDbKey(idQuantity++);
                        }
                    }
                }else if (newTask instanceof CopexActionAcquisition){
                    ArrayList<QData> listD = ((CopexActionAcquisition)newTask).getListDataProd() ;
                    int nbD = listD.size() ;
                    for (int i=0; i<nbD; i++){
                        listD.get(i).setDbKey(idQuantity++);
                    }
                }else if (newTask instanceof CopexActionTreatment){
                    ArrayList<QData> listD = ((CopexActionTreatment)newTask).getListDataProd() ;
                    int nbD = listD.size() ;
                    for (int i=0; i<nbD; i++){
                        listD.get(i).setDbKey(idQuantity++);
                    }
                }
            }
        }
        
        // mise a  jour date de modif 
        CopexReturn cr = updateDateProc(expProc);
        if (cr.isError()){
            return cr;
        }
       
        // mise a  jour en memoire 
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
            // on met a  jour la question du proc egalement
            expProc.getQuestion().setDescription(newTask.getDescription());
            expProc.getQuestion().setComments(newTask.getComments());
            if (newTask instanceof Question){
                expProc.getQuestion().setHypothesis(((Question)newTask).getHypothesis());
                expProc.getQuestion().setGeneralPrinciple(((Question)newTask).getGeneralPrinciple());
            }
        }
        
        // en v[0] le protocole mis a  jour 
        v.add((LearnerProcedure)expProc.clone());
        return new CopexReturn();
    }

    /* creation d'un nouveau protocole vierge */
    @Override
    public CopexReturn createProc(String procName, InitialProcedure initProc ) {
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
        loadProcCopy = false;
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
        long dbKeyProc = idProc++;
        proc.setDbKey(dbKeyProc);
        long dbKeyQuestion = idTask++;
        proc.getQuestion().setDbKey(dbKeyQuestion);
        proc.getListTask().get(0).setDbKey(dbKeyQuestion);
        // enregistre les taches
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
                        ((CopexActionParam)task).getTabParam()[j].setDbKey(idParam++);
                    }
                    if(task instanceof CopexActionManipulation){
                        ArrayList<Material> listM = ((CopexActionManipulation)task).getListMaterialProd() ;
                        int nbM = listM.size();
                        for (int i=0; i<nbM; i++){
                        Material m = listM.get(i);
                        m.setDbKey(idMaterial++);
                        // creation des parametres
                        int nbParam = m.getListParameters().size();
                        for (int j=0; j<nbParam; j++){
                            listM.get(i).getListParameters().get(j).setDbKey(idQuantity++);
                        }
                    }
                    }else if (task instanceof CopexActionAcquisition){
                        ArrayList<QData> listD = ((CopexActionAcquisition)task).getListDataProd() ;
                        int nbD = listD.size() ;
                        for (int i=0; i<nbD; i++){
                            listD.get(i).setDbKey(idQuantity++);
                        }
                    }else if (task instanceof CopexActionTreatment){
                        ArrayList<QData> listD = ((CopexActionTreatment)task).getListDataProd() ;
                        int nbD = listD.size() ;
                        for (int i=0; i<nbD; i++){
                            listD.get(i).setDbKey(idQuantity++);
                        }
                    }
                }
                // on met a  jour les identifiants
                long dbKey = idTask++ ;
                proc.getListTask().get(k).setDbKey(dbKey);
                if ( proc.getQuestion().getDbKey() == oldDbKey)
                    proc.getQuestion().setDbKey(dbKey);
                // parcours la liste pour mettre a  jour
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
        // on cree le nouveau dataSheet s'il existe 
        if (proc.getDataSheet() != null){
            long dbKeyDataSheet = idDataSheet++;
            proc.getDataSheet().setDbKey(dbKeyDataSheet);
            // creation des data eventuelles
            int nbR = proc.getDataSheet().getNbRows();
            int nbC = proc.getDataSheet().getNbColumns();
            for (int i=0; i<nbR; i++){
                for (int j=0; j<nbC; j++){
                    if (proc.getDataSheet().getDataAt(i, j) != null){
                        CopexData newData = new CopexData(idData++, proc.getDataSheet().getDataAt(i, j).getData());
                        proc.getDataSheet().setValueAt(newData, i, j);
                    }
                }
            }
        }
        // mise a  jour date de modif 
        CopexReturn cr = updateDateProc(proc);
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
        /*int nbM = this.listMissionsUser.size();
        for (int i=0; i<nbM; i++){
            if (missionToOpen.getDbKey() == this.listMissionsUser.get(i).getDbKey()){
                idM = i;
                break;
            }
        }*/
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
            // on l'ajoute a  la liste si il n'y est pas deja
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
       
       // mise a  jour date de modif 
        CopexReturn cr = updateDateMission(proc.getMission().getDbKey());
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

     

    /* retourne en v[0] la liste des protocoles qui peuvent aªtre copies : 
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

    /* mise a  jour du nom du protocole */
    @Override
    public CopexReturn updateProcName(LearnerProcedure proc, String name, char undoRedo) {
        String oldName = name;
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_RENAME_PROC"), false);
        LearnerProcedure procC = listProc.get(idP);
        
        // mise a  jour date de modif 
        CopexReturn cr = updateDateProc(procC);
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
    
    /* mise a  jour du statut des protocoles */
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
        }
        return new CopexReturn();
    }

    /* creation d'une feuille de donnees vierge pour le protocole */
    @Override
    public CopexReturn createDataSheet(LearnerProcedure proc, int nbR, int nbC, char undoRedo, ArrayList v) {
        DataSheet dataSheet = new DataSheet(nbR, nbC);
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_CREATE_DATASHEET"), false);
        // recupere l'id
        long dbKeyDataSheet = idDataSheet++;
        dataSheet.setDbKey(dbKeyDataSheet);
        // mise a  jour date de modif 
        CopexReturn cr = updateDateProc(proc);
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
        CopexReturn cr = new CopexReturn() ;
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_CREATE_DATASHEET"), false);
        
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
    
    
    /* modification de la feuille de donnees */
    @Override
    public CopexReturn modifyDataSheet(LearnerProcedure proc, int nbR, int nbC, char undoRedo) {
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_UPDATE_DATASHEET"), false);
        DataSheet dataSheet = proc.getDataSheet();
        int oldNbRow = dataSheet.getNbRows();
        int oldNbCol = dataSheet.getNbColumns();
        
        // mise a  jour date de modif 
        CopexReturn cr = updateDateProc(proc);
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

    /* modification d'une cellule d'une feuille de donnees*/
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
        if (dbKeyData == -1)
            dbKeyData  = idData++;
        CopexData newData = new CopexData(dbKeyData, value) ;
        // mise a  jour date de modif 
        CopexReturn cr = updateDateProc(proc);
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
     * @param : boolean printDataSheet : impression de la feuille de donnees associee au protocole
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
        // mise a  jour date de modif 
        CopexReturn cr = updateDateProc(expProc);
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
        
        // reconnecte eventuellement un lien parent 
        if (parent != null){
             expProc.getListTask().get(idParent).setDbKeyChild(-1);
            // on branche le premier petit frere 
            if (subTree.getLastBrother() != -1){
                
                expProc.getListTask().get(idParent).setDbKeyChild(subTree.getLastBrother());
            }
        }
        // on rebranche eventuellement les freres
        if (oldBrother != null){
            expProc.getListTask().get(idOldBrother).setDbKeyBrother(-1);
            if (subTree.getLastBrother() != -1){
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
            // mise a  jour dans la liste
            expProc.getListTask().get(idB).setDbKeyChild(taskBranch.getDbKey());
            
            //si il y avait un fils on supprime le lien : 
            if (firstChild != -1){
                // on cree un nouveau lien frere entre lastTaskBranch et firstChild
                lastTaskBranch.setDbKeyBrother(firstChild);
                expProc.getListTask().get(idLastTaskBranch).setDbKeyBrother(firstChild);
                lastTaskBranch.setDbKeyBrother(firstChild);
            }
        }else{ // branche en frere 
            long dbKeyOldBrother = expProc.getListTask().get(idB).getDbKeyBrother();
            if (dbKeyOldBrother != -1)
                lastTaskBranch.setDbKeyBrother(dbKeyOldBrother);
            taskBrother.setDbKeyBrother(taskBranch.getDbKey());
            // mise a  jour dans la liste
            expProc.getListTask().get(idB).setDbKeyBrother(taskBranch.getDbKey());
            if (dbKeyOldBrother != -1){
                 expProc.getListTask().get(idLastTaskBranch).setDbKeyBrother(dbKeyOldBrother);
                lastTaskBranch.setDbKeyBrother(dbKeyOldBrother);
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
            String visible = task.isVisible() ? "visible" :"cachee";
            System.out.println("  - Ta¢che "+task.getDescription()+" ("+task.getDbKey()+") : "+frere+" / "+enfant+ " ("+visible+")");
           
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
        return new CopexReturn();
    }
    
    /* arret de l'edp */
    @Override
    public CopexReturn stopEdP(){
        

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

    /* mise a  jour des taches visibles */
    private CopexReturn saveTaskVisible(){
        return new CopexReturn();
    }
    
    
    /* mise a  jour de l'etat visible des taches du proc */
    // MBO le 22/10/08 : on ne fera la maj en base qu'en sortie sinon ralentissement de l'appli
    @Override
    public CopexReturn updateTaskVisible(LearnerProcedure proc, ArrayList<CopexTask> listTask){
        int idP = getIdProc(proc.getDbKey());
        if (idP == -1)
            return new CopexReturn(edP.getBundleString("MSG_ERROR_TASK_VISIBLE"), false);
        LearnerProcedure expProc = listProc.get(idP);
        // mise a  jour dans la base
       // CopexReturn cr = TaskFromDB.updateTaskVisibleInDB_xml(db.getDbC(), listTask);
       // if (cr.isError())
       //     return cr;
        // mise a  jour en memoire
        int nb = listTask.size();
        for (int i=0; i<nb; i++){
            int idT = getId(expProc.getListTask(), listTask.get(i).getDbKey());
            if (idT == -1)
                return new CopexReturn(edP.getBundleString("MSG_ERROR_TASK_VISIBLE"), false);
            expProc.getListTask().get(idT).setVisible(listTask.get(i).isVisible());
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
        InitialProcedure initProc = new InitialProcedure(-2, "help proc", null, false,MyConstants.NONE_RIGHT, "help proc",true,false, null);
        helpProc = new LearnerProcedure(edP.getBundleString("PROC_HELP_PROC_NAME"), helpMission, CopexUtilities.getCurrentDate(), initProc);
        helpProc.setRight(MyConstants.NONE_RIGHT);
        helpProc.setDbKey(-2);
        // liste des taches
        ArrayList<CopexTask> listTask = new ArrayList();
        TaskRight tr = new TaskRight(MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT, MyConstants.NONE_RIGHT);
        Question question = new Question(1, "question", edP.getBundleString("PROC_HELP_QUESTION"), "", "", null, null, edP.getBundleString("PROC_HELP_GENERAL_PRINCIPLE"), true, tr, true, -1, 2);
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
        TypeMaterial typeIngredient = new TypeMaterial(2, edP.getBundleString("HELP_TYPE_MATERIAL_INGREDIENT"));
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
        // mise a  jour date de modif 
        CopexReturn cr = updateDateProc(proc);
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
         // mise a  jour date de modif 
        CopexReturn cr = updateDateProc(proc);
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
        
         // mise a  jour date de modif 
        CopexReturn cr = updateDateProc(proc);
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
   

    

    /* chargement de l'utilisateur selon le mode - retourne en v[0] le dbKeyUser de COPEX */
    private CopexReturn loadUser(String idUser, int mode, String userName, String firstName, long dbKeyMission, ArrayList v){
        v.add(new Long(1));
        return new CopexReturn();
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

    /* mise a  jour date du proc*/
    private CopexReturn updateDateProc(LearnerProcedure p){
        int id = getIdProc(listProc, p.getDbKey());
        if (id != -1)
            this.listProc.get(id).setDateLastModification(CopexUtilities.getCurrentDate());
        return new CopexReturn();
    }

    private CopexReturn updateDateMission(long dbKeyMission){
        return new CopexReturn();
    }

    /* retourne l'elo exp proc */
    @Override
    public Element getExperimentalProcedure(LearnerProcedure p){
        if (p == null)
            return null;
        int id = getIdProc(p.getDbKey());
        if (id == -1)
            return null;
        LearnerProcedure proc = listProc.get(id);
        //mission
        XMLMission mis = getXMLMission();
        //initialProc
        String idInitProc = ""+proc.getInitialProc().getDbKey();
        // question
        XMLQuestion question = getXMLQuestion(proc);
        // datasheet
        XMLDataSheet ds = null;
        if (proc.getDataSheet() != null){
            List<XMLRow> listRows = new LinkedList<XMLRow>();
            int nbR = proc.getDataSheet().getNbRows() ;
            int nbC = proc.getDataSheet().getNbColumns() ;
            for (int i=0; i<nbR; i++){
                List<XMLColumn> listColumns = new LinkedList<XMLColumn>();
                for (int j=0; j<nbC; j++){
                    String value= "";
                    if (proc.getDataSheet().getDataAt(i, j) != null)
                        value=  proc.getDataSheet().getDataAt(i, j).getData() ;
                    listColumns.add(new XMLColumn(value));
                }
                listRows.add(new XMLRow(listColumns));
            }
            ds = new XMLDataSheet(""+proc.getDataSheet().getDbKey(), ""+nbR, ""+nbC, listRows);
        }
        //material use
        List<XMLMaterialUse> listMaterialUse = null;
        if (proc.getListMaterialUse() != null){
            int nb = proc.getListMaterialUse().size();
            if (nb > 0){
                listMaterialUse = new LinkedList<XMLMaterialUse>();
                for (int i=0; i<nb; i++){
                    listMaterialUse.add(new XMLMaterialUse(""+proc.getListMaterialUse().get(i).getMaterial().getDbKey(), proc.getListMaterialUse().get(i).getMaterial().getName(), proc.getListMaterialUse().get(i).getJustification()));
                }
            }
        }

        ExperimentalProcedureELO elo = new ExperimentalProcedureELO(proc.getName(), ""+proc.getDbKey(), mis, idInitProc, question,  ds, listMaterialUse );
        return elo.toXML();
    }

    private XMLMission getXMLMission(){
        // list init proc
        List<XMLInitialProc> listInitProc = new LinkedList<XMLInitialProc>();
        int nb = listInitialProc.size();
        for (int i=0; i<nb; i++){
            listInitProc.add(getXMLInitialProc(listInitialProc.get(i)));
        }
        // options
        XMLOption option = new XMLOption(useDataSheet(), canAddProc());
        return new XMLMission(listInitProc, option);
    }

    // material
    private XMLMaterial getXMLMaterial(Material m){
        // types
        List<XMLMaterialType> listType = new LinkedList<XMLMaterialType>();
        int nb=  m.getListType().size() ;
        for (int i=0; i<nb; i++){
            listType.add(new XMLMaterialType(""+m.getListType().get(i).getDbKey(),m.getListType().get(i).getType() ));
        }
        // param
        List<XMLMaterialParameter> listParam = null;
        if (m.getListParameters() != null){
            nb = m.getListParameters().size() ;
            listParam = new LinkedList<XMLMaterialParameter>();
            for (int i=0; i<nb; i++){
                Parameter p = m.getListParameters().get(i);
                listParam.add(new XMLMaterialParameter(""+p.getDbKey(), p.getName(), p.getType(),""+p.getValue(), p.getUncertainty(), ""+p.getUnit().getDbKey() ));
            }
        }
        return new XMLMaterial(""+m.getDbKey(),m.getName(), m.getDescription(), listType, listParam);
    }

    // construction proc initial
    private XMLInitialProc getXMLInitialProc(InitialProcedure p){
        List<XMLNamedAction> listNamedAction  = null;
        if (p.getListNamedAction() != null){
            listNamedAction = new LinkedList<XMLNamedAction>();
            int nb = p.getListNamedAction().size();
            for (int i=0; i<nb; i++){
                listNamedAction.add(new XMLNamedAction(""+p.getListNamedAction().get(i).getDbKey(),p.getListNamedAction().get(i).getLibelle() , p.getListNamedAction().get(i).isDraw()  , p.getListNamedAction().get(i).isRepeat() ));
            }
        }
        XMLActionType actionType = new XMLActionType(p.isFreeAction(), p.isTaskRepeat(), listNamedAction);
        //list material available
        List<XMLMaterial> listMaterialAvailable = null;
        if(p.getListMaterial() != null){
            listMaterialAvailable = new LinkedList<XMLMaterial>();
            int nb = p.getListMaterial().size();
            for (int i=0; i<nb; i++){
                listMaterialAvailable.add(getXMLMaterial(p.getListMaterial().get(i)));
            }
        }
        return new XMLInitialProc(""+p.getDbKey(), p.getName(), p.getCode(),getXMLQuestion(p), actionType, listMaterialAvailable);
    }

    // construction de la question
    private XMLQuestion getXMLQuestion(ExperimentalProcedure proc){
        TaskRight tr = proc.getQuestion().getTaskRight();
        List<XMLTask> listTask = getSubTask(proc, proc.getQuestion());

        XMLTaskRight questionRight = new XMLTaskRight(""+tr.getEditRight(), ""+tr.getDeleteRight(), ""+tr.getCopyRight(), ""+tr.getMoveRight(), ""+tr.getParentRight(), ""+tr.getDrawRight(), ""+tr.getRepeatRight());
        XMLQuestion question = new XMLQuestion(""+proc.getQuestion().getDbKey(), proc.getQuestion().getDescription(),
                proc.getQuestion().getHypothesis(), proc.getQuestion().getGeneralPrinciple(),
                proc.getQuestion().getComments(), proc.getQuestion().getTaskImage(), proc.getQuestion().getDraw(), questionRight, listTask);
        return question;
    }

    // retourne la liste des enfants d'une tache
    private LinkedList<XMLTask> getSubTask(ExperimentalProcedure proc, CopexTask task){
        LinkedList<XMLTask> listT = null;
        boolean hasChild = task.getDbKeyChild() != -1 ;
        if (hasChild){
            listT = new LinkedList();
            ArrayList<CopexTask> listChild = getChilds(proc, task);
            int nbC = listChild.size();
            for (int i=0; i<nbC; i++){
                listT.add(getTask(proc, listChild.get(i)));
            }

        }
        return listT;
    }

    // tache en tache XML
    private XMLTask getTask(ExperimentalProcedure proc, CopexTask task){
        String nature = "";
        String hyp = null;
        String princ = null;
        String actionNamed = null;
        if (task.isAction()){
            nature = XMLTask.TASK_NATURE_ACTION;
            if (((CopexAction)task) instanceof CopexActionNamed){
                actionNamed = ""+((CopexActionNamed)task).getNamedAction().getDbKey() ;
            }
        }else if (task.isQuestion()){
            nature = XMLTask.TASK_NATURE_QUESTION;
            hyp = ((Question)task).getHypothesis() ;
            princ = ((Question)task).getGeneralPrinciple() ;
        }else if (task.isStep())
            nature = XMLTask.TASK_NATURE_STEP;
        XMLTaskRight right = new XMLTaskRight(""+task.getTaskRight().getEditRight(), ""+task.getTaskRight().getDeleteRight(), ""+task.getTaskRight().getCopyRight(), ""+task.getTaskRight().getMoveRight(), ""+task.getTaskRight().getParentRight(), ""+task.getTaskRight().getDrawRight(), ""+task.getTaskRight().getRepeatRight());
        LinkedList<XMLTask> listSubTask = getSubTask(proc, task);
        XMLTask t = new XMLTask(""+task.getDbKey(), nature, task.getName(), task.getDescription(),
                task.getComments(), hyp, princ,task.getTaskImage(),task.getDraw(),  right, actionNamed, listSubTask );
        return t;
    }

    

    // retourne la liste des enfants
    private static ArrayList<CopexTask> getChilds(ExperimentalProcedure proc, CopexTask task){
        ArrayList<CopexTask> listT = new ArrayList();
        long dbKeyC = task.getDbKeyChild() ;
        if(dbKeyC != -1){
            CopexTask firstC = getTask(proc, dbKeyC);
            if(firstC != null)
                listT.add(firstC);
            ArrayList<CopexTask> listB = getBrothers(proc, firstC);
            int nbB = listB.size() ;
            for (int i=0; i<nbB; i++){
                listT.add(listB.get(i));
            }
        }
        return listT;
    }

    // retourne la tache avec ce dbKey
    private static CopexTask getTask(ExperimentalProcedure proc, long dbKey){
        int nbT = proc.getListTask().size() ;
        for (int i=0; i<nbT; i++){
            if(proc.getListTask().get(i).getDbKey() == dbKey)
                return proc.getListTask().get(i);
        }
        return null;
    }

    // retourne la liste des freres
    private static ArrayList<CopexTask> getBrothers(ExperimentalProcedure proc, CopexTask task){
        ArrayList<CopexTask> listB = new ArrayList();
        long dbKeyB = task.getDbKeyBrother() ;
        if (dbKeyB != -1){
            CopexTask t = getTask(proc, dbKeyB);
            if(t!=null){
                listB.add(t);
                ArrayList<CopexTask> l = getBrothers(proc, t);
                int nb = l.size() ;
                for (int i=0; i<nb; i++){
                    listB.add(l.get(i));
                }
            }
        }
        return listB ;
    }

     /* chargement d'un ELO*/
    @Override
    public CopexReturn loadELO(Element xmlContent){
        try {
            ExperimentalProcedureELO elo = new ExperimentalProcedureELO(xmlContent);
            // mission
            OptionMission options = new OptionMission(elo.getMission().getMissionOptions().canAddProc(), elo.getMission().getMissionOptions().isUseDataSheet(), false);
            this.mission = new CopexMission(idMission++,"", "", "", CopexMission.STATUT_MISSION_TREAT, options);
            // proc initiaux
            if (this.listInitialProc == null)
                this.listInitialProc = new ArrayList();
            int nb = elo.getMission().getListInitialProc().size();
            for (int i=0; i<nb; i++){
                InitialProcedure p = getInitialProcFromXML(elo.getMission().getListInitialProc().get(i));
                p.setMission(mission);
                listInitialProc.add(p);
            }
            
            //proc
            long dbKeyIP = -1;
            try{
                dbKeyIP = Long.parseLong(elo.getIdInitProc() );
            }catch(NumberFormatException e){

            }
            InitialProcedure initialProc = getInitialProc(dbKeyIP);
            LearnerProcedure proc = new LearnerProcedure(idProc++, elo.getName(), CopexUtilities.getCurrentDate(), true, MyConstants.EXECUTE_RIGHT,
                  initialProc)  ;
            
            //material use
            ArrayList<MaterialUseForProc> listMaterialUse = new ArrayList();
            if (elo.getListMaterialUse() != null){
                listMaterialUse = new ArrayList();
                nb = elo.getListMaterialUse().size();
                for (int i=0; i<nb; i++){
                    listMaterialUse.add(getMaterialUseFromXML(initialProc.getListMaterial(), elo.getListMaterialUse().get(i)));
                }
            }
            proc.setListMaterialUse(listMaterialUse);
            // taches
            proc.setQuestion(getQuestionFromXML(elo.getQuestion()));
            ArrayList<CopexTask> listTask = getListTaskFromXML(proc.getQuestion(), elo.getQuestion().getListTask(), initialProc.getListNamedAction());
            listTask.add(proc.getQuestion());
            proc.setListTask(listTask);
            proc.setMission(mission);
            this.listProc.add(proc);
            // ihm
            int nbP = listProc.size();
            ArrayList<LearnerProcedure> listP = new ArrayList();
            for (int k=0; k<nbP; k++)
                listP.add((LearnerProcedure)listProc.get(k).clone());
            ArrayList<InitialProcedure> listIC = new ArrayList();
            int nbIP = this.listInitialProc.size();
            for (int k=0; k<nbIP; k++){
                listIC.add((InitialProcedure)listInitialProc.get(k).clone());
            }
            edP.updateMission((CopexMission)mission.clone(),listP, listIC );
            edP.createProc((LearnerProcedure)proc.clone());
        } catch (JDOMException ex) {
            return new CopexReturn(edP.getBundleString("MSG_ERROR_OPEN_PROC"), false);
        }
        return new CopexReturn();
    }

    /* convertir elo init proc en init proc */
    private InitialProcedure getInitialProcFromXML(XMLInitialProc p){
        long dbKey = -1;
        try{
            dbKey = Long.parseLong(p.getIdProc());
        }catch (NumberFormatException e){
            dbKey = idProc++;
        }
        boolean isFreeAction = p.getActionType().isFreeAction();
        boolean isTaskRepeat = p.getActionType().isTaskRepeat();
        ArrayList<InitialNamedAction> listNamedAction = null;
        if (p.getActionType().getListNamedAction() != null){
            listNamedAction = new ArrayList();
            int nb = p.getActionType().getListNamedAction().size();
            for (int i=0; i<nb; i++){
                listNamedAction.add(getInitialNamedActionFromXML(p.getActionType().getListNamedAction().get(i)));
            }
        }
        InitialProcedure proc  = new InitialProcedure(dbKey, p.getProcName(), CopexUtilities.getCurrentDate(), false, MyConstants.NONE_RIGHT,p.getProcCode(),isFreeAction, isTaskRepeat, listNamedAction );
        // taches
        proc.setQuestion(getQuestionFromXML(p.getQuestion()));
        ArrayList<CopexTask> listTask = getListTaskFromXML(proc.getQuestion(), p.getQuestion().getListTask(), proc.getListNamedAction());
        listTask.add(proc.getQuestion());
        proc.setListTask(listTask);
        proc.setMission(mission);
        // material
        ArrayList listMaterial = new ArrayList();
        if (p.getListMaterialAvailable() != null){
            int nb =p.getListMaterialAvailable().size();
            for (int i=0; i<nb; i++){
                listMaterial.add(getMaterialFromXML(p.getListMaterialAvailable().get(i)));
            }
        }
        proc.setListMaterial(listMaterial);
        return proc;
    }

    /* initial named action */
    private InitialNamedAction getInitialNamedActionFromXML(XMLNamedAction a){
        long dbKey = -1;
        try{
            dbKey = Long.parseLong(a.getIdAction());
        }catch (NumberFormatException e){
            dbKey = idTask++;
        }
        return new InitialNamedAction(dbKey, "", a.getNameAction(), false, null, a.isDraw(), a.isRepeat());
    }

    /* material */
    private Material getMaterialFromXML(XMLMaterial m){
        long dbKey = -1;
        try{
            dbKey = Long.parseLong(m.getIdMaterial());
        }catch (NumberFormatException e){
            dbKey = idMaterial++;
        }
        Material material = new Material(dbKey, m.getMaterialName(), m.getMaterialDescription());
        ArrayList<TypeMaterial> listType = new ArrayList();
        int nb = m.getListType().size();
        for (int i=0; i<nb; i++){
            long dbKeyT = -1;
            try{
                dbKeyT = Long.parseLong(m.getListType().get(i).getIdType());
            }catch (NumberFormatException e){
                dbKeyT = idTypeMaterial++;
            }
            listType.add(new TypeMaterial(dbKeyT, m.getListType().get(i).getTypeName()));
        }
        material.setListType(listType);
        ArrayList<Parameter> listParameters = null;
        if (m.getListParameters() != null){
            listParameters = new ArrayList();
            nb = m.getListParameters().size();
            for (int i=0; i<nb; i++){
                long dbKeyP = -1;
                try{
                    dbKeyP = Long.parseLong(m.getListParameters().get(i).getIdParam());
                }catch (NumberFormatException e){
                    dbKeyP = idParam++;
                }
                double value = 0;
                try{
                    value = Double.parseDouble(m.getListParameters().get(i).getParamValue());
                }catch (NumberFormatException e){
                }
                long dbKeyU = -1;
                try{
                    dbKeyU = Long.parseLong(m.getListParameters().get(i).getParamUnit());
                }catch (NumberFormatException e){
                }
                CopexUnit unit = getUnit(dbKeyU);
                listParameters.add(new Parameter(dbKeyP, m.getListParameters().get(i).getParamName(), m.getListParameters().get(i).getParamType(),
                        value, m.getListParameters().get(i).getParamUncertainty(), unit));
            }
        }
        material.setListParameters(listParameters);
        return material;
    }

    /* retourne l'unite*/
    private CopexUnit getUnit(long dbKey){
        int nb = listPhysicalQuantity.size();
        for (int i=0; i<nb; i++){
            int nbu = listPhysicalQuantity.get(i).getListUnit().size();
            for (int j=0; j<nbu; j++){
                if(listPhysicalQuantity.get(i).getListUnit().get(j).getDbKey() == dbKey)
                    return listPhysicalQuantity.get(i).getListUnit().get(j);
            }
        }
        return null;
    }

    /* retourne le proc initial correspodant */
    private InitialProcedure getInitialProc(long dbKey){
        int nb = this.listInitialProc.size();
        for (int i=0; i<nb; i++){
            if (this.listInitialProc.get(i).getDbKey() == dbKey)
                return this.listInitialProc.get(i);
        }
        return null;
    }

    /* question */
    private Question getQuestionFromXML(XMLQuestion q){
        TaskRight tr = getTaskRightFromXML(q.getQuestionRight());
        return new Question(idTask++, "", q.getDescription(), q.getHypothesis(), q.getComments(), q.getImage(), q.getDraw(), q.getGeneralPrinciple(), true, tr, true);
    }

    /* tache */
    private CopexTask getTaskFromXML(XMLTask t, ArrayList<InitialNamedAction> listINA){
        TaskRight tr = getTaskRightFromXML(t.getRight());
        String nature = t.getNature();
        if (nature.equals(XMLTask.TASK_NATURE_QUESTION)){
            //sous question
            return new Question(idTask++, "", t.getDescription(), t.getHypothesis(), t.getComments(), t.getImage(),t.getDraw()  ,t.getPrinciple(), true, tr, false);
        }else if (nature.equals(XMLTask.TASK_NATURE_STEP)){
            //etape
            return new Step(idTask++, t.getTaskName(), t.getDescription(), t.getComments(), t.getImage(),t.getDraw(),true, tr, null );
        }else{
            // action
            if(t.getActionNamed() == null || t.getActionNamed().trim().length() ==0){
                return new CopexAction(idTask++, t.getTaskName(), t.getDescription(), t.getComments(), t.getImage(), t.getDraw(), true, tr, null);
            }else{
                long dbKey = -1;
                try{
                    dbKey = Long.parseLong(t.getActionNamed());
                }catch(NumberFormatException e){
                    
                }
                InitialNamedAction initAN = getInitialNamedAction(dbKey, listINA);
                return new CopexActionNamed(idTask++, t.getTaskName(), t.getDescription(), t.getComments(), t.getImage(),t.getDraw(), true, tr, initAN, null);
            }
        }
    }

    /* recherche d'une action init*/
    private InitialNamedAction getInitialNamedAction(long dbKey, ArrayList<InitialNamedAction> listINA){
        if (listINA == null)
            return null;
        int nb = listINA.size();
        for (int i=0; i<nb; i++){
            if (listINA.get(i).getDbKey() == dbKey)
                return listINA.get(i);
        }
        return null;
    }

    /* task right */
    private TaskRight getTaskRightFromXML(XMLTaskRight tr){
        return new TaskRight(getRight(tr.getEditRight()), getRight(tr.getDeleteRight()), getRight(tr.getCopyRight()), getRight(tr.getMoveRight()), getRight(tr.getParentRight()) , getRight(tr.getDrawRight()), getRight(tr.getRepeatRight()) );
    }

    /* droit */
    private char getRight(String r){
        char c = r.charAt(0);
        if (c != MyConstants.NONE_RIGHT && c != MyConstants.EXECUTE_RIGHT)
            return MyConstants.EXECUTE_RIGHT ;
        return c;
    }

    /* material use*/
    private MaterialUseForProc getMaterialUseFromXML(ArrayList<Material> listMaterial, XMLMaterialUse m){
        long dbKey  = -1;
        try{
            dbKey = Long.parseLong(m.getIdMaterial());
        }catch(NumberFormatException e){

        }
        Material material = getMaterial(listMaterial, dbKey);
        return new MaterialUseForProc(material, m.getJustification());
    }

    /* recherche materieal */
    private Material getMaterial(ArrayList<Material> listM, long dbKey){
        int nb = listM.size();
        for (int i=0; i<nb; i++){
            if (listM.get(i).getDbKey() == dbKey)
                return listM.get(i) ;
        }
        return null;
    }

    /* construction liste des taches a  partir de  l'ELO */
    private ArrayList<CopexTask> getListTaskFromXML(CopexTask task, List<XMLTask> listT, ArrayList<InitialNamedAction> listINA){
        ArrayList<CopexTask> listTask = new ArrayList();
        if(listT == null){
            return listTask;
        }else{
            int nbT = listT.size();
            long dbKeyB = -1;
            for (int i=nbT-1; i>-1; i--){
                CopexTask t = getTaskFromXML(listT.get(i), listINA);
                if(i==0){
                    task.setDbKeyChild(t.getDbKey());
                }
                t.setDbKeyBrother(dbKeyB);
                dbKeyB = t.getDbKey() ;
                listTask.add(t);
                // taches enfants
                ArrayList<CopexTask> listChildren = getListTaskFromXML(t, listT.get(i).getListTask(), listINA);
                int nbC = listChildren.size();
                for (int j=0; j<nbC; j++){
                    listTask.add(listChildren.get(j));
                }
            }
        }
        return listTask;
    }

     /*  creation d'un nouvel ELO*/
    @Override
    public CopexReturn newELO(){
        return createProc(listInitialProc.get(0).getName(), listInitialProc.get(0), false );
    }

    /* retourne la liste des parametres des actions de l'etape */
    @Override
    public CopexReturn getTaskInitialParam(LearnerProcedure proc, CopexTask task, ArrayList v){
        ArrayList<InitialActionParam> list = new ArrayList();
        ArrayList<CopexTask> listChilds = getAllChilds(proc, task);
        int nb = listChilds.size();
        for (int i=0; i<nb; i++){
            CopexTask t = listChilds.get(i);
            if (t instanceof CopexActionManipulation){
                InitialNamedAction a = ((CopexActionManipulation)t).getNamedAction();
                if (a != null && a.getVariable() != null){
                    InitialActionParam[] tab = a.getVariable().getTabParam();
                    if(tab != null){
                        for (int j=0; j<tab.length; j++){
                            list.add(tab[j]);
                        }
                    }
                }
            }else if (t instanceof CopexActionAcquisition){
                InitialNamedAction a = ((CopexActionAcquisition)t).getNamedAction();
                if (a != null && a.getVariable() != null){
                    InitialActionParam[] tab = a.getVariable().getTabParam();
                    if(tab != null){
                        for (int j=0; j<tab.length; j++){
                            list.add(tab[j]);
                        }
                    }
                }
            }else if (t instanceof CopexActionTreatment){
                InitialNamedAction a = ((CopexActionTreatment)t).getNamedAction();
                if (a != null && a.getVariable() != null){
                    InitialActionParam[] tab = a.getVariable().getTabParam();
                    if(tab != null){
                        for (int j=0; j<tab.length; j++){
                            list.add(tab[j]);
                        }
                    }
                }
            }
        }
        v.add(list);
        return new CopexReturn();
    }


    /* retourne la liste des output des actions de l'etape */
    @Override
    public CopexReturn getTaskInitialOutput(LearnerProcedure proc, CopexTask task, ArrayList v){
        ArrayList<InitialOutput> list = new ArrayList();
        ArrayList<CopexTask> listChilds = getAllChilds(proc, task);
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
        v.add(list);
        return new CopexReturn();
    }

    public static  ArrayList<CopexTask> getAllChilds(LearnerProcedure proc, CopexTask task){
        ArrayList<CopexTask> listAllChilds = new ArrayList();
        ArrayList<CopexTask> listC = getChilds(proc, task);
        int nb = listC.size();
        for (int i=0; i<nb; i++){
            listAllChilds.add(listC.get(i));
            // ajout des enfants
            ArrayList<CopexTask> l = getAllChilds(proc, listC.get(i));
            int n = l.size();
            for (int j=0; j<n; j++){
                listAllChilds.add(l.get(j));
            }
        }
        return listAllChilds ;
    }
    
}
