/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.elofactory;

/**
 * Registry for creating new ELOs.
 *
 * @author sikkenj
 */
public interface NewEloCreationRegistry {

   /**
    * Register an eloType and its name to display, when asking the user.
    * 
    * @param eloType
    * @param displayName
    */
   public void registerEloCreation(String eloType, String displayName);

   /**
    * Return the eloType belonging to the typeName
    *
    * @param typeName
    * @return
    */
   public String getEloType(String typeName);

   /**
    * Return the eloType name belonging to the type
    *
    * @param type
    * @return
    */
   public String getEloTypeName(String type);

   /**
    * Return all registered ELO type names
    * 
    * @return
    */
   public String[] getEloTypeNames();
}
