/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.synchro;

import eu.scy.tools.dataProcessTool.dataTool.DataProcessToolPanel;
import eu.scy.tools.dataProcessTool.db.DataBaseCommunication;
import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import eu.scy.tools.dataProcessTool.utilities.DataConstants;
import java.util.ArrayList;

/**
 * Cette classe implemente la gestion des verrous pour fitex
 * Elle repose sur la table VERROU contenant les champs ID_DATASET pour la feuille de donneees verouillee, et DAT_VER pour la date du verouillage.
 * Le principe de fonctionnement est le suivant: L'editeur qui souhaite bloquer une feuille de donnees procede de la maniere suivante:
 * (1) Verification de l'absence de verrou valide (plus jeune que LOCKER_VALIDITY) ;
 * (2) Placement d'un tel enregistrement de verrouillage;
 * (3) Tant que l'on ne souhaite pas deverrouiller la feuille de donnees, il faut reposer avec une periodicite LOCKER_DELAY le verrou pour qu'il reste valide.
 * @author Marjolaine
 */
public class Locker {
     // CONSTANTES
    /** Le delai de validite d'un verrou (en secondes). */
    public static final int LOCKER_VALIDITY = 600;
    /** Le delai de replacement d'un verrou (en secondes). */
    public static final int LOCKER_DELAY = 300;


    /* fitex */
    private  DataProcessToolPanel fitex;
    /* connection base */
    private  DataBaseCommunication dbC;
    /** Le user de l'application. */
    private  long idUser = -1;
    /** Ce vecteur contient la liste des verrous a remettre a jour dans cette application. */
    private  ArrayList lockers = new ArrayList();
    /* thread */
    private ActivatorThread thread;

    /**
    * Constructeur du locker. Il s'agit essentiellement de lancer le thread en batch qui reactive periodiquement les verrous decrits dans le vector lockers.
    */
    public Locker(DataProcessToolPanel fitex, DataBaseCommunication dbC, long idUser) {
        this.fitex = fitex;
        this.dbC = dbC;
        // Sauvegarde du user
        this.idUser = idUser;
        this.lockers = new ArrayList();
        // Creation du Thread de reactivation des verrous.
        thread = new ActivatorThread(fitex, dbC, this) ;
        thread.start();

    }

    public  ArrayList getLockers() {
        return lockers;
    }

    public  void setLockers(ArrayList lockers) {
        this.lockers = lockers;
    }

   /* pose de verrous sur une liste de feuilles de donnees */
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

    /* pose un verrou sur une feuille de donnees*/
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

    /* supprime le verrou d'une feuille de donnees */
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

    /* supprime le verrou d'une liste de feuilles de donnees*/
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

    /* retourne vrai si la feuille de donnees est verrouille */
    public  boolean isLocked(long idLabdoc){
        // suppression des anciens verrous
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

    /* suppression d'anciens verrous */
    private  CopexReturn deleteOldLockers(){
        String query = "DELETE FROM LABDOC_STATUS WHERE (NOW() - DAT_VER)  > " + LOCKER_VALIDITY +" AND LABDOC_STATUS = "+DataConstants.LABDOC_STATUS_LOCK+" ;";
        ArrayList v = new ArrayList();
        String[] querys = new String[1];
        querys[0] = query;
        CopexReturn cr = dbC.executeQuery(querys, v);
        return cr;
    }

    /* arret du thread */
    public void stop(){
        this.thread.interrupt();
    }
}
