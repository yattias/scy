/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools;

import roolo.elo.api.IELO;

/**
 * Call back interface for reporting the results of elo saving
 * @author sikken
 */
public interface EloSaverCallBack {

   /**
    * Reports that the elo has been saved.
    *
    * @param elo - the saved elo
    */
   public void eloSaved(IELO elo);

   /**
    * Reports that the elo save has been cancelled.
    *
    * @param elo - the passed on elo
    */
   public void eloSaveCancelled(IELO elo);
}
