/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import java.util.Set;

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
   public void registerEloCreation(String eloType);

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

    /**
    * Return all technical formats for registered ELO types
    *
    * @return all registered ELO types, not the display names
    */
   public Set<String> getEloTypes();

   /**
    * Returns true if the eloType is registered
    * 
    * @param eloType
    * @return
    */
   public boolean containsEloType(String eloType);
}
