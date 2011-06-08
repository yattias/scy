/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.db;

import eu.scy.client.tools.copex.common.*;
import eu.scy.client.tools.copex.synchro.Locker;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.CopexUtilities;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Class that manages the methods to access to database
 * @author marjolaine
 */
public class AccesDB {
    private String idUser;
    private DataBaseCommunication dbC ;

    public AccesDB() {
        
    }
     
    
    public AccesDB(URL copexURL, long idMission, String idUser){
       this.idUser = idUser;
        dbC = new DataBaseCommunication(copexURL, MyConstants.DB_LABBOOK_COPEX, idMission, idUser);
    }



    public void setIdUser(String idUser) {
        this.idUser = idUser;
        dbC.setIdUser(idUser);
    }
    
    
    
    public DataBaseCommunication getDbC(){
        return this.dbC;
    }

    /**
     * replace the string toReplace in the string inText with the string newTextToReplace.
     * @return java.lang.String
     * @param toReplace java.lang.String
     * @param inText java.lang.String
     */
    public static String replace(String toReplace, String inText, String newTextToReplace) {
	if (inText == null ) 
            return null; 
	String newText="";
	int j=inText.indexOf(toReplace);
	if (j!=-1){
		int i = 0;
		while(i<inText.length()){
			newText += inText.substring(i,j)+newTextToReplace;
			int l=inText.indexOf(toReplace,j+1);
			if (l==-1) {
                            if (j+1 < inText.length())
				newText+=inText.substring(j+1,inText.length());
                            break;
			}
			else {
                            i=j+1;
                            j=l;
			}
		}
	}
	else 
            newText=inText;
	return newText;
    }


   
    

    public CopexReturn getMissionFromDB(long dbKeyMission, ArrayList v) {
        return MissionFromDB.getMissionFromDB(this.dbC, dbKeyMission,  v);
    }

    public CopexReturn getGroupFromDB(long dbKeyGroup ,ArrayList v) {
        return UserFromDB.getGroupFromDB(this.dbC, dbKeyGroup, v);
    }
    
   
    public CopexReturn getProcMissionFromDB(Locker locker, boolean controlLock, long dbKeyMission, long dbKeyUser,long dbKeyProc, Locale locale, long dbKeyInitProc,  ArrayList<PhysicalQuantity> listPhysicalQuantity, ArrayList<MaterialStrategy> listMaterialStrategy,  ArrayList v) {
        return ExperimentalProcedureFromDB.getProcMissionFromDB(this.dbC, locker, controlLock, locale, dbKeyMission , dbKeyUser, dbKeyProc,  dbKeyInitProc, listPhysicalQuantity, listMaterialStrategy,  v);
    }
    
    /* add a task, returns in v[0] the new id */
    public CopexReturn addTaskBrotherInDB(Locale locale,CopexTask task, long idProc, CopexTask brotherTask, ArrayList v){
        CopexReturn cr = TaskFromDB.addTaskBrotherInDB(this.dbC,locale, task, idProc, brotherTask, v);
        return cr;
    }

    /* add a task, returns in v[0] the new id  */
    public CopexReturn addTaskParentInDB(Locale locale,CopexTask task, long idProc, CopexTask parentTask, ArrayList v){
        return TaskFromDB.addTaskParentInDB(this.dbC,locale, task, idProc, parentTask, v);
    }
    
    
    
    /*update a task*/
    public CopexReturn updateTaskInDB(Locale locale,CopexTask newTask, long idProc, CopexTask oldTask, ArrayList v){
        return TaskFromDB.updateTaskInDB(this.dbC,locale, newTask, idProc, oldTask, v);
       
    }
    
    /* delete tasks */
    public CopexReturn deleteTasksFromDB(long dbKeyProc, ArrayList<CopexTask> listTask){
        return TaskFromDB.deleteTasksFromDB(this.dbC, true, dbKeyProc, listTask);
        
    }
    
