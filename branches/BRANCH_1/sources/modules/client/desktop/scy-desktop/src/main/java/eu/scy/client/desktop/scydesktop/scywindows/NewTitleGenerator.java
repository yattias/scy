/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.scywindows;

/**
 *
 * @author sikken
 */
public interface NewTitleGenerator {

   public String generateNewTitleFromType(String eloType);

   public String generateNewTitleFromName(String nameBase);
   
}
