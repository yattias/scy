/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package roolo.renumber


/**
 *
 * @author sikken
 */
interface EloStoreDAO {
	EloStore readEloStore(File dir);
   void writeEloStore(EloStore eloStore, File dir);
}

