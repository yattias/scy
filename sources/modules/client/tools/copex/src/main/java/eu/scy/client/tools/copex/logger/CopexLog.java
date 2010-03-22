/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.logger;

import eu.scy.client.tools.copex.common.CopexTask;
import eu.scy.client.tools.copex.common.Evaluation;
import eu.scy.client.tools.copex.common.GeneralPrinciple;
import eu.scy.client.tools.copex.common.Hypothesis;
import eu.scy.client.tools.copex.common.LearnerProcedure;
import eu.scy.client.tools.copex.common.MaterialUsed;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * copex log
 * @author Marjolaine
 */
public class CopexLog {
    public final static String toolName = "copex";

    public final static String TAG_MISSION_ID = "mission_id";
    public final static String TAG_MISSION_CODE = "mission_code";
    public final static String TAG_PROC_ID = "id";
    public final static String TAG_PROC_NAME = "proc_name";
    public final static String TAG_PROC_ID_COPY = "id_to_copy";
    public final static String TAG_PROC_NAME_COPY = "proc_name_to_copy";
    public final static String TAG_TASK_ID = "id_task";
    public final static String TAG_TASK = "task";
    public final static String TAG_OLD = "old";
    public final static String TAG_NEW = "new";
    public final static String TAG_TASK_POSITION = "posistion";
    public final static String TAG_MATERIAL_USED = "material_used";

    /* log: open proc*/
    public static List<CopexProperty> logOpenProc(Locale locale, LearnerProcedure proc, long dbKeyMission, String missionCode){
        List<CopexProperty> list = new LinkedList();
        list.add(new CopexProperty(TAG_MISSION_ID, Long.toString(dbKeyMission), null));
        list.add(new CopexProperty(TAG_MISSION_CODE, missionCode, null));
        list.add(new CopexProperty(TAG_PROC_ID, Long.toString(proc.getDbKey()), null));
        list.add(new CopexProperty(TAG_PROC_NAME, proc.getName(locale), null));
        return list;
    }

    /* log:  proc*/
    public static List<CopexProperty> logProc(Locale locale, LearnerProcedure proc){
        List<CopexProperty> list = new LinkedList();
        list.add(new CopexProperty(TAG_PROC_ID, Long.toString(proc.getDbKey()), null));
        list.add(new CopexProperty(TAG_PROC_NAME, proc.getName(locale), null));
        return list;
    }

     /* log: rename proc*/
    public static List<CopexProperty> logRenameProc(LearnerProcedure proc, String oldName, String newName){
        List<CopexProperty> list = new LinkedList();
        list.add(new CopexProperty(TAG_PROC_ID, Long.toString(proc.getDbKey()), null));
        list.add(new CopexProperty(TAG_OLD, oldName, null));
        list.add(new CopexProperty(TAG_NEW, newName, null));
        return list;
    }

    /* log:  copy proc*/
    public static List<CopexProperty> logCopyProc(Locale locale, LearnerProcedure proc, LearnerProcedure copyToProc){
        List<CopexProperty> list = new LinkedList();
        list.add(new CopexProperty(TAG_PROC_ID, Long.toString(proc.getDbKey()), null));
        list.add(new CopexProperty(TAG_PROC_NAME, proc.getName(locale), null));
        list.add(new CopexProperty(TAG_PROC_ID_COPY, Long.toString(proc.getDbKey()), null));
        list.add(new CopexProperty(TAG_PROC_NAME_COPY, proc.getName(locale), null));
        return list;
    }
    /* log : update task */
    public static List<CopexProperty> logUpdateTask(Locale locale, LearnerProcedure proc, CopexTask oldTask, CopexTask newTask){
        List<CopexProperty> list = new LinkedList();
        list.add(new CopexProperty(TAG_PROC_ID, Long.toString(proc.getDbKey()), null));
        list.add(new CopexProperty(TAG_PROC_NAME, proc.getName(locale), null));
        list.add(new CopexProperty(TAG_TASK_ID, Long.toString(oldTask.getDbKey()), null));
        list.add(new CopexProperty(TAG_OLD, "oldTask", oldTask.toXML()));
        list.add(new CopexProperty(TAG_NEW, "newTask", newTask.toXML()));
        return list;
    }

    /* log: add task*/
    public static List<CopexProperty> logAddTask(Locale locale, LearnerProcedure proc, CopexTask task, TaskTreePosition position){
        List<CopexProperty> list = new LinkedList();
        list.add(new CopexProperty(TAG_PROC_ID, Long.toString(proc.getDbKey()), null));
        list.add(new CopexProperty(TAG_PROC_NAME, proc.getName(locale), null));
        list.add(new CopexProperty(TAG_NEW, "", task.toXML()));
        list.add(new CopexProperty(TAG_TASK_POSITION, "task_position", position.toXML()));
        return list;
    }


    /* log : task */
    public static List<CopexProperty> logTask(Locale locale,LearnerProcedure proc, CopexTask task, TaskTreePosition position){
        List<CopexProperty> list = new LinkedList();
        list.add(new CopexProperty(TAG_PROC_ID, Long.toString(proc.getDbKey()), null));
        list.add(new CopexProperty(TAG_PROC_NAME, proc.getName(locale), null));
        list.add(new CopexProperty(TAG_TASK, "", task.toXML()));
        list.add(new CopexProperty(TAG_TASK_POSITION, "task_position", position.toXML()));
        return list;
    }