     /* update links */
    public CopexReturn updateLinksInDB(long dbKeyProc, ArrayList<CopexTask> listTaskUpdateBrother, ArrayList<CopexTask> listTaskUpdateChild){
       return TaskFromDB.updateLinksInDB(this.dbC, dbKeyProc, listTaskUpdateBrother, listTaskUpdateChild);
        
    }
    
      /* remove a procedure */
    public CopexReturn removeProcInDB(LearnerProcedure proc){
            CopexReturn cr;
            // remove tasks of the proc
            cr = TaskFromDB.deleteTasksFromDB(this.dbC, false,  proc.getDbKey(), proc.getListTask());
            if (cr.isError()){
                return cr;
            }
            // sremove hypothesis, general principle and evaluation
            cr = ExperimentalProcedureFromDB.deleteHypothesisFromDB(dbC, proc);
            if (cr.isError()){
                return cr;
            }
            cr = ExperimentalProcedureFromDB.deleteGeneralPrincipleFromDB(dbC, proc);
            if (cr.isError()){
                return cr;
            }
            cr = ExperimentalProcedureFromDB.deleteEvaluationFromDB(dbC, proc);
            if (cr.isError()){
                return cr;
            }
            // remove material user
            cr = ExperimentalProcedureFromDB.deleteMaterialUsedFromDB(dbC, proc.getDbKey(), proc.getListMaterialUsed());
            if (cr.isError()){
                return cr;
            }
            // remove proc
            cr = ExperimentalProcedureFromDB.deleteProcedureFromDB(this.dbC, proc.getDbKey());
            if (cr.isError()){
                return cr;
            }
        return new CopexReturn();
    }
    
    /* returns all mission of a user, expect tthe mission with the specified id; with the proc */
    public CopexReturn getAllMissionsFromDB(Locale locale,long dbKeyUser, long dbKeyMission, ArrayList v){
            ArrayList v2 = new ArrayList();
           CopexReturn cr = MissionFromDB.getAllMissionsFromDB(this.dbC, dbKeyUser, dbKeyMission, v2);
           if (cr.isError()){
               return cr;
           }
           ArrayList<CopexMission> listMission = (ArrayList<CopexMission>)v2.get(0);
           // gets proc
           ArrayList<ArrayList<LearnerProcedure>> listPM = new ArrayList();
           int nbM = listMission.size();
           for (int m=0; m<nbM; m++){
               v2 = new ArrayList();
              cr = ExperimentalProcedureFromDB.getShortProcMissionFromDB(dbC, locale,listMission.get(m).getDbKey(), dbKeyUser,   v2);
               if (cr.isError()){
                    return cr;
               }
               ArrayList<LearnerProcedure> listP = (ArrayList<LearnerProcedure>)v2.get(0);
               listPM.add(listP);
           }
           v.add(listMission);
           v.add(listPM);
           return new CopexReturn();
    }
    
    /* update the proc name */
    public CopexReturn updateProcName(long dbKeyProc, String name){
        return  ExperimentalProcedureFromDB.updateProcNameInDB(dbC, dbKeyProc, name);
       
    }
    
    /* update the modification date of a proc and mission  */
    public CopexReturn updateDateProc(LearnerProcedure proc){
        java.sql.Date date = CopexUtilities.getCurrentDate();
        
        CopexReturn cr = ExperimentalProcedureFromDB.updateDateProcInDB(dbC, proc.getDbKey(), date);
        if (cr.isError()){
            return cr;
        }
        return new CopexReturn();
    }
    
    
    
    /* update the proc activ in a mission */
    public CopexReturn updateProcActiv(ArrayList<LearnerProcedure> listProc){
       CopexReturn cr ;
        int nbP = listProc.size();
        for (int i=0; i<nbP; i++){
            cr = ExperimentalProcedureFromDB.updateActivProcInDB(dbC, listProc.get(i).getDbKey(), listProc.get(i).isActiv());
            if (cr.isError()){
                return cr;
            }
        }
        return new CopexReturn();
    }
}
