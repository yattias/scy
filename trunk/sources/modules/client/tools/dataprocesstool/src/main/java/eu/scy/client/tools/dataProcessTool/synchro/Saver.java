/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.dataProcessTool.synchro;

import eu.scy.client.tools.dataProcessTool.db.DataBaseCommunication;
import eu.scy.client.tools.fitex.main.DataProcessToolPanel;

/**
 *
 * @author Marjolaine
 */
public class Saver {
    /* thread */
    private SaveThread thread;

    /* fitex */
    private  DataProcessToolPanel fitex;
    /*connection db */
    private DataBaseCommunication dbC;
    private long dbKeyLabdoc;

    /**
    * Constructor saver: essentially: run the thread
    */
    public Saver(DataProcessToolPanel fitex, DataBaseCommunication dbC, long dbKeyLabdoc) {
        this.fitex = fitex;
        this.dbC = dbC;
        this.dbKeyLabdoc = dbKeyLabdoc;
        // thread creaetion
        thread = new SaveThread(fitex, dbC, dbKeyLabdoc) ;
        thread.start();

    }
}
