/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.copex.db;

import eu.scy.tools.copex.common.*;
import eu.scy.tools.copex.edp.CopexApplet;
import eu.scy.tools.copex.synchro.Locker;
import eu.scy.tools.copex.utilities.CopexReturn;
import eu.scy.tools.copex.utilities.CopexUtilities;
import eu.scy.tools.copex.utilities.MyConstants;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Classe gérant la connection avec la base de données 
 * @author MBO
 */
public class AccesDB {
    // CONSTANTES
    // MODE D'ACCES A LA BASE
    public static boolean BD_XML = true;
    // ATTRIBUTS
    /* edP */
    private CopexApplet edP;
    private String idUser;
    /* utilisateur */
    private String user;
    private String password;
    private String url;
    /* connexion */
    private  Connection con = null;
    /* boolean indiquant si on est connecte */
    private  boolean isConnected = false;
    private DataBaseCommunication dbC ;

    public AccesDB() {
        
    }
     
    
    // CONSTRUCTEUR
    public AccesDB(CopexApplet edP, long idMission, String idUser){
        this.edP = edP;
       this.idUser = idUser;
        //System.out.println("connection adresse IP applet signée v10");
        dbC = new DataBaseCommunication(edP, MyConstants.DB_COPEX_EDP, idMission, idUser);
    }
    public AccesDB(URL copexURL, long idMission, String idUser){
       this.idUser = idUser;
        //System.out.println("connection adresse IP applet signée v10");
        dbC = new DataBaseCommunication(copexURL, MyConstants.DB_COPEX_EDP, idMission, idUser);
    }



