/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools;

import roolo.elo.api.IELO;

/**
 * This interface makes it easier for the tools to save elos. The tool just has to pass the elo and the rest will be done, including asking the user for additional information, such as a title.
 *
 * It will make it possible that scy-desktop will use a nice looking "save as" dialog, which then will be used every where.
 * @author sikken
 */
public interface EloSaver {

   /**
    * does a save as of the elo in the tool
    *
    * The elo will be saved as a fork (IRepository.addForkedELO), when it is an existing elo. Otherwise it will just be saved as a new elo (IRepository.addNewELO).
    *
    * The saved elo information will be updated in the scy-dekstop administation. It is assumed that the tool will display the saved elo.
    *
    * @param elo
    * @param eloSaverCallBack - if not null, the saved elo is returned or save cancelled is reported
    */
   public void eloSaveAs(IELO elo, EloSaverCallBack eloSaverCallBack);

   /**
    * does an save or update of the elo in the tool.
    *
    * If the elo has not been saved at all, it will ask the user for the title and as a new elo (IRepository.addNewELO). If the elo is saved before, it will just update the elo (IRepository.updateELO)
    *
    * The saved elo information will be updated in the scy-dekstop administation.
    *
    * @param elo
    * @param eloSaverCallBack - if not null, the saved elo is returned or save cancelled is reported
    */
   public void eloUpdate(IELO elo, EloSaverCallBack eloSaverCallBack);

   /**
    * does a save as of an elo, which is not the THE elo in the tool.
    *
    * The elo will be saved as a fork (IRepository.addForkedELO), when it is an existing elo. Otherwise it will just be saved as a new elo (IRepository.addNewELO).
    *
    * @param elo
    * @param eloSaverCallBack - if not null, the saved elo is returned or save cancelled is reported
    */
   public void otherEloSaveAs(IELO elo, EloSaverCallBack eloSaverCallBack);
}
