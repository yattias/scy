/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.synchro;

import eu.scy.client.tools.copex.db.DataBaseCommunication;
import eu.scy.client.tools.copex.main.CopexPanel;

/**
 *
 * @author Marjolaine
 */
public class Saver {
    /* thread */
    private SaveThread thread;

    /* fitex */
    private  CopexPanel copex;
    /*connection db */
    private DataBaseCommunication dbC;
    private long dbKeyLabdoc;

    /**
    * Constructor saver: essentially: run the thread
    */
    public Saver(CopexPanel copex, DataBaseCommunication dbC, long dbKeyLabdoc) {
        this.copex = copex;
        this.dbC = dbC;
        this.dbKeyLabdoc = dbKeyLabdoc;
        // thread creaetion
        thread = new SaveThread(copex, dbC, dbKeyLabdoc) ;
        thread.start();

    }
}
