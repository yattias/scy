/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.elofactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author sikkenj
 */
public class NewEloCreationRegistryImpl implements NewEloCreationRegistry
{

   private final static Logger logger = Logger.getLogger(NewEloCreationRegistryImpl.class);
   private HashMap<String, String> registry = new HashMap<String, String>();
   private List<String> typeNames = new ArrayList<String>();

   @Override
   public void registerEloCreation(String eloType, String typeName)
   {
      logger.info("registering new elo creation for type " + eloType + " with name " + typeName);
      if (!registry.containsKey(eloType))
      {
         if (!registry.containsValue(typeName))
         {
            registry.put(typeName,eloType);
            typeNames.add(typeName);
         }
         else
         {
            throw new IllegalArgumentException("typeName is allready used: " + typeName);
         }
      }
      else
      {
         throw new IllegalArgumentException("eloType is allready used: " + eloType);
      }
   }

   @Override
   public String getEloType(String typeName)
   {
      return registry.get(typeName);
   }

   @Override
   public String[] getEloTypeNames()
   {
      return typeNames.toArray(new String[0]);
   }
}
