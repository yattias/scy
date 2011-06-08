/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.synchro;

import eu.scy.client.tools.copex.db.DataBaseCommunication;
import eu.scy.client.tools.copex.main.CopexPanel;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;

/**
 * This class implements the management of lockers for COPEX
 * It's based on the class ALBDOC_STATUS, that contains a field ID_LABDOC and LOCKED_DATE
 * THe principle is as following (for an editor):
 * (1) Checking of the existence of a locker valid (younger thant LOCKER_VALIDITY)
 * (2) Set a such of locker
 * (3) As long as we don't want to unlocker the proc, we have to update the locker with a periodicity LOCKER_DELAY
 * @author Marjolaine
 */
public class Locker {
    /** validity delay (in seconds ) */
    public static final int LOCKER_VALIDITY = 300;
    /** replacing locker delay (in seconds). */
    public static final int LOCKER_DELAY = 150;


    /* proc editor */
    private  CopexPanel copex;
    /* database connection */
    private  DataBaseCommunication dbC;
    /** application user */
    private  long idUser = -1;
    /** This list contains the lockers to update in this editor */
    private  ArrayList lockers = new ArrayList();
    /* thread */
    private ActivatorThread thread;

    /**
    * Constructorlocker. Run the thread in batch, which updates periodically the lockersof the list .
    */
    public Locker(CopexPanel copex, DataBaseCommunication dbC, long idUser) {
        this.copex = copex;
        this.dbC = dbC;
        // save user
        this.idUser = idUser;
        this.lockers = new ArrayList();
        // Thread creation
        thread = new ActivatorThread(copex, dbC, this) ;
        thread.start();

    }

    public  ArrayList getLockers() {
        return lockers;
    }

    public  void setLockers(ArrayList lockers) {
        this.lockers = lockers;
    }

   /* set the lcokers on a list of labdocs  */
    public  CopexReturn setLabdocLockers(ArrayList<Long> labdocs) {
        int nbP = labdocs.size();
        ArrayList v = new ArrayList();
        String[] querys = new String[nbP];
        for (int i=0; i<nbP; i++){
            querys[i] = "INSERT INTO LABDOC_STATUS (ID_LABDOC,ID_LB_USER,LABDOC_STATUS, LOCK_DATE) " +
                "VALUES( "+labdocs.get(i)+" ," + this.idUser + ",'"+MyConstants.LABDOC_STATUS_LOCK+"', NOW()) ;";
        }
        CopexReturn cr = dbC.executeQuery(querys, v);
        if (cr.isError()){
            copex.displayError(cr, copex.getBundleString("TITLE_DIALOG_ERROR"));
        }
        for (int i=0; i<nbP; i++){
            lockers.add(labdocs.get(i));
        }
        return new CopexReturn();
    }

    /* set the locker on a labdoc */
    public  CopexReturn setLabdocLocker(long idLabdoc){
        String query = "INSERT INTO LABDOC_STATUS (ID_LABDOC,ID_LB_USER,LABDOC_STATUS, LOCK_DATE) " +
                "VALUES( "+idLabdoc+" ," + this.idUser + ",'"+MyConstants.LABDOC_STATUS_LOCK+"', NOW()) ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query ;
        CopexReturn cr = dbC.executeQuery(querys, v);
        if (cr.isError()){
            copex.displayError(cr, copex.getBundleString("TITLE_DIALOG_ERROR"));
        }
        this.lockers.add(idLabdoc);
        return new CopexReturn();
    }

    /* delete a locker for a labdoc  */
    public  CopexReturn unsetLabdocLocker(long idLabdoc){
        String query = "DELETE FROM LABDOC_STATUS WHERE ID_LABDOC = "+idLabdoc+" AND LABDOC_STATUS = '"+MyConstants.LABDOC_STATUS_LOCK+"';";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query;
        CopexReturn cr = dbC.executeQuery(querys, v);
        if (cr.isError()){
            copex.displayError(cr, copex.getBundleString("TITLE_DIALOG_ERROR"));
        }
        this.lockers.remove(idLabdoc);
        return new CopexReturn();
    }

    /* delete the lockers of a list of labdocs  */
    public  CopexReturn unsetLabdocLockers(ArrayList<Long> labdocs){
        int nbP = labdocs.size();
        ArrayList v = new ArrayList();
        String[] querys = new String[nbP];
        for (int i=0; i<nbP; i++){
            querys[i] = "DELETE FROM LABDOC_STATUS WHERE ID_LABDOC = "+labdocs.get(i)+" AND LABDOC_STATUS = '"+MyConstants.LABDOC_STATUS_LOCK+"' ;";
        }
        CopexReturn cr = dbC.executeQuery(querys, v);
        if (cr.isError()){
            copex.displayError(cr, copex.getBundleString("TITLE_DIALOG_ERROR"));
        }
        for (int i=0; i<nbP; i++){
            this.lockers.remove(labdocs.get(i));
        }
        return new CopexReturn();
    }

    /* returns true if the specified labdoc is locked */
    public  boolean isLocked(long idLabdoc){
        // remove old lockers
        CopexReturn cr = deleteOldLockers();
        if (cr.isError()){
            copex.displayError(cr, copex.getBundleString("TITLE_DIALOG_ERROR"));
        }
        String query = "SELECT * FROM LABDOC_STATUS WHERE ID_LABDOC = "+idLabdoc+" AND LABDOC_STATUS = '"+MyConstants.LABDOC_STATUS_LOCK+"';";
        ArrayList v2 = new ArrayList();
        ArrayList<String> listFields = new ArrayList();
        listFields.add("DAT_VER");

        cr = dbC.sendQuery(query, listFields, v2);
        if (cr.isError()){
            copex.displayError(cr, copex.getBundleString("TITLE_DIALOG_ERROR"));
            return false;
        }
        int nbR = v2.size();
        return nbR > 0;
    }

    /* remove old lockers */
    private  CopexReturn deleteOldLockers(){
        String query = "DELETE FROM LABDOC_STATUS WHERE (NOW() - DAT_VER)  > " + LOCKER_VALIDITY+" AND LABDOC_STATUS = '"+MyConstants.LABDOC_STATUS_LOCK+"';";
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
