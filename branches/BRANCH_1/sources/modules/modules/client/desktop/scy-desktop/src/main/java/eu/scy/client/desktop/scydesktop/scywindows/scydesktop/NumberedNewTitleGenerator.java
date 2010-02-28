/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.scywindows.NewTitleGenerator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sikken
 */
public class NumberedNewTitleGenerator implements NewTitleGenerator{
   
   private final String untitledName = "new";

   private Map<String,Integer> typeCounters = new HashMap<String,Integer>();
   private NewEloCreationRegistry newEloCreationRegistry;

   public NumberedNewTitleGenerator(NewEloCreationRegistry newEloCreationRegistry)
   {
      this.newEloCreationRegistry = newEloCreationRegistry;
   }

   public void setNewEloCreationRegistry(NewEloCreationRegistry newEloCreationRegistry)
   {
      this.newEloCreationRegistry = newEloCreationRegistry;
   }

   @Override
   public String generateNewTitleFromType(String eloType)
   {
      return generateNewTitleFromName(newEloCreationRegistry.getEloTypeName(eloType));
   }

   @Override
   public String generateNewTitleFromName(String nameBase)
   {
      Integer counter = typeCounters.get(nameBase);
      if (counter==null){
         counter = 1;
      }
      else{
         counter = counter+1;
      }
      typeCounters.put(nameBase, counter);
      return untitledName + " " + nameBase + " " + counter;
   }

}
