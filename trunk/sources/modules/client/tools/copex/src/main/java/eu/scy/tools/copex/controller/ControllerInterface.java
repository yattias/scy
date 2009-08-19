/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.controller;

import eu.scy.tools.copex.common.CopexAction;
import eu.scy.tools.copex.common.CopexMission;
import eu.scy.tools.copex.common.CopexTask;
import eu.scy.tools.copex.common.InitialActionParam;
import eu.scy.tools.copex.common.InitialOutput;
import eu.scy.tools.copex.common.InitialProcedure;
import eu.scy.tools.copex.common.LearnerProcedure;
import eu.scy.tools.copex.common.Material;
import eu.scy.tools.copex.common.Question;
import eu.scy.tools.copex.common.Step;
import eu.scy.tools.copex.dnd.SubTree;
import eu.scy.tools.copex.edp.TaskSelected;
import eu.scy.tools.copex.utilities.CopexReturn;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import org.jdom.Element;
/**
 * interface du controller
 * @author MBO
 */
public interface ControllerInterface {
    /* initialisation de l'edp */
    public CopexReturn initEdP(Locale locale, String idUser, long dbKeyMission, int mode, String userName, String firstname, String logFileName);
    /* retourne vrai si utilisation datasheet */
    public boolean useDataSheet();
    /* ajout d'une sous question */
    public void addQuestion();
    /* ajout d'une etape */
    public void addEtape();
    /* ajout d'une action */
    public void addAction();
    /* ajout d'une tache */
    public CopexReturn addTask(CopexTask task, LearnerProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v, char undoRedo, boolean cut) ;
    /* ajout d'une action */
    public CopexReturn addAction(CopexAction action, LearnerProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v);
    /* modification d'une action */
    public CopexReturn updateAction(CopexAction newAction, LearnerProcedure proc, CopexAction oldAction, ArrayList v);
    /* ajout d'une étape */
    public CopexReturn addStep(Step step, LearnerProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v);
    /* modification d'une étape */
    public CopexReturn updateStep(Step newStep, LearnerProcedure proc, Step oldStep, ArrayList v);
    /* ajout d'une sous question */
    public CopexReturn addQuestion(Question question, LearnerProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v);
    /* modification d'une sous question */
    public CopexReturn updateQuestion(Question newQuestion, LearnerProcedure proc, Question oldQuestion, ArrayList v);
     /* modification d'une tache */
    public CopexReturn updateTask(CopexTask newTask, LearnerProcedure proc, CopexTask oldTask, char undoRedo, ArrayList v);
   
    /* imprimer */
    public void print();
    /* couper */
    public CopexReturn cut();
    /* copier */
    public CopexReturn copy();
    /* coller */
    public CopexReturn paste();
    public CopexReturn paste(LearnerProcedure proc, SubTree subTree, TaskSelected ts, char undoRedo, ArrayList v);
    
    /* supprimer */
    public CopexReturn suppr(ArrayList<TaskSelected> listTs, ArrayList v, boolean suppr, char undoRedo);
     /* couper depuis undo redo */
    public CopexReturn cut(ArrayList<TaskSelected> listTs, SubTree subTree, ArrayList v,char undoRedo);
        /* creation d'un nouveau dataSheet pour le protocole */
    public CopexReturn createDataSheet(LearnerProcedure proc, int nbR, int nbC, char undoRedo, ArrayList v);
   /* creation d'un nouveau protocole "vierge" */
    public CopexReturn createProc(String procName, InitialProcedure initProc);
    /* copie d'un protocole de la mission */
    public CopexReturn copyProc(String name, LearnerProcedure procToCopy);
    /* ouverture d'un protocole existant */
    public CopexReturn openProc(CopexMission missionToOpen, LearnerProcedure procToOpen);
    /* fermeture d'un protocole */
    public CopexReturn closeProc(LearnerProcedure proc);
    /* suppression d'un protocole */
    public CopexReturn deleteProc(LearnerProcedure proc);
    /* retourne en v[0] la liste des protocoles qui peuvent être copiés : 
     * liste des protocoles de la mission en cours
     * retourne en v[1] la liste des missions de l'utilisateur et en v[2]) la 
     * liste des protocoles pour chacune de ces missions
     */
    public CopexReturn getListProcToCopyOrOpen(ArrayList v);
    /* mise à jour du nom du protocole */
    public CopexReturn updateProcName(LearnerProcedure proc, String name, char undoRedo);
    /* mise à jour du protocole actif */
    public CopexReturn setProcActiv(LearnerProcedure proc);
    /* suppression dataSheet pour le protocole */
    public CopexReturn deleteDataSheet(LearnerProcedure proc, long dbKeyDataSheet, char undoRedo);
    /* modification d'un dataSheet pour le protocole */
    public CopexReturn modifyDataSheet(LearnerProcedure proc, int nbR, int nbC, char undoRedo);
    /* modification valeur dataSheet */
    public CopexReturn updateDataSheet(LearnerProcedure proc, String value, int noRow, int noCol, ArrayList v, char undoRedo);
    /* impression */
    public CopexReturn printCopex(LearnerProcedure procToPrint, boolean printComments, boolean printDataSheet);
    /* drag and drop */
    public CopexReturn move(TaskSelected task, SubTree tree, char undoRedo);
    public CopexReturn finalizeDragAndDrop(LearnerProcedure proc);
    /* arret de l'edp */
    public CopexReturn stopEdP();
    /* mise à jour de la visiblite des taches*/
    public CopexReturn updateTaskVisible(LearnerProcedure p, ArrayList<CopexTask> listTask);
    /* retourne les données pour le proc aide */
    public CopexReturn getHelpProc(ArrayList v);
    /* ajout de l'utilisation d'un material pour un proc*/
    public CopexReturn addMaterialUseForProc(LearnerProcedure p, Material m, String justification, char undoRedo);
     /* modification de l'utilisation d'un material pour un proc*/
    public CopexReturn updateMaterialUseForProc(LearnerProcedure p, Material m, String justification, char undoRedo);
    /* suppression de l'utilisation d'un material pour un proc*/
    public CopexReturn removeMaterialUseForProc(LearnerProcedure p, Material m, char undoRedo);
    /* exportation */
    public CopexReturn exportDataSheet(LearnerProcedure p, File file);
    /* ouverture fenetre aide*/
    public CopexReturn openHelpDialog();
    /* fermeture fenetre aide */
    public CopexReturn closeHelpDialog();
    /*ouverture proc d'aide */
    public CopexReturn openHelpProc();
    /* retourne l'elo exp proc */
    public Element getExperimentalProcedure(LearnerProcedure proc);
    /* chargement d'un ELO*/
    public CopexReturn loadELO(Element xmlContent);
    /*  creation d'un nouvel ELO*/
    public CopexReturn newELO();

    /* retourne la liste des parametres des actions de l'etape */
    public CopexReturn  getTaskInitialParam(LearnerProcedure proc, CopexTask task, ArrayList v);
    /* retourne la liste des output des actions de l'�tape */
    public CopexReturn getTaskInitialOutput(LearnerProcedure proc, CopexTask task, ArrayList v);
}
