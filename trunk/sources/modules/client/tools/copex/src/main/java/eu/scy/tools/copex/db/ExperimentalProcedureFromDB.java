/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.db;



import eu.scy.tools.copex.common.*;
import eu.scy.tools.copex.profiler.Profiler;
import eu.scy.tools.copex.synchro.Locker;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
import java.util.ArrayList;
import java.sql.*;
import java.util.Locale;
import org.jdom.Element;

/**
 * MBO le 03/03/09 : multi proc initiaux
 * gere les protocoles 
 * @author MBO
 */
public class ExperimentalProcedureFromDB {
    /* chargement des protocoles lié à une mission et un utilisateur
     * MBO le 27/02/09 : proc lockes
     en v[0] : la liste des proc
     en v[1] : la liste des proc initiaux
     en v[2] : le nom des proc lockes
     en v[3] ; un boolean qui indique si ts les proc de cet utilisateurs pour cette mission sont lockes
     */
    public static CopexReturn getProcMissionFromDB_xml(DataBaseCommunication dbC, Locker locker, Locale locale, long dbKeyMission, long dbKeyUser, ArrayList<Long> listIdInitProc, ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList v) {
        ArrayList<LearnerProcedure> listP = new ArrayList();
        ArrayList v2 = new ArrayList();
        CopexReturn cr = getInitialProcFromDB(dbC, locale, dbKeyMission, dbKeyUser, listIdInitProc, listPhysicalQuantity, v2) ;
        if (cr.isError())
            return cr;
        ArrayList<InitialProcedure> listInitProc = (ArrayList<InitialProcedure>)v2.get(0);
        // lsite de noms des proc lockes
        ArrayList<String> listProcLocked = new ArrayList();
        boolean allProcLocked = true;
        String query = "SELECT ID_PROC, PROC_NAME, DATE_LAST_MODIFICATION, ACTIV, PROC_RIGHT " +
                " FROM EXPERIMENTAL_PROCEDURE WHERE ID_PROC IN (" +
                "SELECT ID_PROC FROM LINK_MISSION_PROC WHERE " +
                "ID_MISSION = "+dbKeyMission+" AND ID_USER = "+dbKeyUser+" ) AND " +
                "ID_PROC IN (SELECT ID_PROC FROM LEARNER_PROC ) ;";
                
        System.out.println("chargement proc user : "+query);
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
            System.out.println("loadlearner proc"+i) ;
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_PROC");
            if (s == null)
                continue;
            long dbKey = Long.parseLong(s);
            System.out.println("proc user : "+dbKey);
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
            if (locker.isLocked(dbKey)){
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
                LearnerProcedure proc = new LearnerProcedure(dbKey, name, dateLastModif, isActiv, r, initProcCorresp);
                // recuperation de la question
                Profiler.start("xml_loadQuestion");
                v3 = new ArrayList();
                cr = getQuestionProcFromDB_xml(dbC, dbKey, v3);
                if (cr.isError())
                    return cr;
                Question question = (Question)v3.get(0);
                proc.setQuestion(question);
                Profiler.end("xml_loadQuestion");
                // recuperation de l'arbre des taches
                Profiler.start("xml_loadTasks");
                v3 = new ArrayList();
                cr = TaskFromDB.getAllTaskFromDB_xml(dbC, dbKey, question.getDbKey(), initProcCorresp.getListNamedAction(), listMaterial, listPhysicalQuantity, v3);
                if (cr.isError())
                    return cr;
                ArrayList<CopexTask> listTask = (ArrayList<CopexTask>)v3.get(0);
                proc.setListTask(listTask);
                Profiler.end("xml_loadTasks");
                // met le lien du fils à la question
                int ts = listTask.size();
                for (int k=0; k<ts; k++){
                    if (listTask.get(k).getDbKey() == question.getDbKey()){
                        question.setDbKeyChild(listTask.get(k).getDbKeyChild());
                    }
                }
                // materiel utilise pour proc
                v3 = new ArrayList();
                cr = getMaterialUseForProcFromDB_xml(dbC, dbKey, listMaterial, v3);
                if (cr.isError())
                    return cr;
                ArrayList<MaterialUseForProc> listMaterialUse = (ArrayList<MaterialUseForProc>)v3.get(0);
                proc.setListMaterialUse(listMaterialUse);
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
    public static CopexReturn getShortProcMissionFromDB_xml(DataBaseCommunication dbC, long dbKeyMission, long dbKeyUser ,  ArrayList v){
        ArrayList<LearnerProcedure> listP = new ArrayList();
         String query = "SELECT E.ID_PROC, E.PROC_NAME, L.ID_INITIAL_PROC  " +
                " FROM EXPERIMENTAL_PROCEDURE E, LINK_LEARNER_INITIAL_PROC L, LINK_MISSION_PROC M, LEARNER_PROC P " +
                "WHERE M.ID_MISSION = "+dbKeyMission+" AND M.ID_USER = "+dbKeyUser+" AND "+
                "M.ID_PROC = P.ID_PROC AND M.ID_PROC  = E.ID_PROC AND M.ID_PROC = L.ID_LEARNER_PROC ;";


        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("E.ID_PROC");
        listFields.add("E.PROC_NAME");
        listFields.add("L.ID_INITIAL_PROC");
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
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
            s = rs.getColumnData("L.ID_INITIAL_PROC") ;
            long dbKeyInitProc = Long.parseLong(s);
            ArrayList v3 = new ArrayList();
            ArrayList<Long> listInitProc = new ArrayList();
            listInitProc.add(dbKeyInitProc);
            cr = getSimpleInitialProcFromDB(dbC, dbKeyMission, dbKeyUser, listInitProc, v3);
            if(cr.isError())
                return cr;
            ArrayList<InitialProcedure> listIP = (ArrayList<InitialProcedure>)v3.get(0);
            InitialProcedure initProc = listIP.get(0);
            LearnerProcedure proc = new LearnerProcedure(dbKey, name, null, true,MyConstants.NONE_RIGHT, initProc );
            // ajoute dans la liste
            listP.add(proc);
        }
        v.add(listP);
        return new CopexReturn();
    }


     /* chargement de la question d'un protocole */
    public static CopexReturn getQuestionProcFromDB_xml(DataBaseCommunication dbC, long dbKeyProc, ArrayList v) {
        String query = "SELECT L.ID_TASK, T.TASK_NAME, T.DESCRIPTION, T.COMMENTS, T.TASK_IMAGE, T.DRAW_ELO, T.IS_VISIBLE, R.EDIT_RIGHT, R.DELETE_RIGHT, R.COPY_RIGHT, R.MOVE_RIGHT, R.PARENT_RIGHT, R.DRAW_RIGHT, R.REPEAT_RIGHT, Q.HYPOTHESIS, Q.GENERAL_PRINCIPLE  " +
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
        listFields.add("Q.HYPOTHESIS");
        listFields.add("Q.GENERAL_PRINCIPLE");
        
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
            Element draw = CopexTask.getElement(s);
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
            String hypothesis = rs.getColumnData("Q.HYPOTHESIS");
            if (hypothesis == null)
                continue;
            String generalPrinciple = rs.getColumnData("Q.GENERAL_PRINCIPLE");
            if (generalPrinciple == null)
                continue;
            question = new Question(dbKeyQuestion, name, description, hypothesis, comments, taskImage, draw, generalPrinciple, isVisible, new TaskRight(editR, deleteR, copyR, moveR, parentR, drawR, repeatR), true);
        } 
        v.add(question);
        return new CopexReturn();
    }
    
    
    
   
    
