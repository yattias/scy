/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;



import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.profiler.Profiler;
import eu.scy.client.tools.copex.synchro.Locker;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Element;

/**
 * MBO le 03/03/09 : multi proc initiaux
 * gere les protocoles 
 * @author MBO
 */
public class ExperimentalProcedureFromDB {
    private static final Logger logger = Logger.getLogger(ExperimentalProcedureFromDB.class.getName());

    /* chargement des protocoles lie a une mission et un utilisateur
     * MBO le 27/02/09 : proc lockes
     en v[0] : la liste des proc
     en v[1] : la liste des proc initiaux
     en v[2] : le nom des proc lockes
     en v[3] ; un boolean qui indique si ts les proc de cet utilisateurs pour cette mission sont lockes
     */
    public static CopexReturn getProcMissionFromDB(DataBaseCommunication dbC, Locker locker, Locale locale, long dbKeyMission, long dbKeyLabDoc, ArrayList<Long> listIdInitProc, ArrayList<PhysicalQuantity> listPhysicalQuantity,ArrayList<MaterialStrategy> listMaterialStrategy,  ArrayList v) {
        ArrayList<LearnerProcedure> listP = new ArrayList();
        ArrayList v2 = new ArrayList();
        CopexReturn cr = getInitialProcFromDB(dbC, locale, dbKeyMission, listIdInitProc, listPhysicalQuantity, listMaterialStrategy, v2) ;
        if (cr.isError())
            return cr;
        ArrayList<InitialProcedure> listInitProc = (ArrayList<InitialProcedure>)v2.get(0);
        // lsite de noms des proc lockes
        ArrayList<String> listProcLocked = new ArrayList();
        boolean allProcLocked = true;
        String query = "SELECT ID_PROC, PROC_NAME, DATE_LAST_MODIFICATION, ACTIV, PROC_RIGHT " +
                " FROM EXPERIMENTAL_PROCEDURE WHERE ID_PROC IN (" +
                "SELECT ID_PROC FROM LINK_LABDOC_PROC WHERE " +
                "ID_LABDOC = "+dbKeyLabDoc+" ) AND " +
                "ID_PROC IN (SELECT ID_PROC FROM LEARNER_PROC ) ;";
                
        v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_PROC");
        listFields.add("PROC_NAME");
        listFields.add("DATE_LAST_MODIFICATION");
        listFields.add("ACTIV");
        listFields.add("PROC_RIGHT");
        Profiler.start("xml_loadProc");
        cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        if(nbR == 0)
            allProcLocked = false;
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_PROC");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("PROC_NAME");
            if (name == null)
                continue;
            Date dateLastModif = null;
            s = rs.getColumnData("DATE_LAST_MODIFICATION");
            if (s == null)
                continue;
            dateLastModif = CopexUtilities.getDate(s);
            s = rs.getColumnData("ACTIV");
            if (s == null)
                continue;
            int activ = 0;
            try{
                activ = Integer.parseInt(s);
            }catch(NumberFormatException e){
                activ = 0;
            }
            boolean isActiv = (activ == 1);
            s = rs.getColumnData("PROC_RIGHT");
            if (s == null)
                continue;
            char r = s.charAt(0);


            // MBO proc lockes
            if (locker.isLocked(dbKeyLabDoc)){
                listProcLocked.add(name);
            }else{
                allProcLocked = false;
                // proc initial correspondant ?
                ArrayList v3 = new ArrayList();
                cr = getCorrespondingProcInitialFromDB(dbC, dbKey, listInitProc, v3);
                if (cr.isError())
                    return cr;
                InitialProcedure initProcCorresp = (InitialProcedure)v3.get(0);
                ArrayList<Material> listMaterial = initProcCorresp.getListMaterial() ;
                // recuperation materiel utilise
                v3 = new ArrayList();
                cr = getMaterialUsedFromDB(dbC, locale,dbKey, listMaterial, listPhysicalQuantity, v3);
                if(cr.isError())
                    return cr;
                ArrayList<MaterialUsed> listMaterialUsed = (ArrayList<MaterialUsed>)v3.get(0);
                LearnerProcedure proc = new LearnerProcedure(dbKey, CopexUtilities.getLocalText(name, locale), dateLastModif, isActiv, r, initProcCorresp, listMaterialUsed);
                proc.setDbKeyLabDoc(dbKeyLabDoc);
                // recuperation de la question
                Profiler.start("xml_loadQuestion");
                v3 = new ArrayList();
                cr = getQuestionProcFromDB(dbC, locale,dbKey, v3);
                if (cr.isError())
                    return cr;
                Question question = (Question)v3.get(0);
                proc.setQuestion(question);
                Profiler.end("xml_loadQuestion");
                // recuperation de l'arbre des taches
                Profiler.start("xml_loadTasks");
                v3 = new ArrayList();
                cr = TaskFromDB.getAllTaskFromDB(dbC, locale, dbKey, question.getDbKey(), initProcCorresp.getListNamedAction(), listMaterial, listPhysicalQuantity, v3);
                if (cr.isError())
                    return cr;
                ArrayList<CopexTask> listTask = (ArrayList<CopexTask>)v3.get(0);
                proc.setListTask(listTask);
                Profiler.end("xml_loadTasks");
                // met le lien du fils a la question
                int ts = listTask.size();
                for (int k=0; k<ts; k++){
                    if (listTask.get(k).getDbKey() == question.getDbKey()){
                        question.setDbKeyChild(listTask.get(k).getDbKeyChild());
                    }
                }

                // recuperation de l'hypothese
                v3 = new ArrayList();
                cr = getHypothesisFromDB(dbC, locale,proc, v3);
                if(cr.isError())
                    return cr;
                Hypothesis hypothesis = null;
                if(v3.size()>0 && v3.get(0) != null)
                    hypothesis = (Hypothesis)v3.get(0);
                proc.setHypothesis(hypothesis);
                // general principle
                v3 = new ArrayList();
                cr = getGeneralPrincipleFromDB(dbC, locale,proc, v3);
                if(cr.isError())
                    return cr;
                GeneralPrinciple principle = null;
                if(v3.size()>0 && v3.get(0) != null)
                    principle = (GeneralPrinciple)v3.get(0);
                proc.setGeneralPrinciple(principle);
                // recuperation de l'evaluation
                v3 = new ArrayList();
                cr = getEvaluationFromDB(dbC, locale,proc, v3);
                if(cr.isError())
                    return cr;
                Evaluation evaluation = null;
                if(v3.size()>0 && v3.get(0) != null)
                    evaluation = (Evaluation)v3.get(0);
                proc.setEvaluation(evaluation);
                // ajoute dans la liste
                listP.add(proc);
            }
        }
        Profiler.end("xml_loadProc");   
        
