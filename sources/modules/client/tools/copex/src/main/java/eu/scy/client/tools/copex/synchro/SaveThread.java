/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.synchro;

import eu.scy.client.tools.copex.db.DataBaseCommunication;
import eu.scy.client.tools.copex.db.LabBookFromDB;
import eu.scy.client.tools.copex.main.CopexPanel;
import eu.scy.client.tools.copex.utilities.CopexReturn;

/**
 *
 * @author Marjolaine
 */
public class SaveThread  extends Thread{
    public static final int SAVE_DELAY = 180;

    /* fitex */
    private  CopexPanel copex;
    /*connection db */
    private DataBaseCommunication dbC;
    private long dbKeyLabdoc;

    public SaveThread(CopexPanel copex, DataBaseCommunication dbC, long dbKeyLabdoc) {
        super("SaveThread");
        this.copex = copex;
        this.dbC = dbC;
        this.dbKeyLabdoc = dbKeyLabdoc;
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
                sleep(1000 * SAVE_DELAY);
            } catch (InterruptedException e) {
                // System.out.println("SaveThread interrompu !!" +e);

            }
            CopexReturn cr= LabBookFromDB.setExperimentalProcedureInDB(dbC, dbKeyLabdoc, copex.getXProc(), copex.getPreview());
        }
    }
}
