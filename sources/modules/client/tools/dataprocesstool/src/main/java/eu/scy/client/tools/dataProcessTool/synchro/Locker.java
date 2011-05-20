/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.synchro;

import eu.scy.client.tools.fitex.main.DataProcessToolPanel;
import eu.scy.client.tools.dataProcessTool.db.DataBaseCommunication;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.dataProcessTool.utilities.DataConstants;
import java.util.ArrayList;

/**
 * This class implements the lockers for fitex
 * It's base on the table LABDOC_STATUS which contains the fields ID_LABDOC for the locked labdocee, and  LOCK_DATE for the locked date.
 * The principle is as follows:  the tool that want to block a dataset does:
 * (1) Check the absence of valid lock (younger than LOCKER_VALIDITY) ;
 * (2) set a locker record
 * (3) As we don't want unlock the dataset, we have to record, with a periodicity   LOCKER_DELAY the locker, in order it's still valid
 * @author Marjolaine
 */
public class Locker {
    /** The period of validity of a lock (in seconds) */
    public static final int LOCKER_VALIDITY = 300;
    /**The delay in replacing the lock (in seconds)*/
    public static final int LOCKER_DELAY = 150;


    /* fitex */
    private  DataProcessToolPanel fitex;
    /*base connection */
    private  DataBaseCommunication dbC;
    /** user */
    private  long idUser = -1;
    /** list of lockers to update in this application. */
    private  ArrayList lockers = new ArrayList();
    /* thread */
    private ActivatorThread thread;

    /**
    * Constructor locker: essentially: run the thread, which periodically reactivate the lockers.
    */
    public Locker(DataProcessToolPanel fitex, DataBaseCommunication dbC, long idUser) {
        this.fitex = fitex;
        this.dbC = dbC;
        // save user
        this.idUser = idUser;
        this.lockers = new ArrayList();
        // thread creaetion
        thread = new ActivatorThread(fitex, dbC, this) ;
        thread.start();

    }

    public  ArrayList getLockers() {
        return lockers;
    }

    public  void setLockers(ArrayList lockers) {
        this.lockers = lockers;
    }

   /* set lockers on list dataset */
    public  CopexReturn setLabdocLockers(ArrayList<Long> labdocs) {
        int nbDs = labdocs.size();
        ArrayList v = new ArrayList();
        String[] querys = new String[nbDs];
        for (int i=0; i<nbDs; i++){
            querys[i] = "INSERT INTO LABDOC_STATUS (ID_LABDOC,ID_LB_USER,LABDOC_STATUS, LOCK_DATE) " +
                "VALUES( "+labdocs.get(i)+" ," + this.idUser + ",'"+DataConstants.LABDOC_STATUS_LOCK+"', NOW()) ;";
        }
        CopexReturn cr = dbC.executeQuery(querys, v);
        if (cr.isError()){
            fitex.displayError(cr, fitex.getBundleString("TITLE_DIALOG_ERROR"));
        }
        for (int i=0; i<nbDs; i++){
            lockers.add(labdocs.get(i));
        }
        return new CopexReturn();
    }

    /* set locker on a dataset*/
    public  CopexReturn setLabdocLocker(long idLabdoc){
        String query = "INSERT INTO LABDOC_STATUS (ID_LABDOC,ID_LB_USER,LABDOC_STATUS, LOCK_DATE) " +
                "VALUES( "+idLabdoc+" ," + this.idUser + ",'"+DataConstants.LABDOC_STATUS_LOCK+"', NOW()) ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        if (cr.isError()){
            fitex.displayError(cr, fitex.getBundleString("TITLE_DIALOG_ERROR"));
        }
        this.lockers.add(idLabdoc);
        return new CopexReturn();
    }

    /* remove the locker of a dataset */
    public  CopexReturn unsetLabdocLocker(long idLabdoc){
        String query = "DELETE FROM LABDOC_STATUS WHERE ID_LABDOC = "+idLabdoc+" AND LABDOC_STATUS = '"+DataConstants.LABDOC_STATUS_LOCK+"';";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query;
        CopexReturn cr = dbC.executeQuery(querys, v);
        if (cr.isError()){
            fitex.displayError(cr, fitex.getBundleString("TITLE_DIALOG_ERROR"));
        }
        this.lockers.remove(idLabdoc);
        return new CopexReturn();
    }

    /*remove the lockers of a list of dataset*/
    public  CopexReturn unsetLabdocLockers(ArrayList<Long> labdocs){
        int nbDs = labdocs.size();
        ArrayList v = new ArrayList();
        String[] querys = new String[nbDs];
        for (int i=0; i<nbDs; i++){
            querys[i] = "DELETE FROM LABDOC_STATUS WHERE ID_LABDOC = "+labdocs.get(i)+" AND LABDOC_STATUS = '"+DataConstants.LABDOC_STATUS_LOCK+"';";
        }
        CopexReturn cr = dbC.executeQuery(querys, v);
        if (cr.isError()){
            fitex.displayError(cr, fitex.getBundleString("TITLE_DIALOG_ERROR"));
        }
        for (int i=0; i<nbDs; i++){
            this.lockers.remove(labdocs.get(i));
        }
        return new CopexReturn();
    }

    /* returns true if the dataset is locked */
    public  boolean isLocked(long idLabdoc){
        // remove old lockers
        CopexReturn cr = deleteOldLockers();
        if (cr.isError()){
            fitex.displayError(cr, fitex.getBundleString("TITLE_DIALOG_ERROR"));
        }
        String query = "SELECT * FROM LABDOC_STATUS WHERE ID_LABDOC = "+idLabdoc+" AND LABDOC_STATUS = '"+DataConstants.LABDOC_STATUS_LOCK+"';";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("DAT_VER");

        cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            fitex.displayError(cr, fitex.getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        int nbR = v2.size();
        return nbR > 0;
    }

    /* remove old lockers */
    private  CopexReturn deleteOldLockers(){
        String query = "DELETE FROM LABDOC_STATUS WHERE (NOW() - DAT_VER)  > " + LOCKER_VALIDITY +" AND LABDOC_STATUS = "+DataConstants.LABDOC_STATUS_LOCK+" ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* stop the thread */
    public void stop(){
        this.thread.interrupt();
    }
}
