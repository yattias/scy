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
 * This thread controls periodically the database (for lockers)
 * @author Marjolaine
 */
public class ActivatorThread extends Thread{
    /* owner */
    private CopexPanel copex;
    /* database connection */
    private DataBaseCommunication dbC;
    /** locker  */
    private Locker locker = null;

    public ActivatorThread(CopexPanel copex, DataBaseCommunication dbC, Locker l) {
        super("ActivatorThread");
        this.copex = copex;
        this.dbC = dbC;
        this.locker = l;
    }

    /**
     *Thread run to re activate the lockers, infinite loop
     */

    @Override
    public void run() {
        // Infinite loop to check in database
        while (true) {
            // Time delay
            try {
                sleep(1000 * Locker.LOCKER_DELAY);
            } catch (InterruptedException e) {
                //System.out.println("ActivatorThread interrompu !!"+e);
//                if(edP.isAppletVisible())
//                    edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_THREAD_INTERRUPT")+ e, false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            }
            // For each element from l.lockers, search the element
            // If doesn't exist: warning error,
            // If exists: replace the locked date with the current date 
            int nbLock = this.locker.getLockers().size();
            if (nbLock == 0) {
                continue;
            }
            // open connection 
            for (int i = 0; i < nbLock; i++) {
                //long lockedProc = (Long) this.locker.getLockers().get(i);
                long lockedLabdoc = (Long) this.locker.getLockers().get(i);
                //String query = "UPDATE VERROU SET DAT_VER=NOW() WHERE ID_PROC=" + lockedProc +" ;";
                String query = "UPDATE LABDOC_STATUS SET LOCK_DATE=NOW() WHERE ID_LABDOC=" + lockedLabdoc +" AND LABDOC_STATUS = '"+MyConstants.LABDOC_STATUS_LOCK+"' ;";
                ArrayList v = new ArrayList();
                String[] querys = new String[1];
                querys[0] = query ;
                CopexReturn cr = dbC.executeQuery(querys, v);
                if (cr.isError()){
                    copex.displayError(cr, copex.getBundleString("TITLE_ERROR_DIALOG"));
                }
            }
        }
    }
}
