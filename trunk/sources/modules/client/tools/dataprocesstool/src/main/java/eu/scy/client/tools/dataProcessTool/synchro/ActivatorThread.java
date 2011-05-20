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
 * this thread checks periodically the database
 * @author Marjolaine
 */
public class ActivatorThread  extends Thread {
    /* fitex */
    private  DataProcessToolPanel fitex;
    /*connection db */
    private DataBaseCommunication dbC;
    /** locker. */
    private Locker locker = null;

    public ActivatorThread(DataProcessToolPanel fitex, DataBaseCommunication dbC, Locker l) {
        super("ActivatorThread");
        this.fitex = fitex;
        this.dbC = dbC;
        this.locker = l;
    }

    /**
     * run  thread to reactivate lockers, inifinite loop
     */

    @Override
    public void run() {
        // Inifinite loop for checking database, with wait time
        while (true) {
            // wait...
            try {
                sleep(1000 * Locker.LOCKER_DELAY);
            } catch (InterruptedException e) {
                // System.out.println("ActivatorThread interrompu !!");
            }
            // For each elements of l.lockers, search the record
            // doesn't exist: ERROR locker has blown
            // exists: replace lock date with the current date
            int nbLock = this.locker.getLockers().size();
            if (nbLock == 0) {
                continue;
            }
            // open connection
            for (int i = 0; i < nbLock; i++) {
                long lockedLabdoc = (Long) this.locker.getLockers().get(i);
                String query = "UPDATE LABDOC_STATUS SET LOCK_DATE=NOW() WHERE ID_LABDOC=" + lockedLabdoc +" AND LABDOC_STATUS = '"+DataConstants.LABDOC_STATUS_LOCK+"' ;";

                ArrayList v = new ArrayList();
                String[] querys = new String[1];
                querys[0] = query ;
                CopexReturn cr = dbC.executeQuery(querys, v);
                if (cr.isError()){
                    fitex.displayError(cr, fitex.getBundleString("TITLE_ERROR_DIALOG"));
                }
            }
        }
    }
}
