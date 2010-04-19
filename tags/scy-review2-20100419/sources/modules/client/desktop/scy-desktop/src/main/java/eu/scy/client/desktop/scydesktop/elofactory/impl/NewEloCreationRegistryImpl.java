/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.elofactory.impl;

import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
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
   public void registerEloCreation(String eloType, String displayName)
   {
      logger.info("registering new elo creation for type " + eloType + " with name " + displayName);
      if (!registry.containsKey(eloType))
      {
         if (!registry.containsValue(displayName))
         {
            registry.put(displayName,eloType);
            typeNames.add(displayName);
         }
         else
         {
            throw new IllegalArgumentException("displayName is allready used: " + displayName);
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
   public String getEloTypeName(String type)
   {
      for (Entry<String, String> entry : registry.entrySet()){
         if (entry.getValue().equals(type))
         {
            return entry.getKey();
         }
      }
      return "Unknown type: " + type;
   }

   @Override
   public String[] getEloTypeNames()
   {
      return typeNames.toArray(new String[0]);
   }
}
