/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;


import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Element;


/**
 * gere le chargement des taches
 * @author MBO
 */
public class TaskFromDB {
    private static final Logger logger = Logger.getLogger(TaskFromDB.class.getName());
   
    /* chargement des t�ches liees a un protocole */
    public static CopexReturn getAllTaskFromDB(DataBaseCommunication dbC,Locale locale,  long idProc, long idQuestion, ArrayList<InitialNamedAction> listInitialNamedActions, ArrayList<Material> listMaterial, ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList v){
        ArrayList<CopexTask> listT = new ArrayList();
        ArrayList v2 = new ArrayList();
        // chargement des questions 
        CopexReturn cr = getAllQuestionProcFromDB(dbC, locale,idProc, idQuestion, v2);
        if (cr.isError())
            return cr;
        listT = (ArrayList<CopexTask>)v2.get(0);
        
        // chargement des actions
        v2 = new ArrayList();
        cr = getAllActionsProcFromDB(dbC, locale, idProc, listInitialNamedActions,  listMaterial,listPhysicalQuantity,  v2);
        if (cr.isError())
            return cr;
        ArrayList<CopexTask> listA = (ArrayList<CopexTask>)v2.get(0);
        int s = listA.size();
        // pour le chargement des etapes iteratives
        ArrayList<ActionParam> list = new ArrayList();
        ArrayList<Material> listMaterialProd = new ArrayList();
        ArrayList<QData> listDataProd = new ArrayList();

        for (int i=0; i<s; i++){
            listT.add(listA.get(i));
            if (listA.get(i) instanceof CopexActionParam){
                Object[] t = ((CopexActionParam)listA.get(i)).getTabParam();
                for (int j=0; j<t.length; j++){
                    if(t[j] instanceof ActionParam)
                        list.add((ActionParam)t[j]);
                    else if (t[j] instanceof ArrayList){
                        ArrayList<ActionParam> p = (ArrayList<ActionParam>)t[j];
                        int np = p.size();
                        for (int k=0; k<np; k++){
                            list.add(p.get(k));
                        }
                    }
                }
            }
            if(listA.get(i) instanceof CopexActionManipulation){
                ArrayList<Object> l = ((CopexActionManipulation)listA.get(i)).getListMaterialProd();
                int n = l.size();
                for (int j=0; j<n; j++){
                    if (l.get(j) instanceof Material)
                        listMaterialProd.add((Material)l.get(j));
                    else if (l.get(j) instanceof ArrayList){
                        int m = ((ArrayList)l.get(j)).size();
                        for (int o=0; o<m; o++){
                            listMaterialProd.add(((ArrayList<Material>)l.get(j)).get(o));
                        }
                    }
                }
            }else if (listA.get(i) instanceof CopexActionAcquisition){
                ArrayList<Object> l = ((CopexActionAcquisition)listA.get(i)).getListDataProd();
                int n = l.size();
                for (int j=0; j<n; j++){
                    if (l.get(j) instanceof QData)
                        listDataProd.add((QData)l.get(j));
                    else if (l.get(j) instanceof ArrayList){
                        int m = ((ArrayList)l.get(j)).size();
                        for (int o=0; o<m; o++){
                            listDataProd.add(((ArrayList<QData>)l.get(j)).get(o));
                        }
                    }
                }
            }else if (listA.get(i) instanceof CopexActionTreatment){
                ArrayList<Object> l = ((CopexActionTreatment)listA.get(i)).getListDataProd();
                int n = l.size();
                for (int j=0; j<n; j++){
                    if (l.get(j) instanceof QData)
                        listDataProd.add((QData)l.get(j));
                    else if (l.get(j) instanceof ArrayList){
                        int m = ((ArrayList)l.get(j)).size();
                        for (int o=0; o<m; o++){
                            listDataProd.add(((ArrayList<QData>)l.get(j)).get(o));
                        }
                    }
                }
            }
        }
        // transformation en tableau
        int nb = list.size();
        ActionParam[] listActionParam = new ActionParam[nb];
        for (int i=0; i<nb; i++){
            listActionParam[i] = list.get(i);
        }
        // chargement des etapes
        v2 = new ArrayList();
        cr = getAllStepProcFromDB(dbC, locale,idProc, listInitialNamedActions,listActionParam, listMaterialProd, listDataProd, v2);
        if (cr.isError())
            return cr;
        ArrayList<CopexTask> listS = (ArrayList<CopexTask>)v2.get(0);
        s = listS.size();
        for (int i=0; i<s; i++){
            listT.add(listS.get(i));
        }
        
        v.add(listT);
        return new CopexReturn();
    }
    
    
    
    
     /* chargement des freres et enfants */
    public static CopexReturn getBrotherAndChildFromDB(DataBaseCommunication dbC, long idTask, ArrayList v){
        long dbKeyBrother = -1;
        long dbKeyChild = -1;
        
        String queryBrother = "SELECT ID_TASK_BROTHER FROM LINK_BROTHER " +
                "WHERE ID_TASK = "+idTask+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_TASK_BROTHER");
        CopexReturn cr = dbC.sendQuery(queryBrother, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_TASK_BROTHER");
            if (s == null)
                continue;
            dbKeyBrother = Long.parseLong(s);
        }
        String queryChild = "SELECT ID_TASK_CHILD FROM LINK_CHILD " +
                "WHERE ID_TASK = "+idTask+" ;";
        v2 = new ArrayList();
        listFields = new ArrayList();
        listFields.add("ID_TASK_CHILD");
        cr = dbC.sendQuery(queryChild, listFields, v2);
        if (cr.isError())
            return cr;
        nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_TASK_CHILD");
            if (s == null)
                continue;
            dbKeyChild = Long.parseLong(s);
        }
            
        
        v.add(dbKeyBrother);
        v.add(dbKeyChild);
        return new CopexReturn();
    }
    
    
   
    
    /* chargement des taches questions liees a un protocole */
    public static CopexReturn getAllQuestionProcFromDB(DataBaseCommunication dbC,Locale locale, long idProc, long idQuestion, ArrayList v){
        ArrayList<CopexTask> listQ = new ArrayList();
        String query = "SELECT T.TASK_NAME, T.DESCRIPTION, T.COMMENTS,T.TASK_IMAGE,T.DRAW_ELO,  T.IS_VISIBLE,  R.EDIT_RIGHT, R.DELETE_RIGHT, R.COPY_RIGHT, R.MOVE_RIGHT, R.PARENT_RIGHT, R.DRAW_RIGHT, R.REPEAT_RIGHT, " +
                " Q.ID_QUESTION FROM COPEX_TASK T, QUESTION Q, LINK_PROC_TASK L, TASK_RIGHT R  " +
                "WHERE L.ID_PROC = "+idProc+" AND L.ID_TASK = T.ID_TASK AND L.ID_TASK = Q.ID_QUESTION AND L.ID_TASK = R.ID_TASK;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("T.TASK_NAME");
        listFields.add("T.DESCRIPTION");
        listFields.add("T.COMMENTS");
        listFields.add("T.TASK_IMAGE");
        listFields.add("T.DRAW_ELO");
        listFields.add("T.IS_VISIBLE");
        listFields.add("R.EDIT_RIGHT");
        listFields.add("R.DELETE_RIGHT");
        listFields.add("R.COPY_RIGHT");
        listFields.add("R.MOVE_RIGHT");
        listFields.add("R.PARENT_RIGHT");
        listFields.add("R.DRAW_RIGHT");
        listFields.add("R.REPEAT_RIGHT");
        listFields.add("Q.ID_QUESTION");
        
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String name = rs.getColumnData("T.TASK_NAME");
            if (name == null)
                continue;
            String description = rs.getColumnData("T.DESCRIPTION");
            if (description == null)
                continue;
            String comments = rs.getColumnData("T.COMMENTS");
            if (comments == null)
                continue;
            String taskImage = rs.getColumnData("T.TASK_IMAGE");
            if (taskImage == null)
                continue;
            String s = rs.getColumnData("T.DRAW_ELO");
            Element draw = CopexUtilities.getElement(s);
            s = rs.getColumnData("T.IS_VISIBLE");
            if (s == null)
                continue;
            int vis = 1;
            try{
                vis = Integer.parseInt(s);
            }catch(NumberFormatException e){
                vis = 1;
            }
            boolean isVisible = vis == 1;
            String right = rs.getColumnData("R.EDIT_RIGHT");
            if (right == null)
                continue;
            char editR = right.charAt(0);
            right = rs.getColumnData("R.DELETE_RIGHT");
            if (right == null)
                continue;
            char deleteR = right.charAt(0);
            right = rs.getColumnData("R.COPY_RIGHT");
            if (right == null)
                continue;
            char copyR = right.charAt(0);
            right = rs.getColumnData("R.MOVE_RIGHT");
            if (right == null)
                continue;
            char moveR = right.charAt(0);
            right = rs.getColumnData("R.PARENT_RIGHT");
            if (right == null)
                continue;
            char parentR = right.charAt(0);
            right = rs.getColumnData("R.DRAW_RIGHT");
            if (right == null)
                continue;
            char drawR = right.charAt(0);
            right = rs.getColumnData("R.REPEAT_RIGHT");
            if (right == null)
                continue;
            char repeatR = right.charAt(0);
            s = rs.getColumnData("Q.ID_QUESTION");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            boolean root = (dbKey == idQuestion);
            Question question = new Question(dbKey, locale,name, description,  comments, taskImage, draw,  isVisible, new TaskRight(editR, deleteR, copyR, moveR, parentR, drawR, repeatR), root);
            // charge l'id frere et enfant 
            ArrayList v3 = new ArrayList();
            cr = getBrotherAndChildFromDB(dbC, dbKey, v3);
            if (cr.isError())
                return cr;
            long dbKeyBrother = (Long)v3.get(0);
            long dbKeyChild = (Long)v3.get(1);
            question.setDbKeyBrother(dbKeyBrother);
            question.setDbKeyChild(dbKeyChild);
                       
            listQ.add(question);
        }
               
        v.add(listQ);
        return new CopexReturn();
    }
    
    
    /* chargement des taches etapes liees a un protocole */
    public static CopexReturn getAllStepProcFromDB(DataBaseCommunication dbC, Locale locale,long idProc, ArrayList<InitialNamedAction> listInitAction,  ActionParam[] listActionParam, ArrayList<Material> listMaterialProd, ArrayList<QData> listDataProd, ArrayList v){
        ArrayList<CopexTask> listS = new ArrayList();
        String query = "SELECT T.TASK_NAME, T.DESCRIPTION, T.COMMENTS, T.TASK_IMAGE, T.DRAW_ELO, T.IS_VISIBLE, R.EDIT_RIGHT, R.DELETE_RIGHT, R.COPY_RIGHT, R.MOVE_RIGHT, R.PARENT_RIGHT, R.DRAW_RIGHT, R.REPEAT_RIGHT, " +
                "S.ID_STEP FROM COPEX_TASK T, STEP S, LINK_PROC_TASK L, TASK_RIGHT R " +
                "WHERE L.ID_PROC = "+idProc+" AND L.ID_TASK = T.ID_TASK AND L.ID_TASK = S.ID_STEP AND L.ID_TASK = R.ID_TASK;";
        
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("T.TASK_NAME");
        listFields.add("T.DESCRIPTION");
        listFields.add("T.COMMENTS");
        listFields.add("T.TASK_IMAGE");
        listFields.add("T.DRAW_ELO");
        listFields.add("T.IS_VISIBLE");
        listFields.add("R.EDIT_RIGHT");
        listFields.add("R.DELETE_RIGHT");
        listFields.add("R.COPY_RIGHT");
        listFields.add("R.MOVE_RIGHT");
        listFields.add("R.PARENT_RIGHT");
        listFields.add("R.DRAW_RIGHT");
        listFields.add("R.REPEAT_RIGHT");
        listFields.add("S.ID_STEP");
        
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String name = rs.getColumnData("T.TASK_NAME");
            if (name == null)
                continue;
            String description = rs.getColumnData("T.DESCRIPTION");
            if (description == null)
                continue;
            String comments = rs.getColumnData("T.COMMENTS");
            if (comments == null)
                continue;
            String taskImage = rs.getColumnData("T.TASK_IMAGE");
            if (taskImage == null)
                continue;
            String s = rs.getColumnData("T.DRAW_ELO");
            Element draw = CopexUtilities.getElement(s);
            s = rs.getColumnData("T.IS_VISIBLE");
            if (s == null)
                continue;
            int vis = 0;
            try{
                vis = Integer.parseInt(s);
            }catch(NumberFormatException e){
                vis = 1;
            }
            boolean isVisible = vis == 1;
            String right = rs.getColumnData("R.EDIT_RIGHT");
            if (right == null)
                continue;
            char editR = right.charAt(0);
            right = rs.getColumnData("R.DELETE_RIGHT");
            if (right == null)
                continue;
            char deleteR = right.charAt(0);
            right = rs.getColumnData("R.COPY_RIGHT");
            if (right == null)
                continue;
            char copyR = right.charAt(0);
            right = rs.getColumnData("R.MOVE_RIGHT");
            if (right == null)
                continue;
            char moveR = right.charAt(0);
            right = rs.getColumnData("R.PARENT_RIGHT");
            if (right == null)
                continue;
            char parentR = right.charAt(0);
            right = rs.getColumnData("R.DRAW_RIGHT");
            if (right == null)
                continue;
            char drawR = right.charAt(0);
            right = rs.getColumnData("R.REPEAT_RIGHT");
            if (right == null)
                continue;
            char repeatR = right.charAt(0);
            s = rs.getColumnData("S.ID_STEP");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            //tache iterative
            // recuperation taches iteratives
            ArrayList v3 = new ArrayList();
            cr = getTaskRepeatFromDB(dbC, dbKey, listInitAction, listActionParam, listMaterialProd, listDataProd, v3);
            if(cr.isError())
                return cr;
            TaskRepeat taskRepeat = null;
            if(v3.size() > 0 && v3.get(0) != null)
                taskRepeat = (TaskRepeat)v3.get(0);
            Step step = new Step(dbKey, locale,name, description, comments, taskImage, draw, isVisible, new TaskRight(editR, deleteR, copyR, moveR, parentR, drawR, repeatR), taskRepeat);
            // charge l'id frere et enfant 
            v3 = new ArrayList();
            cr = getBrotherAndChildFromDB(dbC, dbKey, v3);
            if (cr.isError())
                return cr;
            long dbKeyBrother = (Long)v3.get(0);
            long dbKeyChild = (Long)v3.get(1);
            step.setDbKeyBrother(dbKeyBrother);
            step.setDbKeyChild(dbKeyChild);
                       
            listS.add(step);
        }
        v.add(listS);
        return new CopexReturn();
    }
    
    
   
    
    /* chargement des taches actions liees a un protocole */
    public static CopexReturn getAllActionsProcFromDB(DataBaseCommunication dbC, Locale locale, long idProc, ArrayList<InitialNamedAction> listInitialNamedAction, ArrayList<Material> listMaterial, ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList v){
        ArrayList<CopexTask> listA = new ArrayList();
        String query = "SELECT T.TASK_NAME, T.DESCRIPTION, T.COMMENTS, T.TASK_IMAGE, T.DRAW_ELO, T.IS_VISIBLE, R.EDIT_RIGHT, R.DELETE_RIGHT, R.COPY_RIGHT, R.MOVE_RIGHT, R.PARENT_RIGHT, R.DRAW_RIGHT, R.REPEAT_RIGHT,  " +
                "A.ID_ACTION FROM COPEX_TASK T, ACTION A, LINK_PROC_TASK L, TASK_RIGHT R " +
                "WHERE L.ID_PROC = "+idProc+" AND L.ID_TASK = T.ID_TASK AND L.ID_TASK = A.ID_ACTION AND L.ID_TASK = R.ID_TASK  ;";
        
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("T.TASK_NAME");
        listFields.add("T.DESCRIPTION");
        listFields.add("T.COMMENTS");
        listFields.add("T.TASK_IMAGE");
        listFields.add("T.DRAW_ELO");
        listFields.add("T.IS_VISIBLE");
        listFields.add("R.EDIT_RIGHT");
        listFields.add("R.DELETE_RIGHT");
        listFields.add("R.COPY_RIGHT");
        listFields.add("R.MOVE_RIGHT");
        listFields.add("R.PARENT_RIGHT");
        listFields.add("R.DRAW_RIGHT");
        listFields.add("R.REPEAT_RIGHT");
        listFields.add("A.ID_ACTION");
        
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String name = rs.getColumnData("T.TASK_NAME");
            if (name == null)
                continue;
            String description = rs.getColumnData("T.DESCRIPTION");
            if (description == null)
                continue;
            String comments = rs.getColumnData("T.COMMENTS");
            if (comments == null)
                continue;
            String taskImage = rs.getColumnData("T.TASK_IMAGE");
            if (taskImage == null)
                continue;
            String s = rs.getColumnData("T.DRAW_ELO");
            Element draw = CopexUtilities.getElement(s);
            s = rs.getColumnData("T.IS_VISIBLE");
            if (s == null)
                continue;
            int vis = 0;
            try{
                vis = Integer.parseInt(s);
            }catch(NumberFormatException e){
                vis = 1;
            }
            boolean isVisible = vis == 1;
            String right = rs.getColumnData("R.EDIT_RIGHT");
            if (right == null)
                continue;
            char editR = right.charAt(0);
            right = rs.getColumnData("R.DELETE_RIGHT");
            if (right == null)
                continue;
            char deleteR = right.charAt(0);
            right = rs.getColumnData("R.COPY_RIGHT");
            if (right == null)
                continue;
            char copyR = right.charAt(0);
            right = rs.getColumnData("R.MOVE_RIGHT");
            if (right == null)
                continue;
            char moveR = right.charAt(0);
            right = rs.getColumnData("R.PARENT_RIGHT");
            if (right == null)
                continue;
            char parentR = right.charAt(0);
            right = rs.getColumnData("R.DRAW_RIGHT");
            if (right == null)
                continue;
            char drawR = right.charAt(0);
            right = rs.getColumnData("R.REPEAT_RIGHT");
            if (right == null)
                continue;
            char repeatR = right.charAt(0);
            s = rs.getColumnData("A.ID_ACTION");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            // recup de l'action nommee
            ArrayList v3 = new ArrayList();
            cr = getNamedActionFromDB(dbC, dbKey, listInitialNamedAction,  v3);
            if (cr.isError())
                return cr;
            InitialNamedAction namedAction = (InitialNamedAction)v3.get(0);
            // recuperation eventuellement des parametres
            v3 = new ArrayList();
            cr = getActionParamFromDB(dbC, locale, dbKey, namedAction, listMaterial,  listPhysicalQuantity, v3);
            if (cr.isError())
                return cr;
            Object[] tabParam = (Object[])v3.get(0);
            CopexAction action = null;

            ArrayList<Material> listMaterialProd = new ArrayList();
            ArrayList<QData> listDataProd = new ArrayList();
            if (namedAction == null){
                action = new CopexAction(dbKey, locale,name, description, comments, taskImage,draw, isVisible, new TaskRight(editR, deleteR, copyR, moveR, parentR, drawR, repeatR), null);
            }else{
                TaskRight taskRight = new TaskRight(editR, deleteR, copyR, moveR, parentR, drawR, repeatR);
                if (namedAction.isSetting()){
                    
                    if (namedAction instanceof InitialActionChoice){
                        action = new CopexActionChoice(dbKey,locale, name, description, comments, taskImage, draw, isVisible, taskRight, namedAction, tabParam, null);
                    }else if (namedAction instanceof InitialActionManipulation){
                        v3 = new ArrayList();
                        cr = getMaterialProdFromDB(dbC, locale, dbKey, listPhysicalQuantity, v3);
                        if (cr.isError())
                            return cr;
                        listMaterialProd = (ArrayList<Material>)v3.get(0);
                        action = new CopexActionManipulation(dbKey, locale,name, description, comments, taskImage, draw, isVisible, taskRight, namedAction, tabParam, null, null);
                    }else if (namedAction instanceof InitialActionAcquisition){
                        v3 = new ArrayList();
                        cr = getDataProdFromDB(dbC, locale, dbKey,listPhysicalQuantity, v3);
                        if (cr.isError())
                            return cr;
                        listDataProd = (ArrayList<QData>)v3.get(0);
                        action = new CopexActionAcquisition(dbKey,locale, name, description, comments, taskImage, draw, isVisible, taskRight, namedAction, tabParam, null, null);
                    }else if (namedAction instanceof InitialActionTreatment){
                        v3 = new ArrayList();
                        cr = getDataProdFromDB(dbC,locale,  dbKey, listPhysicalQuantity,v3);
                        if (cr.isError())
                            return cr;
                        listDataProd = (ArrayList<QData>)v3.get(0);
                        action = new CopexActionTreatment(dbKey,locale, name, description, comments, taskImage, draw, isVisible, taskRight, namedAction, tabParam, null, null);
                    }
                }else
                    action = new CopexActionNamed(dbKey, locale,name, description, comments, taskImage,draw, isVisible, taskRight, namedAction, null);
            }
            // recuperation taches iteratives
            v3 = new ArrayList();
            cr = getTaskRepeatFromDB(dbC, dbKey, listInitialNamedAction, tabParam,listMaterialProd, listDataProd, v3);
            if(cr.isError())
                return cr;
            TaskRepeat taskRepeat = null;
            if(v3.size() > 0 && v3.get(0) != null)
                taskRepeat = (TaskRepeat)v3.get(0);
            action.setTaskRepeat(taskRepeat);
            //
            if(action instanceof CopexActionManipulation ){
                ArrayList<Object> l = new ArrayList();
                int n = listMaterialProd.size();
                for (int o=0; o<n; o++){
                    if(taskRepeat == null){
                        l.add(listMaterialProd.get(o));
                    }else{
                        ArrayList<InitialManipulationOutput> listOutput = ((InitialActionManipulation)namedAction).getListOutput();
                        l = getListMaterialProdRepeat(taskRepeat,listMaterialProd,  listOutput);
                    }

                }
                ((CopexActionManipulation)action).setListMaterialProd(l);
            }else if (action instanceof CopexActionAcquisition){
                ArrayList<Object> l = new ArrayList();
                int n = listDataProd.size();
                for (int o=0; o<n; o++){
                    if(taskRepeat == null){
                        l.add(listDataProd.get(o));
                    }else{
                        ArrayList<InitialAcquisitionOutput> listOutput = ((InitialActionAcquisition)namedAction).getListOutput();
                        l = getListDataProdRepeat(taskRepeat,listDataProd,  listOutput);
                    }

                }
                ((CopexActionAcquisition)action).setListDataProd(l);
            }else if (action instanceof CopexActionTreatment){
                ArrayList<Object> l = new ArrayList();
                int n = listDataProd.size();
                for (int o=0; o<n; o++){
                    if(taskRepeat == null){
                        l.add(listDataProd.get(o));
                    }else{
                        ArrayList<InitialTreatmentOutput> listOutput = ((InitialActionTreatment)namedAction).getListOutput();
                        l = getListDataProdTRepeat(taskRepeat,listDataProd,  listOutput);
                    }

                }
                ((CopexActionTreatment)action).setListDataProd(l);
            }
            // charge l'id frere et enfant 
            v3 = new ArrayList();
            cr = getBrotherAndChildFromDB(dbC, dbKey, v3);
            if (cr.isError())
                return cr;
            long dbKeyBrother = (Long)v3.get(0);
            long dbKeyChild = (Long)v3.get(1);
            action.setDbKeyBrother(dbKeyBrother);
            action.setDbKeyChild(dbKeyChild);
                       
            listA.add(action);
        }
        v.add(listA);
        return new CopexReturn();
    }


    private static ArrayList<Object> getListMaterialProdRepeat(TaskRepeat taskRepeat, ArrayList<Material> listMat, ArrayList<InitialManipulationOutput> listOutput){
        ArrayList<Object> list = new ArrayList();
        int nbMat = listMat.size();
        for (int i=0; i<nbMat; i++){
            list.add(listMat.get(i));
        }
        ArrayList<TaskRepeatParam> listTaskRepeatParam  = taskRepeat.getListParam();
        int nbO = listOutput.size();
        int nb = listTaskRepeatParam.size();
        for (int i=0; i<nb; i++){
            if (listTaskRepeatParam.get(i) instanceof TaskRepeatParamOutputManipulation){
                TaskRepeatParamOutputManipulation r = (TaskRepeatParamOutputManipulation)listTaskRepeatParam.get(i);
                ArrayList<TaskRepeatValueMaterialProd> values = r.getListValue();
                InitialManipulationOutput out = r.getOutput();
                int nbV = values.size();
                ArrayList<Material> l = new ArrayList();
                for (int j=0; j<nbV; j++){
                    l.add(values.get(j).getMaterial());
                    list.set(j, null);
                }
                int id=-1;
                for (int j=0; j<nbO; j++){
                    if (listOutput.get(j).getDbKey() == out.getDbKey()){
                        id = j;
                        break;
                    }
                }
                if(id != -1){
                    list.set(id, l);
                }
            }
        }
        return list;
    }


    private static ArrayList<Object> getListDataProdRepeat(TaskRepeat taskRepeat, ArrayList<QData> listData, ArrayList<InitialAcquisitionOutput> listOutput){
        ArrayList<Object> list = new ArrayList();
        int nbData = listData.size();
        for (int i=0; i<nbData; i++){
            list.add(listData.get(i));
        }
        ArrayList<TaskRepeatParam> listTaskRepeatParam  = taskRepeat.getListParam();
        int nbO = listOutput.size();
        int nb = listTaskRepeatParam.size();
        for (int i=0; i<nb; i++){
            if (listTaskRepeatParam.get(i) instanceof TaskRepeatParamOutputAcquisition){
                TaskRepeatParamOutputAcquisition r = (TaskRepeatParamOutputAcquisition)listTaskRepeatParam.get(i);
                ArrayList<TaskRepeatValueDataProd> values = r.getListValue();
                InitialAcquisitionOutput out = r.getOutput();
                int nbV = values.size();
                ArrayList<QData> l = new ArrayList();
                for (int j=0; j<nbV; j++){
                    l.add(values.get(j).getData());
                    list.set(j, null);
                }
                int id=-1;
                for (int j=0; j<nbO; j++){
                    if (listOutput.get(j).getDbKey() == out.getDbKey()){
                        id = j;
                        break;
                    }
                }
                if(id != -1){
                    list.set(id, l);
                }
            }
        }
        return list;
    }

    private static ArrayList<Object> getListDataProdTRepeat(TaskRepeat taskRepeat, ArrayList<QData> listData, ArrayList<InitialTreatmentOutput> listOutput){
        ArrayList<Object> list = new ArrayList();
        int nbData = listData.size();
        for (int i=0; i<nbData; i++){
            list.add(listData.get(i));
        }
        ArrayList<TaskRepeatParam> listTaskRepeatParam  = taskRepeat.getListParam();
        int nbO = listOutput.size();
        int nb = listTaskRepeatParam.size();
        for (int i=0; i<nb; i++){
            if (listTaskRepeatParam.get(i) instanceof TaskRepeatParamOutputAcquisition){
                TaskRepeatParamOutputAcquisition r = (TaskRepeatParamOutputAcquisition)listTaskRepeatParam.get(i);
                ArrayList<TaskRepeatValueDataProd> values = r.getListValue();
                InitialAcquisitionOutput out = r.getOutput();
                int nbV = values.size();
                ArrayList<QData> l = new ArrayList();
                for (int j=0; j<nbV; j++){
                    l.add(values.get(j).getData());
                    list.set(j, null);
                }
                int id=-1;
                for (int j=0; j<nbO; j++){
                    if (listOutput.get(j).getDbKey() == out.getDbKey()){
                        id = j;
                        break;
                    }
                }
                if(id != -1){
                    list.set(id, l);
                }
            }
        }
        return list;
    }


    /* retourne en v[0] la liste du materiel d'une action */
    private static CopexReturn getMaterialProdFromDB(DataBaseCommunication dbC, Locale locale, long dbKey, ArrayList<PhysicalQuantity> listPhysicalQuantity,ArrayList v){
        ArrayList<Material> listMaterial = new ArrayList();
        String query = "SELECT L.ID_MATERIAL, M.MATERIAL_NAME, M.DESCRIPTION, M.URL_DESCRIPTION " +
                "FROM LINK_ACTION_MATERIAL_PROD L, MATERIAL M " +
                "WHERE L.ID_ACTION = "+dbKey+" AND L.ID_MATERIAL = M.ID_MATERIAL ; ";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("L.ID_MATERIAL");
        listFields.add("M.MATERIAL_NAME");
        listFields.add("M.DESCRIPTION");
        listFields.add("M.URL_DESCRIPTION");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("L.ID_MATERIAL");
            if (s == null)
                continue;
            long dbKeyMat = Long.parseLong(s);
            String matName = rs.getColumnData("M.MATERIAL_NAME");
            String desc = rs.getColumnData("M.DESCRIPTION");
            String urlDescription = rs.getColumnData("M.URL_DESCRIPTION");
            Material material = new Material(dbKeyMat,CopexUtilities.getLocalText(matName, locale), CopexUtilities.getLocalText(desc, locale), urlDescription, new MaterialSourceAction(dbKey));
            // type
            ArrayList<TypeMaterial> listT = new ArrayList();
            String query2 = "SELECT T.ID_TYPE, T.TYPE_NAME FROM MATERIAL_TYPE T, LINK_TYPE_MATERIAL L " +
                    "WHERE L.ID_MATERIAL = "+dbKeyMat+" AND L.ID_TYPE = T.ID_TYPE ;";

            ArrayList v4 = new ArrayList();
            ArrayList<String> listFields2 = new ArrayList();
            listFields2.add("T.ID_TYPE");
            listFields2.add("T.TYPE_NAME");

            cr = dbC.sendQuery(query2, listFields2, v4);
            if (cr.isError())
                return cr;
            int nbR2 = v4.size();
            for (int j=0; j<nbR2; j++){
                ResultSetXML rs2 = (ResultSetXML)v4.get(j);
                s = rs2.getColumnData("T.ID_TYPE");
                if (s == null)
                    continue;
                long dbKeyType = Long.parseLong(s);
                String typeName = rs2.getColumnData("T.TYPE_NAME");
                TypeMaterial type = new TypeMaterial(dbKeyType, CopexUtilities.getLocalText( typeName, locale));
                listT.add(type);
            }
            material.setListType(listT) ;
            // parametres
            v4 = new ArrayList();
            cr = ExperimentalProcedureFromDB.getMaterialParametersFromDB(dbC,locale, dbKeyMat, listPhysicalQuantity, v4);
            if (cr.isError())
                return cr;

            ArrayList<Parameter> listP = (ArrayList<Parameter>)v4.get(0);
            if (listP != null)
                material.setListParameters(listP);

            listMaterial.add(material);

        }
        v.add(listMaterial);
        return new CopexReturn();
    }

    /* retourne en v[0] la liste des data  d'une action */
    private static CopexReturn getDataProdFromDB(DataBaseCommunication dbC, Locale locale, long dbKey,ArrayList<PhysicalQuantity> listPhysicalQuantity,  ArrayList v){
        ArrayList<QData> listData = new ArrayList();
        int nbPhysQ = listPhysicalQuantity.size();
        String query = "SELECT Q.ID_QUANTITY, Q.QUANTITY_NAME, Q.TYPE, Q.VALUE, Q.UNCERTAINTY, Q.UNIT  " +
                "FROM LINK_ACTION_DATA_PROD L, QUANTITY Q " +
                "WHERE L.ID_ACTION = "+dbKey+" AND L.ID_DATA = Q.ID_QUANTITY ; ";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("L.ID_QUANTITY");
        listFields.add("Q.QUANTITY_NAME");
        listFields.add("Q.TYPE");
        listFields.add("Q.VALUE");
        listFields.add("Q.UNCERTAINTY");
        listFields.add("Q.UNIT");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("L.ID_QUANTITY");
            if (s ==null)
                continue;
            long dbKeyQ = Long.parseLong(s);
            String qName = rs.getColumnData("Q.QUANTITY_NAME");
            String qType = rs.getColumnData("Q.TYPE");
            s = rs.getColumnData("Q.VALUE");
            double qValue = 0;
            try {
                qValue = Double.parseDouble(s);
            }catch(NumberFormatException e){

            }
            String qUncertainty = rs.getColumnData("Q.UNCERTAINTY");
            s =rs.getColumnData("Q.UNIT");
            long dbKeyUnit = Long.parseLong(s);
            CopexUnit unit = null;
            for (int j=0; j<nbPhysQ; j++){
                PhysicalQuantity phQ = listPhysicalQuantity.get(j);
                CopexUnit u = phQ.getUnit(dbKeyUnit) ;
                if (u != null){
                    unit = u;
                    break;
                }
            }
            QData qData = new QData(dbKeyQ, locale,qName, qType, qValue, qUncertainty, unit);
            listData.add(qData);
        }
        v.add(listData);
        return new CopexReturn();
    }
    /* retourne en v[0] l'action nommee correspondant a l'action, null sinon */
    private static  CopexReturn getNamedActionFromDB(DataBaseCommunication dbC, long dbKey, ArrayList<InitialNamedAction> listInitialNamedAction, ArrayList v){
        InitialNamedAction a = null;
        int nb = listInitialNamedAction.size();
        String query = "SELECT ID_ACTION_NOMMEE FROM ACTION_NOMMEE WHERE ID_ACTION = "+dbKey+" ;  ";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_ACTION_NOMMEE");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_ACTION_NOMMEE");
            if (s == null)
                continue;
            long dbKeyN = Long.parseLong(s);
            for (int j=0; j<nb; j++) {
                if (listInitialNamedAction.get(j).getDbKey() == dbKeyN)
                    a = listInitialNamedAction.get(j);
            }
        }
        v.add(a) ;
        return new CopexReturn();
    }
    
    /* ajout d'une tache, retourne en v2[0] le nouvel id */
    public static  CopexReturn addTaskBrotherInDB(DataBaseCommunication dbC,Locale locale,  CopexTask task, long idProc, CopexTask brotherTask, ArrayList v){
        String name = task.getName(locale) != null ? task.getName(locale) :"";
        name =  AccesDB.replace("\'",name,"''") ;
        String description = task.getDescription(locale) != null ? task.getDescription(locale) :"";
        description =  AccesDB.replace("\'",description,"''") ;
        String comments = task.getComments(locale) != null ? task.getComments(locale) :"";
        comments =  AccesDB.replace("\'",comments,"''") ;
        String taskImage = task.getTaskImage() != null ? task.getTaskImage() :"";
        taskImage =  AccesDB.replace("\'",taskImage,"''") ;
        TaskRepeat taskRepeat = task.getTaskRepeat();
        String draw = null;
        if(task.getDraw() != null){
            draw = CopexUtilities.xmlToString(task.getDraw());
        }
	
        String query = "INSERT INTO COPEX_TASK (ID_TASK, TASK_NAME, DESCRIPTION, COMMENTS, TASK_IMAGE, DRAW_ELO) " +
                "VALUES (NULL, '"+name+"', '"+description+"', " +
                "'"+comments+"', '"+taskImage+"', '"+draw+"');";
        String queryID = "SELECT max(last_insert_id(`ID_TASK`))   FROM COPEX_TASK ;";  
        String queryAdd = "INSERT INTO COPEX_TASK (ID_TASK, TASK_NAME, DESCRIPTION, COMMENTS) " +
                "VALUES ("+task.getDbKey()+", '"+name+"', '"+description+"', " +
                "'"+comments+"');";
        long dbKey = -1;
        ArrayList v2;
        String[] querys;
        CopexReturn cr;
       if (task.getDbKey() == -1){
            v2 = new ArrayList();
            cr = dbC.getNewIdInsertInDB(query, queryID, v2);
            if (cr.isError())
                return cr;
            dbKey = (Long)v2.get(0);
        }else{ // enregistrement avec le dbkey
            v2 = new ArrayList();
            querys = new String[1];
            querys[0] = queryAdd ; 
            cr = dbC.executeQuery(querys, v2);
            if (cr.isError())
                return cr;
            dbKey = task.getDbKey();
        }
        // enregistre les droits
            char editR = task.getEditRight();
            char deleteR = task.getDeleteRight();
            char copyR = task.getCopyRight();
            char moveR = task.getMoveRight();
            char parentR = task.getParentRight();
            char drawR = task.getDrawRight();
            char repeatR = task.getRepeatRight();
            String queryRight = "INSERT INTO TASK_RIGHT (ID_TASK, EDIT_RIGHT, DELETE_RIGHT, COPY_RIGHT, MOVE_RIGHT, PARENT_RIGHT, DRAW_RIGHT, REPEAT_RIGHT)" +
                    "VALUES ("+dbKey+", '"+editR+"', '"+deleteR+"', '"+copyR+"', '"+moveR+"', '"+parentR+"', '"+drawR+"', '"+repeatR+"') ; ";
           
        // enregistre le lien proc /tache
        v2 = new ArrayList();
        int n = 4;
        long oldDbKeyBrother = brotherTask.getDbKeyBrother();
        if (oldDbKeyBrother != -1){
            n = n+2;
        }
        boolean isActionSetting = task instanceof CopexActionChoice || task instanceof CopexActionAcquisition || task instanceof CopexActionManipulation || task instanceof CopexActionTreatment ;
        boolean isNamedAction = task instanceof CopexActionNamed || isActionSetting ;
        int nbparam = isActionSetting ? ((CopexActionParam)task).getNbParam() :0;
        if (isNamedAction){
            n++ ;
            if (isActionSetting){
                n += nbparam;
            }
        }
        int q = 0;
        querys = new String[n];
            int question = 0;
            querys[q] = queryRight;
            q++;    
            if (task.isQuestionRoot())
                question = 1;
            String queryLinkT = "INSERT INTO LINK_PROC_TASK (ID_PROC, ID_TASK, QUESTION) " +
                "VALUES ("+idProc+", "+dbKey+", "+question+") ;";
        
            querys[q] = queryLinkT;
            q++;    
                
            String queryAction = "INSERT INTO ACTION (ID_ACTION) VALUES ("+dbKey+") ;";
            String queryStep = "INSERT INTO STEP (ID_STEP) VALUES ("+dbKey+") ;";
            String queryQuestion = "INSERT INTO QUESTION (ID_QUESTION) VALUES ("+dbKey+") ;";
            
            
            if (task instanceof CopexAction)
                querys[q] = queryAction;
            else if (task instanceof Step)
                querys[q] = queryStep;
            else if (task instanceof Question)
                querys[q] = queryQuestion;
            q++;
            if (isNamedAction){
                String queryN = "INSERT INTO ACTION_NOMMEE (ID_ACTION, ID_ACTION_NOMMEE) VALUES ("+dbKey+", "+((CopexActionNamed)task).getNamedAction().getDbKey()+");";
                querys[q] = queryN ;
                q++;
                if(isActionSetting){
                    Object[] tabParam = ((CopexActionParam)task).getTabParam() ;
                    ArrayList v3 = new ArrayList();
                    cr = createActionParamInDB(dbC, locale,dbKey, tabParam, v3);
                    if (cr.isError()){
                        return cr;
                    }
                    tabParam = (Object[])v3.get(0);
                    ((CopexActionParam)task).setTabParam(tabParam);
                    if(task instanceof CopexActionManipulation){
                        v3 = new ArrayList();
                        createActionMaterialProdInDB(dbC, locale, dbKey,((CopexActionManipulation)task).getListMaterialProd(), idProc, v3 );
                        if (cr.isError())
                            return cr;
                        ArrayList<Object> listMaterialProd  = (ArrayList<Object>)v3.get(0);
                        ((CopexActionManipulation)task).setListMaterialProd(listMaterialProd);
                    }else if (task instanceof CopexActionAcquisition){
                        v3 = new ArrayList();
                        createActionDataProdInDB(dbC, locale,dbKey,((CopexActionAcquisition)task).getListDataProd(), v3 );
                        if (cr.isError())
                            return cr;
                        ArrayList<Object> listDataProd  = (ArrayList<Object>)v3.get(0);
                        ((CopexActionAcquisition)task).setListDataProd(listDataProd);
                    }else if (task instanceof CopexActionTreatment){
                        v3 = new ArrayList();
                        createActionDataProdInDB(dbC, locale,dbKey,((CopexActionTreatment)task).getListDataProd(), v3 );
                        if (cr.isError())
                            return cr;
                        ArrayList<Object> listDataProd  = (ArrayList<Object>)v3.get(0);
                        ((CopexActionTreatment)task).setListDataProd(listDataProd);
                    }
                }
            }
            // supprime le lien existant s'il y avait 
            // on cree les nouveaux 
            long dbKeyBrother = brotherTask.getDbKey();
            if (oldDbKeyBrother != -1){
                String queryDel = "DELETE FROM LINK_BROTHER WHERE " +
                        "ID_TASK = "+dbKeyBrother+" AND ID_TASK_BROTHER = "+oldDbKeyBrother+ " ;";
                querys[q] = queryDel;
                q++;
                String queryAddLink = "INSERT INTO LINK_BROTHER (ID_TASK, ID_TASK_BROTHER)" +
                    " VALUES ("+dbKey+", "+oldDbKeyBrother+") ;";
                querys[q] = queryAddLink;
                q++;
            }
            String queryLink = "INSERT INTO LINK_BROTHER (ID_TASK, ID_TASK_BROTHER)" +
                    " VALUES ("+dbKeyBrother+", "+dbKey+") ;";
            querys[q] = queryLink;
            q++;
            
            // tout s'est bien passe, on commit
            cr = dbC.executeQuery(querys, v2);
            if (cr.isError())
                return cr;
            // enregistrement repetition
            if(taskRepeat != null){
                ArrayList v3 = new ArrayList();
                cr = insertTaskRepeatInDB(dbC, dbKey, taskRepeat, v3);
                if(cr.isError())
                    return cr;
                taskRepeat = (TaskRepeat)v3.get(0);
            }
            v.add(dbKey);
            v.add(task);
            if(taskRepeat != null)
                v.add(taskRepeat);
            return new CopexReturn();
        
    }
    
    
    
     /* ajout d'une tache, retourne en v2[0] le nouvel id */
    public static  CopexReturn addTaskParentInDB(DataBaseCommunication dbC,Locale locale, CopexTask task, long idProc, CopexTask parentTask, ArrayList v){
        String name = task.getName(locale) != null ? task.getName(locale) :"";
	    name =  AccesDB.replace("\'",name,"''") ;
        String description = task.getDescription(locale) != null ? task.getDescription(locale) :"";
        description =  AccesDB.replace("\'",description,"''") ;
        String comments = task.getComments(locale) != null ? task.getComments(locale) :"";
        comments =  AccesDB.replace("\'",comments,"''") ;
        String taskImage = task.getTaskImage() != null ? task.getTaskImage() :"";
        taskImage =  AccesDB.replace("\'",taskImage,"''") ;
        String draw = null;
        if(task.getDraw() != null){
            draw = CopexUtilities.xmlToString(task.getDraw());
        }
        TaskRepeat taskRepeat = task.getTaskRepeat();
        String query = "INSERT INTO COPEX_TASK (ID_TASK, TASK_NAME, DESCRIPTION, COMMENTS, TASK_IMAGE, DRAW_ELO) " +
                "VALUES (NULL, '"+name+"', '"+description+"', " +
                "'"+comments+"' , '"+taskImage+"', '"+draw+"');";
        long dbKey = -1;
        String queryID = "SELECT max(last_insert_id(`ID_TASK`))   FROM COPEX_TASK ;";  
        String queryAdd = "INSERT INTO COPEX_TASK (ID_TASK, TASK_NAME, DESCRIPTION, COMMENTS) " +
                "VALUES ("+task.getDbKey()+", '"+name+"', '"+description+"', " +
                "'"+comments+"');";
        ArrayList v2;
        String[] querys ;
        CopexReturn cr;
        if (task.getDbKey() == -1){
            v2 = new ArrayList();
            cr = dbC.getNewIdInsertInDB(query, queryID, v2);
            if (cr.isError())
                return cr;
            dbKey = (Long)v2.get(0);
        }else{ // enregistrement avec le bon dbKey
            v2 = new ArrayList();
            querys = new String[1];
            querys[0] = queryAdd ; 
            cr = dbC.executeQuery(querys, v2);
            if (cr.isError())
                return cr;
            dbKey = task.getDbKey();
        }
            
        // enregistre les droits
            char editR = task.getEditRight();
            char deleteR = task.getDeleteRight();
            char copyR = task.getCopyRight();
            char moveR = task.getMoveRight();
            char parentR = task.getParentRight();
            char drawR = task.getDrawRight();
            char repeatR = task.getRepeatRight();
            String queryRight = "INSERT INTO TASK_RIGHT (ID_TASK, EDIT_RIGHT, DELETE_RIGHT, COPY_RIGHT, MOVE_RIGHT, PARENT_RIGHT, DRAW_RIGHT, REPEAT_RIGHT)" +
                    "VALUES ("+dbKey+", '"+editR+"', '"+deleteR+"', '"+copyR+"', '"+moveR+"', '"+parentR+"', '"+drawR+"', '"+repeatR+"') ; ";
           
            
            // enregistre le lien proc /tache
        v2 = new ArrayList();
        int n = 4;
        boolean isActionSetting = task instanceof CopexActionChoice || task instanceof CopexActionAcquisition || task instanceof CopexActionManipulation || task instanceof CopexActionTreatment ;
        boolean isNamedAction = task instanceof CopexActionNamed || isActionSetting ;
        int nbparam = isActionSetting ? ((CopexActionParam)task).getNbParam() :0;
        if (isNamedAction){
            n++ ;
            if (isActionSetting){
                n += nbparam;
            }
        }
        querys = new String[n];
        querys[0] = queryRight;    
        int question = 0;


            if (task.isQuestionRoot())
                question = 1;
            String queryLinkT = "INSERT INTO LINK_PROC_TASK (ID_PROC, ID_TASK, QUESTION) " +
                "VALUES ("+idProc+", "+dbKey+", "+question+") ;";
        
            querys[1] = queryLinkT;
            String queryAction = "INSERT INTO ACTION (ID_ACTION) VALUES ("+dbKey+") ;";
            String queryStep = "INSERT INTO STEP (ID_STEP) VALUES ("+dbKey+") ;";
            String queryQuestion = "INSERT INTO QUESTION (ID_QUESTION) VALUES ("+dbKey+") ;";
            
            if (task instanceof CopexAction)
                querys[2] = queryAction;
            else if (task instanceof Step)
                querys[2] = queryStep;
            else if (task instanceof Question)
                querys[2] = queryQuestion;
            int q=3;
            if (isNamedAction){
                String queryN = "INSERT INTO ACTION_NOMMEE (ID_ACTION, ID_ACTION_NOMMEE) VALUES ("+dbKey+", "+((CopexActionNamed)task).getNamedAction().getDbKey()+");";
                querys[q] = queryN ;
                q++;
                if(isActionSetting){
                    Object[] tabParam = ((CopexActionParam)task).getTabParam() ;
                    ArrayList v3 = new ArrayList();
                    cr = createActionParamInDB(dbC, locale,dbKey, tabParam, v3);
                    if (cr.isError()){
                        return cr;
                    }
                    tabParam = (Object[])v3.get(0);
                    ((CopexActionParam)task).setTabParam(tabParam);
                    if(task instanceof CopexActionManipulation){
                        v3 = new ArrayList();
                        createActionMaterialProdInDB(dbC, locale, dbKey,((CopexActionManipulation)task).getListMaterialProd(), idProc, v3 );
                        if (cr.isError())
                            return cr;
                        ArrayList<Object> listMaterialProd  = (ArrayList<Object>)v3.get(0);
                        ((CopexActionManipulation)task).setListMaterialProd(listMaterialProd);
                    }else if (task instanceof CopexActionAcquisition){
                        v3 = new ArrayList();
                        createActionDataProdInDB(dbC, locale,dbKey,((CopexActionAcquisition)task).getListDataProd(), v3 );
                        if (cr.isError())
                            return cr;
                        ArrayList<Object> listDataProd  = (ArrayList<Object>)v3.get(0);
                        ((CopexActionAcquisition)task).setListDataProd(listDataProd);
                    }else if (task instanceof CopexActionTreatment){
                        v3 = new ArrayList();
                        createActionDataProdInDB(dbC,locale, dbKey,((CopexActionTreatment)task).getListDataProd(), v3 );
                        if (cr.isError())
                            return cr;
                        ArrayList<Object> listDataProd  = (ArrayList<Object>)v3.get(0);
                        ((CopexActionTreatment)task).setListDataProd(listDataProd);
                    }
                }
            }
            
            // creation du nouveau lien 
            long dbKeyParent = parentTask.getDbKey();
            deleteLinkChildInDB(dbC, dbKeyParent);
            String queryLink = "INSERT INTO LINK_CHILD (ID_TASK, ID_TASK_CHILD)" +
                    " VALUES ("+dbKeyParent+", "+dbKey+") ;";
            querys[q] = queryLink;
            
            cr = dbC.executeQuery(querys, v2);
            if (cr.isError())
                return cr;
            // enregistrement repetition
            if(taskRepeat != null){
                ArrayList v3 = new ArrayList();
                cr = insertTaskRepeatInDB(dbC, dbKey, taskRepeat, v3);
                if(cr.isError())
                    return cr;
                taskRepeat = (TaskRepeat)v3.get(0);
            }
            v.add(dbKey);
            if(taskRepeat != null )
                v.add(taskRepeat);
            return new CopexReturn();
       
	
    }
    
    /* creation d'un materiel lie a une action, retourne en v[0] la lsite du materiel avec les nouveaux dbKey */
    private static CopexReturn createActionMaterialProdInDB(DataBaseCommunication dbC, Locale locale,long dbKeyAction, ArrayList<Object> listMaterial, long idProc,  ArrayList v){
        int nbMat = listMaterial.size();
        for (int i=0; i<nbMat; i++){
            if(listMaterial.get(i) instanceof Material){
                Material m = (Material)listMaterial.get(i);
                ArrayList v2 = new ArrayList();
                CopexReturn cr = createActionMaterialProdInDB(dbC, locale, dbKeyAction, m, idProc, v2);
                if (cr.isError())
                    return cr;
                m =(Material)v2.get(0);
                listMaterial.set(i, m);
            }else if (listMaterial.get(i) instanceof ArrayList){
                int n = ((ArrayList)listMaterial.get(i)).size();
                for (int k=0; k<n; k++){
                    Material m = ((ArrayList<Material>)listMaterial.get(i)).get(k);
                    ArrayList v2 = new ArrayList();
                    CopexReturn cr = createActionMaterialProdInDB(dbC, locale, dbKeyAction, m, idProc, v2);
                    if (cr.isError())
                        return cr;
                    m =(Material)v2.get(0);
                    ((ArrayList<Material>)listMaterial.get(i)).set(k, m);
                }
            }
        }
        v.add(listMaterial);
        return new CopexReturn();
    }
       /* creation d'un materiel lie a une action, retourne en v[0] la lsite du materiel avec les nouveaux dbKey */
       private static CopexReturn createActionMaterialProdInDB(DataBaseCommunication dbC, Locale locale, long dbKeyAction, Material material, long idProc, ArrayList v){
            if (material.getDbKey() == -1){
                ArrayList v2 = new ArrayList();
                CopexReturn cr = ExperimentalProcedureFromDB.createMaterialInDB(dbC, locale, material,idProc,  v2);
                if(cr.isError())
                    return cr;
                material = (Material)v2.get(0);
                // creation du lien avec action
                String querys[] = new String[2];
                String queryAction = "INSERT INTO MATERIAL_PRODUCE (ID_MATERIAL) VALUES ("+material.getDbKey()+");";
                querys[0] = queryAction;
                String queryLink = "INSERT INTO LINK_ACTION_MATERIAL_PROD (ID_ACTION, ID_MATERIAL) VALUES ("+dbKeyAction+", "+material.getDbKey()+") ;";
                querys[1] = queryLink;
                cr = dbC.executeQuery(querys, v2);
                if (cr.isError())
                    return cr;
                
            }else{
                // material existe deja :
                long dbKey = material.getDbKey();
                // creation du lien avec action
                String[] querys = new String[2];
                ArrayList v2 = new ArrayList();
                String queryAction = "INSERT INTO MATERIAL_PRODUCE (ID_MATERIAL) VALUES ("+dbKey+");";
                querys[0] = queryAction;
                String queryLink = "INSERT INTO LINK_ACTION_MATERIAL_PROD (ID_ACTION, ID_MATERIAL) VALUES ("+dbKeyAction+", "+dbKey+") ;";
                querys[1] = queryLink;
                CopexReturn cr = dbC.executeQuery(querys, v2);
                if (cr.isError())
                    return cr;
            }
        v.add(material);
        return new CopexReturn();
    }

    private static CopexReturn createActionDataProdInDB(DataBaseCommunication dbC, Locale locale, long dbKeyAction, ArrayList<Object> listDataProd, ArrayList v){
        int nbData = listDataProd.size();
        for (int i=0; i<nbData; i++){
            if(listDataProd.get(i) instanceof QData){
                QData d = (QData)listDataProd.get(i);
                ArrayList v2 = new ArrayList();
                CopexReturn cr = createActionDataProdInDB(dbC, locale,dbKeyAction, d, v2);
                if (cr.isError())
                    return cr;
                d =(QData)v2.get(0);
                listDataProd.set(i, d);
            }else if (listDataProd.get(i) instanceof ArrayList){
                int n = ((ArrayList)listDataProd.get(i)).size();
                for (int k=0; k<n; k++){
                    QData d = ((ArrayList<QData>)listDataProd.get(i)).get(k);
                    ArrayList v2 = new ArrayList();
                    CopexReturn cr = createActionDataProdInDB(dbC, locale, dbKeyAction, d, v2);
                    if (cr.isError())
                        return cr;
                    d =(QData)v2.get(0);
                    ((ArrayList<QData>)listDataProd.get(i)).set(k, d);
                }
            }
        }
        v.add(listDataProd);
        return new CopexReturn();

       }
    /* cree une data prod par une action et retourne en v[0] cette data avec le nouveau dbKey */
    private static CopexReturn createActionDataProdInDB(DataBaseCommunication dbC,Locale locale, long dbKeyAction, QData data, ArrayList v){
        String qName = data.getName(locale);
        qName =  AccesDB.replace("\'",qName,"''") ;
        String qType = data.getType(locale) ;
        qType =  AccesDB.replace("\'",qType,"''") ;
        double qValue = data.getValue() ;
        String qUncertainty = data.getUncertainty(locale) ;
        qUncertainty =  AccesDB.replace("\'",qUncertainty,"''") ;
        long dbKeyUnit = data.getUnit().getDbKey() ;
        String query = "INSERT INTO QUANTITY (ID_QUANTITY, QUANTITY_NAME, TYPE, VALUE, UNCERTAINTY, UNIT) " +
                    " VALUES (NULL,'"+qName+"' , '"+qType+"',"+qValue+" , '"+qUncertainty+"',"+dbKeyUnit+" ); ";
        String queryID = "SELECT max(last_insert_id(`ID_QUANTITY`))   FROM QUANTITY ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        data.setDbKey(dbKey);
        // creation des liens
        String[] querys = new String[3];
        String queryD = "INSERT INTO Q_DATA (ID_DATA) VALUES ("+dbKey+") ;";
        querys[0] = queryD;
        String queryProd = "INSERT INTO DATA_PRODUCE (ID_DATA) VALUES ("+dbKey+") ;";
        querys[1] = queryProd ;
        String queryLink = "INSERT INTO LINK_ACTION_DATA_PROD (ID_ACTION, ID_DATA) VALUES ("+dbKeyAction+", "+dbKey+"); ";
        querys[2] = queryLink ;
        ArrayList v3 =new ArrayList();
        cr = dbC.executeQuery(querys, v3);
        if (cr.isError()){
            return cr;
        }
        v.add(data);
        return new CopexReturn();
    }


    /* modification d'une tache , en v[0] les nouveaux param eventuellement */
    public static CopexReturn updateTaskInDB(DataBaseCommunication dbC,Locale locale, CopexTask newTask, long idProc, CopexTask oldTask, ArrayList v){
        long dbKeyOldTask = oldTask.getDbKey() ;
        int nbQ = 1;
        String description = newTask.getDescription(locale) != null ? newTask.getDescription(locale) :"";
        description =  AccesDB.replace("\'",description,"''") ;
        String comments = newTask.getComments(locale) != null ? newTask.getComments(locale) :"";
        comments =  AccesDB.replace("\'",comments,"''") ;
        String hypothesis = "";
        String principle = "";
        String query = "UPDATE COPEX_TASK SET DESCRIPTION = '"+description+"' , COMMENTS = '"+comments+"'" +
                " WHERE ID_TASK = "+dbKeyOldTask+" ;";
        String queryA = "";
        if (newTask instanceof CopexAction){
           InitialNamedAction oldA = null;
           InitialNamedAction newA = null;
           boolean isOldTaskActionNamed = oldTask instanceof  CopexActionNamed || oldTask instanceof CopexActionAcquisition || oldTask instanceof CopexActionChoice || oldTask instanceof CopexActionManipulation || oldTask instanceof CopexActionTreatment ; 
           boolean isNewTaskActionNamed = newTask instanceof  CopexActionNamed || newTask instanceof CopexActionAcquisition || newTask instanceof CopexActionChoice || newTask instanceof CopexActionManipulation || newTask instanceof CopexActionTreatment ; 
           if (isOldTaskActionNamed){
               oldA = ((CopexActionNamed)oldTask).getNamedAction() ;
           }
           if (isNewTaskActionNamed){
               newA = ((CopexActionNamed)newTask).getNamedAction() ;
           }
           if (oldA == null && newA != null){
               // insertion action nommee
               queryA = "INSERT INTO ACTION_NOMMEE (ID_ACTION, ID_ACTION_NOMMEE) VALUES ("+dbKeyOldTask+", "+newA.getDbKey()+") ;";
               nbQ++;
               if (newA.isSetting()){
                   ArrayList v2 = new ArrayList();
                   Object[] tabParam = ((CopexActionParam)newTask).getTabParam() ;
                   CopexReturn cr = createActionParamInDB(dbC, locale, newA.getDbKey(), tabParam, v2) ;
                   if (cr.isError())
                       return cr;
                   tabParam = (Object[])v2.get(0);
                   ((CopexActionParam)newTask).setTabParam(tabParam);
               }
           }else if (oldA != null && newA == null){
               // suppression action nommee
               queryA = "DELETE FROM ACTION_NOMMEE WHERE ID_ACTION = "+dbKeyOldTask+" ;";
               nbQ++;
               if (oldA.isSetting()){
                   CopexReturn cr = deleteActionParamFromDB(dbC, dbKeyOldTask) ;
                   if (cr.isError())
                       return cr;
               }
           }else if (oldA != null && newA != null ){
               // maj action nommee
               queryA = "UPDATE ACTION_NOMMEE SET ID_ACTION_NOMMEE = "+newA.getDbKey() +" WHERE ID_ACTION = "+dbKeyOldTask+" ;";
               nbQ++;
               // maj des parametres
                if (oldA.isSetting() && !newA.isSetting()){
                   CopexReturn cr = deleteActionParamFromDB(dbC, dbKeyOldTask) ;
                   if (cr.isError())
                       return cr;
               }else if (!oldA.isSetting() && newA.isSetting()) {
                    Object[] newTabParam = ((CopexActionParam)newTask).getTabParam() ;
                   ArrayList v2 = new ArrayList();
                   CopexReturn cr = createActionParamInDB(dbC, locale,dbKeyOldTask, newTabParam, v2) ;
                   if (cr.isError())
                       return cr;
                   newTabParam = (Object[])v2.get(0);
                   ((CopexActionParam)newTask).setTabParam(newTabParam);
               }else if (oldA.isSetting() && newA.isSetting()){
                   // on supprime les parametres et on recree tout
                   CopexReturn cr = deleteActionParamFromDB(dbC, dbKeyOldTask) ;
                   if (cr.isError())
                       return cr;
                    ArrayList v2 = new ArrayList();
                   Object[] newTabParam = ((CopexActionParam)newTask).getTabParam() ;
                   cr = createActionParamInDB(dbC, locale,dbKeyOldTask, newTabParam, v2) ;
                   if (cr.isError())
                       return cr;
                   newTabParam = (Object[])v2.get(0);
                   ((CopexActionParam)newTask).setTabParam(newTabParam);
                    if(newTask instanceof CopexActionManipulation){
                        ArrayList v3 = new ArrayList();
                        createActionMaterialProdInDB(dbC, locale, dbKeyOldTask,((CopexActionManipulation)newTask).getListMaterialProd(), idProc, v3 );
                        if (cr.isError())
                            return cr;
                        ArrayList<Object> listMaterialProd  = (ArrayList<Object>)v3.get(0);
                        ((CopexActionManipulation)newTask).setListMaterialProd(listMaterialProd);
                    }else if (newTask instanceof CopexActionAcquisition){
                        ArrayList v3 = new ArrayList();
                        createActionDataProdInDB(dbC, locale, dbKeyOldTask,((CopexActionAcquisition)newTask).getListDataProd(), v3 );
                        if (cr.isError())
                            return cr;
                        ArrayList<Object> listDataProd  = (ArrayList<Object>)v3.get(0);
                        ((CopexActionAcquisition)newTask).setListDataProd(listDataProd);
                    }else if (newTask instanceof CopexActionTreatment){
                        ArrayList v3 = new ArrayList();
                        createActionDataProdInDB(dbC, locale, dbKeyOldTask,((CopexActionTreatment)newTask).getListDataProd(), v3 );
                        if (cr.isError())
                            return cr;
                        ArrayList<Object> listDataProd  = (ArrayList<Object>)v3.get(0);
                        ((CopexActionTreatment)newTask).setListDataProd(listDataProd);
                    }
               }

           }
       }
        ArrayList v2 = new ArrayList();
        String[] querys = new String [nbQ] ;
        if (newTask instanceof Question){
            querys[0] = query ;
        }else{
            querys[0] = query ;
            if (nbQ > 1)
                querys[1] = queryA; 
        }
        CopexReturn cr = dbC.executeQuery(querys, v2);
        v.add(newTask);
        return cr;
    }
    
    
    
    
    /* suppression des taches
     * on supprimer les liens : LINK_PROC_TASK, LINK_BROTHER et LINK_CHILD
     * puis on supprime dans les tables ACTION OU STEP ou QUESTION
     * puis on supprime dans la table TASK
     */
    public static CopexReturn deleteTasksFromDB(DataBaseCommunication dbC, boolean setAutoCommit, long dbKeyProc, List<CopexTask> listTask){
        String queryDelProc = "";
        String queryDelBrother = "";
        String queryDelBrother1 = "";
        String queryDelChild = "";
        String queryDelChild1 = "";
        String queryDelTask = "";
        String queryDelRight = "";
        String queryDel = "";
        int nbT = listTask.size();
        for (int t=0; t<nbT; t++){
            CopexTask task = listTask.get(t);
            long dbKeyTask = task.getDbKey();
            long dbKeyBrother = task.getDbKeyBrother();
            long dbKeyChild = task.getDbKeyChild();
            int n = 6;
            ArrayList v = new ArrayList();
            if (dbKeyBrother != -1)
                n++;
            if (dbKeyChild != -1)
                n++;
            if( task instanceof CopexActionNamed ){
                n++;
            }
            String[] querys = new String[n];
            int q=0;
            
            // suppression du lien avec le protocole 
            queryDelProc = "DELETE FROM LINK_PROC_TASK WHERE " +
                        "ID_TASK = "+dbKeyTask+" AND ID_PROC = "+dbKeyProc+";";
               
            querys[q] = queryDelProc ;
            q++;
            // suppression du lien frere
            if (dbKeyBrother != -1){
               queryDelBrother1 = "DELETE FROM LINK_BROTHER WHERE " +
                            "ID_TASK = "+dbKeyTask+" AND ID_TASK_BROTHER = "+dbKeyBrother+" ;";
               querys[q] = queryDelBrother1 ;
               q++;
            }
            // on supprime egalement les liens ou il etait frere
            queryDelBrother = "DELETE FROM LINK_BROTHER WHERE " +
                            "ID_TASK_BROTHER = "+dbKeyTask+" ;";
            querys[q] = queryDelBrother ;
            q++;
            // suppression du lien enfant 
            if (dbKeyChild != -1){
                 queryDelChild1 = "DELETE FROM LINK_CHILD WHERE " +
                            "ID_TASK = "+dbKeyTask+" AND ID_TASK_CHILD = "+dbKeyChild+" ;";
                 querys[q] = queryDelChild1 ;
                 q++;
             }
            // on supprime egalement les liens ou il etait enfant
            queryDelChild = "DELETE FROM LINK_CHILD WHERE " +
                            "ID_TASK_CHILD = "+dbKeyTask+" ;";
            querys[q] = queryDelChild ; 
            q++;
            // on supprime les droits
            queryDelRight = "DELETE FROM TASK_RIGHT WHERE ID_TASK = "+dbKeyTask+" ;";
            querys[q] = queryDelRight ; 
            q++;
            // suppression des repetitions
            if (task.getTaskRepeat() != null){
                CopexReturn cr = deleteTaskRepeatFromDB(dbC, dbKeyTask, task.getTaskRepeat());
                if(cr.isError())
                    return cr;
            }
            // selon le type de tache
            if (task instanceof CopexAction){
                queryDel = "DELETE FROM ACTION WHERE ID_ACTION = "+dbKeyTask+" ;";
                querys[q] = queryDel ;
                q++;
                if(task instanceof CopexActionNamed){
                    // si action nommee
                    String queryDelActionNommee = " DELETE FROM ACTION_NOMMEE WHERE ID_ACTION = "+dbKeyTask+" ;";
                    querys[q] = queryDelActionNommee ;
                    q++;
                    if(task instanceof CopexActionChoice || task instanceof CopexActionAcquisition || task instanceof CopexActionManipulation || task instanceof CopexActionTreatment ){
                        CopexReturn cr = deleteActionParamFromDB(dbC, dbKeyTask) ;
                        if (cr.isError())
                            return cr;
                    }
                }

             }else if (task instanceof Step){
                queryDel = "DELETE FROM STEP WHERE ID_STEP = "+dbKeyTask+" ;";
                querys[q] = queryDel ;
                q++;
            }else if (task instanceof Question){
                queryDel = "DELETE FROM QUESTION WHERE ID_QUESTION = "+dbKeyTask+" ;";
                querys[q] = queryDel ;
                q++;
            }
           // suppression de la tache 
           queryDelTask = "DELETE FROM COPEX_TASK " +
                        "WHERE ID_TASK = "+dbKeyTask+" ;";
               
           querys[q] = queryDelTask ;
           q++;
           CopexReturn cr = dbC.executeQuery(querys, v);
           if (cr.isError())
               return cr;
        }
        return new CopexReturn();
    }
    
    
    
    /* modification des liens des taches */
    public static CopexReturn updateLinksInDB(DataBaseCommunication dbC, long dbKeyProc, ArrayList<CopexTask> listTaskUpdateBrother, ArrayList<CopexTask> listTaskUpdateChild){
        // mise a jour des liens freres
        int nbB = listTaskUpdateBrother.size();
        int nbC = listTaskUpdateChild.size();
        ArrayList v = new ArrayList();
        String[] querys  = new String[nbB + nbC];
        for (int i=0;i<nbB;i++){
            CopexTask t = listTaskUpdateBrother.get(i);
            long dbKey = t.getDbKey();
            long dbKeyBrother = t.getDbKeyBrother();
            String queryDelB = "INSERT INTO LINK_BROTHER (ID_TASK, ID_TASK_BROTHER) " +
                        "VALUES ("+dbKey+", "+dbKeyBrother+") ;";
            
            querys[i] = queryDelB ;
        }
        // mise a jour des liens enfants
        for (int i=0;i<nbC;i++){
            CopexTask t = listTaskUpdateChild.get(i);
            long dbKey = t.getDbKey();
            long dbKeyChild = t.getDbKeyChild();
            String queryDelC = "INSERT INTO LINK_CHILD (ID_TASK, ID_TASK_CHILD) " +
                        "VALUES ("+dbKey+", "+dbKeyChild+") ;";
           querys[nbB + i] = queryDelC ;    
        }
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }
    
    
    
   
    /* creation de la question principale d'un protocole - en v[0] son id */
    static public CopexReturn createQuestionInDB(DataBaseCommunication dbC, Locale locale, Question question, ArrayList v){
        String name = question.getName(locale) != null ? question.getName(locale) :"";
        name =  AccesDB.replace("\'",name,"''") ;
        String description = question.getDescription(locale) != null ? question.getDescription(locale) :"";
        description =  AccesDB.replace("\'",description,"''") ;
        String comments = question.getComments(locale) != null ? question.getComments(locale) :"";
        comments =  AccesDB.replace("\'",comments,"''") ;
        
        String query = "INSERT INTO COPEX_TASK (ID_TASK, TASK_NAME, DESCRIPTION, COMMENTS) " +
                "VALUES (NULL, '"+name+"', '"+description+"', " +
                "'"+comments+"');";
        
        String queryID = "SELECT max(last_insert_id(`ID_TASK`))   FROM COPEX_TASK ;";  
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
         
         // enregistre les droits
            char editR = question.getEditRight();
            char deleteR = question.getDeleteRight();
            char copyR = question.getCopyRight();
            char moveR = question.getMoveRight();
            char parentR = question.getParentRight();
            char drawR = question.getDrawRight();
            char repeatR = question.getRepeatRight();
            String queryRight = "INSERT INTO TASK_RIGHT (ID_TASK, EDIT_RIGHT, DELETE_RIGHT, COPY_RIGHT, MOVE_RIGHT, PARENT_RIGHT, DRAW_RIGHT, REPEAT_RIGHT)" +
                    "VALUES ("+dbKey+", '"+editR+"', '"+deleteR+"', '"+copyR+"', '"+moveR+"', '"+parentR+"', '"+drawR+"', '"+repeatR+"') ; ";
           
            
        // question
        String queryQuestion = "INSERT INTO QUESTION (ID_QUESTION) VALUES ("+dbKey+") ;";
        v2 = new ArrayList();
        String[]querys = new String[2];
        querys[0] = queryRight ;
        querys[1] = queryQuestion ;
        cr = dbC.executeQuery(querys, v2);
        if (cr.isError())
            return cr;
        v.add(dbKey);
        return cr;
    }
    
    
    
    /* creation de taches dans la base */
    static public CopexReturn createTasksInDB(DataBaseCommunication dbC, Locale locale, long idProc, List<CopexTask> listTask, Question questionProc, boolean createQuestion, ArrayList v){
       int nbT = listTask.size();
       for (int t=0; t<nbT; t++){
           CopexTask task = listTask.get(t);
           if (!createQuestion && task.isQuestionRoot()){
                    
           }else{
            long oldDbKey = task.getDbKey();
            String name = task.getName(locale) != null ? task.getName(locale) :"";
            name =  AccesDB.replace("\'",name,"''") ;
            String description = task.getDescription(locale) != null ? task.getDescription(locale) :"";
            description =  AccesDB.replace("\'",description,"''") ;
            String comments = task.getComments(locale) != null ? task.getComments(locale) :"";
            comments =  AccesDB.replace("\'",comments,"''") ;
            String taskImage = task.getTaskImage() != null ? task.getTaskImage() :"";
            taskImage =  AccesDB.replace("\'",taskImage,"''") ;
            String draw = null;
            if(task.getDraw() != null){
                draw = CopexUtilities.xmlToString(task.getDraw());
            }
            TaskRepeat taskRepeat = task.getTaskRepeat() ;
            String hypothesis = "";
            String principle = "";
            String query = "INSERT INTO COPEX_TASK (ID_TASK, TASK_NAME, DESCRIPTION, COMMENTS, TASK_IMAGE, DRAW_ELO) " +
                        "VALUES (NULL, '"+name+"', '"+description+"', " +
                        "'"+comments+"','"+taskImage+"' , '"+draw+"');";
            String queryID = "SELECT max(last_insert_id(`ID_TASK`))   FROM COPEX_TASK ;";  
            long dbKey = -1;
            ArrayList v2 = new ArrayList();
            CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
            if (cr.isError())
                return cr;
            dbKey = (Long)v2.get(0);
             // enregistre les droits
            char editR = task.getEditRight();
            char deleteR = task.getDeleteRight();
            char copyR = task.getCopyRight();
            char moveR = task.getMoveRight();
            char parentR = task.getParentRight();
            char drawR = task.getDrawRight();
            char repeatR = task.getRepeatRight();
            String queryRight = "INSERT INTO TASK_RIGHT (ID_TASK, EDIT_RIGHT, DELETE_RIGHT, COPY_RIGHT, MOVE_RIGHT, PARENT_RIGHT, DRAW_RIGHT, REPEAT_RIGHT)" +
                    "VALUES ("+dbKey+", '"+editR+"', '"+deleteR+"', '"+copyR+"', '"+moveR+"', '"+parentR+"', '"+drawR+"', '"+repeatR+"') ; ";
            // enregistre le lien proc /tache
            int question = 0;
            if (task.isQuestionRoot())
                question = 1;
            String queryLinkT = "INSERT INTO LINK_PROC_TASK (ID_PROC, ID_TASK, QUESTION) " +
                    "VALUES ("+idProc+", "+dbKey+", "+question+") ;";
            v2 = new ArrayList();
            boolean isActionSetting = task instanceof CopexActionChoice || task instanceof CopexActionAcquisition || task instanceof CopexActionManipulation || task instanceof CopexActionTreatment ;
            boolean isNamedAction = task instanceof CopexActionNamed || isActionSetting ;
            int nbparam = isActionSetting ? ((CopexActionParam)task).getNbParam() :0;
            int n=3;
            if (isNamedAction){
                n++ ;
                if (isActionSetting){
                    n += nbparam;
                }
            }

            String[]querys = new String[n];
            querys[0] = queryRight ;
            querys[1] = queryLinkT ;
                
            String queryAction = "INSERT INTO ACTION (ID_ACTION) VALUES ("+dbKey+") ;";
            String queryStep = "INSERT INTO STEP (ID_STEP) VALUES ("+dbKey+") ;";
            String queryQuestion = "INSERT INTO QUESTION (ID_QUESTION, HYPOTHESIS, GENERAL_PRINCIPLE) VALUES ("+dbKey+", '"+hypothesis+"', '"+principle+"') ;";
            
            if (task instanceof CopexAction)
               querys[2] = queryAction ;
            else if (task instanceof Step)
               querys[2] = queryStep ;
            else if (task instanceof Question)
               querys[2] = queryQuestion ;
            

            int q=3;
            if (isNamedAction){
                String queryN = "INSERT INTO ACTION_NOMMEE (ID_ACTION, ID_ACTION_NOMMEE) VALUES ("+dbKey+", "+((CopexActionNamed)task).getNamedAction().getDbKey()+");";
                querys[q] = queryN ;
                q++;
                if(isActionSetting){
                    Object[] tabParam = ((CopexActionParam)task).getTabParam() ;
                    ArrayList v3 = new ArrayList();
                    cr = createActionParamInDB(dbC, locale,dbKey, tabParam, v3);
                    if (cr.isError()){
                        return cr;
                    }
                    tabParam = (Object[])v3.get(0);
                    ((CopexActionParam)task).setTabParam(tabParam);
                    if(task instanceof CopexActionManipulation){
                        v3 = new ArrayList();
                        createActionMaterialProdInDB(dbC, locale, dbKey,((CopexActionManipulation)task).getListMaterialProd(), idProc, v3 );
                        if (cr.isError())
                            return cr;
                        ArrayList<Object> listMaterialProd  = (ArrayList<Object>)v3.get(0);
                        ((CopexActionManipulation)task).setListMaterialProd(listMaterialProd);
                    }else if (task instanceof CopexActionAcquisition){
                        v3 = new ArrayList();
                        createActionDataProdInDB(dbC, locale, dbKey,((CopexActionAcquisition)task).getListDataProd(), v3 );
                        if (cr.isError())
                            return cr;
                        ArrayList<Object> listDataProd  = (ArrayList<Object>)v3.get(0);
                        ((CopexActionAcquisition)task).setListDataProd(listDataProd);
                    }else if (task instanceof CopexActionTreatment){
                        v3 = new ArrayList();
                        createActionDataProdInDB(dbC, locale, dbKey,((CopexActionTreatment)task).getListDataProd(), v3 );
                        if (cr.isError())
                            return cr;
                        ArrayList<Object> listDataProd  = (ArrayList<Object>)v3.get(0);
                        ((CopexActionTreatment)task).setListDataProd(listDataProd);
                    }
                }
            }

            cr = dbC.executeQuery(querys, v2)     ;
            if (cr.isError())
               return cr;
            // enregistrement repetition
            if(taskRepeat != null){
                ArrayList v3 = new ArrayList();
                cr = insertTaskRepeatInDB(dbC, dbKey, taskRepeat, v3);
                if(cr.isError())
                    return cr;
                taskRepeat = (TaskRepeat)v3.get(0);
                task.setTaskRepeat(taskRepeat);
            }

            // on met a jour les identifiants 
            task.setDbKey(dbKey);
            if (questionProc.getDbKey() == oldDbKey)
               questionProc.setDbKey(dbKey);
            // parcours la liste pour mettre a jour
            for (int k=0; k<nbT; k++){
               CopexTask ct = listTask.get(k);
               if (ct.getDbKeyBrother() == oldDbKey)
                  ct.setDbKeyBrother(dbKey);
               else if (ct.getDbKeyChild() == oldDbKey)
                   ct.setDbKeyChild(dbKey);
                if (questionProc.getDbKeyChild() == oldDbKey)
                   questionProc.setDbKeyChild(dbKey);
                                
            }
                
          }
       } // fin boucle tache
            // on reboucle pour enregistrer les liens
            for (int t=0; t<nbT; t++){
                CopexTask task = listTask.get(t);
                long dbKey = task.getDbKey();
                long dbKeyBrother = task.getDbKeyBrother();
                long dbKeyChild = task.getDbKeyChild();
                ArrayList v2 = new ArrayList();
                int n=0;
                if (dbKeyBrother != -1)
                    n++;
                if (dbKeyChild != -1)
                    n++;
                String[] querys = new String[n];
                int q=0;
                
                if (dbKeyBrother != -1){
                    String queryLink = "INSERT INTO LINK_BROTHER (ID_TASK, ID_TASK_BROTHER)" +
                    " VALUES ("+dbKey+", "+dbKeyBrother+") ;";
                    querys[q] = queryLink;
                    q++;
                }
                if (dbKeyChild != -1){
                    String queryLink = "INSERT INTO LINK_CHILD (ID_TASK, ID_TASK_CHILD)" +
                    " VALUES ("+dbKey+", "+dbKeyChild+") ;";
                    querys[q] = queryLink;
                    q++;
                }
                if (n != 0){
                    CopexReturn cr = dbC.executeQuery(querys, v2);
                    if (cr.isError())
                        return cr;
                }
            }
           
            v.add(listTask);
            return new CopexReturn();
        
    }
    
    
    
    /* creation d'un lien enfant */
    static public CopexReturn createLinkChildInDB(DataBaseCommunication dbC, long dbKey, long dbKeyChild){
        String queryLink = "INSERT INTO LINK_CHILD (ID_TASK, ID_TASK_CHILD)" +
                    " VALUES ("+dbKey+", "+dbKeyChild+") ;";
       ArrayList v = new ArrayList();
       String[] querys = new String[1];
       querys[0] = queryLink ;
       CopexReturn cr = dbC.executeQuery(querys, v);
       return cr;
    }

     /* creation d'un lien frere */
    static public CopexReturn createLinkBrotherInDB(DataBaseCommunication dbC, long dbKey, long dbKeyBrother){
        if (dbKey == dbKeyBrother)
            return new CopexReturn("ERREUR LORS DE L'INSERTION DE 2 ID IDENTIQUES !!!", false);
        String queryLink = "INSERT INTO LINK_BROTHER (ID_TASK, ID_TASK_BROTHER)" +
                    " VALUES ("+dbKey+", "+dbKeyBrother+") ;";
         
	    ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = queryLink ; 
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }
    
    
    
      /* suppression d'un lien frere */
    static public CopexReturn deleteLinkBrotherInDB(DataBaseCommunication dbC, long dbKey, long dbKeyBrother){
       String queryDel = "DELETE FROM LINK_BROTHER " +
                    " WHERE ID_TASK = "+dbKey+" AND ID_TASK_BROTHER =  "+dbKeyBrother+" ;";

       String[] querys = new String[1];
       querys[0] = queryDel ;
       ArrayList v = new ArrayList();
       CopexReturn cr = dbC.executeQuery(querys, v);
       return cr;

    }


    /* suppression d'un lien enfant */
    static public CopexReturn deleteLinkChildInDB(DataBaseCommunication dbC, long dbKey){
       String queryDel = "DELETE FROM LINK_CHILD " +
                    " WHERE ID_TASK = "+dbKey+"  ;";
       ArrayList v = new ArrayList();
       String[] querys = new String[1];
       querys[0] = queryDel ;
       CopexReturn cr = dbC.executeQuery(querys, v);
       return cr;
    }
    
    
    
     /* suppression d'un lien parent */
    static public CopexReturn deleteLinkParentInDB(DataBaseCommunication dbC, long dbKeyChild){
       String queryDel = "DELETE FROM LINK_CHILD " +
                    " WHERE ID_TASK_CHILD = "+dbKeyChild+"  ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = queryDel ; 
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }
    
    
    
    
    /* suppression d'un lien parent */
    static public CopexReturn deleteLinkBrotherInDB(DataBaseCommunication dbC, long dbKey){
        String queryDel = "DELETE FROM LINK_BROTHER " +
                    " WHERE ID_TASK = "+dbKey+"  ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = queryDel ; 
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }
    
   
    /* suppression d'un lien petit frere */
    static public CopexReturn deleteLinkSubBrotherInDB(DataBaseCommunication dbC, long dbKeyBrother){
        String queryDel = "DELETE FROM LINK_BROTHER " +
                    " WHERE ID_TASK_BROTHER = "+dbKeyBrother+"  ;";
        
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = queryDel ; 
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }
    
    /* mis a jour de la variable visible de taches */
    static public CopexReturn updateTaskVisibleInDB(DataBaseCommunication dbC, List<CopexTask> listTask){
        int nb = listTask.size();
        ArrayList v = new ArrayList();
        String[] querys = new String[nb];
        for (int i=0; i<nb; i++){
            CopexTask task = listTask.get(i);
            int vis = 0;
            if (task.isVisible())
                vis = 1;
           String query = "UPDATE COPEX_TASK SET IS_VISIBLE = "+vis+" WHERE ID_TASK = "+task.getDbKey()+" ;";
            querys[i] = query;
        }
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* chargement des parametres d'une tache */
    public static CopexReturn getActionParamFromDB(DataBaseCommunication dbC, Locale locale, long dbKeyAction, InitialNamedAction namedAction, ArrayList<Material> listMaterial, ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList v){
        int nbPhysQ = listPhysicalQuantity.size();
        if (namedAction == null || !namedAction.isSetting()){
            v.add(new ActionParam[0]) ;
            return new CopexReturn();
        }
        int nbParam = namedAction.getVariable().getNbParam() ;
        InitialActionParam[] tabInitialParam = namedAction.getVariable().getTabParam() ;
        ArrayList<ActionParam> listParam = new ArrayList();
        // recuperation des param data
        String queryD = "SELECT P.ID_ACTION_PARAM_DATA, P.ID_DATA, Q.QUANTITY_NAME, Q.TYPE, Q.VALUE, Q.UNCERTAINTY, Q.UNIT, I.ID_INITIAL_PARAM " +
                "FROM ACTION_PARAM_DATA P, QUANTITY Q, LINK_ACTION_PARAM L, LINK_PARAM_INITIAL I " +
                "WHERE L.ID_ACTION = "+dbKeyAction+" AND L.ID_PARAM = P.ID_ACTION_PARAM_DATA AND P.ID_DATA = Q.ID_QUANTITY AND L.ID_PARAM = I.ID_PARAM ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("P.ID_ACTION_PARAM_DATA");
        listFields.add("P.ID_DATA");
        listFields.add("Q.QUANTITY_NAME");
        listFields.add("Q.TYPE");
        listFields.add("Q.VALUE");
        listFields.add("Q.UNCERTAINTY");
        listFields.add("Q.UNIT");
        listFields.add("I.ID_INITIAL_PARAM");

        CopexReturn cr = dbC.sendQuery(queryD, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("P.ID_ACTION_PARAM_DATA");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("P.ID_DATA");
            if (s == null)
                continue;
            long dbKeyData = Long.parseLong(s);
            String qName = rs.getColumnData("Q.QUANTITY_NAME");
            String qType = rs.getColumnData("Q.TYPE") ;
            s = rs.getColumnData("Q.VALUE");
            if (s == null)
                continue;
            double qValue = Double.parseDouble(s);
            String qUncertainty = rs.getColumnData("Q.UNCERTAINTY");
            s = rs.getColumnData("Q.UNIT");
            long dbKeyUnit = Long.parseLong(s);
            CopexUnit qUnit = null;
            for (int j=0; j<nbPhysQ; j++){
                PhysicalQuantity phQ = listPhysicalQuantity.get(j);
                CopexUnit u = phQ.getUnit(dbKeyUnit) ;
                if (u != null){
                    qUnit = u;
                    break;
                }
            }
            QData data=  new QData(dbKeyData, locale, qName, qType, qValue, qUncertainty, qUnit);
            s = rs.getColumnData("I.ID_INITIAL_PARAM");
            if (s==null)
                continue;
            long dbKeyInitParam = Long.parseLong(s);
            // recherche dans la liste le parametre correspondant
            InitialActionParam initialParam = null;
            for (int k=0; k<tabInitialParam.length; k++){
                if (tabInitialParam[k].getDbKey() == dbKeyInitParam){
                    initialParam  =tabInitialParam[k];
                    break;
                }
            }
            ActionParamData paramD = new ActionParamData(dbKey, initialParam, data) ;
            listParam.add(paramD) ;
        }
        // recuperation des param quantite
        String queryQ = "SELECT P.ID_ACTION_PARAM_QUANTITY, P.ID_PARAMETER, Q.QUANTITY_NAME, Q.TYPE, Q.VALUE, Q.UNCERTAINTY, Q.UNIT, I.ID_INITIAL_PARAM  " +
                " FROM ACTION_PARAM_QUANTITY P, QUANTITY Q, LINK_ACTION_PARAM L, LINK_PARAM_INITIAL I " +
                "WHERE L.ID_ACTION = "+dbKeyAction+" AND L.ID_PARAM = P.ID_ACTION_PARAM_QUANTITY  AND P.ID_PARAMETER = Q.ID_QUANTITY AND L.ID_PARAM = I.ID_PARAM   ;  ";
        v2 = new ArrayList();
        listFields = new ArrayList();
        listFields.add("P.ID_ACTION_PARAM_QUANTITY");
        listFields.add("P.ID_PARAMETER");
        listFields.add("Q.QUANTITY_NAME");
        listFields.add("Q.TYPE");
        listFields.add("Q.VALUE");
        listFields.add("Q.UNCERTAINTY");
        listFields.add("Q.UNIT");
        listFields.add("I.ID_INITIAL_PARAM");

        cr = dbC.sendQuery(queryQ, listFields, v2);
        if (cr.isError())
            return cr;
        nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("P.ID_ACTION_PARAM_QUANTITY");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("P.ID_PARAMETER");
            if (s == null)
                continue;
            long dbKeyParameter = Long.parseLong(s);
            String qName = rs.getColumnData("Q.QUANTITY_NAME");
            String qType = rs.getColumnData("Q.TYPE") ;
            s = rs.getColumnData("Q.VALUE");
            if (s == null)
                continue;
            double qValue = Double.parseDouble(s);
            String qUncertainty = rs.getColumnData("Q.UNCERTAINTY");
            s = rs.getColumnData("Q.UNIT");
            long dbKeyUnit = Long.parseLong(s);
            CopexUnit qUnit = null;
            for (int j=0; j<nbPhysQ; j++){
                PhysicalQuantity phQ = listPhysicalQuantity.get(j);
                CopexUnit u = phQ.getUnit(dbKeyUnit) ;
                if (u != null){
                    qUnit = u;
                    break;
                }
            }
            Parameter parameter=  new Parameter(dbKeyParameter, locale, qName, qType, qValue, qUncertainty, qUnit);
            s = rs.getColumnData("I.ID_INITIAL_PARAM");
            if (s==null)
                continue;
            long dbKeyInitParam = Long.parseLong(s);
            // recherche dans la liste le parametre correspondant
            InitialActionParam initialParam = null;
            for (int k=0; k<tabInitialParam.length; k++){
                if (tabInitialParam[k].getDbKey() == dbKeyInitParam){
                    initialParam  =tabInitialParam[k];
                    break;
                }
            }
            ActionParamQuantity paramQ = new ActionParamQuantity(dbKey, initialParam, parameter) ;
            listParam.add(paramQ) ;

        }
        // recuperation des param material
        String queryM = "SELECT P.ID_ACTION_PARAM_MATERIAL, P.ID_MATERIAL, I.ID_INITIAL_PARAM   " +
                " FROM ACTION_PARAM_MATERIAL P,  LINK_ACTION_PARAM L, LINK_PARAM_INITIAL I " +
                "WHERE L.ID_ACTION = "+dbKeyAction+" AND L.ID_PARAM = P.ID_ACTION_PARAM_MATERIAL AND L.ID_PARAM = I.ID_PARAM   ;  ";
        v2 = new ArrayList();
        listFields = new ArrayList();
        listFields.add("P.ID_ACTION_PARAM_MATERIAL");
        listFields.add("P.ID_MATERIAL");
        listFields.add("I.ID_INITIAL_PARAM");

        cr = dbC.sendQuery(queryM, listFields, v2);
        if (cr.isError())
            return cr;
        nbR = v2.size();
        int nbMat = listMaterial.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("P.ID_ACTION_PARAM_MATERIAL");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("P.ID_MATERIAL");
            long dbKeyMat = Long.parseLong(s);
            // recherche du material
            Material material = null;
            for (int k=0; k<nbMat; k++){
                if (listMaterial.get(k).getDbKey() == dbKeyMat){
                    material = listMaterial.get(k);
                    break;
                }
            }
            if (material == null){
                ArrayList v4 = new ArrayList();
                cr = loadMaterialFromDB(dbC,locale, dbKeyMat,listPhysicalQuantity, v4);
                if (cr.isError())
                    return cr;
                material = (Material)v4.get(0);
                if (material == null){
                    logger.log(Level.SEVERE, (" !!!! material null"+dbKeyMat+" ("+dbKey+")"));
                }
            }
            s = rs.getColumnData("I.ID_INITIAL_PARAM");
            if (s==null)
                continue;
            long dbKeyInitParam = Long.parseLong(s);
            // recherche dans la liste le parametre correspondant
            InitialActionParam initialParam = null;
            for (int k=0; k<tabInitialParam.length; k++){
                if (tabInitialParam[k].getDbKey() == dbKeyInitParam){
                    initialParam = tabInitialParam[k];
                    break;
                }
            }
            // est il associe a un parametre Quantity ?
            ActionParamQuantity quantity = null;
            ArrayList v3 = new ArrayList();
            String query = "SELECT ID_PARAM_QUANTTY FROM LINK_PARAM_QUANTITY_MATERIAL WHERE ID_PARAM_MATERIAL = "+dbKey+" ;";
            ArrayList listFields2 = new ArrayList();
            listFields2.add("ID_PARAM_QUANTITY");
            cr = dbC.sendQuery(query, listFields2,v3);
            if (cr.isError())
                return cr;
            int nbR2 = v3.size();
            for (int j=0; j<nbR2; j++){
                ResultSetXML rs2=  (ResultSetXML)v3.get(j);
                s = rs2.getColumnData("ID_PARAM_QUANTITY");
                if (s==null)
                    continue;
                long dbKeyQ = Long.parseLong(s);
                // recherche du param quantity associe
                for (int k=0; k<listParam.size(); k++){
                    if (listParam.get(k) != null && listParam.get(k) instanceof ActionParamQuantity && listParam.get(k).getDbKey() == dbKeyQ){
                        quantity = (ActionParamQuantity)listParam.get(k);
                        break;
                    }
                }
            }
            ActionParamMaterial paramM = new ActionParamMaterial(dbKey, initialParam, material, quantity) ;
            listParam.add(paramM) ;
        }
        // tri des param selon l'action initial
        Object[] tabParam = new Object[nbParam];
        int nb = listParam.size();
        for (int i=0; i<nb; i++){
            long d = listParam.get(i).getInitialParam().getDbKey();
            // recherche de l'indice : 
            for (int j=0; j<nbParam; j++){
                long dbKeyIP = tabInitialParam[j].getDbKey();
                if(dbKeyIP == d){
                    // on le place en j
                    if(tabParam[j] == null)
                        tabParam[j] = listParam.get(i);
                    else if (tabParam[j] instanceof ArrayList){
                        ((ArrayList)tabParam[j]).add(listParam.get(i));
                    }else if (tabParam[j] instanceof ActionParam){
                        ArrayList<ActionParam> list = new ArrayList();
                        list.add((ActionParam)tabParam[j]);
                        list.add(listParam.get(i));
                        tabParam[j] = list;
                    }
                    break;
                }
            }
        }
        
        v.add(tabParam);
        return new CopexReturn();
    }

    /* charge un  materiel */
    private static CopexReturn loadMaterialFromDB(DataBaseCommunication dbC, Locale locale, long dbKeyMaterial, ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList v){
        Material m = null;
        String query = "SELECT MATERIAL_NAME, DESCRIPTION, URL_DESCRIPTION FROM MATERIAL WHERE ID_MATERIAL = "+dbKeyMaterial+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("MATERIAL_NAME");
        listFields.add("DESCRIPTION");
        listFields.add("URL_DESCRIPTION");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String name = rs.getColumnData("MATERIAL_NAME");
            String description = rs.getColumnData("DESCRIPTION");
            String urlDescription = rs.getColumnData("URL_DESCRIPTION");
            ArrayList v4 = new ArrayList();
            cr  = ExperimentalProcedureFromDB.getMaterialSource(dbC, dbKeyMaterial, v4);
            if(cr.isError())
                return cr;
            MaterialSource materialSource = (MaterialSource)v4.get(0);
            m = new Material(dbKeyMaterial, CopexUtilities.getLocalText(name, locale), CopexUtilities.getLocalText(description, locale), urlDescription, materialSource) ;
             // on recupere les parametres
            v4 = new ArrayList();
            cr = ExperimentalProcedureFromDB.getMaterialParametersFromDB(dbC,locale, dbKeyMaterial, listPhysicalQuantity, v4);
            if (cr.isError())
                return cr;

            ArrayList<Parameter> listP = (ArrayList<Parameter>)v4.get(0);
            if (listP != null)
                m.setListParameters(listP);
            // on associe le type de materiel au materiel
            ArrayList<TypeMaterial> listT = new ArrayList();
            String query2 = "SELECT T.ID_TYPE, T.TYPE_NAME FROM MATERIAL_TYPE T, LINK_TYPE_MATERIAL L WHERE " +
                       "L.ID_MATERIAL = "+dbKeyMaterial+" AND L.ID_TYPE = T.ID_TYPE ;";

            v4 = new ArrayList();
            ArrayList<String> listFields2 = new ArrayList();
            listFields2.add("T.ID_TYPE");
            listFields2.add("T.TYPE_NAME");

            cr = dbC.sendQuery(query2, listFields2, v4);
            if (cr.isError())
                return cr;
            int nbR2 = v4.size();
            for (int j=0; j<nbR2; j++){
                ResultSetXML rs2 = (ResultSetXML)v4.get(j);
                String s = rs2.getColumnData("T.ID_TYPE");
                if (s == null)
                    continue;
                long idT = Long.parseLong(s);
                TypeMaterial t = new TypeMaterial(idT, CopexUtilities.getLocalText(name, locale));
                listT.add(t);
            }

            m.setListType(listT);
        }
        v.add(m);
        return new CopexReturn();
    }
    /* creation des parametres d'une action , en v[0]  le tableau avec les nouveaux id */
    private static CopexReturn createActionParamInDB(DataBaseCommunication dbC, Locale locale,long dbKeyAction, Object[] tabParam, ArrayList v){
        // on enregistre d'abord les param de type qualite et data et ensuite les material pour eventuellement creer les liens
        // on fait 2 boucles car selon les langues le parametre qtt peut se retrouver apres le material
        for (int i=0; i<tabParam.length; i++){
            if (tabParam[i] instanceof ActionParamQuantity) {
                ArrayList v2 = new ArrayList();
                CopexReturn cr = createActionParamQuantityInDB(dbC, locale,dbKeyAction,(ActionParamQuantity)tabParam[i], v2 );
                if (cr.isError())
                    return cr;
                tabParam[i] = (ActionParamQuantity)v2.get(0);
             }else if (tabParam[i] instanceof ActionParamData) {
                ArrayList v2 = new ArrayList();
                CopexReturn cr = createActionParamDataInDB(dbC, locale,dbKeyAction,(ActionParamData)tabParam[i], v2 );
                if (cr.isError())
                    return cr;
                tabParam[i] = (ActionParamData)v2.get(0);
             }else if (tabParam[i] instanceof ArrayList){
                 ArrayList<ActionParam> p =(ArrayList<ActionParam>)tabParam[i];
                 int np = p.size();
                 for (int k=0; k<np; k++){
                     if (p.get(k) instanceof ActionParamQuantity){
                         ArrayList v2 = new ArrayList();
                         CopexReturn cr = createActionParamQuantityInDB(dbC, locale,dbKeyAction,(ActionParamQuantity)p.get(k), v2 );
                         if (cr.isError())
                            return cr;
                         ((ArrayList<ActionParam>)tabParam[i]).set(k, (ActionParamQuantity)v2.get(0));
                     }else if (p.get(k) instanceof ActionParamData){
                         ArrayList v2 = new ArrayList();
                         CopexReturn cr = createActionParamDataInDB(dbC, locale,dbKeyAction,(ActionParamData)p.get(k), v2 );
                         if (cr.isError())
                            return cr;
                         ((ArrayList<ActionParam>)tabParam[i]).set(k, (ActionParamData)v2.get(0));
                     }
                 }
             }
        }
        // deuxieme tour pour enregistrer  les param material
         for (int i=0; i<tabParam.length; i++){
            if (tabParam[i] instanceof ActionParamMaterial) {
                ArrayList v2 = new ArrayList();
                CopexReturn cr = createActionParamMaterialInDB(dbC, dbKeyAction,(ActionParamMaterial)tabParam[i], v2 );
                if (cr.isError())
                    return cr;
                tabParam[i] = (ActionParamMaterial)v2.get(0);
            }else if (tabParam[i] instanceof ArrayList){
                 ArrayList<ActionParam> p =(ArrayList<ActionParam>)tabParam[i];
                 int np = p.size();
                 for (int k=0; k<np; k++){
                     if (p.get(k) instanceof ActionParamMaterial){
                         ArrayList v2 = new ArrayList();
                         CopexReturn cr = createActionParamMaterialInDB(dbC, dbKeyAction,(ActionParamMaterial)p.get(k), v2 );
                         if (cr.isError())
                            return cr;
                         ((ArrayList<ActionParam>)tabParam[i]).set(k, (ActionParamMaterial)v2.get(0));
                     }
                 }
             }
         }
         v.add(tabParam);
         return new CopexReturn();
    }

    /* creation param qtt */
    private static CopexReturn createActionParamQuantityInDB(DataBaseCommunication dbC, Locale locale,long dbKeyAction, ActionParamQuantity actionParamQuantity, ArrayList v){
        String query = "INSERT INTO ACTION_PARAM (ID_ACTION_PARAM) VALUES (NULL) ;";
        String queryID = "SELECT max(last_insert_id(`ID_ACTION_PARAM`))   FROM ACTION_PARAM ;";
        long dbKey = -1;
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        dbKey = (Long)v2.get(0);
        // lien
        String queryLink = "INSERT INTO LINK_ACTION_PARAM (ID_ACTION, ID_PARAM) VALUES ("+dbKeyAction+", "+dbKey+") ;";
        v2 = new ArrayList();
        String[]querys = new String[1];
        querys[0] = queryLink ;
        cr = dbC.executeQuery(querys, v2);
        if (cr.isError())
             return cr;
         // parametre
         v2 = new ArrayList();
         cr = createQuantityInDB(dbC,locale,actionParamQuantity. getParameter(), v2);
         if (cr.isError())
            return cr;
         long dbKeyParameter = (Long)v2.get(0);
         String queryQ = "INSERT INTO ACTION_PARAM_QUANTITY (ID_ACTION_PARAM_QUANTITY, ID_PARAMETER) VALUES ("+dbKey+", "+dbKeyParameter+") ;";
         String queryLinkInitial = "INSERT INTO LINK_PARAM_INITIAL (ID_INITIAL_PARAM, ID_PARAM ) VALUES ("+(actionParamQuantity).getInitialParam().getDbKey()+", "+dbKey+") ;";
         v2 = new ArrayList();
         querys = new String[2];
         querys[0] = queryQ ;
         querys[1] = queryLinkInitial ;
         cr = dbC.executeQuery(querys, v2);
         if (cr.isError())
            return cr;
         (actionParamQuantity).setDbKey(dbKey);
         v.add(actionParamQuantity);
         return new CopexReturn();
    }

    /* creation param qtt */
    private static CopexReturn createActionParamDataInDB(DataBaseCommunication dbC, Locale locale,long dbKeyAction, ActionParamData actionParamData, ArrayList v){
        String query = "INSERT INTO ACTION_PARAM (ID_ACTION_PARAM) VALUES (NULL) ;";
        String queryID = "SELECT max(last_insert_id(`ID_ACTION_PARAM`))   FROM ACTION_PARAM ;";
        long dbKey = -1;
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        dbKey = (Long)v2.get(0);
        // lien
        String queryLink = "INSERT INTO LINK_ACTION_PARAM (ID_ACTION, ID_PARAM) VALUES ("+dbKeyAction+", "+dbKey+") ;";
        v2 = new ArrayList();
        String[]querys = new String[1];
        querys[0] = queryLink ;
        cr = dbC.executeQuery(querys, v2);
        if (cr.isError())
            return cr;
       // parametre
        v2 = new ArrayList();
        cr = createQuantityInDB(dbC,locale,actionParamData. getData(), v2);
        if (cr.isError())
           return cr;
        long dbKeyData = (Long)v2.get(0);
        String queryQ = "INSERT INTO ACTION_PARAM_DATA (ID_ACTION_PARAM_DATA, ID_DATA) VALUES ("+dbKey+", "+dbKeyData+") ;";
        String queryLinkInitial = "INSERT INTO LINK_PARAM_INITIAL (ID_INITIAL_PARAM, ID_PARAM ) VALUES ("+actionParamData.getInitialParam().getDbKey()+", "+dbKey+") ;";
        v2 = new ArrayList();
        querys = new String[2];
        querys[0] = queryQ ;
        querys[1] = queryLinkInitial ;
        cr = dbC.executeQuery(querys, v2);
        if (cr.isError())
           return cr;
        actionParamData.setDbKey(dbKey);
        v.add(actionParamData);
        return new CopexReturn();
    }

    /* creation param material */
    private static CopexReturn createActionParamMaterialInDB(DataBaseCommunication dbC, long dbKeyAction, ActionParamMaterial actionParamMaterial, ArrayList v){
        String query = "INSERT INTO ACTION_PARAM (ID_ACTION_PARAM) VALUES (NULL) ;";
        String queryID = "SELECT max(last_insert_id(`ID_ACTION_PARAM`))   FROM ACTION_PARAM ;";
        long dbKey = -1;
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
           return cr;
        dbKey = (Long)v2.get(0);
        // lien
        String queryLink = "INSERT INTO LINK_ACTION_PARAM (ID_ACTION, ID_PARAM) VALUES ("+dbKeyAction+", "+dbKey+") ;";
        v2 = new ArrayList();
        String[]querys = new String[1];
        querys[0] = queryLink ;
        cr = dbC.executeQuery(querys, v2);
        if (cr.isError())
            return cr;
        //
        int n=2;
        boolean addQ = false;
        if (actionParamMaterial.getQuantity()!= null){
             n++;
             addQ = true;
        }
        String queryParamMat = "INSERT INTO ACTION_PARAM_MATERIAL (ID_ACTION_PARAM_MATERIAL, ID_MATERIAL) VALUES ("+dbKey+", "+actionParamMaterial.getMaterial().getDbKey()+") ;";
        String queryLinkInitial = "INSERT INTO LINK_PARAM_INITIAL (ID_INITIAL_PARAM, ID_PARAM ) VALUES ("+actionParamMaterial.getInitialParam().getDbKey()+", "+dbKey+") ;";
        v2 = new ArrayList();
        querys = new String[n];
        querys[0] = queryParamMat ;
        querys[1] = queryLinkInitial ;
        if (addQ){
            String queryLinkQ = "INSERT INTO LINK_PARAM_QUANTITY_MATERIAL (ID_MATERIAL, ID_QUANTITY) VALUES ("+dbKey+", "+actionParamMaterial.getQuantity().getDbKey()+") ;";
            querys[2] = queryLinkQ ;
        }
        cr = dbC.executeQuery(querys, v2);
        if (cr.isError())
           return cr;
        actionParamMaterial.setDbKey(dbKey);
        v.add(actionParamMaterial);
        return new CopexReturn();
    }

    /* creation d'un parametre dans la base, en v[0] le ,nouvel identifiant */
    private static CopexReturn createQuantityInDB(DataBaseCommunication dbC, Locale locale,Quantity quantity, ArrayList v){
        String name = quantity.getName(locale) ;
        name =  AccesDB.replace("\'",name,"''") ;
        String type = quantity.getType(locale) ;
        type =  AccesDB.replace("\'",type,"''") ;
        String uncertainty = quantity.getUncertainty(locale) ;
        uncertainty =  AccesDB.replace("\'",uncertainty,"''") ;
        String query = "INSERT INTO QUANTITY (ID_QUANTITY, QUANTITY_NAME, TYPE, VALUE, UNCERTAINTY, UNIT) VALUES (NULL, '"+name+"', '"+type+"',"+quantity.getValue()+" , '"+uncertainty+"', "+quantity.getUnit().getDbKey()+") ;";
        String queryID = "SELECT max(last_insert_id(`ID_QUANTITY`))   FROM QUANTITY ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
         v2 = new ArrayList();
        String[] querys = new String[1];
        String queryP = "";
        if (quantity instanceof Parameter){
            queryP = "INSERT INTO COPEX_PARAMETER (ID_PARAMETER) VALUES ("+dbKey+");";
        }else if (quantity instanceof QData){
            queryP = "INSERT INTO Q_DATA (ID_DATA) VALUES ("+dbKey+");";
        }
        querys[0] = queryP;
        cr = dbC.executeQuery(querys, v2);
        v.add(dbKey);
        return cr;
    }

   

    /* suppression des parametre d'une action */
    private static CopexReturn deleteActionParamFromDB(DataBaseCommunication dbC, long dbKeyAction){
        String[] querys = new String[14];
        int q=0;
        String queryDelParameter = "DELETE FROM COPEX_PARAMETER WHERE ID_PARAMETER IN (SELECT ID_PARAMETER FROM ACTION_PARAM_QUANTITY WHERE ID_ACTION_PARAM IN (SELECT ID_PARAM FROM LINK_ACTION_PARAM WHERE ID_ACTION = "+dbKeyAction+" ) );";
        querys[q] = queryDelParameter ;
        q++;
        String queryDelParameterQ = "DELETE FROM QUANTITY WHERE ID_QUANTITY IN (SELECT ID_PARAMETER FROM ACTION_PARAM_QUANTITY WHERE ID_ACTION_PARAM IN (SELECT ID_PARAM FROM LINK_ACTION_PARAM WHERE ID_ACTION = "+dbKeyAction+" ) );";
        querys[q] = queryDelParameterQ ;
        q++;
        String queryDelLink = "DELETE FROM LINK_PARAM_QUANTITY_MATERIAL WHERE ID_PARAM_MATERIAL IN (SELECT ID_PARAM FROM LINK_ACTION_PARAM WHERE ID_ACTION = "+dbKeyAction+" ) ;";
        querys[q] = queryDelLink ;
        q++;
        String queryDelActionParamQ = "DELETE FROM ACTION_PARAM_QUANTITY WHERE ID_ACTION_PARAM IN (SELECT ID_PARAM FROM LINK_ACTION_PARAM WHERE ID_ACTION = "+dbKeyAction+" ) ;";
        querys[q] = queryDelActionParamQ ;
        q++;
        String queryDelActionParamMat = "DELETE FROM ACTION_PARAM_MATERIAL WHERE ID_ACTION_PARAM_MATERIAL IN (SELECT ID_PARAM FROM LINK_ACTION_PARAM WHERE ID_ACTION = "+dbKeyAction+" ) ;";
        querys[q] = queryDelActionParamMat ;
        q++;
        String queryDelActionParamData = "DELETE FROM ACTION_PARAM_DATA WHERE ID_ACTION_PARAM_DATA IN (SELECT ID_PARAM FROM LINK_ACTION_PARAM WHERE ID_ACTION = "+dbKeyAction+" ) ;";
        querys[q] = queryDelActionParamData ;
        q++;
        String queryDelActionParam = "DELETE FROM ACTION_PARAM WHERE ID_ACTION_PARAM IN (SELECT ID_PARAM FROM LINK_ACTION_PARAM WHERE ID_ACTION = "+dbKeyAction+" ) ;";
        querys[q] = queryDelActionParam ;
        q++;
        String queryDelLinkAction = "DELETE FROM LINK_ACTION_PARAM WHERE ID_ACTION = "+dbKeyAction+"  ;";
        querys[q] = queryDelLinkAction ;
        q++;
        String queryDelLinkMaterialProd = "DELETE FROM MATERIAL_PRODUCE WHERE ID_MATERIAL IN (SELECT ID_MATERIAL FROM LINK_ACTION_MATERIAL_PROD WHERE ID_ACTION = "+dbKeyAction+")  ;";
        querys[q] = queryDelLinkMaterialProd ;
        q++;
        String queryDelLinkTypeMaterial = "DELETE FROM LINK_TYPE_MATERIAL WHERE ID_MATERIAL IN (SELECT ID_MATERIAL FROM LINK_ACTION_MATERIAL_PROD WHERE ID_ACTION = "+dbKeyAction+")  ;";
        //querys[q] = queryDelLinkTypeMaterial ;
        //q++;
        String queryDelQuantity = "DELETE FROM QUANTITY WHERE ID_QUANTITY IN (SELECT ID_PARAMETER FROM LINK_MATERIAL_PARAMETERS WHERE ID_MATERIAL IN (SELECT ID_MATERIAL FROM  LINK_ACTION_MATERIAL_PROD WHERE ID_ACTION = "+dbKeyAction+") );" ;
        //querys[q] = queryDelQuantity ;
        //q++;
        String queryDelParameter2 = "DELETE FROM COPEX_PARAMETER WHERE ID_PARAMETER IN (SELECT ID_PARAMETER FROM LINK_MATERIAL_PARAMETERS WHERE ID_MATERIAL IN (SELECT ID_MATERIAL FROM  LINK_ACTION_MATERIAL_PROD WHERE ID_ACTION = "+dbKeyAction+") );" ;
        //querys[q] = queryDelParameter2 ;
       // q++;
        String queryDelLinkParamMat = "DELETE FROM LINK_MATERIAL_PARAMETERS WHERE ID_MATERIAL IN (SELECT ID_MATERIAL FROM  LINK_ACTION_MATERIAL_PROD WHERE ID_ACTION = "+dbKeyAction+") ;" ;
        //querys[q] = queryDelLinkParamMat ;
        //q++;
        String queryDelMaterial = "DELETE FROM MATERIAL WHERE ID_MATERIAL IN (SELECT ID_MATERIAL FROM LINK_ACTION_MATERIAL_PROD WHERE ID_ACTION = "+dbKeyAction+")  ;";
        //querys[q] = queryDelMaterial ;
        //q++;
        String queryDelLinkActionMaterial = "DELETE FROM LINK_ACTION_MATERIAL_PROD WHERE ID_ACTION = "+dbKeyAction+"  ;";
        querys[q] = queryDelLinkActionMaterial ;
        q++;
        String queryDelLinkDataProd = "DELETE FROM DATA_PRODUCE WHERE ID_DATA IN (SELECT ID_DATA FROM LINK_ACTION_DATA_PROD WHERE ID_ACTION = "+dbKeyAction+")  ;";
        querys[q] = queryDelLinkDataProd ;
        q++;
        String queryDelQuantity2 = "DELETE FROM QUANTITY WHERE ID_QUANTITY IN (SELECT ID_DATA FROM LINK_ACTION_DATA_PROD WHERE  ID_ACTION = "+dbKeyAction+") ;" ;
        querys[q] = queryDelQuantity2 ;
        q++;
        String queryDelQData = "DELETE FROM Q_DATA WHERE ID_DATA IN (SELECT ID_DATA FROM LINK_ACTION_DATA_PROD WHERE  ID_ACTION = "+dbKeyAction+") ;" ;
        querys[q] = queryDelQData ;
        q++;
        String queryDelLinkActionData = "DELETE FROM LINK_ACTION_DATA_PROD WHERE ID_ACTION = "+dbKeyAction+"  ;";
        querys[q] = queryDelLinkActionData ;
        q++;
        ArrayList v = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }


    /* chargement repetition d'une t�che */
    private static  CopexReturn getTaskRepeatFromDB(DataBaseCommunication dbC, long dbKeyTask, ArrayList<InitialNamedAction> listInitAction, Object[] listActionParam, ArrayList<Material> listMaterialProd, ArrayList<QData> listDataProd,ArrayList v){
        TaskRepeat taskRepeat = null;
        String query = "SELECT R.ID_REPEAT, R.NB_REPEAT FROM TASK_REPEAT R, LINK_TASK_REPEAT L WHERE L.ID_TASK = "+dbKeyTask+" AND L.ID_REPEAT = R.ID_REPEAT ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("R.ID_REPEAT");
        listFields.add("R.NB_REPEAT");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("R.ID_REPEAT");
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("R.NB_REPEAT");
            int nbRepeat = Integer.parseInt(s);
            taskRepeat = new TaskRepeat(dbKey, nbRepeat);
            // chargement des parametres
            ArrayList v3 = new ArrayList();
            cr = getTaskRepeatParamFromDB(dbC, dbKey, listInitAction, listActionParam,listMaterialProd, listDataProd, v3);
            if (cr.isError())
                return cr;
            ArrayList<TaskRepeatParam> listParam = (ArrayList<TaskRepeatParam>)v3.get(0);
            taskRepeat.setListParam(listParam);
        }
        v.add(taskRepeat);
        return new CopexReturn();
    }

    /* chargement des parametres de repetition */
    private static CopexReturn getTaskRepeatParamFromDB(DataBaseCommunication dbC, long dbKeyRepeat, ArrayList<InitialNamedAction> listInitAction, Object[] listActionParam, ArrayList<Material> listMaterialProd, ArrayList<QData> listDataProd, ArrayList v){
        ArrayList listParam = new ArrayList();
        ArrayList v2 = new ArrayList();
        CopexReturn cr = getTaskRepeatParamDataFromDB(dbC, dbKeyRepeat, listInitAction, listActionParam, v2);
        if (cr.isError())
            return cr;
        ArrayList<TaskRepeatParamData> l = (ArrayList<TaskRepeatParamData>)v2.get(0);
        int nb = l.size();
        for (int i=0; i<nb; i++){
            listParam.add(l.get(i));
        }
        v2 = new ArrayList();
        cr = getTaskRepeatParamMaterialFromDB(dbC, dbKeyRepeat,listInitAction, listActionParam, v2);
        if (cr.isError())
            return cr;
        ArrayList<TaskRepeatParamMaterial> l2 = (ArrayList<TaskRepeatParamMaterial>)v2.get(0);
        nb = l2.size();
        for (int i=0; i<nb; i++){
            listParam.add(l2.get(i));
        }
        v2 = new ArrayList();
        cr = getTaskRepeatParamQuantityFromDB(dbC, dbKeyRepeat, listInitAction, listActionParam, v2);
        if (cr.isError())
            return cr;
        ArrayList<TaskRepeatParamQuantity> l3 = (ArrayList<TaskRepeatParamQuantity>)v2.get(0);
        nb = l3.size();
        for (int i=0; i<nb; i++){
            listParam.add(l3.get(i));
        }
        v2 = new ArrayList();
        cr = getTaskRepeatParamOutputAcquisitionFromDB(dbC, dbKeyRepeat,listInitAction, listDataProd, v2);
        if (cr.isError())
            return cr;
        ArrayList<TaskRepeatParamOutputAcquisition> l4 = (ArrayList<TaskRepeatParamOutputAcquisition>)v2.get(0);
        nb = l4.size();
        for (int i=0; i<nb; i++){
            listParam.add(l4.get(i));
        }
        v2 = new ArrayList();
        cr = getTaskRepeatParamOutputManipulationFromDB(dbC, dbKeyRepeat, listInitAction, listMaterialProd, v2);
        if (cr.isError())
            return cr;
        ArrayList<TaskRepeatParamOutputManipulation> l5 = (ArrayList<TaskRepeatParamOutputManipulation>)v2.get(0);
        nb = l5.size();
        for (int i=0; i<nb; i++){
            listParam.add(l5.get(i));
        }
        v2 = new ArrayList();
        cr = getTaskRepeatParamOutputTreatmentFromDB(dbC, dbKeyRepeat,listInitAction, listDataProd, v2);
        if (cr.isError())
            return cr;
        ArrayList<TaskRepeatParamOutputTreatment> l6 = (ArrayList<TaskRepeatParamOutputTreatment>)v2.get(0);
        nb = l6.size();
        for (int i=0; i<nb; i++){
            listParam.add(l6.get(i));
        }
        v.add(listParam);
        return new CopexReturn();
    }

    /* chargement des parametres de repetition type data */
    private static CopexReturn getTaskRepeatParamDataFromDB(DataBaseCommunication dbC, long dbKeyTaskRepeat, ArrayList<InitialNamedAction> listInitAction, Object[] listActionParam, ArrayList v){
        ArrayList<TaskRepeatParamData> list = new ArrayList();
        String query = "SELECT P.ID_PARAM_REPEAT, P.ID_PARAM, P.ID_ACTION FROM PARAM_TASK_REPEAT P, LINK_TASK_REPEAT_PARAM L " +
                "WHERE L.ID_REPEAT = "+dbKeyTaskRepeat+" AND " +
                "L.ID_PARAM_REPEAT = P.ID_PARAM_REPEAT AND " +
                "P.ID_PARAM IN (SELECT ID_PARAM FROM INITIAL_PARAM_DATA);";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("P.ID_PARAM_REPEAT");
        listFields.add("P.ID_PARAM");
        listFields.add("P.ID_ACTION");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("P.ID_PARAM_REPEAT");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("P.ID_PARAM");
            long dbKeyInitParam = Long.parseLong(s);
            s = rs.getColumnData("P.ID_ACTION");
            long dbKeyActionParam = -1;
            try{
                dbKeyActionParam = Long.parseLong(s);
            }catch(NumberFormatException e){
                
            }
            // initialAction para correspondant
            InitialParamData initialParamData = getInitialParamData(dbKeyInitParam, listInitAction);
            // chargement des valeurs
            ArrayList v3 = new ArrayList();
            cr = getTaskRepeatValueParamDataFromDB(dbC, dbKey, listActionParam, v3);
            if(cr.isError())
                return cr;
            ArrayList<TaskRepeatValueParamData> listValue = (ArrayList<TaskRepeatValueParamData>)v3.get(0);
            TaskRepeatParamData d = new TaskRepeatParamData(dbKey, dbKeyActionParam, initialParamData, listValue);
            list.add(d);
        }
        v.add(list);
        return new CopexReturn();
    }


    /* recherche param initial dans une liste d'actions initiales, null sinon  */
    private static InitialParamData getInitialParamData(long dbKey, ArrayList<InitialNamedAction> list){
        int nb = list.size();
        for (int i=0; i<nb; i++){
            InitialNamedAction a = list.get(i);
            if (a.getVariable() != null){
                InitialActionParam[] tab =a.getVariable().getTabParam();
                for (int j=0; j<tab.length; j++){
                    if (tab[j] instanceof InitialParamData && tab[j].getDbKey() == dbKey)
                        return ((InitialParamData)tab[j]);
                }
            }
        }
        return null;
    }

    /* chargement des valeurs des parametres param data */
    private static CopexReturn getTaskRepeatValueParamDataFromDB(DataBaseCommunication dbC, long dbKeyParamRepeat, Object[] listActionParam, ArrayList v){
        ArrayList<TaskRepeatValueParamData> list = new ArrayList();
        String query = "SELECT V.ID_VALUE, V.NO_REPEAT, V.ID_ACTION_PARAM FROM PARAM_VALUE_TASK_REPEAT V, LINK_PARAM_VALUE_TASK_REPEAT L " +
                " WHERE L.ID_PARAM_REPEAT = "+dbKeyParamRepeat+" AND " +
                "L.ID_VALUE = V.ID_VALUE AND " +
                "V.ID_ACTION_PARAM IN (SELECT ID_ACTION_PARAM_DATA FROM ACTION_PARAM_DATA ) ORDER BY V.NO_REPEAT;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("V.ID_VALUE");
        listFields.add("V.NO_REPEAT");
        listFields.add("V.ID_ACTION_PARAM");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("V.ID_VALUE");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("V.NO_REPEAT");
            int noRepeat = Integer.parseInt(s);
            s = rs.getColumnData("V.ID_ACTION_PARAM");
            long dbKeyAction = Long.parseLong(s);
            ActionParamData actionParamData = getActionParamData(dbKeyAction, listActionParam);
            TaskRepeatValueParamData d = new TaskRepeatValueParamData(dbKey, noRepeat, actionParamData);
            list.add(d);
        }
        v.add(list);
        return new CopexReturn();
    }

    /* retourne l'action parametree correspondante, null sinon */
    private static ActionParamData getActionParamData(long dbKey, Object[] tabParam){
        for (int i=0; i<tabParam.length; i++){
            if (tabParam[i] instanceof ActionParamData){
                if (((ActionParamData)tabParam[i]).getDbKey() == dbKey)
                    return ((ActionParamData)tabParam[i]);
            }else if (tabParam[i] instanceof ArrayList ){
                ArrayList<ActionParam> list = (ArrayList<ActionParam>)tabParam[i];
                int nb = list.size();
                for (int k=0; k<nb; k++){
                    if (list.get(k) instanceof ActionParamData && ((ActionParamData)list.get(k)).getDbKey() == dbKey){
                        return ((ActionParamData)list.get(k));
                    }
                }
            }
        }
        return null;
    }


    /* chargement des parametres de repetition type material */
    private static CopexReturn getTaskRepeatParamMaterialFromDB(DataBaseCommunication dbC, long dbKeyTaskRepeat, ArrayList<InitialNamedAction> listInitAction, Object[] listActionParam, ArrayList v){
        ArrayList<TaskRepeatParamMaterial> list = new ArrayList();
        String query = "SELECT P.ID_PARAM_REPEAT, P.ID_PARAM, P.ID_ACTION FROM PARAM_TASK_REPEAT P, LINK_TASK_REPEAT_PARAM L " +
                "WHERE L.ID_REPEAT = "+dbKeyTaskRepeat+" AND " +
                "L.ID_PARAM_REPEAT = P.ID_PARAM_REPEAT AND " +
                "P.ID_PARAM IN (SELECT ID_PARAM FROM INITIAL_PARAM_MATERIAL);";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("P.ID_PARAM_REPEAT");
        listFields.add("P.ID_PARAM");
        listFields.add("P.ID_ACTION");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("P.ID_PARAM_REPEAT");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("P.ID_PARAM");
            long dbKeyInitParam = Long.parseLong(s);
            // initialAction para correspondant
            InitialParamMaterial initialParamMaterial = getInitialParamMaterial(dbKeyInitParam, listInitAction);
            s = rs.getColumnData("P.ID_ACTION");
            long dbKeyActionParam = -1;
            try{
                dbKeyActionParam = Long.parseLong(s);
            }catch(NumberFormatException e){

            }
            // chargement des valeurs
            ArrayList v3 = new ArrayList();
            cr = getTaskRepeatValueParamMaterialFromDB(dbC, dbKey, listActionParam, v3);
            if(cr.isError())
                return cr;
            ArrayList<TaskRepeatValueParamMaterial> listValue = (ArrayList<TaskRepeatValueParamMaterial>)v3.get(0);
            TaskRepeatParamMaterial d = new TaskRepeatParamMaterial(dbKey,dbKeyActionParam,  initialParamMaterial, listValue);
            list.add(d);
        }
        v.add(list);
        return new CopexReturn();
    }

    /* recherche param initial dans une liste d'actions initiales, null sinon  */
    private static InitialParamMaterial getInitialParamMaterial(long dbKey, ArrayList<InitialNamedAction> list){
        int nb = list.size();
        for (int i=0; i<nb; i++){
            InitialNamedAction a = list.get(i);
            if (a.getVariable() != null){
                InitialActionParam[] tab =a.getVariable().getTabParam();
                for (int j=0; j<tab.length; j++){
                    if (tab[j] instanceof InitialParamMaterial && tab[j].getDbKey() == dbKey)
                        return ((InitialParamMaterial)tab[j]);
                }
            }
        }
        return null;
    }

    /* chargement des valeurs des parametres param material */
    private static CopexReturn getTaskRepeatValueParamMaterialFromDB(DataBaseCommunication dbC, long dbKeyParamRepeat, Object[] listActionParam, ArrayList v){
        ArrayList<TaskRepeatValueParamMaterial> list = new ArrayList();
        String query = "SELECT V.ID_VALUE, V.NO_REPEAT, V.ID_ACTION_PARAM FROM PARAM_VALUE_TASK_REPEAT V, LINK_PARAM_VALUE_TASK_REPEAT L " +
                " WHERE L.ID_PARAM_REPEAT = "+dbKeyParamRepeat+" AND " +
                "L.ID_VALUE = V.ID_VALUE AND " +
                "V.ID_ACTION_PARAM IN (SELECT ID_ACTION_PARAM_MATERIAL FROM ACTION_PARAM_MATERIAL )  ORDER BY V.NO_REPEAT;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("V.ID_VALUE");
        listFields.add("V.NO_REPEAT");
        listFields.add("V.ID_ACTION_PARAM");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("V.ID_VALUE");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("V.NO_REPEAT");
            int noRepeat = Integer.parseInt(s);
            s = rs.getColumnData("V.ID_ACTION_PARAM");
            long dbKeyAction = Long.parseLong(s);
            ActionParamMaterial actionParamMaterial = getActionParamMaterial(dbKeyAction, listActionParam);
            TaskRepeatValueParamMaterial m = new TaskRepeatValueParamMaterial(dbKey, noRepeat, actionParamMaterial);
            list.add(m);
        }
        v.add(list);
        return new CopexReturn();
    }

    /* retourne l'action parametree correspondante, null sinon */
    private static ActionParamMaterial getActionParamMaterial(long dbKey, Object[] tabParam){
        for (int i=0; i<tabParam.length; i++){
            if (tabParam[i] instanceof ActionParamMaterial){
                if(((ActionParamMaterial)tabParam[i]).getDbKey() == dbKey)
                    return ((ActionParamMaterial)tabParam[i]);
            }else if (tabParam[i] instanceof ArrayList){
                ArrayList<ActionParam> list = (ArrayList<ActionParam>)tabParam[i];
                int nb = list.size();
                for (int k=0; k<nb; k++){
                    if(list.get(k) instanceof ActionParamMaterial && ((ActionParamMaterial)list.get(k)).getDbKey() == dbKey){
                        return ((ActionParamMaterial)list.get(k));
                    }
                }
            }
        }
        return null;
    }


    /* chargement des parametres de repetition type qtt */
    private static CopexReturn getTaskRepeatParamQuantityFromDB(DataBaseCommunication dbC, long dbKeyTaskRepeat, ArrayList<InitialNamedAction> listInitAction, Object[] listActionParam, ArrayList v){
        ArrayList<TaskRepeatParamQuantity> list = new ArrayList();
        String query = "SELECT P.ID_PARAM_REPEAT, P.ID_PARAM, P.ID_ACTION FROM PARAM_TASK_REPEAT P, LINK_TASK_REPEAT_PARAM L " +
                "WHERE L.ID_REPEAT = "+dbKeyTaskRepeat+" AND " +
                "L.ID_PARAM_REPEAT = P.ID_PARAM_REPEAT AND " +
                "P.ID_PARAM IN (SELECT ID_PARAM FROM INITIAL_PARAM_QUANTITY);";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("P.ID_PARAM_REPEAT");
        listFields.add("P.ID_PARAM");
        listFields.add("P.ID_ACTION");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("P.ID_PARAM_REPEAT");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("P.ID_PARAM");
            long dbKeyInitParam = Long.parseLong(s);
            // initialAction para correspondant
            InitialParamQuantity initialParamQuantity = getInitialParamQuantity(dbKeyInitParam, listInitAction);
            s = rs.getColumnData("P.ID_ACTION");
            long dbKeyActionParam = -1;
            try{
                dbKeyActionParam = Long.parseLong(s);
            }catch(NumberFormatException e){

            }
            // chargement des valeurs
            ArrayList v3 = new ArrayList();
            cr = getTaskRepeatValueParamQuantityFromDB(dbC, dbKey, listActionParam, v3);
            if(cr.isError())
                return cr;
            ArrayList<TaskRepeatValueParamQuantity> listValue = (ArrayList<TaskRepeatValueParamQuantity>)v3.get(0);
            TaskRepeatParamQuantity q = new TaskRepeatParamQuantity(dbKey, dbKeyActionParam, initialParamQuantity, listValue);
            list.add(q);
        }
        v.add(list);
        return new CopexReturn();
    }


    /* recherche param initial dans une liste d'actions initiales, null sinon  */
    private static InitialParamQuantity getInitialParamQuantity(long dbKey, ArrayList<InitialNamedAction> list){
        int nb = list.size();
        for (int i=0; i<nb; i++){
            InitialNamedAction a = list.get(i);
            if (a.getVariable() != null){
                InitialActionParam[] tab =a.getVariable().getTabParam();
                for (int j=0; j<tab.length; j++){
                    if (tab[j] instanceof InitialParamQuantity && tab[j].getDbKey() == dbKey)
                        return ((InitialParamQuantity)tab[j]);
                }
            }
        }
        return null;
    }

    /* chargement des valeurs des parametres param qtt */
    private static CopexReturn getTaskRepeatValueParamQuantityFromDB(DataBaseCommunication dbC, long dbKeyParamRepeat, Object[] listActionParam, ArrayList v){
        ArrayList<TaskRepeatValueParamQuantity> list = new ArrayList();
        String query = "SELECT V.ID_VALUE, V.NO_REPEAT, V.ID_ACTION_PARAM FROM PARAM_VALUE_TASK_REPEAT V, LINK_PARAM_VALUE_TASK_REPEAT L " +
                " WHERE L.ID_PARAM_REPEAT = "+dbKeyParamRepeat+" AND " +
                "L.ID_VALUE = V.ID_VALUE AND " +
                "V.ID_ACTION_PARAM IN (SELECT ID_ACTION_PARAM_QUANTITY FROM ACTION_PARAM_QUANTITY )  ORDER BY V.NO_REPEAT;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("V.ID_VALUE");
        listFields.add("V.NO_REPEAT");
        listFields.add("V.ID_ACTION_PARAM");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("V.ID_VALUE");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("V.NO_REPEAT");
            int noRepeat = Integer.parseInt(s);
            s = rs.getColumnData("V.ID_ACTION_PARAM");
            long dbKeyAction = Long.parseLong(s);
            ActionParamQuantity actionParamQtt = getActionParamQuantity(dbKeyAction, listActionParam);
            TaskRepeatValueParamQuantity q = new TaskRepeatValueParamQuantity(dbKey, noRepeat, actionParamQtt);
            list.add(q);
        }
        v.add(list);
        return new CopexReturn();
    }

    /* retourne l'action parametree correspondante, null sinon */
    private static ActionParamQuantity getActionParamQuantity(long dbKey, Object[] tabParam){
        for (int i=0; i<tabParam.length; i++){
            if (tabParam[i] instanceof ActionParamQuantity){
                if(((ActionParamQuantity)tabParam[i]).getDbKey() == dbKey)
                    return ((ActionParamQuantity)tabParam[i]);
            }else if (tabParam[i] instanceof ArrayList){
                ArrayList<ActionParam> list = (ArrayList<ActionParam>)tabParam[i];
                int nb = list.size();
                for (int k=0; k<nb; k++){
                    if(list.get(k) instanceof ActionParamQuantity && ((ActionParamQuantity)list.get(k)).getDbKey() == dbKey){
                        return ((ActionParamQuantity)list.get(k));
                    }
                }
            }
        }
        return null;
    }



    /* chargement des parametres de repetition type outputMAnipulation */
    private static CopexReturn getTaskRepeatParamOutputManipulationFromDB(DataBaseCommunication dbC, long dbKeyTaskRepeat, ArrayList<InitialNamedAction> listInitAction, ArrayList<Material> listMaterial, ArrayList v){
        ArrayList<TaskRepeatParamOutputManipulation> list = new ArrayList();
        String query = "SELECT P.ID_PARAM_REPEAT, P.ID_OUTPUT_MANIPULATION, P.ID_ACTION FROM PARAM_TASK_REPEAT P, LINK_TASK_REPEAT_PARAM L " +
                "WHERE L.ID_REPEAT = "+dbKeyTaskRepeat+" AND " +
                "L.ID_PARAM_REPEAT = P.ID_PARAM_REPEAT AND " +
                "P.ID_OUTPUT_MANIPULATION IS NOT NULL;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("P.ID_PARAM_REPEAT");
        listFields.add("P.ID_OUTPUT_MANIPULATION");
        listFields.add("P.ID_ACTION");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("P.ID_PARAM_REPEAT");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("P.ID_OUTPUT_MANIPULATION");
            long dbKeyOutput = Long.parseLong(s);
            // output manipulation correspondant
            InitialManipulationOutput initialOutput = getInitialOutputManipulation(dbKeyOutput, listInitAction);
            s = rs.getColumnData("P.ID_ACTION");
            long dbKeyActionParam = -1;
            try{
                dbKeyActionParam = Long.parseLong(s);
            }catch(NumberFormatException e){

            }
            // chargement des valeurs
            ArrayList v3 = new ArrayList();
            cr = getTaskRepeatValueOutputManipulationFromDB(dbC, dbKey, listMaterial, v3);
            if(cr.isError())
                return cr;
            ArrayList<TaskRepeatValueMaterialProd> listValue = (ArrayList<TaskRepeatValueMaterialProd>)v3.get(0);
            TaskRepeatParamOutputManipulation o = new TaskRepeatParamOutputManipulation(dbKey, dbKeyActionParam, initialOutput, listValue);
            list.add(o);
        }
        v.add(list);
        return new CopexReturn();
    }

    /* recherche output manipulation dans une liste d'actions initiales, null sinon  */
    private static InitialManipulationOutput getInitialOutputManipulation(long dbKey, ArrayList<InitialNamedAction> list){
        int nb = list.size();
        for (int i=0; i<nb; i++){
            InitialNamedAction a = list.get(i);
            if(a instanceof InitialActionManipulation){
                ArrayList<InitialManipulationOutput> listOutput = ((InitialActionManipulation)a).getListOutput();
                int nbo = listOutput.size();
                for (int j=0; j<nbo; j++){
                    if (listOutput.get(j).getDbKey() == dbKey)
                        return listOutput.get(j);
                }
            }
        }
        return null;
    }

     /* chargement des valeurs des parametres output manipulation */
    private static CopexReturn getTaskRepeatValueOutputManipulationFromDB(DataBaseCommunication dbC, long dbKeyParamRepeat, ArrayList<Material> listMaterials, ArrayList v){
        ArrayList<TaskRepeatValueMaterialProd> list = new ArrayList();
        String query = "SELECT V.ID_VALUE, V.NO_REPEAT, V.ID_MATERIAL_PROD FROM PARAM_VALUE_TASK_REPEAT V, LINK_PARAM_VALUE_TASK_REPEAT L " +
                " WHERE L.ID_PARAM_REPEAT = "+dbKeyParamRepeat+" AND " +
                "L.ID_VALUE = V.ID_VALUE AND " +
                "V.ID_MATERIAL_PROD IN (SELECT ID_MATERIAL FROM MATERIAL_PRODUCE)  ORDER BY V.NO_REPEAT;;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("V.ID_VALUE");
        listFields.add("V.NO_REPEAT");
        listFields.add("V.ID_MATERIAL_PROD");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("V.ID_VALUE");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("V.NO_REPEAT");
            int noRepeat = Integer.parseInt(s);
            s = rs.getColumnData("V.ID_MATERIAL_PROD");
            long dbKeyMaterial = Long.parseLong(s);
            Material m = getMaterialProd(dbKeyMaterial, listMaterials);
            TaskRepeatValueMaterialProd t = new TaskRepeatValueMaterialProd(dbKey, noRepeat, m);
            list.add(t);
        }
        v.add(list);
        return new CopexReturn();
    }


    /* retourne le material correspondant , null sinon*/
    private static Material getMaterialProd(long dbKey, ArrayList<Material> list){
        int nb = list.size();
        for (int i=0; i<nb; i++){
            if (list.get(i).getDbKey() == dbKey)
                return list.get(i);
        }
        return null;
    }

    /* chargement des parametres de repetition type outputAcquisition */
    private static CopexReturn getTaskRepeatParamOutputAcquisitionFromDB(DataBaseCommunication dbC, long dbKeyTaskRepeat, ArrayList<InitialNamedAction> listInitAction, ArrayList<QData> listDataProd, ArrayList v){
        ArrayList<TaskRepeatParamOutputAcquisition> list = new ArrayList();
        String query = "SELECT P.ID_PARAM_REPEAT, P.ID_OUTPUT_ACQUISITION, P.ID_ACTION FROM PARAM_TASK_REPEAT P, LINK_TASK_REPEAT_PARAM L " +
                "WHERE L.ID_REPEAT = "+dbKeyTaskRepeat+" AND " +
                "L.ID_PARAM_REPEAT = P.ID_PARAM_REPEAT AND " +
                "P.ID_OUTPUT_ACQUISITION IS NOT NULL;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("P.ID_PARAM_REPEAT");
        listFields.add("P.ID_OUTPUT_ACQUISITION");
        listFields.add("P.ID_ACTION");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("P.ID_PARAM_REPEAT");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("P.ID_OUTPUT_ACQUISITION");
            long dbKeyOutput = Long.parseLong(s);
            // output acq correspondant
            InitialAcquisitionOutput initialOutput = getInitialOutputAcquisition(dbKeyOutput, listInitAction);
            s = rs.getColumnData("P.ID_ACTION");
            long dbKeyActionParam = -1;
            try{
                dbKeyActionParam = Long.parseLong(s);
            }catch(NumberFormatException e){

            }
            // chargement des valeurs
            ArrayList v3 = new ArrayList();
            cr = getTaskRepeatValueOutputAcquisitionFromDB(dbC, dbKey, listDataProd, v3);
            if(cr.isError())
                return cr;
            ArrayList<TaskRepeatValueDataProd> listValue = (ArrayList<TaskRepeatValueDataProd>)v3.get(0);
            TaskRepeatParamOutputAcquisition o = new TaskRepeatParamOutputAcquisition(dbKey, dbKeyActionParam, initialOutput, listValue);
            list.add(o);
        }
        v.add(list);
        return new CopexReturn();
    }

    /* recherche output acquisition dans une liste d'actions initiales, null sinon  */
    private static InitialAcquisitionOutput getInitialOutputAcquisition(long dbKey, ArrayList<InitialNamedAction> list){
        int nb = list.size();
        for (int i=0; i<nb; i++){
            InitialNamedAction a = list.get(i);
            if(a instanceof InitialActionAcquisition){
                ArrayList<InitialAcquisitionOutput> listOutput = ((InitialActionAcquisition)a).getListOutput();
                int nbo = listOutput.size();
                for (int j=0; j<nbo; j++){
                    if (listOutput.get(j).getDbKey() == dbKey)
                        return listOutput.get(j);
                }
            }
        }
        return null;
    }

     /* chargement des valeurs des parametres output manipulation */
    private static CopexReturn getTaskRepeatValueOutputAcquisitionFromDB(DataBaseCommunication dbC, long dbKeyParamRepeat, ArrayList<QData> listDataProd, ArrayList v){
        ArrayList<TaskRepeatValueDataProd> list = new ArrayList();
        String query = "SELECT V.ID_VALUE, V.NO_REPEAT, V.ID_DATA_PROD FROM PARAM_VALUE_TASK_REPEAT V, LINK_PARAM_VALUE_TASK_REPEAT L " +
                " WHERE L.ID_PARAM_REPEAT = "+dbKeyParamRepeat+" AND " +
                "L.ID_VALUE = V.ID_VALUE AND " +
                "V.ID_DATA_PROD IN (SELECT ID_DATA FROM DATA_PRODUCE)  ORDER BY V.NO_REPEAT;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("V.ID_VALUE");
        listFields.add("V.NO_REPEAT");
        listFields.add("V.ID_DATA_PROD");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("V.ID_VALUE");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("V.NO_REPEAT");
            int noRepeat = Integer.parseInt(s);
            s = rs.getColumnData("V.ID_DATA_PROD");
            long dbKeyData = Long.parseLong(s);
            QData q = getDataProd(dbKeyData, listDataProd);
            TaskRepeatValueDataProd t = new TaskRepeatValueDataProd(dbKey, noRepeat, q);
            list.add(t);
        }
        v.add(list);
        return new CopexReturn();
    }


    /* retourne le data correspondant , null sinon*/
    private static QData getDataProd(long dbKey, ArrayList<QData> list){
        int nb = list.size();
        for (int i=0; i<nb; i++){
            if (list.get(i).getDbKey() == dbKey)
                return list.get(i);
        }
        return null;
    }

     /* chargement des parametres de repetition type output treatment */
    private static CopexReturn getTaskRepeatParamOutputTreatmentFromDB(DataBaseCommunication dbC, long dbKeyTaskRepeat, ArrayList<InitialNamedAction> listInitAction, ArrayList<QData> listDataProd, ArrayList v){
        ArrayList<TaskRepeatParamOutputTreatment> list = new ArrayList();
        String query = "SELECT P.ID_PARAM_REPEAT, P.ID_OUTPUT_TREATMENT, P.ID_ACTION FROM PARAM_TASK_REPEAT P, LINK_TASK_REPEAT_PARAM L " +
                "WHERE L.ID_REPEAT = "+dbKeyTaskRepeat+" AND " +
                "L.ID_PARAM_REPEAT = P.ID_PARAM_REPEAT AND " +
                "P.ID_OUTPUT_TREATMENT IS NOT NULL;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("P.ID_PARAM_REPEAT");
        listFields.add("P.ID_OUTPUT_TREATMENT");
        listFields.add("P.ID_ACTION");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("P.ID_PARAM_REPEAT");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            s = rs.getColumnData("P.ID_OUTPUT_TREATMENT");
            long dbKeyOutput = Long.parseLong(s);
            // output treat correspondant
            InitialTreatmentOutput initialOutput = getInitialOutputTreatment(dbKeyOutput, listInitAction);
            s = rs.getColumnData("P.ID_ACTION");
            long dbKeyActionParam = -1;
            try{
                dbKeyActionParam = Long.parseLong(s);
            }catch(NumberFormatException e){

            }
            // chargement des valeurs
            ArrayList v3 = new ArrayList();
            cr = getTaskRepeatValueOutputAcquisitionFromDB(dbC, dbKey, listDataProd, v3);
            if(cr.isError())
                return cr;
            ArrayList<TaskRepeatValueDataProd> listValue = (ArrayList<TaskRepeatValueDataProd>)v3.get(0);
            TaskRepeatParamOutputTreatment o = new TaskRepeatParamOutputTreatment(dbKey, dbKeyActionParam, initialOutput, listValue);
            list.add(o);
        }
        v.add(list);
        return new CopexReturn();
    }

    /* recherche output treatment dans une liste d'actions initiales, null sinon  */
    private static InitialTreatmentOutput getInitialOutputTreatment(long dbKey, ArrayList<InitialNamedAction> list){
        int nb = list.size();
        for (int i=0; i<nb; i++){
            InitialNamedAction a = list.get(i);
            if(a instanceof InitialActionTreatment){
                ArrayList<InitialTreatmentOutput> listOutput = ((InitialActionTreatment)a).getListOutput();
                int nbo = listOutput.size();
                for (int j=0; j<nbo; j++){
                    if (listOutput.get(j).getDbKey() == dbKey)
                        return listOutput.get(j);
                }
            }
        }
        return null;
    }



    /*insertion repetition tache, retourne en v[0] la repetition de taches */
    public static CopexReturn insertTaskRepeatInDB(DataBaseCommunication dbC, long dbKeyTask, TaskRepeat taskRepeat, ArrayList v){
        String query = "INSERT INTO TASK_REPEAT (ID_REPEAT, NB_REPEAT) VALUES (NULL, "+taskRepeat.getNbRepeat()+") ;";
        String queryID = "SELECT max(last_insert_id(`ID_REPEAT`))   FROM TASK_REPEAT ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        taskRepeat.setDbKey(dbKey);
        // lien avec la tache
        String queryLink = "INSERT INTO LINK_TASK_REPEAT (ID_TASK, ID_REPEAT) VALUES ("+dbKeyTask+", "+dbKey+") ;";
        String[] querys = new String[1];
        querys[0] = queryLink ;
        v2 = new ArrayList();
        cr = dbC.executeQuery(querys, v2);
        if (cr.isError())
            return cr;
        // enregistrement des parametres
        int nbParam = taskRepeat.getListParam().size();
        for (int i=0; i<nbParam; i++){
            TaskRepeatParam p = taskRepeat.getListParam().get(i);
            v2 = new ArrayList();
            cr = insertTaskRepeatParamInDB(dbC, dbKey, p, v2);
            if (cr.isError())
                return cr;
            p = (TaskRepeatParam)v2.get(0);
        }
        v.add(taskRepeat);
        return new CopexReturn();
    }

    /* insertion des parametres de repetition */
    private static CopexReturn insertTaskRepeatParamInDB(DataBaseCommunication dbC,long dbKeyTaskRepeat, TaskRepeatParam paramRepeat,  ArrayList v){
        if(paramRepeat instanceof TaskRepeatParamOutputAcquisition){
            TaskRepeatParamOutputAcquisition p = (TaskRepeatParamOutputAcquisition)paramRepeat;
            String query = "INSERT INTO PARAM_TASK_REPEAT (ID_PARAM_REPEAT, ID_OUTPUT_ACQUISITION) VALUES (NULL, "+p.getOutput().getDbKey()+");";
            String queryID = "SELECT max(last_insert_id(`ID_PARAM_REPEAT`))   FROM PARAM_TASK_REPEAT ;";
            ArrayList v2 = new ArrayList();
            CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
            if (cr.isError())
                return cr;
            long dbKey = (Long)v2.get(0);
            paramRepeat.setDbKey(dbKey);
            // enregistrement des valeurs
            int nbV = p.getListValue().size();
            for (int i=0; i<nbV; i++){
                TaskRepeatValueDataProd d = p.getListValue().get(i);
                String queryValue = "INSERT INTO PARAM_VALUE_TASK_REPEAT (ID_VALUE, NO_REPEAT, ID_DATA_PROD) VALUES (NULL, "+i+", "+d.getData().getDbKey()+");";
                String queryIDV = "SELECT max(last_insert_id(`ID_VALUE`))   FROM PARAM_VALUE_TASK_REPEAT ;";
                v2 = new ArrayList();
                cr = dbC.getNewIdInsertInDB(queryValue, queryIDV, v2);
                if (cr.isError())
                    return cr;
                long dbKeyV = (Long)v2.get(0);
                d.setDbKey(dbKeyV);
                // lien
                String queryLink = "INSERT INTO LINK_PARAM_VALUE_TASK_REPEAT (ID_PARAM_REPEAT, ID_VALUE) VALUES ("+dbKey+", "+dbKeyV+") ;";
                String[] querys = new String[1];
                querys[0] = queryLink ;
                v2 = new ArrayList();
                cr = dbC.executeQuery(querys, v2);
                if (cr.isError())
                    return cr;
            }
        }else if (paramRepeat instanceof TaskRepeatParamOutputManipulation){
            TaskRepeatParamOutputManipulation p = (TaskRepeatParamOutputManipulation)paramRepeat;
            String query = "INSERT INTO PARAM_TASK_REPEAT (ID_PARAM_REPEAT, ID_OUTPUT_MANIPULATION) VALUES (NULL, "+p.getOutput().getDbKey()+");";
            String queryID = "SELECT max(last_insert_id(`ID_PARAM_REPEAT`))   FROM PARAM_TASK_REPEAT ;";
            ArrayList v2 = new ArrayList();
            CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
            if (cr.isError())
                return cr;
            long dbKey = (Long)v2.get(0);
            paramRepeat.setDbKey(dbKey);
            // enregistrement des valeurs
            int nbV = p.getListValue().size();
            for (int i=0; i<nbV; i++){
                TaskRepeatValueMaterialProd m = p.getListValue().get(i);
                String queryValue = "INSERT INTO PARAM_VALUE_TASK_REPEAT (ID_VALUE, NO_REPEAT, ID_MATERIAL_PROD) VALUES (NULL, "+i+", "+m.getMaterial().getDbKey()+");";
                String queryIDV = "SELECT max(last_insert_id(`ID_VALUE`))   FROM PARAM_VALUE_TASK_REPEAT ;";
                v2 = new ArrayList();
                cr = dbC.getNewIdInsertInDB(queryValue, queryIDV, v2);
                if (cr.isError())
                    return cr;
                long dbKeyV = (Long)v2.get(0);
                m.setDbKey(dbKeyV);
                // lien
                String queryLink = "INSERT INTO LINK_PARAM_VALUE_TASK_REPEAT (ID_PARAM_REPEAT, ID_VALUE) VALUES ("+dbKey+", "+dbKeyV+") ;";
                String[] querys = new String[1];
                querys[0] = queryLink ;
                v2 = new ArrayList();
                cr = dbC.executeQuery(querys, v2);
                if (cr.isError())
                    return cr;
            }
        }else if (paramRepeat instanceof TaskRepeatParamOutputTreatment){
            TaskRepeatParamOutputTreatment p = (TaskRepeatParamOutputTreatment)paramRepeat;
            String query = "INSERT INTO PARAM_TASK_REPEAT (ID_PARAM_REPEAT, ID_OUTPUT_TREATMENT) VALUES (NULL, "+p.getOutput().getDbKey()+");";
            String queryID = "SELECT max(last_insert_id(`ID_PARAM_REPEAT`))   FROM PARAM_TASK_REPEAT ;";
            ArrayList v2 = new ArrayList();
            CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
            if (cr.isError())
                return cr;
            long dbKey = (Long)v2.get(0);
            paramRepeat.setDbKey(dbKey);
            // enregistrement des valeurs
            int nbV = p.getListValue().size();
            for (int i=0; i<nbV; i++){
                TaskRepeatValueDataProd d = p.getListValue().get(i);
                String queryValue = "INSERT INTO PARAM_VALUE_TASK_REPEAT (ID_VALUE, NO_REPEAT, ID_DATA_PROD) VALUES (NULL, "+i+", "+d.getData().getDbKey()+");";
                String queryIDV = "SELECT max(last_insert_id(`ID_VALUE`))   FROM PARAM_VALUE_TASK_REPEAT ;";
                v2 = new ArrayList();
                cr = dbC.getNewIdInsertInDB(queryValue, queryIDV, v2);
                if (cr.isError())
                    return cr;
                long dbKeyV = (Long)v2.get(0);
                d.setDbKey(dbKeyV);
                // lien
                String queryLink = "INSERT INTO LINK_PARAM_VALUE_TASK_REPEAT (ID_PARAM_REPEAT, ID_VALUE) VALUES ("+dbKey+", "+dbKeyV+") ;";
                String[] querys = new String[1];
                querys[0] = queryLink ;
                v2 = new ArrayList();
                cr = dbC.executeQuery(querys, v2);
                if (cr.isError())
                    return cr;
            }
        }else{
            long id = -1;
            if(paramRepeat instanceof TaskRepeatParamData)
                id = ((TaskRepeatParamData)paramRepeat).getInitialParamData().getDbKey() ;
            else if (paramRepeat instanceof TaskRepeatParamMaterial)
                id = ((TaskRepeatParamMaterial)paramRepeat).getInitialParamMaterial().getDbKey() ;
            else if (paramRepeat instanceof TaskRepeatParamQuantity)
                id = ((TaskRepeatParamQuantity)paramRepeat).getInitialParamQuantity().getDbKey() ;
            String query = "INSERT INTO PARAM_TASK_REPEAT (ID_PARAM_REPEAT, ID_PARAM) VALUES (NULL, "+id+");";
            String queryID = "SELECT max(last_insert_id(`ID_PARAM_REPEAT`))   FROM PARAM_TASK_REPEAT ;";
            ArrayList v2 = new ArrayList();
            CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
            if (cr.isError())
                return cr;
            long dbKey = (Long)v2.get(0);
            paramRepeat.setDbKey(dbKey);
            // enregistrement des valeurs
            if(paramRepeat instanceof TaskRepeatParamData){
                ArrayList<TaskRepeatValueParamData> list = ((TaskRepeatParamData)paramRepeat).getListValue();
                int nb = list.size();
                for (int i=0; i<nb; i++){
                    TaskRepeatValueParamData d = list.get(i);
                    String queryValue = "INSERT INTO PARAM_VALUE_TASK_REPEAT (ID_VALUE, NO_REPEAT, ID_ACTION_PARAM) VALUES (NULL, "+i+", "+d.getActionParamData().getDbKey()+") ;";
                    String queryIDV = "SELECT max(last_insert_id(`ID_VALUE`))   FROM PARAM_VALUE_TASK_REPEAT ;";
                    v2 = new ArrayList();
                    cr = dbC.getNewIdInsertInDB(queryValue, queryIDV, v2);
                    if (cr.isError())
                        return cr;
                    long dbKeyV = (Long)v2.get(0);
                    d.setDbKey(dbKeyV);
                    // lien
                    String queryLink = "INSERT INTO LINK_PARAM_VALUE_TASK_REPEAT (ID_PARAM_REPEAT, ID_VALUE) VALUES ("+dbKey+", "+dbKeyV+") ;";
                    String[] querys = new String[1];
                    querys[0] = queryLink ;
                    v2 = new ArrayList();
                    cr = dbC.executeQuery(querys, v2);
                    if (cr.isError())
                        return cr;
                }
            }else if (paramRepeat instanceof TaskRepeatParamMaterial){
                ArrayList<TaskRepeatValueParamMaterial> list = ((TaskRepeatParamMaterial)paramRepeat).getListValue();
                int nb = list.size();
                for (int i=0; i<nb; i++){
                    TaskRepeatValueParamMaterial m = list.get(i);
                    String queryValue = "INSERT INTO PARAM_VALUE_TASK_REPEAT (ID_VALUE, NO_REPEAT, ID_ACTION_PARAM) VALUES (NULL, "+i+", "+m.getActionParamMaterial().getDbKey()+") ;";
                    String queryIDV = "SELECT max(last_insert_id(`ID_VALUE`))   FROM PARAM_VALUE_TASK_REPEAT ;";
                    v2 = new ArrayList();
                    cr = dbC.getNewIdInsertInDB(queryValue, queryIDV, v2);
                    if (cr.isError())
                        return cr;
                    long dbKeyV = (Long)v2.get(0);
                    m.setDbKey(dbKeyV);
                    // lien
                    String queryLink = "INSERT INTO LINK_PARAM_VALUE_TASK_REPEAT (ID_PARAM_REPEAT, ID_VALUE) VALUES ("+dbKey+", "+dbKeyV+") ;";
                    String[] querys = new String[1];
                    querys[0] = queryLink ;
                    v2 = new ArrayList();
                    cr = dbC.executeQuery(querys, v2);
                    if (cr.isError())
                        return cr;
                }
            }else if (paramRepeat instanceof TaskRepeatParamQuantity){
                ArrayList<TaskRepeatValueParamQuantity> list = ((TaskRepeatParamQuantity)paramRepeat).getListValue();
                int nb = list.size();
                for (int i=0; i<nb; i++){
                    TaskRepeatValueParamQuantity q = list.get(i);
                    String queryValue = "INSERT INTO PARAM_VALUE_TASK_REPEAT (ID_VALUE, NO_REPEAT, ID_ACTION_PARAM) VALUES (NULL, "+i+", "+q.getActionParamQuantity().getDbKey()+") ;";
                    String queryIDV = "SELECT max(last_insert_id(`ID_VALUE`))   FROM PARAM_VALUE_TASK_REPEAT ;";
                    v2 = new ArrayList();
                    cr = dbC.getNewIdInsertInDB(queryValue, queryIDV, v2);
                    if (cr.isError())
                        return cr;
                    long dbKeyV = (Long)v2.get(0);
                    q.setDbKey(dbKeyV);
                    // lien
                    String queryLink = "INSERT INTO LINK_PARAM_VALUE_TASK_REPEAT (ID_PARAM_REPEAT, ID_VALUE) VALUES ("+dbKey+", "+dbKeyV+") ;";
                    String[] querys = new String[1];
                    querys[0] = queryLink ;
                    v2 = new ArrayList();
                    cr = dbC.executeQuery(querys, v2);
                    if (cr.isError())
                        return cr;
                }
            }
        }

        // enregistrement lien
        String queryLink = "INSERT INTO LINK_TASK_REPEAT_PARAM (ID_REPEAT, ID_PARAM_REPEAT) VALUES ("+dbKeyTaskRepeat+", "+paramRepeat.getDbKey()+") ;";
        String[] querys = new String[1];
        querys[0] = queryLink ;
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        if (cr.isError())
            return cr;

        v.add(paramRepeat);
        return new CopexReturn();
    }


    /* suppression des repetitions */
    public static CopexReturn deleteTaskRepeatFromDB(DataBaseCommunication dbC, long dbKeyTask, TaskRepeat taskRepeat){
        CopexReturn cr = deleteParamTaskRepeatFromDB(dbC, taskRepeat.getDbKey());
        if( cr.isError())
            return cr;
        String[] querys = new String[2];
        int q=0;
        String query = "DELETE FROM TASK_REPEAT WHERE ID_REPEAT = "+taskRepeat.getDbKey()+" ;";
        querys[q] = query;
        q++;
        String queryLink = "DELETE FROM LINK_TASK_REPEAT WHERE ID_TASK = "+dbKeyTask+" ;";
        querys[q] = queryLink;
        q++;
        ArrayList v = new ArrayList();
        cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* mise a jour de la repetition d'une tache */
    public static CopexReturn updateTaskRepeatInDB(DataBaseCommunication dbC, TaskRepeat taskRepeat, ArrayList v){
        String query = "UPDATE TASK_REPEAT SET NB_REPEAT = "+taskRepeat.getNbRepeat()+" WHERE ID_REPEAT = "+taskRepeat.getDbKey()+" ;";
        String[] querys = new String[1];
        querys[0] = query ;
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        if (cr.isError())
            return cr;
        // mise a jour des parametres
        // on efface tout et on recree tout
        cr = deleteParamTaskRepeatFromDB(dbC, taskRepeat.getDbKey());
        if(cr.isError())
            return cr;
        int nbParam = taskRepeat.getListParam().size();
        for (int i=0; i<nbParam; i++){
            TaskRepeatParam p = taskRepeat.getListParam().get(i);
            v2 = new ArrayList();
            cr = insertTaskRepeatParamInDB(dbC, taskRepeat.getDbKey(), p, v2);
            if (cr.isError())
                return cr;
            taskRepeat.getListParam().set(i, (TaskRepeatParam)v2.get(0)) ;
        }
        v.add(taskRepeat);
        return new CopexReturn();
    }

    /* suppression des parametres de la repetition */
    private static CopexReturn deleteParamTaskRepeatFromDB(DataBaseCommunication dbC, long dbKey){
        String[] querys = new String[4];
        int q=0;
        String queryParamValue = "DELETE FROM PARAM_VALUE_TASK_REPEAT WHERE ID_VALUE IN (SELECT ID_VALUE FROM LINK_PARAM_VALUE_TASK_REPEAT WHERE ID_PARAM_REPEAT IN (SELECT ID_PARAM_REPEAT FROM LINK_TASK_REPEAT_PARAM WHERE ID_REPEAT = "+dbKey+")) ;";
        querys[q] = queryParamValue;
        q++;
        String queryDelLinkValue = "DELETE FROM LINK_PARAM_VALUE_TASK_REPEAT WHERE ID_PARAM_REPEAT IN (SELECT ID_PARAM_REPEAT FROM LINK_TASK_REPEAT_PARAM WHERE ID_REPEAT = "+dbKey+") ;";
        querys[q] = queryDelLinkValue;
        q++;
        String queryDelParam = "DELETE FROM PARAM_TASK_REPEAT WHERE ID_PARAM_REPEAT IN (SELECT ID_PARAM_REPEAT FROM LINK_TASK_REPEAT_PARAM WHERE ID_REPEAT = "+dbKey+");";
        querys[q] = queryDelParam;
        q++;
        String queryDelLinkParam = "DELETE FROM LINK_TASK_REPEAT_PARAM WHERE ID_REPEAT = "+dbKey+";";
        querys[q] = queryDelLinkParam;

        ArrayList v = new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }
}
