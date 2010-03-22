/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.controller;

import eu.scy.client.tools.copex.common.CopexAction;
import eu.scy.client.tools.copex.common.CopexMission;
import eu.scy.client.tools.copex.common.CopexTask;
import eu.scy.client.tools.copex.common.Evaluation;
import eu.scy.client.tools.copex.common.GeneralPrinciple;
import eu.scy.client.tools.copex.common.Hypothesis;
import eu.scy.client.tools.copex.common.InitialProcedure;
import eu.scy.client.tools.copex.common.LearnerProcedure;
import eu.scy.client.tools.copex.common.MaterialUsed;
import eu.scy.client.tools.copex.common.Question;
import eu.scy.client.tools.copex.common.Step;
import eu.scy.client.tools.copex.common.TypeMaterial;
import eu.scy.client.tools.copex.dnd.SubTree;
import eu.scy.client.tools.copex.edp.TaskSelected;
import eu.scy.client.tools.copex.logger.CopexProperty;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.jdom.Element;
/**
 * interface du controller
 * @author MBO
 */
public interface ControllerInterface {
    /* initialisation de l'edp */
    public CopexReturn initEdP(Locale locale, String idUser, long dbKeyMission, int mode, String userName, String firstname, String logFileName);
    /* ajout d'une tache */
    public CopexReturn addTask(CopexTask task, LearnerProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v, char undoRedo, boolean cut) ;
    /* ajout d'une action */
    public CopexReturn addAction(CopexAction action, LearnerProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v);
    /* modification d'une action */
    public CopexReturn updateAction(CopexAction newAction, LearnerProcedure proc, CopexAction oldAction, ArrayList v);
    /* ajout d'une etape */
    public CopexReturn addStep(Step step, LearnerProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v);
    /* modification d'une etape */
    public CopexReturn updateStep(Step newStep, LearnerProcedure proc, Step oldStep, ArrayList v);
    /* modification d'une question */
    public CopexReturn updateQuestion(Question newQuestion, LearnerProcedure proc, Question oldQuestion, ArrayList v);
     /* modification d'une tache */
    public CopexReturn updateTask(CopexTask newTask, LearnerProcedure proc, CopexTask oldTask, char undoRedo, ArrayList v);
   
    public CopexReturn paste(LearnerProcedure proc, SubTree subTree, TaskSelected ts, char undoRedo, ArrayList v);
    
    /* supprimer */
    public CopexReturn suppr(ArrayList<TaskSelected> listTs, ArrayList v, boolean suppr, char undoRedo);
    public CopexReturn cut(LearnerProcedure proc,SubTree subTree, TaskSelected ts);
    public CopexReturn copy(LearnerProcedure proc , TaskSelected ts,SubTree subTree);
    public CopexReturn paste(LearnerProcedure proc , TaskSelected ts,SubTree subTree);
     /* couper depuis undo redo */
    public CopexReturn cut(ArrayList<TaskSelected> listTs, SubTree subTree, ArrayList v,char undoRedo);
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
    /* retourne en v[0] la liste des protocoles qui peuvent etre copies : 
     * liste des protocoles de la mission en cours
     * retourne en v[1] la liste des missions de l'utilisateur et en v[2]) la 
     * liste des protocoles pour chacune de ces missions
     */
    public CopexReturn getListProcToCopyOrOpen(ArrayList v);
    /* mise a jour du nom du protocole */
    public CopexReturn updateProcName(LearnerProcedure proc, String name, char undoRedo);
    /* mise a jour du protocole actif */
    public CopexReturn setProcActiv(LearnerProcedure proc);
    /* impression */
    public CopexReturn printCopex(LearnerProcedure procToPrint);
    /* drag and drop */
    public CopexReturn move(TaskSelected task, SubTree tree, char undoRedo);
    public CopexReturn finalizeDragAndDrop(LearnerProcedure proc);
    /* arret de l'edp */
    public CopexReturn stopEdP();
    /* mise a jour de la visiblite des taches*/
    public CopexReturn updateTaskVisible(LearnerProcedure p, ArrayList<CopexTask> listTask);
    /* retourne les donnees pour le proc aide */
    public CopexReturn getHelpProc(ArrayList v);
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
    /* retourne la liste des output des actions de l'etape */
    public CopexReturn getTaskInitialOutput(LearnerProcedure proc, CopexTask task, ArrayList v);
    /* retourne le type de material par defaut */
    public TypeMaterial getDefaultMaterialType();
    /* hypotheses du proc*/
    public CopexReturn setHypothesis(LearnerProcedure proc, Hypothesis hypothesis, ArrayList v);
    /* general principe du proc*/
    public CopexReturn setGeneralPrinciple(LearnerProcedure proc, GeneralPrinciple principle, ArrayList v);
    /* evaluation */
    public CopexReturn setEvaluation(LearnerProcedure proc, Evaluation evaluation, ArrayList v);
    /* mise a jour du material used */
    public CopexReturn setMaterialUsed(LearnerProcedure proc, ArrayList<MaterialUsed> listMaterialToCreate, ArrayList<MaterialUsed> listMaterialToDelete, ArrayList<MaterialUsed> listMaterialToUpdate, ArrayList v);
    /* log user action in db*/
    public CopexReturn logUserActionInDB(String type, List<CopexProperty> attribute);
}