        v.add(listP);
        v.add(listInitProc);
        v.add(listProcLocked);
        v.add(allProcLocked);
        return new CopexReturn();
    }

    /* retourne en v[0] le proc initial correspondant au proc */
    private static  CopexReturn getCorrespondingProcInitialFromDB(DataBaseCommunication dbC, long dbKeyProc, ArrayList<InitialProcedure> listInitialProc, ArrayList v){
        InitialProcedure initProc = null;
        int nbInitProc = listInitialProc.size();
        String query = "SELECT ID_INITIAL_PROC FROM LINK_LEARNER_INITIAL_PROC WHERE ID_LEARNER_PROC = "+dbKeyProc+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_INITIAL_PROC");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_INITIAL_PROC");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            for (int j=0; j<nbInitProc; j++){
                if (listInitialProc.get(j).getDbKey() == dbKey){
                    initProc = listInitialProc.get(j);
                    break;
                }
            }
        }
        if (initProc == null)
            return new CopexReturn("ERROR : initial proc null!!", false);
        v.add(initProc);
        return new CopexReturn();
    }
    /* chargement des noms des procs d'une mission */
    public static CopexReturn getShortProcMissionFromDB(DataBaseCommunication dbC, Locale locale,long dbKeyMission, long dbKeyUser ,  ArrayList v){
        // recup dans labbook des labdocs
        dbC.updateDb(MyConstants.DB_LABBOOK);
        ArrayList<Long> listLabDoc = new ArrayList();
        String query = "SELECT D.ID_LABDOC FROM LABDOC D, MISSION_CONF C " +
                "WHERE C.ID_MISSION_CONF = D.ID_MISSION_CONF AND C.ID_MISSION = "+dbKeyMission+" AND D.TYPE='Procedure' " +
                "AND C.ID_LEARNER_GROUP IN (SELECT ID_LEARNER_GROUP FROM LINK_GROUP_LEARNER WHERE ID_LEARNER = "+dbKeyUser+" ) ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("D.ID_LABDOC");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
            return cr;
        }
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("D.ID_LABDOC");
            try{
                Long ld = Long.parseLong(s);
                listLabDoc.add(ld);
            }catch(NumberFormatException e){

            }
        }
        dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
        //recup des proc
        ArrayList<LearnerProcedure> listP = new ArrayList();
        int nbLD = listLabDoc.size();
        if(nbLD == 0){
            v.add(listP);
            return new CopexReturn();
        }
        String listLD = "";
        for(int i=0; i<nbLD; i++){
            listLD += listLabDoc.get(i);
            if(i < nbLD-1)
                listLD += ", ";
        }
         query = "SELECT E.ID_PROC, E.PROC_NAME, L.ID_INITIAL_PROC, M.ID_MISSION  " +
                " FROM EXPERIMENTAL_PROCEDURE E, LINK_LEARNER_INITIAL_PROC L, LINK_LABDOC_PROC D, LEARNER_PROC P, LINK_MISSION_INITIAL_PROC M " +
                "WHERE D.ID_LABDOC IN ("+listLD+") AND L.ID_INITIAL_PROC = M.ID_INIT_PROC AND "+
                "D.ID_PROC = P.ID_PROC AND D.ID_PROC  = E.ID_PROC AND D.ID_PROC = L.ID_LEARNER_PROC ;";
         v2 = new ArrayList();
        listFields = new ArrayList();
        listFields.add("E.ID_PROC");
        listFields.add("E.PROC_NAME");
        listFields.add("L.ID_INITIAL_PROC");
        listFields.add("M.ID_MISSION");
        cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("E.ID_PROC");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("E.PROC_NAME");
            if (name == null)
                continue;
            s = rs.getColumnData("L.ID_INITIAL_PROC") ;
            long dbKeyInitProc = Long.parseLong(s);
            s = rs.getColumnData("M.ID_MISSION");
            long idMission = Long.parseLong(s);
            ArrayList v3 = new ArrayList();
            ArrayList<Long> listInitProc = new ArrayList();
            listInitProc.add(dbKeyInitProc);
            cr = getSimpleInitialProcFromDB(dbC, locale,idMission, listInitProc, v3);
            if(cr.isError())
                return cr;
            ArrayList<InitialProcedure> listIP = (ArrayList<InitialProcedure>)v3.get(0);
            InitialProcedure initProc = listIP.get(0);
            LearnerProcedure proc = new LearnerProcedure(dbKey, CopexUtilities.getLocalText(name, locale), null, true,MyConstants.NONE_RIGHT, initProc, null );
            // ajoute dans la liste
            listP.add(proc);
        }
        v.add(listP);
        return new CopexReturn();
    }


     /* chargement de la question d'un protocole */
    public static CopexReturn getQuestionProcFromDB(DataBaseCommunication dbC,Locale locale, long dbKeyProc, ArrayList v) {
        String query = "SELECT L.ID_TASK, T.TASK_NAME, T.DESCRIPTION, T.COMMENTS, T.TASK_IMAGE, T.DRAW_ELO, T.IS_VISIBLE, R.EDIT_RIGHT, R.DELETE_RIGHT, R.COPY_RIGHT, R.MOVE_RIGHT, R.PARENT_RIGHT, R.DRAW_RIGHT, R.REPEAT_RIGHT  " +
                " FROM COPEX_TASK T, QUESTION Q, LINK_PROC_TASK L, TASK_RIGHT R " +
                "WHERE L.ID_PROC = "+dbKeyProc+" AND L.QUESTION = 1 AND " +
                "T.ID_TASK = L.ID_TASK AND " +
                "Q.ID_QUESTION = L.ID_TASK AND " +
                "T.ID_TASK = R.ID_TASK ;";
        Question question = null;        
        
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("L.ID_TASK");
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
        
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("L.ID_TASK");
            if (s == null)
                continue;
            long dbKeyQuestion = Long.parseLong(s);
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
            s = rs.getColumnData("T.DRAW_ELO");
            Element draw = CopexUtilities.getElement(s);
            s = rs.getColumnData("T.IS_VISIBLE");
            if (s == null)
                continue;
            int vis = 1;
            try {
                vis = Integer.parseInt(s);
            }catch(NumberFormatException e){
                vis = 1;
            }
            boolean isVisible = (vis == 1);
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
            question = new Question(dbKeyQuestion,locale, name, description,  comments, taskImage, draw, isVisible, new TaskRight(editR, deleteR, copyR, moveR, parentR, drawR, repeatR), true);
        } 
        v.add(question);
        return new CopexReturn();
    }
    
    static public CopexReturn createProcedureInDB(DataBaseCommunication dbC,Locale locale, LearnerProcedure proc, long dbKeyLabDoc, ArrayList v){
        String name = proc.getName(locale) != null ? proc.getName(locale) :"";
        name =  AccesDB.replace("\'",name,"''") ;
        int activ = 0;
        if (proc.isActiv())
            activ = 1;
        char right = proc.getRight();
        // question 
        ArrayList v2 = new ArrayList();
        CopexReturn cr = TaskFromDB.createQuestionInDB(dbC, locale, proc.getQuestion(), v2);
        if (cr.isError()){
           return cr;
        }
        long idQ = (Long)v2.get(0);
        java.sql.Date dateM = proc.getDateLastModification();
        String dM = CopexUtilities.dateToSQL(dateM);
        String query = "INSERT INTO EXPERIMENTAL_PROCEDURE " +
                "(ID_PROC, PROC_NAME, DATE_LAST_MODIFICATION, ACTIV, PROC_RIGHT) " +
                "VALUES (NULL, '"+name+"', '"+dateM+"', "+activ+", '"+right+"' );";
       
         String queryID = "SELECT max(last_insert_id(`ID_PROC`))   FROM EXPERIMENTAL_PROCEDURE ;";  
          v2 = new ArrayList();
          cr = dbC.getNewIdInsertInDB(query, queryID, v2);
          if (cr.isError())
              return cr;
          long dbKey = (Long)v2.get(0);
         // protocole eleve
         String queryLearner = "INSERT INTO LEARNER_PROC (ID_PROC) VALUES " +
                    "("+dbKey+") ;";
         // on cree le lien proc /question
         String queryLinkProcQuestion = "INSERT INTO LINK_PROC_TASK (ID_PROC, ID_TASK, QUESTION) " +
                    "VALUES ("+dbKey+", "+idQ+", 1) ;";
            
         // on cree le lien avec le labdoc
         String queryLink = "INSERT INTO LINK_LABDOC_PROC (ID_LABDOC, ID_PROC) " +
                    "VALUES ("+dbKeyLabDoc+", "+dbKey+") ;";
          // on cree le lien entre proc et proc initial
         long initDbKey = proc.getInitialProc().getDbKey();
         String queryLinkProc = "INSERT INTO LINK_LEARNER_INITIAL_PROC (ID_LEARNER_PROC, ID_INITIAL_PROC) " +
                 "VALUES ("+dbKey+", "+initDbKey+") ;";
          v2 = new ArrayList();
          String[]querys = new String[4];
          querys[0] = queryLearner ;
          querys[1] = queryLinkProcQuestion ;
          querys[2] = queryLink ;
          querys[3] = queryLinkProc ;
          cr = dbC.executeQuery(querys, v2);
          if (cr.isError())
              return cr;
            
            // on ne cree pas la liste des taches associees, cela se fait dans 
            // un second temps
            
            v.add(dbKey);
            v.add(idQ);
            return new CopexReturn();
    }
    
    
   
    
    /* mise a jour du statut activ d'un protocole */
    static public CopexReturn updateActivProcInDB(DataBaseCommunication dbC, long dbKeyProc, boolean activ){
        int a = 0;
        if (activ)
             a =1;
        String query = "UPDATE EXPERIMENTAL_PROCEDURE SET ACTIV = "+a+" "+
                    "WHERE ID_PROC = "+dbKeyProc+" ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }
    
    
    
    
    /* mise a jour de la date de modif d'un protocole */
    static public CopexReturn updateDateProcInDB(DataBaseCommunication dbC, long dbKeyProc, java.sql.Date date){
        String dM = CopexUtilities.dateToSQL(date);
        String query = "UPDATE EXPERIMENTAL_PROCEDURE SET DATE_LAST_MODIFICATION = '"+dM+"' "+
                    "WHERE ID_PROC = "+dbKeyProc+" ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }
         
    
    

    
    /* suppression d'un protocole */
    static public CopexReturn deleteProcedureFromDB(DataBaseCommunication dbC, long dbKeyProc){
        // suppression lien mission
        String queryLink = "DELETE FROM LINK_LABDOC_PROC WHERE " +
                    "ID_PROC = "+dbKeyProc+"  ;";
         // suppression du protocole
         String queryDel = "DELETE FROM EXPERIMENTAL_PROCEDURE WHERE " +
                    "ID_PROC = "+dbKeyProc+" ;";
         String queryLearner = "DELETE FROM LEARNER_PROC WHERE " +
                    "ID_PROC = "+dbKeyProc+" ;";
         String queryLinkP = "DELETE FROM LINK_LEARNER_INITIAL_PROC WHERE ID_LEARNER_PROC = "+dbKeyProc+" ;";
         ArrayList v = new ArrayList();
         String[] querys = new String[4];
         querys[0] = queryLink ;
         querys[1] = queryLinkP ;
         querys[2] = queryDel ;
         querys[3] = queryLearner ;
         CopexReturn cr = dbC.executeQuery(querys, v);
         return cr;
        
    }
    
    
   
    /* mise a jour du nom du protocole */
    static public CopexReturn updateProcNameInDB(DataBaseCommunication dbC, long dbKeyProc, String name) {
        name =  AccesDB.replace("\'",name,"''") ;
        
        String query = "UPDATE EXPERIMENTAL_PROCEDURE SET PROC_NAME = '"+name+"' "+
                    "WHERE ID_PROC = "+dbKeyProc+" ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }
    
    
    /* retourne en v[0] la liste des actions nommees */
    public static  CopexReturn getListNamedActionFromDB(DataBaseCommunication dbC, long dbKeyProc, Locale locale, ArrayList<TypeMaterial> listTypeMaterial, ArrayList<PhysicalQuantity> listPhysicalQuantity,  ArrayList v){
        ArrayList<InitialNamedAction> l = new ArrayList();
        String lib = "LIB_"+locale.getLanguage() ;
        String query = "SELECT A.ID_ACTION_NOMMEE, A.CODE,A.DRAW, A.DEFAULT_DRAW, A.IS_REPEAT, A.IS_SETTING,  A."+lib+" " +
                "FROM INITIAL_ACTION_NOMMEE A, LINK_INITIAL_PROC_ACTION_NOMMEE L " +
                "WHERE L.ID_PROC = "+dbKeyProc +" AND L.ID_ACTION_NOMMEE = A.ID_ACTION_NOMMEE ORDER BY A.CODE ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("A.ID_ACTION_NOMMEE");
        listFields.add("A.CODE");
        listFields.add("A.DRAW");
        listFields.add("A.DEFAULT_DRAW");
        listFields.add("A.IS_REPEAT");
        listFields.add("A.IS_SETTING");
        listFields.add("A."+lib);

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("A.ID_ACTION_NOMMEE");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String code = rs.getColumnData("A.CODE");
            if (code == null)
                continue;
            s = rs.getColumnData("A.DRAW");
            if (s == null)
                continue;
            boolean draw = s.equals("1");
            s = rs.getColumnData("A.DEFAULT_DRAW");
            Element defaultDraw = null;
            if(s != null){
                defaultDraw = CopexUtilities.getElement(s);
            }
            s = rs.getColumnData("A.IS_REPEAT");
            if (s == null)
                continue;
            boolean repeat = s.equals("1");
            s = rs.getColumnData("A.IS_SETTING");
            if (s == null)
                continue;
            int p = 0;
            try{
                p = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            boolean isSetting = (p == 1);
            String libelle = rs.getColumnData("A."+lib);
            if (libelle == null)
                continue;
            InitialActionVariable variable = null ;
            InitialNamedAction action = null;
            if (isSetting){
                ArrayList v3 = new ArrayList();
                cr = getActionVariableFromDB(dbC, dbKey, locale, listTypeMaterial, listPhysicalQuantity,  v3);
                if (cr.isError())
                    return cr;
                variable = (InitialActionVariable)v3.get(0);
                // determine si choix, manip, acq ou treat.
                v3 = new ArrayList();
                cr = getInitialActionFromDB(dbC, locale, dbKey, code, libelle, draw, defaultDraw, repeat, isSetting, variable, listPhysicalQuantity, v3);
                if (cr.isError())
                    return cr;
                action = (InitialNamedAction)v3.get(0);
            }else
                action = new InitialNamedAction(dbKey, code, CopexUtilities.getLocalText(libelle, locale), isSetting, variable, draw, repeat, defaultDraw);
            l.add(action);
        }
        v.add(l);
        return new CopexReturn();
    }

    /* retourne en v[0] la variable de l'action nommee */
    public static CopexReturn getActionVariableFromDB(DataBaseCommunication dbC, long dbKeyAction, Locale locale, ArrayList<TypeMaterial> listTypeMaterial, ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList v){
        int nbPhysQ = listPhysicalQuantity.size();
        InitialActionVariable variable = null;
        String lib = "LIB_"+locale.getLanguage() ;
        String query = "SELECT V.ID_VARIABLE, V.CODE, V.NB_PARAM, V."+lib+" " +
                " FROM INITIAL_ACTION_VARIABLE V, LINK_INITIAL_ACTION_VARIABLE L " +
                "WHERE L.ID_ACTION_NOMMEE = "+dbKeyAction+" AND L.ID_VARIABLE = V.ID_VARIABLE ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("V.ID_VARIABLE");
        listFields.add("V.CODE");
        listFields.add("V.NB_PARAM");
        listFields.add("V."+lib);

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("V.ID_VARIABLE");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String code = rs.getColumnData("V.CODE");
            s = rs.getColumnData("V.NB_PARAM");
            if (s==null)
                continue;
            int nbParam = 0;
            try{
                nbParam = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            String libelle = rs.getColumnData("V."+lib);
            InitialActionParam[] tabParam = new InitialActionParam[nbParam];
            // chargement des parametres
            int id=0;
            String lib2 = "PARAM_NAME_"+locale.getLanguage();
            //     de type data

            String queryParamD = "SELECT D.ID_PARAM, I."+lib2+" FROM INITIAL_PARAM_DATA D, LINK_INITIAL_VARIABLE_PARAM L, INITIAL_ACTION_PARAM I  " +
                    " WHERE L.ID_VARIABLE = "+dbKey+ " AND L.ID_PARAM = D.ID_PARAM AND L.ID_PARAM = I.ID_PARAM ;";
            ArrayList v3 = new ArrayList();
            ArrayList<String> listFields2 = new ArrayList();
            listFields2.add("D.ID_PARAM");
            listFields2.add("I."+lib2);
            cr = dbC.sendQuery(queryParamD, listFields2, v3);
            if (cr.isError())
                return cr;
            int nbR2 = v3.size();
            for (int j=0; j<nbR2; j++){
                ResultSetXML rs2 = (ResultSetXML)v3.get(j);
                s= rs2.getColumnData("D.ID_PARAM");
                long dbKeyParamD = Long.parseLong(s);
                String paramName = rs2.getColumnData("I."+lib2);
                InitialParamData d = new InitialParamData(dbKeyParamD, CopexUtilities.getLocalText(paramName, locale)) ;
                if (id >= tabParam.length){
                    logger.log(Level.SEVERE, ("param data : depassement nbParam"));
                    return new CopexReturn("ERROR LOAD ACTION PARAM", false);
                }
                tabParam[id] = d;
                id++;
            }
            //     de type quantite
            String queryParamQ = "SELECT Q.ID_PARAM, Q.ID_PHYSICAL_QUANTITY, Q.QUANTITY_NAME, I."+lib2+" FROM INITIAL_PARAM_QUANTITY Q, LINK_INITIAL_VARIABLE_PARAM L, INITIAL_ACTION_PARAM I " +
                    " WHERE L.ID_VARIABLE = "+dbKey+ " AND L.ID_PARAM = Q.ID_PARAM AND L.ID_PARAM = I.ID_PARAM ;";
            v3 = new ArrayList();
            listFields2 = new ArrayList();
            listFields2.add("Q.ID_PARAM");
            listFields2.add("Q.ID_PHYSICAL_QUANTITY");
            listFields2.add("Q.QUANTITY_NAME");
            listFields2.add("I."+lib2);
            cr = dbC.sendQuery(queryParamQ, listFields2, v3);
            if (cr.isError())
                return cr;
            nbR2 = v3.size();
            for (int j=0; j<nbR2; j++){
                ResultSetXML rs2 = (ResultSetXML)v3.get(j);
                s= rs2.getColumnData("Q.ID_PARAM");
                long dbKeyParamQ = Long.parseLong(s);
                s = rs2.getColumnData("Q.ID_PHYSICAL_QUANTITY");
                long dbKeyPhyQ = Long.parseLong(s);
                PhysicalQuantity physQ = null;
                for (int k=0; k<nbPhysQ; k++){
                    if(listPhysicalQuantity.get(k).getDbKey() == dbKeyPhyQ){
                        physQ = listPhysicalQuantity.get(k);
                        break;
                    }
                }
                String qName = rs2.getColumnData("Q.QUANTITY_NAME");
                String paramName = rs2.getColumnData("I."+lib2);
                InitialParamQuantity p = new InitialParamQuantity(dbKeyParamQ, CopexUtilities.getLocalText(paramName, locale), physQ, CopexUtilities.getLocalText(qName, locale));
                if (id >= tabParam.length){
                    logger.log(Level.SEVERE, ("param quantity : depassement nbParam"));
                    return new CopexReturn("ERROR LOAD ACTION PARAM", false);
                }
                tabParam[id] = p;
                id++;
            }
            //     de type type de materiel
            v3 = new ArrayList();
            listFields2 = new ArrayList();
            String queryParamM = "SELECT M.ID_PARAM, M.ID_TYPE_MATERIAL, M.ID_TYPE_MATERIAL_2, M.ANDTYPES,  I."+lib2+" FROM INITIAL_PARAM_MATERIAL M, LINK_INITIAL_VARIABLE_PARAM L, INITIAL_ACTION_PARAM I  " +
                    "WHERE L.ID_VARIABLE = "+dbKey +" AND L.ID_PARAM = M.ID_PARAM AND L.ID_PARAM = I.ID_PARAM ;";
            listFields2.add("M.ID_PARAM");
            listFields2.add("M.ID_TYPE_MATERIAL");
            listFields2.add("M.ID_TYPE_MATERIAL_2");
            listFields2.add("M.ANDTYPES");
            listFields2.add("I."+lib2);
            cr = dbC.sendQuery(queryParamM, listFields2, v3);
            if (cr.isError())
                return cr;
            nbR2 = v3.size();
            for (int j=0; j<nbR2; j++){
                ResultSetXML rs2 = (ResultSetXML)v3.get(j);
                s= rs2.getColumnData("M.ID_PARAM");
                long dbKeyParamM = Long.parseLong(s);
                String paramName = rs2.getColumnData("I."+lib2);
                s = rs2.getColumnData("M.ID_TYPE_MATERIAL");
                boolean allType = false;
                TypeMaterial typeMat = null;
                if(s== null || s.length() == 0)
                    allType =true;
                else{
                    try{
                        long dbKeyMat = Long.parseLong(s);
                        typeMat = getTypeMaterial(dbKeyMat, listTypeMaterial);
                        if (typeMat == null){
                            logger.log(Level.SEVERE, ("Erreur sur le type de material "));
                            return new CopexReturn("ERROR LOAD ACTION PARAM", false);
                        }
                    }catch(NumberFormatException e){
                        allType = true;
                    }
                }
                
                s = rs2.getColumnData("M.ID_TYPE_MATERIAL_2");
                TypeMaterial typeMat2 = null;
                if (s == null ||s.equals(""))
                    typeMat2 = null;
                else{
                    long dbKeyMat2 = -1;
                    try{
                        dbKeyMat2 = Long.parseLong(s);
                        typeMat2 = getTypeMaterial(dbKeyMat2, listTypeMaterial);
                    }catch(NumberFormatException e){
                        typeMat2 = null;
                    }
                }
                s = rs2.getColumnData("M.ANDTYPES");
                boolean andTypes = s.equals("1");
                // chargement des liens entre parametres
                String queryLink = "SELECT ID_PARAM_QUANTITY FROM LINK_INITIAL_PARAM_QUANTITY_MATERIAL WHERE ID_PARAM_MATERIAL = "+dbKeyParamM+" ;";
                ArrayList v4 = new ArrayList();
                ArrayList<String> listFields3 = new ArrayList();
                listFields3.add("ID_PARAM_QUANTITY");
                cr = dbC.sendQuery(queryLink, listFields3, v4);
                if (cr.isError())
                    return cr;
                int nbR3 = v4.size();
                InitialParamQuantity paramQ = null;
                for (int k=0; k<nbR3; k++){
                    ResultSetXML rs3 = (ResultSetXML)v4.get(k);
                    s = rs3.getColumnData("ID_PARAM_QUANTITY");
                    if (s==null)
                        continue;
                    long dbKeyPQuant = Long.parseLong(s);
                    paramQ = getInitialParamQuantity(dbKeyPQuant, tabParam);
                    if (paramQ == null){
                        logger.log(Level.SEVERE, ("Erreur sur les liens entre param qtt et mat"));
                        return new CopexReturn("ERROR LOAD ACTION PARAM", false);
                    }
                }
                InitialParamMaterial p = new InitialParamMaterial(dbKeyParamM, CopexUtilities.getLocalText(paramName, locale), typeMat,typeMat2, andTypes,paramQ, allType);
                if (id >= tabParam.length){
                    logger.log(Level.SEVERE, ("param material : depassement nbParam"));
                    return new CopexReturn("ERROR LOAD ACTION PARAM", false);
                }
                tabParam[id] = p;
                id++;
            }
            // variable
            variable = new InitialActionVariable(dbKey, locale,code, nbParam, CopexUtilities.getLocalText(libelle, locale), tabParam) ;
        }

        v.add(variable);
        return new CopexReturn();
    }


    /* retourne le type de materiel correspodant a l'identifiant */
    private static TypeMaterial getTypeMaterial(long dbKey, ArrayList<TypeMaterial> listTypeMaterial){
        int nb = listTypeMaterial.size();
        for (int i=0; i<nb; i++){
            if (listTypeMaterial.get(i).getDbKey() == dbKey)
                return listTypeMaterial.get(i);
        }
        return null;
    }

    /* retourne le parametre quantity */
    private static InitialParamQuantity getInitialParamQuantity(long dbKey, InitialActionParam[] tabParam){
        for (int i=0; i<tabParam.length; i++){
            if (tabParam[i] != null && tabParam[i] instanceof InitialParamQuantity && tabParam[i].getDbKey() == dbKey)
                return (InitialParamQuantity)tabParam[i];
        }
        return null;
    }

    /* retourne en v[0] l'action de type choix, manip acq ou treat */
    private static CopexReturn getInitialActionFromDB(DataBaseCommunication dbC, Locale locale, long dbKey, String code,String libelle,boolean draw, Element defaultDraw, boolean repeat,  boolean isSetting, InitialActionVariable variable, ArrayList<PhysicalQuantity> listPhysicalQuantity,ArrayList v){
        InitialNamedAction action = null;
        // action choix?
        ArrayList v2 = new ArrayList();
        String queryChoice = "SELECT ID_ACTION FROM INITIAL_ACTION_CHOICE WHERE ID_ACTION = "+dbKey+" ;";
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_ACTION");
        CopexReturn cr = dbC.sendQuery(queryChoice, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs2 = (ResultSetXML)v2.get(i);
            String s = rs2.getColumnData("ID_ACTION");
            if (s == null)
                continue;
            action = new InitialActionChoice(dbKey, code, CopexUtilities.getLocalText(libelle, locale), isSetting, variable, draw, repeat, defaultDraw) ;
            v.add(action);
            return new CopexReturn();
        }
        // action manipulation ?
        v2 = new ArrayList();
        String queryManip = "SELECT ID_ACTION, NB_MATERIAL_PROD FROM INITIAL_ACTION_MANIPULATION WHERE ID_ACTION = "+dbKey+" ;";
        listFields = new ArrayList();
        listFields.add("ID_ACTION");
        listFields.add("NB_MATERIAL_PROD");
        cr = dbC.sendQuery(queryManip, listFields, v2);
        if (cr.isError())
            return cr;
        nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs2 = (ResultSetXML)v2.get(i);
            String s = rs2.getColumnData("ID_ACTION");
            if (s == null)
                continue;
            s = rs2.getColumnData("NB_MATERIAL_PROD");
            if (s == null)
                continue;
            int nbMaterialProd = 0;
            try{
                nbMaterialProd = Integer.parseInt(s);
            }catch(NumberFormatException e){
            }
            ArrayList v3 = new ArrayList();
            cr = getInitialManipulationOutputFromDB(dbC, locale, dbKey, v3);
            if (cr.isError())
                return cr;
            ArrayList<InitialManipulationOutput> listOutput = (ArrayList<InitialManipulationOutput>)v3.get(0);
            action = new InitialActionManipulation(dbKey, code, CopexUtilities.getLocalText(libelle, locale), isSetting, variable, draw, repeat, nbMaterialProd, listOutput, defaultDraw);
            v.add(action);
            return new CopexReturn();
         }
        // action acquisition ?
        v2 = new ArrayList();
        String queryAcq = "SELECT ID_ACTION, NB_DATA_PROD FROM INITIAL_ACTION_ACQUISITION WHERE ID_ACTION = "+dbKey+" ;";
        listFields = new ArrayList();
        listFields.add("ID_ACTION");
        listFields.add("NB_DATA_PROD");
        cr = dbC.sendQuery(queryAcq, listFields, v2);
        if (cr.isError())
            return cr;
        nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs2 = (ResultSetXML)v2.get(i);
            String s = rs2.getColumnData("ID_ACTION");
            if (s == null)
                continue;
            s = rs2.getColumnData("NB_DATA_PROD");
            if (s == null)
                continue;
            int nbDataProd = 0;
            try{
                nbDataProd = Integer.parseInt(s);
            }catch(NumberFormatException e){
            }
            ArrayList v3 = new ArrayList();
            cr = getInitialAcquisitionOutputFromDB(dbC, locale, dbKey, nbDataProd, listPhysicalQuantity, v3);
            if (cr.isError())
                return cr;
            ArrayList<InitialAcquisitionOutput> listOutput = (ArrayList<InitialAcquisitionOutput>)v3.get(0);
            action = new InitialActionAcquisition(dbKey, code, CopexUtilities.getLocalText(libelle, locale), isSetting, variable, draw, repeat, nbDataProd, listOutput, defaultDraw);
            v.add(action);
            return new CopexReturn();
         }
        // action treatment ?
        v2 = new ArrayList();
        String queryTreat = "SELECT ID_ACTION, NB_DATA_PROD FROM INITIAL_ACTION_TREATMENT WHERE ID_ACTION = "+dbKey+" ;";
        listFields = new ArrayList();
        listFields.add("ID_ACTION");
        listFields.add("NB_DATA_PROD");
        cr = dbC.sendQuery(queryTreat, listFields, v2);
        if (cr.isError())
            return cr;
        nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs2 = (ResultSetXML)v2.get(i);
            String s = rs2.getColumnData("ID_ACTION");
            if (s == null)
                continue;
            s = rs2.getColumnData("NB_DATA_PROD");
            if (s == null)
                continue;
            int nbDataProd = 0;
            try{
                nbDataProd = Integer.parseInt(s);
            }catch(NumberFormatException e){
            }
            ArrayList v3 = new ArrayList();
            cr = getInitialTreatmentOutputFromDB(dbC, locale, dbKey, nbDataProd, listPhysicalQuantity, v3);
            if (cr.isError())
                return cr;
            ArrayList<InitialTreatmentOutput> listOutput = (ArrayList<InitialTreatmentOutput>)v3.get(0);
            action = new InitialActionTreatment(dbKey, code, CopexUtilities.getLocalText(libelle, locale), isSetting, variable, draw, repeat, nbDataProd, listOutput, defaultDraw);
            v.add(action);
            return new CopexReturn();
         }
        
        return new CopexReturn("ERROR LOADING ACTION "+dbKey, false);
    }

    /*retourne en v[0] la liste des output d'une action manipulation */
    private static CopexReturn getInitialManipulationOutputFromDB(DataBaseCommunication dbC, Locale locale, long dbKeyAction, ArrayList v){
        ArrayList<InitialManipulationOutput> list = new ArrayList();
        String lib = "NAME_"+locale.getLanguage();
        String query = "SELECT O.ID_OUTPUT, O.TEXT_PROD, O."+lib+" FROM  INITIAL_MANIPULATION_OUTPUT O, LINK_INITIAL_MANIPULATION_OUTPUT L " +
                "WHERE L.ID_ACTION = "+dbKeyAction+" AND L.ID_OUTPUT = O.ID_OUTPUT  ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("O.ID_OUTPUT");
        listFields.add("O.TEXT_PROD");
        listFields.add("O."+lib);
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("O.ID_OUTPUT");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("O."+lib);
            String textprod = rs.getColumnData("O.TEXT_PROD");
            ArrayList v3 = new ArrayList();
            cr = getInitialTypeMaterialManipulationFromDB(dbC,locale,  dbKeyAction, v3);
            if (cr.isError())
                return cr;
            ArrayList<TypeMaterial> typeMaterialProd = (ArrayList<TypeMaterial>)v3.get(0);
            InitialManipulationOutput out = new InitialManipulationOutput(dbKey, CopexUtilities.getLocalText(textprod, locale), CopexUtilities.getLocalText(name, locale), typeMaterialProd);
            list.add(out);
        }
        v.add(list);
        return new CopexReturn();
    }


    /* retourne en v[0] la liste du type de  material prod par une tache de manipulation */
    private static CopexReturn getInitialTypeMaterialManipulationFromDB(DataBaseCommunication dbC,Locale locale,  long dbKeyAction,  ArrayList v){
        ArrayList<TypeMaterial> typeMaterialProd = new ArrayList();
        String query = "SELECT DISTINCT T.ID_TYPE, T.TYPE_NAME " +
                "FROM MATERIAL_TYPE T, INITIAL_TYPE_MATERIAL_MANIPULATION M, LINK_INITIAL_MANIPULATION_MATERIAL L, INITIAL_MANIPULATION_OUTPUT O, LINK_INITIAL_MANIPULATION_OUTPUT L2 " +
                "WHERE T.ID_TYPE = M.ID_TYPE AND " +
                "M.ID_INITIAL_TYPE_MATERIAL_MANIPULATION = L.ID_INITIAL_TYPE_MATERIAL AND " +
                "L.ID_OUTPUT = L2.ID_OUTPUT AND " +
                "L2.ID_ACTION = "+dbKeyAction+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("T.ID_TYPE");
        listFields.add("T.TYPE_NAME");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("T.ID_TYPE");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("T.TYPE_NAME");
            TypeMaterial type = new TypeMaterial(dbKey, CopexUtilities.getLocalText(name, locale));
            typeMaterialProd.add(type);
        }
        v.add(typeMaterialProd);
        return new CopexReturn();
    }

    /*retourne en v[0] la liste des output d'une action acquisition */
    private static CopexReturn getInitialAcquisitionOutputFromDB(DataBaseCommunication dbC, Locale locale, long dbKeyAction, int nbDataProd, ArrayList<PhysicalQuantity> listPhysicalQuantity,ArrayList v){
        ArrayList<InitialAcquisitionOutput> list = new ArrayList();
        String lib = "NAME_"+locale.getLanguage();
        String query = "SELECT O.ID_OUTPUT, O.TEXT_PROD, O."+lib+" FROM  INITIAL_ACQUISITION_OUTPUT O, LINK_INITIAL_ACQUISITION_OUTPUT L " +
                "WHERE L.ID_ACTION = "+dbKeyAction+" AND L.ID_OUTPUT = O.ID_OUTPUT  ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("O.ID_OUTPUT");
        listFields.add("O.TEXT_PROD");
        listFields.add("O."+lib);
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("O.ID_OUTPUT");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("O."+lib);
            String textprod = rs.getColumnData("O.TEXT_PROD");
            ArrayList v3 = new ArrayList();
            cr = getInitialUnitDataAcquisitionFromDB(dbC, dbKey, nbDataProd,listPhysicalQuantity, v3);
            if (cr.isError())
                return cr;
            CopexUnit[] unitDataProd = (CopexUnit[])v3.get(0);
            InitialAcquisitionOutput out = new InitialAcquisitionOutput(dbKey, CopexUtilities.getLocalText(textprod, locale), CopexUtilities.getLocalText(name, locale), unitDataProd);
            list.add(out);
        }
        v.add(list);
        return new CopexReturn();
    }

    /* retourne en v[0] la liste des unites des data prod par une tache d'acquisition */
    private static CopexReturn getInitialUnitDataAcquisitionFromDB(DataBaseCommunication dbC, long dbKeyOutput, int nbDataProd, ArrayList<PhysicalQuantity> listPhysicalQuantity,ArrayList v){
        int nbPhysQ = listPhysicalQuantity.size();
        CopexUnit[] unitDataProd = new CopexUnit[nbDataProd];
        String query = "SELECT I.ID_UNIT  FROM INITIAL_UNIT_DATA_ACQUISITION I, LINK_INITIAL_ACQUISITION_DATA  L  " +
                "WHERE I.ID_INITIAL_UNIT_DATA_ACQUISITION = L.ID_INITIAL_UNIT AND L.ID_OUTPUT = "+dbKeyOutput+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("I.ID_UNIT");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("I.ID_UNIT");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            CopexUnit unit = null;
            for (int j=0; j<nbPhysQ; j++){
                PhysicalQuantity phQ = listPhysicalQuantity.get(j);
                CopexUnit u = phQ.getUnit(dbKey) ;
                if (u != null){
                    unit = u;
                    break;
                }
            }
            if (i<nbDataProd)
                unitDataProd[i] = unit;
        }
        v.add(unitDataProd);
        return new CopexReturn();
    }

    /*retourne en v[0] la liste des output d'une action treatment */
    private static CopexReturn getInitialTreatmentOutputFromDB(DataBaseCommunication dbC, Locale locale, long dbKeyAction, int nbDataProd, ArrayList<PhysicalQuantity> listPhysicalQuantity,ArrayList v){
        ArrayList<InitialTreatmentOutput> list = new ArrayList();
        String lib = "NAME_"+locale.getLanguage();
        String query = "SELECT O.ID_OUTPUT, O.TEXT_PROD, O."+lib+" FROM  INITIAL_TREATMENT_OUTPUT O, LINK_INITIAL_TREATMENT_OUTPUT L " +
                "WHERE L.ID_ACTION = "+dbKeyAction+" AND L.ID_OUTPUT = O.ID_OUTPUT  ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("O.ID_OUTPUT");
        listFields.add("O.TEXT_PROD");
        listFields.add("O."+lib);
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("O.ID_OUTPUT");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("O."+lib);
            String textprod = rs.getColumnData("O.TEXT_PROD");
            ArrayList v3 = new ArrayList();
            cr = getInitialUnitDataTreatmentFromDB(dbC, dbKey, nbDataProd,listPhysicalQuantity, v3);
            if (cr.isError())
                return cr;
            CopexUnit[] unitDataProd = (CopexUnit[])v3.get(0);
            InitialTreatmentOutput out = new InitialTreatmentOutput(dbKey, CopexUtilities.getLocalText(textprod, locale), CopexUtilities.getLocalText(name, locale), unitDataProd);
            list.add(out);
        }
        v.add(list);
        return new CopexReturn();
    }


    /* retourne en v[0] la liste des unites des data prod par une tache de treatment */
    private static CopexReturn getInitialUnitDataTreatmentFromDB(DataBaseCommunication dbC, long dbKeyOutput, int nbDataProd, ArrayList<PhysicalQuantity> listPhysicalQuantity,ArrayList v){
        int nbPhysQ = listPhysicalQuantity.size();
        CopexUnit[] unitDataProd = new CopexUnit[nbDataProd];
        String query = "SELECT I.ID_UNIT  FROM INITIAL_UNIT_DATA_TREATMENT I, LINK_INITIAL_TREATMENT_DATA  L  " +
                "WHERE I.ID_INITIAL_UNIT_DATA_TREATMENT = L.ID_INITIAL_DATA AND L.ID_OUTPUT = "+dbKeyOutput+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("I.ID_UNIT");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("I.ID_UNIT");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            CopexUnit unit = null;
            for (int j=0; j<nbPhysQ; j++){
                PhysicalQuantity phQ = listPhysicalQuantity.get(j);
                CopexUnit u = phQ.getUnit(dbKey) ;
                if (u != null){
                    unit = u;
                    break;
                }
            }
            if (i<nbDataProd)
                unitDataProd[i] = unit;
        }
        v.add(unitDataProd);
        return new CopexReturn();
    }

    /* chargement proc initial */
    public static CopexReturn getInitialProcFromDB(DataBaseCommunication dbC, Locale locale,  long dbKeyMission, ArrayList<Long> listIdInitProc,  ArrayList<PhysicalQuantity> listPhysicalQuantity,ArrayList<MaterialStrategy> listMaterialStrategy, ArrayList v){
        ArrayList<InitialProcedure> listInitProc = new ArrayList();
        // chargement du protocole initial
        String list = "("+listIdInitProc.get(0);
        int nbI = listIdInitProc.size();
        for (int i=1; i<nbI; i++){
            list += ","+listIdInitProc.get(i);
        }
        list += ")";
        String queryInit = "SELECT E.ID_PROC, E.PROC_NAME, E.DATE_LAST_MODIFICATION, E.ACTIV, E.PROC_RIGHT, " +
                "I.IS_FREE_ACTION,I.IS_TASK, I.IS_TASK_REPEAT, I.CODE_PROC, I.HYPOTHESIS_MODE, I.PRINCIPLE_MODE, I.DRAW_PRINCIPLE, I.EVALUATION_MODE, I.IS_TASK_DRAW " +
           " FROM EXPERIMENTAL_PROCEDURE E, LINK_MISSION_INITIAL_PROC L, INITIAL_PROC I " +
                   "WHERE L.ID_MISSION = "+dbKeyMission+ " AND "+
                   "L.ID_INIT_PROC IN "+list+" AND "+
                   "L.ID_INIT_PROC  = E.ID_PROC AND L.ID_INIT_PROC = I.ID_PROC  " +
                   " ORDER BY I.CODE_PROC  ;";

        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("E.ID_PROC");
        listFields.add("E.PROC_NAME");
        listFields.add("E.DATE_LAST_MODIFICATION");
        listFields.add("E.ACTIV");
        listFields.add("E.PROC_RIGHT");
        listFields.add("I.IS_FREE_ACTION");
        listFields.add("I.IS_TASK");
        listFields.add("I.IS_TASK_REPEAT");
        listFields.add("I.CODE_PROC");
        listFields.add("I.HYPOTHESIS_MODE");
        listFields.add("I.PRINCIPLE_MODE");
        listFields.add("I.DRAW_PRINCIPLE");
        listFields.add("I.EVALUATION_MODE");
        listFields.add("I.IS_TASK_DRAW");

        CopexReturn cr = dbC.sendQuery(queryInit, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("E.ID_PROC");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("E.PROC_NAME");
            if (name == null)
                continue;
            Date dateLastModif = null;
            s = rs.getColumnData("E.DATE_LAST_MODIFICATION");
            if (s == null)
                continue;
            dateLastModif = CopexUtilities.getDate(s);
            s = rs.getColumnData("E.ACTIV");
            if (s == null)
                continue;
            int activ = 0;
            try{
                activ = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            boolean isActiv = (activ == 1);
            s = rs.getColumnData("E.PROC_RIGHT");
            if (s == null)
                continue;
            char r = s.charAt(0);
            s = rs.getColumnData("I.IS_FREE_ACTION");
            if (s == null)
                continue;
            int f = 0;
            try{
                f = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            boolean isFreeAction = (f == 1);
            s = rs.getColumnData("I.IS_TASK");
            if (s == null)
                continue;
            int t = 0;
            try{
                t = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            boolean isTaskMode = (t == 1);
            s = rs.getColumnData("I.IS_TASK_REPEAT");
            if (s == null)
                continue;
            f = 0;
            try{
                f = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            boolean isTaskRepeat = (f == 1);
            String code = rs.getColumnData("I.CODE_PROC");
            s = rs.getColumnData("I.HYPOTHESIS_MODE");
            char hypothesisMode = s.charAt(0);
            s = rs.getColumnData("I.PRINCIPLE_MODE");
            char principleMode = s.charAt(0);
            s = rs.getColumnData("I.DRAW_PRINCIPLE");
            int d = 0;
            try{
                d = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            boolean drawPrinciple = (d == 1);
            s = rs.getColumnData("I.EVALUATION_MODE");
            char evaluationMode = s.charAt(0);

            // strategy material
            ArrayList v3 = new ArrayList();
            cr = getInitialMaterialStrategyFromDB(dbC, dbKey, listMaterialStrategy, v3);
            if(cr.isError())
                return cr;
            MaterialStrategy materialStrategy = (MaterialStrategy)v3.get(0);
            // material
            v3 = new ArrayList();
            cr = getInitialProcMaterialFromDB(dbC, locale, dbKey, listPhysicalQuantity, v3 );
            if (cr.isError())
                return cr;
            ArrayList<Material> listMaterial = (ArrayList<Material>)v3.get(0);
            ArrayList<TypeMaterial> listTypeMaterial = (ArrayList<TypeMaterial>)v3.get(1);
            // recuperation actions nommees
            v3 = new ArrayList();
            cr = getListNamedActionFromDB(dbC, dbKey, locale, listTypeMaterial, listPhysicalQuantity, v3);
            if (cr.isError())
                return cr;
            ArrayList<InitialNamedAction> listAction = (ArrayList<InitialNamedAction>)v3.get(0);
            s = rs.getColumnData("I.IS_TASK_DRAW");
            if (s == null)
                continue;
            f = 0;
            try{
                f = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            boolean isTaskDraw = (f == 1);
            // initial proc
            InitialProcedure initProc = new InitialProcedure(dbKey, CopexUtilities.getLocalText(name, locale), dateLastModif, isActiv, r, code, isFreeAction,isTaskMode, isTaskRepeat,  listAction, hypothesisMode, principleMode, drawPrinciple, evaluationMode, materialStrategy, isTaskDraw);
            
            initProc.setListMaterial(listMaterial);
                    
            v3 = new ArrayList();
            Profiler.start("xml_loadQuestionInitial");
            cr = getQuestionProcFromDB(dbC,locale, dbKey, v3);
            if (cr.isError())
                return cr;
            Question question = (Question)v3.get(0);
            initProc.setQuestion(question);
            Profiler.end("xml_loadQuestionInitial");
            // recuperation de l'arbre des taches
            Profiler.start("xml_loadTasksInitial");
            v3 = new ArrayList();
            cr = TaskFromDB.getAllTaskFromDB(dbC, locale, dbKey, question.getDbKey(), listAction, listMaterial, listPhysicalQuantity, v3);
            if (cr.isError())
                return cr;
            ArrayList<CopexTask> listTask = (ArrayList<CopexTask>)v3.get(0);
            initProc.setListTask(listTask);
            Profiler.end("xml_loadTasksInitial");
            // met le lien du fils a la question
            int ts = listTask.size();
            for (int k=0; k<ts; k++){
                if (listTask.get(k).getDbKey() == question.getDbKey()){
                    question.setDbKeyChild(listTask.get(k).getDbKeyChild());
                }
            }
            // recuperation de l'hypothese
            v3 = new ArrayList();
            cr = getHypothesisFromDB(dbC, locale,initProc, v3);
            if(cr.isError())
                return cr;
            Hypothesis hypothesis = null;
            if(v3.size()>0 && v3.get(0) != null)
                hypothesis = (Hypothesis)v3.get(0);
            initProc.setHypothesis(hypothesis);
            // general principle
            v3 = new ArrayList();
            cr = getGeneralPrincipleFromDB(dbC, locale,initProc, v3);
            if(cr.isError())
                return cr;
            GeneralPrinciple principle = null;
            if(v3.size()>0 && v3.get(0) != null)
                principle = (GeneralPrinciple)v3.get(0);
            initProc.setGeneralPrinciple(principle);
            // recuperation de l'evaluation
            v3 = new ArrayList();
            cr = getEvaluationFromDB(dbC, locale,initProc, v3);
            if(cr.isError())
                return cr;
            Evaluation evaluation = null;
            if(v3.size()>0 && v3.get(0) != null)
                evaluation = (Evaluation)v3.get(0);
            initProc.setEvaluation(evaluation);
            listInitProc.add(initProc);
        }
        v.add(listInitProc);
        return new CopexReturn();

    }
    /* chargement d'un protocoles lie a une mission
     * MBO le 27/02/09 : proc lockes
     en v[0] : le proc, null so locke et que l'on doit faire le controle
     */
    public static CopexReturn getProcMissionFromDB(DataBaseCommunication dbC, Locker locker, boolean controlLock, Locale locale,  long dbKeyMission, long dbKeyUser, long dbKeyProc,  long dbKeyInitProc, ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList<MaterialStrategy> listMaterialStrategy, ArrayList v) {
        LearnerProcedure proc  = null;
        ArrayList v2 = new ArrayList();
        ArrayList<Long> listIdInitProc = new ArrayList();
        listIdInitProc.add(dbKeyInitProc);
        CopexReturn cr = getInitialProcFromDB(dbC, locale,  dbKeyMission, listIdInitProc, listPhysicalQuantity, listMaterialStrategy, v2);
        if (cr.isError())
            return cr;
        ArrayList<InitialProcedure> listInitProc = (ArrayList<InitialProcedure>)v2.get(0);

        String query = "SELECT PROC_NAME, DATE_LAST_MODIFICATION, ACTIV, PROC_RIGHT " +
                " FROM EXPERIMENTAL_PROCEDURE WHERE ID_PROC = "+dbKeyProc+" ; ";

        v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("PROC_NAME");
        listFields.add("DATE_LAST_MODIFICATION");
        listFields.add("ACTIV");
        listFields.add("PROC_RIGHT");
        cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String name = rs.getColumnData("PROC_NAME");
            if (name == null)
                continue;
            Date dateLastModif = null;
            String s = rs.getColumnData("DATE_LAST_MODIFICATION");
            if (s == null)
                continue;
            dateLastModif = CopexUtilities.getDate(s);
            s = rs.getColumnData("ACTIV");
            if (s == null)
                continue;
            int activ = 0;
            try{
                activ = Integer.parseInt(s);
            }catch(NumberFormatException e){
                activ = 0;
            }
            boolean isActiv = (activ == 1);
            s = rs.getColumnData("PROC_RIGHT");
            if (s == null)
                continue;
            char r = s.charAt(0);

            // MBO proc lockes
            if (controlLock && locker.isLocked(dbKeyProc)){
                v.add(proc);
                return new CopexReturn();
            }else{
                // proc initial correspondant ?
                ArrayList v3 = new ArrayList();
                cr = getCorrespondingProcInitialFromDB(dbC, dbKeyProc, listInitProc, v3);
                if (cr.isError())
                    return cr;
                InitialProcedure initProcCorresp = (InitialProcedure)v3.get(0);
                ArrayList<Material> listMaterial = initProcCorresp.getListMaterial();
                // recuperation materiel utilise
                v3 = new ArrayList();
                cr = getMaterialUsedFromDB(dbC, locale,dbKeyProc, listMaterial,listPhysicalQuantity,  v3);
                if(cr.isError())
                    return cr;
                ArrayList<MaterialUsed> listMaterialUsed = (ArrayList<MaterialUsed>)v3.get(0);
                proc = new LearnerProcedure(dbKeyProc, CopexUtilities.getLocalText(name, locale), dateLastModif, isActiv, r, initProcCorresp, listMaterialUsed);
                // recuperation de la question
                v3 = new ArrayList();
                cr = getQuestionProcFromDB(dbC, locale,dbKeyProc, v3);
                if (cr.isError())
                    return cr;
                Question question = (Question)v3.get(0);
                proc.setQuestion(question);
                // recuperation de l'arbre des taches
                v3 = new ArrayList();
                cr = TaskFromDB.getAllTaskFromDB(dbC, locale, dbKeyProc, question.getDbKey(), initProcCorresp.getListNamedAction(), listMaterial, listPhysicalQuantity, v3);
                if (cr.isError())
                    return cr;
                ArrayList<CopexTask> listTask = (ArrayList<CopexTask>)v3.get(0);
                proc.setListTask(listTask);
                // met le lien du fils a la question
                int ts = listTask.size();
                for (int k=0; k<ts; k++){
                    if (listTask.get(k).getDbKey() == question.getDbKey()){
                        question.setDbKeyChild(listTask.get(k).getDbKeyChild());
                    }
                }
                // recuperation de l'hypothese
                v3 = new ArrayList();
                cr = getHypothesisFromDB(dbC, locale,proc, v3);
                if(cr.isError())
                    return cr;
                Hypothesis hypothesis = null;
                if(v3.size()>0 && v3.get(0) != null)
                    hypothesis = (Hypothesis)v3.get(0);
                proc.setHypothesis(hypothesis);
                // general principle
                v3 = new ArrayList();
                cr = getGeneralPrincipleFromDB(dbC, locale,proc, v3);
                if(cr.isError())
                    return cr;
                GeneralPrinciple principle = null;
                if(v3.size()>0 && v3.get(0) != null)
                    principle = (GeneralPrinciple)v3.get(0);
                proc.setGeneralPrinciple(principle);
                // recuperation de l'evaluation
                v3 = new ArrayList();
                cr = getEvaluationFromDB(dbC, locale,proc, v3);
                if(cr.isError())
                    return cr;
                Evaluation evaluation = null;
                if(v3.size()>0 && v3.get(0) != null)
                    evaluation = (Evaluation)v3.get(0);
                proc.setEvaluation(evaluation);
            }
        }

        v.add(proc);
        return new CopexReturn();
    }

    /* chargement du materiel associe a un proc initial :
     * - en v[0] : la liste du materiel
     * - en v[1] : la liste du type de materiel
     */
    public static CopexReturn getInitialProcMaterialFromDB(DataBaseCommunication dbC,Locale locale, long dbKey,  ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList v){
        // recuperation des types de materiel
        ArrayList v2 = new ArrayList();
       CopexReturn cr = getAllTypeMaterialFromDB(dbC, locale,v2);
       if (cr.isError())
           return cr;
       ArrayList<TypeMaterial> listTypeMat = (ArrayList<TypeMaterial>)v2.get(0);
       ArrayList<Material> listM = new ArrayList();

        String query = "SELECT M.ID_MATERIAL, M.MATERIAL_NAME, M.DESCRIPTION, M.URL_DESCRIPTION FROM " +
               "MATERIAL M, LINK_INIT_PROC_MATERIAL M2 WHERE " +
               "M2.ID_PROC = "+dbKey+" AND M2.ID_MATERIAL = M.ID_MATERIAL ORDER BY M.MATERIAL_NAME  ;" ;

        ArrayList v3 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("M.ID_MATERIAL");
        listFields.add("M.MATERIAL_NAME");
        listFields.add("M.DESCRIPTION");
        listFields.add("M.URL_DESCRIPTION");

        cr = dbC.sendQuery(query, listFields, v3);
        if (cr.isError())
            return cr;
        int nbR = v3.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v3.get(i);
            String s = rs.getColumnData("M.ID_MATERIAL");
            if (s == null)
                continue;
            long idMat = Long.parseLong(s);
            String matName = rs.getColumnData("M.MATERIAL_NAME");
            if (matName == null)
                continue;
            String description = rs.getColumnData("M.DESCRIPTION");
            if (description == null)
                continue;
            String urlDescription = rs.getColumnData("M.URL_DESCRIPTION");
            ArrayList v4 = new ArrayList();
            cr  = getMaterialSource(dbC, idMat, v4);
            if(cr.isError())
                return cr;
            MaterialSource materialSource = (MaterialSource)v4.get(0);
            Material m = new Material(idMat, CopexUtilities.getLocalText(matName, locale), CopexUtilities.getLocalText(description, locale), urlDescription, materialSource);
            // on recupere les parametres
            v4 = new ArrayList();
            cr = getMaterialParametersFromDB(dbC, locale,idMat, listPhysicalQuantity, v4);
            if (cr.isError())
                return cr;

            ArrayList<Parameter> listP = (ArrayList<Parameter>)v4.get(0);
            if (listP != null)
                m.setListParameters(listP);
            // on associe le type de materiel au materiel

            ArrayList<TypeMaterial> listT = new ArrayList();
            String query2 = "SELECT ID_TYPE FROM LINK_TYPE_MATERIAL WHERE " +
                       "ID_MATERIAL = "+idMat+" ;";

            v4 = new ArrayList();
            ArrayList<String> listFields2 = new ArrayList();
            listFields2.add("ID_TYPE");

            cr = dbC.sendQuery(query2, listFields2, v4);
            if (cr.isError())
                return cr;
            int nbR2 = v4.size();
            for (int j=0; j<nbR2; j++){
                ResultSetXML rs2 = (ResultSetXML)v4.get(j);
                s = rs2.getColumnData("ID_TYPE");
                if (s == null)
                    continue;
                long idT = Long.parseLong(s);
                TypeMaterial t = TypeMaterial.getTypeMaterial(listTypeMat, idT);
                if (t != null)
                    listT.add(t);
            }

            m.setListType(listT);
            // ajoute a la liste
            listM.add(m);
        }

        v.add(listM);
        v.add(listTypeMat);
        return new CopexReturn();
    }

    /* chargement du materiel associe a un proc  :
     * - en v[0] : la liste du materiel
     */
    public static CopexReturn getProcMaterialFromDB(DataBaseCommunication dbC,Locale locale, long dbKey,  ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList v){
        // recuperation des types de materiel
        ArrayList v2 = new ArrayList();
       CopexReturn cr = getAllTypeMaterialFromDB(dbC, locale,v2);
       if (cr.isError())
           return cr;
       ArrayList<TypeMaterial> listTypeMat = (ArrayList<TypeMaterial>)v2.get(0);
       ArrayList<Material> listM = new ArrayList();

        String query = "SELECT M.ID_MATERIAL, M.MATERIAL_NAME, M.DESCRIPTION, M.URL_DESCRIPTION FROM " +
               "MATERIAL M, MATERIAL_USED M2 WHERE " +
               "M2.ID_PROC = "+dbKey+" AND M2.ID_MATERIAL = M.ID_MATERIAL ORDER BY M.MATERIAL_NAME  ;" ;

        ArrayList v3 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("M.ID_MATERIAL");
        listFields.add("M.MATERIAL_NAME");
        listFields.add("M.DESCRIPTION");
        listFields.add("M.URL_DESCRIPTION");

        cr = dbC.sendQuery(query, listFields, v3);
        if (cr.isError())
            return cr;
        int nbR = v3.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v3.get(i);
            String s = rs.getColumnData("M.ID_MATERIAL");
            if (s == null)
                continue;
            long idMat = Long.parseLong(s);
            String matName = rs.getColumnData("M.MATERIAL_NAME");
            if (matName == null)
                continue;
            String description = rs.getColumnData("M.DESCRIPTION");
            if (description == null)
                continue;
            String urlDescription = rs.getColumnData("M.URL_DESCRIPTION");

            ArrayList v4 = new ArrayList();
            cr  = getMaterialSource(dbC, idMat, v4);
            if(cr.isError())
                return cr;
            MaterialSource materialSource = (MaterialSource)v4.get(0);
            Material m = new Material(idMat, CopexUtilities.getLocalText(matName, locale), CopexUtilities.getLocalText(description, locale), urlDescription, materialSource);
            // on recupere les parametres
            v4 = new ArrayList();
            cr = getMaterialParametersFromDB(dbC, locale,idMat, listPhysicalQuantity, v4);
            if (cr.isError())
                return cr;

            ArrayList<Parameter> listP = (ArrayList<Parameter>)v4.get(0);
            if (listP != null)
                m.setListParameters(listP);
            // on associe le type de materiel au materiel

            ArrayList<TypeMaterial> listT = new ArrayList();
            String query2 = "SELECT ID_TYPE FROM LINK_TYPE_MATERIAL WHERE " +
                       "ID_MATERIAL = "+idMat+" ;";

            v4 = new ArrayList();
            ArrayList<String> listFields2 = new ArrayList();
            listFields2.add("ID_TYPE");

            cr = dbC.sendQuery(query2, listFields2, v4);
            if (cr.isError())
                return cr;
            int nbR2 = v4.size();
            for (int j=0; j<nbR2; j++){
                ResultSetXML rs2 = (ResultSetXML)v4.get(j);
                s = rs2.getColumnData("ID_TYPE");
                if (s == null)
                    continue;
                long idT = Long.parseLong(s);
                TypeMaterial t = TypeMaterial.getTypeMaterial(listTypeMat, idT);
                if (t != null)
                    listT.add(t);
            }

            m.setListType(listT);
            // ajoute a la liste
            listM.add(m);
        }

        v.add(listM);
        v.add(listTypeMat);
        return new CopexReturn();
    }

     /* charge tous les types de materiel */
    public static CopexReturn getAllTypeMaterialFromDB(DataBaseCommunication dbC, Locale locale,ArrayList v){
        ArrayList<TypeMaterial> listT = new ArrayList();
        String query = "SELECT ID_TYPE, TYPE_NAME FROM MATERIAL_TYPE ";

        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_TYPE");
        listFields.add("TYPE_NAME");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_TYPE");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String type = rs.getColumnData("TYPE_NAME");
            if (type == null)
                continue;
            TypeMaterial t = new TypeMaterial(dbKey, CopexUtilities.getLocalText(type, locale));
            listT.add(t);
        }
        v.add(listT);
        return new CopexReturn();
    }

    /* retourne la liste des parametres pour un materiel */
    public static CopexReturn getMaterialParametersFromDB(DataBaseCommunication dbC, Locale locale,long idMat,ArrayList<PhysicalQuantity> listPhysicalQuantity,  ArrayList v){
        ArrayList<Parameter> listP = new ArrayList();
        int nbPhysQ = listPhysicalQuantity.size();
        String query = "SELECT ID_QUANTITY, QUANTITY_NAME, QUANTITY_TYPE, QUANTITY_VALUE, UNCERTAINTY, UNIT " +
                " FROM QUANTITY WHERE ID_QUANTITY IN " +
                "(SELECT ID_PARAMETER FROM LINK_MATERIAL_PARAMETERS WHERE ID_MATERIAL =  "+idMat+ ") ;";

        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_QUANTITY");
        listFields.add("QUANTITY_NAME");
        listFields.add("QUANTITY_TYPE");
        listFields.add("QUANTITY_VALUE");
        listFields.add("UNCERTAINTY");
        listFields.add("UNIT");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_QUANTITY");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("QUANTITY_NAME");
            if (name == null)
                continue;
            String type = rs.getColumnData("QUANTITY_TYPE");
            if (type == null)
                continue;
            s = rs.getColumnData("QUANTITY_ALUE");
            if (s == null)
                continue;
            Double value = Double.parseDouble(s);
            String uncertainty = rs.getColumnData("UNCERTAINTY");
            if (uncertainty == null)
                continue;
            s = rs.getColumnData("UNIT");
            if (s == null)
                continue;
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
            Parameter p = new Parameter(dbKey, locale,name, type, value, uncertainty, unit);
            listP.add(p);
        }
        v.add(listP);
        return new CopexReturn();
    }


    /* controle s'il existe des learner proc pour une mission et un eleve, si oui renoit egalement les id des proc initiaux */
    public static CopexReturn controlLearnerProcInDB(DataBaseCommunication dbC, long dbKeyLabDoc, ArrayList v){
        boolean learnerProc = false;
        ArrayList<Long> listIdInitProc = new ArrayList();
        String query = "SELECT P.PROC_NAME, I.ID_INITIAL_PROC " +
                "FROM EXPERIMENTAL_PROCEDURE P, LEARNER_PROC L, LINK_LEARNER_INITIAL_PROC I, LINK_LABDOC_PROC M "+
                "WHERE M.ID_LABDOC = "+dbKeyLabDoc+"  AND "+
                "M.ID_PROC = P.ID_PROC AND M.ID_PROC = L.ID_PROC AND M.ID_PROC = I.ID_LEARNER_PROC";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("P.PROC_NAME");
        listFields.add("I.ID_INITIAL_PROC");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            learnerProc = true;
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("I.ID_INITIAL_PROC");
            long dbKey = Long.parseLong(s);
            listIdInitProc.add(dbKey);
        }
        v.add(learnerProc);
        v.add(listIdInitProc);
        return new CopexReturn();
    }

    /* controle s'il existe plusieurs proc initiaux pour cette mission   */
    public static CopexReturn controlInitialProcInDB(DataBaseCommunication dbC, long dbKeyMission, ArrayList v){
        ArrayList<Long> listIdInitProc = new ArrayList();
        String query = "SELECT I.ID_PROC FROM INITIAL_PROC I, LINK_MISSION_INITIAL_PROC M "+
                "WHERE M.ID_MISSION = "+dbKeyMission+"  AND "+
                "M.ID_INIT_PROC = I.ID_PROC ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("I.ID_PROC");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("I.ID_PROC");
            long dbKey = Long.parseLong(s);
            listIdInitProc.add(dbKey);
        }
        v.add(nbR == 1);
        v.add(listIdInitProc);
        return new CopexReturn();
    }

    /* chargement proc initial simplifiee : uniquement le code et le nom*/
    public static CopexReturn getSimpleInitialProcFromDB(DataBaseCommunication dbC,  Locale locale,long dbKeyMission ,  ArrayList<Long> listIdInitProc,  ArrayList v){
        ArrayList<InitialProcedure> listInitProc = new ArrayList();
        // chargement du protocole initial
        String list = "("+listIdInitProc.get(0);
        int nbI = listIdInitProc.size();
        for (int i=1; i<nbI; i++){
            list += ","+listIdInitProc.get(i);
        }
        list += ")";
        String queryInit = "SELECT E.ID_PROC, E.PROC_NAME, E.DATE_LAST_MODIFICATION, E.ACTIV, E.PROC_RIGHT, " +
                "I.IS_FREE_ACTION, I.IS_TASK, I.IS_TASK_REPEAT, I.CODE_PROC, I.HYPOTHESIS_MODE, I.PRINCIPLE_MODE, I.DRAW_PRINCIPLE, I.EVALUATION_MODE, I.IS_TASK_DRAW  " +
           " FROM EXPERIMENTAL_PROCEDURE E, LINK_MISSION_INITIAL_PROC L, INITIAL_PROC I " +
                   "WHERE L.ID_MISSION = "+dbKeyMission+"  AND "+
                   "L.ID_INIT_PROC IN "+list+" AND "+
                   "L.ID_INIT_PROC  = E.ID_PROC AND L.ID_INIT_PROC = I.ID_PROC  " +
                   " ORDER BY I.CODE_PROC  ;";

        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("E.ID_PROC");
        listFields.add("E.PROC_NAME");
        listFields.add("E.DATE_LAST_MODIFICATION");
        listFields.add("E.ACTIV");
        listFields.add("E.PROC_RIGHT");
        listFields.add("I.IS_FREE_ACTION");
        listFields.add("I.IS_TASK");
        listFields.add("I.IS_TASK_REPEAT");
        listFields.add("I.CODE_PROC");
        listFields.add("I.HYPOTHESIS_MODE");
        listFields.add("I.PRINCIPLE_MODE");
        listFields.add("I.DRAW_PRINCIPLE");
        listFields.add("I.EVALUATION_MODE");
        listFields.add("I.IS_TASK_DRAW");

        CopexReturn cr = dbC.sendQuery(queryInit, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("E.ID_PROC");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String name = rs.getColumnData("E.PROC_NAME");
            if (name == null)
                continue;
            Date dateLastModif = null;
            s = rs.getColumnData("E.DATE_LAST_MODIFICATION");
            if (s == null)
                continue;
            dateLastModif = CopexUtilities.getDate(s);
            s = rs.getColumnData("E.ACTIV");
            if (s == null)
                continue;
            int activ = 0;
            try{
                activ = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            boolean isActiv = (activ == 1);
            s = rs.getColumnData("E.PROC_RIGHT");
            if (s == null)
                continue;
            char r = s.charAt(0);
            s = rs.getColumnData("I.IS_FREE_ACTION");
            if (s == null)
                continue;
            int f = 0;
            try{
                f = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            boolean isFreeAction = (f == 1);
            s = rs.getColumnData("I.IS_TASK");
            if (s == null)
                continue;
            int t = 0;
            try{
                t = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            boolean isTaskMode = (t == 1);
            s = rs.getColumnData("I.IS_TASK_REPEAT");
            if (s == null)
                continue;
            f = 0;
            try{
                f = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            boolean isTaskRepeat = (f == 1);
            String code = rs.getColumnData("I.CODE_PROC");

            s = rs.getColumnData("I.HYPOTHESIS_MODE");
            char hypothesisMode = s.charAt(0);
            s = rs.getColumnData("I.PRINCIPLE_MODE");
            char principleMode = s.charAt(0);
            s = rs.getColumnData("I.DRAW_PRINCIPLE");
            int d = 0;
            try{
                d = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            boolean drawPrinciple = (d == 1);
            s = rs.getColumnData("I.EVALUATION_MODE");
            char evaluationMode = s.charAt(0);
            s = rs.getColumnData("I.IS_TASK_DRAW");
            if (s == null)
                continue;
            f = 0;
            try{
                f = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            boolean isTaskDraw = (f == 1);
            // initial proc
            InitialProcedure initProc = new InitialProcedure(dbKey,CopexUtilities.getLocalText(name, locale), dateLastModif, isActiv, r, code, isFreeAction, isTaskMode, isTaskRepeat, null, hypothesisMode, principleMode, drawPrinciple, evaluationMode, null, isTaskDraw);


            listInitProc.add(initProc);
        }
        v.add(listInitProc);
        return new CopexReturn();

    }

    public static CopexReturn deleteHypothesisFromDB(DataBaseCommunication dbC, ExperimentalProcedure proc ){
        String queryDel = "DELETE FROM HYPOTHESIS WHERE ID_HYPOTHESIS IN (SELECT ID_HYPOTHESIS FROM LINK_PROC_HYPOTHESIS WHERE ID_PROC = "+proc.getDbKey()+") ;";
        String queryDelLink = "DELETE FROM LINK_PROC_HYPOTHESIS WHERE ID_PROC = "+proc.getDbKey()+" ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[2];
        querys[0] = queryDel ;
        querys[1] = queryDelLink ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    public static CopexReturn createHypothesisInDB(DataBaseCommunication dbC,Locale locale,  ExperimentalProcedure proc, Hypothesis hypothesis,  ArrayList v){
        String hyp = hypothesis.getHypothesis(locale);
        hyp =  AccesDB.replace("\'",hyp,"''");
        String comment = hypothesis.getComment(locale);
        comment =  AccesDB.replace("\'",comment,"''");
        int hide = hypothesis.isHide() ? 1 : 0;
        String query = "INSERT INTO HYPOTHESIS (ID_HYPOTHESIS, HYPOTHESIS,HYPOTHESIS_COMMENT, HYP_HIDE) " +
                    " VALUES (NULL,'"+hyp+"' , '"+comment+"', "+hide+" ); ";
        String queryID = "SELECT max(last_insert_id(`ID_HYPOTHESIS`))   FROM HYPOTHESIS ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        hypothesis.setDbKey(dbKey);
        // creation des liens
        String[] querys = new String[1];
        String queryD = "INSERT INTO LINK_PROC_HYPOTHESIS (ID_PROC, ID_HYPOTHESIS) VALUES ("+proc.getDbKey()+", "+dbKey+") ;";
        querys[0] = queryD;
        ArrayList v3 =new ArrayList();
        cr = dbC.executeQuery(querys, v3);
        if (cr.isError()){
            return cr;
        }
        v.add(hypothesis);
        return new CopexReturn();
    }

    public static CopexReturn updateHypothesisInDB(DataBaseCommunication dbC, Locale locale,Hypothesis hypothesis){
        String hyp = hypothesis.getHypothesis(locale);
        hyp =  AccesDB.replace("\'",hyp,"''");
        String comment = hypothesis.getComment(locale);
        comment =  AccesDB.replace("\'",comment,"''");
        int hide = hypothesis.isHide() ? 1 : 0;
        String query = "UPDATE HYPOTHESIS SET HYPOTHESIS = '"+hyp+"', HYPOTHESIS_COMMENT = '"+comment+"', HYP_HIDE = "+hide+" WHERE ID_HYPOTHESIS = "+hypothesis.getDbKey()+" ;";
        String[] querys = new String[1];
        querys[0] = query;
        ArrayList v3 =new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v3);
        if (cr.isError()){
            return cr;
        }
        return new CopexReturn();
    }

    public static CopexReturn getHypothesisFromDB(DataBaseCommunication dbC, Locale locale,ExperimentalProcedure proc, ArrayList v){
        Hypothesis hypothesis = null;
        String query = "SELECT H.ID_HYPOTHESIS, H.HYPOTHESIS,H.HYPOTHESIS_COMMENT, H.HYP_HIDE FROM HYPOTHESIS H , LINK_PROC_HYPOTHESIS L " +
                "WHERE L.ID_PROC = "+proc.getDbKey()+" AND L.ID_HYPOTHESIS = H.ID_HYPOTHESIS ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("H.ID_HYPOTHESIS");
        listFields.add("H.HYPOTHESIS");
        listFields.add("H.HYPOTHESIS_COMMENT");
        listFields.add("H.HYP_HIDE");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("H.ID_HYPOTHESIS");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String hyp = rs.getColumnData("H.HYPOTHESIS");
            String comment = rs.getColumnData("H.HYPOTHESIS_COMMENT");
            s = rs.getColumnData("H.HYP_HIDE");
            boolean hide = s.equals("1");
            hypothesis = new Hypothesis(dbKey, CopexUtilities.getLocalText(hyp, locale),CopexUtilities.getLocalText(comment, locale), hide);
        }
        v.add(hypothesis);
        return new CopexReturn();
    }



    public static CopexReturn deleteGeneralPrincipleFromDB(DataBaseCommunication dbC, ExperimentalProcedure proc ){
        String queryDel = "DELETE FROM GENERAL_PRINCIPLE WHERE ID_PRINCIPLE IN (SELECT ID_PRINCIPLE FROM LINK_PROC_PRINCIPLE WHERE ID_PROC = "+proc.getDbKey()+") ;";
        String queryDelLink = "DELETE FROM LINK_PROC_PRINCIPLE WHERE ID_PROC = "+proc.getDbKey()+" ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[2];
        querys[0] = queryDel ;
        querys[1] = queryDelLink ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    public static CopexReturn createGeneralPrincipleInDB(DataBaseCommunication dbC, Locale locale,ExperimentalProcedure proc, GeneralPrinciple principle,  ArrayList v){
        String gp = principle.getPrinciple(locale);
        gp =  AccesDB.replace("\'",gp,"''");
        String comment = principle.getComment(locale);
        comment =  AccesDB.replace("\'",comment,"''");
        int hide = principle.isHide() ? 1 : 0;
        String draw = null;
        if(principle.getDrawing() != null){
            draw = CopexUtilities.xmlToString(principle.getDrawing());
        }
        String query = "INSERT INTO GENERAL_PRINCIPLE (ID_PRINCIPLE, GENERAL_PRINCIPLE, PRINCIPLE_COMMENT,PRINC_HIDE, PRINCIPLE_DRAWING) " +
                    " VALUES (NULL,'"+gp+"' , '"+comment+"', "+hide+", '"+draw+"'); ";
        String queryID = "SELECT max(last_insert_id(`ID_PRINCIPLE`))   FROM GENERAL_PRINCIPLE ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        principle.setDbKey(dbKey);
        // creation des liens
        String[] querys = new String[1];
        String queryD = "INSERT INTO LINK_PROC_PRINCIPLE (ID_PROC, ID_PRINCIPLE) VALUES ("+proc.getDbKey()+", "+dbKey+") ;";
        querys[0] = queryD;
        ArrayList v3 =new ArrayList();
        cr = dbC.executeQuery(querys, v3);
        if (cr.isError()){
            return cr;
        }
        v.add(principle);
        return new CopexReturn();
    }

    public static CopexReturn updateGeneralPrincipleInDB(DataBaseCommunication dbC, Locale locale,GeneralPrinciple principle){
        String gp = principle.getPrinciple(locale);
        gp =  AccesDB.replace("\'",gp,"''");
        String comment = principle.getComment(locale);
        comment =  AccesDB.replace("\'",comment,"''");
        int hide = principle.isHide() ? 1 : 0;
        String draw = null;
        if(principle.getDrawing() != null){
            draw = CopexUtilities.xmlToString(principle.getDrawing());
        }
        String query = "UPDATE GENERAL_PRINCIPLE SET GENERAL_PRINCIPLE = '"+gp+"', PRINCIPLE_COMMENT = '"+comment+"', PRINC_HIDE = "+hide+", PRINCIPLE_DRAWING = '"+draw+"' WHERE ID_PRINCIPLE = "+principle.getDbKey()+" ;";
        String[] querys = new String[1];
        querys[0] = query;
        ArrayList v3 =new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v3);
        if (cr.isError()){
            return cr;
        }
        return new CopexReturn();
    }

    public static CopexReturn getGeneralPrincipleFromDB(DataBaseCommunication dbC, Locale locale,ExperimentalProcedure proc, ArrayList v){
        GeneralPrinciple principle = null;
        String query = "SELECT P.ID_PRINCIPLE, P.GENERAL_PRINCIPLE,P.PRINCIPLE_COMMENT, P.PRINC_HIDE, P.PRINCIPLE_DRAWING FROM GENERAL_PRINCIPLE P, LINK_PROC_PRINCIPLE L " +
                "WHERE L.ID_PROC = "+proc.getDbKey()+" AND L.ID_PRINCIPLE = P.ID_PRINCIPLE;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("P.ID_PRINCIPLE");
        listFields.add("P.GENERAL_PRINCIPLE");
        listFields.add("P.PRINCIPLE_COMMENT");
        listFields.add("P.PRINC_HIDE");
        listFields.add("P.PRINCIPLE_DRAWING");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("P.ID_PRINCIPLE");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String p = rs.getColumnData("P.GENERAL_PRINCIPLE");
            String comment = rs.getColumnData("P.PRINCIPLE_COMMENT");
            s = rs.getColumnData("P.PRINC_HIDE");
            boolean hide = s.equals("1");
            s = rs.getColumnData("P.PRINCIPLE_DRAWING");
            Element drawing = s == null|| s.equals("null") ? null : CopexUtilities.getElement(s);
            principle = new GeneralPrinciple(dbKey, CopexUtilities.getLocalText(p, locale),CopexUtilities.getLocalText(comment, locale), drawing, hide);
        }
        v.add(principle);
        return new CopexReturn();
    }

    public static CopexReturn deleteEvaluationFromDB(DataBaseCommunication dbC, ExperimentalProcedure proc ){
        String queryDel = "DELETE FROM EVALUATION WHERE ID_EVALUATION IN (SELECT ID_EVALUATION FROM LINK_PROC_EVALUATION WHERE ID_PROC = "+proc.getDbKey()+") ;";
        String queryDelLink = "DELETE FROM LINK_PROC_EVALUATION WHERE ID_PROC = "+proc.getDbKey()+" ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[2];
        querys[0] = queryDel ;
        querys[1] = queryDelLink ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    public static CopexReturn createEvaluationInDB(DataBaseCommunication dbC, Locale locale,ExperimentalProcedure proc, Evaluation evaluation,  ArrayList v){
        String eval = evaluation.getEvaluation(locale);
        eval =  AccesDB.replace("\'",eval,"''");
        String comment = evaluation.getComment(locale);
        comment =  AccesDB.replace("\'",comment,"''");
        int hide = evaluation.isHide() ? 1 : 0;
        String query = "INSERT INTO EVALUATION (ID_EVALUATION, EVALUATION, EVALUATION_COMMENT,EVAL_HIDE) " +
                    " VALUES (NULL,'"+eval+"', '"+comment+"', "+hide+"  ); ";
        String queryID = "SELECT max(last_insert_id(`ID_EVALUATION`))   FROM EVALUATION ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        evaluation.setDbKey(dbKey);
        // creation des liens
        String[] querys = new String[1];
        String queryD = "INSERT INTO LINK_PROC_EVALUATION (ID_PROC, ID_EVALUATION) VALUES ("+proc.getDbKey()+", "+dbKey+") ;";
        querys[0] = queryD;
        ArrayList v3 =new ArrayList();
        cr = dbC.executeQuery(querys, v3);
        if (cr.isError()){
            return cr;
        }
        v.add(evaluation);
        return new CopexReturn();
    }

    public static CopexReturn updateEvaluationInDB(DataBaseCommunication dbC,Locale locale, Evaluation evaluation){
        String eval = evaluation.getEvaluation(locale);
        eval =  AccesDB.replace("\'",eval,"''");
        String comment = evaluation.getComment(locale);
        comment =  AccesDB.replace("\'",comment,"''");
        int hide = evaluation.isHide() ? 1 : 0;
        String query = "UPDATE EVALUATION SET EVALUATION = '"+eval+"', EVALUATION_COMMENT = '"+comment+"', EVAL_HIDE = "+hide+" WHERE ID_EVALUATION = "+evaluation.getDbKey()+" ;";
        String[] querys = new String[1];
        querys[0] = query;
        ArrayList v3 =new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v3);
        if (cr.isError()){
            return cr;
        }
        return new CopexReturn();
    }

    public static CopexReturn getEvaluationFromDB(DataBaseCommunication dbC, Locale locale,ExperimentalProcedure proc, ArrayList v){
        Evaluation evaluation = null;
        String query = "SELECT E.ID_EVALUATION, E.EVALUATION,E.EVALUATION_COMMENT, E.EVAL_HIDE FROM EVALUATION E , LINK_PROC_EVALUATION L " +
                "WHERE L.ID_PROC = "+proc.getDbKey()+" AND L.ID_EVALUATION = E.ID_EVALUATION ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("E.ID_EVALUATION");
        listFields.add("E.EVALUATION");
        listFields.add("E.EVALUATION_COMMENT");
        listFields.add("E.EVAL_HIDE");

        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("E.ID_EVALUATION");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String eval = rs.getColumnData("E.EVALUATION");
            String comment = rs.getColumnData("E.EVALUATION_COMMENT");
            s = rs.getColumnData("E.EVAL_HIDE");
            boolean hide = s.equals("1");
            evaluation = new Evaluation(dbKey, CopexUtilities.getLocalText(eval, locale), CopexUtilities.getLocalText(comment, locale),hide);
        }
        v.add(evaluation);
        return new CopexReturn();
    }

    // material strategy of an init proc
    public static CopexReturn getInitialMaterialStrategyFromDB(DataBaseCommunication dbC, long dbKeyProc, ArrayList<MaterialStrategy> listMaterialStrategy, ArrayList v){
        MaterialStrategy strategy = null;
        int nb = listMaterialStrategy.size();
        String query = "SELECT ID_STRATEGY FROM LINK_INIT_PROC_STRATEGY_MATERIAL WHERE ID_PROC = "+dbKeyProc+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_STRATEGY");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_STRATEGY");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            for (int k=0; k<nb; k++){
                if(listMaterialStrategy.get(k).getDbKey() == dbKey){
                    strategy = listMaterialStrategy.get(k);
                    break;
                }
            }
        }
        v.add(strategy);
        return new CopexReturn();
    }

    private static CopexReturn getMaterialUsedFromDB(DataBaseCommunication dbC, Locale locale, long dbKeyProc, ArrayList<Material> listMaterial, ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList v){
        ArrayList<MaterialUsed> listMaterialUsed = new ArrayList();
        // charge le materiel lie au proc
        ArrayList v3 = new ArrayList();
        CopexReturn cr = getProcMaterialFromDB(dbC, locale, dbKeyProc, listPhysicalQuantity, v3);
        if(cr.isError())
            return cr;
        ArrayList<Material> listMaterialProc = (ArrayList<Material>)v3.get(0);
        int nbMatP = listMaterialProc.size();
        int nbMat = listMaterial.size();
        String query = "SELECT ID_MATERIAL, COMMENTS, MAT_USED " +
                "FROM MATERIAL_USED " +
                "WHERE ID_PROC = "+dbKeyProc+" ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_MATERIAL");
        listFields.add("COMMENTS");
        listFields.add("MAT_USED");
        cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_MATERIAL");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            String comments = rs.getColumnData("COMMENTS");
            s = rs.getColumnData("MAT_USED");
            boolean matUsed = s.equals("1");
            // mat est il dans la liste?
            Material material = null;
            for (int j=0;j<nbMat; j++){
                if(listMaterial.get(j).getDbKey() == dbKey){
                    material = listMaterial.get(j);
                    break;
                }
            }
            if(material == null){
                for (int j=0;j<nbMatP; j++){
                if(listMaterialProc.get(j).getDbKey() == dbKey){
                    material = listMaterialProc.get(j);
                    break;
                }
                }
            }
            MaterialUsed mUsed = new MaterialUsed(material, CopexUtilities.getLocalText(comments, locale), matUsed);
            listMaterialUsed.add(mUsed);
        }
        v.add(listMaterialUsed);
        return  new CopexReturn();
    }

    public static CopexReturn createListMaterialUsedInDB(DataBaseCommunication dbC,Locale locale,  long dbKeyProc, List<MaterialUsed> listMaterialUsed){
        int nb = listMaterialUsed.size();
        String[] querys = new String[nb];
        for (int i=0; i<nb; i++){
            String comment = listMaterialUsed.get(i).getComment(locale);
            comment =  AccesDB.replace("\'",comment,"''");
            int isUsed = listMaterialUsed.get(i).isUsed() ? 1 : 0;
            String query = "INSERT INTO MATERIAL_USED (ID_MATERIAL, ID_PROC, COMMENTS, MAT_USED) " +
                    "VALUES ("+listMaterialUsed.get(i).getMaterial().getDbKey()+", "+dbKeyProc+", '"+comment+"', "+isUsed+"); ";
            querys[i] = query;
        }
        ArrayList v2 =new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }

    public static CopexReturn deleteMaterialUsedFromDB(DataBaseCommunication dbC,  long dbKeyProc,List<MaterialUsed> listMaterialUsed){
        int nb = listMaterialUsed.size();
        for (int i=0; i<nb; i++){
            if(listMaterialUsed.get(i).isUserMaterial()){
                // on supprime le material
                CopexReturn cr = deleteMaterialFromDB(dbC, listMaterialUsed.get(i).getMaterial());
                if(cr.isError())
                    return cr;
            }
            String query = "DELETE FROM MATERIAL_USED WHERE ID_MATERIAL = "+listMaterialUsed.get(i).getMaterial().getDbKey()+" AND ID_PROC = "+dbKeyProc+" ;";
            ArrayList v = new ArrayList();
            String[] querys = new String[2];
            querys[0] = query ;
            CopexReturn cr = dbC.executeQuery(querys, v);
            if(cr.isError())
                return cr;
        }
        return new CopexReturn();
    }

    public static CopexReturn updateMaterialUsedInDB(DataBaseCommunication dbC, Locale locale, long dbKeyProc, ArrayList<MaterialUsed> listMaterialUsed){
        int nb = listMaterialUsed.size();
        String[] querys = new String[nb];
        for (int k=0; k<nb; k++){
            if(listMaterialUsed.get(k).isUserMaterial()){
                // modif material
                CopexReturn cr = updateMaterialInDB(dbC, locale, listMaterialUsed.get(k).getMaterial());
                if(cr.isError())
                    return cr;
            }
            String comments = listMaterialUsed.get(k).getComment(locale);
            comments = AccesDB.replace("\'",comments,"''");
            int isUsed = listMaterialUsed.get(k).isUsed() ? 1 : 0;
            String query = "UPDATE MATERIAL_USED SET COMMENTS = '"+comments+"', MAT_USED = "+isUsed+" WHERE ID_MATERIAL = "+listMaterialUsed.get(k).getMaterial().getDbKey()+" AND ID_PROC = "+dbKeyProc+" ;";
            querys[k] = query;
        }
        ArrayList v2 =new ArrayList();
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }

    public static CopexReturn createMaterialInDB(DataBaseCommunication dbC,Locale locale, Material material, long idProc,ArrayList v){
        String name = material.getName(locale) ;
        name =  AccesDB.replace("\'",name,"''") ;
        String description  = material.getDescription(locale);
        description =  AccesDB.replace("\'",description,"''") ;
        String query = "INSERT INTO MATERIAL (ID_MATERIAL, MATERIAL_NAME, DESCRIPTION) VALUES (NULL, '"+name+"', '"+description+"') ;";
        String queryID = "SELECT max(last_insert_id(`ID_MATERIAL`))   FROM MATERIAL ;";
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.getNewIdInsertInDB(query, queryID, v2);
        if (cr.isError())
            return cr;
        long dbKey = (Long)v2.get(0);
        material.setDbKey(dbKey);
        // creation source
        cr = createMaterialSourceInDB(dbC,  material, idProc);
        if(cr.isError())
            return cr;
        // creation du type
        int nbT = material.getListType().size() ;
        String[] querys = new String[nbT];
        int id=0;
        for (int j=0; j<nbT; j++){
            String queryType = "INSERT INTO LINK_TYPE_MATERIAL (ID_TYPE, ID_MATERIAL) VALUES ("+material.getListType().get(j).getDbKey()+", "+dbKey+"); ";
            querys[id] = queryType ;
            id++;
        }
        cr = dbC.executeQuery(querys, v2);
        if (cr.isError())
            return cr;
        // creation des parametres
        int nbParam = material.getListParameters().size();
        for (int j=0; j<nbParam; j++){
            String qName = material.getListParameters().get(j).getName(locale);
            qName =  AccesDB.replace("\'",qName,"''") ;
            String qType = material.getListParameters().get(j).getType(locale) ;
            qType =  AccesDB.replace("\'",qType,"''") ;
            double qValue = material.getListParameters().get(j).getValue() ;
            String qUncertainty = material.getListParameters().get(j).getUncertainty(locale) ;
            qUncertainty =  AccesDB.replace("\'",qUncertainty,"''") ;
            long dbKeyUnit = material.getListParameters().get(j).getUnit().getDbKey() ;
            String queryQ = "INSERT INTO QUANTITY (ID_QUANTITY, QUANTITY_NAME, QUANTITY_TYPE, QUANTITY_VALUE, UNCERTAINTY, UNIT) " +
            " VALUES (NULL,'"+qName+"' , '"+qType+"',"+qValue+" , '"+qUncertainty+"',"+dbKeyUnit+" ); ";
            String queryIDQ = "SELECT max(last_insert_id(`ID_QUANTITY`))   FROM QUANTITY ;";
            ArrayList v3 = new ArrayList();
            cr = dbC.getNewIdInsertInDB(queryQ, queryIDQ, v3);
            if (cr.isError())
                return cr;
            long dbKeyQ = (Long)v3.get(0);
            querys = new String[2];
            String queryP = "INSERT INTO COPEX_PARAMETER (ID_PARAMETER) VALUES ("+dbKeyQ+"); ";
            querys[0] = queryP ;
            String queryL =  "INSERT INTO LINK_MATERIAL_PARAMETERS (ID_MATERIAL, ID_PARAMETER) VALUES ("+dbKey+", "+dbKeyQ+") ;";
            querys[1] = queryL;
            v3 = new ArrayList();
            cr = dbC.executeQuery(querys, v3);
            if (cr.isError())
                return cr;
            material.getListParameters().get(j).setDbKey(dbKeyQ);
        }
        v.add(material);
        return new CopexReturn();
    }

    public static CopexReturn createMaterialSourceInDB(DataBaseCommunication dbC,Material material, long idProc){
        if(material.isUserMaterial()){
            String query = "INSERT INTO MATERIAL_CREATED_BY_USER (ID_MATERIAL, ID_PROC) VALUES ("+material.getDbKey()+", "+idProc+");";
            CopexReturn cr=  dbC.executeQuery(query);
            if(cr.isError())
                return cr;
            return new CopexReturn();
        }else if(material.isTeacherMaterial()){
            String query = "INSERT INTO LINK_TEACHER_MATERIAL(ID_TEACHER, ID_MATERIAL) VALUES ("+((MaterialSourceTeacher)(material.getMaterialSource())).getIdTeacher()+", "+material.getDbKey()+");";
            CopexReturn cr = dbC.executeQuery(query);
            if(cr.isError())
                return cr;
            return new CopexReturn();
        }
        return new CopexReturn();
    }

    private static CopexReturn updateMaterialInDB(DataBaseCommunication dbC, Locale locale, Material material){
        String name = material.getName(locale) ;
        name =  AccesDB.replace("\'",name,"''") ;
        String description  = material.getDescription(locale);
        description =  AccesDB.replace("\'",description,"''") ;
        String query = "UPDATE MATERIAL SET MATERIAL_NAME = '"+name+"', DESCRIPTION = '"+description+"' WHERE ID_MATERIAL = "+material.getDbKey()+" ;";
        String querys[] = new String[1];
        ArrayList v2 = new ArrayList();
        querys[0] = query;
        CopexReturn cr = dbC.executeQuery(querys, v2);
        return cr;
    }

   // suppression d'un materiel
    private static CopexReturn deleteMaterialFromDB(DataBaseCommunication dbC, Material material){
        int nb = 6;
        int i=0;
        ArrayList v = new ArrayList();
        String[] querys = new String[nb];
        String queryDelLinkType = "DELETE FROM LINK_TYPE_MATERIAL WHERE ID_MATERIAL = "+material.getDbKey()+" ; ";
        querys[i++] = queryDelLinkType;
        String queryDelQuantity = "DELETE FROM QUANTITY WHERE ID_QUANTITY IN (SELECT ID_PARAMETER FROM LINK_MATERIAL_PARAMETERS WHERE ID_MATERIAL= "+material.getDbKey()+" );" ;
        querys[i++] = queryDelQuantity;
        String queryDelParameter = "DELETE FROM COPEX_PARAMETER WHERE ID_PARAMETER IN (SELECT ID_PARAMETER FROM LINK_MATERIAL_PARAMETERS WHERE ID_MATERIAL  = "+material.getDbKey()+" );" ;
        querys[i++] = queryDelParameter;
        String queryDelLinkParamMat = "DELETE FROM LINK_MATERIAL_PARAMETERS WHERE ID_MATERIAL = "+material.getDbKey()+" ;" ;
        querys[i++] = queryDelLinkParamMat;
        String queryDelMaterial = "DELETE FROM MATERIAL WHERE ID_MATERIAL = "+material.getDbKey()+" ;";
        querys[i++] = queryDelMaterial;
        String queryDelLinkGroup = "DELETE FROM LINK_GROUP_MATERIAL WHERE ID_MATERIAL = "+material.getDbKey()+" ;";
        querys[i++] = queryDelLinkGroup;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    public static CopexReturn setPreviewLabdocInDB(DataBaseCommunication dbC, long dbKeyLabdoc, String preview){
        preview =  AccesDB.replace("\'",preview,"''") ;
        String query = "UPDATE LABDOC SET PREVIEW =  '"+preview+"' WHERE ID_LABDOC = "+dbKeyLabdoc+" ; ";
        dbC.updateDb(MyConstants.DB_LABBOOK);
        CopexReturn cr = dbC.executeQuery(query);
        if(cr.isError()){
            dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
            return cr;
        }
        dbC.updateDb(MyConstants.DB_LABBOOK_COPEX);
        return new CopexReturn();
    }

    public static CopexReturn getMaterialSource(DataBaseCommunication dbC, long dbKeyMaterial, ArrayList v){
        String queryAction = "SELECT ID_ACTION FROM MATERIAL_PRODUCE ACTION_PRODUCE_MATERIAL WHERE ID_MATERIAL = "+dbKeyMaterial+";";
        String queryUser = "SELECT ID_MATERIAL FROM MATERIAL_CREATED_BY_USER WHERE ID_MATERIAL = "+dbKeyMaterial+";";
        String queryTeacher = "SELECT ID_TEACHER FROM LINK_TEACHER_MATERIAL WHERE ID_MATERIAL = "+dbKeyMaterial+" ;";
        MaterialSource materialSource = new MaterialSourceCopex();
        ArrayList v2 = new ArrayList();
        CopexReturn cr = dbC.sendQuery(queryAction,  v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            long idAction = -1;
            try{
                idAction = Long.parseLong(rs.getColumnData("ID_ACTION"));
            }catch(NumberFormatException e){

            }
            materialSource = new MaterialSourceAction(idAction);
        }
        v2 = new ArrayList();
        cr = dbC.sendQuery(queryUser,  v2);
        if (cr.isError())
            return cr;
        nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            materialSource = new MaterialSourceUser();
        }
        v2 = new ArrayList();
        cr = dbC.sendQuery(queryTeacher,  v2);
        if (cr.isError())
            return cr;
        nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            long idTeacher = -1;
            try{
                idTeacher = Long.parseLong(rs.getColumnData("ID_TEACHER"));
            }catch(NumberFormatException e){

            }
            materialSource = new MaterialSourceTeacher(idTeacher);
        }
        v.add(materialSource);
        return new CopexReturn();
    }
}