    static public CopexReturn createProcedureInDB_xml(DataBaseCommunication dbC, LearnerProcedure proc, long idLearner, ArrayList v){
        String name = proc.getName() != null ? proc.getName() :"";
        name =  AccesDB.replace("\'",name,"''") ;
        int activ = 0;
        if (proc.isActiv())
            activ = 1;
        char right = proc.getRight();
        // question 
        ArrayList v2 = new ArrayList();
        CopexReturn cr = TaskFromDB.createQuestionInDB_xml(dbC, proc.getQuestion(), v2);
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
            
         // on cree le lien avec la mission / utilisateur 
         String queryLink = "INSERT INTO LINK_MISSION_PROC (ID_MISSION, ID_PROC, ID_USER) " +
                    "VALUES ("+proc.getMission().getDbKey()+", "+dbKey+", "+idLearner+") ;";
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
            
            // on ne cree pas la liste des taches associées, cela se fait dans 
            // un second temps
            
            v.add(dbKey);
            v.add(idQ);
            return new CopexReturn();
    }
    
    
   
    
    /* mise à jour du statut activ d'un protocole */
    static public CopexReturn updateActivProcInDB_xml(DataBaseCommunication dbC, long dbKeyProc, boolean activ){
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
    
    
    
    
    /* mise à jour de la date de modif d'un protocole */
    static public CopexReturn updateDateProcInDB_xml(DataBaseCommunication dbC, long dbKeyProc, java.sql.Date date){
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
    static public CopexReturn deleteProcedureFromDB_xml(DataBaseCommunication dbC, long dbKeyProc, long dbKeyUser){
        // suppression lien mission
        String queryLink = "DELETE FROM LINK_MISSION_PROC WHERE " +
                    "ID_PROC = "+dbKeyProc+" AND ID_USER = "+dbKeyUser+" ;";
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
    
    
   
    /* mise à jour du nom du protocole */
    static public CopexReturn updateProcNameInDB_xml(DataBaseCommunication dbC, long dbKeyProc, String name) {
        name =  AccesDB.replace("\'",name,"''") ;
        
        String query = "UPDATE EXPERIMENTAL_PROCEDURE SET PROC_NAME = '"+name+"' "+
                    "WHERE ID_PROC = "+dbKeyProc+" ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }
    
     /* chargement du materiel utilise pour le proc */
    public static CopexReturn getMaterialUseForProcFromDB_xml(DataBaseCommunication dbC, long dbKeyProc, ArrayList<Material> listMaterial,  ArrayList v) {
        int nbM =  listMaterial.size();
        ArrayList<MaterialUseForProc> listMaterialUse = new ArrayList();
        String query = "SELECT ID_MATERIAL, JUSTIFICATION FROM MATERIAL_USE_PROC WHERE ID_PROC = "+dbKeyProc+" ; ";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_MATERIAL");
        listFields.add("JUSTIFICATION");
        
        CopexReturn cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        for (int i=0; i<nbR; i++){
            ResultSetXML rs = (ResultSetXML)v2.get(i);
            String s = rs.getColumnData("ID_MATERIAL");
            if (s == null)
                continue;
            long dbKeyMaterial = Long.parseLong(s);
            String justification = rs.getColumnData("JUSTIFICATION");
            if (justification == null)
                continue;
            // on cherche l'indice dans la liste 
            int idM = -1;
            for (int j=0; j<nbM; j++){
                if (listMaterial.get(j).getDbKey() == dbKeyMaterial){
                    idM = j;
                    break;
                }
            }
            if (idM != -1){
                Material m = listMaterial.get(idM);
                MaterialUseForProc mu = new MaterialUseForProc(m, justification);
                listMaterialUse.add(mu);
            }
        } 
        v.add(listMaterialUse);
        return new CopexReturn();
    }
    
    /* ajout d'un materiel utlise pour un proc */
    public static CopexReturn addMaterialUseForProcInDB_xml(DataBaseCommunication dbC, long dbKeyProc, long dbKeyMat, String justification){
        justification =  AccesDB.replace("\'",justification,"''") ;
        String query = "INSERT INTO MATERIAL_USE_PROC (ID_PROC, ID_MATERIAL, JUSTIFICATION) VALUES ("+dbKeyProc+","+dbKeyMat+" , '"+justification+"') ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }
    
     /* modification d'un materiel utlise pour un proc */
    public static CopexReturn updateMaterialUseForProcInDB_xml(DataBaseCommunication dbC, long dbKeyProc, long dbKeyMat, String justification){
        justification =  AccesDB.replace("\'",justification,"''") ;
        String query = "UPDATE MATERIAL_USE_PROC SET JUSTIFICATION = '"+justification+"' WHERE ID_PROC = "+dbKeyProc+" AND ID_MATERIAL = "+dbKeyMat+";";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }
    
     /* suppression d'un materiel utlise pour un proc */
    public static CopexReturn removeMaterialUseForProcFromDB_xml(DataBaseCommunication dbC, long dbKeyProc, long dbKeyMat){
       String query = "DELETE FROM  MATERIAL_USE_PROC WHERE ID_PROC = "+dbKeyProc+" AND ID_MATERIAL = "+dbKeyMat+";";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* retourne en v[0] la liste des actions nommees */
    public static  CopexReturn getListNamedActionFromDB(DataBaseCommunication dbC, long dbKeyProc, Locale locale, ArrayList<TypeMaterial> listTypeMaterial, ArrayList<PhysicalQuantity> listPhysicalQuantity,  ArrayList v){
        ArrayList<InitialNamedAction> l = new ArrayList();
        String lib = "LIB_"+locale.getLanguage() ;
        String query = "SELECT A.ID_ACTION_NOMMEE, A.CODE,A.DRAW, A.REPEAT, A.IS_SETTING,  A."+lib+" " +
                "FROM INITIAL_ACTION_NOMMEE A, LINK_INITIAL_PROC_ACTION_NOMMEE L " +
                "WHERE L.ID_PROC = "+dbKeyProc +" AND L.ID_ACTION_NOMMEE = A.ID_ACTION_NOMMEE ORDER BY A.CODE ;";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("A.ID_ACTION_NOMMEE");
        listFields.add("A.CODE");
        listFields.add("A.DRAW");
        listFields.add("A.REPEAT");
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
            s = rs.getColumnData("A.REPEAT");
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
                cr = getInitialActionFromDB(dbC, locale, dbKey, code, libelle, draw, repeat, isSetting, variable, listPhysicalQuantity, v3);
                if (cr.isError())
                    return cr;
                action = (InitialNamedAction)v3.get(0);


            }else
                action = new InitialNamedAction(dbKey, code, libelle, isSetting, variable, draw, repeat);
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
                InitialParamData d = new InitialParamData(dbKeyParamD, paramName) ;
                if (id >= tabParam.length){
                    System.out.println("param data : depassement nbParam");
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
                InitialParamQuantity p = new InitialParamQuantity(dbKeyParamQ, paramName, physQ, qName);
                if (id >= tabParam.length){
                    System.out.println("param quantity : depassement nbParam");
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
                if(s== null)
                    allType =true;
                else{
                    try{
                        long dbKeyMat = Long.parseLong(s);
                        typeMat = getTypeMaterial(dbKeyMat, listTypeMaterial);
                        if (typeMat == null){
                            System.out.println("Erreur sur le type de material ");
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
                        System.out.println("Erreur sur les liens entre param qtt et mat");
                        return new CopexReturn("ERROR LOAD ACTION PARAM", false);
                    }
                }
                InitialParamMaterial p = new InitialParamMaterial(dbKeyParamM, paramName, typeMat,typeMat2, andTypes,paramQ, allType);
                if (id >= tabParam.length){
                    System.out.println("param material : depassement nbParam");
                    return new CopexReturn("ERROR LOAD ACTION PARAM", false);
                }
                tabParam[id] = p;
                id++;
            }
            // variable
            variable = new InitialActionVariable(dbKey, code, nbParam, libelle, tabParam) ;
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
    private static CopexReturn getInitialActionFromDB(DataBaseCommunication dbC, Locale locale, long dbKey, String code,String libelle,boolean draw, boolean repeat,  boolean isSetting, InitialActionVariable variable, ArrayList<PhysicalQuantity> listPhysicalQuantity,ArrayList v){
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
            action = new InitialActionChoice(dbKey, code, libelle, isSetting, variable, draw, repeat) ;
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
            action = new InitialActionManipulation(dbKey, code, libelle, isSetting, variable, draw, repeat, nbMaterialProd, listOutput);
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
            action = new InitialActionAcquisition(dbKey, code, libelle, isSetting, variable, draw, repeat, nbDataProd, listOutput);
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
            action = new InitialActionTreatment(dbKey, code, libelle, isSetting, variable, draw, repeat, nbDataProd, listOutput);
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
            cr = getInitialTypeMaterialManipulationFromDB(dbC, dbKey, v3);
            if (cr.isError())
                return cr;
            ArrayList<TypeMaterial> typeMaterialProd = (ArrayList<TypeMaterial>)v3.get(0);
            InitialManipulationOutput out = new InitialManipulationOutput(dbKey, textprod, name, typeMaterialProd);
            list.add(out);
        }
        v.add(list);
        return new CopexReturn();
    }


    /* retourne en v[0] la liste du type de  material prod par une tache de manipulation */
    private static CopexReturn getInitialTypeMaterialManipulationFromDB(DataBaseCommunication dbC, long dbKeyAction,  ArrayList v){
        ArrayList<TypeMaterial> typeMaterialProd = new ArrayList();
        String query = "SELECT T.ID_TYPE, T.TYPE_NAME FROM MATERIAL_TYPE T, INITIAL_TYPE_MATERIAL_MANIPULATION I, LINK_INITIAL_MATERIAL_MANIPULATION L  " +
                "WHERE T.ID_TYPE = I.ID_TYPE AND I.ID_INITIAL_TYPE_MATERIAL_MANIPULATION = L.ID_INITIAL_TYPE_MATERIAL AND L.ID_ACTION = "+dbKeyAction+" ;";
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
            TypeMaterial type = new TypeMaterial(dbKey, name);
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
            InitialAcquisitionOutput out = new InitialAcquisitionOutput(dbKey, textprod, name, unitDataProd);
            list.add(out);
        }
        v.add(list);
        return new CopexReturn();
    }

    /* retourne en v[0] la liste des unites des data prod par une tache d'acquisition */
    private static CopexReturn getInitialUnitDataAcquisitionFromDB(DataBaseCommunication dbC, long dbKeyAction, int nbDataProd, ArrayList<PhysicalQuantity> listPhysicalQuantity,ArrayList v){
        int nbPhysQ = listPhysicalQuantity.size();
        CopexUnit[] unitDataProd = new CopexUnit[nbDataProd];
        String query = "SELECT I.ID_UNIT  FROM INITIAL_UNIT_DATA_ACQUISITION I, LINK_INITIAL_DATA_ACQUISITION  L  " +
                "WHERE I.ID_INITIAL_UNIT_DATA_ACQUISITION = L.ID_INITIAL_DATA AND L.ID_ACTION = "+dbKeyAction+" ;";
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
            InitialTreatmentOutput out = new InitialTreatmentOutput(dbKey, textprod, name, unitDataProd);
            list.add(out);
        }
        v.add(list);
        return new CopexReturn();
    }


    /* retourne en v[0] la liste des unites des data prod par une tache de treatment */
    private static CopexReturn getInitialUnitDataTreatmentFromDB(DataBaseCommunication dbC, long dbKeyAction, int nbDataProd, ArrayList<PhysicalQuantity> listPhysicalQuantity,ArrayList v){
        int nbPhysQ = listPhysicalQuantity.size();
        CopexUnit[] unitDataProd = new CopexUnit[nbDataProd];
        String query = "SELECT I.ID_UNIT  FROM INITIAL_UNIT_DATA_TREATMENT I, LINK_INITIAL_DATA_TREATMENT  L  " +
                "WHERE I.ID_INITIAL_UNIT_DATA_TREATMENT = L.ID_INITIAL_DATA AND L.ID_ACTION = "+dbKeyAction+" ;";
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
    public static CopexReturn getInitialProcFromDB(DataBaseCommunication dbC, Locale locale, long dbKeyMission , long dbKeyUser, ArrayList<Long> listIdInitProc,  ArrayList<PhysicalQuantity> listPhysicalQuantity,ArrayList v){
        ArrayList<InitialProcedure> listInitProc = new ArrayList();
        // chargement du protocole initial
        String list = "("+listIdInitProc.get(0);
        int nbI = listIdInitProc.size();
        for (int i=1; i<nbI; i++){
            list += ","+listIdInitProc.get(i);
        }
        list += ")";
        String queryInit = "SELECT E.ID_PROC, E.PROC_NAME, E.DATE_LAST_MODIFICATION, E.ACTIV, E.PROC_RIGHT, I.IS_FREE_ACTION,I.IS_TASK_REPEAT, I.CODE_PROC " +
           " FROM EXPERIMENTAL_PROCEDURE E, LINK_MISSION_PROC L, INITIAL_PROC I " +
                   "WHERE L.ID_MISSION = "+dbKeyMission+" AND L.ID_USER = "+dbKeyUser+ " AND "+
                   "L.ID_PROC IN "+list+" AND "+
                   "L.ID_PROC  = E.ID_PROC AND L.ID_PROC = I.ID_PROC  " +
                   " ORDER BY I.CODE_PROC  ;";

        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("E.ID_PROC");
        listFields.add("E.PROC_NAME");
        listFields.add("E.DATE_LAST_MODIFICATION");
        listFields.add("E.ACTIV");
        listFields.add("E.PROC_RIGHT");
        listFields.add("I.IS_FREE_ACTION");
        listFields.add("I.IS_TASK_REPEAT");
        listFields.add("I.CODE_PROC");

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
            System.out.println("proc initial : "+dbKey);
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
            // material
            ArrayList v3 = new ArrayList();
            cr = getInitialProcMaterialFromDB(dbC, dbKey, listPhysicalQuantity, v3 );
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
            // initial proc
            InitialProcedure initProc = new InitialProcedure(dbKey, name, dateLastModif, isActiv, r, code, isFreeAction,isTaskRepeat,  listAction);
            
            initProc.setListMaterial(listMaterial);
                    
            v3 = new ArrayList();
            Profiler.start("xml_loadQuestionInitial");
            cr = getQuestionProcFromDB_xml(dbC, dbKey, v3);
            if (cr.isError())
                return cr;
            Question question = (Question)v3.get(0);
            initProc.setQuestion(question);
            Profiler.end("xml_loadQuestionInitial");
            // recuperation de l'arbre des taches
            Profiler.start("xml_loadTasksInitial");
            v3 = new ArrayList();
            cr = TaskFromDB.getAllTaskFromDB_xml(dbC, dbKey, question.getDbKey(), listAction, listMaterial, listPhysicalQuantity, v3);
            if (cr.isError())
                return cr;
            ArrayList<CopexTask> listTask = (ArrayList<CopexTask>)v3.get(0);
            initProc.setListTask(listTask);
            Profiler.end("xml_loadTasksInitial");
            // met le lien du fils à la question
            int ts = listTask.size();
            for (int k=0; k<ts; k++){
                if (listTask.get(k).getDbKey() == question.getDbKey()){
                    question.setDbKeyChild(listTask.get(k).getDbKeyChild());
                }
            }
            
            listInitProc.add(initProc);
        }
        v.add(listInitProc);
        return new CopexReturn();

    }
    /* chargement d'un protocoles lié à une mission
     * MBO le 27/02/09 : proc lockes
     en v[0] : le proc, null so locke et que l'on doit faire le controle
     */
    public static CopexReturn getProcMissionFromDB_xml(DataBaseCommunication dbC, Locker locker, boolean controlLock, Locale locale,  long dbKeyMission, long dbKeyUser, long dbKeyProc,  long dbKeyInitProc, ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList v) {
        System.out.println("getProcMissionFromDB_xml");
        LearnerProcedure proc  = null;
        ArrayList v2 = new ArrayList();
        System.out.println("*** chargement proc initial");
        ArrayList<Long> listIdInitProc = new ArrayList();
        listIdInitProc.add(dbKeyInitProc);
        CopexReturn cr = getInitialProcFromDB(dbC, locale, dbKeyMission, dbKeyUser, listIdInitProc, listPhysicalQuantity, v2);
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
        System.out.println("chargement proc "+query);
        cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError())
            return cr;
        int nbR = v2.size();
        System.out.println("nbR : "+nbR);
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
            System.out.println("chargement du proc, doit on controler ?");
            if (controlLock && locker.isLocked(dbKeyProc)){
                System.out.println("proc locked : "+dbKeyProc);
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

                proc = new LearnerProcedure(dbKeyProc, name, dateLastModif, isActiv, r, initProcCorresp);
                // recuperation de la question
                v3 = new ArrayList();
                cr = getQuestionProcFromDB_xml(dbC, dbKeyProc, v3);
                if (cr.isError())
                    return cr;
                Question question = (Question)v3.get(0);
                proc.setQuestion(question);
                // recuperation de l'arbre des taches
                v3 = new ArrayList();
                cr = TaskFromDB.getAllTaskFromDB_xml(dbC, dbKeyProc, question.getDbKey(), initProcCorresp.getListNamedAction(), listMaterial, listPhysicalQuantity, v3);
                if (cr.isError())
                    return cr;
                ArrayList<CopexTask> listTask = (ArrayList<CopexTask>)v3.get(0);
                proc.setListTask(listTask);
                // met le lien du fils à la question
                int ts = listTask.size();
                for (int k=0; k<ts; k++){
                    if (listTask.get(k).getDbKey() == question.getDbKey()){
                        question.setDbKeyChild(listTask.get(k).getDbKeyChild());
                    }
                }
                // materiel utilise pour proc
                v3 = new ArrayList();
                cr = getMaterialUseForProcFromDB_xml(dbC, dbKeyProc, listMaterial, v3);
                if (cr.isError())
                    return cr;
                ArrayList<MaterialUseForProc> listMaterialUse = (ArrayList<MaterialUseForProc>)v3.get(0);
                proc.setListMaterialUse(listMaterialUse);
            }
        }

        v.add(proc);
        return new CopexReturn();
    }

    /* chargement du materiel associé à un proc initial :
     * - en v[0] : la liste du materiel
     * - en v[1] : la liste du type de materiel
     */
    public static CopexReturn getInitialProcMaterialFromDB(DataBaseCommunication dbC, long dbKey,  ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList v){
        // recuperation des types de materiel
        ArrayList v2 = new ArrayList();
       CopexReturn cr = getAllTypeMaterialFromDB_xml(dbC, v2);
       if (cr.isError())
           return cr;
       ArrayList<TypeMaterial> listTypeMat = (ArrayList<TypeMaterial>)v2.get(0);
       ArrayList<Material> listM = new ArrayList();

        String query = "SELECT M.ID_MATERIAL, M.MATERIAL_NAME, M.DESCRIPTION, M2.ADVISE FROM " +
               "MATERIAL M, LINK_INIT_PROC_MATERIAL M2 WHERE " +
               "M2.ID_PROC = "+dbKey+" AND M2.ID_MATERIAL = M.ID_MATERIAL ORDER BY M.MATERIAL_NAME  ;" ;

        ArrayList v3 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("M.ID_MATERIAL");
        listFields.add("M.MATERIAL_NAME");
        listFields.add("M.DESCRIPTION");
        listFields.add("M2.ADVISE");

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
            s = rs.getColumnData("M2.ADVISE");
            if (s == null)
                continue;
            int advise = 0;
            try{
                advise = Integer.parseInt(s);
            }catch(NumberFormatException e){

            }
            Material m = new Material(idMat, matName, description);
            m.setAdvisedLearner(advise == 1);
            // on recupere les parametres
            ArrayList v4 = new ArrayList();
            cr = getMaterialParametersFromDB_xml(dbC, idMat, listPhysicalQuantity, v4);
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
            // ajoute à la liste
            listM.add(m);
        }

        v.add(listM);
        v.add(listTypeMat);
        return new CopexReturn();
    }

     /* charge tous les types de materiel */
    public static CopexReturn getAllTypeMaterialFromDB_xml(DataBaseCommunication dbC, ArrayList v){
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
            TypeMaterial t = new TypeMaterial(dbKey, type);
            listT.add(t);
        }
        v.add(listT);
        return new CopexReturn();
    }

    /* retourne la liste des parametres pour un materiel */
    public static CopexReturn getMaterialParametersFromDB_xml(DataBaseCommunication dbC, long idMat,ArrayList<PhysicalQuantity> listPhysicalQuantity,  ArrayList v){
        ArrayList<Parameter> listP = new ArrayList();
        int nbPhysQ = listPhysicalQuantity.size();
        String query = "SELECT ID_QUANTITY, QUANTITY_NAME, TYPE, VALUE, UNCERTAINTY, UNIT " +
                " FROM QUANTITY WHERE ID_QUANTITY IN " +
                "(SELECT ID_PARAMETER FROM LINK_MATERIAL_PARAMETERS WHERE ID_MATERIAL =  "+idMat+ ") ;";

        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("ID_QUANTITY");
        listFields.add("QUANTITY_NAME");
        listFields.add("TYPE");
        listFields.add("VALUE");
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
            String type = rs.getColumnData("TYPE");
            if (type == null)
                continue;
            s = rs.getColumnData("VALUE");
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
            Parameter p = new Parameter(dbKey, name, type, value, uncertainty, unit);
            listP.add(p);
        }
        v.add(listP);
        return new CopexReturn();
    }


