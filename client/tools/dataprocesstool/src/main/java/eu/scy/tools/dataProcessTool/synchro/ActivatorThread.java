/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.synchro;

import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel;
import eu.scy.tools.dataProcessTool.db.DataBaseCommunication;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import java.util.ArrayList;

/**
 * Ce thread contrale periodiquement la base de donnees.
 * @author Marjolaine
 */
public class ActivatorThread  extends Thread {
    /* fitex */
    private  DataProcessToolPanel fitex;
    /*connection db */
    private DataBaseCommunication dbC;
    /** Le locker pour garder le contact dans ce thread avec le reste de l'execution. */
    private Locker locker = null;

    // CONSTRUCTOR
    public ActivatorThread(DataProcessToolPanel fitex, DataBaseCommunication dbC, Locker l) {
        super("ActivatorThread");
        this.fitex = fitex;
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
                System.out.println("ActivatorThread interrompu !!");
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
                long lockedDs = (Long) this.locker.getLockers().get(i);
                String query = "UPDATE VERROU SET DAT_VER=NOW() WHERE ID_DATASET=" + lockedDs +" ;";

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
