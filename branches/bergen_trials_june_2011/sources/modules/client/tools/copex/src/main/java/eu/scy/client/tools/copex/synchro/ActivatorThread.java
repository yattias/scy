/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.copex.synchro;

import eu.scy.client.tools.copex.db.DataBaseCommunication;
import eu.scy.client.tools.copex.edp.CopexPanel;
import eu.scy.client.tools.copex.utilities.CopexReturn;
import eu.scy.client.tools.copex.utilities.MyConstants;
import java.util.ArrayList;

/**
 * Ce thread controle periodiquement la base de donnees.
 * @author Marjolaine
 */
public class ActivatorThread extends Thread{
    /* editeur proc */
    private CopexPanel copex;
    /*connection db */
    private DataBaseCommunication dbC;
    /** Le locker pour garder le contact dans ce thread avec le reste de l'execution. */
    private Locker locker = null;

    // CONSTRUCTOR
    public ActivatorThread(CopexPanel copex, DataBaseCommunication dbC, Locker l) {
        super("ActivatorThread");
        this.copex = copex;
        this.dbC = dbC;
        this.locker = l;
    }

    /**
     * Lancement du thread de reactivation des verrous, et boucle infinie.
     */

    @Override
    public void run() {
        // Boucle infinie de contrale de la base de donnees, avec temps d'attente.
        while (true) {
            // Temps d'attente
            try {
                sleep(1000 * Locker.LOCKER_DELAY);
            } catch (InterruptedException e) {
                //System.out.println("ActivatorThread interrompu !!"+e);
//                if(edP.isAppletVisible())
//                    edP.displayError(new CopexReturn(edP.getBundleString("MSG_ERROR_THREAD_INTERRUPT")+ e, false), edP.getBundleString("TITLE_DIALOG_ERROR"));
            }
            // Pour chaque elements de l.lockers, rechercher l'enregistrement correspondant.
            // S'il n'existe pas: ATTENTION ERREUR: le verrou a saute (comment signaler l'erreur)
            // S'il existe, remplacer sa date de verrouillage par la date courante.
            int nbLock = this.locker.getLockers().size();
            if (nbLock == 0) {
                continue;
            }
            // Ouverture d'une connection
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
