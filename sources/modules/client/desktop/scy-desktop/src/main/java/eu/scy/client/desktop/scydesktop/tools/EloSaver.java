/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools;

import roolo.elo.api.IELO;

/**
 *
 * @author sikken
 */
public interface EloSaver {

   public IELO eloSaveAs(IELO elo);

   public IELO eloUpdate(IELO elo);

   public IELO otherEloSaveAs(IELO elo);
}
