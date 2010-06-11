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
 * controller interface
 * @author marjolaine
 */
public interface ControllerInterface {
    /** initialization of the copex editor */
    public CopexReturn initEdP(Locale locale, String idUser, long dbKeyMission, long dbKeyGroup, long dbKeyLabDoc, String fileMission);
    /** add a task */
    public CopexReturn addTask(CopexTask task, LearnerProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v, char undoRedo, boolean cut) ;
    /** add an action */
    public CopexReturn addAction(CopexAction action, LearnerProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v);
    /** modify an action */
    public CopexReturn updateAction(CopexAction newAction, LearnerProcedure proc, CopexAction oldAction, ArrayList v);
    /** add a step */
    public CopexReturn addStep(Step step, LearnerProcedure proc, CopexTask taskBrother, CopexTask taskParent, ArrayList v);
    /** update a step */
    public CopexReturn updateStep(Step newStep, LearnerProcedure proc, Step oldStep, ArrayList v);
    /** modify a question */
    public CopexReturn updateQuestion(Question newQuestion, LearnerProcedure proc, Question oldQuestion, ArrayList v);
     /** modify a task */
    public CopexReturn updateTask(CopexTask newTask, LearnerProcedure proc, CopexTask oldTask, char undoRedo, ArrayList v);
    /**paste some tasks*/
    public CopexReturn paste(LearnerProcedure proc, SubTree subTree, TaskSelected ts, char undoRedo, ArrayList v);
    /** delete some tasks*/
    public CopexReturn suppr(ArrayList<TaskSelected> listTs, ArrayList v, boolean suppr, char undoRedo);
    /**cut some tasks*/
    public CopexReturn cut(LearnerProcedure proc,SubTree subTree, TaskSelected ts);
    /** copy some tasks*/
    public CopexReturn copy(LearnerProcedure proc , TaskSelected ts,SubTree subTree);
    /** paste */
    public CopexReturn paste(LearnerProcedure proc , TaskSelected ts,SubTree subTree);
     /** cut- undo redo */
    public CopexReturn cut(ArrayList<TaskSelected> listTs, SubTree subTree, ArrayList v,char undoRedo);
    /** create a new procedure */
    public CopexReturn createProc(String procName, InitialProcedure initProc);
    /** copy of a proc  */
    public CopexReturn copyProc(String name, LearnerProcedure procToCopy);
    /**open an existing proc */
    public CopexReturn openProc(CopexMission missionToOpen, LearnerProcedure procToOpen);
    /**close a proc */
    public CopexReturn closeProc(LearnerProcedure proc);
    /** delete a proc */
    public CopexReturn deleteProc(LearnerProcedure proc);
    /** returns the list of the proc to be copied or opened
     * v[0] list of proc that can be copied: list of the proc of the current mission
     * v[1] list of the missions of the user
     * v[2] list of the proc of each mission
     */
    public CopexReturn getListProcToCopyOrOpen(ArrayList v);
    /**update the proc name*/
    public CopexReturn updateProcName(LearnerProcedure proc, String name, char undoRedo);
    /**update the current proc */
    public CopexReturn setProcActiv(LearnerProcedure proc);
    /**export pdf  */
    public CopexReturn printCopex(LearnerProcedure procToPrint);
    /**drag and drop */
    public CopexReturn move(TaskSelected task, SubTree tree, char undoRedo);
    /**drag and drop, finalize */
    public CopexReturn finalizeDragAndDrop(LearnerProcedure proc);
    /** quit the procedure editor */
    public CopexReturn stopEdP();
    /** update the visible tasks */
    public CopexReturn updateTaskVisible(LearnerProcedure p, ArrayList<CopexTask> listTask);
    /**gets the help procedure */
    public CopexReturn getHelpProc(ArrayList v);
    /** open the help dialog*/
    public CopexReturn openHelpDialog();
    /** close the help dialog */
    public CopexReturn closeHelpDialog();
    /** open the help procedure */
    public CopexReturn openHelpProc();
    /** gets the ELO xproc experimentalProcedure */
    public Element getExperimentalProcedure(LearnerProcedure proc);
    /** load an ELO*/
    public CopexReturn loadELO(Element xmlContent);
    /** create a new ELO */
    public CopexReturn newELO();
    /** returns the parameters list of the actions of a step */
    public CopexReturn  getTaskInitialParam(LearnerProcedure proc, CopexTask task, ArrayList v);
    /** returns the output of the actions of a step */
    public CopexReturn getTaskInitialOutput(LearnerProcedure proc, CopexTask task, ArrayList v);
    /** returns the default material type */
    public TypeMaterial getDefaultMaterialType();
    /** update the hypothesis of a proc*/
    public CopexReturn setHypothesis(LearnerProcedure proc, Hypothesis hypothesis, ArrayList v);
    /** update the general principle of the proc*/
    public CopexReturn setGeneralPrinciple(LearnerProcedure proc, GeneralPrinciple principle, ArrayList v);
    /** update the evaluation of the proc */
    public CopexReturn setEvaluation(LearnerProcedure proc, Evaluation evaluation, ArrayList v);
    /** update the material used*/
    public CopexReturn setMaterialUsed(LearnerProcedure proc, ArrayList<MaterialUsed> listMaterialToCreate, ArrayList<MaterialUsed> listMaterialToDelete, ArrayList<MaterialUsed> listMaterialToUpdate, ArrayList v);
    /** log a user action in the db*/
    public CopexReturn logUserActionInDB(String type, List<CopexProperty> attribute);
    /** returns if a proc is the proc of the labdoc  */
    public CopexReturn isLabDocProc(LearnerProcedure p, ArrayList v);
}