    /* controle s'il existe des learner proc pour une mission et un eleve, si oui renoit egalement les id des proc initiaux */
    public static CopexReturn controlLearnerProcInDB(DataBaseCommunication dbC, long dbKeyMission, long dbKeyUser, ArrayList v){
        boolean learnerProc = false;
        ArrayList<Long> listIdInitProc = new ArrayList();
        String query = "SELECT P.PROC_NAME, I.ID_INITIAL_PROC " +
                "FROM EXPERIMENTAL_PROCEDURE P, LEARNER_PROC L, LINK_LEARNER_INITIAL_PROC I, LINK_MISSION_PROC M "+
                "WHERE M.ID_MISSION = "+dbKeyMission+" AND M.ID_USER = "+dbKeyUser+" AND "+
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

    /* controle s'il existe plusieurs proc initiaux pour cette mission et cet utilisateur  */
    public static CopexReturn controlInitialProcInDB(DataBaseCommunication dbC, long dbKeyMission, long dbKeyUser, ArrayList v){
        ArrayList<Long> listIdInitProc = new ArrayList();
        String query = "SELECT I.ID_PROC FROM INITIAL_PROC I, LINK_MISSION_PROC M "+
                "WHERE M.ID_MISSION = "+dbKeyMission+" AND M.ID_USER = "+dbKeyUser+" AND "+
                "M.ID_PROC = I.ID_PROC ;";
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

    /* chargement proc initial simplifiée : uniquement le code et le nom*/
    public static CopexReturn getSimpleInitialProcFromDB(DataBaseCommunication dbC,  long dbKeyMission , long dbKeyUser, ArrayList<Long> listIdInitProc,  ArrayList v){
        ArrayList<InitialProcedure> listInitProc = new ArrayList();
        // chargement du protocole initial
        String list = "("+listIdInitProc.get(0);
        int nbI = listIdInitProc.size();
        for (int i=1; i<nbI; i++){
            list += ","+listIdInitProc.get(i);
        }
        list += ")";
        String queryInit = "SELECT E.ID_PROC, E.PROC_NAME, E.DATE_LAST_MODIFICATION, E.ACTIV, E.PROC_RIGHT, I.IS_FREE_ACTION, I.IS_TASK_REPEAT, I.CODE_PROC " +
           " FROM EXPERIMENTAL_PROCEDURE E, LINK_MISSION_PROC L, INITIAL_PROC I " +
                   "WHERE L.ID_MISSION = "+dbKeyMission+" AND L.ID_USER = "+dbKeyUser+ " AND "+
                   "L.ID_PROC IN "+list+" AND "+
                   "L.ID_PROC  = E.ID_PROC AND L.ID_PROC = I.ID_PROC  " +
                   " ORDER BY I.CODE_PROC  ;";

        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("E.ID_PROC");
        listFields.add("E.PROC_NAME");
        listFields.add("E.DATE_LAST_MODIFICATION");
        listFields.add("E.ACTIV");
        listFields.add("E.PROC_RIGHT");
        listFields.add("I.IS_FREE_ACTION");
        listFields.add("I.IS_TASK_REPEAT");
        listFields.add("I.CODE_PROC");

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
            System.out.println("proc initial : "+dbKey);
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

            // initial proc
            InitialProcedure initProc = new InitialProcedure(dbKey, name, dateLastModif, isActiv, r, code, isFreeAction, isTaskRepeat, null);


            listInitProc.add(initProc);
        }
        v.add(listInitProc);
        return new CopexReturn();

    }
}
