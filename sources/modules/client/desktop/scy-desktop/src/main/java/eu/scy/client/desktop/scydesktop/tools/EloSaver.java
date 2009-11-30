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

   public IELO saveElo(IELO elo, boolean myElo);

   public IELO updateElo(IELO elo, boolean myElo);
}