    public void setIdUser(String idUser) {
        this.idUser = idUser;
        dbC.setIdUser(idUser);
    }
    
    
    // METHODES
    /* renvoit la connection */
    public Connection getConnection(){
        return this.con;
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


   
    

    public CopexReturn getDataSheetFromDB(ArrayList<LearnerProcedure> listProc, ArrayList v) {
        return DataSheetFromDB.getAllDataSheetFromDB_xml(this.dbC, listProc , v);
    }

   

    public CopexReturn getMissionFromDB(long dbKeyMission, long dbKeyUser ,ArrayList v) {
        return MissionFromDB.getMissionFromDB_xml(this.dbC, dbKeyMission, dbKeyUser, v);
    }

    public CopexReturn getUserFromDB(long dbKeyUser ,ArrayList v) {
        return UserFromDB.getUserFromDB_xml(this.dbC, dbKeyUser, v);
    }
    
   
    public CopexReturn getProcMissionFromDB(Locker locker, boolean controlLock, long dbKeyMission, long dbKeyUser,long dbKeyProc, Locale locale, long dbKeyInitProc,  ArrayList<PhysicalQuantity> listPhysicalQuantity,  ArrayList v) {
        return ExperimentalProcedureFromDB.getProcMissionFromDB_xml(this.dbC, locker, controlLock, locale, dbKeyMission , dbKeyUser, dbKeyProc,  dbKeyInitProc, listPhysicalQuantity,  v);
    }
    
    /* ajout d'une tache, retourne en v2[0] le nouvel id */
    public CopexReturn addTaskBrotherInDB(CopexTask task, long idProc, CopexTask brotherTask, ArrayList v){
     
        CopexReturn cr = TaskFromDB.addTaskBrotherInDB_xml(this.dbC, task, idProc, brotherTask, v);
        return cr;
    }
    /* ajout d'une tache, retourne en v2[0] le nouvel id */
    public CopexReturn addTaskParentInDB(CopexTask task, long idProc, CopexTask parentTask, ArrayList v){
        return TaskFromDB.addTaskParentInDB_xml(this.dbC, task, idProc, parentTask, v);
       
    }
    
    
    
    /* modification d'une tache*/
    public CopexReturn updateTaskInDB(CopexTask newTask, long idProc, CopexTask oldTask, ArrayList v){
        return TaskFromDB.updateTaskInDB_xml(this.dbC, newTask, idProc, oldTask, v);
       
    }
    
    /* suppression de taches */
    public CopexReturn deleteTasksFromDB(long dbKeyProc, ArrayList<CopexTask> listTask){
        return TaskFromDB.deleteTasksFromDB_xml(this.dbC, true, dbKeyProc, listTask);
        
    }
    
     /* mise à jour des liens */
    public CopexReturn updateLinksInDB(long dbKeyProc, ArrayList<CopexTask> listTaskUpdateBrother, ArrayList<CopexTask> listTaskUpdateChild){
       return TaskFromDB.updateLinksInDB_xml(this.dbC, dbKeyProc, listTaskUpdateBrother, listTaskUpdateChild);
        
    }
    
      /* suppression d'un protocole */
    public CopexReturn removeProcInDB(ExperimentalProcedure proc, long dbKeyUser){
        
            CopexReturn cr;
            String msg = "";
            // supprime la feuille de données liée au protocole 
            DataSheet dataSheet = proc.getDataSheet();
            if (dataSheet != null){
                cr = DataSheetFromDB.deleteDataSheetFromDB_xml(this.dbC, dataSheet.getDbKey());
                if (cr.isError()){
                    return cr;
                }
            }
            // supprime les taches liees au protocole 
            cr = TaskFromDB.deleteTasksFromDB_xml(this.dbC, false,  proc.getDbKey(), proc.getListTask());
            
            if (cr.isError()){
                return cr;
            }
            // suppression du protocole 
            cr = ExperimentalProcedureFromDB.deleteProcedureFromDB_xml(this.dbC, proc.getDbKey(), dbKeyUser);
            if (cr.isError()){
                return cr;
            }
           
        return new CopexReturn();
    }
    
    /* retourne les missions de l'utilisateur sauf celle avec cet id ainsi que les protocoles associés */
    public CopexReturn getAllMissionsFromDB(long dbKeyUser, long dbKeyMission, ArrayList v){
            ArrayList v2 = new ArrayList();
           CopexReturn cr = MissionFromDB.getAllMissionsFromDB_xml(this.dbC, dbKeyUser, dbKeyMission, v2);
           if (cr.isError()){
               return cr;
           }
           ArrayList<CopexMission> listMission = (ArrayList<CopexMission>)v2.get(0);
           // recuperation des protocoles
           ArrayList<ArrayList<LearnerProcedure>> listPM = new ArrayList();
           int nbM = listMission.size();
           for (int m=0; m<nbM; m++){
               v2 = new ArrayList();
              cr = ExperimentalProcedureFromDB.getShortProcMissionFromDB_xml(dbC, listMission.get(m).getDbKey(), dbKeyUser,   v2);
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
    
    /* mise à jour du nom du protocole */
    public CopexReturn updateProcName(long dbKeyProc, String name){
        return  ExperimentalProcedureFromDB.updateProcNameInDB_xml(dbC, dbKeyProc, name);
       
    }
    
    /* mise à jour de la date de modif d'un protocole et de sa mission associée */
    public CopexReturn updateDateProc(ExperimentalProcedure proc){
        java.sql.Date date = CopexUtilities.getCurrentDate();
        
        CopexReturn cr = MissionFromDB.updateDateMissionInDB_xml(dbC, proc.getMission().getDbKey(), date);
        if (cr.isError()){
            return cr;
        }
        cr = ExperimentalProcedureFromDB.updateDateProcInDB_xml(dbC, proc.getDbKey(), date);
        if (cr.isError()){
            return cr;
        }
        return new CopexReturn();
    }
    
    /* mise à jour de la date de modif d'une mission */
    public CopexReturn updateDateMission(long dbKeyMission){
        java.sql.Date date = CopexUtilities.getCurrentDate();
        CopexReturn cr = MissionFromDB.updateDateMissionInDB_xml(dbC, dbKeyMission, date);
        return cr;
    }
    
    
    /* mise à jour du protocole actif d'une mission */
    public CopexReturn updateProcActiv(ArrayList<LearnerProcedure> listProc){
       CopexReturn cr ;
        int nbP = listProc.size();
        for (int i=0; i<nbP; i++){
            cr = ExperimentalProcedureFromDB.updateActivProcInDB_xml(dbC, listProc.get(i).getDbKey(), listProc.get(i).isActiv());
            if (cr.isError()){
                return cr;
            }
        }
        return new CopexReturn();
    }

    /* retourne la date et l'heure*/
    public CopexReturn getDateAndTime(ArrayList v){
        return TraceFromDB.getDateAndTime(this.dbC, v);
    }
}
