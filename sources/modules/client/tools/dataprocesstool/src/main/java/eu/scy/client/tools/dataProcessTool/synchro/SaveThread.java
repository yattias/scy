/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.synchro;

import eu.scy.client.tools.dataProcessTool.db.DataBaseCommunication;
import eu.scy.client.tools.dataProcessTool.db.LabBookFromDB;
import eu.scy.client.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.client.tools.fitex.main.DataProcessToolPanel;

/**
 *
 * @author Marjolaine
 */
public class SaveThread  extends Thread{
    public static final int SAVE_DELAY = 180;

    /* fitex */
    private  DataProcessToolPanel fitex;
    /*connection db */
    private DataBaseCommunication dbC;
    private long dbKeyLabdoc;

    public SaveThread(DataProcessToolPanel fitex, DataBaseCommunication dbC, long dbKeyLabdoc) {
        super("SaveThread");
        this.fitex = fitex;
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
            CopexReturn cr= LabBookFromDB.setDatasetInDB(dbC, dbKeyLabdoc, fitex.getPDS(), fitex.getPreview());
        }
    }
}