    /* log : tasks list */
    public static List<CopexProperty> logListTask(Locale locale,LearnerProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask){
        List<CopexProperty> list = new LinkedList();
        list.add(new CopexProperty(TAG_PROC_ID, Long.toString(proc.getDbKey()), null));
        list.add(new CopexProperty(TAG_PROC_NAME, proc.getName(locale), null));
        int nb = listTask.size();
        for (int i=0; i<nb; i++){
            list.add(new CopexProperty(TAG_TASK, "", listTask.get(i).toXML()));
            list.add(new CopexProperty(TAG_TASK_POSITION, "task_position", listPositionTask.get(i).toXML()));
        }
        return list;
    }

    /* log : tasks list */
    public static List<CopexProperty> logDragDrop(Locale locale,LearnerProcedure proc, ArrayList<CopexTask> listTask, List<TaskTreePosition> listPositionTask, TaskTreePosition insertPosition){
        List<CopexProperty> list = new LinkedList();
        list.add(new CopexProperty(TAG_PROC_ID, Long.toString(proc.getDbKey()), null));
        list.add(new CopexProperty(TAG_PROC_NAME, proc.getName(locale), null));
        int nb = listTask.size();
        for (int i=0; i<nb; i++){
            list.add(new CopexProperty(TAG_TASK, "", listTask.get(i).toXML()));
            list.add(new CopexProperty(TAG_TASK_POSITION, "task_position", listPositionTask.get(i).toXML()));
        }
        list.add(new CopexProperty(TAG_TASK_POSITION, "insert_task_position", insertPosition.toXML()));
        return list;
    }

    /* log : hypothesis */
    public static List<CopexProperty> logHypothesis(Locale locale, LearnerProcedure proc, Hypothesis oldHypothesis, Hypothesis newHypothesis){
        List<CopexProperty> list = new LinkedList();
        list.add(new CopexProperty(TAG_PROC_ID, Long.toString(proc.getDbKey()), null));
        list.add(new CopexProperty(TAG_PROC_NAME, proc.getName(locale), null));
        if(oldHypothesis != null){
            list.add(new CopexProperty(TAG_OLD, "old_hypothesis", oldHypothesis.toXML()));
        }
        if(newHypothesis != null){
            list.add(new CopexProperty(TAG_NEW, "new_hypothesis", newHypothesis.toXML()));
        }
        return list;
    }
    /* log : genral principle */
    public static List<CopexProperty> logGeneralPrinciple(Locale locale, LearnerProcedure proc, GeneralPrinciple oldGeneralPrinciple, GeneralPrinciple newGeneralPrinciple){
        List<CopexProperty> list = new LinkedList();
        list.add(new CopexProperty(TAG_PROC_ID, Long.toString(proc.getDbKey()), null));
        list.add(new CopexProperty(TAG_PROC_NAME, proc.getName(locale), null));
        if(oldGeneralPrinciple != null){
            list.add(new CopexProperty(TAG_OLD, "old_general_principle", oldGeneralPrinciple.toXML()));
        }
        if(newGeneralPrinciple != null){
            list.add(new CopexProperty(TAG_NEW, "new_general_principle", newGeneralPrinciple.toXML()));
        }
        return list;
    }
    /* log : evaluation */
    public static List<CopexProperty> logEvaluation(Locale locale, LearnerProcedure proc, Evaluation oldEvaluation, Evaluation newEvaluation){
        List<CopexProperty> list = new LinkedList();
        list.add(new CopexProperty(TAG_PROC_ID, Long.toString(proc.getDbKey()), null));
        list.add(new CopexProperty(TAG_PROC_NAME, proc.getName(locale), null));
        if(oldEvaluation != null){
            list.add(new CopexProperty(TAG_OLD, "old_evaluation", oldEvaluation.toXML()));
        }
        if(newEvaluation != null){
            list.add(new CopexProperty(TAG_NEW, "new_evaluation", newEvaluation.toXML()));
        }
        return list;
    }

    /* log : material used */
    public static List<CopexProperty> logMaterialUsed(Locale locale, LearnerProcedure proc, ArrayList<MaterialUsed> listMaterialUsed){
        List<CopexProperty> list = new LinkedList();
        list.add(new CopexProperty(TAG_PROC_ID, Long.toString(proc.getDbKey()), null));
        list.add(new CopexProperty(TAG_PROC_NAME, proc.getName(locale), null));
        for(Iterator<MaterialUsed> m = listMaterialUsed.iterator();m.hasNext();){
            list.add(new CopexProperty(TAG_MATERIAL_USED, "material_used", m.next().toXML()));
        }
        return list;
    }

     /* log : update material used */
    public static List<CopexProperty> logUpdateMaterialUsed(Locale locale, LearnerProcedure proc, ArrayList<MaterialUsed> oldListMaterialUsed, ArrayList<MaterialUsed> newListMaterialUsed){
        List<CopexProperty> list = new LinkedList();
        list.add(new CopexProperty(TAG_PROC_ID, Long.toString(proc.getDbKey()), null));
        list.add(new CopexProperty(TAG_PROC_NAME, proc.getName(locale), null));
        int nb = oldListMaterialUsed.size();
        for (int i=0; i<nb; i++){
            list.add(new CopexProperty(TAG_OLD, "old_material_used", oldListMaterialUsed.get(i).toXML()));
            list.add(new CopexProperty(TAG_NEW, "new_material_used", newListMaterialUsed.get(i).toXML()));
        }
        return list;
    }
   
}
