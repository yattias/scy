/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

import eu.scy.client.desktop.desktoputils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author sikken
 */
public class BasicDisplayNames implements DisplayNames
{

   private final Logger logger = Logger.getLogger(BasicDisplayNames.class);

   private String name = "?";
   private List<TypeNameSet> typeNameSets;
   private Map<String, String> typeNameMap;

   public void initialize()
   {
      typeNameMap = new HashMap<String, String>();
      for (TypeNameSet typeNameSet : typeNameSets)
      {
         if (StringUtils.isEmpty(typeNameSet.getType()))
         {
            throw new IllegalArgumentException("type may not be empty");
         }
         if (StringUtils.isEmpty(typeNameSet.getName()))
         {
            throw new IllegalArgumentException("name may not be empty");
         }
         if (typeNameMap.containsKey(typeNameSet.getType()))
         {
            throw new IllegalArgumentException("duplicate type: " + typeNameSet.getType());
         }
         typeNameMap.put(typeNameSet.getType(), typeNameSet.getName());
      }
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public void setTypeNameSets(List<TypeNameSet> typeNameSets)
   {
      this.typeNameSets = typeNameSets;
   }

   @Override
   public String getDisplayName(String type)
   {
      return typeNameMap.get(type);
   }

   @Override
   public List<String> getDisplayNames(List<String> types)
   {
      List<String> displayNames = new ArrayList<String>();
      for (String type : types)
      {
         String displayName = getDisplayName(type);
         if (displayName != null)
         {
            displayNames.add(displayName);
         }
         else
         {
            logger.warn("asked display name for unknow " + name + " type: " + type);
         }
      }
      return displayNames;
   }

   @Override
   public boolean typeExists(String type)
   {
      return typeNameMap.containsKey(type);
   }
}
