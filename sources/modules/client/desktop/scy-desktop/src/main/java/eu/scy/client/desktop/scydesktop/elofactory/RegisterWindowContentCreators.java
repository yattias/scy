/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.elofactory;

/**
 *
 * @author sikkenj
 */
public interface RegisterWindowContentCreators
{

   public void registerWindowContentCreators(WindowContentCreatorRegistry windowContentCreatorRegistry);

   public void registerNewEloCreation(NewEloCreationRegistry newEloCreationRegistry);
}
