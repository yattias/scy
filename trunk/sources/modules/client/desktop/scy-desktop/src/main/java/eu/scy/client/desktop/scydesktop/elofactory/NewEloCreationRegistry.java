/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.elofactory;

/**
 *
 * @author sikkenj
 */
public interface NewEloCreationRegistry {

   public void registerEloCreation(String eloType, String typeName);

   public String getEloType(String typeName);

   public String[] getEloTypeNames();
}
