/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.elofactory.impl;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author sikkenj
 */
public class NewEloCreationRegistryImpl implements NewEloCreationRegistry
{

   private final static Logger logger = Logger.getLogger(NewEloCreationRegistryImpl.class);
   private HashMap<String, String> nameTypeMap = new HashMap<String, String>();
   private HashMap<String, String> typeNameMap = new HashMap<String, String>();
   private List<String> typeNames = new ArrayList<String>();
   private final ResourceBundleWrapper resourceBundleWrapper = new ResourceBundleWrapper(this);

   @Override
   public void registerEloCreation(String eloType)
   {
      logger.info("registering new elo creation for type " + eloType);
      if (!nameTypeMap.containsValue(eloType))
      {
         String i18nDisplayName = resourceBundleWrapper.getString("technicalFormat."+eloType);
         if (!nameTypeMap.containsValue(i18nDisplayName))
         {
            nameTypeMap.put(i18nDisplayName,eloType);
            typeNameMap.put(eloType,i18nDisplayName);
            typeNames.add(i18nDisplayName);
         }
         else
         {
            throw new IllegalArgumentException("displayName is allready used: " + i18nDisplayName);
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
      return nameTypeMap.get(typeName);
   }

   @Override
   public String getEloTypeName(String type)
   {
      String name = typeNameMap.get(type);
      if (name!=null){
         return name;
      }
      return "Unknown type: " + type;
   }

   @Override
   public String[] getEloTypeNames()
   {
      return typeNames.toArray(new String[0]);
   }

   @Override
   public boolean containsEloType(String eloType)
   {
      return typeNameMap.containsKey(eloType);
   }

    @Override
    public Set<String> getEloTypes() {
        return typeNameMap.keySet();
    }
}
