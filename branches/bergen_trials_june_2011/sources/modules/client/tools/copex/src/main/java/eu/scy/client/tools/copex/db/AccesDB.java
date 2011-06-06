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
 * Classe gerant la connection avec la base de donnees 
 * @author MBO
 */
public class AccesDB {
    private String idUser;
    private DataBaseCommunication dbC ;

    public AccesDB() {
        
    }
     
    
    public AccesDB(URL copexURL, long idMission, String idUser){
       this.idUser = idUser;
        //System.out.println("connection adresse IP applet signee v10");
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
     * On replace la chaine toReplace dans la chaine inText par la chaine newTextToReplace.
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
    
    /* ajout d'une tache, retourne en v2[0] le nouvel id */
    public CopexReturn addTaskBrotherInDB(Locale locale,CopexTask task, long idProc, CopexTask brotherTask, ArrayList v){
     
        CopexReturn cr = TaskFromDB.addTaskBrotherInDB(this.dbC,locale, task, idProc, brotherTask, v);
        return cr;
    }
    /* ajout d'une tache, retourne en v2[0] le nouvel id */
    public CopexReturn addTaskParentInDB(Locale locale,CopexTask task, long idProc, CopexTask parentTask, ArrayList v){
        return TaskFromDB.addTaskParentInDB(this.dbC,locale, task, idProc, parentTask, v);
       
    }
    
    
    
    /* modification d'une tache*/
    public CopexReturn updateTaskInDB(Locale locale,CopexTask newTask, long idProc, CopexTask oldTask, ArrayList v){
        return TaskFromDB.updateTaskInDB(this.dbC,locale, newTask, idProc, oldTask, v);
       
    }
    
    /* suppression de taches */
    public CopexReturn deleteTasksFromDB(long dbKeyProc, ArrayList<CopexTask> listTask){
        return TaskFromDB.deleteTasksFromDB(this.dbC, true, dbKeyProc, listTask);
        
    }
    
     /* mise a jour des liens */
    public CopexReturn updateLinksInDB(long dbKeyProc, ArrayList<CopexTask> listTaskUpdateBrother, ArrayList<CopexTask> listTaskUpdateChild){
       return TaskFromDB.updateLinksInDB(this.dbC, dbKeyProc, listTaskUpdateBrother, listTaskUpdateChild);
        
    }
    
      /* suppression d'un protocole */
    public CopexReturn removeProcInDB(LearnerProcedure proc){
        
            CopexReturn cr;
            
            // supprime les taches liees au protocole 
            cr = TaskFromDB.deleteTasksFromDB(this.dbC, false,  proc.getDbKey(), proc.getListTask());
            
            if (cr.isError()){
                return cr;
            }
            // suppression hypothese, principle et evaluation
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
            // suppression du mat used
            cr = ExperimentalProcedureFromDB.deleteMaterialUsedFromDB(dbC, proc.getDbKey(), proc.getListMaterialUsed());
            if (cr.isError()){
                return cr;
            }
            // suppression du protocole 
            cr = ExperimentalProcedureFromDB.deleteProcedureFromDB(this.dbC, proc.getDbKey());
            if (cr.isError()){
                return cr;
            }
           
        return new CopexReturn();
    }
    
    /* retourne les missions de l'utilisateur sauf celle avec cet id ainsi que les protocoles associes */
    public CopexReturn getAllMissionsFromDB(Locale locale,long dbKeyUser, long dbKeyMission, ArrayList v){
            ArrayList v2 = new ArrayList();
           CopexReturn cr = MissionFromDB.getAllMissionsFromDB(this.dbC, dbKeyUser, dbKeyMission, v2);
           if (cr.isError()){
               return cr;
           }
           ArrayList<CopexMission> listMission = (ArrayList<CopexMission>)v2.get(0);
           // recuperation des protocoles
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
    
    /* mise a jour du nom du protocole */
    public CopexReturn updateProcName(long dbKeyProc, String name){
        return  ExperimentalProcedureFromDB.updateProcNameInDB(dbC, dbKeyProc, name);
       
    }
    
    /* mise a jour de la date de modif d'un protocole et de sa mission associee */
    public CopexReturn updateDateProc(LearnerProcedure proc){
        java.sql.Date date = CopexUtilities.getCurrentDate();
        
        CopexReturn cr = ExperimentalProcedureFromDB.updateDateProcInDB(dbC, proc.getDbKey(), date);
        if (cr.isError()){
            return cr;
        }
        return new CopexReturn();
    }
    
    
    
    /* mise a jour du protocole actif d'une mission */
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
